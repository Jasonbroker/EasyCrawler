package Crawler; /**
 * Created by jason on 18/01/2017.
 */

import Helper.MetaDataHelper;
import Helper.UserConfigManager;
import UI.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTabPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.io.File.separator;

public class Main extends Application implements SpiderListener {

    static Spider spider;
    static Spider domainSpider;

    StackPane motherPane;
    TabPane tabPane;
    SearchEmailBox searchEmailBox;
    SearchDomainBox searchDomainBox;
    Settings settings;
    private UserConfigManager userConfigManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 创建UI
        createUI(primaryStage);

        userConfigManager = new UserConfigManager();

        this.bindData();

        searchEmailBox.addCLickLisener(new SearchEmailBoxListener() {
            @Override
            public boolean buttonClicked(boolean on) {
                try {
                    if (on) {
                        return runP();
                    } else  {
                        return stopP();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                return true;
            }

            public void saveButtonPressedWithText(String string) {
                saveData();
            }
        });

        searchDomainBox.addCLickLisener(new SearchDomainBoxListener() {
            @Override
            public boolean buttonClicked(boolean on) {
                try {
                    if (on) {
                        return runP();
                    } else {
                        return stopP();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
        });

        settings.addCLickLisener(() -> {
            saveData();
            showAlertWithMessage("SETTING", "Settings saved!");
        });

    }

    private boolean stopP () {
        Spider currentSpider = getFunctionType()?domainSpider:spider;
        currentSpider.stop();
        return false;
    }

    private  boolean runP () throws IOException, InterruptedException {

        System.out.println(this.getFunctionType());
        String outputDirectory = settings.getFolder();
        if (this.getFunctionType() == true) {
            String url = searchDomainBox.getUrl();
            if (url.isEmpty()) {
                showAlertWithMessage("URL ERROR", "Please enter the right web url, which is prefixed with http:// ");
                return false;
            }
            if (!url.startsWith("http")) {
                this.showAlertWithMessage("URL ERROR", "Please enter the right web url, which is prefixed with http:// ");
                return false;
            }
            ///////////////////////////////////////////////
            ArrayList keywords = MetaDataHelper.getKeywords(searchDomainBox.getKeywords());
            if (keywords.isEmpty()) {
                showAlertWithMessage("ERROR", "Please enter the right web url, which is prefixed with http://");
                return false;
            }

            KeywordFindingPageProcessor pageProcessor = new KeywordFindingPageProcessor(keywords);
            domainSpider = Spider.create(pageProcessor);


            domainSpider.addUrl(url);
            domainSpider.setExitWhenComplete(true);

            String outPutPath = outputDirectory + separator + "web_" + domainSpider.getSite().getDomain() + ".csv";
            System.out.println(outPutPath);

            FilePersistentBase pa = new FilePersistentBase();
            pa.setPath(outPutPath);
            File file = pa.getFile(outPutPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            domainSpider.addPipeline(new KeywordPipline(file.getAbsolutePath()));

            domainSpider.thread(10);

            LinkedList list = new LinkedList<>();
            list.add(this);
            domainSpider.setSpiderListeners(list);
            domainSpider.runAsync();

        } else {

            int threadNum = searchEmailBox.getThreadNum();
            String url = searchEmailBox.getUrl();

            if (url.isEmpty()) {
                this.showAlertWithMessage("URL ERROR", "Please enter the right web url, which is prefixed with http://");
                return false;
            }
            if (!url.startsWith("http")) {
                this.showAlertWithMessage("URL ERROR", "Please enter the right web url, which is prefixed with http://");
                return false;
            }

            if (threadNum > 10000) {
                threadNum = 40;
            } else if (threadNum < 1) {
                threadNum = 1;
            }

            int workingMode = searchEmailBox.getWorkingMode();
            int startIndex = searchEmailBox.getStartIndex();
            int endIndex = searchEmailBox.getEndIndex();
            boolean strictMode = searchEmailBox.enableStrictMode();
            String ats = searchEmailBox.getSeperator();
            System.out.println("custom seperator:"+ats);
            //////////////////////////////////////////////////////////////////////////////////////

            EmailPageProcessor pageProcessor = new EmailPageProcessor(false, searchEmailBox.getBackSpaceHead(), searchEmailBox.getBackSpaceTail(), searchEmailBox.isEnableDebug());
            spider = Spider.create(pageProcessor);

            ArrayList urls = new ArrayList();
            if (workingMode == 0) {
                spider.addUrl(url);
            }else  {

                if (!url.contains("$")) {
                    System.out.println("输入错误!\n请输入要查询的地址,请去除其中的有序索引数字, 使用$代替:");
                    showAlertWithMessage("WRONG INPUT", "PLEASE USE $ TO INSTEAD THE NUMERIC.");
                    return false;
                }

                for (int i = startIndex; i <= endIndex; i++) {
                    String replacedUrl = url.replace("$", "" + i);
                    urls.add(replacedUrl);
                    spider.addUrl(replacedUrl);
                }

                pageProcessor.maxDepth = 0;
            }
            spider.thread(threadNum);

            String outPutPath = outputDirectory + separator + spider.getSite().getDomain() + ".csv";

            System.out.println("saving place: \n" + outPutPath);

            FilePersistentBase pa = new FilePersistentBase();
            pa.setPath(outPutPath);
            File file = pa.getFile(outPutPath);
            if (!file.exists()) {
                file.createNewFile();
            }

            PrintWriter printWriter;
            printWriter = new PrintWriter(new FileWriter(file.getAbsolutePath(), true));
            printWriter.print("URL" +"," + "E-mail\n");
            printWriter.close();

            pageProcessor.strict = strictMode;
            spider.addPipeline(new PendingPipline(file.getAbsolutePath(), strictMode));

            if (ats.length()>0) {
                String[] str = ats.split(",");
                pageProcessor.operators = str;
                System.out.println("读取配置文件" + str[0]);
            }else {
                System.out.println("配置文件未读取!!!!!!!!!!...");
            }

            spider.setExitWhenComplete(true);
            LinkedList list = new LinkedList<>();
            list.add(this);
            spider.setSpiderListeners(list);
            spider.runAsync();

        }


        return true;
    }

    @Override
    public void onSuccess(Request request) {
        Spider currentSpider = getFunctionType()?domainSpider:spider;
        int thread = currentSpider.getThreadAlive();
        if (thread == 0) {
            this.showAlertWithMessage("Done", "抓取成功,请在桌面查看");
            this.startScrawling(false);
        }
    }

    @Override
    public void onError(Request request) {

    }

    private void showAlertWithMessage(String title, String message) {

        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setPrefWidth(motherPane.getWidth() * 0.8);
        layout.setPadding(new Insets(10, 10., 10, 10));

        Label heading = new Label(title);
        heading.setFont(new Font(15));
        layout.setHeading(heading);
        Label messLabel = new Label(message);
        messLabel.setWrapText(true);
        layout.setBody(messLabel);
        JFXButton accept = new JFXButton(" OK ");
        layout.setActions(accept);

        JFXDialog dialog = new JFXDialog(motherPane, layout, JFXDialog.DialogTransition.CENTER);

        accept.setOnMouseClicked((e) -> {
            dialog.close();
        });

        dialog.show();
    }



    void startScrawling (boolean start) {
        if (getFunctionType()) {
            searchDomainBox.setWorking(start);
        } else  {
            searchEmailBox.setWorking(start);
        }
    }


    public boolean getFunctionType() {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        return selectionModel.isSelected(1);
    }

    private void createUI(Stage primaryStage) {

        searchEmailBox = getSearchEmailBox();
        Tab searchEmailPan = new Tab(" Email ", searchEmailBox);
        searchEmailPan.setClosable(false);

        searchDomainBox = getSearchDomainBox();
        Tab domainSearchTab = new Tab(" Keywords ", searchDomainBox);
        domainSearchTab.setClosable(false);

        settings = new Settings(20);
        Tab settingTab = new Tab(" Setting ", settings);
        settingTab.setClosable(false);

        tabPane = new JFXTabPane();
        tabPane.setTabMinHeight(44);
        ZCTabPaneSkin skin = new ZCTabPaneSkin(tabPane);
        tabPane.setSkin(skin);

        tabPane.getTabs().addAll(searchEmailPan, domainSearchTab, settingTab);
        StackPane pane = new StackPane(tabPane);
        motherPane = pane;

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        String str = Main.class.getResource("/css/jfoenix-components.css").toExternalForm();
        scene.getStylesheets().add(str);
        primaryStage.setTitle("web crawler".toUpperCase());
        primaryStage.show();
    }

    private void bindData() {
        searchEmailBox.setUrl(userConfigManager.getEmail_url());
        searchEmailBox.setWorkMode(userConfigManager.getWork_mode());
        searchEmailBox.setThreadNum(userConfigManager.getCrawl_speed());
        searchEmailBox.setstartEndIndex(userConfigManager.getIndex_start(), userConfigManager.getIndex_end());
        searchEmailBox.setStrictMode(userConfigManager.isStrict_mode());
        searchEmailBox.setEnableSpaceHead(userConfigManager.isPrefix_space());
        searchEmailBox.setEnableSpaceTrail(userConfigManager.isSuffix_space());
        searchEmailBox.setSeperator(userConfigManager.getReplace_words());
        searchEmailBox.setDebugEnabled(userConfigManager.isDebug_mode());

        settings.setFolder(userConfigManager.getDefault_folder());

    }

    private void saveData() {
        userConfigManager.setEmail_url(searchEmailBox.getUrl());
        userConfigManager.setWork_mode(searchEmailBox.getWorkingMode());
        userConfigManager.setCrawl_speed(searchEmailBox.getThreadNum());

        userConfigManager.setIndex_start(searchEmailBox.getStartIndex());
        userConfigManager.setIndex_end(searchEmailBox.getEndIndex());
        userConfigManager.setStrict_mode(searchEmailBox.enableStrictMode());
        userConfigManager.setPrefix_space(searchEmailBox.getBackSpaceHead());
        userConfigManager.setSuffix_space(searchEmailBox.getBackSpaceTail());
        userConfigManager.setReplace_words(searchEmailBox.getSeperator());
        userConfigManager.setDebug_mode(searchEmailBox.isEnableDebug());

        userConfigManager.setDefault_folder(settings.getFolder());

        userConfigManager.synchronize();
    }

    SearchDomainBox getSearchDomainBox() {
        return new SearchDomainBox(20);
    }

    SearchEmailBox getSearchEmailBox() {
        return new SearchEmailBox(20);
    }
}


package com.company;
/**
 * Created by jason on 15/01/2017.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.utils.FilePersistentBase;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import static java.io.File.separator;

public class ScrawerUI extends Application implements SpiderListener{

    static Spider spider;
    static Spider domainSpider;

    TabPane tabPane;
    SearchEmailBox searchEmailBox;
    SearchDomainBox searchDomainBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 创建UI
        createUI(primaryStage);

        String path = FileHelper.pathInCodeFolder("config.txt");
        System.out.println("loading config file at "+ path);

        String ats = null;
        try {
            ats = FileHelper.readFileByLines(path);
        } catch (IOException e) {
        }
        searchEmailBox.setSeperator(ats);

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
                try {
                    FileHelper.writeToConfigFile(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    }

    private boolean stopP () {
        Spider currentSpider = getFunctionType()?domainSpider:spider;
        currentSpider.stop();
        return false;
    }

    private  boolean runP () throws IOException, InterruptedException {

        System.out.println(this.getFunctionType());
        if (this.getFunctionType() == true) {
            String url = searchDomainBox.getUrl();
            if (url.isEmpty()) {
                showAlertWithMessage("请输入网址");
                return false;
            }
            if (!url.startsWith("http")) {
                this.showAlertWithMessage("网址格式非法");
                return false;
            }
            ///////////////////////////////////////////////
            ArrayList keywords = searchDomainBox.getKeywords();
            if (keywords.isEmpty()) {
                showAlertWithMessage("请输入关键词");
                return false;
            }

            KeywordFindingPageProcessor pageProcessor = new KeywordFindingPageProcessor(keywords);
            domainSpider = Spider.create(pageProcessor);


            domainSpider.addUrl(url);
            domainSpider.setExitWhenComplete(true);

            String outPutPath = FileHelper.desktopPathWithFileName("web_" + domainSpider.getSite().getDomain() + ".csv");
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
                this.showAlertWithMessage("请输入网址");
                return false;
            }
            if (!url.startsWith("http")) {
                this.showAlertWithMessage("网址格式非法");
                return false;
            }

            if (threadNum > 10000) {
                threadNum = 40;
            } else if (threadNum < 1) {
                threadNum = 1;
            }

            boolean workingMode = searchEmailBox.getWorkingMode();
            int startIndex = searchEmailBox.getStartIndex();
            int endIndex = searchEmailBox.getEndIndex();
            boolean strictMode = searchEmailBox.enableStrictMode();
            String ats = searchEmailBox.getSeperator();
            System.out.println("custom seperator:"+ats);
            //////////////////////////////////////////////////////////////////////////////////////

            GithubRepoPageProcessor pageProcessor = new GithubRepoPageProcessor(false, searchEmailBox.getBackSpaceHead(), searchEmailBox.getBackSpaceTail());
            spider = Spider.create(pageProcessor);

            ArrayList urls = new ArrayList();
            if (workingMode) {
                spider.addUrl(url);
            }else  {

                if (!url.contains("$")) {
                    System.out.println("输入错误!\n请输入要查询的地址,请去除其中的有序索引数字, 使用$代替:");
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

            Properties properties = System.getProperties() ;
            String path = properties.getProperty("user.home");

            File com = new File(path);

            String outPutPath = com.getAbsolutePath() + separator +"Desktop"+ separator + spider.getSite().getDomain() + ".csv";

            System.out.println("存储位置:\n" + outPutPath);

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
            this.showAlertWithMessage("抓取成功,请在桌面查看");
            this.startScrawling(false);
        }
    }

    @Override
    public void onError(Request request) {

    }

    private void showAlertWithMessage(String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING, message , ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            //do stuff
        }
    }



    void startScrawling (boolean start) {
        if (getFunctionType()) {
            searchDomainBox.setWorking(start);
        } else  {
            searchEmailBox.setWorking(start);
        }
    }



    public void addSearchDomainCLickLisener(SearchDomainBoxListener listener) {
        searchDomainBox.getStartButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean suc = listener.buttonClicked(!searchDomainBox.isWorking());
                startScrawling(suc);
            }
        });
    }

    public boolean getFunctionType() {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        return selectionModel.isSelected(1);
    }

    private MenuBar getMenu() {
            MenuBar menuBar = new MenuBar();
        menuBar.setPadding(new Insets(0,0,0,0));
            Menu m1 = new Menu();
            m1.setText("文件");
            Menu m2 = new Menu();
            m2.setText("编辑");
            Menu m3 = new Menu();
            m3.setText("帮助");

            MenuItem item11 = new MenuItem();
            item11.setText("打开");
            MenuItem item12 = new MenuItem();
            item12.setText("保存");
            MenuItem item13 = new MenuItem();
            item13.setText("退出");

            MenuItem item21 = new MenuItem();
            item21.setText("复制");
            MenuItem item22 = new MenuItem();
            item22.setText("拷贝");
            MenuItem item23 = new MenuItem();
            item23.setText("剪切");

            MenuItem item31 = new MenuItem();
            item31.setText("欢迎");
            MenuItem item32 = new MenuItem();
            item32.setText("搜索");
            MenuItem item33 = new MenuItem();
            item33.setText("版本信息");
            item33.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showAlertWithMessage("版本信息");
                }
            });

            m1.getItems().addAll(item11,item12,item13);
            m2.getItems().addAll(item21,item22, item23);
            m3.getItems().addAll(item31,item32, item33);
            menuBar.getMenus().addAll(m1,m2,m3);

        return menuBar;
    }

    private void createUI(Stage primaryStage) {

        searchEmailBox = getSearchEmailBox();
        Tab searchEmailPan = new Tab("    邮箱关键词检索器    ", searchEmailBox);
        searchEmailPan.setClosable(false);

        searchDomainBox = getSearchDomainBox();
        Tab domainSearchTab = new Tab("    全站关键词搜索    ", searchDomainBox);
        domainSearchTab.setClosable(false);

        tabPane = new TabPane(searchEmailPan, domainSearchTab);
        BorderPane pane = new BorderPane(tabPane, getMenu(), null, null, null);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("网站信息提取系统");
        primaryStage.show();
    }

    SearchDomainBox getSearchDomainBox() {return new SearchDomainBox(10);}

    SearchEmailBox getSearchEmailBox() {return new SearchEmailBox(10);}
}

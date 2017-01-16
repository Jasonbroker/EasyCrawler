package com.company;/**
 * Created by jason on 15/01/2017.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.jsoup.helper.StringUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.utils.FilePersistentBase;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;

import static java.io.File.separator;

interface CrawlerListener {

    boolean buttonClicked(boolean on);

}

public class ScrawerUI extends Application implements SpiderListener{

    private Button startButton;
    private Spinner spinner;

    TextArea text1;
    Spinner indexingSpinner1;
    Spinner indexingSpinner2;
    RadioButton precending;
    ToggleGroup toggleGroup;
    CheckBox strictModeBox;
    CheckBox debugModeBox;
    private TextArea sepText;
    static Spider spider;

    static Spider domainSpider;

    TabPane tabPane;
    /********* domain para *********/
    SearchDomainBox searchDomainBox;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 创建UI
        createUI(primaryStage);

        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.contains("crawler.jar")) {
            path = path.replace("crawler.jar", "config.txt");
        }else {
            path = path.concat("config.txt");
        }

        System.out.println("loading config file at "+ path);

        String ats = null;
        try {
            ats = Main.readFileByLines(path);
        } catch (IOException e) {

        }
        this.setSeperator(ats);

        this.addCLickLisener(new CrawlerListener() {
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
        });

        this.addSearchDomainCLickLisener(new SearchDomainBoxListener() {
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

            Properties properties = System.getProperties() ;
            String path = properties.getProperty("user.home");

            File file = new File(path);

            String outPutPath = file.getAbsolutePath() + separator + domainSpider.getSite().getDomain() + ".csv";

            FilePersistentBase pa = new FilePersistentBase();
            pa.setPath(outPutPath);
            file = pa.getFile(outPutPath);
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

            int threadNum = this.getThreadNum();
            String url = this.getUrl();

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

            boolean workingMode = this.getWorkingMode();
            int startIndex = this.getStartIndex();
            int endIndex = this.getEndIndex();
            boolean strictMode = this.enableStrictMode();
            String ats = this.getSeperator();
            //////////////////////////////////////////////////////////////////////////////////////

            GithubRepoPageProcessor pageProcessor = new GithubRepoPageProcessor(false);
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

    private void showAlertWithMessage(String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING, message , ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            //do stuff
        }
    }

    @Override
    public void onError(Request request) {

    }

    public static String readFileByLines(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = null;
        String str = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                str += tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("read file error:" + fileName);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return str;
    }

    public void startScrawling (boolean start) {
        if (getFunctionType()) {
            if (start) {
                searchDomainBox.getStartButton().setText("结束");
            } else  {
                searchDomainBox.getStartButton().setText("开始");
            }
        } else  {
            if (start) {
                startButton.setText("结束");
            } else  {
                startButton.setText("开始");
            }
        }

    }

    public void addCLickLisener(CrawlerListener listener) {
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean suc = listener.buttonClicked(startButton.getText().equals("开始"));
                startScrawling(suc);
            }
        });
    }

    public void addSearchDomainCLickLisener(SearchDomainBoxListener listener) {
        searchDomainBox.getStartButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean suc = listener.buttonClicked(searchDomainBox.getStartButton().getText().equals("开始"));
                startScrawling(suc);
            }
        });
    }


    public boolean getFunctionType() {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        return selectionModel.isSelected(1);
    }

    public String getUrl() {
        return text1.getText().trim();
    }

    public boolean getWorkingMode() {
        return precending.isSelected();
    }

    public int getStartIndex() {
        return Integer.valueOf(indexingSpinner1.getValue().toString());
    }

    public int getEndIndex() {
        return Integer.valueOf(indexingSpinner2.getValue().toString());
    }

    public int getThreadNum() {
        return Integer.valueOf(spinner.getValue().toString());
    }

    public boolean enableStrictMode() {
        return strictModeBox.isSelected();
    }

    public void setSeperator(String string) {
        sepText.setText(string);
    }

    public String getSeperator() {
        return sepText.getText();
    }

    public boolean isEnableDebug() {
        return debugModeBox.isSelected();
    }

    public void  setDebugEnabled(boolean enabled) {
        debugModeBox.setSelected(enabled);
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

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        /**************** 1 ********************/
//        GridPane gridPane = new GridPane();
//        gridPane.setHgap(30);
//        vbox.setPadding(new Insets(10,10,10,10));
//        Text title = new Text("请选择模式");
//        title.textAlignmentProperty().set(TextAlignment.CENTER);
//        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        gridPane.add(title, 0, 0);

//        ComboBox box = new ComboBox();
//        box.getItems().add("超级邮箱检索器");
//        box.getItems().add("网站关键词杀手");
//        box.getSelectionModel().select(0);
//        gridPane.add(box, 1, 0);

//        vbox.getChildren().add(gridPane);

        /**************** 2 ********************/
        Text httpHint = new Text("请输入需要抓取的起始地址以http开头:");
        vbox.getChildren().add(httpHint);

        text1 = new TextArea();
        text1.setWrapText(true);
        text1.setPrefSize(300, 80);
        text1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vbox.getChildren().add(text1);

        /**************** working mode ********************/
        GridPane workingModePane = new GridPane();
        workingModePane.setHgap(10);
        workingModePane.setVgap(20);
        workingModePane.setPadding(new Insets(10,0,10,0));
        workingModePane.setAlignment(Pos.CENTER_LEFT);

        Text workingModel = new Text("请选择工作模式:");
        workingModePane.add(workingModel, 0, 0);

        precending = new RadioButton("precendent");
        precending.setSelected(true);
        workingModePane.add(precending, 1, 0);

        RadioButton indexing = new RadioButton("indexing");
        indexing.setSelected(false);
        workingModePane.add(indexing, 2, 0);

        toggleGroup = new ToggleGroup();
        precending.setToggleGroup(toggleGroup);
        indexing.setToggleGroup(toggleGroup);

        /**************** indexing spin ********************/

        Text indexingText = new Text("Indexing");

        indexingSpinner1 = new Spinner(0, 99999999, 0, 1);
        indexingSpinner1.setMaxWidth(100);
        indexingSpinner2 = new Spinner(0, 99999999, 0, 1);
        indexingSpinner2.setMaxWidth(100);
        workingModePane.add(indexingText, 0,1);
        workingModePane.add(indexingSpinner1, 1, 1);
        workingModePane.add(indexingSpinner2, 2, 1);

        /**************** speed ********************/

        String disText = "请选择抓取速度,量力而行, \n数值越大越影响本机速度:";
        Text speedText = new Text(disText);
        speedText.setFill(Color.RED);
        speedText.setTextAlignment(TextAlignment.LEFT);
        workingModePane.add(speedText, 0, 2);



        spinner = new Spinner(0, 100, 10, 1);
        spinner.setMaxWidth(100);
        workingModePane.add(spinner, 2, 2);

        vbox.getChildren().add(workingModePane);

        /**************** strict mode ********************/

        strictModeBox = new CheckBox("开启严肃模式");
        strictModeBox.setSelected(true);
        vbox.getChildren().add(strictModeBox);

        /**************** revise code ********************/

        Text revise = new Text("添加@替换,逗号分隔");
        vbox.getChildren().add(revise);

        sepText = new TextArea();
        sepText.setWrapText(true);
        sepText.setPrefSize(300, 50);
        sepText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vbox.getChildren().add(sepText);

        debugModeBox = new CheckBox("开启debug模式");
        debugModeBox.setSelected(false);
        vbox.getChildren().add(debugModeBox);

        startButton = new Button("开始");
        startButton.setFont(Font.font(18));
        startButton.setTextFill(Color.RED);
        startButton.setMinWidth(40);
        startButton.setMaxWidth(Double.MAX_VALUE);
        vbox.getChildren().add(startButton);



        Tab searchEmailPan = new Tab("    邮箱关键词检索器    ", vbox);
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

    SearchDomainBox getSearchDomainBox() {
        SearchDomainBox domainSearchVbox = new SearchDomainBox(10);
        return domainSearchVbox;
    }
}

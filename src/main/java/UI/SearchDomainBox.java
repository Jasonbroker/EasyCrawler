package UI;

import Helper.MetaDataHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;

/**
 * Created by jason on 16/01/2017.
 */

public class SearchDomainBox extends VBox {

    private SearchDomainBoxListener listener;

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        startButton.setText(working?"停止":"开始");
        isWorking = working;
    }

    boolean isWorking = false;

    private JFXButton startButton;

    JFXTextArea urlText;
    JFXTextArea keywordText;

    public SearchDomainBox(double spacing) {
        super(spacing);
        this.setStyle("-fx-background-color:#FFFFFF;");

        this.setPadding(new Insets(20, 20, 20, 20));
        Label httpHint = new Label("the url to crawl, starting with http:".toUpperCase());
        this.getChildren().add(httpHint);

        urlText = gennerateTextArea();
        urlText.setWrapText(true);
        urlText.setPrefSize(300, 80);
        urlText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(urlText);

        Label keywordHint = new Label("请输入要检索的关键词,用英文逗号隔开");
        this.getChildren().add(keywordHint);

        keywordText = gennerateTextArea();
        keywordText.setWrapText(true);
        keywordText.setPrefSize(300, 80);
        keywordText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(keywordText);


        startButton = new JFXButton("开始");
        startButton.setFont(Font.font(18));
        startButton.setMinWidth(40);
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.getStyleClass().add("button-raised");
        this.getChildren().add(startButton);
    }

    public String getUrl() {
        return urlText.getText().trim();
    }

    public ArrayList getKeywords() {

        ArrayList arrayList = new ArrayList();
        if (keywordText.getText().length()>0) {
            String[] stringsArr = keywordText.getText().split(",");
            for (String string : stringsArr) {
                if (StringUtil.isBlank(string)) {
                    continue;
                }
                arrayList.add(string.trim());
            }
        }else {
            System.out.println("输入错误");
        }
        return arrayList;
    }

    private JFXTextArea gennerateTextArea() {
        JFXTextArea textArea = new JFXTextArea();
        textArea.setUnFocusColor(Color.color(0.1, 0.1, 0.1, 0.1));
        textArea.setFocusColor(MetaDataHelper.appThemeColor());
        textArea.setWrapText(true);
        return textArea;
    }

    public void addCLickLisener(SearchDomainBoxListener searchEmailBoxListener) {
        listener = searchEmailBoxListener;
    }

}

package com.company;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;

/**
 * Created by jason on 16/01/2017.
 */

interface SearchDomainBoxListener {

    boolean buttonClicked(boolean on);

}

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

    public Button getStartButton() {
        return startButton;
    }

    public void setStartButton(Button startButton) {
        this.startButton = startButton;
    }

    private Button startButton;

    TextArea urlText;
    TextArea keywordText;

    public SearchDomainBox(double spacing) {
        super(spacing);

        this.setPadding(new Insets(10,10,10,10));
        Text httpHint = new Text("请输入需要抓取的起始地址以http开头:");
        this.getChildren().add(httpHint);

        urlText = new TextArea();
        urlText.setWrapText(true);
        urlText.setPrefSize(300, 80);
        urlText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(urlText);

        Text keywordHint = new Text("请输入要检索的关键词,用英文逗号隔开");
        this.getChildren().add(keywordHint);

        keywordText = new TextArea();
        keywordText.setWrapText(true);
        keywordText.setPrefSize(300, 80);
        keywordText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(keywordText);


        startButton = new Button("开始");
        startButton.setFont(Font.font(18));
        startButton.setTextFill(Color.RED);
        startButton.setMinWidth(40);
        startButton.setMaxWidth(Double.MAX_VALUE);
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

    void addCLickLisener(SearchDomainBoxListener searchEmailBoxListener) {
        listener = searchEmailBoxListener;
    }

}

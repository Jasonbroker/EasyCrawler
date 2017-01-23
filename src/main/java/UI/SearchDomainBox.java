package UI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import static UI.ComponentFactory.gennerateTextArea;

/**
 * Created by jason on 16/01/2017.
 */

public class SearchDomainBox extends VBox {

    private SearchDomainBoxListener listener;

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        startButton.setText(working ? "STOP" : "START");
        isWorking = working;
    }

    boolean isWorking = false;

    private JFXButton startButton;

    JFXTextArea urlText;
    JFXTextArea keywordText;

    public SearchDomainBox(double spacing) {
        super(spacing);

        this.setPadding(new Insets(20, 20, 20, 20));

        Label httpHint = new Label("the url to crawl, starting with http:".toUpperCase());
        this.getChildren().add(httpHint);

        urlText = gennerateTextArea(null);
        urlText.setWrapText(true);
        urlText.setPrefSize(300, 80);
        urlText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        urlText.setPadding(new Insets(20, 0, 0, 0));

        Label keywordHint = new Label("Input the keywords, seperated by coma.".toUpperCase());
        keywordHint.setWrapText(true);
        keywordHint.setPadding(new Insets(20, 0, 20, 0));

        keywordText = gennerateTextArea(null);
        keywordText.setWrapText(true);
        keywordText.setPrefSize(300, 80);
        keywordText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        startButton = ComponentFactory.gennerateButton("Start".toUpperCase());

        this.getChildren().addAll(ComponentFactory.roundCardStackPane(0, httpHint, urlText, keywordHint, keywordText), startButton);
        this.getStyleClass().add("background-gradient");
    }

    public String getUrl() {
        return urlText.getText().trim();
    }

    public void setUrl(String url) {
        urlText.setText(url);
    }

    public String getKeywords() {
        return urlText.getText();
    }

    public void setKeyword(String keyword) {
        keywordText.setText(keyword);
    }

    public void addCLickLisener(SearchDomainBoxListener searchEmailBoxListener) {
        listener = searchEmailBoxListener;
    }

}

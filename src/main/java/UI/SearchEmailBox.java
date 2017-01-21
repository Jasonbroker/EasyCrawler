package UI;

import Helper.MetaDataHelper;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * Created by jason on 16/01/2017.
 */

public class SearchEmailBox extends VBox {

    private boolean debug = false;
    private SearchEmailBoxListener listener;

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        startButton.setText(working?"停止":"开始");
        isWorking = working;
    }

    boolean isWorking = false;

    private JFXButton startButton;


    JFXTextArea text1;

    JFXTextField textField;

    JFXRadioButton precending;
    ToggleGroup toggleGroup;
    private JFXSlider speedSlider;

    JFXToggleButton backspaceSwitch1;
    JFXToggleButton backspaceSwitch2;

    JFXToggleButton strictModeSwitch;
    JFXCheckBox debugModeBox;
    private JFXTextArea sepText;
    JFXButton saveButton;

    public SearchEmailBox(double padding) {

        super(padding);
        this.setStyle("-fx-background-color:#FFFFFF;");
        this.setFillWidth(true);

        this.setPadding(new Insets(20, 20, 20, 20));
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        /**************** 2 ********************/
        String crawlHint = "the url to crawl, starting with http:";
        Label httpHint = new Label(crawlHint.toUpperCase());
        this.getChildren().add(httpHint);

        text1 = gennerateTextArea(null);
        text1.setPadding(new Insets(0, 0, 0, 0));
        text1.setPrefSize(300, 80);
        text1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        text1.setLabelFloat(true);
        VBox.setVgrow(text1, Priority.ALWAYS);
        this.getChildren().add(text1);
//        text1.getStyleClass().add("button-like");
        int minHeight = 40;
        /**************** working mode ********************/
        GridPane workingModePane = new GridPane();
        workingModePane.setHgap(10);
        workingModePane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//        workingModePane.setVgap(20);
        workingModePane.setPadding(new Insets(0, 0, 10, 0));

        Label workingModel = new Label("WORK MODE");
        workingModePane.add(workingModel, 0, 0);
        workingModel.setMinHeight(minHeight);

        precending = new JFXRadioButton("NORMAL");
        precending.setSelected(true);
        workingModePane.add(precending, 1, 0);

        JFXRadioButton indexing = new JFXRadioButton("INDEXING");
        indexing.setSelected(false);

        workingModePane.add(indexing, 2, 0);

        toggleGroup = new ToggleGroup();
        precending.setToggleGroup(toggleGroup);
        indexing.setToggleGroup(toggleGroup);

        /**************** indexing spin ********************/

        Label indexingText = new Label("INDEXING");
        indexingText.setMinHeight(minHeight);
        workingModePane.add(indexingText, 0, 1);

        textField = new JFXTextField("0,1000");
        textField.setUnFocusColor(Color.TRANSPARENT);
        textField.setFocusColor(Color.TRANSPARENT);
        textField.setPrefWidth(50);
        textField.setMaxWidth(50);
        textField.setMaxHeight(20);
        GridPane.setHalignment(textField, HPos.RIGHT);
        GridPane.setMargin(textField, new Insets(0, -10, 0, 0));
        workingModePane.add(textField, 2, 1);

//        indexingSpinner1 = new Spinner(0, 99999999, 0, 1);
//        indexingSpinner1.setMaxWidth(100);
//        indexingSpinner1.setEditable(true);
//        indexingSpinner2 = new Spinner(0, 99999999, 0, 1);
//        indexingSpinner2.setMaxWidth(100);
//        indexingSpinner2.setEditable(true);
//        workingModePane.add(indexingText, 0,1);
//        workingModePane.add(indexingSpinner1, 1, 1);
//        workingModePane.add(indexingSpinner2, 2, 1);

        /**************** speed ********************/

        String disText = "CRAWL SPEED";
        Label speedText = new Label(disText);
        speedText.setTextAlignment(TextAlignment.LEFT);
        speedText.setMinHeight(minHeight);
        workingModePane.add(speedText, 0, 2);


//        spinner = new Spinner(0, 100, 10, 1);
//        spinner.setMaxWidth(100);
        speedSlider = new JFXSlider(1, 50, 10);
        speedSlider.setPrefWidth(100);
        workingModePane.add(speedSlider, 2, 2);

        /**************** strict mode ********************/
        Label strictLabel = new Label("STRICT MODE");
        strictLabel.setMinHeight(minHeight);
        workingModePane.add(strictLabel, 0, 3);
        GridPane.setHalignment(strictLabel, HPos.LEFT);

        strictModeSwitch = generateRightSwitch();
        workingModePane.add(strictModeSwitch, 2, 3);
        this.getChildren().add(workingModePane);

        /**************** revise code ********************/
        Label space1 = new Label("PREFIX SPACE ENTER");
        workingModePane.add(space1, 0, 4);
        space1.setMinHeight(minHeight);
        GridPane.setHalignment(space1, HPos.LEFT);

        backspaceSwitch1 = generateRightSwitch();
        workingModePane.add(backspaceSwitch1, 2, 4);
        backspaceSwitch1.setSelected(false);
        Label space2 = new Label("SUFFIX SPACE ENTER");
        GridPane.setHalignment(space2, HPos.LEFT);
        workingModePane.add(space2, 0, 5);

        backspaceSwitch2 = generateRightSwitch();
        workingModePane.add(backspaceSwitch2, 2, 5);
        backspaceSwitch2.setSelected(false);

//
//        Label revise = new Label("添加@替换,逗号分隔");
//        this.getChildren().add(revise);
//
//        GridPane reviseCodePane = new GridPane();
//        reviseCodePane.setHgap(30);
//        backspaceCheckBox1 = new JFXCheckBox("@前匹配空格或回车");
//        backspaceCheckBox1.setSelected(false);
//        reviseCodePane.add(backspaceCheckBox1, 0, 0); //\s*
//
//        backspaceCheckBox2 = new JFXCheckBox("@后匹配空格或回车");
//        backspaceCheckBox2.setSelected(false);
//        reviseCodePane.add(backspaceCheckBox2, 1, 0); //\s*
//
//        this.getChildren().add(reviseCodePane);


        sepText = gennerateTextArea(null);
        sepText.setPrefHeight(60);
        sepText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        sepText.textProperty().addListener((observable) -> {
//            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                saveButton.setDisable(false);
//            }
        });

        saveButton = new JFXButton("save".toUpperCase());
        saveButton.setMinSize(60, 60);
        saveButton.getStyleClass().add("savebutton-raised");
        saveButton.setDisable(true);

        HBox hbox = new HBox(10,sepText, saveButton);
        hbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        hbox.setPrefWidth(300);
        this.getChildren().add(hbox);
        HBox.setHgrow(sepText, Priority.ALWAYS);

        debugModeBox = new JFXCheckBox("DEBUG MODE");
        debugModeBox.setSelected(false);
        this.getChildren().add(debugModeBox);

        startButton = new JFXButton("开始");
        startButton.setFont(Font.font(18));
        startButton.setMinWidth(40);
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.getStyleClass().add("button-raised");
        VBox.setVgrow(startButton, Priority.ALWAYS);
        this.getChildren().add(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean suc = listener.buttonClicked(!isWorking);
                setWorking(suc);
            }
        });

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                listener.saveButtonPressedWithText(sepText.getText());
                saveButton.setDisable(true);
            }
        });

        workingModePane.setGridLinesVisible(debug);
    }

    private JFXToggleButton generateRightSwitch() {
        JFXToggleButton button = new JFXToggleButton();
        button.setPrefHeight(30);
        button.setMaxHeight(40);
        button.setSelected(true);
        GridPane.setHalignment(button, HPos.RIGHT);
        GridPane.setMargin(button, new Insets(-5, -15, -5, 0));
        return button;
    }

    private JFXTextArea gennerateTextArea(String string) {
        JFXTextArea textArea = new JFXTextArea(string);
        textArea.setUnFocusColor(Color.color(0.1, 0.1, 0.1, 0.1));
        textArea.setFocusColor(MetaDataHelper.appThemeColor());
        textArea.setWrapText(true);
        return textArea;
    }

    private VBox lineWithTitleAndSwitch(String title) {
        JFXToggleButton button = new JFXToggleButton();
        button.setPrefHeight(30);
        button.setMaxHeight(40);
        VBox box = lineWithTitleAndParent(title, button);
        GridPane.setMargin(button, new Insets(-5, -15, -5, 0));
        return box;
    }

    private VBox lineWithTitleAndParent(String string, Parent parent) {
        GridPane lineContents = new GridPane();

        Label text = new Label(string.toUpperCase());
        text.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        lineContents.add(text, 0, 0);
        lineContents.add(parent, 1, 0);
        lineContents.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane.setHalignment(text, HPos.LEFT);
        GridPane.setHalignment(parent, HPos.RIGHT);
        lineContents.setGridLinesVisible(true);
        VBox box = new VBox(lineContents);
        box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        box.setFillWidth(true);
        return box;
    }

    private GridPane gridPanelineWithTitleAndParent(String string, Parent parent) {
        GridPane lineContents = new GridPane();

        Label text = new Label(string.toUpperCase());
        text.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        lineContents.add(text, 0, 0);
        lineContents.add(parent, 1, 0);
        lineContents.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane.setHalignment(text, HPos.LEFT);
        GridPane.setHalignment(parent, HPos.RIGHT);
        lineContents.setGridLinesVisible(true);

        return lineContents;
    }


    public void addCLickLisener(SearchEmailBoxListener searchEmailBoxListener) {
        listener = searchEmailBoxListener;
    }


    public String getUrl() {
        return text1.getText().trim();
    }

    public boolean getWorkingMode() {
        return precending.isSelected();
    }

    public int getStartIndex() {
        ArrayList<String> list = MetaDataHelper.getKeywords(textField.getText());
        return Integer.valueOf(list.get(0));
    }

    public int getEndIndex() {
        ArrayList<String> list = MetaDataHelper.getKeywords(textField.getText());
        return Integer.valueOf(list.get(1));
    }

    public int getThreadNum() {
        return (int) speedSlider.getValue();
    }

    public boolean enableStrictMode() {
        return strictModeSwitch.isSelected();
    }

    public void setSeperator(String string) {
        sepText.setText(string);
    }

    public String getSeperator() {
        return sepText.getText();
    }

    public boolean getBackSpaceHead() {
        return backspaceSwitch1.isSelected();
    }

    public boolean getBackSpaceTail() {
        return backspaceSwitch2.isSelected();
    }

    public boolean isEnableDebug() {
        return debugModeBox.isSelected();
    }

    public void  setDebugEnabled(boolean enabled) {
        debugModeBox.setSelected(enabled);
    }



}

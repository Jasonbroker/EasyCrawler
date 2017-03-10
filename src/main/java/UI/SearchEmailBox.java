package UI;

import Helper.MetaDataHelper;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

import static UI.ComponentFactory.generateRightSwitch;
import static UI.ComponentFactory.gennerateTextArea;

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
        startButton.setText(working ? "STOP" : "START");
        isWorking = working;
    }

    boolean isWorking = false;

    private JFXButton startButton;


    JFXTextArea text1;

    JFXTextField textField;

    JFXRadioButton precending;
    JFXRadioButton indexing;
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
        this.setFillWidth(true);

        this.setPadding(new Insets(20, 20, 20, 20));
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        /**************** crawlHint ********************/
        String crawlHint = "the url to crawl, start with http:";
        Label httpHint = new Label(crawlHint.toUpperCase());
//        this.getChildren().add(httpHint);

        text1 = gennerateTextArea(null);
        text1.setPadding(new Insets(0, 0, 0, 0));
        text1.setPrefSize(300, 80);
        text1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        text1.setLabelFloat(true);
        VBox.setVgrow(text1, Priority.ALWAYS);
//        this.getChildren().add(text1);
        int minHeight = 40;

        /**************** working mode ********************/
        GridPane workingModePane = new GridPane();
        workingModePane.setHgap(10);
        workingModePane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        workingModePane.setPadding(new Insets(0, 0, 10, 0));

        Label workingModel = new Label("WORK MODE");
        workingModePane.add(workingModel, 0, 0);
        workingModel.setMinHeight(minHeight);

        precending = new JFXRadioButton("NORMAL");
        precending.setSelected(true);
        workingModePane.add(precending, 1, 0);

        indexing = new JFXRadioButton("INDEXING");
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
//        this.getChildren().add(workingModePane);

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

        /*********************    *********************************/
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
        saveButton.getStyleClass().add("save-button");
        saveButton.setDisable(true);

        HBox hbox = new HBox(10,sepText, saveButton);
        hbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        hbox.setPrefWidth(300);
//        this.getChildren().add(hbox);
        HBox.setHgrow(sepText, Priority.ALWAYS);

        debugModeBox = new JFXCheckBox("DEBUG MODE");
        debugModeBox.setSelected(false);
//        this.getChildren().add(debugModeBox);

        startButton = ComponentFactory.gennerateButton("start".toUpperCase());
        VBox.setVgrow(startButton, Priority.ALWAYS);

        StackPane card = ComponentFactory.roundCardStackPane(20, httpHint, text1, workingModePane, hbox, debugModeBox);
        VBox.setVgrow(card, Priority.ALWAYS);
        this.getChildren().addAll(card, startButton);

/***************************************************************************************/

//        Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED) };
//        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
//
//        Rectangle r1 = new Rectangle(0, 0, 100, 100);
//        r1.setFill(lg1);

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
        this.getStyleClass().add("background-gradient");

    }

    public void addCLickLisener(SearchEmailBoxListener searchEmailBoxListener) {
        listener = searchEmailBoxListener;
    }

    public void setUrl(String text) {
        text1.setText(text);
    }
    public String getUrl() {
        return text1.getText().trim();
    }

    public int getWorkingMode() {
        return precending.isSelected() ? 0 : 1;
    }

    public void setWorkMode(int workmode) {
        precending.setSelected(workmode == 0);
        indexing.setSelected(workmode == 1);
    }

    public int getStartIndex() {
        ArrayList<String> list = MetaDataHelper.getKeywords(textField.getText());
        return Integer.valueOf(list.get(0));
    }

    public int getEndIndex() {
        ArrayList<String> list = MetaDataHelper.getKeywords(textField.getText());
        return Integer.valueOf(list.get(1));
    }

    public void setstartEndIndex(int start, int end) {
        textField.setText(start + "," + end);
    }

    public int getThreadNum() {
        return (int) speedSlider.getValue();
    }

    public void setThreadNum(int num) {
        speedSlider.setValue(num);
    }

    public boolean enableStrictMode() {
        return strictModeSwitch.isSelected();
    }

    public void setStrictMode(boolean enable) {
        strictModeSwitch.setSelected(enable);
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

    public void setEnableSpaceHead(boolean enable) {
        backspaceSwitch1.setSelected(enable);
    }

    public void setEnableSpaceTrail(boolean enable) {
        backspaceSwitch2.setSelected(enable);
    }

    public boolean isEnableDebug() {
        return debugModeBox.isSelected();
    }

    public void  setDebugEnabled(boolean enabled) {
        debugModeBox.setSelected(enabled);
    }



}

package com.company;

import com.sun.java.swing.action.SaveAction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by jason on 16/01/2017.
 */
interface SearchEmailBoxListener {

    boolean buttonClicked(boolean on);

    void saveButtonPressedWithText(String str);

}

public class SearchEmailBox extends VBox {

    private SearchEmailBoxListener listener;

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        startButton.setText(working?"停止":"开始");
        isWorking = working;
    }

    boolean isWorking = false;

    private Button startButton;
    private Spinner spinner;

    TextArea text1;
    Spinner indexingSpinner1;
    Spinner indexingSpinner2;
    RadioButton precending;
    ToggleGroup toggleGroup;
    CheckBox backspaceCheckBox1;
    CheckBox backspaceCheckBox2;
    CheckBox strictModeBox;
    CheckBox debugModeBox;
    private TextArea sepText;
    Button saveButton;
    public SearchEmailBox(double padding) {

        super(padding);
        this.setPadding(new Insets(10,10,10,10));
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        /**************** 2 ********************/
        Text httpHint = new Text("请输入需要抓取的起始地址以http开头:");
        this.getChildren().add(httpHint);

        text1 = new TextArea();
        text1.setWrapText(true);
        text1.setPrefSize(300, 80);
        text1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(text1);

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
        indexingSpinner1.setEditable(true);
        indexingSpinner2 = new Spinner(0, 99999999, 0, 1);
        indexingSpinner2.setMaxWidth(100);
        indexingSpinner2.setEditable(true);
        workingModePane.add(indexingText, 0,1);
        workingModePane.add(indexingSpinner1, 1, 1);
        workingModePane.add(indexingSpinner2, 2, 1);

        /**************** speed ********************/

        String disText = "请选择抓取速度:";
        Text speedText = new Text(disText);
        speedText.setTextAlignment(TextAlignment.LEFT);
        workingModePane.add(speedText, 0, 2);

        spinner = new Spinner(0, 100, 10, 1);
        spinner.setMaxWidth(100);
        workingModePane.add(spinner, 2, 2);

        /**************** strict mode ********************/

        strictModeBox = new CheckBox("严肃模式");
        strictModeBox.setSelected(true);
        workingModePane.add(strictModeBox,0, 3);

        this.getChildren().add(workingModePane);

        /**************** revise code ********************/

        Text revise = new Text("添加@替换,逗号分隔");
        this.getChildren().add(revise);

        GridPane reviseCodePane = new GridPane();
        reviseCodePane.setHgap(30);
        backspaceCheckBox1 = new CheckBox("@前匹配空格或回车");
        backspaceCheckBox1.setSelected(false);
        reviseCodePane.add(backspaceCheckBox1, 0, 0); //\s*

        backspaceCheckBox2 = new CheckBox("@后匹配空格或回车");
        backspaceCheckBox2.setSelected(false);
        reviseCodePane.add(backspaceCheckBox2, 1, 0); //\s*

        this.getChildren().add(reviseCodePane);


        sepText = new TextArea();
        sepText.setWrapText(true);
        sepText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        sepText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                saveButton.setDisable(false);
            }
        });

        saveButton = new Button("save");
        saveButton.setMinSize(60, 60);
        saveButton.setDisable(true);
        HBox hbox = new HBox(10,sepText, saveButton);
        hbox.setPrefSize(300, 60);
        this.getChildren().add(hbox);

        debugModeBox = new CheckBox("开启debug模式");
        debugModeBox.setSelected(false);
        this.getChildren().add(debugModeBox);

        startButton = new Button("开始");
        startButton.setFont(Font.font(18));
        startButton.setTextFill(Color.RED);
        startButton.setMinWidth(40);
        startButton.setMaxWidth(Double.MAX_VALUE);
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



    }

    void addCLickLisener(SearchEmailBoxListener searchEmailBoxListener) {
        listener = searchEmailBoxListener;
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

    public boolean getBackSpaceHead() {
        return backspaceCheckBox1.isSelected();
    }

    public boolean getBackSpaceTail() {
        return backspaceCheckBox2.isSelected();
    }

    public boolean isEnableDebug() {
        return debugModeBox.isSelected();
    }

    public void  setDebugEnabled(boolean enabled) {
        debugModeBox.setSelected(enabled);
    }



}

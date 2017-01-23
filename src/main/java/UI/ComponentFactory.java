package UI;

import Helper.MetaDataHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.effects.JFXDepthManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by jason on 22/01/2017.
 */
public class ComponentFactory {

    public static JFXToggleButton generateRightSwitch() {
        JFXToggleButton button = new JFXToggleButton();
        button.setPrefHeight(30);
        button.setMaxHeight(40);
        button.setSelected(true);
        GridPane.setHalignment(button, HPos.RIGHT);
        GridPane.setMargin(button, new Insets(-5, -15, -5, 0));
        return button;
    }

    public static JFXTextArea gennerateTextArea(String string) {
        JFXTextArea textArea = new JFXTextArea(string);
        textArea.setUnFocusColor(Color.valueOf("#e2cfff"));
        textArea.setFocusColor(MetaDataHelper.appThemeDarkerColor());
        textArea.setWrapText(true);
        return textArea;
    }

    public static JFXButton gennerateButton(String title) {
        JFXButton button = new JFXButton(title);
        button.setPadding(new Insets(20, 20, 20, 20));
        button.setFont(Font.font(18));
        button.setMinWidth(40);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("button-raised");
        button.setStyle("-fx-text-fill: " + MetaDataHelper.appThemeDarkerColorString());
        return button;
    }

    public static StackPane roundCardStackPane(double padding, Node... nodes) {
        VBox innerBox = new VBox(padding, nodes);
        innerBox.setStyle("-fx-background-color: rgb(0,0,0,0);");
        innerBox.setPadding(new Insets(20, 20, 20, 20));
        StackPane innerStack = new StackPane(innerBox);
        innerStack.setStyle("-fx-background-radius: 6 6 6 6; -fx-background-color: rgb(255,255,255,1);");
        JFXDepthManager.setDepth(innerStack, 1);
        return innerStack;
    }

    public static VBox lineWithTitleAndSwitch(String title) {
        JFXToggleButton button = new JFXToggleButton();
        button.setPrefHeight(30);
        button.setMaxHeight(40);
        VBox box = lineWithTitleAndParent(title, button);
        GridPane.setMargin(button, new Insets(-5, -15, -5, 0));
        return box;
    }

    public static VBox lineWithTitleAndParent(String string, Parent parent) {
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

    public static GridPane gridPanelineWithTitleAndParent(String string, Parent parent) {
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
}

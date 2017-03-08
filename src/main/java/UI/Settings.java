package UI;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

/**
 * Created by jason on 19/01/2017.
 */
public class Settings extends VBox {

    private Label folderText;
    private SettingsListener listener;

    public String getFolder() {
        return folderText.getText();
    }

    public void setFolder(String folder) {
        this.folder = folder;
        folderText.setText(folder);
    }

    public String folder;

    public Settings(double padding) {
        super(padding);

        this.setPadding(new Insets(20, 20, 20, 20));

        Label text = new Label("set default saving folder".toUpperCase());
//        this.getChildren().add(text);

        folderText = new Label();
//        this.getChildren().add(folderText);

        File folderFile = new File(folderText.getText());
        if (!folderFile.isDirectory()) {

        }


        JFXButton button = new JFXButton("Choose".toUpperCase());
        button.getStyleClass().add("save-button");

        Stage stage = new Stage(StageStyle.DECORATED);
        button.setOnAction((e) -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(new File(folderText.getText()));
            chooser.setTitle("Choose the path to save csv file.");
            File file = chooser.showDialog(stage);
            folderText.setText(file.getAbsolutePath());
            layout();
        });

        JFXButton readme = new JFXButton("read me".toUpperCase());
        readme.setOnAction((e) -> {
            this.listener.readmeButtonClicked();
        });


        StackPane pane = ComponentFactory.roundCardStackPane(20, text, folderText, button, readme);

        JFXButton saveButton = ComponentFactory.gennerateButton("Save".toUpperCase());

        this.getChildren().addAll(pane, saveButton);


        this.getStyleClass().add("background-gradient");

        saveButton.setOnAction((event) -> {
            this.listener.saveButtonClicked();
        });


    }

    public void addCLickLisener(SettingsListener settingsListener) {
        this.listener = settingsListener;
    }

}

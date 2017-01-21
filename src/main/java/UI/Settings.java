package UI;

import Helper.FileHelper;
import com.jfoenix.controls.JFXButton;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

/**
 * Created by jason on 19/01/2017.
 */
public class Settings extends StackPane {

    public Settings() {
        super();
        JFXButton button = new JFXButton("Choose".toUpperCase());

        Stage stage = new Stage(StageStyle.DECORATED);
        button.setOnAction((e) -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(new File(FileHelper.desktopPathWithFileName("")));
            chooser.setTitle("Choose the path to save csv file.");
            File file = chooser.showDialog(stage);
            System.out.print(file.getAbsolutePath());
        });
        this.getChildren().add(button);
    }

}

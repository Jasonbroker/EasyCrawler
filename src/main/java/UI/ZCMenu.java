package UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Created by jason on 21/01/2017.
 */
public class ZCMenu {

    private MenuBar getMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.setPadding(new Insets(0, 0, 0, 0));
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
//                showAlertWithMessage("版本信息", "");
            }
        });

        m1.getItems().addAll(item11, item12, item13);
        m2.getItems().addAll(item21, item22, item23);
        m3.getItems().addAll(item31, item32, item33);
        menuBar.getMenus().addAll(m1, m2, m3);

        return menuBar;
    }
}

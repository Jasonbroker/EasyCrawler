package Helper;

import javafx.scene.paint.Color;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;

/**
 * Created by jason on 21/01/2017.
 */
public class MetaDataHelper {
    public static ArrayList<String> getKeywords(String string) {

        ArrayList arrayList = new ArrayList();
        if (string.length() > 0) {
            String[] stringsArr = string.split(",");
            for (String innerString : stringsArr) {
                if (StringUtil.isBlank(innerString)) {
                    continue;
                }
                arrayList.add(innerString.trim());
            }
        } else {
            return null;
        }
        return arrayList;
    }

    public static Color appThemeDarkerColor() {
        return Color.valueOf(appThemeDarkerColorString());
    }

    public static Color appThemeLighterColor() {
        return Color.valueOf(appThemeLighterColorString());
    }

    public static String appThemeDarkerColorString() {
        return "#A068F9";
    }

    public static String appThemeLighterColorString() {
        return "#9A9AF4";
    }

    public static String appThemeBackGroundColor() {
        return "-fx-background-color:" + "#A068F9";
    }

}

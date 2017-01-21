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
                arrayList.add(string.trim());
            }
        } else {
            return null;
        }
        return arrayList;
    }

    public static Color appThemeColor() {
//        return Color.color(0.1,0.1,0.1,0.1);
        return Color.GREEN;
    }
}

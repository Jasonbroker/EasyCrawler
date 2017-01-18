package Crawler;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by jason on 23/10/2016.
 */
public class RegexGenerator {

    public static String regexForArray(ArrayList<String> arrayList) {
        String result = "";
        if (arrayList.size() == 1) {
            result = escapeExprSpecialWord(arrayList.get(0));
        }else {
            int i = 0;
            for (String keyword : arrayList) {
                String regKeyword = escapeExprSpecialWord(keyword);
                result += "(" + regKeyword + ")";
                if (i != arrayList.size() - 1) {
                    result += "|";
                }
                i++;
            }
        }

        return result;
    }


    public static String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}

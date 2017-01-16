package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static java.io.File.separator;

/**
 * Created by jason on 16/01/2017.
 */
public class FileHelper {

    public static String jarPath() {
        return Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    }

    public static String pathInCodeFolder(String filename) {
        String path = jarPath();
        if (path.contains("crawler.jar")) {
            path = path.replace("crawler.jar", filename);
        }else {
            path = path.concat(filename);
        }
        return path;
    }

    public static String desktopPathWithFileName(String filename) {
        Properties properties = System.getProperties() ;
        String path = properties.getProperty("user.home");
        File file = new File(path);
        return  file.getAbsolutePath() + separator + "Desktop"+ separator + filename;
    }

    public static String readFileByLines(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = null;
        String str = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            //int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                str += tempString;
                //line++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("read file error:" + fileName);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return str;
    }


}

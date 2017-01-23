package Helper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.io.File.separator;

/**
 * Created by jason on 16/01/2017.
 */
public class FileHelper {

    static PrintWriter printWriter;
    // 用户配置文件名
    final static String USERINFO_FILENAME = "userinfo.config";
    public static String jarPath() {
        return FileHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

//    public static void writeToUserInfoFile(HashMap hashMap) throws IOException {
//        if (printWriter == null) {
//            printWriter = new PrintWriter(new FileWriter(pathInCodeFolder(USERINFO_FILENAME), false));
//        }
//        printWriter.print(hashMap);
//        printWriter.close();
//    }
//
//    public static HashMap<String, String> readUserInfo() throws IOException {
//        String string = readFileByLines(pathInCodeFolder(USERINFO_FILENAME));
//        if (!string.isEmpty()) {
//            HashMap hashMap = string.;
//        }
//    }

    public static void saveUserConfig(Map map) {
        writeObject(map, pathInCodeFolder(USERINFO_FILENAME));
    }

    public static HashMap<String, String> readConfig() {
        return readObject(pathInCodeFolder(USERINFO_FILENAME));
    }

    public static void writeObject(Map map, String path) {
        try {
            FileOutputStream outStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);

            objectOutputStream.writeObject(map);
            outStream.close();
            System.out.println("save successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> readObject(String path) {
        FileInputStream freader;
//        HashMap<String, String> map = null;
        try {
            freader = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, String> map = new HashMap<String, String>();

//            List<Map<String, Object>> list = (List<Map<String, Object>>) objectInputStream.readObject();
            HashMap<String, String> map = (HashMap<String, String>) objectInputStream.readObject();
            return map;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String desktopPathWithFileName(String filename) {
        Properties properties = System.getProperties() ;
        String path = properties.getProperty("user.home");
        File file = new File(path);
        return  file.getAbsolutePath() + separator + "Desktop"+ separator + filename;
    }

    public static void writeToConfigFile(String string) throws IOException {
        if (printWriter == null) {
            printWriter = new PrintWriter(new FileWriter(pathInCodeFolder("config.txt"), false));
        }
        printWriter.print(string);
        printWriter.close();
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

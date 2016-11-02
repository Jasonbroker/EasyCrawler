package com.company;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.helper.StringUtil;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.utils.FilePersistentBase;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        EntryLogger.logWelcome();

        Scanner s = new Scanner(System.in);

        String entry = EntryLogger.logFuction(s);

        EntryLogger.enterFunction(entry);

        Spider spider;

        if (entry.equals("1")) {

            System.out.println("请输入要查询的邮箱的主站地址,以http开头, 回车继续:");

            String url = s.nextLine().trim();
            while (url.isEmpty()) {

                System.out.println("请输入要查询的邮箱的主站地址, 以http开头, 回车确认:");
                url = s.nextLine().trim();
            }

            System.out.println("请输入需要查询的关键词,用英文隔开, 回车确认:");

            String keywordsStr = s.nextLine().trim();
            ArrayList arrayList = new ArrayList();
            if (keywordsStr.length()>0) {
                String[] stringsArr = keywordsStr.split(",");
                for (String string : stringsArr) {
                    if (StringUtil.isBlank(string)) {
                        continue;
                    }
                    arrayList.add(string.trim());
                }
            }else {
                System.out.println("输入错误");
            }

            KeywordFindingPageProcessor pageProcessor = new KeywordFindingPageProcessor(arrayList);
            spider = Spider.create(pageProcessor);
            spider.addUrl(url);
            spider.setExitWhenComplete(true);
            File file;
            FileSystemView fsv = FileSystemView.getFileSystemView();
            File com=fsv.getHomeDirectory();

            String outPutPath = com.getAbsolutePath()+ "/Desktop/" + "findings/" + spider.getSite().getDomain() + ".csv";

            FilePersistentBase pa = new FilePersistentBase();
            pa.setPath(outPutPath);
            file = pa.getFile(outPutPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            spider.addPipeline(new KeywordPipline(file.getAbsolutePath()));
            System.out.println("开启20个线程抓取, 当前占用系统资源较多,请注意避免开启太多软件造成卡顿!");
            System.out.println("开始批量抓取目标网址");

            spider.thread(20);
            spider.runAsync();


        }else {

            GithubRepoPageProcessor pageProcessor = new GithubRepoPageProcessor(false);
            spider = Spider.create(pageProcessor);

            System.out.println("请选择工作模式: 1 层级抓取 2 序列抓取");

            String modeStr = s.nextLine().trim();
            ArrayList urls = new ArrayList();
            if (modeStr.equals("1")) {
                System.out.println("工作模式: 层级抓取");

                System.out.println("请输入要查询的邮箱的主站地址,以http开头, 回车继续:");

                String url = s.nextLine().trim();
                while (url.isEmpty()) {

                    System.out.println("请输入要查询的邮箱的主站地址, 以http开头, 回车继续:");
                    url = s.nextLine().trim();
                }
                spider.addUrl(url);

            }else  {
                System.out.println("工作模式: 序列抓取");

                System.out.println("请输入要查询的地址,请去除其中的有序索引数字, 使用$代替:");
                String url = s.nextLine().trim();

                while (!url.contains("$")) {

                    System.out.println("输入错误!\n请输入要查询的地址,请去除其中的有序索引数字, 使用$代替:");
                    url = s.nextLine().trim();
                }

                System.out.println("请输入起始索引数字:");
                int head = Integer.valueOf(s.next().trim());
                System.out.println("请输入结束索引数字:");
                int tail = Integer.valueOf(s.next().trim());
                System.out.println("正在生成对应地址………");
                for (int i = head; i <= tail; i++) {
                    String replacedUrl = url.replace("$", "" + i);
                    urls.add(replacedUrl);
                    spider.addUrl(replacedUrl);
                }
                System.out.println("在索引模式下,默认只抓取当前页,不进行深入挖掘 \n如果想拓展功能,请联系开发者 周正昌");
                pageProcessor.maxDepth = 0;
            }
            spider.thread(10);

            System.out.println("请输入要存储的位置(直接回车存到桌面):");
//
            File file = null;

            if (s.nextLine().equals("")) {
                FileSystemView fsv = FileSystemView.getFileSystemView();
                File com=fsv.getHomeDirectory();

                String outPutPath = com.getAbsolutePath()+ "/Desktop/" + spider.getSite().getDomain() + ".csv";

                FilePersistentBase pa = new FilePersistentBase();
                pa.setPath(outPutPath);
                file = pa.getFile(outPutPath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                System.out.println("存储到桌面!" + file.getAbsolutePath());
                PrintWriter printWriter;
                printWriter = new PrintWriter(new FileWriter(file.getAbsolutePath(), true));
                printWriter.print("URL" +"," + "E-mail\n");
                printWriter.close();
            }

            System.out.println("是否需要开启严肃模式(只爬取当页下面的页面,不会继续往下爬取了,建议开启):需要开启输入: y");

            if (s.nextLine().trim().equals("y")){
                System.out.println("开启严肃模式!");
                pageProcessor.strict = true;
                spider.addPipeline(new PendingPipline(file.getAbsolutePath(), true));
            }else {
                spider.addPipeline(new PendingPipline(file.getAbsolutePath(), false));
            }

            System.out.println("如果存在特殊字符不要忘记更改配置文件。");

            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()+ "config.con";
            if (path.contains("crawler.jar")) {
                path = path.replace("crawler.jar", "");
            }
            String ats = Main.readFileByLines(path);
            System.out.println("读取配置文件路径…………"+path + "    content:" + ats);

            if (ats.length()>0) {
                String[] str = ats.split(",");
                pageProcessor.operators = str;
                System.out.println("读取配置文件" + str[0]);
            }else {
                System.out.println("配置文件未读取!!!!!!!!!!...");
            }

            System.out.println("开启10个线程批量抓取email...网速快的话一分钟能抓100个email 地址……");
            System.out.println("开始批量抓取email...");

            spider.setExitWhenComplete(true);
            spider.runAsync();

        }

    }

    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String str = "";
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                str += tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
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


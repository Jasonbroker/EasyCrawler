package com.company;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.helper.StringUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.utils.FilePersistentBase;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import static java.io.File.separator;

public class Main {

    static MainWindow frame1;
    static Spider spider;
    public static void main(String[] args) throws IOException, InterruptedException {


        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                frame1 = MainWindow.createAndShowGUI();

                String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                if (path.contains("crawler.jar")) {
                    path = path.replace("crawler.jar", "config.txt");
                }else {
                   path = path.concat(separator+"config.txt");
                }

                System.out.println("loading config file at "+ path);

                String ats = null;
                try {
                    ats = Main.readFileByLines(path);
                } catch (IOException e) {

                }
                frame1.setSeperator(ats);

                frame1.addCLickLisener(new MainWindowListener() {
                    @Override
                    public boolean buttonClicked(boolean on) {
                        try {
                            if (on) {
                                return runP();
                            } else  {
                                return stop();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        return true;
                    }
                });
            }
        });

    }

    private  static boolean stop () {
        spider.stop();
        return true;
    }

    private  static boolean runP () throws IOException, InterruptedException {

        int threadNum = frame1.getThreadNum();
        String url = frame1.getUrl();

        if (url.isEmpty()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "网址问题", "请输入网址", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!url.startsWith("http")) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "网址问题", "please input the url with http prefix.", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (threadNum > 10000) {
            threadNum = 40;
        } else if (threadNum < 1) {
            threadNum = 1;
        }

        System.out.println(frame1.getFunctionType());
        if (frame1.getFunctionType() == 1) {

//            System.out.println("请输入需要查询的关键词,用英文隔开, 回车确认:");

            String keywordsStr = "21";

            ///////////////////////////////////////////////

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

            String outPutPath = com.getAbsolutePath() + separator + "findings" + separator + spider.getSite().getDomain() + ".csv";

            FilePersistentBase pa = new FilePersistentBase();
            pa.setPath(outPutPath);
            file = pa.getFile(outPutPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            spider.addPipeline(new KeywordPipline(file.getAbsolutePath()));

            spider.thread(threadNum);

        } else {

            boolean workingMode = frame1.getWorkingMode();
            int startIndex = frame1.getStartIndex();
            int endIndex = frame1.getEndIndex();
            boolean strictMode = frame1.enableStrictMode();
            String ats = frame1.getSeperator();
            //////////////////////////////////////////////////////////////////////////////////////

            GithubRepoPageProcessor pageProcessor = new GithubRepoPageProcessor(false);
            spider = Spider.create(pageProcessor);

            ArrayList urls = new ArrayList();
            if (workingMode) {
                spider.addUrl(url);
            }else  {

                if (!url.contains("$")) {
                    System.out.println("输入错误!\n请输入要查询的地址,请去除其中的有序索引数字, 使用$代替:");
                    return false;
                }

                for (int i = startIndex; i <= endIndex; i++) {
                    String replacedUrl = url.replace("$", "" + i);
                    urls.add(replacedUrl);
                    spider.addUrl(replacedUrl);
                }

                pageProcessor.maxDepth = 0;
            }
            spider.thread(threadNum);

            FileSystemView fsv = FileSystemView.getFileSystemView();

            File com=fsv.getHomeDirectory();

            String outPutPath = com.getAbsolutePath() + separator +"Desktop"+ separator + spider.getSite().getDomain() + ".csv";

            System.out.println("test存储到桌面!" + outPutPath);

            FilePersistentBase pa = new FilePersistentBase();
            pa.setPath(outPutPath);
            File file = pa.getFile(outPutPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            System.out.println("存储到桌面!" + file.getAbsolutePath());
            PrintWriter printWriter;
            printWriter = new PrintWriter(new FileWriter(file.getAbsolutePath(), true));
            printWriter.print("URL" +"," + "E-mail\n");
            printWriter.close();

            System.out.println("是否需要开启严肃模式(只爬取当页下面的页面,不会继续往下爬取了,建议开启):需要开启输入: y");

            pageProcessor.strict = strictMode;
            spider.addPipeline(new PendingPipline(file.getAbsolutePath(), strictMode));

            if (ats.length()>0) {
                String[] str = ats.split(",");
                pageProcessor.operators = str;
                System.out.println("读取配置文件" + str[0]);
            }else {
                System.out.println("配置文件未读取!!!!!!!!!!...");
            }

            spider.setExitWhenComplete(true);

        }
        SpiderListener listener = new SpiderListener() {
            @Override
            public void onSuccess(Request request) {
                int thread = spider.getThreadAlive();
                if (thread == 0) {
                    frame1.start(false);
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "成功", "抓取成功,请在桌面查看",JOptionPane.INFORMATION_MESSAGE);
                }

            }

            @Override
            public void onError(Request request) {

            }
        };
        LinkedList list = new LinkedList<>();
        list.add(listener);
        spider.setSpiderListeners(list);
        spider.runAsync();
        return true;
    }

    public static String readFileByLines(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = null;
        String str = "";
        try {
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
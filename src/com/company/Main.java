package com.company;

import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.FilePersistentBase;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner s = new Scanner(System.in);
        System.out.println("请输入要查询的邮箱的主站地址,以http开头, 回车继续:");

        String url = s.nextLine().trim();
        while (url.isEmpty()) {

            System.out.println("请输入要查询的邮箱的主站地址, 以http开头, 回车继续:");
            url = s.nextLine().trim();
        }
    GithubRepoPageProcessor pageProcessor = new GithubRepoPageProcessor(false);
        Spider spider = Spider.create(pageProcessor).addUrl(url).thread(10);

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
        String ats = Main.readFileByLines(path);
        System.out.println("读取配置文件路径…………"+path);

        if (ats.length()>0) {
            String[] str = ats.split(",");
            pageProcessor.operators = str;
        }else {
            System.out.println("配置文件未读取!!!!!!!!!!...");
        }

        System.out.println("开启10个线程批量抓取email...网速快的话一分钟能抓100个email 地址……");
        System.out.println("开始批量抓取email...");

        spider.setExitWhenComplete(true);
        spider.run();


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




class GithubRepoPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(60000);
    private String regString = null;
    public boolean strict = false;
    public String[] operators;
    public int depth = 0;
    GithubRepoPageProcessor(boolean strict) {
        this.strict = strict;
    }

    @Override
    public void process(Page page) {

        if (regString == null) {
            regString = site.getDomain().replaceAll("\\.", "\\\\.");
//            regString = "^(http(s?):\\/\\/)?(" + regString + ")[^\\s]*";
            regString = regString + "[^\\s]*";
        }

        System.out.println("depth: " + depth);
        String emailRex = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

        Selectable selectablem = null;
        if (operators.length>0){
            String atRegex = "";
            if (operators.length == 1) {
                atRegex = escapeExprSpecialWord(operators[0]);
            }else {
                for (int i = 0; i < operators.length; i++) {
                    atRegex += "(" + operators[i] + ")";
                    if (i != operators.length - 1) {
                        atRegex += "|";
                    }
                }
            }
           selectablem = page.getHtml().replace(atRegex, "@");
        } else {
            selectablem = page.getHtml();
        }
//        String emailRex = "/[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?/";


//System.out.print(selectablem);
//        page.putField("email", page.getHtml().regex(emailRex));
//        String str = page.getHtml().replace("\\s\\*\\s|\\s\\*&nbsp;", "@").toString();

        Pattern p = Pattern.compile(emailRex);
        Matcher m = p.matcher(selectablem.toString());
        String resultEmails = "";

        int count = 0;
        HashSet set = new HashSet();
        while (m.find()) {
            if (count > 0) {
                resultEmails += "\n,";
            }
            count++;
            String str = m.group();
            if (!set.contains(str)) {
                resultEmails += m.group();
            }
        }

        page.putField("email", resultEmails);

        if (page.getResultItems().get("email") == null) {
            //skip this page
            page.getResultItems().setSkip(true);
            page.setSkip(true);
        } else {
            if (page.getRequest().getUrl().endsWith(".jpg")
                    || page.getRequest().getUrl().endsWith(".jpeg")
                    || page.getRequest().getUrl().endsWith(".pdf") ) {
                page.setSkip(true);
                page.getResultItems().setSkip(true);
            }

            if (this.strict) {
                // 控制深度为1层
                if (depth == 0) {
                    depth++;
                    for (String url : page.getHtml().links().all()) {
                        Request request = new Request(url).setPriority(1).putExtra("index", Integer.valueOf(1));
                        page.addTargetRequest(request);
                    }
                }
            } else {

//                page.addTargetRequests(page.getHtml().links().regex(regString).all());
                page.addTargetRequests(page.getHtml().links().all());
            }

//            if (this.strict) {
//
//                page.addTargetRequests(page.getHtml().links().regex(regString).all());
//            } else {
////            count++;
////            System.out.println("=---------" + count);
//                page.addTargetRequests(page.getHtml().links().all());
//            }

        }


        // 部分三：从页面发现后续的url地址来抓取
//        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
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


    @Override
    public Site getSite() {
        return site;
    }
}



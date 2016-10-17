package com.company;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.thread.CountableThreadPool;
import us.codecraft.webmagic.utils.FilePersistentBase;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

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
        System.out.println("开启10个线程批量抓取email...网速快的话一分钟能抓100个email 地址……");
        System.out.println("开始批量抓取email...");

        spider.setExitWhenComplete(true);
        spider.run();


    }
}

class GithubRepoPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(60000);
    private String regString = null;
    public boolean strict = false;
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

        page.putField("email", page.getHtml().regex("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*(@|(at)|\\[at\\])(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?"));
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



    @Override
    public Site getSite() {
        return site;
    }
}



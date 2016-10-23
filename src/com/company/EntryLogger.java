package com.company;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by jason on 23/10/2016.
 */
public class EntryLogger {

    public static void  logWelcome() throws InterruptedException {

        System.out.println("***********************************************");
        Thread.sleep(100);
        System.out.println("*                                             *");
        Thread.sleep(100);
        System.out.println("*           欢迎您 使用网站信息提取系统          *");
        Thread.sleep(100);
        System.out.println("*                                             *");
        Thread.sleep(100);
        System.out.println("*            Powered By Jason Zhou            *");
        Thread.sleep(100);
        System.out.println("*                                             *");
        Thread.sleep(100);
        System.out.println("***********************************************");

    }

    public static String logFuction(Scanner scanner) {

        System.out.println();
        System.out.println("请选择需要的功能");
        System.out.println("1 网站关键词杀手(帮助获取site map)");
        System.out.println("2 超级邮箱检索器");
        String entry =  scanner.nextLine();
        while (!entry.equals("1") && !entry.equals("2")){
            System.out.println("输入 1 或 2!");
            entry = EntryLogger.logFuction(scanner);
        }

        return entry;
    }

    public static void enterFunction(String string) {

        System.out.println();
        if (string.equals("1")) {
            System.out.println("进入网站关键词杀手(帮助获取site map)");
        }else {
            System.out.println("进入超级邮箱检索器");
        }
    }

    public static void testFunction() {

        ArrayList arrayList = new ArrayList(3);
        arrayList.add("chem");
        arrayList.add("go");
        arrayList.add("hh");

        String str = RegexGenerator.regexForArray(arrayList);
        System.out.println(str);


    }


}

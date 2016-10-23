package com.company;

import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 23/10/2016.
 */
class GithubRepoPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(60000);
    private String regString = null;
    public boolean strict = false;
    public String[] operators;
    public int depth = 0;
    public int maxDepth = 1;
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

//        System.out.print(page.getHtml().toString());

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

                    if (maxDepth == depth) {return;}

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


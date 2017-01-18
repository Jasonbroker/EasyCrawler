package Crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;

/**
 * Created by jason on 23/10/2016.
 */
public class KeywordFindingPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(60000);
    private String domain = null;

    private ArrayList keywords = null;
    private String regKeywords = "";

    public KeywordFindingPageProcessor(ArrayList keywords) {
        this.keywords = keywords;
        regKeywords = RegexGenerator.regexForArray(keywords);
    }

    @Override
    public void process(Page page) {
        if (domain == null) {
            domain = site.getDomain();
        }

        String url = page.getUrl().toString();
        System.out.println("current url: " + url);
        if (!url.contains(domain)) { return; }

        if (page.getUrl().regex(this.regKeywords).match()) {
            page.putField("url", url);
        } else  {
            //page.addTargetRequests(page.getHtml().links().all());
        }

        page.addTargetRequests(page.getHtml().links().all());


    }

    @Override
    public Site getSite() {
        return site;
    }
}

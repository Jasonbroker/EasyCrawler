package Helper;

import java.util.HashMap;

/**
 * Created by jason on 23/01/2017.
 */
public class UserConfigManager {

    public final String EMAIL_URL = "email_url";
    public final String WORK_MODE = "work_mode";
    public final String INDEXING_first = "indexing_first";
    public final String INDEXING_END = "indexing_end";
    public final String Crawl_speed = "crawl_speed";
    public final String Strict_mode = "strict_mode";
    public final String Prefix_space = "prefix_space";
    public final String Suffix_space = "suffix_space";
    public final String Replace_words = "replace_words";
    public final String Debug_mode = "debug_mode";

    // domain
    public final String DOMAIN_URL = "domain_url";
    public final String DOMAIN_KEYWORDS = "domain_kewords";

    // settings
    private final String DEFAULT_FOLDER = "default_folder";

    public String getEmail_url() {
        return email_url;
    }

    public void setEmail_url(String email_url) {
        this.email_url = email_url;
    }

    public int getWork_mode() {
        return work_mode;
    }

    public void setWork_mode(int work_mode) {
        this.work_mode = work_mode;
    }

    public int getIndex_start() {
        return index_start;
    }

    public void setIndex_start(int index_start) {
        this.index_start = index_start;
    }

    public int getIndex_end() {
        return index_end;
    }

    public void setIndex_end(int index_end) {
        this.index_end = index_end;
    }

    public int getCrawl_speed() {
        return crawl_speed;
    }

    public void setCrawl_speed(int crawl_speed) {
        this.crawl_speed = crawl_speed;
    }

    public boolean isStrict_mode() {
        return strict_mode;
    }

    public void setStrict_mode(boolean strict_mode) {
        this.strict_mode = strict_mode;
    }

    public boolean isPrefix_space() {
        return prefix_space;
    }

    public void setPrefix_space(boolean prefix_space) {
        this.prefix_space = prefix_space;
    }

    public boolean isSuffix_space() {
        return suffix_space;
    }

    public void setSuffix_space(boolean suffix_space) {
        this.suffix_space = suffix_space;
    }

    public String getReplace_words() {
        return replace_words;
    }

    public void setReplace_words(String replace_words) {
        this.replace_words = replace_words;
    }

    public boolean isDebug_mode() {
        return debug_mode;
    }

    public void setDebug_mode(boolean debug_mode) {
        this.debug_mode = debug_mode;
    }


    private String email_url = "";
    private int work_mode = 0;
    private int index_start = 0;
    private int index_end = 1000;
    private int crawl_speed = 10;
    private boolean strict_mode = true;
    private boolean prefix_space = false;
    private boolean suffix_space = false;
    private String replace_words = "";
    private boolean debug_mode = false;

    public String getDomain_url() {
        return domain_url;
    }

    public void setDomain_url(String domain_url) {
        this.domain_url = domain_url;
    }

    public String getDomain_keywords() {
        return domain_keywords;
    }

    public void setDomain_keywords(String domain_keywords) {
        this.domain_keywords = domain_keywords;
    }

    private String domain_url = "";
    private String domain_keywords = "";

    public String getDefault_folder() {
        return default_folder;
    }

    public void setDefault_folder(String default_folder) {
        this.default_folder = default_folder;
    }

    private String default_folder = FileHelper.desktopPathWithFileName("");

    public UserConfigManager() {

        HashMap map = FileHelper.readConfig();
        System.out.println(map);
        if (map != null) {
            try {
                this.email_url = map.get(EMAIL_URL).toString();

                this.work_mode = Integer.valueOf(map.get(WORK_MODE).toString());
                this.index_start = Integer.valueOf(map.get(INDEXING_first).toString());
                this.index_end = Integer.valueOf(map.get(INDEXING_END).toString());
                this.crawl_speed = Integer.valueOf(map.get(Crawl_speed).toString());
                this.strict_mode = Boolean.valueOf(map.get(Strict_mode).toString());
                this.prefix_space = Boolean.valueOf(map.get(Prefix_space).toString());
                this.suffix_space = Boolean.valueOf(map.get(Suffix_space).toString());
                this.replace_words = map.get(Replace_words).toString();
                this.debug_mode = Boolean.valueOf(map.get(Debug_mode).toString());

                this.domain_url = map.get(DOMAIN_URL).toString();
                this.domain_keywords = map.get(DOMAIN_KEYWORDS).toString();

                this.default_folder = map.get(DEFAULT_FOLDER).toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void synchronize() {

        HashMap hashMap = new HashMap(12);
        hashMap.put(EMAIL_URL, email_url);
        hashMap.put(WORK_MODE, "" + this.work_mode);
        hashMap.put(INDEXING_first, "" + this.index_start);
        hashMap.put(INDEXING_END, "" + this.index_end);
        hashMap.put(Crawl_speed, "" + this.crawl_speed);
        hashMap.put(Strict_mode, "" + this.strict_mode);
        hashMap.put(Prefix_space, "" + this.prefix_space);
        hashMap.put(Suffix_space, "" + this.suffix_space);
        hashMap.put(Replace_words, "" + this.replace_words);
        hashMap.put(Debug_mode, "" + this.debug_mode);

        hashMap.put(DOMAIN_URL, this.domain_url);
        hashMap.put(DOMAIN_KEYWORDS, this.domain_keywords);

        hashMap.put(DEFAULT_FOLDER, this.default_folder);

        FileHelper.saveUserConfig(hashMap);


    }

}

package Crawler;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by jason on 23/10/2016.
 */
public class KeywordPipline implements Pipeline {


    private String path = "";

    HashSet<String> results = new HashSet();

    public KeywordPipline(String piplinePath) {

        this.path = piplinePath;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        PrintWriter printWriter = null;
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            Object value = entry.getValue();
            String result = value.toString();

            if (result != null) {

                String url = resultItems.getRequest().getUrl();
                System.out.println("Jason get page: " + url );
                if (!this.results.contains(result)) {
                    this.results.add(result);
                    try {
                        printWriter = new PrintWriter(new FileWriter(this.path, true));
                        printWriter.println(result);

                    } catch (IOException e) {
                        System.out.println("write file error" + e.getMessage());
                    }
                }

            }

            if (printWriter != null) {
                printWriter.flush();
            }
        }
    }
}

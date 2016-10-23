package com.company;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
/**
 * Created by jason on 14/10/2016.
 */

public class PendingPipline implements Pipeline {

    private String path = "";

    HashSet<String> emails = new HashSet();

    private  boolean enableStrictMode = false;

    PendingPipline(String piplinePath, boolean enableStrictMode) {

        this.path = piplinePath;
        this.enableStrictMode = enableStrictMode;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        PrintWriter printWriter = null;
         if (resultItems.isSkip()) {
                System.out.println("bad url return!");
                return;
            }

                for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                    Object value = entry.getValue();
                    String email = value.toString();

                    if (email != null) {
                        if (email.contains("/")){
                           int index = email.lastIndexOf("/");
                            email = email.substring(index+1);
                        }

                        String url = resultItems.getRequest().getUrl();
                        System.out.println("get page: " + url );
                        System.out.println(entry.getKey() + ":\t" + email);
                        if (!this.emails.contains(email)) {
                            this.emails.add(email);
                            try {
                                printWriter = new PrintWriter(new FileWriter(this.path, true));
                                printWriter.println(url + "," + email);

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
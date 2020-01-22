package com.conceptualGraph.controller.threads;

import com.conceptualGraph.controller.Interrogator;

import java.util.ArrayList;

public class SelectLinksThread extends Thread{
    String pageTitle;

    public SelectLinksThread(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    public void run() {
        ArrayList<String> links = Interrogator.wikiAPISelectLinks(pageTitle);
        for (String link:links){
            System.out.println(link);
        }
    }
}

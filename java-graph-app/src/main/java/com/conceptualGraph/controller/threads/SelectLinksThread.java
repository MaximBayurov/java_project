package com.conceptualGraph.controller.threads;

import com.conceptualGraph.controller.Interrogator;

import java.util.ArrayList;

public class SelectLinksThread extends Thread{
    String pageTitle;
    ArrayList<String> links;
    long articleID;

    public SelectLinksThread(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public SelectLinksThread(String pageTitle, long articleID){
        this.pageTitle=pageTitle;
        this.articleID = articleID;
    }

    @Override
    public void run() {
        links = Interrogator.wikiAPISelectLinks(pageTitle);
    }

    public long getArticleID() {
        return articleID;
    }

    public ArrayList<String> getLinks() {
        return links;
    }
}

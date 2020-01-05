package com.conceptualGraph.controller;

import java.util.ArrayList;

public class selectLinksThread extends Thread{
    String pageTitle;

    public selectLinksThread(String pageTitle) {
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

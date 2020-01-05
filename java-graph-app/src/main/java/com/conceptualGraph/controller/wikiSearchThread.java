package com.conceptualGraph.controller;

import java.io.IOException;

public class wikiSearchThread extends Thread {
    String word;
    String link;

    wikiSearchThread(String word){
        this.word = word;
    }
    @Override
    public void run() {
        try {
//            System.out.println(Interrogator.wikiOpenSearch(word).getJSONArray(3).get(1));
            link = Interrogator.wikiOpenSearch(word).getJSONArray(3).get(1).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getWordWithLink(){
        return new String[]{word,link};
    }
}

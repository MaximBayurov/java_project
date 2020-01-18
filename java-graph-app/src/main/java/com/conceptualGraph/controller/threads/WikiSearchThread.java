package com.conceptualGraph.controller.threads;

import com.conceptualGraph.controller.Interrogator;
import org.json.JSONArray;

import java.io.IOException;

public class WikiSearchThread extends Thread {
    String word;
    String link;

    public WikiSearchThread(String word){
        this.word = word;
    }
    @Override
    public void run() {
        try {
            JSONArray openSearchResults = Interrogator.wikiOpenSearch(word);
            if(!openSearchResults.getJSONArray(3).isNull(1)){
                link = openSearchResults.getJSONArray(3).get(1).toString();
            }else {
                link = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getWordWithLink(){
        return new String[]{word,link};
    }

    public String getLink() {
        return link;
    }
}

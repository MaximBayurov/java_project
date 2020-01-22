package com.conceptualGraph.controller.threads;

import com.conceptualGraph.controller.Interrogator;
import org.json.JSONArray;

import java.io.IOException;

public class WikiSearchThread extends Thread {
    String word;
    String link;
    int wordsCount;

    public WikiSearchThread(String word){
        this.word = word;
        wordsCount=0;
    }
    @Override
    public void run() {
        try {
            JSONArray openSearchResults = Interrogator.wikiOpenSearch(word);
            if(!openSearchResults.getJSONArray(3).isNull(1)){
                link = openSearchResults.getJSONArray(3).get(1).toString();
                wordsCount = Interrogator.countWords(link);
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

    public int getWordsCount() {
        return wordsCount;
    }
}

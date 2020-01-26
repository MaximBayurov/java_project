package com.conceptualGraph.controller.threads;

import com.conceptualGraph.controller.Interrogator;
import org.json.JSONArray;

import java.io.IOException;
import java.net.ConnectException;

public class WikiSearchThread extends Thread {
    String word;
    String link;
    int wordsCount;

    public WikiSearchThread(String word){
        this.word = word;
        this.link = null;
        wordsCount=0;
    }
    @Override
    public void run() {
        try {
            JSONArray openSearchResults = Interrogator.wikiOpenSearch(word);
            if(!openSearchResults.getJSONArray(3).isNull(1)){
                link = openSearchResults.getJSONArray(1).get(0).toString();
                wordsCount = Interrogator.countWords(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("I'm shitted on this word: "+word);
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

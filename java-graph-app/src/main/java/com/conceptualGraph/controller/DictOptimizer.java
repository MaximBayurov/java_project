package com.conceptualGraph.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class DictOptimizer {
    private String getPage(String pageURL) throws IOException {
        Document doc = Jsoup.connect(pageURL).get();
        String docText = doc.select("" +
                "div#mw-content-text > div.mw-parser-output > p," +
                " div#mw-content-text > div.mw-parser-output > table").text();
        return docText;
    }

    public void makeUpMap(){
        HashMap<String, Integer> dictMap = new HashMap<String, Integer>();
        try{
            String pageText = getPage("https://ru.wikipedia.org/wiki/Россия");
            Scanner scanner = new Scanner(pageText);
            String word="";
            while (scanner.hasNext()){
                word=scanner.next();
                String stemmedWord = Stemmer.stem(WordChecker.bringTo(word));
                System.out.println(stemmedWord+" | "+word);
                if (!stemmedWord.equals("")){
                    if (!dictMap.containsKey(stemmedWord)){
                        dictMap.put(stemmedWord,1);
                    } else{
                        dictMap.put(stemmedWord, dictMap.get(stemmedWord)+1);
                    }
                }
            }
            System.out.println(dictMap);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    };

}

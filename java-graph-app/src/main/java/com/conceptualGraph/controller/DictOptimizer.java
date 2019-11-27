package com.conceptualGraph.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class DictOptimizer {

    private HashMap<String, Integer> dictMap;


    public DictOptimizer(){
        dictMap = new HashMap<String, Integer>();
    }

    private String getPage(String pageURL) throws IOException {
        Document doc = Jsoup.connect(pageURL).get();
        String docText = doc.select("" +
                "div#mw-content-text > div.mw-parser-output > p," +
                " div#mw-content-text > div.mw-parser-output > table").text();
        return docText;
    }

    public void makeUpMap(String pageURL){
        try{
            String pageText = getPage(pageURL);
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
            System.out.println("Страница: "+pageURL+" прочитана.");
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public List sortMap() {
        List list = new ArrayList(dictMap.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.parseInt(o2.toString().replaceAll("[^0-9]",""))
                        -Integer.parseInt(o1.toString().replaceAll("[^0-9]",""));
            }
        });
        System.out.println(list);
        return list;
    }

}

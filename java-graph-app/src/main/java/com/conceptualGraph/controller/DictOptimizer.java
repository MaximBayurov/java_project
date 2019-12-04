package com.conceptualGraph.controller;

import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class DictOptimizer {

    private HashMap<String, Integer> dictMap;


    public DictOptimizer(){
        dictMap = new HashMap<String, Integer>();
        PreChecker.readDicts();
    }

    private String getPage(String pageURL) throws IOException, HttpStatusException {
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
                String stemmedWord = Stemmer.stem(bringTo(word));
                if (!stemmedWord.equals("")&&!PreChecker.checkContent(stemmedWord,1)){
                    System.out.println(stemmedWord+" | "+word);
                    if (!dictMap.containsKey(stemmedWord)){
                        dictMap.put(stemmedWord,1);
                    } else{
                        dictMap.put(stemmedWord, dictMap.get(stemmedWord)+1);
                    }
                }
            }
            System.out.println("Страница: "+pageURL+" прочитана.");
        }catch (HttpStatusException HttpEx){
            System.out.println("Нет такой страницы: "+pageURL);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private String bringTo(String word) {
        return word.toLowerCase().replaceAll("[^a-zа-яё\\-/ ]","");
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

    public int[] getIDs(String requestURL) throws IOException{
        int[] IDs;
        System.out.println(Interrogator.readJsonFromUrl(requestURL));
        return new int[]{1,2,3,4};
    }
}

package com.conceptualGraph.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DictOptimizer {

    private HashMap<String, Integer> dictMap;


    public DictOptimizer(){
        dictMap = new HashMap<String, Integer>();
        PreChecker.readDicts();
    }

    private Elements getPage(String pageURL) throws IOException, HttpStatusException {
        Document doc = Jsoup.connect(pageURL).get();
        Elements docText = doc.select("" +
                "div#mw-content-text > div.mw-parser-output > p," +
                " div#mw-content-text > div.mw-parser-output > table");
        return docText;
    }

    public void makeUpMap(String pageURL){
        try{
            Elements pageText = getPage(pageURL);
            for (Element paragraph: pageText){
                String[] sentences = paragraph.text().split("(?<![\\. ][A-ZА-ЯЁ])[\\.\\?\\;\\!]");
                for (String sentence: sentences){
                    sentence= sentence.toLowerCase().replaceAll("[^a-zа-яё\\-/ ]","")
                            .replaceAll("^-| -|- ", " ").replaceAll(" +"," ");
                    String[] words = sentence.trim().split(" ");
                    for (String word: words){
                        String stemmedWord = Stemmer.stem(word);
                        if (!stemmedWord.equals("")&&!PreChecker.checkContent(word,1)){
                            System.out.println(stemmedWord+" | "+word);
                            if (!dictMap.containsKey(stemmedWord)){
                                dictMap.put(stemmedWord,1);
                            } else{
                                dictMap.put(stemmedWord, dictMap.get(stemmedWord)+1);
                            }
                        }
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

    public ArrayList<Integer> getIDs(String requestURL) throws IOException{
        ArrayList<Integer> IDs = new ArrayList<Integer>();
        Connection connection= Jsoup.connect(requestURL).ignoreContentType(true);
        Connection.Response response = connection.execute();
        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray randArticles = jsonObject.getJSONObject("query").getJSONArray("random");
        for (Object randArticle: randArticles){
            Integer id = (Integer)((JSONObject) randArticle).get("id");
            IDs.add(id);
        }
        return IDs;
    }
}

package com.conceptualGraph.controller;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.util.ArrayList;


public class Interrogator {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException{
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public void wikiOpenSearch(String searchWord){
        String HTTPRequest = "https://ru.wikipedia.org/w/api.php?action=opensearch&search="+searchWord+"&format=json";
        try{
            JSONArray jo = readJsonFromUrl(HTTPRequest);
            System.out.println(jo.getJSONArray(1).toString());
            System.out.println(jo.getJSONArray(2).toString());
            System.out.println(jo.getJSONArray(3).toString()); //массив с ссылками, из него нужно будет убрать все пробелы
//            selectLinks(jo.getJSONArray(3).getString(1));
//            System.out.println(countWords(searchWord));
        }catch (Exception ex){
            System.out.println("Неудалось получить результаты запроса: " + HTTPRequest);
            ex.printStackTrace();
        }
    }

    private int countWords(String searchWord) {
        try{
            URL url = new URL("https://ru.wikipedia.org/w/" +
                    "api.php?action=query&list=search&srwhat=nearmatch&srsearch="+searchWord+"&format=json");
            InputStream is = url.openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject jo = new JSONObject(jsonText);
            return (int)jo.getJSONObject("query").getJSONArray("search").getJSONObject(0).get("wordcount");
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void selectLinks(String pageLink){
        try{
            Document doc = Jsoup.connect(pageLink).get();
            Elements links = doc.select(
                    "div#mw-content-text > div.mw-parser-output > p a," +
                            " div#mw-content-text > div.mw-parser-output > table a ");

            for (Element link : links){
                if (link.attr("href").contains("wiki")){
                    System.out.print(link.attr("title") + " | ");
                    System.out.println(link.attr("href"));
                }
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

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

    /**
     * Возвращает массивы [0] названий статей, [1] краткое описание и [2]ссылки
     * в Wikipedia по searchWord'у
     * @param searchWord - искомое слово в Wiki
     * @return JSONArray
     * @throws IOException в случае неудачного соединения с Wiki
     */
    public static JSONArray wikiOpenSearch(String searchWord) throws IOException{
        String HTTPRequest = "https://ru.wikipedia.org/w/api.php?action=opensearch&search="+searchWord+"&format=json";
//        System.out.println(HTTPRequest);
        JSONArray jo = readJsonFromUrl(HTTPRequest);

//        System.out.println(jo.getJSONArray(3).get(0).toString());

//        System.out.println(jo.getJSONArray(1).toString());
//        System.out.println(jo.getJSONArray(2).toString());
//        System.out.println(jo.getJSONArray(3).toString()); //массив с ссылками, из него нужно будет убрать все пробелы
        return jo;
    }

    private static int countWords(String searchWord) {
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

    public static Elements selectLinks(String pageLink) throws IOException{
        Document doc = Jsoup.connect(pageLink).get();
        Elements links = doc.select(
                "div#mw-content-text > div.mw-parser-output > p a," +
                        " div#mw-content-text > div.mw-parser-output > table a ");

    //            for (Element link : links){
    //                if (link.attr("href").contains("wiki")){
    //                    System.out.print(link.attr("title") + " | ");
    //                    System.out.println(link.attr("href"));
    //                }
    //            }
        return links;
    }
}

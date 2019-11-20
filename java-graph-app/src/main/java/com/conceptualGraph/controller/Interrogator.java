package com.conceptualGraph.controller;


import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;



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

    public void getPage(String searchWord){
        String HTTPRequest = "https://ru.wikipedia.org/w/api.php?action=opensearch&search="+searchWord+"&format=json";
        try{
            JSONArray jo = readJsonFromUrl(HTTPRequest);
            System.out.println(jo.getJSONArray(1).toString());
            System.out.println(jo.getJSONArray(2).toString());
            System.out.println(jo.getJSONArray(3).toString()); //массив с ссылками, из него нужно будет убрать все пробелы
        }catch (Exception ex){
            System.out.println("Неудалось получить результаты запроса: " + HTTPRequest);
            ex.printStackTrace();
        }
    }
}

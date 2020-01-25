package com.conceptualGraph.controller;


import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;


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
            String HTTPRequest = "https://ru.wikipedia.org/w/api.php?action=opensearch&search=" + searchWord + "&format=json";
            JSONArray jo = readJsonFromUrl(HTTPRequest);
            return jo;
    }

    public static int countWords(String searchWord) {
        try{
            URL url = new URL("https://ru.wikipedia.org/w/" +
                    "api.php?action=query&list=search&srwhat=nearmatch&srsearch="+searchWord.replace(" ", "_")+"&format=json");
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

    public static ArrayList<String> wikiAPISelectLinks(String pageTitle){
        try{
            ArrayList<String> correctLinks = new ArrayList<>();
            String url = "https://ru.wikipedia.org/w/api.php?action=parse&page="+pageTitle.replace(" ", "_")+"&prop=links&format=json";
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray links = jsonObject.getJSONObject("parse").getJSONArray("links");
            Iterator linksIterator = links.iterator();
            while(linksIterator.hasNext()){
                JSONObject link = (JSONObject) linksIterator.next();
                if (link.has("exists")&&link.get("ns").equals(0)) correctLinks.add(link.get("*").toString());
            }
            return correctLinks;
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static Elements selectLinks(String pageLink) throws IOException{
        Document doc = Jsoup.connect(pageLink).get();
        Elements links = doc.select(
                "div#mw-content-text > div.mw-parser-output > p a," +
                        " div#mw-content-text > div.mw-parser-output > table a ");
        for (int i=0;i<links.size(); i++){
            String href = links.get(i).attr("href");
            String title = links.get(i).attr("title");
            if (
                    title.equals("") //пустышки
                            || href.matches("^http.*") //ссылка не внутренняя или неправильная
                            || href.matches(".*?redlink=1.*")
                            || title.matches("^.*?(?:В|в)икипедия.*$")
                            || href.matches(".*?[.](jpg|ogg|svg|png|gif|bmp|jpeg)$")  //картинки флагов/карты
                            || title.matches("^[0-9]{0,4}(?:(?:-е|-ый|-й)? годы?|(?: до н[.]э[.]| до нашей эры).*|)$")  //года -e годы просто 1999 до н.э.
                            || title.matches("^. \\((?:.*?ца|число)\\)") //кириллица/латиница/число
                            || title.matches("^[0-9]{0,2} (?:января|февраля|марта|апреля|мая|июня|июля|августа|сентября|октября|ноября|декабря)$") //месяц
                            || title.matches("^(Шаблон|Обсуждение|Категория|Служебная|Участник|Проект):.*")
                            || title.matches("(^UTC\\+.)")
            ){
                links.remove(i);
                i--;
            }
        }

        return links;
    }
}

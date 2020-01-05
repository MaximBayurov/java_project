package com.conceptualGraph.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class InterrogatorTest {




    @Test
    void selectLinks() throws IOException{
//        try{
//            Elements testLinks = Interrogator.selectLinks("https://ru.wikipedia.org/wiki/%D0%9C%D0%BE%D1%81%D0%BA%D0%B2%D0%B0");
//
//            System.out.println("Выбираемые ссылки:");
//            for (Element link:testLinks){
//                String href = link.attr("href");
//                String title = link.attr("title");
//                if (    title.equals("") //пустышки
//                                || href.matches("^http.*") //ссылка не внутренняя или неправильная
//                                || href.matches(".*?redlink=1.*")
//                                || title.matches("^.*?(?:В|в)икипедия.*$")
//                                || href.matches(".*?[.](jpg|ogg|svg|png|gif|bmp|jpeg)$")  //картинки флагов/карты
//                                || title.matches("^[0-9]{0,4}(?:(?:-е|-ый|-й)? годы?|(?: до н[.]э[.]| до нашей эры).*|)$")  //года -e годы просто 1999 до н.э.
//                                || title.matches("^. \\((?:.*?ца|число)\\)") //кириллица/латиница/число
//                                || title.matches("^[0-9]{0,2} (?:января|февраля|марта|апреля|мая|июня|июля|августа|сентября|октября|ноября|декабря)$") //месяц
//                                || title.matches("^(Шаблон|Обсуждение|UTC.*|Категория|Служебная|Участник|Проект):.*")
//                                || title.matches("(^UTC\\+.)")){
//                    System.out.println("Ошибочная ссылка: \n" + href + " | " + title);
//                    Assert.fail("Выбрана несоответствующая ссылка!");
//                }else System.out.println(href + " | " + title);
//            }
//
//            Assert.assertTrue(true);
//            } catch (IOException ex){
//            Assert.fail("Невозможно получить документ по ссылке (установить соединение)");
//        }
        String url = "https://ru.wikipedia.org/w/api.php?action=parse&pageid=123&prop=links&format=json";
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
            if (link.has("exists")&&link.get("ns").equals(0)) System.out.println(link);
        }
    }

    @Test
    void wikiOpenSearch() throws IOException, InterruptedException {
        List<String> links = Collections.synchronizedList(new ArrayList<String>());
        ArrayList<String> words = new ArrayList<>();
        for (int i=0;i<10;i++){
            words.add("Москва");
            words.add("подарок");
            words.add("фотография");
            words.add("мать");
            words.add("душа");
            words.add("язык");
            words.add("писатель");
            words.add("сомнение");
            words.add("работа");
            words.add("журналист");
        }


//        long start = System.nanoTime();
//        for (String word:words){
//            links.add(Interrogator.wikiOpenSearch(word).getJSONArray(3).get(1).toString());
//        }
//        double end = Math.pow(10,-9)*(System.nanoTime() - start);
//        System.out.println(end);
//        //~9 sec


        long start1 = System.nanoTime();
        wikiSearchThread myThreads[] = new wikiSearchThread[words.size()];
        for (int j = 0; j < words.size(); j++) {
            myThreads[j] = new wikiSearchThread(words.get(j));
            myThreads[j].start();
        }
        for (int j = 0; j < words.size(); j++) {
            myThreads[j].join(); //todo add catch exception
        }
        for (int j=0; j< words.size(); j++){
            System.out.println("Слово: "+myThreads[j].getWordWithLink()[0]+"\nСсылка: "+myThreads[j].getWordWithLink()[1]+"\n");
        }
        double end1 = Math.pow(10,-9)*(System.nanoTime() - start1);
        System.out.println(end1);
        //~3 second
    }
}
package com.conceptualGraph.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InterrogatorTest {

    @Test
    void selectLinks() {
        try{
            Elements testLinks = Interrogator.selectLinks("https://ru.wikipedia.org/wiki/%D0%9C%D0%BE%D1%81%D0%BA%D0%B2%D0%B0");

            System.out.println("Выбираемые ссылки:");
            for (Element link:testLinks){
                String href = link.attr("href");
                String title = link.attr("title");
                if (    title.equals("") //пустышки
                                || href.matches("^http.*") //ссылка не внутренняя или неправильная
                                || href.matches(".*?redlink=1.*")
                                || title.matches("^.*?(?:В|в)икипедия.*$")
                                || href.matches(".*?[.](jpg|ogg|svg|png|gif|bmp|jpeg)$")  //картинки флагов/карты
                                || title.matches("^[0-9]{0,4}(?:(?:-е|-ый|-й)? годы?|(?: до н[.]э[.]| до нашей эры).*|)$")  //года -e годы просто 1999 до н.э.
                                || title.matches("^. \\((?:.*?ца|число)\\)") //кириллица/латиница/число
                                || title.matches("^[0-9]{0,2} (?:января|февраля|марта|апреля|мая|июня|июля|августа|сентября|октября|ноября|декабря)$") //месяц
                                || title.matches("^(Шаблон|Обсуждение|UTC.*|Категория|Служебная|Участник|Проект):.*")
                                || title.matches("(^UTC\\+.)")){
                    System.out.println("Ошибочная ссылка: \n" + href + " | " + title);
                    Assert.fail("Выбрана несоответствующая ссылка!");
                }else System.out.println(href + " | " + title);
            }

            Assert.assertTrue(true);
            } catch (IOException ex){
            Assert.fail("Невозможно получить документ по ссылке (установить соединение)");
        }
    }
}
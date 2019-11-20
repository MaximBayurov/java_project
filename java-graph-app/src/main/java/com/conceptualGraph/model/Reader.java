package com.conceptualGraph.model;

import com.conceptualGraph.controller.Stemmer;
import com.conceptualGraph.controller.WordChecker;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {
    private static ArrayList<String> dictionary = new ArrayList<>();

    public static void checkAndRead(File file){
//           if (file.toString().toLowerCase().endsWith("doc")){readDoc(file);};
           if (file.getName().toLowerCase().endsWith("docx")){readDocx(file);}
           if (file.getName().toLowerCase().endsWith("html")){
               try {
                   readHTML(file);
               } catch (Exception e) {
                   System.out.println(e.getMessage());
               }
           }
           if (file.getName().toLowerCase().endsWith("txt")){readTXT(file);}

    }

    //работает некорректно
    private static void readTXT(File chosenFile) {
        try{
            //readDict();
            BufferedReader bfr = new BufferedReader(new FileReader(chosenFile));
            BufferedWriter bfw = new BufferedWriter(new FileWriter("ReaderResults.txt"));
            int wordsNumber = 0;
            int countDictWords = 0;
            String line = bfr.readLine().trim();
            while (line != null){
                System.out.println("Начинается с " + wordsNumber + " слова|");
                line=line.trim();
                String[] words = line.split(" ");
                for (String word:words) {
                    word = word.replace(".","").trim();
                    if (dictionary.contains(word)) countDictWords++;
                }
                wordsNumber += words.length;
                line = bfr.readLine();
            }
            bfw.write("Количество слов в книге:" + wordsNumber + "\n");
            bfw.write("Количество идентифицированных слов:" + countDictWords + "\n");
            bfw.close();
            bfr.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + "\n");
        }
    }

//    private static void readDoc(File file) {
//
//    }

    private static void readHTML(File chosenFile) throws Exception {
        System.out.println("readHTML start");
        BufferedWriter bw = new BufferedWriter(new FileWriter("outputFromHTML.txt", true));
        Document doc = Jsoup.parse(chosenFile, "UTF-8");
        WordChecker.readFullDict();
//        Elements h1 = doc.getElementsByTag("h1");
//        Elements h2 = doc.getElementsByTag("h2");
//        Elements h3 = doc.getElementsByTag("h3");
//        Elements h4 = doc.getElementsByTag("h4");
//        Elements h5 = doc.getElementsByTag("h5");
//        Elements h6 = doc.getElementsByTag("h6");
        Elements h = doc.select("h1, h2, h3, h4, h5, h6");
        Elements text = doc.select("h1, h2, h3, h4, h5, h6, p");
        ArrayList<Integer> indexList = new ArrayList<>();
        int wordsNumber = 1;
        int countDictWords = 0;
        for (int i = 0; i < text.size(); i++) {
//            ПОКА НЕ УДАЛЯТЬ!!!
//            bw.write(text.get(i).text() + "\n");
//            bw.flush();
            boolean a = true;
            while (a) {
                for (int j = 0; j < h.size(); j++) {
                    if (h.get(j).text().equals(text.get(i).text())) {
                        indexList.add(i); // Будет запись не в коллекцию, а в БД
                        break;
                    }
                }

                a = false;
            }

            String[] sentences = text.get(i).text().split("\\.");
            for (String sentence: sentences) {
                String[] words = sentence.replace(".","").split(" ");
                for (String word:words){
                    word = WordChecker.bringTo(word);
                    if (word.equals(" ")||word.isEmpty()) continue;
                    else if (WordChecker.check(word)) countDictWords++;
                    wordsNumber++;
                }
            }
        }
        bw.close();
        System.out.println("readHTML end");
        System.out.println(indexList);
        System.out.println("Количество глав: " + h.size());
        System.out.println("Количество слов в книге: " + wordsNumber);
        System.out.println("Количество совпавших с словарём слов: " + countDictWords);
    }

    //Просто сигнатура
    private static void readDocx(File chosenFile) {
        try{
            WordChecker.readFullDict();
            InputStream inputStream = new FileInputStream(chosenFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            XWPFDocument exampleDoc = new XWPFDocument(bufferedInputStream);
            List<XWPFParagraph> paragraphs = exampleDoc.getParagraphs();
            int wordsNumber = 1;
            int countDictWords = 0;
            for (XWPFParagraph paragraph:paragraphs) {
                System.out.println("Начинается с " + wordsNumber + " слова|");
                String[] sentences = paragraph.getText().split("\\.");
                for (String sentence:sentences){
                    String[] words = sentence.replace(".","").split(" ");
                    for (String word:words){
                        word = WordChecker.bringTo(word);
                        if (word.isEmpty()) continue;
                        else if (WordChecker.check(word)) countDictWords++;
                        wordsNumber++;
                    }
                }
            }
            System.out.println("Количество слов в книге:" + wordsNumber );
            System.out.println("Количество совпавших с словарём слов:" + countDictWords);
            exampleDoc.close();
            System.out.println("Количество слов в книге: " + wordsNumber);
            System.out.println("Количество совпавших с словарём слов: " + countDictWords);
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + "\n");
        }

    }

}

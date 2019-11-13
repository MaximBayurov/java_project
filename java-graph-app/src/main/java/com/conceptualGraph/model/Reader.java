package com.conceptualGraph.model;

import com.conceptualGraph.controller.PartOfSpeechChecker;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {
    private static ArrayList<String> dictionary = new ArrayList<>();
    private static ArrayList<String> pronouns = new ArrayList<>();

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

    private static void readTXT(File chosenFile) {
        try{
            readDict();
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
                    if (PartOfSpeechChecker.isVerb(word)) countDictWords++;
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
//        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("filename.out")));
//        Document doc = Jsoup.parse(chosenFile,"UTF-8");
//        writer.print(doc.body().text());
//        writer.flush();
//        writer.close();
        System.out.println("readHTML start");
        BufferedWriter bw = new BufferedWriter(new FileWriter("outputFromHTML.txt", true));
        Document doc = Jsoup.parse(chosenFile, "UTF-8");
        Elements h1 = doc.getElementsByTag("h1");
        Elements h2 = doc.getElementsByTag("h2");
        Elements h3 = doc.getElementsByTag("h3");
        Elements h4 = doc.getElementsByTag("h4");
        Elements h5 = doc.getElementsByTag("h5");
        Elements h6 = doc.getElementsByTag("h6");
        Elements text = doc.select("h1, h2, h3, h4, h5, h6, p");
        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < text.size(); i++) {
            bw.write(text.get(i).text() + "\n");
            bw.flush();
            boolean a = true;
            while (a) {
                for (int j = 0; j < h1.size(); j++) {
                    if (h1.get(j).text().equals(text.get(i).text())) {
                        indexList.add(i + 1);
                        a = false;
                    }
                }
                for (int j = 0; j < h2.size(); j++) {
                    if (h2.get(j).text().equals(text.get(i).text())) {
                        indexList.add(i + 1);
                        a = false;
                    }
                }
                for (int j = 0; j < h3.size(); j++) {
                    if (h3.get(j).text().equals(text.get(i).text())) {
                        indexList.add(i + 1);
                        a = false;
                    }
                }
                for (int j = 0; j < h4.size(); j++) {
                    if (h4.get(j).text().equals(text.get(i).text())) {
                        indexList.add(i + 1);
                        a = false;
                    }
                }
                for (int j = 0; j < h5.size(); j++) {
                    if (h5.get(j).text().equals(text.get(i).text())) {
                        indexList.add(i + 1);
                        a = false;
                    }
                }
                for (int j = 0; j < h6.size(); j++) {
                    if (h6.get(j).text().equals(text.get(i).text())) {
                        indexList.add(i + 1);
                        a = false;
                    }
                }
                a = false;
            }
        }
        bw.close();
        System.out.println("readHTML end");
        System.out.println(indexList);
    }

    //Просто сигнатура
    private static void readDocx(File chosenFile) {
        try{
            readStemDict();
            readPronouns();
            readUnions();
            InputStream inputStream = new FileInputStream(chosenFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            XWPFDocument exampleDoc = new XWPFDocument(bufferedInputStream);
            BufferedWriter bfw = new BufferedWriter(new FileWriter("ReaderResults.txt"));
            List<XWPFParagraph> paragraphs = exampleDoc.getParagraphs();
            int wordsNumber = 1;
            int countDictWords = 0;
            for (XWPFParagraph paragraph:paragraphs) {
                System.out.println("Начинается с " + wordsNumber + " слова|");
                String[] sentences = paragraph.getText().split("\\.");
                for (String sentence:sentences){
                    String[] words = sentence.replace(".","").split(" ");
                    for (String word:words){
                        word = bringTo(word);
                        String stemedWord = PartOfSpeechChecker.stem(word);
                        if (dictionary.contains(stemedWord)) countDictWords++;
                        else if (PartOfSpeechChecker.isVerb(stemedWord)) countDictWords++;
                        else if (stemedWord.equals("")){
                            continue;
                        }else System.out.println(word + " | " + stemedWord);
                        wordsNumber++;
                    }
                }
            }
            bfw.write("Количество слов в книге:" + wordsNumber + "\n");
            bfw.write("Количество совпавших с словарём слов:" + countDictWords + "\n");
            bfw.close();
            exampleDoc.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + "\n");
        }

    }

    private static String bringTo(String word) {
        return word.toLowerCase().replaceAll("[^а-яё]","");
    }

    private static void readDict() {
        File dict = new File("word_rus.txt");
        try {
            String dictWord;
            FileReader dictReader = new FileReader(dict);
            BufferedReader bufferedDictReader = new BufferedReader(dictReader);
            dictWord = bufferedDictReader.readLine();
            while(dictWord != null){
                dictionary.add(dictWord);
                dictWord=bufferedDictReader.readLine();
            }
            System.out.println("Словарь прочитан!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("\nНеудалось открыть словарь!");
        }
    }

    public static void readStemDict(){
        File dict = new File("stemed_word_rus.txt");
        try {
            Scanner in = new Scanner(dict);
            dictionary.clear();
            while (in.hasNext()){
                if (!in.equals(' ')){
                    String dictWord =in.next();
                    dictionary.add(dictWord);
                }
            }
            System.out.println("Словарь прочитан!");
            in.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("\nНеудалось открыть словарь!");
        }
    }

    public static void stemTheDict(){
        File stemDict = new File("stemed_word_rus.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(stemDict));
            System.out.println("Начало стемминга!");
            readDict();
            for (String word : dictionary) {
                bw.write(PartOfSpeechChecker.stem(word)+" ");
            }
            System.out.println("Конец стемминга!");
            bw.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void readPronouns(){
        File pronounsFile = new File("pronouns");
        try {
            Scanner in = new Scanner(pronounsFile);
            pronouns.clear();
            while (in.hasNext()){
                if (!in.equals(' ')){
                    String pronoun =PartOfSpeechChecker.stem(bringTo(in.next()));
                    if (!pronouns.contains(pronoun)){
                        pronouns.add(pronoun);
                    };
                }
            }
            dictionary.addAll(pronouns);
            in.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    protected static void readUnions(){
        File unionsFile = new File("unions");
        ArrayList<String> unions = new ArrayList<String>();
        try {
            Scanner in = new Scanner(unionsFile);
            while (in.hasNext()){
                if (!in.equals(' ')){
                    String union = PartOfSpeechChecker.stem(bringTo(in.next()));
                    if (!unions.contains(union)) unions.add(union);
                }
            }
            dictionary.addAll(unions);
            in.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

}

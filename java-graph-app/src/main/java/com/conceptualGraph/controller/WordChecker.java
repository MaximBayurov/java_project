package com.conceptualGraph.controller;

import com.conceptualGraph.controller.threads.WikiSearchThread;
import com.conceptualGraph.dBServise.DBException;
import com.conceptualGraph.dBServise.DBService;
import com.conceptualGraph.dBServise.dao.StructureDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordChecker {

    public static DBService dbService = new DBService();
    public static int threadsLimit = 30;
    public static int threadsCount = 0;
    private static int sentenceNumber = 0;
    private static int paragraphNumber = 0;
    private static String[] potentialTerms =new String[threadsLimit];
    private static ArrayList<String> dictionary = new ArrayList<>();
    private static int[][] structure = new int[threadsLimit][3];
    private static final Pattern namesPattern = Pattern.compile("(?:[А-ЯЁA-Z][а-яa-zё]+(?:\\s|-|[.]|\\n)){2,}|(?:[А-ЯЁA-][.]\\s?(?:[А-ЯЁA-Я][.]?\\s?)?[А-ЯЁA-Z][а-яa-zё]+(?:-[А-ЯЁA-Z][а-яa-zё])*?)");

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


    public static void readFullDict() {
        stemTheDict();
        readStemDict();
    }

    /**
     * Проверяет содерижтся ли слово в словаре
     * @param word слово
     * @return
     */
    public static boolean check(String word) {
        String stemedWord = Stemmer.stem(word);
        if (dictionary.contains(stemedWord)) return true;
        else{
//            System.out.println(word + " | " + stemedWord); //слово, на которое стоит обратить внимание
            return false;
        }
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

    private static void stemTheDict(){
        File stemDict = new File("stemed_word_rus.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(stemDict));
            System.out.println("Начало стемминга!");
            readDict();
            for (String word : dictionary) {
                bw.write(Stemmer.stem(word)+" ");
            }
            System.out.println("Конец стемминга!");
            bw.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static int[] paragraphCheck(String paragraph, int countDictWords, int wordsNumber) throws InterruptedException {
        paragraphNumber++;
        String[] sentences = paragraph.split("(?<![A-ZА-ЯЁ])[\\.\\?\\;\\!]+");
        for (String sentence: sentences) {
            sentenceNumber++;
            dbService.addStructure(paragraphNumber,sentenceNumber);
            Boolean[] nameBools = findNames(sentence);
            sentence= sentence.toLowerCase().replaceAll("[^a-zа-яё\\-/ ]","")
                    .replaceAll("^-| -|- ", " ").replaceAll(" +"," ");
            String[] words = sentence.trim().split(" ");
            Boolean[] booleans = PreChecker.arrayCheck(words);
            for (int k=0; k<booleans.length; k++){
                if (nameBools[k]==true){
                    booleans[k]=true;
                }
            }
            String word;
            for (int j = 0; j<words.length; j++){
                word = words[j].trim();
                if (word.isEmpty()) {
                    continue;
                }
                if (booleans[j]){
                    countDictWords++;
                    continue;
                }else if (!check(word)) {
                    if (threadsCount<threadsLimit){
                        structure[threadsCount][0] = paragraphNumber;
                        structure[threadsCount][1] = sentenceNumber;
                        structure[threadsCount][2] = wordsNumber;
                        potentialTerms[threadsCount] = word;
                        dbService.insertTerm(word, wordsNumber, sentenceNumber);
                        countDictWords++;
                        threadsCount++;
                    }else{
                        WikiSearchThread[] wikiSearchThreads = new WikiSearchThread[threadsLimit];
                        for (int i = 0; i<threadsLimit; i++){
                            wikiSearchThreads[i] = new WikiSearchThread(potentialTerms[i]);
                            wikiSearchThreads[i].start();
                        }
                        for (int i = 0; i<threadsLimit; i++){
                            wikiSearchThreads[i].join();
                        }
                        for (int i =0; i<threadsLimit; i++){
                            dbService.insertWikiWord(potentialTerms[i], structure[i][2], structure[i][1], wikiSearchThreads[i].getLink());
                        }
                    }
                }
                wordsNumber++;
            }
        }

        return new int[]{countDictWords,wordsNumber};
    }

    public static Boolean[] findNames(String sentence){
        ArrayList<String> names = new ArrayList<>();
        Matcher matcher = namesPattern.matcher(sentence);
        while (matcher.find()){
            names.add(matcher.group().trim());
            /*Вставить имя в БД
            DataBase.insertTerm(word, 1, 1);*/
        }
        String[] words = sentence.trim().replaceAll(" +"," ").split(" ");
        Boolean[] booleans = new Boolean[words.length];
        for (int i=0; i < words.length; i++){
            String word = words[i];
            booleans[i] = false;
            for (String name: names){
                if (name.contains(word)){
                    booleans[i] = true;
                    break;
                }
            }
        }
        return booleans;
    }

}

package com.conceptualGraph.controller;

import com.conceptualGraph.controller.threads.SelectLinksThread;
import com.conceptualGraph.controller.threads.WikiSearchThread;
import com.conceptualGraph.dBServise.DBException;
import com.conceptualGraph.dBServise.DBService;
import com.conceptualGraph.dBServise.dao.StructureDAO;
import com.conceptualGraph.model.Reader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static long[][] structure = new long[threadsLimit][3];
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

    /**
     * Проверяет параграф и уве
     * @param paragraph - проверяемый параграф
     * @return массив int[countDictWords,wordsNumber] , где wordsNumber и countDictWords количество слов после проверки
     * (всего и совпавших со словарём соответственно)
     * @throws InterruptedException
     */
    public static void paragraphCheck(String paragraph){
        paragraphNumber++;
        String[] sentences = paragraph.split("(?<![A-ZА-ЯЁ])[\\.\\?\\;\\!]+");
        for (String sentence: sentences) {
            sentenceNumber++;
            dbService.addStructure(paragraphNumber,sentenceNumber);
//            Boolean[] nameBools = findNames(sentence);
            sentence = sentence.toLowerCase().replaceAll("[^a-zа-яё/ ]"," ")
                    .replaceAll("^-| -|- ", " ").replaceAll(" +"," ");
//            HashMap<Integer, Integer> names = new HashMap<Integer, Integer>();
//            names = findNames(sentence);
            String[] words = sentence.trim().split(" ");
            Boolean[] booleans = PreChecker.arrayCheck(words);
            String word;
            for (int j = 0; j<words.length; j++){
                word = words[j].trim();
                Reader.wordsNumber++;
                if (word.isEmpty()) {
                    continue;
                }
                /*if (names.containsKey(j)) {
                    int sizeOfName = names.get(j);
                    for (int n = 1; n<sizeOfName; n++) {
                        word = word + words[j+n].trim();
                    }
                    j = j + sizeOfName - 1;
                    if (threadsCount<threadsLimit){
                        structure[threadsCount][0] = paragraphNumber;
                        structure[threadsCount][1] = sentenceNumber;
                        structure[threadsCount][2] = wordsNumber;
                        potentialTerms[threadsCount] = Stemmer.stem(word);;
                        countDictWords++;
                        threadsCount++;
                    }else{
                        threadsRun();
                    }
                } else */
                if (booleans[j] || word.length()<=2){
                    continue;
                }else if (!check(word)) {
                    Reader.termsCount++;
                    if (threadsCount<threadsLimit-1){
                        structure[threadsCount][0] = paragraphNumber;
                        structure[threadsCount][1] = sentenceNumber;
                        structure[threadsCount][2] = Reader.wordsNumber;
                        potentialTerms[threadsCount] = Stemmer.stem(word);
                        threadsCount++;
                    }else{
                        structure[threadsCount][0] = paragraphNumber;
                        structure[threadsCount][1] = sentenceNumber;
                        structure[threadsCount][2] = Reader.wordsNumber;
                        potentialTerms[threadsCount] = Stemmer.stem(word);
                        threadsCount++;
                        threadsRun();
                    }
                }
            }
        }
    }

    public static void threadsRun(){
        try{
            WikiSearchThread[] wikiSearchThreads = new WikiSearchThread[threadsCount];
            SelectLinksThread[] selectLinksThreads = new SelectLinksThread[threadsCount];
            for (int i = 0; i<threadsCount; i++){
                wikiSearchThreads[i] = new WikiSearchThread(potentialTerms[i]);
                wikiSearchThreads[i].start();
            }
            for (int i = 0; i<threadsCount; i++){
                wikiSearchThreads[i].join();
            }

            for (int i =0; i<threadsCount; i++){
                String wordLink = wikiSearchThreads[i].getLink();
                Reader.termsWordsNumber += wikiSearchThreads[i].getWordsCount();
                long articleID = dbService.insertWikiWord(potentialTerms[i], structure[i][2], structure[i][1], wordLink);
                if (articleID>0){
                    selectLinksThreads[i] = new SelectLinksThread(wordLink, articleID);
                    selectLinksThreads[i].start();
                }
            }

            for (int i = 0; i<threadsCount; i++){
                if (selectLinksThreads[i]!=null){
                    selectLinksThreads[i].join();
                }
            }

            for (int i = 0; i<threadsCount; i++){
                if (selectLinksThreads[i]!=null&&selectLinksThreads[i].getLinks()!=null){
                    dbService.insertPageLinks(
                            selectLinksThreads[i].getArticleID(),
                            selectLinksThreads[i].getLinks()
                    );
                }
            }
            threadsCount=0;
        } catch (InterruptedException ex){
            threadsCount=0;
            ex.printStackTrace();
        }
    }

    public static HashMap findNames(String sentence){
        HashMap<Integer, Integer> names = new HashMap<Integer, Integer>();
        Matcher matcher = namesPattern.matcher(sentence);
        int lastPos = 0;
        int newPos;
        int sizeOfName;
        int sizeOfSubstring;
        while (matcher.find()){
//            names.add(matcher.group().trim());
            /*Вставить имя в БД
            DataBase.insertTerm(word, 1, 1);*/
            String matched = matcher.group();
            sizeOfName = matched.split(" |\\.").length;
            newPos = sentence.indexOf(matched, lastPos);
            sizeOfSubstring = sentence.substring(lastPos, newPos).split(" |\\.").length;
            names.put(sizeOfSubstring, sizeOfName);
            newPos = newPos + sizeOfName;
            lastPos = newPos;
        }
        return names;
    }

    public static double getConnections() {
        return dbService.getConnectionsNumber();
    }

    public static double getRefrences(){
        return dbService.getAllReferences();
    }

    public static double getAccuracy() {
        return Reader.termsCount - dbService.getUnknownTermsNumber();
    }


}

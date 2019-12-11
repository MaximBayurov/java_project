package com.conceptualGraph.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordChecker {

    private static ArrayList<String> dictionary = new ArrayList<>();
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
            System.out.println(word + " | " + stemedWord); //слово, на которое стоит обратить внимание
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

    public static int[] paragraphCheck(String paragraph, int countDictWords, int wordsNumber) {


        String[] sentences = paragraph.split("(?<![A-ZА-ЯЁ])[\\.\\?\\;\\!]+"); //(?<![\\. ][A-ZА-ЯЁ])[\\.\\?\\;\\!]
        for (String sentence: sentences) {
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
                }else if (check(word)) {
                    DataBase.insertTerm(word, 1, 1);
                    countDictWords++;
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
            for (String name: names){
                if (name.contains(word)){
                    booleans[i] = true;
                    break;
                }else booleans[i]=false;
            }
        }
        return booleans;
    }
}

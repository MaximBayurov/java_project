package com.conceptualGraph.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WordChecker {

    private static ArrayList<String> dictionary = new ArrayList<>();

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

//    public static void readPronouns(){
//        File pronounsFile = new File("pronouns");
//        ArrayList<String> pronouns = new ArrayList<>();
//        try {
//            Scanner in = new Scanner(pronounsFile);
//            while (in.hasNext()){
//                if (!in.equals(' ')){
//                    String pronoun = Stemmer.stem(bringTo(in.next()));
//                    if (!pronouns.contains(pronoun)){
//                        pronouns.add(pronoun);
//                    };
//                }
//            }
//            dictionary.addAll(pronouns);
//            in.close();
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }
//    }

//    protected static void readUnions(){
//                    File unionsFile = new File("unions");
//                    ArrayList<String> unions = new ArrayList<String>();
//                    try {
//                        Scanner in = new Scanner(unionsFile);
//                        while (in.hasNext()){
//                            if (!in.equals(' ')){
//                                String union = Stemmer.stem(bringTo(in.next()));
//                                if (!unions.contains(union)) unions.add(union);
//                            }
//            }
//            dictionary.addAll(unions);
//            in.close();
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }
//    }

    public static void readFullDict() {
        stemTheDict();
        readStemDict();
//        readPronouns();
//        readUnions();
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
        String[] sentences = paragraph.split("(?<![\\. ][A-ZА-ЯЁ])[\\.\\?\\;\\!]");
        for (String sentence: sentences) {
            System.out.println("START");
            sentence = sentence.toLowerCase().replaceAll("[^a-zа-яё\\-/ ]","")
                    .replaceAll("^-| -|- ", " ").replaceAll(" +"," ")
                    .trim();
            if (sentence.isEmpty()) continue;
            String[] words = sentence.split(" ");
            for (String str: words) {
                System.out.print(str + " ");
            }

            Boolean[] booleans = PreChecker.arrayCheck(words);
            String word;
            for (int i=0; i<words.length; i++) {
                System.out.println(booleans[i] + " " + words[i]);
            }
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
}

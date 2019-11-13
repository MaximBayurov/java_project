package com.conceptualGraph.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WordChecker {

    private static ArrayList<String> dictionary = new ArrayList<>();

    public static String bringTo(String word) {
        return word.toLowerCase().replaceAll("[^а-яё]","");
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

    public static void readPronouns(){
        File pronounsFile = new File("pronouns");
        ArrayList<String> pronouns = new ArrayList<>();
        try {
            Scanner in = new Scanner(pronounsFile);
            pronouns.clear();
            while (in.hasNext()){
                if (!in.equals(' ')){
                    String pronoun = Stemmer.stem(bringTo(in.next()));
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
                    String union = Stemmer.stem(bringTo(in.next()));
                    if (!unions.contains(union)) unions.add(union);
                }
            }
            dictionary.addAll(unions);
            in.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void readFullDict() {
        readStemDict();
        readPronouns();
        readUnions();
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
}

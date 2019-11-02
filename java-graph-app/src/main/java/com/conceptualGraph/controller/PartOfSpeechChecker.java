package com.conceptualGraph.controller;

public class PartOfSpeechChecker {
    public static boolean isVerb(String word){
        int wordsLength = word.length();
        if(wordsLength>=6 && (word.substring(wordsLength - 3, wordsLength).equals("тся")||
            word.substring(wordsLength - 4, wordsLength).equals("ться"))){
            return true;
        } else return false;
    }
}

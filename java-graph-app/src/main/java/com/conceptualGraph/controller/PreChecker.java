package com.conceptualGraph.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PreChecker {

    static private ArrayList<String>[] pronounsDicts;
    static private ArrayList<String>[] unionsDicts ;
    static private ArrayList<String>[] prepsDicts ;
    static private ArrayList<String>[] partsDicts ;


    protected static ArrayList<String>[] univDictRead(String dictPath) throws IOException{
        File dictFile = new File(dictPath);
        System.out.println("Чтение файла "+dictFile.getPath());
        ArrayList<String> addDict1 = new ArrayList<String>(); //состоящий из одного слова
        ArrayList<String> addDict2 = new ArrayList<String>(); //из двух
        ArrayList<String> addDict3 = new ArrayList<String>(); // из трёх
        Scanner in = new Scanner(dictFile);
        while (in.hasNextLine()){
            String dictWord =in.nextLine();
            if (dictWord.matches("[а-яё\\-/]+")){
                addDict1.add(dictWord);
            } else  if (dictWord.matches("[а-яё\\-/]+ [а-яё\\-/]+")){
                addDict2.add(dictWord);
            } else if (dictWord.matches("[а-яё\\-/]+ [а-яё\\-/]+ [а-яё\\-/]+")) {
                addDict3.add(dictWord);
            }
        }
        in.close();
        return new ArrayList[]{addDict1,addDict2,addDict3};
    }

    public static Boolean[] arrayCheck(String[] words) {
        Boolean[] booleans = new Boolean[words.length];
        try{
            pronounsDicts = univDictRead("pronouns");
            System.out.println(pronounsDicts[0]);
            System.out.println(pronounsDicts[1]);
            System.out.println(pronounsDicts[2]);
            unionsDicts = univDictRead("unions");
            System.out.println(unionsDicts[0]);
            System.out.println(unionsDicts[1]);
            System.out.println(unionsDicts[2]);
            prepsDicts = univDictRead("prepositions");
            System.out.println(prepsDicts[0]);
            System.out.println(prepsDicts[1]);
            System.out.println(prepsDicts[2]);
            partsDicts = univDictRead("particles");
            System.out.println(partsDicts[0]);
            System.out.println(partsDicts[1]);
            System.out.println(partsDicts[2]);
            String words3;
            String words2;
            String words1;
            for (int k=0; k<words.length; k++){
                words[k]=bringTo(words[k]);
            }
            for (String word: words) {
                System.out.print(word+" ");
            }
            System.out.println();
            if (words.length>=3) {
                for (int i = 0; i < words.length - 2; i++) {
                    words1 = words[i];
                    if (checkContent(words1,1)){
                        booleans[i]=true;
                        continue;
                    }
                    words2 = words[i] + " " + words[i + 1];
                    if (checkContent(words2,2)){
                        booleans[i]=true;
                        booleans[i+1]=true;
                        continue;
                    }
                    words3 = words[i] + " " + words[i + 1] + " " + words[i + 2];
                    if (checkContent(words3,3)) {
                        booleans[i] = true;
                        booleans[i+1] = true;
                        booleans[i+2] = true;
                        continue;
                    }
                    System.out.println( words3 + " | "+ words2 + " | " + words1);
                }
            }
            if (words.length>=2){
                words2=words[words.length-2]+" "+words[words.length-1];
                if (checkContent(words2,2)){
                    booleans[booleans.length-1] = true;
                    booleans[booleans.length-2] = true;
                }
                System.out.println(words2);
            }
            if (words.length>=1){
                words1=words[words.length-1];
                if (checkContent(words1,1)) booleans[booleans.length-1] = true;
                System.out.println(words1);
            }
            for (Boolean bool:booleans){
                System.out.print(bool+" ");
            }
            System.out.println();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return booleans;
    }

    /**
     * Метод проверяет содержится ли данные слова и словосочетания от одного до трёх слов в словарях
     * @param words - слова, которые нужно проверить на содержание в словарях
     * @param wordsCount - количество слов содержащихся в words (от 1 до 3)
     * @return true - если содержится, false - если нет
     */
    private static boolean checkContent(String words, int wordsCount) {
        if (pronounsDicts[wordsCount-1].contains(words)||partsDicts[wordsCount-1].contains(words)||
                unionsDicts[wordsCount-1].contains(words)||prepsDicts[wordsCount-1].contains(words)){
            return true;
        } else return false;
    }
    protected static String bringTo(String word){
        return word.toLowerCase().replaceAll("[^a-zа-яё]","");
    }
}

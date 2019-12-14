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
    static private Boolean dictsFlag = false;


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

    /**
     * Обязателен вызов метода readDicts, чтобы корректно проверялось совпадения
     * @param words - массив слов, который нужно проверить
     * @return booleans - массив Booleans, показывающий находится ли слово по такому же индексу в words
     * в малых словарях
     */
    public static Boolean[] arrayCheck(String[] words) {
        Boolean[] booleans = new Boolean[words.length];
        String words3;
        String words2;
        String words1;
        if (words.length>=3) {
            for (int i = 0; i < words.length - 2; i++) {
                words3 = words[i] + " " + words[i + 1] + " " + words[i + 2];
                if (checkContent(words3,3)) {
                    booleans[i] = true;
                    booleans[i+1] = true;
                    booleans[i+2] = true;
                    i+=2;
                    continue;
                }
                words2 = words[i] + " " + words[i + 1];
                if (checkContent(words2,2)){
                    booleans[i]=true;
                    booleans[i+1]=true;
                    i++;
                    continue;
                }
                words1 = words[i];
                if (checkContent(words1,1)){
                    booleans[i]=true;
                    continue;
                } else booleans[i] = false;
            }
        }
        if (words.length>=2){
            words2=words[words.length-2]+" "+words[words.length-1];
            System.out.println(words2);
            if (checkContent(words2,2)){
                booleans[booleans.length-1] = true;
                booleans[booleans.length-2] = true;
                return booleans;
            }else if (checkContent(words[words.length-2],1)){
                booleans[booleans.length-2] = true;
            }else
                booleans[booleans.length-2] = false;
        }
        if (words.length>=1){
            words1=words[words.length-1];
            System.out.println(words1);
            if (checkContent(words1,1)){
                booleans[booleans.length-1] = true;
            } else booleans[booleans.length-1] = false;
        }
        return booleans;
    }

    /**
     * Метод проверяет содержится ли данные слова и словосочетания от одного до трёх слов в словарях
     * @param words - слова, которые нужно проверить на содержание в словарях
     * @param wordsCount - количество слов содержащихся в words (от 1 до 3)
     * @return true - если содержится, false - если нет
     */
    public static boolean checkContent(String words, int wordsCount) {
        if (pronounsDicts[wordsCount-1].contains(words)||partsDicts[wordsCount-1].contains(words)||
                unionsDicts[wordsCount-1].contains(words)||prepsDicts[wordsCount-1].contains(words)){
            return true;
        } else return false;
    }
    public static void readDicts(){
        try{
            if (!dictsFlag){
                pronounsDicts = univDictRead("pronouns");
                unionsDicts = univDictRead("unions");
                prepsDicts = univDictRead("prepositions");
                partsDicts = univDictRead("particles");
                dictsFlag = true;
            } else System.out.println("Словари уже прочитаны!");
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

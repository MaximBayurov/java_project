package com.conceptualGraph;


import java.io.*;
import java.util.ArrayList;

public class BookGenerator {

    static File randBook;
    static ArrayList<String> dictionary=new ArrayList<String>();

    public static void main(String[] args) {
        generateBook();
    }

    public static void readDict() {
        File dict = new File("word_rus.txt");

        try {
            String dictWord;

            FileReader dictReader = new FileReader(dict);
            BufferedReader bufferedDictReader = new BufferedReader(dictReader);

            dictWord=bufferedDictReader.readLine();

            while(dictWord!=null){
                dictionary.add(dictWord);
                dictWord=bufferedDictReader.readLine();
            }

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("\nНеудалось открыть словарь!");
        }
    }

    public static void generateBook(){

        readDict();

        randBook = new File("RandomBook.txt");
        try {
            BufferedWriter buferedRandBookWriter= new BufferedWriter(new FileWriter(randBook));

            for (int stringCount=0;stringCount<500;stringCount++){
                buferedRandBookWriter.write(generateString());
            }

            buferedRandBookWriter.close();
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("\nНеудалось создать книгу из случайных слов словаря!");
        }

    }

    private static String generateString() {
        int wordsInString= rnd(10,20);
        String bookString="";

        for (int i=0; i<wordsInString; i++){
            bookString+=' '+dictionary.get((int)(dictionary.size()*Math.random()));
        }

        return bookString+".\n";
    }

    public static int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}

package com.conceptualGraph;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.*;
import org.jsoup.nodes.Document;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Reader {

   static ArrayList<String> dictionary=new ArrayList<String>();

    public static void checkAndRead(File file){
//           if (file.toString().toLowerCase().endsWith("doc")){readDoc(file);};
           if (file.getName().toLowerCase().endsWith("docx")){readDocx(file);};
           if (file.getName().toLowerCase().endsWith("html")){readHTML(file);};
           if (file.getName().toLowerCase().endsWith("txt")){readTXT(file);};

    }

    private static void readTXT(File chosenFile) {
        try{
            readDict();
            BufferedReader bfr=new BufferedReader(new FileReader(chosenFile));

            BufferedWriter bfw=new BufferedWriter(new FileWriter("ReaderResults.txt"));
            int wordsNumber=1;
            int countDictWords=0;

            String line= bfr.readLine();
            System.out.println(line);
            while (line!=null){
                System.out.println("Начинается с "+wordsNumber+" слова|");
                String words[]=line.split(" ");
                for (String word:words){
                    word=word.replace(".","").trim();

                    if (dictionary.contains(word)) countDictWords++;
                }
                wordsNumber+=words.length;
                line=bfr.readLine();
            }

            bfw.write("Количество слов в книге:"+wordsNumber+"\n");
            bfw.write("Количество совпавших с словарём слов:"+countDictWords+"\n");

            bfw.close();
            bfr.close();
        } catch (IOException exception) {
            System.err.println(exception.getMessage() + '\n');
            exception.printStackTrace();
        }
    }

    private static void readDoc(File file) {

    }

    private static void readHTML(File chosenFile) {

        try{
            PrintWriter writer= new PrintWriter(new BufferedWriter(new FileWriter("filename.out")));
            Document doc= Jsoup.parse(chosenFile,"UTF-8");
            writer.print(doc.body().text());
            writer.flush();
            writer.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    //Просто сигнатура
    private static void readDocx(File chosenFile) {
        try{
            readDict();
            InputStream inputStream=new FileInputStream(chosenFile);
            BufferedInputStream bufferedInputStream= new BufferedInputStream(inputStream);
            XWPFDocument exampleDoc= new XWPFDocument(bufferedInputStream);

            BufferedWriter bfw=new BufferedWriter(new FileWriter("ReaderResults.txt"));
            List<XWPFParagraph> paragraphs= exampleDoc.getParagraphs();
            int wordsNumber=1;
            int countDictWords=0;

            for (XWPFParagraph paragraph:paragraphs){
                System.out.println("Начинается с "+wordsNumber+" слова|");
                String words[]=paragraph.getText().split(" ");
                for (String word:words){
                    if (dictionary.contains(word)) countDictWords++;
                    wordsNumber++;
                }
            }

            bfw.write("Количество слов в книге:"+wordsNumber+"\n");
            bfw.write("Количество совпавших с словарём слов:"+countDictWords+"\n");

            bfw.close();
            exampleDoc.close();

        } catch (IOException exception) {
            System.err.println(exception.getMessage() + '\n');
            exception.printStackTrace();
        }
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

            System.out.println("Словарь прочитан!");
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("\nНеудалось открыть словарь!");
        }
    }
}

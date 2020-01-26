package com.conceptualGraph.model;

import com.conceptualGraph.controller.PreChecker;
import com.conceptualGraph.controller.WordChecker;
import com.conceptualGraph.dBServise.DBException;
import com.conceptualGraph.dBServise.DBService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class Reader {

    /**
     *
     * @param termsCount - количество слов книги, не имеющихся в словаре
     * @param wordsNumber - общее количество слов
     */

    private static ArrayList<String> dictionary = new ArrayList<>();
    public static long termsCount = 0;
    public static long wordsNumber = 1;
    public static long termsWordsNumber = 0;
    public static double[] checkAndRead(File file){
        try{

            WordChecker.dbService.dropDB();
            WordChecker.dbService.createDBTables();
            //           if (file.toString().toLowerCase().endsWith("doc")){readDoc(file);};
            if (file.getName().toLowerCase().endsWith("docx")){
                readDocx(file);
            }
            if (file.getName().toLowerCase().endsWith("html")){
                try {
                    readHTML(file);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (file.getName().toLowerCase().endsWith("txt")){readTXT(file);}
        } catch (DBException ex){
            ex.printStackTrace();
        }

        long allWordsNumber = termsWordsNumber + wordsNumber;
        double termsAverageDifficulty = WordChecker.getRefrences() / termsWordsNumber; /*будет очень меньше единицы, надо найти формулу
                                                                для выравнивания от 0 до 1*/
        double termConnection = WordChecker.getConnections() / termsCount; //
        double accuracy = WordChecker.getAccuracy();
        double bookAverageDifficulty = (termsCount / wordsNumber + termsAverageDifficulty) / 2;

        System.out.println(wordsNumber + " - количество всего слов в книге");
        System.out.println(termsCount + " - количество терминов в книге ");
        System.out.println(allWordsNumber + " - слов на всех страницах");
        System.out.println(termConnection + " - терминов, связаных между собой");
        System.out.println(WordChecker.getConnections() + " - терминов, связаных между собой");
        System.out.println(accuracy + " - точность");
        System.out.println(bookAverageDifficulty + " - средняя сложность книги");

        return new double[] {termsCount, wordsNumber, allWordsNumber / 120 / 60, termsAverageDifficulty, termConnection, bookAverageDifficulty, accuracy};
    }

    //работает некорректно
    private static void readTXT(File chosenFile) {
        try{
            //readDict();
            BufferedReader bfr = new BufferedReader(new FileReader(chosenFile));
            BufferedWriter bfw = new BufferedWriter(new FileWriter("ReaderResults.txt"));
            int wordsNumber = 0;
            int termsCount = 0;
            String line = bfr.readLine().trim();
            while (line != null){
                System.out.println("Начинается с " + wordsNumber + " слова|");
                line=line.trim();
                String[] words = line.split(" ");
                for (String word:words) {
                    word = word.replace(".","").trim();
                    if (dictionary.contains(word)) termsCount++;
                }
                wordsNumber += words.length;
                line = bfr.readLine();
            }
            bfw.write("Количество слов в книге:" + wordsNumber + "\n");
            bfw.write("Количество идентифицированных слов:" + termsCount + "\n");
            bfw.close();
            bfr.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + "\n");
        }
    }

//    private static void readDoc(File file) {
//
//    }

    private static void readHTML(File chosenFile) throws Exception {
        System.out.println("readHTML start");
        BufferedWriter bw = new BufferedWriter(new FileWriter("outputFromHTML.txt", true));
        Document doc = Jsoup.parse(chosenFile, "UTF-8");
        WordChecker.readFullDict();
//        Elements h1 = doc.getElementsByTag("h1");
//        Elements h2 = doc.getElementsByTag("h2");
//        Elements h3 = doc.getElementsByTag("h3");
//        Elements h4 = doc.getElementsByTag("h4");
//        Elements h5 = doc.getElementsByTag("h5");
//        Elements h6 = doc.getElementsByTag("h6");
        Elements h = doc.select("h1, h2, h3, h4, h5, h6");
        Elements text = doc.select("h1, h2, h3, h4, h5, h6, p");
        ArrayList<Integer> indexList = new ArrayList<>();
        PreChecker.readDicts();
        for (int i = 0; i < text.size(); i++) {
//            ПОКА НЕ УДАЛЯТЬ!!!
//            bw.write(text.get(i).text() + "\n");
//            bw.flush();
            boolean a = true;
            while (a) {
                for (int j = 0; j < h.size(); j++) {
                    if (h.get(j).text().equals(text.get(i).text())) {
                        indexList.add(i); // Будет запись не в коллекцию, а в БД
                        break;
                    }
                }

                a = false;
            }

            WordChecker.paragraphCheck(text.get(i).text());
        }
        if (WordChecker.threadsCount!=0){
            WordChecker.threadsRun();

        }
        bw.close();
        System.out.println("readHTML end");
        System.out.println(indexList);
        System.out.println("Количество глав: " + h.size());
        System.out.println("Количество слов в книге: " + wordsNumber);
        System.out.println("Количество терминов: " + termsCount);
    }


    private static void readDocx(File chosenFile){
        try{
            WordChecker.readFullDict();
            InputStream inputStream = new FileInputStream(chosenFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            XWPFDocument exampleDoc = new XWPFDocument(bufferedInputStream);
            List<XWPFParagraph> paragraphs = exampleDoc.getParagraphs();
            PreChecker.readDicts();
            for (XWPFParagraph paragraph:paragraphs) {
                WordChecker.paragraphCheck(paragraph.getText());
            }
            if (WordChecker.threadsCount!=0){
                WordChecker.threadsRun();

            }
            System.out.println("Количество слов в книге:" + wordsNumber );
            System.out.println("Количество терминов:" + termsCount);
            exampleDoc.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + "\n");
        }
    }

}

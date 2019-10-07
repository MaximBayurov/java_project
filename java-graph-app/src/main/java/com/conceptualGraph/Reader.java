package com.conceptualGraph;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;


import java.io.*;
import java.util.List;

public class Reader {

    public static void readHTML(File chosenFile) {

        try{
            PrintWriter writer= new PrintWriter(new BufferedWriter(new FileWriter("filename.out")));
            Document doc= Jsoup.parse(chosenFile,"UTF-8");
            writer.print(doc.body().text());
            writer.flush();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    //Просто сигнатура
    public static void readDoc(File chosenFile) {
        try{
            PrintWriter writer= new PrintWriter(new BufferedWriter(new FileWriter("filename.out")));

            writer.flush();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

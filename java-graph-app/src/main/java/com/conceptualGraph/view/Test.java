package com.conceptualGraph.view;

import com.conceptualGraph.dBServise.DBException;
import com.conceptualGraph.dBServise.DBService;
import com.conceptualGraph.dBServise.dataSets.WordsDataSet;

import java.util.List;


public class Test {
    public static void main(String[] args) {
        DBService dbService = new DBService();
        try{
            dbService.printAllArticles();
//            for (WordsDataSet word: words){
//                System.out.println(word);
//            }
        }catch (DBException e){
            e.printStackTrace();
        }
    }


}
package com.conceptualGraph.view;

import com.conceptualGraph.controller.WordChecker;
import com.conceptualGraph.dBServise.DBException;
import com.conceptualGraph.dBServise.DBService;
import com.conceptualGraph.dBServise.dao.PositionsDAO;
import com.conceptualGraph.dBServise.dao.ArticlesDAO;
import com.conceptualGraph.dBServise.dao.WordsDAO;
import com.conceptualGraph.dBServise.dataSets.ArticlesDataSet;
import com.conceptualGraph.dBServise.dataSets.WordsDataSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class Test {
    public static void main(String[] args) {
        DBService dbService = new DBService();
        try{
            List<WordsDataSet> words = dbService.getAllWords();
            for (WordsDataSet word: words){
                System.out.println(word);
            }
        }catch (DBException e){
            e.printStackTrace();
        }
    }


}
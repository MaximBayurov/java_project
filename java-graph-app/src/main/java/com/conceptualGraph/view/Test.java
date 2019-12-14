package com.conceptualGraph.view;

import com.conceptualGraph.controller.WordChecker;
import com.conceptualGraph.dBServise.DBException;
import com.conceptualGraph.dBServise.DBService;
import com.conceptualGraph.dBServise.dataSets.WordsDataSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class Test {
    public static void main(String[] args) {
        DBService dbService = new DBService();
        dbService.printConnectInfo();
        try {
            List<WordsDataSet> wordsDataSets = dbService.getAllMatches("penis");
            for (WordsDataSet wordsDataSet: wordsDataSets){
                System.out.println(wordsDataSet);
            }
        }catch (DBException dbEx){
            dbEx.printStackTrace();
        }
    }


}
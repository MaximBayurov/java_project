package com.conceptualGraph.view;

import com.conceptualGraph.controller.WordChecker;
import com.conceptualGraph.dBServise.DBService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Test {
    public static void main(String[] args) {
        DBService dbService = new DBService();
        dbService.printConnectInfo();
    }


}
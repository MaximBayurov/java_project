package com.conceptualGraph.view;

import com.conceptualGraph.controller.WordChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Test {
    public static void main(String[] args) {
        WordChecker.findNames("Максим Баюров предложил купить слона Никите Будкину за символическую сумму 1000$.");
    }


}
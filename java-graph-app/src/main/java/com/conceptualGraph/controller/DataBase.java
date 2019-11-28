package com.conceptualGraph.controller;

import org.h2.*;
import org.h2.Driver;
import org.json.JSONArray;

import java.sql.*;
import java.util.Collections;

public class DataBase {

    static Connection conn;

    public static void createDB() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:./db/test");
            Statement st = conn.createStatement();
            st.execute("DROP ALL OBJECTS");
            st.execute("CREATE TABLE `structure` (\n" +
                    "  `paragraph` int NOT NULL,\n" +
//                    "  `chapter` int NOT NULL,\n" + //глава, не понятно как взять
                    "  `sentence` int AUTO_INCREMENT PRIMARY KEY NOT NULL \n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `words` (\n" +
                    "  `id` int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "  `word` char NOT NULL,\n" +
                    "  `link` char NULL,\n" +
                    "  `article` char NULL\n" + // название статьи, пока не нужно
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `positions` (\n" +
                    "  `sentence` int PRIMARY KEY NOT NULL,\n" +
                    "  `id` int NOT NULL,\n" +
                    "  `position` int NOT NULL\n" +
//                    "  `emphasized` bool NOT NULL DEFAULT false\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `wikipedia` (\n" +
                    "  `id` int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "  `word_id` int NOT NULL,\n" +
                    "  `link` int NOT NULL\n" +
//                    "  `article` int NOT NULL\n" +
                    ");");
            st.execute("ALTER TABLE `positions` ADD FOREIGN KEY (`sentence`) REFERENCES `structure` (`sentence`);\n" +
                    "\n" +
                    "ALTER TABLE `positions` ADD FOREIGN KEY (`id`) REFERENCES `words` (`word`);\n" +
                    "\n" +
                    "ALTER TABLE `wikipedia` ADD FOREIGN KEY (`word_id`) REFERENCES `words` (`id`);");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTerm(String term, int termPosition, int sentenceNumber){
        try{
            JSONArray jo = Interrogator.wikiOpenSearch(term);
            PreparedStatement pst = conn.prepareStatement("INSERT INTO `words` VALUES (null, ?,?,?);");
            //проверить работу инсёрта с null значением
            //проверить SELECT
            pst.setString(1, term);
            System.out.println(jo.getJSONArray(3).get(0).toString());
            pst.setString(2, jo.getJSONArray(3).get(0).toString());
            pst.setString(3, jo.getJSONArray(1).get(0).toString());
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

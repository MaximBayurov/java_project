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
                    "  `sentence` int AUTO_INCREMENT PRIMARY KEY NOT NULL \n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `words` (\n" +
                    "  `id` int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "  `word` char NOT NULL,\n" +
                    "  `article` int NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `positions` (\n" +
                    "  `id` int NOT NULL,\n" +
                    "  `position` int NOT NULL\n" +
                    "  `sentence` int PRIMARY KEY NOT NULL,\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `articles` (\n" +
                    "  `id` int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "  `link` char NOT NULL,\n" +
                    "  `article` char NOT NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `pages` (\n" +
                    "  `page` int NOT NULL,\n" +
                    "  `article` int NOT NULL\n" +
                    ");");
            st.execute("ALTER TABLE `positions` ADD FOREIGN KEY (`sentence`) REFERENCES `structure` (`sentence`);\n" +
                    "\n" +
                    "ALTER TABLE `positions` ADD FOREIGN KEY (`id`) REFERENCES `words` (`id`);\n" +
                    "\n" +
                    "ALTER TABLE `words` ADD FOREIGN KEY (`article`) REFERENCES `articles` (`id`);\n" +
                    "\n" +
                    "ALTER TABLE `pages` ADD FOREIGN KEY (`page`) REFERENCES `articles` (`id`);\n" +
                    "\n" +
                    "ALTER TABLE `pages` ADD FOREIGN KEY (`article`) REFERENCES `articles` (`id`);\n" +
                    "\n" +
                    "ALTER TABLE `wikipedia` ADD FOREIGN KEY (`id`) REFERENCES `words` (`id`);");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTerm(String term, int termPosition, int sentenceNumber){
        try{
            JSONArray jo = Interrogator.wikiOpenSearch(term);
            PreparedStatement pst = conn.prepareStatement("INSERT INTO `words` (word, link, article) VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, term);
            pst.setString(2, jo.getJSONArray(3).get(0).toString());
            pst.setString(3, jo.getJSONArray(1).get(0).toString());
            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            System.out.println(term);
            System.out.println(generatedKeys);
//            String sql = "SELECT * FROM words";
//            Statement stSelect = conn.createStatement();
//            ResultSet rs = stSelect.executeQuery(sql);
//            while (rs.next()) {
//                String id = rs.getString("id");
//                String word = rs.getString("word");
//                System.out.println(id + " " + word);
//            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

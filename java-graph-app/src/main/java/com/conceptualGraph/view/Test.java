package com.conceptualGraph.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Test {
    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./db/test");
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
                    "  `word` char NOT NULL\n" +
                    "  `link` char NULL\n" +
//                    "  `article` char NULL\n" + // название статьи, пока не нужно
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `positions` (\n" +
                    "  `sentence` int PRIMARY KEY NOT NULL,\n" +
                    "  `id` int NOT NULL,\n" +
                    "  `position` int NOT NULL,\n" +
                    "  `emphasized` bool NOT NULL DEFAULT false\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `wikipedia` (\n" +
                    "  `id` int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "  `word_id` int NOT NULL\n" +
                    "  `link` int NOT NULL\n" +
//                    "  `article` int NOT NULL\n" +
                    ");");
            st.execute("ALTER TABLE `positions` ADD FOREIGN KEY (`sentence`) REFERENCES `structure` (`sentence`);\n" +
                    "\n" +
                    "ALTER TABLE `positions` ADD FOREIGN KEY (`id`) REFERENCES `words` (`word`);\n" +
                    "\n" +
                    "ALTER TABLE `wikipedia` ADD FOREIGN KEY (`word_id`) REFERENCES `words` (`id`);");
            st.execute("insert into structure values (1, 1, null);");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
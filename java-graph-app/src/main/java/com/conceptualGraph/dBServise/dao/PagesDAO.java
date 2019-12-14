package com.conceptualGraph.dBServise.dao;

import com.conceptualGraph.dBServise.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class PagesDAO {
    private Executor executor;

    public PagesDAO(Connection connection){
        this.executor = new Executor(connection);
    }


    public void createTable() throws SQLException {
        executor.execUpdate(
                "CREATE TABLE IF NOT EXISTS `pages` (\n" +
                        "  `page` int NOT NULL,\n" +
                        "  `article` int NOT NULL\n" +
                        ");"
        );
    }

    public void insertPage(long page, long article) throws SQLException {
        executor.execUpdate("insert into pages (page, article) values (" + page + ", " + article + ")");
    }

    public void dropTable() throws  SQLException{
        executor.execUpdate("drop table pages cascade");
    }
}

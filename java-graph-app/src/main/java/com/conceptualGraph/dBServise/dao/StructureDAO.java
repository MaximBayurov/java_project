package com.conceptualGraph.dBServise.dao;

import com.conceptualGraph.dBServise.dataSets.StructureDataSet;
import com.conceptualGraph.dBServise.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class StructureDAO {
    private Executor executor;

    public StructureDAO(Connection connection){
        this.executor = new Executor(connection);
    }


    public void createTable() throws SQLException {
        executor.execUpdate(
                "CREATE TABLE IF NOT EXISTS `structure` (\n" +
                        "  `paragraph` int NOT NULL,\n" +
                        "  `sentence` int AUTO_INCREMENT PRIMARY KEY NOT NULL \n" +
                        ");\n"
        );
    }

    public void insertStructure(long paragraph, long sentence) throws SQLException {
        executor.execUpdate("insert into structure (paragraph, sentence) values (" + paragraph + ", " + sentence + ")");
    }

    public void dropTable() throws  SQLException{
        executor.execUpdate("drop table structure cascade");
    }
}

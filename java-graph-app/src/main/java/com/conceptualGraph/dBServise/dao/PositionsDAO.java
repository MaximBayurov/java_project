package com.conceptualGraph.dBServise.dao;

import com.conceptualGraph.dBServise.executor.Executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PositionsDAO {

    private Executor executor;

    public PositionsDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public void createTable() throws SQLException {
        executor.execUpdate("CREATE TABLE IF NOT EXISTS `positions` (\n" +
                "  `id` int NOT NULL,\n" +
                "  `position` int NOT NULL,\n" +
                "  `sentence` int NOT NULL,\n" +
                "  PRIMARY KEY (position, sentence)\n" +
                ");");
//        executor.execUpdate("ALTER TABLE `positions` ADD PRIMARY KEY (position, sentence);");//ОБРАБОТАТЬ ЭКСЕПТИОН
    }

    public void insertPosition(long id, int postion, int sentence) throws SQLException {
        String sql;
        sql = "INSERT INTO positions VALUES (?, ?, ?)";
        executor.prepareQuery(sql);
        executor.preparedStatement.setLong(1, id);
        executor.preparedStatement.setInt(2, postion);
        executor.preparedStatement.setInt(3, sentence);
        executor.preparedStatement.execute();
    }

    public int getWordCount(long id) throws SQLException {
        String sql;
        sql = "SELECT COUNT(*) FROM positions WHERE id = ?";
        executor.prepareQuery(sql);
        executor.preparedStatement.setLong(1, id);
        executor.preparedStatement.execute();
        ResultSet rs = executor.preparedStatement.getResultSet();
        int count = -1;
        if (rs.next()) { count = rs.getInt(1); }
        return count;
    }

    public void dropTable() throws  SQLException{
        executor.execUpdate("drop table positions");
    }
}

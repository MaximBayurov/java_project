package com.conceptualGraph.dBServise.dao;

import com.conceptualGraph.dBServise.dataSets.WordsDataSet;
import com.conceptualGraph.dBServise.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordsDAO {
    private Executor executor;

    public WordsDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public WordsDataSet get(long id) throws SQLException {
        return executor.execQuery("select * from words where id=" + id, result -> {
            result.next();
            return new WordsDataSet(
                    result.getLong(1),
                    result.getString(2),
                    result.getLong(3));
        });
    }

    public void createTable() throws SQLException {
        executor.execUpdate("CREATE TABLE IF NOT EXISTS  `words` (\n" +
                "  `id` int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  `word` char NOT NULL,\n" +
                "  `article` int NULL\n" +
                ");\n");
    }

    public void insertWord(String word, long article) throws SQLException {
        executor.execUpdate("insert into words (id,word, article) values (default,'" + word + "'," + article + ")");
    }

    public long getWordId(String word) throws SQLException {
        return executor.execQuery("select * from words where word='" + word + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void dropTable() throws  SQLException{
        executor.execUpdate("drop table words cascade");
    }

    public List<WordsDataSet> getAllByText(String text) throws SQLException {
        return executor.execQuery("select * from words where word='" + text + "'", result -> {
            ArrayList<WordsDataSet> wordsDataSets = new ArrayList<>();
            while (result.next()){
                wordsDataSets.add( new WordsDataSet(
                        result.getLong(1),
                        result.getString(2),
                        result.getLong(3)));

            }
            return wordsDataSets;
        } );
    }
}

package com.conceptualGraph.dBServise.dao;

import com.conceptualGraph.dBServise.dataSets.WordsDataSet;
import com.conceptualGraph.dBServise.executor.Executor;
import com.sun.source.tree.TryTree;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import java.sql.*;
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
                "  `word` char UNIQUE NOT NULL,\n" +
                "  `article` int NULL\n" +
                ");\n");
    }

    /**
     * Если -1 добавляем словов без ссылки (не нашли)
     * Иначе добавляем с айдишником, который получили
     * @param word слово (термин)
     * @param article ID на статью в таблице ARTICLES
     * @throws SQLException
     */
    public long insertWord(String word, long article) throws SQLException {
        try{
            executor.prepareQuery("insert into words (id,word, article) values (default,?, ?)");
            if (article==-1){
                executor.preparedStatement.setString(1,word);
                executor.preparedStatement.setNull(2, Types.INTEGER);
            } else {
                executor.preparedStatement.setString(1,word);
                executor.preparedStatement.setLong(2,article);
            }
            executor.preparedStatement.execute();
            ResultSet generatedKeys = executor.preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            long id = generatedKeys.getLong(1);
            generatedKeys.close();
            return id;
        } catch (JdbcSQLIntegrityConstraintViolationException ex){
            return getWordId(word);
        }finally {
            executor.preparedStatement.close();
        }
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

    public long getExistingID(String link) throws SQLException{
        executor.prepareQuery("SELECT words.id" +
                " FROM `words` INNER JOIN `articles` ON " +
                "words.article = articles.id  WHERE link = ?");
        executor.preparedStatement.setString(1,link);
        executor.preparedStatement.execute();
        ResultSet rs = executor.preparedStatement.getResultSet();
        if (rs.next()) return  rs.getLong(1);
        else return -1;
    }

    public long countAllNulls() throws SQLException{
        return executor.execQuery("select COUNT(*) from words WHERE article IS NULL", result -> {
            result.next();
            return result.getLong(0);
        } );
    }
}

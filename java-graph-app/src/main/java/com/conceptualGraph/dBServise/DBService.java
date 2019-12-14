package com.conceptualGraph.dBServise;

import com.conceptualGraph.dBServise.dao.WordsDAO;
import com.conceptualGraph.dBServise.dataSets.WordsDataSet;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DBService {

    private final Connection connection;

    public DBService(){
        this.connection = getH2Connection();
    }

    private Connection getH2Connection() {
        try {
            String url = "jdbc:h2:./db/test";
            String name = "";
            String pass = "";

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            ds.setUser(name);
            ds.setPassword(pass);

            Connection connection = DriverManager.getConnection(url, name, pass);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printConnectInfo() {
        try {
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WordsDataSet getWord(long id) throws  DBException{
        try {
            return (new WordsDAO(connection).get(id));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void addWord(String word, int article) throws  DBException{
        try {
            connection.setAutoCommit(false);
            WordsDAO dao = new WordsDAO(connection);
            dao.createTable();
            dao.insertWord(word,article);
            System.out.println("Создана таблица и добавлено слово:"+word);
            connection.commit();
        } catch (SQLException e){
            try{
                connection.rollback();
            }catch (SQLException ignore){
            }
            throw  new DBException(e);
        } finally {
            try{
                connection.setAutoCommit(true);
            }catch (SQLException ignore){
            }
        }
    }

    public WordsDataSet getWordByText(String text) throws DBException {
        try {
            return (new WordsDAO((connection)).get(
                    new WordsDAO((connection)).getWordId(text)
            ));
        }catch (SQLException e){
            throw new DBException(e);
        }
    }


    public void dropWords() throws DBException {
        WordsDAO dao = new WordsDAO(connection);
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public List<WordsDataSet> getAllMatches(String text) throws DBException {
        WordsDAO dao = new WordsDAO(connection);
        try{
            List<WordsDataSet> wordsDataSets = dao.getAllByText(text);
            return wordsDataSets;
        }catch (SQLException e){
            throw new DBException(e);
        }
    }
}

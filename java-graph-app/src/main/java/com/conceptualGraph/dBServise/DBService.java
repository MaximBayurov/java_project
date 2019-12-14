package com.conceptualGraph.dBServise;

import com.conceptualGraph.dBServise.dao.PositionsDAO;
import com.conceptualGraph.dBServise.dao.ArticlesDAO;
import com.conceptualGraph.dBServise.dao.PagesDAO;
import com.conceptualGraph.dBServise.dao.WordsDAO;
import com.conceptualGraph.dBServise.dataSets.ArticlesDataSet;
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
    /**ПОЗИЦИИ*/
    public void addPosition(long id, int position, int sentence) throws  DBException {
        try {
            connection.setAutoCommit(false);
            PositionsDAO dao = new PositionsDAO(connection);
            dao.createTable();
            dao.insertPosition(id, position, sentence);
            System.out.println("Создана таблица и добавлены позиции и предложения: " + position + " kek " + sentence);
            connection.commit();
        } catch (SQLException e) {
        }
    }

    public int getCount(long id) throws DBException {
        try {
            return (new PositionsDAO(connection).getWordCount(id));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    public void dropPositions() throws DBException {
        PositionsDAO dao = new PositionsDAO(connection);
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    public void addArticle(String link, String article) throws DBException {
        try {
            connection.setAutoCommit(false);
            ArticlesDAO dao = new ArticlesDAO(connection);
            dao.createTable();
            dao.insertArticle(link,article);
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


    public ArticlesDataSet getArticle(long id) throws  DBException{
        try {
            return (new ArticlesDAO(connection).get(id));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public ArticlesDataSet getArticleByText(String articleName)  throws DBException {
        try {
            return (new ArticlesDAO((connection)).get(
                    new ArticlesDAO((connection)).getArticleId(articleName)
            ));
        }catch (SQLException e){
            throw new DBException(e);
        }
    }

    public void addPage(int page, int article) throws DBException{
        try {
            connection.setAutoCommit(false);
            PagesDAO dao = new PagesDAO(connection);
            dao.createTable();
            dao.insertPage(page,article);
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
}

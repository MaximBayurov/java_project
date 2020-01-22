package com.conceptualGraph.dBServise;

import com.conceptualGraph.controller.Interrogator;
import com.conceptualGraph.dBServise.dao.PositionsDAO;
import com.conceptualGraph.dBServise.dao.ArticlesDAO;
import com.conceptualGraph.dBServise.dao.PagesDAO;
import com.conceptualGraph.dBServise.dao.StructureDAO;
import com.conceptualGraph.dBServise.dao.WordsDAO;
import com.conceptualGraph.dBServise.dataSets.ArticlesDataSet;
import com.conceptualGraph.dBServise.dataSets.WordsDataSet;
import com.conceptualGraph.dBServise.executor.Executor;
import org.h2.bnf.Sentence;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.util.StringUtils;
import org.json.JSONArray;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  DBService {

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
    /**
     * STRUCTURE
     */
    public void addStructure(int paragraph,int sentence){
        try {
            connection.setAutoCommit(false);
            StructureDAO dao = new StructureDAO(connection);
            dao.insertStructure(paragraph, sentence);
            connection.commit();
        } catch (SQLException e){
            try{
                connection.rollback();
            }catch (SQLException ignore){
                ignore.printStackTrace();
            }
        } finally {
            try{
                connection.setAutoCommit(true);
            }catch (SQLException ignore){
            }
        }
    }

    public void createDBTables() throws DBException{
        try{
            ArticlesDAO articlesDAO = new ArticlesDAO(connection);
            PagesDAO pagesDAO = new PagesDAO(connection);
            PositionsDAO positionsDAO = new PositionsDAO(connection);
            StructureDAO structureDAO = new StructureDAO(connection);
            WordsDAO wordsDAO = new WordsDAO(connection);

            articlesDAO.createTable();
            pagesDAO.createTable();
            positionsDAO.createTable();
            structureDAO.createTable();
            wordsDAO.createTable();

            Executor executor = new Executor(connection);
            executor.execUpdate("ALTER TABLE `positions` ADD FOREIGN KEY (`sentence`) REFERENCES `structure` (`sentence`);\n" +
                    "\n" +
                    "ALTER TABLE `positions` ADD FOREIGN KEY (`id`) REFERENCES `words` (`id`);\n" +
                    "\n" +
                    "ALTER TABLE `pages` ADD FOREIGN KEY (`page`) REFERENCES `articles` (`id`);\n" +
                    "\n" +
                    "ALTER TABLE `pages` ADD FOREIGN KEY (`article`) REFERENCES `articles` (`id`);\n" +
                    "\n" +
                    "ALTER TABLE `words` ADD FOREIGN KEY (`article`) REFERENCES `articles` (`id`);"
            );
        } catch (SQLException e){
            throw  new DBException(e);
        }
    }

    public void dropDB() throws DBException{
        try{
            Executor executor = new Executor(connection);
            executor.execUpdate("DROP ALL OBJECTS");
        } catch (SQLException e){
            throw  new DBException(e);
        }
    }

    /**
     * Добавляет слово и его позицию в БД. В случае успешного добавления возвращает id статьи в базе данных, а в случае
     * ошибки -1.
     * @param term - слово
     * @param termPosition - позиция слова в тексте
     * @param sentenceNumber - номер предложения в тексте
     * @param wordLink - ссылка статьи слова в Wiki
     * @return id| -1 (В случае успешного добавления возвращает id статьи в базе данных, а в случае ошибки -1)
     */
    public long insertWikiWord(String term, int termPosition, int sentenceNumber, String wordLink) {
        try{
            connection.setAutoCommit(false);

            long articleID = -1;
            if (!StringUtils.isNullOrEmpty(wordLink)) {
                //вставили ссылку на статью слова
                ArticlesDAO articlesDAO = new ArticlesDAO(connection);
                articleID = articlesDAO.insertArticleWithID(wordLink);
            }
            //вставили слово
            WordsDAO wordsDAO = new WordsDAO(connection);
            long wordID = wordsDAO.insertWord(term, articleID);
            //вставили позицию
            PositionsDAO positionsDAO = new PositionsDAO(connection);
            positionsDAO.insertPosition(wordID, termPosition, sentenceNumber);
            connection.commit();
            return articleID;
        }
        catch (SQLException e){
            try{
                connection.rollback();
            }catch (SQLException ignore){
            }
        }finally {
            try{
                connection.setAutoCommit(true);
            }catch (SQLException ignore){
            }
        }
        return -1;
    }

    public void insertPageLinks(long articleID, ArrayList<String> links) {
        try{
            ArticlesDAO articlesDAO = new ArticlesDAO(connection);
            PagesDAO pagesDAO = new PagesDAO(connection);
            for (int i=0; i<links.size(); i++) {
                long linkID = articlesDAO.insertArticleWithID(links.get(i));
                pagesDAO.insertPage(articleID, linkID);
            }
        }catch (SQLException e){
            try{
                connection.rollback();
            }catch (SQLException ignore){
            }
        }finally {
            try{
                connection.setAutoCommit(true);
            }catch (SQLException ignore){
            }
        }
    }
}

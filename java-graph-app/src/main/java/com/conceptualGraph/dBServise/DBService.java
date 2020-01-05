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
import org.json.JSONArray;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
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

    public List<WordsDataSet> getAllWords() throws DBException {
        WordsDAO dao = new WordsDAO(connection);
        try{
            List<WordsDataSet> wordsDataSets = dao.getAll();
            return wordsDataSets;
        }catch (SQLException e){
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

    public void insertTerm(String term, int termPosition, int sentenceNumber){
        try{
            connection.setAutoCommit(false);
            JSONArray jo = Interrogator.wikiOpenSearch(term);
            if (jo.getJSONArray(3).isNull(0)){
                WordsDAO wordsDAO = new WordsDAO(connection);
                long wordID = wordsDAO.insertWord(term, -1);
                PositionsDAO positionsDAO = new PositionsDAO(connection);
                positionsDAO.insertPosition(wordID, termPosition, sentenceNumber);
                connection.commit();
                return;
            }
            WordsDAO wordsDAO = new WordsDAO(connection);
            String fullLink = jo.getJSONArray(3).get(0).toString();
            String shortLink = jo.getJSONArray(3).get(0).toString().replace("https://ru.wikipedia.org","");
            String articleTitle = jo.getJSONArray(1).get(0).toString();
            long wordID = wordsDAO.getExistingID(shortLink);

            if(wordID==-1){
                /*слово с такой ссылкой не существует (уникально), поэтому добавляем его в WORDS и ARTICLES*/
                long articleID = -1;
                if (!fullLink.isEmpty()) {
                    ArticlesDAO articlesDAO = new ArticlesDAO(connection);
                    articleID = articlesDAO.insertArticleWithID(
                            shortLink, articleTitle);
                    PagesDAO pagesDAO = new PagesDAO(connection);

                    if (articleID != -1) {
                        /*переходим по сслыке википедии, чтобы взять линки*/
                        Elements links = Interrogator.selectLinks(fullLink);
                        long linkID;
                        for (Element link : links){
                            if (link.attr("href").contains("wiki")){
                                linkID = articlesDAO.insertArticleWithID(
                                        link.attr("href"),
                                        link.attr("title"));
                                pagesDAO.insertPage(articleID, linkID);
                            }
                        }
                    }
                }
                /*здесь в зависимости от значения articleID мы вставляем слово в таблицу по разному*/
                wordID = wordsDAO.insertWord(term, articleID);
            }
            PositionsDAO positionsDAO = new PositionsDAO(connection);
            positionsDAO.insertPosition(wordID, termPosition, sentenceNumber);
            connection.commit();
        }
        catch (SQLException e){
            try{
                connection.rollback();
            }catch (SQLException ignore){
            }
        }
        catch (IOException IOEx){
            IOEx.printStackTrace();
        }
        finally {
            try{
                connection.setAutoCommit(true);
            }catch (SQLException ignore){
            }
        }
    }

    public void printWordsWithArticles() {
        try {
            Statement st = connection.createStatement();
            st.execute("SELECT words.id, words.word, articles.article, articles.link FROM WORDS INNER JOIN" +
                    " articles ON words.article = articles.id");
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getLong(1) + " |" +
                        " " + rs.getString(2) + " |" +
                        " " + rs.getString(3) + " |" +
                        " " + rs.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void printAllArticles() throws DBException {
        ArticlesDAO dao = new ArticlesDAO(connection);
        try{
            List<ArticlesDataSet> all = dao.getAll();
            for (ArticlesDataSet dataSet: all){
                System.out.println(dataSet);
            }
        }catch (SQLException e){
            throw new DBException(e);
        }
    }
}

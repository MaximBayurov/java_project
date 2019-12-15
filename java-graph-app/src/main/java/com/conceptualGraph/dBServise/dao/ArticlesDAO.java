package com.conceptualGraph.dBServise.dao;

import com.conceptualGraph.dBServise.dataSets.ArticlesDataSet;
import com.conceptualGraph.dBServise.executor.Executor;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import java.beans.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticlesDAO {
    private Executor executor;

    public ArticlesDAO(Connection connection){
        this.executor = new Executor(connection);
    }
    public ArticlesDataSet get(long id) throws SQLException {
        return executor.execQuery("select * from articles where id=" + id, result -> {
            result.next();
            return new ArticlesDataSet(
                    result.getLong(1),
                    result.getString(2),
                    result.getString(3));
        });
    }

    public void createTable() throws SQLException {
        executor.execUpdate(
                "CREATE TABLE IF NOT EXISTS `articles` (\n" +
                        "  `id` int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                        "  `link` char UNIQUE NOT NULL,\n" +
                        "  `article` char NOT NULL\n" +
                        ");\n"
        );
    }



    public long insertArticleWithID(String link, String article) throws SQLException {
        try {
            executor.prepareQuery("insert into articles (id,link, article) values (default,?,?)");
            executor.preparedStatement.setString(1, link);
            executor.preparedStatement.setString(2, article);
            executor.preparedStatement.execute();
            ResultSet generatedKeys = executor.preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            long id = generatedKeys.getLong(1);
            generatedKeys.close();
            return id;
        } catch (JdbcSQLIntegrityConstraintViolationException ex){
            return getArticleId(link);
        }finally {
            executor.preparedStatement.close();
        }
    }

    public void insertArticle(String link, String article) throws SQLException {
        executor.execUpdate("insert into articles (id,link, article) values (default,'" + link + "','" + article + "')");
    }

    public long getArticleId(String link) throws SQLException {
        return executor.execQuery("select * from articles where link='" + link + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void dropTable() throws  SQLException{
        executor.execUpdate("drop table articles cascade");
    }

    public List<ArticlesDataSet> getAll() throws SQLException {
        return executor.execQuery("select * from articles", result -> {
            ArrayList<ArticlesDataSet> articlesDataSets = new ArrayList<>();
            while (result.next()){
                articlesDataSets.add( new ArticlesDataSet(
                        result.getLong(1),
                        result.getString(2),
                        result.getString(3)));

            }
            return articlesDataSets;
        } );
    }
}

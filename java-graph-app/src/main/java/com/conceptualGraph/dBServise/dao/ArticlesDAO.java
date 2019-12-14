package com.conceptualGraph.dBServise.dao;

import com.conceptualGraph.dBServise.dataSets.ArticlesDataSet;
import com.conceptualGraph.dBServise.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

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
                        "  `link` char NOT NULL,\n" +
                        "  `article` char NOT NULL\n" +
                        ");\n"
        );
    }

    public void insertArticle(String link, String article) throws SQLException {
        executor.execUpdate("insert into articles (id,link, article) values (default,'" + link + "','" + article + "')");
    }

    public long getArticleId(String article) throws SQLException {
        return executor.execQuery("select * from articles where article='" + article + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void dropTable() throws  SQLException{
        executor.execUpdate("drop table articles cascade");
    }
}

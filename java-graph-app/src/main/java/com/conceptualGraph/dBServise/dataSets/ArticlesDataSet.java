package com.conceptualGraph.dBServise.dataSets;

public class ArticlesDataSet {
    private long id;
    private String article;

    public ArticlesDataSet(long id, String article) {
        this.id = id;
        this.article = article;
    }

    public String getArticle() {
        return article;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ArticleDataSet{" +
                "id=" + id +
                ", article=" + article  +
                '}';
    }
}

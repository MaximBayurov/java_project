package com.conceptualGraph.dBServise.dataSets;

public class ArticlesDataSet {
    private long id;
    private String link;
    private String article;

    public ArticlesDataSet(long id, String link, String article) {
        this.id = id;
        this.link = link;
        this.article = article;
    }

    public String getLink() {
        return link;
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
                ", link='" + link + '\'' +
                ", article=" + article  +
                '}';
    }
}

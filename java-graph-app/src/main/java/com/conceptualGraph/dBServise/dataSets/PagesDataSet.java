package com.conceptualGraph.dBServise.dataSets;

public class PagesDataSet {
    private long page;
    private long article;

    public PagesDataSet(long page, long article) {
        this.page = page;
        this.article = article;
    }

    public long getPage() {
        return page;
    }

    public long getArticle() {
        return article;
    }

    @Override
    public String toString() {
        return "PagesDataSet{" +
                "page=" + page +
                ", article=" + article  +
                '}';
    }
}

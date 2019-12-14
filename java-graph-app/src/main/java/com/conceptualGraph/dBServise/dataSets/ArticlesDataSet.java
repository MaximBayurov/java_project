package com.conceptualGraph.dBServise.dataSets;

public class ArticlesDataSet {
    private long id;
    private String word;
    private long article;

    public ArticlesDataSet(long id, String word, long article) {
        this.id = id;
        this.word = word;
        this.article = article;
    }

    public String getWord() {
        return word;
    }

    public long getArticle(){
        return (int)article;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "WordsDataSet{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", article=" + article  +
                '}';
    }
}

package com.conceptualGraph.dBServise.dataSets;

public class StructureDataSet {

    private long sentence;
    private long paragraph;

    public StructureDataSet(long sentence, long paragraph) {
        this.sentence = sentence;
        this.paragraph = paragraph;
    }

    public long getSentence() {
        return sentence;
    }

    public long getParagraph() {
        return paragraph;
    }

    @Override
    public String toString() {
        return "StructureDataSet{" +
                "sentence=" + sentence +
                ", paragraph=" + paragraph  +
                '}';
    }
}

package com.conceptualGraph.controller;

public class WordCheckerController implements WordCheckerControllerMBean {
    @Override
    public int getQueryLimit() {
        return WordChecker.threadsLimit;
    }

    @Override
    public void setQueryLimit(int limit) {
        WordChecker.threadsLimit = limit;
    }
}

package com.conceptualGraph.controller;

public class WordCheckerController implements WordCheckerControllerMBean {
    @Override
    public int getQueryLimit() {
        return WordChecker.queryLimit;
    }

    @Override
    public void setQueryLimit(int limit) {
        WordChecker.queryLimit = limit;
    }
}

package com.conceptualGraph.model;

import com.conceptualGraph.controller.Configurator;
import com.conceptualGraph.controller.DataBase;
import com.conceptualGraph.controller.WordChecker;
import com.conceptualGraph.dBServise.DBException;
import com.conceptualGraph.view.App;

public class Main {
    public static void main( String[] args )
    {
        Configurator configurator = new Configurator();
        new App(configurator.getProperties()).start();
    }
}

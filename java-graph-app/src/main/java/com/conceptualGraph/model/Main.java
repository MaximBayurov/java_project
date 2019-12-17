package com.conceptualGraph.model;

import com.conceptualGraph.controller.Configurator;
import com.conceptualGraph.controller.DataBase;
import com.conceptualGraph.controller.WordChecker;
import com.conceptualGraph.dBServise.DBException;
import com.conceptualGraph.view.App;
import com.conceptualGraph.view.AppFX;

public class Main {
    public static void main( String[] args )
    {
        try {
            WordChecker.dbService.createDBTables();
            Configurator configurator = new Configurator();
            new App(configurator.getProperties()).start();
        } catch (DBException ex){
            ex.printStackTrace();
        }
    }
}

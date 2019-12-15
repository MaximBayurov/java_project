package com.conceptualGraph.model;

import com.conceptualGraph.controller.Configurator;
import com.conceptualGraph.controller.DataBase;
import com.conceptualGraph.view.App;
import com.conceptualGraph.view.AppFX;

public class Main {
    public static void main( String[] args )
    {
        DataBase.createDB();
        Configurator configurator = new Configurator();
        new App(configurator.getProperties()).start();
    }
}

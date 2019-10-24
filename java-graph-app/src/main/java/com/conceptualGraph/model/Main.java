package com.conceptualGraph.model;

import com.conceptualGraph.controller.Configurator;
import com.conceptualGraph.view.App;

public class Main {
    public static void main( String[] args )
    {
        Configurator configurator = new Configurator();
        new App(configurator.getProperties()).start();
    }
}

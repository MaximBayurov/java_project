package com.conceptualGraph.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Читает свойства и возвращает их
 */
public class Configurator {

    private Properties properties;

    public Properties getProperties(){
        setProperties();
        return properties;
    }

    private void setProperties() {
        File configFile = new File("src/main/resources/config.properties");
        try {
            FileInputStream fis = new FileInputStream(configFile);
            properties=new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

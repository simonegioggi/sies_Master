package it.mig.sies.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * SIES FASE 2 - Classe di utility per il caricamento delle properties
 * 
 * @author Federico Paparoni
 * */

public class ApplicationProperties {

    private static final String FILE_PROPERTIES = "/sies.properties";
    private static Properties properties;
    private static ApplicationProperties _singleton = new ApplicationProperties();
    
    /**
     * Costruttore privato che inizializza un singleton utilizzato
     * per accedere alle properties
     * */
    private ApplicationProperties() {
        InputStream inputStream = getClass().getResourceAsStream(FILE_PROPERTIES);
        try {
            load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Recupera la proprietï¿½ a partire dalla chiave
     * @param key Chiave della proprietï¿½
     * */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Recupera l'istanza dell'ApplicationProperties
     * */
    public static ApplicationProperties getIstance() {
        return _singleton;
    }
    
    /**
     * Carica il file di properties a partire da un InputStream
     * @param inStream InputStream
     * @throws IOException
     * */
    private static void load(InputStream inStream) throws IOException {
        properties = new Properties();
        properties.load(inStream);
    }
}
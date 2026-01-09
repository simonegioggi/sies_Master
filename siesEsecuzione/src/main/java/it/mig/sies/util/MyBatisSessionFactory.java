package it.mig.sies.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

/**
 * SIES FASE 2 - Classe di utility 
 * per la gestione delle sessioni MyBatis
 * 
 * @author Federico Paparoni
 * */

public class MyBatisSessionFactory {
	private static final Logger logger=Logger.getLogger(MyBatisSessionFactory.class);
    private static SqlSessionFactory factory = getSessionFactory();
    
    /**
     * Recupera la sessione tramite il factory
     * */
    public static SqlSession getSession() {
        return factory.openSession();
    }
    
    /**
     * Inizializza la factory 
     * a partire dal file di configurazione
     * */
    private static SqlSessionFactory getSessionFactory() {
    	//File di configurazione di MyBatis
        String resource = "mybatis/configuration.xml";
        Reader reader = null;
        try {
        	logger.info("CARICAMENTO MYBATIS");
        	//Lettura della risorsa
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException ex) {
        	logger.error(ExceptionUtils.getFullStackTrace(ex));
        }
        //Creazione della factory 
        //che diventa una variabile statica della classe
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
        return sqlMapper;
    }
}
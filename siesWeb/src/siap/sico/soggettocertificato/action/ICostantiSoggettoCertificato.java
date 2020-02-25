package siap.sico.soggettocertificato.action;

/**
* <p>Title: ICostantiSoggettoCertificato</p>
* <p>Description: Classe di costanti di SoggettoCertificato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiSoggettoCertificato {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_SOGGETTO_CERTIFICATO     = "IdSoggettoCertificato"; 
    public static final String CAMPO_SOG_ID_SOGGETTO             = "SogIdSoggetto"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO     = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO       = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO       = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO   = "CodOperatoreInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO     = "CodUfficioInserimento"; 
    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO   = "GiornoDataAggiornamento"; 
    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO     = "MeseDataAggiornamento"; 
    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO     = "AnnoDataAggiornamento"; 
    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento"; 
    public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO   = "CodUfficioAggiornamento"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASOGGETTOCERTIFICATO  	= IWebConstants.ROOT_DIR + "files/siap/sico/soggettocertificato/LoadRicercaSoggettoCertificato.jsp";
    public static final String PG_LOAD_DETTAGLIOSOGGETTOCERTIFICATO	= IWebConstants.ROOT_DIR + "files/siap/sico/soggettocertificato/DettaglioSoggettoCertificato.jsp";
    public static final String PG_RICERCASOGGETTOCERTIFICATO       	= IWebConstants.ROOT_DIR + "files/siap/sico/soggettocertificato/RicercaSoggettoCertificato.jsp";
    public static final String PG_LOAD_INSERISCISOGGETTOCERTIFICATO	= IWebConstants.ROOT_DIR + "files/siap/sico/soggettocertificato/LoadInserisciSoggettoCertificato.jsp";
    public static final String PG_LOAD_CANCELLASOGGETTOCERTIFICATO 	= IWebConstants.ROOT_DIR + "files/siap/sico/soggettocertificato/DettaglioSoggettoCertificato.jsp";
}
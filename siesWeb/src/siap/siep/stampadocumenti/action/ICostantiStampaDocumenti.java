package siap.siep.stampadocumenti.action;

/**
* <p>Title: ICostantiStampaDocumenti</p>
* <p>Description: Classe di costanti di StampaDocumenti</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiStampaDocumenti {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_STAMPA              = "IdStampa"; 
    public static final String CAMPO_ID_UTENTE              = "IdUtente"; 
    public static final String CAMPO_GIORNO_DATA            = "GiornoData"; 
    public static final String CAMPO_MESE_DATA              = "MeseData"; 
    public static final String CAMPO_ANNO_DATA              = "AnnoData"; 
    public static final String CAMPO_STATO                  = "Stato"; 
    public static final String CAMPO_NUM_STAMPE_RICHIESTE   = "NumStampeRichieste"; 
    public static final String CAMPO_NUME_STAMPE_EFFETTUATE = "NumeStampeEffettuate"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASTAMPADOCUMENTI  	= IWebConstants.ROOT_DIR + "files/siap/siep/stampadocumenti/LoadRicercaStampaDocumenti.jsp";
    public static final String PG_LOAD_DETTAGLIOSTAMPADOCUMENTI	= IWebConstants.ROOT_DIR + "files/siap/siep/stampadocumenti/DettaglioStampaDocumenti.jsp";
    public static final String PG_RICERCASTAMPADOCUMENTI       	= IWebConstants.ROOT_DIR + "files/siap/siep/stampadocumenti/RicercaStampaDocumenti.jsp";
    public static final String PG_LOAD_INSERISCISTAMPADOCUMENTI	= IWebConstants.ROOT_DIR + "files/siap/siep/stampadocumenti/LoadInserisciStampaDocumenti.jsp";
    public static final String PG_LOAD_CANCELLASTAMPADOCUMENTI 	= IWebConstants.ROOT_DIR + "files/siap/siep/stampadocumenti/DettaglioStampaDocumenti.jsp";
}
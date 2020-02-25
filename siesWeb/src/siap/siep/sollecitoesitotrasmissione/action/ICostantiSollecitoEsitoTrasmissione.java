package siap.siep.sollecitoesitotrasmissione.action;

/**
* <p>Title: ICostantiSollecitoEsitoTrasmissione</p>
* <p>Description: Classe di costanti di SollecitoEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.siep.web.ISIEPCostantiWeb;

public interface ICostantiSollecitoEsitoTrasmissione extends ISIEPCostantiWeb {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_SOLLECITO                 = "IdSollecito"; 
    public static final String CAMPO_COD_UFF_SOLLECITATO          = "CodUffSollecitato"; 
    public static final String CAMPO_OGGETTO_MS_SOLLECITO         = "OggettoMsSollecito"; 
    public static final String CAMPO_OGGETTO_MS_SOLLECITATO       = "OggettoMsSollecitato"; 
    public static final String CAMPO_GIORNO_DATA_INVIO_MS_SOLLECITATO = "GiornoDataInvioMsSollecitato"; 
    public static final String CAMPO_MESE_DATA_INVIO_MS_SOLLECITATO = "MeseDataInvioMsSollecitato"; 
    public static final String CAMPO_ANNO_DATA_INVIO_MS_SOLLECITATO = "AnnoDataInvioMsSollecitato"; 
    public static final String CAMPO_MES_ID_MESSAGGIO_SOLLECITATO = "MesIdMessaggioSollecitato"; 
    public static final String CAMPO_COD_UFF_INOLTRANTE           = "CodUffInoltrante"; 
    public static final String CAMPO_GIORNO_DATA_INOLTRO          = "GiornoDataInoltro"; 
    public static final String CAMPO_MESE_DATA_INOLTRO            = "MeseDataInoltro"; 
    public static final String CAMPO_ANNO_DATA_INOLTRO            = "AnnoDataInoltro"; 
    public static final String CAMPO_MES_ID_MESSAGGIO_INOLTRO     = "MesIdMessaggioInoltro"; 
    public static final String CAMPO_EVE_ID_EVENTO                = "EveIdEvento"; 
    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP    = "FasSieIdFascicoloSiep"; 
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO    = "CodOperatoreInserimento"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO      = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO        = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO        = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO      = "CodUfficioInserimento"; 
    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO  = "CodOperatoreAggiornamento"; 
    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO    = "GiornoDataAggiornamento"; 
    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO      = "MeseDataAggiornamento"; 
    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO      = "AnnoDataAggiornamento"; 
    public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO    = "CodUfficioAggiornamento"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASOLLECITOESITOTRASMISSIONE  	= ROOT_DIR + "files/siap/jms/sollecitoesitotrasmissione/LoadRicercaSollecitoEsitoTrasmissione.jsp";
    public static final String PG_LOAD_DETTAGLIOSOLLECITOESITOTRASMISSIONE	= ROOT_DIR + "files/siap/jms/sollecitoesitotrasmissione/DettaglioSollecitoEsitoTrasmissione.jsp";
    public static final String PG_RICERCASOLLECITOESITOTRASMISSIONE       	= ROOT_DIR + "files/siap/jms/sollecitoesitotrasmissione/RicercaSollecitoEsitoTrasmissione.jsp";
    public static final String PG_LOAD_INSERISCISOLLECITOESITOTRASMISSIONE	= ROOT_DIR + "files/siap/jms/sollecitoesitotrasmissione/LoadInserisciSollecitoEsitoTrasmissione.jsp";
    public static final String PG_LOAD_CANCELLASOLLECITOESITOTRASMISSIONE 	= ROOT_DIR + "files/siap/jms/sollecitoesitotrasmissione/DettaglioSollecitoEsitoTrasmissione.jsp";
}
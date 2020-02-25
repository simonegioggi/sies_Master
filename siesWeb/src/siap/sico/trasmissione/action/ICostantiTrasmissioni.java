package siap.sico.trasmissione.action;

/**
* <p>Title: ICostantiTrasmissioni</p>
* <p>Description: Classe di costanti di Trasmissioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiTrasmissioni {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_TRASMISSIONE   = "IdTrasmissione"; 
    public static final String CAMPO_TIPO_TRASMISSIONE = "TipoTrasmissione"; 
    public static final String CAMPO_GIORNO_DATA_TRASMISSIONE = "GiornoDataTrasmissione"; 
    public static final String CAMPO_MESE_DATA_TRASMISSIONE = "MeseDataTrasmissione"; 
    public static final String CAMPO_ANNO_DATA_TRASMISSIONE = "AnnoDataTrasmissione"; 
    public static final String CAMPO_ESITO_TRASMISSIONE = "EsitoTrasmissione"; 
    public static final String CAMPO_COD_ERRORE        = "CodErrore"; 
    public static final String CAMPO_TIPO_OPERAZIONE   = "TipoOperazione"; 
    public static final String CAMPO_DESTINAZIONE      = "Destinazione"; 
    public static final String CAMPO_CHIAVE_SIES_SOGG  = "ChiaveSiesSogg"; 
    public static final String CAMPO_CHIAVE_SIES_FASC  = "ChiaveSiesFasc"; 
    public static final String CAMPO_CHIAVE_NSC_SOGG   = "ChiaveNscSogg"; 
    public static final String CAMPO_CHIAVE_NSC_PROV   = "ChiaveNscProv"; 
    public static final String CAMPO_CHIAVE_ANNO   = "Chiave_Anno"; 
    public static final String CAMPO_CHIAVE_PROGR   = "Chiave_Progr"; 
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO   = "Cod_Operatore_Inserimento"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO   = "Cod_Ufficio_Inserimento"; 
    

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCATRASMISSIONI  	= IWebConstants.ROOT_DIR + "files/siap/sico/trasmissione/LoadRicercaTrasmissioni.jsp";
    public static final String PG_LOAD_DETTAGLIOTRASMISSIONI	= IWebConstants.ROOT_DIR + "files/siap/sico/trasmissione/DettaglioTrasmissioni.jsp";
    public static final String PG_RICERCATRASMISSIONI       	= IWebConstants.ROOT_DIR + "files/siap/sico/trasmissione/RicercaTrasmissioni.jsp";
    public static final String PG_LOAD_INSERISCITRASMISSIONI	= IWebConstants.ROOT_DIR + "files/siap/sico/trasmissione/LoadInserisciTrasmissioni.jsp";
    public static final String PG_LOAD_CANCELLATRASMISSIONI 	= IWebConstants.ROOT_DIR + "files/siap/sico/trasmissione/DettaglioTrasmissioni.jsp";
}
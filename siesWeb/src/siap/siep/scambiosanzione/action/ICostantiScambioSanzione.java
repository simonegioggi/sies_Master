package siap.siep.scambiosanzione.action;

/**
* <p>Title: ICostantiScambioSanzione</p>
* <p>Description: Classe di costanti di ScambioSanzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiScambioSanzione {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_SCAMBIO_SANZIONE         = "IdScambioSanzione"; 
    public static final String CAMPO_COD_TIPO_DECISIONE          = "CodTipoDecisione"; 
    public static final String CAMPO_COD_NATURA_SANZIONE         = "CodNaturaSanzione"; 
    public static final String CAMPO_COD_TIPO_SANZIONE           = "CodTipoSanzione"; 
    public static final String CAMPO_GIORNO_DATA_INIZIO          = "GiornoDataInizio"; 
    public static final String CAMPO_MESE_DATA_INIZIO            = "MeseDataInizio"; 
    public static final String CAMPO_ANNO_DATA_INIZIO            = "AnnoDataInizio"; 
    public static final String CAMPO_GIORNO_DATA_FINE            = "GiornoDataFine"; 
    public static final String CAMPO_MESE_DATA_FINE              = "MeseDataFine"; 
    public static final String CAMPO_ANNO_DATA_FINE              = "AnnoDataFine"; 
    public static final String CAMPO_NOTE                        = "Note"; 
    public static final String CAMPO_ANNO_REGISTRO               = "AnnoRegistro"; 
    public static final String CAMPO_NUMERO_REGISTRO             = "NumeroRegistro"; 
    public static final String CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS  = "ChiaveAnnoFascicoloSius"; 
    public static final String CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS = "ChiaveProgrFascicoloSius"; 
    public static final String CAMPO_COD_UFFICIO_SORVEGLIANZA    = "CodUfficioSorveglianza"; 
    public static final String CAMPO_COD_UFFICIO_EMITTENTE       = "CodUfficioEmittente"; 
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO   = "CodOperatoreInserimento"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO     = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO       = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO       = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO     = "CodUfficioInserimento"; 
    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento"; 
    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO   = "GiornoDataAggiornamento"; 
    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO     = "MeseDataAggiornamento"; 
    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO     = "AnnoDataAggiornamento"; 
    public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO   = "CodUfficioAggiornamento"; 
    public static final String CAMPO_GIORNO_DATA_EMISSIONE       = "GiornoDataEmissione"; 
    public static final String CAMPO_MESE_DATA_EMISSIONE         = "MeseDataEmissione"; 
    public static final String CAMPO_ANNO_DATA_EMISSIONE         = "AnnoDataEmissione"; 
    public static final String CAMPO_EVE_ID_EVENTO               = "EveIdEvento"; 

    
    public static final String CAMPO_COD_AUTORITA_EMITTENTE       = "CodAutoritaEmittente"; 
    public static final String CAMPO_COD_SEDE_AUTORITA_EMITTENTE  = "SedeAutoritaEmittente"; 
    
    public static final String CAMPO_NUM_GIORNI_RECLUSIONE     = "NumGiorniReclusione"; 
    public static final String CAMPO_NUM_MESI_RECLUSIONE 	   = "NumMesiReclusione"; 
    public static final String CAMPO_NUM_ANNI_RECLUSIONE       = "NumAnniReclusione"; 
    public static final String CAMPO_NUM_GIORNI_ARRESTO        = "NumGiorniArresto"; 
    public static final String CAMPO_NUM_MESI_ARRESTO          = "NumMesiArresto"; 
    public static final String CAMPO_NUM_ANNI_ARRESTO          = "NumAnniArresto"; 
    
    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //=================================================================
    public static final String CAMPO_ID_DOCUMENTO_SIUS = "IdDocumentoSius";
    public static final String CAMPO_NATURA_SS = "NaturaSS";
    public static final String CAMPO_TIPO_SS= "TipoSS";    
    
    public static final String ANNOTAZIONE = "ANNOTAZIONE";
    public static final String REVOCA_CONVERSIONE = "REVOCA_CONVERSIONE";
    
    public static final String CAMPO_DESC_UFF_SORVE = "DescUfficioSorveglianza";
    public static final String CAMPO_DESC_UFF_EMITT = "DescUfficioEmittente";
    
    public static final String CAMPO_NOTE_ANN						= "NoteAnn";

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASCAMBIOSANZIONE  	= IWebConstants.ROOT_DIR + "files/siap/siep/scambiosanzione/LoadRicercaScambioSanzione.jsp";
    public static final String PG_LOAD_DETTAGLIOSCAMBIOSANZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/scambiosanzione/DettaglioScambioSanzione.jsp";
    public static final String PG_RICERCASCAMBIOSANZIONE       	= IWebConstants.ROOT_DIR + "files/siap/siep/scambiosanzione/RicercaScambioSanzione.jsp";
    public static final String PG_LOAD_INSERISCISCAMBIOSANZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/scambiosanzione/LoadInserisciScambioSanzione.jsp";
    public static final String PG_LOAD_CANCELLASCAMBIOSANZIONE 	= IWebConstants.ROOT_DIR + "files/siap/siep/scambiosanzione/DettaglioScambioSanzione.jsp";
    
    public static final String PG_LISTA_DOCUMENTI_SIUS 			= IWebConstants.ROOT_DIR + "files/siap/siep/scambiosanzione/ListaDocumentiSius.jsp";
}
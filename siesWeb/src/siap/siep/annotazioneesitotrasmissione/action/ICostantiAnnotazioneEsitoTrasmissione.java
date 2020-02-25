package siap.siep.annotazioneesitotrasmissione.action;

/**
* <p>Title: ICostantiAnnotazioneEsitoTrasmissione</p>
* <p>Description: Classe di costanti di AnnotazioneEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiAnnotazioneEsitoTrasmissione {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_ESITO_TRASMISSIONE       = "IdEsitoTrasmissione"; 
    public static final String CAMPO_GIORNO_DATA_TRASMISSIONE    = "GiornoDataTrasmissione"; 
    public static final String CAMPO_MESE_DATA_TRASMISSIONE      = "MeseDataTrasmissione"; 
    public static final String CAMPO_ANNO_DATA_TRASMISSIONE      = "AnnoDataTrasmissione"; 
    public static final String CAMPO_OGGETTO_TRASMISSIONE        = "OggettoTrasmissione"; 
    public static final String CAMPO_COD_UFFICIO_DESTINATARIO    = "CodUfficioDestinatario"; 
    public static final String CAMPO_COD_UFFICIO_INOLTRANTE      = "CodUfficioInoltrante"; 
    public static final String CAMPO_COD_UFFICIO_INOLTRO         = "CodUfficioInoltro"; 
    public static final String CAMPO_TIPO_UFFICIO_INOLTRO        = "TipoUfficioInoltro"; 
    public static final String CAMPO_COMUNE_UFFICIO_INOLTRO      = "ComuneUfficioInoltro";     
    public static final String CAMPO_COD_UFFICIO_ESITO           = "CodUfficioEsito"; 
    public static final String CAMPO_TIPO_UFFICIO_ESITO          = "TipoUfficioEsito"; 
    public static final String CAMPO_COMUNE_UFFICIO_ESITO        = "ComuneUfficioEsito"; 
    public static final String CAMPO_GIORNO_DATA_ESITO           = "GiornoDataEsito"; 
    public static final String CAMPO_MESE_DATA_ESITO             = "MeseDataEsito"; 
    public static final String CAMPO_ANNO_DATA_ESITO             = "AnnoDataEsito"; 
    public static final String CAMPO_COD_ESITO                   = "CodEsito"; 
    public static final String CAMPO_NOTE_ESITO                  = "NoteEsito"; 
    public static final String CAMPO_CHIAVE_ANNO                 = "ChiaveAnno"; 
    public static final String CAMPO_CHIAVE_PROGR                = "ChiaveProgr"; 
    public static final String CAMPO_CHIAVE_UFFICIO              = "ChiaveUfficio"; 
    public static final String CAMPO_EVE_ID_EVENTO               = "EveIdEvento"; 
    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP   = "FasSieIdFascicoloSiep"; 
    public static final String CAMPO_MES_ID_MESSAGGIO_RICHIESTA  = "MesIdMessaggioRichiesta"; 
    public static final String CAMPO_MES_ID_MESSAGGIO_ESITO      = "MesIdMessaggioEsito"; 
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

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCAANNOTAZIONEESITOTRASMISSIONE  	= IWebConstants.ROOT_DIR + "files/siap/siep/annotazioneesitotrasmissione/LoadRicercaAnnotazioneEsitoTrasmissione.jsp";
    public static final String PG_LOAD_DETTAGLIOANNOTAZIONEESITOTRASMISSIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/annotazioneesitotrasmissione/DettaglioAnnotazioneEsitoTrasmissione.jsp";
    public static final String PG_RICERCAANNOTAZIONEESITOTRASMISSIONE       	= IWebConstants.ROOT_DIR + "files/siap/siep/annotazioneesitotrasmissione/RicercaAnnotazioneEsitoTrasmissione.jsp";
    public static final String PG_LOAD_INSERISCIANNOTAZIONEESITOTRASMISSIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/annotazioneesitotrasmissione/LoadInserisciAnnotazioneEsitoTrasmissione.jsp";
    public static final String PG_LOAD_CANCELLAANNOTAZIONEESITOTRASMISSIONE 	= IWebConstants.ROOT_DIR + "files/siap/siep/annotazioneesitotrasmissione/DettaglioAnnotazioneEsitoTrasmissione.jsp";
}
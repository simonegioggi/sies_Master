package siap.siep.competenza.action;

/**
* <p>Title: ICostantiCompetenza</p>
* <p>Description: Classe di costanti di Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiCompetenza {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_COMPETENZA                  = "IdCompetenza"; 
    public static final String CAMPO_COD_TIPO_PROVVEDIMENTO         = "CodTipoProvvedimentoComp"; 
    public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO      = "GiornoDataProvvedimentoComp"; 
    public static final String CAMPO_MESE_DATA_PROVVEDIMENTO        = "MeseDataProvvedimentoComp"; 
    public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO        = "AnnoDataProvvedimentoComp"; 
    
    public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE    = "CodTipoAutoritaEmittenteComp"; 
    public static final String CAMPO_COD_LUOGO_EMITTENTE            = "CodLuogoEmittenteComp"; 

    public static final String CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE = "NumSezioneAutoritaEmittenteComp"; 
    public static final String CAMPO_ANNO_SENTENZA                  = "AnnoSentenzaComp"; 
    public static final String CAMPO_NUMERO_SENTENZA                = "NumeroSentenzaComp"; 
    public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA     = "GiornoDataIrrevocabilitaComp"; 
    public static final String CAMPO_MESE_DATA_IRREVOCABILITA       = "MeseDataIrrevocabilitaComp"; 
    public static final String CAMPO_ANNO_DATA_IRREVOCABILITA       = "AnnoDataIrrevocabilitaComp"; 
    public static final String CAMPO_SEN_ID_SENTENZA                = "SenIdSentenzaComp"; 
    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP      = "FasSieIdFascicoloSiepComp"; 
    public static final String CAMPO_EVE_ID_EVENTO                  = "EveIdEvento"; 
    
    public static final String CAMPO_CHIAVE_ANNO                    = "ChiaveAnnoComp"; 
    public static final String CAMPO_CHIAVE_UFFICIO                 = "ChiaveUfficioComp";     
    public static final String CAMPO_CHIAVE_PROGR                   = "ChiaveProgrComp"; 
    public static final String CAMPO_FLAG_ACCORPATO                 = "FlagAccorpato"; 
    public static final String CAMPO_CHIAVE_UFFICIO_ORIGINE         = "ChiaveUfficioOrigine"; 
    public static final String CAMPO_CHIAVE_PROGR_ORIGINE           = "ChiaveProgrOrigine"; 
    
    //
    public static final String CAMPO_COD_TIPO_UFFICIO                   = "CodTipoUfficioComp";     
    public static final String CAMPO_SEDE_UFFICIO                       = "SedeUfficioComp";     
   
    public static final String CAMPO_LUOGO_UFFICIO_TROVATO              = "LuogoUfficioTrovato";
    
    public static final String CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA     = "LuogoUfficioSentenzaTrovato"; // descr sede autorità emittente sentenza
    public static final String CAMPO_COD_LUOGO_UFFICIO_TROVATO_SENTENZA = "CodLuogoUfficioSentenzaTrovato";

    
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO      = "CodOperatoreInserimento"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO        = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO          = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO          = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO        = "CodUfficioInserimento"; 
    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO    = "CodOperatoreAggiornamento"; 
    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO      = "GiornoDataAggiornamento"; 
    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO        = "MeseDataAggiornamento"; 
    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO        = "AnnoDataAggiornamento"; 
    public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO      = "CodUfficioAggiornamento";
    
    public static final String CAMPO_FASCICOLO_SIEP_TROVATO      	= "fasFascicoloTrovato";
    public static final String CAMPO_SENTENZA_SIEP_TROVATO      	= "senFascicoloTrovato";
        
    public static final String CAMPO_CODICE_CUI_SOGGETTO_RICH		= "CodiceCuiSoggettoRichiesto"; 
    

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCACOMPETENZA  	= IWebConstants.ROOT_DIR + "files/siap/siep/competenza/LoadRicercaCompetenza.jsp";
    public static final String PG_LOAD_DETTAGLIOCOMPETENZA	= IWebConstants.ROOT_DIR + "files/siap/siep/competenza/DettaglioCompetenza.jsp";
    public static final String PG_RICERCACOMPETENZA       	= IWebConstants.ROOT_DIR + "files/siap/siep/competenza/RicercaCompetenza.jsp";
    public static final String PG_LOAD_INSERISCICOMPETENZA	= IWebConstants.ROOT_DIR + "files/siap/siep/competenza/LoadInserisciCompetenza.jsp";
    public static final String PG_LOAD_CANCELLACOMPETENZA 	= IWebConstants.ROOT_DIR + "files/siap/siep/competenza/DettaglioCompetenza.jsp";
    
}
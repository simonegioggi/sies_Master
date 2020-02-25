package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiPosizioneGiuridicaCumulo</p>
* <p>Description: Classe di costanti di PosizioneGiuridicaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


public interface ICostantiPosizioneGiuridicaCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_POSIZIONE_GIURIDICA_CUM     = "IdPosizioneGiuridicaCum"; 
    public static final String CAMPO_COD_POSIZIONE_GIURIDICA        = "CodPosizioneGiuridica"; 
    public static final String CAMPO_GIORNO_DATA_INIZIO             = "GiornoDataInizio"; 
    public static final String CAMPO_MESE_DATA_INIZIO               = "MeseDataInizio"; 
    public static final String CAMPO_ANNO_DATA_INIZIO               = "AnnoDataInizio"; 
    
    public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione"; 
    public static final String CAMPO_ALTRO_LUOGO                    = "AltroLuogo"; 
    
    public static final String CAMPO_CHIAVE_ANNO_FAS_SIUS           = "ChiaveAnnoFasSius"; 
    public static final String CAMPO_CHIAVE_PROGR_FAS_SIUS          = "ChiaveProgrFasSius"; 
    public static final String CAMPO_CHIAVE_UFF_FAS_SIUS            = "ChiaveUffFasSius"; 
    public static final String CAMPO_TIPO_UFF_FAS_SIUS              = "TipoUffFasSius"; 
    public static final String CAMPO_SEDE_UFF_FAS_SIUS              = "SedeUffFasSius"; 
    
    public static final String CAMPO_ANNO_REGISTRO                  = "AnnoRegistro"; 
    public static final String CAMPO_NUMERO_REGISTRO                = "NumeroRegistro"; 
    public static final String CAMPO_COD_TIPO_PROVVEDIMENTO         = "CodTipoProvvedimento";     
    public static final String CAMPO_GIORNO_DATA_EMISSIONE_PROVV    = "GiornoDataEmissioneProvv"; 
    public static final String CAMPO_MESE_DATA_EMISSIONE_PROVV      = "MeseDataEmissioneProvv"; 
    public static final String CAMPO_ANNO_DATA_EMISSIONE_PROVV      = "AnnoDataEmissioneProvv"; 
    
    public static final String CAMPO_NUM_ANNI_MISURA                = "NumAnniMisura"; 
    public static final String CAMPO_NUM_MESI_MISURA                = "NumMesiMisura"; 
    public static final String CAMPO_NUM_GIORNI_MISURA              = "NumGiorniMisura"; 
    public static final String CAMPO_GIORNO_DATA_FINE_MISURA        = "GiornoDataFineMisura"; 
    public static final String CAMPO_MESE_DATA_FINE_MISURA          = "MeseDataFineMisura"; 
    public static final String CAMPO_ANNO_DATA_FINE_MISURA          = "AnnoDataFineMisura";  
    
    public static final String CAMPO_DAT_ID_DATI_FINALI_CUMULO      = "DatIdDatiFinaliCumulo";
    public static final String CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO     = "IstrIdIstruttoriaCumulo"; 

    public static final String CAMPO_RADIO_TIPO_POS            = "campoRadioTipoPosizione"; 
    public static final String CAMPO_CHECK_TIPO_POS_LIBERO     = "tipoLibero"; 
    public static final String CAMPO_CHECK_TIPO_POS_ESPIST     = "tipoEspIst"; 
    public static final String CAMPO_CHECK_TIPO_POS_ESPALTRO   = "tipoEspAltro";
    
    public static final String CAMPO_CHECK_DECISIONE_TDS   = "DecisioneTDS";
    
    // MEV 42 - Ulteriori Requisiti - Gestione 'Differimento pena nella forma della detenzione domiciliare'.
    public static final String CAMPO_CHECK_DIFFERIMENTO_DET_DOM = "DifferimentoDetenzioneDomicilio";
    public static final String CAMPO_GIORNO_DATA_INIZIO_MISURA  = "GiornoDataInizioMisura"; 
    public static final String CAMPO_MESE_DATA_INIZIO_MISURA    = "MeseDataInizioMisura"; 
    public static final String CAMPO_ANNO_DATA_INIZIO_MISURA    = "AnnoDataInizioMisura";
    //public static final String CAMPO_FLAG_PRESENZA_QUANTUM      = "FlagPresenzaQuantum";
    
    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
//    public static final String PG_LOAD_RICERCAPOSIZIONEGIURIDICACUMULO  	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/LoadRicercaPosizioneGiuridicaCumulo.jsp";
//    public static final String PG_LOAD_DETTAGLIOPOSIZIONEGIURIDICACUMULO	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/DettaglioPosizioneGiuridicaCumulo.jsp";
//    public static final String PG_RICERCAPOSIZIONEGIURIDICACUMULO       	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/RicercaPosizioneGiuridicaCumulo.jsp";
//    public static final String PG_LOAD_INSERISCIPOSIZIONEGIURIDICACUMULO	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/LoadInserisciPosizioneGiuridicaCumulo.jsp";
//    public static final String PG_LOAD_CANCELLAPOSIZIONEGIURIDICACUMULO 	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/DettaglioPosizioneGiuridicaCumulo.jsp";
}
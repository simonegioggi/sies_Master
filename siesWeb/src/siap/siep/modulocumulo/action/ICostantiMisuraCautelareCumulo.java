package siap.siep.modulocumulo.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiMisuraCautelareCumulo</p>
* <p>Description: Classe di costanti di MisuraCautelareCumulo</p>
* @version 1.0
*/


public interface ICostantiMisuraCautelareCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_MISURA_CAUTELARE_CUMULO     = "IdMisuraCautelareCumulo"; 
    public static final String CAMPO_COD_TIPO_MISURA                = "CodTipoMisura"; 
    
    public static final String CAMPO_GIORNO_DATA_INIZIO             = "GiornoDataInizio"; 
    public static final String CAMPO_MESE_DATA_INIZIO               = "MeseDataInizio"; 
    public static final String CAMPO_ANNO_DATA_INIZIO               = "AnnoDataInizio"; 
    public static final String CAMPO_GIORNO_DATA_FINE               = "GiornoDataFine"; 
    public static final String CAMPO_MESE_DATA_FINE                 = "MeseDataFine"; 
    public static final String CAMPO_ANNO_DATA_FINE                 = "AnnoDataFine"; 
    public static final String CAMPO_NUM_ANNI                       = "NumAnni"; 
    public static final String CAMPO_NUM_MESI                       = "NumMesi"; 
    public static final String CAMPO_NUM_GIORNI                     = "NumGiorni"; 
    public static final String CAMPO_GIORNI                         = "Giorni"; 
    
    public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione"; 
    public static final String CAMPO_ALTRO_LUOGO_DETENZIONE         = "AltroLuogoDetenzione"; 
    public static final String CAMPO_AUTORITA_COMPETENTE            = "AutoritaCompetente"; 
    public static final String CAMPO_AUTORITA_COMPETENTE_SEDE       = "AutoritaCompetenteSede"; 
    public static final String CAMPO_DESCR_AUTORITA_COMPETENTE_SEDE = "DescrAutoritaCompetenteSede"; 
    public static final String CAMPO_AUTORITA_COMPETENTE_INDIRIZZO  = "AutoritaCompetenteIndirizzo"; 
    
    public static final String CAMPO_FLAG_COMPUTABILE               = "FlagComputabile"; 
    public static final String CAMPO_COD_MOTIVO_NON_COMPUTABILE     = "CodMotivoNonComputabile"; 
    public static final String CAMPO_COD_TIPO_UFFICIO_RIFER         = "CodTipoUfficioRifer"; 
    public static final String CAMPO_COD_LUOGO_UFFICIO_RIFER        = "CodLuogoUfficioRifer"; 
    public static final String CAMPO_GIORNO_DATA_FUNGIBILITA        = "GiornoDataFungibilita"; 
    public static final String CAMPO_MESE_DATA_FUNGIBILITA          = "MeseDataFungibilita"; 
    public static final String CAMPO_ANNO_DATA_FUNGIBILITA          = "AnnoDataFungibilita"; 
    public static final String CAMPO_ANNO_RIFER                     = "AnnoRifer"; 
    public static final String CAMPO_NUM_RIFER                      = "NumRifer"; 
    
    public static final String CAMPO_NOTE                           = "Note"; 
    
    public static final String CAMPO_ANNO_FASC_BDMC                 = "AnnoFascBdmc"; 
    public static final String CAMPO_NUME_FASC_BDMC                 = "NumeFascBdmc"; 
//    public static final String CAMPO_CODICE_UFFICIO_PM_SEDE         = "CodiceUfficioPmSede"; 
    public static final String CAMPO_TIPO_UFFICIO_PM_SEDE           = "TipoUfficioPmSede"; 
    public static final String CAMPO_DESCR_COMUNE_PM_SEDE           = "DescrComuneUfficioPmSede";
    public static final String CAMPO_ANNO_REGE_PM                   = "AnnoRegePm"; 
    public static final String CAMPO_NUMERO_REGE_PM                 = "NumeroRegePm";
    
    public static final String CAMPO_ANNO_RGNR                      = "AnnoRgnr"; 
    public static final String CAMPO_NUMERO_RGNR                    = "NumeroRgnr"; 
    public static final String CAMPO_ANNO_REG_GEN                   = "AnnoRegGen"; 
    public static final String CAMPO_NUMERO_REG_GEN                 = "NumeroRegGen"; 
    public static final String CAMPO_TIPO_UFFICIO_REG_GEN           = "TipoUfficioRegGen"; 
    public static final String CAMPO_AUTORITA_EMITTENTE             = "AutoritaEmittente"; 
    public static final String CAMPO_DESCR_AUTORITA_EMITTENTE_LUOGO = "DescrAutoritaEmittenteLuogo";
    public static final String CAMPO_COD_TIPO_RITO                  = "CodTipoRito";
    public static final String CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA = "GiornoDataEmissioneOrdinanza"; 
    public static final String CAMPO_MESE_DATA_EMISSIONE_ORDINANZA  = "MeseDataEmissioneOrdinanza"; 
    public static final String CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA  = "AnnoDataEmissioneOrdinanza";     
    
    public static final String CAMPO_TIT_ID_TITOLO_CUMULATO         = "TitIdTitoloCumulato"; 
    public static final String CAMPO_FLAG_STATO                     = "FlagStato"; 
    public static final String CAMPO_MOTIVO_MODIFICA                = "MotivoModifica"; 
    public static final String CAMPO_ID_MISURA_CAUTELARE_ORIGINE    = "IdMisuraCautelareOrigine"; 
    
    public static final String CAMPO_TIPO_ESPIAZIONE                = "TipoEspiazione"; 
    
    public static final String VAL_TIPO_ESPIAZIONE_ISTITUTO         = "TipoEspiazioneIstituto"; 
    public static final String VAL_TIPO_ESPIAZIONE_ALTRO            = "TipoEspiazioneAltro"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_ELENCO_MISURE_CAUTELARI_CUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/ElencoMisureCautelariCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_MISURA_CAUTELARE_CUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciMisuraCautelareCumulo.jsp";
    
    public static final String PG_DETTAGLIO_MISURA_CAUTELARE_CUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioMisuraCautelareCumulo.jsp";
    
}
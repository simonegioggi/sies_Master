package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiTitoloCumulato</p>
* <p>Description: Classe di costanti di TitoloCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiTitoloCumulato {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_TITOLO_CUMULATO             = "IdTitoloCumulato"; 
    public static final String CAMPO_COD_TIPO_PROVVEDIMENTO         = "CodTipoProvvedimento"; 
    public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA     = "GiornoDataIrrevocabilita"; 
    public static final String CAMPO_MESE_DATA_IRREVOCABILITA       = "MeseDataIrrevocabilita"; 
    public static final String CAMPO_ANNO_DATA_IRREVOCABILITA       = "AnnoDataIrrevocabilita"; 
    public static final String CAMPO_ANNO_REGE_PM                   = "AnnoRegePm"; 
    public static final String CAMPO_NUMERO_REGE_PM                 = "NumeroRegePm"; 
    public static final String CAMPO_COD_SEDE_NOTIZIA_REATO         = "CodSedeNotiziaReato"; 
    public static final String CAMPO_ANNO_REG_GEN                   = "AnnoRegGen"; 
    public static final String CAMPO_NUMERO_REG_GEN                 = "NumeroRegGen"; 
    public static final String CAMPO_TIPO_REG_GEN                   = "TipoRegGen"; 
    public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO      = "GiornoDataProvvedimento"; 
    public static final String CAMPO_MESE_DATA_PROVVEDIMENTO        = "MeseDataProvvedimento"; 
    public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO        = "AnnoDataProvvedimento"; 
    public static final String CAMPO_ANNO_SENTENZA                  = "AnnoSentenza"; 
    public static final String CAMPO_NUMERO_SENTENZA                = "NumeroSentenza"; 
    public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE    = "CodTipoAutoritaEmittente"; 
    public static final String CAMPO_COD_LUOGO_EMITTENTE            = "CodLuogoEmittente"; 
    public static final String CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE = "NumSezioneAutoritaEmittente"; 
    public static final String CAMPO_COD_TIPO_RITO                  = "CodTipoRito"; 
    public static final String CAMPO_COD_TIPO_PROVVEDIMENTO_RIF     = "CodTipoProvvedimentoRif"; 
    public static final String CAMPO_COD_TIPO_PROVV_RIF             = "CodTipoProvvRif"; 
    public static final String CAMPO_GIORNO_DATA_PROVV_RIF          = "GiornoDataProvvRif"; 
    public static final String CAMPO_MESE_DATA_PROVV_RIF            = "MeseDataProvvRif"; 
    public static final String CAMPO_ANNO_DATA_PROVV_RIF            = "AnnoDataProvvRif"; 
    public static final String CAMPO_ANNO_PROVV_RIF                 = "AnnoProvvRif"; 
    public static final String CAMPO_NUMERO_PROVV_RIF               = "NumeroProvvRif"; 
    public static final String CAMPO_COD_TIPO_AUTORITA_PROVV_RIF    = "CodTipoAutoritaProvvRif"; 
    public static final String CAMPO_COD_LUOGO_PROVV_RIF            = "CodLuogoProvvRif"; 
    public static final String CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF = "NumSezioneAutoritaProvvRif"; 
    public static final String CAMPO_COD_TIPO_RITO_RIF              = "CodTipoRitoRif"; 
    public static final String CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO   = "CodTipoProvvedimentoAltro"; 
    public static final String CAMPO_NOTE1_DECISIONE_CASSAZIONE     = "Note1DecisioneCassazione"; 
    public static final String CAMPO_NOTE2_DECISIONE_CASSAZIONE     = "Note2DecisioneCassazione"; 
    public static final String CAMPO_ANNO_SENTENZA_CASSAZIONE       = "AnnoSentenzaCassazione"; 
    public static final String CAMPO_NUMERO_SENTENZA_CASSAZIONE     = "NumeroSentenzaCassazione"; 
    public static final String CAMPO_ANNO_RACCOLTA_GENERALE         = "AnnoRaccoltaGenerale"; 
    public static final String CAMPO_NUMERO_RACCOLTA_GENERALE       = "NumeroRaccoltaGenerale"; 
    public static final String CAMPO_COD_TIPO_DECISIONE_CASSAZIONE  = "CodTipoDecisioneCassazione"; 
    public static final String CAMPO_NOTE                           = "Note"; 
    public static final String CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO     = "IstrIdIstruttoriaCumulo"; 
    public static final String CAMPO_FLAG_STATO                     = "FlagStato"; 
    public static final String CAMPO_MOTIVO_MODIFICA                = "MotivoModifica"; 
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

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCATITOLOCUMULATO  	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadRicercaTitoloCumulato.jsp";
    //FIXME nome da correggere
    public static final String PG_LOAD_DETTAGLIOTITOLOCUMULATO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioTitoloCumulatoInCumulo.jsp";
    public static final String PG_RICERCATITOLOCUMULATO       	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/RicercaTitoloCumulato.jsp";
    public static final String PG_LOAD_INSERISCITITOLOCUMULATO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciTitoloCumulato.jsp";

    // jsp inclusa nelle varie pagine
    public static final String PG_INCLUDE_DETTAGLIO_TITOLO 	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp";


}
package siap.bdmc.fascicolosiepbdmc.action;

/**
* <p>Title: ICostantiFascicoloSiepBdmc</p>
* <p>Description: Classe di costanti di FascicoloSiepBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiFascicoloSiepBdmc {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_FASCICOLO_BDMC           = "IdFascicoloBdmc"; 
    public static final String CAMPO_CHIAVE_ANNO_BDMC            = "ChiaveAnnoBdmc"; 
    public static final String CAMPO_CHIAVE_UFFICIO_BDMC         = "ChiaveUfficioBdmc"; 
    public static final String CAMPO_CHIAVE_PROGR_BDMC           = "ChiaveProgrBdmc"; 
    public static final String CAMPO_CHIAVE_ANNO_SIEP            = "ChiaveAnnoSiep"; 
    public static final String CAMPO_CHIAVE_UFFICIO_SIEP         = "ChiaveUfficioSiep"; 
    public static final String CAMPO_CHIAVE_PROGR_SIEP           = "ChiaveProgrSiep"; 
    public static final String CAMPO_FLAG_TRASMISSIONE           = "FlagTrasmissione"; 
    public static final String CAMPO_GIORNO_DATA_TRASMISSIONE    = "GiornoDataTrasmissione"; 
    public static final String CAMPO_MESE_DATA_TRASMISSIONE      = "MeseDataTrasmissione"; 
    public static final String CAMPO_ANNO_DATA_TRASMISSIONE      = "AnnoDataTrasmissione"; 
    public static final String CAMPO_GIORNO_DATA_DISATTIVAZIONE  = "GiornoDataDisattivazione"; 
    public static final String CAMPO_MESE_DATA_DISATTIVAZIONE    = "MeseDataDisattivazione"; 
    public static final String CAMPO_ANNO_DATA_DISATTIVAZIONE    = "AnnoDataDisattivazione"; 
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

    public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE   = "CodUfficioEmittente"; 
    public static final String CAMPO_COD_LUOGO_EMITTENTE   = "CodLuogoEmittente"; 
    public static final String CAMPO_DESCR_AUTEMI_BDMC   = "CodDescrEmittente"; 
    public static final String CAMPO_DESCR_AUTEMI_SIEP   = "CodDescrEmittenteSiep"; 
    public static final String CAMPO_DATA_TRASMISSIONE    = "DataTrasmissione"; 
    public static final String CAMPO_DATA_DISATTIVAZIONE  = "DataDisattivazione"; 
    public static final String CAMPO_DATA_INSERIMENTO     = "DataInserimento"; 
    public static final String CAMPO_DATA_AGGIORNAMENTO   = "DataAggiornamento"; 
    
    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCAFASCICOLOSIEPBDMC  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/fascicolosiepbdmc/LoadRicercaFascicoloSiepBdmc.jsp";
    public static final String PG_LOAD_DETTAGLIOFASCICOLOSIEPBDMC	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/fascicolosiepbdmc/DettaglioFascicoloSiepBdmc.jsp";
    public static final String PG_RICERCAFASCICOLOSIEPBDMC       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/fascicolosiepbdmc/RicercaFascicoloSiepBdmc.jsp";
    public static final String PG_LOAD_INSERISCIFASCICOLOSIEPBDMC	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/fascicolosiepbdmc/LoadInserisciFascicoloSiepBdmc.jsp";
    public static final String PG_LOAD_CANCELLAFASCICOLOSIEPBDMC 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/fascicolosiepbdmc/DettaglioFascicoloSiepBdmc.jsp";
}
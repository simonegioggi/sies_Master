package siap.siep.tipologiaorario.action;

/**
* <p>Title: ICostantiTipologiaOrario</p>
* <p>Description: Classe di costanti di TipologiaOrario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiTipologiaOrario {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_TIPOLOGIA_ORARIO         = "IdTipologiaOrario"; 
    public static final String CAMPO_ENTE_INCARICATO             = "EnteIncaricato"; 
    public static final String CAMPO_BEN_ID_BENEFICIO            = "BenIdBeneficio"; 
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
    
 
    
   //ore differenziate per giorni!! 
    public static final String CAMPO_DALLE_ORE_LUN                   = "DalleOreLun"; 
    public static final String CAMPO_ALLE_ORE_LUN                    = "AlleOreLun"; 
    public static final String CAMPO_DALLE_ORE_MAR                   = "DalleOreMar"; 
    public static final String CAMPO_ALLE_ORE_MAR                    = "AlleOreMar"; 
    public static final String CAMPO_DALLE_ORE_MER                   = "DalleOreMer"; 
    public static final String CAMPO_ALLE_ORE_MER                    = "AlleOreMer"; 
    public static final String CAMPO_DALLE_ORE_GIOV                  = "DalleOreGiov"; 
    public static final String CAMPO_ALLE_ORE_GIOV                   = "AlleOreGiov"; 
    public static final String CAMPO_DALLE_ORE_VEN                   = "DalleOreVen"; 
    public static final String CAMPO_ALLE_ORE_VEN                    = "AlleOreVen"; 
    public static final String CAMPO_DALLE_ORE_SAB                   = "DalleOreSab"; 
    public static final String CAMPO_ALLE_ORE_SAB                    = "AlleOreSab"; 
    public static final String CAMPO_DALLE_ORE_DOM                   = "DalleOreDom"; 
    public static final String CAMPO_ALLE_ORE_DOM                    = "AlleOreDom"; 
    
    
    
    public static final String CAMPO_COD_NUM_GIORNO_LUN              = "CodNumGiornoLun"; 
    public static final String CAMPO_COD_NUM_GIORNO_MAR              = "CodNumGiornoMar"; 
    public static final String CAMPO_COD_NUM_GIORNO_MER              = "CodNumGiornoMer"; 
    public static final String CAMPO_COD_NUM_GIORNO_GIOV             = "CodNumGiornoGiov"; 
    public static final String CAMPO_COD_NUM_GIORNO_VEN              = "CodNumGiornoVen"; 
    public static final String CAMPO_COD_NUM_GIORNO_SAB              = "CodNumGiornoSab"; 
    public static final String CAMPO_COD_NUM_GIORNO_DOM              = "CodNumGiornoDom"; 
    
    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCATIPOLOGIAORARIO  	= IWebConstants.ROOT_DIR + "files/siap/siep/tipologiaorario/LoadRicercaTipologiaOrario.jsp";
    public static final String PG_LOAD_DETTAGLIOTIPOLOGIAORARIO	= IWebConstants.ROOT_DIR + "files/siap/siep/tipologiaorario/DettaglioTipologiaOrario.jsp";
    public static final String PG_RICERCATIPOLOGIAORARIO       	= IWebConstants.ROOT_DIR + "files/siap/siep/tipologiaorario/RicercaTipologiaOrario.jsp";
    public static final String PG_LOAD_INSERISCITIPOLOGIAORARIO	= IWebConstants.ROOT_DIR + "files/siap/siep/tipologiaorario/LoadInserisciTipologiaOrario.jsp";
    public static final String PG_LOAD_CANCELLATIPOLOGIAORARIO 	= IWebConstants.ROOT_DIR + "files/siap/siep/tipologiaorario/DettaglioTipologiaOrario.jsp";
}
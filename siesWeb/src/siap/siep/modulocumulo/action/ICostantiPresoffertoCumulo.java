package siap.siep.modulocumulo.action;

/**
* <p>Title: </p>
* <p>Description: Classe di costanti di </p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiPresoffertoCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_MOTIVO_MODIFICA             = "MotivoModifica"; 
    public static final String CAMPO_FLAG_STATO                  = "FlagStato"; 
    
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
    
    public static final String CAMPO_ANNO_PROVV               	= "AnnoProvvedimento"; 
    public static final String CAMPO_NUMERO_PROVV               = "NumeroProvvedimento"; 
    
    public static final String CAMPO_COD_TIPO_MISURA_DET        = "CodTipoMisuraDet"; 
    public static final String CAMPO_COD_TIPO_MISURA_NONDET     = "CodTipoMisuraNonDet";
    
    public static final String CAMPO_ANNO_PROC_SIEP             = "AnnpProcedimentoSiep";
    public static final String CAMPO_NUMERO_PROC_SIEP           = "NumeroprocedimentoSiep";
    
    //========================================== 
    // PRESOFFERTO 
    //========================================== 
//    public static final String PG_ELENCO_PRESOFFERTI_CUMULO         = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoPresoffertiCumulo.jsp";
//    public static final String PG_LOAD_INSERISCI_PRESOFFERTI_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPresoffertoCumulo.jsp";
//    public static final String PG_LOAD_DETTAGLIO_PRESOFFERTI_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPresoffertoCumulo.jsp";

    //========================================== 
    // FUNGIBILITA'
    //========================================== 
    public static final String PG_ELENCO_FUNGIBILITA_CUMULO         = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoFungibilitaCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_FUNGIBILITA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciFungibilitaCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_PERIODO_FUNGIBILITA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPeriodoFungibilitaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_FUNGIBILITA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioFungibilitaCumulo.jsp";
    
    
    
    
    
}
package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiMisuraSicurezzaCumulo</p>
* <p>Description: Classe di costanti di MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiMisuraSicurezzaCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_MISURA_SICUREZZA_CUMULO  = "IdMisuraSicurezzaCumulo"; 
    public static final String CAMPO_COD_NATURA                  = "CodNatura"; 
    public static final String CAMPO_COD_TIPO                    = "CodTipo"; 
    public static final String CAMPO_NUM_ANNI                    = "NumAnni"; 
    public static final String CAMPO_NUM_MESI                    = "NumMesi"; 
    public static final String CAMPO_NUM_GIORNI                  = "NumGiorni"; 
    public static final String CAMPO_ANNO_REG_38                 = "AnnoReg38"; 
    public static final String CAMPO_NUM_REG_38                  = "NumReg38"; 
    public static final String CAMPO_MOTIVO_MODIFICA             = "MotivoModifica"; 
    public static final String CAMPO_FLAG_STATO                  = "FlagStato"; 
    public static final String CAMPO_TIT_ID_TITOLO_CUMULATO      = "TitIdTitoloCumulato"; 
    
    public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA 	 = "GiornoDataFineValidita";
    public static final String CAMPO_MESE_DATA_FINE_VALIDITA 	 = "MeseDataFineValidita";
	public static final String CAMPO_ANNO_DATA_FINE_VALIDITA 	 = "AnnoDataFineValidita";
    public static final String CAMPO_LUOGO_ESECUZIONE_MISURA 	 		= "LuogoEsecuzioneMisura";
    public static final String CAMPO_MIS_ID_MISURA_SICUREZZA_CUMULO 	= "MisIdMisuraSicurezza";
    public static final String CAMPO_ID_MISURA_SICUREZZA_ORIGINE    	= "IdMisuraSicurezzaOrigine";
    public static final String CAMPO_MIS_ID_MISURA_SICUREZZA_ORIG 		= "IdMisuraSicurezzaOrigine"; 

    public static final String CAMPO_COD_STATO_MISURA       = "CodStatoMisura"; 

    public static final String CAMPO_FLAG_DATI_FINALI       = "flagDatiFinali"; 
    public static final String CAMPO_FLAG_CREA_PROCEDIMENTO = "flagCreaProcedimento"; 
    
    
    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_ELENCO_MISURASICUREZZA_CUMULO        = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoMisuraSicurezzaCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_MISURASICUREZZA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciMisuraSicurezzaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_MISURASICUREZZA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioMisuraSicurezzaCumulo.jsp";

    public static final String PG_LOAD_RICERCAMISURASICUREZZACUMULO  	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaMisuraSicurezzaCumulo.jsp";
    public static final String PG_LOAD_CANCELLAMISURASICUREZZACUMULO 	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioMisuraSicurezzaCumulo.jsp";
}
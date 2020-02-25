package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiProcedimentoCumulato</p>
* <p>Description: Classe di costanti di ProcedimentoCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiProcedimentoCumulato {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_PROCEDIMENTO_CUMULATO       = "IdProcedimentoCumulato"; 
    public static final String CAMPO_ID_FASCICOLO_SIEP_CUMULATO     = "IdFascicoloSiepCumulato"; 
    public static final String CAMPO_CHIAVE_ANNO_FAS_CUMULATO       = "ChiaveAnnoFasCumulato"; 
    public static final String CAMPO_CHIAVE_PROGR_FAS_CUMULATO      = "ChiaveProgrFasCumulato"; 
    public static final String CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO  = "CodTipoUfficioFasCumulato"; 
    public static final String CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO = "CodLuogoUfficioFasCumulato"; 
    public static final String CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO = "DescLuogoUfficioFasCumulato"; 
    
    
    public static final String CAMPO_COD_UFFICIO_FAS_CUMULATO       = "CodUfficioFasCumulato"; 
    public static final String CAMPO_GIORNO_DATA_RICHIESTA_FASCICOLO = "GiornoDataRichiestaFascicolo"; 
    public static final String CAMPO_MESE_DATA_RICHIESTA_FASCICOLO  = "MeseDataRichiestaFascicolo"; 
    public static final String CAMPO_ANNO_DATA_RICHIESTA_FASCICOLO  = "AnnoDataRichiestaFascicolo"; 
    public static final String CAMPO_GIORNO_DATA_PERVENIMENTO_FASCICOLO = "GiornoDataPervenimentoFascicolo"; 
    public static final String CAMPO_MESE_DATA_PERVENIMENTO_FASCICOLO = "MeseDataPervenimentoFascicolo"; 
    public static final String CAMPO_ANNO_DATA_PERVENIMENTO_FASCICOLO = "AnnoDataPervenimentoFascicolo"; 
    
    
    public static final String CAMPO_FLAG_ACCORPATO                 = "FlagAccorpato"; 
    public static final String CAMPO_CHIAVE_UFFICIO_ORIGINE         = "ChiaveUfficioOrigine"; 
    public static final String CAMPO_CHIAVE_PROGR_ORIGINE           = "ChiaveProgrOrigine";     
    
    public static final String CAMPO_NOTE                           = "Note"; 
    public static final String CAMPO_TIT_ID_TITOLO_CUMULATO         = "TitIdTitoloCumulato"; 
    public static final String CAMPO_FLAG_STATO                     = "FlagStato"; 
    public static final String CAMPO_MOTIVO_MODIFICA                = "MotivoModifica"; 
    

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCAPROCEDIMENTOCUMULATO  	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadRicercaProcedimentoCumulato.jsp";
    public static final String PG_LOAD_DETTAGLIOPROCEDIMENTOCUMULATO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioProcedimentoCumulato.jsp";
    public static final String PG_RICERCAPROCEDIMENTOCUMULATO       	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/RicercaProcedimentoCumulato.jsp";
    public static final String PG_LOAD_INSERISCIPROCEDIMENTOCUMULATO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciProcedimentoCumulato.jsp";
    public static final String PG_LOAD_CANCELLAPROCEDIMENTOCUMULATO 	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioProcedimentoCumulato.jsp";
}
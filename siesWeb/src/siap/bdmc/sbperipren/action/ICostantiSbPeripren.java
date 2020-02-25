package siap.bdmc.sbperipren.action;

/**
* <p>Title: ICostantiSbPeripren</p>
* <p>Description: Classe di costanti di SbPeripren</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiSbPeripren {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_GIORNO_DATA_INIZ_PERI = "GiornoDataInizPeri"; 
    public static final String CAMPO_MESE_DATA_INIZ_PERI = "MeseDataInizPeri"; 
    public static final String CAMPO_ANNO_DATA_INIZ_PERI = "AnnoDataInizPeri"; 
    public static final String CAMPO_GIORNO_DATA_FINE_PERI = "GiornoDataFinePeri"; 
    public static final String CAMPO_MESE_DATA_FINE_PERI = "MeseDataFinePeri"; 
    public static final String CAMPO_ANNO_DATA_FINE_PERI = "AnnoDataFinePeri"; 
    public static final String CAMPO_PROG_PERI_PRES     = "ProgPeriPres"; 
    public static final String CAMPO_ID_PREN            = "IdPren"; 
    public static final String CAMPO_COD_UFFI_SIES      = "CodUffiSies"; 
    public static final String CAMPO_ANNO_FASC_SIEP     = "AnnoFascSiep"; 
    public static final String CAMPO_NUME_FASC_SIEP     = "NumeFascSiep"; 
    public static final String CAMPO_CODI_SEDE_INST     = "CodiSedeInst"; 
    public static final String CAMPO_ANNO_FASC_BDMC     = "AnnoFascBdmc"; 
    public static final String CAMPO_NUME_FASC_BDMC     = "NumeFascBdmc"; 
    public static final String CAMPO_COD_STAT_PREN_PERI = "CodStatPrenPeri"; 
    public static final String CAMPO_COD_TIPO_PERI      = "CodTipoPeri"; 
    public static final String CAMPO_GIORNO_DATA_PREN_PERI = "GiornoDataPrenPeri"; 
    public static final String CAMPO_MESE_DATA_PREN_PERI = "MeseDataPrenPeri"; 
    public static final String CAMPO_ANNO_DATA_PREN_PERI = "AnnoDataPrenPeri"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASBPERIPREN  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbperipren/LoadRicercaSbPeripren.jsp";
    public static final String PG_LOAD_DETTAGLIOSBPERIPREN	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbperipren/DettaglioSbPeripren.jsp";
    public static final String PG_RICERCASBPERIPREN       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbperipren/RicercaSbPeripren.jsp";
    public static final String PG_LOAD_INSERISCISBPERIPREN	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbperipren/LoadInserisciSbPeripren.jsp";
    public static final String PG_LOAD_CANCELLASBPERIPREN 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbperipren/DettaglioSbPeripren.jsp";
}
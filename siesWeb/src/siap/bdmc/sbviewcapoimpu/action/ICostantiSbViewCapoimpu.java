package siap.bdmc.sbviewcapoimpu.action;

/**
* <p>Title: ICostantiSbViewCapoimpu</p>
* <p>Description: Classe di costanti di SbViewCapoimpu</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiSbViewCapoimpu {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_FLAG_ARTI_0056      = "FlagArti0056"; 
    public static final String CAMPO_FLAG_ARTI_0061      = "FlagArti0061"; 
    public static final String CAMPO_ARTI_0061_COMM      = "Arti0061Comm"; 
    public static final String CAMPO_FLAG_ARTI_0081      = "FlagArti0081"; 
    public static final String CAMPO_ARTI_0081_COMM      = "Arti0081Comm"; 
    public static final String CAMPO_FLAG_ART_0110       = "FlagArt0110"; 
    public static final String CAMPO_FLAG_ARTI_0112      = "FlagArti0112"; 
    public static final String CAMPO_ARTI_0112_COMMI     = "Arti0112Commi"; 
    public static final String CAMPO_FLAG_ARTI_0113      = "FlagArti0113"; 
    public static final String CAMPO_FLAG_ARTI_0114      = "FlagArti0114"; 
    public static final String CAMPO_FLAG_ARTI_0116      = "FlagArti0116"; 
    public static final String CAMPO_FLAG_ARTI_0117      = "FlagArti0117"; 
    public static final String CAMPO_LUOG_REAT           = "LuogReat"; 
    public static final String CAMPO_FLAG_PERI_TEMP      = "FlagPeriTemp"; 
    public static final String CAMPO_GIORNO_DATA_REAT_0101 = "GiornoDataReat0101"; 
    public static final String CAMPO_MESE_DATA_REAT_0101 = "MeseDataReat0101"; 
    public static final String CAMPO_ANNO_DATA_REAT_0101 = "AnnoDataReat0101"; 
    public static final String CAMPO_GIORNO_DATA_REAT_0202 = "GiornoDataReat0202"; 
    public static final String CAMPO_MESE_DATA_REAT_0202 = "MeseDataReat0202"; 
    public static final String CAMPO_ANNO_DATA_REAT_0202 = "AnnoDataReat0202"; 
    public static final String CAMPO_DESC_PERI_TEMP      = "DescPeriTemp"; 
    public static final String CAMPO_NUME_PROG_CAPO_IMPU = "NumeProgCapoImpu"; 
    public static final String CAMPO_ID_PREN             = "IdPren"; 
    public static final String CAMPO_ANNO_FASC_BDMC      = "AnnoFascBdmc"; 
    public static final String CAMPO_NUME_FASC_BDMC      = "NumeFascBdmc"; 
    public static final String CAMPO_CODI_SEDE_INST      = "CodiSedeInst"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASBVIEWCAPOIMPU  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewcapoimpu/LoadRicercaSbViewCapoimpu.jsp";
    public static final String PG_LOAD_DETTAGLIOSBVIEWCAPOIMPU	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewcapoimpu/DettaglioSbViewCapoimpu.jsp";
    public static final String PG_RICERCASBVIEWCAPOIMPU       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewcapoimpu/RicercaSbViewCapoimpu.jsp";
    public static final String PG_LOAD_INSERISCISBVIEWCAPOIMPU	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewcapoimpu/LoadInserisciSbViewCapoimpu.jsp";
    public static final String PG_LOAD_CANCELLASBVIEWCAPOIMPU 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewcapoimpu/DettaglioSbViewCapoimpu.jsp";
}
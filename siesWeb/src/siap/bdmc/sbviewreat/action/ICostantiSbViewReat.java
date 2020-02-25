package siap.bdmc.sbviewreat.action;

/**
* <p>Title: ICostantiSbViewReat</p>
* <p>Description: Classe di costanti di SbViewReat</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiSbViewReat {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_NUME_PROG_CAPO_IMPU = "NumeProgCapoImpu"; 
    public static final String CAMPO_NUME_PROG_REAT      = "NumeProgReat"; 
    public static final String CAMPO_CODI_FONT_GIUR      = "CodiFontGiur"; 
    public static final String CAMPO_ANNO_FONT_GIUR      = "AnnoFontGiur"; 
    public static final String CAMPO_NUME_FONT_GIUR      = "NumeFontGiur"; 
    public static final String CAMPO_ARTI_FONT_GIUR      = "ArtiFontGiur"; 
    public static final String CAMPO_COMMI_ARTI_FONT     = "CommiArtiFont"; 
    public static final String CAMPO_LETT_ARTI_FONT      = "LettArtiFont"; 
    public static final String CAMPO_NUME_ARTI_FONT      = "NumeArtiFont"; 
    public static final String CAMPO_ARTI_QUAL_FONT      = "ArtiQualFont"; 
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
    public static final String PG_LOAD_RICERCASBVIEWREAT  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewreat/LoadRicercaSbViewReat.jsp";
    public static final String PG_LOAD_DETTAGLIOSBVIEWREAT	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewreat/DettaglioSbViewReat.jsp";
    public static final String PG_RICERCASBVIEWREAT       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewreat/RicercaSbViewReat.jsp";
    public static final String PG_LOAD_INSERISCISBVIEWREAT	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewreat/LoadInserisciSbViewReat.jsp";
    public static final String PG_LOAD_CANCELLASBVIEWREAT 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewreat/DettaglioSbViewReat.jsp";
}
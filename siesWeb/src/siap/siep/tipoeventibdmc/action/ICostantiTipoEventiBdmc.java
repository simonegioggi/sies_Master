package siap.siep.tipoeventibdmc.action;

/**
* <p>Title: ICostantiTipoEventiBdmc</p>
* <p>Description: Classe di costanti di TipoEventiBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiTipoEventiBdmc {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_TIPO_EVENTI_BDMC = "IdTipoEventiBdmc"; 
    public static final String CAMPO_COD_TIPO_EVENTO     = "CodTipoEvento"; 
    public static final String CAMPO_COD_PROVVEDIMENTO   = "CodProvvedimento"; 
    public static final String CAMPO_COD_MOTIVO          = "CodMotivo"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCATIPOEVENTIBDMC  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/siep/tipoeventibdmc/LoadRicercaTipoEventiBdmc.jsp";
    public static final String PG_LOAD_DETTAGLIOTIPOEVENTIBDMC	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/siep/tipoeventibdmc/DettaglioTipoEventiBdmc.jsp";
    public static final String PG_RICERCATIPOEVENTIBDMC       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/siep/tipoeventibdmc/RicercaTipoEventiBdmc.jsp";
    public static final String PG_LOAD_INSERISCITIPOEVENTIBDMC	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/siep/tipoeventibdmc/LoadInserisciTipoEventiBdmc.jsp";
    public static final String PG_LOAD_CANCELLATIPOEVENTIBDMC 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/siep/tipoeventibdmc/DettaglioTipoEventiBdmc.jsp";
}
package siap.bdmc.notifichesies.action;

/**
* <p>Title: ICostantiNotificheSies</p>
* <p>Description: Classe di costanti di NotificheSies</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiNotificheSies {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_NOTIFICHE_SIES         = "IdNotificheSies"; 
    public static final String CAMPO_ANNO_SIEP                 = "AnnoSiep"; 
    public static final String CAMPO_PROG_SIEP                 = "ProgSiep"; 
    public static final String CAMPO_UFFICIO_SIEP              = "UfficioSiep"; 
    public static final String CAMPO_ANNO_FASC_BDMC            = "AnnoFascBdmc"; 
    public static final String CAMPO_UFFICIO_FASC_BDMC         = "UfficioFascBdmc"; 
    public static final String CAMPO_NUMERO_FASC_BDMC          = "NumeroFascBdmc"; 
    public static final String CAMPO_TIPO_NOTIFICA             = "TipoNotifica"; 
    public static final String CAMPO_GIORNO_DATA_NOTIFICA      = "GiornoDataNotifica"; 
    public static final String CAMPO_MESE_DATA_NOTIFICA        = "MeseDataNotifica"; 
    public static final String CAMPO_ANNO_DATA_NOTIFICA        = "AnnoDataNotifica"; 
    public static final String CAMPO_STATO_TRASMISSIONE        = "StatoTrasmissione"; 
    public static final String CAMPO_GIORNO_DATA_TRASMISSIONE  = "GiornoDataTrasmissione"; 
    public static final String CAMPO_MESE_DATA_TRASMISSIONE    = "MeseDataTrasmissione"; 
    public static final String CAMPO_ANNO_DATA_TRASMISSIONE    = "AnnoDataTrasmissione"; 
    public static final String CAMPO_ID_PREN                   = "IdPren"; 
    public static final String CAMPO_PROG_PERI_PRES            = "ProgPeriPres"; 
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO   = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO     = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO     = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO   = "CodUfficioInserimento"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCANOTIFICHESIES  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/notifichesies/LoadRicercaNotificheSies.jsp";
    public static final String PG_LOAD_DETTAGLIONOTIFICHESIES	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/notifichesies/DettaglioNotificheSies.jsp";
    public static final String PG_RICERCANOTIFICHESIES       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/notifichesies/RicercaNotificheSies.jsp";
    public static final String PG_LOAD_INSERISCINOTIFICHESIES	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/notifichesies/LoadInserisciNotificheSies.jsp";
    public static final String PG_LOAD_CANCELLANOTIFICHESIES 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/notifichesies/DettaglioNotificheSies.jsp";
}
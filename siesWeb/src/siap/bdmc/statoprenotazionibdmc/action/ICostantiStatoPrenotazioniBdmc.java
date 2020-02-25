package siap.bdmc.statoprenotazionibdmc.action;

/**
* <p>Title: ICostantiStatoPrenotazioniBdmc</p>
* <p>Description: Classe di costanti di StatoPrenotazioniBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiStatoPrenotazioniBdmc {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_STATO_PRENOTAZIONI_BDMC  = "StatoPrenotazioniBdmc"; 
    public static final String CAMPO_ID_MISURA_CAUTELARE_BDMC = "IdMisuraCautelareBdmc"; 
    public static final String CAMPO_GIORNO_DATA_TRASMISSIONE = "GiornoDataTrasmissione"; 
    public static final String CAMPO_MESE_DATA_TRASMISSIONE   = "MeseDataTrasmissione"; 
    public static final String CAMPO_ANNO_DATA_TRASMISSIONE   = "AnnoDataTrasmissione"; 
    public static final String CAMPO_ESITO_ID                 = "EsitoId"; 
    public static final String CAMPO_ESITO_MSG                = "EsitoMsg"; 
    public static final String CAMPO_ID_PRENOTAZIONE          = "IdPrenotazione"; 
    public static final String CAMPO_PROG_PERI_PRES           = "ProgPeriPres"; 
    public static final String CAMPO_TIPO_TRASMISSIONE        = "TipoTrasmissione"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASTATOPRENOTAZIONIBDMC  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/statoprenotazionibdmc/LoadRicercaStatoPrenotazioniBdmc.jsp";
    public static final String PG_LOAD_DETTAGLIOSTATOPRENOTAZIONIBDMC	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/statoprenotazionibdmc/DettaglioStatoPrenotazioniBdmc.jsp";
    public static final String PG_RICERCASTATOPRENOTAZIONIBDMC       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/statoprenotazionibdmc/RicercaStatoPrenotazioniBdmc.jsp";
    public static final String PG_LOAD_INSERISCISTATOPRENOTAZIONIBDMC	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/statoprenotazionibdmc/LoadInserisciStatoPrenotazioniBdmc.jsp";
    public static final String PG_LOAD_CANCELLASTATOPRENOTAZIONIBDMC 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/statoprenotazionibdmc/DettaglioStatoPrenotazioniBdmc.jsp";
    public static final String PG_LOAD_DETTAGLIOMISURACAUTELAREBDMC	=     ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/statoprenotazionibdmc/DettaglioMisuraCautelareBdmc.jsp";
    public static final String PG_LOAD_DETTAGLIOFASCICOLOSIEPBDMC	=     ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/statoprenotazionibdmc/DettaglioFascicoloSiepBdmc.jsp";
    
}
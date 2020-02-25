package siap.bdmc.sbviewnotifiche.action;

/**
* <p>Title: ICostantiSbViewNotifiche</p>
* <p>Description: Classe di costanti di SbViewNotifiche</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiSbViewNotifiche {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_PROG_NOTI           = "ProgNoti"; 
    public static final String CAMPO_CODI_NOTI           = "CodiNoti"; 
    public static final String CAMPO_DESCRIZIONE         = "Descrizione"; 
    public static final String CAMPO_GIORNO_DATA_INVI_NOTI = "GiornoDataInviNoti"; 
    public static final String CAMPO_MESE_DATA_INVI_NOTI = "MeseDataInviNoti"; 
    public static final String CAMPO_ANNO_DATA_INVI_NOTI = "AnnoDataInviNoti"; 
    public static final String CAMPO_GIORNO_DATA_REGI_NOTI = "GiornoDataRegiNoti"; 
    public static final String CAMPO_MESE_DATA_REGI_NOTI = "MeseDataRegiNoti"; 
    public static final String CAMPO_ANNO_DATA_REGI_NOTI = "AnnoDataRegiNoti"; 
    public static final String CAMPO_GIORNO_DATA_VALI_NOTI = "GiornoDataValiNoti"; 
    public static final String CAMPO_MESE_DATA_VALI_NOTI = "MeseDataValiNoti"; 
    public static final String CAMPO_ANNO_DATA_VALI_NOTI = "AnnoDataValiNoti"; 
    public static final String CAMPO_NOTE                = "Note"; 
    public static final String CAMPO_CODI_UFFI_SIES      = "CodiUffiSies"; 
    public static final String CAMPO_CODI_UFFI           = "CodiUffi"; 
    public static final String CAMPO_FLAG_STAT_NOTI      = "FlagStatNoti"; 
    public static final String CAMPO_GIORNO_DATA_CHIU_NOTI = "GiornoDataChiuNoti"; 
    public static final String CAMPO_MESE_DATA_CHIU_NOTI = "MeseDataChiuNoti"; 
    public static final String CAMPO_ANNO_DATA_CHIU_NOTI = "AnnoDataChiuNoti"; 
    public static final String CAMPO_FLAG_TRAS           = "FlagTras"; 
    public static final String CAMPO_STOP_ANNO_FASC_BDMC = "StopAnnoFascBdmc"; 
    public static final String CAMPO_STOP_NUME_FASC_BDMC = "StopNumeFascBdmc"; 
    public static final String CAMPO_UTEN_SIES           = "UtenSies"; 
    public static final String CAMPO_ID_PREN             = "IdPren"; 
    public static final String CAMPO_PROG_PERI           = "ProgPeri"; 
    public static final String CAMPO_MODI_ANNO_FASC_BDMC = "ModiAnnoFascBdmc"; 
    public static final String CAMPO_MODI_NUME_FASC_BDMC = "ModiNumeFascBdmc"; 
    public static final String CAMPO_FLAG_MODI           = "FlagModi"; 
    public static final String CAMPO_GIORNO_DATA_INIZ    = "GiornoDataIniz"; 
    public static final String CAMPO_MESE_DATA_INIZ      = "MeseDataIniz"; 
    public static final String CAMPO_ANNO_DATA_INIZ      = "AnnoDataIniz"; 
    public static final String CAMPO_GIORNO_DATA_FINE    = "GiornoDataFine"; 
    public static final String CAMPO_MESE_DATA_FINE      = "MeseDataFine"; 
    public static final String CAMPO_ANNO_DATA_FINE      = "AnnoDataFine"; 
    public static final String CAMPO_GIORNO_DATA_INIZ_PREC = "GiornoDataInizPrec"; 
    public static final String CAMPO_MESE_DATA_INIZ_PREC = "MeseDataInizPrec"; 
    public static final String CAMPO_ANNO_DATA_INIZ_PREC = "AnnoDataInizPrec"; 
    public static final String CAMPO_GIORNO_DATA_FINE_PREC = "GiornoDataFinePrec"; 
    public static final String CAMPO_MESE_DATA_FINE_PREC = "MeseDataFinePrec"; 
    public static final String CAMPO_ANNO_DATA_FINE_PREC = "AnnoDataFinePrec"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASBVIEWNOTIFICHE  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewnotifiche/LoadRicercaSbViewNotifiche.jsp";
    public static final String PG_LOAD_DETTAGLIOSBVIEWNOTIFICHE	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewnotifiche/DettaglioSbViewNotifiche.jsp";
    public static final String PG_RICERCASBVIEWNOTIFICHE       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewnotifiche/RicercaSbViewNotifiche.jsp";
    public static final String PG_LOAD_INSERISCISBVIEWNOTIFICHE	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewnotifiche/LoadInserisciSbViewNotifiche.jsp";
    public static final String PG_LOAD_CANCELLASBVIEWNOTIFICHE 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewnotifiche/DettaglioSbViewNotifiche.jsp";

}
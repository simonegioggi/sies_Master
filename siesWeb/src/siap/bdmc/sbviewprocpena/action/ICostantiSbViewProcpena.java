package siap.bdmc.sbviewprocpena.action;

/**
* <p>Title: ICostantiSbViewProcpena</p>
* <p>Description: Classe di costanti di SbViewProcpena</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;

public interface ICostantiSbViewProcpena {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_CODI_UFFI_PMPM      = "CodiUffiPmpm"; 
    public static final String CAMPO_ANNO_REGI_PMPM      = "AnnoRegiPmpm"; 
    public static final String CAMPO_NUME_REGI_PMPM      = "NumeRegiPmpm"; 
    public static final String CAMPO_CODI_UFFI_GIPP      = "CodiUffiGipp"; 
    public static final String CAMPO_ANNO_REGI_GIPP      = "AnnoRegiGipp"; 
    public static final String CAMPO_NUME_REGI_GIPP      = "NumeRegiGipp"; 
    public static final String CAMPO_CODI_UFFI_DIBB      = "CodiUffiDibb"; 
    public static final String CAMPO_ANNO_REGI_DIBB      = "AnnoRegiDibb"; 
    public static final String CAMPO_NUME_REGI_DIBB      = "NumeRegiDibb"; 
    public static final String CAMPO_CODI_UFFI_COAP      = "CodiUffiCoap"; 
    public static final String CAMPO_NUME_REGI_COAP      = "NumeRegiCoap"; 
    public static final String CAMPO_ANNO_REGI_COAP      = "AnnoRegiCoap"; 
    public static final String CAMPO_GIORNO_DATA_PASS_GIUD = "GiornoDataPassGiud"; 
    public static final String CAMPO_MESE_DATA_PASS_GIUD = "MeseDataPassGiud"; 
    public static final String CAMPO_ANNO_DATA_PASS_GIUD = "AnnoDataPassGiud"; 
    public static final String CAMPO_FLAG_RECL_ARRE_GIGU = "FlagReclArreGigu"; 
    public static final String CAMPO_ANNI_PENA_GIGU      = "AnniPenaGigu"; 
    public static final String CAMPO_MESI_PENA_GIGU      = "MesiPenaGigu"; 
    public static final String CAMPO_GIOR_PENA_GIGU      = "GiorPenaGigu"; 
    public static final String CAMPO_GIORNO_DATA_SENT_1GRA = "GiornoDataSent1gra"; 
    public static final String CAMPO_MESE_DATA_SENT_1GRA = "MeseDataSent1gra"; 
    public static final String CAMPO_ANNO_DATA_SENT_1GRA = "AnnoDataSent1gra"; 
    public static final String CAMPO_ANNO_SENT_1GRA      = "AnnoSent1gra"; 
    public static final String CAMPO_NUME_SENT_1GRA      = "NumeSent1gra"; 
    public static final String CAMPO_GIORNO_DATA_SENT_2GRA = "GiornoDataSent2gra"; 
    public static final String CAMPO_MESE_DATA_SENT_2GRA = "MeseDataSent2gra"; 
    public static final String CAMPO_ANNO_DATA_SENT_2GRA = "AnnoDataSent2gra"; 
    public static final String CAMPO_ANNO_SENT_2GRA      = "AnnoSent2gra"; 
    public static final String CAMPO_NUME_SENT_2GRA      = "NumeSent2gra"; 
    public static final String CAMPO_GIORNO_DATA_SENT_GIPP_GUPP = "GiornoDataSentGippGupp"; 
    public static final String CAMPO_MESE_DATA_SENT_GIPP_GUPP = "MeseDataSentGippGupp"; 
    public static final String CAMPO_ANNO_DATA_SENT_GIPP_GUPP = "AnnoDataSentGippGupp"; 
    public static final String CAMPO_NUME_SENT_GIPP_GUPP = "NumeSentGippGupp"; 
    public static final String CAMPO_ANNO_SENT_GIPP_GUPP = "AnnoSentGippGupp"; 
    public static final String CAMPO_ANNI_PENA_DIBA      = "AnniPenaDiba"; 
    public static final String CAMPO_MESI_PENA_DIBA      = "MesiPenaDiba"; 
    public static final String CAMPO_GIOR_PENA_DIBA      = "GiorPenaDiba"; 
    public static final String CAMPO_ANNI_PENA_APPE      = "AnniPenaAppe"; 
    public static final String CAMPO_MESI_PENA_APPE      = "MesiPenaAppe"; 
    public static final String CAMPO_GIOR_PENA_APPE      = "GiorPenaAppe"; 
    public static final String CAMPO_FLAG_RECL_ARRE_DIBA = "FlagReclArreDiba"; 
    public static final String CAMPO_FLAG_RECL_ARRE_APPE = "FlagReclArreAppe"; 
    public static final String CAMPO_FLAG_ARTI_0089      = "FlagArti0089"; 
    public static final String CAMPO_FLAG_ARTI_0090      = "FlagArti0090"; 
    public static final String CAMPO_FLAG_ARTI_0091      = "FlagArti0091"; 
    public static final String CAMPO_FLAG_ARTI_0092      = "FlagArti0092"; 
    public static final String CAMPO_FLAG_ARTI_0093      = "FlagArti0093"; 
    public static final String CAMPO_FLAG_ARTI_0094      = "FlagArti0094"; 
    public static final String CAMPO_FLAG_ARTI_0095      = "FlagArti0095"; 
    public static final String CAMPO_FLAG_ARTI_0096      = "FlagArti0096"; 
    public static final String CAMPO_FLAG_ARTI_0097      = "FlagArti0097"; 
    public static final String CAMPO_FLAG_ARTI_0098      = "FlagArti0098"; 
    public static final String CAMPO_FLAG_ARTI_0099      = "FlagArti0099"; 
    public static final String CAMPO_FLAG_ARTI_62        = "FlagArti62"; 
    public static final String CAMPO_ARTI_0062_COMM      = "Arti0062Comm"; 
    public static final String CAMPO_FLAG_ART_62BI       = "FlagArt62bi"; 
    public static final String CAMPO_CODI_MISU_CUST      = "CodiMisuCust"; 
    public static final String CAMPO_CODI_ISTI_PENA      = "CodiIstiPena"; 
    public static final String CAMPO_DESC_LUOG           = "DescLuog"; 
    public static final String CAMPO_ID_PREN             = "IdPren"; 
    public static final String CAMPO_CODI_SEDE_INST      = "CodiSedeInst"; 
    public static final String CAMPO_NUME_FASC_BDMC      = "NumeFascBdmc"; 
    public static final String CAMPO_ANNO_FASC_BDMC      = "AnnoFascBdmc"; 
    public static final String CAMPO_FLAG_INFO_SELE      = "FlagInfoSele"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 
    public static final String CAMPO_GIORNO_DATA_SENTENZA    = "GiornoDataSentenza";
    public static final String CAMPO_MESE_DATA_SENTENZA      = "MeseDataSentenza";
    public static final String CAMPO_ANNO_DATA_SENTENZA      = "AnnoDataSentenza";
    public static final String CAMPO_ANNO_DATA_IRREVOCABILITA      = "AnnoDataIrrevocabilita";
    public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA      = "GiornoDataIrrevocabilita";
    public static final String CAMPO_MESE_DATA_IRREVOCABILITA      = "MeseDataIrrevocabilita";
    public static final String CAMPO_GIORNO_DATA_ARRIVO_ATTO      = "GiornoDataArrivoAtto";
    public static final String CAMPO_ANNO_DATA_ARRIVO_ATTO      = "AnnoDataArrivoAtto";
    public static final String CAMPO_MESE_DATA_ARRIVO_ATTO      = "MeseDataArrivoAtto";
    public static final String CAMPO_SBVIEW_PROCPENA = "SbViewProcpena";
    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASBVIEWPROCPENA  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewprocpena/LoadRicercaSbViewProcpena.jsp";
    public static final String PG_LOAD_DETTAGLIOSBVIEWPROCPENA	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewprocpena/DettaglioSbViewProcpena.jsp";
    public static final String PG_RICERCASBVIEWPROCPENA       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewprocpena/RicercaSbViewProcpena.jsp";
    public static final String PG_LOAD_INSERISCISBVIEWPROCPENA	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewprocpena/LoadInserisciSbViewProcpena.jsp";
    public static final String PG_LOAD_CANCELLASBVIEWPROCPENA 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbviewprocpena/DettaglioSbViewProcpena.jsp";
}
package siap.bdmc.sbpren.action;

/**
* <p>Title: ICostantiSbPren</p>
* <p>Description: Classe di costanti di SbPren</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;

public interface ICostantiSbPren {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_GIORNO_DATA_PREN    = "GiornoDataPren"; 
    public static final String CAMPO_MESE_DATA_PREN      = "MeseDataPren"; 
    public static final String CAMPO_ANNO_DATA_PREN      = "AnnoDataPren"; 
    public static final String CAMPO_ID_PREN             = "IdPren"; 
    public static final String CAMPO_UTEN_SIES           = "UtenSies"; 
    public static final String CAMPO_NOTE                = "Note"; 
    public static final String CAMPO_GIORNO_DALLA_DATA   = "GiornoDallaData"; 
    public static final String CAMPO_MESE_DALLA_DATA     = "MeseDallaData"; 
    public static final String CAMPO_ANNO_DALLA_DATA     = "AnnoDallaData"; 
    public static final String CAMPO_GIORNO_ALLA_DATA    = "GiornoAllaData"; 
    public static final String CAMPO_MESE_ALLA_DATA      = "MeseAllaData"; 
    public static final String CAMPO_ANNO_ALLA_DATA      = "AnnoAllaData"; 
    public static final String CAMPO_GIORNO_DATA_ANNU    = "GiornoDataAnnu"; 
    public static final String CAMPO_MESE_DATA_ANNU      = "MeseDataAnnu"; 
    public static final String CAMPO_ANNO_DATA_ANNU      = "AnnoDataAnnu"; 
    public static final String CAMPO_MOTI_ANNU           = "MotiAnnu"; 
    public static final String CAMPO_FLAG_PREN           = "FlagPren"; 
    public static final String CAMPO_FLAG_SELE_CAPO_IMPU = "FlagSeleCapoImpu"; 
    public static final String CAMPO_FLAG_SELE_PROC_PENA = "FlagSeleProcPena"; 
    public static final String CAMPO_FLAG_SELE_SENT      = "FlagSeleSent"; 
    public static final String CAMPO_FLAG_PREN_PRES      = "FlagPrenPres"; 
    public static final String CAMPO_CODI_UFFI_SIES      = "CodiUffiSies"; 
    public static final String CAMPO_FLAG_SELE_PERI_COMP = "FlagSelePeriComp"; 
    public static final String CAMPO_NUME_FASC_BDMC      = "NumeFascBdmc"; 
    public static final String CAMPO_ANNO_FASC_BDMC      = "AnnoFascBdmc"; 
    public static final String CAMPO_CODI_SEDE_INST      = "CodiSedeInst"; 
    public static final String CAMPO_COGN_SOGG           = "CognSogg"; 
    public static final String CAMPO_NOME_SOGG           = "NomeSogg"; 
    public static final String CAMPO_FLAG_SESS           = "FlagSess"; 
    public static final String CAMPO_CODI_STAT           = "CodiStat"; 
    public static final String CAMPO_LUOG_NASC           = "LuogNasc"; 
    public static final String CAMPO_CODI_IDEN_AFIS      = "CodiIdenAfis"; 
    public static final String CAMPO_GIORNO_DATA_NASC    = "GiornoDataNasc"; 
    public static final String CAMPO_MESE_DATA_NASC      = "MeseDataNasc"; 
    public static final String CAMPO_ANNO_DATA_NASC      = "AnnoDataNasc"; 
    public static final String CAMPO_FLAG_SELE_CIRC_SOGG = "FlagSeleCircSogg"; 
    public static final String CAMPO_SOGGETTO_OMONIMO = "Omonimo";
    public static final String CAMPO_GIORNO_ISCRIZIONE_ATTI = "GiornoIscrizioneAtti";
    public static final String CAMPO_MESE_ISCRIZIONE_ATTI = "MeseIscrizioneAtti";
    public static final String CAMPO_ANNO_ISCRIZIONE_ATTI = "AnnoIscrizioneAtti";
     public static final String CAMPO_AUTORITA = "AutoritaEmittente";
    public static final String CAMPO_LUOGO_AUTORITA = "LuogoEmittente";

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 
    public static final String CAMPO_CHECK_PROCDIMENTO = "CheckProcedimento";
    public static final String CAMPO_CHECK_SENTENZA = "CheckSentenza";
    public static final String CAMPO_CHECK_REATO = "CheckReato";
    public static final String CAMPO_CHECK_CIRCOSTANZA = "CheckCircostanza";
    public static final String CAMPO_CHECK_PERIODI = "CheckPeriodi";
    public static final String CAMPO_CHECK_PERIODI_NC = "CheckPeriodiNC";

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASBPREN  	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbpren/LoadRicercaSbPren.jsp";
    public static final String PG_LOAD_DETTAGLIOSBPREN	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbpren/DettaglioSbPren.jsp";
    public static final String PG_LOAD_DETTAGLIOPRENOTAZIONE	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbpren/DettaglioPrenotazione.jsp";
    public static final String PG_LOAD_DETTAGLIOSOGGETTO = ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbpren/DettaglioSbSoggetto.jsp";
    public static final String PG_RICERCASBPREN       	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbpren/RicercaSbPren.jsp";
    public static final String PG_LOAD_INSERISCISBPREN	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbpren/LoadInserisciSbPren.jsp";
    public static final String PG_LOAD_CANCELLASBPREN 	= ISIAPCostantiWeb.ROOT_DIR + "files/siap/bdmc/sbpren/DettaglioSbPren.jsp";
    public static final String PG_INSERISCIFASCICOLO = IWebConstants.ROOT_DIR + "files/siap/bdmc/sbpren/LoadInserimentoProcedimento.jsp";
    public static final String PG_RIASSUNTO_INTEGRAZIONE = IWebConstants.ROOT_DIR + "files/siap/bdmc/sbpren/RiassuntoIntegrazione.jsp";
    public static final String PG_COLLEGAMENTO_BDMC = IWebConstants.ROOT_DIR + "files/siap/bdmc/sbpren/CollegamentoBdmc.jsp";
    public static final String ID_FILE = "IdFile";
    public static final String CAMPO_ID_FILE = "IdFile";
    public static final String PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE = IWebConstants.ROOT_DIR + "files/siap/bdmc/sbpren/DettaglioSbSoggettoInclude.jsp";
    public static final String PAGE_DETTAGLIO_PROVVEDIMENTO_SOLO_INCLUDE = IWebConstants.ROOT_DIR + "files/siap/bdmc/sbpren/DettaglioSbPrenInclude.jsp";
    public static final String PAGE_BUTTONS_BDMC = IWebConstants.ROOT_DIR + "files/siap/bdmc/buttonsSb.jsp";
    public static final String PG_TOOLBAR_BDMC_HEADER = IWebConstants.ROOT_DIR + "files/siap/bdmc/toolbarSbheader.jsp";
    public static final String PG_ESITO_IMPORT = IWebConstants.ROOT_DIR + "files/siap/bdmc/sbpren/EsitoImport.jsp";
    public static final String CAMPO_ENTITA_CHIAVE_UNO = "ChiaveUno";
    public static final String CAMPO_VALORE_CHIAVE_UNO = "ValoreUno";
    public static final String CAMPO_ENTITA_CHIAVE_DUE = "ChiaveDue";
    public static final String CAMPO_VALORE_CHIAVE_DUE = "ValoreDue";
    public static final String CAMPO_ENTITA_CHIAVE_TRE = "ChiaveTre";
    public static final String CAMPO_VALORE_CHIAVE_TRE = "ValoreTre";
    public static final String CAMPO_ENTITA_CHIAVE_QUATTRO = "ChiaveQuattro";
    public static final String CAMPO_VALORE_CHIAVE_QUATTRO = "ValoreQuattro";
    public static final String CAMPO_TORNA_INDIETRO = "TornaIndietro";
    public static final String CAMPO_GIORNO_DATA_ARRIVO_ATTO = "GiornoDataArrivoAtto";
    public static final String CAMPO_MESE_DATA_ARRIVO_ATTO = "MeseDataArrivoAtto";
    public static final String CAMPO_ANNO_DATA_ARRIVO_ATTO = "AnnoDataArrivoAtto";
    public static final String CAMPO_GIORNO_DATA_IRR = "GiornoDataIrr";
    public static final String CAMPO_MESE_DATA_IRR = "MeseDataIrr";
    public static final String CAMPO_ANNO_DATA_IRR = "AnnoDataArrivoIrr";
    public static final String CAMPO_FLAG_INFO_SELE      = "FlagInfoSele";
    public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO = "GiornoDataProvvedimento";
    public static final String CAMPO_MESE_DATA_PROVVEDIMENTO = "MeseDataProvvedimento";
    public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO = "AnnoDataProvvedimento";
    public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA = "GiornoDataIrrevocabilita";
    public static final String CAMPO_MESE_DATA_IRREVOCABILITA = "MeseDataIrrevocabilita";
    public static final String CAMPO_ANNO_DATA_IRREVOCABILITA = "AnnoDataIrrevocabilita";

    //========================================== 
    // Costanti MISURE DI CUSTODIA  
    //========================================== 
    public static final String MISU_CUST_CUSTODIA_CAUTELARE_IN_CARCERE ="CUSTODIA CAUTELARE IN CARCERE";
    public static final String MISU_CUST_CUSTODIA_CAUTELARE_IN_LUOGO_DI_CURA="CUSTODIA CAUTELARE IN LUOGO DI CURA";
    public static final String MISU_CUST_ARRESTI_DOMICILIARI="ARRESTI DOMICILIARI";
    public static final String MISU_CUST_OSPEDALE_PSICHIATRICO="OSPEDALE PSICHIATRICO";
    public static final String MISU_CUST_CASA_CURA_E_CUSTODIA="CASA CURA E CUSTODIA";
    
    public static final String ESITO_POSITIVO = "0000";
    public static final String ESITO_DATO_PRESENTE = "1111";

    public static final String ESITO_ERRORE = "9999";

}
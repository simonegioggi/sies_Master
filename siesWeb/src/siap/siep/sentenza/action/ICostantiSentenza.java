package siap.siep.sentenza.action;

import siap.siep.web.ISIEPCostantiWeb;

public interface ICostantiSentenza extends ISIEPCostantiWeb
{
	
  public static final String CAMPO_ANNO_RGNR_INIZIALE = "AnnoRGNRIniziale";
  public static final String CAMPO_NUMERO_RGNR_INIZIALE = "NumeroRGNRIniziale";

  public static final String CAMPO_ANNO_RGNR_FINALE = "AnnoRGNRFinale";
  public static final String CAMPO_NUMERO_RGNR_FINALE = "NumeroRGNRFinale";
  
  public static final String CAMPO_ID_SENTENZA = "IdSentenza";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
  public static final String CAMPO_ANNO_REGISTRO_GENERALE = "AnnoRegistroGenerale";
  public static final String CAMPO_NUMERO_REGISTRO_GENERALE = "NumeroRegistroGenerale";
  public static final String CAMPO_ANNO_REGE_PM = "AnnoRegePm";
  public static final String CAMPO_NUMERO_REGE_PM = "NumeroRegePm";
  public static final String CAMPO_GIORNO_DATA_ARRIVO_ATTO = "GiornoDataArrivoAtto";
  public static final String CAMPO_MESE_DATA_ARRIVO_ATTO = "MeseDataArrivoAtto";
  public static final String CAMPO_ANNO_DATA_ARRIVO_ATTO = "AnnoDataArrivoAtto";
  public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO = "GiornoDataProvvedimento";
  public static final String CAMPO_MESE_DATA_PROVVEDIMENTO = "MeseDataProvvedimento";
  public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO = "AnnoDataProvvedimento";
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE = "CodTipoAutoritaEmittente";
  public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
  public static final String CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE = "NumSezioneAutoritaEmittente";
  public static final String CAMPO_ANNO_SENTENZA = "AnnoSentenza";
  public static final String CAMPO_NUMERO_SENTENZA = "NumeroSentenza";
  public static final String CAMPO_ANNO_PROVVEDIMENTO = "AnnoProvvedimento";
  public static final String CAMPO_NUMERO_PROVVEDIMENTO = "NumeroProvvedimento";
  public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA = "GiornoDataIrrevocabilita";
  public static final String CAMPO_MESE_DATA_IRREVOCABILITA = "MeseDataIrrevocabilita";
  public static final String CAMPO_ANNO_DATA_IRREVOCABILITA = "AnnoDataIrrevocabilita";
  public static final String CAMPO_GIORNO_DATA_ISCRIZIONE = "GiornoDataIscrizione";
  public static final String CAMPO_MESE_DATA_ISCRIZIONE = "MeseDataIscrizione";
  public static final String CAMPO_ANNO_DATA_ISCRIZIONE = "AnnoDataIscrizione";
 
  public static final String CAMPO_FLAG_SENTENZA_APPLICAZ_PENA = "FlagSentenzaApplicazPena";
  public static final String CAMPO_COD_TIPO_PROVV_RIF = "CodTipoProvvRif";
  public static final String CAMPO_GIORNO_DATA_PROVV_RIF = "GiornoDataProvvRif";
  public static final String CAMPO_MESE_DATA_PROVV_RIF = "MeseDataProvvRif";
  public static final String CAMPO_ANNO_DATA_PROVV_RIF = "AnnoDataProvvRif";
  public static final String CAMPO_COD_TIPO_AUTORITA_PROVV_RIF = "CodTipoAutoritaProvvRif";
  public static final String CAMPO_ANNO_PROVV_RIF = "AnnoProvvRif";
  public static final String CAMPO_NUMERO_PROVV_RIF = "NumeroProvvRif";
  public static final String CAMPO_COD_LUOGO_PROVV_RIF = "CodLuogoProvvRif";
  public static final String CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF = "NumSezioneAutoritaProvvRif";
  public static final String CAMPO_COD_TIPO_DECISIONE_CASSAZIONE = "CodTipoDecisioneCassazione";
  public static final String CAMPO_NOTE1_DECISIONE_CASSAZIONE = "Note1DecisioneCassazione";
  public static final String CAMPO_NOTE2_DECISIONE_CASSAZIONE = "Note2DecisioneCassazione";
  public static final String CAMPO_ANNO_SENTENZA_CASSAZIONE = "AnnoSentenzaCassazione";
  public static final String CAMPO_NUMERO_SENTENZA_CASSAZIONE = "NumeroSentenzaCassazione";
  public static final String CAMPO_ANNO_RACCOLTA_GENERALE = "AnnoRaccoltaGenerale";
  public static final String CAMPO_NUMERO_RACCOLTA_GENERALE = "NumeroRaccoltaGenerale";
  public static final String CAMPO_FLAG_ALTRE_SENTENZE = "FlagAltreSentenze";
  public static final String CAMPO_DESCR_ALTRE_SENTENZE = "DescrAltreSentenze";
  public static final String CAMPO_ANNO_REGISTRO_35 = "AnnoRegistro35";
  public static final String CAMPO_NUM_REGISTRO_35 = "NumRegistro35";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_DESCR_NUM_CAMPIONE_PENALE = "DescrNumCampionePenale";
  public static final String CAMPO_ANNO_REGE_GIP             = "AnnoRegeGip";
  public static final String CAMPO_NUMERO_REGE_GIP           = "NumeroRegeGip";
  public static final String CAMPO_ANNO_REGE_DIB_CAS         = "AnnoRegeDibCas";
  public static final String CAMPO_NUMERO_REGE_DIB_CAS       = "NumeroRegeDibCas";
  public static final String CAMPO_ANNO_REGE_CAS             = "AnnoRegeCas";
  public static final String CAMPO_NUMERO_REGE_CAS           = "NumeroRegeCas";
  public static final String CAMPO_ANNO_REGE_CAP_CASAP       = "AnnoRegeCapCasap";
  public static final String CAMPO_NUMERO_REGE_CAP_CASAP     = "NumeroRegeCapCasap";
  public static final String CAMPO_ANNO_REGE_CASAP           = "AnnoRegeCasap";
  public static final String CAMPO_NUMERO_REGE_CASAP         = "NumeroRegeCasap";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO   = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO     = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO     = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO   = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO   = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO   = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
  public static final String CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE = "CodBilanciamentoCircostanze";
  public static final String CAMPO_FLAG_GIUDIZIO_ABBREVIATO = "FlagGiudizioAbbreviato";
  public static final String CAMPO_SEDE_PM 					= "SedePM";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO_RIF 	= "CodTipoProvvedimentoRif";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO	= "CodTipoProvvedimentoAltro";
  public static final String CAMPO_SEDE_NOTIZIA_REATO			= "SedeNotiziaReato";

  public static final String CAMPO_DA_ANNO_PROVVEDIMENTO   = "CampoDaAnnoProvvedimento";
  public static final String CAMPO_DA_MESE_PROVVEDIMENTO   = "CampoDaMeseProvvedimento";
  public static final String CAMPO_DA_GIORNO_PROVVEDIMENTO = "CampoDaGiornoProvvedimento";

  public static final String CAMPO_A_ANNO_PROVVEDIMENTO    = "CampoAAnnoProvvedimento";
  public static final String CAMPO_A_MESE_PROVVEDIMENTO    = "CampoAMeseProvvedimento";
  public static final String CAMPO_A_GIORNO_PROVVEDIMENTO  = "CampoAGiornoProvvedimento";

/********************************MODIFICHE 26 MARZO 03*************************************************/
  public static final String CAMPO_DA_GIORNO_IRREVOCABILITA = "GiornoDaIrrevocabilita";
  public static final String CAMPO_DA_MESE_IRREVOCABILITA = "MeseDaIrrevocabilita";
  public static final String CAMPO_DA_ANNO_IRREVOCABILITA = "AnnoDaIrrevocabilita";

  public static final String CAMPO_A_GIORNO_IRREVOCABILITA = "GiornoAIrrevocabilita";
  public static final String CAMPO_A_MESE_IRREVOCABILITA = "MeseAIrrevocabilita";
  public static final String CAMPO_A_ANNO_IRREVOCABILITA = "AnnoAIrrevocabilita";

  /**********************************************************************************/
  /************************DECRETO***************************************************/
  public static final String CAMPO_ANNO_REGE_PM_D = "AnnoRegePmD";
  public static final String CAMPO_NUMERO_REGE_PM_D = "NumeroRegePmD";
  public static final String CAMPO_SEDE_NOTIZIA_REATO_D = "SedeNotiziaReatoD";
   
  public static final String CAMPO_ANNO_REGE_GIP_D  = "AnnoRegeGipD";
  public static final String CAMPO_NUMERO_REGE_GIP_D  = "NumeroRegeGipD";
  public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO_D = "GiornoDataProvvedimentoD";   
  public static final String CAMPO_MESE_DATA_PROVVEDIMENTO_D = "MeseDataProvvedimentoD";
  public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO_D = "annoDataProvvedimentoD";
  public static final String CAMPO_ANNO_SENTENZA_D= "AnnoSentenzaD";
  public static final String CAMPO_NUMERO_SENTENZA_D = "NumeroSentenzaD";
  

  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D = "CodTipoAutoritaEmittenteD";   
  public static final String CAMPO_COD_LUOGO_EMITTENTE_D = "CodLuogoEmittenteD";
 
  
  public static final String CAMPO_ANNO_SENTENZA_CASSAZIONE_D = "AnnoSentenzaCassazioneD";
  public static final String CAMPO_NUMERO_SENTENZA_CASSAZIONE_D = "NumeroSentenzaCassazioneD";
  public static final String CAMPO_ANNO_RACCOLTA_GENERALE_D = "AnnoRaccoltaGeneraleD";
  public static final String CAMPO_NUMERO_RACCOLTA_GENERALE_D= "NumeroRaccoltaGeneraleD";
  
  public static final String CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_D = "NumSezioneAutoritaEmittenteD";
  public static final String CAMPO_COD_TIPO_DECISIONE_CASSAZIONE_D = "CodTipoDecisioneCassazioneD";  
  public static final String CAMPO_NOTE_D = "NoteD"; 



  /*************************FINE DECRETO********************************************/ 
  /************************SENTENZA STRANIERA***************************************/
  
  public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS = "GiornoDataProvvedimentoSS";
  public static final String CAMPO_MESE_DATA_PROVVEDIMENTO_SS = "MeseDataProvvedimentoSS";
  public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO_SS = "AnnoDataProvvedimentoSS";
   
  public static final String CAMPO_ANNO_SENTENZA_SS = "AnnoSentenzaSS";
  public static final String CAMPO_NUMERO_SENTENZA_SS = "NumeroSentenzaSS";
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS = "CodTipoAutoritaEmittenteSS";   
  
  public static final String CAMPO_COD_LUOGO_EMITTENTE_SS = "CodLuogoEmittenteSS";
  public static final String CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_SS = "NumSezioneAutoritaEmittenteSS";
  public static final String CAMPO_NOTE_SS = "NoteSS"; 

  /*************************FINE SENTENZA STRANIERA**********************************/ 
  
  public static final String CAMPO_COD_TIPO_RITO = "CodTipoRito";
  public static final String CAMPO_COD_TIPO_RITO_RIF = "CodTipoRitoRif";
  
  /* #### */
  public static final String PARAMETRO_ORDINAMENTO = "ParametroOrdinamento";
  
  public static final String ELENCO_TITOLI_ESECUTIVI_ISCRITTI_SIGE="elencoTitoliEsecutiviIscrittiSIGE";

  
  public static final String PG_LOAD_RICERCAPROCEDIMENTORGNR	 = ROOT_DIR + "files/siap/siep/sentenza/LoadRicercaProcedimentoRGNR.jsp";
/* inizio modifica marzo 2010 */
  public static final String PG_LOAD_RICERCASENTENZASOGGETTO     = ROOT_DIR + "files/siap/siep/sentenza/LoadRicercaSentenzaSoggetto.jsp";
  /* fine modifica marzo 2010 */
  public static final String PG_LOAD_RICERCASENTENZA	         = ROOT_DIR + "files/siap/siep/sentenza/LoadRicercaSentenza.jsp";
  
  public static final String PG_RICERCASENTENZA	                 = ROOT_DIR + "files/siap/siep/sentenza/RicercaSentenza.jsp";
  public static final String PG_LOAD_INSERISCISENTENZA	         = ROOT_DIR + "files/siap/siep/sentenza/LoadInserisciSentenza.jsp";
  public static final String PG_RICERCASENTENZE                  = ROOT_DIR + "files/siap/siep/sentenza/RicercaSentenza.jsp";
  public static final String PG_RICERCAPROCEDIMENTORGNR                  = ROOT_DIR + "files/siap/siep/sentenza/RicercaProcedimentoRGNR.jsp";
  public static final String PG_RICERCASENTENZE_DUPLICATE        = ROOT_DIR + "files/siap/siep/sentenza/RicercaSentenzaDuplicata.jsp";
  public static final String PG_DETTAGLIOSENTENZA                = ROOT_DIR + "files/siap/siep/sentenza/DettaglioSentenza.jsp";
  public static final String PG_LOAD_INSERISCISENTENZA_DUPLICATA = ROOT_DIR + "files/siap/siep/sentenza/LoadInserisciSentenzaDuplicata.jsp";
  public static final String PG_LOAD_RICERCASENTENZA_ALTRE_BDI   = ROOT_DIR + "files/siap/siep/sentenza/LoadRicercaSentenzaAltreBDI.jsp";
  public static final String PG_LOAD_INSERISCIDECRETO	           = ROOT_DIR + "files/siap/siep/sentenza/LoadInserisciDecreto.jsp";
  public static final String PG_DETTAGLIODECRETO                 = ROOT_DIR + "files/siap/siep/sentenza/DettaglioDecreto.jsp";

  public static final String PG_LOAD_INSERISCISENTENZASTRANIERA	    = ROOT_DIR + "files/siap/siep/sentenza/LoadInserisciSentenzaStraniera.jsp";
  public static final String PG_DETTAGLIOSENTENZASTRANIERA          = ROOT_DIR + "files/siap/siep/sentenza/DettaglioSentenzaStraniera.jsp";

  
  public static final String PG_LOAD_INSERIMENTO_TITOLO  = ROOT_DIR + "files/siap/siep/sentenza/MainInserimentoTitolo.jsp";
  public static final String PG_BUTTONS_SENTENZA         = ROOT_DIR + "files/siap/sico/security/buttonsSentenza.jsp";

  public static final String PG_RICERCASENTENZESOGGETTO                  = ROOT_DIR + "files/siap/siep/sentenza/RicercaSentenzaSoggetto.jsp";
  
  public static final String PG_ELENCOTITOLIESECUTIVIISCRITTISIGE	= ROOT_DIR + "files/siap/siep/sentenza/ElencoTitoliEsecutiviIscrittiSige.jsp";
  
  public static final String PG_LOAD_MODIFICA_DATI_PROVVEDIMENTO_MS_FUORI_SENT  = ROOT_DIR + "files/siap/siep/sentenza/LoadModificaDatiProvvedimentoMSFuoriSent.jsp";
  public static final String PG_DETTAGLIO_DATI_PROVVEDIMENTO_MS_FUORI_SENT    	= ROOT_DIR + "files/siap/siep/sentenza/DettaglioDatiProvvedimentoMSFuoriSent.jsp";

}
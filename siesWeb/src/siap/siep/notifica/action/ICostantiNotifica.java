package siap.siep.notifica.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiNotifica</p>
* <p>Description: Classe di costanti di Notifica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiNotifica
{
	 public static final String CAMPO_ID_NOTIFICA = "IdNotifica";
	 public static final String CAMPO_COD_TIPO_NOTIFICA = "CodTipoNotifica";
	 public static final String CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA = "GiornoDataAvvenutaNotifica";
	 public static final String CAMPO_MESE_DATA_AVVENUTA_NOTIFICA = "MeseDataAvvenutaNotifica";
	 public static final String CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA = "AnnoDataAvvenutaNotifica";
	 public static final String CAMPO_GIORNO_DATA_INVIO = "GiornoDataInvio";
	 public static final String CAMPO_MESE_DATA_INVIO = "MeseDataInvio";
	 public static final String CAMPO_ANNO_DATA_INVIO = "AnnoDataInvio";
	 public static final String CAMPO_COD_ESITO = "CodEsito";
	 public static final String CAMPO_NOTE = "Note";
   public static final String CAMPO_NOTE_FUNGI = "NoteFungibilita";
	 public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	 public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	 public static final String CAMPO_CODICE_OPERATORE_AGGIORNAMENTO = "CodiceOperatoreAggiornamento";
	 public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	 public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	 public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	 public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
	 public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
	 public static final String CAMPO_AUT_EST_ID_AUTORITA_ESTERNA = "AutEstIdAutoritaEsterna";
   public static final String CAMPO_AUT_EST_ID_AUTORITA_EST_DELEG = "AutEstIdAutoritaEstDeleg";
	 public static final String CAMPO_SOG_ID_SOGGETTO = "SogIdSoggetto";
	 public static final String CAMPO_AVV_ID_AVVOCATO_FASCICOLO_SIEP = "AvvIdAvvocatoFascicoloSiep";
	 public static final String CAMPO_UFF_COD_UFFICIO = "UffCodUfficio";
	 public static final String CAMPO_SOLLECITO = "Sollecito";
	 public static final String CAMPO_AVV_ID_AVVOCATO_FASCICOLO_SIUS = "AvvIdAvvocatoFascicoloSius";
	 public static final String CAMPO_CSS_ID_CSSA = "CssIdCssa";
   public static final String CAMPO_AZIONE_DETTAGLIO = "AzioneDettaglio";
   public static final String CAMPO_SEDE_TDS = "SedeTds";
   public static final String CAMPO_SEDE_MDS = "SedeMds";

   public static final String CAMPO_COD_UFFICIO_GE = "CodUfficioGe";
   public static final String CAMPO_SEDE_UFFICIO_GE = "SedeUfficioGe";
   public static final String CAMPO_COD_UFFICIO_PM = "CodUfficioPm";
   public static final String CAMPO_SEDE_UFFICIO_PM = "SedeUfficioPm";
	 public static final String CAMPO_ID_NOTIFICA_INS = "IdNotificaIns";
   //modifica relativa al tipo istituto
   public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione";
   public static final String CAMPO_NOTE_E = "NoteE";
   public static final String CAMPO_NOTE_C = "NoteC";
   public static final String CAMPO_NOTE_IST = "NoteIST";
   public static final String CAMPO_NOTE_CSSA = "NoteCssa";
   public static final String CAMPO_NOTE_UFF = "NoteUff";

   public static final String CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV = "GiornoDataAvvenutaNotificaAvv";
   public static final String CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV  = "MeseDataAvvenutaNotificaAvv";
   public static final String CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV  = "AnnoDataAvvenutaNotificaAvv";

   //MEV 26 - CUMULO
   public static final String CAMPO_COD_TIPO_UFF_REC_CREDITI  = "CodTipoAutUffRecCrediti";
   public static final String CAMPO_SEDE_UFF_REC_CREDITI = "SedeUffRecCrediti";
   
   //
   public static final String PG_LOAD_RICERCANOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadRicercaNotifica.jsp";
	 public static final String PG_LOAD_DETTAGLIONOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadRicercaNotifica.jsp";
	 public static final String PG_RICERCANOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/RicercaNotifica.jsp";
	 public static final String PG_LOAD_INSERISCINOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadInserisciNotifica.jsp";
   public static final String PG_LOAD_DETTAGLIO_NOTIFICA_CONDANNATO	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/DettaglioNotificaOECondannato.jsp";
   public static final String PG_LOAD_DETTAGLIO_NOTIFICA_DIFENSORE	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/DettaglioNotificaOEDifensore.jsp";
   public static final String PG_LOAD_MODIFICA_NOTIFICA_DIFENSORE  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/ModificaNotificaDifensore.jsp";
   public static final String PG_LOAD_MODIFICA_NOTIFICA_CONDANNATO  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/ModificaNotificaCondannato.jsp";

   public static final String PG_LOAD_OMESSA_NOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadOmessaNotifica.jsp";
   public static final String PG_LOAD_OE_OMESSA_NOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadOEOmessaNotifica.jsp";
   public static final String PG_DETTAGLIO_OMESSA_NOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadDettaglioOmessaNotifica.jsp";
   public static final String PG_DETTAGLIO_OE_OMESSA_NOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadDettaglioOEOmessaNotifica.jsp";
   
   public static final String PG_LOAD_RINNOVAZIONE_NOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadRinnovazioneNotifica.jsp";
   public static final String PG_DETTAGLIO_RINNOVAZIONE_NOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadDettaglioRinnovazioneNotifica.jsp";
   public static final String PG_LOAD_SOLLECITI_NOTIFICA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadInserisciSolleciti.jsp";
   public static final String PG_LOAD_SOLLECITI_VANE_RICERCHE	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadInserisciSollecitiVerbVaneRicerche.jsp";
   public static final String PG_DETTAGLIO_SOLLECITI	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadDettaglioSolleciti.jsp";
   public static final String PG_DETTAGLIO_SOLLECITI_VANE_RICERCHE	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadDettaglioSollecitiVerbVaneRicerche.jsp";

   public static final String PG_LOAD_RICERCA_NOTIFICA_LISTA_RINNOVAZIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadRicercaNotificaListaRinnovazione.jsp";
   public static final String PG_LOAD_RICERCA_SOLLECITI  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadRicercaSolleciti.jsp";

   public static final String PG_LOAD_RICERCA_NOTIFICA_LISTA  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadRicercaNotificaLista.jsp";
   public static final String PG_LOAD_GRIGLIA_DECRETO  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadGrigliaDecretoSospensione.jsp";
   public static final String PG_LOAD_GRIGLIA_NOTIFICHE  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadGrigliaNotifiche.jsp";
   public static final String PG_LOAD_RICH_INFO_8_BIS	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadInserisciRich8Bis.jsp";
   public static final String PG_LOAD_DETTAGLIO_RICH_INFO_8_BIS	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadDettaglioRich8Bis.jsp";

   public static final String PG_LOAD_GRIGLIA_IRREPERIBILITA  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadGrigliaIrreperibilita.jsp";
   public static final String PG_LOAD_DECRETO_IRREPERIBILITA	= IWebConstants.ROOT_DIR + "files/siap/siep/notifica/DettaglioNotificaDecretoIrreperibilita.jsp";
   public static final String PG_LOAD_MODIFICA_NOTIFICA_DIFENSORE_IRREPERIBILITA  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/ModificaNotificaDifensoreIrreperibilita.jsp";

   // Ulteriore descrizione del destinatario
   public static final String CAMPO_ULT_DESCR = "DescrizioneUlteriore";

   public static final String PG_LOAD_DETTAGLIO_NOTIFICHE_LS  = IWebConstants.ROOT_DIR + "files/siap/sico/evento/DettaglioNotificheModificateEventoLS.jsp";
   
   // Gruppo di costanti utilizzate per la Ricerca Notifiche/Comunicazioni SIUS
   public static final String PG_LOAD_RICERCA_NOTIFICHE_COMUNICAZIONI_SIUS  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/LoadRicercaNotificaSius.jsp";
	 public static final String CAMPO_GIORNO_DATA_INSERIMENTO2 = "GiornoDataInserimento2";
	 public static final String CAMPO_MESE_DATA_INSERIMENTO2 = "MeseDataInserimento2";
	 public static final String CAMPO_ANNO_DATA_INSERIMENTO2 = "AnnoDataInserimento2";

	 public static final String TIPO_PROVVEDIMENTO = "TipoProvvedimento";
	 // Valori assunti dalla condizione di ricerca  TIPO_PROVVEDIMENTO
	 public static final String TUTTI = "T";
	 public static final String DECRETO_CITAZIONE = "C";  	// Solo decreto di citazione
	 public static final String ALTRI_PROVVEDIMENTI = "A";	// Tutti provvedimenti tranne decreto di citazione
	 
	 public static final String TIPO_DESTINATARIO = "TipoDestinatario";
	 // Valori definiti per la condizione di ricerca  TIPO_DESTINATARIO
	   // UNEP
	 public static final String UNEP_TUTTI = "UNEP";
	 public static final String UNEP_SEDE = "UNEPSede";
	 public static final String UNEP_ALTRE_SEDI = "UNEPAltreSedi";
	 public static final String UNEP_ESCLUSO = "NoUNEP";
	 public static final String ISTITUTO_DETENZIONE = "IstDet";
	 
	   // Procura Generale
	 public static final String PGCAP_TUTTI = "PGCAP";
	 public static final String PGCAP_SEDE = "PGCAPSede";
	 public static final String PGCAP_ALTRE_SEDI = "PGCAPAltreSedi";
	 public static final String PGCAP_ESCLUSO = "NoPGCAP";
	   // Procura della Repubblica c/o Tribunale Ordinario
	 public static final String PM_TUTTI = "PM";
	 public static final String PM_SEDE = "PMSede";
	 public static final String PM_ALTRE_SEDI = "PMAltreSedi";
	 public static final String PM_ESCLUSO = "NoPM";
	 
	 // Tipo di ordinamento e valori definiti
	 public static final String TIPO_ORDINAMENTO = "Ordinamento";
	 public static final String PROCEDIMENTO = "P";
	 public static final String SOGGETTO = "S";
	 public static final String DATA = "D";
	 
	 public static final String PG_ELENCO_NOTIFICHE_SIUS  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/ElencoNotificheSius.jsp";
	 public static final String PG_INCLUDE_NOTIFICHE_SIUS  = IWebConstants.ROOT_DIR + "files/siap/siep/notifica/DivRicercaNotifiche.jsp";
 }
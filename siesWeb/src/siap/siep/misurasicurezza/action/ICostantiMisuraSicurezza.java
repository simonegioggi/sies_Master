package siap.siep.misurasicurezza.action;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe di costanti di MisuraSicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public interface ICostantiMisuraSicurezza {

	public static final String CAMPO_ID_MISURA_SICUREZZA = "IdMisuraSicurezza";
	public static final String CAMPO_COD_NATURA = "CodNatura";
	public static final String CAMPO_COD_TIPO = "CodTipo";
	public static final String CAMPO_NUM_ANNI = "NumAnni";
	public static final String CAMPO_NUM_MESI = "NumMesi";
	public static final String CAMPO_NUM_GIORNI = "NumGiorni";
	public static final String CAMPO_ANNO_REG_38 = "AnnoReg38";
	public static final String CAMPO_NUM_REG_38 = "NumReg38";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
	public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
	public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
	public static final String CAMPO_COD_RICH_MISURA_SIC = "CodRichiestaMisuraSicurezza";
	public static final String CAMPO_TIPO_ISCRIZIONE_MISURA = "TipoIscrizioneMisura";
	// 20/06/2014 Nuovi Campi
	public static final String CAMPO_GIORNO_DATA_DEFINIZIONE = "GiornoDataDefinizione";
	public static final String CAMPO_MESE_DATA_DEFINIZIONE = "MeseDataDefinizione";
	public static final String CAMPO_ANNO_DATA_DEFINIZIONE = "AnnoDataDefinizione";
	public static final String CAMPO_FLAG_ESECUZIONE_IMMEDIATA = "FlagEsecImmediata";
	public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
	public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
	public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";

	public static final String COD_DICHIARA_SCEMATA_PERICOLOSITA = "0189";
	public static final String COD_TRASFORMA_MISURA = "0057";
	public static final String COD_ACCERTA_PERICOLOSITA_SOCIALE = "0051";
	public static final String COD_CONFERMA_PERICOLOSITA_PROROGA = "0053";
	public static final String COD_SOSTITUISCE_MISURA = "0133";
	public static final String COD_DETERMINA_PRESCRIZIONI = "0082";
	public static final String COD_MODIFICA_LIBERAZIONE_CONDIZIONALE = "0048";

	public static final String CAMPO_MIS_ID_MISURA_SICUREZZA = "MisIdMisuraSicurezza";
	// 15/10/2014
	public static final String CAMPO_COD_TIPO_AUTORITA_DESIGNAZIONE_IST = "CodTipoAutoDesignaIst";
	public static final String CAMPO_COD_LUOGO_AUTORITA_DESIGNAZIONE_IST = "CodLuogoAutoDesignaIst";

	public static final String CAMPO_CK_WARNING_MIS = "WarningMisuraFuoriSent";

	public static final String COD_TIPO_RELAZIONE_MS_IN_ESECUZIONE_DI = "IN_ESECUZIONE_DI";
	public static final String COD_TIPO_RELAZIONE_MS_ISCRITTO_AL = "ISCRITTO_AL";

	public static final String CAMPO_COD_TIPO_ALTRA_AUTORITA = "CodTipoAltraAutorita";
	public static final String CAMPO_COD_SEDE_ALTRA_AUTORITA = "CodSedeAltraAutorita";
	public static final String CAMPO_NOTE_ALTRA_AUTORITA = "NoteAltraAutorita";

	public static final String CAMPO_LUOGO_ESECUZIONE_MISURA = "LuogoEsecuzioneMisura";
	public static final String CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS = "NumerazioneManualeMisureProvvFS";

	public static final String PG_LOAD_RICERCAMISURASICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadRicercaMisuraSicurezza.jsp";
	public static final String PG_LOAD_DETTAGLIOMISURASICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/DettaglioMisuraSicurezza.jsp";
	public static final String PG_RICERCAMISURASICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/RicercaMisuraSicurezza.jsp";
	public static final String PG_RICERCA_FASCICOLI_MISURASICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/RicercaFascicoliMisuraSicurezza.jsp";
	public static final String PG_LOAD_INSERISCIMISURASICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadInserisciMisuraSicurezza.jsp";
	public static final String PG_GRIGLIA_MISURE_SICU = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/GrigliaBottoniMisureSicurezza.jsp";
	public static final String PG_GRIGLIA_MISURE_SICU_RICH = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/GrigliaRichiesteTrasmiMisureSicurezza.jsp";

	public static final String PG_LOAD_RICH_RIESAME_PERICOLO_SOC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadRichRiesamePericoloSociale.jsp";
	public static final String PG_DETTAGLIO_RICH_ACCERTA_PERICOLO_SOC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioRichiestaAccertaPericoloSociale.jsp";
	public static final String PG_TRASFERISCI_RICH_ACCERTA_PERICOLO_SOC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadTrasferisciRichiestaAccertaPericoloSociale.jsp";
	public static final String PG_LOAD_CONFERMA_TRASFERISCI_RICH_ACCERTA_PERICOLO_SOC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadConfermaTrasferisciRichiestaAccertaPericoloSociale.jsp";

	public static final String PG_LOAD_INS_ANNOTAZIONE_SORVEGLIANZA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciAnnotazioneDecisioneDellaSorveglianza.jsp";
	public static final String PG_DETTAGLIO_ANNOTA_DECISIONE_SORVEGLIANZA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioAnnotazioneDecisionedellaSorveglianza.jsp";

	public static final String PG_LOAD_INS_COMUNICAZIONE_POLIZIA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciComunicazionePolizia.jsp";
	public static final String PG_DETTAGLIO_COMUNICAZIONE_POLIZIA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioComunicazionePolizia.jsp";

	public static final String PG_LOAD_INS_RICHIESTA_DAP = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciRichiestaDAP.jsp";
	public static final String PG_DETTAGLIO_RICHIESTA_DAP = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioRichiestaDAP.jsp";

	public static final String PG_LOAD_INS_OE_INTERNAMENTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciOEInternamento.jsp";
	public static final String PG_DETTAGLIO_OE_INTERNAMENTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioOEInternamento.jsp";

	public static final String PG_LOAD_INS_ORDINE_LIBERAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciOrdineLiberazione.jsp";
	public static final String PG_DETTAGLIO_ORDINE_LIBERAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioOrdineLiberazione.jsp";

	public static final String PG_LOAD_INS_ARCHIVIAZIONE_MANUALE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciArchiviazioneManuale.jsp";
	public static final String PG_DETTAGLIO_ARCHIVIAZIONE_MANUALE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioArchiviazioneManuale.jsp";

	public static final String PG_LOAD_INS_ARCHIVIAZIONE_PROVV_SORVE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciArchiviazionePerProvvSorveglianza.jsp";
	public static final String PG_DETTAGLIO_ARCHIVIAZIONE_PROVV_SORVE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioArchiviazionePerProvvSorveglianza.jsp";
	public static final String PG_LOAD_LISTA_ARCHIVIAZIONE_PROVV_SORVE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadListaArchiviazioniProvvedimentiSIUS.jsp";

	public static final String PG_LOAD_INS_ARCHIVIAZIONE_PROVV_ALTROUFF = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciArchiviazionePerProvvAltroUfficio.jsp";
	public static final String PG_DETTAGLIO_ARCHIVIAZIONE_PROVV_ALTROUFF = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioArchiviazionePerProvvAltroUfficio.jsp";

	public static final String PG_LOAD_INS_ARCHIVIAZIONE_PROVV_GE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciArchiviazionePerProvvGEsecuzione.jsp";
	public static final String PG_DETTAGLIO_ARCHIVIAZIONE_PROVV_GE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioArchiviazionePerProvvGEsecuzione.jsp";
	public static final String PG_LOAD_LISTA_ARCHIVIAZIONE_PROVV_GE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadListaArchiviazioniProvvedimentiGE.jsp";

	public static final String PG_LOAD_INSERISCI_DESIGNAZIONE_ISTITUTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciDesignazioneIstituto.jsp";
	public static final String PG_LOAD_DETTAGLIO_DESIGNAZIONE_ISTITUTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadDettaglioDesignazioneIstituto.jsp";

	public static final String PG_LOAD_INSERISCI_ORDINE_CONSEGNA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciOrdinediConsegna.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINE_CONSEGNA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadDettaglioOrdinediConsegna.jsp";

	// Step 2
	public static final String PG_LOAD_RICERCA_SOGG_MIS_SIC_PROVVISORIA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadRicercaSoggettoIscrizioneMisuraProvvisoria.jsp";
	public static final String PG_RICERCA_SOGG_MIS_SIC_PROVVISORIA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/RicercaSoggettoIscrizioneMisuraProvvisoria.jsp";
	public static final String PG_LOAD_ISCR_PROC_APPLICAZIONE_MIS_SIC_PROVVISORIA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadIscrizioneProcApplicazioneMisuraProvvisoria.jsp";

	public static final String PG_WARNING_MIS_SIC_FUORI_SENT = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/WarningMisFuoriSentenza.jsp";

	public static final String PG_LOAD_RICERCA_PROVV_MIS_SIC_FUORI_SENTENZA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadRicercaProvvedimentiIscrizioneMisuraFuoriSentenza.jsp";
	public static final String PG_RICERCA_PROVV_MIS_SIC_FUORI_SENTENZA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/RicercaProvvedimentiIscrizioneMisuraFuoriSentenza.jsp";
	public static final String PG_LOAD_ISCR_PROC_MIS_SIC_FUORI_SENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadIscrizioneProcedimentoMisuraFuoriSentenza.jsp";

	public static final String PG_LOAD_RICERCA_TITOLOESEC_DA_ASSOCIARE_A_MIS_SIC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadRicercaAssocia_TitoloEsec_aMisuraSic.jsp";

	public static final String PG_MODIFICA_COMUNICAZIONE_ORDINE_CONSEGNA_MS = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaComunicazioneOrdineConsegnaMS.jsp";
	public static final String PG_MODIFICA_OE_INTERNAMENTO_ORDINE_LIBERAZIONR_MS = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaOEInternamentoOrdineLiberazioneMS.jsp";
	public static final String PG_MODIFICA_ARCHIVIAZIONE_MANUALE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaArchiviazioneManualeMS.jsp";
	public static final String PG_MODIFICA_ARCHIVIAZIONE_SORVEGLIANZA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaArchiviazionePerProvvSorveglianzaMS.jsp";
	public static final String PG_MODIFICA_ARCHIVIAZIONE_GIUD_ESECUZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaArchiviazionePerProvvGEsecuzioneMS.jsp";
	public static final String PG_MODIFICA_ARCHIVIAZIONE_ALTRO_UFFICIO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaArchiviazionePerProvvAltroUfficioMS.jsp";
	public static final String PG_MODIFICA_RICHIESTA_AL_DAP = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaRichiestaDAP.jsp";
	public static final String PG_MODIFICA_DESIGNAZIONE_ISTITUTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaDesignazioneIstituto.jsp";

	public static final String PG_LOAD_INS_ANNOTAZIONE_DECISIONE_GIUDICE_CASSAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciAnnotazioneDecisioneGiudiceCassazione.jsp";
	public static final String PG_DETTAGLIO_ANNOTAZIONE_DECISIONE_GIUDICE_CASSAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadDettaglioAnnotazioneDecisioneGiudiceCassazione.jsp";
	public static final String PG_LOAD_INS_ARCHIVIAZIONE_PER_PROVV_GIUDICE_CASSAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciArchiviazionePerProvvGiudiceCassazione.jsp";
	public static final String PG_LOAD_DETTAGLIO_ARCHIVIAZIONE_PER_PROVV_GIUDICE_CASSAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadDettaglioArchiviazionePerProvvGiudiceCassazione.jsp";
	public static final String PG_LISTA_PROVV_GIUDICE_CASSAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/ListaProvvedimentiGiudiceCassazione.jsp";
	public static final String PG_LISTA_INSERT_PROVV_GIUDICE_CASSAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/ListaProvvedimentiGiudiceCassazioneInsArch.jsp";
	public static final String PG_LOAD_MODIFICA_ARCHIVIAZIONE_PROVV_GIUDICE_CASSAZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaArchiviazionePerProvvGiudiceCassazione.jsp";
	// Add Diego
	public static final String PG_LOAD_INS_TRASM_COMP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadInserisciTrasmissioneCompetenza.jsp";
	public static final String PG_DETTAGLIO_TRASMISSIONE_COMP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/DettaglioTrasmissioneCompetenza.jsp";
	public static final String PG_LOAD_TRASFERISCI_TRASMISSIONE_COMP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadTrasferisciTrasmissioneCompetenza.jsp";

	public static final String PG_LISTA_ATTI_RICEVUTI_TRASMISSIONE_COMPETENZA_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/ListaAttiRicevutiDaPrendereInCarico.jsp";
	public static final String PG_LOAD_DETTAGLIO_ATTO_RICEVUTO_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/DettaglioAttoRicevutoPerCompetenza.jsp";
	public static final String PG_LOAD_RESTITUZIONE_ATTO_RICEVUTO_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/PopupRestituzioneAttoRicevuto.jsp";

	public static final String PG_LISTA_ATTI_PRESI_IN_CARICO_DA_ISCRIVERE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/ListaAttiRicevutiPresiInCaricoDaIscrivere.jsp";

	public static final String PG_LOAD_RICERCA_ATTI_PRESI_IN_CARICO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/RicercaAttiRicevutiPresiInCarico.jsp";
	public static final String PG_LISTA_ATTI_PRESI_IN_CARICO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/ListaAttiRicevutiPresiInCarico.jsp";
	public static final String PG_LOAD_INS_FASCICOLO_CLASSE_IV = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadInserisciFascicoloSiepClasseIV.jsp";

	public static final String PG_LOAD_RICERCA_ATTI_TRASMESSI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadRicercaAttiTrasmessi.jsp";
	public static final String PG_LISTA_ATTI_TRASMESSI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/ListaAttiTrasmessi.jsp";
	public static final String PG_DETTAGLIO_ATTI_TRASMESSI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/DettaglioAttoTrasmesso.jsp";
	public static final String PG_LOAD_INOLTRO_ATTI_RICEVUTI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadInoltroAttoRicevuto.jsp";

	public static final String PG_LOAD_INSERISCI_ANNOTAZIONE_ESITO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadInserisciAnnotazioneEsitoAttoTrasmesso.jsp";
	public static final String PG_DETTAGLIO_ANNOTAZIONE_ESITO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/DettaglioAnnotazioneEsitoAttoTrasmesso.jsp";

	public static final String PG_LOAD_INSERISCI_SOLLECITO_ESITO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadInserisciSollecitoAttoTrasmesso.jsp";
	public static final String PG_DETTAGLIO_SOLLECITO_ESITO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/DettaglioSollecitoEsitoTrasmissione.jsp";

	public static final String PG_LOAD_TRASFERISCI_SOLLECITO_ESITO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/misurasicurezza/LoadTrasferisciSollecitoEsito.jsp";

	/* 
	 * ISSUE MEV : aggiunte costanti per OE Differimento e Restituzione Ordine di Consegna
	 * Numero MEV : 39
	 * Autore    : Gioggi
	 * Data      : 21/feb/2017
	 * Branch    : MEV_39
	 */
	public static final String PG_LOAD_INS_OE_DIFFERIMENTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciOLDifferimento.jsp";
	
	public static final String PG_LOAD_INS_OE_DIFFERIMENTO_DEC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciOLDifferimentoDecreto.jsp";
	
	
	public static final String PG_DETTAGLIO_OE_DIFFERIMENTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioOLDifferimento.jsp";
	public static final String PG_MODIFICA_OE_DIFFERIMENTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaOLDifferimento.jsp";
	public static final String PG_LOAD_LISTA_DIFFERIMENTO_PROVV_SORVE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadListaDifferimentiProvvedimentiSIUS.jsp";
	public static final String COD_ACCOGLIE_APPELLO_E_MODIFICA_MDS = "0181";
	public static final String PG_LOAD_INSERISCI_RESTITUZIONE_ORDINE_CONSEGNA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciRestituzioneOrdineConsegna.jsp";
	public static final String PG_LOAD_DETTAGLIO_RESTITUZIONE_ORDINE_CONSEGNA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadDettaglioRestituzioneOrdineConsegna.jsp";
	public static final String PG_MODIFICA_RESTITUZIONE_ORDINE_CONSEGNA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaRestituzioneOrdineConsegna.jsp";
	public static final String PG_LOAD_INS_ARCHIVIAZIONE_PER_PROVV_CUMULO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadInserisciArchiviazionePerProvvCumulo.jsp";
	public static final String PG_LOAD_DETTAGLIO_ARCHIVIAZIONE_PER_PROVV_CUMULO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadDettaglioArchiviazionePerProvvCumulo.jsp";
	public static final String PG_LOAD_MODIFICA_ARCHIVIAZIONE_PER_PROVV_CUMULO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaArchiviazionePerProvvCumulo.jsp";
	//***** FINE INTERVENTO MEV_39 *****//
	
	public static final String PG_DETTAGLIO_OE_DIFFERIMENTO_DEC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/DettaglioOLDifferimentoDecreto.jsp";
	public static final String PG_MODIFICA_OE_DIFFERIMENTO_DEC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/misurasicurezza/LoadModificaOLDifferimentoDecreto.jsp";

}
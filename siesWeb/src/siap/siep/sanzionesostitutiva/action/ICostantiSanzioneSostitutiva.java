package siap.siep.sanzionesostitutiva.action;

import f3b.web.IWebConstants;

/**
 * Title: ICostantiSanzioneSostitutiva Description: Classe di costanti di SanzioneSostitutiva
 *
 * @version 1.0
 */
public interface ICostantiSanzioneSostitutiva {

	public static final String CAMPO_ID_SANZIONE_SOSTITUTIVA = "IdSanzioneSostitutiva";
	public static final String CAMPO_COD_TIPO_SANZIONE = "CodTipoSanzione";
	public static final String CAMPO_NUM_ANNI = "NumAnni";
	public static final String CAMPO_NUM_MESI = "NumMesi";
	public static final String CAMPO_NUM_GIORNI = "NumGiorni";
	public static final String CAMPO_SANZIONE_PECUNIARIA = "SanzionePecuniaria";
	public static final String CAMPO_ANNO_REGISTRO = "AnnoRegistro";
	public static final String CAMPO_NUM_REGISTRO = "NumRegistro";
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
	public static final String CAMPO_PEN_COM_ID_PENA_COMPLESSIVA = "PenComIdPenaComplessiva";

	public static final String CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA = "interoSanzionePecuniariaMulta";
	public static final String CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA = "decimaleSanzionePecuniariaMulta";
	public static final String CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA = "interoSanzionePecuniariaAmmenda";
	public static final String CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA = "decimaleSanzionePecuniariaAmmenda";
	public static final String CAMPO_VALUTA_SANZIONE_PECUNIARIA = "valutaSanzionePecuniaria";
	public static final String CAMPO_TIPO_PROVVEDIMENTO = "TipoProvvedimento";
	public static final String CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS = "chiaveAnnoFascicoloSius";
	public static final String CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS = "chiaveProgrFascicoloSius";
	public static final String CAMPO_ANNO_PROVVEDIMENTO = "AnnoProvvedimento";
	public static final String CAMPO_NUMERO_PROVVEDIMENTO = "NumeroProvvedimento";
	public static final String CAMPO_UFFICIO_SORVEGLIANZA = "ufficioSorveglianza";
	public static final String CAMPO_COD_UFFICIO_SORVEGLIANZA = "codUfficioSorveglianza";
	public static final String CAMPO_SEDE_UDS_EMITT = "sedeUffSorvEmittente";
	public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
	public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
	public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
	public static final String CAMPO_OGGETTO_PROVVEDIMENTO = "OggettoProvvedimento";
	public static final String CAMPO_ESITO = "campoEsito";
	public static final String CAMPO_NOTE = "campoNote";
	public static final String CAMPO_UFFICIO_COMPETENTE = "UfficioCompetente";
	public static final String CAMPO_SEDE_UFFICIO_COMPETENTE = "sedeUfficioCompetente";
	public static final String CAMPO_GIORNO_DATA_RICEZIONE = "GiornoDataRicezione";
	public static final String CAMPO_MESE_DATA_RICEZIONE = "MeseDataRicezione";
	public static final String CAMPO_ANNO_DATA_RICEZIONE = "AnnoDataRicezione";
	public static final String CAMPO_COD_UFFICIO_ = "CodUfficio";
	public static final String CAMPO_SEDE_UFFICIO = "SedeUfficio";

	// MEV_2023-13
	public static final String CAMPO_COD_TIPO_PENA_SOSTITUTIVA = "CodTipoPenaSostitutiva";
	public static final String CAMPO_NUM_ANNI_PENA_SOSTITUTIVA = "NumAnniPenaSostitutiva";
	public static final String CAMPO_NUM_MESI_PENA_SOSTITUTIVA = "NumMesiPenaSostitutiva";
	public static final String CAMPO_NUM_GIORNI_PENA_SOSTITUTIVA = "NumGiorniPenaSostitutiva";
	public static final String CAMPO_INTERO_PENA_PECUNIARIA_SOSTITUTIVA = "interoPenaPecuniariaSostitutiva";
	public static final String CAMPO_DECIMALE_PENA_PECUNIARIA_SOSTITUTIVA = "decimalePenaPecuniariaSostitutiva";

	public static final String TIPO_PENA_SOSTITUTIVA_SS = "Sanzione Sostitutiva";
	public static final String TIPO_PENA_SOSTITUTIVA_PS = "Pena Sostitutiva";

	public static final String PG_GRIGLIA_RISCOSSIONE_PP = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/GrigliaRiscossionePenePecuniarie.jsp";
	public static final String PG_LOAD_INSERISCI_ORDINE_INGIUNZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciOrdineIngiunzione.jsp";
	public static final String PG_DETTAGLIO_ORDINE_INGIUNZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadDettaglioOrdineIngiunzione.jsp";
	public static final String PG_GRIGLIA_BOLLETTINI_PAGOPA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/GrigliaBollettiniPagoPA.jsp";
	public static final String PG_ELENCO_RICHIESTA_BOLLETTINI = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/ElencoRichiestaBollettini.jsp";
	public static final String PG_LOAD_GRIGLIA_ORDINE_INGIUNZIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadGrigliaOrdineIngiunzione.jsp";
	public static final String PG_LOAD_INSERIMENTO_NOTIFICHE_OI = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserimentoNotificheOrdineIngiunzione.jsp";
	public static final String PG_LOAD_DETTAGLIO_NOTIFICHE_OI = IWebConstants.ROOT_DIR
	            + "/files/siap/siep/sanzionesostitutiva/LoadDettaglioNotificheOrdineIngiunzione.jsp";
	public static final String PG_VERIFICA_STATO_PAGAMENTI = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/VerificaStatoPagamenti.jsp";
	public static final String PG_ELENCO_STATO_PAGAMENTI = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/ElencoStatoPagamenti.jsp";
	public static final String PG_VERIFICA_STATO_BOLLETTINO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/VerificaStatoBollettino.jsp";
	public static final String PG_VERIFICA_ELENCO_STATO_BOLLETTINI = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/VerificaElencoStatoBollettini.jsp";
	// MEV_2023-13 - FINE
	
	
	// MEV_2023-33
	public static final String PG_LOAD_INSERISCI_RINNOVO_RICERCHE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciRinnovoRicerche.jsp";
	public static final String PG_LOAD_SELEZIONA_ORDINE_INGIUNZIONE= IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadSelezionaOrdineIngiunzione.jsp";	
	// MEV_2023-33 - FINE
	

	public static final String PG_LOAD_RICERCASANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/LoadRicercaSanzioneSostitutiva.jsp";
	public static final String PG_LOAD_DETTAGLIOSANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/LoadRicercaSanzioneSostitutiva.jsp";
	public static final String PG_RICERCASANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/RicercaSanzioneSostitutiva.jsp";
	public static final String PG_LOAD_INSERISCISANZIONESOSTITUTIVA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/LoadInserisciSanzioneSostitutiva.jsp";

	public static final String PG_LOAD_INSERISCI_TRASMISSIONE_ATTI_ESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/LoadInserisciTrasmissioneAttiEsecuzione.jsp";
	public static final String PG_DETTAGLIO_TRASMISSIONE_ATTI_ESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/DettaglioTrasmissioneAttiEsecuzione.jsp";

	public static final String PG_LOAD_INSERISCI_ANNOTAZIONI_PROVVEDIMENTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciAnnotazioneProvvedimento.jsp";
	public static final String PG_DETTAGLIO_INSERISCI_ANNOTAZIONI_PROVVEDIMENTO = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/DettaglioInserisciAnnotazioneProvvedimento.jsp";

	public static final String PG_LOAD_RICERCA_TRASMISSIONE_ATTI_ESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/LoadRicercaTrasmissioneAttiEsecuzione.jsp";
	public static final String PG_RICERCA_TRASMISSIONE_ATTI_ESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/RicercaTrasmissioneAttiEsecuzione.jsp";

	public static final String PG_LOAD_RICERCA_PRESAINCARICO_ATTI_ESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/LoadRicercaPresaIncaricoAttiEsecuzione.jsp";
	public static final String PG_RICERCA_PRESAINCARICO_ATTI_ESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/sanzionesostitutiva/RicercaPresaIncaricoAttiEsecuzione.jsp";

	public static final String PG_LOAD_INSERISCI_ANNOTAZIONI_REVOCA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadAnnotazioneRevocaConversione.jsp";
	public static final String PG_DETTAGLIO_INSERISCI_ANNOTAZIONI_REVOCA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/DettaglioRevocaConversione.jsp";

	public static final String PG_LOAD_INSERISCI_RICHIESTA_REVOCA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciRichiestaRevocaSS.jsp";
	public static final String PG_DETTAGLIO_RICHIESTA_REVOCA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/DettaglioRichiestaRevocaSS.jsp";

	// GRIGLIE BOTTONI
	public static final String PG_GRIGLIA_ALTRE_SANZIONI = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/GrigliaBottoniAltreSanzioni.jsp";
	public static final String PG_GRIGLIA_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/GrigliaBottoniSanzioniSostitutive.jsp";
	public static final String PG_GRIGLIA_SEMIDETENZIONE_LC = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/GrigliaBottoniSemidetenzioneLC.jsp";
	public static final String PG_GRIGLIA_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/GrigliaBottoniEspulsione.jsp";
	public static final String PG_GRIGLIA_RISCONTRO_TRASMISSIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/GrigliaRiscontroTrasmissioneAtti.jsp";

	// ============================================================================
	// ESPULSIONE
	// ============================================================================
	// Annotazione Espulsione
	public static final String PG_LOAD_INSERISCI_ANNOTAZIONE_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciAnnotazioneEspulsione.jsp";
	public static final String PG_LOAD_DETTAGLIO_ANNOTAZIONE_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadDettaglioAnnotazioneEspulsione.jsp";

	// Mancata Espulsione
	public static final String PG_LOAD_INSERISCI_MANCATA_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciMancataEspulsione.jsp";
	public static final String PG_LOAD_DETTAGLIO_MANCATA_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadDettaglioMancataEspulsione.jsp";

	// Richiesta Revoca Espulsione
	public static final String PG_LOAD_INSERISCI_RICHIESTA_REVOCA_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciRichiestaRevocaEspulsione.jsp";
	public static final String PG_LOAD_DETTAGLIO_RICHIESTA_REVOCA_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadDettaglioRichiestaRevocaEspulsione.jsp";

	// ============================================================================
	// Comunicazione Nuovo Residuo Pena
	// ============================================================================
	public static final String PG_LOAD_INSERISCI_COM_NUOVO_RES_PENA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciComNuovoResPena.jsp";
	public static final String PG_LOAD_DETTAGLIO_COM_NUOVO_RES_PENA = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadDettaglioComNuovoResPena.jsp";

	// ============================================================================
	// Rideterminazione Pena revoca/conversione Sanzione Sostitutiva
	// ============================================================================
	public static final String PG_LOAD_INSERISCI_RIDETPENA_REVOCA_SS = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadInserisciRideterminazionePenaRevocaSS.jsp";
	public static final String PG_LOAD_DETTAGLIO_RIDETPENA_REVOCA_SS = IWebConstants.ROOT_DIR
			+ "/files/siap/siep/sanzionesostitutiva/LoadDettaglioRidetPenaRevocaSS.jsp";

}
package siap.siep.pagoPA.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BProperties;
import it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBeanServiceLocator;
import it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento;
import siap.siep.pagoPA.action.ICostantiPagoPA;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Il job preleva dalla tabella BOLLETTINO_PAGOPA i bollettini non ancora pagati (DATA_AVV_PAGAMENTO null o
 * STATO_PAGAMENTO = PN) e per ogni bollettini egffettua lì'interrogazione al servizio "elencopagamenti" di
 * pagoPA.
 *
 * Viene aggiornata BOLLETTINO_PAGOPA con la data ultimo controllo e la eventuale risposta XML
 *
 * @author d.fiorletta
 *
 */
public class ConsultaPagamentiJob implements Job {
	public static final String JOB_NAME = "ConsultaPagamentiJob";
	public static final String JOB_GROUP = "PagoPAGroup";
	public static final String JOB_DESC = "Demone responsabile della consultazione dello stato dei pagamento su PagoPA";

	public static final String JOB_DESC_COL = "xxxxxxxxxxxxxxx";

	private static Logger pagoPaLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		pagoPaLogger.debug("===================================================");
		pagoPaLogger.debug(" Avvio job di PagoPA - ConsultazionePagamenti      ");
		pagoPaLogger.debug("===================================================");
		pagoPaLogger.debug("");

		String esitoEsecuzione = "Batch avviato";
		String erroriEsecuzione = "";
		BatchPagopaModel lBatchModel = null;
		//
		int contaNumPosBebitorieVerificate = 0;
		int contaNumBollettiniAggiornati = 0;

		IBatchPagopa lCtrlBatch = null;

		try {
			// Loggare su BATCH_PAGOPA l'avvio e l'esito del batch
			lCtrlBatch = SIEPLookupRemote.getBatchPagopaPagopaRemote();

			lBatchModel = new BatchPagopaModel();
			lBatchModel.setDataInizioEsecuzione(DateUtils.getSysDate());
			lBatchModel.setEsitoEsecuzione(esitoEsecuzione);
			lBatchModel = lCtrlBatch.ExInserisciLancioBatchPagopa(lBatchModel);

			ServiziConsultazionePagamentiTelematici scpt = null;
			try {
				String endpointAddressSCPT = F3BProperties.getProperty("EAPPA_SCPT");
				pagoPaLogger.debug(" Apertura connessione con endpoint = " + endpointAddressSCPT);
				ServiziConsultazionePagamentiTelematiciBeanServiceLocator scptbsl = new ServiziConsultazionePagamentiTelematiciBeanServiceLocator();
				scptbsl.setServiziConsultazionePagamentiTelematiciSOAPPortEndpointAddress(
						endpointAddressSCPT);
				scpt = scptbsl.getServiziConsultazionePagamentiTelematiciSOAPPort();
				pagoPaLogger.debug(" Connessione aperta con successo");
			} catch (Exception e) {
				pagoPaLogger.error("Errore in fase di apertura della connessione con l'endpoint pagoPA ", e);
				throw e;
			}

			int controllateDaGiorni = 0;
			int inScadenzaTraGiorni = 0;

			IBollettinoPagopa lCtrlBollettini = SIEPLookupRemote.getBollettinoPagopaRemote();
			pagoPaLogger.debug(" Ricerca debitori con posizioni aperte: inScadenzaTraGiorni "
					+ inScadenzaTraGiorni + ", controllateDaGiorni " + controllateDaGiorni);
			Vector<BollettinoPagopaModel> lElencoDebitori = lCtrlBollettini
					.ExRicercaDebitoriConPosizioniAperteInScadenza(inScadenzaTraGiorni, controllateDaGiorni);
			pagoPaLogger.debug(" Debitori trovati = " + lElencoDebitori.size());

			// Ricerca per debitore
			for (int i = 0; i < lElencoDebitori.size(); i++) {
				BollettinoPagopaModel lDebitore = lElencoDebitori.elementAt(i);

				try {
					pagoPaLogger.debug(
							" Inizio richiesta a pagoPa per il Debitore = " + lDebitore.getCodiceFiscale()
									+ ", codice distretto = " + lDebitore.getCodiceDistretto());
					contaNumPosBebitorieVerificate++;

					// n.b. la ricerca per codiceCRS vuole comunque il codice fiscale obblgatorio altrimenti
					// non trova nulla
					String codiceCRS = null; // "330091047213734917"; // solo per ricerca puntuale
					String tipologia = "PENPE";
					String codiceFiscale = lDebitore.getCodiceFiscale();
					String codiceDistretto = lDebitore.getCodiceDistretto();
					// "GLTO"; // dipende dal distretto!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					// Deve essere un parametro letto da F£B.properties
					String causale = null; // "Sanzione pecuniaria";
					String stato = null; // ICostantiPagoPA.SIES_STATO_PAGATO; // servirebbero solo i pagati

					// L'elemento stato contiene l'indicazione dello stato del pagamento nel contesto del PST
					// e può assumere uno dei seguenti valori
					// - GENERATA - Valore ottenuto ni test dopo la generazione del bollettino
					// - CARRELLO - indica che la richiesta è nel carrello (non pagato)
					// - DISPONIBILE - Il valore indica che è presente una Ricevuta Telematica con esito
					// positivo
					// (pagamento eseguito).
					// - ALTRI NON DI INTERESSE

					// servono quelli pagati? se CARRELLO allora sono quelli ancora non pagati
					// vedi pg 12 - APPLICATIVI - Flussi pagamento telematico tramite PST vers. 3.1.pdf
					Calendar dataRichiestaDa = null; // testare
					Calendar dataRichiestaA = null;
					// ? il numero di risultati restituiti per es se invocato da web
					int dimensionePagina = 0;
					int numeroPagina = 0;

					// Nota
					// per lo stesso soggetto (CF) potrebbero essere presenti puù fascicoli
					RisultatoRicerca rr = scpt.elencoPagamenti(codiceCRS, tipologia, codiceFiscale,
							codiceDistretto, causale, stato, dataRichiestaDa, dataRichiestaA,
							dimensionePagina, numeroPagina);

					pagoPaLogger.debug("RisultatoRicerca.getCount()            = " + rr.getCount());
					pagoPaLogger
							.debug("RisultatoRicerca.getDimensionePagina() = " + rr.getDimensionePagina());
					pagoPaLogger.debug("RisultatoRicerca.getNumeroPagina()     = " + rr.getNumeroPagina());
					pagoPaLogger.debug("RisultatoRicerca.getItems().length     = "
							+ (rr.getItems() != null ? rr.getItems().length : null));

					Object[] pagamenti = rr.getItems();

					if (pagamenti == null || pagamenti.length == 0) {
						pagoPaLogger
								.warn("Nessuna StatoRichiesta restituito per il debitore " + codiceFiscale);
						// TODO dovrei aggiornare tutte le posizioni
						esitoEsecuzione += "\nNessuna StatoRichiesta restituita per il debitore "
								+ codiceFiscale;
					} else {
						// recupero in una sola transazione tutti i bollettini a carico del CF per poterli
						// confrontare
						// con quelli restituiti del WS

						for (int progPagamento = 0; progPagamento < pagamenti.length; progPagamento++) {
							BollettinoPagopaModel bollettinoSIES = null;
							try {
								StatoRichiestaPagamento statoRichiesta = null;
								if (pagamenti[progPagamento] instanceof StatoRichiestaPagamento) {
									statoRichiesta = (StatoRichiestaPagamento) pagamenti[progPagamento];
									pagoPaLogger.debug("statoRichiesta " + statoRichiesta.toString1());

									// Devo verificrae se il pagamento restituito è tra quelli pending e in
									// caso aggiornare
									// lo stato
									// Recupero il pagamento per numero di avviso (NON CRS)
									String iuv = statoRichiesta.getNumeroAvviso().substring(1);
									bollettinoSIES = lCtrlBollettini.ExRicercaBollettinoPagopaByIUV(iuv);
									if (bollettinoSIES != null) {
										if (ICostantiPagoPA.SIES_STATO_NON_PAGATO
												.equals(bollettinoSIES.getStatoPagamento())) {

											bollettinoSIES.setDataUltimoControllo(DateUtils.getSysDate());

											bollettinoSIES.setCodOperatoreAggiornamento("BATCH");
											bollettinoSIES.setCodUfficioAggiornamento("BATCH");
											bollettinoSIES.setDataAggiornamento(DateUtils.getSysDate());

											// Loggo comunque lo stato
											bollettinoSIES.setStatoPagopa(statoRichiesta.getStato());

											if (statoRichiesta.getStato()
													.equals(ICostantiPagoPA.PAGOPA_STATO_DISPONIBILE)) {
												// Il bollettino è stato pagato. Aggiorno opportunamente il
												// campo - // PA = Pagato
												bollettinoSIES
														.setStatoPagamento(ICostantiPagoPA.SIES_STATO_PAGATO);
												bollettinoSIES.setDataAvvPagamento(
														DateUtils.getDate(statoRichiesta.getDataRicevuta()));
												// verificare che sia l'importo veramente pagato
												bollettinoSIES.setImportoPagato(
														BigDecimal.valueOf(statoRichiesta.getImporto()));

												contaNumBollettiniAggiornati++;
												// TODO calcolare le scadenze delle successive rate se
												// necessario
											}
											// else if
											// (statoRichiesta.getStato().equals(ICostantiPagoPA.PAGOPA_STATO_CARRELLO))
											// {
											else if (statoRichiesta.getStato()
													.equals(ICostantiPagoPA.PAGOPA_STATO_GENERATA)) {
												// Non aggiorno nulla. Bollettino non ancora pagato ho già
												// salvato stato e data controllo
											} else {
												// Non aggiorno nulla. Ho già salvato stato e data controllo
											}

											// Aggiorno il DB
											pagoPaLogger.debug(" Aggiornamento stato pagamento con esito  "
													+ bollettinoSIES.getStatoPagamento());
											lCtrlBollettini
													.ExAggiornaStatoPagamentoBollettinoPagopa(bollettinoSIES);
											pagoPaLogger.debug(" Aggiornamento effettuato");
										} else {
											// TODO per scrupolo verifico lo stato comunque
										}
									} else {
										pagoPaLogger.debug("Per il numero di avviso " + iuv
												+ " non esiste alcuna posizione sulla tabella dei bollettini");
										esitoEsecuzione += "\nwarn iuv " + iuv
												+ " non presente in banca dati";
									}
								} else {
									pagoPaLogger.warn(
											"L'oggetto restituito NON è una istanza di StatoRichiestaPagamento");
								}
							} catch (Exception e) {
								pagoPaLogger.error("Exception", e);
								erroriEsecuzione += "\n" + e.getMessage();
								// Errore sul singolo bollettino
								if (bollettinoSIES != null) {
									bollettinoSIES.setDataUltimoControllo(DateUtils.getSysDate());

									bollettinoSIES.setCodOperatoreAggiornamento("BATCH");
									bollettinoSIES.setCodUfficioAggiornamento("BATCH");
									bollettinoSIES.setDataAggiornamento(DateUtils.getSysDate());

									bollettinoSIES
											.setErrorePagopa("Errore in fase di verifica: " + e.getMessage());

									lCtrlBollettini.ExAggiornaStatoPagamentoBollettinoPagopa(bollettinoSIES);
								}
							}

						}
					}
				} catch (Exception e) {
					pagoPaLogger.error(
							" Errore in fase di invocazine del WS PagoPA.elencoPagamenti per il Debitore: CF = "
									+ lDebitore.getCodiceFiscale(),
							e);
					erroriEsecuzione += "\nErrore in fase di invocazine del WS PagoPA.elencoPagamenti per il Debitore: CF = "
							+ lDebitore.getCodiceFiscale();
				}
			}
		} catch (Exception e) {
			pagoPaLogger.error("Errore in fase di esecuzione del job di consultazionePagamenti", e);
			erroriEsecuzione += "\nErrore in fase di esecuzione del job di consultazionePagamenti";
		} finally {
			try {
				// Aggiorno l'esecuzione del batch
				lBatchModel.setDataFineEsecuzione(DateUtils.getSysDate());
				lBatchModel.setEsitoEsecuzione("Batch terminato");
				lBatchModel.setNumPosDebitorieVerificate(new BigDecimal(contaNumPosBebitorieVerificate));
				lBatchModel.setNumBollettiniAggiornati(new BigDecimal(contaNumBollettiniAggiornati));

				lBatchModel.setEsitoEsecuzione(esitoEsecuzione);
				lBatchModel.setErroreEsecuzione(erroriEsecuzione);

				lBatchModel = lCtrlBatch.ExAggiornaLancioBatchPagopa(lBatchModel);
			} catch (Exception e) {
				pagoPaLogger.error("Errore in fase di Aggiornamento BATCH_PAGOPA", e);
			}
		}

		pagoPaLogger.debug("===================================================");
		pagoPaLogger.debug(" Job di PagoPA terminato - ConsultazionePagamenti  ");
		pagoPaLogger.debug("===================================================");
	}

	private void checkBollettiniByIUV() {

		try {
			int dayOffset = 0;

			IBollettinoPagopa lCtrlBollettini = SIEPLookupRemote.getBollettinoPagopaRemote();

			// Attenzione solo i bollettino con data scadenza passata
			pagoPaLogger.debug(" Ricerca bollettini non pagati con offset = " + dayOffset);
			Vector<BollettinoPagopaModel> lElencoBollettini = lCtrlBollettini
					.ExRicercaBollettinoPagopaNonPagati(dayOffset);
			pagoPaLogger.debug(" Bollettini trovati = " + lElencoBollettini.size());

			String endpointAddressSCPT = F3BProperties.getProperty("EAPPA_SCPT");
			ServiziConsultazionePagamentiTelematiciBeanServiceLocator scptbsl = new ServiziConsultazionePagamentiTelematiciBeanServiceLocator();
			scptbsl.setServiziConsultazionePagamentiTelematiciSOAPPortEndpointAddress(endpointAddressSCPT);
			ServiziConsultazionePagamentiTelematici scpt = scptbsl
					.getServiziConsultazionePagamentiTelematiciSOAPPort();

			for (int i = 0; i < lElencoBollettini.size(); i++) {
				BollettinoPagopaModel lBollettino = lElencoBollettini.elementAt(i);

				try {
					pagoPaLogger.debug(" Inizio richiesta a pagaPa per il bollettino = "
							+ lBollettino.getIdBollettinoPagopa() + " (IUV = " + lBollettino.getIuv() + ")");

					StatoRichiestaPagamento statoRichiesta = null;
					statoRichiesta = scpt.getPagamentoByCRS(lBollettino.getIuv()); // NOOOOO

					// Controllo lo stato
					String statoPagamento = statoRichiesta.getStato();

					// Quando entra la prima rata (da capire quale sarebbe) va registrata la data di
					// pagamento, ma anche
					// calcolata la data di scadenza delle successive rate collegate alla prima che scadono
					// l'ultimo del
					// mese a partir dal mese successivo alla data del pagamento della prima rata

					// Se il bollettino risulta pagato devo registrare la data di pagamento del bollettino e
					// calcolare
					// la data di scadenza della rate successive

					lBollettino.setStatoPagamento("NP");
					lBollettino.setImportoPagato(null);
					// lBollettino.set

					// lBollettino.setDataUltimoControllo(DateUtils.getSysDate());

					lBollettino.setCodOperatoreAggiornamento("BATCH");
					lBollettino.setCodUfficioAggiornamento("");
					lBollettino.setDataAggiornamento(DateUtils.getSysDate());

					pagoPaLogger.debug(
							" Aggiornamento stato pagamento con esito  " + lBollettino.getStatoPagamento());
					lCtrlBollettini.ExAggiornaStatoPagamentoBollettinoPagopa(lBollettino);
					pagoPaLogger.debug(" Aggiornamento effettuato");
				} catch (Exception e) {
					pagoPaLogger.error(" Errore in fase di invocazine del WS PagoPA per il bollettino: id = "
							+ lBollettino.getIdBollettinoPagopa() + " (IUV = " + lBollettino.getIuv() + ")");
					lBollettino.setStatoPagamento("NP");
					lBollettino.setDataUltimoControllo(DateUtils.getSysDate());

					lBollettino.setCodOperatoreAggiornamento("BATCH");
					lBollettino.setCodUfficioAggiornamento("");
					lBollettino.setDataAggiornamento(DateUtils.getSysDate());
					// Prevedere un campo per l'errore (esito)

					lCtrlBollettini.ExAggiornaStatoPagamentoBollettinoPagopa(lBollettino);
				}
			} // end for
		} catch (Exception e) {
			pagoPaLogger.error("Errore in fase di esecuzione del job di consultazionePagamenti", e);
		}
	}

}
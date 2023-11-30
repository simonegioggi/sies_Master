package siap.siep.pagoPaBatch.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBeanServiceLocator;
import it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento;
import siap.controller.SiapController;
import siap.sico.utente.model.UtenteModel;
import siap.siep.pagoPA.action.ICostantiPagoPA;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.pagoPaBatch.dao.BatchPagopaDAO;
import siap.siep.pagoPaBatch.dao.BatchPagopaSqlDAO;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.pagoPaBatch.model.BollettinoBatchPagopaModel;
import siap.siep.pagoPaBatch.model.CriteriRicercaBatchPagopaModel;
import siap.siep.pagoPaBatch.model.InvocazionePagopaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Batch per PagoPA
 *
 * @author d.fiorletta
 * @since MEV_2023-13
 * @version 1.0
 */
public class BatchPagopaController extends SiapController implements IBatchPagopa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);

	public BatchPagopaModel ExInserisciLancioBatchPagopa(BatchPagopaModel batchModel) throws F3BException {

		BatchPagopaModel batchModelRet = null;
		BatchPagopaDAO batchDao = null;
		Connection c = null;

		try {
			c = getDBConnection();

			batchModelRet = new BatchPagopaModel(batchModel);

			batchDao = new BatchPagopaDAO(c);

			// Inserimento Esito lancio
			batchDao.setDAOFromModel(batchModelRet);

			BigDecimal id = batchDao.insert();
			batchModelRet.setIdBatchPagopa(id);

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(c);
			throw new F3BException("BatchPagopaController.ExInserisciLancioBatchPagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			rollback(c);
			throw new F3BException("BatchPagopaController.ExInserisciLancioBatchPagopa : " + e);
		} finally {
			cleanup(batchDao);
			cleanup(c);
		}

		return batchModelRet;
	}

	public BatchPagopaModel ExAggiornaLancioBatchPagopa(BatchPagopaModel batchModel) throws F3BException {

		BatchPagopaModel batchModelRet = null;
		BatchPagopaDAO batchDao = null;
		Connection c = null;

		try {
			c = getDBConnection();

			batchModelRet = new BatchPagopaModel(batchModel);

			batchDao = new BatchPagopaDAO(c);

			siesLogger.debug("batchModelRet = " + batchModelRet);

			// Aggiornamento esito lancio batch
			batchDao.setDAOFromModelForUpdate(batchModelRet);
			batchDao.setCondizioneUpdate(batchModel.getIdBatchPagopa());
			batchDao.update();

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(c);
			throw new F3BException("BatchPagopaController.ExAggiornaLancioBatchPagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			rollback(c);
			throw new F3BException("BatchPagopaController.ExAggiornaLancioBatchPagopa : " + e);
		} finally {
			cleanup(batchDao);
			cleanup(c);
		}

		return batchModelRet;
	}

	public BatchPagopaModel ExRicercaBatchPagopaByKey(BigDecimal aIdBatch) throws F3BException {
		BatchPagopaModel lBatchModel = null;
		BatchPagopaSqlDAO lBatchSqlDao = null;
		Connection conn = null;

		try {
			conn = getDBConnection();

			lBatchSqlDao = new BatchPagopaSqlDAO(conn);

			lBatchSqlDao.ricercaBatchByKey(aIdBatch);

			lBatchModel = (BatchPagopaModel) lBatchSqlDao.getModelByKey();

			commit(conn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(conn);
			throw new F3BException("BatchPagopaController.ExRicercaBatchPagopaByKey : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			rollback(conn);
			throw new F3BException("BatchPagopaController.ExRicercaBatchPagopaByKey : " + e);
		} finally {
			cleanup(lBatchSqlDao);
			cleanup(conn);
		}

		return lBatchModel;
	}

	@SuppressWarnings("unchecked")
	public Vector<BatchPagopaModel> ExRecuperaLancioBatchPagopa(CriteriRicercaBatchPagopaModel criteriModel, int aPage)
			throws F3BException {

		Vector<BatchPagopaModel> listaLanci = null;
		BatchPagopaSqlDAO batchSqlDao = null;
		Connection conn = null;

		try {
			conn = getDBConnection();

			batchSqlDao = new BatchPagopaSqlDAO(conn);

			batchSqlDao.ricercaEsecuzioneBatchPaged(criteriModel, aPage);

			listaLanci = new Vector<BatchPagopaModel>(batchSqlDao.getModels());

			commit(conn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(conn);
			throw new F3BException("BatchPagopaController.ExRecuperaLancioBatchPagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			rollback(conn);
			throw new F3BException("BatchPagopaController.ExRecuperaLancioBatchPagopa : " + e);
		} finally {
			cleanup(batchSqlDao);
			cleanup(conn);
		}

		return listaLanci;
	}

  public void ExLancioBatchPagopa (UtenteModel aUtente) throws F3BException {
    siesLogger.debug("===================================================");
    siesLogger.debug(" ExLancioBatchPagopa - ConsultazionePagamenti      ");
    siesLogger.debug("===================================================");
    siesLogger.debug("");

    String esitoEsecuzione = "Batch avviato";
    String erroriEsecuzione = "";
    BatchPagopaModel lBatchModel = null;

    int contaNumPosDebitorieVerificate = 0;
    int contaNumIUVVerificati = 0; // 2023.11.02 nuovo contatore per i bollettini privi di CF
    int contaNumBollettiniAggiornati = 0;
    int contaNumBollettiniInErrore = 0;


    try {
      lBatchModel = new BatchPagopaModel();
      lBatchModel.setDataInizioEsecuzione(DateUtils.getSysDate());
      lBatchModel.setEsitoEsecuzione(esitoEsecuzione);
      lBatchModel = this.ExInserisciLancioBatchPagopa(lBatchModel);

      ServiziConsultazionePagamentiTelematici scpt = null;
      try {
        String endpointAddressSCPT = F3BProperties.getProperty("EAPPA_SCPT");
        siesLogger.debug(" Apertura connessione con endpoint = " + endpointAddressSCPT);
        ServiziConsultazionePagamentiTelematiciBeanServiceLocator scptbsl = new ServiziConsultazionePagamentiTelematiciBeanServiceLocator();
        scptbsl.setServiziConsultazionePagamentiTelematiciSOAPPortEndpointAddress(
            endpointAddressSCPT);
        scpt = scptbsl.getServiziConsultazionePagamentiTelematiciSOAPPort();
        siesLogger.debug(" Connessione aperta con successo");
      } catch (Exception e) {
        siesLogger.error("Errore in fase di apertura della connessione con l'endpoint pagoPA ", e);
        throw e;
      }

      String controllateDaGiorni = F3BProperties.getProperty("CONTROLLATEDAGIORNI");
      String inScadenzaTraGiorni = F3BProperties.getProperty("INSCADENZATRAGIORNI");
      String generatiDaGiorni = F3BProperties.getProperty("GENERATIDAGIORNI");
      String controllarePerGiorni = F3BProperties.getProperty("CONTROLLAREPERGIORNI");
      siesLogger.debug(
          " Ricerca debitori con posizioni aperte: inScadenzaTraGiorni = " + inScadenzaTraGiorni
              + ", controllateDaGiorni = " + controllateDaGiorni + ", generatiDaGiorni = "
              + generatiDaGiorni + ", controllarePerGiorni = " + controllarePerGiorni);

      int cdg = 0;
      int istg = 0;
      int gdg = 0;
      int cpg = 0;
      try {
        cdg = new Integer(controllateDaGiorni).intValue();
        istg = new Integer(inScadenzaTraGiorni).intValue();
        gdg = new Integer(generatiDaGiorni).intValue();
        cpg = new Integer(controllarePerGiorni).intValue();
      } catch (Exception e) {
        e.printStackTrace();
        siesLogger.error(e.getMessage());
        siesLogger.error("ATTENZIONE! Nel file f3b.properties sono stati inseriti valori "
            + "NON numerici per i parametri richiesti!");
        siesLogger.info("I parametri valgono entrambi 0!");
      }

      IBollettinoPagopa lCtrlBollettini = SIEPLookupRemote.getBollettinoPagopaRemote();
      Vector<BollettinoPagopaModel> lElencoDebitori = lCtrlBollettini
          .ExRicercaDebitoriConPosizioniAperteInScadenza(istg, cdg, gdg, cpg);
      siesLogger.debug("Debitori trovati = " + lElencoDebitori.size());

      // 2023.11.02 - Si aggiunge la gestione dei bollettini privi di CF a seguito del venir meno
      // dell'obbligatorietà
      // per SNT del CF
      Vector<BollettinoPagopaModel> lElencoBollettiniNoCF = lCtrlBollettini
          .ExRicercaBollettiniSenzaCFConPosizioniAperte(istg, cdg, gdg, cpg);
      siesLogger.debug("Bollettini privi di CF trovati = " + lElencoBollettiniNoCF.size());
      lElencoDebitori.addAll(lElencoBollettiniNoCF);
      // 2023.11.02 - FINE

      IInvocazionePagopa lCtrlInvocazione = SIEPLookupRemote.getInvocazionePagopaRemote();
      // Ricerca per debitore
      for (int i = 0; i < lElencoDebitori.size(); i++) {
        BollettinoPagopaModel lDebitore = lElencoDebitori.elementAt(i);
        String erroreInvocazione = "";
        InvocazionePagopaModel lInvocazioneModel = new InvocazionePagopaModel();

        try {
          if (lDebitore.getIuv() != null && lDebitore.getIuv().length() > 0) {
            siesLogger.debug(" Inizio richiesta a pagoPa per lo IUV = " + lDebitore.getIuv()
                + ", codice distretto = " + lDebitore.getCodiceDistretto());
            contaNumIUVVerificati++;
          } else {
            siesLogger.debug(
                " Inizio richiesta a pagoPa per il Debitore = " + lDebitore.getCodiceFiscale()
                    + ", codice distretto = " + lDebitore.getCodiceDistretto());
            contaNumPosDebitorieVerificate++;
          }
          // n.b. la ricerca per codiceCRS vuole comunque il codice fiscale obblgatorio altrimenti
          // non trova nulla
          // String codiceCRS = null; // "30091047213734917"; // solo per ricerca puntuale n.b
          // passare lo IUV ovvero numero di avviso senza il 3 iniziale

          String codiceCRS = null; // "330091047213734917";
          String tipologia = "PENPE";
          String codiceFiscale = lDebitore.getCodiceFiscale();
          String codiceDistretto = lDebitore.getCodiceDistretto();
          // "GLTO"; // dipende dal distretto!!!!!!!!!!!!!!!!!!!!!!!!!!!!
          // Deve essere un parametro letto da F£B.properties
          String causale = null; // "Pagamenti in favore Amministrazione";
          // n.b. i bollettini restituiti in ambiente di test hanno causale="Sanzioni penali"
          String stato = null; // ICostantiPagoPA.SIES_STATO_PAGATO; // servirebbero solo i pagati

          // L'elemento stato contiene l'indicazione dello stato del pagamento nel contesto del PST
          // e può assumere uno dei seguenti valori
          // - GENERATA - Valore ottenuto in test dopo la generazione del bollettino
          // - CARRELLO - indica che la richiesta è nel carrello (non pagato)
          // - DISPONIBILE - Il valore indica che è presente una Ricevuta Telematica con esito
          // positivo
          // (pagamento eseguito).
          // - ALTRI NON DI INTERESSE

          // servono quelli pagati? se CARRELLO allora sono quelli ancora non pagati
          // vedi pg 12 - APPLICATIVI - Flussi pagamento telematico tramite PST vers. 3.1.pdf
          Calendar dataRichiestaDa = null;
          Calendar dataRichiestaA = null;
          int dimensionePagina = 0;
          int numeroPagina = 0;
          Calendar dataRicevutaDa = null;
          Calendar dataRicevutaA = null;

          // MEV_2023-33 - Si tiene traccia di ogni invocazione al WS
          lInvocazioneModel.setCodiceFiscale(codiceFiscale);
          lInvocazioneModel.setDataInvocazione(new Date());
          lInvocazioneModel.setFkIdBatch(lBatchModel.getIdBatchPagopa());
          lInvocazioneModel.setCodOperatoreInserimento("BATCH_PAGOPA");
          lInvocazioneModel.setDataInserimento(new Date());

          // 2023.11.02 - Aggiungo lo IUV se presente
          lInvocazioneModel.setIuv(lDebitore.getIuv());
          // 2023.11.02 - FINE

          lInvocazioneModel = lCtrlInvocazione.ExInserisciInvocazionePagopa(lInvocazioneModel);
          // MEV_2023-33

          Object[] pagamenti = null;
          // Nota: per lo stesso soggetto (CF) potrebbero essere presenti più fascicoli
          // 2023.11.02 - Se sto elaborando un Bollettini privo di CF allora aggiungo alla
          // chiamata lo IUV (codiceCRS)
          if (lDebitore.getIuv() != null && lDebitore.getIuv().length() > 0) {
            siesLogger.debug("Bollettini privo di CF aggiungo lo IUV alla chiamata 3"
                + lDebitore.getIuv());
            codiceCRS = "3" + lDebitore.getIuv();
          }
          // 2023.11.02

          /*RisultatoRicerca rr = scpt.elencoPagamenti(codiceCRS, tipologia, codiceFiscale,
              codiceDistretto, causale, stato, dataRichiestaDa, dataRichiestaA,
              dimensionePagina, numeroPagina);*/
          RisultatoRicerca rr = scpt.elencoPagamenti(codiceCRS, tipologia, codiceFiscale,
              codiceDistretto, causale, stato, dataRichiestaDa, dataRichiestaA,
              dimensionePagina, numeroPagina, dataRicevutaDa, dataRicevutaA);
          // MEV_2023-33 Si registrano i dati scambiati: XML
          org.apache.axis.client.Call _call = scpt.getLastCall();
          String requestXML = _call.getMessageContext().getRequestMessage()
              .getSOAPPartAsString();
          String responseXML = _call.getMessageContext().getResponseMessage()
              .getSOAPPartAsString();
          siesLogger.debug("requestXML \n" + requestXML);
          siesLogger.debug("responseXML \n" + responseXML);

          lInvocazioneModel.setXmlRichiesta(requestXML);
          lInvocazioneModel.setXmlRisposta(responseXML);

          lInvocazioneModel.setErrore(null);
          lInvocazioneModel = lCtrlInvocazione.ExAggiornaInvocazionePagopa(lInvocazioneModel);
          // MEV_2023-33 fine

          siesLogger.debug("RisultatoRicerca.getCount()            = " + rr.getCount());
          siesLogger.debug("RisultatoRicerca.getDimensionePagina() = " + rr.getDimensionePagina());
          siesLogger.debug("RisultatoRicerca.getNumeroPagina()     = " + rr.getNumeroPagina());
          siesLogger.debug("RisultatoRicerca.getItems().length     = "
              + (rr.getItems() != null ? rr.getItems().length : null));
          pagamenti = rr.getItems();
          

          if (pagamenti == null || pagamenti.length == 0) {
            if (lDebitore.getIuv() != null && lDebitore.getIuv().length() > 0) {
              siesLogger.warn(
                  "Nessuna StatoRichiesta restituito per lo IUV " + lDebitore.getIuv());
              esitoEsecuzione = appendString(esitoEsecuzione,
                  "\nNessuna StatoRichiesta restituita per lo IUV " + lDebitore.getIuv());
            } else {
              siesLogger.warn(
                  "Nessuna StatoRichiesta restituito per il debitore " + codiceFiscale);
              esitoEsecuzione = appendString(esitoEsecuzione,
                  "\nNessuna StatoRichiesta restituita per il debitore " + codiceFiscale);
            }
          } else {
            // recupero in una sola transazione tutti i bollettini a carico del CF per poterli
            // confrontare con quelli restituiti del WS

            for (int progPagamento = 0; progPagamento < pagamenti.length; progPagamento++) {
              BollettinoPagopaModel bollettinoSIES = null;
              try {
                StatoRichiestaPagamento statoRichiesta = null;
                if (pagamenti[progPagamento] instanceof StatoRichiestaPagamento) {
                  statoRichiesta = (StatoRichiestaPagamento) pagamenti[progPagamento];
                  siesLogger.debug("statoRichiesta " + statoRichiesta.toString1());
                  // Devo verificare se il pagamento restituito è tra quelli pending e in
                  // caso aggiornare lo stato
                  // Recupero il pagamento per numero di avviso (NON CRS)
                  // tolgo il primo carattere (3)
                  String iuv = statoRichiesta.getNumeroAvviso().substring(1);
                  bollettinoSIES = lCtrlBollettini.ExRicercaBollettinoPagopaByIUV(iuv);
                  if (bollettinoSIES != null) {
                    if (ICostantiPagoPA.SIES_STATO_NON_PAGATO
                        .equals(bollettinoSIES.getStatoPagamento())) {
                      bollettinoSIES.setDataUltimoControllo(DateUtils.getSysDate());
                      //bollettinoSIES.setCodOperatoreAggiornamento("BATCH");
                      //bollettinoSIES.setCodUfficioAggiornamento("BATCH");
                      bollettinoSIES.setCodOperatoreAggiornamento(aUtente.getUserId());
                      bollettinoSIES.setCodUfficioAggiornamento("BATCH");
                      bollettinoSIES.setDataAggiornamento(DateUtils.getSysDate());

                      // Loggo comunque lo stato
                      bollettinoSIES.setStatoPagopa(statoRichiesta.getStato());

                      // MEV_2023-33 si storicizza l'esito ricevuto
                      try {
                        BollettinoBatchPagopaModel lBollBatchModel = new BollettinoBatchPagopaModel();
                        lBollBatchModel.setFkIdInvocazionePagopa(
                            lInvocazioneModel.getIdInvocazionePagopa());
                        lBollBatchModel.setFkIdBollettinoPagopa(
                            bollettinoSIES.getIdBollettinoPagopa());
                        lBollBatchModel
                            .setFkIdBatchPagopa(lBatchModel.getIdBatchPagopa());
                        lBollBatchModel.setStatoPagopa(statoRichiesta.getStato());

                        lCtrlInvocazione
                            .ExInserisciBollettinoBatchPagopa(lBollBatchModel);
                      } catch (Exception e) {
                        // Trattandosi di tracciatura non si blocca l'aggiornamento
                        siesLogger.error(
                            "Errore in fase di inserimento sulla tabella di tracciatura BOLLETTINO_BATCH_PAGOPA",
                            e);
                      }
                      // MEV_2023-33

                      if (statoRichiesta.getStato()
                          .equals(ICostantiPagoPA.PAGOPA_STATO_DISPONIBILE)) {
                        // Il bollettino è stato pagato. Aggiorno opportunamente il
                        // campo; PA = Pagato
                        bollettinoSIES
                            .setStatoPagamento(ICostantiPagoPA.SIES_STATO_PAGATO);
                        bollettinoSIES.setDataAvvPagamento(
                            DateUtils.getDate(statoRichiesta.getDataRicevuta()));
                        // verificare che sia l'importo veramente pagato
                        bollettinoSIES.setImportoPagato(
                            BigDecimal.valueOf(statoRichiesta.getImporto()));
                        contaNumBollettiniAggiornati++;
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
                      siesLogger.debug(" Aggiornamento stato pagamento con esito  "
                          + bollettinoSIES.getStatoPagamento());
                      lCtrlBollettini
                          .ExAggiornaStatoPagamentoBollettinoPagopa(bollettinoSIES);
                      siesLogger.debug(" Aggiornamento effettuato");
                    }
                  } else {
                    siesLogger.warn("IUV non trovato = " + erroreInvocazione.length());
                    erroreInvocazione += ((erroreInvocazione.length() > 0 ? "\n" : "")
                        + "warn iuv " + iuv + " non presente in banca dati");
                    siesLogger.warn(
                        "IUV non trovato erroreInvocazione " + erroreInvocazione);
                    siesLogger.warn("Per il numero di avviso " + iuv
                        + " non esiste alcuna posizione sulla tabella dei bollettini");
                    // esitoEsecuzione = appendString(esitoEsecuzione,
                    // "\nwarn iuv " + iuv + " non presente in banca dati");
                  }
                } else {
                  siesLogger.warn(
                      "L'oggetto restituito NON è una istanza di StatoRichiestaPagamento");
                }
              } catch (Exception e) {
                siesLogger.error("Exception", e);
                // erroriEsecuzione = appendString(erroriEsecuzione, "\n" + e.getMessage());
                // Errore sul singolo bollettino
                if (bollettinoSIES != null) {
                  bollettinoSIES.setDataUltimoControllo(DateUtils.getSysDate());

                  bollettinoSIES.setCodOperatoreAggiornamento(aUtente.getUserId());
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
          contaNumBollettiniInErrore++;
          String errore = "";
          if (lDebitore.getIuv() != null && lDebitore.getIuv().length() > 0) {
            siesLogger.error(
                " Errore in fase di invocazine del WS PagoPA.elencoPagamenti per lo IUV = "
                    + lDebitore.getIuv(),
                e);
            errore = "Errore in fase di invocazine del WS PagoPA.elencoPagamenti per IUV = "
                + lDebitore.getIuv();
          } else {
            siesLogger.error(
                " Errore in fase di invocazine del WS PagoPA.elencoPagamenti per il Debitore: CF = "
                    + lDebitore.getCodiceFiscale(),
                e);
            errore = "Errore in fase di invocazine del WS PagoPA.elencoPagamenti per il Debitore: CF = "
                + lDebitore.getCodiceFiscale();
          }
          errore += "\n" + e.getMessage();
          siesLogger.error("Numero Bollettini in ERRORE = " + contaNumBollettiniInErrore);
          siesLogger.error("ERRORE = " + errore);

          lInvocazioneModel.setErrore(e.getMessage());
          lInvocazioneModel = lCtrlInvocazione.ExAggiornaInvocazionePagopa(lInvocazioneModel);
        } finally {
          if (erroreInvocazione.length() > 0) {
            lInvocazioneModel.setErrore(erroreInvocazione);
            lInvocazioneModel = lCtrlInvocazione.ExAggiornaInvocazionePagopa(lInvocazioneModel);
          }
        }
      }

      esitoEsecuzione = "Batch terminato";
    } catch (Exception e) {
      esitoEsecuzione = "Batch terminato con ";
      siesLogger.error("Errore in fase di esecuzione del job di consultazionePagamenti", e);
      erroriEsecuzione = appendString(erroriEsecuzione,
          "\nErrore in fase di esecuzione del job di consultazionePagamenti");
    } finally {
      try {
        // Aggiorno l'esecuzione del batch
        lBatchModel.setDataFineEsecuzione(DateUtils.getSysDate());
        lBatchModel.setNumPosDebitorieVerificate(new BigDecimal(contaNumPosDebitorieVerificate));
        lBatchModel.setNumIUVVerificati(new BigDecimal(contaNumIUVVerificati));
        lBatchModel.setNumBollettiniAggiornati(new BigDecimal(contaNumBollettiniAggiornati));
        lBatchModel.setNumErroriInvocazione(new BigDecimal(contaNumBollettiniInErrore));

        if (contaNumBollettiniInErrore > 0) {
          esitoEsecuzione = "Batch terminato con segnalazioni";
        }

        lBatchModel.setEsitoEsecuzione(esitoEsecuzione);
        lBatchModel.setErroreEsecuzione(erroriEsecuzione);

        lBatchModel = this.ExAggiornaLancioBatchPagopa(lBatchModel);
      } catch (Exception e) {
        siesLogger.error("Errore in fase di Aggiornamento BATCH_PAGOPA", e);
      }
    }

    siesLogger.debug("===================================================");
    siesLogger.debug(" Job di PagoPA terminato - ConsultazionePagamenti  ");
    siesLogger.debug("===================================================");
  
  }
  
  private String appendString(String stringa, String strToAppend) {

    int maxLength = 2000;

    if ((stringa.length() + strToAppend.length()) < maxLength)
      stringa = stringa + strToAppend;
    else
      stringa = stringa.substring(strToAppend.length()) + strToAppend;

    return stringa;
  }
  
  
  @SuppressWarnings("unchecked")
  public BatchPagopaModel getLastEsecuzioneBatch() throws F3BException 
  {
    BatchPagopaModel lLastLancio = null;
    BatchPagopaSqlDAO batchSqlDao = null;
    Connection conn = null;

    try {
      conn = getDBConnection();

      batchSqlDao = new BatchPagopaSqlDAO(conn);

      batchSqlDao.ricercaUltimaEsecuzioneBatch();

      lLastLancio = (BatchPagopaModel) batchSqlDao.getModelByKey();

      commit(conn);
    } catch (DAOException ex) {
      siesLogger.error("DAOException", ex);
      rollback(conn);
      throw new F3BException("BatchPagopaController.getLastEsecuzioneBatch : " + ex);
    } catch (Exception e) {
      siesLogger.error("Exception", e);
      rollback(conn);
      throw new F3BException("BatchPagopaController.getLastEsecuzioneBatch : " + e);
    } finally {
      cleanup(batchSqlDao);
      cleanup(conn);
    }

    return lLastLancio;
  }
}
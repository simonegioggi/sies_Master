package siap.siep.istruttoria.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.jms.SIAPReceiver;
import siap.jms.connection.ConnectionPoolJMS;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.FascicoloSiepStampaController;
import siap.siep.fascicolo.controller.IFascicoloSiepStampa;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.dao.IstruttoriaNotizieReatoSqlDAO;
import siap.siep.istruttoria.dao.IstruttoriaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.stampadocumenti.dao.StampaDocumentiDAO;
import siap.siep.stampadocumenti.model.StampaDocumentiModel;
import siap.siep.util.SIEPLookupRemote;
import siap.util.SIAPPathProperties;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: IstruttoriaController
 * </p>
 * <p>
 * Description: Controller dell 'Istruttoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class IstruttoriaController extends SiapController implements IIstruttoria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	private String mPath = null;

	/**
	 * Stampa un documento di Istruttoria
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaIstruttoria(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			IEvento lEvCrtl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEventoModel = lEvCrtl.ExRicercaEventoNotificaByKey(aEvento.getEvento()
					.getIdEvento());

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			TreeModel lTree = lStampa.prelevaDatiIstruttoria(lEventoModel, aUtente);

			ReportGenerator lReport = new ReportGenerator();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Chiave = " + aEvento.getNomeTemplate());
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			/*
			 * aProvvedimento.setDocumentoOut( lByteArrayOut ); aEvento.setDocBlobIn( lByteArrayInput );
			 */
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("EVENTO >>> " + aEvento.getEvento().toString());
			// lModel.setIdEvento(aEvento.getIdEvento());
			// lModel.setDocBlobOut(lByteArrayOut);
			aEvento.getEvento().setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstruttoriaController.ExStampaIstruttoria: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * stampa il documento per la stampa copertina
	 * 
	 * @param aFasc
	 * @param lIdTemplate
	 * @param aUtente
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumento(FascicoloSiepModel aFasc, String lIdTemplate,
			UtenteModel aUtente) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		IFascicoloSiepStampa lCtrStam = SIEPLookupRemote.getFascicoloSiepStampaRemote();
		TreeModel lTree = lCtrStam.prelevaDatiStampaFascicolo(aFasc, aUtente);

		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

		ReportGenerator lReport = new ReportGenerator();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * stampa il documento per la stampa delle copertine
	 * 
	 * @param aFasc
	 * @param aUtente
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaCopertineMultiple(FascicoloSiepModel aFasc, UtenteModel aUtente)
			throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		IFascicoloSiepStampa lCtrStam = SIEPLookupRemote.getFascicoloSiepStampaRemote();

		TreeModel lTree = lCtrStam.prelevaDatiStampaFascicoliMultipli(aFasc, aUtente);

		ReportGenerator lReport = new ReportGenerator();

		// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
		mPath = mPathProperties.getProperty("SIEPCOPERTINE");
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, mPath);

		return lByteArrayOut;
	}

	/**
	 * Ricerca gli IdFascicolo dei fascicoli che ricadono nell'intervallo di ricerca
	 * 
	 * @param aFasc
	 *            - Model con gli estremi delle ricerca
	 * @param aValidato
	 *            - se true viene effettuata la sola ricerca dei fascicoli con flag_validato a S
	 * @return Vector - Vettore di FascicoloSiepModel con valorizzati i soli id_fascicolo_siep che ricadono
	 *         nell'intervallo
	 * @throws F3BException
	 */
	public Vector ExCercaIntervalloFascicoli(FascicoloSiepModel aFasc, boolean aValidato) throws F3BException {

		Vector lFascicoli = null;
		IstruttoriaSqlDAO lIstrDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lIstrDao = new IstruttoriaSqlDAO(lConn);

			if (aValidato)
				lIstrDao.ricercaIdFascicoliEsecuzione(aFasc);
			else
				lIstrDao.ricercaIdFascicoli(aFasc);

			lFascicoli = new Vector(lIstrDao.getModels());

//			Iterator lItx = lFascicoli.iterator();

			if (lFascicoli.size() == 0)
				throw new F3BException("Nessun procedimento nell'intervallo impostato!");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstruttoriaController.ExCercaIntervalloFascicoli: " + daoEx);
		} finally {
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Metodo invocato dal Listener Di Stampa. Effettua la Stampa Inizio Esecuzione per l'elenco dei fascicoli
	 * specificati nel messaggio.
	 * 
	 * @param FascicoloSiepModel
	 *            model di appoggio contenente l'intervallo di fascicoli per i quali generare la stampa inizio
	 *            esecuzione
	 * @param UtenteModel
	 * @param UfficioModel
	 * @param lSequence
	 *            = id del record tabella Stampa_documenti da aggiornare con il blob generato
	 */
	public boolean ExStampaInizioEsecuzioneMultiple(FascicoloSiepModel aFasc, UtenteModel aUtente,
			UfficioModel aUfficio, BigDecimal lSequence) throws F3BException {

		StampaDocumentiDAO lStampaDAO = null;
		StampaDocumentiDAO lStampaBlobDAO = null;

		Connection lConn = null;
		Connection lConnErr = null;

		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("STAMPA INSERITA " + lSequence);

			lConn = getDBTransaction();
			FascicoloSiepStampaController lFasc = new FascicoloSiepStampaController();

			// Creo la root del documentone finale
			TreeModel lTreeRoot = new TreeModel(lFasc.createRootFascicolo(aFasc, aUtente));
			lTreeRoot.add(new TreeModel(aUtente));

			// Nome file xml su cui memorizzo tutti i vari documenti cercati.
			String lNomeFileXml = aUtente.getUserId() + DateUtils.getSysDate().getTime() + ".xml";

			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("TEMP");
			File outFile = new File(mPath + lNomeFileXml);
			FileWriter out = new FileWriter(outFile);
			out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			out.write("\n<X>");
			out.flush();
			out.close();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Scritto Fileeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

			// ========================================================================
			// Recupera gli id dei fascicoli VALIDATI che ricadono nell'intervallo
			// ========================================================================
			Vector lFascicoli = this.ExCercaIntervalloFascicoli(aFasc, true);
			Iterator lItxFasc = lFascicoli.iterator();

			// ========================================================================
			// Per ogni fascicolo si effettua la Stampa Inizio Esecuzione
			// ========================================================================
			while (lItxFasc.hasNext()) {
				FascicoloSiepModel lFascicolo = (FascicoloSiepModel) lItxFasc.next();
				// Inserisco l'evento per ogni fascicolo selezionato
				// Cerca se l'INizio Esecuzione è già esistente

				EventoNotificaModel lRetModel = new EventoNotificaModel();
				IEvento lCtrl = SICOLookupRemote.getEventoRemote();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("\n>>> FASCICOLO ID = " + lFascicolo.getIdFascicoloSiep() + "\n");

				EventoModel lEveMod = new EventoModel();
				lEveMod.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
				lEveMod.setCodMotivo("0047");
				lEveMod.setCodTipoEvento("05");

				Vector lVect = new Vector();
				try {
					lVect = lCtrl.ExRicercaEvento(lEveMod);
				} catch (F3BException ex) { // Non è stato trovato nessun elemento
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
					siesLogger.debug(">>> +++ *** Nessun Evento Trovato!");
				}

				if (lVect != null && lVect.size() > 0) {
					// lRetModel.setEvento((EventoModel) lVect.firstElement());
					EventoModel lEve = (EventoModel) lVect.firstElement();
					// IEvento lEvCrtl= SICOLookupRemote.getEventoRemote();
					lRetModel = lCtrl.ExRicercaEventoNotificaByKey(lEve.getIdEvento());

				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
					siesLogger.debug(">>> +++ *** INSERISCO EVENTO INIZIO ESECUZIONE!");
					// --- Inserisci Evento Inizio Esecuzione
					EventoNotificaModel lEve = new EventoNotificaModel();

					lEve.getEvento().setCodTipoEvento("05"); // Tipo Evento = Richiesta Istruttoria
					lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza

					// Codice motivo da CG_REF_CODES....
					// IDecodifiche lDec = SICOLookupRemote.getDecodificheRemote();
					// DecodificheModel lDecMod = lDec.ExRicercaDecodificheByHighValue("IS01");
					lEve.getEvento().setCodMotivo("0047");

					lEve.getEvento().setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
					// 08/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
					// lEve.getEvento().setDataEmissione(DateUtils.getSysDate());
					lEve.getEvento().setDataEmissione(
							DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
					lEve.getEvento().setCodOperatoreInserimento(aUtente.getUserId());
					lEve.getEvento().setCodLuogoEmittente(aUfficio.getCodComune());
					lEve.getEvento().setCodUfficioEmittente(aUfficio.getCodUfficio());
					lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
					lEve.getEvento().setCodUfficioInserimento(aUfficio.getCodUfficio());
					lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
					lEve.getEvento().setCodEsito("-");
					lEve.getEvento().setCodLuogoDestinatario("-");
					lEve.getEvento().setCodTipoUfficioDestinatario("-");

					NotificaModel lNotifiche[] = new NotificaModel[1];
					NotificaModel lNot = new NotificaModel();

					lNot.setCodTipoNotifica("N");
					lNot.setDataInvio(DateUtils.getSysDate());
					lNot.setCodEsito("-");
					lNot.setCodOperatoreInserimento(aUtente.getUserId());
					lNot.setDataInserimento(DateUtils.getSysDate());
					lNot.setCodUfficioInserimento(aUfficio.getCodUfficio());
					lNot.setUffCodUfficio(aUfficio.getCodUfficio());

					lNotifiche[0] = lNot;

					// Inserisco l'array di Notifiche nell'Evento
					lEve.setNotifiche(lNotifiche);
					lEve.getEvento().setFlagDocumentoRegistrato("N");

					// IEvento lCtrl = SICOLookupRemote.getEventoRemote();
					lRetModel = lCtrl.ExInserisciEventoNotifica(lEve, lConn);
				}

				lRetModel.setNomeTemplate("ISIP");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" * * * Ufficio = " + aUfficio);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Setto la descrizione dell'Ufficio = " + aUfficio.getDescrTipoUfficio());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Setto la descrizione dell'Ufficio = " + aUfficio.getDescrComune());
				lRetModel.getEvento().setDescrUfficioEmittente(aUfficio.getDescrTipoUfficio());
				lRetModel.getEvento().setDescrLuogoEmittente(aUfficio.getDescrComune());

				aUtente.setUfficioUtente(aUfficio);

				/*TreeModel lTree = */this.ExStampaMultiInizio(lRetModel, aUtente, lNomeFileXml, lConn);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(">>> PRIMA DI lReport TREE MODEL");
			ReportGenerator lReport = new ReportGenerator();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(">>> PRIMA DI lByteArrayOut TREE MODEL");

			// Chiudo l'xml
			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("TEMP");
			outFile = new File(mPath + lNomeFileXml);
			out = new FileWriter(outFile, true);
			out.write("\n</X>");
			out.flush();
			out.close();

			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("SIEPISIPMULTI");
			ByteArrayOutputStream lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocumentFromFile(
					lNomeFileXml, mPath);
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(">>> Dopo di lByteArrayOut TREE MODEL " + lByteArrayOut.size());

			// Devo inserire la Stampa generata nella tabella STAMPA
			StampaDocumentiModel lStampaBlob = new StampaDocumentiModel();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Sto Eseguendo l'update di Stampa per id " + lSequence);
			// Devo inserire il documento
			lStampaBlob.setIdStampa(lSequence);
			lStampaBlob.setStato("1");
			lStampaBlob.setDocBlobIn(lByteArrayInput);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(">>> Dopo di lByteArrayOut TREE MODEL " + lByteArrayInput.available());

			lStampaDAO = new StampaDocumentiDAO(lConn);

			lStampaDAO.setDAOFromModelForUpdateBlob(lStampaBlob);
			lStampaDAO.selCondizioneUpdate(lSequence);
			lStampaDAO.update();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Eseguita l'UPDATE di Stampa");

			commit(lConn);

			// cancello il file temporaneo creato per il file XML
			outFile.delete();
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ERRORE nella generazione del DOC di stampa" + ex, ex);
			rollback(lConn);

			// Scrittura su Stampa che l'operazione è andata in errore.
			lConnErr = getDBConnection();
			try {
				StampaDocumentiModel lStampa = new StampaDocumentiModel();
				lStampa.setIdStampa(lSequence);
				lStampa.setStato("2");
				lStampaBlobDAO = new StampaDocumentiDAO(lConnErr);
				lStampaBlobDAO.setDAOFromModelForUpdateBlob(lStampa);
				lStampaBlobDAO.selCondizioneUpdate(lSequence);
				lStampaBlobDAO.update();
				commit(lConnErr);
			} catch (Exception eex) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Non sono riuscito neanche a scrivere errore in tabella" + eex, eex);
			}
		} finally {
			cleanup(lStampaDAO);
			cleanup(lStampaBlobDAO);

			cleanup(lConn);
			cleanup(lConnErr);

		}
		return true;

	}

	/**
	 * Produce il documento e aggiorna l'evento con il blob
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	private TreeModel ExStampaMultiInizio(EventoNotificaModel aEvento, UtenteModel aUtente, String aFileXml,
			Connection lConn) throws F3BException {

		// Connection lConn = null;
		EventoDAO lEveDao = null;
		TreeModel lTree = null;
		TreeModel lTreeCopy = new TreeModel();

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("ExStampaMultiInizio = " + aEvento.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Descr Ufficio Emittente = " + aEvento.getEvento().getDescrUfficioEmittente());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Utente = " + aUtente);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Evento = " + aEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Ufficio = " + aUtente.getUfficioUtente());

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			lTree = lStampa.prelevaDatiIstruttoria(aEvento, aUtente);

			ReportGenerator lReport = new ReportGenerator();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Chiave = " + aEvento.getNomeTemplate());
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocumentAndStoreXml(lTree, lNomeTemplate,
					aFileXml);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			aEvento.getEvento().setDocBlobIn(lByteArrayInput);
			// lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());
			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstruttoriaController.ExStampaMultiInizio: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e.getStackTrace()[0]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e.getStackTrace()[1]);
			throw new F3BException(
					"IstruttoriaController.ExStampaMultiInizio: Non posso inserire l'Evento : " + e);
		} finally {
			cleanup(lEveDao);
			// cleanup(lConn);
		}

		return lTreeCopy;
	}

	/**
	 * Metodo che inserisce la richiesta di stampa (STAMPA_DOCUMENTO) e invia sul sistema asincrono JMS la
	 * richiesta di stampa per rendere l'esecuzione asincrona
	 * 
	 */
	public BigDecimal ExInserisciRichiestaJmsStampaInizioEsecuzioneMultiple(FascicoloSiepModel aFasc,
			UtenteModel aUtente, UfficioModel aUfficio) throws F3BException {

		QueueConnection lQueueConn = null;
		ConnectionPoolJMS lConnPool = null;
		StampaDocumentiDAO lStampaDAO = null;
		Connection lConn = null;
		BigDecimal lSequence = null;
		String lDescrizione = "Stampa Inizio Esecuzione da procedimento n. " + aFasc.getChiaveProgrIniziale()
				+ "/" + aFasc.getChiaveAnnoIniziale() + " a " + aFasc.getChiaveProgrFinale() + "/"
				+ aFasc.getChiaveAnnoFinale();

		aUtente.setUfficioUtente(aUfficio);

		try {
			lConnPool = ConnectionPoolJMS.getInstance();

			lQueueConn = lConnPool.getConnection();
			QueueSession qSession = lQueueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			lConn = getDBTransaction();

			// Inserisco il record di stampa
			StampaDocumentiModel lStampa = new StampaDocumentiModel();
			lStampa.setData(DateUtils.getSysDate());
			lStampa.setIdUtente(aUtente.getUserId());
			lStampa.setStato("0");
			lStampa.setDescrizione(lDescrizione);
			lStampa.setNumeStampeEffettuate(new Integer(0));
			lStampa.setNumStampeRichieste(new Integer(0));
			// lStampa.setDocBlobOut(lByteArrayOut);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Prima della Insert su Stampa");

			// Inserisco la Stampa
			lStampaDAO = new StampaDocumentiDAO(lConn);
			lStampaDAO.setDAOFromModel(lStampa);
			lSequence = lStampaDAO.insert();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Dopo della Insert su Stampa" + lSequence);

			// Creo un Text Message
			Message lMessage = qSession.createObjectMessage(aUtente);

			// Definisco la coda di Spedizione Locale
			String lNameQueue = "Stampa";
			Queue queue = qSession.createQueue(lNameQueue);

			if (queue != null) {
				// Definisco un Queue Sender
				QueueSender qSender = qSession.createSender(queue);

				if (qSender != null) {
					// Setto le properties del Messaggio partendo dal MessaggioModel
					lMessage.setStringProperty("TIPO_OPERAZIONE", "StampaInizioEsecuzioneMultiple");
					lMessage.setStringProperty("UTENTE", aUtente.getUserId());
					lMessage.setStringProperty("PROGR_INIZIALE", "" + aFasc.getChiaveProgrIniziale());
					lMessage.setStringProperty("PROGR_FINALE", "" + aFasc.getChiaveProgrFinale());
					lMessage.setStringProperty("ANNO_INIZIALE", "" + aFasc.getChiaveAnnoIniziale());
					lMessage.setStringProperty("ANNO_FINALE", "" + aFasc.getChiaveAnnoFinale());
					lMessage.setStringProperty("COD_UFFICIO", aUfficio.getCodUfficio());
					lMessage.setStringProperty("COMUNE_UFFICIO", aUfficio.getCodComune());
					lMessage.setStringProperty("TIPO_UFFICIO", aUfficio.getDescrTipoUfficio());
					lMessage.setStringProperty("ID_STAMPA", "" + lSequence);

					// lMessage.setObjectProperty("UTENTE", aUtente);

				}
				qSender.send(lMessage);

				commit(lConn);

				lConnPool.releaseConnection(lQueueConn);

				// --- Faccio partire i Listner sulle code
				SIAPReceiver.getInstance().testStampa();
				// SIAPReceiver.getInstance();
			}
		} catch (JMSException jmsEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error(
					"ExInserisciRichiestaJmsStampaInizioEsecuzioneMultiple():Exception = "
							+ jmsEx.getErrorCode());
			jmsEx.printStackTrace();
			rollback(lConn);
			if (lConnPool != null) {
				lConnPool.releaseConnection(lQueueConn);
			}
			throw new F3BException("Eccezione durante la spedizione del messaggio: " + jmsEx);
		} catch (Exception exception) {
			exception.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error(
					"SIAPSender.ExInserisciRichiestaJmsStampaInizioEsecuzioneMultiple():Exception = "
							+ exception);
			rollback(lConn);
			if (exception.toString().equals("java.lang.NullPointerException"))
				exception.printStackTrace();

			if (lConnPool != null) {
				lConnPool.releaseConnection(lQueueConn);
			}
			throw new F3BException("Eccezione durante la spedizione del messaggio: " + exception);
		} finally {
			if (lConnPool != null)
				lConnPool.releaseConnection(lQueueConn);

			cleanup(lStampaDAO);
			cleanup(lConn);

		}

		return lSequence;

	}

	/**
	 * ExControllaNotizieDiReatoRege - Controlla su Rege la presenza di un titolo esecutivo e soggetto che ha
	 * anche delle notizie di reato da importare.
	 */
	public String ExControllaNotizieDiReatoRege(SoggettoModel aSogg, SentenzaModel aSentenza)
			throws F3BException {

		String lKeyRege = "";
		IstruttoriaNotizieReatoSqlDAO lIstRegeDao = null;

		Connection lConn = null;
		try {
			lConn = getDBConnection();
			lIstRegeDao = new IstruttoriaNotizieReatoSqlDAO(lConn);
			lIstRegeDao.getNotizieRege(aSogg, aSentenza);
			lIstRegeDao.start();
			if (lIstRegeDao.next())
				lKeyRege = lIstRegeDao.getString("idFile");
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"IstruttoriaController.ExControllaNotizieDiReatoRege: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e.getStackTrace()[0]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e.getStackTrace()[1]);
			throw new F3BException(
					"IstruttoriaController.ExControllaNotizieDiReatoRege: Non posso inserire l'Evento : " + e);
		} finally {
			cleanup(lIstRegeDao);
			cleanup(lConn);

		}

		return lKeyRege;
	}

}
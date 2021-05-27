package siap.sige.decretounificazione.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.report.ReportGenerator;
import siap.sige.SIGEException;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.dao.FascicoloSigeDAO;
import siap.sige.fascicolo.dao.FascicoloSigeSqlDAO;
//import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.dao.TenoreSentenzaReatoDAO;
import siap.sige.tenore.dao.TenoreSigeDAO;
import siap.sige.tenore.dao.TenoreSigeSqlDAO;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: DecretoUnificazioneSigeController
 * </p>
 * <p>
 * Description: Classe Controller per DecretoUnificazioneSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecretoUnificazioneSigeController extends SiapController implements IDecretoUnificazioneSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Verifica del Procedimento Sige prima dell' Unificazione.
	 * <p>
	 *
	 * @param aAnnoFascicolo
	 * @param aNumeroFascicolo
	 * @param aUfficioUtenteConnesso
	 * @param aRuoloFascicolo
	 * @return lFasEstMod
	 * @throws F3BException
	 */
	public FascicoloSigeEstesoModel ExVerificaFascicoloSigePerUnificazione(String aAnnoFascicolo,
			String aNumeroFascicolo, String aUfficioUtenteConnesso, String aRuoloFascicolo)
			throws F3BException {

		FascicoloSigeEstesoModel lFasEstMod = new FascicoloSigeEstesoModel();
		FascicoloSigeModel lFasMod = new FascicoloSigeModel();

		lFasMod.setChiaveAnno(new BigDecimal(aAnnoFascicolo));
		lFasMod.setChiaveProgr(new BigDecimal(aNumeroFascicolo));
		lFasMod.setChiaveUfficio(aUfficioUtenteConnesso);

		// Verifica Fascicolo
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		lFasEstMod = lCtrl.ExRicercaFascicoloSigeByAnnoNumCodUfficio(lFasMod);

		if (lFasEstMod == null || lFasEstMod.getFascicoloSige() == null)
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Procedimento " + aRuoloFascicolo + " non esistente in archivio");

		if (lFasEstMod.getFascicoloSige().getCodStatoFascicolo().equals("05"))
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Procedimento " + aRuoloFascicolo + " gia unificato ");

		// Ticket#20191213011 — SIES : impossibile riunire du procedimenti (rilascio per 11.2.4)
		// sentita Nunzia aggiungo anche lo stato 20 (Decreto Fissazione Udienza)
		if (!lFasEstMod.getFascicoloSige().getCodStatoFascicolo().equals("02")
				&& !lFasEstMod.getFascicoloSige().getCodStatoFascicolo().equals("10")
				&& !lFasEstMod.getFascicoloSige().getCodStatoFascicolo().equals("13")
				&& !lFasEstMod.getFascicoloSige().getCodStatoFascicolo().equals("20"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Il Procedimento " + aRuoloFascicolo
					+ " presenta una stato incompatibile per l'unificazione :  Unificazione Impossibile");

		return lFasEstMod;
	}

	/**
	 * Esecuzione stampa Decreto di Unificazione
	 * <p>
	 *
	 * @param aEvento
	 * @param lUfficio
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDecretoUnificazioneSige(EventoModel lEvento, BigDecimal aIdFascicolo,
			BigDecimal aIdFascicoloUnificante, String lTipoUfficio, UtenteModel aUtenteModel)
			throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
		// Riempie l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO, ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO,
				ICostantiStampaSige.TREE_FASCICOLOSIGEUNIFICANTE, ICostantiStampaSige.TREE_PROVVEDIMENTO,
				ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
				ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
				ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
				ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA };
		int aTipoStampa = ICostantiStampaSige.STAMPA_DECRETO_UNIFICAZIONE;

		TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(lEvento.getIdEvento(), aIdFascicolo,
				aIdFascicoloUnificante, aTipoDati, aTipoStampa, lTipoUfficio);

		// ReportGenerator lReport = new ReportGenerator();
		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
		String lIdTemplate = lEvento.getTemIdTemplate();
		String lNomeTemplate = "";
		if (lIdTemplate == null || lIdTemplate.length() == 0)
			lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_DE_008");
		else
			lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>>> Generato il Documento .");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("EVENTO >>> " + lEvento.toString());
		// Si imposta il ByteArrayInput ovverro il doc generato nell'evento
		// precisamente nel attributo DocBlobIn.
		lEvento.setDocBlobIn(lByteArrayInput);

		// Inserisce il documento generato nel model di ritorno
		// In esso inserisce il Nome del template di ritorno
		// e il documento generato.
		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Prepara un EventoDAO
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(lEvento);

			// Seleziona le condizioni di Update
			lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException("ProvvedimentoSigeController.ExStampaProvvedimento: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + ex);
			throw new F3BException("ProvvedimentoSigeController.ExStampaProvvedimento: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Inserimento del Decreto di Unificazione : è stata già Verificata la Unificabilità dei due Procedimenti
	 * Individuati.; Vengono replicati i Tenori del FasDaUnif in FasUnificante; Il FasDaUnif viene aggiornato
	 * come Unificato; Si effettua l'inserimento dell'EVENTO relativo.
	 * <p>
	 *
	 * @param aAnnoDaUnif
	 * @param aNumeroDaUnif
	 * @param aAnnoUnificante
	 * @param aNumeroUnificante
	 * @param aUtenteConnesso
	 * @param aUfficioUtenteConnesso
	 * @param aLuogoUfficioUtenteConnesso
	 * @param aDataUnificazione
	 * @return EventoModel
	 * @throws F3BException
	 */

	public ProvvedimentoSigeModel ExInserisciDecretoUnificazioneSige(String aAnnoDaUnif, String aNumeroDaUnif,
			String aAnnoUnificante, String aNumeroUnificante, String aUfficioUtenteConnesso,
			String aUtenteConnesso, String aLuogoUfficioUtenteConnesso, Date aDataUnificazione)
			throws F3BException {

		Connection lConn = null;

		FascicoloSigeDAO lFasDao = null;
		TenoreSigeDAO lTenDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDao = null;
		EventoDAO lEveDao = null;
		ProvvedimentoSigeDAO lProvvDao = null;

		FascicoloSigeEstesoModel lFasUnificante = new FascicoloSigeEstesoModel();
		FascicoloSigeEstesoModel lFasDaUnif = new FascicoloSigeEstesoModel();
		EventoModel lEveMod = new EventoModel();
		ProvvedimentoSigeModel lProvvMod = new ProvvedimentoSigeModel();

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSigeDAO(lConn);
			lTenDao = new TenoreSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lProvvDao = new ProvvedimentoSigeDAO(lConn);
			lTenSenReaDao = new TenoreSentenzaReatoDAO(lConn);

			// Lettura del PROCEDIMENTO Unificante.
			IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();

			FascicoloSigeModel lFasModUnificante = new FascicoloSigeModel();
			lFasModUnificante.setChiaveAnno(new BigDecimal(aAnnoUnificante));
			lFasModUnificante.setChiaveProgr(new BigDecimal(aNumeroUnificante));
			lFasModUnificante.setChiaveUfficio(aUfficioUtenteConnesso);
			lFasUnificante = lCtrl.ExRicercaFascicoloSigeByAnnoNumCodUfficio(lFasModUnificante);

			// Lettura del PROCEDIMENTO da Unificare.
			FascicoloSigeModel lFasModDaUnificare = new FascicoloSigeModel();
			lFasModDaUnificare.setChiaveAnno(new BigDecimal(aAnnoDaUnif));
			lFasModDaUnificare.setChiaveProgr(new BigDecimal(aNumeroDaUnif));
			lFasModDaUnificare.setChiaveUfficio(aUfficioUtenteConnesso);
			lFasDaUnif = lCtrl.ExRicercaFascicoloSigeByAnnoNumCodUfficio(lFasModDaUnificare);

			// Aggiornamento del FASCICOLO SIGE del PROCEDIMENTO da Unificare.
			lFasDao.setDAOFromModel(lFasDaUnif.getFascicoloSige());
			lFasDao.setCodStatoFascicolo("05");
			lFasDao.setFasSigIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
			lFasDao.setDataDefinizione(aDataUnificazione);
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasDaUnif.getFascicoloSige().getIdFascicoloSige());
			lFasDao.update();
			lFasDao.stop();

			// STUB 29/04/2004 Aggiornamento del FASCICOLO SIGE Unificante.
			lFasDao.setDAOFromModel(lFasUnificante.getFascicoloSige());
			if (lFasUnificante.getFascicoloSige().getNumeroFascicoliUnificati() != null)
				lFasDao.setNumeroFascicoliUnificati(lFasUnificante.getFascicoloSige()
						.getNumeroFascicoliUnificati().add(new BigDecimal(1)));
			else
				lFasDao.setNumeroFascicoliUnificati(new BigDecimal(1));
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
			lFasDao.update();
			lFasDao.stop();

			// Inserimento dell'EVENTO.
			lEveMod.setCodTipoEvento("01");
			lEveMod.setCodTipoProvvedimento("02");
			lEveMod.setDataEmissione(aDataUnificazione);
			lEveMod.setCodMotivo("0600");
			// lEveMod.setFasSigIdFascicoloSige(lFasDaUnif.getFascicoloSige().getIdFascicoloSige());
			// 14/10/2011 L'evento di Unificazione SIGE non va collegato al Fascicolo SIEP
			// if (lFasDaUnif.getFascicoloSiep() != null)
			// lEveMod.setFasSieIdFascicoloSiep(lFasDaUnif.getFascicoloSiep().getFasSieIdFascicoloSiep());
			lEveMod.setCodUfficioEmittente(aUfficioUtenteConnesso);
			lEveMod.setDataInserimento(DateUtils.getSysDate());
			lEveMod.setCodUfficioInserimento(aUfficioUtenteConnesso);
			lEveMod.setCodOperatoreInserimento(aUtenteConnesso);

			lEveMod.setCodLuogoEmittente(aLuogoUfficioUtenteConnesso);
			lEveMod.setCodEsito("0600");
			lEveMod.setCodLuogoDestinatario("-");
			lEveMod.setCodTipoUfficioDestinatario("-");

			lEveDao.setDAOFromModel(lEveMod);
			lEveMod.setIdEvento(lEveDao.insert());

			// Inserimento del Provvedimento Sige, legato all'Evento appena generato
			lProvvMod.setCodTipoProvvedimento("02");
			lProvvMod.setCodTipoProvvedimentoSige("55");
			lProvvMod.setDefinitorio("N");
			lProvvMod.setDataEmissione(aDataUnificazione);
			lProvvMod.setFasIdFascicoloSige(lFasDaUnif.getFascicoloSige().getIdFascicoloSige());
			lProvvMod.setIdEventoGenerato(lEveMod.getIdEvento());
			lProvvMod.setDataInserimento(DateUtils.getSysDate());
			lProvvMod.setCodUfficioInserimento(aUfficioUtenteConnesso);
			lProvvMod.setCodOperatoreInserimento(aUtenteConnesso);
			lProvvMod.setChiaveUfficio(aUfficioUtenteConnesso);

			lProvvDao.setDAOFromModel(lProvvMod);
			BigDecimal idDecUnif = lProvvDao.insert();
			lProvvMod.setIdProvvedimentoSige(idDecUnif);

			// Accodamento dei TENORE da Unificare in lFasUnificante.

			TenoreSigeModel lTenModUnificato = new TenoreSigeModel();
			TenoreSigeModel lTenModUnificante = new TenoreSigeModel();
			lTenModUnificato.setFasIdFascicoloSige(lFasDaUnif.getFascicoloSige().getIdFascicoloSige());
			lTenModUnificante.setFasIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());

			ITenoreSige lCtrlTS = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenoriUnificato = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificato);
			Vector lTenoriUnificante = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificante);

			Iterator itxTenoriUnificante = lTenoriUnificante.iterator();
			String lCodOggettiPresenti = "";
			while (itxTenoriUnificante.hasNext()) {
				TenoreSigeModel lTenoreFasUnificante = (TenoreSigeModel) itxTenoriUnificante.next();
				lCodOggettiPresenti += lTenoreFasUnificante.getCodOggettoSige() + ";";
			}

			Iterator itxTenoriUnificato = lTenoriUnificato.iterator();
			// int j = 0;

			while (itxTenoriUnificato.hasNext()) {
				// j++;

				TenoreSigeModel lTenModDaUnificare = (TenoreSigeModel) itxTenoriUnificato.next();
				TenoreSigeModel lTenModDaUnificareInFascUnificato = new TenoreSigeModel(lTenModDaUnificare);

				if (lCodOggettiPresenti.indexOf(lTenModDaUnificare.getCodOggettoSige()) < 0) {
					// lTenModDaUnificare.setProgrTenore(new BigDecimal ((double)(lTenoriUnificante+j+1)));
					lTenModDaUnificare.setNote("UNIFICATO");
					lTenModDaUnificare
							.setFasIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
					lTenModDaUnificare.setRicSigIdRichiestaSige(
							lFasUnificante.getFascicoloSige().getRicIdRichiestaSige());
					lTenModDaUnificare.setProvIdProvvedimentoSige(idDecUnif);

					// Setto il DAO dal Model ed inserisco il Tenore
					lTenDao.setDAOFromModel(lTenModDaUnificare);
					BigDecimal idTenUnificato = lTenDao.insert();

					// Inserisco il record anche in tenore_sentenza_reato
					// TenoreSentenzaReatoModel lTenSenReaMod = new TenoreSentenzaReatoModel();
					lTenModDaUnificare.setIdTenoreSige(idTenUnificato);
					lTenSenReaDao.setDAOFromTenoreModel(lTenModDaUnificare);
					lTenSenReaDao.insert();

					// Aggiorno il tenore anche per il fascicolo unificato, impostando l'esito
					// senza aggiungerlo in tenore_sentenza_reato
					lTenModDaUnificareInFascUnificato.setProvIdProvvedimentoSige(idDecUnif);
					lTenModDaUnificareInFascUnificato.setCodEsitoSige("0501"); // Unificato
					// lTenDao.setDAOFromModelForUpdateEsito(lTenModDaUnificareInFascUnificato);
					lTenDao.setDAOFromModel(lTenModDaUnificareInFascUnificato);
					lTenDao.selCondizioneDelete(lTenModDaUnificareInFascUnificato.getIdTenoreSige());
					lTenDao.update();
				}
			}

			// COMMIT
			commit(lConn);

		} catch (SIGEException se) {
			rollback(lConn);
			throw se;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException(
					"DecretoUnificazioneSigeController.exInserisciDecretoUnificazioneSige: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException(
					"DecretoUnificazioneSigeController.exInserisciDecretoUnificazioneSige: " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lTenDao);
			cleanup(lEveDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTenSenReaDao);
			cleanup(lProvvDao);
			cleanup(lConn);
		}

		return lProvvMod;
	}

	/**
	 * Esegue la cancellazione di un Decreto di unificazione in Sige.
	 * <p>
	 *
	 * @param aKeyProvvedimento
	 *            : chiave del record
	 * @throws F3BException
	 */
	public void ExCancellaDecretoUnificazioneSige(BigDecimal aKeyProvvedimento, String aUfficioUtenteConnesso,
			String aUtenteConnesso, String aLuogoUfficioUtenteConnesso) throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasSqlDao = null;
		FascicoloSigeDAO lFasDao = null;
		TenoreSigeDAO lTenDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;
		EventoDAO lEveDao = null;
		ProvvedimentoSigeDAO lProvvDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDao = null;

		// Preleva il ProvvedimentoModel da Cancellare
		IProvvedimentoSige lProvvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel aProvvMod = lProvvCtrl.ExRicercaProvvedimentoById(aKeyProvvedimento);
		if (aProvvMod == null)
			throw new F3BException("Attenzione! Decreto Unificazione non trovato!");

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeDAO(lConn);
			lTenDao = new TenoreSigeDAO(lConn);
			lTenSqlDao = new TenoreSigeSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lProvvDao = new ProvvedimentoSigeDAO(lConn);
			lTenSenReaDao = new TenoreSentenzaReatoDAO(lConn);

			// Lettura del Procedimento SIGE Unificato.
			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);
			lFasSqlDao.ricercaFascicoloSigeByKey(aProvvMod.getProvvedimento().getFasIdFascicoloSige());
			FascicoloSigeModel lFasEstUnificato = (FascicoloSigeModel) lFasSqlDao.getModelByKey();

			// Caricamento dei Tenori del Procedimento Unificato.
			TenoreSigeModel TenoreSigeUnificatoModel = new TenoreSigeModel();
			TenoreSigeUnificatoModel.setFasIdFascicoloSige(lFasEstUnificato.getIdFascicoloSige());
			lTenSqlDao.ricercaTenoriSigeAttivi(TenoreSigeUnificatoModel);

			Vector lVectTenoriUnificato = new Vector(lTenSqlDao.getModels());
			lTenSqlDao.stop();

			// Lettura del Procedimento SIGE Unificante
			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);
			lFasSqlDao.ricercaFascicoloSigeByKey(lFasEstUnificato.getFasSigIdFascicoloSige());
			FascicoloSigeModel lFasEstUnificante = (FascicoloSigeModel) lFasSqlDao.getModelByKey();

			// Caricamento dei Tenori del Procedimento Unificante
			TenoreSigeModel tenoreSigeUnificanteModel = new TenoreSigeModel();
			tenoreSigeUnificanteModel.setFasIdFascicoloSige(lFasEstUnificante.getIdFascicoloSige());
			lTenSqlDao.ricercaTenoriSigeAttivi(tenoreSigeUnificanteModel);

			Vector lVectTenoriUnificante = new Vector(lTenSqlDao.getModels());
			lTenSqlDao.stop();

			// Aggiornamento del FASCICOLO SIGE da DIS-Unificare.
			lFasDao.setCodStatoFascicolo("02");
			BigDecimal lBigDec = null;
			lFasDao.setFasSigIdFascicoloSige(lBigDec);
			Date lDate = null;
			lFasDao.setDataDefinizione(lDate);
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasEstUnificato.getIdFascicoloSige());
			lFasDao.update();
			lFasDao.stop();

			// Aggiornamento del FASCICOLO SIGE Unificante.
			if (lFasEstUnificante.getNumeroFascicoliUnificati().intValue() > 0)
				lFasDao.setNumeroFascicoliUnificati(
						lFasEstUnificante.getNumeroFascicoliUnificati().subtract(new BigDecimal(1)));
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasEstUnificante.getIdFascicoloSige());
			lFasDao.update();
			lFasDao.stop();

			//=====================================================================
			// Ticket#20210322016 - la eliminazione / aggiornamento dei tenori veniva 
			// effettuata confrontando il getCodOggettoSige indipendentemente se i tenori fosse o meno 
			// collegati al provvedimento di unificazione che si sta cancellando. 
			// Se presenti più tenori con lo stesso oggetto sull'unificato veniva sganciata dall'unificante
			// solo la prima occorrenza. Inoltre non è detto che fosse quella legata al provvedimento di 
			// unificazione
			//if (lVectTenoriUnificante != null) {
		    if (!lVectTenoriUnificante.isEmpty()) {
				Iterator itxUnificante = lVectTenoriUnificante.iterator();
				while (itxUnificante.hasNext()) {
					TenoreSigeModel lTenoreDiUnificante = (TenoreSigeModel) itxUnificante.next();
					if (lTenoreDiUnificante.getProvIdProvvedimentoSige()!=null
						&& lTenoreDiUnificante.getProvIdProvvedimentoSige().compareTo(aKeyProvvedimento)==0	
					   )
					{ 
						// Il tenore dell'unificante punta il provvedimento di unificazione dell'unificato, quindi è 
						// l'originale duplicato e lo elimina
						// Prima del Tenore occorre cancellare il TenoreSentenzaReato
						lTenSenReaDao.selCondizioneDelete(lTenoreDiUnificante.getIdTenoreSige());
						lTenSenReaDao.delete();

						// Setto il DAO dal Model e cancello il Tenore.
						lTenDao.selCondizioneDelete(lTenoreDiUnificante.getIdTenoreSige());
						lTenDao.delete();						
					}
				}
				
				//
				Iterator itxUnificato = lVectTenoriUnificato.iterator();
				while (itxUnificato.hasNext()) {
					TenoreSigeModel lTenoreDiUnificato = (TenoreSigeModel) itxUnificato.next();
					if (   lTenoreDiUnificato.getProvIdProvvedimentoSige()!=null
						&& lTenoreDiUnificato.getProvIdProvvedimentoSige().compareTo(aKeyProvvedimento)==0	
					   )
					{
						// Il tenore dell'unificato punta il provvedimento di unificazione. Devo sganciarlo
						// Il tenore del fascicolo dis-unificato deve cambiare stato
						lTenoreDiUnificato.setCodEsitoSige (null);           // azzero: vale 0501 = Unificato
						lTenoreDiUnificato.setProvIdProvvedimentoSige(null); // Sgancio dal provvedimento che devo cancellare
						lTenoreDiUnificato.setRicSigIdRichiestaSige (null);  // Sgancio dalla richiesta dell'unificante
						
						if (lTenoreDiUnificato.getNote() != null && "UNIFICATO".compareTo(lTenoreDiUnificato.getNote()) == 0)
							lTenoreDiUnificato.setNote(null);
						
						lTenoreDiUnificato.setCodOperatoreAggiornamento (aUtenteConnesso);
						lTenoreDiUnificato.setDataAggiornamento (DateUtils.getSysDate());
						
						lTenDao.setDAOFromModel (lTenoreDiUnificato);
						lTenDao.selCondizioneDelete (lTenoreDiUnificato.getIdTenoreSige());

						lTenDao.update();					
					}
				}				
			} else {
				// MERGE v10: cancellazione preventiva
				// Caricamento dei Tenori del Procedimento Unificante
				lTenSqlDao.ricercaTenoriByProvvedimento(aKeyProvvedimento);
				Vector listaTenori = new Vector(lTenSqlDao.getModels());
				lTenSqlDao.stop();
				Iterator iterator = listaTenori.iterator();
				while (iterator.hasNext()) {
					TenoreSigeModel temoreSigeModel = (TenoreSigeModel) iterator.next();
					// Prima del Tenore occorre cancellare il TenoreSentenzaReato
					lTenSenReaDao.selCondizioneDelete(temoreSigeModel.getIdTenoreSige());
					lTenSenReaDao.delete();
					// Setto il DAO dal Model e cancello il Tenore.
					lTenDao.selCondizioneDelete(temoreSigeModel.getIdTenoreSige());
					lTenDao.delete();
				}
			}			
		    // Ticket#20210322016 - fine nuovo codice
		    
			/* Ticket#20210322016 vecchio codice commentato
			// Scodamento dei TENORI da Fascicolo Unificante e riaccodamento a Fascicolo Unificato.
			if (!lVectTenoriUnificante.isEmpty()) {
				Iterator itxUnificante = lVectTenoriUnificante.iterator();
				while (itxUnificante.hasNext()) {
					TenoreSigeModel lTenoreDiUnificante = (TenoreSigeModel) itxUnificante.next();
					Iterator itxUnificato = lVectTenoriUnificato.iterator();
					while (itxUnificato.hasNext()) {
						TenoreSigeModel lTenoreDiUnificato = (TenoreSigeModel) itxUnificato.next();
						if (lTenoreDiUnificante.getCodOggettoSige()
								.equals(lTenoreDiUnificato.getCodOggettoSige()) &&
						// lTenoreDiUnificante.getCodDettaglioOggetto().equals(lTenoreDiUnificato.getCodDettaglioOggetto())
						// &&
								lTenoreDiUnificante.getNote() != null
								&& "UNIFICATO".compareTo(lTenoreDiUnificante.getNote()) == 0) {
							// Prima del Tenore occorre cancellare il TenoreSentenzaReato
							lTenSenReaDao.selCondizioneDelete(lTenoreDiUnificante.getIdTenoreSige());
							lTenSenReaDao.delete();

							// Setto il DAO dal Model e cancello il Tenore.
							lTenDao.selCondizioneDelete(lTenoreDiUnificante.getIdTenoreSige());
							lTenDao.delete();

							// Il tenore del fascicolo dis-unificato deve cambiare stato
							lTenoreDiUnificato.setCodEsitoSige(null);
							lTenoreDiUnificato.setProvIdProvvedimentoSige(null);
							if (lTenoreDiUnificato.getNote() != null
									&& "UNIFICATO".compareTo(lTenoreDiUnificato.getNote()) == 0)
								lTenoreDiUnificato.setNote(null);
							lTenoreDiUnificato.setCodOperatoreAggiornamento(aUtenteConnesso);
							lTenoreDiUnificato.setDataAggiornamento(DateUtils.getSysDate());
							// lTenDao.setDAOFromModelForUpdateEsito(lTenoreDiUnificato);
							lTenDao.setDAOFromModel(lTenoreDiUnificato);
							lTenDao.selCondizioneDelete(lTenoreDiUnificato.getIdTenoreSige());

							lTenDao.update();

							break;
						}
					}
				}
			} else {
				// MERGE v10: cancellazione preventiva
				// Caricamento dei Tenori del Procedimento Unificante
				lTenSqlDao.ricercaTenoriByProvvedimento(aKeyProvvedimento);
				Vector listaTenori = new Vector(lTenSqlDao.getModels());
				lTenSqlDao.stop();
				Iterator iterator = listaTenori.iterator();
				while (iterator.hasNext()) {
					TenoreSigeModel temoreSigeModel = (TenoreSigeModel) iterator.next();
					// Prima del Tenore occorre cancellare il TenoreSentenzaReato
					lTenSenReaDao.selCondizioneDelete(temoreSigeModel.getIdTenoreSige());
					lTenSenReaDao.delete();
					// Setto il DAO dal Model e cancello il Tenore.
					lTenDao.selCondizioneDelete(temoreSigeModel.getIdTenoreSige());
					lTenDao.delete();
				}
			}
			Ticket#20210322016 - FINE codice commentato
*/
			// cancellazione Provvedimento (ed Evento) collegato.
			// lProvvDao.selCondizioneUpdate(aKeyProvvedimento);
			lProvvDao.selCondizioneByKey(aKeyProvvedimento);
			lProvvDao.delete();

			if (aProvvMod.getProvvedimento().getIdEventoGenerato() != null) {
				lEveDao.selCondizioneUpdate(aProvvMod.getProvvedimento().getIdEventoGenerato());
				lEveDao.delete();
			}

			// COMMIT
			commit(lConn);
		} catch (F3BException Fe) {
			rollback(lConn);
			throw Fe;
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException(
					"DecretoUnificazioneSigeController.ExCancellaDecretoUnificazioneSige: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new SIGEException(
					"DecretoUnificazioneSigeController.ExCancellaDecretoUnificazioneSige: " + e);
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lFasDao);
			cleanup(lTenDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTenSqlDao);
			cleanup(lEveDao);
			cleanup(lProvvDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
	}

	/**
	 * Inserimento del Verbale di Unificazione : è stata già Verificata la Unificabilità dei due Procedimenti
	 * Individuati.; Vengono replicati i Tenori del FasDaUnif in FasUnificante; Il FasDaUnif viene aggiornato
	 * come Unificato; Si effettua l'inserimento dell'EVENTO relativo.
	 * <p>
	 *
	 * @param aAnnoDaUnif
	 * @param aNumeroDaUnif
	 * @param aAnnoUnificante
	 * @param aNumeroUnificante
	 * @param aUtenteConnesso
	 * @param aUfficioUtenteConnesso
	 * @param aLuogoUfficioUtenteConnesso
	 * @param aDataUnificazione
	 * @return EventoModel
	 * @throws F3BException
	 */

	public ProvvedimentoSigeModel ExInserisciVerbaleUnificazioneSige(String aAnnoDaUnif, String aNumeroDaUnif,
			String aAnnoUnificante, String aNumeroUnificante, String aUfficioUtenteConnesso,
			String aUtenteConnesso, String aLuogoUfficioUtenteConnesso, Date aDataUnificazione)
			throws F3BException {

		Connection lConn = null;

		FascicoloSigeDAO lFasDao = null;
		TenoreSigeDAO lTenDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDao = null;
		EventoDAO lEveDao = null;
		ProvvedimentoSigeDAO lProvvDao = null;

		FascicoloSigeEstesoModel lFasUnificante = new FascicoloSigeEstesoModel();
		FascicoloSigeEstesoModel lFasDaUnif = new FascicoloSigeEstesoModel();
		EventoModel lEveMod = new EventoModel();
		ProvvedimentoSigeModel lProvvMod = new ProvvedimentoSigeModel();

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSigeDAO(lConn);
			lTenDao = new TenoreSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lProvvDao = new ProvvedimentoSigeDAO(lConn);
			lTenSenReaDao = new TenoreSentenzaReatoDAO(lConn);

			// Lettura del PROCEDIMENTO Unificante.
			IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();

			FascicoloSigeModel lFasModUnificante = new FascicoloSigeModel();
			lFasModUnificante.setChiaveAnno(new BigDecimal(aAnnoUnificante));
			lFasModUnificante.setChiaveProgr(new BigDecimal(aNumeroUnificante));
			lFasModUnificante.setChiaveUfficio(aUfficioUtenteConnesso);
			lFasUnificante = lCtrl.ExRicercaFascicoloSigeByAnnoNumCodUfficio(lFasModUnificante);

			// Lettura del PROCEDIMENTO da Unificare.
			FascicoloSigeModel lFasModDaUnificare = new FascicoloSigeModel();
			lFasModDaUnificare.setChiaveAnno(new BigDecimal(aAnnoDaUnif));
			lFasModDaUnificare.setChiaveProgr(new BigDecimal(aNumeroDaUnif));
			lFasModDaUnificare.setChiaveUfficio(aUfficioUtenteConnesso);
			lFasDaUnif = lCtrl.ExRicercaFascicoloSigeByAnnoNumCodUfficio(lFasModDaUnificare);

			// Aggiornamento del FASCICOLO SIGE del PROCEDIMENTO da Unificare.
			lFasDao.setDAOFromModel(lFasDaUnif.getFascicoloSige());
			lFasDao.setCodStatoFascicolo("05");
			lFasDao.setFasSigIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
			lFasDao.setDataDefinizione(aDataUnificazione);
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasDaUnif.getFascicoloSige().getIdFascicoloSige());
			lFasDao.update();
			lFasDao.stop();

			// STUB 29/04/2004 Aggiornamento del FASCICOLO SIGE Unificante.
			lFasDao.setDAOFromModel(lFasUnificante.getFascicoloSige());
			if (lFasUnificante.getFascicoloSige().getNumeroFascicoliUnificati() != null)
				lFasDao.setNumeroFascicoliUnificati(lFasUnificante.getFascicoloSige()
						.getNumeroFascicoliUnificati().add(new BigDecimal(1)));
			else
				lFasDao.setNumeroFascicoliUnificati(new BigDecimal(1));
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
			lFasDao.update();
			lFasDao.stop();

			// Inserimento dell'EVENTO.
			lEveMod.setCodTipoEvento("01");
			lEveMod.setCodTipoProvvedimento("14"); // Verbale di Unificazione
			lEveMod.setDataEmissione(aDataUnificazione);
			lEveMod.setCodMotivo("0600");
			// lEveMod.setFasSigIdFascicoloSige(lFasDaUnif.getFascicoloSige().getIdFascicoloSige());
			// 14/10/2011 L'evento di Unificazione SIGE non va collegato al Fascicolo SIEP
			// if (lFasDaUnif.getFascicoloSiep() != null)
			// lEveMod.setFasSieIdFascicoloSiep(lFasDaUnif.getFascicoloSiep().getFasSieIdFascicoloSiep());
			lEveMod.setCodUfficioEmittente(aUfficioUtenteConnesso);
			lEveMod.setDataInserimento(DateUtils.getSysDate());
			lEveMod.setCodUfficioInserimento(aUfficioUtenteConnesso);
			lEveMod.setCodOperatoreInserimento(aUtenteConnesso);

			lEveMod.setCodLuogoEmittente(aLuogoUfficioUtenteConnesso);
			lEveMod.setCodEsito("0600");
			lEveMod.setCodLuogoDestinatario("-");
			lEveMod.setCodTipoUfficioDestinatario("-");

			lEveDao.setDAOFromModel(lEveMod);
			lEveMod.setIdEvento(lEveDao.insert());

			// Inserimento del Provvedimento Sige, legato all'Evento appena generato
			lProvvMod.setCodTipoProvvedimento("02");
			lProvvMod.setCodTipoProvvedimentoSige("56"); // Verbale di Unificazione
			lProvvMod.setDefinitorio("N");
			lProvvMod.setDataEmissione(aDataUnificazione);
			lProvvMod.setFasIdFascicoloSige(lFasDaUnif.getFascicoloSige().getIdFascicoloSige());
			lProvvMod.setIdEventoGenerato(lEveMod.getIdEvento());
			lProvvMod.setDataInserimento(DateUtils.getSysDate());
			lProvvMod.setCodUfficioInserimento(aUfficioUtenteConnesso);
			lProvvMod.setCodOperatoreInserimento(aUtenteConnesso);
			lProvvMod.setChiaveUfficio(aUfficioUtenteConnesso);

			lProvvDao.setDAOFromModel(lProvvMod);
			BigDecimal idDecUnif = lProvvDao.insert();
			lProvvMod.setIdProvvedimentoSige(idDecUnif);

			// Accodamento dei TENORE da Unificare in lFasUnificante.

			TenoreSigeModel lTenModUnificato = new TenoreSigeModel();
			TenoreSigeModel lTenModUnificante = new TenoreSigeModel();
			lTenModUnificato.setFasIdFascicoloSige(lFasDaUnif.getFascicoloSige().getIdFascicoloSige());
			lTenModUnificante.setFasIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());

			ITenoreSige lCtrlTS = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenoriUnificato = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificato);
			Vector lTenoriUnificante = lCtrlTS.ExRicercaTenoriAttivi(lTenModUnificante);

			Iterator itxTenoriUnificante = lTenoriUnificante.iterator();
			String lCodOggettiPresenti = "";
			while (itxTenoriUnificante.hasNext()) {
				TenoreSigeModel lTenoreFasUnificante = (TenoreSigeModel) itxTenoriUnificante.next();
				lCodOggettiPresenti += lTenoreFasUnificante.getCodOggettoSige() + ";";
			}

			Iterator itxTenoriUnificato = lTenoriUnificato.iterator();
			// int j = 0;

			while (itxTenoriUnificato.hasNext()) {
				// j++;

				TenoreSigeModel lTenModDaUnificare = (TenoreSigeModel) itxTenoriUnificato.next();
				TenoreSigeModel lTenModDaUnificareInFascUnificato = new TenoreSigeModel(lTenModDaUnificare);

				if (lCodOggettiPresenti.indexOf(lTenModDaUnificare.getCodOggettoSige()) < 0) {
					// lTenModDaUnificare.setProgrTenore(new BigDecimal ((double)(lTenoriUnificante+j+1)));
					lTenModDaUnificare.setNote("UNIFICATO");
					lTenModDaUnificare
							.setFasIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
					lTenModDaUnificare.setRicSigIdRichiestaSige(
							lFasUnificante.getFascicoloSige().getRicIdRichiestaSige());
					lTenModDaUnificare.setProvIdProvvedimentoSige(idDecUnif);

					// Setto il DAO dal Model ed inserisco il Tenore
					lTenDao.setDAOFromModel(lTenModDaUnificare);
					BigDecimal idTenUnificato = lTenDao.insert();

					// Inserisco il record anche in tenore_sentenza_reato
					// TenoreSentenzaReatoModel lTenSenReaMod = new TenoreSentenzaReatoModel();
					lTenModDaUnificare.setIdTenoreSige(idTenUnificato);
					lTenSenReaDao.setDAOFromTenoreModel(lTenModDaUnificare);
					lTenSenReaDao.insert();

					// Aggiorno il tenore anche per il fascicolo unificato, impostando l'esito
					// senza aggiungerlo in tenore_sentenza_reato
					lTenModDaUnificareInFascUnificato.setProvIdProvvedimentoSige(idDecUnif);
					lTenModDaUnificareInFascUnificato.setCodEsitoSige("0501"); // Unificato
					// lTenDao.setDAOFromModelForUpdateEsito(lTenModDaUnificareInFascUnificato);
					lTenDao.setDAOFromModel(lTenModDaUnificareInFascUnificato);
					lTenDao.selCondizioneDelete(lTenModDaUnificareInFascUnificato.getIdTenoreSige());
					lTenDao.update();

				}
			}

			// COMMIT
			commit(lConn);

		} catch (SIGEException se) {
			rollback(lConn);
			throw se;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException(
					"DecretoUnificazioneSigeController.ExInserisciVerbaleUnificazioneSige: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException(
					"DecretoUnificazioneSigeController.ExInserisciVerbaleUnificazioneSige: " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lTenDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTenSenReaDao);
			cleanup(lEveDao);
			cleanup(lProvvDao);

			cleanup(lConn);
		}

		return lProvvMod;
	}

	/**
	 * Esegue la cancellazione di un Verbale di unificazione in Sige.
	 * <p>
	 *
	 * @param aKeyProvvedimento
	 *            : chiave del record
	 * @throws F3BException
	 */
	public void ExCancellaVerbaleUnificazioneSige(BigDecimal aKeyProvvedimento, String aUfficioUtenteConnesso,
			String aUtenteConnesso, String aLuogoUfficioUtenteConnesso) throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasSqlDao = null;
		FascicoloSigeDAO lFasDao = null;
		TenoreSigeDAO lTenDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;
		EventoDAO lEveDao = null;
		ProvvedimentoSigeDAO lProvvDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDao = null;

		// Preleva il ProvvedimentoModel da Cancellare
		IProvvedimentoSige lProvvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel aProvvMod = lProvvCtrl.ExRicercaProvvedimentoById(aKeyProvvedimento);
		if (aProvvMod == null)
			throw new F3BException("Attenzione! Verbale Unificazione non trovato!");

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeDAO(lConn);
			lTenDao = new TenoreSigeDAO(lConn);
			lTenSqlDao = new TenoreSigeSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lProvvDao = new ProvvedimentoSigeDAO(lConn);
			lTenSenReaDao = new TenoreSentenzaReatoDAO(lConn);

			// Lettura del Procedimento SIGE Unificato.
			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);
			lFasSqlDao.ricercaFascicoloSigeByKey(aProvvMod.getProvvedimento().getFasIdFascicoloSige());
			FascicoloSigeModel lFasEstUnificato = (FascicoloSigeModel) lFasSqlDao.getModelByKey();

			// Caricamento dei Tenori del Procedimento Unificato.
			TenoreSigeModel TenoreSigeUnificatoModel = new TenoreSigeModel();
			TenoreSigeUnificatoModel.setFasIdFascicoloSige(lFasEstUnificato.getIdFascicoloSige());
			lTenSqlDao.ricercaTenoriSigeAttivi(TenoreSigeUnificatoModel);

			Vector lVectTenoriUnificato = new Vector(lTenSqlDao.getModels());
			lTenSqlDao.stop();

			// Lettura del Procedimento SIGE Unificante
			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);
			lFasSqlDao.ricercaFascicoloSigeByKey(lFasEstUnificato.getFasSigIdFascicoloSige());
			FascicoloSigeModel lFasEstUnificante = (FascicoloSigeModel) lFasSqlDao.getModelByKey();

			// Caricamento dei Tenori del Procedimento Unificante
			TenoreSigeModel TenoreSigeUnificanteModel = new TenoreSigeModel();
			TenoreSigeUnificanteModel.setFasIdFascicoloSige(lFasEstUnificante.getIdFascicoloSige());
			lTenSqlDao.ricercaTenoriSigeAttivi(TenoreSigeUnificanteModel);

			Vector lVectTenoriUnificante = new Vector(lTenSqlDao.getModels());
			lTenSqlDao.stop();

			// Aggiornamento del FASCICOLO SIGE da DIS-Unificare.
			lFasDao.setCodStatoFascicolo("02");
			BigDecimal lBigDec = null;
			lFasDao.setFasSigIdFascicoloSige(lBigDec);
			Date lDate = null;
			lFasDao.setDataDefinizione(lDate);
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasEstUnificato.getIdFascicoloSige());
			lFasDao.update();
			lFasDao.stop();

			// Aggiornamento del FASCICOLO SIGE Unificante.
			if (lFasEstUnificante.getNumeroFascicoliUnificati().intValue() > 0)
				lFasDao.setNumeroFascicoliUnificati(
						lFasEstUnificante.getNumeroFascicoliUnificati().subtract(new BigDecimal(1)));
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasEstUnificante.getIdFascicoloSige());
			lFasDao.update();
			lFasDao.stop();

			//=====================================================================
			// Ticket#20210322016 - la eliminazione / aggiornamento dei tenori veniva 
			// effettuata confrontando il getCodOggettoSige indipendentemente se i tenori fosse o meno 
			// collegati al provvedimento di unificazione che si sta cancellando. 
			// Se presenti più tenori con lo stesso oggetto sull'unificato veniva sganciata dall'unificante
			// solo la prima occorrenza. Inoltre non è detto che fosse quella legata al provvedimento di 
			// unificazione
			if (lVectTenoriUnificante != null) {
				Iterator itxUnificante = lVectTenoriUnificante.iterator();
				while (itxUnificante.hasNext()) {
					TenoreSigeModel lTenoreDiUnificante = (TenoreSigeModel) itxUnificante.next();
					if (lTenoreDiUnificante.getProvIdProvvedimentoSige()!=null
						&& lTenoreDiUnificante.getProvIdProvvedimentoSige().compareTo(aKeyProvvedimento)==0	
					   )
					{ 
						// Il tenore dell'unificante punta il provvedimento di unificazione dell'unificato, quindi è 
						// l'originale duplicato e lo elimina
						// Prima del Tenore occorre cancellare il TenoreSentenzaReato
						lTenSenReaDao.selCondizioneDelete(lTenoreDiUnificante.getIdTenoreSige());
						lTenSenReaDao.delete();

						// Setto il DAO dal Model e cancello il Tenore.
						lTenDao.selCondizioneDelete(lTenoreDiUnificante.getIdTenoreSige());
						lTenDao.delete();						
					}
				}
			}
			
			//
			Iterator itxUnificato = lVectTenoriUnificato.iterator();
			while (itxUnificato.hasNext()) {
				TenoreSigeModel lTenoreDiUnificato = (TenoreSigeModel) itxUnificato.next();
				if (   lTenoreDiUnificato.getProvIdProvvedimentoSige()!=null
					&& lTenoreDiUnificato.getProvIdProvvedimentoSige().compareTo(aKeyProvvedimento)==0	
				   )
				{
					// Il tenore dell'unificato punta il provvedimento di unificazione. Devo sganciarlo
					// Il tenore del fascicolo dis-unificato deve cambiare stato
					lTenoreDiUnificato.setCodEsitoSige (null);           // azzero: vale 0501 = Unificato
					lTenoreDiUnificato.setProvIdProvvedimentoSige(null); // Sgancio dal provvedimento che devo cancellare
					lTenoreDiUnificato.setRicSigIdRichiestaSige (null);  // Sgancio dalla richiesta dell'unificante
					
					if (lTenoreDiUnificato.getNote() != null && "UNIFICATO".compareTo(lTenoreDiUnificato.getNote()) == 0)
						lTenoreDiUnificato.setNote(null);
					
					lTenoreDiUnificato.setCodOperatoreAggiornamento (aUtenteConnesso);
					lTenoreDiUnificato.setDataAggiornamento (DateUtils.getSysDate());
					
					lTenDao.setDAOFromModel (lTenoreDiUnificato);
					lTenDao.selCondizioneDelete (lTenoreDiUnificato.getIdTenoreSige());

					lTenDao.update();					
				}
			}
			
			/* Ticket#20210322016 vecchio codice commentato
			// Scodamento dei TENORI da Fascicolo Unificante e riaccodamento a Fascicolo Unificato.
			if (lVectTenoriUnificante != null) {
				Iterator itxUnificante = lVectTenoriUnificante.iterator();
				while (itxUnificante.hasNext()) {
					TenoreSigeModel lTenoreDiUnificante = (TenoreSigeModel) itxUnificante.next();
					Iterator itxUnificato = lVectTenoriUnificato.iterator();
					while (itxUnificato.hasNext()) {
						TenoreSigeModel lTenoreDiUnificato = (TenoreSigeModel) itxUnificato.next();
						if (lTenoreDiUnificante.getCodOggettoSige()
								.equals(lTenoreDiUnificato.getCodOggettoSige()) &&
						// lTenoreDiUnificante.getCodDettaglioOggetto().equals(lTenoreDiUnificato.getCodDettaglioOggetto())
						// &&
								lTenoreDiUnificante.getNote() != null
								&& "UNIFICATO".compareTo(lTenoreDiUnificante.getNote()) == 0) {
							// Prima del Tenore occorre cancellare il TenoreSentenzaReato
							lTenSenReaDao.selCondizioneDelete(lTenoreDiUnificante.getIdTenoreSige());
							lTenSenReaDao.delete();

							// Setto il DAO dal Model e cancello il Tenore.
							lTenDao.selCondizioneDelete(lTenoreDiUnificante.getIdTenoreSige());
							lTenDao.delete();

							// Il tenore del fascicolo dis-unificato deve cambiare stato
							lTenoreDiUnificato.setCodEsitoSige(null);
							lTenoreDiUnificato.setProvIdProvvedimentoSige(null);
							
							if (lTenoreDiUnificato.getNote() != null
									&& "UNIFICATO".compareTo(lTenoreDiUnificato.getNote()) == 0)
								lTenoreDiUnificato.setNote(null);
							lTenoreDiUnificato.setCodOperatoreAggiornamento(aUtenteConnesso);
							lTenoreDiUnificato.setDataAggiornamento(DateUtils.getSysDate());
							
							// lTenDao.setDAOFromModelForUpdateEsito(lTenoreDiUnificato);
							lTenDao.setDAOFromModel(lTenoreDiUnificato);
							lTenDao.selCondizioneDelete(lTenoreDiUnificato.getIdTenoreSige());

							lTenDao.update();

							break;
						}
					}
				}
			}
			*/ //Ticket#20210322016 fine vecchio codice commentato
			
			// cancellazione Provvedimento (ed Evento) collegato.
			// lProvvDao.selCondizioneUpdate(aKeyProvvedimento);
			lProvvDao.selCondizioneByKey(aKeyProvvedimento);
			lProvvDao.delete();

			if (aProvvMod.getProvvedimento().getIdEventoGenerato() != null) {
				lEveDao.selCondizioneUpdate(aProvvMod.getProvvedimento().getIdEventoGenerato());
				lEveDao.delete();
			}

			// COMMIT
			commit(lConn);
		} catch (F3BException Fe) {
			rollback(lConn);
			throw Fe;
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException(
					"DecretoUnificazioneSigeController.ExCancellaVerbaleUnificazioneSige: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new SIGEException(
					"DecretoUnificazioneSigeController.ExCancellaVerbaleUnificazioneSige: " + e);
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lFasDao);
			cleanup(lTenDao);
			cleanup(lTenSqlDao);
			cleanup(lEveDao);
			cleanup(lProvvDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTenSenReaDao);

			cleanup(lConn);
		}
	}

	/*
	 * ISSUE MEV : Esecuzione stampa Verbale di Unificazione Numero MEV : 15_S4 Autore : sessa Data :
	 * 26/gen/2016 Branch : MEV_15_S4
	 */
	/**
	 * Esecuzione stampa Verbale di Unificazione
	 * <p>
	 *
	 * @param aEvento
	 * @param lUfficio
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaVerbaleUnificazioneSige(EventoModel lEvento, BigDecimal aIdFascicolo,
			BigDecimal aIdFascicoloUnificante, String lTipoUfficio, UtenteModel aUtenteModel)
			throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
		// Riempie l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO, ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO,
				ICostantiStampaSige.TREE_FASCICOLOSIGEUNIFICANTE, ICostantiStampaSige.TREE_PROVVEDIMENTO,
				ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
				ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
				ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
				ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA };
		int aTipoStampa = ICostantiStampaSige.STAMPA_VERBALE_UNIFICAZIONE;

		TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(lEvento.getIdEvento(), aIdFascicolo,
				aIdFascicoloUnificante, aTipoDati, aTipoStampa, lTipoUfficio);

		// ReportGenerator lReport = new ReportGenerator();
		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
		String lIdTemplate = lEvento.getTemIdTemplate();
		String lNomeTemplate = "";
		if (lIdTemplate == null || lIdTemplate.length() == 0)
			lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_DE_009");
		else
			lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>>> Generato il Documento .");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("EVENTO >>> " + lEvento.toString());
		// Si imposta il ByteArrayInput ovverro il doc generato nell'evento
		// precisamente nel attributo DocBlobIn.
		lEvento.setDocBlobIn(lByteArrayInput);

		// Inserisce il documento generato nel model di ritorno
		// In esso inserisce il Nome del template di ritorno
		// e il documento generato.

		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Prepara un EventoDAO
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(lEvento);

			// Seleziona le condizioni di Update
			lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException("ProvvedimentoSigeController.ExStampaProvvedimento: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + ex);
			throw new F3BException("ProvvedimentoSigeController.ExStampaProvvedimento: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}
	// ***** FINE INTERVENTO MEV_15_S4 *****//

}
package siap.sige.udienzaprocedimento.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
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
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.XModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.report.ReportGenerator;
import siap.siep.notifica.dao.NotificaDAO;
import siap.sige.SIGEException;
import siap.sige.fascicolo.dao.FascicoloSigeDAO;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.model.UdienzaSigeModel;
/*
 import siap.sius.avvocato.dao.AvvocatoFascicoloSiusDAO;
 import siap.sius.depositodecreto.controller.IDepositoDecreto;
 import siap.sius.depositodecreto.model.DepositoDecretoModel;
 import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
 import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
 import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
 import siap.sius.fascicolo.dao.FascicoloSiusDAO;
 import siap.sius.fascicolo.model.FascicoloGPModel;
 import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
 import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
 import siap.sius.tenore.dao.TenoreDAO;
 import siap.sius.tenore.dao.TenoreSqlDAO;
 import siap.sius.tenore.model.TenoreModel;
 import siap.sius.udienza.model.UdienzaModel;
 import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
 */
import siap.sige.udienzaprocedimento.dao.ProcedimentixUdienzaSqlDAO;
import siap.sige.udienzaprocedimento.dao.UdienzaProcedimentoSigeDAO;
import siap.sige.udienzaprocedimento.dao.UdienzaProcedimentoSigeSqlDAO;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcSigeUdienzaModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: UdienzaProcedimentoSigeController
 * </p>
 * <p>
 * Description: Classe Controller per UdienzaProcedimento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UdienzaProcedimentoSigeController extends SiapController implements IUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public UdienzaProcedimentoSigeModel ExInserisciUdienzaProcedimentoSige(
			UdienzaProcedimentoSigeModel aUdienzaProcedimento) throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoSigeDAO lUdiDao = null;
		UdienzaProcedimentoSigeModel lUdiMod = null;

		try {
			lConn = getDBConnection();
			lUdiMod = new UdienzaProcedimentoSigeModel(aUdienzaProcedimento);
			lUdiDao = new UdienzaProcedimentoSigeDAO(lConn);
			lUdiDao.setDAOFromModel(aUdienzaProcedimento);
			BigDecimal lKey = null;
			lKey = lUdiDao.insert();
			commit(lConn);
			lUdiMod.setIdUdienzaProcedimentoSige(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExInserisciUdienzaProcedimentoSige: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExInserisciUdienzaProcedimentoSige: " + e);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	/**
	 * Ricerca in Udienza_Procedimento_Sige per FAS_ID_FASCICOLO_SIGE
	 *
	 * @param aKey
	 *            FasIdFascicoloSige
	 * @return Vector di UdienzaProcedimentoSige
	 * @throws F3BException
	 */
	public Vector ExRicercaUdienzaProcedimentoByIdFascicoloSige(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lUdienzaProcedimenti = new Vector();
		UdienzaProcedimentoSigeSqlDAO lUdiProSqlDao = null;
		try {
			lConn = getDBConnection();
			lUdiProSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			lUdiProSqlDao.ricercaUdienzaProcedimentoByIdFascicoloSige(aKey);
			lUdienzaProcedimenti = new Vector(lUdiProSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByIdFascicoloSige: "
							+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByIdFascicoloSige: " + e);
		} finally {
			cleanup(lUdiProSqlDao);
			cleanup(lConn);
		}
		return lUdienzaProcedimenti;
	}

	/**
	 * Ricerca in Udienza_Procedimento_Sige per EVE_ID_EVENTO
	 *
	 * @param aKey
	 *            EveIdEvento
	 * @return UdienzaProcedimentoSigeModel
	 * @throws F3BException
	 */
	public UdienzaProcedimentoSigeModel ExRicercaUdienzaProcedimentoByEve(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		UdienzaProcedimentoSigeSqlDAO lUdiSqlDao = null;
		UdienzaProcedimentoSigeModel lUdiMod = null;

		try {
			lConn = getDBConnection();
			lUdiSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			lUdiSqlDao.ricercaUdienzaProcedimentoByEve(aKey);
			lUdiMod = (UdienzaProcedimentoSigeModel) lUdiSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByEve: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByEve: " + e);
		} finally {
			cleanup(lUdiSqlDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	public UdienzaProcedimentoSigeModel ExRicercaUdienzaProcedimentoSigeByKey(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		UdienzaProcedimentoSigeSqlDAO lUdiDao = null;
		UdienzaProcedimentoSigeModel lUdiMod;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			lUdiDao.ricercaUdienzaProcedimentoByKey(aKey);
			lUdiMod = (UdienzaProcedimentoSigeModel) lUdiDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoSigeByKey: : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoSigeByKey: " + e);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	/**
	 * Effettua l'annullamento di una udienza precedentemente fissata.
	 * <p>
	 *
	 * @param UdienzaProcedimentoSigeModel
	 * @throws F3BException
	 */
	public void ExCancellaFissazioneUdienza(UdienzaProcedimentoSigeModel aUdienzaProcedimento)
			throws F3BException {
		Connection lConn = null;
		NotificaDAO lNotDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		FascicoloSigeDAO lFasSigeDAO = null;
		ProvvedimentoSigeEventoModel lProvEve = null;
		UdienzaProcedimentoSigeDAO lUdiDao = null;
		IProvvedimentoSige lProvCtrl = null;

		BigDecimal lIdEvento = null;
		BigDecimal lIdFascicolo = null;

		lIdEvento = aUdienzaProcedimento.getEveIdEvento();
		if (lIdEvento == null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID Evento non valorizzato nell' UDIENZA_PROCEDIMENTO");
		// throw new F3BException("ID Evento non valorizzato nell' UDIENZA_PROCEDIMENTO");

		lIdFascicolo = aUdienzaProcedimento.getFasIdFascicoloSige();
		if (lIdFascicolo == null)
			throw new F3BException("ID Fascicolo non valorizzato nell' UDIENZA_PROCEDIMENTO");

		if (lIdEvento != null) {
			// ricerca del record PROVVEDIMENTO_SIGE da cancellare
			lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
			lProvEve = lProvCtrl.ExRicercaProvvedimentoByIdEvento(lIdEvento);
			if (lProvEve == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("record PROVVEDIMENTO_SIGE inesistente");
		}

		try {
			lConn = getDBConnection();

			if (lIdEvento != null) {
				// Cancellazione Notifiche
				lNotDao = new NotificaDAO(lConn);
				lNotDao.setCondizioneEvento(lIdEvento);
				lNotDao.delete();
				lNotDao.stop();

				// Cancellazione Campo Note
				lCampoNotaDao = new CampoNotaDAO(lConn);
				lCampoNotaDao.setCondizioneEvento(lIdEvento);
				lCampoNotaDao.delete();
				lCampoNotaDao.stop();
			}

			// Update di FASCICOLO_SIGE
			lFasSigeDAO = new FascicoloSigeDAO(lConn);
			lFasSigeDAO.setCodUfficioAggiornamento(aUdienzaProcedimento.getCodUfficioAggiornamento());
			lFasSigeDAO.setCodOperatoreAggiornamento(aUdienzaProcedimento.getCodOperatoreAggiornamento());
			lFasSigeDAO.setDataAggiornamento(aUdienzaProcedimento.getDataAggiornamento());
			lFasSigeDAO.setCondizioneUpdate(lIdFascicolo);
			lFasSigeDAO.update();
			lFasSigeDAO.stop();

			// Update UDIENZA_PROCEDIMENTO_SIGE
			lUdiDao = new UdienzaProcedimentoSigeDAO(lConn);
			// Viene cancellato il riferimento all'evento
			aUdienzaProcedimento.seEveIdEvento(null);
			lUdiDao.setDAOFromModelForUpdate(aUdienzaProcedimento);
			lUdiDao.update();

			if (lProvEve != null) {
				// Cancellazione Provvedimento ed Evento
				ProvvedimentoSigeModel lProvSige = lProvEve.getProvvedimento();
				lProvSige.setCodUfficioAggiornamento(aUdienzaProcedimento.getCodUfficioAggiornamento());
				lProvSige.setCodOperatoreAggiornamento(aUdienzaProcedimento.getCodOperatoreAggiornamento());
				lProvSige.setDataAggiornamento(aUdienzaProcedimento.getDataAggiornamento());
				lProvCtrl.ExCancellaProvvedimentoSige(lProvSige, lConn);
			}

			// FINE
			commit(lConn);

		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Fascicoli gia' fissati per l'udienza. Impossibile effettuare la Cancellazione!");

			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExCancellaFissazioneUdienza:  " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("UdienzaProcedimentoSigeController.ExCancellaFissazioneUdienza: " + e);
		} finally {
			if (lIdEvento != null) {
				cleanup(lCampoNotaDao);
				cleanup(lNotDao);
			}
			cleanup(lFasSigeDAO);
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return;
	}

	/**
	 * Ricerca in udienza_procedimento_sige per id_Fascicolo_sige il record con il valore flag_rinviata
	 * contenuto nella stringa passata
	 *
	 * @param aKey
	 *            idFascicoloSige
	 * @param aFilter
	 *            Stringa composta dai valori da ricercare per Flag_Rinviata es: "'F','S'"
	 * @return UdienzaProcedimentoSigeModel
	 * @throws F3BException
	 */
	public UdienzaProcedimentoSigeModel ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
			BigDecimal aKey, String aFilter) throws F3BException {
		return ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(aKey, aFilter, true);
	}

	public UdienzaProcedimentoSigeModel ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
			BigDecimal aKey, String aFilter, boolean complete) throws F3BException {
		Connection lConn = null;
		UdienzaProcedimentoSigeSqlDAO lUdiDao = null;
		UdienzaProcedimentoSigeModel lUdiMod = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			if (complete) {
				lUdiDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(aKey, aFilter);
				lUdiDao.start();
				if (lUdiDao.next())
					lUdiMod = (UdienzaProcedimentoSigeModel) lUdiDao.getModelConDataUdienza();
				lUdiDao.stop();
			} else {
				lUdiDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(aKey, aFilter, false);
				lUdiDao.start();
				if (lUdiDao.next())
					lUdiMod = (UdienzaProcedimentoSigeModel) lUdiDao.getModelConDataUdienza();
				lUdiDao.stop();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata: "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata: "
							+ ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	/**
	 * Inserisce Ordinanza Rinvio Udienza
	 * <p>
	 *
	 * @param aUdienza
	 *            Dati udienza.
	 * @param aFascSige
	 *            dati fascicolo SIGE.
	 * @param aEve
	 *            dati evento.
	 * @param aTenori
	 *            dati tenori.
	 * @param lDepositoOrdinanzaPc
	 *            dati deposito ordinanza
	 * @param aGeneraleProcedimentoold
	 *            dati Generale procedimento.
	 * @return
	 *         <p>
	 * @throws F3BException
	 *             propaga errore di eccezione
	 */

	// Funzione non utilizzata
	public EventoModel ExInserisciOrdinanzaRinvioUdienza(UdienzaSigeModel aUdienza,
			FascicoloSigeModel aFascSige, ProvvedimentoSigeEventoModel aProvvEvento) throws F3BException {
		Connection lConn = null;

		UdienzaProcedimentoSigeDAO lUdiProcDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiProcSqlDao = null;
		FascicoloSigeDAO lFasSigeDao = null;
		ProvvedimentoSigeDAO lProvvDao = null;
		EventoDAO lEveDao = null;
		EventoModel lEvento = new EventoModel(aProvvEvento.getEventoNotifica().getEvento());

		try {
			lConn = getDBConnection();

			// Si esegue inserimento nuovo evento.
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEvento);
			lEvento.setIdEvento(lEveDao.insert());
			lEveDao.stop();

			// Ricerca se esistono udienze per quel Fascicolo Sige con stato F o S
			UdienzaProcedimentoSigeModel lUdiProcSige = new UdienzaProcedimentoSigeModel();
			lUdiProcSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			lUdiProcSqlDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(
					aFascSige.getIdFascicoloSige(), "'F','S'");

			lUdiProcSqlDao.start();
			if (lUdiProcSqlDao.next())
				lUdiProcSige = (UdienzaProcedimentoSigeModel) lUdiProcSqlDao.getModelConDataUdienza();
			lUdiProcSqlDao.stop();

			if (lUdiProcSige == null)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");

			BigDecimal lIdProvvSige = null;

			//
			// Sulla tabella Udienza Procedimento si esegue l'update del flag_rinviata
			//
			lUdiProcDao = new UdienzaProcedimentoSigeDAO(lConn);

			if (aUdienza.getDataUdienza() != null) {
				// Aggiornamento ultimo rec
				lUdiProcDao.setFlagRinviata("R");
				lUdiProcDao.setUdiIdUdienzaRinvio(aUdienza.getIdUdienzaSige());
				lUdiProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setDataAggiornamento(DateUtils.getSysDate());
				// lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimentoSige(lUdiProcSige.getIdUdienzaProcedimentoSige());
				lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimentoSige(lUdiProcSige);
				lUdiProcDao.update();
				lUdiProcDao.stop();

				// Inserimento ultimo Rec.
				lUdiProcDao = new UdienzaProcedimentoSigeDAO(lConn);
				// lUdiProcDao.setGenPridGeneraleProcedimento(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lUdiProcDao.setFasIdFascicoloSige(aFascSige.getIdFascicoloSige());
				lUdiProcDao.setUdiIdUdienzaSige(aUdienza.getIdUdienzaSige());
				lUdiProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdiProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setFlagRinviata("S");
				lUdiProcDao.setEveIdEvento(lEvento.getIdEvento());
				lUdiProcDao.insert();
				lUdiProcDao.stop();

				// Generazione Provvedimento
				lProvvDao = new ProvvedimentoSigeDAO(lConn);
				lProvvDao.setDAOFromModel(aProvvEvento.getProvvedimento());
				lProvvDao.setIdEventoGenerato(lEvento.getIdEvento());
				lIdProvvSige = lProvvDao.insert();
				lProvvDao.stop();

			} else // Condizione di Rinvio a nuovo ruolo.
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Prima Update record ....");

				// Esegue Aggiornamento ultimo record in (M).
				lUdiProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setDataAggiornamento(DateUtils.getSysDate());
				lUdiProcDao.setFlagRinviata("M");
				lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimentoSige(lUdiProcSige);
				lUdiProcDao.update();
				lUdiProcDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dopo Update record ....");

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Prima Insert record ....");

				// Esegue inserimento nuovo record come Nuovo Ruolo (N).
				// lUdiProcDao.setGenPridGeneraleProcedimento(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lUdiProcDao.setFasIdFascicoloSige(aFascSige.getIdFascicoloSige());
				lUdiProcDao.setUdiIdUdienzaSige(lUdiProcSige.getUdiIdUdienzaSige());
				lUdiProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdiProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setFlagRinviata("N");
				lUdiProcDao.setEveIdEvento(lEvento.getIdEvento());
				lUdiProcDao.insert();
				lUdiProcDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dopo Insert record ....");

				// Fascicolo SIGE Rinviato a nuovo ruolo.
				lFasSigeDao = new FascicoloSigeDAO(lConn);
				lFasSigeDao.setCodStatoFascicolo("10");
				lFasSigeDao.setCondizioneUpdate(aFascSige.getIdFascicoloSige());
				lFasSigeDao.update();
				lFasSigeDao.stop();

				// Generazione Provvedimento
				lProvvDao = new ProvvedimentoSigeDAO(lConn);
				lProvvDao.setDAOFromModel(aProvvEvento.getProvvedimento());
				lProvvDao.setIdEventoGenerato(lEvento.getIdEvento());
				lIdProvvSige = lProvvDao.insert();
				lProvvDao.stop();

			}

			// Imposta l'id Provvedimento Sige inserito, nel model del provvedimento
			// necessario per l'inserimento dei tenori
			aProvvEvento.getProvvedimento().setIdProvvedimentoSige(lIdProvvSige);

			// Gestione dei Tenori.

			// Valorizzazione campi Tenori.

			// In sessione c'è una lista di TenoreEstesoModel
			TenoriSigeUtil lTenUtil = new TenoriSigeUtil();
			Vector lTenori = lTenUtil.listaTenoriDaListaTenoriEstesi(aProvvEvento.getTenoriEstesi());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Dimensione lista Tenori :" + lTenori.size());

			lTenori = aggiornaListaTenori(lTenori, aProvvEvento.getProvvedimento());

			// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciOrdinanzaRinvioUdienza : Fase di chiusura per il Tenore");

			ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
			lCtrlTen.ExInserisciOggetti(lTenori, aFascSige.getIdFascicoloSige(), lConn);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);

			// Controlla l'esistenza di un procedimento
			// per l'udienza richiesta.
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Esiste già un procedimento per l'udienza richiesta!");

			throw new SIGEException(
					"UdienzaController.ExInserisciOrdinanzaRinvioUdienza: Non posso leggere : " + daoEx);
		} catch (F3BException f3bex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("F3BException: " + f3bex);

			throw f3bex;
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);

			throw new SIGEException(
					"UdienzaController.ExInserisciOrdinanzaRinvioUdienza: Non posso leggere  : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lProvvDao);
			cleanup(lUdiProcDao);
			cleanup(lUdiProcSqlDao);
			cleanup(lFasSigeDao);
			cleanup(lConn);
		}

		return lEvento;
	}

	/**
	 * Inserisce Ordinanza Rinvio Udienza
	 * <p>
	 *
	 * @param aUdienza
	 *            Dati udienza.
	 * @param aFascSige
	 *            dati fascicolo SIGE.
	 * @param aEve
	 *            dati evento.
	 * @param aTenori
	 *            dati tenori.
	 * @param lDepositoOrdinanzaPc
	 *            dati deposito ordinanza
	 * @param aGeneraleProcedimentoold
	 *            dati Generale procedimento.
	 * @return
	 *         <p>
	 * @throws F3BException
	 *             propaga errore di eccezione
	 */
	public EventoModel ExInserisciRinvioUdienza(UdienzaSigeModel aUdienza, FascicoloSigeModel aFascSige,
			ProvvedimentoSigeEventoModel aProvvEvento) throws F3BException {
		Connection lConn = null;

		UdienzaProcedimentoSigeDAO lUdiProcDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiProcSqlDao = null;
		FascicoloSigeDAO lFasSigeDao = null;
		ProvvedimentoSigeDAO lProvvDao = null;
		EventoDAO lEveDao = null;
		EventoModel lEvento = new EventoModel(aProvvEvento.getEventoNotifica().getEvento());

		try {
			lConn = getDBConnection();

			// Si esegue inserimento nuovo evento.
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEvento);
			lEvento.setIdEvento(lEveDao.insert());
			lEveDao.stop();

			// Ricerca se esistono udienze per quel Fascicolo Sige con stato F o S
			UdienzaProcedimentoSigeModel lUdiProcSige = new UdienzaProcedimentoSigeModel();
			lUdiProcSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			lUdiProcSqlDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(
					aFascSige.getIdFascicoloSige(), "'F','S'");

			lUdiProcSqlDao.start();
			if (lUdiProcSqlDao.next())
				lUdiProcSige = (UdienzaProcedimentoSigeModel) lUdiProcSqlDao.getModelConDataUdienza();
			lUdiProcSqlDao.stop();

			if (lUdiProcSige == null)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");

			BigDecimal lIdProvvSige = null;

			//
			// Sulla tabella Udienza Procedimento si esegue l'update del flag_rinviata
			//
			lUdiProcDao = new UdienzaProcedimentoSigeDAO(lConn);

			if (aUdienza.getDataUdienza() != null) {
				// Aggiornamento ultimo rec
				lUdiProcDao.setFlagRinviata("R");
				lUdiProcDao.setUdiIdUdienzaRinvio(aUdienza.getIdUdienzaSige());
				lUdiProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setDataAggiornamento(DateUtils.getSysDate());
				// lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimentoSige(lUdiProcSige.getIdUdienzaProcedimentoSige());
				lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimentoSige(lUdiProcSige);
				lUdiProcDao.update();
				lUdiProcDao.stop();

				// Inserimento ultimo Rec.
				lUdiProcDao = new UdienzaProcedimentoSigeDAO(lConn);
				// lUdiProcDao.setGenPridGeneraleProcedimento(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lUdiProcDao.setFasIdFascicoloSige(aFascSige.getIdFascicoloSige());
				lUdiProcDao.setUdiIdUdienzaSige(aUdienza.getIdUdienzaSige());
				lUdiProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdiProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setFlagRinviata("S");
				lUdiProcDao.setEveIdEvento(lEvento.getIdEvento());
				lUdiProcDao.insert();
				lUdiProcDao.stop();

				// Generazione Provvedimento
				lProvvDao = new ProvvedimentoSigeDAO(lConn);
				lProvvDao.setDAOFromModel(aProvvEvento.getProvvedimento());
				lProvvDao.setIdEventoGenerato(lEvento.getIdEvento());
				lIdProvvSige = lProvvDao.insert();
				lProvvDao.stop();

			} else // Condizione di Rinvio a nuovo ruolo.
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Prima Update record ....");

				// Esegue Aggiornamento ultimo record in (M).
				lUdiProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setDataAggiornamento(DateUtils.getSysDate());
				lUdiProcDao.setFlagRinviata("M");
				lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimentoSige(lUdiProcSige);
				lUdiProcDao.update();
				lUdiProcDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dopo Update record ....");

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Prima Insert record ....");

				// Esegue inserimento nuovo record come Nuovo Ruolo (N).
				// lUdiProcDao.setGenPridGeneraleProcedimento(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lUdiProcDao.setFasIdFascicoloSige(aFascSige.getIdFascicoloSige());
				lUdiProcDao.setUdiIdUdienzaSige(aUdienza.getIdUdienzaSige());
				lUdiProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdiProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setFlagRinviata("N");
				lUdiProcDao.setEveIdEvento(lEvento.getIdEvento());
				lUdiProcDao.insert();
				lUdiProcDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dopo Insert record ....");

				// Fascicolo SIGE Rinviato a nuovo ruolo.
				lFasSigeDao = new FascicoloSigeDAO(lConn);
				lFasSigeDao.setCodStatoFascicolo("10");
				lFasSigeDao.setCondizioneUpdate(aFascSige.getIdFascicoloSige());
				lFasSigeDao.update();
				lFasSigeDao.stop();

				// Generazione Provvedimento
				lProvvDao = new ProvvedimentoSigeDAO(lConn);
				lProvvDao.setDAOFromModel(aProvvEvento.getProvvedimento());
				lProvvDao.setIdEventoGenerato(lEvento.getIdEvento());
				lIdProvvSige = lProvvDao.insert();
				lProvvDao.stop();
			}

			// Imposta l'id Provvedimento Sige inserito, nel model del provvedimento
			// necessario per l'inserimento dei tenori
			aProvvEvento.getProvvedimento().setIdProvvedimentoSige(lIdProvvSige);

			// Gestione dei Tenori.

			// Valorizzazione campi Tenori.

			// In sessione c'è una lista di TenoreEstesoModel
			TenoriSigeUtil lTenUtil = new TenoriSigeUtil();
			Vector lTenori = lTenUtil.listaTenoriDaListaTenoriEstesi(aProvvEvento.getTenoriEstesi());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Dimensione lista Tenori :" + lTenori.size());

			lTenori = aggiornaListaTenori(lTenori, aProvvEvento.getProvvedimento());

			// pone tutti i codice esito dei Tenori a "-"
			lTenori = resetListaTenori(lTenori);

			// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciOrdinanzaRinvioUdienza : Fase di chiusura per il Tenore");

			ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
			lCtrlTen.ExInserisciOggetti(lTenori, aFascSige.getIdFascicoloSige(), lConn);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);

			// Controlla l'esistenza di un procedimento
			// per l'udienza richiesta.
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Esiste già un procedimento per l'udienza richiesta!");

			throw new SIGEException(
					"UdienzaController.ExInserisciOrdinanzaRinvioUdienza: Non posso leggere : " + daoEx);
		} catch (F3BException f3bex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("F3BException: " + f3bex);

			throw f3bex;
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);

			throw new SIGEException(
					"UdienzaController.ExInserisciOrdinanzaRinvioUdienza: Non posso leggere  : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lProvvDao);
			cleanup(lUdiProcDao);
			cleanup(lUdiProcSqlDao);
			cleanup(lFasSigeDao);
			cleanup(lConn);
		}

		return lEvento;
	}

	/**
	 * Effettua l'annullamento di una udienza già rinviata.
	 * <p>
	 *
	 * @param aUdienzaProcedimento
	 * @param aIdFasSius
	 *            Id del fascicolo SIUS di rferimento
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExCancellaRinvioUdienza(UdienzaProcedimentoSigeModel aUdienzaProcedimento,
			BigDecimal aIdFasSige) throws F3BException {
		Connection lConn = null;
		NotificaDAO lNotDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		// UdienzaProcedimentoDAO lUdiDao = null;
		UdienzaProcedimentoSigeDAO lUdiSigeDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiSigeSqlDao = null;
		IProvvedimentoSige lProvvCtrl = null;
		ProvvedimentoSigeEventoModel lProvvEvMod = null;
		FascicoloSigeDAO lFasSigeDao = null;
		// EventoDAO lEventoDao = null;
		EventoSqlDAO lEventoSqlDao = null;

		BigDecimal lIdEvento = null; // BigDecimal lIdGenPro = null;

		lIdEvento = aUdienzaProcedimento.getEveIdEvento();

		if (lIdEvento == null)
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"ID Evento non valorizzato nell' UDIENZA_PROCEDIMENTO");

		if (lIdEvento != null) {
			// Ricerca del record PROVVEDIMENTO_SIGE da cancellare
			lProvvCtrl = SIGELookupRemote.getProvvedimentoRemote();
			lProvvEvMod = lProvvCtrl.ExRicercaProvvedimentoByIdEvento(lIdEvento);

			if (lProvvEvMod == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Record Provvedimento inesistente.");

		}

		try {
			lConn = getDBConnection();

			if (lIdEvento != null) {
				// Cancellazione Notifiche.
				lNotDao = new NotificaDAO(lConn);
				lNotDao.setCondizioneEvento(lIdEvento);
				lNotDao.delete();
				lNotDao.stop();

				// Cancellazione Campo Note.
				lCampoNotaDao = new CampoNotaDAO(lConn);
				lCampoNotaDao.setCondizioneEvento(lIdEvento);
				lCampoNotaDao.delete();
				lCampoNotaDao.stop();
			}

			// Rimozione UDIENZA_PROCEDIMENTO.
			lUdiSigeDao = new UdienzaProcedimentoSigeDAO(lConn);
			lUdiSigeDao.setCondizioneUpdate(aUdienzaProcedimento.getIdUdienzaProcedimentoSige());
			lUdiSigeDao.delete();
			lUdiSigeDao.stop();
			// *_*//

			// Lettura dell'ultimo Record.
			lUdiSigeSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			lUdiSigeSqlDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(aIdFasSige, "'R','M'");
			// lUdiSqlDao.ricercaUdienzaProcedimentoUdienzaByGenProByFlagRinviata( lIdGenPro, "'R','M'" );
			lUdiSigeSqlDao.start();

			UdienzaProcSigeUdienzaModel lUdiProSigeUdi = new UdienzaProcSigeUdienzaModel();

			if (lUdiSigeSqlDao.next())
				lUdiProSigeUdi = (UdienzaProcSigeUdienzaModel) lUdiSigeSqlDao.getModelConUdienza();

			lUdiSigeSqlDao.stop();

			// *_*//

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>>> lUdiProUdi : " + lUdiProSigeUdi);

			if (lUdiProSigeUdi.getUdienzaProcedimento() != null) {
				// Update UDIENZA_PROCEDIMENTO.
				lUdiSigeDao = new UdienzaProcedimentoSigeDAO(lConn);
				lUdiSigeDao.setFlagRinviata("F");
				lUdiSigeDao.setUdiIdUdienzaRinvio(null);
				lUdiSigeDao.setCondizioneUpdate(
						lUdiProSigeUdi.getUdienzaProcedimento().getIdUdienzaProcedimentoSige());
				lUdiSigeDao.update();
				lUdiSigeDao.stop();
			}

			// Cancellazione Provvedimento
			if (lProvvEvMod != null) {
				// Cancellazione Provvedimento ed Evento
				ProvvedimentoSigeModel lProvSige = lProvvEvMod.getProvvedimento();
				lProvSige.setCodUfficioAggiornamento(aUdienzaProcedimento.getCodUfficioAggiornamento());
				lProvSige.setCodOperatoreAggiornamento(aUdienzaProcedimento.getCodOperatoreAggiornamento());
				lProvSige.setDataAggiornamento(aUdienzaProcedimento.getDataAggiornamento());
				lProvvCtrl.ExCancellaProvvedimentoSige(lProvSige, lConn);
			}
			// Update dello stato del fascicolo SIUS se trattasi di nuovo ruolo
			// il fascicolo viene reimpostato con codice 02 ( iscritto ).
			if (lUdiProSigeUdi.getUdienzaProcedimento() == null
					|| (lUdiProSigeUdi.getUdienzaProcedimento() != null && lUdiProSigeUdi
							.getUdienzaProcedimento().getFlagRinviata().equalsIgnoreCase("M"))) {
				lFasSigeDao = new FascicoloSigeDAO(lConn);
				lFasSigeDao.setCodStatoFascicolo("02"); // Iscritto
				lFasSigeDao.setCondizioneUpdate(aIdFasSige);
				lFasSigeDao.update();
				lFasSigeDao.stop();
			}

			commit(lConn); // Fine
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Fascicoli gia' fissati per l'udienza. Impossibile effettuare la Cancellazione!");

			throw new SIGEException("UdienzaProcedimentoController.ExCancellaRinvioUdienza:  " + daoEx);
		} catch (F3BException fex) {
			rollback(lConn);
			throw fex;
		} catch (Exception e) {
			rollback(lConn);
			throw new SIGEException("UdienzaProcedimentoController.ExCancellaRinvioUdienza: " + e);
		} finally {
			if (lIdEvento != null) {
				cleanup(lCampoNotaDao);
				cleanup(lNotDao);
			}

			cleanup(lEventoSqlDao);
			// cleanup(lGenDAO);
			// cleanup(lUdiDao);
			cleanup(lConn);
		}
	}

	private Vector aggiornaListaTenori(Vector aListaTenori, ProvvedimentoSigeModel aProvvedimento) {
		if (aListaTenori != null) {
			Iterator itx = aListaTenori.iterator();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();

				lTenore.setCodOperatoreInserimento(aProvvedimento.getCodOperatoreInserimento());
				lTenore.setCodUfficioInserimento(aProvvedimento.getCodUfficioInserimento());
				lTenore.setDataInserimento(aProvvedimento.getDataInserimento());
				lTenore.setData(aProvvedimento.getDataInserimento());
				lTenore.setFasIdFascicoloSige(aProvvedimento.getFasIdFascicoloSige());
				lTenore.setProvIdProvvedimentoSige(aProvvedimento.getIdProvvedimentoSige());
				lTenore.setRicSigIdRichiestaSige(null);
			}
		} // endif

		return aListaTenori;
	}

	/**
	 * Resetta tutti gli esiti a "-".
	 *
	 * @param aListaTenori
	 * @return
	 */

	private Vector resetListaTenori(Vector aListaTenori) {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("resetListaTenori: inizio");

		if (aListaTenori != null) {
			Iterator itx = aListaTenori.iterator();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();
				lTenore.setCodEsitoSige("-");
			}
		} // endif
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
		siesLogger.debug("resetListaTenori: fine");

		return aListaTenori;
	}

	/**
	 * Esecuzione stampa Ordinanza Rinvio Udienza
	 * <p>
	 *
	 * @param lEvento
	 * @param lUfficio
	 * @param aUtenteModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaOrdinanzaRinvioUdienza(BigDecimal aIdFascicolo, EventoModel lEvento,
			String aCodUff, UtenteModel aUtenteModel) throws F3BException {
		ByteArrayOutputStream lByteArrayOut = null;
		EventoDAO lEveDao = null;
		Connection lConn = null;

		try {
			// Generazione documento di stampa
			IStampaSige lCtrlStampa = SIGELookupRemote.getStampaRemote();

			// Riempie l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO,
					ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO, ICostantiStampaSige.TREE_PROVVEDIMENTO,
					ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
					ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
					ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
					ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA };

			int aTipoStampa = ICostantiStampaSige.STAMPA_UDIENZA;

			TreeModel lTree = lCtrlStampa.ExPrelevaDatiStampa(aIdFascicolo, aTipoDati, aTipoStampa, aCodUff,
					null);

			ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lEvento.getTemIdTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug( ">>>>>> Generato il Documento ." );
			lEvento.setDocBlobIn(lByteArrayInput);

			// Inserisce il documento generato nel model di ritorno.
			// In esso inserisce il Nome del template di ritorno e il documento generato.

			// Preleva connessione dal DB
			lConn = getDBConnection();

			// Prepara un EventoDAO.
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(lEvento);

			// Seleziona le condizioni di Update.
			lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExStampaOrdinanzaRinvioUdienza: Non posso inserire il documento nell'evento : "
							+ daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + ex);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExStampaOrdinanzaRinvioUdienza: Non posso inserire il documento nell'evento : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Metodo ExRicercaProcedimentixUdienza con l'aggiunta di parametri discriminanti il tipo Procedimento.
	 * <p>
	 *
	 * @param aDataUdienza
	 * @param aOrderBy
	 * @param aStatoProcedimento
	 * @param aTipoProc
	 */
	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentixDataUdienza(Date aDataUdienza,
			String aOrderBy, String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inizio");

		Connection lConn = null;
		Vector<ProcedimentixUdienzaModel> lProcedimenti = new Vector<>();
		// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
		// variabile introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
		// prototipo PALERMO)
		Vector<ProcedimentixUdienzaModel> lProcedimentiNoDu = new Vector<>();
		ProcedimentixUdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);
			lUdiDao.ricercaProcedimentixUdienzaByDataUdienza(aDataUdienza, aOrderBy, aStatoProcedimento,
					aTipoProc, aCodUfficioConnesso);
			lUdiDao.start();
			ProcedimentixUdienzaModel lProcedimentoUdienza = null;
			Vector<BigDecimal> vIdFascSige = new Vector<>();
			while (lUdiDao.next()) {
				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lUdiDao.getModelEvento();
				lProcedimenti.add(lProcedimentoUdienza);
			}

			lUdiDao.stop();

			// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
			// controllo introdotta per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018
			// (errore prototipo PALERMO)
			if (lProcedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato!");
			} else {

				// for(int i=0;i<lProcedimenti.size();i++)
				// {
				// BigDecimal idfas = lProcedimenti.get(i).getIdFasSIGE();
				//
				// if (lProcedimentiNoDu.size() == 0) {
				// lProcedimentiNoDu.add(lProcedimenti.get(i));
				// }
				// else{
				//
				// for(int a=0;a<lProcedimentiNoDu.size();a++)
				// {
				// BigDecimal idfasNod = lProcedimenti.get(a).getIdFasSIGE();
				//
				// if(!idfas.equals(idfasNod))
				// {
				// lProcedimentiNoDu.add(lProcedimenti.get(i));
				// }
				// }
				// }
				//
				// }

				if (lProcedimenti.size() > 0) {

					for (int i = 0; i < lProcedimenti.size(); i++) {
						BigDecimal idfas = lProcedimenti.get(i).getIdFasSIGE();

						if (lProcedimentiNoDu.size() == 0) {
							lProcedimentiNoDu.add(lProcedimenti.get(i));
							vIdFascSige.add(idfas);
						} else {
							if (!vIdFascSige.contains(idfas)) {
								lProcedimentiNoDu.add(lProcedimenti.get(i));
								vIdFascSige.add(idfas);
							}
						}

					}
				}
			}

		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ExRicercaProcedimentixDataUdienza: " + daoEx);
		} catch (F3BException f3bex) {
			throw f3bex;
		} catch (Exception ex) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixDataUdienza: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("fine");
		// ritorno il vettore dei procedimenti non duplicati
		return lProcedimentiNoDu;
	}

	/**
	 * Ricerca Udienze - Magistrati Relatori - Procedimenti per data minima, data massima dell'Udienza.
	 *
	 * @param aUdienza
	 * @return
	 * @throws F3BException
	 */
	public Collection<Object> ExRicercaUdienzeMagistratiProcedimentiByDate(UdienzaSigeModel aUdienza)
			throws F3BException {
		Connection lConn = null;
		UdienzaProcedimentoSigeSqlDAO lUdiDao = null;
		Collection lListaRisultato = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			lListaRisultato = lUdiDao.ricercaUdienzeMagistratiProcedimenti(aUdienza);
		} catch (Exception e) {
			throw new F3BException(F3BException.USER_MESSAGE, "Errore nella lettura : " + e);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lListaRisultato;
	}

	/**
	 *
	 */
	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentixUdienza(BigDecimal aIdUdienza,
			String aOrderBy) throws F3BException {
		Connection lConn = null;
		Vector<ProcedimentixUdienzaModel> lProcedimenti = new Vector<>();
		ProcedimentixUdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);
			lUdiDao.ricercaProcedimentixUdienzaByUdienza(aIdUdienza, aOrderBy);

			// lProcedimenti = new Vector(lUdiDao.getModels());

			lUdiDao.start();

			ProcedimentixUdienzaModel lProcedimentoUdienza = null;
			while (lUdiDao.next()) {
				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lUdiDao.getExtendModel();
				lProcedimenti.add(lProcedimentoUdienza);
			}
			lUdiDao.stop();

			// if (lProcedimenti.size() == 0) {
			// throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			// }
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + daoEx);
		} catch (F3BException f3bex) {
			throw f3bex;
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return lProcedimenti;
	}

	/**
	 * OverLoading del metodo ExRicercaProcedimentixUdienza con l'aggiunta di parametri discriminanti il tipo
	 * Procedimento.
	 *
	 * @param aIdUdienza
	 * @param aOrderBy
	 * @param aStatoProcedimento
	 * @param aTipoProc
	 */
	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentixUdienza(BigDecimal aIdUdienza,
			String aOrderBy, String aStatoProcedimento, String aTipoProc, String flagModifBlocco,
			String codMAg) throws F3BException {
		Connection lConn = null;
		Vector<ProcedimentixUdienzaModel> lProcedimenti = new Vector<>();
		// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
		// variabile introdotta per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
		// prototipo PALERMO)
		Vector<ProcedimentixUdienzaModel> lProcedimentiNoDu = new Vector<>();
		Vector<BigDecimal> vIdFascSige = new Vector<>();

		ProcedimentixUdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);
			lUdiDao.ricercaProcedimentiRuoloByUdienza(aIdUdienza, aOrderBy, aStatoProcedimento, aTipoProc,
					flagModifBlocco, codMAg);
			lUdiDao.start();
			ProcedimentixUdienzaModel lProcedimentoUdienza = null;
			while (lUdiDao.next()) {
				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lUdiDao.getExtendModel();
				lProcedimenti.add(lProcedimentoUdienza);
			}
			lUdiDao.stop();

			// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
			// controllo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018
			// (errore prototipo PALERMO)
			if (lProcedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			} else {

				for (int i = 0; i < lProcedimenti.size(); i++) {
					BigDecimal idfas = lProcedimenti.get(i).getIdFasSIGE();

					if (lProcedimentiNoDu.size() == 0) {
						lProcedimentiNoDu.add(lProcedimenti.get(i));
						vIdFascSige.add(idfas);
					} else {
						if (!vIdFascSige.contains(idfas)) {
							lProcedimentiNoDu.add(lProcedimenti.get(i));
							vIdFascSige.add(idfas);
						}
					}

				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + daoEx);
		} catch (F3BException f3bex) {
			throw f3bex;
		} catch (Exception ex) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		// ritorno il vettore di procedimenti non duplicati
		return lProcedimentiNoDu;
	}

	public ByteArrayOutputStream ExStampaProcedimentixUdienzaFascicolo(BigDecimal aIdUdienza,
			BigDecimal aIdFascicolo, String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa,
			String aIdDocumento, String lOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento,
			String aTipoProc, String aCodUfficioConnesso) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		ByteArrayOutputStream lByteArrayOut = null;
		// TreeModel lTree = this.prelevaDati( aIdUdienza, aCodMagistrato, aIdEsperto, aStampa, lOrderBy);

		IStampaSige lCtrStampa = SIGELookupRemote.getStampaRemote();

		TreeModel lTree = lCtrStampa.ExPrelevaDatiStampaProcedimentixUdienza(aIdUdienza, aIdFascicolo,
				aCodMagistrato, aIdEsperto, aStampa, aIdDocumento, lOrderBy, aUtenteModel, aStatoProcedimento,
				aTipoProc, aCodUfficioConnesso);

		// TreeModel lTree =
		// this.prelevaDati( aIdUdienza, aCodMagistrato, aIdEsperto,
		// aStampa, lOrderBy, aStatoProcedimento,
		// aTipoProc, aCodUfficioConnesso );
		// ReportGenerator lReport = new ReportGenerator();

		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		String lNomeTemplate = null;
		// if (aIdMagistrato == null ){
		lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdDocumento);
		// }else{
		// lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIUS_RU_002");
		// }

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		// lReport.parseTreeXML(lTree);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

		return lByteArrayOut;
	}

	public ByteArrayOutputStream ExStampaProcedimentixUdienza(BigDecimal aIdUdienza, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String lOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		ByteArrayOutputStream lByteArrayOut = null;
		// TreeModel lTree = this.prelevaDati( aIdUdienza, aCodMagistrato, aIdEsperto, aStampa, lOrderBy);

		IStampaSige lCtrStampa = SIGELookupRemote.getStampaRemote();

		TreeModel lTree = lCtrStampa.ExPrelevaDatiStampaProcedimentixUdienza(aIdUdienza, aIdFascicolo,
				aCodMagistrato, aIdEsperto, aStampa, aIdDocumento, lOrderBy, aUtenteModel, aStatoProcedimento,
				aTipoProc, aCodUfficioConnesso);
		// TreeModel lTree =
		// this.prelevaDati( aIdUdienza, aCodMagistrato, aIdEsperto,
		// aStampa, lOrderBy, aStatoProcedimento,
		// aTipoProc, aCodUfficioConnesso );
		// ReportGenerator lReport = new ReportGenerator();

		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		String lNomeTemplate = null;
		// if (aIdMagistrato == null ){
		lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdDocumento);
		// }else{
		// lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIUS_RU_002");
		// }

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		// lReport.parseTreeXML(lTree);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

		return lByteArrayOut;
	}

	/**
	 *
	 */
	public ByteArrayOutputStream ExStampaProcedimentixDataUdienza(Date aDataUdienza, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String lOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		IStampaSige lCtrStampa = SIGELookupRemote.getStampaRemote();
		TreeModel lTree = lCtrStampa.ExPrelevaDatiStampaProcedimentixDataUdienza(aDataUdienza, aIdFascicolo,
				aCodMagistrato, aIdEsperto, aStampa, aIdDocumento, lOrderBy, aUtenteModel, aStatoProcedimento,
				aTipoProc, aCodUfficioConnesso);

		String lNomeTemplate = null;
		lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdDocumento);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		ByteArrayOutputStream lByteArrayOut = null;

		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

		return lByteArrayOut;
	}

	/**
	 * 20110121 - Da eliminare dopo il completamento della nuova funzione di stampa [p]
	 *
	 * Metdodo che si occupa di prelevare i dati necessari alla stampa e quindi alla generazione della
	 * struttura dati XML. Come si noterà il metodo prende un parametro di tipo Object poichè è stato
	 * generalizzato per gestire il recupero dati sia per IDUdienza (BigDecimal) che per DataUdienza (Date).
	 */
	// private TreeModel prelevaDati(Object aValue, String aCodMagistrato, BigDecimal aIdEsperto,
	// XModel aStampaMod, String lOrderBy, String aStatoProcedimento, String tipoProc,
	// String aCodUfficioConnesso) throws F3BException {
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.debug("UdienzaProcedimentoController.prelevaDati() : inizio");
	//
	// TreeModel lTreeRoot = null;
	// TreeModel lTreeUdienza = null;
	// TreeModel lTreeFasSIGE = null;
	// TreeModel lTreeMagistrato = null;
	// TreeModel lTreeSoggetto = null;
	// TreeModel lTreeAvvocato = null;
	//
	// // TreeModel lTreeRoot; // radice dell'albero generale del documento
	// // TreeModel lRootUdienza; // radice del sottoalbero a partire dal nodo Udienza
	// // TreeModel lTreeTenore = null;
	// // TreeModel lTreeOrdinanza = null;
	//// String lCodMagistrato = null;
	// // Vector lTenori = new Vector();
	//
	// UdienzaSigeModel lUdienzaSige = null;
	//// EventoModel lEvento;
	//
	// // RiferimentoFascicoloSiusModel lRifaSius;
	// // FascicoloGPModel lFasGPMod = new FascicoloGPModel();
	//
	// Collection<ProcedimentixUdienzaModel> lProcedimentiPerUdienza = new
	// Vector<ProcedimentixUdienzaModel>();
	//
	// // Si individua il tipo di parametro passato ( BigDecimal o Date )
	//
	// if (aValue instanceof BigDecimal) { // bigdecimal
	// BigDecimal lIdUdienza = (BigDecimal) aValue;
	// lProcedimentiPerUdienza = ExRicercaProcedimentixUdienza(lIdUdienza, lOrderBy, aStatoProcedimento,
	// tipoProc);
	// // Recupera i dati dell'udienza. ( verificare se recuperare dati dalllo stampacontrollersige
	// IUdienzaSige lCtrlUd = SIGELookupRemote.getUdienzaSigeRemote();
	// lUdienzaSige = lCtrlUd.ExRicercaUdienzaSigeById(lIdUdienza);
	// } else if (aValue instanceof Date) { // date
	// Date lDataUdienza = (Date) aValue;
	// lProcedimentiPerUdienza = ExRicercaProcedimentixDataUdienza(lDataUdienza, lOrderBy,
	// aStatoProcedimento, tipoProc, aCodUfficioConnesso);
	// lUdienzaSige = new UdienzaSigeModel();
	// lUdienzaSige.setDataUdienza(lDataUdienza);
	// }
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.info("UdienzaSige: " + lUdienzaSige);
	//
	// // Ricerca Udienza
	// Iterator<ProcedimentixUdienzaModel> itx = lProcedimentiPerUdienza.iterator();
	//
	// // Intestazione del documento
	// lTreeRoot = new TreeModel(aStampaMod);
	//
	// // Abilita la profondita sull ispezione dei model.
	// // lTreeRoot.setDeepImpact(true);
	//
	// lTreeUdienza = new TreeModel(lUdienzaSige);
	// // lTreeUdienza.add(new TreeModel(lUdienzaSige.getCollegio()));
	// // lTreeUdienza.add(new TreeModel(lUdienzaSige.getCollegio().getSezione()));
	// // lTreeUdienza.add(new TreeModel(lUdienzaSige.getCollegio().));
	//
	// /*
	// * try { lTreeUdienza = XMLUtils.buildTreeModel(new TreeModel(lUdienzaSige)); } catch(Exception ex){
	// * throw new F3BException(ex); }
	// */
	//// lCodMagistrato = (aCodMagistrato != null && aCodMagistrato.trim().length() > 1) ? aCodMagistrato
	//// : null;
	//// int i = 0; // Contatore del progressivo fascicolo
	//
	// while (itx.hasNext()) {
	// ProcedimentixUdienzaModel lProcedimento = (ProcedimentixUdienzaModel) itx.next();
	//
	//// i++; // Si incrementa di +1 il contatore del progressivo fascicolo.
	//
	// // Preleva fascisoloSIGE
	// lTreeFasSIGE = new TreeModel(lProcedimento.getFascicoloSige());
	//
	// // Preleva i dati del soggetto
	// lTreeSoggetto = new TreeModel(lProcedimento.getSoggetto());
	//
	// // Preleva dati del magistrato assegnatario o non ?
	// lTreeMagistrato = new TreeModel(lProcedimento.getMagistrato());
	//
	// // Preleva dati dell'avvocato difensore
	// lTreeAvvocato = new TreeModel(lProcedimento.getAvvocato());
	//
	// // Oggetti
	// Utils.arrayToString(lProcedimento.getDescrOggettiProcedimento(), ";");
	//
	// // Composizione
	// lTreeFasSIGE.add(lTreeSoggetto);
	// lTreeFasSIGE.add(lTreeMagistrato);
	// lTreeFasSIGE.add(lTreeAvvocato);
	// lTreeFasSIGE.add(new TreeModel(lProcedimento));
	// lTreeUdienza.add(lTreeFasSIGE);
	//
	// lTreeRoot.add(lTreeUdienza);
	//
	// /*
	// * if ( (lCodMagistrato == null && aIdEsperto == null) || (lCodMagistrato != null &&
	// * lProcedimento.getCodMagistrato() != null &&
	// * lProcedimento.getCodMagistrato().compareTo(lCodMagistrato) == 0 ) || (aIdEsperto != null &&
	// * lProcedimento.getIdEsperto() != null && aIdEsperto.compareTo(lProcedimento.getIdEsperto())== 0
	// * ) ){
	// *
	// * i++; // Contatore per progressivo fascicolo
	// *
	// * // Preleva i dati del fascicolo ( da sistemare per sige ) IFascicoloSius lCtrl =
	// * SIUSLookupRemote.getFascicoloSiusRemote(); //lFasGPMod =
	// * lCtrl.ExRicercaFascicoloByKey(lProcedimento.getIdFasSIUS()); //lTreeFasSIUS = new
	// * TreeModel(lFasGPMod.getFascicoloSiusModel() );
	// *
	// * // Setta il progressivo del fascicolo lRifaSius = new RiferimentoFascicoloSiusModel();
	// * lRifaSius.setProgrFascicoloSius(new BigDecimal(i)) ; lTreeFasSIUS.add (new
	// * TreeModel(lRifaSius));
	// *
	// * // Preleva dati Generale Procedimento // lTreeFasSIUS.add(new
	// * TreeModel(lFasGPMod.getGeneraleProcedimentoModel() )) ;
	// *
	// * //lTreeGenProc = new TreeModel( lFasGPMod.getGeneraleProcedimentoModel() );
	// *
	// * TenoreSqlDAO lTenDao = null; Vector lTenoriMod = new Vector();
	// *
	// * ITenore lCtrlTenGP = SIUSLookupRemote.getTenoreRemote(); //lTenori =
	// * lCtrlTenGP.ExRicercaTenoreByGenProc(
	// * lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() );
	// *
	// * if (lTenori.size() != 0) { Iterator lItxTen = lTenori.iterator(); while( lItxTen.hasNext() )
	// * lTreeGenProc.add(new TreeModel( (TenoreModel)lItxTen.next())); }
	// *
	// * lTreeFasSIUS.add(lTreeGenProc); // Preleva altri dati del fascicolo IStampaSius lCtrlSta =
	// * SIUSLookupRemote.getStampaRemote(); //Riempi l'Array contenente le tipologie di dati da
	// * prelevare int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO ,
	// * ICostantiStampaSius.TREE_AVVOCATO , ICostantiStampaSius.TREE_LUOGODET}; //Crea il TreeModel con
	// * i dati che occorrono lTreeFasSIUS =
	// * lCtrlSta.ExAggiungiDatiStampa(lProcedimento.getIdFasSIUS(),aTipoDati, lTreeFasSIUS);
	// *
	// * // Aggiunge i dati UdienzaProcedimento lTreeFasSIUS.add (new TreeModel(lProcedimento));
	// *
	// * // Preleva i dati dei fascicoli unificati Vector lVectFas = null; if
	// * (lFasGPMod.getFascicoloSiusModel() != null &&
	// * lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null &&
	// * lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati(). intValue() > 0) {
	// * FascicoloSiusModel lFasRicModel = new FascicoloSiusModel();
	// * lFasRicModel.setFasSiuIdFascicoloSius(lProcedimento.getIdFasSIUS()); lVectFas =
	// * lCtrl.ExRicercaElencoFascicoliUnificati(lFasRicModel); Iterator itx4 = lVectFas.iterator();
	// * while (itx4.hasNext()) { FascicoloSiusModel lFasUnificati = (FascicoloSiusModel) itx4.next();
	// * lTreeFasSIUS.add( new TreeModel(lFasUnificati)); } }
	// *
	// * // Preleva dati ultimo Evento if (lFasGPMod.getFascicoloSiusModel() != null ) { IEvento
	// * lCtrlEve = SICOLookupRemote.getEventoRemote(); lEvento =
	// * lCtrlEve.ExRicercaUltimoByFasSius(lFasGPMod
	// * .getFascicoloSiusModel().getIdFascicoloSius().toString() ); if (lEvento != null) {
	// * lTreeFasSIUS.add( new TreeModel(lEvento )); // Tenori Ordinanza
	// * if(lEvento.getCodTipoProvvedimento()!= null && lEvento.getCodTipoProvvedimento().equals("02"))
	// * { // Lettura del Deposito Ordinanza. IDepositoOrdinanzaPc lCtrlOrd =
	// * SIUSLookupRemote.getDepositoOrdinanzaPcRemote(); DepositoOrdinanzaPcModel llDepMod =
	// * lCtrlOrd.ExRicercaDepositoOrdinanzaPcByEvento(lEvento.getIdEvento());
	// *
	// * if (llDepMod != null && llDepMod.getIdDepositoOrdinanzaPc() != null) { ITenore lCtrlTen =
	// * SIUSLookupRemote.getTenoreRemote(); lTenori =
	// * lCtrlTen.ExRicercaTenoreByOrdinanza(llDepMod.getIdDepositoOrdinanzaPc()); if (lTenori.size() !=
	// * 0) { Iterator lItxTen = lTenori.iterator(); while (lItxTen.hasNext()) { lTreeFasSIUS.add(new
	// * TreeModel( (TenoreModel) lItxTen.next())); } } } }//endif
	// *
	// * // Tenori Decreto if(lEvento.getCodTipoProvvedimento()!= null &&
	// * lEvento.getCodTipoProvvedimento().equals("03")){ // Lettura del Deposito Ordinanza.
	// * IDepositoDecreto lCtrlDec = SIUSLookupRemote.getDepositoDecretoRemote(); DepositoDecretoModel
	// * llDepModDec = lCtrlDec.ExRicercaDepositoDecretoByIdEvento(lEvento.getIdEvento()); if
	// * (llDepModDec != null && llDepModDec.getIdDepositoDecreto() != null) { ITenore lCtrlTen =
	// * SIUSLookupRemote.getTenoreRemote(); lTenori =
	// * lCtrlTen.ExRicercaTenoreByDecreto(llDepModDec.getIdDepositoDecreto()); if (lTenori.size() != 0)
	// * { Iterator lItxTen = lTenori.iterator(); while (lItxTen.hasNext()) { lTreeFasSIUS.add(new
	// * TreeModel( (TenoreModel) lItxTen.next())); } } } }//endif
	// *
	// * }// end if evento
	// *
	// * } lTreeUdienza.add(lTreeFasSIGE); lTreeRoot.add(lRootUdienza);
	// *
	// * }
	// */
	// }
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.debug("XML : " + lTreeRoot);
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// LogF3B.getLogger()
	// siesLogger.debug("UdienzaProcedimentoController.prelevaDati() : fine");
	//
	// return lTreeRoot;
	// }

	@Override
	public UdienzaProcedimentoSigeModel ExRicercaUdienzaProcedimentoByIdUdienza(BigDecimal idUdienza)
			throws F3BException {
		Connection lConn = null;

		UdienzaProcedimentoSigeSqlDAO lUdiProSqlDao = null;
		try {
			lConn = getDBConnection();
			lUdiProSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			UdienzaProcedimentoSigeModel model = lUdiProSqlDao
					.ricercaUdienzaProcedimentoByIdUdienza(idUdienza);
			return model;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByIdFascicoloSige: "
							+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByIdFascicoloSige: " + e);
		} finally {
			cleanup(lUdiProSqlDao);
			cleanup(lConn);
		}

	}

	@Override
	public BigDecimal countUdienzeByIdFascicolo(BigDecimal idFascicolo) throws F3BException {

		Connection lConn = null;

		UdienzaProcedimentoSigeSqlDAO lUdiProSqlDao = null;
		try {
			lConn = getDBConnection();
			lUdiProSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			BigDecimal numUdienze = lUdiProSqlDao.countUdienzeByIdFascicolo(idFascicolo);
			return numUdienze;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByIdFascicoloSige: "
							+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByIdFascicoloSige: " + e);
		} finally {
			cleanup(lUdiProSqlDao);
			cleanup(lConn);
		}
	}

	@Override
	public void ExModificaUdienzaProcedimento(UdienzaProcedimentoSigeModel model) throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoSigeDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSigeDAO(lConn);
			lUdiDao.setDAOFromModel(model);
			lUdiDao.setCondizioneUpdate(model.getIdUdienzaProcedimentoSige());
			lUdiDao.update();
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExInserisciUdienzaProcedimentoSige: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExInserisciUdienzaProcedimentoSige: " + e);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
	}

	/**
	 * MERGE v10: aggiunto metodo di controllo
	 */
	public BigDecimal contaUdienzeProcedimentoSigeByIdUdienza(BigDecimal idUdienza) throws F3BException {

		Connection lConn = null;

		UdienzaProcedimentoSigeSqlDAO lUdiProSqlDao = null;
		try {
			lConn = getDBConnection();
			lUdiProSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
			BigDecimal numUdienze = lUdiProSqlDao.contaUdienzeProcedimentoSigeByIdUdienza(idUdienza);
			// valore di ritorno
			return numUdienze;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByIdFascicoloSige: "
							+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"UdienzaProcedimentoSigeController.ExRicercaUdienzaProcedimentoByIdFascicoloSige: " + e);
		} finally {
			cleanup(lUdiProSqlDao);
			cleanup(lConn);
		}
	}

	/**
	 * OverLoading del metodo ExRicercaProcedimentixUdienza con l'aggiunta di parametri discriminanti il tipo
	 * Procedimento.
	 *
	 * @param aIdUdienza
	 * @param aOrderBy
	 * @param aStatoProcedimento
	 * @param aTipoProc
	 */
	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentiPerUdienza(BigDecimal aIdUdienza,
			String statoFascicolo, String flagModifBlocco) throws F3BException {
		Connection lConn = null;
		Vector<ProcedimentixUdienzaModel> lProcedimenti = new Vector<>();
		// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
		// variabile introdotta per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
		// prototipo PALERMO)
		Vector<ProcedimentixUdienzaModel> lProcedimentiNoDu = new Vector<>();
		Vector<BigDecimal> vIdFascSige = new Vector<>();

		ProcedimentixUdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);
			lUdiDao.ricercaProcedimentiRuoloByUdienzaOrdinanza(aIdUdienza, null, "ND", null, flagModifBlocco,
					null);
			lUdiDao.start();
			ProcedimentixUdienzaModel lProcedimentoUdienza = null;
			while (lUdiDao.next()) {
				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lUdiDao.getModelEvento();
				lProcedimenti.add(lProcedimentoUdienza);
			}
			lUdiDao.stop();

			// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
			// controllo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018
			// (errore prototipo PALERMO)
			if (lProcedimenti.size() > 0) {

				for (int i = 0; i < lProcedimenti.size(); i++) {
					BigDecimal idfas = lProcedimenti.get(i).getIdFasSIGE();

					if (lProcedimentiNoDu.size() == 0) {
						lProcedimentiNoDu.add(lProcedimenti.get(i));
						vIdFascSige.add(idfas);
					} else {
						if (!vIdFascSige.contains(idfas)) {
							lProcedimentiNoDu.add(lProcedimenti.get(i));
							vIdFascSige.add(idfas);
						}
					}

				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + daoEx);
		} catch (F3BException f3bex) {
			throw f3bex;
		} catch (Exception ex) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		// ritorno il vettore di procedimenti non duplicati
		return lProcedimentiNoDu;
	}

	/**
	 * OverLoading del metodo ExRicercaProcedimentixUdienza con l'aggiunta di parametri discriminanti il tipo
	 * Procedimento.
	 *
	 * @param aIdUdienza
	 * @param aOrderBy
	 * @param aStatoProcedimento
	 * @param aTipoProc
	 */
	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentixUdienzaOrOrdinanza(
			BigDecimal aIdUdienza, String aOrderBy, String aStatoProcedimento, String aTipoProc,
			String flagModifBlocco, String codMAg) throws F3BException {
		Connection lConn = null;
		Vector<ProcedimentixUdienzaModel> lProcedimenti = new Vector<>();
		// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
		// variabile introdotta per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018 (errore
		// prototipo PALERMO)
		Vector<ProcedimentixUdienzaModel> lProcedimentiNoDu = new Vector<>();
		Vector<BigDecimal> vIdFascSige = new Vector<>();

		ProcedimentixUdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);
			lUdiDao.ricercaProcedimentiRuoloByUdienzaOrOrdinanza(aIdUdienza, aOrderBy, aStatoProcedimento,
					aTipoProc, flagModifBlocco, codMAg);
			lUdiDao.start();
			ProcedimentixUdienzaModel lProcedimentoUdienza = null;
			while (lUdiDao.next()) {
				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lUdiDao.getExtendModel();
				lProcedimenti.add(lProcedimentoUdienza);
			}
			lUdiDao.stop();

			// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
			// controllo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018
			// (errore prototipo PALERMO)
			if (lProcedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			} else {

				for (int i = 0; i < lProcedimenti.size(); i++) {
					BigDecimal idfas = lProcedimenti.get(i).getIdFasSIGE();

					if (lProcedimentiNoDu.size() == 0) {
						lProcedimentiNoDu.add(lProcedimenti.get(i));
						vIdFascSige.add(idfas);
					} else {
						if (!vIdFascSige.contains(idfas)) {
							lProcedimentiNoDu.add(lProcedimenti.get(i));
							vIdFascSige.add(idfas);
						}
					}

				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + daoEx);
		} catch (F3BException f3bex) {
			throw f3bex;
		} catch (Exception ex) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		// ritorno il vettore di procedimenti non duplicati
		return lProcedimentiNoDu;
	}

}
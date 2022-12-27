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
 * Title: UdienzaProcedimentoSigeController
 * Description: Classe Controller per UdienzaProcedimento
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
			// ma perchè se cancello fissazione udienza non torna ad iscritto???
			// lFasSigeDAO.setCodStatoFascicolo("02"); // ISCRITTO
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
	 * @return EventoModel
	 *
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
	 * @return EventoModel
	 *
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
		UdienzaProcedimentoSigeDAO lUdiSigeDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiSigeSqlDao = null;
		FascicoloSigeDAO lFasSigeDao = null;
		EventoSqlDAO lEventoSqlDao = null;

		IProvvedimentoSige lProvvCtrl = null;
		ProvvedimentoSigeEventoModel lProvvEvMod = null;

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
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lUdiSigeDao);
			cleanup(lUdiSigeSqlDao);
			cleanup(lFasSigeDao);
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
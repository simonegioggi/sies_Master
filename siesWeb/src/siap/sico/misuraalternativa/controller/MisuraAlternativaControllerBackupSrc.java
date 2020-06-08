package siap.sico.misuraalternativa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;

/*
 07-07-2006 -- DARIO
 classe di backup del controller delle misure alternative, dove si trovano
 i metodi richiamati dalle action le quali non vengono attualmento usate. Le
 action dovranno essere riviste in seguito..........
 */
/**
 * <p>
 * Title: MisuraAlternativaControllerBackupSrc
 * </p>
 * <p>
 * Description: Classe Controller per MisuraAlternativaBackupSrc
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MisuraAlternativaControllerBackupSrc extends SiapController
		implements IMisuraAlternativaBackupSrc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// [FT] - 03/08/2016 - MAC_LOG - Commento la dichiarazione di mLog in favore della variabile siesLogger
	// static Logger mLog = LogF3B.getLogger();

	/**
	 * ExRicercaMisuraAlternativaProrogaUltPeriodoByIdFascicolo - ricerca misura alternativa proroga ulteriore
	 * periodo
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaProrogaUltPeriodoByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaProrogaUltPeriodoByIdFascicolo(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaControllerBackupSrc.ExRicercaMisuraAlternativaProrogaUltPeriodoByIdFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMisuraAlternativaRipristinoDetDomSpecByIdFascicolo - ricerca misura alternativa ripristino det
	 * dom speciale
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaRipristinoDetDomSpecByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaRipristinoDetDomSpecIdFascicolo(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaControllerBackupSrc.ExRicercaMisuraAlternativaRipristinoDetDomSpecByIdFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMisuraAlternativaDicEffAffInProvaByIdFascicolo
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaDicEffAffInProvaByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lMisMod = new MisuraAlternativaModel();

			lMisDao.ricercaMisuraAlternativaDicEffAffInProvaByIdFascicolo(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaControllerBackupSrc.ExRicercaMisuraAlternativaConcessaAffInProvaByIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMAAmmissioneADetDomByIdFascicolo - Ricerca Ammissione DA Detenzione Domiciliare Speciale A
	 * Detenzione Domiciliare
	 *
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMAAmmissioneADetDomByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;
		EventoSqlDAO lEveSqlDAO = null;
		Vector lEveVec;
		EventoModel lEveMod;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lMisMod = new MisuraAlternativaModel();
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoOrdinanzaAmmissionaADetDomIdFascicolo(aIdFascicolo);

			// lEveMod=(EventoModel) lEveSqlDAO.getModels();
			lEveVec = new Vector(lEveSqlDAO.getModels());
			if (lEveVec.size() > 0) {
				lEveMod = (EventoModel) lEveVec.get(0);
				BigDecimal lKeyEvento = lEveMod.getIdEvento();

				lMisDao.ricercaMisuraAlternativaAmmissioneADetDomMAByIdEvento(lKeyEvento);
				lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaControllerBackupSrc.ExRicercaMAAmmissioneADetDomByIdFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lEveSqlDAO);
			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExUpdateValidaMADicEff
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMADicEff(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// SETTA LO STATO PROCEDIMENTO
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0059");

			// Aggiorna tabella nome_provvedimento

			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP050");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;
			// aggiorna le notifiche

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// commit(lConnBlob);

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADicEff : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADicEff : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lNotEveDao);
			cleanup(lNomProvvDAO);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * ExUpdateValidaAmmissioneADetDom
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaAmmissioneADetDom(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();

			// cerca Misura Alternariva by Evento

			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoOrdinanzaAmmissionaADetDomIdFascicolo(aFascicolo.getIdFascicoloSiep());

			EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();

			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaAmmissioneADetDomMAByIdEvento(lEveModelTDS.getIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			// String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;

			String flagcheck = lMisModelOrder.getCodTipoUfficioScarcerazione();

			if (flagcheck.equals("SORV")) {
				lStatoProcMod = "0028";
			} else {
				lStatoProcMod = "0027";
			}
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// ricerca pena residua

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// aggiorna misura alternativa
			lMiDAO = new MisuraAlternativaDAO(lConn);

			lMisSqlDAO.ricercaMisuraAlternativaByIdEventoDataInizio(lEveModel.getIdEvento(), lPosMod);
			MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();
			// se esiste occorrenza con dataInizioPosizioneGiuridica=dataInizioMisura
			// aggiorno la misura
			if (lMisModel != null) {
				lMiDAO.setCondizioneUpdate(lMisModel.getIdMisuraAlternativa());
				lMiDAO.setDataFineMisura(lEveModel.getDataEmissione());
				lMiDAO.update();
				lMiDAO.stop();

			}

			if (lMisModelOrder != null) {
				// se esiste occorrenza data inserimento più alta
				// aggiorno la misura
				lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
				lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione());
				lMiDAO.update();
				lMiDAO.stop();
			}

			// Aggiorna SCADENZARIO fine pena
			if (lMisModelOrder != null && lMisModelOrder.getDataFineMisura() != null)
				InserimentoAggiornamentoScadenzarioFinePena(lConn, lPenResMod, lEveModel,
						lMisModelOrder.getDataFineMisura(), aFascicolo.getIdFascicoloSiep());

			// Aggiorna POSIZIONE_GIURIDICA

			InserimentoAggiornamentoPosizioneGiuridica(lConn, "12", lPosMod, lEveModel.getDataEmissione(),
					lEveModel, aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());

			// Aggiorna tabella nome_provvedimento
			// String lPosizioneGiu = lPosMod.getCodPosizioneGiuridica();

			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP054");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;
			// aggiorna le notifiche

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaAmmissioneADetDom : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaAmmissioneADetDom : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lMisSqlDAO);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * ExUpdateValidaMADetDomSpec
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMADetDomSpec(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		ScadenzarioDAO lScaDao = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDao = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveModel = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// * Cerca L'EVENTO *
			lEveDao = new EventoDAO(lConn);
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
				lEveModel.setDataEmissione(lEveDao.getDataEmissione());
			}

			// POSIZIONE_GIURIDICA
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();
			BigDecimal lKey = null;
			if (lCodPosizione != null && lCodPosizione.equals("03")) {
				lKey = InserimentoAggiornamentoPosizioneGiuridica(lConn, "12", lPosMod,
						lEveApp.getDataEmissione(), lEveModel, aFascicolo.getIdFascicoloSiep(),
						aEvento.getIdEvento());
			}

			// SETTA LO STATO PROCEDIMENTO
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0061");

			// ** NOME PROVVEDIMENTO **
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento("NP051");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ** Cerca la Misura Alternativa
			lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDao.ricercaMisuraAlternativaByKey(lEveApp.getEveIdEvento());
			MisuraAlternativaModel lMisAlt = (MisuraAlternativaModel) lMisSqlDao.getModelByKey();

			// ** Aggiorna SCADENZARIO**
			if (lMisAlt != null && lMisAlt.getDataFineMisura() != null) {
				lScaDao = new ScadenzarioDAO(lConn);
				ScadenzarioModel lScaMod = null;

				lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "09"); // DETENZIONE
																												// DOMICILIARE
																												// SPECIALE
				lScaDao.start();
				if (lScaDao.next())
					lScaMod = (ScadenzarioModel) lScaDao.getModel();
				lScaDao.stop();

				if (lScaMod != null) {
					// aggiorna scadenzario
					lScaDao.setDataFineScadenza(lMisAlt.getDataFineMisura());
					lScaDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(DateUtils.getSysDate());
					lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());

					lScaDao.update();
					lScaDao.stop();
				} else {
					// inserisce scadenzario
					lScaMod = new ScadenzarioModel();
					lScaMod.setCodTipoScadenzario("09"); // DETENZIONE DOMICILIARE SPECIALE
					lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lScaMod.setDataInizioScadenza(lEveApp.getDataEmissione());
					lScaMod.setDataFineScadenza(lMisAlt.getDataFineMisura());
					lScaMod.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
					lScaMod.setDataInserimento(DateUtils.getSysDate());
					lScaMod.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());

					lScaDao.setDAOFromModel(lScaMod);
					lScaDao.insert();
					lScaDao.stop();
				}
			}
			// aggiorna il luogo detenzione
			if (lMisAlt != null && lMisAlt.getDescrLuogoProva() != null) {
				lLuogoDAO = new LuogoDetenzioneDAO(lConn);
				lLuogoDAO.setDataInserimento(aEvento.getDataAggiornamento());
				lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lLuogoDAO.setAltroLuogo(lMisAlt.getDescrLuogoProva());
				if (lKey != null) {
					lLuogoDAO.setCondizioneIdPosizioneGiuridica(lKey);
				} else {
					lLuogoDAO.setCondizioneIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				}
				lLuogoDAO.insert();
				lLuogoDAO.stop();
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);

			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.setFlagStampaSiep("S");
			lEveDaoBlob.setFlagVideoSiep("S");
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// commit(lConnBlob);
			// ---------------------
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADetDomSpec : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADetDomSpec : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lEveDaoBlob);
			cleanup(lScaDao);
			cleanup(lMisSqlDao);
			cleanup(lLuogoDAO);

			// cleanup(lConnBlob);
			cleanup(lConn);
		}

		return lEveModel;
	}

	/**
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMADetDomSpecSospProvv(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDao = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveModel = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// * Cerca L'EVENTO *
			lEveDao = new EventoDAO(lConn);

			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
				lEveModel.setDataEmissione(lEveDao.getDataEmissione());
			}

			// POSIZIONE_GIURIDICA
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			if (lCodPosizione != null && lCodPosizione.equals("12")) {
				InserimentoAggiornamentoPosizioneGiuridica(lConn, "03", lPosMod, lEveApp.getDataEmissione(),
						lEveModel, aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());
			}
			/********************************************
			 * FINE POSIZIONE GIURIDICA
			 ***********************************************/

			// SETTA LO STATO PROCEDIMENTO
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0034");

			// ** NOME PROVVEDIMENTO **
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento("NP033");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ** Cerca la Misura Alternativa
			lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDao.ricercaMisuraAlternativaByKey(lEveApp.getEveIdEvento());
			/* MisuraAlternativaModel lMisAlt = (MisuraAlternativaModel) */lMisSqlDao.getModelByKey();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.setFlagStampaSiep("S");
			lEveDaoBlob.setFlagVideoSiep("S");
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// commit(lConnBlob);
			// ---------------------
			commit(lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"MisuraAternativaController.ExUpdateValidaMADetDomSpecSospProvv : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADetDomSpecSospProvv : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lEveDaoBlob);
			cleanup(lMisSqlDao);

			// cleanup(lConnBlob);
			cleanup(lConn);
		}

		return lEveModel;
	}

	/**
	 * ExUpdateValidaMAProrogaUltPeriodo
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMAProrogaUltPeriodo(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		ScadenzarioDAO lScaDao = null;
		MisuraAlternativaSqlDAO lMisSqlDao = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveModel = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// * Cerca L'EVENTO *
			lEveDao = new EventoDAO(lConn);

			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
				lEveModel.setDataEmissione(lEveDao.getDataEmissione());
			}

			// SETTA LO STATO PROCEDIMENTO
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0061");

			// ** NOME PROVVEDIMENTO **
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento("NP052");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ** Cerca la Misura Alternativa
			lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDao.ricercaMisuraAlternativaByKey(lEveApp.getEveIdEvento());
			MisuraAlternativaModel lMisAlt = (MisuraAlternativaModel) lMisSqlDao.getModelByKey();

			// ** Aggiorna SCADENZARIO**
			if (lMisAlt != null && lMisAlt.getDataFineMisura() != null) {
				lScaDao = new ScadenzarioDAO(lConn);
				ScadenzarioModel lScaMod = null;

				lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "09"); // DETENZIONE
																												// DOMICILIARE
																												// SPECIALE
				lScaDao.start();
				if (lScaDao.next()) {
					lScaMod = (ScadenzarioModel) lScaDao.getModel();
				}
				lScaDao.stop();

				if (lScaMod != null) {
					// aggiorna scadenzario
					lScaDao.setDataFineScadenza(lMisAlt.getDataFineMisura());
					lScaDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(DateUtils.getSysDate());
					lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());

					lScaDao.update();
					lScaDao.stop();
				} else {
					// inserisce scadenzario
					lScaMod = new ScadenzarioModel();
					lScaMod.setCodTipoScadenzario("09"); // DETENZIONE DOMICILIARE SPECIALE
					lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lScaMod.setDataInizioScadenza(lEveApp.getDataEmissione());
					lScaMod.setDataFineScadenza(lMisAlt.getDataFineMisura());
					lScaMod.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
					lScaMod.setDataInserimento(DateUtils.getSysDate());
					lScaMod.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());

					lScaDao.setDAOFromModel(lScaMod);
					lScaDao.insert();
					lScaDao.stop();
				}
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);

			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.setFlagStampaSiep("S");
			lEveDaoBlob.setFlagVideoSiep("S");

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// commit(lConnBlob);
			// ---------------------
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMAProrogaUltPeriodo : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMAProrogaUltPeriodo : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNomProvvDAO);
			cleanup(lEveDaoBlob);
			cleanup(lScaDao);
			cleanup(lMisSqlDao);

			// cleanup(lConnBlob);
			cleanup(lConn);
		}

		return lEveModel;
	}

	// ricerca misura alternativa con codDecisione = CO concessa affidamento in prova
	/**
	 * ExRicercaMisuraAlternativaDetDomSpeAmmAffByIdFascicolo ricerca misura alternativa con codDecisione = CO
	 * concessa affidamento in prova
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaDetDomSpeAmmAffByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;
		EventoSqlDAO lEveSqlDAO = null;
		// Vector lEveVec;
		// EventoModel lEveMod;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lMisMod = new MisuraAlternativaModel();

			lMisDao.ricercaMisuraAlternativaDetDomSpeAmmAffByIdFascicolo(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaControllerBackupSrc.ExRicercaMisuraAlternativaConcessaAffInProvaByIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lEveSqlDAO);
			cleanup(lConn);
		}

		return lMisMod;
	}

	/**
	 * ExUpdateValidaMADetDomSpeAmmAff
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param tipoMisura
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMADetDomSpeAmmAff(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String tipoMisura) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDaoBlob = null;
		// Connection lConnBlob = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		EventoModel lEveModelMDS = null;
		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// cerca Ordinanza Ripristino by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaEventoDetDomSpeAmmAffByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lMisSqlDAO.start();
			while (lMisSqlDAO.next()) {
				lEveModelMDS = (EventoModel) lMisSqlDAO.getModelEvento();
			}
			lMisSqlDAO.stop();
			// ordinanza di ripristino
			lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModelMDS.getIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			// String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;
			if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC"))
				lStatoProcMod = "0064";
			if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV"))
				lStatoProcMod = "0024";

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// ricerca pena residua

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			/* PenaResiduaModel lPenResMod = (PenaResiduaModel) */lPenResSqlDao.getModelByKey();

			// aggiorna misura alternativa
			lMiDAO = new MisuraAlternativaDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEventoDataInizio(lEveModel.getIdEvento(), lPosMod);
			MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();
			// se esiste occorrenza con dataInizioPosizioneGiuridica=dataInizioMisura
			// aggiorno la misura
			if (lMisModel != null) {
				lMiDAO.setCondizioneUpdate(lMisModel.getIdMisuraAlternativa());
				lMiDAO.setDataFineMisura(lEveModel.getDataEmissione());
				lMiDAO.update();
				lMiDAO.stop();

			}

			if (lMisModelOrder != null) {
				// se esiste occorrenza data inserimento più alta
				// aggiorno la misura
				lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
				lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione());
				lMiDAO.update();
				lMiDAO.stop();

			}

			// ** Aggiorna SCADENZARIO**
			if (lMisModelOrder != null && lMisModelOrder.getFlagCheck().equals("S")) {
				lScaDao = new ScadenzarioDAO(lConn);
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);
				ScadenzarioModel lScaMod = null;

				lScaSqlDao.ricercaScadenzarioByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				if (lScaMod != null) {
					if (lScaMod.getCodTipoScadenzario().equals("09")) {
						// cancello scadenzario
						lScaDao.setCondizioneForDelete(lScaMod.getIdScadenzario());
						lScaDao.delete();
						lScaDao.stop();
					}
				}
			}

			// Aggiorna POSIZIONE_GIURIDICA
			BigDecimal lKey = null;
			if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {

				lKey = InserimentoAggiornamentoPosizioneGiuridica(lConn, "13", lPosMod,
						lEveModel.getDataEmissione(), lEveModel, aFascicolo.getIdFascicoloSiep(),
						aEvento.getIdEvento());
			}

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP053");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;
			// aggiorna il luogo detenzione
			if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {
				lLuogoDAO = new LuogoDetenzioneDAO(lConn);
				lLuogoDAO.setDataInserimento(aEvento.getDataAggiornamento());
				lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lLuogoDAO.setAltroLuogo(lMisModelOrder.getDescrLuogoProva());
				if (lKey != null) {
					lLuogoDAO.setCondizioneIdPosizioneGiuridica(lKey);
				} else {
					lLuogoDAO.setCondizioneIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				}
				lLuogoDAO.insert();
				lLuogoDAO.stop();
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADetDomSpeAmmAff : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADetDomSpeAmmAff : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lLuogoDAO);
			cleanup(lMisSqlDAO);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * valida ripristino det dom spec
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMARipristinoDetDomSpec(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDao = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveModel = new EventoModel(aEvento);
		try {
			lConn = getDBTransaction();

			// * Cerca L'EVENTO *
			lEveDao = new EventoDAO(lConn);
			EventoModel lEveApp = new EventoModel();
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
				lEveModel.setDataEmissione(lEveDao.getDataEmissione());
			}

			// misura alternativa
			lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDao
					.ricercaMisuraAlternativaRipristinoDetDomSpecIdFascicolo(aFascicolo.getIdFascicoloSiep());
			MisuraAlternativaModel lMisAlt = (MisuraAlternativaModel) lMisSqlDao.getModelByKey();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;
			if (lMisAlt.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lStatoProcMod = "0066";
			} else {
				lStatoProcMod = "0067";
			}
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// ** NOME PROVVEDIMENTO **
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento("NP055");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// aggiorna il luogo detenzione
			if (lMisAlt != null && lMisAlt.getDescrLuogoProva() != null) {
				lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);

				lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				PosizioneGiuridicaModel lPosModCorr = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();

				lLuogoDAO = new LuogoDetenzioneDAO(lConn);
				lLuogoDAO.setDataInserimento(aEvento.getDataAggiornamento());
				lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lLuogoDAO.setAltroLuogo(lMisAlt.getDescrLuogoProva());
				lLuogoDAO.setCondizioneIdPosizioneGiuridica(lPosModCorr.getIdPosizioneGiuridica());
				lLuogoDAO.insert();
				lLuogoDAO.stop();
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();
			lEveDaoBlob = new EventoDAO(lConn);

			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.setFlagStampaSiep("S");
			lEveDaoBlob.setFlagVideoSiep("S");

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// commit(lConnBlob);
			// ---------------------
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"MisuraAternativaController.ExUpdateValidaMARipristinoDetDomSpec : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMARipristinoDetDomSpec : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lNomProvvDAO);
			cleanup(lEveDaoBlob);
			cleanup(lMisSqlDao);
			cleanup(lLuogoDAO);
			cleanup(lPosSqlDAO);

			cleanup(lConn);
		}

		return lEveModel;
	}

	// upload Ulteriore Periodo Misura alternativa
	public EventoModel ExUpdateValidaUlteriorePeriodoMA(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// SETTA LO STATO PROCEDIMENTO
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0026");

			// ricerca pena residua
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// ** Aggiorna SCADENZARIO**
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			ScadenzarioModel lScaMod = null;

			lScaSqlDao.ricercaScadenzarioByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
			if (lScaMod != null) {
				if (lScaMod.getCodTipoScadenzario().equals("09")) {
					// aggiorna scadenzario
					lScaDao.setDataFineScadenza(lMisModelOrder.getDataFineMisura());
					lScaDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(DateUtils.getSysDate());
					// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
					lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDao.update();
					lScaDao.stop();
				}
			}

			// Aggiorna tabella nome_provvedimento

			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			if (lMisModelOrder.getCodTipoMisura().equals("0101")
					&& lMisModelOrder.getDataFineMisura() == lPenResMod.getDataFine()) {
				lNomProvvDAO.setCodNomeProvvedimento("NP069");

			}
			if (lMisModelOrder.getCodTipoMisura().equals("0101")
					&& lMisModelOrder.getDataFineMisura() != lPenResMod.getDataFine()) {
				lNomProvvDAO.setCodNomeProvvedimento("NP070");

			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;

			// aggiorna il luogo detenzione
			if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {

				lLuogoDAO = new LuogoDetenzioneDAO(lConn);
				lLuogoDAO.setDataInserimento(aEvento.getDataAggiornamento());
				lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lLuogoDAO.setAltroLuogo(lMisModelOrder.getDescrLuogoProva());
				lLuogoDAO.setCondizioneIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				lLuogoDAO.insert();
				lLuogoDAO.stop();
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaUlteriorePeriodoMA : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaUlteriorePeriodoMA : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lLuogoDAO);
			cleanup(lMisSqlDAO);

			cleanup(lConn);

			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	// upload concessione liberazione condizionale
	public EventoModel ExUpdateValidaMACoLibCond(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			PosizioneGiuridicaModel aPosizione) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		LuogoDetenzioneDAO lLuogoDAO = null;

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			BigDecimal lKey = InserimentoAggiornamentoPosizioneGiuridica(lConn,
					aPosizione.getCodPosizioneGiuridica(), lPosMod, lEveModel.getDataEmissione(), lEveModel,
					aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());

			// SETTA LO STATO PROCEDIMENTO
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0070");

			// ricerca pena residua

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// aggiorna misura alternativa
			lMiDAO = new MisuraAlternativaDAO(lConn);
			if (lCodPosizione.equals("12") || lCodPosizione.equals("13") || lCodPosizione.equals("14")) {
				if (lMisModelOrder != null
						&& lPosMod.getDataInizio() == lMisModelOrder.getDataInizioMisura()) {
					// se esiste occorrenza corrente
					// aggiorno la misura
					lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
					lMiDAO.setDataFineMisura(lEveModel.getDataEmissione());
					lMiDAO.update();
					lMiDAO.stop();

				}
				if (lMisModelOrder != null) {
					lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
					lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione());
					if (lPenResMod != null && lPenResMod.getDataFine() != null) {
						lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
					} else {

						lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdFascicolo(
								aFascicolo.getIdFascicoloSiep());
						Vector lMisuraPrec = new Vector(lMisSqlDAO.getModels());
						MisuraAlternativaModel lMisModPrec = null;

						if (lMisuraPrec.size() > 1) {

							lMisModPrec = (MisuraAlternativaModel) lMisuraPrec.get(1);
							lMiDAO.setDataFineMisura(lMisModPrec.getDataFineMisura());

						}
					}
					lMiDAO.update();
					lMiDAO.stop();

				}
			}

			if (lCodPosizione.equals("03")) {
				lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());

				lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione());
				lMiDAO.setDataFineMisura(lPenResMod.getDataFine());

				lMiDAO.update();
				lMiDAO.stop();

			}
			// ** Aggiorna SCADENZARIO**
			if (lMisModelOrder != null && lMisModelOrder.getDataFineMisura() != null) {
				Date lData = lMisModelOrder.getDataFineMisura();
				InserimentoAggiornamentoScadenzarioFinePena(lConn, lPenResMod, lEveModel, lData,
						aFascicolo.getIdFascicoloSiep());
			}

			// Aggiorna tabella nome_provvedimento

			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP067");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;
			// aggiorna il luogo detenzione
			if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {

				lLuogoDAO = new LuogoDetenzioneDAO(lConn);
				lLuogoDAO.setDataInserimento(aEvento.getDataAggiornamento());
				lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lLuogoDAO.setAltroLuogo(lMisModelOrder.getDescrLuogoProva());
				lLuogoDAO.setCondizioneIdPosizioneGiuridica(lKey);
				lLuogoDAO.insert();
				lLuogoDAO.stop();
			}

			// aggiorna le notifiche

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMACoLibCond : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMACoLibCond : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lLuogoDAO);
			cleanup(lMisSqlDAO);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// LIBERAZIONE CONDIZIONALE
	public MisuraAlternativaModel ExRicercaMisuraAlternativaConcessioneLibCondByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;
		EventoSqlDAO lEveSqlDAO = null;
		// Vector lEveVec;
		// Vector lEve;
		// EventoModel lEveMod;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lMisMod = new MisuraAlternativaModel();
			lMisDao.ricercaMisuraAlternativaConcessioneLibCondByIdFascicolo(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaControllerBackupSrc.ExRicercaMisuraAlternativaSosProvAffInProvByIdFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lEveSqlDAO);
			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExUpdateValidaMAReLibCond
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMAReLibCond(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		LuogoDetenzioneDAO lLuogoDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();
			// String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// SETTA LO STATO PROCEDIMENTO
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0071");

			// Aggiorna POSIZIONE_GIURIDICA
			Date lData = lEveModel.getDataEmissione();
			InserimentoAggiornamentoPosizioneGiuridica(lConn, "03", lPosMod, lData, lEveModel,
					aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP068");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;

			// aggiorna il luogo detenzione
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);

			lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {
				lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

				PosizioneGiuridicaModel lPosModCorr = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();

				lLuogoDAO = new LuogoDetenzioneDAO(lConn);
				lLuogoDAO.setDataInserimento(aEvento.getDataAggiornamento());
				lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lLuogoDAO.setAltroLuogo(lMisModelOrder.getDescrLuogoProva());
				lLuogoDAO.setCondizioneIdPosizioneGiuridica(lPosModCorr.getIdPosizioneGiuridica());
				lLuogoDAO.insert();
				lLuogoDAO.stop();
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMAReLibCond : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaController.ExUpdateValidaMAReLibCond : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lNotEveDao);
			cleanup(lNomProvvDAO);
			cleanup(lPosSqlDAO);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lMisSqlDAO);
			cleanup(lLuogoDAO);
		}

		return lEveMod;
	}

	// METODI PRIVATI
	// Aggiorna/inserisce POSIZIONE_GIURIDICA
	/**
	 * InserimentoAggiornamentoPosizioneGiuridica
	 *
	 * @param lConn
	 * @param lPosizione
	 * @param lPos
	 * @param lData
	 * @param lEveModel
	 * @param aKeyFasc
	 * @param aKeyEve
	 * @return
	 * @throws DAOException
	 * @throws F3BException
	 */
	private BigDecimal InserimentoAggiornamentoPosizioneGiuridica(Connection lConn, String lPosizione,
			PosizioneGiuridicaModel lPos, Date lData, EventoModel lEveModel, BigDecimal aKeyFasc,
			BigDecimal aKeyEve) throws DAOException, F3BException {

		PosizioneGiuridicaDAO lPosDao = null;
		BigDecimal lIdPosizioneGiuridica = null;

		try {

			lPosDao = new PosizioneGiuridicaDAO(lConn);

			if (lPos != null && lPos.getDataFine() == null) {
				lPosDao.setDataFine(lData);
				lPosDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataAggiornamento(lEveModel.getDataAggiornamento());
				lPosDao.setCondizioneUpdate(lPos.getIdPosizioneGiuridica());
				lPosDao.update();
				lPosDao.stop();
			}
			if (lPosizione != null) {
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setDataInizio(lData);
				lPosDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(lEveModel.getDataAggiornamento());
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setFasSieIdFascicoloSiep(aKeyFasc);
				lPosDao.setIdEventoRiferimento(aKeyEve);

				lIdPosizioneGiuridica = lPosDao.insert();
				lPosDao.stop();
			}
		} finally {
			// sca
			cleanup(lPosDao);
		}

		return lIdPosizioneGiuridica;
	}

	/**
	 * InserimentoAggiornamentoScadenzarioFinePena
	 *
	 * @param lConn
	 * @param lPenResMod
	 * @param lEveModel
	 * @param lData
	 * @param aKey
	 * @throws DAOException
	 * @throws F3BException
	 */
	private void InserimentoAggiornamentoScadenzarioFinePena(Connection lConn, PenaResiduaModel lPenResMod,
			EventoModel lEveModel, Date lData, BigDecimal aKey) throws DAOException, F3BException {

		ScadenzarioDAO lScaDao = new ScadenzarioDAO(lConn);
		ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);
		ScadenzarioModel lScaMod = null;
		try {
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02", aKey);
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null) {
				lScaDao.setDataFineScadenza(lData);
				lScaDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lScaDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
				lScaDao.stop();
			} else {
				// inserisce scadenzario
				ScadenzarioModel lScaModel = new ScadenzarioModel();
				lScaModel.setCodTipoScadenzario("02");
				lScaModel.setDataInizioScadenza(lPenResMod.getDataInizio());
				lScaModel.setDataFineScadenza(lData);

				lScaModel.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lScaModel.setDataInserimento(DateUtils.getSysDate());
				lScaModel.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lScaModel.setFasSieIdFascicoloSiep(aKey);

				lScaDao.setDAOFromModel(lScaModel);
				lScaDao.insert();
				lScaDao.stop();

			}
		} finally {
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
		}
	}

	/**
	 * InserimentoCancellazioneStatoProcedimento
	 *
	 * @param lConn
	 * @param aKey
	 * @param lEveModel
	 * @param lStatoProcMod
	 * @throws DAOException
	 * @throws F3BException
	 */
	private void InserimentoCancellazioneStatoProcedimento(Connection lConn, BigDecimal aKey,
			EventoModel lEveModel, String lStatoProcMod) throws DAOException, F3BException {

		StatoProcedimentoDAO lStatoDao = new StatoProcedimentoDAO(lConn);

		try {
			// cancellazione
			lStatoDao.setCondizioneByIdFascicolo(aKey);
			lStatoDao.delete();
			lStatoDao.stop();
			// inserimento
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(lEveModel.getDataEmissione());
			lStatoDao.setCodStatoProcedimento(lStatoProcMod);
			lStatoDao.setFasSieIdFascicoloSiep(aKey);
			lStatoDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
			lStatoDao.setDataInserimento(lEveModel.getDataAggiornamento());
			lStatoDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
			lStatoDao.insert();
		} finally {
			cleanup(lStatoDao);
		}
	}

}
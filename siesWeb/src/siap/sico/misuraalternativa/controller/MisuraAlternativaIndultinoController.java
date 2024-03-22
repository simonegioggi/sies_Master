package siap.sico.misuraalternativa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSimeoneSqlDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.dao.EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO;
import siap.sico.evento.dao.EventoStoreProcedureAggiornaScadenzarioSimeoneDAO;
import siap.sico.evento.dao.EventoStoreProcedurePulisciDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: MisuraAlternativaController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraAlternativa
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
public class MisuraAlternativaIndultinoController extends SiapController
		implements IMisuraAlternativaIndultino {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MisuraAlternativaModel ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
			BigDecimal aKey, String[] aNatura, String[] aTipoMisura, String[] aDecisione)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lMisMod = new MisuraAlternativaModel();

			lMisDao.ricercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisioneOrderDescData(aKey, aNatura,
					aTipoMisura, aDecisione);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraAternativaIndultinoController.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	public MisuraAlternativaModel ExRicercaMisuraAlternativaByIdFascicoloNaturaDecisione(BigDecimal aKey,
			String aNatura) throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lMisMod = new MisuraAlternativaModel();

			lMisDao.ricercaMisuraAlternativaByIdFascicoloNaturaDecisione(aKey, aNatura);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraAternativaIndultinoController.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	public MisuraAlternativaModel ExRicercaMisuraAlternativaPrecedenteByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;

		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		Vector lMisura = new Vector();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lMisDao.ricercaMisuraAlternativaByIdFascicolo(aKey);

			lMisura = new Vector(lMisDao.getModels());

			if (lMisura.size() > 1) {

				lMisMod = (MisuraAlternativaModel) lMisura.get(1);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraAlternativaIndultinoController.ExRicercaMisuraAlternativaPrecedenteByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	public EventoModel ExUpdateValidaMARipristinoIndultino(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		// EventoModel lEveModelMDS = null;
		EventoDAO lEveDaoMisAlt = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// misura alternativa
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelMis = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelMis != null && (lEveModelMis.getFlagDocumentoRegistrato() == null
						|| lEveModelMis.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelMis.setFlagDocumentoRegistrato("S");
					lEveModelMis.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelMis.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelMis.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelMis);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;
			if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lStatoProcMod = "0090";
			} else if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lStatoProcMod = "0091";
			}
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// Aggiorna Inserisci PENA_RESIDUA
			PenaResiduaModel lPenRes = null;
			lPenRes = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), null);

			// scadenzario MISURA ALTERNATIVA
			Date lData = null;

			if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null) {
				if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
					lData = lEveModel.getDataEmissione();
				}
				if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
					lData = lMisModelOrder.getDataScarcerazione();
				}
			}

			// aggiorna/inserisci scadenzario MISURA ALTERNATIVA
			if (lData != null)
				InserimentoAggiornamentoScadenzarioMisuraAlternativa(lConn, lData, lPenRes, lEveModel,
						aFascicolo.getIdFascicoloSiep());

			// Aggiorna POSIZIONE_GIURIDICA
			if (lCodPosizione.equals("35")) {
				InserimentoAggiornamentoPosizioneGiuridica(lConn, "27", lPosMod, lData, lEveModel,
						aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());
			}

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP109");
			} else if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP110");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;

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
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaMARipristinoIndultino : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaMARipristinoIndultino : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNotEveDao);
			cleanup(lNomProvvDAO);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	public EventoModel ExUpdateValidaMAProsecuzione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		EventoDAO lEveDaoMisAlt = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// Cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelTDS != null && (lEveModelTDS.getFlagDocumentoRegistrato() == null
						|| lEveModelTDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelTDS.setFlagDocumentoRegistrato("S");
					lEveModelTDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelTDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelTDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelTDS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();

			// Aggiorna Inserisci PENA_RESIDUA
			PenaResiduaModel lPenResMod = null;
			lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), "S");

			// SETTA LO STATO PROCEDIMENTO

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0096");

			// aggiorna misura alternativa
			lMiDAO = new MisuraAlternativaDAO(lConn);

			if (lMisModelOrder != null) {
				lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
				lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
				lMiDAO.update();
				lMiDAO.stop();
			}

			// Aggiorna SCADENZARIO FINE PENA
			ScadenzarioModel lScaMod = null;
			if (lPenResMod != null && lPenResMod.getDataInizio() != null
					&& lPenResMod.getDataFine() != null) {
				// Per tipo misura "AFFIDAMENTO" e "AFFIDAMENTOCUMULO"
				if (lMisModelOrder.getCodTipoMisura().equals("2205")
						|| lMisModelOrder.getCodTipoMisura().equals("2281")
						|| lMisModelOrder.getCodTipoMisura().equals("2282"))
				// if (atipoMisura != null && (atipoMisura.equals("AFFIDAMENTO") ||
				// atipoMisura.equals("AFFIDAMENTOCUMULO")))
				{
					InserimentoAggiornamentoScadenzarioFinePena(lConn, lPenResMod, lEveModel,
							aFascicolo.getIdFascicoloSiep());
				}
			}

			// CANCELLA SCADENZARIO MISURA ALTERNATIVA
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13", aFascicolo.getIdFascicoloSiep());
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null) {
				lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
				lScaDao.delete();
				lScaDao.stop();
			}

			// Aggiorna/inserisce POSIZIONE_GIURIDICA
			String lPosizione = null;

			if (lMisModelOrder != null && lMisModelOrder.getCodTipoMisura() != null) {
				String lMotivo = lMisModelOrder.getCodTipoMisura();
				if (lMotivo.equals("2205") || lMotivo.equals("2281") || lMotivo.equals("2282")) {
					lPosizione = "42";
				}
				if (lMotivo.equals("2283")) {
					lPosizione = "43";
				}
				if (lMotivo.equals("2284") || lMotivo.equals("2285") || lMotivo.equals("2287")
						|| lMotivo.equals("2288")) {
					lPosizione = "41";
				}
				if (lMotivo.equals("2286")) {
					lPosizione = "44";
				}
			}

			InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod,
					lEveModel.getDataEmissione(), lEveModel, aFascicolo.getIdFascicoloSiep(),
					aEvento.getIdEvento());

			// Aggiorna tabella nome_provvedimento

			/* String lPosizioneGiu = */lPosMod.getCodPosizioneGiuridica();
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			// Per tipo misura "AFFIDAMENTO" e "AFFIDAMENTOCUMULO"
			if (lMisModelOrder.getCodTipoMisura().equals("2205")
					|| lMisModelOrder.getCodTipoMisura().equals("2281")
					|| lMisModelOrder.getCodTipoMisura().equals("2282")) {
				// if (atipoMisura != null && (atipoMisura.equals("AFFIDAMENTO") ||
				// atipoMisura.equals("AFFIDAMENTOCUMULO")))
				lNomProvvDAO.setCodNomeProvvedimento("NP116");
			}
			// Per tipo misura "DETENZIONE" e "DETENZIONECUMULO"
			else if (lMisModelOrder.getCodTipoMisura().equals("2284")
					|| lMisModelOrder.getCodTipoMisura().equals("2285")
					|| lMisModelOrder.getCodTipoMisura().equals("2286")
					|| lMisModelOrder.getCodTipoMisura().equals("2287")
					|| lMisModelOrder.getCodTipoMisura().equals("2288")) {
				// else if (atipoMisura != null && (atipoMisura.equals("DETENZIONE") ||
				// atipoMisura.equals("DETENZIONECUMULO")))
				lNomProvvDAO.setCodNomeProvvedimento("NP117");
			}
			// Per tipo misura "SEMILIBERTA" e "SEMILIBERTACUMULO"
			else if (lMisModelOrder.getCodTipoMisura().equals("2283")) {
				// else if (atipoMisura != null && (atipoMisura.equals("SEMILIBERTA") ||
				// atipoMisura.equals("SEMILIBERTACUMULO")))
				lNomProvvDAO.setCodNomeProvvedimento("NP118");
			} else {
				lNomProvvDAO.setCodNomeProvvedimento("NP119");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaMAProsecuzione : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaMAProsecuzione : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDAO);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Valida l'evento a seguito della concessione della Proseczione della misura disposta dal MDS o dal TDS
	 * secondo le nuove disposizioni del DL 146/2013
	 *
	 * @param aEvento
	 *            - Model con valorizzati solo ID e "dati Aggiornamento" + Blob
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 * @since DL 146/2013
	 */
	public EventoModel ExUpdateValidaMAProsecuzione51Bis(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		EventoDAO lEveDaoMisAlt = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// Recupero l'EVENTO. Quello passato in input è un model incompleto
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// n.b. utili solo per passare i dati dell'aggiornamento alla funzioni richiamate
			lEveModel.setDataAggiornamento(aEvento.getDataAggiornamento());
			lEveModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

			// Cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// ========================================================================
			// Valido l'evento SIUS riferito alla misura alternativa (se non validato)
			// ========================================================================
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelSIUS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelSIUS != null && (lEveModelSIUS.getFlagDocumentoRegistrato() == null
						|| lEveModelSIUS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelSIUS.setFlagDocumentoRegistrato("S");
					lEveModelSIUS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelSIUS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelSIUS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelSIUS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// ========================================================================
			// Aggiorno e valido PENA_RESIDUA se rideterminata
			// ========================================================================
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			PenaResiduaModel lPenResMod = null;
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveModel.getIdEvento());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod == null) {
				// Se la pena non è stata agganciata all'evento (non calcolata) verifico
				// se presente una PR non validata. In caso affermativo la aggancio e valido.
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());
				lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
				if (lPenResMod != null && "S".equals(lPenResMod.getFlagValidato()))
					lPenResMod = null;
			}

			if (lPenResMod != null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
				lPenResDao.setFlagValidato("S");

				lPenResDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());

				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			}

			// lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, aEvento,
			// aFascicolo.getIdFascicoloSiep(), "S");

			// ======================================
			// Modifica LO STATO PROCEDIMENTO
			// ======================================
			List<String> lCodiciMDS = Arrays.asList("5470", "5471", "5472", "5473", "5474", "5475", "5476",
					"5477", "5478", "5479");
			List<String> lCodiciTDS = Arrays.asList("5480", "5481", "5482", "5483", "5484", "5485", "5486",
					"5487", "5489", "5490");

			List<String> lCodiciTDSAffi = Arrays.asList("5480", "5481", "5482");
			List<String> lCodiciTDSDetDom = Arrays.asList("5483", "5484", "5485", "5486");

			boolean isDispostaTDS = false;
			String lCodStatoProcedimento = null;
			if (lCodiciMDS.contains(lEveModel.getCodMotivo())) {
				isDispostaTDS = false;
				lCodStatoProcedimento = "0438"; // Prosecuzione della Misura in Corso di espiazione ex art. 51
												// bis legge 21.02.2014 N.10
			} else if (lCodiciTDS.contains(lEveModel.getCodMotivo())) {
				isDispostaTDS = true;
				// Prosecuzione disposta dal TDS
				if (lCodiciTDSAffi.contains(lEveModel.getCodMotivo())) {
					if ("12".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0439"; // Prosecuzione Affidamento in Prova - Emessa
														// Comunicazione Scadenza Misura
					else if ("09".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0440"; // Prosecuzione Affidamento in Prova - Emesso Ordine
														// Esecuzione con Scadenza Misura
				} else if (lCodiciTDSDetDom.contains(lEveModel.getCodMotivo())) {
					if ("12".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0441"; // Prosecuzione Detenzione Domiciliare - Emessa
														// Comunicazione Scadenza Misura
					else if ("09".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0442"; // Prosecuzione Detenzione Domiciliare - Emesso Ordine
														// Esecuzione con Scadenza Misura
				} else if ("5487".equals(lEveModel.getCodMotivo())) {
					if ("12".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0443"; // Prosecuzione Semiliberta' - Emessa Comunicazione
														// Scadenza Misura
					else if ("09".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0444"; // Prosecuzione Semiliberta' - Emesso Ordine
														// Esecuzione con Scadenza Misura
				} else if ("5490".equals(lEveModel.getCodMotivo())) {
					if ("12".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0445"; // Prosecuzione Differimento Nella Norme della
														// Detenzione Domiciliare - Emessa Comunicazione
														// Scadenza Misura
					else if ("09".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0446"; // Prosecuzione Differimento Nella Norme della
														// Detenzione Domiciliare - Emesso Ordine di
														// Esecuzione con Scadenza Misura
				} else if ("5489".equals(lEveModel.getCodMotivo())) {
					if ("12".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0447"; // Prosecuzione Esecuzione Pena presso Domicilio -
														// Emessa Comunicazione Scadenza Misura
					else if ("09".equals(lEveModel.getCodTipoProvvedimento()))
						lCodStatoProcedimento = "0448"; // Prosecuzione Esecuzione Pena presso Domicilio -
														// Emesso Ordine Esecuzione con Scadenza Misura
				}
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("Impossibile determinare lo stato procedimento per l'evento con cod Motivo = "
						+ lEveModel.getCodMotivo());
			}

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lCodStatoProcedimento);

			// ========================================================================
			// In caso di prosecuzione disposta dal TDS determino l'inizio Misura
			// = data scarcerazine se indicata oppure data emissione provvedimento
			// ========================================================================
			lMiDAO = new MisuraAlternativaDAO(lConn);
			Date lDataInizioMisura = null;
			if (isDispostaTDS) {
				if (lMisModelOrder.getDataScarcerazione() != null) {
					lDataInizioMisura = lMisModelOrder.getDataScarcerazione();
				} else {
					lDataInizioMisura = lEveModel.getDataEmissione();
				}

				lMisModelOrder.setDataInizioMisura(lDataInizioMisura);

				lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
				lMiDAO.setDataInizioMisura(lDataInizioMisura);
				lMiDAO.update();
				lMiDAO.stop();
			}

			// Aggiorna la Data Fine Misura su Misura alternativa impostandola al fine
			// pena solo nel caso di Prosecuzione
			// Escludo il Differimente nelle forme della detenzione domiciliare
			// TDS (5490) e MDS(5479) in quanto il fine misura non coincide con il
			// fine pena ma è stato già indicato dell'utente in fase di inserimento
			// dell'ordinenza/decreto
			// TODO verificare inizio misura
			if ("5490".equals(lEveModel.getCodMotivo()) || "5479".equals(lEveModel.getCodMotivo())) {
				// Detenzione Domiciliare nelle forme del Differimento, il fine misura
				// non coincide con il fine pena essendo una misura a termine.
				// Il fine misura è stato già inserito in fase di inserimento del
				// provvedimento direttamente dall'utente o calcolato
				// a partire dalla durata della misura.
			} else {
				//
				// lMiDAO = new MisuraAlternativaDAO(lConn);
				if (lMisModelOrder != null && lPenResMod != null) {
					lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
					lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
					lMiDAO.update();
					lMiDAO.stop();
				}
			}

			// Aggiorna SCADENZARIO FINE PENA
			ScadenzarioModel lScaMod = null;
			if (lPenResMod != null && lPenResMod.getDataInizio() != null
					&& lPenResMod.getDataFine() != null) {
				// Per tipo misura "AFFIDAMENTO IN PROVA" (con o senza cumulo)
				// FIXME DL 146/2013 verificare perchè lo scadenzario FP va aggiornato solo se in affidamento
				if (lEveModel.getCodMotivo().equals("5470") || lEveModel.getCodMotivo().equals("5471")
						|| lEveModel.getCodMotivo().equals("5472")) {
					InserimentoAggiornamentoScadenzarioFinePena(lConn, lPenResMod, aEvento,
							aFascicolo.getIdFascicoloSiep());
				}
			}

			// Se disposta dal TDS va inserito lo scadenzario fine Misura (13) o
			// Detenzione domiciliare a termine (14)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("isDispostaTDS    = " + isDispostaTDS);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DataInizioMisura = " + lMisModelOrder.getDataInizioMisura());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DataFineMisura   = " + lMisModelOrder.getDataFineMisura());

			if (isDispostaTDS && lMisModelOrder.getDataInizioMisura() != null
					&& lMisModelOrder.getDataFineMisura() != null) {
				String lCodTipoScadenzario = null;

				if ("5490".equals(lEveModel.getCodMotivo())) {
					// Tipo scadenzario 14
					lCodTipoScadenzario = "14"; // Detenzione Domiciliare a termine
				} else {
					lCodTipoScadenzario = "13"; // Misura Alternativa
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						" Procedo all'aggiornamento/Inserimento scadenzario misura. lCodTipoScadenzario = "
								+ lCodTipoScadenzario);

				InserimentoAggiornamentoScadenzario(lConn, lCodTipoScadenzario,
						lMisModelOrder.getDataInizioMisura(), lMisModelOrder.getDataFineMisura(),
						aEvento.getCodOperatoreAggiornamento(), aEvento.getCodUfficioAggiornamento(),
						aFascicolo.getIdFascicoloSiep());

			} else {
				// CANCELLA SCADENZARIO MISURA ALTERNATIVA (13)
				// FIXME DL 146/2013 verificare perchè cancella lo scadenzario MA
				lScaDao = new ScadenzarioDAO(lConn);
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);
				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13",
						aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				if (lScaMod != null) {
					lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
					lScaDao.delete();
					lScaDao.stop();
				}
			}

			// ========================================================================
			// Aggiorna/inserisce POSIZIONE_GIURIDICA
			// Se Senza Cumulo va Messo Libero (10) essendo detenuto altra causa (solo se decorrenza futura)
			// Se Con Cumulo:
			// ========================================================================
			String lPosizione = null;
			if (lCodiciMDS.contains(lEveModel.getCodMotivo())) {
				// Prosecuzione disposta dal MDS
				if (lMisModelOrder.getCodNaturaDecisione().equals("ED")) // Estensione MDS senza Cumulo
				{
					if (lMisModelOrder.getDataInizioMisura() != null && DateUtils
							.isGreater(lMisModelOrder.getDataInizioMisura(), DateUtils.getSysDate())) { // se
																										// a
																										// decorrenza
																										// futura
																										// cambio
																										// la
																										// PG
																										// in
																										// Libero
																										// sempre,
																										// altrimenti
																										// resta
																										// invariata
						lPosizione = "10"; // Libero
					}
				} else { // Senza cumulo aggiorno opportunamente la Posizione In Misura /questa causa)
					String lMotivo = lEveModel.getCodMotivo();
					if (lMotivo.equals("5470") || lMotivo.equals("5471") || lMotivo.equals("5472")) {
						lPosizione = "13"; // Espiazione Pena in Regime di Affidamento in Prova
					} else if (lMotivo.equals("5473") || lMotivo.equals("5474") || lMotivo.equals("5475")
							|| lMotivo.equals("5476")) {
						lPosizione = "12"; // Espiazione Pena in Regime di Detenzione Domiciliare
					} else if (lMotivo.equals("5477")) {
						lPosizione = "14"; // Espiazione Pena in Regime di Semiliberta'
					} else if (lMotivo.equals("5478")) {
						lPosizione = "50"; // Esecuzione presso domicilio della pena detentiva
					} else if (lMotivo.equals("5479")) // Differimento Nelle Forme della Detenzione
														// Domiciliare: ovvero Det Dom A Termine
					{
						lPosizione = "12"; // Espiazione Pena in Regime di Detenzione Domiciliare
					}
				}
			} else if (lCodiciTDS.contains(lEveModel.getCodMotivo())) {
				// Prosecuzione disposta dal TDS su reclamo
				if (lCodiciTDSAffi.contains(lEveModel.getCodMotivo())) {
					lPosizione = "13"; // Espiazione Pena in Regime di Affidamento in Prova
				} else if (lCodiciTDSDetDom.contains(lEveModel.getCodMotivo())) {
					lPosizione = "12"; // Espiazione Pena in Regime di Detenzione Domiciliare
				} else if ("5487".equals(lEveModel.getCodMotivo())) {
					lPosizione = "14"; // Espiazione Pena in Regime di Semiliberta'
				} else if ("5490".equals(lEveModel.getCodMotivo())) {
					lPosizione = "12"; // Espiazione Pena in Regime di Detenzione Domiciliare
				} else if ("5489".equals(lEveModel.getCodMotivo())) {
					lPosizione = "50"; // Esecuzione presso domicilio della pena detentiva
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();

			if (lPosizione != null) {
				InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod,
						lEveModel.getDataEmissione(), aEvento, aFascicolo.getIdFascicoloSiep(),
						aEvento.getIdEvento());
			}

			// Aggiorno il BLOB
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);

		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Eccezione: ", ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException(
					"MisuraAlternativaIndultinoController.ExUpdateValidaMAProsecuzione51Bis : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDAO);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lMiDAO);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);

			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	public EventoModel ExUpdateValidaMAEstensione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		EventoDAO lEveDaoMisAlt = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// Cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelTDS != null && (lEveModelTDS.getFlagDocumentoRegistrato() == null
						|| lEveModelTDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelTDS.setFlagDocumentoRegistrato("S");
					lEveModelTDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelTDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelTDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelTDS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();
			/* String lCodPosizione = */lPosMod.getCodPosizioneGiuridica();

			// Aggiorna Inserisci PENA_RESIDUA
			PenaResiduaModel lPenResMod = null;
			lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), null);

			// SETTA LO STATO PROCEDIMENTO
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					"0101");

			// aggiorna misura alternativa
			lMiDAO = new MisuraAlternativaDAO(lConn);

			if (lMisModelOrder != null) {
				lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
				lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
				lMiDAO.update();
				lMiDAO.stop();
			}

			// aggiorna/inserisci scadenzario MISURA ALTERNATIVA
			Date lData = lMisModelOrder.getDataInizioMisura();
			if (lData != null)
				InserimentoAggiornamentoScadenzarioMisuraAlternativa(lConn, lData, lPenResMod, lEveModel,
						aFascicolo.getIdFascicoloSiep());

			// Aggiorna/inserisce POSIZIONE_GIURIDICA
			String lPosizione = null;
			if (lMisModelOrder != null && lMisModelOrder.getCodTipoMisura() != null) {
				String lMotivo = lMisModelOrder.getCodTipoMisura();
				if (lMotivo.equals("0020") || lMotivo.equals("0092") || lMotivo.equals("0093")) {
					lPosizione = "13";
				}
				if (lMotivo.equals("0094")) {
					lPosizione = "14";
				}
				if (lMotivo.equals("0095") || lMotivo.equals("0100") || lMotivo.equals("0101")
						|| lMotivo.equals("0103")) {
					lPosizione = "12";
				}
				if (lMotivo.equals("0102")) {
					lPosizione = "25";
				}
			}

			InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod,
					lEveModel.getDataEmissione(), lEveModel, aFascicolo.getIdFascicoloSiep(),
					aEvento.getIdEvento());
			// Aggiorna tabella nome_provvedimento

			/* String lPosizioneGiu = */lPosMod.getCodPosizioneGiuridica();
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			// Per tipo misura "AFFIDAMENTO" senza estenzione cumulo
			if ((lMisModelOrder.getCodTipoMisura().equals("0020")
					|| lMisModelOrder.getCodTipoMisura().equals("0092")
					|| lMisModelOrder.getCodTipoMisura().equals("0093"))
					&& !lMisModelOrder.getCodNaturaDecisione().equals("EC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP121");
			}
			// Per tipo misura "DETENZIONE" senza estenzione cumulo
			else if ((lMisModelOrder.getCodTipoMisura().equals("0095")
					|| lMisModelOrder.getCodTipoMisura().equals("0100")
					|| lMisModelOrder.getCodTipoMisura().equals("0101")
					|| lMisModelOrder.getCodTipoMisura().equals("0102")
					|| lMisModelOrder.getCodTipoMisura().equals("0103"))
					&& !lMisModelOrder.getCodNaturaDecisione().equals("EC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP122");
			}
			// Per tipo misura "SEMILIBERTA" senza estenzione cumulo
			else if (lMisModelOrder.getCodTipoMisura().equals("0094")
					&& !lMisModelOrder.getCodNaturaDecisione().equals("EC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP123");
			}
			// per le altre "tipo Misura" con estenzione cumulo
			else {
				lNomProvvDAO.setCodNomeProvvedimento("NP124");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaMAEstensione : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("MisuraAternativaIndultinoController.ExUpdateValidaMAEstensione : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDAO);
			cleanup(lNotEveDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	public EventoModel ExUpdateValidaMARigetto(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		EventoDAO lEveDaoMisAlt = null;

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			aEvento.setDataEmissione(lEveModel.getDataEmissione());
			// Cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			// lMisSqlDAO.ricercaMisuraAlternativaByKey(aKeyMis);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelTDS != null && (lEveModelTDS.getFlagDocumentoRegistrato() == null
						|| lEveModelTDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelTDS.setFlagDocumentoRegistrato("S");
					lEveModelTDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelTDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelTDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelTDS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// Aggiorna Inserisci PENA_RESIDUA
			// PenaResiduaModel lPenResMod = null;
			/* lPenResMod = */InserimentoAggiornamentoPenResMisuraAlternativa(lConn, aEvento,
					aFascicolo.getIdFascicoloSiep(), null);

			// SETTA LO STATO PROCEDIMENTO
			String lCodStato = null;
			if (lCodPosizione != null && lCodPosizione.equals("29")) {
				lCodStato = "0123";
			} else if (lCodPosizione != null && (lCodPosizione.equals("46") || lCodPosizione.equals("47"))) {
				lCodStato = "0122";
			} else // codice ripetuto sotto indicazione di viviana
			{
				lCodStato = "0122";
			}

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), aEvento,
					lCodStato);

			// aggiorna misura alternativa
			lMiDAO = new MisuraAlternativaDAO(lConn);
			if (lCodPosizione != null && lCodPosizione.equals("29")) {
				if (lMisModelOrder != null) {
					lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
					lMiDAO.setDataFineMisura(lEveModel.getDataEmissione());
					lMiDAO.update();
					lMiDAO.stop();
				}
			}

			// cancellazione scadenzario MISURA ALTERNATIVA
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			if (lCodPosizione != null && lCodPosizione.equals("29")) {
				ScadenzarioModel lScaMod = null;
				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13",
						aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
					lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
					lScaDao.delete();
					lScaDao.stop();
				}
			}

			// Aggiorna/inserisce POSIZIONE_GIURIDICA
			if (lCodPosizione != null && lCodPosizione.equals("29")) {
				InserimentoAggiornamentoPosizioneGiuridica(lConn, "03", lPosMod, lEveModel.getDataEmissione(),
						aEvento, aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());
			}

			// Aggiorna tabella nome_provvedimento
			/* String lPosizioneGiu = */lPosMod.getCodPosizioneGiuridica();
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (lCodPosizione != null && lCodPosizione.equals("29")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP207");
			} else if (lCodPosizione != null && (lCodPosizione.equals("46") || lCodPosizione.equals("47"))) {
				lNomProvvDAO.setCodNomeProvvedimento("NP208");
			} else // codice ripetuto sotto indicazione di viviana--dario
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP208");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("MisuraAternativaIndultinoController.ExUpdateValidaMARigetto : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("MisuraAternativaIndultinoController.ExUpdateValidaMARigetto : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDAO);
			cleanup(lNotEveDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lMisSqlDAO);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lEveDaoBlob);
			cleanup(lEveDaoMisAlt);
			// cleanup(lConnBlob);
			cleanup(lConn);
		}

		return aEvento;
	}

	// Cancellazione Ordinanze e Decreti
	public EventoModel ExAggiornaEventoInserisciCampoNota(EventoModel aEvento, CampoNotaModel aCampoNota)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		CampoNotaModel lCampoMod = null;
		PenaResiduaSqlDAO lPenSql = null;
		PenaResiduaDAO lPenDAO = null;
		AnnotazioneManualeSqlDAO lAnnSql = null;
		AnnotazioneManualeDAO lAnnDAO = null;
		EventoStoreProcedurePulisciDAO lEventoProc = null;
		PosizioneGiuridicaSqlDAO lPosSql = null;
		PosizioneGiuridicaDAO lPosDAO = null;
		ScadenzarioSqlDAO lScaSqlDAO = null;
		ScadenzarioDAO lScaDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		EventoSimeoneSqlDAO lSqlSimeoneDao = null;
		SospensioneSqlDAO lSospSqlDAO = null;
		SospensioneDAO lSospDAO = null;
		LicenzaLibanticipataSqlDAO lLicAntiSql = null;
		LicenzaLibanticipataDAO lLicAntiDAO = null;
		Vector lAnnVect = null;
		EventoModel lEveRet = new EventoModel(aEvento);
		// paolo cherubini 24-11-2010 aggiungo la procedura per il ricalcolo dello scadenzario simeone
		EventoStoreProcedureAggiornaScadenzarioSimeoneDAO lEventoProcScad = null;
		EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO lEventoProc03 = null;

		try {
			lConn = getDBTransaction();

			// RICERCA EVENTO DA ANNULLARE
			lSqlDAO = new EventoSqlDAO(lConn);
			lSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveDaCanc = (EventoModel) lSqlDAO.getModelByKey();

			// ANNULLO L'EVENTO DELL'ORDINANZA/DECRETO
			lEveDao = new EventoDAO(lConn);

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.setFlagDocumentoRegistrato("A");
			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// RICERCO SE ESISTE LIBERAZIONE ANTICIPATA LEGATA ALLìEVENTO SE C'è LA CANCELLO

			lLicAntiSql = new LicenzaLibanticipataSqlDAO(lConn);
			lLicAntiDAO = new LicenzaLibanticipataDAO(lConn);
			lLicAntiSql.ricercaLicenzaLibanticipataByEve(lEveDaCanc.getIdEvento());
			LicenzaLibAnticipataModel lLibMod = (LicenzaLibAnticipataModel) lLicAntiSql.getModelByKey();
			if (lLibMod != null) {
				lLicAntiDAO.setDAOFromModelForUpdate(lLibMod);
				lLicAntiDAO.delete();
				lLicAntiDAO.stop();
			}

			// RICERCO IL PROVVEDIMENTO LEGATO ALL'EVENTO DELL'ORDINANZA/DECRETO PER ANNULLARLO O CANCELLARLO
			lSqlDAO.ricercaEventoByEveIdEvento(aEvento.getIdEvento());
			Vector lVectEventiProv = new Vector(lSqlDAO.getModels());

			lCampoNotaDao = new CampoNotaDAO(lConn);
			lCampoMod = new CampoNotaModel();

			lPosSql = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDAO = new PosizioneGiuridicaDAO(lConn);
			PosizioneGiuridicaModel lPosMod = null;
			Vector lPosizioni = null;

			Iterator iter = lVectEventiProv.iterator();
			while (iter.hasNext()) {
				EventoModel lEveProv = (EventoModel) iter.next();
				if (lEveProv != null && lEveProv.getFlagDocumentoRegistrato() != null
						&& lEveProv.getFlagDocumentoRegistrato().equals("S")) {
					lEveDao.setIdEvento(lEveProv.getIdEvento());
					lEveDao.setFlagDocumentoRegistrato("A");
					lEveDao.selByKey();
					lEveDao.update();
					lEveDao.stop();

					// paolo cherubini 24-11-2010 aggiungo la procedura per la cancellazione della pena
					// residua
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di mLog
					// siesLogger.debug("CANCELLO "+lEveProv.getIdEvento());
					lSospSqlDAO = new SospensioneSqlDAO(lConn);
					lSospDAO = new SospensioneDAO(lConn);
					lPenDAO = new PenaResiduaDAO(lConn);
					lPenSql = new PenaResiduaSqlDAO(lConn);
					lPenSql.ricercaPenaResiduaByKeyEvento(lEveProv.getIdEvento());
					PenaResiduaModel lPenMod = (PenaResiduaModel) lPenSql.getModelByKey();
					if (lPenMod != null) {
						lSospSqlDAO.ricercaSospensioneByIdPenaResidua(lPenMod.getIdPenaResidua());
						SospensioneModel lSospMod = (SospensioneModel) lSospSqlDAO.getModelByKey();
						if (lSospMod != null) {
							lSospDAO.setDAOFromModelForUpdate(lSospMod);
							lSospDAO.delete();
							lSospDAO.stop();
						}

						lPenDAO.setDAOFromModelForUpdate(lPenMod);
						lPenDAO.delete();
						lPenDAO.stop();
					} // fine paolo cherubini 24-11-2010 aggiungo la procedura per la cancellazione della pena
						// residua

					// CAMPO NOTA relativo all'evento decreto/ordinanza
					if (!aCampoNota.getDescr().equals("")) {
						lCampoMod.setEveIdEvento(lEveProv.getIdEvento());
						lCampoMod.setProgressivo(new BigDecimal(1));
						lCampoMod.setDataInserimento(aCampoNota.getDataInserimento());
						lCampoMod.setCodOperatoreInserimento(aCampoNota.getCodOperatoreInserimento());
						lCampoMod.setCodUfficioInserimento(aCampoNota.getCodUfficioInserimento()); // paolo
																									// cherubini
																									// 24-11-2010
						lCampoMod.setDescr(aCampoNota.getDescr());
						lCampoNotaDao.setDAOFromModel(lCampoMod);
						lCampoNotaDao.insert();
						lCampoNotaDao.stop();
					}

				} else if (lEveProv != null && (lEveProv.getFlagDocumentoRegistrato() == null
						|| lEveProv.getFlagDocumentoRegistrato().equals("N"))) {
					// Cancello se nn validato
					lEventoProc = new EventoStoreProcedurePulisciDAO(lConn);
					lEventoProc.setIdEvento(lEveProv.getIdEvento());
					lEventoProc.execute();
				}

				if (lEveProv != null) {
					// RICERCA POSIZIONE GIURIDICA ASSOCIATA ALL'EVENTO
					lPosSql.ricercaPosizioneGiuridicaByIdEvento(lEveProv.getIdEvento());
					lPosMod = (PosizioneGiuridicaModel) lPosSql.getModelByKey();

					// CANCELLA POSIZIONE GIURIDICA
					if (lPosMod != null) {
						// cancella pos giu evento
						lPosDAO.setDAOFromModelForUpdate(lPosMod);
						lPosDAO.delete();
						lPosDAO.stop();

						lPosSql.ricercaPosGiuCorrenteByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
						lPosizioni = new Vector(lPosSql.getModels());
						PosizioneGiuridicaModel lPosPrec = (PosizioneGiuridicaModel) lPosizioni.get(0);

						// ripristino posizione giuridica pecedente
						lPosPrec.setDataFine(null);
						lPosPrec.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
						lPosPrec.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
						lPosPrec.setDataAggiornamento(aCampoNota.getDataInserimento());

						lPosDAO.setDAOFromModelForUpdate(lPosPrec);
						lPosDAO.update();
						lPosDAO.stop();
					}
				}

			}

			// CAMPO NOTA
			if (!aCampoNota.getDescr().equals("")) {
				lCampoMod.setEveIdEvento(lEveRet.getIdEvento());
				lCampoMod.setProgressivo(new BigDecimal(1));
				lCampoMod.setDataInserimento(aCampoNota.getDataInserimento());
				lCampoMod.setCodOperatoreInserimento(aCampoNota.getCodOperatoreInserimento());
				lCampoMod.setDescr(aCampoNota.getDescr());
				lCampoNotaDao.setDAOFromModel(lCampoMod);
				lCampoNotaDao.insert();
				lCampoNotaDao.stop();
			}

			// RICERCA MISURA ALTERNATIVA E CANCELLAZIONE ASSOCIATE ALL'EVENTO DECRETO/ORDINANZA
			// Commentato perchè non deve essere cancellato altrimenti non si visualizza nell'elenco
			// procedimenti della sorveglianza l'esito del tenore!!! --dario--viviana 22-05-06
			/*
			 * lMisSqlDao = new MisuraAlternativaSqlDAO(lConn); lMisDao = new MisuraAlternativaDAO(lConn);
			 *
			 * MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
			 * lMisSqlDao.ricercaMisuraAlternativaByIdEvento(lEveRet.getIdEvento()); lMisMod =
			 * (MisuraAlternativaModel)lMisSqlDao.getModelByKey();
			 *
			 * if(lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
			 * lMisDao.setDAOFromModelForUpdate(lMisMod); lMisDao.delete(); lMisDao.stop(); }
			 *
			 * //RICERCA DEPOSITO ORDINANZA PC E CANCELLAZIONE lDepOrdDAO = new DepositoOrdinanzaPcDAO(lConn);
			 * lDepOrdSqlDAO = new DepositoOrdinanzaPcSqlDAO(lConn); lTenSqlDAO = new TenoreSqlDAO(lConn);
			 * lTenDAO = new TenoreDAO(lConn);
			 *
			 * DepositoOrdinanzaPcModel lDepPCMod = new DepositoOrdinanzaPcModel();
			 * lDepOrdSqlDAO.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveRet.getIdEvento()); lDepPCMod =
			 * (DepositoOrdinanzaPcModel)lDepOrdSqlDAO.getModelByKey();
			 *
			 * if(lDepPCMod != null && lDepPCMod.getIdDepositoOrdinanzaPc() != null) { //ricerca tenore ed
			 * aggiornamento. lTenDAO.setDAOForDeleteDepOrd(lDepPCMod.getIdDepositoOrdinanzaPc());
			 * lTenDAO.setDataAggiornamento(aCampoNota.getDataInserimento());
			 * lTenDAO.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			 * lTenDAO.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento()); lTenDAO.update();
			 * lTenDAO.stop();
			 *
			 * //ricerca DepositoOrdinanzaPc e cancellazione. lDepOrdDAO.setDAOFromModelForUpdate(lDepPCMod);
			 * lDepOrdDAO.delete(); lDepOrdDAO.stop(); }
			 *
			 * //RICERCA DEPOSITO Decreto E CANCELLAZIONE lDepDAO = new DepositoDecretoDAO(lConn); lDepSqlDAO
			 * = new DepositoDecretoSqlDAO(lConn);
			 *
			 * lDepSqlDAO.ricercaDepositoDecretoByIdEveGeneratoNoDescTipoDecreto(lEveRet.getIdEvento());
			 * DepositoDecretoModel lDepDec = new DepositoDecretoModel(); lDepSqlDAO.start();
			 * lDepSqlDAO.next(); lDepDec = (DepositoDecretoModel)lDepSqlDAO.getModelNoDescTipoDecreto();
			 * lDepSqlDAO.stop();
			 *
			 * if(lDepDec != null && lDepDec.getIdDepositoDecreto() != null) { //ricerca tenore ed
			 * aggiornamento. lTenDAO.setDAOForDeleteDepDec(lDepDec.getIdDepositoDecreto());
			 * lTenDAO.setDataAggiornamento(aCampoNota.getDataInserimento());
			 * lTenDAO.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			 * lTenDAO.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento()); lTenDAO.update();
			 * lTenDAO.stop();
			 *
			 * //ricerca DepositoDecreto e cancellazione. lDepDAO.setDAOFromModelForUpdate(lDepDec);
			 * lDepDAO.delete(); lDepDAO.stop();
			 *
			 * }
			 */

			// RICERCA EVENTI
			lSqlSimeoneDao = new EventoSimeoneSqlDAO(lConn);

			String[] lTipoEvento = { "01" };
			String[] lTipoProv = { "02", "03" };
			Vector eventi = null;
			lSqlSimeoneDao.ricercaEventoByFascicoloTipEveTipProvDesc(aEvento.getFasSieIdFascicoloSiep(),
					lTipoEvento, lTipoProv);
			eventi = new Vector(lSqlSimeoneDao.getModels());

			lPosMod = null;
			lPosizioni = null;

			// RICERCA POSIZIONE GIURIDICA ASSOCIATA ALL'EVENTO
			lPosSql.ricercaPosizioneGiuridicaByIdEvento(lEveRet.getIdEvento());
			lPosMod = (PosizioneGiuridicaModel) lPosSql.getModelByKey();

			// SETTTO LO STATO DEL PROCEDIMENTO
			lStatoDao = new StatoProcedimentoDAO(lConn);

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			lStatoProcMod.setCodStatoProcedimento("0129");
			lStatoProcMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aCampoNota.getCodOperatoreInserimento());
			lStatoProcMod.setDataInserimento(aCampoNota.getDataInserimento());
			lStatoProcMod.setCodUfficioInserimento(aCampoNota.getCodUfficioInserimento());
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveDaCanc.getDataEmissione());

			lPenDAO = new PenaResiduaDAO(lConn);
			lPenSql = new PenaResiduaSqlDAO(lConn);

			if (eventi != null && eventi.size() >= 2) {
				EventoModel UltimoEvento = (EventoModel) eventi.get(0);

				// EVENTO E' L'ULTIMO DELLA LISTA
				if (UltimoEvento.getIdEvento().compareTo(aEvento.getIdEvento()) >= 0) {

					// CANCELLA POSIZIONE GIURIDICA
					if (lPosMod != null) {
						// cancella pos giu dell'ultimo evento
						lPosDAO.setDAOFromModelForUpdate(lPosMod);
						lPosDAO.delete();
						lPosDAO.stop();

						lPosSql.ricercaPosGiuCorrenteByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
						lPosizioni = new Vector(lPosSql.getModels());
						PosizioneGiuridicaModel lPosPrec = (PosizioneGiuridicaModel) lPosizioni.get(0);

						// ripristino posizione giuridica pecedente
						lPosPrec.setDataFine(null);
						lPosPrec.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
						lPosPrec.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
						lPosPrec.setDataAggiornamento(aCampoNota.getDataInserimento());

						lPosDAO.setDAOFromModelForUpdate(lPosPrec);
						lPosDAO.update();
						lPosDAO.stop();
					}

					// ricerca evento precedente
					EventoModel EventoPrec = (EventoModel) eventi.get(1);

					// ricerca pena residua precedente legata a evento precedente
					lPenSql.ricercaPenaResiduaByKeyEvento(EventoPrec.getIdEvento());
					PenaResiduaModel lPenPrecMod = (PenaResiduaModel) lPenSql.getModelByKey();

					// scadenzario
					ScadenzarioModel lScaMod = null;
					ScadenzarioModel lScaModSet = new ScadenzarioModel();
					lScaSqlDAO = new ScadenzarioSqlDAO(lConn);
					lScaDAO = new ScadenzarioDAO(lConn);

					lScaModSet.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
					String[] tipoSca = { "02", "01", "05", "13" };
					lScaSqlDAO.ricercaScadenzarioPerTipoScadenzario(tipoSca,
							aEvento.getFasSieIdFascicoloSiep());
					lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();

					if (lScaMod != null && lPenPrecMod != null && lPenPrecMod.getDataFine() != null) {
						lScaDAO.setDataFineScadenza(lPenPrecMod.getDataFine());
						lScaDAO.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
						lScaDAO.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
						lScaDAO.setDataAggiornamento(aCampoNota.getDataInserimento());
						lScaMod.setFlagVisto("N");

						lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
						lScaDAO.update();
						lScaDAO.stop();

					}

					// Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
					lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
					lStatoDao.delete();

					// INSERISCO stato procedimento
					lStatoDao.setDAOFromModel(lStatoProcMod);
					lStatoDao.insert();
					lStatoDao.stop();
				} else {
					// CANCELLA POSIZIONE GIURIDICA PERCHè NN è L'ULTIMO EVENTO
					if (lPosMod != null) {
						lPosDAO.setDAOFromModelForUpdate(lPosMod);
						lPosDAO.delete();
						lPosDAO.stop();
					}
				}
			} else { // ESISTE SOLO UN UNICO EVENTO
						// Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
				lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
				lStatoDao.delete();

				// INSERISCO stato procedimento
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();

			}

			// PENA RESIDUA
			lSospSqlDAO = new SospensioneSqlDAO(lConn);
			lSospDAO = new SospensioneDAO(lConn);

			lPenSql.ricercaPenaResiduaByKeyEvento(lEveRet.getIdEvento());

			PenaResiduaModel lPenMod = (PenaResiduaModel) lPenSql.getModelByKey();
			if (lPenMod != null) {
				lSospSqlDAO.ricercaSospensioneByIdPenaResidua(lPenMod.getIdPenaResidua());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDAO.getModelByKey();
				if (lSospMod != null) {
					lSospDAO.setDAOFromModelForUpdate(lSospMod);
					lSospDAO.delete();
					lSospDAO.stop();
				}

				lPenDAO.setDAOFromModelForUpdate(lPenMod);
				lPenDAO.delete();
				lPenDAO.stop();
			}

			// cancella annotazioni manuali
			lAnnSql = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDAO = new AnnotazioneManualeDAO(lConn);

			lAnnSql.ricercaAnnotazioneManualeByIdEvento(aEvento.getIdEvento());
			lAnnVect = new Vector(lAnnSql.getModels());

			for (int i = 0; i < lAnnVect.size(); i++) {

				AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lAnnVect.get(i);

				lAnnDAO.setDAOFromModelForUpdate(lAnnMod);
				lAnnDAO.delete();
				lAnnDAO.stop();

			}

			// paolo cherubini 24-11-2010 aggiungo la procedura per il ricalcolo dello scadenzario simeone
			// =========================================
			// Richiama la store procedure per ripristinare lo scadenzario Simeone
			// =========================================

			BigDecimal par_anno = new BigDecimal(DateUtils.getSysDate("yyyy"));
			String par_bdi = "";
			// String par_ufficio = aEvento.getCodUfficioInserimento();
			String par_ufficio = aCampoNota.getCodUfficioInserimento(); // paolo cherubini escamotage
																		// 24-11-2010
			BigDecimal par_id_fascicolo = aEvento.getFasSieIdFascicoloSiep();
			lEventoProcScad = new EventoStoreProcedureAggiornaScadenzarioSimeoneDAO(lConn);
			lEventoProcScad.setpar_anno(par_anno);
			lEventoProcScad.setpar_bdi(par_bdi);
			lEventoProcScad.setpar_ufficio(par_ufficio);
			lEventoProcScad.setpar_id_fascicolo(par_id_fascicolo);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("lEventoProcScad "+par_anno+ " "+par_bdi+ " "+par_ufficio+
			// " "+par_id_fascicolo);
			lEventoProcScad.execute();
			// fine paolo cherubini 24-11-2010 aggiungo la procedura per il ricalcolo dello scadenzario
			// simeone

			// =========================================
			// Richiama la store procedure per ripristinare lo scadenzario 03 Vane Ricerche
			// =========================================

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("par_anno = " + par_anno + "par_bdi = " + par_bdi);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("par_ufficio = " + par_ufficio + "par_id_fascicolo = " + par_id_fascicolo);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO");

			// BigDecimal par_anno = new BigDecimal(DateUtils.getSysDate("yyyy"));
			// String par_bdi = "";
			// String par_ufficio = aEvento.getCodUfficioInserimento();
			// BigDecimal par_id_fascicolo = aEvento.getFasSieIdFascicoloSiep();
			lEventoProc03 = new EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO(lConn);
			lEventoProc03.setpar_anno(par_anno);
			lEventoProc03.setpar_bdi(par_bdi);
			lEventoProc03.setpar_ufficio(par_ufficio);
			lEventoProc03.setpar_id_fascicolo(par_id_fascicolo);
			lEventoProc03.execute();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("fine EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO");

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"MisuraAlternativaIndultinoController.ExAggiornaEventoInserisciCampoNota: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException(
					"MisuraAlternativaIndultinoController.ExAggiornaEventoInserisciCampoNota: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lPenSql);
			cleanup(lPenDAO);
			cleanup(lSospDAO);
			cleanup(lSospSqlDAO);
			cleanup(lEveDao);
			cleanup(lSqlDAO);
			cleanup(lAnnSql);
			cleanup(lAnnDAO);
			cleanup(lEventoProc);
			cleanup(lPosSql);
			cleanup(lPosDAO);
			cleanup(lScaSqlDAO);
			cleanup(lScaDAO);
			cleanup(lStatoDao);
			cleanup(lSqlSimeoneDao);
			cleanup(lLicAntiSql);
			cleanup(lLicAntiDAO);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEventoProcScad);
			cleanup(lEventoProc03);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * ExUpdateValidaMAAmmProvvisoria Upload Ammissione Provvisoria
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMAAmmProvvisoria(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		EventoDAO lEveDaoMisAlt = null;

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

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelMDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelMDS != null && (lEveModelMDS.getFlagDocumentoRegistrato() == null
						|| lEveModelMDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelMDS.setFlagDocumentoRegistrato("S");
					lEveModelMDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelMDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelMDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelMDS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			// String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
			PosizioneGiuridicaModel lPosGiu = new PosizioneGiuridicaModel();
			IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

			lPosAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
					aFascicolo.getIdFascicoloSiep());
			String lPosizioneGiu = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();
			lPosGiu.setCodPosizioneGiuridica(lPosizioneGiu);

			String lCodTipoPosGiuridicaAltraCausa = "";
			if (lPosAltra.getAltraCausa() != null
					&& lPosAltra.getAltraCausa().getCodTipoPosGiuridica() != null
					&& !lPosAltra.getAltraCausa().getCodTipoPosGiuridica().equals("")) {
				lCodTipoPosGiuridicaAltraCausa = lPosAltra.getAltraCausa().getCodTipoPosGiuridica();
			}

			// MEV-9 si aggiungono gli ulteriori codici motivo (sorveglianza)
			Set<String> codiciAffidamentoSorv = new HashSet<String>(Arrays.asList(new String[]{"2006","2008","0680","0681","0690","0691","0692"}));
			Set<String> codiciDetenzioneSorv  = new HashSet<String>(Arrays.asList(new String[]{"2005","0682","0693"}));
			
			// se la misura è eseguita da SORV cambio sempre la posizione giuridica
			String lPosizioneDiArrivo = null;
			boolean lCambioPos = false;
			if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lCambioPos = true;
				// MEV_9 - Gestiti i nuovi codici
//				if ("2006".equals(lMisModelOrder.getCodTipoMisura()) || "2008".equals(lMisModelOrder.getCodTipoMisura()) // dl 146 2013
//				) // Per Tipo Misura "AFFIDAMENTO"
				if (codiciAffidamentoSorv.contains(lMisModelOrder.getCodTipoMisura()))
				{
					lPosizioneDiArrivo = "54"; // Affidamento in Prova Provvisorio - Cambio da 51 a 54
				}

			  // MEV_9 - Gestiti i nuovi codici
				//if ("2005".equals(lMisModelOrder.getCodTipoMisura())) // Per Tipo Misura "DETENZIONE"
				if (codiciDetenzioneSorv.contains(lMisModelOrder.getCodTipoMisura()))
				{
					lPosizioneDiArrivo = "29"; // Detenzione Domiciliare Provvisoria
				}
			}

			// se la misura è eseguita da PROC cambio la posizione giuridica solo se non è libero
			// da libero occorre fare prima il verbale, è il verbale che cambia la posizione

			// 20/05/2015
			// MEV 10 S3 Minori - viene aggiunta una ulteriore condizione per i fascicoli
			// la cui posizione giuridica è uguale a 07 (Libero) sulla tabella
			// POSIZIONE_GIURICA e il codice tipo posizione giuridica (COD_TIPO_POS_GIURIDICA)
			// presente sulla tabella ALTRA_CAUSA è uguale a 70, 71, 72, 78, 79, 80, 81
			// oppure uguale a 73, 74, 75, 76, 77
			if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC") && (!lPosMod.isLibero()
					|| (lPosizioneGiu.equals("07") && !lCodTipoPosGiuridicaAltraCausa.equals("")
							&& (lCodTipoPosGiuridicaAltraCausa.equals("70")
									|| lCodTipoPosGiuridicaAltraCausa.equals("71")
									|| lCodTipoPosGiuridicaAltraCausa.equals("72")
									|| lCodTipoPosGiuridicaAltraCausa.equals("78")
									|| lCodTipoPosGiuridicaAltraCausa.equals("79")
									|| lCodTipoPosGiuridicaAltraCausa.equals("80")
									|| lCodTipoPosGiuridicaAltraCausa.equals("81")
									|| lCodTipoPosGiuridicaAltraCausa.equals("73")
									|| lCodTipoPosGiuridicaAltraCausa.equals("74")
									|| lCodTipoPosGiuridicaAltraCausa.equals("75")
									|| lCodTipoPosGiuridicaAltraCausa.equals("76")
									|| lCodTipoPosGiuridicaAltraCausa.equals("77"))))) {
				lCambioPos = true;
				// MEV_9 - Gestiti i nuovi codici
				//if (("2006".equals(lMisModelOrder.getCodTipoMisura()) || "2008".equals(lMisModelOrder.getCodTipoMisura()))
				if (   codiciAffidamentoSorv.contains(lMisModelOrder.getCodTipoMisura())					
						&& !"54".equals(lPosMod.getCodPosizioneGiuridica())) // Per Tipo Misura "AFFIDAMENTO"
				{
					lPosizioneDiArrivo = "54"; // Affidamento in Prova Provvisorio - Cambio da 51 a 54
				}

				// MEV_9 - Gestiti i nuovi codici
				//if ("2005".equals(lMisModelOrder.getCodTipoMisura())
				if (   codiciDetenzioneSorv.contains(lMisModelOrder.getCodTipoMisura())	
						&& !"29".equals(lPosMod.getCodPosizioneGiuridica())) // Per Tipo Misura "DETENZIONE"
				{
					lPosizioneDiArrivo = "29"; // Detenzione Domiciliare Provvisoria
				}
			}

			// ambrosino - vECCHIA VERSIONE
			/*
			 * // Per Tipo Misura "AFFIDAMENTO" e "DETENZIONE" if((lCambioPos) ||
			 * ("2006".equals(lMisModelOrder.getCodTipoMisura()) &&( lCodPosizione.equals("03") ||
			 * lCodPosizione.equals("14") || lCodPosizione.equals("04") || lCodPosizione.equals("12") ||
			 * lCodPosizione.equals("29")))) { BigDecimal lKey =
			 * InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizioneDiArrivo, lPosMod,
			 * lEveModel.getDataEmissione(), lEveMod, aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());
			 * }
			 *
			 * fine Vecchia Versione
			 */

			if (lCambioPos) {
				/* BigDecimal lKey = */InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizioneDiArrivo,
						lPosMod, lEveModel.getDataEmissione(), lEveMod, aFascicolo.getIdFascicoloSiep(),
						aEvento.getIdEvento());
			}

			// SETTA LO STATO PROCEDIMENTO
			String lStato = null;

			// MEV_9 - si aggiungono gli ulteriori codici
			//if ("2006".equals(lMisModelOrder.getCodTipoMisura()) || "2008".equals(lMisModelOrder.getCodTipoMisura())) {
			if (codiciAffidamentoSorv.contains(lMisModelOrder.getCodTipoMisura())) {
				// Per Tipo Misura "AFFIDAMENTO"
				if (lEveModel.getCodMotivo().equals("5420"))
					lStato = "0522"; // Richiesto Verbale per codice 2006 (affidamento Terapeutica)
				else if (lEveModel.getCodMotivo().equals("5421"))
					lStato = "0523"; // Richiesto Verbale per codice 2008 ()				
				// MEV_9 - nuovi codici		
				else if (lEveModel.getCodMotivo().equals("1400"))
					lStato = "0560"; 
				else if (lEveModel.getCodMotivo().equals("1401"))
					lStato = "0561"; 
				else if (lEveModel.getCodMotivo().equals("1410"))
					lStato = "0562";
				else if (lEveModel.getCodMotivo().equals("1411"))
					lStato = "0563";
				else if (lEveModel.getCodMotivo().equals("1412"))
					lStato = "0564";
				// Nel caso di richiesta verbale
				else if (lEveModel.getCodMotivo().equals("5422"))
					lStato = "0565"; 
				else if (lEveModel.getCodMotivo().equals("5423"))
					lStato = "0566"; 
				else if (lEveModel.getCodMotivo().equals("5424"))
					lStato = "0567";
				else if (lEveModel.getCodMotivo().equals("5425"))
					lStato = "0568";
				else if (lEveModel.getCodMotivo().equals("5426"))
					lStato = "0569";				
				// MEV_9 - FINE
				else if ("2006".equals(lMisModelOrder.getCodTipoMisura()))
					lStato = "0520"; // Tutti gli altri eventi
				else if ("2008".equals(lMisModelOrder.getCodTipoMisura()))
					lStato = "0521"; // Tutti gli altri eventi
				// lStato = "0173";
			} else if (codiciDetenzioneSorv.contains(lMisModelOrder.getCodTipoMisura())) {	
			//} else if ("2005".equals(lMisModelOrder.getCodTipoMisura())) { // Per Tipo Misura "DETENZIONE"
				if ("2005".equals(lMisModelOrder.getCodTipoMisura())) 
					lStato = "0068";
				else if (lEveModel.getCodMotivo().equals("1402")) 
					lStato = "0570";
				else if (lEveModel.getCodMotivo().equals("1413")) 
				  lStato = "0571";			
		  }

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveMod,
					lStato);

			// ricerca pena residua
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveMod, aFascicolo.getIdFascicoloSiep(),
					"N");

			// aggiorna misura alternativa
			lMiDAO = new MisuraAlternativaDAO(lConn);
			String lAggiorna = "N";
			if (lMisModelOrder != null) {
				lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());

				if (lMisModelOrder.getDataInizioMisura() == null && !lPosMod.isLibero()) {
					if (lMisModelOrder.getDataScarcerazione() != null) {
						lMiDAO.setDataInizioMisura(lMisModelOrder.getDataScarcerazione());
						lAggiorna = "S";
					} else {
						lMiDAO.setDataInizioMisura(lMisModelOrder.getDataDecisione());
						lAggiorna = "S";
					}
				}

				if (lPenResMod != null && lPenResMod.getDataFine() != null) {
					lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
					lAggiorna = "S";
				}

				if (lAggiorna.equals("S")) {
					lMiDAO.update();
				}

				lMiDAO.stop();
			}

			// ** Aggiorna SCADENZARIO**
			if (lMisModelOrder != null && lMisModelOrder.getDataInizioMisura() != null
					&& lMisModelOrder.getDataFineMisura() == null) {
				Date lData = lMisModelOrder.getDataInizioMisura();
				InserimentoAggiornamentoScadenzarioMisuraAlternativa(lConn, lData, lPenResMod, lEveModel,
						aFascicolo.getIdFascicoloSiep());
			}

			// Aggiorna tabella nome_provvedimento
			if ("2005".equals(lMisModelOrder.getCodTipoMisura())) {
				lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
				lNomProvvDAO.setCodNomeProvvedimento("NP061");
				lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

				lNomProvvDAO.insert();
				lNomProvvDAO.stop();
			}

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
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaMAAmmProvvisoria : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaMAAmmProvvisoria : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lLuogoDAO);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}
	// ---------------------- AMBROSINO 09/2010 : Variazione Data Inizio Misura - Validazione

	/**
	 * ExUpdateValidaVariazioneMAAmmProvvisoria Upload Ammissione Provvisoria
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaVariazioneMAAmmProvvisoria(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		EventoSqlDAO lEveSqlDao5414 = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		EventoDAO lEveDaoMisAlt = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		/*
		 * PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		 *
		 * NotificaEventoSqlDAO lNotEveDao = null; MisuraAlternativaDAO lMiDAO = null; NomeProvvedimentoDAO
		 * lNomProvvDAO = null; LuogoDetenzioneDAO lLuogoDAO = null;
		 */

		EventoModel lEveMod = new EventoModel(aEvento);

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			// Ricerca Evento da Validare (Motivo 5415)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// AMBROSINO 09/2010 - La Misura Alternativa è legata all'evento 'Motivo 5414' ,
			// che a sua volta è legato all'evento 'Motivo 5415' (quello da Validare)

			// Ricerca Evento Motivo 5414
			lEveSqlDao5414 = new EventoSqlDAO(lConn);
			lEveSqlDao5414.ricercaEventoByKey(lEveModel.getEveIdEvento());
			EventoModel lEveModel5414 = new EventoModel();
			lEveModel5414 = (EventoModel) lEveSqlDao5414.getModelByKey();

			// cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModel5414.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento 5415
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				// lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				// EventoModel lEveModelMDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModel.getFlagDocumentoRegistrato() == null
						|| lEveModel.getFlagDocumentoRegistrato().equals("N")) {
					lEveModel.setFlagDocumentoRegistrato("S");
					lEveModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModel.setDataAggiornamento(DateUtils.getSysDate());
					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModel);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// ricerca pena residua
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// ** Aggiorna SCADENZARIO**
			if (lMisModelOrder != null && lMisModelOrder.getDataInizioMisura() != null
					&& lMisModelOrder.getDataFineMisura() == null) {
				Date lData = lMisModelOrder.getDataInizioMisura();
				InserimentoAggiornamentoScadenzarioMisuraAlternativa(lConn, lData, lPenResMod, lEveModel,
						aFascicolo.getIdFascicoloSiep());
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();
			// lEveDaoBlob = new EventoDAO(lConnBlob);
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
			// rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaVariazioneMAAmmProvvisoria : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException(
					"MisuraAternativaIndultinoController.ExUpdateValidaVariazioneMAAmmProvvisoria : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lEveSqlDao5414);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lPenResSqlDao);
			/*
			 * cleanup(lPosSqlDao); cleanup(lNotEveDao); cleanup(lMiDAO); cleanup(lNomProvvDAO);
			 * cleanup(lLuogoDAO);
			 */
			cleanup(lEveDaoBlob);
			cleanup(lConn);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// METODI PRIVATI
	private void InserimentoAggiornamentoScadenzarioMisuraAlternativa(Connection lConn, Date aDataInizio,
			PenaResiduaModel lPenResMod, EventoModel lEveModel, BigDecimal aIdFacicoloSiep)
			throws DAOException, F3BException {

		ScadenzarioDAO lScaDao = new ScadenzarioDAO(lConn);
		ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);
		ScadenzarioModel lScaMod = null;

		try {
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13", aIdFacicoloSiep);
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null) {
				lScaDao.setDataInizioScadenza(aDataInizio);
				lScaDao.setDataFineScadenza(lPenResMod.getDataFine());

				lScaDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lScaDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());

				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
				lScaDao.stop();
			} else {
				// inserisce scadenzario
				ScadenzarioModel lScaModel = new ScadenzarioModel();

				lScaModel.setCodTipoScadenzario("13");
				lScaModel.setDataInizioScadenza(aDataInizio);
				lScaModel.setDataFineScadenza(lPenResMod.getDataFine());

				lScaModel.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lScaModel.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lScaModel.setDataInserimento(DateUtils.getSysDate());

				lScaModel.setFasSieIdFascicoloSiep(aIdFacicoloSiep);

				lScaDao.setDAOFromModel(lScaModel);
				lScaDao.insert();
				lScaDao.stop();
			}
		} finally {
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
		}
	}

	private PenaResiduaModel InserimentoAggiornamentoPenResMisuraAlternativa(Connection lConn,
			EventoModel lEveModel, BigDecimal aKey, String lProsecuzione) throws DAOException, F3BException {

		PenaResiduaDAO lPenResDao = new PenaResiduaDAO(lConn);
		PenaResiduaSqlDAO lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
		PenaResiduaModel lPenResMod = new PenaResiduaModel();
		try {
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aKey);
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			if ("S".equals(lProsecuzione))
				lPenResMod.setEveIdEvento(null);

			if (lPenResMod.getEveIdEvento() == null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
				lPenResDao.setFlagValidato("S");

				lPenResDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());

				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				lPenResDao.setDAOFromModel(lPenResMod);
				lPenResDao.setFlagValidato("S");
				lPenResDao.setEveIdEvento(lEveModel.getIdEvento());

				lPenResDao.setDataInserimento(DateUtils.getSysDate());
				lPenResDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());

				lPenResDao.insert();
				lPenResDao.stop();
			}
		} finally {
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
		}
		return lPenResMod;
	}

	private void InserimentoAggiornamentoScadenzarioFinePena(Connection lConn, PenaResiduaModel lPenResMod,
			EventoModel lEveModel, BigDecimal aKey) throws DAOException, F3BException {

		ScadenzarioDAO lScaDao = new ScadenzarioDAO(lConn);
		ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);
		ScadenzarioModel lScaMod = null;
		try {
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02", aKey);
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null) {
				lScaDao.setDataFineScadenza(lPenResMod.getDataFine());

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
				lScaModel.setDataFineScadenza(lPenResMod.getDataFine());

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

		PosizioneGiuridicaDAO lPosDao = new PosizioneGiuridicaDAO(lConn);
		BigDecimal lKey = null;

		try {
			// Aggiorna la precedente Posizione
			if (lPos != null && lPos.getDataFine() == null) {
				lPosDao.setDataFine(lData);

				lPosDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataAggiornamento(lEveModel.getDataAggiornamento());

				lPosDao.setCondizioneUpdate(lPos.getIdPosizioneGiuridica());
				lPosDao.update();
				lPosDao.stop();
			}

			// Inserisce la Nuova posizione
			if (lPosizione != null) {
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setDataInizio(lData);
				lPosDao.setFasSieIdFascicoloSiep(aKeyFasc);
				lPosDao.setIdEventoRiferimento(aKeyEve);

				lPosDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(lEveModel.getDataAggiornamento());
				lPosDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());

				lPosDao.setCodPosizioneProcessuale("-");

				lKey = lPosDao.insert();
				lPosDao.stop();
			}
		} finally {
			cleanup(lPosDao);
		}
		return lKey;
	}

	/**
	 * Cancella lo STATO_PROCEDIMENTO attuale. Inserisce il nuovo STATO_PROCEDIMENTO.
	 *
	 * @param lConn
	 * @param aKey
	 *            - idFascicoloSiep
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

	/**
	 * Aggiorna/Inserisce un generico scadenzario secondo quanto indicato
	 *
	 * @param aConnection
	 * @param aCodTipoScadenzario
	 * @param aDataInizio
	 * @param aDataFine
	 * @param aCodOperatore
	 * @param aCodUfficio
	 * @param aIdFacicoloSiep
	 * @throws DAOException
	 * @throws F3BException
	 */
	private void InserimentoAggiornamentoScadenzario(Connection aConnection, String aCodTipoScadenzario,
			Date aDataInizio, Date aDataFine, String aCodOperatore, String aCodUfficio,
			BigDecimal aIdFacicoloSiep) throws DAOException, F3BException {

		ScadenzarioDAO lScaDao = new ScadenzarioDAO(aConnection);
		ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(aConnection);
		ScadenzarioModel lScaMod = null;

		try {
			//
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo(aCodTipoScadenzario, aIdFacicoloSiep);
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null) {
				lScaDao.setDataInizioScadenza(aDataInizio);
				lScaDao.setDataFineScadenza(aDataFine);

				lScaDao.setCodOperatoreAggiornamento(aCodOperatore);
				lScaDao.setCodUfficioAggiornamento(aCodUfficio);
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());

				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
				lScaDao.stop();
			} else {
				// inserisce scadenzario
				ScadenzarioModel lScaModel = new ScadenzarioModel();

				lScaModel.setCodTipoScadenzario(aCodTipoScadenzario);
				lScaModel.setDataInizioScadenza(aDataInizio);
				lScaModel.setDataFineScadenza(aDataFine);

				lScaModel.setCodOperatoreInserimento(aCodOperatore);
				lScaModel.setCodUfficioInserimento(aCodUfficio);
				lScaModel.setDataInserimento(DateUtils.getSysDate());

				lScaModel.setFasSieIdFascicoloSiep(aIdFacicoloSiep);

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
	 * Metodo di validazione dei provvdimenti legati all'applicazione provvosiria della semilibertà
	 * 
	 * @since MEV_9-SIEP
	 */
  public EventoModel ExUpdateValidaMAAmmProvSemiliberta (EventoModel aEvento, FascicoloSiepModel aFascicolo)
      throws F3BException {

    Connection lConn = null;

    EventoSqlDAO lEveSqlDao = null;
    PenaResiduaSqlDAO lPenResSqlDao = null;
    MisuraAlternativaDAO lMiDAO = null;
    MisuraAlternativaSqlDAO lMisSqlDAO = null;
    LuogoDetenzioneDAO lLuogoDAO = null;
    EventoDAO lEveDaoMisAlt = null;

    EventoModel lEveMod = new EventoModel(aEvento);
    EventoDAO lEveDaoBlob = null;

    try {
      lConn = getDBTransaction();

      // 
      lEveSqlDao = new EventoSqlDAO(lConn);
      lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
      EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

      // cerca Misura Alternariva by Evento
      lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
      lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModel.getEveIdEvento());
      MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

      // Valido l'evento riferito alla misura alternativa (decsreto/ordinanza)
      if (   lMisModelOrder != null 
      		&& lMisModelOrder.getIdMisuraAlternativa() != null) 
      {
        lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
        EventoModel lEveModelMDS = (EventoModel) lEveSqlDao.getModelByKey();
        lEveDaoMisAlt = new EventoDAO(lConn);

        if (   lEveModelMDS != null 
        		&& (lEveModelMDS.getFlagDocumentoRegistrato() == null
            || lEveModelMDS.getFlagDocumentoRegistrato().equals("N"))) {
          lEveModelMDS.setFlagDocumentoRegistrato("S");
          lEveModelMDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
          lEveModelMDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
          lEveModelMDS.setDataAggiornamento(DateUtils.getSysDate());

          lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelMDS);
          lEveDaoMisAlt.update();
          lEveDaoMisAlt.stop();
        }
      }

      // Cerca POSIZIONE_GIURIDICA corrente
      PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = null;
      PosizioneGiuridicaModel lPosGiuModel = null;
      
      IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
      lPosAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
          aFascicolo.getIdFascicoloSiep());
          
      lPosGiuModel = lPosAltra.getPosizioneGiuridica();

      String lCodTipoPosGiuridicaAltraCausa = "";
      if (   lPosAltra.getAltraCausa() != null
          && lPosAltra.getAltraCausa().getCodTipoPosGiuridica() != null
          && !lPosAltra.getAltraCausa().getCodTipoPosGiuridica().equals("")) {
        lCodTipoPosGiuridicaAltraCausa = lPosAltra.getAltraCausa().getCodTipoPosGiuridica();
      }

      
      // se la misura è eseguita da SORV cambio sempre la posizione giuridica
      String lPosizioneDiArrivo = null;
      boolean lCambioPos = false;
      if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
        lCambioPos = true;
        lPosizioneDiArrivo = "91"; // Nuova PG
      }

      // se la misura è eseguita da PROC cambio la posizione giuridica solo se non è libero
      // da libero occorre fare prima il verbale, è il verbale che cambia la posizione

      // 20/05/2015
      // MEV 10 S3 Minori - viene aggiunta una ulteriore condizione per i fascicoli
      // la cui posizione giuridica è uguale a 07 (Libero) sulla tabella
      // POSIZIONE_GIURICA e il codice tipo posizione giuridica (COD_TIPO_POS_GIURIDICA)
      // presente sulla tabella ALTRA_CAUSA è uguale a 70, 71, 72, 78, 79, 80, 81
      // oppure uguale a 73, 74, 75, 76, 77
      if (   lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC") 
          && (   !lPosGiuModel.isLibero()
              || (   lPosGiuModel.getCodPosizioneGiuridica().equals("07") 
                  && (   lCodTipoPosGiuridicaAltraCausa.equals("70")
                      || lCodTipoPosGiuridicaAltraCausa.equals("71")
                      || lCodTipoPosGiuridicaAltraCausa.equals("72")
                      || lCodTipoPosGiuridicaAltraCausa.equals("78")
                      || lCodTipoPosGiuridicaAltraCausa.equals("79")
                      || lCodTipoPosGiuridicaAltraCausa.equals("80")
                      || lCodTipoPosGiuridicaAltraCausa.equals("81")
                      || lCodTipoPosGiuridicaAltraCausa.equals("73")
                      || lCodTipoPosGiuridicaAltraCausa.equals("74")
                      || lCodTipoPosGiuridicaAltraCausa.equals("75")
                      || lCodTipoPosGiuridicaAltraCausa.equals("76")
                      || lCodTipoPosGiuridicaAltraCausa.equals("77")
                     )
                  )
             )
         ) 
      {
        lCambioPos = true;
        lPosizioneDiArrivo = "14"; //FIXME verificare se gestire una nuova PG es 91
      }

      if (lCambioPos) {
        InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizioneDiArrivo,
        		lPosGiuModel, lEveModel.getDataEmissione(), lEveMod, aFascicolo.getIdFascicoloSiep(),
            aEvento.getIdEvento());
      }

      // SETTA LO STATO PROCEDIMENTO
      String lStato = null;
      if (lEveModel.getCodMotivo().equals("2007"))
        lStato = "0572";     
      else if (lEveModel.getCodMotivo().equals("1403"))
        lStato = "0573";    
      else if (lEveModel.getCodMotivo().equals("1414"))
        lStato = "0574";
      // con richiesta verbale
      else if (lEveModel.getCodMotivo().equals("5427"))
        lStato = "5475";     
      else if (lEveModel.getCodMotivo().equals("5428"))
        lStato = "0576";    
      else if (lEveModel.getCodMotivo().equals("5429"))
        lStato = "0577";         

      // Delete/Insert
      InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveMod, lStato);

      // aggancio/duplico l'ultima PR a sistema
      InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveMod, aFascicolo.getIdFascicoloSiep(), "N");

      // aggiorna misura alternativa - Data Inizio Misura - Data Fine Misura
      lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
      lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
      PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

      // aggiorna misura alternativa - Data Inizio Misura - Data Fine Misura
      lMiDAO = new MisuraAlternativaDAO(lConn);
      String lAggiorna = "N";
      if (lMisModelOrder != null) {
        lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());

        if (lMisModelOrder.getDataInizioMisura() == null && !lPosGiuModel.isLibero()) {
          if (lMisModelOrder.getDataScarcerazione() != null) {
            lMiDAO.setDataInizioMisura(lMisModelOrder.getDataScarcerazione());
            lAggiorna = "S";
          } else {
            lMiDAO.setDataInizioMisura(lMisModelOrder.getDataDecisione());
            lAggiorna = "S";
          }
        }

        if (lPenResMod != null && lPenResMod.getDataFine() != null) {
          lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
          lAggiorna = "S";
        }

        if (lAggiorna.equals("S")) {
          lMiDAO.update();
        }

        lMiDAO.stop();
      }

      // ** Aggiorna SCADENZARIO** 13 - Misura Altrenativa
      // Se presente si aggiorna data:
      // -- Data Inizio = lData
      // -- Data Fine   = lPenResMod.getDataFine()
      if (   lMisModelOrder != null 
          && lMisModelOrder.getDataInizioMisura() != null
          && lMisModelOrder.getDataFineMisura() == null) // ????
      {
        Date lData = lMisModelOrder.getDataInizioMisura();
        InserimentoAggiornamentoScadenzarioMisuraAlternativa(lConn, lData, lPenResMod, lEveModel,
            aFascicolo.getIdFascicoloSiep());
      }

      // aggiorna il luogo detenzione
      if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {
        lLuogoDAO = new LuogoDetenzioneDAO(lConn);
        lLuogoDAO.setDataInserimento(aEvento.getDataAggiornamento());
        lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
        lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
        lLuogoDAO.setAltroLuogo(lMisModelOrder.getDescrLuogoProva());
        lLuogoDAO.setCondizioneIdPosizioneGiuridica(lPosGiuModel.getIdPosizioneGiuridica());
        lLuogoDAO.insert();
        lLuogoDAO.stop();
      }

      // ------- EVENTO-------
      lEveDaoBlob = new EventoDAO(lConn);
      lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

      lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
      lEveDaoBlob.update();
      lEveDaoBlob.stop();
      // ---------------------
      commit(lConn);
    } catch (DAOException daoEx) {
      siesLogger.error("DAOException: ", daoEx);
      rollback(lConn);
      throw new F3BException("MisuraAternativaIndultinoController.ExUpdateValidaMAAmmProvSemiliberta : " + daoEx);
    } catch (Exception ex) {
      siesLogger.error("Exception: ", ex);
      rollback(lConn);
      throw new F3BException("MisuraAternativaIndultinoController.ExUpdateValidaMAAmmProvSemiliberta : " + ex);
    } finally {
      cleanup(lEveSqlDao);
      cleanup(lPenResSqlDao);
      cleanup(lMiDAO);
      cleanup(lLuogoDAO);
      cleanup(lMisSqlDAO);
      cleanup(lEveDaoMisAlt);
      
      cleanup(lConn);

      cleanup(lEveDaoBlob);
    }

    return lEveMod;
  }
}
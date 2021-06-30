package siap.siep.rinnovo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.parametro.dao.ParametroSqlDAO;
import siap.siep.rinnovo.dao.RinnovoDAO;
import siap.siep.rinnovo.dao.RinnovoSqlDAO;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.dao.VerbaleDAO;
import siap.siep.verbale.model.VerbaleModel;

/**
 * <p>
 * Title: RinnovoController
 * </p>
 * <p>
 * Description: Classe Controller per Rinnovo
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
public class RinnovoController extends SiapController implements IRinnovo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public RinnovoModel ExInserisciRinnovoVerbale(RinnovoModel aRinnovo, VerbaleModel aVerbale)
			throws F3BException {

		Connection lConn = null;

		RinnovoDAO lRinDao = null;
		VerbaleDAO lVerDao = null;
		RinnovoModel lRinMod = null;

		try {
			lConn = getDBTransaction();

			lRinDao = new RinnovoDAO(lConn);
			lVerDao = new VerbaleDAO(lConn);

			// inserisco il verbale solo se nn c'è l'id del verbale
			BigDecimal lKeyVer = null;
			if (aVerbale != null && aVerbale.getIdVerbale() == null) {
				lVerDao.setDAOFromModel(aVerbale);

				lKeyVer = lVerDao.insert();
				lVerDao.stop();
			} else {
				lKeyVer = aVerbale.getIdVerbale();
			}

			// inserisco il rinnovo
			aRinnovo.setVerIdVerbale(lKeyVer);
			lRinMod = new RinnovoModel(aRinnovo);

			lRinDao.setDAOFromModel(lRinMod);
			BigDecimal lKey = null;
			lKey = lRinDao.insert();

			commit(lConn);

			lRinMod.setIdRinnovo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("RinnovoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lRinDao);
			cleanup(lVerDao);

			cleanup(lConn);
		}

		return lRinMod;
	}

	public RinnovoModel ExInserisciRinnovo(RinnovoModel aRinnovo) throws F3BException {

		Connection lConn = null;

		RinnovoDAO lRinDao = null;
		RinnovoModel lRinMod = new RinnovoModel(aRinnovo);

		try {
			lConn = getDBTransaction();

			lRinDao = new RinnovoDAO(lConn);

			// inserisco il rinnovo
			lRinDao.setDAOFromModel(lRinMod);
			BigDecimal lKey = null;
			lKey = lRinDao.insert();

			commit(lConn);
			lRinMod.setIdRinnovo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);

			throw new F3BException("RinnovoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lRinDao);

			cleanup(lConn);
		}

		return lRinMod;
	}

	public Vector ExInserisciRinnovo(Vector aRinnovo) throws F3BException {

		Connection lConn = null;

		RinnovoDAO lRinDao = null;
		Vector idRinnovi = null;

		try {
			lConn = getDBTransaction();
			lRinDao = new RinnovoDAO(lConn);
			BigDecimal lKey = null;
			idRinnovi = new Vector();

			// inserisco il rinnovo
			for (int i = 0; i < aRinnovo.size(); i++) {
				RinnovoModel lRinMod = new RinnovoModel();
				lRinMod = (RinnovoModel) aRinnovo.get(i);
				lRinDao.setDAOFromModel(lRinMod);

				lKey = lRinDao.insert();
				lRinMod.setIdRinnovo(lKey);
				idRinnovi.add(lRinMod);
				lRinDao.stop();
			}
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("RinnovoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lRinDao);
			cleanup(lConn);
		}
		return idRinnovi;
	}

	public List ExRicercaRinnovoIdNotifica(BigDecimal aIdNotifica) throws F3BException {

		Connection lConn = null;

		List lRinnovi = new ArrayList();

		RinnovoSqlDAO lRinDao = null;

		try {
			lConn = getDBConnection();

			lRinDao = new RinnovoSqlDAO(lConn);

			lRinDao.ricercaRinnovoIdNotifica(aIdNotifica);

			lRinnovi = new ArrayList(lRinDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RinnovoController.ExRicercaRinnovoIdNotifica: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRinDao);

			cleanup(lConn);
		}

		return lRinnovi;
	}

	public Vector ExRicercaRinnovoIdNotificaCodTipoRinnovo(BigDecimal aIdNotifica, String[] aTipoRinnovo)
			throws F3BException {

		Connection lConn = null;

		Vector lRinnovi = new Vector();
		RinnovoSqlDAO lRinDao = null;

		try {
			lConn = getDBConnection();

			lRinDao = new RinnovoSqlDAO(lConn);

			lRinDao.ricercaRinnovoIdNotificaCodTipoRinnovo(aIdNotifica, aTipoRinnovo);

			lRinnovi = new Vector(lRinDao.getModels());
		} catch (DAOException daoEx) {

			throw new F3BException(
					"RinnovoController.ExRicercaRinnovoIdNotificaCodTipoRinnovo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRinDao);

			cleanup(lConn);
		}

		return lRinnovi;
	}

	public RinnovoModel ExRicercaRinnovoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RinnovoSqlDAO lRinDao = null;
		RinnovoModel lRinMod;

		try {
			lConn = getDBConnection();
			lRinDao = new RinnovoSqlDAO(lConn);
			lRinDao.ricercaRinnovoByKey(aKey);
			lRinMod = (RinnovoModel) lRinDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("RinnovoController.ExRicercaRinnovo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRinDao);
			cleanup(lConn);
		}
		return lRinMod;
	}

	// Paolo Cherubini 06/02/2012
	// Aggiunto metodo relativamente alla segnalazione bb/rr/004 v.a. StatoEsecuzioneController
	// prendo il più recente
	public RinnovoModel ExRicercaRinnovoByKeyEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RinnovoSqlDAO lRinDao = null;
		RinnovoModel lRinMod;

		try {
			lConn = getDBConnection();
			lRinDao = new RinnovoSqlDAO(lConn);
			lRinDao.ricercaRinnovoByKeyEvento(aKey);
			lRinMod = (RinnovoModel) lRinDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RinnovoController.ExRicercaRinnovoByKeyEvento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRinDao);
			cleanup(lConn);
		}
		return lRinMod;
	}
	
	/**
	 * Ticket#202106220110 - per la stampa mi interessa solo un rinnovo validato con
	 * DATA_RINNOVO valorizzata
	 * Metodo aggiunto in sostituzione del metodo ExRicercaRinnovoByKeyEvento precedentemente 
	 * utilizzato.
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public RinnovoModel ExRicercaUltimoRinnovoByKeyEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RinnovoSqlDAO lRinDao = null;
		RinnovoModel lRinMod;

		try {
			lConn = getDBConnection();
			lRinDao = new RinnovoSqlDAO(lConn);
			lRinDao.ricercaUltimoRinnovoByKeyEvento(aKey);
			lRinMod = (RinnovoModel) lRinDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RinnovoController.ExRicercaUltimoRinnovoByKeyEvento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRinDao);
			cleanup(lConn);
		}
		return lRinMod;
	}	
	

	public RinnovoModel ExModificaRinnovo(RinnovoModel aRinnovo) throws F3BException {

		Connection lConn = null;
		RinnovoDAO lRinDao = null;
		RinnovoModel lRinMod = new RinnovoModel(aRinnovo);

		try {
			lConn = getDBConnection();
			lRinDao = new RinnovoDAO(lConn);
			lRinDao.setDAOFromModelForUpdate(aRinnovo);
			lRinDao.update();
			lRinDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("RinnovoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRinDao);
			cleanup(lConn);
		}
		return lRinMod;
	}

	public void ExCancellaRinnovo(RinnovoModel aRinnovo) throws F3BException {

		Connection lConn = null;
		RinnovoDAO lRinDao = null;
		NotificaSqlDAO lNotSqlDao = null;
		RinnovoSqlDAO lRinSqlDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		ScadenzarioDAO lScaDao = null;

		try {
			lConn = getDBTransaction();
			lRinDao = new RinnovoDAO(lConn);
			lNotSqlDao = new NotificaSqlDAO(lConn);
			lRinSqlDao = new RinnovoSqlDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);

			// ricerca rinnovo da cancellare
			lRinSqlDao.ricercaRinnovoByKey(aRinnovo.getIdRinnovo());
			RinnovoModel lRinnovoMod = (RinnovoModel) lRinSqlDao.getModelByKey();
			lRinSqlDao.stop();

			// cancella rinnovo
			lRinDao.setCondizioneUpdate(lRinnovoMod.getIdRinnovo());
			lRinDao.delete();
			lRinDao.stop();

			// ricerca notifica
			lNotSqlDao.ricercaNotificaByKey(lRinnovoMod.getNotIdNotifica());
			NotificaModel lNotMod = (NotificaModel) lNotSqlDao.getModelByKey();
			lNotSqlDao.stop();

			// ricerca notifiche per il metodo di calcolo della data avvenuta notifica
			// e dello stato notifica!!
			lNotSqlDao.ricercaNotificaByEvento(lNotMod.getEveIdEvento());
			List lNotifiche = (List) lNotSqlDao.getModels();
			lNotSqlDao.stop();

			// ricerca Scadenzario
			lScaSqlDao.ricercaScadenzarioByIdEvento(lNotMod.getEveIdEvento());
			ScadenzarioModel lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			// ricerca rinnovo da mandare
			lRinSqlDao.ricercaRinnovoIdNotifica(lNotMod.getIdNotifica());
			RinnovoModel lRinnovoModel = (RinnovoModel) lRinSqlDao.getModelByKey();
			lRinSqlDao.stop();

			// decide lo stato della notifica
			IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			Date lDataAvvenutaNotifica = lCtrl
					.calcolaDataMaggiore((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
			String lCoStatoNotifica = lCtrl.calcolaCodStatoNotifica(lNotifiche, lDataAvvenutaNotifica,
					lRinnovoModel);

			// Aggiorna lo stato della notifica nella tabella scandezario
			lScaDao.setCodStatoNotifica(lCoStatoNotifica);
			lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
			lScaDao.setCodOperatoreAggiornamento(lRinnovoMod.getCodOperatoreInserimento());
			lScaDao.setDataAggiornamento(DateUtils.getSysDate());
			lScaDao.setCodUfficioAggiornamento(lRinnovoMod.getCodUfficioInserimento());

			lScaDao.update();
			lScaDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("RinnovoController.ExCancellaRinnovo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRinDao);
			cleanup(lNotSqlDao);
			cleanup(lRinSqlDao);
			cleanup(lScaSqlDao);
			cleanup(lScaDao);
			cleanup(lConn);
		}
	}

	public RinnovoModel ExUpdateValidaRinnovo(FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
			throws F3BException {

		Connection lConn = null;

		RinnovoDAO lRinDaoBlob = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		ParametroSqlDAO lParSqlDao = null;
		RinnovoSqlDAO lRinSql = null;

		try {
			lConn = getDBTransaction();

			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);
			lParSqlDao = new ParametroSqlDAO(lConn);
			lRinSql = new RinnovoSqlDAO(lConn);

			// ricerca rinnovo
			RinnovoModel lRinMod = new RinnovoModel();
			lRinSql.ricercaRinnovoByKey(aRinnovo.getIdRinnovo());
			lRinMod = (RinnovoModel) lRinSql.getModelByKey();

			// AMBROSINO 30/06/2011 - Su segnalazione di Pina Marchese viene tolta la parte che va ad
			// aggiornare
			// la data scadenza dello scadenzario tipo PP 03
			/*
			 * //ricerca scadenzario lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("03",
			 * aFasc.getIdFascicoloSiep()); ScadenzarioModel lScaMod = new ScadenzarioModel(); lScaMod =
			 * (ScadenzarioModel)lScaSqlDao.getModelByKey();
			 *
			 * //ricerca parametro lParSqlDao.ricercaParametroScadenzario("VANE RICERCHE PERVENUTO",aRinnovo.
			 * getCodUfficioAggiornamento()); Vector lVectPar = new Vector(lParSqlDao.getModels());
			 *
			 * if(lRinMod != null && lRinMod.getDataRinnovo()!= null) { Iterator lIter = lVectPar.iterator();
			 * Date lSommaAnni = null; Date lSommaMesi = null; Date lFineScadenza = null;
			 *
			 * if (lIter.hasNext()) { ParametroModel lParModel = (ParametroModel) lIter.next(); lSommaAnni =
			 * DateUtils.moveDateTo(lRinMod.getDataRinnovo(), java.util.Calendar.YEAR,
			 * lParModel.getAnni().intValue()); lSommaMesi = DateUtils.moveDateTo(lSommaAnni,
			 * java.util.Calendar.MONTH, lParModel.getMesi().intValue()); lFineScadenza =
			 * DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
			 * lParModel.getGiorni().intValue()); } // AMBROSINO -- In caso di Rinnovo NON va cambiata la data
			 * Inizio Scadenza // lScaDao.setDataInizioScadenza(lRinMod.getDataRinnovo());
			 * lScaDao.setDataFineScadenza(lFineScadenza); }
			 *
			 * if(lScaMod != null && lScaMod.getIdScadenzario() != null) {
			 * lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
			 * lScaDao.setCodOperatoreAggiornamento(aRinnovo.getCodOperatoreAggiornamento());
			 * lScaDao.setCodUfficioAggiornamento(aRinnovo.getCodUfficioAggiornamento());
			 * lScaDao.setDataAggiornamento(DateUtils.getSysDate());
			 *
			 * lScaDao.update(); lScaDao.stop(); } else { lScaDao.setCodTipoScadenzario("03");
			 * lScaDao.setFasSieIdFascicoloSiep(aFasc.getIdFascicoloSiep());
			 * lScaDao.setCodOperatoreInserimento(aRinnovo.getCodOperatoreAggiornamento());
			 * lScaDao.setCodUfficioInserimento(aRinnovo.getCodUfficioAggiornamento());
			 * lScaDao.setDataInserimento(DateUtils.getSysDate()); // a6-rr-238 -11-2010
			 * lScaDao.setCodStatoNotifica("PP"); lScaDao.setDataInizioScadenza(lRinMod.getDataRinnovo()); //
			 * lScaDao.insert(); lScaDao.stop(); }
			 */
			// Fine AMBROSINO 30/06/2011

			// Stato procedimento
			String lStatoProcMod = null;

			if (lRinMod != null && lRinMod.getCodTipoRinnovo() != null
					&& lRinMod.getCodTipoRinnovo().equals("R")) {
				lStatoProcMod = "0110";
			} else if (lRinMod != null && lRinMod.getCodTipoRinnovo() != null
					&& lRinMod.getCodTipoRinnovo().equals("N")) {
				lStatoProcMod = "0111";
			} else if (lRinMod != null && lRinMod.getCodTipoRinnovo() != null
					&& lRinMod.getCodTipoRinnovo().equals("A")) {
				lStatoProcMod = "0112";
			}

			aRinnovo.setDataRinnovo(lRinMod.getDataRinnovo());
			InserimentoCancellazioneStatoProcedimento(lConn, aFasc.getIdFascicoloSiep(), aRinnovo,
					lStatoProcMod);

			lRinDaoBlob = new RinnovoDAO(lConn);
			lRinDaoBlob.setDAOFromModelForUpdateBlob(aRinnovo);

			lRinDaoBlob.setCondizioneUpdate(aRinnovo.getIdRinnovo());
			lRinDaoBlob.update();
			lRinDaoBlob.stop();

			// Ricalcola lo stato dello scadenzario Simeone
			aggiornaStatoNotificaScadenzarioSimeone(lConn, lScaDao, lRinMod, aFasc.getIdFascicoloSiep());

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("RinnovoController.ExUpdateValidaRinnovo : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("RinnovoController.ExUpdateValidaRinnovo : " + ex);
		} finally {
			cleanup(lRinDaoBlob);
			cleanup(lScaSqlDao);
			cleanup(lScaDao);
			cleanup(lParSqlDao);
			cleanup(lRinSql);

			cleanup(lConn);
		}

		return aRinnovo;
	}

	private void aggiornaStatoNotificaScadenzarioSimeone(Connection aConn, ScadenzarioDAO aScaDao,
			RinnovoModel aRinMod, BigDecimal aIdFasicolo) throws F3BException {

		NotificaDAO lNotDao = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("aRinMod : " + aRinMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("aRinMod.getNotIdNotifica() : " + aRinMod.getNotIdNotifica());

			// Ricalcola lo stato dello scadenzario Simeone
			if (aRinMod != null && aRinMod.getNotIdNotifica() != null) {
				lNotDao = new NotificaDAO(aConn);
				lNotDao.setIdNotifica(aRinMod.getNotIdNotifica());
				lNotDao.selByKey();

				NotificaModel lNotMod = (NotificaModel) lNotDao.getModelByKey();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("lNotMod : " + lNotMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("lNotMod.getCodTipoNotifica() : " + lNotMod.getCodTipoNotifica());

				if (lNotMod != null && "E".equals(lNotMod.getCodTipoNotifica())) {
					IOrdineEsecuzione lCtrlOE = SIEPLookupRemote.getOrdineEsecuzioneRemote();

					List lListNotifica = new ArrayList();
					lListNotifica.add(lNotMod);

					String lCodStatoNotifica = lCtrlOE.calcolaCodStatoNotifica(lListNotifica, null, aRinMod);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("lCodStatoNotifica : " + lCodStatoNotifica);

					// Controlla l'esistenza dello scadenzario
					aScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aIdFasicolo, "01");
					ScadenzarioModel lScaSimeoneMod = (ScadenzarioModel) aScaDao.getModelByKey();
					aScaDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("lScaSimeoneMod : " + lScaSimeoneMod);

					if (lScaSimeoneMod != null) {
						aScaDao.setCodStatoNotifica(lCodStatoNotifica);
						aScaDao.setDataFineScadenza(null); // Per pulire eventuali dati sporchi su questa data

						aScaDao.setCodOperatoreAggiornamento(aRinMod.getCodOperatoreInserimento());
						aScaDao.setCodUfficioAggiornamento(aRinMod.getCodUfficioInserimento());
						aScaDao.setDataAggiornamento(DateUtils.getSysDate());

						aScaDao.setCondizioneUpdate(lScaSimeoneMod.getIdScadenzario());
						aScaDao.update();
						aScaDao.stop();
					}
				}
			}
			// FINE Ricalcola lo stato dello scadenzario Simeone
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			throw new F3BException("RinnovoController.aggiornaStatoNotificaScadenzarioSimeone : " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new F3BException("RinnovoController.aggiornaStatoNotificaScadenzarioSimeone : " + ex);
		} finally {
			cleanup(lNotDao);
		}
	}

	public RinnovoModel ExUpdateValidaRinnovazioneNotifiche(FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
			throws F3BException {

		Connection lConn = null;

		RinnovoDAO lRinDaoBlob = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		RinnovoSqlDAO lRinSql = null;

		try {
			lConn = getDBTransaction();

			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);
			lRinSql = new RinnovoSqlDAO(lConn);

			// ricerca rinnovo
			RinnovoModel lRinMod = new RinnovoModel();
			lRinSql.ricercaRinnovoByKey(aRinnovo.getIdRinnovo());
			lRinMod = (RinnovoModel) lRinSql.getModelByKey();

			// ricerca scadenzario
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("01", aFasc.getIdFascicoloSiep());
			ScadenzarioModel lScaMod = new ScadenzarioModel();
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.setCodOperatoreAggiornamento(aRinnovo.getCodOperatoreAggiornamento());
				lScaDao.setCodUfficioAggiornamento(aRinnovo.getCodUfficioAggiornamento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				lScaDao.setDataInizioScadenza(lRinMod.getDataRinnovo());
				lScaDao.setDataFineScadenza(
						DateUtils.moveDateTo(lRinMod.getDataRinnovo(), java.util.Calendar.DAY_OF_MONTH, 30));
				lScaDao.update();
				lScaDao.stop();
			} else {
				lScaDao.setDataInizioScadenza(lRinMod.getDataRinnovo());
				lScaDao.setDataFineScadenza(
						DateUtils.moveDateTo(lRinMod.getDataRinnovo(), java.util.Calendar.DAY_OF_MONTH, 30));
				lScaDao.setCodTipoScadenzario("01");
				lScaDao.setFasSieIdFascicoloSiep(aFasc.getIdFascicoloSiep());
				lScaDao.setCodOperatoreInserimento(aRinnovo.getCodOperatoreAggiornamento());
				lScaDao.setCodUfficioInserimento(aRinnovo.getCodUfficioAggiornamento());
				lScaDao.setDataInserimento(DateUtils.getSysDate());
				lScaDao.insert();
				lScaDao.stop();
			}

			// Stato procedimento
			aRinnovo.setDataRinnovo(lRinMod.getDataRinnovo());
			InserimentoCancellazioneStatoProcedimento(lConn, aFasc.getIdFascicoloSiep(), aRinnovo, "0114");

			lRinDaoBlob = new RinnovoDAO(lConn);
			lRinDaoBlob.setDAOFromModelForUpdateBlob(aRinnovo);

			lRinDaoBlob.setCondizioneUpdate(aRinnovo.getIdRinnovo());
			lRinDaoBlob.update();
			lRinDaoBlob.stop();

			// Ricalcola lo stato dello scadenzario Simeone
			aggiornaStatoNotificaScadenzarioSimeone(lConn, lScaDao, lRinMod, aFasc.getIdFascicoloSiep());

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("RinnovoController.ExUpdateValidaRinnovazioneNotifiche : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("RinnovoController.ExUpdateValidaRinnovazioneNotifiche : " + ex);
		} finally {
			cleanup(lRinDaoBlob);
			cleanup(lScaSqlDao);
			cleanup(lScaDao);
			cleanup(lRinSql);

			cleanup(lConn);
		}

		return aRinnovo;
	}

	public RinnovoModel ExUpdateValidaRich8Bis(FascicoloSiepModel aFasc, Vector aRinnovi)
			throws F3BException {

		Connection lConn = null;

		RinnovoSqlDAO lRinSqlDao = null;
		RinnovoDAO lRinDaoBlob = null;
		ScadenzarioDAO lScaDao = null;

		RinnovoModel lRinMod = null;

		try {
			lConn = getDBTransaction();

			lRinSqlDao = new RinnovoSqlDAO(lConn);
			lRinDaoBlob = new RinnovoDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);

			lRinMod = (RinnovoModel) aRinnovi.get(0);

			// ******************************************
			if (lRinMod != null && lRinMod.getIdRinnovo() != null) {
				lRinSqlDao.ricercaRinnovoByKey(lRinMod.getIdRinnovo());
				lRinMod = (RinnovoModel) lRinSqlDao.getModelByKey();
			}
			// ******************************************

			// Stato procedimento
			InserimentoCancellazioneStatoProcedimento(lConn, aFasc.getIdFascicoloSiep(), lRinMod, "0113");

			for (int i = 0; i < aRinnovi.size(); i++) {
				RinnovoModel lRinModel = (RinnovoModel) aRinnovi.get(i);
				lRinDaoBlob.setDAOFromModelForUpdateBlob(lRinModel);

				lRinDaoBlob.setCondizioneUpdate(lRinModel.getIdRinnovo());
				lRinDaoBlob.update();
				lRinDaoBlob.stop();
			}

			// Ricalcola lo stato dello scadenzario Simeone
			aggiornaStatoNotificaScadenzarioSimeone(lConn, lScaDao, lRinMod, aFasc.getIdFascicoloSiep());

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("RinnovoController.ExUpdateValidaRich8Bis : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("RinnovoController.ExUpdateValidaRich8Bis : " + ex);
		} finally {
			cleanup(lRinSqlDao);
			cleanup(lRinDaoBlob);
			cleanup(lScaDao);

			cleanup(lConn);
		}

		return lRinMod;
	}

	public RinnovoModel ExUpdateDocument(RinnovoModel aRinnovo) throws F3BException {

		Connection lConn = null;
		RinnovoDAO lRinDao = null;
		RinnovoModel lRinMod = new RinnovoModel(aRinnovo);

		try {
			lConn = getDBConnection();
			lRinDao = new RinnovoDAO(lConn);
			lRinDao.setDAOFromModelForUpdateBlob(aRinnovo);
			lRinDao.setCondizioneUpdate(aRinnovo.getIdRinnovo());
			lRinDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("RinnovoController.ExUpdateDocument: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRinDao);
			cleanup(lConn);
		}
		return lRinMod;
	}

	public RinnovoModel ExUpdateValidaSolleciti(FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
			throws F3BException {

		Connection lConn = null;

		RinnovoDAO lRinDaoBlob = null;
		RinnovoSqlDAO lRinSql = null;
		ScadenzarioDAO lScaDao = null;

		try {
			lConn = getDBTransaction();

			lRinSql = new RinnovoSqlDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);

			// ricerca rinnovo
			RinnovoModel lRinMod = new RinnovoModel();
			lRinSql.ricercaRinnovoByKey(aRinnovo.getIdRinnovo());
			lRinMod = (RinnovoModel) lRinSql.getModelByKey();

			// Stato procedimento
			aRinnovo.setDataRinnovo(lRinMod.getDataRinnovo());
			InserimentoCancellazioneStatoProcedimento(lConn, aFasc.getIdFascicoloSiep(), aRinnovo, "0116");

			lRinDaoBlob = new RinnovoDAO(lConn);
			lRinDaoBlob.setDAOFromModelForUpdateBlob(aRinnovo);

			lRinDaoBlob.setCondizioneUpdate(aRinnovo.getIdRinnovo());
			lRinDaoBlob.update();
			lRinDaoBlob.stop();

			// Ricalcola lo stato dello scadenzario Simeone
			aggiornaStatoNotificaScadenzarioSimeone(lConn, lScaDao, lRinMod, aFasc.getIdFascicoloSiep());

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("RinnovoController.ExUpdateValidaSolleciti : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("RinnovoController.ExUpdateValidaSolleciti : " + ex);
		} finally {
			cleanup(lRinDaoBlob);
			cleanup(lRinSql);
			cleanup(lScaDao);

			cleanup(lConn);
		}

		return aRinnovo;
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 *
	 * @param aProvvedimento
	 * @return Array con il Documento recuperato dal DB
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExGetDocumento(RinnovoModel aRinnovo) throws F3BException {

		Connection lConn = null;

		RinnovoDAO lRinDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBConnection();

			lRinDao = new RinnovoDAO(lConn);

			lRinDao.setIdRinnovo(aRinnovo.getIdRinnovo());
			lRinDao.selByKey();

			lRinDao.start(1);

			if (lRinDao.next())
				lByteArrayOut = lRinDao.getDocBlob();

			lRinDao.stop();

			if (lByteArrayOut == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");

			if (lByteArrayOut.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");
		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanup(lRinDao);

			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	// metodi privati
	private void InserimentoCancellazioneStatoProcedimento(Connection lConn, BigDecimal aKey,
			RinnovoModel lRinModel, String lStatoProcMod) throws DAOException, F3BException {

		StatoProcedimentoDAO lStatoDao = null;

		try {
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// cancellazione
			lStatoDao.setCondizioneByIdFascicolo(aKey);
			lStatoDao.delete();
			lStatoDao.stop();

			// inserimento
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(lRinModel.getDataRinnovo());
			lStatoDao.setCodStatoProcedimento(lStatoProcMod);
			lStatoDao.setFasSieIdFascicoloSiep(aKey);
			lStatoDao.setCodOperatoreInserimento(lRinModel.getCodOperatoreAggiornamento());
			lStatoDao.setDataInserimento(lRinModel.getDataAggiornamento());
			lStatoDao.setCodUfficioInserimento(lRinModel.getCodUfficioAggiornamento());
			lStatoDao.insert();
		} finally {
			cleanup(lStatoDao);
		}
	}

}
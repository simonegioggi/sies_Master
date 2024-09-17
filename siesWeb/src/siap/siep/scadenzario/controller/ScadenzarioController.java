package siap.siep.scadenzario.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoSimeoneSqlDAO;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.SIEPException;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSoggettoSqlDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;

/**
 * <p>
 * Title: ScadenzarioController
 * </p>
 * <p>
 * Description: Classe Controller per Scadenzario
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
public class ScadenzarioController extends SiapController implements IScadenzario {

	public ScadenzarioModel ExInserisciScadenzario(ScadenzarioModel aScadenzario) throws F3BException {

		Connection lConn = null;

		ScadenzarioDAO lScaDao = null;

		ScadenzarioModel lScaMod = null;

		try {
			lConn = getDBConnection();

			lScaMod = new ScadenzarioModel(aScadenzario);
			lScaDao = new ScadenzarioDAO(lConn);

			lScaDao.setDAOFromModel(lScaMod);

			BigDecimal lKey = null;
			lKey = lScaDao.insert();

			commit(lConn);

			lScaMod.setIdScadenzario(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ScadenzarioController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}

		return lScaMod;
	}

	// MEV_39: aggiunto parametro di passaggio
	public Vector ExRicercaScadenzario(ScadenzarioModel aScadenzario, String tipoRicerca)
			throws F3BException {

		Connection lConn = null;

		Vector lScadenzari = new Vector();

		ScadenzarioSoggettoSqlDAO lScaSogDao = null;

		try {
			lConn = getDBConnection();

			lScaSogDao = new ScadenzarioSoggettoSqlDAO(lConn);

			// MEV_39: aggiunto parametro di passaggio
			lScaSogDao.ricercaScadenzarioCompleta(aScadenzario, tipoRicerca);
			if ("CSMS".equals(tipoRicerca)) {
				lScaSogDao.start();
				while (lScaSogDao.next()) {
					lScadenzari.add(lScaSogDao.getModelScadeMisSic());
				}
				lScaSogDao.stop();
			} else
				lScadenzari = new Vector(lScaSogDao.getModels());

			if (lScadenzari.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScadenzarioController.ExRicercaScadenzario: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScaSogDao);
			// cleanup(lFascDao);
			// cleanup(lSoggDao);
			cleanup(lConn);
		}
		return lScadenzari;
	}

	public Vector ExRicercaScadenzarioPaged(ScadenzarioModel aScadenzario, int aPage) throws F3BException {

		Connection lConn = null;

		Vector lScadenzari = new Vector();

		ScadenzarioSoggettoSqlDAO lScaSogDao = null;

		try {
			lConn = getDBConnection();

			lScaSogDao = new ScadenzarioSoggettoSqlDAO(lConn);
			lScaSogDao.ricercaScadenzarioPagedCompleta(aScadenzario, aPage);

			lScadenzari = new Vector(lScaSogDao.getModels());

			if (lScadenzari.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExRicercaScadenzarioPaged: " + daoEx);
		} finally {
			cleanup(lScaSogDao);

			cleanup(lConn);
		}

		return lScadenzari;
	}

	// AMBROSINO 04-02-2011 Vers 5.1 - Su segnalazione di Marchese Aggiungo Data VVR alla ricerca Scadenzario
	// .
	// uery per caricare il vettore a 20 a 20 coi dati per poi passarlo alla jsp.
	public Vector ExRicercaScadenzarioVVRPaged(ScadenzarioModel aScadenzario, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lScadenzari = new Vector();
		ScadenzarioSoggettoSqlDAO lScaSogDao = null;

		try {
			lConn = getDBConnection();

			lScaSogDao = new ScadenzarioSoggettoSqlDAO(lConn);
			lScaSogDao.ricercaScadenzarioVVRPagedCompleta(aScadenzario, aPage);

			lScaSogDao.start();

			while (lScaSogDao.next()) {
				lScadenzari.add(lScaSogDao.getVVRModel());
			}

			lScaSogDao.stop();

			if (lScadenzari.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExRicercaScadenzarioVVRPaged 1 : " + daoEx);
		} finally {
			cleanup(lScaSogDao);
			cleanup(lConn);
		}

		return lScadenzari;
	}

	// AMBROSINO 04-02-2011 Vers 5.1 - Serve solo per la Conta del totale delle righe trovate
	// uso la query solo per il conteggio totale delle righe
	public BigDecimal ExGetCountScadenzarioVVRPaged(ScadenzarioModel aScadenzario, int aPage)
			throws F3BException {

		Connection lConn = null;
		BigDecimal HowManyRecords = null;
		ScadenzarioSoggettoSqlDAO lScaSogDao = null;

		try {
			lConn = getDBConnection();

			lScaSogDao = new ScadenzarioSoggettoSqlDAO(lConn);
			lScaSogDao.ricercaScadenzarioVVRPagedCompleta(aScadenzario, aPage);
			lScaSogDao.start();
			lScaSogDao.next();
			HowManyRecords = lScaSogDao.getBigDecimal("HowManyRecords");
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExGetCountScadenzarioVVRPaged 1 : " + daoEx);
		} finally {
			cleanup(lScaSogDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}
	// END AMBROSINO

	public List ExRicercaScadenzarioSimeonePaged(ScadenzarioModel aScadenzario, int aPage)
			throws F3BException {

		Connection lConn = null;

		List lScadenzari = new ArrayList();

		ScadenzarioSoggettoSqlDAO lScaSogDao = null;

		try {
			lConn = getDBConnection();

			lScaSogDao = new ScadenzarioSoggettoSqlDAO(lConn);

			lScaSogDao.ricercaScadenzarioSimeonePagedCompleta(aScadenzario, aPage);

			// lScaSogDao.ricercaScadenzarioPagedCompleta(aScadenzario,aPage);

			lScaSogDao.start();

			while (lScaSogDao.next()) {
				lScadenzari.add(lScaSogDao.getModelSimeone());
			}

			lScaSogDao.stop();

			if (lScadenzari.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
			/*
			 * lScadenzari = new Vector(lScaSogDao.getModels());
			 *
			 * if (lScadenzari.size() == 0) { throw new F3BException(F3BException.USER_MESSAGE,
			 * "Nessun Elemento trovato"); }
			 */
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExRicercaScadenzarioSimeonePaged: " + daoEx);
		} finally {
			cleanup(lScaSogDao);

			cleanup(lConn);
		}

		return lScadenzari;
	}

	public ScadenzarioModel ExRicercaScadenzarioByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		ScadenzarioModel lScaMod = new ScadenzarioModel();

		BigDecimal lFascID;
		FascicoloSiepModel lFascicolo = null;
		SentenzaModel lSentMod = null;
		SoggettoModel lSoggMod = null;

		ScadenzarioSqlDAO lScaDao = null;
		FascicoloSiepSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();

			lScaDao = new ScadenzarioSqlDAO(lConn);
			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			lScaDao.ricercaScadenzarioByKey(aKey);
			BigDecimal lSentenzaID;
			lScaMod = (ScadenzarioModel) lScaDao.getModelByKey();
			lFascID = lScaMod.getFasSieIdFascicoloSiep();

			// Cerca il fascicolo by key fascicolo
			lFascDao.ricercaFascicoloByKey(lFascID);
			lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

			lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();
			lFascicolo.setSoggetto(lSoggMod);
			lSentenzaID = lFascicolo.getSenIdSentenza();

			lSentDao.ricercaSentenzaBykey(lSentenzaID);
			lSentMod = (SentenzaModel) lSentDao.getModelByKey();
			lFascicolo.setSentenza(lSentMod);

			lScaMod.setFascicoloModel(lFascicolo);
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExRicercaScadenzarioByKey : " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}

		return lScaMod;
	}

	public ScadenzarioModel ExModificaScadenzario(ScadenzarioModel aScadenzario) throws F3BException {

		Connection lConn = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioModel lScaMod = new ScadenzarioModel(aScadenzario);

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setDAOFromModelForUpdate(aScadenzario);
			lScaDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ScadenzarioController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	public void ExCancellaScadenzario(ScadenzarioModel aScadenzario) throws F3BException {

		Connection lConn = null;
		ScadenzarioDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneUpdate(aScadenzario.getIdScadenzario());
			lScaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScadenzarioController.ExCancellaScadenzario: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaScadenzarioSimeone(ScadenzarioModel aScadenzario) throws F3BException {

		Connection lConn = null;

		ScadenzarioDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneForDelete(aScadenzario.getIdScadenzario());
			lScaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScadenzarioController.ExCancellaScadenzarioSimeone: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScaDao);

			cleanup(lConn);
		}
	}

	public List ExScadenzarioByIdFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		ScadenzarioSqlDAO lScaDao = null;

		List lScadenzario = new ArrayList();

		try {
			lConn = getDBConnection();

			lScaDao = new ScadenzarioSqlDAO(lConn);

			lScaDao.ricercaScadenzarioByIdFascicolo(aKey);

			lScadenzario = new Vector(lScaDao.getModels());

			if (lScadenzario.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExScadenzarioByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lScaDao);

			cleanup(lConn);
		}

		return lScadenzario;
	}

	public ScadenzarioModel ExScadenzarioCorrenteByIdFascicoloTipoScadenzario(BigDecimal aIdFascicolo,
			String aCodTipoScadenzario) throws F3BException {

		Connection lConn = null;

		ScadenzarioModel lScaMod = new ScadenzarioModel();

		ScadenzarioSqlDAO lScaDao = null;

		try {
			lConn = getDBConnection();

			lScaDao = new ScadenzarioSqlDAO(lConn);

			lScaDao.ricercaScadenzarioCorrenteByIdFascicoloCodTipoScadenzario(aIdFascicolo,
					aCodTipoScadenzario);

			lScaMod = (ScadenzarioModel) lScaDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScadenzarioController.ExScadenzarioCorrenteByIdFascicoloTipoScadenzario : " + daoEx);
		} finally {
			cleanup(lScaDao);

			cleanup(lConn);
		}

		return lScaMod;
	}

	/**
	 * Aggiorna lo scadenzario FINE PENA più recente, se presente, altrimenti lo inserisce
	 *
	 * @param aScadenzario
	 */
	public ScadenzarioModel ExRicercaInserisciAggScadenzarioIdFascicoloCorrente(ScadenzarioModel aScadenzario)
			throws F3BException {

		Connection lConn = null;

		ScadenzarioSqlDAO lScaDao = null;
		ScadenzarioDAO lScadeDao = null;

		ScadenzarioModel lScadenzario = new ScadenzarioModel();

		try {
			lConn = getDBConnection();

			lScaDao = new ScadenzarioSqlDAO(lConn);
			lScadeDao = new ScadenzarioDAO(lConn);

			lScaDao.ricercaScadenzarioByIdFascicoloCorrente(aScadenzario.getFasSieIdFascicoloSiep());

			lScadenzario = (ScadenzarioModel) lScaDao.getModelByKey();

			if (lScadenzario == null) {
				// inserisco scadenzario
				aScadenzario.setCodTipoScadenzario("02");
				lScadeDao.setDAOFromModel(aScadenzario);

				BigDecimal lKey = null;
				lKey = lScadeDao.insert();
				commit(lConn);

				aScadenzario.setIdScadenzario(lKey);
				lScadenzario = aScadenzario;

			} else if (lScadenzario != null) {
				// aggiorno scadenzario
				lScadenzario.setCodOperatoreAggiornamento(aScadenzario.getCodOperatoreInserimento());
				lScadenzario.setCodUfficioAggiornamento(aScadenzario.getCodUfficioInserimento());
				lScadenzario.setDataAggiornamento(aScadenzario.getDataInserimento());
				lScadenzario.setDataInizioScadenza(aScadenzario.getDataInizioScadenza());
				lScadenzario.setDataFineScadenza(aScadenzario.getDataFineScadenza());

				lScadeDao.setDAOFromModelForUpdate(lScadenzario);
				lScadeDao.update();
				commit(lConn);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScadenzarioController.ExRicercaInserisciAggScadenzarioIdFascicoloCorrente: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lScadeDao);

			cleanup(lConn);
		}

		return lScadenzario;
	}

	public Vector ExRicercaScadenzarioVerbaleArresto(ScadenzarioModel aScadenzario) throws F3BException {

		Connection lConn = null;

		Vector lScadenzarii;
		ScadenzarioSqlDAO lScaDao = null;
		FascicoloSiepSqlDAO lFascDao = null;
		FascicoloSiepModel lFascicolo = null;
		SoggettoSqlDAO lSoggDao = null;
		SoggettoModel lSoggMod = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSqlDAO(lConn);
			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);

			lScaDao.ricercaScadenzarioVerbaleArresto(aScadenzario);
			lScadenzarii = new Vector(lScaDao.getModels());

			if (lScadenzarii.size() == 0) {
				// throw new F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
			} else {
				ScadenzarioModel lScadModTmp = new ScadenzarioModel();
				BigDecimal lFascID;

				for (int i = 0; i <= lScadenzarii.size() - 1; i++) {
					lScadModTmp = (ScadenzarioModel) lScadenzarii.get(i);
					lFascID = lScadModTmp.getFasSieIdFascicoloSiep();

					// Cerca il fascicolo by key fascicolo
					lFascDao.ricercaFascicoloByKey(lFascID);

					lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

					// Cerca il soggetto associato al fascicolo
					lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
					lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();
					lFascicolo.setSoggetto(lSoggMod);

					lScadModTmp.setFascicoloModel(lFascicolo);

					lScadenzarii.set(i, lScadModTmp);
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScadenzarioController.ExRicercaScadenzario: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lSoggDao);
			cleanup(lFascDao);

			cleanup(lConn);
		}

		return lScadenzarii;
	}

	public BigDecimal ExGetCountScadenzari(ScadenzarioModel aScadenzario) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		ScadenzarioSqlDAO lScaDao = new ScadenzarioSqlDAO(lConn);

		try {
			lConn = getDBConnection();

			lScaDao = new ScadenzarioSqlDAO(lConn);
			lScaDao.getCountScadenzari(aScadenzario);
			lScaDao.start();
			lScaDao.next();

			lCount = lScaDao.getBigDecimal("HowManyRecords");
			lScaDao.stop();
		} catch (DAOException daoEx) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"ScadenzarioController.ExGetCountScadenzari: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}

		return lCount;
	}

	public BigDecimal ExGetCountScadenzarioSimeone(ScadenzarioModel aScadenzario) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		ScadenzarioSoggettoSqlDAO lScaDao = new ScadenzarioSoggettoSqlDAO(lConn);

		try {
			lConn = getDBConnection();

			lScaDao = new ScadenzarioSoggettoSqlDAO(lConn);
			// lScaDao.getCountScadenzarioSimeone(aScadenzario);
			lScaDao.getCountScadenzari(aScadenzario);
			lScaDao.start();
			lScaDao.next();

			lCount = lScaDao.getBigDecimal("HowManyRecords");
			lScaDao.stop();
		} catch (DAOException daoEx) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"ScadenzarioController.ExGetCountScadenzarioSimeone: " + daoEx);
		} finally {
			cleanup(lScaDao);

			cleanup(lConn);
		}

		return lCount;
	}

	public ScadenzarioModel ExRicercaScadenzarioCorrenteByIdFascicoloIdNotifica(BigDecimal aKey,
			BigDecimal aKeyNot, String aTipSca) throws F3BException {

		Connection lConn = null;

		ScadenzarioSqlDAO lScaDao = null;

		ScadenzarioModel lScadenzario = new ScadenzarioModel();

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSqlDAO(lConn);

			lScaDao.ricercaScadenzarioByIdFascicoloIdNotifica(aKey, aKeyNot, aTipSca);
			lScadenzario = (ScadenzarioModel) lScaDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScadenzarioController.ExRicercaScadenzarioCorrenteByIdFascicoloIdNotifica: " + daoEx);
		} finally {
			cleanup(lScaDao);

			cleanup(lConn);
		}

		return lScadenzario;
	}

	// AMBROSINO Decreto L78/2013
	public BigDecimal ExGetCountTrasmessiL78del2013(ScadenzarioModel aScadenzario, Boolean Attivi,
			String acoduffcoll, Boolean nostato) throws F3BException {

		BigDecimal HowManyRecords = null;
		Connection lConn = null;
		EventoSimeoneSqlDAO lEveFasDao = null;

		try {
			lConn = getDBConnection();

			lEveFasDao = new EventoSimeoneSqlDAO(lConn);
			lEveFasDao.ricercaEventoL78del2013(aScadenzario, Attivi, acoduffcoll, nostato);
			lEveFasDao.start();
			lEveFasDao.next();

			HowManyRecords = lEveFasDao.getBigDecimal("HowManyRecords");
			lEveFasDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExGetCountTrasmessiL78del2013: " + daoEx);
		} finally {
			cleanup(lEveFasDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	} // Chiude ExGetCountTrasmessiL78del2013

	public List ExRicercaTrasmessiL78del2013Paged(ScadenzarioModel aScadenzario, int aPage, Boolean Attivi,
			String acoduffcoll, Boolean nostato) throws F3BException {

		Connection lConn = null;
		List lEventi = new ArrayList();
		EventoSimeoneSqlDAO lEveFasDao = null;

		try {
			lConn = getDBConnection();

			lEveFasDao = new EventoSimeoneSqlDAO(lConn);
			lEveFasDao.ricercaEventoL78del2013Completa(aScadenzario, aPage, Attivi, acoduffcoll, nostato);
			lEveFasDao.start();

			while (lEveFasDao.next()) {
				lEventi.add(lEveFasDao.getModelL78del2013());
			}

			lEveFasDao.stop();

			if (lEventi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}

		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExRicercaTrasmessiL78del2013Paged: " + daoEx);
		} finally {
			cleanup(lEveFasDao);
			cleanup(lConn);
		}

		return lEventi;
	}

	public Vector ExRicercaScadenzarioPagedMisSic(ScadenzarioModel aScadenzario, int aPage)
			throws F3BException {

		Connection lConn = null;

		Vector lScadenzari = new Vector();

		ScadenzarioSoggettoSqlDAO lScaSogDao = null;

		try {
			lConn = getDBConnection();

			lScaSogDao = new ScadenzarioSoggettoSqlDAO(lConn);
			lScaSogDao.ricercaScadenzarioPagedCompletaMisSic(aScadenzario, aPage);
			lScaSogDao.start();

			while (lScaSogDao.next()) {
				lScadenzari.add(lScaSogDao.getModelScadeMisSic());
			}

			lScaSogDao.stop();

			if (lScadenzari.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExRicercaScadenzarioPaged: " + daoEx);
		} finally {
			cleanup(lScaSogDao);

			cleanup(lConn);
		}

		return lScadenzari;
	}

	// MEV_39: aggiunti nuovi metodi di ricerca
	public ScadenzarioModel ExRicercaScadenzarioCSMSByKey(BigDecimal idScadenzario,
			String codUfficioUtenteConnesso) throws F3BException {

		Connection lConn = null;

		ScadenzarioModel lScaMod = new ScadenzarioModel();

		BigDecimal lFascID;
		FascicoloSiepModel lFascicolo = null;
		SentenzaModel lSentMod = null;
		SoggettoModel lSoggMod = null;

		ScadenzarioSoggettoSqlDAO lScaSogDao = null;
		FascicoloSiepSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();

			lScaSogDao = new ScadenzarioSoggettoSqlDAO(lConn);
			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			lScaSogDao.ricercaScadenzarioCSMSByKey(idScadenzario, codUfficioUtenteConnesso);
			lScaSogDao.start();
			lScaSogDao.next();
			lScaMod = (ScadenzarioModel) lScaSogDao.getModelScadeMisSic();
			lScaSogDao.stop();
			lFascID = lScaMod.getFasSieIdFascicoloSiep();

			// Cerca il fascicolo by key fascicolo
			lFascDao.ricercaFascicoloByKey(lFascID);
			lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

			lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();
			lFascicolo.setSoggetto(lSoggMod);
			BigDecimal lSentenzaID = lFascicolo.getSenIdSentenza();

			lSentDao.ricercaSentenzaBykey(lSentenzaID);
			lSentMod = (SentenzaModel) lSentDao.getModelByKey();
			lFascicolo.setSentenza(lSentMod);

			lScaMod.setFascicoloModel(lFascicolo);
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExRicercaScadenzarioByKey : " + daoEx);
		} finally {
			cleanup(lScaSogDao);
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}

		return lScaMod;
	}

	public BigDecimal ExGetCountScadenzariDifferimentoMS(ScadenzarioModel aScadenzario) throws F3BException {

		BigDecimal count = new BigDecimal(0);
		Connection c = null;
		ScadenzarioSoggettoSqlDAO sssDAO = null;

		try {
			c = getDBConnection();
			sssDAO = new ScadenzarioSoggettoSqlDAO(c);
			sssDAO.getCountScadenzariDifferimentoMS(aScadenzario);
			sssDAO.start();
			sssDAO.next();
			count = sssDAO.getBigDecimal("HowManyRecords");
			sssDAO.stop();
		} catch (DAOException daoEx) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"ScadenzarioController.ExGetCountScadenzariDifferimentoMS: Non posso leggere : " + daoEx);
		} finally {
			cleanup(sssDAO);
			cleanup(c);
		}
		// valore di ritorno
		return count;
	}

	public Vector ExRicercaScadenzarioDifferimentoMSPaged(ScadenzarioModel aScadenzario, int aPage)
			throws F3BException {

		Connection c = null;
		Vector scadenzari = new Vector();
		ScadenzarioSoggettoSqlDAO sssDAO = null;

		try {
			c = getDBConnection();
			sssDAO = new ScadenzarioSoggettoSqlDAO(c);
			sssDAO.ricercaScadenzarioDifferimentoMSPaged(aScadenzario, aPage);
			scadenzari = new Vector(sssDAO.getModels());
			if (scadenzari.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExRicercaScadenzarioDifferimentoMSPaged: " + daoEx);
		} finally {
			cleanup(sssDAO);
			cleanup(c);
		}
		// valore di ritorno
		return scadenzari;
	}

	// 20191121 [SG]: aggiunto metodo
	public BigDecimal ExGetCountRicercaScadenzarioPagedMisSic(ScadenzarioModel sm) throws F3BException {

		Connection c = null;
		BigDecimal bd = null;
		ScadenzarioSoggettoSqlDAO sssDAO = null;

		try {
			c = getDBConnection();
			sssDAO = new ScadenzarioSoggettoSqlDAO(c);
			sssDAO.ricercaScadenzarioPagedCompletaMisSic(sm, 0);
			sssDAO.start();
			sssDAO.next();
			bd = sssDAO.getBigDecimal("totale");
		} catch (DAOException daoEx) {
			throw new F3BException("ScadenzarioController.ExGetCountScadenzarioVVRPaged 1 : " + daoEx);
		} finally {
			cleanup(sssDAO);
			cleanup(c);
		}

		return bd;
	}
	// FINE MEV_39

	// MEV_2023-33
	 public BigDecimal ExGetCountScadenzariPP (ScadenzarioModel aScadenzario) throws F3BException {

	    BigDecimal lCount = new BigDecimal(0);
	    Connection lConn = null;

	    ScadenzarioSoggettoSqlDAO lScaSqlDao = new ScadenzarioSoggettoSqlDAO(lConn);

	    try {
	      lConn = getDBConnection();

	      lScaSqlDao = new ScadenzarioSoggettoSqlDAO(lConn);
	      lScaSqlDao.getCountScadenzariPP(aScadenzario);
	      lScaSqlDao.start();
	      lScaSqlDao.next();

	      lCount = lScaSqlDao.getBigDecimal("HowManyRecords");
	      lScaSqlDao.stop();
	    } catch (DAOException daoEx) {
	      throw new SIEPException(SIEPException.USER_MESSAGE,
	          "ScadenzarioController.ExGetCountScadenzariPP: Non posso leggere : " + daoEx);
	    } finally {
	      cleanup(lScaSqlDao);
	      cleanup(lConn);
	    }

	    return lCount;
	  }
	 
	 
	 public Vector ExRicercaScadenzarioPagedPP (ScadenzarioModel aScadenzario, int aPage) throws F3BException {

	    Connection lConn = null;

	    Vector lScadenzari = new Vector();

	    ScadenzarioSoggettoSqlDAO lScaSogSqlDao = null;

	    try {
	      lConn = getDBConnection();

	      lScaSogSqlDao = new ScadenzarioSoggettoSqlDAO(lConn);
	      lScaSogSqlDao.ricercaScadenzarioPagedPPCompleta(aScadenzario, aPage);
	      lScaSogSqlDao.start();
        while (lScaSogSqlDao.next()) {
          lScadenzari.add(lScaSogSqlDao.getModelScadePP());
        }
        lScaSogSqlDao.stop();

	      if (lScadenzari.size() == 0) {
	        throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
	      }
	    } catch (DAOException daoEx) {
	      throw new F3BException("ScadenzarioController.ExRicercaScadenzarioPagedPP: " + daoEx);
	    } finally {
	      cleanup(lScaSogSqlDao);

	      cleanup(lConn);
	    }

	    return lScadenzari;
	  }
	// MEV_2023-33 - FINE
	
} // CHIUDE CLASSE ScadenzarioController
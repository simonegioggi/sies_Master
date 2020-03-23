package siap.sius.sanzionesostitutiva.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneDAO;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneSqlDAO;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;

/**
 * <p>
 * Title: PeriodoAltraSanzioneController
 * </p>
 * <p>
 * Description: Classe Controller per PeriodoAltraSanzione
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
public class PeriodoAltraSanzioneController extends SiapController implements IPeriodoAltraSanzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * 1 Inserimento Periodo Altra Sanzione
	 ****************************************************************************/
	public PeriodoAltraSanzioneModel ExInserisciPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod)
			throws F3BException {

		Connection lConn = null;
		PeriodoAltraSanzioneDAO lPerDao = null;
		try {

			lConn = getDBConnection();

			// inserimento Periodo Altra Sanzione
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.setDAOFromModel(lPerMod);
			BigDecimal lSequence = lPerDao.insert();
			lPerMod = new PeriodoAltraSanzioneModel(lPerMod);
			lPerMod.setMessage("Inserimento avvenuto correttamente!");
			lPerMod.setIdPeriodoAltraSanzione(lSequence);

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraSanzioneController.ExInserisciPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 2 modifica Periodo Altra Sanzione
	 ****************************************************************************/
	public PeriodoAltraSanzioneModel ExModificaPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod)
			throws F3BException {

		Connection lConn = null;
		PeriodoAltraSanzioneDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.setDAOFromModelforUpdate(lPerMod);
			lPerDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraSanzioneController.ExModificaPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 3 modifica Periodo Altra Sanzione + Ess Inserimento Evento + scadenzario
	 ****************************************************************************/
	public PeriodoAltraSanzioneModel ExModificaPeriodoAltraSanzione(EventoModel aEveMod,
			ScadenzarioSiusModel lScaMod, PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lEssMod) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		PeriodoAltraSanzioneDAO lPerDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;

		try {
			lConn = getDBTransaction();

			// inserimento evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEveMod);
			BigDecimal lKeyEvento = lEveDao.insert();

			// inserisci scadenzario
			lScaMod.setEveIdEvento(lKeyEvento);
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setDAOFromModel(lScaMod);
			lScaDao.insert();

			// modifica Periodo Altra Sanzione
			lPerMod.setEveIdEvento(lKeyEvento);
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.setDAOFromModelforUpdate(lPerMod);
			lPerDao.update();

			// modifica Esecuzione Sanzione Sostitutiva
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEssMod);
			lEseDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraSanzioneController.ExModificaPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lScaDao);
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 4 cancella Periodo Altra Sanzione (inizio sanzione) Modifica Ess cancella Evento + scadenzario
	 ****************************************************************************/
	public void ExCancellaPeriodoAltraSanzione(ScadenzarioSiusModel lScaMod,
			PeriodoAltraSanzioneModel lPerMod, EsecuzioneSanzioneSostitutivaModel lEssMod)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		PeriodoAltraSanzioneDAO lPerDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;

		try {
			lConn = getDBTransaction();

			// cancellazione scadenzario (18/03/2010 subordinata all'esistenza di ScaMod)
			if (lScaMod != null && lScaMod.getIdScadenzarioSius() != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzarioSius());
				lScaDao.delete();
			}

			// cancellazione periodo
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.selCondizioneUpdate(lPerMod.getIdPeriodoAltraSanzione());
			lPerDao.delete();

			// cancellazione evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.selCondizioneUpdate(lPerMod.getEveIdEvento());
			lEveDao.delete();

			// modifica Esecuzione Sanzione Sostitutiva
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEssMod);
			lEseDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraSanzioneController.ExCancellaPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lScaDao);
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * 5 Inserimento Periodo Altra Sanzione e Modifica ESS inserimento scadenzario eventuale modifica altro
	 * scadenzario
	 *****************************************************************************/
	public PeriodoAltraSanzioneModel ExInserisciPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod,
			ScadenzarioSiusModel lScaMod1, EsecuzioneSanzioneSostitutivaModel lEssMod,
			ScadenzarioSiusModel lScaMod2) throws F3BException {

		Connection lConn = null;
		PeriodoAltraSanzioneDAO lPerDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBTransaction();

			// inserimento Periodo Altra Sanzione
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.setDAOFromModel(lPerMod);
			BigDecimal lSequence = lPerDao.insert();
			lPerMod = new PeriodoAltraSanzioneModel(lPerMod);
			lPerMod.setMessage("Inserimento avvenuto correttamente!");
			lPerMod.setIdPeriodoAltraSanzione(lSequence);

			// inserisci scadenzario
			// lScaMod1.setEveIdEvento(lPerMod.getIdPeriodoAltraSanzione());
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setDAOFromModel(lScaMod1);
			lScaDao.insert();

			// modifica Esecuzione Sanzione Sostitutiva
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEssMod);
			lEseDao.update();

			// eventuale modifica scadenzario
			if (lScaMod2 != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setDAOFromModelForUpdate(lScaMod2);
				lScaDao.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraSanzioneController.ExInserisciPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 5bis Inserimento Periodo Altra Sanzione e Modifica ESS modifica scadenzario eventuale modifica altro
	 * scadenzario
	 *****************************************************************************/
	public PeriodoAltraSanzioneModel ExInserisciPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lEssMod, ScadenzarioSiusModel lScaMod1,
			ScadenzarioSiusModel lScaMod2) throws F3BException {

		Connection lConn = null;
		PeriodoAltraSanzioneDAO lPerDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBTransaction();

			// inserimento Periodo Altra Sanzione
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.setDAOFromModel(lPerMod);
			BigDecimal lSequence = lPerDao.insert();
			lPerMod = new PeriodoAltraSanzioneModel(lPerMod);
			lPerMod.setMessage("Inserimento avvenuto correttamente!");
			lPerMod.setIdPeriodoAltraSanzione(lSequence);

			// modifica Esecuzione Sanzione Sostitutiva
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEssMod);
			lEseDao.update();

			// modifica scadenzario
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setDAOFromModelForUpdate(lScaMod1);
			lScaDao.update();

			// eventuale modifica scadenzario
			if (lScaMod2 != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setDAOFromModelForUpdate(lScaMod2);
				lScaDao.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraSanzioneController.ExInserisciPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 6 modifica Periodo Altra Sanzione + Ess modifica scadenzario eventuale modifica altro scadenzario
	 ****************************************************************************/
	public PeriodoAltraSanzioneModel ExModificaPeriodoAltraSanzione(ScadenzarioSiusModel lScaMod1,
			ScadenzarioSiusModel lScaMod2, PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lEssMod) throws F3BException {

		Connection lConn = null;
		ScadenzarioSiusDAO lScaDao = null;
		PeriodoAltraSanzioneDAO lPerDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;

		try {
			lConn = getDBTransaction();

			// modifica scadenzario sospensione
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setDAOFromModelForUpdate(lScaMod1);
			lScaDao.update();

			// eventuale modifica altro scadenzario
			if (lScaMod2 != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setDAOFromModelForUpdate(lScaMod2);
				lScaDao.update();
			}

			// modifica Periodo Altra Sanzione
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.setDAOFromModelforUpdate(lPerMod);
			lPerDao.update();

			// modifica Esecuzione Sanzione Sostitutiva
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEssMod);
			lEseDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraSanzioneController.ExModificaPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 7 cancella Periodo Altra Sanzione (ripresa sanzione) Modifica Ess eventuale cancella scadenzario
	 * eventuale modifiche di altri 2 scadenzari
	 ****************************************************************************/
	public void ExCancellaPeriodoAltraSanzione(ScadenzarioSiusModel lScaMod1, ScadenzarioSiusModel lScaMod2,
			ScadenzarioSiusModel lScaMod3, BigDecimal idPerMod, EsecuzioneSanzioneSostitutivaModel lEssMod)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		PeriodoAltraSanzioneDAO lPerDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;

		try {
			lConn = getDBTransaction();

			// eventuale cancellazione scadenzario
			if (lScaMod1 != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setDAOFromModelForUpdate(lScaMod1);
				lScaDao.delete();
			}

			// eventuale modifica altro scadenzario
			if (lScaMod2 != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setDAOFromModelForUpdate(lScaMod2);
				lScaDao.update();
			}

			// eventuale modifica altro scadenzario
			if (lScaMod3 != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setDAOFromModelForUpdate(lScaMod3);
				lScaDao.update();
			}

			// modifica Esecuzione Sanzione Sostitutiva
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEssMod);
			lEseDao.update();

			// cancellazione periodo
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.selCondizioneUpdate(idPerMod);
			lPerDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraSanzioneController.ExCancellaPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lScaDao);
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati PeriodoAltraSanzione
	 *
	 * @param lEssMod
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod) throws F3BException {

		Connection lConn = null;
		Vector lPeriodoAltraSanzioni = new Vector();
		PeriodoAltraSanzioneDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraSanzioneDAO(lConn);
			lPerDao.setCondizioni(lPerMod);
			lPerDao.setOrderBy();
			lPerDao.start();
			while (lPerDao.next()) {
				lPeriodoAltraSanzioni.add(lPerDao.getModel());
			}
			lPerDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraSanzioneController.ExRicercaPeriodoAltraSanzione: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}

		return lPeriodoAltraSanzioni;
	}

	/****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ***************************************************************************/
	public PeriodoAltraSanzioneModel ExRicercaPeriodoAltraSanzioneById(BigDecimal aIdPeriodoAltraSanzione)
			throws F3BException {

		Connection lConn = null;
		PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
		PeriodoAltraSanzioneSqlDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraSanzioneSqlDAO(lConn);
			lPerDao.ricercaPeriodoAltraSanzioneByKey(aIdPeriodoAltraSanzione);
			lPerMod = (PeriodoAltraSanzioneModel) lPerDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraSanzioneController.ExRicercaPeriodoAltraSanzioneById: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}

		return lPerMod;
	}

	/*****************************************************************************/
	public List ExRicercaSanzioneSostitutivaByIdFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		PeriodoAltraSanzioneSqlDAO lPerDao = null;
		List lLisPer = new ArrayList();

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraSanzioneSqlDAO(lConn);
			lPerDao.ricercaSanzioneSostitutivaByIdFascicolo(aKey);
			lLisPer = new ArrayList(lPerDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraSanzioneController.ExRicercaSanzioneSostitutivaByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lLisPer;
	}

	/*****************************************************************************/
	public List ExRicercaSanzioneSostitutivaByIdSiep(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		PeriodoAltraSanzioneSqlDAO lPerDao = null;
		List lLisPer = new ArrayList();

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraSanzioneSqlDAO(lConn);
			lPerDao.ricercaSanzioneSostitutivaByIdSiep(aKey);
			lLisPer = new ArrayList(lPerDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraSanzioneController.ExRicercaSanzioneSostitutivaByIdSiep: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lLisPer;
	}

	/*****************************************************************************
	 * Effettua la ricerca per id evento
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 * @param lEssMod
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public PeriodoAltraSanzioneModel ExRicercaSanzioneSostitutivaByIdEvento(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
		PeriodoAltraSanzioneSqlDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraSanzioneSqlDAO(lConn);
			lPerDao.ricercaSanzioneSostitutivaByIdEvento(aKey);
			lPerMod = (PeriodoAltraSanzioneModel) lPerDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraSanzioneController.ExRicercaSanzioneSostitutivaByIdEvento: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

}
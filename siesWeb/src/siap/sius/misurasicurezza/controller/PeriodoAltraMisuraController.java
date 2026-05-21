package siap.sius.misurasicurezza.controller;

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
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaDAO;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.dao.PeriodoAltraMisuraDAO;
import siap.sius.misurasicurezza.dao.PeriodoAltraMisuraSqlDAO;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.misurasicurezza.model.ProvvedimentoEventoTenoreFascicoloSiusModel;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;

/**
 * <p>
 * Title: PeriodoAltraMisuraController
 * </p>
 * <p>
 * Description: Classe Controller per PeriodoAltraMisura
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
public class PeriodoAltraMisuraController extends SiapController implements IPeriodoAltraMisura {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * 1 Inserimento Periodo Altra Misura
	 ****************************************************************************/
	public PeriodoAltraMisuraModel ExInserisciPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod)
			throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraDAO lPerDao = null;
		try {

			lConn = getDBConnection();

			// inserimento Periodo Altra Misura
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.setDAOFromModel(lPerMod);
			BigDecimal lSequence = lPerDao.insert();
			lPerMod = new PeriodoAltraMisuraModel(lPerMod);
			lPerMod.setMessage("Inserimento avvenuto correttamente!");
			lPerMod.setIdPeriodoAltraMisura(lSequence);

			commit(lConn);

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExInserisciPeriodoAltraMisura: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 2 modifica Periodo Altra Misura
	 ****************************************************************************/

	public PeriodoAltraMisuraModel ExModificaPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod)
			throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.setDAOFromModelforUpdate(lPerMod);
			lPerDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExModificaPeriodoAltraMisura: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 3 modifica Periodo Altra Misura + Ems Inserimento Evento + scadenzario
	 ****************************************************************************/
	public PeriodoAltraMisuraModel ExModificaPeriodoAltraMisura(EventoModel aEveMod,
			ScadenzarioSiusModel lScaMod, PeriodoAltraMisuraModel lPerMod,
			EsecuzioneMisuraSicurezzaModel lEmsMod) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		PeriodoAltraMisuraDAO lPerDao = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;

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

			// modifica Periodo Altra Misura
			lPerMod.setEveIdEvento(lKeyEvento);
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.setDAOFromModelforUpdate(lPerMod);
			lPerDao.update();

			// modifica Esecuzione Misura Sicurezza
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEmsMod);
			lEseDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExModificaPeriodoAltraMisura: " + daoEx);
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
	 * 4 cancella Periodo Altra Misura (inizio misura) Modifica Ems cancella Evento + scadenzario
	 ****************************************************************************/
	public void ExCancellaPeriodoAltraMisura(ScadenzarioSiusModel lScaMod, PeriodoAltraMisuraModel lPerMod,
			EsecuzioneMisuraSicurezzaModel lEmsMod) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		PeriodoAltraMisuraDAO lPerDao = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;

		try {
			lConn = getDBTransaction();

			// cancellazione scadenzario (18/03/2010 subordinata all'esistenza di ScaMod)
			if (lScaMod != null && lScaMod.getIdScadenzarioSius() != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzarioSius());
				lScaDao.delete();
			}

			// cancellazione periodo
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.selCondizioneUpdate(lPerMod.getIdPeriodoAltraMisura());
			lPerDao.delete();

			// cancellazione evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.selCondizioneUpdate(lPerMod.getEveIdEvento());
			lEveDao.delete();

			// modifica Esecuzione Misura Sicurezza
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEmsMod);
			lEseDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExCancellaPeriodoAltraMisura: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lScaDao);
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * 5 Inserimento Periodo Altra Misura e Modifica EMS inserimento scadenzario eventuale modifica altro
	 * scadenzario
	 *****************************************************************************/
	public PeriodoAltraMisuraModel ExInserisciPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod,
			ScadenzarioSiusModel lScaMod1, EsecuzioneMisuraSicurezzaModel lEmsMod,
			ScadenzarioSiusModel lScaMod2) throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraDAO lPerDao = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBTransaction();

			// inserimento Periodo Altra Misura
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.setDAOFromModel(lPerMod);
			BigDecimal lSequence = lPerDao.insert();
			lPerMod = new PeriodoAltraMisuraModel(lPerMod);
			lPerMod.setMessage("Inserimento avvenuto correttamente!");
			lPerMod.setIdPeriodoAltraMisura(lSequence);

			// inserisci scadenzario
			// lScaMod1.setEveIdEvento(lPerMod.getIdPeriodoAltraMisura());
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setDAOFromModel(lScaMod1);
			lScaDao.insert();

			// modifica Esecuzione Misura Sicurezza
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEmsMod);
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
			throw new F3BException("PeriodoAltraMisuraController.ExInserisciPeriodoAltraMisura: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 5bis Inserimento Periodo Altra Misura e Modifica EMS modifica scadenzario eventuale modifica altro
	 * scadenzario
	 *****************************************************************************/
	public PeriodoAltraMisuraModel ExInserisciPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod,
			EsecuzioneMisuraSicurezzaModel lEmsMod, ScadenzarioSiusModel lScaMod1,
			ScadenzarioSiusModel lScaMod2) throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraDAO lPerDao = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBTransaction();

			// inserimento Periodo Altra Misura
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.setDAOFromModel(lPerMod);
			BigDecimal lSequence = lPerDao.insert();
			lPerMod = new PeriodoAltraMisuraModel(lPerMod);
			lPerMod.setMessage("Inserimento avvenuto correttamente!");
			lPerMod.setIdPeriodoAltraMisura(lSequence);

			// modifica Esecuzione Misura Sicurezza
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEmsMod);
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
			throw new F3BException("PeriodoAltraMisuraController.ExInserisciPeriodoAltraMisura: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 6 modifica Periodo Altra Misura + Ems modifica scadenzario eventuale modifica altro scadenzario
	 ****************************************************************************/
	public PeriodoAltraMisuraModel ExModificaPeriodoAltraMisura(ScadenzarioSiusModel lScaMod1,
			ScadenzarioSiusModel lScaMod2, PeriodoAltraMisuraModel lPerMod,
			EsecuzioneMisuraSicurezzaModel lEmsMod) throws F3BException {

		Connection lConn = null;
		ScadenzarioSiusDAO lScaDao = null;
		PeriodoAltraMisuraDAO lPerDao = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;

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

			// modifica Periodo Altra Misura
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.setDAOFromModelforUpdate(lPerMod);
			lPerDao.update();

			// modifica Esecuzione Misura Sicurezza
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEmsMod);
			lEseDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExModificaPeriodoAltraMisura: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	/*****************************************************************************
	 * 7 cancella Periodo Altra Misura (ripresa misura) Modifica Ems eventuale cancella scadenzario eventuale
	 * modifiche di altri 2 scadenzari
	 ****************************************************************************/
	public void ExCancellaPeriodoAltraMisura(ScadenzarioSiusModel lScaMod1, ScadenzarioSiusModel lScaMod2,
			ScadenzarioSiusModel lScaMod3, BigDecimal idPerMod, EsecuzioneMisuraSicurezzaModel lEmsMod)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		PeriodoAltraMisuraDAO lPerDao = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;

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

			// modifica Esecuzione Misura Sicurezza
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEseDao.setDAOFromModelForUpdate(lEmsMod);
			lEseDao.update();

			// cancellazione periodo
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.selCondizioneUpdate(idPerMod);
			lPerDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExCancellaPeriodoAltraMisura: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lScaDao);
			cleanup(lPerDao);
			cleanup(lEseDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati PeriodoAltraMisura
	 *
	 * @param lEmsMod
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod) throws F3BException {

		Connection lConn = null;
		Vector lPeriodoAltraMisure = new Vector();
		PeriodoAltraMisuraDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.setCondizioni(lPerMod);
			lPerDao.setOrderBy();
			lPerDao.start();
			while (lPerDao.next()) {
				lPeriodoAltraMisure.add(lPerDao.getModel());
			}
			lPerDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExRicercaPeriodoAltraMisura: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}

		return lPeriodoAltraMisure;
	}

	/****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ***************************************************************************/
	public PeriodoAltraMisuraModel ExRicercaPeriodoAltraMisuraById(BigDecimal aIdPeriodoAltraMisura)
			throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraModel lPerMod = new PeriodoAltraMisuraModel();
		PeriodoAltraMisuraSqlDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraMisuraSqlDAO(lConn);
			lPerDao.ricercaPeriodoAltraMisuraByKey(aIdPeriodoAltraMisura);
			lPerMod = (PeriodoAltraMisuraModel) lPerDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExRicercaPeriodoAltraMisuraById: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}

		return lPerMod;
	}

	/*****************************************************************************/

	public List ExRicercaMisuraSicurezzaByIdFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraSqlDAO lPerDao = null;
		List lLisPer = new ArrayList();

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraMisuraSqlDAO(lConn);
			lPerDao.ricercaMisuraSicurezzaByIdFascicolo(aKey);
			lLisPer = new ArrayList(lPerDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraMisuraController.ExRicercaMisuraSicurezzaByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lLisPer;
	}

	/*****************************************************************************/
	public List ExRicercaMisuraSicurezzaByIdSiep(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraSqlDAO lPerDao = null;
		List lLisPer = new ArrayList();

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraMisuraSqlDAO(lConn);
			lPerDao.ricercaMisuraSicurezzaByIdSiep(aKey);
			lLisPer = new ArrayList(lPerDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PeriodoAltraMisuraController.ExRicercaMisuraSicurezzaByIdSiep: " + daoEx);
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
	 * @param lEmsMod
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public PeriodoAltraMisuraModel ExRicercaMisuraSicurezzaByIdEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraModel lPerMod = new PeriodoAltraMisuraModel();
		PeriodoAltraMisuraSqlDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraMisuraSqlDAO(lConn);
			lPerDao.ricercaMisuraSicurezzaByIdEvento(aKey);
			lPerMod = (PeriodoAltraMisuraModel) lPerDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraMisuraController.ExRicercaMisuraSicurezzaByIdEvento: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	public PeriodoAltraMisuraModel ExModificaDateInizioMisuraSicurezza(PeriodoAltraMisuraModel lPerMod)
			throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraDAO lPerDao = null;

		try {
			lConn = getDBConnection();
			lPerDao = new PeriodoAltraMisuraDAO(lConn);
			lPerDao.setDAOFromModelforUpdate(lPerMod);
			lPerDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraMisuraController.ExModificaDateInizioMisuraSicurezza: " + daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lPerMod;
	}

	//
	/**
	 * 06/02/2015 Revisione criterio di recupero informazioni Mis.Sic. lato Sorveglianza. Metodo di ricerca
	 * dei provvedimenti(Ord&Dec)/Esito/FascicoloSIUS in base all'IdFascicoloSIEP; L'interrogazione cerca nel
	 * FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP il procedimento SIEP Corrente e nel
	 * EVENTO.FAS_SIE_ID_FASCICOLO_SIEP il procedimento SIEP Corrente.
	 *
	 * <p>
	 *
	 * @param aFascSiepKey
	 *            id FASCICLO_SIEP
	 * @return dati dell'ordinanza.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaProvvedimentoEventoByFascicoloSiep(BigDecimal aFascSiepKey) throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraSqlDAO lPAMDao = null;
		Vector lProvvVec = new Vector();
		try {
			lConn = getDBConnection();
			lPAMDao = new PeriodoAltraMisuraSqlDAO(lConn);
			lPAMDao.ricercaProvvedimentiMisSicByIdFascicoloSiep(aFascSiepKey);

			lPAMDao.start();
			ProvvedimentoEventoTenoreFascicoloSiusModel lProvvMod = null;
			while (lPAMDao.next()) {
				lProvvMod = (ProvvedimentoEventoTenoreFascicoloSiusModel) lPAMDao.getModelEsitoMisSic();
				lProvvVec.add(lProvvMod);
			}
			lPAMDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"PeriodoAltraMisuraController.ExRicercaProvvedimentoEventoByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPAMDao);
			cleanup(lConn);
		}

		return lProvvVec;
	} // CHIUDE ExRicercaProvvedimentoEventoByFascicoloSiep

	/**
	 * Ticket#202602170130 - Si aggiunge metodo per recuperare i dati per IdEventoSIUS
	 * @param aKeyEventoSius
	 * @throws DAOException
	 */
	public ProvvedimentoEventoTenoreFascicoloSiusModel ExRicercaProvvedimentoEventoByIdEvento(BigDecimal aIdEventoSIUS) throws F3BException {

		Connection lConn = null;
		PeriodoAltraMisuraSqlDAO lPAMDao = null;
		ProvvedimentoEventoTenoreFascicoloSiusModel lProvvMod = null;
		try {
			lConn = getDBConnection();
			lPAMDao = new PeriodoAltraMisuraSqlDAO(lConn);
			lPAMDao.ricercaProvvedimentoMisSicByIdFascicoloEvento(aIdEventoSIUS);
			
			lPAMDao.start();
			lPAMDao.next();
			lProvvMod = (ProvvedimentoEventoTenoreFascicoloSiusModel) lPAMDao.getModelEsitoMisSic();
			lPAMDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: ", daoEx);
			throw new F3BException(
					"PeriodoAltraMisuraController.ExRicercaProvvedimentoEventoByIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPAMDao);
			cleanup(lConn);
		}

		return lProvvMod;
	}
}
package siap.sius.prescrizione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sius.SIUSException;
import siap.sius.prescrizione.dao.PrescrizioneDAO;
import siap.sius.prescrizione.dao.PrescrizioneSqlDAO;
import siap.sius.prescrizione.model.PrescrizioneModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PrescrizioneController
 * </p>
 * <p>
 * Description: Classe Controller per Prescrizione
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
public class PrescrizioneController extends SiapController implements IPrescrizione {

	/**
	 * Effettua l'inserimento delle prescrizioni, inoltre cancella eventuali record prescrizioni esistenti per
	 * l'id del deposito ordinanza.
	 * <p>
	 * 
	 * @param aPrescrizioni
	 *            Array di model prescrizioni da inserire.
	 * @param aKeyDepOrdPC
	 *            id del deposito ordinaza, utile per la cancellazione.
	 * @return Prescrizione model.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public PrescrizioneModel ExInserisciPrescrizioni(PrescrizioneModel[] aPrescrizioni,
			BigDecimal aKeyDepOrdPC) throws F3BException {
		Connection lConn = null;
		PrescrizioneDAO lPreDao = null;
		PrescrizioneModel lPreMod = null;

		try {
			lConn = getDBTransaction();
			lPreDao = new PrescrizioneDAO(lConn);

			// Effettua la delete di eventuali prescrizioni già inserite
			// per lo stesso id_depositoordinaza.
			// Preleva l'id Deposito orrdianza dal primo elemento della
			// prescrizione.
			lPreDao.setCondizioneByDepOrdPC(aKeyDepOrdPC);
			lPreDao.delete();
			lPreDao.stop();

			// Effettua l'inserimento delle nuove prescrizioni.
			int lSize = aPrescrizioni.length;
			for (int lIndex = 0; lIndex < lSize; lIndex++) {
				// Imposta nel model il progressivo prescrizione lIndex + 1.
				aPrescrizioni[lIndex].setProgrPrescrizione(new BigDecimal((double) lIndex + 1));
				lPreDao.setDAOFromModel(aPrescrizioni[lIndex]);
				lPreDao.insert();
				lPreDao.stop();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new SIUSException("PrescrizioneController.ExInserisciPrescrizioni: Non posso inserire: "
					+ ex);
		} finally {
			cleanup(lPreDao);
			cleanup(lConn);
		}
		return lPreMod;
	}

	/**
	 * Inserisce una prescrizione.
	 * <p>
	 * 
	 * @param aPrescrizione
	 *            model di prescrizione.
	 * @return il model di prescrizione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public PrescrizioneModel ExInserisciPrescrizione(PrescrizioneModel aPrescrizione) throws F3BException {
		Connection lConn = null;
		PrescrizioneDAO lPreDao = null;
		PrescrizioneModel lPreMod = null;

		try {
			lConn = getDBConnection();
			lPreMod = new PrescrizioneModel(aPrescrizione);
			lPreDao = new PrescrizioneDAO(lConn);
			lPreDao.setDAOFromModel(aPrescrizione);
			BigDecimal lKey = null;
			lKey = lPreDao.insert();
			commit(lConn);
			lPreMod.setIdPrescrizione(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new SIUSException("PrescrizioneController.ExInserisciPrescrizione: Non posso inserire: "
					+ ex);
		} finally {
			cleanup(lPreDao);
			cleanup(lConn);
		}
		return lPreMod;
	}

	/**
	 * Effettua la ricerca delle prescrizione per l'id del deposito ordinanza.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave deposito ordinanza per ricerca prescrizione.
	 * @return l'elenco delle prescrizioni per il dep ordinaza pc.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaPrescrizioneByOrdinanza(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lPrescrizioni = new Vector();
		PrescrizioneSqlDAO lPreDao = null;

		try {
			lConn = getDBConnection();
			lPreDao = new PrescrizioneSqlDAO(lConn);
			lPreDao.ricercaPrescrizioneByDepOrdinanzaPc(aKey);
			lPrescrizioni = new Vector(lPreDao.getModels());

			if (lPrescrizioni.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIUSException(
					"PrescrizioneController.ExRicercaPrescrizioneByOrdinanza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPreDao);
			cleanup(lConn);
		}
		return lPrescrizioni;
	}

	/**
	 * Effettua la ricerca delle prescrizione per l'id dell'evento collegato.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave evento per ricerca prescrizione.
	 * @return l'elenco delle prescrizioni .
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaPrescrizioneByEvento(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lPrescrizioni = new Vector();
		PrescrizioneSqlDAO lPreDao = null;

		try {
			lConn = getDBConnection();
			lPreDao = new PrescrizioneSqlDAO(lConn);
			lPreDao.ricercaPrescrizioneByIdEve(aKey);
			lPrescrizioni = new Vector(lPreDao.getModels());

			if (lPrescrizioni.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIUSException(
					"PrescrizioneController.ExRicercaPrescrizioneByEvento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPreDao);
			cleanup(lConn);
		}
		return lPrescrizioni;
	}

	/**
	 * Elenco delle prescrizioni
	 * <p>
	 * 
	 * @param aPrescrizione
	 *            model delle prescrizioni.
	 * @return elenco delle prescrizioni trovate.
	 * @throws F3BException
	 */
	public Vector ExRicercaPrescrizione(PrescrizioneModel aPrescrizione) throws F3BException {
		Connection lConn = null;
		Vector lPrescrizioni = new Vector();
		PrescrizioneSqlDAO lPreDao = null;

		try {
			lConn = getDBConnection();
			lPreDao = new PrescrizioneSqlDAO(lConn);
			lPreDao.ricercaPrescrizione(aPrescrizione);
			lPrescrizioni = new Vector(lPreDao.getModels());

			if (lPrescrizioni.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIUSException("PrescrizioneController.ExRicercaPrescrizione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lPreDao);
			cleanup(lConn);
		}

		return lPrescrizioni;
	}

	/**
	 * Ricerca una prescrizione per la propria chiave id
	 * <p>
	 * 
	 * @param aKey
	 *            chiave della prescrizione da ricercare.
	 * @return il model della prescrizione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public PrescrizioneModel ExRicercaPrescrizioneByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		PrescrizioneSqlDAO lPreDao = null;
		PrescrizioneModel lPreMod;

		try {
			lConn = getDBConnection();
			lPreDao = new PrescrizioneSqlDAO(lConn);
			lPreDao.ricercaPrescrizioneByKey(aKey);
			lPreMod = (PrescrizioneModel) lPreDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new SIUSException("PrescrizioneController.ExRicercaPrescrizione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lPreDao);
			cleanup(lConn);
		}
		return lPreMod;
	}

	/**
	 * Effettua la modifica di una prescrizione.
	 * <p>
	 * 
	 * @param aPrescrizione
	 *            prescrizione model.
	 * @return la prscrizione modifica.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public PrescrizioneModel ExModificaPrescrizione(PrescrizioneModel aPrescrizione) throws F3BException {
		Connection lConn = null;
		PrescrizioneDAO lPreDao = null;
		PrescrizioneModel lPreMod = new PrescrizioneModel(aPrescrizione);

		try {
			lConn = getDBConnection();
			lPreDao = new PrescrizioneDAO(lConn);
			lPreDao.setDAOFromModelForUpdate(aPrescrizione);
			lPreDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new SIUSException("PrescrizioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lPreDao);
			cleanup(lConn);
		}

		return lPreMod;
	}

	/**
	 * Effettua la cacellazione di una prescrizione.
	 * <p>
	 * 
	 * @param aPrescrizione
	 *            prescrizione model
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public void ExCancellaPrescrizione(PrescrizioneModel aPrescrizione) throws F3BException {
		Connection lConn = null;
		PrescrizioneDAO lPreDao = null;

		try {
			lConn = getDBConnection();
			lPreDao = new PrescrizioneDAO(lConn);
			lPreDao.setCondizioneUpdate(aPrescrizione.getIdPrescrizione());
			lPreDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new SIUSException("PrescrizioneController.ExCancellaPrescrizione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lPreDao);
			cleanup(lConn);
		}
	}

}
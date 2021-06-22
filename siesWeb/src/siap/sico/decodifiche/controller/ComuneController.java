package siap.sico.decodifiche.controller;

import java.sql.Connection;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.decodifiche.dao.ComuneDAO;
import siap.sico.decodifiche.dao.ComuneSqlDAO;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sius.SIUSException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComuneController extends SiapController implements IComune {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ComuneModel ExGetCodiceComune(ComuneModel lModel) throws F3BException {

		return ExGetComune(lModel, "N");
	}

	public ComuneModel ExGetCodiceComuneValidita(ComuneModel lModel) throws F3BException {

		return ExGetComune(lModel, "S");
	}

	public ComuneModel ExGetDescComune(ComuneModel lModel) throws F3BException {

		return ExGetComune(lModel, "N");
	}

	private ComuneModel ExGetComune(ComuneModel lModel, String FlagVal) throws F3BException {

		Connection lConn = null;
		ComuneDAO lComDao = null;
		ComuneModel lComMod = new ComuneModel();

		try {
			lConn = getDBConnection();

			lComDao = new ComuneDAO(lConn);

			lComDao.selCondizioni(lModel, FlagVal);

			lComDao.start();

			// Si prende il primo elemento trovato
			if (lComDao.next()) {
				lComMod = (ComuneModel) lComDao.getModel();
			} else {
				if (lModel.getDescrizione() != null && lModel.getDescrizione().length() > 0)
					throw new SICOException(SICOException.USER_MESSAGE,
							lModel.getDescrizione() + " - Comune non Esistente");
				else
					throw new SICOException(SICOException.USER_MESSAGE, "Comune non Esistente");
			}

			// Se è settato a true procedo con controllo omonimi
			if (lModel.getControlloOmonimi()) {

				// Controllo se sono tornate più occorrenze, quindi se esistono comuni omonimi
				if (lComDao.next()) {

					throw new SICOException(SICOException.USER_MESSAGE,
							"Esistono dei Comuni omonimi a quello inserito. Selezionare il Comune dall'elenco dei Comuni.");
				}
			}
		} catch (DAOException daoex) {
			throw new SICOException("ComuneModel.ExGetCodiceComune: Non posso leggere i comuni : " + daoex);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}

		return lComMod;
	}

	public Collection ExGetComuni() throws F3BException {

		Connection lConn = null;
		ComuneDAO lDao = null;

		Collection lCollComuni = new Vector();

		try {
			lConn = getDBConnection();

			lDao = new ComuneDAO(lConn);

			lDao.start();

			while (lDao.next()) {
				lCollComuni.add(lDao.getModel());
			}

			lDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException("ComuneController.ExGetComuni: Non posso leggere i comuni : " + daoex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lCollComuni;
	}

	public Vector ExGetListaComuni(ComuneModel lModel) throws F3BException {

		Connection lConn = null;
		ComuneDAO lDao = null;

		Vector lCollComuni = new Vector();

		try {
			lConn = getDBConnection();

			lDao = new ComuneDAO(lConn);
			lDao.selCondizioniLike(lModel);
			lDao.start();

			while (lDao.next()) {
				lCollComuni.add(lDao.getModel());
			}

			lDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException("ComuneController.ExGetListaComuni: " + daoex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lCollComuni;
	}

	// 20210517	MEV21
	public Vector ExGetListaComuniNascita(ComuneModel lModel) throws F3BException {

		Connection lConn = null;
		ComuneDAO lDao = null;

		Vector lCollComuni = new Vector();

		try {
			lConn = getDBConnection();

			lDao = new ComuneDAO(lConn);
			lDao.selCondizioniNascita(lModel);
			lDao.start();

			while (lDao.next()) {
				lCollComuni.add(lDao.getModel());
			}

			lDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException("ComuneController.ExGetListaComuni: " + daoex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lCollComuni;
	}
	
	public Vector ExGetListaComuniTds() throws F3BException {

		Connection lConn = null;
		ComuneSqlDAO lDao = null;

		Vector lCollComuni = new Vector();

		try {
			lConn = getDBConnection();

			lDao = new ComuneSqlDAO(lConn);
			lDao.ricercaComuneTds();
			lDao.start();

			while (lDao.next()) {
				lCollComuni.add(lDao.getModelComuneTds());
			}

			lDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException("ComuneController.ExGetComuni: Non posso leggere i comuni : " + daoex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lCollComuni;
	}

	public Vector ExGetListaComuniTdsm() throws F3BException {

		Connection lConn = null;
		ComuneSqlDAO lDao = null;

		Vector lCollComuni = new Vector();

		try {
			lConn = getDBConnection();

			lDao = new ComuneSqlDAO(lConn);
			lDao.ricercaComuneTdsm();
			lDao.start();

			while (lDao.next()) {
				lCollComuni.add(lDao.getModelComuneTds());
			}

			lDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException("ComuneController.ExGetComuni: Non posso leggere i comuni : " + daoex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lCollComuni;
	}

	/**
	 * Ricerca Comuni sedi UNEP per Distretto. La funzione restituisce l'elenco dei comuni (Vector di
	 * ComuneModel) appartenenti ad un Distretto (individuato da aCodDistretto ) che sono sedi di uffici UNEP.
	 *
	 * @param aCodDistretto
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExGetListaSediUNEPperDistretto(String aCodDistretto) throws F3BException {

		Connection lConn = null;
		ComuneSqlDAO lDao = null;

		Vector lCollComuni = new Vector();

		try {
			lConn = getDBConnection();

			lDao = new ComuneSqlDAO(lConn);
			lDao.ricercaSediUNEPperDistretto(aCodDistretto);
			lDao.start();

			while (lDao.next())
				lCollComuni.add(lDao.getModelComuneTds());
			lDao.stop();
		} catch (Exception sqex) {
			throw new SICOException(
					"ComuneController.ExGetListaSediUNEPperDistretto: Non posso leggere i comuni : " + sqex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lCollComuni;
	}

	/**
	 * Verifica se il Comune individuato dal suo codice può essere sede di ufficio UNEP.
	 *
	 * @param aCodComune
	 * @return boolean
	 * @throws F3BException
	 */
	public boolean ExIsComuneSedeUNEP(String aCodComune) throws F3BException {

		Connection lConn = null;
		ComuneSqlDAO lDao = null;

		boolean lEsiste = false;

		try {
			lConn = getDBConnection();
			lDao = new ComuneSqlDAO(lConn);

			int lNum = lDao.getNumSediUNEPperCodComune(aCodComune);
			if (lNum > 0)
				lEsiste = true;
		} catch (Exception sqe) {
			throw new SIUSException("ComuneController.ExIsComuneSedeUNEP: " + sqe);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * La funzione effettua la ricerca del Comune univocamente individuato dal codice passato come argomento.
	 * Viene restituito il ComuneModel se la ricerca ha successo, una eccezione negli altri casi.
	 *
	 * @param String
	 *            aKey : codice comune.
	 * @return ComuneModel.
	 * @throws F3BException
	 */
	public ComuneModel ExRicercaComuneByKey(String aKey) throws F3BException {

		Connection lConn = null;
		ComuneDAO lComDao = null;
		ComuneModel lComMod = null;

		try {
			lConn = getDBConnection();
			lComDao = new ComuneDAO(lConn);
			lComDao.setCodComune(aKey);
			lComDao.selByKey();
			lComMod = (ComuneModel) lComDao.getModelByKey();
			if (lComMod == null)
				throw new F3BException("Ricerca Comune fallita. Codice Comune inesistente : " + aKey);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ComuneController.ExRicercaComune: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("ComuneController.ExRicercaComune: Non posso leggere  : " + e);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}
		return lComMod;
	}

	// MEV_21: ricerco il comune dato il codice catastale
	@Override
	public ComuneModel ExRicercaComuneByCodCatastale(String ccc) throws F3BException {

		Connection c = null;
		ComuneSqlDAO csdao = null;
		ComuneModel cm = null;

		try {
			c = getDBConnection();
			csdao = new ComuneSqlDAO(c);
<<<<<<< HEAD
				
			// 20210616 MEV_21 In caso di soggetto nato all'estero il ccc inizia con 'Z'.
			// In tal caso anziché leggere dalla tabella COMUNE occorre leggere da CG_REF_CODES.
			if (ccc.startsWith("Z")) {
				csdao.ricercaNazionePerCcc(ccc);
				csdao.start();
				if (csdao.next())
					cm = (ComuneModel) csdao.getModelComuneCcc();
				csdao.stop();				
			} else {
				csdao.ricercaComuneByCodCatastale(ccc);
				csdao.start();
				if (csdao.next())
					cm = (ComuneModel) csdao.getModelComuneCcc();
				csdao.stop();
			}
			
		} catch (Exception e) {
=======
			csdao.ricercaComuneByCodCatastale(ccc);
			csdao.start();
			if (csdao.next())
				cm = (ComuneModel) csdao.getModelComuneTds();
			csdao.stop();
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
>>>>>>> 4105b5017a19972ad6840c25416777d0682603ba
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"ComuneController.ExRicercaComuneByCodCatastale: Non posso leggere  : " + e);
		} finally {
			cleanup(csdao);
			cleanup(c);
		}
		return cm;
	}
<<<<<<< HEAD
			
=======

>>>>>>> 4105b5017a19972ad6840c25416777d0682603ba
}
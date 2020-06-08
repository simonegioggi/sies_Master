package siap.sico.provvedimentisiesnsc.controller;

import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.provvedimentisiesnsc.dao.ProvvSiesNscDAO;
import siap.sico.provvedimentisiesnsc.dao.ProvvSiesNscSqlDAO;
import siap.sico.provvedimentisiesnsc.model.ProvvSiesNscModel;

/**
 * <p>
 * Title: ProvvSiesNscController
 * </p>
 * <p>
 * Description: Classe Controller per ProvvSiesNsc
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
public class ProvvSiesNscController extends SiapController implements IProvvSiesNsc {

	/*****************************************************************************
	 * Effettua la ricerca dei dati ProvvSiesNsc
	 *
	 * @param aProvvSiesNsc
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ExRicercaProvvSiesNsc(ProvvSiesNscModel aProvvSiesNsc) throws F3BException {

		Connection lConn = null;
		Vector lProvvSiesNsi = new Vector();
		ProvvSiesNscDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProvvSiesNscDAO(lConn);
			lProDao.setCondizioni(aProvvSiesNsc);
			lProDao.setOrderBy();
			lProDao.start();
			while (lProDao.next()) {
				lProvvSiesNsi.add(lProDao.getModel());
			}
			lProDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ProvvSiesNscController.ExRicercaProvvSiesNsc: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}

		return lProvvSiesNsi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public ProvvSiesNscModel ExRicercaProvvSiesNscById(String aProvvDomain, String aProvvCodcentr)
			throws F3BException {

		Connection lConn = null;
		ProvvSiesNscModel lProvvSiesNscMod = new ProvvSiesNscModel();
		ProvvSiesNscSqlDAO lProvvSiesNscSqlDao = null;

		try {
			lConn = getDBConnection();
			lProvvSiesNscSqlDao = new ProvvSiesNscSqlDAO(lConn);
			lProvvSiesNscSqlDao.ricercaProvvSiesNscByKey(aProvvDomain, aProvvCodcentr);
			lProvvSiesNscMod = (ProvvSiesNscModel) lProvvSiesNscSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ProvvSiesNscController.ExRicercaProvvSiesNscById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lProvvSiesNscSqlDao);
			cleanup(lConn);
		}

		return lProvvSiesNscMod;
	}

}
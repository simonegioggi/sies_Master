package siap.sico.camponota.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: CampoNotaController
 * </p>
 * <p>
 * Description: Controller per la gestione accesso ai dati del CampoNota.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class CampoNotaController extends SiapController implements ICampoNota {

	// Ricerca by IDCampoNota
	public CampoNotaModel ExRicercaCampoNotaByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		CampoNotaSqlDAO lCamDao = null;
		CampoNotaModel lCamMod = null;

		try {
			lConn = getDBConnection();
			lCamDao = new CampoNotaSqlDAO(lConn);

			lCamDao.ricercaCampoNotaByKey(aKey);
			lCamMod = (CampoNotaModel) lCamDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new SICOException("CampoNotaController.ExRicercaCampoNotaByKey: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lCamDao);
			cleanup(lConn);
		}

		return lCamMod;
	}

	/**
	 * ExRicercaCampoNotaByIdEvento
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */

	public CampoNotaModel ExRicercaCampoNotaByIdEvento(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		CampoNotaSqlDAO lCamDao = null;
		CampoNotaModel lCamMod = null;

		try {
			lConn = getDBConnection();
			lCamDao = new CampoNotaSqlDAO(lConn);

			lCamDao.ricercaCampoNotaByKeyEvento(aKey);
			lCamMod = (CampoNotaModel) lCamDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new SICOException("CampoNotaController.ExRicercaCampoNotaByIdEvento: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lCamDao);
			cleanup(lConn);
		}

		return lCamMod;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ExRicercaVectCampoNotaByIdEvento(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		CampoNotaSqlDAO lCamDao = null;
//		CampoNotaModel lCamMod = null;

		Vector lVecCampoNota = new Vector();

		try {
			lConn = getDBConnection();
			lCamDao = new CampoNotaSqlDAO(lConn);

			lCamDao.ricercaCampoNotaByKeyEvento(aKey);
			lVecCampoNota = new Vector(lCamDao.getModels());

			// if (lVecCampoNota.size() == 0)
			// {
			// throw new F3BException(F3BException.USER_MESSAGE, " Nessun Elemento trovato");
			// }

		} catch (DAOException daoEx) {
			throw new SICOException(
					"CampoNotaController.ExRicercaVectCampoNotaByIdEvento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCamDao);
			cleanup(lConn);
		}

		return lVecCampoNota;

	} // Chiude ExRicercaVectCampoNotaByIdEvento

} // Chiude class CampoNotaController
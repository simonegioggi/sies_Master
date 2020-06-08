package siap.siep.istitutodetenzione.controller;

import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.SIEPException;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;

/**
 * <p>
 * Title: IstitutoDetenzioneController
 * </p>
 * <p>
 * Description: Classe Controller per IstitutoDetenzione
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
public class IstitutoDetenzioneController extends SiapController implements IIstitutoDetenzione {

	public Vector ExRicercaIstitutoDetenzione(IstitutoDetenzioneModel aIstitutoDetenzione)
			throws F3BException {

		Connection lConn = null;
		Vector lIstitutoDetenzioni = new Vector();
		IstitutoDetenzioneSqlDAO lIstDao = null;

		try {
			lConn = getDBConnection();
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lIstDao.ricercaIstitutoDetenzione(aIstitutoDetenzione);
			lIstitutoDetenzioni = new Vector(lIstDao.getModels());
			if (lIstitutoDetenzioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"IstitutoDetenzioneController.ExRicercaIstitutoDetenzione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}
		return lIstitutoDetenzioni;
	}

	public IstitutoDetenzioneModel ExRicercaIstitutoDetenzioneByKey(String aKey) throws F3BException {

		Connection lConn = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstMod;

		try {
			lConn = getDBConnection();
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lIstDao.ricercaIstitutoDetenzioneByKey(aKey);
			lIstMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"IstitutoDetenzioneController.ExRicercaIstitutoDetenzione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}
		return lIstMod;
	}

	public Vector ListaIstitutoDetenzione() throws F3BException {

		Connection lConn = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		Vector lIstituti = new Vector();

		try {
			lConn = getDBConnection();
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lIstDao.listaIstitutoDetenzione();
			lIstDao.start();

			IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();

			while (lIstDao.next()) {
				// Decodifica
				lIstMod = new IstitutoDetenzioneModel((IstitutoDetenzioneModel) lIstDao.getModel());
				// Add al vettore
				lIstituti.add(lIstMod);
				// fine while
			}
			lIstDao.stop();

			if (lIstituti.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIEPException("CSSAController.ListaIstitutoDetenzione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}
		return lIstituti;
	}

	public Vector ExRicercaIstitutoDetenzionePerDistretto(IstitutoDetenzioneModel aIstitutoDetenzione)
			throws F3BException {

		Connection lConn = null;
		Vector lIstitutoDetenzioni = new Vector();
		IstitutoDetenzioneSqlDAO lIstDao = null;

		try {
			lConn = getDBConnection();
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lIstDao.ricercaIstitutoDetenzionePerDistretto(aIstitutoDetenzione);
			lIstitutoDetenzioni = new Vector(lIstDao.getModels());
			if (lIstitutoDetenzioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"IstitutoDetenzioneController.ExRicercaIstitutoDetenzionePerDistretto: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}
		return lIstitutoDetenzioni;
	}

}
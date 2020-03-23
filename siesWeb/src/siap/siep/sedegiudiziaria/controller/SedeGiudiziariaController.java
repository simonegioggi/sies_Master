package siap.siep.sedegiudiziaria.controller;

import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.sedegiudiziaria.dao.SedeGiudiziariaSqlDAO;
import siap.siep.sedegiudiziaria.model.SedeGiudiziariaModel;

/**
 * <p>
 * Title: SedeGiudiziariaController
 * </p>
 * <p>
 * Description: Classe Controller per SedeGiudiziaria
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
public class SedeGiudiziariaController extends SiapController implements ISedeGiudiziaria {

	public Vector ExRicercaSedeGiudiziaria(SedeGiudiziariaModel aSedeGiudiziaria) throws F3BException {

		Connection lConn = null;
		Vector lSedeGiudiziarii = new Vector();
		SedeGiudiziariaSqlDAO lSedDao = null;

		try {
			lConn = getDBConnection();
			lSedDao = new SedeGiudiziariaSqlDAO(lConn);
			lSedDao.ricercaSedeGiudiziaria(aSedeGiudiziaria);
			lSedeGiudiziarii = new Vector(lSedDao.getModels());
			if (lSedeGiudiziarii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SedeGiudiziariaController.ExRicercaSedeGiudiziaria: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSedDao);
			cleanup(lConn);
		}
		return lSedeGiudiziarii;
	}

	public SedeGiudiziariaModel ExRicercaSedeGiudiziariaByKey(String aKey) throws F3BException {

		Connection lConn = null;
		SedeGiudiziariaSqlDAO lSedDao = null;
		SedeGiudiziariaModel lSedMod;

		try {
			lConn = getDBConnection();
			lSedDao = new SedeGiudiziariaSqlDAO(lConn);
			lSedDao.ricercaSedeGiudiziariaByKey(aKey);
			lSedMod = (SedeGiudiziariaModel) lSedDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SedeGiudiziariaController.ExRicercaSedeGiudiziaria: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSedDao);
			cleanup(lConn);
		}
		return lSedMod;
	}

	public SedeGiudiziariaModel ExRicercaSedeGiudiziariaByDescrizione(String aDescrizione)
			throws F3BException {

		Connection lConn = null;
		SedeGiudiziariaSqlDAO lSedDao = null;
		SedeGiudiziariaModel lSedMod = null;

		if (aDescrizione == null || aDescrizione.trim().length() < 1) {
			throw new F3BException(
					"SedeGiudiziariaController.ExRicercaSedeGiudiziariaByDescrizione: Manca la Descrizione della sede da ricercare");
		}
		try {
			lConn = getDBConnection();
			lSedDao = new SedeGiudiziariaSqlDAO(lConn);
			lSedDao.ricercaSedeGiudiziariaByDescrizione(aDescrizione);
			lSedMod = (SedeGiudiziariaModel) lSedDao.getModelByKey();
		} catch (Exception daoEx) {
			throw new F3BException(
					"SedeGiudiziariaController.ExRicercaSedeGiudiziariaByDescrizione: " + daoEx);
		} finally {
			cleanup(lSedDao);
			cleanup(lConn);
		}
		return lSedMod;
	}

}
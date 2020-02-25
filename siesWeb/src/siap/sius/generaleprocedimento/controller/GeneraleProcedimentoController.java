package siap.sius.generaleprocedimento.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoSqlDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: GeneraleProcedimentoController
 * </p>
 * <p>
 * Description: Classe Controller per GeneraleProcedimento
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
public class GeneraleProcedimentoController extends SiapController implements IGeneraleProcedimento {

	public Vector ExRicercaGeneraleProcedimento(GeneraleProcedimentoModel aGeneraleProcedimento)
			throws F3BException {

		Connection lConn = null;
		Vector lGeneraleProcedimenti = new Vector();
		GeneraleProcedimentoSqlDAO lGenDao = null;

		try {
			lConn = getDBConnection();
			lGenDao = new GeneraleProcedimentoSqlDAO(lConn);
			lGenDao.ricercaGeneraleProcedimento(aGeneraleProcedimento);
			lGeneraleProcedimenti = new Vector(lGenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"GeneraleProcedimentoController.ExRicercaGeneraleProcedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lGenDao);
			cleanup(lConn);
		}
		return lGeneraleProcedimenti;
	}

	public GeneraleProcedimentoModel ExRicercaGeneraleProcedimentoByFascicolo(BigDecimal aIdFasSius)
			throws F3BException {

		Connection lConn = null;
		GeneraleProcedimentoSqlDAO lGenDao = null;
		GeneraleProcedimentoModel lGenMod;

		try {
			lConn = getDBConnection();
			lGenDao = new GeneraleProcedimentoSqlDAO(lConn);
			lGenDao.ricercaGeneraleProcedimentoByIdFas(aIdFasSius);
			lGenMod = (GeneraleProcedimentoModel) lGenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"GeneraleProcedimentoController.ExRicercaGeneraleProcedimentoByFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lGenDao);
			cleanup(lConn);
		}
		return lGenMod;
	}

	// 19/03/2008 Aggiornamento Note.
	public GeneraleProcedimentoModel ExModificaNoteProcedimento(
			GeneraleProcedimentoModel aGeneraleProcedimento) throws F3BException {

		Connection lConn = null;
		GeneraleProcedimentoDAO lGenDao = null;
		GeneraleProcedimentoModel lGenMod = new GeneraleProcedimentoModel(aGeneraleProcedimento);

		try {
			lConn = getDBConnection();
			lGenDao = new GeneraleProcedimentoDAO(lConn);
			lGenDao.setDAOFromModelForUpdateNote(aGeneraleProcedimento);
			lGenDao.update();
			lGenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("GeneraleProcedimentoController.ExModificaNoteProcedimento: " + ex);
		} finally {
			cleanup(lGenDao);
			cleanup(lConn);
		}
		return lGenMod;
	}

}
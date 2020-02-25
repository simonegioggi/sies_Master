package siap.sico.assistentegiudiziario.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.assistentegiudiziario.dao.AssistenteGiudiziarioDAO;
import siap.sico.assistentegiudiziario.dao.AssistenteGiudiziarioSqlDAO;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AssistenteGiudiziarioController
 * </p>
 * <p>
 * Description: Classe Controller per AssistenteGiudiziario
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
public class AssistenteGiudiziarioController extends SiapController implements IAssistenteGiudiziario {

	public AssistenteGiudiziarioModel ExInserisciAssistenteGiudiziario(
			AssistenteGiudiziarioModel aAssistenteGiudiziario) throws F3BException {
		Connection lConn = null;
		AssistenteGiudiziarioDAO lAssDao = null;
		AssistenteGiudiziarioModel lAssMod = null;

		try {
			lConn = getDBConnection();
			lAssMod = new AssistenteGiudiziarioModel(aAssistenteGiudiziario);
			lAssDao = new AssistenteGiudiziarioDAO(lConn);
			lAssDao.setDAOFromModel(aAssistenteGiudiziario);
			BigDecimal lKey = null;
			lKey = lAssDao.insert();
			commit(lConn);
			lAssMod.setIdAssistenteGiudiziario(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Inserimento impossibile: Assistente Giudiziario già esistente.");

			throw new F3BException("AssistenteGiudiziarioController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lAssDao);
			cleanup(lConn);
		}
		return lAssMod;
	}

	public Vector ExRicercaAssistenteGiudiziario(AssistenteGiudiziarioModel aAssistenteGiudiziario)
			throws F3BException {
		Connection lConn = null;
		Vector lAssistenteGiudiziarii = new Vector();
		AssistenteGiudiziarioSqlDAO lAssDao = null;

		try {
			lConn = getDBConnection();
			lAssDao = new AssistenteGiudiziarioSqlDAO(lConn);
			lAssDao.ricercaAssistenteGiudiziario(aAssistenteGiudiziario);
			lAssistenteGiudiziarii = new Vector(lAssDao.getModels());
			if (lAssistenteGiudiziarii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AssistenteGiudiziarioController.ExRicercaAssistenteGiudiziario: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAssDao);
			cleanup(lConn);
		}
		return lAssistenteGiudiziarii;
	}

	public AssistenteGiudiziarioModel ExRicercaAssistenteGiudiziarioByKey(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		AssistenteGiudiziarioSqlDAO lAssDao = null;
		AssistenteGiudiziarioModel lAssMod;

		try {
			lConn = getDBConnection();
			lAssDao = new AssistenteGiudiziarioSqlDAO(lConn);
			lAssDao.ricercaAssistenteGiudiziarioByKey(aKey);
			lAssMod = (AssistenteGiudiziarioModel) lAssDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AssistenteGiudiziarioController.ExRicercaAssistenteGiudiziario: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAssDao);
			cleanup(lConn);
		}
		return lAssMod;
	}

	public AssistenteGiudiziarioModel ExModificaAssistenteGiudiziario(
			AssistenteGiudiziarioModel aAssistenteGiudiziario) throws F3BException {
		Connection lConn = null;
		AssistenteGiudiziarioDAO lAssDao = null;
		AssistenteGiudiziarioModel lAssMod = new AssistenteGiudiziarioModel(aAssistenteGiudiziario);

		try {
			lConn = getDBConnection();
			lAssDao = new AssistenteGiudiziarioDAO(lConn);
			lAssDao.setDAOFromModelForUpdate(aAssistenteGiudiziario);
			lAssDao.update();
			lAssDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AssistenteGiudiziarioController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lAssDao);
			cleanup(lConn);
		}
		return lAssMod;
	}

	public void ExCancellaAssistenteGiudiziario(AssistenteGiudiziarioModel aAssistenteGiudiziario)
			throws F3BException {
		Connection lConn = null;
		AssistenteGiudiziarioDAO lAssDao = null;

		try {
			lConn = getDBConnection();
			lAssDao = new AssistenteGiudiziarioDAO(lConn);
			lAssDao.setCondizioneUpdate(aAssistenteGiudiziario.getIdAssistenteGiudiziario());
			lAssDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Assistente già collegato ad altri dati. Impossibile effettuare la Cancellazione!");

			throw new F3BException(
					"AssistenteGiudiziarioController.ExCancellaAssistenteGiudiziario: Non posso cancellar : "
							+ daoEx);
		} finally {
			cleanup(lAssDao);
			cleanup(lConn);
		}
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aEsperto
	 * @return
	 * @throws F3BException
	 */

	public Vector ExElencoCbxAssistenteGiudiziarioByCodUfficio(String aCodUfficio) throws F3BException {
		Connection lConn = null;
		AssistenteGiudiziarioSqlDAO lAssDao = null;
		Vector lEspDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lAssDao = new AssistenteGiudiziarioSqlDAO(lConn);
			lAssDao.ricercaAssistenteGiudiziarioByCodUfficio(aCodUfficio);

			lAssDao.start();

			// da Considerare l'implematazione di metodi getDecodeModel nel genricDAO.
			// .... penserò. Paolo
			while (lAssDao.next()) {
				String lIdAssistente = lAssDao.getBigDecimal("ID_ASSISTENTE_GIUDIZIARIO").toString();
				String lNome = lAssDao.getString("NOME");
				String lCognome = lAssDao.getString("COGNOME");

				lEspDecMods.add(new DecodeModel(lIdAssistente, lCognome + " " + lNome));
			}

			lAssDao.stop();

			// lMagMods = new Vector( lMagDao.getModels() );
		} catch (DAOException daoEx) {
			throw new F3BException("EspertoController.ExElencoCbxEspertiByCodUfficio: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lAssDao);
			cleanup(lConn);
		}

		return lEspDecMods;
	}

}
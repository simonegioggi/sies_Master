package siap.jms.jmscode.controller;

import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.jms.jmscode.dao.JmsCodeSqlDAO;
import siap.jms.jmscode.model.JmsCodeModel;
import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: JmsCodeController
 * </p>
 * <p>
 * Description: Classe Controller per JmsCode
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
public class JmsCodeController extends SiapController implements IJmsCode {

	/**
	 * Ricerca le BDI configurate nel DB
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAllBDI() throws F3BException {
		Connection lConn = null;
		Vector lJmsCodi = new Vector();
		JmsCodeSqlDAO lJmsDao = null;

		try {
			lConn = getDBConnection();
			lJmsDao = new JmsCodeSqlDAO(lConn);
			lJmsDao.ricercaAllBDI();
			lJmsCodi = new Vector(lJmsDao.getModels());
			if (lJmsCodi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("JmsCodeController.ExRicercaJmsCode: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lJmsDao);
			cleanup(lConn);
		}
		return lJmsCodi;
	}

	// RICERCA PER DOMINIO

	public Vector ExRicercaPerDominio(String aDominio) throws F3BException {
		Connection lConn = null;
		Vector lJmsCodi = new Vector();
		JmsCodeSqlDAO lJmsDao = null;

		try {
			lConn = getDBConnection();
			lJmsDao = new JmsCodeSqlDAO(lConn);
			lJmsDao.ricercaPerDominio(aDominio);
			lJmsCodi = new Vector(lJmsDao.getModels());
			if (lJmsCodi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("JmsCodeController.ExRicercaPerDominio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lJmsDao);
			cleanup(lConn);
		}
		return lJmsCodi;
	}

	/**
	 * Ricerca il Codice dalla chiave
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public JmsCodeModel ExRicercaJmsCodeByKey(String aDominio, String aCodice) throws F3BException {
		Connection lConn = null;
		JmsCodeSqlDAO lJmsDao = null;
		JmsCodeModel lJmsMod;

		try {
			lConn = getDBConnection();
			lJmsDao = new JmsCodeSqlDAO(lConn);
			lJmsDao.ricercaJmsCodeByKey(aDominio, aCodice);
			lJmsMod = (JmsCodeModel) lJmsDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("JmsCodeController.ExRicercaJmsCode: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lJmsDao);
			cleanup(lConn);
		}
		return lJmsMod;
	}

	public JmsCodeModel ExRicercaBDICodeByCodUff(String aCode) throws F3BException {
		Connection lConn = null;
		JmsCodeSqlDAO lJmsDao = null;
		JmsCodeModel lJmsMod;

		try {
			lConn = getDBConnection();
			lJmsDao = new JmsCodeSqlDAO(lConn);
			lJmsDao.RicercaBDICodeByCodUff(aCode);
			lJmsMod = (JmsCodeModel) lJmsDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("JmsCodeController.ExRicercaJmsCode: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lJmsDao);
			cleanup(lConn);
		}
		return lJmsMod;
	}

	// RICERCA PER DOMINIO & DESCRIZIONE

	public Vector ExRicercaPerDominioEDescrizione(String aDominio, String aDescrizione) throws F3BException {
		Connection lConn = null;
		Vector lJmsCodi = new Vector();
		JmsCodeSqlDAO lJmsDao = null;

		try {
			lJmsCodi.add(new DecodeModel("-", "Tutti"));
			lConn = getDBConnection();
			lJmsDao = new JmsCodeSqlDAO(lConn);
			lJmsDao.ricercaPerDominioEDescrizione(aDominio, aDescrizione);
			lJmsDao.start();
			while (lJmsDao.next()) {
				String lCod = ((JmsCodeModel) lJmsDao.getModel()).getCodice();
				String lDescrizione = ((JmsCodeModel) lJmsDao.getModel()).getDescrizione();

				lJmsCodi.add(new DecodeModel(lCod, lDescrizione));
			}
			lJmsDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("JmsCodeController.ExRicercaPerDominioEDescrizione: " + daoEx);
		} finally {
			cleanup(lJmsDao);
			cleanup(lConn);
		}
		if (lJmsCodi.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}
		return lJmsCodi;
	}

	// Ricerca Progressivi della BDI

	public Vector ExRicercaProgressiviBDI() throws F3BException {
		Connection lConn = null;
		Vector lJmsCodi = new Vector();
		JmsCodeSqlDAO lJmsDao = null;

		try {
			lConn = getDBConnection();
			lJmsDao = new JmsCodeSqlDAO(lConn);
			lJmsDao.ricercaProgrBDI();
			lJmsCodi = new Vector(lJmsDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException("JmsCodeController.ExRicercaProgressiviBDI: " + daoEx);
		} finally {
			cleanup(lJmsDao);
			cleanup(lConn);
		}
		if (lJmsCodi.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}
		return lJmsCodi;
	}

	/**
	 * Effettua la ricerca sulla tabella JMS utilizzando come criteri i dati presenti nel model
	 * 
	 * @param aJmsModel
	 *            - Model con i dati di ricerca. Presi in considerazione se <> null e <> ""
	 */
	public Vector<JmsCodeModel> ExRicercaJmsCode(JmsCodeModel aJmsModel) throws F3BException {
		Connection lConn = null;
		JmsCodeSqlDAO lJmsDao = null;

		Vector<JmsCodeModel> lListaJmsMod = new Vector<JmsCodeModel>();

		try {
			lConn = getDBConnection();

			lJmsDao = new JmsCodeSqlDAO(lConn);

			lJmsDao.ricercaJmsCode(aJmsModel);

			lListaJmsMod = new Vector<JmsCodeModel>(lJmsDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("JmsCodeController.ExRicercaJmsCode: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lJmsDao);
			cleanup(lConn);
		}
		return lListaJmsMod;
	}

}
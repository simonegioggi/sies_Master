package siap.jms.jmscode.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.jms.jmscode.model.JmsCodeModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: JmsCodeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella JmsCode
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
public class JmsCodeSqlDAO extends SqlDAO {

	public JmsCodeSqlDAO(Connection con) {
		super(con);
	}

	/**
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaJmsCode(JmsCodeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE 1=1 " + setCondizione(aModel);

		setStatement(lSql);
	}

	public void ricercaProgrBDI() throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE DOMINIO='PROGR_BDI' ";
		setStatement(lSql);
	}

	public void ricercaAllBDI() throws DAOException {
		String lStatement = "";
		lStatement += "  SELECT  jm.DOMINIO DOMINIO, jm.CODICE CODICE, jm.DESCRIZIONE DESCRIZIONE ";
		lStatement += "	FROM JMS_CODE jm, JMS_CODE jm2 ";
		lStatement += "  WHERE jm.DOMINIO = 'BDI' AND jm.DESCRIZIONE = jm2.CODICE ";
		lStatement += "  AND jm2.DOMINIO='CONN_JMS_STRING'";

		setStatement(lStatement);
	}

	// ricerca per dominio
	public void ricercaPerDominio(String aDominio) throws DAOException {
		String lStatement = "";
		lStatement += "  SELECT  jm.DOMINIO DOMINIO, jm.CODICE CODICE, jm.DESCRIZIONE DESCRIZIONE ";
		lStatement += "	FROM JMS_CODE jm";
		lStatement += "  WHERE jm.DOMINIO = '" + aDominio + "'";

		setStatement(lStatement);
	}

	// ricerca per dominio-descrizione
	public void ricercaPerDominioEDescrizione(String aDominio, String aDescrizione) throws DAOException {
		String lStatement = "";
		lStatement += "  SELECT  jm.DOMINIO DOMINIO, jm.CODICE CODICE, jm.DESCRIZIONE DESCRIZIONE ";
		lStatement += "	FROM JMS_CODE jm";
		lStatement += "  WHERE jm.DOMINIO = '" + aDominio + "'";
		lStatement += "  AND jm.DESCRIZIONE like '" + aDescrizione + "%'";

		setStatement(lStatement);
	}

	/**
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaJmsCodeByKey(String aDominio, String aCodice) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE DOMINIO = '" + aDominio + "' AND CODICE = '" + aCodice + "'";
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + " DOMINIO, " + " CODICE, " + " DESCRIZIONE ";
		lStatement += " FROM jms_code";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		JmsCodeModel aModel = new JmsCodeModel();
		aModel.setDominio(getString("DOMINIO"));
		aModel.setCodice(getString("CODICE"));
		aModel.setDescrizione(getString("DESCRIZIONE"));
		return aModel;
	}

	/**
	 * 
	 * @param aModel
	 * @return
	 */
	public String setCondizione(JmsCodeModel aModel) {
		String lCondizioni = new String();

		if (aModel.getDominio() != null && aModel.getDominio().length() > 0)
			lCondizioni = lCondizioni + " AND DOMINIO = '" + aModel.getDominio() + "' ";

		if (aModel.getCodice() != null && aModel.getCodice().length() > 0)
			lCondizioni = lCondizioni + " AND CODICE = '" + aModel.getCodice() + "' ";

		if (aModel.getDescrizione() != null && aModel.getDescrizione().length() > 0)
			lCondizioni = lCondizioni + " AND DESCRIZIONE = '" + aModel.getDescrizione() + "' ";

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_jms_code = " + aKey;
	}

	public void RicercaBDICodeByCodUff(String aKey) throws DAOException {
		String lSql = "SELECT * FROM JMS_CODE";
		lSql += " WHERE JMS_CODE.dominio='PROGR_BDI'";
		lSql += " AND JMS_CODE.CODICE =";
		lSql += " ( ";
		lSql += " SELECT descrizione FROM COMUNE";
		lSql += " WHERE";
		lSql += " COMUNE.cod_comune =";
		lSql += " (";
		lSql += " SELECT cod_comune FROM UFFICIO ";
		lSql += " WHERE ";
		lSql += " cod_distretto=cod_ufficio";
		lSql += " AND ";
		lSql += " cod_distretto=";
		lSql += " (SELECT DISTINCT cod_distretto FROM UFFICIO WHERE cod_comune='" + aKey + "')";
		lSql += " ))";

		setStatement(lSql);
	}

}
package siap.sico.w_magistrato.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.w_magistrato.model.WMagistratoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: WMagistratoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella WMagistrato
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
public class WMagistratoSqlDAO extends SIAPSqlDAO {

	public WMagistratoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaWMagistratoByCognome(String aCognome) throws DAOException {
		String lSql = getSqlQuery();
		if (aCognome != null)
			lSql += " " + "WHERE COGNOME LIKE '" + aCognome + "%'";
		lSql += " ORDER BY COGNOME";
		setStatement(lSql);
	}

	public void ricercaWMagistrato(WMagistratoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaWMagistratoByKey(String aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "COD_MAGISTRATO, " + "COGNOME, " + "NOME, " + "DATA_NASCITA, "
				+ "DESC_LUOGO_NASCITA, " + "DATA_CARICAMENTO ";
		lStatement += " FROM W_MAGISTRATO";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		WMagistratoModel aModel = new WMagistratoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		// aModel.setDescrMagistrato(getString("") ); Luigi
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDescLuogoNascita(getString("DESC_LUOGO_NASCITA"));
		aModel.setDataCaricamento(getDate("DATA_CARICAMENTO"));
		return aModel;
	}

	public String setCondizione(WMagistratoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(String aKey) {
		String lCondizioni = new String();
		lCondizioni += " " + "WHERE COD_MAGISTRATO = " + aKey;

		return lCondizioni;
	}

}
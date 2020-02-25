package siap.siep.scarti.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.scarti.model.WScartiModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: WScartiSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella WScarti
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
public class WScartiSqlDAO extends SqlDAO {

	public WScartiSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaWScarti(WScartiModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaWScartiByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaScartiPaged(WScartiModel aModel, int aPage) throws DAOException {
		String lStatement = new String("");
		String lPaginedStatement = new String("");

		lStatement += " " + getSqlQuery();

		// lStatement += " " + setCondizionePerInserimento(aModel);
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void getCountScarti(WScartiModel aModel) throws DAOException {

		String lStatement = "SELECT COUNT(*) HowManyRecords FROM W_SCARTI";

		// lStatement += " " + setCondizionePerInserimento(aModel);
		setStatement(lStatement);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_SCARTI, " + "TABELLA, " + "ANN_RES, " + "NUM_RES, " + "LET_RES, "
				+ "CHIAVE_ALTERNATIVA, " + "NOTE_SCARTO, " + "CAUSA_SCARTO ";
		lStatement += " FROM W_SCARTI";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		WScartiModel aModel = new WScartiModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdScarti(getBigDecimal("ID_SCARTI"));
		aModel.setTabella(getString("TABELLA"));
		aModel.setAnnRes(getBigDecimal("ANN_RES"));
		aModel.setNumRes(getString("NUM_RES"));
		aModel.setLetRes(getString("LET_RES"));
		aModel.setChiaveAlternativa(getString("CHIAVE_ALTERNATIVA"));
		aModel.setNoteScarto(getString("NOTE_SCARTO"));
		aModel.setCausaScarto(getString("CAUSA_SCARTO"));
		return aModel;
	}

	public String setCondizione(WScartiModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_W_SCARTI = " + aKey;
	}

}
package siap.regesies.regescarti.dao;

import java.sql.Connection;

import siap.regesies.regescarti.model.RegeFileModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: RegeFileSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RegeFile
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
public class RegeFileSqlDAO extends SqlDAO {

	public RegeFileSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRegeFilePage(RegeFileModel aModel, int aPage) throws DAOException {
		String lPaginedStatement = new String("");

		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel, true);

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		// setStatement(lSql);
		setStatement(lPaginedStatement);
	}

	public void getCountFileRege(RegeFileModel aModel) throws DAOException {

		String lStatement = "SELECT COUNT(*) HowManyRecords FROM REGE_FILE WHERE ";
		lStatement += " " + setCondizione(aModel, false);
		setStatement(lStatement);

	}

	public void ricercaRegeFileByKey(String aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_FILE, " + "DATA_INSERIMENTO, " + "REGE_FILE.COD_COMUNE COD_COMUNE, "
				+ "COM.DESCRIZIONE DESCRIZIONECOMUNE, " +
				// "FILE_BLOB, "+
				"AF10FASC, " + "AF10PROG, " + "AF10TIPOR, " + "COD_STATO, " + "DESC_ERR ";
		lStatement += " FROM REGE_FILE, COMUNE COM";
		lStatement += " WHERE REGE_FILE.COD_COMUNE=COM.COD_COMUNE";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		RegeFileModel aModel = new RegeFileModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFile(getString("ID_FILE"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodComune(getString("COD_COMUNE"));
		aModel.setDescrComune(getString("DESCRIZIONECOMUNE"));
		// aModel.setFileBlob(getBlob("FILE_BLOB") );
		aModel.setAf10fasc(getString("AF10FASC"));
		aModel.setAf10prog(getString("AF10PROG"));
		aModel.setAf10tipor(getString("AF10TIPOR"));
		aModel.setCodStato(getBigDecimal("COD_STATO"));
		// aModel.setDescrStato(getString("") );
		aModel.setDescErr(getString("DESC_ERR"));
		return aModel;
	}

	public String setCondizione(RegeFileModel aModel, boolean chkand) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		if (chkand) {
			lCondizioni = " AND";
		}

		lCondizioni = lCondizioni + " COD_STATO=" + aModel.getCodStato();
		lCondizioni = lCondizioni + " AND REGE_FILE.COD_COMUNE=" + aModel.getCodComune();
		lCondizioni = lCondizioni + " ORDER BY DATA_INSERIMENTO DESC";

		// lInserito=true;

		return lCondizioni;
	}

	public String setCondizioniByKey(String aKey) {
		return " AND ID_FILE = '" + aKey + "'";
	}

}
package siap.siep.penasospesa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.penasospesa.model.AnnmanReatoModel;

/**
 * <p>
 * Title: AnnmanReatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella AnnmanReato
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

public class AnnmanReatoSqlDAO extends SqlDAO {
	public AnnmanReatoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaAnnmanReato(AnnmanReatoModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE ";
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaAnnmanReatoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		if (!lSql.contains("WHERE"))
			lSql += " WHERE " + setCondizioniByKeysenzaAND(aKey);
		else
			lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ANNMAN_REATO, " + "ANNOTAZIONEMANUALE_ID, " + "REATO_ID ";
		lStatement += " FROM ANNMAN_REATO";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		AnnmanReatoModel aModel = new AnnmanReatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdAnnmanReato(getBigDecimal("ID_ANNMAN_REATO"));
		aModel.setAnnotazionemanualeId(getBigDecimal("ANNOTAZIONEMANUALE_ID"));
		aModel.setReatoId(getBigDecimal("REATO_ID"));
		return aModel;
	}

	public String setCondizione(AnnmanReatoModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		if (aModel.getAnnotazionemanualeId() != null) {
			lCondizioni += " annotazionemanuale_id=" + aModel.getAnnotazionemanualeId();
			// lInserito=true;
		}
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ANNMAN_REATO = " + aKey;
	}

	public String setCondizioniByKeysenzaAND(BigDecimal aKey) {
		return " ID_ANNMAN_REATO = " + aKey;
	}

}
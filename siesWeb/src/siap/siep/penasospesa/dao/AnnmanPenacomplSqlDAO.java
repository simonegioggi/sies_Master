package siap.siep.penasospesa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.penasospesa.model.AnnmanPenacomplModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: AnnmanPenacomplSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella AnnmanPenacompl
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
public class AnnmanPenacomplSqlDAO extends SqlDAO {

	public AnnmanPenacomplSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaAnnmanPenacompl(AnnmanPenacomplModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE ";
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaAnnmanPenacomplByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ANNMAN_PENACOMPL, " + "ANNOTAZIONEMANUALE_ID, "
				+ "PENACOMPLESSIVA_ID ";
		lStatement += " FROM ANNMAN_PENACOMPL";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		AnnmanPenacomplModel aModel = new AnnmanPenacomplModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdAnnmanPenacompl(getBigDecimal("ID_ANNMAN_PENACOMPL"));
		aModel.setAnnotazionemanualeId(getBigDecimal("ANNOTAZIONEMANUALE_ID"));
		aModel.setPenacomplessivaId(getBigDecimal("PENACOMPLESSIVA_ID"));
		return aModel;
	}

	public String setCondizione(AnnmanPenacomplModel aModel) {
		String lCondizioni = new String();

//		boolean lInserito = false;
		if (aModel.getAnnotazionemanualeId() != null) {
			lCondizioni += " annotazionemanuale_id=" + aModel.getAnnotazionemanualeId();
//			lInserito = true;
		}
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ANNMAN_PENACOMPL = " + aKey;
	}

}
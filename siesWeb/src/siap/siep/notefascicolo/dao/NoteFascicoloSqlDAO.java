package siap.siep.notefascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.notefascicolo.model.NoteFascicoloModel;

/**
 * <p>
 * Title: NoteFascicoloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella NoteFascicolo
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
public class NoteFascicoloSqlDAO extends SqlDAO {

	public NoteFascicoloSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaNoteFascicolo(NoteFascicoloModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaNoteFascicoloByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "NOTA_DISPOSITIVO, " + "NOTA_AVVOCATI, " + "FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " FROM note_fascicolo";
		lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		NoteFascicoloModel aModel = new NoteFascicoloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setNotaDispositivo(getString("NOTA_DISPOSITIVO"));
		aModel.setNotaAvvocati(getString("NOTA_AVVOCATI"));
		aModel.setFasIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		return aModel;
	}

	public String setCondizione(NoteFascicoloModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
	}

}
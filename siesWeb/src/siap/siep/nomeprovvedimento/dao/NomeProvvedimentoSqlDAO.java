package siap.siep.nomeprovvedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.nomeprovvedimento.model.NomeProvvedimentoModel;

/**
 * <p>
 * Title: NomeProvvedimentoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella NomeProvvedimento
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

public class NomeProvvedimentoSqlDAO extends SqlDAO {
	public NomeProvvedimentoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaNomeProvvedimento(NomeProvvedimentoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaNomeProvvedimentoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "COD_NOME_PROVVEDIMENTO, " + "EVE_ID_EVENTO ";
		lStatement += " FROM NOME_PROVVEDIMENTO";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		NomeProvvedimentoModel aModel = new NomeProvvedimentoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setCodNomeProvvedimento(getString("COD_NOME_PROVVEDIMENTO"));
		aModel.setDescrNomeProvvedimento(getString(""));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		return aModel;
	}

	public String setCondizione(NomeProvvedimentoModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_NOME_PROVVEDIMENTO = " + aKey;
	}
}

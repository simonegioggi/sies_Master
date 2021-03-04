package siap.sico.utenzaAdn.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sico.utenzaAdn.model.UtenzaAdnModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class UtenzaAdnSqlDAO extends SqlDAO {

	public UtenzaAdnSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaUtenteAdn(UtenzaAdnModel aModel) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	private String setCondizione(UtenzaAdnModel aModel) {

		String condizioni = new String();
		if (Utils.isNullObj(aModel.getId())) {
			if (Utils.isPresent(aModel.getSamAccountName()))
				condizioni += " where SAMACCOUNTNAME = '"
						+ StringUtils.convertSqlString(aModel.getSamAccountName()) + "'";
		}

		// valore di ritorno
		return condizioni;
	}

	public void listaUtentiAdn() throws DAOException {

		String lSql = getSqlQuery();
		lSql += " ORDER BY ID";
		setStatement(lSql);
	}

	public void ricercaUtenteAdnByKey(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	private String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + "ID, " + "SAMACCOUNTNAME, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO ";
		lStatement += "FROM UTENZA_ADN";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		UtenzaAdnModel aModel = new UtenzaAdnModel();
		aModel.setId(getBigDecimal("ID"));
		aModel.setSamAccountName(getString("SAMACCOUNTNAME"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));

		return aModel;
	}

	public String setCondizioniByKey(BigDecimal aKey) {

		return " WHERE ID = " + aKey;
	}

}
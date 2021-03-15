package siap.sico.utenzaAdn.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sico.utenzaAdn.model.AssocUtenteSiesAdnModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class AssocUtenteSiesAdnSqlDAO extends SqlDAO {

	public AssocUtenteSiesAdnSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaAssocUtenteSiesAdn(AssocUtenteSiesAdnModel aModel) throws DAOException {

		String sql = getSqlQuery();
		sql += " " + setCondizione(aModel);
		setStatement(sql);
	}

	private String setCondizione(AssocUtenteSiesAdnModel aModel) {

		String condizioni = new String();
		if (aModel.getId().doubleValue() == 0) {
			if (!("".equals(aModel.getUteCodUtente())))
				condizioni += " AND UTE_COD_UTENTE = '"
						+ StringUtils.convertSqlString(aModel.getUteCodUtente()) + "'";
			if (!(Utils.isNullObj(aModel.getIdUtenteAdn())))
				condizioni += " AND ID_UTENTE_ADN = " + aModel.getIdUtenteAdn();
		}

		// valore di ritorno
		return condizioni;
	}

	public void listaAssocUtentiSiesAdn() throws DAOException {

		String sql = getSqlQuery();
		sql += " ORDER BY ID";
		setStatement(sql);
	}

	public void ricercaAssocUtentiSiesAdnByKey(BigDecimal aKey) throws DAOException {

		String sql = getSqlQuery();
		sql += " " + setCondizioniByKey(aKey);
		setStatement(sql);
	}

	private String getSqlQuery() {

		String s = new String("");

		s += "SELECT " + "ID, " + "UTE_COD_UTENTE, " + "ID_UTENTE_ADN, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO ";
		s += "FROM ASSOC_UTENTE_SIES_ADN";

		return s;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		AssocUtenteSiesAdnModel aModel = new AssocUtenteSiesAdnModel();
		aModel.setId(getBigDecimal("ID"));
		aModel.setUteCodUtente(getString("UTE_COD_UTENTE"));
		aModel.setIdUtenteAdn(getBigDecimal("ID_UTENTE_ADN"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));

		return aModel;
	}

	public String setCondizioniByKey(BigDecimal aKey) {

		return " WHERE ID = " + aKey;
	}

	public void verificaAssociazioneSiesAdn(String userId) {

		String sql = "select a.* from ASSOC_UTENTE_SIES_ADN a, utente u where a.id_utente_adn in"
				+ " (select t.id from UTENZA_ADN t where t.samaccountname = '" + userId + "')"
				+ " and u.cod_utente = a.ute_cod_utente"
				+ " and (u.data_fine_validita is null or u.data_fine_validita > sysdate) order by 2";

		setStatement(sql);
	}

}
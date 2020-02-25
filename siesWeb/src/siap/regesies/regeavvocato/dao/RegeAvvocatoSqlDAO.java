package siap.regesies.regeavvocato.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.regesies.regeavvocato.model.RegeAvvocatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: RegeAvvocatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RegeAvvocato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class RegeAvvocatoSqlDAO extends SIAPSqlDAO {

	public RegeAvvocatoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRegeAvvocatobyKey(String aKey, int aProgr) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND ID_FILE = '" + aKey + "'";
		lSql += " AND PROGR_AVVOCATO = " + aProgr;
		setStatement(lSql);
	}

	public void ricercaRegeAvvocatoByProvvedimento(String aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND ID_FILE = '" + aKey + "'";
		lSql += setOrder();
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_FILE, " + "PROGR_AVVOCATO, " + "COGNOME, " + "NOME, " + "FORO, "
				+ "AVVTIPODESC.RV_MEANING DESCRTIPO, " + "COD_TIPO_AVVOCATO, " + "DATA_INIZIO_VALIDITA, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM rege_avvocato,CG_REF_CODES AVVTIPODESC";
		lStatement += " WHERE ";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=COD_TIPO_AVVOCATO ";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		RegeAvvocatoModel aModel = new RegeAvvocatoModel();

		aModel.setIdFile(getString("ID_FILE"));
		aModel.setProgrAvvocato(getInt("PROGR_AVVOCATO"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setForo(getString("FORO"));
		aModel.setCodTipoAvvocato(getString("COD_TIPO_AVVOCATO"));
		aModel.setDescrTipoAvvocato(getString("DESCRTIPO"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		return aModel;
	}

	public String setCondizione(RegeAvvocatoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setOrder() {
		return " ORDER BY PROGR_AVVOCATO ";
	}

}
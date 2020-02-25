package siap.siep.parametro.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.parametro.model.ParametroModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ParametroSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Parametro
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
public class ParametroSqlDAO extends SIAPSqlDAO {

	public ParametroSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaParametro(ParametroModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		setStatement(lSql);
	}

	public void ricercaParametroByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaParametroScadenzario(String aNomeParametro, String aUfficioValidita)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneParametroScadenzario(aNomeParametro, aUfficioValidita);
		setStatement(lSql);
	}

	public void ricercaParametroUfficioConnesso(ParametroModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneParametroUfficioConnesso(aModel);
		setStatement(lSql);
	}

	public void ricercaParametroUfficioConnesso(String aNomeParametro, String aUfficioValidita)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneParametroUfficioConnesso(aNomeParametro, aUfficioValidita);

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PARAMETRO, " + "NOME_PARAMETRO, " + "VALORE, " + "ANNI, " + "MESI, "
				+ "GIORNI, " + "IMPORTO, " + "DATA_INIZIO_VALIDITA, " + "DATA_FINE_VALIDITA, "
				+ "COD_UFFICIO_VALIDITA, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORMANENTO ";
		lStatement += " FROM PARAMETRO";
		lStatement += " WHERE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		ParametroModel aModel = new ParametroModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdParametro(getBigDecimal("ID_PARAMETRO"));
		aModel.setNomeParametro(getString("NOME_PARAMETRO"));
		aModel.setValore(getString("VALORE"));
		aModel.setAnni(getBigDecimal("ANNI"));
		aModel.setMesi(getBigDecimal("MESI"));
		aModel.setGiorni(getBigDecimal("GIORNI"));
		aModel.setImporto(getBigDecimal("IMPORTO"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setCodUfficioValidita(getString("COD_UFFICIO_VALIDITA"));
		// aModel.setDescrUfficioValidita(getString("") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiormanento(getString("COD_UFFICIO_AGGIORMANENTO"));
		// aModel.setDescrUfficioAggiormanento(getString("") );

		return aModel;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " ID_PARAMETRO = " + aKey;
	}

	public String setCondizioneParametroScadenzario(String aNomeParametro, String aUfficioValidita) {
		String lCondizioni = new String();

		lCondizioni += " NOME_PARAMETRO = '" + aNomeParametro + "'";

		if (aUfficioValidita == null || aUfficioValidita.equals("")) {
			lCondizioni += " AND COD_UFFICIO_VALIDITA is null";
		} else {
			lCondizioni += " AND COD_UFFICIO_VALIDITA = '" + aUfficioValidita + "'";
		}

		return lCondizioni;
	}

	public String setCondizioneParametroUfficioConnesso(String aNomeParametro, String aUfficioValidita) {
		String lCondizioni = new String();

		lCondizioni += " NOME_PARAMETRO = '" + aNomeParametro + "'";

		if (aUfficioValidita == null || aUfficioValidita.equals("")) {
			lCondizioni += " AND COD_UFFICIO_VALIDITA is null";
		} else {
			lCondizioni += " AND COD_UFFICIO_VALIDITA = '" + aUfficioValidita + "'";
		}

		return lCondizioni;
	}

	public String setCondizioneParametroUfficioConnesso(ParametroModel aModel) {
		String lCondizioni = new String();

		lCondizioni += " NOME_PARAMETRO = '" + aModel.getNomeParametro() + "'";
		lCondizioni += " AND COD_UFFICIO_VALIDITA = '" + aModel.getCodUfficioValidita() + "'";

		return lCondizioni;
	}

}
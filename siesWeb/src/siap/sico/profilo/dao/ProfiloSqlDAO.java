package siap.sico.profilo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.profilo.model.ProfiloModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ProfiloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Profilo
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
public class ProfiloSqlDAO extends SqlDAO {

	public ProfiloSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaProfiloByCodiceUtente(String aCodUtente) throws DAOException {

		String lStatement = "";

		lStatement += "SELECT PRO.*,";
		lStatement += " UTE_PRO.DATA_FINE_VALIDITA DT_FIN_UTE_PRO";
		lStatement += " FROM PROFILO PRO, UTENTE_PROFILO UTE_PRO";
		lStatement += " WHERE (PRO.COD_PROFILO = UTE_PRO.PRF_COD_PROFILO)";
		lStatement += " AND (UTE_PRO.UTE_COD_UTENTE = '" + aCodUtente + "')";

		setStatement(lStatement);
	}

	/**
	 * Metodo che crea statemment SQL per ricercare l'elenco dei profili filtrati per tipo ufficio,
	 * opportunamente presenti nella tabella di corrsispondenza ( PROFILO_TIPOUFFICIO )
	 * <p>
	 * 
	 * @param aCodTipoUfficio
	 *            String - Codice tipo uffcio per il quale presentare i profili associati.
	 * @throws DAOException
	 *             ( propaga l'errore di eccezione )
	 */
	public void ricercaProfiliByCodTipoUfficio(String aCodTipoUfficio) throws DAOException {
		String lStatement = "";

		lStatement += "SELECT PROF.* ";
		lStatement += " FROM PROFILO PROF, PROFILO_TIPOUFFICIO PROF_TIPOUFF ";
		lStatement += " WHERE PROF_TIPOUFF.UFF_COD_TIPO_UFFICIO = '" + aCodTipoUfficio + "'";
		lStatement += " AND PROF.COD_PROFILO = PRF_COD_PROFILO ";
		lStatement += " ORDER BY PROF.DESCRIZIONE ";

		setStatement(lStatement);
	}

	public void ricercaProfilo(ProfiloModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	public void ListaProfili() throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE COD_PROFILO <> 9999 ORDER BY DESCRIZIONE";

		setStatement(lSql);
	}

	public void ricercaProfiloByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "COD_PROFILO, " + "DESCRIZIONE, " + "DATA_FINE_VALIDITA ";
		lStatement += " FROM PROFILO";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		ProfiloModel aModel = new ProfiloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setCodProfilo(getBigDecimal("COD_PROFILO"));
		aModel.setDescrizione(getString("DESCRIZIONE"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		return aModel;
	}

	public String setCondizione(ProfiloModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " WHERE COD_PROFILO = " + aKey;
	}

}
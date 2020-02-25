package siap.sico.utente.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.utente.model.UtenteModel;
import siap.sico.utente.model.UtenteViewModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: UtenteSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Utente
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

public class UtenteSqlDAO extends SqlDAO {
	public UtenteSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaUtente(UtenteModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void listaUtenti() throws DAOException {
		String lSql = getSqlQuery();
		lSql += " ORDER BY COGNOME,NOME";
		setStatement(lSql);
	}

	public void listaUtentiAttivi(int aPage) throws DAOException {
		String lSql = getSqlQuery();
		lSql += setCondizioneAttivo(true, aPage);
		lSql += " ORDER BY COGNOME,NOME";
		setStatement(lSql);
	}

	public void listaUtentiNonAttivi(int aPage) throws DAOException {
		String lSql = getSqlQuery();
		lSql += setCondizioneAttivo(false, aPage);
		lSql += " ORDER BY COGNOME,NOME";
		setStatement(lSql);
	}

	public String setCondizioneAttivo(boolean listaUtentiAttivi, int aPage) {
		if (listaUtentiAttivi) {
			return " WHERE DATA_FINE_VALIDITA>=TO_DATE('"
					+ DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy HH:mm:ss")
					+ "','DD/MM/YYYY HH24:MI:SS')  OR DATA_FINE_VALIDITA IS NULL ORDER BY DESCR_TIPO_UFFICIO,DESCR_COMUNE,COGNOME,NOME) inner) WHERE rn BETWEEN "
					+ ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND " + (aPage)
					* IWebConstants.RESULT_PER_PAGE;
		} else
			return " WHERE DATA_FINE_VALIDITA<TO_DATE('"
					+ DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy HH:mm:ss")
					+ "','DD/MM/YYYY HH24:MI:SS') ORDER BY DESCR_TIPO_UFFICIO,DESCR_COMUNE,COGNOME,NOME) inner) WHERE rn BETWEEN "
					+ ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND " + (aPage)
					* IWebConstants.RESULT_PER_PAGE;
	}

	// STUB 20060621
	public String setCondizioneAttivo(boolean listaUtentiAttivi, int aPage, String aUfficio, String aDistretto) {
		String lSql = new String();

		lSql += " WHERE ";

		if (aUfficio != null)
			lSql += " COD_UFFICIO = '" + aUfficio + "' AND ";
		else if (aDistretto != null)
			lSql += " COD_DISTRETTO = '" + aDistretto + "' AND ";

		// MEV10-s3: aggiunta and condition per eliminare il tribunale dei minorenni
		lSql += " COD_TIPO_UFFICIO != 'DIBM' AND ";

		if (listaUtentiAttivi) {
			lSql += " ( DATA_FINE_VALIDITA>=TO_DATE('"
					+ DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy HH:mm:ss")
					+ "','DD/MM/YYYY HH24:MI:SS')  OR DATA_FINE_VALIDITA IS NULL ) ORDER BY DESCR_TIPO_UFFICIO,DESCR_COMUNE,COGNOME,NOME) inner) WHERE rn BETWEEN "
					+ ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND " + (aPage)
					* IWebConstants.RESULT_PER_PAGE;
		} else {
			lSql += " DATA_FINE_VALIDITA<TO_DATE('"
					+ DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy HH:mm:ss")
					+ "','DD/MM/YYYY HH24:MI:SS') ORDER BY DESCR_TIPO_UFFICIO,DESCR_COMUNE,COGNOME,NOME) inner) WHERE rn BETWEEN "
					+ ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND " + (aPage)
					* IWebConstants.RESULT_PER_PAGE;
		}

		return lSql;
	}

	public void ricercaUtenteByKey(String aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "COD_UTENTE, " + "COGNOME, " + "NOME, " + "PWD, " + "TELEFONO, " + "FAX, "
				+ "E_MAIL, " + "DATA_FINE_VALIDITA, " + "DATA_ORA_CONNESSIONE, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "DATA_ULTIMA_MODIFICA_PWD,IP ";
		lStatement += " FROM UTENTE";
		// lStatement += " WHERE ";

		return lStatement;
	}

	/*
	 * public void ListaUtentiAttiviFromView(int aPage) { String lSql=""; lSql=
	 * "SELECT * FROM ( SELECT COD_UTENTE,NOME,COGNOME,UTENTE_TEL,UTENTE_FAX,UTENTE_E_MAIL,DESCR_COMUNE,DESCR_TIPO_UFFICIO,DESCRIZIONE,Rownum rn FROM "
	 * +
	 * "( SELECT COD_UTENTE,NOME,COGNOME,UTENTE_TEL,UTENTE_FAX,UTENTE_E_MAIL,DESCR_COMUNE,DESCR_TIPO_UFFICIO,DESCRIZIONE FROM V_UTENTI_COMPLETA "
	 * +setCondizioneAttivo(true, aPage); lSql += " ORDER BY UPPER(COGNOME),UPPER(NOME),COD_UTENTE"; setStatement(lSql);
	 * 
	 * }
	 */

	// STUB 20060621
	public void ListaUtentiAttiviFromView(int aPage, String aUfficio, String aDistretto) {
		String lSql = "";
		lSql = "SELECT * FROM ( SELECT COD_UTENTE,COD_PROFILO,NOME,COGNOME,UTENTE_TEL,UTENTE_FAX,UTENTE_E_MAIL,DESCR_COMUNE,DESCR_TIPO_UFFICIO,DESCRIZIONE,Rownum rn FROM "
				+ "( SELECT COD_UTENTE,COD_PROFILO,NOME,COGNOME,UTENTE_TEL,UTENTE_FAX,UTENTE_E_MAIL,DESCR_COMUNE,DESCR_TIPO_UFFICIO,DESCRIZIONE FROM V_UTENTI_COMPLETA "
				+ setCondizioneAttivo(true, aPage, aUfficio, aDistretto);
		lSql += " ORDER BY UPPER(COGNOME),UPPER(NOME),COD_UTENTE";

		setStatement(lSql);

	}

	/*
	 * public void ListaUtentiNonAttiviFromView(int aPage) { String lSql=""; lSql=
	 * "SELECT * FROM ( SELECT COD_UTENTE,NOME,COGNOME,UTENTE_TEL,UTENTE_FAX,UTENTE_E_MAIL,DESCR_COMUNE,DESCR_TIPO_UFFICIO,DESCRIZIONE,Rownum rn FROM "
	 * +
	 * "( SELECT COD_UTENTE,NOME,COGNOME,UTENTE_TEL,UTENTE_FAX,UTENTE_E_MAIL,DESCR_COMUNE,DESCR_TIPO_UFFICIO,DESCRIZIONE FROM V_UTENTI_COMPLETA "
	 * +setCondizioneAttivo(false, aPage); lSql += " ORDER BY UPPER(COGNOME),UPPER(NOME),COD_UTENTE";
	 * setStatement(lSql); }
	 */

	// STUB 20060621
	public void ListaUtentiNonAttiviFromView(int aPage, String aUfficio, String aDistretto) {
		String lSql = "";
		lSql = "SELECT * FROM ( SELECT COD_UTENTE, COD_PROFILO, NOME,COGNOME,UTENTE_TEL,UTENTE_FAX,UTENTE_E_MAIL,DESCR_COMUNE,DESCR_TIPO_UFFICIO,DESCRIZIONE,Rownum rn FROM "
				+ "( SELECT COD_UTENTE, COD_PROFILO, NOME,COGNOME,UTENTE_TEL,UTENTE_FAX,UTENTE_E_MAIL,DESCR_COMUNE,DESCR_TIPO_UFFICIO,DESCRIZIONE FROM V_UTENTI_COMPLETA "
				+ setCondizioneAttivo(false, aPage, aUfficio, aDistretto);
		lSql += " ORDER BY UPPER(COGNOME),UPPER(NOME),COD_UTENTE";

		setStatement(lSql);
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		UtenteModel aModel = new UtenteModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setUserId(getString("COD_UTENTE"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setPwd(getString("PWD"));
		aModel.setTelefono(getString("TELEFONO"));
		aModel.setFax(getString("FAX"));
		aModel.setEmail(getString("E_MAIL"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setDataOraConnessione(getDate("DATA_ORA_CONNESSIONE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setDataUltimaModifcaPwd(getDate("DATA_ULTIMA_MODIFICA_PWD"));
		aModel.setIP(getString("IP"));

		return aModel;
	}

	public UtenteViewModel getModelFromView() throws DAOException {
		UtenteViewModel lUt = new UtenteViewModel();
		ProfileModel lPro = new ProfileModel();

		lUt.setUserId(getString("COD_UTENTE"));
		lUt.setNome(getString("NOME"));
		lUt.setCognome(getString("COGNOME"));
		lUt.setUtTelefono(getString("UTENTE_TEL"));
		lUt.setUtFax(getString("UTENTE_FAX"));
		lUt.setUtEmail(getString("UTENTE_E_MAIL"));

		lPro.setProfileId(getBigDecimal("COD_PROFILO"));
		lUt.setUserProfile(lPro);

		lUt.setComuneUfficio(getString("DESCR_COMUNE"));
		lUt.setDescTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lUt.setDescProfilo(getString("DESCRIZIONE"));

		return lUt;
	}

	public String setCondizione(UtenteModel aModel) {
		String lCondizioni = new String();

//		boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(String aKey) {
		return " WHERE COD_UTENTE = '" + aKey + "'";
	}

	public void updateProfiloByCodUtente(String aCodUtente, BigDecimal aCodProfilo) {
		String lSql = new String();
		lSql = "UPDATE UTENTE_PROFILO SET PRF_COD_PROFILO=" + aCodProfilo + " WHERE UTE_COD_UTENTE='" + aCodUtente
				+ "'";
		setStatement(lSql);
	}

	public void updateUfficioByCodUtente(String aCodUtente, String aCodUfficio) {
		String lSql = new String();
		lSql = "UPDATE UTENTE_UFFICIO SET UFF_COD_UFFICIO='" + aCodUfficio + "' WHERE UTE_COD_UTENTE='" + aCodUtente
				+ "'";
		setStatement(lSql);
	}

	public void deleteUtenteProfiloByCodUtente(String aCodUtente) {
		String lSql = new String();
		lSql = "DELETE FROM UTENTE_PROFILO WHERE UTE_COD_UTENTE='" + aCodUtente + "'";
		setStatement(lSql);
	}

	public void deleteUtenteUfficioByCodUtente(String aCodUtente) {
		String lSql = new String();
		lSql = "DELETE FROM UTENTE_UFFICIO WHERE UTE_COD_UTENTE='" + aCodUtente + "'";
		setStatement(lSql);
	}

	public void setUtente_Ufficio(UtenteModel aModel, String aCodUfficio) {
		String lsql = "";
		lsql = "INSERT INTO UTENTE_UFFICIO (DATA_INIZIO_VALIDITA,UTE_COD_UTENTE,UFF_COD_UFFICIO,COD_UTENTE_INSERIMENTO,DATA_INSERIMENTO) ";
		lsql += " VALUES (";
		lsql += "TO_DATE('" + DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy") + "','DD/MM/YYYY'),";
		lsql += "'" + StringUtils.convertSqlString(aModel.getUserId()) + "',";
		lsql += "'" + aCodUfficio + "',";
		lsql += "'" + aModel.getCodOperatoreInserimento() + "',";
		lsql += "TO_DATE('" + DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy") + "','DD/MM/YYYY'))";

		setStatement(lsql);
	}

	public void setUtente_Profilo(UtenteModel aModel, BigDecimal aCodProfilo) {
		String lsql = "";
		lsql = "INSERT INTO UTENTE_PROFILO (DATA_INIZIO_VALIDITA,UTE_COD_UTENTE,PRF_COD_PROFILO,COD_OPERATORE_INSERIMENTO,DATA_INSERIMENTO) ";
		lsql += " VALUES (";
		lsql += "TO_DATE('" + DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy") + "','DD/MM/YYYY'),";
		lsql += "'" + StringUtils.convertSqlString(aModel.getUserId()) + "',";
		lsql += aCodProfilo + ",";
		lsql += "'" + aModel.getCodOperatoreInserimento() + "',";
		lsql += "TO_DATE('" + DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy") + "','DD/MM/YYYY'))";

		setStatement(lsql);
	}

	public void setUtente_DataFine(UtenteModel aModel) {
		String lsql = "";
		lsql = "UPDATE UTENTE SET DATA_FINE_VALIDITA=";
		lsql += "TO_DATE('" + DateUtils.getDateToString(aModel.getDataFineValidita(), "dd/MM/yyyy HH:mm:ss")
				+ "','DD/MM/YYYY HH24:MI:SS'),";
		lsql += "COD_OPERATORE_AGGIORNAMENTO='" + aModel.getCodOperatoreAggiornamento() + "',";
		lsql += "DATA_AGGIORNAMENTO=TO_DATE('" + DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy")
				+ "','DD/MM/YYYY')";
		lsql += " WHERE COD_UTENTE='" + aModel.getUserId() + "'";
		setStatement(lsql);
	}

	public void setUtente_DataLogin(String aCodUtente, String aIP) {
		String lsql = "";

		lsql = "UPDATE UTENTE SET DATA_ORA_CONNESSIONE=";
		lsql += "TO_DATE('" + DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy HH:mm:ss")
				+ "','DD/MM/YYYY HH24:mi:ss'), ";
		lsql += "IP='" + aIP + "' ";
		lsql += " WHERE COD_UTENTE='" + aCodUtente + "'";

		setStatement(lsql);
	}

	/*
	 * public void getCountUtentiAttivi() throws DAOException { String lStatement =" SELECT COUNT(*) HowManyRecords" +
	 * " FROM V_UTENTI_COMPLETA " +
	 * " WHERE DATA_FINE_VALIDITA>=TO_DATE('"+DateUtils.getDateToString(DateUtils.getSysDate
	 * (),"dd/MM/yyyy HH:mm:ss")+"','DD/MM/YYYY HH24:MI:SS')  OR DATA_FINE_VALIDITA IS NULL ";
	 * 
	 * setStatement(lStatement); }
	 */

	// STUB 20060621
	public void getCountUtentiAttivi(String aUfficio, String aDistretto) throws DAOException {
		String lSql = " SELECT COUNT(*) HowManyRecords";
		lSql += " FROM V_UTENTI_COMPLETA ";
		lSql += " WHERE ";

		if (aUfficio != null)
			lSql += " COD_UFFICIO = '" + aUfficio + "' AND ";
		else if (aDistretto != null)
			lSql += " COD_DISTRETTO = '" + aDistretto + "' AND ";

		// MEV10-s3: aggiunta and condition per eliminare il tribunale dei minorenni
		lSql += " COD_TIPO_UFFICIO != 'DIBM' AND ";

		lSql += " ( DATA_FINE_VALIDITA>=TO_DATE('"
				+ DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy HH:mm:ss")
				+ "','DD/MM/YYYY HH24:MI:SS')  OR DATA_FINE_VALIDITA IS NULL )";

		setStatement(lSql);
	}

	/*
	 * public void getCountUtentiNonAttivi() throws DAOException { String lStatement =" SELECT COUNT(*) HowManyRecords"
	 * + " FROM V_UTENTI_COMPLETA " +
	 * " WHERE DATA_FINE_VALIDITA<TO_DATE('"+DateUtils.getDateToString(DateUtils.getSysDate
	 * (),"dd/MM/yyyy HH:mm:ss")+"','DD/MM/YYYY HH24:MI:SS') ";
	 * 
	 * setStatement(lStatement); }
	 */

	// STUB 20060621
	public void getCountUtentiNonAttivi(String aUfficio, String aDistretto) throws DAOException {
		String lSql = " SELECT COUNT(*) HowManyRecords";
		lSql += " FROM V_UTENTI_COMPLETA ";
		lSql += " WHERE ";

		if (aUfficio != null)
			lSql += " COD_UFFICIO = '" + aUfficio + "' AND ";
		else if (aDistretto != null)
			lSql += " COD_DISTRETTO = '" + aDistretto + "' AND ";

		lSql += " DATA_FINE_VALIDITA<TO_DATE('"
				+ DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy HH:mm:ss")
				+ "','DD/MM/YYYY HH24:MI:SS') ";

		setStatement(lSql);
	}

}

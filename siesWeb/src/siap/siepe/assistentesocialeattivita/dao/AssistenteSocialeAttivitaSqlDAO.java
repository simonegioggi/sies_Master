package siap.siepe.assistentesocialeattivita.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.siepe.assistentesocialeattivita.model.AssistenteSocialeAttivitaAssSocModel;
import siap.siepe.assistentesocialeattivita.model.AssistenteSocialeAttivitaModel;

/**
 * <p>
 * Title: AssistenteSocialeAttivitaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella AssistenteSocialeAttivita
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
public class AssistenteSocialeAttivitaSqlDAO extends SqlDAO {

	public AssistenteSocialeAttivitaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Prepara lo statement per una funzione di ricerca. Tale statement attiverà la ricerca di tutti gli
	 * Assistenti Sociali collegati ad una stessa attività.
	 * 
	 * @param aKeyAttivita
	 *            : chiave dell'Attivita
	 * @throws DAOException
	 */

	public void ricercaAssistentiSocXAttivita(BigDecimal aKeyAttivita) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "WHERE " + setCondizioneByKeyAttivita(aKeyAttivita);
		lSql += setOrder();
		setStatement(lSql);
	}

	/**
	 * Prepara lo statement per una funzione di ricerca. Tale statement attiverà la ricerca di tutti gli
	 * Assistenti Sociali collegati ad una stessa attività ed attivi, ovvero con un periodo di abilitazione
	 * che comprenda la data attuale.
	 * 
	 * @param aKeyAttivita
	 *            : chiave dell'Attivita
	 * @throws DAOException
	 */

	public void ricercaAssistentiSocialiAttiviXAttivita(BigDecimal aKeyAttivita, Date aData)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += "WHERE " + setCondizioneByKeyAttivita(aKeyAttivita);
		lSql += "AND " + setCondizioneXAssSocAttivo(aData);
		setStatement(lSql);
	}

	public void ricercaAssistenteSocialeAttivita(AssistenteSocialeAttivitaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "AA.DATA_INIZIO, " + "AA.DATA_FINE, " + "AA.COD_OPERATORE_INSERIMENTO, "
				+ "AA.DATA_INSERIMENTO, " + "AA.COD_UFFICIO_INSERIMENTO, "
				+ "AA.COD_OPERATORE_AGGIORNAMENTO, " + "AA.DATA_AGGIORNAMENTO, "
				+ "AA.COD_UFFICIO_AGGIORNAMENTO, " + "AA.ASS_SOC_ID_ASS_SOCIALE, " + "AA.ATT_ID_ATTIVITA, "
				+ "ASOC.NOME, " + "ASOC.COGNOME ";
		lStatement += " FROM ASSISTENTE_SOCIALE_ATTIVITA AA join ASSISTENTE_SOCIALE ASOC ON  AA.ASS_SOC_ID_ASS_SOCIALE = ASOC.ID_ASSISTENTE_SOCIALE ";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		AssistenteSocialeAttivitaAssSocModel aModel = new AssistenteSocialeAttivitaAssSocModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setAssSocIdAssSociale(getBigDecimal("ASS_SOC_ID_ASS_SOCIALE"));
		aModel.setAttIdAttivita(getBigDecimal("ATT_ID_ATTIVITA"));
		aModel.setNome(getString("NOME"));
		aModel.setCognome(getString("COGNOME"));
		return aModel;
	}

	public String setCondizione(AssistenteSocialeAttivitaModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioneByKeyAttivita(BigDecimal aKey) {
		return " ATT_ID_ATTIVITA = " + aKey;
	}

	public String setCondizioneXAssSocAttivo(Date aDataConfronto) {
		String lCondizioni = new String();

		lCondizioni = " TO_CHAR(DATA_INIZIO,'YYYYMMDD') <= '"
				+ DateUtils.getDateToString(aDataConfronto, "yyyyMMdd") + "'";
		lCondizioni += " AND (DATA_FINE IS null ";
		lCondizioni += "OR TO_CHAR(DATA_FINE,'YYYYMMDD') >= '"
				+ DateUtils.getDateToString(aDataConfronto, "yyyyMMdd") + "') ";
		return lCondizioni;
	}

	public String setOrder() {
		return " ORDER BY DATA_FINE DESC ";
	}

}
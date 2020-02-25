package siap.siepe.espertoattivita.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siepe.espertoattivita.model.EspertoAttivitaEspertoModel;
import siap.siepe.espertoattivita.model.EspertoAttivitaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: EspertoAttivitaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella EspertoAttivita
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
public class EspertoAttivitaSqlDAO extends SqlDAO {

	public EspertoAttivitaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaEspertoAttivita(EspertoAttivitaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	/**
	 * Prepara lo statement per una funzione di ricerca. Tale statement attiverà la ricerca di tutti gli
	 * esperti collegati ad una stessa attività ed attualmente attivi, ovvero con un periodo di abilitazione
	 * che comprenda la data attuale.
	 * 
	 * @param aKeyAttivita
	 *            : chiave dell'Attivita
	 * @throws DAOException
	 */
	public void ricercaEspertiXAttivita(BigDecimal aKeyAttivita) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "WHERE " + setCondizioneByKeyAttivita(aKeyAttivita);
		setStatement(lSql);
	}

	/**
	 * Prepara lo statement per una funzione di ricerca. Tale statement attiverà la ricerca di tutti gli
	 * esperti collegati ad una stessa attività ed ancora attivi
	 * 
	 * @param aKeyAttivita
	 *            : chiave dell'Attivita
	 * @throws DAOException
	 */

	public void ricercaEspertiAttiviXAttivita(BigDecimal aKeyAttivita, Date aData) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "WHERE " + setCondizioneByKeyAttivita(aKeyAttivita);
		lSql += "AND " + setCondizioneXEspertoAttivo(aData);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "EA.DATA_INIZIO, " + "EA.DATA_FINE, " + "EA.COD_OPERATORE_INSERIMENTO, "
				+ "EA.DATA_INSERIMENTO, " + "EA.COD_UFFICIO_INSERIMENTO, "
				+ "EA.COD_OPERATORE_AGGIORNAMENTO, " + "EA.DATA_AGGIORNAMENTO, "
				+ "EA.COD_UFFICIO_AGGIORNAMENTO, " + "EA.ESP_ID_ESPERTO, " + "EA.ATT_ID_ATTIVITA, "
				+ "E.NOME, " + "E.COGNOME ";
		lStatement += " FROM ESPERTO_ATTIVITA EA join ESPERTO E ON  EA.ESP_ID_ESPERTO = E.ID_ESPERTO ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		EspertoAttivitaEspertoModel aModel = new EspertoAttivitaEspertoModel();

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
		aModel.setEspIdEsperto(getBigDecimal("ESP_ID_ESPERTO"));
		aModel.setAttIdAttivita(getBigDecimal("ATT_ID_ATTIVITA"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		return aModel;
	}

	public String setCondizione(EspertoAttivitaModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioneByKeyAttivita(BigDecimal aKey) {
		return " ATT_ID_ATTIVITA = " + aKey;
	}

	public String setCondizioneXEspertoAttivo(Date aDataConfronto) {
		String lCondizioni = new String();

		lCondizioni = " TO_CHAR(DATA_INIZIO,'YYYYMMDD') <= '"
				+ DateUtils.getDateToString(aDataConfronto, "yyyyMMdd") + "'";
		lCondizioni += " AND (DATA_FINE IS null ";
		lCondizioni += "OR TO_CHAR(DATA_FINE,'YYYYMMDD') >= '"
				+ DateUtils.getDateToString(aDataConfronto, "yyyyMMdd") + "') ";
		return lCondizioni;
	}

}
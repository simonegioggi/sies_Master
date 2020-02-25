package siap.regesies.regenotiziareato.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: RegeNotiziaReatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RegeNotiziaReato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class RegeNotiziaReatoSqlDAO extends SIAPSqlDAO {

	public RegeNotiziaReatoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRegeNotiziaReato(String aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND ID_FILE = '" + aKey + "'";
		lSql += setOrder();

		setStatement(lSql);
	}

	public void ricercaRegeNotiziaReatoByKey(String aKey, int aProgr) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND ID_FILE = '" + aKey + "'";
		lSql += " AND PROGR_NOTIZIA = " + aProgr;

		setStatement(lSql);
	}

	public void ricercaRegeNotiziaReatoByIdFile(String aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_FILE, " + "PROGR_NOTIZIA, " + "DATA_PERVENIMENTO, "
				+ "ACQUISIZIONE_DIRETTA, " + "DATA_FATTO, " + "COD_FONTE, " + "TIPO_FONTE, "
				+ "COD_COMUNE_FONTE, " + "COM.DESCRIZIONE COMUNE," + "NUM_REG_AUTORITA, "
				+ "LUOGO_PROVENIENZA, " + "DATA_ACQUISIZIONE, " + "NUMERO_RICEVUTA, " + "DESCRIZIONE_FONTE, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "COM.DESCRIZIONE COMUNE";
		lStatement += " FROM rege_notizia_reato,  " + " COMUNE COM  ";
		lStatement += "where COD_COMUNE_FONTE = COM.COD_COMUNE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		RegeNotiziaReatoModel aModel = new RegeNotiziaReatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFile(getString("ID_FILE"));
		aModel.setProgrNotizia(getString("PROGR_NOTIZIA"));
		aModel.setDataPervenimento(getDate("DATA_PERVENIMENTO"));
		aModel.setAcquisizioneDiretta(getString("ACQUISIZIONE_DIRETTA"));
		aModel.setDataFatto(getDate("DATA_FATTO"));
		aModel.setCodFonte(getString("COD_FONTE"));
		aModel.setTipoFonte(getString("TIPO_FONTE"));
		aModel.setCodComuneFonte(getString("COD_COMUNE_FONTE"));
		aModel.setDescrComuneFonte(getString("COMUNE"));
		aModel.setNumRegAutorita(getString("NUM_REG_AUTORITA"));
		aModel.setLuogoProvenienza(getString("LUOGO_PROVENIENZA"));
		aModel.setDataAcquisizione(getDate("DATA_ACQUISIZIONE"));
		aModel.setNumeroRicevuta(getString("NUMERO_RICEVUTA"));
		aModel.setDescrizioneFonte(getString("DESCRIZIONE_FONTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		return aModel;
	}

	public String setCondizione(RegeNotiziaReatoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(String aKey) {
		return " AND ID_FILE = '" + aKey + "'";
	}

	public String setOrder() {
		return " order BY PROGR_NOTIZIA";
	}
}
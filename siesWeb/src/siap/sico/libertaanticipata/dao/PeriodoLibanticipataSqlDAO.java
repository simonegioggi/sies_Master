package siap.sico.libertaanticipata.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: PeriodoLibanticipataSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella PeriodoLibanticipata
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

public class PeriodoLibanticipataSqlDAO extends SqlDAO {

	public PeriodoLibanticipataSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaPeriodoLibanticipata(PeriodoLibAnticipataModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaPeriodoLibanticipataByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaPeriodoLibanticipataByLic(BigDecimal aIdLicenza) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + " WHERE LIC_ID_LICENZA_LIBANTICIPATA = " + aIdLicenza;
		lSql += " ORDER BY DATA_INIZIO";
		setStatement(lSql);
	}

	public void ricercaPeriodoLibanticipataOrdByLic(BigDecimal aIdLicenza) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + " WHERE LIC_ID_LICENZA_LIBANTICIPATA = " + aIdLicenza;
		lSql += " ORDER BY ID_PERIODO_LIBANTICIPATA";
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PERIODO_LIBANTICIPATA, " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "FLAG_CONCESSO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "LIC_ID_LICENZA_LIBANTICIPATA ";
		lStatement += " FROM PERIODO_LIBANTICIPATA";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		PeriodoLibAnticipataModel aModel = new PeriodoLibAnticipataModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPeriodoLibanticipata(getBigDecimal("ID_PERIODO_LIBANTICIPATA"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setFlagConcesso(getString("FLAG_CONCESSO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento("");
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento("");
		aModel.setLicIdLicenzaLibanticipata(getBigDecimal("LIC_ID_LICENZA_LIBANTICIPATA"));
		return aModel;
	}

	public String setCondizione(PeriodoLibAnticipataModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_PERIODO_LIBANTICIPATA = " + aKey;
	}

}
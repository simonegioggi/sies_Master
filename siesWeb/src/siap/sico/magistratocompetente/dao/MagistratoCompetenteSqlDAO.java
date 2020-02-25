package siap.sico.magistratocompetente.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: MagistratoCompetenteSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella MagistratoCompetente
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

public class MagistratoCompetenteSqlDAO extends SIAPSqlDAO {

	public MagistratoCompetenteSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaMagistratoCompetente(MagistratoCompetenteModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaMagistratoCompetenteByFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		setStatement(lSql);
	}

	public void ricercaMagistratoCompetenteByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaMagistratoEsistente(MagistratoCompetenteMagistratoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND MAG_COD_MAGISTRATO = " + "'" + aModel.getMagistrato().getCodMagistrato() + "'";
		lSql += " AND COD_RUOLO_MAGISTRATO = '01' ";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP ="
				+ aModel.getMagistratoCompetente().getFasSieIdFascicoloSiep();
		lSql += " ORDER BY DATA_INIZIO DESC ";
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "COD_RUOLO_MAGISTRATO, COD_RUOL.RV_MEANING RUOLO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "MAG_COD_MAGISTRATO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " FROM MAGISTRATO_COMPETENTE,CG_REF_CODES COD_RUOL";
		lStatement += " WHERE COD_RUOL.RV_DOMAIN = 'RUOLO_MAGISTRATO' AND COD_RUOL.RV_LOW_VALUE = COD_RUOLO_MAGISTRATO";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		MagistratoCompetenteModel aModel = new MagistratoCompetenteModel();

		// Inserire le opportune set delle descrizioni!

		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setCodRuoloMagistrato(getString("COD_RUOLO_MAGISTRATO"));
		aModel.setDescrRuoloMagistrato(getString("RUOLO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setMagCodMagistrato(getString("MAG_COD_MAGISTRATO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		return aModel;
	}

	public String setCondizione(MagistratoCompetenteModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_MAGISTRATO_COMPETENTE = " + aKey;
	}

	/**
	 * Metodo che imposta lo statement, per recuperare la count dei records di Magistrato Competente che
	 * afferiscano al Magistrato aCodMagistrato
	 * <p>
	 * 
	 * @param aCodMagistrato
	 *            utilizzato per impostare le condizioni di filtro.
	 */
	public void countMagCompByCodMagistrato(String aCodMagistrato) {
		String lStatement = "SELECT COUNT(*) AS COUNT FROM MAGISTRATO_COMPETENTE WHERE";
		lStatement += " MAG_COD_MAGISTRATO = '" + aCodMagistrato + "'";

		setStatement(lStatement);
	}

}
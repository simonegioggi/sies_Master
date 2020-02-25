package siap.siep.statoprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: StatoProcedimentoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella StatoProcedimento
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
public class StatoProcedimentoSqlDAO extends SqlDAO {

	public StatoProcedimentoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaStatoProcedimento(StatoProcedimentoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaStatoProcedimentoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void ricercaStatoProcedimentoByFascicoloSiep(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aKey);
		lSql += " ORDER BY PROGRESSIVO ASC ";
		setStatement(lSql);
	}

	public void ricercaStatoProcedimentoByFascicoloSiepDesc(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aKey);
		lSql += " ORDER BY PROGRESSIVO DESC ";
		setStatement(lSql);
	}

	public void ricercaStatoProcedimentoByFascicoloSiepStato(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicoloStato(aKey);
		setStatement(lSql);
	}

	public void ricercaMaxStatoProcedimentoByFascicoloSiep(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryMaxStato();
		lSql += " WHERE A.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + " PROGRESSIVO, " + " COD_STATO_PROCEDIMENTO, CODIFICA.RV_MEANING STATO, "
				+ " DATA, " + " COD_OPERATORE_INSERIMENTO, " + " DATA_INSERIMENTO, "
				+ " COD_UFFICIO_INSERIMENTO, " + " FAS_SIE_ID_FASCICOLO_SIEP, " + " EVE_ID_EVENTO";
		lStatement += " FROM STATO_PROCEDIMENTO STATO, CG_REF_CODES CODIFICA ";
		lStatement += " WHERE CODIFICA.RV_DOMAIN = 'STATO_PROCEDIMENTO' AND CODIFICA.RV_LOW_VALUE = STATO.COD_STATO_PROCEDIMENTO ";

		return lStatement;
	}

	protected String getSqlQueryMaxStato() {
		String lStatement = new String("");
		lStatement += " SELECT  A.*";
		lStatement += " FROM STATO_PROCEDIMENTO A JOIN MAX_STATO_PROCEDIMENTO B  ON";
		lStatement += " ( A.FAS_SIE_ID_FASCICOLO_SIEP = B.FAS_SIE_ID_FASCICOLO_SIEP";
		lStatement += "  AND A.PROGRESSIVO = B.STA_PRO_PROGRESSIVO)";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		StatoProcedimentoModel aModel = new StatoProcedimentoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setProgressivo(getBigDecimal("PROGRESSIVO"));
		aModel.setCodStatoProcedimento(getString("COD_STATO_PROCEDIMENTO"));
		aModel.setDescrStatoProcedimento(getString("STATO"));
		aModel.setData(getDate("DATA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		return aModel;
	}

	public String getCodStatoByKey() throws DAOException {
		String lCode = "";
		start();

		if (next())
			lCode = getString("COD_STATO_PROCEDIMENTO");
		stop();

		return lCode;
	}

	public String setCondizione(StatoProcedimentoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND PROGRESSIVO = " + aKey;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aIdFascicolo) {
		return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
	}

	public String setCondizioniByIdFascicoloStato(BigDecimal aIdFascicolo) {
		String lCondizioni = new String();

		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lCondizioni += " AND COD_STATO_PROCEDIMENTO = '0001'";

		return lCondizioni;
	}

	public BigDecimal getProgressivo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += "SELECT MAX(PROGRESSIVO) aMAX";
		lStatement += " FROM STATO_PROCEDIMENTO";
		lStatement += " WHERE STATO_PROCEDIMENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);

		this.start();

		BigDecimal lProgressivo = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgressivo = this.getBigDecimal("aMAX");

		this.stop();

		if (lProgressivo == null)
			lProgressivo = new BigDecimal(0);

		return lProgressivo;
	}

}
package siap.sige.scadenzario.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.utente.model.UtenteModel;
import siap.sige.scadenzario.model.ScadenzarioSigeModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: ScadenzarioSigeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella ScadenzarioSige
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
public class ScadenzarioSigeSqlDAO extends SqlDAO {

	public ScadenzarioSigeSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaScadenzarioSige(ScadenzarioSigeModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaScadenzarioSigeByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaScadenzarioSigeByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo)
			throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFascicoloTipo(aIdFascicolo, aTipo);
		setStatement(lSql);
	}

	public void ricercaScadenzarioSigePerDate(Date aData1, Date aData2) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniPerDate(aData1, aData2);
		setStatement(lSql);
	}

	public void ricercaScadenzarioSigePerTipoDate(String aTipoScadenzario, Date aData1, Date aData2)
			throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniPerTipoDate(aTipoScadenzario, aData1, aData2);
		setStatement(lSql);
	}

	public void ricercaScadenzarioSigePerTipoDate(String aTipoScadenzario, Date aData1, Date aData2,
			UtenteModel lUteMod) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniPerTipoDate(aTipoScadenzario, aData1, aData2, lUteMod);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_SCADENZARIO_SIGE, "
				+ "COD_TIPO_SCADENZARIO, TIPSCA.RV_MEANING DESCR_TIPO_SCADENZARIO, "
				+ "DATA_INIZIO_SCADENZA, " + "DATA_FINE_SCADENZA, " + "FLAG_VISTO, " + "DATA_VISTO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_ID_FASCICOLO_SIGE, " + "EVE_ID_EVENTO, "
				+ "(DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO";
		lStatement += " FROM SCADENZARIO_SIGE, CG_REF_CODES TIPSCA ";
		lStatement += " WHERE  TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' AND TIPSCA.RV_LOW_VALUE = COD_TIPO_SCADENZARIO ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		ScadenzarioSigeModel aModel = new ScadenzarioSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdScadenzarioSige(getBigDecimal("ID_SCADENZARIO_SIGE"));
		aModel.setCodTipoScadenzario(getString("COD_TIPO_SCADENZARIO"));
		aModel.setDescrTipoScadenzario("DESCR_TIPO_SCADENZARIO");
		aModel.setDataInizioScadenza(getDate("DATA_INIZIO_SCADENZA"));
		aModel.setDataFineScadenza(getDate("DATA_FINE_SCADENZA"));
		aModel.setFlagVisto(getString("FLAG_VISTO"));
		aModel.setDataVisto(getDate("DATA_VISTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento("");
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento("");
		aModel.setGiorniResidui(getBigDecimal("RESIDUO"));
		aModel.setFasIdFascicoloSige(getBigDecimal("FAS_ID_FASCICOLO_SIGE"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		return aModel;
	}

	public String setCondizione(ScadenzarioSigeModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_SCADENZARIO_SIGE = " + aKey;
	}

	public String setCondizioniByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo) {
		String lStatement = new String();

		lStatement += " AND FAS_ID_FASCICOLO_SIGE = '" + aIdFascicolo + "'";
		lStatement += " AND COD_TIPO_SCADENZARIO = " + aTipo;

		return lStatement;
	}

	public String setCondizioniPerDate(Date aData1, Date aData2) {
		String lCondizioni = new String();

		if (aData1 != null) {
			lCondizioni += " AND DATA_FINE_SCADENZA >= TO_DATE("
					+ DateUtils.getDateToString(aData1, "yyyyMMdd") + ",'YYYYMMDD' )";
		}
		if (aData2 != null) {
			lCondizioni += " AND DATA_FINE_SCADENZA <= TO_DATE("
					+ DateUtils.getDateToString(aData2, "yyyyMMdd") + ",'YYYYMMDD' )";
		}
		return lCondizioni;
	}

	public String setCondizioniPerTipoDate(String aTipoScadenzario, Date aData1, Date aData2) {
		String lCondizioni = new String();
		lCondizioni += " AND COD_TIPO_SCADENZARIO = '" + aTipoScadenzario + "'";
		if (aData1 != null)
			lCondizioni += " AND DATA_FINE_SCADENZA >= TO_DATE("
					+ DateUtils.getDateToString(aData1, "yyyyMMdd") + ",'YYYYMMDD' )";
		if (aData2 != null)
			lCondizioni += " AND DATA_FINE_SCADENZA <= TO_DATE("
					+ DateUtils.getDateToString(aData2, "yyyyMMdd") + ",'YYYYMMDD' )";
		return lCondizioni;
	}

	public String setCondizioniPerTipoDate(String aTipoScadenzario, Date aData1, Date aData2,
			UtenteModel lUteMod) {
		String lCondizioni = new String();
		lCondizioni += " AND COD_TIPO_SCADENZARIO = '" + aTipoScadenzario + "'";
		if (lUteMod.getUfficioUtente() != null)
			lCondizioni += " AND SCADENZARIO_SIGE.COD_UFFICIO_INSERIMENTO = '"
					+ lUteMod.getUfficioUtente().getCodUfficio() + "'";
		if (aData1 != null)
			lCondizioni += " AND DATA_FINE_SCADENZA >= TO_DATE("
					+ DateUtils.getDateToString(aData1, "yyyyMMdd") + ",'YYYYMMDD' )";
		if (aData2 != null)
			lCondizioni += " AND DATA_FINE_SCADENZA <= TO_DATE("
					+ DateUtils.getDateToString(aData2, "yyyyMMdd") + ",'YYYYMMDD' )";
		return lCondizioni;
	}

	public void ricercaTipiScadenzariSigeByTipoUfficio(String aTipoUfficio) throws DAOException {
		String lStatement = "SELECT RV_LOW_VALUE COD_TIPO_SCADENZARIO, RV_MEANING DESCR_TIPO_SCADENZARIO ";
		lStatement += " FROM CG_REF_CODES TIPSCA ";
		lStatement += " WHERE  TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' ";
		lStatement += " AND (TIPSCA.RV_ABBREVIATION = 'SIGE' ";
		lStatement += " OR TIPSCA.RV_ABBREVIATION = '" + aTipoUfficio + "')";

		setStatement(lStatement);
	}

}
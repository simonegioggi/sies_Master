package siap.sige.datiprovsige.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: DatiProvvedimentoSigeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella DATI_PROVVEDIMENTO_SIGE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class DatiProvvedimentoSigeSqlDAO extends SqlDAO {

	public DatiProvvedimentoSigeSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaDatiProvvedimentoSige(DatiProvvedimentoSigeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaDatiProvvedimentoSigeByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaDatiProvvedimentoSigeByIdTenore(BigDecimal aIdTenore) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneByIdTenore(aIdTenore);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "D.ID_DATI_PROVVEDIMENTO_SIGE, " + "D.COD_TIPO_DATI_PROV, " + "D.NOTE, "
				+ "D.COD_OPERATORE_INSERIMENTO, " + "D.DATA_INSERIMENTO, " + "D.COD_UFFICIO_INSERIMENTO, "
				+ "D.COD_OPERATORE_AGGIORNAMENTO, " + "D.DATA_AGGIORNAMENTO, "
				+ "D.COD_UFFICIO_AGGIORNAMENTO, " + "D.TEN_ID_TENORE_SIGE, "
				+ "C.RV_MEANING DESC_TIPO_DATI_PROV ";
		lStatement += "FROM DATI_PROVVEDIMENTO_SIGE D ";
		lStatement += "JOIN CG_REF_CODES C on (C.RV_DOMAIN = 'DATI_PROVVEDIMENTO_SIGE' AND C.RV_LOW_VALUE = D.COD_TIPO_DATI_PROV) ";

		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		DatiProvvedimentoSigeModel aModel = new DatiProvvedimentoSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdDatiProvvedimentoSige(getBigDecimal("ID_DATI_PROVVEDIMENTO_SIGE"));
		aModel.setCodTipoDatiProv(getString("COD_TIPO_DATI_PROV"));
		aModel.setDescrTipoDatiProv(getString("DESC_TIPO_DATI_PROV"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setTenIdTenoreSige(getBigDecimal("TEN_ID_TENORE_SIGE"));
		return aModel;
	}

	public String setCondizione(DatiProvvedimentoSigeModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_DATI_PROVVEDIMENTO_SIGE = " + aKey;
	}

	public String setCondizioneByIdTenore(BigDecimal aKey) {
		return " WHERE TEN_ID_TENORE_SIGE = " + aKey;
	}

}
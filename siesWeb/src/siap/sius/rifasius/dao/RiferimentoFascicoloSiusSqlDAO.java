package siap.sius.rifasius.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.sius.rifasius.model.RiferimentoFascicoloSiusModel;

/**
 * <p>
 * Title: RiferimentoFascicoloSiusSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RiferimentoFascicoloSius
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
public class RiferimentoFascicoloSiusSqlDAO extends SqlDAO {

	public RiferimentoFascicoloSiusSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaRiferimentoFascicoloSius(RiferimentoFascicoloSiusModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaRiferimentoFascicoloSiusByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");
		lStatement += " SELECT " + "ID_RIFERIMENTO_FASCICOLO_SIUS, " + "ANNO_FASCICOLO_SIUS, "
				+ "PROGR_FASCICOLO_SIUS, " + "COD_UFF_FASCICOLO_SIUS, " + "DATA_RICEZIONE, "
				+ "COD_OGGETTO_PROCEDIMENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " FROM RIFERIMENTO_FASCICOLO_SIUS";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		RiferimentoFascicoloSiusModel aModel = new RiferimentoFascicoloSiusModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRiferimentoFascicoloSius(getBigDecimal("ID_RIFERIMENTO_FASCICOLO_SIUS"));
		aModel.setAnnoFascicoloSius(getBigDecimal("ANNO_FASCICOLO_SIUS"));
		aModel.setProgrFascicoloSius(getBigDecimal("PROGR_FASCICOLO_SIUS"));
		aModel.setCodUffFascicoloSius(getString("COD_UFF_FASCICOLO_SIUS"));
		aModel.setDescrUffFascicoloSius((""));
		aModel.setDataRicezione(getDate("DATA_RICEZIONE"));
		aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		aModel.setDescrOggettoProcedimento((""));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		return aModel;
	}

	public String setCondizione(RiferimentoFascicoloSiusModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		lCondizioni += " WHERE ANNO_FASCICOLO_SIUS = '" + aModel.getAnnoFascicoloSius() + "'";
		lCondizioni += " AND PROGR_FASCICOLO_SIUS = '" + aModel.getProgrFascicoloSius() + "'";
		lCondizioni += " AND COD_UFF_FASCICOLO_SIUS = " + aModel.getCodUffFascicoloSius();
		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = '" + aModel.getFasSieIdFascicoloSiep() + "'";
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_RIFERIMENTO_FASCICOLO_SIUS = " + aKey;
	}

}
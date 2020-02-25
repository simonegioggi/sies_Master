package siap.siep.ulterioresanzionecumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: UlterioreSanzioneCumuloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella UlterioreSanzioneCumulo
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
public class UlterioreSanzioneCumuloSqlDAO extends SqlDAO {

	public UlterioreSanzioneCumuloSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaUlterioreSanzioneCumulo(UlterioreSanzioneCumuloModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaUlterioreSanzioneCumuloByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaUlterioreSanzioneCumuloByFascicoloCumulante(FascicoloSiepModel aModel)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByFascicolo(aModel.getIdFascicoloSiep());
		setStatement(lSql);
	}

	public void ricercaUlterioreSanzioneCumuloByFascicoloIdCumulo(BigDecimal aIdFac, BigDecimal aIdCum)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByFascicolo(aIdFac);
		lSql += " " + setCondizioniByCumIdCumulo(aIdCum);
		setStatement(lSql);
	}

	public void ricercaUlterioreSanzioneCumuloByCodUltSanzione(String aCodice, BigDecimal aFasc,
			BigDecimal aIdCum) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByFascicolo(aFasc);
		lSql += " " + setCondizioniByCodiceUltSanzione(aCodice);
		lSql += " " + setCondizioniByCumIdCumulo(aIdCum);

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ULTERIORE_SANZIONE_CUMULO, "
				+ "COD_TIPO_ULTERIORE_SANZIONE, CG.RV_MEANING DESCR_ULTERIORE_SANZIONE, " + "NUM_ANNI, "
				+ "NUM_MESI, " + "NUM_GIORNI, " + "SANZIONE, " + "DATA_INSERIMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "CUM_ID_CUMULO";
		lStatement += " FROM ULTERIORE_SANZIONE_CUMULO , CG_REF_CODES CG";
		lStatement += " WHERE ";
		lStatement += " CG.RV_DOMAIN = 'TIPO_ULTERIORE_SANZIONE' AND CG.RV_LOW_VALUE = COD_TIPO_ULTERIORE_SANZIONE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		UlterioreSanzioneCumuloModel aModel = new UlterioreSanzioneCumuloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdUlterioreSanzioneCumulo(getBigDecimal("ID_ULTERIORE_SANZIONE_CUMULO"));
		aModel.setCodTipoUlterioreSanzione(getString("COD_TIPO_ULTERIORE_SANZIONE"));
		aModel.setDescrTipoUlterioreSanzione(getString("DESCR_ULTERIORE_SANZIONE"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setSanzione(getBigDecimal("SANZIONE"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setCumIdCumulo(getBigDecimal("CUM_ID_CUMULO"));
		return aModel;
	}

	public String setCondizione(UlterioreSanzioneCumuloModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ULTERIORE_SANZIONE_CUMULO = " + aKey;
	}

	public String setCondizioniByCodiceUltSanzione(String aCodice) {
		return " AND COD_TIPO_ULTERIORE_SANZIONE = '" + aCodice + "'";
	}

	public String setCondizioniByFascicolo(BigDecimal aKey) {
		return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
	}

	public String setCondizioniByCumIdCumulo(BigDecimal aKey) {
		return " AND CUM_ID_CUMULO = " + aKey;
	}

}
package siap.siep.altracausa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: AltraCausaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella AltraCausa
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
public class AltraCausaSqlDAO extends SIAPSqlDAO {

	public AltraCausaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaAltraCausa(AltraCausaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	public void ricercaAltraCausaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void ricercaAltraCausaByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ALTRA_CAUSA, " + "ANNO, " + "NUMERO, " + "DATA, "
				+ "COD_LUOGO, LUOGO_AUTORITA.DESCRIZIONE DESC_LUOGO_AUTORITA,"
				+ "COD_AUTORITA, AUTORITA.RV_MEANING DESC_AUTORITA," + "DATA_DECORRENZA, " + "DATA_SCADENZA, "
				+ "COD_TIPO_POS_GIURIDICA, TIPO_POS_GIU.RV_MEANING DESC_TIPO_POS_GIU," +
				// modifica relativa al tipo istituto
				"IST_DET_ID_ISTITUTO_DETENZIONE, " +
				// "COD_TIPO_ISTITUTO, TIPO_ISTITUTO.RV_MEANING DESC_TIPO_ISTITUTO,"+
				// "COD_LUOGO_ISTITUTO, LUOGO_ISTITUTO.DESCRIZIONE DESC_LUOGO_ISTITUTO,"+
				"ALTRO_LUOGO, " + "NOTE, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " FROM ALTRA_CAUSA, CG_REF_CODES AUTORITA, COMUNE LUOGO_AUTORITA, CG_REF_CODES TIPO_POS_GIU ";
		// modifica relativa al tipo istituto
		// lStatement += " CG_REF_CODES TIPO_ISTITUTO,";
		// lStatement += " COMUNE LUOGO_ISTITUTO";
		lStatement += " WHERE AUTORITA.RV_DOMAIN = 'TIPO_UFFICIO' AND AUTORITA.RV_LOW_VALUE = COD_AUTORITA ";
		lStatement += " AND LUOGO_AUTORITA.COD_COMUNE =  COD_LUOGO";
		lStatement += " AND TIPO_POS_GIU.RV_DOMAIN = 'POSIZIONE_GIURIDICA' AND TIPO_POS_GIU.RV_LOW_VALUE = COD_TIPO_POS_GIURIDICA ";
		// modifica relativa al tipo istituto
		// lStatement += " AND TIPO_ISTITUTO.RV_DOMAIN = 'TIPO_ISTITUTO' AND TIPO_ISTITUTO.RV_LOW_VALUE =
		// COD_TIPO_ISTITUTO ";
		// lStatement += " AND LUOGO_ISTITUTO.COD_COMUNE = COD_LUOGO_ISTITUTO";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		AltraCausaModel aModel = new AltraCausaModel();

		aModel.setIdAltraCausa(getBigDecimal("ID_ALTRA_CAUSA"));
		aModel.setAnno(getBigDecimal("ANNO"));
		aModel.setNumero(getString("NUMERO"));
		aModel.setData(getDate("DATA"));
		aModel.setCodLuogo(getString("COD_LUOGO"));
		aModel.setDescrLuogo(getString("DESC_LUOGO_AUTORITA"));
		aModel.setCodAutorita(getString("COD_AUTORITA"));
		aModel.setDescrAutorita(getString("DESC_AUTORITA"));
		aModel.setDataDecorrenza(getDate("DATA_DECORRENZA"));
		aModel.setDataScadenza(getDate("DATA_SCADENZA"));
		aModel.setCodTipoPosGiuridica(getString("COD_TIPO_POS_GIURIDICA"));
		aModel.setDescrTipoPosGiuridica(getString("DESC_TIPO_POS_GIU"));
		// modifica relativa al tipo istituto
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		// aModel.setCodTipoIstituto(getString("COD_TIPO_ISTITUTO") );
		// aModel.setDescrTipoIstituto(getString("DESC_TIPO_ISTITUTO") );
		// aModel.setCodLuogoIstituto(getString("COD_LUOGO_ISTITUTO") );
		// aModel.setDescrLuogoIstituto(getString("DESC_LUOGO_ISTITUTO") );
		aModel.setAltroLuogo(getString("ALTRO_LUOGO"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		return aModel;
	}

	public String setCondizione(AltraCausaModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;

		return lCondizioni;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aIdFascicolo) {
		return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ALTRA_CAUSA = " + aKey;
	}

}
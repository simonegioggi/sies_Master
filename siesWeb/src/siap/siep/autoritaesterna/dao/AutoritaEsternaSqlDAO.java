package siap.siep.autoritaesterna.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: AutoritaEsternaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella AutoritaEsterna
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
public class AutoritaEsternaSqlDAO extends SIAPSqlDAO {

	public AutoritaEsternaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaAutoritaEsternaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryComune();
		lSql += " " + setCondizioniByKey(aKey);
		lSql += " UNION ";
		lSql += getSqlQueryNazione();
		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	protected String getSqlQueryComune() {
		String lStatement = new String("");

		lStatement += " SELECT ID_AUTORITA_ESTERNA, COD_TIPO_AUTORITA, COD.RV_MEANING DESCR_TIPO_AUT, AUT.DESCRIZIONE DESCRIZIONE, ";
		lStatement += "COD_SEDE, COM.DESCRIZIONE SEDE, COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += "FROM AUTORITA_ESTERNA AUT, CG_REF_CODES COD,COMUNE COM ";
		lStatement += "WHERE COD.RV_DOMAIN = 'TIPO_AUTORITA' AND ";
		lStatement += " COD.RV_LOW_VALUE = COD_TIPO_AUTORITA AND";
		lStatement += " COM.COD_COMUNE = COD_SEDE ";
		return lStatement;
	}

	protected String getSqlQueryNazione() {
		String lStatement = new String("");
		lStatement += " SELECT ID_AUTORITA_ESTERNA, COD_TIPO_AUTORITA, COD.RV_MEANING DESCR_TIPO_AUT, AUT.DESCRIZIONE DESCRIZIONE, ";
		lStatement += "COD_SEDE, SEDE.RV_MEANING SEDE, COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += "FROM AUTORITA_ESTERNA AUT, CG_REF_CODES COD, CG_REF_CODES SEDE ";
		lStatement += "WHERE COD.RV_DOMAIN = 'TIPO_AUTORITA' AND ";
		lStatement += " COD.RV_LOW_VALUE = COD_TIPO_AUTORITA AND";
		lStatement += " SEDE.RV_DOMAIN = 'NAZIONE' AND ";
		lStatement += " SEDE.RV_LOW_VALUE = COD_SEDE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		AutoritaEsternaModel aModel = new AutoritaEsternaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdAutoritaEsterna(getBigDecimal("ID_AUTORITA_ESTERNA"));
		aModel.setCodTipoAutorita(getString("COD_TIPO_AUTORITA"));
		aModel.setDescrTipoAutorita(getString("DESCR_TIPO_AUT"));
		aModel.setDescrizione(getString("DESCRIZIONE"));
		// aModel.setDescrizione(getString("DESCR_TIPO_AUT") );
		aModel.setCodSede(getString("COD_SEDE"));
		aModel.setDescrSede(getString("SEDE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		return aModel;
	}

	public String setCondizione(AutoritaEsternaModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_AUTORITA_ESTERNA = " + aKey;
	}

	public String setCondizioniByFascicoloTDS(BigDecimal aFascicolo) {
		return " AND ID_AUTORITA_ESTERNA = " + aFascicolo;
	}

}
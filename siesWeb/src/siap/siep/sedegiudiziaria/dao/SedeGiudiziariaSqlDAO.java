package siap.siep.sedegiudiziaria.dao;

import java.sql.Connection;

import siap.siep.sedegiudiziaria.model.SedeGiudiziariaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: SedeGiudiziariaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SedeGiudiziaria
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
public class SedeGiudiziariaSqlDAO extends SqlDAO {

	public SedeGiudiziariaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaSedeGiudiziaria(SedeGiudiziariaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaSedeGiudiziariaByKey(String aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaSedeGiudiziariaByDescrizione(String aDescrizione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " DESCRIZIONE = '" + aDescrizione.trim().toUpperCase() + "' ";
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "COD_SEDE_GIUDIZIARIA, " + "DESCRIZIONE, " + "DATA_CARICAMENTO_REGE, "
				+ "COD_COMUNE ";
		lStatement += " FROM SEDE_GIUDIZIARIA";
		lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		SedeGiudiziariaModel aModel = new SedeGiudiziariaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setCodSedeGiudiziaria(getString("COD_SEDE_GIUDIZIARIA"));
		// aModel.setDescrSedeGiudiziaria(getString("") );
		aModel.setDescrizione(getString("DESCRIZIONE"));
		aModel.setDataCaricamentoRege(getDate("DATA_CARICAMENTO_REGE"));
		aModel.setCodComune(getString("COD_COMUNE"));
		// aModel.setDescrComune(getString("") );
		return aModel;
	}

	public String setCondizione(SedeGiudiziariaModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(String aKey) {
		return " COD_SEDE_GIUDIZIARIA = '" + aKey + "'";
	}

}
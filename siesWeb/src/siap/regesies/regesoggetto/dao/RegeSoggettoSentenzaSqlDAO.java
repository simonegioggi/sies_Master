package siap.regesies.regesoggetto.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: RegeSoggettoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RegeSoggetto
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
public class RegeSoggettoSentenzaSqlDAO extends SIAPSqlDAO {

	public RegeSoggettoSentenzaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRegeSoggettoSentenza(RegeSentenzaModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " AND ANNO_SENTENZA = " + aModel.getAnnoSentenza() + " AND NUMERO_SENTENZA = '"
				+ aModel.getNumeroSentenza() + "'" + " AND COD_LUOGO_EMITTENTE = '"
				+ aModel.getCodLuogoEmittente() + "'" + " AND COD_TIPO_AUTORITA_EMITTENTE = '"
				+ aModel.getCodTipoAutoritaEmittente() + "'";

		lSql += " ORDER BY COGNOME,NOME";
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "rege_soggetto.ID_FILE ID_FILE, " + "COGNOME, " + "NOME, "
				+ "DATA_NASCITA, " + "SESSO," + "COD_COMUNE_NASCITA, " + " COM.DESCRIZIONE COMUNE,"
				+ "COD_PROVINCIA_NASCITA, " + " PRO.RV_MEANING PROVINCIA," + "COD_STATO_NASCITA, "
				+ " NAZ.RV_MEANING STATO," + "DESC_COMUNE_NASCITA_ESTERO, " + "NAZIONALITA, "
				+ " DECODENAZ.RV_MEANING DESCRNAZ" + " FROM rege_soggetto,rege_sentenza,"
				+ " CG_REF_CODES NAZ," + " CG_REF_CODES PRO," + " CG_REF_CODES DECODENAZ ," + " COMUNE COM  "
				+ " WHERE  rege_soggetto.id_file=rege_sentenza.id_file "
				+ " and  NAZ.RV_LOW_VALUE = COD_STATO_NASCITA AND " + " NAZ.RV_DOMAIN = 'NAZIONE' AND "
				+ " PRO.RV_LOW_VALUE = COD_PROVINCIA_NASCITA "
				+ " AND PRO.RV_DOMAIN = 'PROVINCIA' AND DECODENAZ.RV_LOW_VALUE = NAZIONALITA "
				+ " AND DECODENAZ.RV_DOMAIN='NAZIONALITA' AND COD_COMUNE_NASCITA = COM.COD_COMUNE";

		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		RegeSoggettoModel aModel = new RegeSoggettoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFile(getString("ID_FILE"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setSesso(getString("SESSO"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		aModel.setDescrComuneNascita(getString("COMUNE"));
		aModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		aModel.setDescrProvinciaNascita(getString("PROVINCIA"));
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		aModel.setDescrStatoNascita(getString("STATO"));
		aModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		aModel.setNazionalita(getString("NAZIONALITA"));
		return aModel;
	}

	public String setCondizione(RegeSoggettoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(String aKey) {
		return " AND ID_file = '" + aKey + "'";
	}

}
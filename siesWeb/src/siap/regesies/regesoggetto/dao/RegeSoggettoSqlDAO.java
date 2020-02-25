package siap.regesies.regesoggetto.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
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
public class RegeSoggettoSqlDAO extends SIAPSqlDAO {

	public RegeSoggettoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRegeSoggetto(RegeSoggettoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaRegeSoggettoByKey(String aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_FILE, " + "FLAG_TIPO_SOGG, " + "COD_FISCALE, " + "COD_CS, "
				+ "COD_AFIS, " + "COGNOME, " + "NOME, " + "ANNO_NASCITA, " + "DATA_NASCITA, "
				+ "COD_COMUNE_NASCITA, " + " COM.DESCRIZIONE COMUNE," + "COD_PROVINCIA_NASCITA, "
				+ " PRO.RV_MEANING PROVINCIA," + "COD_STATO_NASCITA, " + " NAZ.RV_MEANING STATO,"
				+ "DESC_COMUNE_NASCITA_ESTERO, " + "NAZIONALITA, " + " DECODENAZ.RV_MEANING DESCRNAZ,"
				+ "PATERNITA, " + "COGNOME_MADRE, " + "NOME_MADRE, " + "SESSO, " + "ATTO_NASCITA, " + "NOTE, "
				+ "DENO_SOGG, " + "RAGI_SOGG, " + "NOME_RAPP_LEGA, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO " + " FROM rege_soggetto,"
				+ " CG_REF_CODES NAZ," + " CG_REF_CODES PRO," + " CG_REF_CODES DECODENAZ ," + " COMUNE COM  "
				+ " WHERE  NAZ.RV_LOW_VALUE = COD_STATO_NASCITA AND " + " NAZ.RV_DOMAIN = 'NAZIONE' AND "
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
		aModel.setFlagTipoSogg(getString("FLAG_TIPO_SOGG"));
		aModel.setCodFiscale(getString("COD_FISCALE"));
		// aModel.setDescrFiscale(getString("") );
		aModel.setCodCs(getString("COD_CS"));
		// aModel.setDescrCs(getString("") );
		aModel.setCodAfis(getString("COD_AFIS"));
		// aModel.setDescrAfis(getString("") );
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setAnnoNascita(getInt("ANNO_NASCITA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		aModel.setDescrComuneNascita(getString("COMUNE"));
		aModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		aModel.setDescrProvinciaNascita(getString("PROVINCIA"));
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		aModel.setDescrStatoNascita(getString("STATO"));
		aModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		aModel.setNazionalita(getString("NAZIONALITA"));
		aModel.setPaternita(getString("PATERNITA"));
		aModel.setCognomeMadre(getString("COGNOME_MADRE"));
		aModel.setNomeMadre(getString("NOME_MADRE"));
		aModel.setSesso(getString("SESSO"));
		aModel.setAttoNascita(getString("ATTO_NASCITA"));
		aModel.setNote(getString("NOTE"));
		aModel.setDenoSogg(getString("DENO_SOGG"));
		aModel.setRagiSogg(getString("RAGI_SOGG"));
		aModel.setNomeRappLega(getString("NOME_RAPP_LEGA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
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
package siap.siep.risultatoricerca.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;
import siap.siep.risultatoricerca.model.RisultatoRicercaModel;

/**
 * <p>
 * Title: RisultatoRicercaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RisultatoRicerca
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

public class RisultatoRicercaSqlDAO extends SqlDAO {
	public RisultatoRicercaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRisultatoRicerca(RisultatoRicercaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaRisultatoRicercaByKeyPage(BigDecimal aKey, int aPage) throws DAOException {
		String lSql = getSqlQuery();
		String lPaginedStatement = new String("");

		lSql += " " + setCondizioniByKey(aKey);
		lSql += setOrderAsc();
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ ") INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND "
				+ (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);

	}

	public void ricercaCompletoById(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		lSql += setOrderAsc();
		setStatement(lSql);
	}

	public void getCountRicercaRisultatoByKey(BigDecimal aKey) throws DAOException

	{
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM RISULTATO_RICERCA";
		lStatement += " WHERE ID_RICERCA = " + aKey;
		setStatement(lStatement);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_RICERCA, " + "COD_UFFICIO, " + "ID_FASCICOLO_SIEP, " + "CHIAVE_ANNO, "
				+ "CHIAVE_PROGR, " + "COGNOME, " + "NOME, " + "LUOGO_NASCITA, " + "DATA_NASCITA, "
				+ "DATA_REATO, " + "DATA_FINE_PENA, " + "NUM_ANNI_PENA_RES, " + "NUM_MESI_PENA_RES, "
				+ "NUM_GIORNI_PENA_RES, " + "COD_POSIZIONE_GIURIDICA, " + "DESCR_POSIZIONE_GIURIDICA, "
				+ "COD_UTENTE, " + "NAZIONALITA";
		lStatement += " FROM RISULTATO_RICERCA";
		lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		RisultatoRicercaModel aModel = new RisultatoRicercaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRicerca(getBigDecimal("ID_RICERCA"));
		aModel.setCodUfficio(getString("COD_UFFICIO"));
		// aModel.setDescrUfficio(getString("") );
		aModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setLuogoNascita(getString("LUOGO_NASCITA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDataReato(getDate("DATA_REATO"));
		aModel.setDataFinePena(getDate("DATA_FINE_PENA"));
		aModel.setNumAnniPenaRes(getBigDecimal("NUM_ANNI_PENA_RES"));
		aModel.setNumMesiPenaRes(getBigDecimal("NUM_MESI_PENA_RES"));
		aModel.setNumGiorniPenaRes(getBigDecimal("NUM_GIORNI_PENA_RES"));
		aModel.setCodPosizioneGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		// aModel.setDescrPosizioneGiuridica(getString("") );
		aModel.setDescrPosizioneGiuridica(getString("DESCR_POSIZIONE_GIURIDICA"));
		aModel.setCodUtente(getString("COD_UTENTE"));
		aModel.setNazionalita(getString("NAZIONALITA"));
		return aModel;
	}

	public String setCondizione(RisultatoRicercaModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setOrderAsc() {
		String lOrd = new String();
		lOrd += " ORDER BY DATA_FINE_PENA ASC,NUM_ANNI_PENA_RES ASC,NUM_MESI_PENA_RES ASC,NUM_GIORNI_PENA_RES ASC";

		return lOrd;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " ID_RICERCA = " + aKey;
	}

}
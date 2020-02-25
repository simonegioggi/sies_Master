package siap.siep.alias.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.alias.model.AliasModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: AliasSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Alias
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
public class AliasSqlDAO extends SqlDAO {

	public AliasSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaAlias(AliasModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaAliasByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaAliasByIdSoggetto(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdSog(aKey);
		setStatement(lSql);
	}

	public void ricercaAliasByIdSoggettoPaged(BigDecimal aKey, int aPage) throws DAOException {
		String lSql = getSqlQuery();
		String lPaginedStatement = new String("");
		lSql += " " + setCondizioniByIdSog(aKey);

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE_ESITO + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE_ESITO;
		setStatement(lPaginedStatement);
	}

	public void getCountAliasByIdSoggetto(BigDecimal aKey) throws DAOException

	{
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM ALIAS WHERE SOG_ID_SOGGETTO =" + aKey;

		setStatement(lStatement);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ali.ID_ALIAS, " + "ali.COGNOME, " + "ali.NOME, " + "ali.PATERNITA, "
				+ "ali.COD_FISCALE, " + "ali.COD_CS, " + "ali.COD_AFIS, " + "ali.ATTO_NASCITA, "
				+ "ali.SESSO, "
				+ "ali.COD_COMUNE_NASCITA, "
				+ "ali.COD_PROVINCIA_NASCITA, "
				+ "ali.COD_STATO_NASCITA, "
				+ "ali.DATA_NASCITA, "
				+ "NAZ.RV_MEANING STATO, "
				+ "ali.NOTE, "
				+ "COMU.DESCRIZIONE DESCRIZ, "
				+ " PROV.RV_MEANING PROVINCIA, "
				+
				// "DECODENAZ1.RV_MEANING DESCRNAZ, " +
				"ali.COD_OPERATORE_INSERIMENTO, " + "ali.DATA_INSERIMENTO, "
				+ "ali.COD_UFFICIO_INSERIMENTO, " + "ali.COD_OPERATORE_AGGIORNAMENTO, "
				+ "ali.DATA_AGGIORNAMENTO, " + "ali.COD_UFFICIO_AGGIORNAMENTO, "
				+ "ali.DESC_COMUNE_NASCITA_ESTERO, "
				+ "ali.SOG_ID_SOGGETTO "
				+ " FROM ALIAS ali, "
				+ " SOGGETTO sog, "
				+ // Paolo Cherubini 27/04/2011
				" CG_REF_CODES NAZ," + " CG_REF_CODES PROV, " + " COMUNE COMU " + " WHERE  "
				+ " ali.COD_COMUNE_NASCITA = COMU.COD_COMUNE "
				+ " AND NAZ.RV_LOW_VALUE = ali.COD_STATO_NASCITA " + " AND PROV.RV_DOMAIN = 'PROVINCIA' "
				+ " AND NAZ.RV_DOMAIN = 'NAZIONE' " + " and sog.ID_SOGGETTO = ali.SOG_ID_SOGGETTO " + // Paolo
																										// Cherubini
																										// 27/04/2011
				" AND ali.COD_PROVINCIA_NASCITA = PROV.RV_LOW_VALUE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		AliasModel aModel = new AliasModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdAlias(getBigDecimal("ID_ALIAS"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setPaternita(getString("PATERNITA"));
		aModel.setCodFiscale(getString("COD_FISCALE"));
		// aModel.setDescrFiscale(getString("") );
		aModel.setCodCs(getString("COD_CS"));
		// aModel.setDescrCs(getString("") );
		aModel.setCodAfis(getString("COD_AFIS"));
		// aModel.setDescrAfis(getString("") );
		aModel.setAttoNascita(getString("ATTO_NASCITA"));
		aModel.setSesso(getString("SESSO"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		aModel.setDescrComuneNascita(getString("DESCRIZ"));
		aModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		aModel.setDescrProvinciaNascita(getString("PROVINCIA"));
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		aModel.setDescrStatoNascita(getString("STATO"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		return aModel;
	}

	public String setCondizione(AliasModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ali.ID_ALIAS = " + aKey;
	}

	public String setCondizioniByIdSog(BigDecimal aKey) {
		return " AND ali.SOG_ID_SOGGETTO = " + aKey;

	}

	// paolo cherubini 03/01/2011 per supersoggetto creo questi 2 metodi per effettuare la ricerca dell'alias
	// con tutti i campi del model. Riempio il campo a null anche se è uguale a "".
	// Questa ricerca serve per trovare l'id del primo alias corrispondente all'insieme dei campi.
	public void ricercaSuperAlias(SoggettoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneSuperAlias(aModel);
		setStatement(lSql);
	}

	public String setCondizioneSuperAlias(SoggettoModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;

		// MEV_39: risolta casistica per unix (l'atto di nascita comprende il carattere ' --> NA'00 287
		if (aModel.getAttoNascita() != null && aModel.getAttoNascita() != "")
			lCondizioni += " AND ali.ATTO_NASCITA = '" + StringUtils.convertSqlString(aModel.getAttoNascita()) + "'";
		else
			lCondizioni += " AND ali.ATTO_NASCITA is null";

		// Paolo Cherubini 27/04/2011 il cui dell'alias è sempre null devo quindi considerare quello del
		// soggetto
		/*
		 * if (aModel.getCodAfis() !=null && aModel.getCodAfis() != "") lCondizioni +=
		 * " AND ali.COD_AFIS = '"+aModel.getCodAfis()+"'"; else lCondizioni += " AND ali.COD_AFIS is null";
		 */

		if (aModel.getCodAfis() != null && aModel.getCodAfis() != "")
			lCondizioni += " AND sog.COD_AFIS = '" + aModel.getCodAfis() + "'";
		else
			lCondizioni += " AND sog.COD_AFIS is null";
		// Paolo Cherubini 27/04/2011 fine

		if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita() != "")
			lCondizioni += " AND ali.COD_COMUNE_NASCITA = '" + aModel.getCodComuneNascita() + "'";
		else
			lCondizioni += " AND ali.COD_COMUNE_NASCITA is null";

		if (aModel.getCodCs() != null && aModel.getCodCs() != "")
			lCondizioni += " AND ali.COD_CS = '" + aModel.getCodCs() + "'";
		else
			lCondizioni += " AND ali.COD_CS is null";

		if (aModel.getCodFiscale() != null && aModel.getCodFiscale() != "")
			lCondizioni += " AND ali.COD_FISCALE = '" + aModel.getCodFiscale() + "'";
		else
			lCondizioni += " AND ali.COD_FISCALE is null";

		if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita() != "")
			lCondizioni += " AND ali.COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "'";
		else
			lCondizioni += " AND ali.COD_PROVINCIA_NASCITA is null";

		if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita() != "")
			lCondizioni += " AND ali.COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "'";
		else
			lCondizioni += " AND ali.COD_STATO_NASCITA is null";

		if (aModel.getCognome() != null && aModel.getCognome() != "")
			lCondizioni += " AND ali.COGNOME = '" + StringUtils.convertSqlString(aModel.getCognome()) + "'";
		else
			lCondizioni += " AND ali.COGNOME is null";

		if (aModel.getDataNascita() != null) // DATE
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(ali.DATA_NASCITA) = to_date('"
					+ DateUtils.getDateToString(aModel.getDataNascita(), "dd/MM/yyyy") + "','DD-MM-YYYY')";
		else
			lCondizioni += " AND ali.DATA_NASCITA is null";

		if (aModel.getDescComuneNascitaEstero() != null && aModel.getDescComuneNascitaEstero().length() > 0)
			lCondizioni += " AND upper(ali.DESC_COMUNE_NASCITA_ESTERO) = '"
					+ StringUtils.convertSqlString(aModel.getDescComuneNascitaEstero().toUpperCase()) + "'";
		else
			lCondizioni += " AND ali.DESC_COMUNE_NASCITA_ESTERO is null";

		if (aModel.getNome() != null && aModel.getNome() != "")
			lCondizioni += " AND ali.NOME = '" + StringUtils.convertSqlString(aModel.getNome()) + "'";
		else
			lCondizioni += " AND ali.NOME is null";

		if (aModel.getPaternita() != null && aModel.getPaternita() != "")
			lCondizioni += " AND upper(ali.PATERNITA) = '"
					+ StringUtils.convertSqlString(aModel.getPaternita().toUpperCase()) + "'";
		else
			lCondizioni += " AND ali.PATERNITA is null";

		if (aModel.getSesso() != null && aModel.getSesso() != "")
			lCondizioni += " AND ali.SESSO = '" + aModel.getSesso() + "'";
		else
			lCondizioni += " AND ali.SESSO is null";

		return lCondizioni;
	}
	// fine paolo cherubini 03/01/2011 per supersoggetto

}
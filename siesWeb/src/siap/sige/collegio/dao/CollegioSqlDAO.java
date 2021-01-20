package siap.sige.collegio.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.sezione.model.SezioneModel;

/**
 * <p>
 * Title: CollegioSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Collegio
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
public class CollegioSqlDAO extends SIAPSqlDAO {

	public CollegioSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaCollegio(CollegioModel aModel) throws DAOException {

		String lSql = getSqlCollegioQuery();

		lSql += "  " + setCondizione(aModel);
		lSql += " ORDER BY SEZ.CODICE,COD_COLLEGIO,DATA_INIZIO_VALIDITA ";
		setStatement(lSql);
	}

	/**
	 * METODO CHE CONTA IL NUMERO DI COLLEGI A PARTIRE DAI FILTRI SETTATI SU COLLEGIO MODEL
	 *
	 * introdotto per 11.2.1
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void getNumRicercaCollegio(CollegioModel aCollegio) throws DAOException {

		String lSql = getSqlCollegioPagedQuery();

		if (aCollegio.getCollegioMagistrati() != null && aCollegio.getCollegioMagistrati().length > 0) {
			lSql += " LEFT JOIN COLLEGIO_MAGISTRATO COLMAG ON COLMAG.COL_ID_COLLEGIO = COLL.ID_COLLEGIO ";
		}

		lSql += "  " + setCondizione(aCollegio);
		if (aCollegio.getCollegioMagistrati() != null && aCollegio.getCollegioMagistrati().length > 0) {
			lSql += " AND   COLMAG.MAG_COD_MAGISTRATO ='"
					+ aCollegio.getCollegioMagistrati()[0].getMagCodMagistrato() + "'";
		}
		lSql += " ORDER BY SEZ.CODICE,COD_COLLEGIO,DATA_INIZIO_VALIDITA ";
		setStatement(lSql);
	}

	public void ricercaCollegioByKey(BigDecimal aKey) throws DAOException {

		String lSql = getSqlCollegioQuery();

		lSql += " WHERE " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaCollegioByCodUfficio(String aCodUfficio) throws DAOException {

		String lSql = getSqlCollegioQuery();
		lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
		lSql += " ORDER BY SEZ.CODICE,COD_COLLEGIO,DATA_INIZIO_VALIDITA ";
		setStatement(lSql);
	}

	// 20171005: [SG] aggiunto codice magistrato
	public void ricercaMaxCodCollegio(CollegioModel aModel, String codMagis, String dataUdienza)
			throws DAOException {

		String lSql = "";
		if (Utils.isPresent(codMagis)) {
			lSql += "select nvl(out.cod_coll, 0) || '#' || out.id_collegio as max"
					+ " from (select max(to_number(coll.cod_collegio)) over(partition by m.mag_cod_magistrato) cod_coll,"
					+ " coll.id_collegio" + " from COLLEGIO COLL, collegio_magistrato m, UDIENZA_SIGE U"
					+ " WHERE COLL.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "'";
			// 20171122: [EC] aggiungo in condition solo se è presente un valore per IdSezione
			if (Utils.isPresent(aModel.getSezIdSezione())) {
				lSql += " AND COLL.SEZ_ID_SEZIONE = '"
						+ (Utils.isPresent(aModel.getSezIdSezione()) ? aModel.getSezIdSezione() : "") + "'";
			}

			lSql += " and m.col_id_collegio = coll.id_collegio" + " and m.mag_cod_magistrato = '" + codMagis
					+ "'";
			// and to_char(u.data_udienza, 'dd/MM/yyyy') = '18/12/2018'
			lSql += " and to_char(u.data_udienza, 'dd/MM/yyyy') = '" + dataUdienza + "'";
			lSql += " and u.id_udienza_sige = m.udi_id_udienza_sige"
					+ " and u.col_id_collegio=coll.id_collegio "
					// intervento per 11.2.1
					// + " and m.mag_cod_magistrato=u.cod_giudice"
					+ " and coll.mag_cod_magistrato = '" + codMagis + "'" + ") out";
		} else {
			lSql += "select nvl(max(to_number(coll.cod_collegio)), 0) as max from COLLEGIO COLL";
			lSql += " " + setCondizioneMaxCodCollegio(aModel);
		}
		setStatement(lSql);
	}

	/**
	 * query per ricercare il max cod_collegio per sezione ed ufficio di appartenenza indipendentemente dal
	 * magistrato presidente
	 *
	 * // 20171122: [EC]
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaMaxCodCollegio(CollegioModel aModel) throws DAOException {

		String lSql = "select nvl(max(to_number(coll.cod_collegio)), 0) as max from COLLEGIO COLL ";
		lSql += " " + setCondizioneMaxCodCollegio(aModel);
		setStatement(lSql);
	}

	protected String getSqlCollegioQuery() {

		String lStatement = new String("");

		lStatement += "SELECT " + " COLL.ID_COLLEGIO, " + " COLL.COD_COLLEGIO, " + " COLL.SEZ_ID_SEZIONE, "
				+ " SEZ.CODICE, " + " SEZ.DESCRIZIONE, " + " COLL.COD_UFFICIO_APPARTENENZA, "
				+ " COLL.DATA_INIZIO_VALIDITA, " + " COLL.DATA_FINE_VALIDITA, "
				+ " COLL.COD_OPERATORE_INSERIMENTO, " + " COLL.DATA_INSERIMENTO, "
				+ " COLL.COD_UFFICIO_INSERIMENTO, " + " COLL.COD_OPERATORE_AGGIORNAMENTO, "
				+ " COLL.DATA_AGGIORNAMENTO, " + " COLL.COD_UFFICIO_AGGIORNAMENTO, COLL.MAG_COD_MAGISTRATO "
				+ "FROM COLLEGIO COLL " + " LEFT JOIN SEZIONE SEZ ON COLL.SEZ_ID_SEZIONE = SEZ.ID_SEZIONE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		CollegioModel aModel = new CollegioModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdCollegio(getBigDecimal("ID_COLLEGIO"));
		aModel.setCodCollegio(getString("COD_COLLEGIO"));
		aModel.setSezIdSezione(getBigDecimal("SEZ_ID_SEZIONE"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		// aggiungo per nuova gestione collegi per 11.2.1
		aModel.setMagCodMagistrato(getString("MAG_COD_MAGISTRATO"));
		// Dati afferenti alla Sezione.
		aModel.setSezione(new SezioneModel());
		aModel.getSezione().setIdSezione(aModel.getSezIdSezione());
		aModel.getSezione().setCodice(getString("CODICE"));
		aModel.getSezione().setDescrizione(getString("DESCRIZIONE"));

		// 20190507 [SG]: aggiunto campo in estrazione
		if (findColumn("DATA_UDIENZA"))
			aModel.setDataUdienza(getDate("DATA_UDIENZA"));

		return aModel;
	}

	public String setCondizione(CollegioModel aModel) {

		String lCondizioni = new String();
		boolean lInserito = false;

		// Imposta la condizione di filtro sul codice Ufficio di appartenenza.
		if ((aModel.getCodUfficioAppartenenza()).length() != 0) {
			lCondizioni = " COLL.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "'";
			lInserito = true;
		}

		// Imposta la condizione di filtro sul Codice Collegio.
		if (aModel.getCodCollegio() != null && (aModel.getCodCollegio()).length() != 0) {
			if (lInserito)
				lCondizioni += " AND";
			lCondizioni += " COD_COLLEGIO = '" + aModel.getCodCollegio() + "'";
			lInserito = true;
		}

		// Imposta la condizione di filtro sulla sezione.
		if (aModel.getSezIdSezione() != null) {
			if (lInserito)
				lCondizioni += " AND";
			lCondizioni += " COLL.SEZ_ID_SEZIONE = " + aModel.getSezIdSezione();
			lInserito = true;
		}

		// 20190507 [SG]: modificate le and condition
		// Imposta condizione di filtro sulle date
		// 1 - Data Creazione Collegio >= della data richiesta
		if (aModel.getDataInserimento() != null) {
			if (lInserito)
				lCondizioni += " AND ";
			lCondizioni += " DATA_INIZIO_VALIDITA >= " + " TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy")
					+ "','DD/MM/YYYY')";
			lInserito = true;
		}

		// INTERVENTO PER 11.2.1
		// 2 - Filtra per data udienza dal - al
		if (aModel.getDataInizioValidita() != null && aModel.getDataFineValidita() != null) {
			if (lInserito)
				lCondizioni += " AND ";
			lCondizioni += " U.DATA_UDIENZA BETWEEN TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataInizioValidita(), "dd/MM/yyyy")
					+ "','DD/MM/YYYY') AND TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataFineValidita(), "dd/MM/yyyy")
					+ "','DD/MM/YYYY')";
			lInserito = true;
		}

		// 3 - Filtra per tutte le occorrenze che hanno la data di fine validità a null
		if (aModel.getMessage() != null && aModel.getMessage().equalsIgnoreCase("dataFineisNull")) {
			if (lInserito)
				lCondizioni += " AND ";
			lCondizioni += " DATA_FINE_VALIDITA IS NULL ";
			lInserito = true;
		}

		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	public String setCondizioneMaxCodCollegio(CollegioModel aModel) {

		String lCondizioni = "";
		String cong = "WHERE";

		// Imposta la condizione di filtro sul codice Ufficio di appartenenza.
		if ((aModel.getCodUfficioAppartenenza()).length() != 0) {
			lCondizioni += cong + " COLL.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza()
					+ "'";
			cong = "AND";
		}

		// Imposta la condizione di filtro sulla sezione.
		if (aModel.getSezIdSezione() != null) {
			lCondizioni += cong + " COLL.SEZ_ID_SEZIONE = " + aModel.getSezIdSezione();
			cong = "AND";
		}

		return lCondizioni;
	}

	/**
	 * Metodo che imposta il filtro di condizione con l'id
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal id sezione.
	 * @return String stringa di ritorno con la condizione.
	 */
	public String setCondizioniByKey(BigDecimal aKey) {
		return " ID_COLLEGIO = " + aKey;
	}

	// 20171006: [SG] aggiunto metodo di ricerca
	public void ricercaCollegioBySezCodColl(BigDecimal sezIdSezione, String codCollegio,
			String codUfficioAppartenenza) throws DAOException {

		String lSql = "select c.id_collegio as id from collegio c where";
		// 20171020: [EC] aggiungo la sezione in query solo se not null
		if (sezIdSezione != null)
			lSql += " c.sez_id_sezione = '" + sezIdSezione + "'" + " and ";
		lSql += "  c.cod_collegio = '" + codCollegio + "'";
		lSql += " and c.COD_UFFICIO_APPARTENENZA = '" + codUfficioAppartenenza + "'";
		lSql += " and c.data_fine_validita is null";
		setStatement(lSql);
	}

	public void ricercaCollegioPaged(CollegioModel aCollegio, int lPagina) throws DAOException {

		String lSql = getSqlCollegioPagedQuery();

		if (aCollegio.getCollegioMagistrati() != null && aCollegio.getCollegioMagistrati().length > 0) {
			lSql += " LEFT JOIN COLLEGIO_MAGISTRATO COLMAG ON COLMAG.COL_ID_COLLEGIO = COLL.ID_COLLEGIO ";
		}

		String lPaginedStatement = new String("");
		lSql += "  " + setCondizione(aCollegio);

		if (aCollegio.getCollegioMagistrati() != null && aCollegio.getCollegioMagistrati().length > 0) {
			lSql += " AND   COLMAG.MAG_COD_MAGISTRATO ='"
					+ aCollegio.getCollegioMagistrati()[0].getMagCodMagistrato() + "'";
		}

		lSql += " ORDER BY SEZ.CODICE,COD_COLLEGIO,DATA_INIZIO_VALIDITA ";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((lPagina - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (lPagina) * IWebConstants.RESULT_PER_PAGE;
		setStatement(lPaginedStatement);
	}

	protected String getSqlCollegioPagedQuery() {

		String lStatement = new String("");

		lStatement += "SELECT DISTINCT " + " COLL.ID_COLLEGIO, " + " COLL.COD_COLLEGIO, "
				+ " COLL.SEZ_ID_SEZIONE," + " SEZ.CODICE, " + " SEZ.DESCRIZIONE, "
				+ " COLL.COD_UFFICIO_APPARTENENZA," + " COLL.DATA_INIZIO_VALIDITA, "
				+ " COLL.DATA_FINE_VALIDITA," + " COLL.COD_OPERATORE_INSERIMENTO, "
				+ " COLL.DATA_INSERIMENTO," + " COLL.COD_UFFICIO_INSERIMENTO, "
				+ " COLL.COD_OPERATORE_AGGIORNAMENTO," + " COLL.DATA_AGGIORNAMENTO, "
				+ " COLL.COD_UFFICIO_AGGIORNAMENTO, COLL.MAG_COD_MAGISTRATO"
				// 20190507 [SG]: aggiunti campi in estrazione
				+ ", U.DATA_UDIENZA" + " FROM COLLEGIO COLL "
				+ " LEFT JOIN SEZIONE SEZ ON COLL.SEZ_ID_SEZIONE = SEZ.ID_SEZIONE";
		// 20190507 [SG]: aggiunta left join
		lStatement += " LEFT JOIN UDIENZA_SIGE U ON U.COL_ID_COLLEGIO = COLL.ID_COLLEGIO";
		return lStatement;
	}

	// 20190507 [SG]: aggiunto metodo privato
	// private boolean findColumn(String aValue) {
	//
	// try {
	// mRs.findColumn(aValue);
	// } catch (Exception sqex) {
	// return false;
	// }
	// return true;
	// }

}
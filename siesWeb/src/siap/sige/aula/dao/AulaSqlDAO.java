package siap.sige.aula.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.sezione.model.SezioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: AulaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella AULA_UDIENZA
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class AulaSqlDAO extends SIAPSqlDAO {

	public AulaSqlDAO(Connection con) {
		super(con);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + " AULA.ID_AULA, " + " AULA.ID_SEZIONE, " + " SEZ.CODICE, "
				+ " SEZ.DESCRIZIONE, " + " AULA.DESCRIZIONE_AULA, " + " AULA.DESCRIZIONE_STANZA, "
				+ " AULA.DESCRIZIONE_INGRESSO, " + " AULA.NUMERO_PIANO, " + " AULA.FLAG_PREDEFINITA, "
				+ " AULA.COD_OPERATORE_INSERIMENTO, " + " AULA.DATA_INSERIMENTO, "
				+ " AULA.COD_UFFICIO_INSERIMENTO, " + " AULA.COD_OPERATORE_AGGIORNAMENTO, "
				+ " AULA.DATA_AGGIORNAMENTO, " + " AULA.COD_UFFICIO_AGGIORNAMENTO "
				+ "FROM AULA_UDIENZA AULA " + " LEFT JOIN SEZIONE SEZ ON AULA.ID_SEZIONE = SEZ.ID_SEZIONE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		AulaUdienzaModel aModel = new AulaUdienzaModel();

		aModel.setIdAula(getBigDecimal("ID_AULA"));
		aModel.setIdSezione(getBigDecimal("ID_SEZIONE"));
		aModel.setDescrizioneAula(getString("DESCRIZIONE_AULA"));
		aModel.setDescrizioneStanza(getString("DESCRIZIONE_STANZA"));
		aModel.setDescrizioneIngresso(getString("DESCRIZIONE_INGRESSO"));
		// 20170908: è un varchar nel db
		// aModel.setNumeroPiano(getBigDecimal("NUMERO_PIANO"));
		aModel.setNumeroPiano(getString("NUMERO_PIANO"));
		aModel.setFlagPredefinita(getString("FLAG_PREDEFINITA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		// Dati afferenti alla Sezione.
		aModel.setSezione(new SezioneModel());
		aModel.getSezione().setIdSezione(aModel.getIdSezione());
		aModel.getSezione().setCodice(getString("CODICE"));
		aModel.getSezione().setDescrizione(getString("DESCRIZIONE"));

		return aModel;
	}

	//
	// METODO RICERCA()
	//
	public void ricercaAula(AulaUdienzaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  " + setCondizione(aModel);

		lSql += " ORDER BY AULA.ID_SEZIONE, AULA.DESCRIZIONE_AULA ";
		setStatement(lSql);
	}

	public void ricercaAulaByKey(BigDecimal aIdAula, BigDecimal aIdSezione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  " + setCondizioniByKey(aIdAula, aIdSezione);

		setStatement(lSql);
	}

	public void ricercaAulaById(BigDecimal aIdAula) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  " + " WHERE AULA.ID_AULA = " + aIdAula;

		setStatement(lSql);
	}

	public void ricercaAulaByIdSezione(BigDecimal idSezione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  " + setCondizioniByIdSezione(idSezione);
		setStatement(lSql);
	}

	public void ricercaAulaByDescrizione(BigDecimal aIdSezione, String descAula) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  " + setCondizioniByDescAula(aIdSezione, descAula);

		setStatement(lSql);
	}

	public void ricercaAulaPredefinitaSezione(String aIdSezione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += "  " + setCondizioniByIdSezioneAulaPredefinita(aIdSezione);

		setStatement(lSql);
	}

	/**
	 * Metodo che imposta il filtro di condizione con l'id della sezione
	 * <p>
	 * 
	 * @param idSezione
	 * @return String stringa di ritorno con la condizione.
	 */
	public String setCondizioniByIdSezione(BigDecimal idSezione) {
		String lCondizioni = new String();

		lCondizioni = " WHERE AULA.ID_SEZIONE = " + idSezione;

		return lCondizioni;
	}

	public String setCondizione(AulaUdienzaModel aModel) {
		String lCondizioni = new String();
		boolean lInserito = false;

		// Imposta la condizione di filtro sul codice Ufficio di appartenenza.
		if ((aModel.getCodUfficioInserimento()).length() != 0) {
			lCondizioni = " AULA.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lInserito = true;
		}

		// Imposta la condizione di filtro sulla sezione.
		if (aModel.getIdSezione() != null) {
			if (lInserito)
				lCondizioni += " AND";

			lCondizioni += " AULA.ID_SEZIONE = " + aModel.getIdSezione();
			lInserito = true;
		}

		// Imposta la condizione di filtro sulla descrizione aula.
		if (aModel.getDescrizioneAula() != null && !aModel.getDescrizioneAula().equals("")) {
			if (lInserito)
				lCondizioni += " AND";

			lCondizioni += " upper(AULA.DESCRIZIONE_AULA) like '%"
					+ aModel.getDescrizioneAula().trim().toUpperCase() + "%'";
			lInserito = true;
		}

		// Imposta la condizione di filtro sulla descrizione stanza.
		if (aModel.getDescrizioneStanza() != null && !aModel.getDescrizioneStanza().equals("")) {
			if (lInserito)
				lCondizioni += " AND";

			lCondizioni += " upper(AULA.DESCRIZIONE_STANZA) like '%"
					+ aModel.getDescrizioneStanza().trim().toUpperCase() + "%'";
			lInserito = true;
		}

		// Imposta la condizione di filtro sulla descrizione ingresso.
		if (aModel.getDescrizioneIngresso() != null && !aModel.getDescrizioneIngresso().equals("")) {
			if (lInserito)
				lCondizioni += " AND";

			lCondizioni += " upper(AULA.DESCRIZIONE_INGRESSO) like '%"
					+ aModel.getDescrizioneIngresso().trim().toUpperCase() + "%'";
			lInserito = true;
		}

		// Imposta la condizione di filtro sulla descrizione ingresso.
		// 20171002: [EC] introdotto il controllo sul valore di numero piano diverso da stringa vuota
		if (aModel.getNumeroPiano() != null && !aModel.getNumeroPiano().equals("")) {
			if (lInserito)
				lCondizioni += " AND";

        lCondizioni += " upper(AULA.NUMERO_PIANO) like '%" + aModel.getNumeroPiano().trim().toUpperCase() + "%'";
			lInserito = true;
		}

		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	/**
	 * Metodo che imposta il filtro di condizione con la chiave dela tabella AULA_UDIENZA
	 * <p>
	 * 
	 * @param aIdAula
	 *            .
	 * @param aIdSezione
	 *            .
	 * @return String stringa di ritorno con la condizione.
	 */
	public String setCondizioniByKey(BigDecimal aIdAula, BigDecimal aIdSezione) {
		String lCondizioni = new String();

		lCondizioni += " WHERE AULA.ID_AULA = " + aIdAula;
		lCondizioni += " AND AULA.ID_SEZIONE = " + aIdSezione;

		return lCondizioni;
	}

	/**
	 * Metodo che imposta il filtro di condizione con l'id sezione e la descrizione dell'aula
	 * <p>
	 * 
	 * @param aIdSezione
	 *            .
	 * @param descAula
	 *            .
	 * @return String stringa di ritorno con la condizione.
	 */
	public String setCondizioniByDescAula(BigDecimal aIdSezione, String descAula) {
		String lCondizioni = new String();

		lCondizioni += " WHERE AULA.ID_SEZIONE = " + aIdSezione;
		lCondizioni += " AND AULA.DESCRIZIONE_AULA = UPPER('" + descAula.trim() + "')";

		return lCondizioni;
	}

	public String setCondizioniByIdSezioneAulaPredefinita(String aIdSezione) {
		String lCondizioni = new String();

		lCondizioni += " WHERE AULA.ID_SEZIONE = " + aIdSezione;
		lCondizioni += " AND UPPER (AULA.FLAG_PREDEFINITA) = 'S'";

		return lCondizioni;
	}

}
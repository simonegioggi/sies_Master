package siap.sico.note.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.note.model.NoteModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: NoteSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Note
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
public class NoteSqlDAO extends SIAPSqlDAO {

	/**
	 * Costruttore di calsse con argomento.
	 * <p>
	 * 
	 * @param con
	 *            connessione al dbase.
	 */
	public NoteSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaNote(NoteModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	/**
	 * Imposta lo statement per la ricerca di un campo nota per la sua chiave.
	 * <p>
	 * 
	 * @param aKey
	 *            valore della chiave.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaNoteByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	/**
	 * Imposta lo statement per ricerca di cmapi nota per l'id dell'evento.
	 * <p>
	 * 
	 * @param aKeyEvento
	 *            chiave id evento.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void ricercaNoteByIdFascicoloSius(BigDecimal aIdFascicoloSius) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE " + setCondizioniByIdFascicoloSius(aIdFascicoloSius);
		lSql += " ORDER BY DATA";
		setStatement(lSql);
	}

	/**
	 * Stringa SQL per l'entità gestita.
	 * <p>
	 * 
	 * @return la stringa SQL.
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT ";
		lStatement += "ID_NOTE, ";
		lStatement += "DATA, ";
		lStatement += "DESCRIZIONE, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += "FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += "FAS_SIGE_ID_FASCICOLO_SIGE ";
		lStatement += " FROM NOTE";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		NoteModel aModel = new NoteModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdNote(getBigDecimal("ID_NOTE"));
		aModel.setData(getDate("DATA"));
		aModel.setDescrizione(getString("DESCRIZIONE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSigeIdFascicoloSige(getBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE"));

		return aModel;
	}

	public String setCondizione(NoteModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;

		return lCondizioni;
	}

	/**
	 * Imposta la condizione di ricerca per id_note.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id note.
	 * @return la stringa SQL di condizione.
	 */
	public String setCondizioniByKey(BigDecimal aKey) {
		return " ID_NOTE = " + aKey;
	}

	/**
	 * Imposta la condizione di ricerca per id evento.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id campo nota.
	 * @return la stringa SQL di condizione.
	 */
	public String setCondizioniByIdFascicoloSius(BigDecimal aIdFascicoloSius) {
		return " FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicoloSius;
	}

	/**
	 * Imposta la condizione di ricerca per id evento.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id campo nota.
	 * @return la stringa SQL di condizione.
	 */
	public String setCondizioniByIdFascicoloSige(BigDecimal aIdFascicoloSige) {
		return " FAS_SIGE_ID_FASCICOLO_SIGE = " + aIdFascicoloSige;
	}

	/**
	 * Ricerca le note associate al Fascicolo Sige.
	 * <p>
	 * 
	 * @param aIdFascicoloSige
	 *            chiave id Fascicolo Sige.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void ricercaNoteByIdFascicoloSige(BigDecimal aIdFascicoloSige) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE " + setCondizioniByIdFascicoloSige(aIdFascicoloSige);
		lSql += " ORDER BY DATA";
		setStatement(lSql);
	}

}
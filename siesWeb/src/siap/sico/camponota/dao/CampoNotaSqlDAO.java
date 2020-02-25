package siap.sico.camponota.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: CampoNotaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella CampoNota
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
public class CampoNotaSqlDAO extends SIAPSqlDAO {

	/**
	 * Costruttore di calsse con argomento.
	 * <p>
	 * 
	 * @param con
	 *            connessione al dbase.
	 */
	public CampoNotaSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaCampoNota(CampoNotaModel aModel) throws DAOException {

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
	public void ricercaCampoNotaByKey(BigDecimal aKey) throws DAOException {

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
	public void ricercaCampoNotaByKeyEvento(BigDecimal aKeyEvento) throws DAOException {

		String lSql = getSqlQuery();
		//@emma 13072018 intervento post COLLAUDO 11.2 (aggiungo clausola su ordinamento)
		lSql += " WHERE " + setCondizioniByKeyEvento(aKeyEvento) + " ORDER BY PROGRESSIVO , DATA_INSERIMENTO desc";

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
	public void ricercaCampoNotaByKeyEventoDesc(BigDecimal aKeyEvento) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " WHERE " + setCondizioniByKeyEvento(aKeyEvento) + " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/**
	 * Stringa SQL per l'entita' gestita.
	 * <p>
	 * 
	 * @return la stringa SQL.
	 */
	protected String getSqlQuery() {

		String lStatement = new String("");

		lStatement += " SELECT ";
		lStatement += "ID_CAMPO_NOTA, ";
		lStatement += "PROGRESSIVO, ";
		lStatement += "DESCR, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "EVE_ID_EVENTO, ";
		lStatement += "OGGETTO_NOTA_RES, "; // STUB 2005/01/31
		lStatement += "FAS_SIE_ID_FASCICOLO_SIEP "; // STUB 2005/01/31
		lStatement += " FROM CAMPO_NOTA";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		CampoNotaModel aModel = new CampoNotaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdCampoNota(getBigDecimal("ID_CAMPO_NOTA"));
		aModel.setProgressivo(getBigDecimal("PROGRESSIVO"));
		aModel.setDescr(getString("DESCR"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setOggettoNotaRes(getString("OGGETTO_NOTA_RES")); // STUB 31/01/2005
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP")); // STUB 31/01/2005

		return aModel;
	}

	public String setCondizione(CampoNotaModel aModel) {

		String lCondizioni = new String();
//		boolean lInserito = false;

		return lCondizioni;
	}

	/**
	 * Imposta la condizione di ricerca per id campo.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id campo nota.
	 * @return la stringa SQL di condizione.
	 */
	public String setCondizioniByKey(BigDecimal aKey) {

		return " ID_CAMPO_NOTA = " + aKey;
	}

	/**
	 * Imposta la condizione di ricerca per id evento.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id campo nota.
	 * @return la stringa SQL di condizione.
	 */
	public String setCondizioniByKeyEvento(BigDecimal aKeyEvento) {

		return " EVE_ID_EVENTO = " + aKeyEvento;
	}

}
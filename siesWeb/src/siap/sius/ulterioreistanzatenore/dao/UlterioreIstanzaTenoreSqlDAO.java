package siap.sius.ulterioreistanzatenore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sius.ulterioreistanzatenore.model.UlterioreIstanzaTenoreModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: UlterioreIstanzaTenoreSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella UlterioreIstanzaTenore
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
public class UlterioreIstanzaTenoreSqlDAO extends SqlDAO {

	public UlterioreIstanzaTenoreSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaUlterioreIstanzaTenore(UlterioreIstanzaTenoreModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	/**
	 * Metodo che esegue la ricerca del tenore attraverso il proprio ID.
	 * <p>
	 * 
	 * @param aKey
	 *            Chiave di ricerca del tenore.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaUlterioreIstanzaTenoreByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	/**
	 * Metodo che esegue la ricerca dei tenori afferenti ad una ulteriore istanza, individuabile attraverso
	 * l'id di relazione.
	 * <p>
	 * 
	 * @param aKey
	 *            Chiave di relazione con la tabella ulteriore istanza.
	 * @throws DAOException
	 *             propaga errore di eccezzione.
	 */
	public void ricercaUlterioreIstanzaTenoreByUltIstKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + this.setCondizioneByUltIstKey(aKey);
		setStatement(lSql);
	}

	/**
	 * 
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + "ID_ULTERIORE_ISTANZA_TENORE, " + "COD_OGGETTO_TENORE, "
				+ "MOTIVO_PROVV.RV_MEANING AS DESCR_OGGETTO_TENORE, " + "DATA, " + "COD_DETTAGLIO_OGGETTO, "
				+ "MITTENTE_ATTO.RV_MEANING AS DESCR_DETTAGLIO_OGGETTO, " + "COD_MAGISTRATO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "ULT_IST_ID_ULTERIORE_ISTANZA ";

		lStatement += " FROM ULTERIORE_ISTANZA_TENORE "
				+ "INNER JOIN CG_REF_CODES MOTIVO_PROVV ON MOTIVO_PROVV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND MOTIVO_PROVV.RV_LOW_VALUE = ULTERIORE_ISTANZA_TENORE.COD_OGGETTO_TENORE "
				+ "INNER JOIN CG_REF_CODES MITTENTE_ATTO ON MITTENTE_ATTO.RV_DOMAIN = 'MITTENTE_ATTO' AND MITTENTE_ATTO.RV_LOW_VALUE = ULTERIORE_ISTANZA_TENORE.COD_DETTAGLIO_OGGETTO ";

		lStatement += " WHERE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		UlterioreIstanzaTenoreModel aModel = new UlterioreIstanzaTenoreModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdUlterioreIstanzaTenore(getBigDecimal("ID_ULTERIORE_ISTANZA_TENORE"));
		aModel.setCodOggettoTenore(getString("COD_OGGETTO_TENORE"));
		aModel.setDescrOggettoTenore(getString("DESCR_OGGETTO_TENORE"));
		aModel.setData(getDate("DATA"));
		aModel.setCodDettaglioOggetto(getString("COD_DETTAGLIO_OGGETTO"));
		aModel.setDescrDettaglioOggetto(getString("DESCR_DETTAGLIO_OGGETTO"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		// aModel.setDescrMagistrato(getString("") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setUltIstIdUlterioreIstanza(getBigDecimal("ULT_IST_ID_ULTERIORE_ISTANZA"));

		return aModel;
	}

	/**
	 * 
	 * @param aModel
	 * @return
	 */
	public String setCondizione(UlterioreIstanzaTenoreModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	/**
	 * Imposta la condizione di filtro, per la ricerca dei tenori afferenti all'ulteriore istanza.
	 * <p>
	 * 
	 * @param aKey
	 *            Id dell'ulteriore istanza.
	 * @return Ritorna la stringa.
	 */
	public String setCondizioneByUltIstKey(BigDecimal aKey) {
		return " ULT_IST_ID_ULTERIORE_ISTANZA = " + aKey;
	}

	/**
	 * Imposta la condizione di filtro, per la ricerca del tenore afferente all'id.
	 * <p>
	 * 
	 * @param aKey
	 *            id di ricerca tenore.
	 * @return ritorna la stringa.
	 */
	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ULTERIORE_ISTANZA_TENORE = " + aKey;
	}

}
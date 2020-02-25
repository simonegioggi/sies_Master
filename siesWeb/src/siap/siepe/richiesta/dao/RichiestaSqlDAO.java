package siap.siepe.richiesta.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siepe.richiesta.model.RichiestaModel;

/**
 * <p>
 * Title: RichiestaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Richiesta
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
public class RichiestaSqlDAO extends SqlDAO {

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param con
	 *            Connection parametro del costruttore
	 */
	public RichiestaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Metodo che crea la query SQL per la ricerca di Richieste, i quali parametri di ricerca sono valorizzati
	 * nel relativo model, passato come argomento.
	 * <p>
	 * 
	 * @param aModel
	 *            RichiestaModel Model contenente i valori di ricerca.
	 * @throws DAOException
	 *             propaga errori di eccezione.
	 */
	/*
	 * public void ricercaRichiesta( RichiestaModel aModel) throws DAOException { String lSql = getSqlQuery();
	 * 
	 * lSql += " " + setCondizione(aModel);
	 * 
	 * setStatement(lSql); }
	 */
	public void ricercaRichiesta(RichiestaModel aModel) throws DAOException {
		String lSql = getSqlQueryWithJoins();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	/**
	 * Metodo che imposta la query di ricerca di una Richiesta previo l'id.
	 * <p>
	 * 
	 * @param aKey
	 *            BigDecimal chiave di ricerca
	 * @throws DAOException
	 *             propaga errore di eccezione
	 */
	public void ricercaRichiestaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioneByKey(aKey);
		setStatement(lSql);
	}

	/**
	 * Metodo che crea la select sino alla FROM, necessaria per statement di interrogazione.
	 * <p>
	 * 
	 * @return String ritorna la select composta.
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT RICHIESTA.ID_RICHIESTA, " + "RICHIESTA.DATA_RICHIESTA, "
				+ "RICHIESTA.COD_TIPO_RICHIESTA, " + "RICHIESTA.COD_TIPO_RICHIEDENTE, " + "RICHIESTA.NOTE, "
				+ "RICHIESTA.DOC_BLOB, " + "RICHIESTA.COD_OPERATORE_INSERIMENTO, "
				+ "RICHIESTA.DATA_INSERIMENTO, " + "RICHIESTA.COD_UFFICIO_INSERIMENTO, "
				+ "RICHIESTA.COD_OPERATORE_AGGIORNAMENTO, " + "RICHIESTA.DATA_AGGIORNAMENTO, "
				+ "RICHIESTA.COD_UFFICIO_AGGIORNAMENTO, " + "RICHIESTA.FAS_SIE_ID_FAS_SIEPE, "
				+ "RICHIESTA.FLAG_DOCUMENTO_REGISTRATO, " + "RICHIESTA.COD_UFFICIO_DESTINATARIO  "
				+ " FROM RICHIESTA " + " WHERE ";

		// STUB : 20060916
		// Capire se sostituire la SQL Query con questa qui di seguito riportata e uniformare il metodo di
		// ricerca e
		// eliminare il metodo getSqlQueryWithJoinWithoutBLOB, poichè il BLOB viene recuperato con un'altra
		// sqlquerey, puntuale.
		/*
		 * lStatement += "SELECT RICHIESTA.ID_RICHIESTA, " + "RICHIESTA.DATA_RICHIESTA, " +
		 * "RICHIESTA.COD_TIPO_RICHIESTA, " + "TIPO_RICHIESTA.RV_MEANING DESC_TIPO_RICHIESTA, "+
		 * "RICHIESTA.COD_TIPO_RICHIEDENTE, "+ "TIPO_RICHIEDENTE.RV_MEANING DESC_TIPO_RICHIEDENTE, "+
		 * "RICHIESTA.NOTE, " + "RICHIESTA.COD_OPERATORE_INSERIMENTO, " + "RICHIESTA.DATA_INSERIMENTO, " +
		 * "RICHIESTA.COD_UFFICIO_INSERIMENTO, " + "RICHIESTA.COD_OPERATORE_AGGIORNAMENTO, " +
		 * "RICHIESTA.DATA_AGGIORNAMENTO, " + "RICHIESTA.COD_UFFICIO_AGGIORNAMENTO, " +
		 * "RICHIESTA.FAS_SIE_ID_FAS_SIEPE, " + "RICHIESTA.FLAG_DOCUMENTO_REGISTRATO, " +
		 * "RICHIESTA.COD_UFFICIO_DESTINATARIO, " + "TIPO_UFFICIO.RV_MEANING DESC_UFFICIO_DESTINATARIO " +
		 * "FROM RICHIESTA " +
		 * "INNER JOIN CG_REF_CODES TIPO_RICHIESTA ON TIPO_RICHIESTA.RV_DOMAIN = 'TIPO_RICHIESTA_SIEPE' AND TIPO_RICHIESTA.RV_LOW_VALUE = RICHIESTA.COD_TIPO_RICHIESTA "
		 * +
		 * "INNER JOIN CG_REF_CODES TIPO_RICHIEDENTE ON TIPO_RICHIEDENTE.RV_DOMAIN = 'TIPO_RICHIEDENTE_SIEPE' AND TIPO_RICHIEDENTE.RV_LOW_VALUE = RICHIESTA.COD_TIPO_RICHIEDENTE "
		 * + "INNER JOIN UFFICIO ON UFFICIO.COD_UFFICIO = RICHIESTA.COD_UFFICIO_DESTINATARIO " +
		 * "INNER JOIN CG_REF_CODES TIPO_UFFICIO ON TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_UFFICIO.RV_LOW_VALUE = UFFICIO.COD_TIPO_UFFICIO "
		 * + "WHERE ";
		 */

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	/**
	 * Metodo che ritorna il model di Richiesta con i dati prelevati dal Dbase.
	 * <p>
	 * 
	 * @throws DAOException
	 *             propaga errore di eccezione
	 * @return GenericModel istanza del model opportunamente popolato.
	 */
	public GenericModel getModel() throws DAOException {
		RichiestaModel aModel = new RichiestaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRichiesta(getBigDecimal("ID_RICHIESTA"));
		aModel.setDataRichiesta(getDate("DATA_RICHIESTA"));
		aModel.setCodTipoRichiesta(getString("COD_TIPO_RICHIESTA"));
		aModel.setDescrTipoRichiesta(getString("DESC_TIPO_RICHIESTA"));
		aModel.setCodTipoRichiedente(getString("COD_TIPO_RICHIEDENTE"));
		aModel.setDescrTipoRichiedente(getString("DESC_TIPO_RICHIEDENTE"));
		aModel.setNote(getString("NOTE"));
		// aModel.setDocBlob(getBlob("DOC_BLOB") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString(""));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString(""));
		aModel.setFasSieIdFasSiepe(getBigDecimal("FAS_SIE_ID_FAS_SIEPE"));
		aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setDescrUfficioDestinatario(getString("DESC_UFFICIO_DESTINATARIO"));
		aModel.setDescrSedeUfficioDestinatario(getString("DESC_SEDE_UFFICIO_DESTINATARIO"));

		return aModel;
	}

	/**
	 * Imposta le condizioni di filtro.
	 * <p>
	 * 
	 * @param aModel
	 *            RichiestaModel model contente i valori per le condizioni di filtro.
	 * @return String ritorna la stringa SQL con le condizioni di ricerca.
	 */
	public String setCondizione(RichiestaModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;

		if (aModel.getFasSieIdFasSiepe() != null) {
			lCondizioni += " FAS_SIE_ID_FAS_SIEPE = " + aModel.getFasSieIdFasSiepe();
		}

		return lCondizioni;
	}

	/**
	 * Imposta la condizione di ricerca per la chiave.
	 * <p>
	 * 
	 * @param aKey
	 *            BigDecimal Chiave di ricerca
	 * @return String stringa SQL composta.
	 */
	public String setCondizioneByKey(BigDecimal aKey) {
		return " ID_RICHIESTA = " + aKey;
	}

	/**
	 * Esegue la ricerca di una richiesta attraverso l'id. la select ritorna i codici opportunamente
	 * decodificati con la propria descrizione.
	 * <p>
	 * 
	 * @param aId
	 *            BigDecimal Chiave di ricerca.
	 */
	public void ricercaRichiestaById(BigDecimal aId) {
		String lStatement = new String();
		// Preleva la Stringa SQL con le decodifice in JOINS dei codici in descrizioni.
		lStatement = this.getSqlQueryWithJoins();
		// Imposta la condizioni della chiave ID di ricerca.
		lStatement += this.setCondizioneByKey(aId);

		setStatement(lStatement);
	}

	/**
	 * Creazione della stringa SQL compresiva di JOINs per la decodifica dei campi di codifica Precisamente :
	 * -) CG_REF_CODES RV_DOMAIN = TIPO_RICHIESTA -) CG_REF_CODES RV_DOMAIN = TIPO_RICHIEDENTE -) UFFICIO -)
	 * CG_REF_CODES RV_DOMAIN = TIPO_UFFICIO -) COMUNE Inoltre, nella clausola SELECT non considera il campo
	 * di tipo BLOB. Tale stringa SQL termina con la clausola WEHERE da completare con l'opportuna condizione
	 * di filtro.
	 * <p>
	 * 
	 * @return String ritorna la stringa sql fino alla clausola WHERE.
	 */
	protected String getSqlQueryWithJoins() {
		String lStatement = new String();
		// Stringa di Select SQL con INNER JOINS.
		lStatement += "SELECT RICHIESTA.ID_RICHIESTA, " + "RICHIESTA.DATA_RICHIESTA, "
				+ "RICHIESTA.COD_TIPO_RICHIESTA, " + "TIPO_RICHIESTA.RV_MEANING DESC_TIPO_RICHIESTA, "
				+ "RICHIESTA.COD_TIPO_RICHIEDENTE, " + "TIPO_RICHIEDENTE.RV_MEANING DESC_TIPO_RICHIEDENTE, "
				+ "RICHIESTA.NOTE, " + "RICHIESTA.COD_OPERATORE_INSERIMENTO, "
				+ "RICHIESTA.DATA_INSERIMENTO, " + "RICHIESTA.COD_UFFICIO_INSERIMENTO, "
				+ "RICHIESTA.COD_OPERATORE_AGGIORNAMENTO, " + "RICHIESTA.DATA_AGGIORNAMENTO, "
				+ "RICHIESTA.COD_UFFICIO_AGGIORNAMENTO, " + "RICHIESTA.FAS_SIE_ID_FAS_SIEPE, "
				+ "RICHIESTA.FLAG_DOCUMENTO_REGISTRATO, " + "RICHIESTA.COD_UFFICIO_DESTINATARIO, "
				+ "TIPO_UFFICIO.RV_MEANING DESC_UFFICIO_DESTINATARIO, "
				+ "COMUNE.DESCRIZIONE DESC_SEDE_UFFICIO_DESTINATARIO " + "FROM RICHIESTA "
				+ "INNER JOIN CG_REF_CODES TIPO_RICHIESTA ON TIPO_RICHIESTA.RV_DOMAIN = 'TIPO_RICHIESTA_SIEPE' AND TIPO_RICHIESTA.RV_LOW_VALUE = RICHIESTA.COD_TIPO_RICHIESTA "
				+ "INNER JOIN CG_REF_CODES TIPO_RICHIEDENTE ON TIPO_RICHIEDENTE.RV_DOMAIN = 'TIPO_RICHIEDENTE_SIEPE' AND TIPO_RICHIEDENTE.RV_LOW_VALUE = RICHIESTA.COD_TIPO_RICHIEDENTE "
				+ "INNER JOIN UFFICIO ON UFFICIO.COD_UFFICIO = RICHIESTA.COD_UFFICIO_DESTINATARIO "
				+ "INNER JOIN CG_REF_CODES TIPO_UFFICIO ON TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_UFFICIO.RV_LOW_VALUE = UFFICIO.COD_TIPO_UFFICIO "
				+ "INNER JOIN COMUNE ON COMUNE.COD_COMUNE = UFFICIO.COD_COMUNE " + "WHERE ";

		return lStatement; // Ritorna la stringa appena composta.
	}

	/*
	 * protected String getSqlQueryElenco() { String lStatement = new String(); // Stringa di Select SQL con
	 * INNER JOINS. lStatement += "SELECT RICHIESTA.ID_RICHIESTA, " + "RICHIESTA.DATA_RICHIESTA, " +
	 * "RICHIESTA.COD_TIPO_RICHIESTA, " + "TIPO_RICHIESTA.RV_MEANING DESC_TIPO_RICHIESTA, "+
	 * "RICHIESTA.COD_TIPO_RICHIEDENTE, "+ "TIPO_RICHIEDENTE.RV_MEANING DESC_TIPO_RICHIEDENTE, "+ //"'', " +
	 * //"'', " + //"'', " + //"'', " + //"'', " + //"'', " + //"'', " + "RICHIESTA.FAS_SIE_ID_FAS_SIEPE, " +
	 * "RICHIESTA.FLAG_DOCUMENTO_REGISTRATO, " + //"'', " + //"'', " +
	 * "COMUNE.DESCRIZIONE DESC_SEDE_UFFICIO_DESTINATARIO " + "FROM RICHIESTA " +
	 * "INNER JOIN CG_REF_CODES TIPO_RICHIESTA ON TIPO_RICHIESTA.RV_DOMAIN = 'TIPO_RICHIESTA_SIEPE' AND TIPO_RICHIESTA.RV_LOW_VALUE = RICHIESTA.COD_TIPO_RICHIESTA "
	 * +
	 * "INNER JOIN CG_REF_CODES TIPO_RICHIEDENTE ON TIPO_RICHIEDENTE.RV_DOMAIN = 'TIPO_RICHIEDENTE_SIEPE' AND TIPO_RICHIEDENTE.RV_LOW_VALUE = RICHIESTA.COD_TIPO_RICHIEDENTE "
	 * + "INNER JOIN UFFICIO ON UFFICIO.COD_UFFICIO = RICHIESTA.COD_UFFICIO_DESTINATARIO " +
	 * "INNER JOIN CG_REF_CODES TIPO_UFFICIO ON TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_UFFICIO.RV_LOW_VALUE = UFFICIO.COD_TIPO_UFFICIO "
	 * + "INNER JOIN COMUNE ON COMUNE.COD_COMUNE = UFFICIO.COD_COMUNE " + "WHERE ";
	 * 
	 * return lStatement; // Ritorna la stringa appena composta.
	 * 
	 * }
	 */

}
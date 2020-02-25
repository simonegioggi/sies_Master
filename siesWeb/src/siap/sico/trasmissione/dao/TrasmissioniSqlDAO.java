package siap.sico.trasmissione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.trasmissione.model.TrasmissioniModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: TrasmissioniSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Trasmissioni
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
public class TrasmissioniSqlDAO extends SqlDAO {

	Logger logger = Logger.getLogger("sqldaoLogger");

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public TrasmissioniSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountTrasmissioni(TrasmissioniModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM TRASMISSIONI ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaTrasmissioniPaged(TrasmissioniModel aModel, int aPage) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		lStatement += " " + getOrderBy() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
		logger.info("lPaginedStatement = " + lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaTrasmissioni(TrasmissioniModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
		logger.info("lSql = " + lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaTrasmissioniByKey(BigDecimal aIdTrasmissione) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdTrasmissione);

		// Imposta lo statement da eseguire
		setStatement(lSql);
		logger.info("lSql = " + lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_TRASMISSIONE, " + "TIPO_TRASMISSIONE, " + "DATA_TRASMISSIONE, "
				+ "ESITO_TRASMISSIONE, " + "COD_ERRORE, " + "TIPO_OPERAZIONE, " + "DESTINAZIONE, "
				+ "CHIAVE_SIES_SOGG, " + "CHIAVE_SIES_FASC, " + "CHIAVE_NSC_SOGG, " + "CHIAVE_NSC_PROV "
				+ "CHIAVE_ANNO " + "CHIAVE_PROGR " + "COD_OPERATORE_INSERIMENTO " + "DATA_INSERIMENTO "
				+ "COD_UFFICIO_INSERIMENTO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM TRASMISSIONI";

		// lStatement +=
		// " (     nvl(TRASMISSIONI.COD_ERRORE,'-') = CODERRORE.RV_LOW_VALUE AND CODERRORE.RV_DOMAIN = 'ERRORE' ) ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		TrasmissioniModel aModel = new TrasmissioniModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdTrasmissione(getBigDecimal("ID_TRASMISSIONE"));
		aModel.setTipoTrasmissione(getString("TIPO_TRASMISSIONE"));
		aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
		aModel.setEsitoTrasmissione(getString("ESITO_TRASMISSIONE"));
		aModel.setCodErrore(getString("COD_ERRORE"));
		// aModel.setDescrErrore(getString("") );
		aModel.setTipoOperazione(getString("TIPO_OPERAZIONE"));
		aModel.setDestinazione(getString("DESTINAZIONE"));
		aModel.setChiaveSiesSogg(getBigDecimal("CHIAVE_SIES_SOGG"));
		aModel.setChiaveSiesFasc(getBigDecimal("CHIAVE_SIES_FASC"));
		aModel.setChiaveNscSogg(getBigDecimal("CHIAVE_NSC_SOGG"));
		aModel.setChiaveNscProv(getBigDecimal("CHIAVE_NSC_PROV"));
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(TrasmissioniModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdTrasmissione() != null) {
			lCondizioni += " and ID_TRASMISSIONE = " + aModel.getIdTrasmissione() + "";
		}
		if (aModel.getTipoTrasmissione() != null && aModel.getTipoTrasmissione().length() > 0) {
			lCondizioni += " and TIPO_TRASMISSIONE = '" + aModel.getTipoTrasmissione() + "' ";
		}
		if (aModel.getDataTrasmissione() != null) {
			lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataTrasmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getEsitoTrasmissione() != null && aModel.getEsitoTrasmissione().length() > 0) {
			lCondizioni += " and ESITO_TRASMISSIONE = '" + aModel.getEsitoTrasmissione() + "' ";
		}
		if (aModel.getCodErrore() != null && aModel.getCodErrore().length() > 0) {
			lCondizioni += " and COD_ERRORE = '" + aModel.getCodErrore() + "' ";
		}
		if (aModel.getTipoOperazione() != null && aModel.getTipoOperazione().length() > 0) {
			lCondizioni += " and TIPO_OPERAZIONE = '" + aModel.getTipoOperazione() + "' ";
		}
		if (aModel.getDestinazione() != null && aModel.getDestinazione().length() > 0) {
			lCondizioni += " and DESTINAZIONE = '" + aModel.getDestinazione() + "' ";
		}
		if (aModel.getChiaveSiesSogg() != null) {
			lCondizioni += " and CHIAVE_SIES_SOGG = " + aModel.getChiaveSiesSogg() + "";
		}
		if (aModel.getChiaveSiesFasc() != null) {
			lCondizioni += " and CHIAVE_SIES_FASC = " + aModel.getChiaveSiesFasc() + "";
		}
		if (aModel.getChiaveNscSogg() != null) {
			lCondizioni += " and CHIAVE_NSC_SOGG = " + aModel.getChiaveNscSogg() + "";
		}
		if (aModel.getChiaveNscProv() != null) {
			lCondizioni += " and CHIAVE_NSC_PROV = " + aModel.getChiaveNscProv() + "";
		}
		if (aModel.getChiaveAnno() != null) {
			lCondizioni += " and CHIAVE_ANNO = " + aModel.getChiaveAnno() + "";
		}
		if (aModel.getChiaveProgr() != null) {
			lCondizioni += " and CHIAVE_PROGR = " + aModel.getChiaveProgr() + "";
		}

		if (aModel.getCodOperatoreInserimento() != null) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "'";
		}

		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}

		if (aModel.getCodUfficioInserimento() != null) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
		}

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdTrasmissione) {
		String lCondizioni = new String();

		lCondizioni += " and ID_TRASMISSIONE = " + aIdTrasmissione;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		// orderBy = " ORDER BY ";
		return orderBy;
	}

	public void ricercaTrasmissioniPerDateTipoEsito(Date dataRicercaInizio, Date dataRicercaFine,
			String lTipoTrasmissione, String lEsitoTrasmissione, String lUfficioUtenteConnesso, int aPage) {

		String lSql = "SELECT * FROM (SELECT INNER.*, ROWNUM rn FROM ";

		// ----> Query Principale
		lSql += "(SELECT * FROM Trasmissioni WHERE ID_TRASMISSIONE is not null ";

		if (dataRicercaInizio != null) {

			lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') >= TO_DATE('"
					+ DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy") + "', 'dd/MM/yyyy')";
		}

		if (dataRicercaFine != null) {
			lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') <= TO_DATE('"
					+ DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy") + "', 'dd/MM/yyyy')";
		}

		if (!lTipoTrasmissione.equals("") && !lTipoTrasmissione.equals("-")) {
			lSql += " and TIPO_TRASMISSIONE = '" + lTipoTrasmissione + "' ";
		}

		if (!lEsitoTrasmissione.equals("") && !lEsitoTrasmissione.equals("-")) {
			lSql += " and ESITO_TRASMISSIONE = '" + lEsitoTrasmissione + "' ";
		}
		lSql += " and COD_UFFICIO_INSERIMENTO = '" + lUfficioUtenteConnesso + "' ";
		lSql += "ORDER BY DATA_TRASMISSIONE DESC)";
		// ----> Query Principale
		lSql += "INNER)";

		lSql += " WHERE rn BETWEEN " + +((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND " + (aPage)
				* IWebConstants.RESULT_PER_PAGE;

		// Imposta lo statement da eseguire
		setStatement(lSql);

		logger.info("lSql = " + lSql);
	}

	public void getCountTrasmissioniPerDateTipoEsito(Date dataRicercaInizio, Date dataRicercaFine,
			String lTipoTrasmissione, String lEsitoTrasmissione, String lUfficioUtenteConnesso)
			throws DAOException {

		String lSql = "SELECT count(*) HowManyRecords FROM Trasmissioni WHERE ID_TRASMISSIONE is not null ";

		if (dataRicercaInizio != null) {
			lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') >= TO_DATE('"
					+ DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy") + "', 'dd/MM/yyyy')";
		}

		if (dataRicercaFine != null) {
			lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') <= TO_DATE('"
					+ DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy") + "', 'dd/MM/yyyy')";
		}

		if (!lTipoTrasmissione.equals("") && !lTipoTrasmissione.equals("-")) {
			lSql += " and TIPO_TRASMISSIONE = '" + lTipoTrasmissione + "' ";
		}

		if (!lEsitoTrasmissione.equals("") && !lEsitoTrasmissione.equals("-")) {
			lSql += " and ESITO_TRASMISSIONE = '" + lEsitoTrasmissione + "' ";
		}

		lSql += " and COD_UFFICIO_INSERIMENTO = '" + lUfficioUtenteConnesso + "' ";

		setStatement(lSql);
	}

}
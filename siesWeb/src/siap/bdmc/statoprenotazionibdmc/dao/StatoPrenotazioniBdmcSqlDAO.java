package siap.bdmc.statoprenotazionibdmc.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: StatoPrenotazioniBdmcSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella StatoPrenotazioniBdmc
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
public class StatoPrenotazioniBdmcSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public StatoPrenotazioniBdmcSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM STATO_PRENOTAZIONI_BDMC ";

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
	public void ricercaStatoPrenotazioniBdmcPaged(StatoPrenotazioniBdmcModel aModel, int aPage)
			throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		lStatement += " " + getOrderBy() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * ISIAPCostantiWeb.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * ISIAPCostantiWeb.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaStatoPrenotazioniBdmcByKey(BigDecimal aStatoPrenotazioniBdmc) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aStatoPrenotazioniBdmc);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "STATO_PRENOTAZIONI_BDMC, " + "ID_MISURA_CAUTELARE_BDMC, "
				+ "DATA_TRASMISSIONE, " + "ESITO_ID, " + "ESITO_MSG, " + "ID_PRENOTAZIONE, "
				+ "PROG_PERI_PRES, " + "TIPO_TRASMISSIONE ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM STATO_PRENOTAZIONI_BDMC";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		StatoPrenotazioniBdmcModel aModel = new StatoPrenotazioniBdmcModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setStatoPrenotazioniBdmc(getBigDecimal("STATO_PRENOTAZIONI_BDMC"));
		aModel.setIdMisuraCautelareBdmc(getBigDecimal("ID_MISURA_CAUTELARE_BDMC"));
		aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
		aModel.setEsitoId(getBigDecimal("ESITO_ID"));
		aModel.setEsitoMsg(getString("ESITO_MSG"));
		aModel.setIdPrenotazione(getBigDecimal("ID_PRENOTAZIONE"));
		aModel.setProgPeriPres(getBigDecimal("PROG_PERI_PRES"));
		aModel.setTipoTrasmissione(getString("TIPO_TRASMISSIONE"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(StatoPrenotazioniBdmcModel aModel) {
		String lCondizioni = new String();

		if (aModel.getStatoPrenotazioniBdmc() != null) {
			lCondizioni += " and STATO_PRENOTAZIONI_BDMC = " + aModel.getStatoPrenotazioniBdmc() + "";
		}
		if (aModel.getIdMisuraCautelareBdmc() != null) {
			lCondizioni += " and ID_MISURA_CAUTELARE_BDMC = " + aModel.getIdMisuraCautelareBdmc() + "";
		}
		if (aModel.getDataTrasmissione() != null) {
			lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataTrasmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getEsitoId() != null && aModel.getEsitoId().compareTo(new BigDecimal(0)) == 0) {
			lCondizioni += " and ESITO_ID = " + aModel.getEsitoId() + "";
		}
		if (aModel.getEsitoId() != null && aModel.getEsitoId().compareTo(new BigDecimal(0)) != 0) {
			lCondizioni += " and ESITO_ID <> 0 "; // + aModel.getEsitoId() + "";
		}
		if (aModel.getEsitoMsg() != null && aModel.getEsitoMsg().length() > 0) {
			lCondizioni += " and ESITO_MSG = '" + aModel.getEsitoMsg() + "' ";
		}
		if (aModel.getIdPrenotazione() != null) {
			lCondizioni += " and ID_PRENOTAZIONE = " + aModel.getIdPrenotazione() + "";
		}
		if (aModel.getProgPeriPres() != null) {
			lCondizioni += " and PROG_PERI_PRES = " + aModel.getProgPeriPres() + "";
		}
		if (aModel.getTipoTrasmissione() != null && aModel.getTipoTrasmissione().length() > 0) {
			lCondizioni += " and TIPO_TRASMISSIONE = '" + aModel.getTipoTrasmissione() + "' ";
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
	public String setCondizioniByKey(BigDecimal aStatoPrenotazioniBdmc) {
		String lCondizioni = new String();

		lCondizioni += " and STATO_PRENOTAZIONI_BDMC = " + aStatoPrenotazioniBdmc;

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

}
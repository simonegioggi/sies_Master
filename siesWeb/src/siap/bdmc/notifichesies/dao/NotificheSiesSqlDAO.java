package siap.bdmc.notifichesies.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: NotificheSiesSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella NotificheSies
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
public class NotificheSiesSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public NotificheSiesSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountNotificheSies(NotificheSiesModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM NOTIFICHE_SIES ";

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
	public void ricercaNotificheSiesPaged(NotificheSiesModel aModel, int aPage) throws DAOException {
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
	public void ricercaNotificheSies(NotificheSiesModel aModel) throws DAOException {
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
	public void ricercaNotificheSiesByKey(BigDecimal aIdNotificheSies) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdNotificheSies);

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

		lStatement += " SELECT " + "ID_NOTIFICHE_SIES, " + "ANNO_SIEP, " + "PROG_SIEP, " + "UFFICIO_SIEP, "
				+ "ANNO_FASC_BDMC, " + "UFFICIO_FASC_BDMC, " + "NUMERO_FASC_BDMC, " + "TIPO_NOTIFICA, "
				+ "DATA_NOTIFICA, " + "STATO_TRASMISSIONE, " + "DATA_TRASMISSIONE, " + "ID_PREN, "
				+ "PROG_PERI_PRES, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "ID_EVENTO, " + "ID_FASCICOLO_BDMC ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM NOTIFICHE_SIES";

		// lStatement +=
		// " (     nvl(NOTIFICHE_SIES.COD_OPERATORE_INSERIMENTO,'-') = CODOPERATOREINSERIMENTO.RV_LOW_VALUE AND CODOPERATOREINSERIMENTO.RV_DOMAIN = 'OPERATORE_INSERIMENTO' ) "
		// lStatement +=
		// " (     nvl(NOTIFICHE_SIES.COD_UFFICIO_INSERIMENTO,'-') = CODUFFICIOINSERIMENTO.RV_LOW_VALUE AND CODUFFICIOINSERIMENTO.RV_DOMAIN = 'UFFICIO_INSERIMENTO' ) "

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		NotificheSiesModel aModel = new NotificheSiesModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdNotificheSies(getBigDecimal("ID_NOTIFICHE_SIES"));
		aModel.setAnnoSiep(getBigDecimal("ANNO_SIEP"));
		aModel.setProgSiep(getBigDecimal("PROG_SIEP"));
		aModel.setUfficioSiep(getString("UFFICIO_SIEP"));
		aModel.setAnnoFascBdmc(getBigDecimal("ANNO_FASC_BDMC"));
		aModel.setUfficioFascBdmc(getString("UFFICIO_FASC_BDMC"));
		aModel.setNumeroFascBdmc(getBigDecimal("NUMERO_FASC_BDMC"));
		aModel.setTipoNotifica(getString("TIPO_NOTIFICA"));
		aModel.setDataNotifica(getDate("DATA_NOTIFICA"));
		aModel.setStatoTrasmissione(getString("STATO_TRASMISSIONE"));
		aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
		aModel.setIdPren(getBigDecimal("ID_PREN"));
		aModel.setProgPeriPres(getBigDecimal("PROG_PERI_PRES"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		aModel.setIdFascicoloBdmc(getBigDecimal("ID_FASCICOLO_BDMC"));

		// aModel.setDescrUfficioInserimento(getString("") );

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(NotificheSiesModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdNotificheSies() != null) {
			lCondizioni += " and ID_NOTIFICHE_SIES = " + aModel.getIdNotificheSies() + "";
		}
		if (aModel.getAnnoSiep() != null) {
			lCondizioni += " and ANNO_SIEP = " + aModel.getAnnoSiep() + "";
		}
		if (aModel.getProgSiep() != null) {
			lCondizioni += " and PROG_SIEP = " + aModel.getProgSiep() + "";
		}
		if (aModel.getUfficioSiep() != null && aModel.getUfficioSiep().length() > 0) {
			lCondizioni += " and UFFICIO_SIEP = '" + aModel.getUfficioSiep() + "' ";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getUfficioFascBdmc() != null && aModel.getUfficioFascBdmc().length() > 0) {
			lCondizioni += " and UFFICIO_FASC_BDMC = '" + aModel.getUfficioFascBdmc() + "' ";
		}
		if (aModel.getNumeroFascBdmc() != null) {
			lCondizioni += " and NUMERO_FASC_BDMC = " + aModel.getNumeroFascBdmc() + "";
		}
		if (aModel.getTipoNotifica() != null && aModel.getTipoNotifica().length() > 0) {
			lCondizioni += " and TIPO_NOTIFICA = '" + aModel.getTipoNotifica() + "' ";
		}
		if (aModel.getDataNotifica() != null) {
			lCondizioni += " and to_char(DATA_NOTIFICA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataNotifica(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getStatoTrasmissione() != null && aModel.getStatoTrasmissione().length() > 0) {
			lCondizioni += " and STATO_TRASMISSIONE = '" + aModel.getStatoTrasmissione() + "' ";
		}
		if (aModel.getDataTrasmissione() != null) {
			lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataTrasmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getProgPeriPres() != null) {
			lCondizioni += " and PROG_PERI_PRES = " + aModel.getProgPeriPres() + "";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getIdEvento() != null) {
			lCondizioni += " and ID_EVENTO = " + aModel.getIdEvento() + "";
		}
		if (aModel.getIdFascicoloBdmc() != null) {
			lCondizioni += " and ID_FASCICOLO_BDMC = " + aModel.getIdFascicoloBdmc() + "";
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
	public String setCondizioniByKey(BigDecimal aIdNotificheSies) {

		String lCondizioni = new String();

		lCondizioni += " and ID_NOTIFICHE_SIES = " + aIdNotificheSies;

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
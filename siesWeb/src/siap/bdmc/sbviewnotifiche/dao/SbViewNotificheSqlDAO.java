package siap.bdmc.sbviewnotifiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SbViewNotificheSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SbViewNotifiche
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
public class SbViewNotificheSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public SbViewNotificheSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountSbViewNotifiche(SbViewNotificheModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM SB_VIEW_NOTIFICHE@SIES_BDMC_LINK ";

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
	public void ricercaSbViewNotifichePaged(SbViewNotificheModel aModel, int aPage) throws DAOException {
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
	public void ricercaSbViewNotifiche(SbViewNotificheModel aModel) throws DAOException {
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
	public void ricercaSbViewNotificheByKey(BigDecimal aProgNoti) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aProgNoti);

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

		lStatement += " SELECT " + "PROG_NOTI, " + "ID_PROV_SIES, " + "CODI_NOTI, " + "DESCRIZIONE, "
				+ "DATA_INVI_NOTI, " + "DATA_REGI_NOTI, " + "DATA_VALI_NOTI, " + "NOTE, "
				+ "CODI_UFFI_SIES, " + "CODI_UFFI, " + "FLAG_STAT_NOTI, " + "DATA_CHIU_NOTI, "
				+ "FLAG_TRAS, " + "STOP_ANNO_FASC_BDMC, " + "STOP_NUME_FASC_BDMC, " + "UTEN_SIES, "
				+ "ID_PREN, " + "PROG_PERI, " + "MODI_ANNO_FASC_BDMC, " + "MODI_NUME_FASC_BDMC, "
				+ "FLAG_MODI, " + "DATA_INIZ, " + "DATA_FINE, " + "DATA_INIZ_PREC, " + "DATA_FINE_PREC ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM SB_VIEW_NOTIFICHE@SIES_BDMC_LINK";

		// lStatement +=
		// " (     nvl(SB_VIEW_NOTIFICHE.CODI_NOTI,'-') = CODINOTI.RV_LOW_VALUE AND CODINOTI.RV_DOMAIN = '_NOTI' ) "
		// lStatement +=
		// " (     nvl(SB_VIEW_NOTIFICHE.CODI_UFFI_SIES,'-') = CODIUFFISIES.RV_LOW_VALUE AND CODIUFFISIES.RV_DOMAIN = '_UFFI_SIES' ) "
		// lStatement +=
		// " (     nvl(SB_VIEW_NOTIFICHE.CODI_UFFI,'-') = CODIUFFI.RV_LOW_VALUE AND CODIUFFI.RV_DOMAIN = '_UFFI' ) "

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		SbViewNotificheModel aModel = new SbViewNotificheModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setProgNoti(getBigDecimal("PROG_NOTI"));
		aModel.setIdProvSies(getBigDecimal("ID_PROV_SIES"));
		aModel.setCodiNoti(getString("CODI_NOTI"));
		// aModel.setDescriNoti(getString("") );
		aModel.setDescrizione(getString("DESCRIZIONE"));
		aModel.setDataInviNoti(getDate("DATA_INVI_NOTI"));
		aModel.setDataRegiNoti(getDate("DATA_REGI_NOTI"));
		aModel.setDataValiNoti(getDate("DATA_VALI_NOTI"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodiUffiSies(getString("CODI_UFFI_SIES"));
		// aModel.setDescriUffiSies(getString("") );
		aModel.setCodiUffi(getString("CODI_UFFI"));
		String DescUff = "";
		try {
			DescUff = UfficioUtils.getDescTipoUffByCodUfficio(getString("CODI_UFFI"));
		} catch (F3BException e) {
			e.printStackTrace();
		}
		aModel.setDescriUffi(DescUff);
		aModel.setFlagStatNoti(getString("FLAG_STAT_NOTI"));
		aModel.setDataChiuNoti(getDate("DATA_CHIU_NOTI"));
		aModel.setFlagTras(getString("FLAG_TRAS"));
		aModel.setStopAnnoFascBdmc(getBigDecimal("STOP_ANNO_FASC_BDMC"));
		aModel.setStopNumeFascBdmc(getBigDecimal("STOP_NUME_FASC_BDMC"));
		aModel.setUtenSies(getString("UTEN_SIES"));
		aModel.setIdPren(getBigDecimal("ID_PREN"));
		aModel.setProgPeri(getBigDecimal("PROG_PERI"));
		aModel.setModiAnnoFascBdmc(getBigDecimal("MODI_ANNO_FASC_BDMC"));
		aModel.setModiNumeFascBdmc(getBigDecimal("MODI_NUME_FASC_BDMC"));
		aModel.setFlagModi(getString("FLAG_MODI"));
		aModel.setDataIniz(getDate("DATA_INIZ"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setDataInizPrec(getDate("DATA_INIZ_PREC"));
		aModel.setDataFinePrec(getDate("DATA_FINE_PREC"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(SbViewNotificheModel aModel) {
		String lCondizioni = new String();

		if (aModel.getProgNoti() != null) {
			lCondizioni += " and PROG_NOTI = " + aModel.getProgNoti() + "";
		}
		if (aModel.getCodiNoti() != null && aModel.getCodiNoti().length() > 0) {
			lCondizioni += " and CODI_NOTI = '" + aModel.getCodiNoti() + "' ";
		}
		if (aModel.getDescrizione() != null && aModel.getDescrizione().length() > 0) {
			lCondizioni += " and DESCRIZIONE = '" + aModel.getDescrizione() + "' ";
		}
		if (aModel.getDataInviNoti() != null) {
			lCondizioni += " and to_char(DATA_INVI_NOTI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInviNoti(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataRegiNoti() != null) {
			lCondizioni += " and to_char(DATA_REGI_NOTI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataRegiNoti(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataValiNoti() != null) {
			lCondizioni += " and to_char(DATA_VALI_NOTI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataValiNoti(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getCodiUffiSies() != null && aModel.getCodiUffiSies().length() > 0) {
			lCondizioni += " and (CODI_UFFI_SIES = '" + aModel.getCodiUffiSies()
					+ "' or CODI_UFFI_SIES = 'ALL' ) ";
		}
		if (aModel.getCodiUffi() != null && aModel.getCodiUffi().length() > 0) {
			lCondizioni += " and CODI_UFFI = '" + aModel.getCodiUffi() + "' ";
		}
		if (aModel.getFlagStatNoti() != null && aModel.getFlagStatNoti().length() > 0) {
			lCondizioni += " and FLAG_STAT_NOTI = '" + aModel.getFlagStatNoti() + "' ";
		}
		if (aModel.getDataChiuNoti() != null) {
			lCondizioni += " and to_char(DATA_CHIU_NOTI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataChiuNoti(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagTras() != null) {
			lCondizioni += " and FLAG_TRAS = '" + aModel.getFlagTras() + "'";
		}
		if (aModel.getStopAnnoFascBdmc() != null) {
			lCondizioni += " and STOP_ANNO_FASC_BDMC = " + aModel.getStopAnnoFascBdmc() + "";
		}
		if (aModel.getStopNumeFascBdmc() != null) {
			lCondizioni += " and STOP_NUME_FASC_BDMC = " + aModel.getStopNumeFascBdmc() + "";
		}
		if (aModel.getUtenSies() != null && aModel.getUtenSies().length() > 0) {
			lCondizioni += " and UTEN_SIES = '" + aModel.getUtenSies() + "' ";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getProgPeri() != null) {
			lCondizioni += " and PROG_PERI = " + aModel.getProgPeri() + "";
		}
		if (aModel.getModiAnnoFascBdmc() != null) {
			lCondizioni += " and MODI_ANNO_FASC_BDMC = " + aModel.getModiAnnoFascBdmc() + "";
		}
		if (aModel.getModiNumeFascBdmc() != null) {
			lCondizioni += " and MODI_NUME_FASC_BDMC = " + aModel.getModiNumeFascBdmc() + "";
		}
		if (aModel.getFlagModi() != null && aModel.getFlagModi().length() > 0) {
			lCondizioni += " and FLAG_MODI = '" + aModel.getFlagModi() + "' ";
		}
		if (aModel.getDataIniz() != null) {
			lCondizioni += " and to_char(DATA_INIZ,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataIniz(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFine() != null) {
			lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFine(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataInizPrec() != null) {
			lCondizioni += " and to_char(DATA_INIZ_PREC,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizPrec(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFinePrec() != null) {
			lCondizioni += " and to_char(DATA_FINE_PREC,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFinePrec(), "dd/MM/yyyy") + "' ";
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
	public String setCondizioniByKey(BigDecimal aProgNoti) {
		String lCondizioni = new String();

		lCondizioni += " and PROG_NOTI = " + aProgNoti;

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
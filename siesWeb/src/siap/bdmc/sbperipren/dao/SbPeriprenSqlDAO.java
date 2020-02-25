package siap.bdmc.sbperipren.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: SbPeriprenSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SbPeripren
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
public class SbPeriprenSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public SbPeriprenSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountSbPeripren(SbPeriprenModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM SB_PERIPREN@SIES_BDMC_LINK ";

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
	public void ricercaSbPeriprenPaged(SbPeriprenModel aModel, int aPage) throws DAOException {
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
	public void ricercaSbPeripren(SbPeriprenModel aModel) throws DAOException {
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
	public void ricercaSbPeriprenByKey(BigDecimal aProgPeriPres) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aProgPeriPres);

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

		lStatement += " SELECT " + "DATA_INIZ_PERI, " + "DATA_FINE_PERI, " + "PROG_PERI_PREN, " + "ID_PREN, "
				+ "CODI_UFFI_SIES, " + "ANNO_FASC_SIEP, " + "NUME_FASC_SIEP, " + "CODI_SEDE_INST, "
				+ "ANNO_FASC_BDMC, " + "NUME_FASC_BDMC, " + "CODI_STAT_PREN_PERI, " +
				// "COD_TIPO_PERI, "+
				"DATA_PREN_PERI, " + "DESC_PERI, " + "DATA_MODI_PREN_PERI ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM SB_PERIPREN@SIES_BDMC_LINK";

		/*
		 * lStatement +=
		 * " (     nvl(SB_PERIPREN.COD_UFFI_SIES,'-') = CODUFFISIES.RV_LOW_VALUE AND CODUFFISIES.RV_DOMAIN = 'UFFI_SIES' ) "
		 * lStatement +=
		 * " (     nvl(SB_PERIPREN.CODI_SEDE_INST,'-') = CODISEDEINST.RV_LOW_VALUE AND CODISEDEINST.RV_DOMAIN = '_SEDE_INST' ) "
		 * lStatement +=
		 * " (     nvl(SB_PERIPREN.COD_STAT_PREN_PERI,'-') = CODSTATPRENPERI.RV_LOW_VALUE AND CODSTATPRENPERI.RV_DOMAIN = 'STAT_PREN_PERI' ) "
		 * lStatement +=
		 * " (     nvl(SB_PERIPREN.COD_TIPO_PERI,'-') = CODTIPOPERI.RV_LOW_VALUE AND CODTIPOPERI.RV_DOMAIN = 'TIPO_PERI' ) "
		 */
		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		SbPeriprenModel aModel = new SbPeriprenModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setDataInizPeri(getDate("DATA_INIZ_PERI"));
		aModel.setDataFinePeri(getDate("DATA_FINE_PERI"));
		aModel.setProgPeriPres(getBigDecimal("PROG_PERI_PREN"));
		aModel.setIdPren(getBigDecimal("ID_PREN"));
		aModel.setCodiUffiSies(getString("CODI_UFFI_SIES"));
		// aModel.setDescrUffiSies(getString("") );
		aModel.setAnnoFascSiep(getBigDecimal("ANNO_FASC_SIEP"));
		aModel.setNumeFascSiep(getBigDecimal("NUME_FASC_SIEP"));
		aModel.setCodiSedeInst(getString("CODI_SEDE_INST"));
		// aModel.setDescriSedeInst(getString("") );
		aModel.setAnnoFascBdmc(getBigDecimal("ANNO_FASC_BDMC"));
		aModel.setNumeFascBdmc(getBigDecimal("NUME_FASC_BDMC"));
		aModel.setCodStatPrenPeri(getString("CODI_STAT_PREN_PERI"));
		// aModel.setDescrStatPrenPeri(getString("") );
		// aModel.setCodTipoPeri ( getString ("COD_TIPO_PERI" ) );
		// aModel.setDescrTipoPeri(getString("") );
		aModel.setDataPrenPeri(getDate("DATA_PREN_PERI"));
		aModel.setDescPeri(getString("DESC_PERI"));
		aModel.setDataModiPrenPeri(getDate("DATA_MODI_PREN_PERI"));
		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(SbPeriprenModel aModel) {
		String lCondizioni = new String();

		if (aModel.getDataInizPeri() != null) {
			lCondizioni += " and to_char(DATA_INIZ_PERI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizPeri(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFinePeri() != null) {
			lCondizioni += " and to_char(DATA_FINE_PERI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFinePeri(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getProgPeriPres() != null) {
			lCondizioni += " and PROG_PERI_PREN = " + aModel.getProgPeriPres() + "";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getCodiUffiSies() != null && aModel.getCodiUffiSies().length() > 0) {
			lCondizioni += " and CODI_UFFI_SIES = '" + aModel.getCodiUffiSies() + "' ";
		}
		if (aModel.getAnnoFascSiep() != null) {
			lCondizioni += " and ANNO_FASC_SIEP = " + aModel.getAnnoFascSiep() + "";
		}
		if (aModel.getNumeFascSiep() != null) {
			lCondizioni += " and NUME_FASC_SIEP = " + aModel.getNumeFascSiep() + "";
		}
		if (aModel.getCodiSedeInst() != null && aModel.getCodiSedeInst().length() > 0) {
			lCondizioni += " and CODI_SEDE_INST = '" + aModel.getCodiSedeInst() + "' ";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getCodStatPrenPeri() != null && aModel.getCodStatPrenPeri().length() > 0) {
			lCondizioni += " and CODI_STAT_PREN_PERI = '" + aModel.getCodStatPrenPeri() + "' ";
		}
		/*
		 * if (aModel.getCodTipoPeri() != null && aModel.getCodTipoPeri().length() > 0) { lCondizioni +=
		 * " and COD_TIPO_PERI = '" + aModel.getCodTipoPeri() + "' "; }
		 */
		if (aModel.getDataPrenPeri() != null) {
			lCondizioni += " and to_char(DATA_PREN_PERI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataPrenPeri(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDescPeri() != null && aModel.getDescPeri().length() > 0) {
			lCondizioni += " and DESC_PERI = '" + aModel.getDescPeri() + "' ";
		}
		if (aModel.getDataModiPrenPeri() != null) {
			lCondizioni += " and to_char(DATA_MODI_PREN_PERI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataModiPrenPeri(), "dd/MM/yyyy") + "' ";
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
	public String setCondizioniByKey(BigDecimal aProgPeriPres) {

		String lCondizioni = new String();

		lCondizioni += " and PROG_PERI_PREN = " + aProgPeriPres;

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
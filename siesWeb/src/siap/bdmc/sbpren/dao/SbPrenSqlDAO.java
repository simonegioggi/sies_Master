package siap.bdmc.sbpren.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.sbpren.model.SbPrenModel;
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
 * Title: SbPrenSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SbPren
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
public class SbPrenSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public SbPrenSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountSbPren(SbPrenModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM SB_PREN@SIES_BDMC_LINK ";

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
	public void ricercaSbPrenPaged(SbPrenModel aModel, int aPage) throws DAOException {
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
	public void ricercaSbPren(SbPrenModel aModel) throws DAOException {
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
	public void ricercaSbPrenByKey(BigDecimal aIdPren) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();
		// Aggiunge le where condition per chiave
		lSql += setCondizioniByKey(aIdPren);
		// Aggiunge le where condition per chiave
		// lSql += " WHERE " + setCondizioniByKey( aIdPren);

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

		lStatement += " SELECT " + "DATA_PREN, " + "ID_PREN, " + "UTEN_SIES, " + "NOTE, " + "DATA_ANNU, "
				+ "MOTI_ANNU, " + "CODI_STAT_PREN, "
				+ "FLAG_SELE_CAPO_IMPU, "
				+ "FLAG_SELE_PROC_PENA, "
				+
				// "FLAG_SELE_SENT, "+
				"FLAG_PREN_PRES, " + "CODI_UFFI_SIES, " + "FLAG_SELE_PERI_COMP, " + "NUME_FASC_BDMC, "
				+ "ANNO_FASC_BDMC, " + "CODI_SEDE_INST, " + "COGN_SOGG, " + "NOME_SOGG, " + "FLAG_SESS, "
				+ "CODI_STAT, " + "LUOG_NASC, " + "CODI_IDEN_AFIS, " + "DATA_NASC, "
				+ "FLAG_SELE_CIRC_SOGG, " + "SEDEINST.DESCR_TIPO_UFFICIO DESC_UFFI_INST ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM SB_PREN@SIES_BDMC_LINK SB_PREN, " + "	UFFICIO_DESCR  SEDEINST where";
		/*
		 * lStatement +=
		 * " (     nvl(SB_PREN.CODI_UFFI_SIES,'-') = CODIUFFISIES.RV_LOW_VALUE AND CODIUFFISIES.RV_DOMAIN = '_UFFI_SIES' ) "
		 * ; lStatement +=
		 * " (     nvl(SB_PREN.CODI_SEDE_INST,'-') = CODISEDEINST.RV_LOW_VALUE AND CODISEDEINST.RV_DOMAIN = '_SEDE_INST' ) "
		 * ; lStatement +=
		 * " (     nvl(SB_PREN.CODI_STAT,'-') = CODISTAT.RV_LOW_VALUE AND CODISTAT.RV_DOMAIN = '_STAT' ) " ;
		 * lStatement +=
		 * " (     nvl(SB_PREN.CODI_IDEN_AFIS,'-') = CODIIDENAFIS.RV_LOW_VALUE AND CODIIDENAFIS.RV_DOMAIN = '_IDEN_AFIS' ) "
		 * ;
		 */
		lStatement += " (     nvl(SB_PREN.CODI_SEDE_INST,'-') = SEDEINST.COD_UFFICIO) ";
		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		SbPrenModel aModel = new SbPrenModel();
		System.out.print("GET MODEL");

		// Inserire le opportune set delle descrizioni!
		aModel.setDataPren(getDate("DATA_PREN"));
		aModel.setIdPren(getBigDecimal("ID_PREN"));
		aModel.setUtenSies(getString("UTEN_SIES"));
		aModel.setNote(getString("NOTE"));
		aModel.setDataAnnu(getDate("DATA_ANNU"));
		aModel.setMotiAnnu(getString("MOTI_ANNU"));
		aModel.setCodiStatPren(getBigDecimal("CODI_STAT_PREN"));
		aModel.setFlagSeleCapoImpu(getString("FLAG_SELE_CAPO_IMPU"));
		aModel.setFlagSeleProcPena(getString("FLAG_SELE_PROC_PENA"));
		// aModel.setFlagSeleSent ( getBigDecimal ("FLAG_SELE_SENT" ) );
		aModel.setFlagPrenPres(getString("FLAG_PREN_PRES"));
		aModel.setCodiUffiSies(getString("CODI_UFFI_SIES"));
		try {
			System.out.print("SBPRENSQLDAO");
			aModel.setDescriUffiSies(UfficioUtils.getDescTipoUffByCodUfficio(getString("CODI_UFFI_SIES")));
		} catch (F3BException e) {
			e.printStackTrace();
		}

		// aModel.setDescriUffiSies(getString("") );
		aModel.setFlagSelePeriComp(getString("FLAG_SELE_PERI_COMP"));
		aModel.setNumeFascBdmc(getBigDecimal("NUME_FASC_BDMC"));
		aModel.setAnnoFascBdmc(getBigDecimal("ANNO_FASC_BDMC"));
		aModel.setCodiSedeInst(getString("CODI_SEDE_INST"));
		aModel.setDescriSedeInst(getString("DESC_UFFI_INST"));
		aModel.setCognSogg(getString("COGN_SOGG"));
		aModel.setNomeSogg(getString("NOME_SOGG"));
		aModel.setFlagSess(getString("FLAG_SESS"));
		aModel.setCodiStat(getString("CODI_STAT"));
		// aModel.setDescriStat(getString("") );
		aModel.setLuogNasc(getString("LUOG_NASC"));
		aModel.setCodiIdenAfis(getString("CODI_IDEN_AFIS"));
		// aModel.setDescriIdenAfis(getString("") );
		aModel.setDataNasc(getDate("DATA_NASC"));
		aModel.setFlagSeleCircSogg(getString("FLAG_SELE_CIRC_SOGG"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(SbPrenModel aModel) {
		String lCondizioni = new String();

		if (aModel.getDataPren() != null) {
			lCondizioni += " and to_char(DATA_PREN,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataPren(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getUtenSies() != null && aModel.getUtenSies().length() > 0) {
			lCondizioni += " and UTEN_SIES = '" + aModel.getUtenSies() + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getDataAnnu() != null) {
			lCondizioni += " and to_char(DATA_ANNU,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAnnu(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getMotiAnnu() != null && aModel.getMotiAnnu().length() > 0) {
			lCondizioni += " and MOTI_ANNU = '" + aModel.getMotiAnnu() + "' ";
		}
		if (aModel.getCodiStatPren() != null) {
			lCondizioni += " and (CODI_STAT_PREN = 0 or CODI_STAT_PREN = 1) ";
		}
		if (aModel.getFlagSeleCapoImpu() != null && aModel.getFlagSeleCapoImpu().length() > 0) {
			lCondizioni += " and FLAG_SELE_CAPO_IMPU = " + aModel.getFlagSeleCapoImpu() + "";
		}
		if (aModel.getFlagSeleProcPena() != null && aModel.getFlagSeleProcPena().length() > 0) {
			lCondizioni += " and FLAG_SELE_PROC_PENA = " + aModel.getFlagSeleProcPena() + "";
		}
		/*
		 * if (aModel.getFlagSeleSent() != null ) { lCondizioni += " and FLAG_SELE_SENT = " +
		 * aModel.getFlagSeleSent() + ""; }
		 */
		if (aModel.getFlagPrenPres() != null && aModel.getFlagPrenPres().length() > 0) {
			lCondizioni += " and FLAG_PREN_PRES = " + aModel.getFlagPrenPres() + "";
		}
		if (aModel.getCodiUffiSies() != null && aModel.getCodiUffiSies().length() > 0) {
			lCondizioni += " and CODI_UFFI_SIES = '" + aModel.getCodiUffiSies() + "' ";
		}
		if (aModel.getFlagSelePeriComp() != null && aModel.getFlagSelePeriComp().length() > 0) {
			lCondizioni += " and FLAG_SELE_PERI_COMP = " + aModel.getFlagSelePeriComp() + "";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getCodiSedeInst() != null && aModel.getCodiSedeInst().length() > 0) {
			lCondizioni += " and CODI_SEDE_INST = '" + aModel.getCodiSedeInst() + "' ";
		}
		if (aModel.getCognSogg() != null && aModel.getCognSogg().length() > 0) {
			lCondizioni += " and COGN_SOGG = '" + aModel.getCognSogg() + "' ";
		}
		if (aModel.getNomeSogg() != null && aModel.getNomeSogg().length() > 0) {
			lCondizioni += " and NOME_SOGG = '" + aModel.getNomeSogg() + "' ";
		}
		if (aModel.getFlagSess() != null && aModel.getFlagSess().length() > 0) {
			lCondizioni += " and FLAG_SESS = '" + aModel.getFlagSess() + "' ";
		}
		if (aModel.getCodiStat() != null && aModel.getCodiStat().length() > 0) {
			lCondizioni += " and CODI_STAT = '" + aModel.getCodiStat() + "' ";
		}
		if (aModel.getLuogNasc() != null && aModel.getLuogNasc().length() > 0) {
			lCondizioni += " and LUOG_NASC = '" + aModel.getLuogNasc() + "' ";
		}
		if (aModel.getCodiIdenAfis() != null && aModel.getCodiIdenAfis().length() > 0) {
			lCondizioni += " and CODI_IDEN_AFIS = '" + aModel.getCodiIdenAfis() + "' ";
		}
		if (aModel.getDataNasc() != null) {
			lCondizioni += " and to_char(DATA_NASC,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataNasc(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagSeleCircSogg() != null && aModel.getFlagSeleCircSogg().length() > 0) {
			lCondizioni += " and FLAG_SELE_CIRC_SOGG = " + aModel.getFlagSeleCircSogg() + "";
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
	public String setCondizioniByKey(BigDecimal aIdPren) {

		String lCondizioni = new String();

		lCondizioni += " and ID_PREN = " + aIdPren;

		// Elimino il primo and
		// if (lCondizioni.length() > 0) {
		// lCondizioni = lCondizioni.substring(4);
		// }

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
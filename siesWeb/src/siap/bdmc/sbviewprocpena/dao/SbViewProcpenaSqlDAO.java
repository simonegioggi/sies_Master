package siap.bdmc.sbviewprocpena.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: SbViewProcpenaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SbViewProcpena
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
public class SbViewProcpenaSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public SbViewProcpenaSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountSbViewProcpena(SbViewProcpenaModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM SB_VIEW_PROCPENA@SIES_BDMC_LINK ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	// select DECODIFICHE.RV_MEANING, COMUNE.DESCRIZIONE from CG_REF_CODES DECODIFICHE, COMUNE, UFFICIO where
	// DECODIFICHE.RV_DOMAIN = 'TIPO_UFFICIO' AND DECODIFICHE.RV_LOW_VALUE = UFFICIO.COD_TIPO_UFFICIO AND
	// COMUNE.COD_COMUNE = UFFICIO.COD_COMUNE AND UFFICIO.COD_UFFICIO = '06400590000'
	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaSbViewProcpenaPaged(SbViewProcpenaModel aModel, int aPage) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		// lStatement += " "+getOrderBy()+" ";

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
	public void ricercaSbViewProcpena(SbViewProcpenaModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += lCondizioni;
		// lSql += " "+getOrderBy()+" ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaSbViewProcpenaByKey(BigDecimal aIdPren) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += setCondizioniByKey(aIdPren);

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

		lStatement += " SELECT " + "CODI_UFFI_PMPM, " + "ANNO_REGI_PMPM, " + "NUME_REGI_PMPM, "
				+ "CODI_UFFI_GIPP, " + "ANNO_REGI_GIPP, " + "NUME_REGI_GIPP, " + "CODI_UFFI_DIBB, "
				+ "ANNO_REGI_DIBB, " + "NUME_REGI_DIBB, " + "CODI_UFFI_COAP, " + "NUME_REGI_COAP, "
				+ "ANNO_REGI_COAP, " + "DATA_PASS_GIUD, " + "FLAG_RECL_ARRE_GIGU, " + "ANNI_PENA_GIGU, "
				+ "MESI_PENA_GIGU, " + "GIOR_PENA_GIGU, " + "DATA_SENT_1GRA, " + "ANNO_SENT_1GRA, "
				+ "NUME_SENT_1GRA, " + "DATA_SENT_2GRA, " + "ANNO_SENT_2GRA, " + "NUME_SENT_2GRA, "
				+ "DATA_SENT_GIPP_GUPP, " + "NUME_SENT_GIPP_GUPP, " + "ANNO_SENT_GIPP_GUPP, "
				+ "ANNI_PENA_DIBA, " + "MESI_PENA_DIBA, " + "GIOR_PENA_DIBA, " + "ANNI_PENA_APPE, "
				+ "MESI_PENA_APPE, " + "GIOR_PENA_APPE, " + "FLAG_RECL_ARRE_DIBA, " + "FLAG_RECL_ARRE_APPE, "
				+ "FLAG_ARTI_0089, " + "FLAG_ARTI_0090, " + "FLAG_ARTI_0091, " + "FLAG_ARTI_0092, "
				+ "FLAG_ARTI_0093, " + "FLAG_ARTI_0094, " + "FLAG_ARTI_0095, " + "FLAG_ARTI_0096, "
				+ "FLAG_ARTI_0097, " + "FLAG_ARTI_0098, " + "FLAG_ARTI_0099, " + "FLAG_ARTI_0062, "
				+ "ARTI_0062_COMM, " + "FLAG_ARTI_62BI, " + "CODI_MISU_CUST, " + "CODI_ISTI_PENA, "
				+ "DESC_LUOG, " + "ID_PREN, " + "CODI_SEDE_INST, " + "NUME_FASC_BDMC, " + "ANNO_FASC_BDMC, "
				+ "FLAG_INFO_SELE, " + "DATA_DECI_CASS, " + "ANNO_DECI_CASS, " + "NUME_DECI_CASS, "
				+ "UFFIPMPM.DESCR_TIPO_UFFICIO DESC_UFFI_PM, " + "UFFIPMPM.DESCR_COMUNE DESC_COMU_PM, "
				+ "UFFIGIPP.DESCR_TIPO_UFFICIO DESC_UFFI_GIPP, " + "UFFIGIPP.DESCR_COMUNE DESC_COMU_GIPP, "
				+ "UFFIDIBB.DESCR_TIPO_UFFICIO DESC_UFFI_DIBB, " + "UFFIDIBB.DESCR_COMUNE DESC_COMU_DIBB, "
				+ "UFFICOAP.DESCR_TIPO_UFFICIO DESC_UFFI_COAP, " + "UFFIDIBB.DESCR_COMUNE DESC_COMU_COAP, "
				+ "SEDEINST.DESCR_TIPO_UFFICIO DESC_UFFI_INST, " + "SEDEINST.DESCR_COMUNE DESC_COMU_INST ";

		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM SB_VIEW_PROCPENA@SIES_BDMC_LINK PENA, " +
		/*
		 * "	UFFICIO  UFFIPMPM, "+ "	UFFICIO  UFFIGIPP, "+ "	UFFICIO  UFFIDIBB, "+ "	UFFICIO  UFFICOAP, "+
		 * "	UFFICIO  SEDEINST, "+ "	COMUNE  COMUPMPM, "+ "	COMUNE  COMUGIPP, "+ "	COMUNE  COMUDIBB, "+
		 * "	COMUNE  COMUCOAP, "+ "	COMUNE  COMUINST, "+ "	CG_REF_CODES  CGUFFIPMPM, "+
		 * "	CG_REF_CODES  CGUFFIGIPP, "+ "	CG_REF_CODES  CGUFFIDIBB, "+ "	CG_REF_CODES  CGUFFICOAP, "+
		 * "	CG_REF_CODES  CGSEDEINST where";
		 */
		"	UFFICIO_DESCR  UFFIGIPP, " + "	UFFICIO_DESCR  UFFIDIBB, " + "	UFFICIO_DESCR  UFFICOAP, "
				+ "	UFFICIO_DESCR  SEDEINST, " + "	UFFICIO_DESCR  UFFIPMPM where";

		/*
		 * lStatement +=
		 * " (     nvl(PENA.CODI_UFFI_PMPM,'-') = UFFIPMPM.COD_UFFICIO AND CGUFFIPMPM.RV_DOMAIN = 'TIPO_UFFICIO' AND COMUPMPM.COD_COMUNE = UFFIPMPM.COD_COMUNE AND CGUFFIPMPM.RV_LOW_VALUE = UFFIPMPM.COD_TIPO_UFFICIO) AND "
		 * ; lStatement +=
		 * " (     nvl(PENA.CODI_UFFI_GIPP,'-') = UFFIGIPP.COD_UFFICIO AND CGUFFIGIPP.RV_DOMAIN = 'TIPO_UFFICIO' AND COMUGIPP.COD_COMUNE = UFFIGIPP.COD_COMUNE AND CGUFFIGIPP.RV_LOW_VALUE = UFFIGIPP.COD_TIPO_UFFICIO) AND "
		 * ; lStatement +=
		 * " (     nvl(PENA.CODI_UFFI_DIBB,'-') = UFFIDIBB.COD_UFFICIO AND CGUFFIDIBB.RV_DOMAIN = 'TIPO_UFFICIO' AND COMUDIBB.COD_COMUNE = UFFIDIBB.COD_COMUNE AND CGUFFIDIBB.RV_LOW_VALUE = UFFIDIBB.COD_TIPO_UFFICIO) AND "
		 * ; lStatement +=
		 * " (     nvl(PENA.CODI_UFFI_COAP,'-') = UFFICOAP.COD_UFFICIO AND CGUFFICOAP.RV_DOMAIN = 'TIPO_UFFICIO' AND COMUCOAP.COD_COMUNE = UFFICOAP.COD_COMUNE AND CGUFFICOAP.RV_LOW_VALUE = UFFICOAP.COD_TIPO_UFFICIO) AND "
		 * ; // lStatement +=
		 * " (     nvl(SB_VIEW_PROCPENA@SIES_BDMC_LINK.CODI_MISU_CUST,'-') = CODIMISUCUST.RV_LOW_VALUE AND CODIMISUCUST.RV_DOMAIN = '_MISU_CUST' ) "
		 * ; //lStatement +=
		 * " (     nvl(SB_VIEW_PROCPENA@SIES_BDMC_LINK.CODI_ISTI_PENA,'-') = CODIISTIPENA.RV_LOW_VALUE AND CODIISTIPENA.RV_DOMAIN = '_ISTI_PENA' ) "
		 * ; lStatement +=
		 * " (     nvl(PENA.CODI_SEDE_INST,'-') = SEDEINST.COD_UFFICIO AND CGSEDEINST.RV_DOMAIN = 'TIPO_UFFICIO' AND COMUINST.COD_COMUNE = SEDEINST.COD_COMUNE AND CGSEDEINST.RV_LOW_VALUE = SEDEINST.COD_TIPO_UFFICIO) "
		 * ;
		 */
		lStatement += " (     nvl(PENA.CODI_UFFI_PMPM,'-') = UFFIPMPM.COD_UFFICIO ) AND ";
		lStatement += " (     nvl(PENA.CODI_UFFI_GIPP,'-') = UFFIGIPP.COD_UFFICIO ) AND ";
		lStatement += " (     nvl(PENA.CODI_UFFI_DIBB,'-') = UFFIDIBB.COD_UFFICIO ) AND ";
		lStatement += " (     nvl(PENA.CODI_UFFI_COAP,'-') = UFFICOAP.COD_UFFICIO ) AND ";
		// lStatement +=
		// " (     nvl(SB_VIEW_PROCPENA@SIES_BDMC_LINK.CODI_MISU_CUST,'-') = CODIMISUCUST.RV_LOW_VALUE AND CODIMISUCUST.RV_DOMAIN = '_MISU_CUST' ) "
		// ;
		// lStatement +=
		// " (     nvl(SB_VIEW_PROCPENA@SIES_BDMC_LINK.CODI_ISTI_PENA,'-') = CODIISTIPENA.RV_LOW_VALUE AND CODIISTIPENA.RV_DOMAIN = '_ISTI_PENA' ) "
		// ;
		lStatement += " (     nvl(PENA.CODI_SEDE_INST,'-') = SEDEINST.COD_UFFICIO) ";

		// valore di ritorno
		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		SbViewProcpenaModel aModel = new SbViewProcpenaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setCodiUffiPmpm(getString("CODI_UFFI_PMPM"));
		aModel.setDescriUffiPmpm(getString("DESC_UFFI_PM"));
		aModel.setDescriComuUffiPmpm(getString("DESC_COMU_PM"));
		aModel.setAnnoRegiPmpm(getBigDecimal("ANNO_REGI_PMPM"));
		aModel.setNumeRegiPmpm(getBigDecimal("NUME_REGI_PMPM"));
		aModel.setCodiUffiGipp(getString("CODI_UFFI_GIPP"));
		aModel.setDescriUffiGipp(getString("DESC_UFFI_GIPP"));
		aModel.setDescriComuUffiGipp(getString("DESC_COMU_GIPP"));
		aModel.setAnnoRegiGipp(getBigDecimal("ANNO_REGI_GIPP"));
		aModel.setNumeRegiGipp(getBigDecimal("NUME_REGI_GIPP"));
		aModel.setCodiUffiDibb(getString("CODI_UFFI_DIBB"));
		aModel.setDescriUffiDibb(getString("DESC_UFFI_DIBB"));
		aModel.setDescriComuUffiDibb(getString("DESC_COMU_DIBB"));
		aModel.setAnnoRegiDibb(getBigDecimal("ANNO_REGI_DIBB"));
		aModel.setNumeRegiDibb(getBigDecimal("NUME_REGI_DIBB"));
		aModel.setCodiUffiCoap(getString("CODI_UFFI_COAP"));
		aModel.setDescriUffiCoap(getString("DESC_UFFI_COAP"));
		aModel.setDescriComuUffiCoap(getString("DESC_COMU_COAP"));
		aModel.setNumeRegiCoap(getBigDecimal("NUME_REGI_COAP"));
		aModel.setAnnoRegiCoap(getBigDecimal("ANNO_REGI_COAP"));
		aModel.setDataPassGiud(getDate("DATA_PASS_GIUD"));
		aModel.setFlagReclArreGigu(getString("FLAG_RECL_ARRE_GIGU"));
		aModel.setAnniPenaGigu(getBigDecimal("ANNI_PENA_GIGU"));
		aModel.setMesiPenaGigu(getBigDecimal("MESI_PENA_GIGU"));
		aModel.setGiorPenaGigu(getBigDecimal("GIOR_PENA_GIGU"));
		aModel.setDataSent1gra(getDate("DATA_SENT_1GRA"));
		aModel.setAnnoSent1gra(getBigDecimal("ANNO_SENT_1GRA"));
		aModel.setNumeSent1gra(getBigDecimal("NUME_SENT_1GRA"));
		aModel.setDataSent2gra(getDate("DATA_SENT_2GRA"));
		aModel.setAnnoSent2gra(getBigDecimal("ANNO_SENT_2GRA"));
		aModel.setNumeSent2gra(getBigDecimal("NUME_SENT_2GRA"));
		aModel.setDataSentGippGupp(getDate("DATA_SENT_GIPP_GUPP"));
		aModel.setNumeSentGippGupp(getBigDecimal("NUME_SENT_GIPP_GUPP"));
		aModel.setAnnoSentGippGupp(getBigDecimal("ANNO_SENT_GIPP_GUPP"));
		aModel.setAnniPenaDiba(getBigDecimal("ANNI_PENA_DIBA"));
		aModel.setMesiPenaDiba(getBigDecimal("MESI_PENA_DIBA"));
		aModel.setGiorPenaDiba(getBigDecimal("GIOR_PENA_DIBA"));
		aModel.setAnniPenaAppe(getBigDecimal("ANNI_PENA_APPE"));
		aModel.setMesiPenaAppe(getBigDecimal("MESI_PENA_APPE"));
		aModel.setGiorPenaAppe(getBigDecimal("GIOR_PENA_APPE"));
		aModel.setFlagReclArreDiba(getString("FLAG_RECL_ARRE_DIBA"));
		aModel.setFlagReclArreAppe(getString("FLAG_RECL_ARRE_APPE"));
		aModel.setFlagArti0089(getString("FLAG_ARTI_0089"));
		aModel.setFlagArti0090(getString("FLAG_ARTI_0090"));
		aModel.setFlagArti0091(getString("FLAG_ARTI_0091"));
		aModel.setFlagArti0092(getString("FLAG_ARTI_0092"));
		aModel.setFlagArti0093(getString("FLAG_ARTI_0093"));
		aModel.setFlagArti0094(getString("FLAG_ARTI_0094"));
		aModel.setFlagArti0095(getString("FLAG_ARTI_0095"));
		aModel.setFlagArti0096(getString("FLAG_ARTI_0096"));
		aModel.setFlagArti0097(getString("FLAG_ARTI_0097"));
		aModel.setFlagArti0098(getString("FLAG_ARTI_0098"));
		aModel.setFlagArti0099(getString("FLAG_ARTI_0099"));
		aModel.setFlagArti62(getString("FLAG_ARTI_0062"));
		aModel.setArti0062Comm(getString("ARTI_0062_COMM"));
		aModel.setFlagArt62bi(getString("FLAG_ARTI_62BI"));
		aModel.setCodiMisuCust(getString("CODI_MISU_CUST"));
		// aModel.setDescriMisuCust(getString("") );
		aModel.setCodiIstiPena(getString("CODI_ISTI_PENA"));
		// aModel.setDescriIstiPena(getString("") );
		aModel.setDescLuog(getString("DESC_LUOG"));
		aModel.setIdPren(getBigDecimal("ID_PREN"));
		aModel.setCodiSedeInst(getString("CODI_SEDE_INST"));
		aModel.setDescriSedeInst(getString("DESC_UFFI_INST"));
		aModel.setDescriComuSedeInst(getString("DESC_COMU_INST"));
		aModel.setNumeFascBdmc(getBigDecimal("NUME_FASC_BDMC"));
		aModel.setAnnoFascBdmc(getBigDecimal("ANNO_FASC_BDMC"));
		aModel.setFlagInfoSele(getString("FLAG_INFO_SELE"));
		aModel.setDataDeciCass(getDate("DATA_DECI_CASS"));
		aModel.setAnnoDeciCass(getBigDecimal("ANNO_DECI_CASS"));
		aModel.setNumeDeciCass(getBigDecimal("NUME_DECI_CASS"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(SbViewProcpenaModel aModel) {
		String lCondizioni = new String();

		if (aModel.getCodiUffiPmpm() != null && aModel.getCodiUffiPmpm().length() > 0) {
			lCondizioni += " and CODI_UFFI_PMPM = '" + aModel.getCodiUffiPmpm() + "' ";
		}
		if (aModel.getAnnoRegiPmpm() != null) {
			lCondizioni += " and ANNO_REGI_PMPM = " + aModel.getAnnoRegiPmpm() + "";
		}
		if (aModel.getNumeRegiPmpm() != null) {
			lCondizioni += " and NUME_REGI_PMPM = " + aModel.getNumeRegiPmpm() + "";
		}
		if (aModel.getCodiUffiGipp() != null && aModel.getCodiUffiGipp().length() > 0) {
			lCondizioni += " and CODI_UFFI_GIPP = '" + aModel.getCodiUffiGipp() + "' ";
		}
		if (aModel.getAnnoRegiGipp() != null) {
			lCondizioni += " and ANNO_REGI_GIPP = " + aModel.getAnnoRegiGipp() + "";
		}
		if (aModel.getNumeRegiGipp() != null) {
			lCondizioni += " and NUME_REGI_GIPP = " + aModel.getNumeRegiGipp() + "";
		}
		if (aModel.getCodiUffiDibb() != null && aModel.getCodiUffiDibb().length() > 0) {
			lCondizioni += " and CODI_UFFI_DIBB = '" + aModel.getCodiUffiDibb() + "' ";
		}
		if (aModel.getAnnoRegiDibb() != null) {
			lCondizioni += " and ANNO_REGI_DIBB = " + aModel.getAnnoRegiDibb() + "";
		}
		if (aModel.getNumeRegiDibb() != null) {
			lCondizioni += " and NUME_REGI_DIBB = " + aModel.getNumeRegiDibb() + "";
		}
		if (aModel.getCodiUffiCoap() != null && aModel.getCodiUffiCoap().length() > 0) {
			lCondizioni += " and CODI_UFFI_COAP = '" + aModel.getCodiUffiCoap() + "' ";
		}
		if (aModel.getNumeRegiCoap() != null) {
			lCondizioni += " and NUME_REGI_COAP = " + aModel.getNumeRegiCoap() + "";
		}
		if (aModel.getAnnoRegiCoap() != null) {
			lCondizioni += " and ANNO_REGI_COAP = " + aModel.getAnnoRegiCoap() + "";
		}
		if (aModel.getDataPassGiud() != null) {
			lCondizioni += " and to_char(DATA_PASS_GIUD,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataPassGiud(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagReclArreGigu() != null && aModel.getFlagReclArreGigu().length() > 0) {
			lCondizioni += " and FLAG_RECL_ARRE_GIGU = '" + aModel.getFlagReclArreGigu() + "' ";
		}
		if (aModel.getAnniPenaGigu() != null) {
			lCondizioni += " and ANNI_PENA_GIGU = " + aModel.getAnniPenaGigu() + "";
		}
		if (aModel.getMesiPenaGigu() != null) {
			lCondizioni += " and MESI_PENA_GIGU = " + aModel.getMesiPenaGigu() + "";
		}
		if (aModel.getGiorPenaGigu() != null) {
			lCondizioni += " and GIOR_PENA_GIGU = " + aModel.getGiorPenaGigu() + "";
		}
		if (aModel.getDataSent1gra() != null) {
			lCondizioni += " and to_char(DATA_SENT_1GRA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataSent1gra(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoSent1gra() != null) {
			lCondizioni += " and ANNO_SENT_1GRA = " + aModel.getAnnoSent1gra() + "";
		}
		if (aModel.getNumeSent1gra() != null) {
			lCondizioni += " and NUME_SENT_1GRA = " + aModel.getNumeSent1gra() + "";
		}
		if (aModel.getDataSent2gra() != null) {
			lCondizioni += " and to_char(DATA_SENT_2GRA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataSent2gra(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoSent2gra() != null) {
			lCondizioni += " and ANNO_SENT_2GRA = " + aModel.getAnnoSent2gra() + "";
		}
		if (aModel.getNumeSent2gra() != null) {
			lCondizioni += " and NUME_SENT_2GRA = " + aModel.getNumeSent2gra() + "";
		}
		if (aModel.getDataSentGippGupp() != null) {
			lCondizioni += " and to_char(DATA_SENT_GIPP_GUPP,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataSentGippGupp(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getNumeSentGippGupp() != null) {
			lCondizioni += " and NUME_SENT_GIPP_GUPP = " + aModel.getNumeSentGippGupp() + "";
		}
		if (aModel.getAnnoSentGippGupp() != null) {
			lCondizioni += " and ANNO_SENT_GIPP_GUPP = " + aModel.getAnnoSentGippGupp() + "";
		}
		if (aModel.getAnniPenaDiba() != null) {
			lCondizioni += " and ANNI_PENA_DIBA = " + aModel.getAnniPenaDiba() + "";
		}
		if (aModel.getMesiPenaDiba() != null) {
			lCondizioni += " and MESI_PENA_DIBA = " + aModel.getMesiPenaDiba() + "";
		}
		if (aModel.getGiorPenaDiba() != null) {
			lCondizioni += " and GIOR_PENA_DIBA = " + aModel.getGiorPenaDiba() + "";
		}
		if (aModel.getAnniPenaAppe() != null) {
			lCondizioni += " and ANNI_PENA_APPE = " + aModel.getAnniPenaAppe() + "";
		}
		if (aModel.getMesiPenaAppe() != null) {
			lCondizioni += " and MESI_PENA_APPE = " + aModel.getMesiPenaAppe() + "";
		}
		if (aModel.getGiorPenaAppe() != null) {
			lCondizioni += " and GIOR_PENA_APPE = " + aModel.getGiorPenaAppe() + "";
		}
		if (aModel.getFlagReclArreDiba() != null && aModel.getFlagReclArreDiba().length() > 0) {
			lCondizioni += " and FLAG_RECL_ARRE_DIBA = '" + aModel.getFlagReclArreDiba() + "' ";
		}
		if (aModel.getFlagReclArreAppe() != null && aModel.getFlagReclArreAppe().length() > 0) {
			lCondizioni += " and FLAG_RECL_ARRE_APPE = '" + aModel.getFlagReclArreAppe() + "' ";
		}
		if (aModel.getFlagArti0089() != null && aModel.getFlagArti0089().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0089 = '" + aModel.getFlagArti0089() + "' ";
		}
		if (aModel.getFlagArti0090() != null && aModel.getFlagArti0090().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0090 = '" + aModel.getFlagArti0090() + "' ";
		}
		if (aModel.getFlagArti0091() != null && aModel.getFlagArti0091().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0091 = '" + aModel.getFlagArti0091() + "' ";
		}
		if (aModel.getFlagArti0092() != null && aModel.getFlagArti0092().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0092 = '" + aModel.getFlagArti0092() + "' ";
		}
		if (aModel.getFlagArti0093() != null && aModel.getFlagArti0093().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0093 = '" + aModel.getFlagArti0093() + "' ";
		}
		if (aModel.getFlagArti0094() != null && aModel.getFlagArti0094().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0094 = '" + aModel.getFlagArti0094() + "' ";
		}
		if (aModel.getFlagArti0095() != null && aModel.getFlagArti0095().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0095 = '" + aModel.getFlagArti0095() + "' ";
		}
		if (aModel.getFlagArti0096() != null && aModel.getFlagArti0096().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0096 = '" + aModel.getFlagArti0096() + "' ";
		}
		if (aModel.getFlagArti0097() != null && aModel.getFlagArti0097().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0097 = '" + aModel.getFlagArti0097() + "' ";
		}
		if (aModel.getFlagArti0098() != null && aModel.getFlagArti0098().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0098 = '" + aModel.getFlagArti0098() + "' ";
		}
		if (aModel.getFlagArti0099() != null && aModel.getFlagArti0099().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0099 = '" + aModel.getFlagArti0099() + "' ";
		}
		if (aModel.getFlagArti62() != null && aModel.getFlagArti62().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0062 = '" + aModel.getFlagArti62() + "' ";
		}
		if (aModel.getArti0062Comm() != null && aModel.getArti0062Comm().length() > 0) {
			lCondizioni += " and ARTI_0062_COMM = '" + aModel.getArti0062Comm() + "' ";
		}
		if (aModel.getFlagArt62bi() != null && aModel.getFlagArt62bi().length() > 0) {
			lCondizioni += " and FLAG_ARTI_62BI = '" + aModel.getFlagArt62bi() + "' ";
		}
		if (aModel.getCodiMisuCust() != null && aModel.getCodiMisuCust().length() > 0) {
			lCondizioni += " and CODI_MISU_CUST = '" + aModel.getCodiMisuCust() + "' ";
		}
		if (aModel.getCodiIstiPena() != null && aModel.getCodiIstiPena().length() > 0) {
			lCondizioni += " and CODI_ISTI_PENA = '" + aModel.getCodiIstiPena() + "' ";
		}
		if (aModel.getDescLuog() != null && aModel.getDescLuog().length() > 0) {
			lCondizioni += " and DESC_LUOG = '" + aModel.getDescLuog() + "' ";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getCodiSedeInst() != null && aModel.getCodiSedeInst().length() > 0) {
			lCondizioni += " and CODI_SEDE_INST = '" + aModel.getCodiSedeInst() + "' ";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getFlagInfoSele() != null && aModel.getFlagInfoSele().length() > 0) {
			lCondizioni += " and FLAG_INFO_SELE = '" + aModel.getFlagInfoSele() + "' ";
		}
		// Elimino il primo and
		/*
		 * if (lCondizioni.length() > 0) { lCondizioni = lCondizioni.substring(4); }
		 */

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
		/*
		 * if (lCondizioni.length() > 0) { lCondizioni = lCondizioni.substring(4); }
		 */

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
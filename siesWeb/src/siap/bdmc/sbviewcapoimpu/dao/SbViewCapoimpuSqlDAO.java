package siap.bdmc.sbviewcapoimpu.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: SbViewCapoimpuSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SbViewCapoimpu
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
public class SbViewCapoimpuSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public SbViewCapoimpuSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountSbViewCapoimpu(SbViewCapoimpuModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM SB_VIEW_CAPOIMPU ";

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
	public void ricercaSbViewCapoimpuPaged(SbViewCapoimpuModel aModel, int aPage) throws DAOException {
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
	public void ricercaSbViewCapoimpu(SbViewCapoimpuModel aModel) throws DAOException {
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
	public void ricercaSbViewCapoimpuByKey(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu)
			throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdPren, aNumeProgCapoImpu);

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

		lStatement += " SELECT " + "FLAG_ARTI_0056, " + "FLAG_ARTI_0061, " + "ARTI_0061_COMM, "
				+ "FLAG_ARTI_0081, " + "ARTI_0081_COMM, " + "FLAG_ARTI_0110, " + "FLAG_ARTI_0112, "
				+ "ARTI_0112_COMM, " + "FLAG_ARTI_0113, " + "FLAG_ARTI_0114, " + "FLAG_ARTI_0116, "
				+ "FLAG_ARTI_0117, " + "LUOG_REAT, " + "FLAG_PERI_TEMP, " + "DATA_REAT_0101, "
				+ "DATA_REAT_0202, " + "DESC_PERI_TEMP, " + "NUME_PROG_CAPO_IMPU, " + "ID_PREN, "
				+ "ANNO_FASC_BDMC, " + "NUME_FASC_BDMC, " + "CODI_SEDE_INST ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM SB_VIEW_CAPOIMPU";

		lStatement += " (     nvl(SB_VIEW_CAPOIMPU.CODI_SEDE_INST,'-') = CODISEDEINST.RV_LOW_VALUE AND CODISEDEINST.RV_DOMAIN = '_SEDE_INST' ) ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		SbViewCapoimpuModel aModel = new SbViewCapoimpuModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setFlagArti0056(getString("FLAG_ARTI_0056"));
		aModel.setFlagArti0061(getString("FLAG_ARTI_0061"));
		aModel.setArti0061Comm(getString("ARTI_0061_COMM"));
		aModel.setFlagArti0081(getString("FLAG_ARTI_0081"));
		aModel.setArti0081Comm(getString("ARTI_0081_COMM"));
		aModel.setFlagArt0110(getString("FLAG_ARTI_0110"));
		aModel.setFlagArti0112(getString("FLAG_ARTI_0112"));
		aModel.setArti0112Commi(getString("ARTI_0112_COMM"));
		aModel.setFlagArti0113(getString("FLAG_ARTI_0113"));
		aModel.setFlagArti0114(getString("FLAG_ARTI_0114"));
		aModel.setFlagArti0116(getString("FLAG_ARTI_0116"));
		aModel.setFlagArti0117(getString("FLAG_ARTI_0117"));
		aModel.setLuogReat(getString("LUOG_REAT"));
		aModel.setFlagPeriTemp(getString("FLAG_PERI_TEMP"));
		aModel.setDataReat0101(getDate("DATA_REAT_0101"));
		aModel.setDataReat0202(getDate("DATA_REAT_0202"));
		aModel.setDescPeriTemp(getString("DESC_PERI_TEMP"));
		aModel.setNumeProgCapoImpu(getBigDecimal("NUME_PROG_CAPO_IMPU"));
		aModel.setIdPren(getBigDecimal("ID_PREN"));
		aModel.setAnnoFascBdmc(getBigDecimal("ANNO_FASC_BDMC"));
		aModel.setNumeFascBdmc(getBigDecimal("NUME_FASC_BDMC"));
		aModel.setCodiSedeInst(getString("CODI_SEDE_INST"));
		// aModel.setDescriSedeInst(getString("") );

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(SbViewCapoimpuModel aModel) {
		String lCondizioni = new String();

		if (aModel.getFlagArti0056() != null && aModel.getFlagArti0056().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0056 = '" + aModel.getFlagArti0056() + "' ";
		}
		if (aModel.getFlagArti0061() != null && aModel.getFlagArti0061().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0061 = '" + aModel.getFlagArti0061() + "' ";
		}
		if (aModel.getArti0061Comm() != null && aModel.getArti0061Comm().length() > 0) {
			lCondizioni += " and ARTI_0061_COMM = '" + aModel.getArti0061Comm() + "' ";
		}
		if (aModel.getFlagArti0081() != null && aModel.getFlagArti0081().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0081 = '" + aModel.getFlagArti0081() + "' ";
		}
		if (aModel.getArti0081Comm() != null && aModel.getArti0081Comm().length() > 0) {
			lCondizioni += " and ARTI_0081_COMM = '" + aModel.getArti0081Comm() + "' ";
		}
		if (aModel.getFlagArt0110() != null && aModel.getFlagArt0110().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0110 = '" + aModel.getFlagArt0110() + "' ";
		}
		if (aModel.getFlagArti0112() != null && aModel.getFlagArti0112().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0112 = '" + aModel.getFlagArti0112() + "' ";
		}
		if (aModel.getArti0112Commi() != null && aModel.getArti0112Commi().length() > 0) {
			lCondizioni += " and ARTI_0112_COMM = '" + aModel.getArti0112Commi() + "' ";
		}
		if (aModel.getFlagArti0113() != null && aModel.getFlagArti0113().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0113 = '" + aModel.getFlagArti0113() + "' ";
		}
		if (aModel.getFlagArti0114() != null && aModel.getFlagArti0114().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0114 = '" + aModel.getFlagArti0114() + "' ";
		}
		if (aModel.getFlagArti0116() != null && aModel.getFlagArti0116().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0116 = '" + aModel.getFlagArti0116() + "' ";
		}
		if (aModel.getFlagArti0117() != null && aModel.getFlagArti0117().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0117 = '" + aModel.getFlagArti0117() + "' ";
		}
		if (aModel.getLuogReat() != null && aModel.getLuogReat().length() > 0) {
			lCondizioni += " and LUOG_REAT = '" + aModel.getLuogReat() + "' ";
		}
		if (aModel.getFlagPeriTemp() != null && aModel.getFlagPeriTemp().length() > 0) {
			lCondizioni += " and FLAG_PERI_TEMP = '" + aModel.getFlagPeriTemp() + "' ";
		}
		if (aModel.getDataReat0101() != null) {
			lCondizioni += " and to_char(DATA_REAT_0101,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataReat0101(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataReat0202() != null) {
			lCondizioni += " and to_char(DATA_REAT_0202,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataReat0202(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDescPeriTemp() != null && aModel.getDescPeriTemp().length() > 0) {
			lCondizioni += " and DESC_PERI_TEMP = '" + aModel.getDescPeriTemp() + "' ";
		}
		if (aModel.getNumeProgCapoImpu() != null) {
			lCondizioni += " and NUME_PROG_CAPO_IMPU = " + aModel.getNumeProgCapoImpu() + "";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getCodiSedeInst() != null && aModel.getCodiSedeInst().length() > 0) {
			lCondizioni += " and CODI_SEDE_INST = '" + aModel.getCodiSedeInst() + "' ";
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
	public String setCondizioniByKey(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu) {

		String lCondizioni = new String();

		lCondizioni += " and ID_PREN = " + aIdPren;
		lCondizioni += " and NUME_PROG_CAPO_IMPU = " + aNumeProgCapoImpu;

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
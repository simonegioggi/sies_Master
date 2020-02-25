package siap.bdmc.sbviewreat.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.sbviewreat.model.SbViewReatModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: SbViewReatSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SbViewReat
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
public class SbViewReatSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public SbViewReatSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountSbViewReat(SbViewReatModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM SB_VIEW_REAT ";

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
	public void ricercaSbViewReatPaged(SbViewReatModel aModel, int aPage) throws DAOException {
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
	public void ricercaSbViewReat(SbViewReatModel aModel) throws DAOException {
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
	public void ricercaSbViewReatByKey(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu,
			BigDecimal aNumeProgReat) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdPren, aNumeProgCapoImpu, aNumeProgReat);

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

		lStatement += " SELECT " + "NUME_PROG_CAPO_IMPU, " + "NUME_PROG_REAT, " + "CODI_FONT_GIUR, "
				+ "ANNO_FONT_GIUR, " + "NUME_FONT_GIUR, " + "ARTI_FONT_GIUR, " + "COMM_ARTI_FONT, "
				+ "LETT_ARTI_FONT, " + "NUME_ARTI_FONT, " + "ARTI_QUAL_FONT, " + "ID_PREN, "
				+ "ANNO_FASC_BDMC, " + "NUME_FASC_BDMC, " + "CODI_SEDE_INST ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM SB_VIEW_REAT";

		lStatement += " (     nvl(SB_VIEW_REAT.CODI_FONT_GIUR,'-') = CODIFONTGIUR.RV_LOW_VALUE AND CODIFONTGIUR.RV_DOMAIN = '_FONT_GIUR' ) ";
		lStatement += " (     nvl(SB_VIEW_REAT.CODI_SEDE_INST,'-') = CODISEDEINST.RV_LOW_VALUE AND CODISEDEINST.RV_DOMAIN = '_SEDE_INST' ) ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		SbViewReatModel aModel = new SbViewReatModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setNumeProgCapoImpu(getBigDecimal("NUME_PROG_CAPO_IMPU"));
		aModel.setNumeProgReat(getBigDecimal("NUME_PROG_REAT"));
		aModel.setCodiFontGiur(getString("CODI_FONT_GIUR"));
		// aModel.setDescriFontGiur(getString("") );
		aModel.setAnnoFontGiur(getBigDecimal("ANNO_FONT_GIUR"));
		aModel.setNumeFontGiur(getBigDecimal("NUME_FONT_GIUR"));
		aModel.setArtiFontGiur(getBigDecimal("ARTI_FONT_GIUR"));
		aModel.setCommiArtiFont(getString("COMM_ARTI_FONT"));
		aModel.setLettArtiFont(getString("LETT_ARTI_FONT"));
		aModel.setNumeArtiFont(getString("NUME_ARTI_FONT"));
		aModel.setArtiQualFont(getString("ARTI_QUAL_FONT"));
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
	public String setCondizioni(SbViewReatModel aModel) {
		String lCondizioni = new String();

		if (aModel.getNumeProgCapoImpu() != null) {
			lCondizioni += " and NUME_PROG_CAPO_IMPU = " + aModel.getNumeProgCapoImpu() + "";
		}
		if (aModel.getNumeProgReat() != null) {
			lCondizioni += " and NUME_PROG_REAT = " + aModel.getNumeProgReat() + "";
		}
		if (aModel.getCodiFontGiur() != null && aModel.getCodiFontGiur().length() > 0) {
			lCondizioni += " and CODI_FONT_GIUR = '" + aModel.getCodiFontGiur() + "' ";
		}
		if (aModel.getAnnoFontGiur() != null) {
			lCondizioni += " and ANNO_FONT_GIUR = " + aModel.getAnnoFontGiur() + "";
		}
		if (aModel.getNumeFontGiur() != null) {
			lCondizioni += " and NUME_FONT_GIUR = " + aModel.getNumeFontGiur() + "";
		}
		if (aModel.getArtiFontGiur() != null) {
			lCondizioni += " and ARTI_FONT_GIUR = " + aModel.getArtiFontGiur() + "";
		}
		if (aModel.getCommiArtiFont() != null && aModel.getCommiArtiFont().length() > 0) {
			lCondizioni += " and COMM_ARTI_FONT = '" + aModel.getCommiArtiFont() + "' ";
		}
		if (aModel.getLettArtiFont() != null && aModel.getLettArtiFont().length() > 0) {
			lCondizioni += " and LETT_ARTI_FONT = '" + aModel.getLettArtiFont() + "' ";
		}
		if (aModel.getNumeArtiFont() != null && aModel.getNumeArtiFont().length() > 0) {
			lCondizioni += " and NUME_ARTI_FONT = '" + aModel.getNumeArtiFont() + "' ";
		}
		if (aModel.getArtiQualFont() != null && aModel.getArtiQualFont().length() > 0) {
			lCondizioni += " and ARTI_QUAL_FONT = '" + aModel.getArtiQualFont() + "' ";
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
	public String setCondizioniByKey(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu,
			BigDecimal aNumeProgReat) {

		String lCondizioni = new String();

		lCondizioni += " and ID_PREN = " + aIdPren;
		lCondizioni += " and NUME_PROG_CAPO_IMPU = " + aNumeProgCapoImpu;
		lCondizioni += " and NUME_PROG_REAT = " + aNumeProgReat;

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
package siap.sius.sanzionesostitutiva.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: PeriodoAltraSanzioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella PeriodoAltraSanzione
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
public class PeriodoAltraSanzioneSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public PeriodoAltraSanzioneSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountPeriodoAltraSanzione(PeriodoAltraSanzioneModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM PERIODO_ALTRA_SANZIONE ";

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
	/*
	 * public void ricercaPeriodoAltraSanzionePaged(PeriodoAltraSanzioneModel aModel, int aPage) throws
	 * DAOException { String lStatement = new String("");
	 * 
	 * lStatement += getSqlQuery();
	 * 
	 * // Recupero la where condition in base al model String lCondizioni = this.setCondizioni(aModel);
	 * 
	 * if (!lCondizioni.trim().equals("")) lStatement+=" WHERE " + lCondizioni;
	 * 
	 * lStatement += " "+getOrderBy()+" ";
	 * 
	 * String lPaginedStatement = ""; lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" +
	 * lStatement + "  ) INNER ) WHERE rn between  " + ( (aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) +
	 * " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
	 * 
	 * setStatement(lPaginedStatement); logger.info("lPaginedStatement = "+lPaginedStatement); }
	 */

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaPeriodoAltraSanzione(PeriodoAltraSanzioneModel aModel) throws DAOException {
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
	public void ricercaPeriodoAltraSanzioneByKey(BigDecimal aIdPeriodoAltraSanzione) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " AND " + setCondizioniByKey(aIdPeriodoAltraSanzione);
		lSql += " " + getOrderBy() + " ";
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

		lStatement += " SELECT " + "ID_PERIODO_ALTRA_SANZIONE, " + "DATA_INIZIO_ESECUZIONE, "
				+ "DATA_SCADENZA, " + "IST_DET_ID_ISTITUTO_DETENZIONE, " + "MOTIVAZIONE, "
				+ "EVE_ID_EVENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "FLAG_MOTIVO, MOTIVOESS.RV_MEANING DESCR_MOTIVO, " + "FLAG_VALIDA, "
				+ "COD_TIPO_AUTORITA, AUTORITA.RV_MEANING DESCR_TIPO_AUTORITA, "
				+ "COD_LUOGO_AUTORITA, COMUNE.DESCRIZIONE DESCR_LUOGO_AUTORITA, "
				+ "COD_TIPO_UFFICIO_SOSP, UFFICIO.RV_MEANING DESCR_TIPO_UFFICIO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "SOSPENSIONE_GG, " + "SOSPENSIONE_MM, "
				+ "SOSPENSIONE_AA, "
				+ "DA_RECUPERARE, "
				+
				// 12-05-2008 Modifica Periodo da Recuperare in GG, MM, AA
				// "NUMERO_GIORNI, "+
				"DA_RECUPERARE_GG, " + "DA_RECUPERARE_MM, " + "DA_RECUPERARE_AA, " + "ESPIATA_GG, "
				+ "ESPIATA_MM, " + "ESPIATA_AA, " + "RESIDUA_GG, " + "RESIDUA_MM, " + "RESIDUA_AA ";

		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM PERIODO_ALTRA_SANZIONE, CG_REF_CODES AUTORITA, ";
		lStatement += " COMUNE, CG_REF_CODES MOTIVOESS, CG_REF_CODES UFFICIO ";

		lStatement += " WHERE AUTORITA.RV_DOMAIN = 'TIPO_AUTORITA' AND AUTORITA.RV_LOW_VALUE = COD_TIPO_AUTORITA ";
		lStatement += " AND COMUNE.COD_COMUNE = COD_LUOGO_AUTORITA";
		lStatement += " AND UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO_SOSP' AND UFFICIO.RV_HIGH_VALUE = COD_TIPO_UFFICIO_SOSP ";
		lStatement += " AND COMUNE.COD_COMUNE = COD_LUOGO_AUTORITA";
		lStatement += " AND MOTIVOESS.RV_DOMAIN = 'MOTIVO_PERIODO_ESS' AND MOTIVOESS.RV_LOW_VALUE = FLAG_MOTIVO ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		PeriodoAltraSanzioneModel aModel = new PeriodoAltraSanzioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPeriodoAltraSanzione(getBigDecimal("ID_PERIODO_ALTRA_SANZIONE"));
		aModel.setDataInizioEsecuzione(getDate("DATA_INIZIO_ESECUZIONE"));
		aModel.setDataScadenza(getDate("DATA_SCADENZA"));
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		aModel.setMotivazione(getString("MOTIVAZIONE"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setFlagMotivo(getString("FLAG_MOTIVO"));
		aModel.setDescrMotivo(getString("DESCR_MOTIVO"));
		aModel.setFlagValida(getString("FLAG_VALIDA"));
		aModel.setCodTipoAutorita(getString("COD_TIPO_AUTORITA"));
		aModel.setCodLuogoAutorita(getString("COD_LUOGO_AUTORITA"));
		aModel.setDescrTipoAutorita(getString("DESCR_TIPO_AUTORITA"));
		aModel.setDescrLuogoAutorita(getString("DESCR_LUOGO_AUTORITA"));
		aModel.setSospensioneGG(getBigDecimal("SOSPENSIONE_GG"));
		aModel.setSospensioneMM(getBigDecimal("SOSPENSIONE_MM"));
		aModel.setSospensioneAA(getBigDecimal("SOSPENSIONE_AA"));
		aModel.setDaRecuperare(getString("DA_RECUPERARE"));
		// 12/05/2008 aModel.setNumeroGiorni ( getBigDecimal ("NUMERO_GIORNI" ) );
		aModel.setDaRecuperareGG(getBigDecimal("DA_RECUPERARE_GG"));
		aModel.setDaRecuperareMM(getBigDecimal("DA_RECUPERARE_MM"));
		aModel.setDaRecuperareAA(getBigDecimal("DA_RECUPERARE_AA"));
		aModel.setCodTipoUfficioSosp(getString("COD_TIPO_UFFICIO_SOSP"));
		aModel.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		aModel.setEspiataGG(getBigDecimal("ESPIATA_GG"));
		aModel.setEspiataMM(getBigDecimal("ESPIATA_MM"));
		aModel.setEspiataAA(getBigDecimal("ESPIATA_AA"));
		aModel.setResiduaGG(getBigDecimal("RESIDUA_GG"));
		aModel.setResiduaMM(getBigDecimal("RESIDUA_MM"));
		aModel.setResiduaAA(getBigDecimal("RESIDUA_AA"));

		// aModel.setDescrUfficioAggiornamento(getString("") );

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(PeriodoAltraSanzioneModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdPeriodoAltraSanzione() != null) {
			lCondizioni += " and ID_PERIODO_ALTRA_SANZIONE = " + aModel.getIdPeriodoAltraSanzione() + "";
		}
		if (aModel.getDataInizioEsecuzione() != null) {
			lCondizioni += " and to_char(DATA_INIZIO_ESECUZIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizioEsecuzione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataScadenza() != null) {
			lCondizioni += " and to_char(DATA_SCADENZA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataScadenza(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getIstDetIdIstitutoDetenzione() != null
				&& aModel.getIstDetIdIstitutoDetenzione().length() > 0) {
			lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione()
					+ "' ";
		}
		if (aModel.getMotivazione() != null && aModel.getMotivazione().length() > 0) {
			lCondizioni += " and MOTIVAZIONE = '" + aModel.getMotivazione() + "' ";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
		}
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
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
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		if (aModel.getFasSiuIdFascicoloSius() != null) {
			lCondizioni += " and FAS_SIU_ID_FASCICOLO_SIUS = " + aModel.getFasSiuIdFascicoloSius() + "";
		}
		if (aModel.getFlagMotivo() != null) {
			lCondizioni += " and FLAG_MOTIVO = '" + aModel.getFlagMotivo() + "'";
		}
		if (aModel.getFlagValida() != null) {
			lCondizioni += " and FLAG_VALIDA = '" + aModel.getFlagValida() + "'";
		}
		if (aModel.getCodTipoAutorita() != null) {
			lCondizioni += " and COD_TIPO_AUTORITA = '" + aModel.getCodTipoAutorita() + "'";
		}
		if (aModel.getCodLuogoAutorita() != null) {
			lCondizioni += " and COD_LUOGO_AUTORITA = '" + aModel.getCodLuogoAutorita() + "'";
		}
		if (aModel.getSospensioneGG() != null) {
			lCondizioni += " and SOSPENSIONE_GG = " + aModel.getSospensioneGG() + "";
		}
		if (aModel.getSospensioneMM() != null) {
			lCondizioni += " and SOSPENSIONE_MM = " + aModel.getSospensioneMM() + "";
		}
		if (aModel.getSospensioneAA() != null) {
			lCondizioni += " and SOSPENSIONE_AA = " + aModel.getSospensioneAA() + "";
		}
		if (aModel.getDaRecuperare() != null) {
			lCondizioni += " and DA_RECUPERARE = '" + aModel.getDaRecuperare() + "'";
		}
		// 12/05/2008 if (aModel.getNumeroGiorni() != null ) {
		// lCondizioni += " and NUMERO_GIORNI = " + aModel.getNumeroGiorni() + "";
		// }
		if (aModel.getDaRecuperareGG() != null) {
			lCondizioni += " and DA_RECUPERARE_GG = '" + aModel.getDaRecuperareGG() + "'";
		}
		if (aModel.getDaRecuperareMM() != null) {
			lCondizioni += " and DA_RECUPERARE_MM = '" + aModel.getDaRecuperareMM() + "'";
		}
		if (aModel.getDaRecuperareAA() != null) {
			lCondizioni += " and DA_RECUPERARE_AA = '" + aModel.getDaRecuperareAA() + "'";
		}
		if (aModel.getCodTipoUfficioSosp() != null) {
			lCondizioni += " and COD_TIPO_UFFICIO_SOSP = '" + aModel.getCodTipoUfficioSosp() + "'";
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
	public String setCondizioniByKey(BigDecimal aIdPeriodoAltraSanzione) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PERIODO_ALTRA_SANZIONE = " + aIdPeriodoAltraSanzione;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);

		return lCondizioni;
	}

	public void ricercaSanzioneSostitutivaByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFascicolo(aKey);
		lSql += " " + getOrderBy() + " ";
		setStatement(lSql);
	}

	public void ricercaSanzioneSostitutivaByIdFascicolo(BigDecimal aKey, String lOrderType)
			throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFascicolo(aKey);
		lSql += " " + getOrderBy(lOrderType) + " ";
		setStatement(lSql);
	}

	public String setCondizioniByIdFascicolo(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " AND FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;

		return lCondizioni;
	}

	public void ricercaSanzioneSostitutivaByIdSiep(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdSiep(aKey);
		lSql += " " + getOrderBy() + " ";
		setStatement(lSql);
	}

	public String setCondizioniByIdSiep(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		return lCondizioni;
	}

	public void ricercaSanzioneSostitutivaByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdEvento(aKey);
		lSql += " " + getOrderBy() + " ";
		setStatement(lSql);
	}

	public String setCondizioniByIdEvento(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " AND EVE_ID_EVENTO  = " + aKey;

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		orderBy = " ORDER BY DATA_INIZIO_ESECUZIONE ASC, FLAG_MOTIVO ASC ";
		return orderBy;
	}

	protected String getOrderBy(String lOrderType) {
		String orderBy = new String("");
		orderBy = " ORDER BY DATA_INIZIO_ESECUZIONE " + lOrderType + "";
		return orderBy;
	}

}
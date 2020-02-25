package siap.sius.misurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.misurasicurezza.model.ProvvedimentoEventoTenoreFascicoloSiusModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: PeriodoAltraMisuraSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella PeriodoAltraMisura
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
public class PeriodoAltraMisuraSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public PeriodoAltraMisuraSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountPeriodoAltraMisura(PeriodoAltraMisuraModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM PERIODO_ALTRA_MISURA ";

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
	 * public void ricercaPeriodoAltraMisuraPaged(PeriodoAltraMisuraModel aModel, int aPage) throws
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
	public void ricercaPeriodoAltraMisura(PeriodoAltraMisuraModel aModel) throws DAOException {
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
	public void ricercaPeriodoAltraMisuraByKey(BigDecimal aIdPeriodoAltraMisura) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " AND " + setCondizioniByKey(aIdPeriodoAltraMisura);
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

		lStatement += " SELECT " + "ID_PERIODO_ALTRA_MISURA, " + "DATA_INIZIO_ESECUZIONE, "
				+ "DATA_SCADENZA, " + "IST_DET_ID_ISTITUTO_DETENZIONE, " + "MOTIVAZIONE, "
				+ "EVE_ID_EVENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "FLAG_MOTIVO, MOTIVOEMS.RV_MEANING DESCR_MOTIVO, " + "FLAG_VALIDA, "
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
		lStatement += " FROM PERIODO_ALTRA_MISURA, CG_REF_CODES AUTORITA, ";
		lStatement += " COMUNE, CG_REF_CODES MOTIVOEMS, CG_REF_CODES UFFICIO ";

		lStatement += " WHERE AUTORITA.RV_DOMAIN = 'TIPO_AUTORITA' AND AUTORITA.RV_LOW_VALUE = COD_TIPO_AUTORITA ";
		lStatement += " AND COMUNE.COD_COMUNE = COD_LUOGO_AUTORITA";
		lStatement += " AND UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO_SOSP' AND UFFICIO.RV_HIGH_VALUE = COD_TIPO_UFFICIO_SOSP ";
		lStatement += " AND COMUNE.COD_COMUNE = COD_LUOGO_AUTORITA";
		lStatement += " AND MOTIVOEMS.RV_DOMAIN = 'MOTIVO_PERIODO_EMS' AND MOTIVOEMS.RV_LOW_VALUE = FLAG_MOTIVO ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		PeriodoAltraMisuraModel aModel = new PeriodoAltraMisuraModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPeriodoAltraMisura(getBigDecimal("ID_PERIODO_ALTRA_MISURA"));
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
	public String setCondizioni(PeriodoAltraMisuraModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdPeriodoAltraMisura() != null) {
			lCondizioni += " and ID_PERIODO_ALTRA_MISURA = " + aModel.getIdPeriodoAltraMisura() + "";
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
	public String setCondizioniByKey(BigDecimal aIdPeriodoAltraMisura) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PERIODO_ALTRA_MISURA = " + aIdPeriodoAltraMisura;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);

		return lCondizioni;
	}

	public void ricercaMisuraSicurezzaByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFascicolo(aKey);
		lSql += " " + getOrderBy() + " ";
		setStatement(lSql);
	}

	public void ricercaMisuraSicurezzaByIdFascicolo(BigDecimal aKey, String lOrderType) throws DAOException {
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

	public void ricercaMisuraSicurezzaByIdSiep(BigDecimal aKey) throws DAOException {
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

	public void ricercaMisuraSicurezzaByIdEvento(BigDecimal aKey) throws DAOException {
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

	// 06/02/2015 Revisione criterio di recupero informazioni provvedimenti SIUS su Misure di Sicurezza.
	public void ricercaProvvedimentiMisSicByIdFascicoloSiep(BigDecimal aKey) throws DAOException {
		String lSql = "";
		lSql += "SELECT distinct (FASC.ID_FASCICOLO_SIUS), FASC.CHIAVE_ANNO as CHIAVE_ANNO, FASC.CHIAVE_PROGR as CHIAVE_PROGR, FASC.CHIAVE_UFFICIO,";
		lSql += " EVE.COD_TIPO_PROVVEDIMENTO, EVE.COD_MOTIVO, EVE.DATA_EMISSIONE AS DATA_EMISSIONE, EVE.ID_EVENTO, EVE.COD_ESITO,";
		lSql += "	TEN.COD_OGGETTO_TENORE, TEN.COD_ESITO_TENORE, DEPO.ANNO_S3 as ANNO_PROV, DEPO.NUM_S3 as NUM_PROV, DEPO.FLAG_ELABORATO,";
		lSql += " UFD.DESCR_TIPO_UFFICIO DESCR_TIPO_UF, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		// MEV_39: aggiunti campi in estrazione
		lSql += " UFF.COD_UFFICIO COD_UFFICIO,";
		lSql += " NULL FLAG_DECISIONE_TRIBUNALE,";
		lSql += " DEPO.LUOGO_SVOLGIMENTO_PROVA,";
		lSql += " DEPO.DATA_INIZIO_PERIODO,";
		lSql += " DEPO.DATA_FINE_MISURA,";
		lSql += " DEPO.SOSPENSIONE_GG,";
		lSql += " DEPO.SOSPENSIONE_MM,";
		lSql += " DEPO.SOSPENSIONE_AA,";

		lSql += " MOTIVO_PROVVEDIMENTO.RV_MEANING OGGETTO,";
		lSql += " ESITO_PROVVEDIMENTO.RV_MEANING ESITO,";
		lSql += " ESITO_TENORE.RV_ABBREVIATION COD_ESITO,";
		lSql += " ESITO_PROVVEDIMENTO.RV_MEANING ESITO";

		lSql += " FROM FASCICOLO_SIUS FASC, EVENTO EVE, TENORE TEN,"
				
				+ " DEPOSITO_ORDINANZA_PC DEPO,";
		
		lSql += " UFFICIO UFF, COMUNE DESCR_COM_UFF, UFFICIO_DESCR UFD,";
		lSql += " CG_REF_CODES MOTIVO_PROVVEDIMENTO, CG_REF_CODES ESITO_TENORE,";
		lSql += " CG_REF_CODES ESITO_PROVVEDIMENTO, CG_REF_CODES OGGETTO_PROCEDIMENTO";
		lSql += " WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lSql += " AND EVE.FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lSql += " AND EVE.COD_MOTIVO = TEN.COD_OGGETTO_TENORE";
		lSql += " AND TEN.DEP_OPID_DEPOSITO_ORDINANZA_PC = DEPO.ID_DEPOSITO_ORDINANZA_PC";
		lSql += " AND EVE.ID_EVENTO = DEPO.ID_EVENTO_GENERATO";
		lSql += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lSql += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lSql += " AND UFF.COD_UFFICIO = UFD.COD_UFFICIO";
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = TEN.COD_OGGETTO_TENORE";
		lSql += " AND ESITO_TENORE.RV_DOMAIN = 'ESITO_TENORE'";
		lSql += " AND ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'";
		lSql += " AND ESITO_PROVVEDIMENTO.RV_LOW_VALUE = ESITO_TENORE.RV_ABBREVIATION";
		lSql += " AND ESITO_TENORE.RV_HIGH_VALUE = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lSql += " AND OGGETTO_PROCEDIMENTO.RV_ALT3_VALUE = 'MSI'";
		lSql += " AND ESITO_TENORE.RV_ABBREVIATION = TEN.COD_ESITO_TENORE";			
		// @emma intervento post collaudo 11.3 (anomalia 9 del verbale) - estrarre solo provvedimenti che hanno misure di sicurezza
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_HIGH_VALUE=OGGETTO_PROCEDIMENTO.RV_LOW_VALUE" ; 
		// fine intervento post collaudo 11.3 		
		// MEV_39: NELL'ELENCO ANNOTAZIONE DECISIONI SORVEGLAINZA, NON DEVE ESTRARRE L'ESITO TENORE 0210 (Accoglie Appello e Applica la Misura)
		// intervento post collaudo 11.3 (terza sessione) per risolvere anomalia 2 (commento riga sotto)
		//lSql += " AND ESITO_TENORE.RV_ABBREVIATION  <> '0210' ";

		lSql += " UNION ";

		lSql += "SELECT distinct (FASC.ID_FASCICOLO_SIUS), FASC.CHIAVE_ANNO as CHIAVE_ANNO, FASC.CHIAVE_PROGR as CHIAVE_PROGR, FASC.CHIAVE_UFFICIO,";
		lSql += " EVE.COD_TIPO_PROVVEDIMENTO, EVE.COD_MOTIVO, EVE.DATA_EMISSIONE AS DATA_EMISSIONE, EVE.ID_EVENTO, EVE.COD_ESITO,";
		lSql += "	TEN.COD_OGGETTO_TENORE, TEN.COD_ESITO_TENORE, DECR.ANNO_S72 as ANNO_PROV, DECR.NUM_S72 as NUM_PROV, DECR.FLAG_ELABORATO,";
		lSql += " UFD.DESCR_TIPO_UFFICIO DESCR_TIPO_UF, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		// MEV_39: aggiunti campi in estrazione
		lSql += " UFF.COD_UFFICIO COD_UFFICIO,";
		lSql += " NULL FLAG_DECISIONE_TRIBUNALE,";
		lSql += " DECR.LUOGO_SVOLGIMENTO_PROVA,";
		lSql += " DECR.DATA_SOSPENSIONE_SS DATA_INIZIO_PERIODO,";
		lSql += " DECR.DATA_SCADENZA_SOSPENSIONE_SS DATA_FINE_MISURA,";
		lSql += " DECR.SOSPENSIONE_GG,";
		lSql += " DECR.SOSPENSIONE_MM,";
		lSql += " DECR.SOSPENSIONE_AA,";

		lSql += " MOTIVO_PROVVEDIMENTO.RV_MEANING OGGETTO,";
		lSql += " ESITO_PROVVEDIMENTO.RV_MEANING ESITO,";
		lSql += " ESITO_TENORE.RV_ABBREVIATION COD_ESITO,";
		lSql += " ESITO_PROVVEDIMENTO.RV_MEANING ESITO";

		lSql += " FROM FASCICOLO_SIUS FASC, EVENTO EVE, TENORE TEN, DEPOSITO_DECRETO DECR,";
		lSql += " UFFICIO UFF, COMUNE DESCR_COM_UFF, UFFICIO_DESCR UFD,";
		lSql += " CG_REF_CODES MOTIVO_PROVVEDIMENTO, CG_REF_CODES ESITO_TENORE,";
		lSql += " CG_REF_CODES ESITO_PROVVEDIMENTO, CG_REF_CODES OGGETTO_PROCEDIMENTO";		
		lSql += " WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lSql += " AND EVE.FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lSql += " AND EVE.COD_MOTIVO = TEN.COD_OGGETTO_TENORE";
		lSql += " AND TEN.DEP_DEC_ID_DEPOSITO_DECRETO = DECR.ID_DEPOSITO_DECRETO";
		lSql += " AND EVE.ID_EVENTO = DECR.ID_EVENTO_GENERATO";
		lSql += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lSql += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lSql += " AND UFF.COD_UFFICIO = UFD.COD_UFFICIO";
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lSql += " AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = TEN.COD_OGGETTO_TENORE";
		lSql += " AND ESITO_TENORE.RV_DOMAIN = 'ESITO_TENORE'";
		lSql += " AND ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'";
		lSql += " AND ESITO_PROVVEDIMENTO.RV_LOW_VALUE = ESITO_TENORE.RV_ABBREVIATION";
		lSql += " AND ESITO_TENORE.RV_HIGH_VALUE = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lSql += " AND OGGETTO_PROCEDIMENTO.RV_ALT3_VALUE = 'MSI'";
		lSql += " AND ESITO_TENORE.RV_ABBREVIATION = TEN.COD_ESITO_TENORE";
		// MEV_39: NELL'ELENCO ANNOTAZIONE DECISIONI SORVEGLAINZA, NON DEVE ESTRARRE L'ESITO TENORE 0210 (Accoglie Appello e Applica la Misura) cap. 7.3 (pag 68) dell'AF
		// intervento post collaudo 11.3 (terza sessione) per risolvere anomalia 2 (commento riga sotto)
		//lSql += " AND ESITO_TENORE.RV_ABBREVIATION  <> '0210' ";
		// @emma intervento post collaudo 11.3 (anomalia 9 del verbale) - estrarre solo provvedimenti che hanno misure di sicurezza
	    lSql += " AND MOTIVO_PROVVEDIMENTO.RV_HIGH_VALUE=OGGETTO_PROCEDIMENTO.RV_LOW_VALUE" ; 
		// fine intervento post collaudo 11.3 
		lSql += " ORDER BY DATA_EMISSIONE, CHIAVE_ANNO, CHIAVE_PROGR";
		setStatement(lSql);

	}

	// METODO GETMODEL() per ricercaProvvedimentoByFascicoloSiepIdEveGenerato
	//
	public GenericModel getModelEsitoMisSic() throws DAOException {

		ProvvedimentoEventoTenoreFascicoloSiusModel aModel = new ProvvedimentoEventoTenoreFascicoloSiusModel();

		FascicoloSiusModel lFas = new FascicoloSiusModel();
		lFas.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFas.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFas.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFas.setChiaveUfficio(getString("CHIAVE_UFFICIO"));

		aModel.setDescrTipoUfficio(getString("DESCR_TIPO_UF"));
		aModel.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));

		EventoModel lEve = new EventoModel();
		lEve.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEve.setCodMotivo(getString("COD_MOTIVO"));
		lEve.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEve.setDataEmissione(getDate("DATA_EMISSIONE"));
		lEve.setCodEsito(getString("COD_ESITO"));
		// MEV_39: aggiunto set di proprieta'
		lEve.setCodLuogoEmittente(getString("COD_UFFICIO"));

		TenoreModel lTen = new TenoreModel();
		aModel.setDescrOggetto(getString("OGGETTO"));
		aModel.setDescrEsito(getString("ESITO"));
		lTen.setCodOggettoTenore(getString("COD_OGGETTO_TENORE"));
		lTen.setCodEsitoTenore(getString("COD_ESITO_TENORE"));

		if (lEve.getCodTipoProvvedimento().compareTo("03") == 0) {
			DepositoOrdinanzaPcModel lDep = new DepositoOrdinanzaPcModel();
			lDep.setAnnoS3(getBigDecimal("ANNO_PROV"));
			lDep.setNumS3(getBigDecimal("NUM_PROV"));
			lDep.setFlagElaborato(getString("FLAG_ELABORATO"));
			// MEV_39: aggiunto set di proprieta'
			lDep.setDataInizioPeriodo(getDate("DATA_INIZIO_PERIODO"));
			lDep.setDataFineMisura(getDate("DATA_FINE_MISURA"));
			lDep.setSospensioneAASS(getBigDecimal("SOSPENSIONE_AA"));
			lDep.setSospensioneMMSS(getBigDecimal("SOSPENSIONE_MM"));
			lDep.setSospensioneGGSS(getBigDecimal("SOSPENSIONE_GG"));
			lDep.setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));
			// lDep.setFlagDecisioneTribunale(getString("FLAG_DECISIONE_TRIBUNALE"));
			aModel.setOrdinanza(lDep);
		}

		if (lEve.getCodTipoProvvedimento().compareTo("02") == 0) {
			DepositoDecretoModel lDec = new DepositoDecretoModel();
			lDec.setAnnoS72(getBigDecimal("ANNO_PROV"));
			lDec.setNumS72(getBigDecimal("NUM_PROV"));
			lDec.setFlagElaborato(getString("FLAG_ELABORATO"));
			// MEV_39: aggiunto set di proprieta'
			lDec.setDataSospensioneSS(getDate("DATA_INIZIO_PERIODO"));
			lDec.setDataScadenzaSospensioneSS(getDate("DATA_FINE_MISURA"));
			lDec.setSospensioneAASS(getBigDecimal("SOSPENSIONE_AA"));
			lDec.setSospensioneMMSS(getBigDecimal("SOSPENSIONE_MM"));
			lDec.setSospensioneGGSS(getBigDecimal("SOSPENSIONE_GG"));
			lDec.setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));
			// lDec.setFlagDecisioneTribunale(getString("FLAG_DECISIONE_TRIBUNALE"));
			aModel.setDecreto(lDec);
		}

		aModel.setFascicoloSius(lFas);
		aModel.setEvento(lEve);
		aModel.setTenore(lTen);

		return aModel;
	}

}
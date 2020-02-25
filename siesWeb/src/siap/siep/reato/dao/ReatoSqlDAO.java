package siap.siep.reato.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import siap.dao.SIAPSqlDAO;
import siap.siep.reato.model.ReatoModel;
import siap.sige.reato.model.ReatoSentenzaSigeModel;

/**
 * <p>
 * Title: ReatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Reato
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

public class ReatoSqlDAO extends SIAPSqlDAO {
	public ReatoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaReato(ReatoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	public void ricercaReati(ReatoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneCount(aModel);
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	public void ricercaReatiByFascicoloSiep(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND PROGR_CIRCOSTANZA = 1"; // I record con PROGR_CIRCOSTANZA = 1 sono reati e non
												// circostanze
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	public void ricercaReatiNoCircostanzaByFascicoloSiep(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	public void ricercaCircostanzeReatoByReatoFascicoloSiep(BigDecimal aKeyReato, BigDecimal aKeyFasc)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND PROGR_REATO = " + aKeyReato;
		lSql += " AND PROGR_CIRCOSTANZA != 1"; // I record con PROGR_CIRCOSTANZA = 1 sono reati e non
												// circostanze
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFasc;
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	public void ricercaNormaPrincipaleByReatoFascicoloSiep(BigDecimal aKeyReato, BigDecimal aKeyFasc)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND PROGR_REATO = " + aKeyReato;
		lSql += " AND PROGR_CIRCOSTANZA = 1";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFasc;
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	public void ricercaReatoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void getCountReati(ReatoModel aModel, Boolean solocumulati) throws DAOException {
		String lStatement = "SELECT COUNT (a.ID_FASCICOLO_SIEP) howmanyrecords ";
		lStatement += "  FROM (SELECT DISTINCT ID_FASCICOLO_SIEP FROM FASCICOLO_SIEP FASC,REATO,SOGGETTO SOGG,UFFICIO UFF,CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIEP = REATO.FAS_SIE_ID_FASCICOLO_SIEP";

		lStatement += "  AND  FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO  ";
		lStatement += "  AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += "  AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "  AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE ";
		lStatement += "  AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";

		lStatement += setCondizioneCountProcperReato(aModel, solocumulati);

		// MEV 26 CUMULO - Ricerca Procedimeto by Reato e Crcostanzae - Cerco anche in REATO_CUMULO
		lStatement += " UNION ";

		lStatement += " SELECT DISTINCT ID_FASCICOLO_SIEP";
		lStatement += " FROM FASCICOLO_SIEP FAS, REATO_CUMULO RCUM, SOGGETTO SOGG, UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF,";
		lStatement += " TITOLO_CUMULATO TITCUM, ISTRUTTORIA_CUMULO ISTRU, EVENTO";
		lStatement += " WHERE";

		lStatement += " FAS.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += "  AND UFF.COD_UFFICIO = FAS.CHIAVE_UFFICIO ";
		lStatement += "  AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "  AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE ";
		lStatement += "  AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";

		lStatement += setCondizioneCountProcperReato_Cumulo(aModel, solocumulati);

		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP ";
		lStatement += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += " AND ISTRU.EVE_ID_EVENTO_PROV = EVENTO.ID_EVENTO ";
		lStatement += " AND TITCUM.ISTR_ID_ISTRUTTORIA_CUMULO = ISTRU.ID_ISTRUTTORIA_CUMULO";
		lStatement += " AND RCUM.TIT_ID_TITOLO_CUMULATO = TITCUM.ID_TITOLO_CUMULATO";

		// Ricerca dell'ultimo provvedimento di Cumulo Valido
		lStatement += setCondizioneDataMaxEventoCumulo();
		// END MEV 26 CUMULO

		lStatement += " )a";

		setStatement(lStatement);

	}

	public BigDecimal getProgressivoReato(BigDecimal aKey) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_REATO) aMAX";
		lStatement += " FROM REATO";
		lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		setStatement(lStatement);

		this.start();

		BigDecimal lProgressivo = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgressivo = this.getBigDecimal("aMAX");

		this.stop();

		if (lProgressivo == null)
			lProgressivo = new BigDecimal(0);

		return lProgressivo;
	}

	public BigDecimal getProgressivoCircostanza(BigDecimal aProgReato, BigDecimal aKeyFas)
			throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_CIRCOSTANZA) aMAX";
		lStatement += " FROM REATO";
		lStatement += " WHERE PROGR_REATO = " + aProgReato;
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFas;

		setStatement(lStatement);

		this.start();

		BigDecimal lProgressivo = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgressivo = this.getBigDecimal("aMAX");

		this.stop();

		if (lProgressivo == null)
			lProgressivo = new BigDecimal(0);

		return lProgressivo;
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_REATO, " + "COD_TIPO_REATO, " + "DATA_REATO, "
				+ "PROGR_NUMERO_MANUALE, " + "PROGR_REATO, " + "PROGR_CIRCOSTANZA, " + "DATA_INIZIO, "
				+ "ANNO_INIZIO, " + "MESE_INIZIO, " + "GIORNO_INIZIO, " + "DATA_FINE, " + "ANNO_FINE, "
				+ "MESE_FINE, " + "GIORNO_FINE, " + "COD_PERIODO_CONSUMAZIONE, " + "DESC_LUOGO, "
				+ "COD_FONTE, " + "ANNO_FONTE, " + "NUMERO_FONTE, " + "COD_SOTTONUMERAZIONE, " + "COMMA, "
				+ "LETTERA, " + "NUMERO, " + "ARTICOLO, " + "NOTE, " + "COD_TIPO_PENA_DETENTIVA, "
				+ "NUM_ANNI, " + "NUM_MESI, " + "NUM_GIORNI, " + "ANNI_ISOLAMENTO_DIURNO, "
				+ "MESI_ISOLAMENTO_DIURNO, " + "GIORNI_ISOLAMENTO_DIURNO, " + "SANZIONE_PECUNIARIA, "
				+ "FLAG_ERGASTOLO, " + "DATA_INIZIO_ISOLAMENTO_DIURNO, " + "DATA_FINE_ISOLAMENTO_DIURNO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "COD_TIPO_SANZIONE, " + "ID_CONTINUAZIONE_REATO, "
				+ "TIPO_CONTINUAZIONE_REATO, " + "KEY_REATO_NSC, " +
				// ***************************************
				// Federica - a9-rr-078
				// aggiunto campo Comma-Qualificante
				"COMMA_QUALIFICANTE, ";
		// ***************************************

		lStatement += "DECOTIPOREATO.RV_MEANING DESCTIPOREATO, ";
		lStatement += "DECOPERCONS.RV_MEANING DESCPECONS, ";
		lStatement += "DECOSOTTONUM.RV_MEANING DESCSOTTONUM, ";
		lStatement += "DECOPENADET.RV_MEANING DESCPENADET, ";
		lStatement += "DECOFONTE.RV_MEANING DESCFONTE, ";
		lStatement += "DECTIPOPENADETENTIVA.RV_MEANING DESCTIPOPENADETENTIVA, ";
		lStatement += "DECTIPOSANZIONE.RV_MEANING DESCTIPOSANZIONE, ";
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		lStatement += "DECOCOMMAQUAL.RV_MEANING DESCCOMMAQUALIFICANTE ";
		// ***************************************
		lStatement += "FROM REATO, CG_REF_CODES DECOTIPOREATO, CG_REF_CODES DECOPERCONS, CG_REF_CODES DECOSOTTONUM, CG_REF_CODES DECOPENADET, CG_REF_CODES DECOFONTE, ";
		lStatement += "CG_REF_CODES DECTIPOPENADETENTIVA, CG_REF_CODES DECTIPOSANZIONE, ";
		// ***************************************
		// Federica - a9-rr-078
		lStatement += "CG_REF_CODES DECOCOMMAQUAL ";
		// ***************************************

		lStatement += " WHERE ";
		lStatement += "DECOTIPOREATO.RV_DOMAIN='TIPO_REATO' AND DECOTIPOREATO.RV_LOW_VALUE=REATO.COD_TIPO_REATO ";
		lStatement += "AND DECOPERCONS.RV_DOMAIN='PERIODO_CONSUMAZIONE' AND DECOPERCONS.RV_LOW_VALUE=REATO.COD_PERIODO_CONSUMAZIONE ";
		lStatement += "AND DECOSOTTONUM.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOSOTTONUM.RV_LOW_VALUE=REATO.COD_SOTTONUMERAZIONE ";
		lStatement += "AND DECOPENADET.RV_DOMAIN='TIPO_PENA_DETENTIVA' AND DECOPENADET.RV_LOW_VALUE=REATO.COD_TIPO_PENA_DETENTIVA ";
		lStatement += "AND DECOFONTE.RV_DOMAIN='FONTE' AND DECOFONTE.RV_LOW_VALUE=REATO.COD_FONTE ";
		lStatement += "AND DECTIPOPENADETENTIVA.RV_DOMAIN='TIPO_PENA_DETENTIVA' AND DECTIPOPENADETENTIVA.RV_LOW_VALUE=REATO.COD_TIPO_PENA_DETENTIVA ";
		lStatement += "AND DECTIPOSANZIONE.RV_DOMAIN='TIPO_SANZIONE' AND DECTIPOSANZIONE.RV_LOW_VALUE=REATO.COD_TIPO_SANZIONE ";
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		lStatement += "AND DECOCOMMAQUAL.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOCOMMAQUAL.RV_LOW_VALUE=REATO.COMMA_QUALIFICANTE ";
		// ***************************************

		return lStatement;
	}

	/**
	 * GetMaxIdContinuazione
	 * 
	 * @param idFas
	 * @return
	 * @throws DAOException
	 */
	public BigDecimal GetMaxIdContinuazione(BigDecimal idFas) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(ID_CONTINUAZIONE_REATO) aMAX";
		lStatement += " FROM REATO";
		lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + idFas;

		setStatement(lStatement);

		this.start();

		BigDecimal maxId = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			maxId = this.getBigDecimal("aMAX");

		this.stop();

		if (maxId == null)
			maxId = new BigDecimal(0);

		return maxId;
	}

	//
	// METODO GET MODEL()
	//

	public GenericModel getModel() throws DAOException {
		ReatoModel aModel = new ReatoModel();

		aModel.setIdReato(getBigDecimal("ID_REATO"));
		aModel.setCodTipoReato(getString("COD_TIPO_REATO"));
		aModel.setDescrTipoReato(getString("DESCTIPOREATO"));
		aModel.setDataReato(getDate("DATA_REATO"));
		aModel.setProgrNumeroManuale(getString("PROGR_NUMERO_MANUALE"));
		aModel.setProgrReato(getBigDecimal("PROGR_REATO"));
		aModel.setProgrCircostanza(getBigDecimal("PROGR_CIRCOSTANZA"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setAnnoInizio(getBigDecimal("ANNO_INIZIO"));
		aModel.setMeseInizio(getBigDecimal("MESE_INIZIO"));
		aModel.setGiornoInizio(getBigDecimal("GIORNO_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setAnnoFine(getBigDecimal("ANNO_FINE"));
		aModel.setMeseFine(getBigDecimal("MESE_FINE"));
		aModel.setGiornoFine(getBigDecimal("GIORNO_FINE"));
		aModel.setCodPeriodoConsumazione(getString("COD_PERIODO_CONSUMAZIONE"));
		aModel.setDescrPeriodoConsumazione(getString("DESCPECONS"));
		aModel.setDescLuogo(getString("DESC_LUOGO"));
		aModel.setCodFonte(getString("COD_FONTE"));
		aModel.setDescrFonte(getString("DESCFONTE"));
		aModel.setAnnoFonte(getBigDecimal("ANNO_FONTE"));
		aModel.setNumeroFonte(getString("NUMERO_FONTE"));
		aModel.setCodSottonumerazione(getString("COD_SOTTONUMERAZIONE"));
		aModel.setDescrSottonumerazione(getString("DESCSOTTONUM"));
		aModel.setComma(getString("COMMA"));
		aModel.setLettera(getString("LETTERA"));
		aModel.setNumero(getString("NUMERO"));
		aModel.setArticolo(getString("ARTICOLO"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodTipoPenaDetentiva(getString("COD_TIPO_PENA_DETENTIVA"));
		aModel.setDescrTipoPenaDetentiva(getString("DESCTIPOPENADETENTIVA"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setSanzionePecuniaria(getBigDecimal("SANZIONE_PECUNIARIA"));
		aModel.setFlagErgastolo(getString("FLAG_ERGASTOLO"));
		aModel.setDataInizioIsolamentoDiurno(getDate("DATA_INIZIO_ISOLAMENTO_DIURNO"));
		aModel.setDataFineIsolamentoDiurno(getDate("DATA_FINE_ISOLAMENTO_DIURNO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		aModel.setDescrTipoSanzione(getString("DESCTIPOSANZIONE"));
		aModel.setNumAnniIsolamentoDiurno(getBigDecimal("ANNI_ISOLAMENTO_DIURNO"));
		aModel.setNumMesiIsolamentoDiurno(getBigDecimal("MESI_ISOLAMENTO_DIURNO"));
		aModel.setNumGiorniIsolamentoDiurno(getBigDecimal("GIORNI_ISOLAMENTO_DIURNO"));
		aModel.setIdContinuazioneReato(getBigDecimal("ID_CONTINUAZIONE_REATO"));
		aModel.setTipoContinuazioneReato(getString("TIPO_CONTINUAZIONE_REATO"));
		aModel.setKeyReatoNsc(getBigDecimal("KEY_REATO_NSC"));
		// ****************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		aModel.setCommaQualificante(getString("COMMA_QUALIFICANTE"));
		aModel.setDescrCommaQualificante(getString("DESCCOMMAQUALIFICANTE"));
		// ****************************************************************************

		aModel.calcolaStringaConsumazione();

		return aModel;
	}

	public String setCondizione(ReatoModel aModel) {
		String lCondizioni = new String("");

		// boolean lInserito = false;
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
			// lInserito=true;
		}
		if (aModel.getProgrReato() != null) {
			lCondizioni += " AND PROGR_REATO=" + aModel.getProgrReato();
			// lInserito=true;
		}

		return lCondizioni;
	}

	public String setCondizioneFas(BigDecimal anno, BigDecimal progr, String uff) {
		String lStatement = "";

		lStatement += " (CHIAVE_PROGR = " + progr + ")";
		lStatement += " AND (CHIAVE_ANNO = " + anno + ")";
		lStatement += " AND (CHIAVE_UFFICIO = '" + uff + "')";

		return lStatement;
	}

	private String filtroDataReato(Date inizio, Date fine) {
		String ret = "";

		if (inizio != null || fine != null) {
			String cond = "";
			if (inizio == null) {
				// è stata inserita solo la data fine
				cond = "<= TO_DATE('" + DateUtils.getDateToString(fine, "ddMMyyyy") + "', 'DDMMYYYY')";
			} else if (fine == null) {
				// è stata inserita solo la data inizio
				cond = ">= TO_DATE('" + DateUtils.getDateToString(inizio, "ddMMyyyy") + "', 'DDMMYYYY')";
			} else {
				// sono state valorizzate entrambe
				cond = "BETWEEN TO_DATE('" + DateUtils.getDateToString(inizio, "ddMMyyyy")
						+ "', 'DDMMYYYY') AND TO_DATE('" + DateUtils.getDateToString(fine, "ddMMyyyy")
						+ "', 'DDMMYYYY')";
			}

			StringBuffer sb = new StringBuffer();
			sb.append(" and ( ");
			sb.append(" ((REATO.DATA_INIZIO is not null) and (REATO.DATA_INIZIO " + cond + " )) ");
			sb.append(
					" or ((REATO.DATA_INIZIO is null) and (REATO.ANNO_INIZIO is not null) and (REATO.MESE_INIZIO is not null) and (REATO.MESE_INIZIO <10 ) and (TO_DATE('0'||REATO.MESE_INIZIO||REATO.ANNO_INIZIO, 'MMYYYY') "
							+ cond + " )) ");
			sb.append(
					" or ((REATO.DATA_INIZIO is null) and (REATO.ANNO_INIZIO is not null) and (REATO.MESE_INIZIO is not null) and (REATO.MESE_INIZIO >=10 ) and (TO_DATE(REATO.MESE_INIZIO||REATO.ANNO_INIZIO, 'MMYYYY') "
							+ cond + " )) ");
			sb.append(
					" or ((REATO.DATA_INIZIO is null) and (REATO.ANNO_INIZIO is not null) and (REATO.MESE_INIZIO is null) and (TO_DATE(REATO.ANNO_INIZIO, 'YYYY') "
							+ cond + " )) ");
			sb.append(" ) ");
			ret = sb.toString();
		}

		return ret;
	}

	public String setCondizioneCount(ReatoModel aRm) {

		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND REATO.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte()) + "'";
		}

		if (aRm.getAnnoFonte() != null && !aRm.getAnnoFonte().toString().equals("")) {
			lCondizioni += " AND REATO.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND REATO.NUMERO_FONTE = '" + StringUtils.convertSqlString(aRm.getNumeroFonte())
					+ "'";
		}

		if (!aRm.getCodSottonumerazione().equals("") && !aRm.getCodSottonumerazione().equals("-")) {
			lCondizioni += " AND REATO.COD_SOTTONUMERAZIONE = '"
					+ StringUtils.convertSqlString(aRm.getCodSottonumerazione()) + "'";
		}

		if (!aRm.getComma().equals("")) {
			lCondizioni += " AND REATO.COMMA = '" + StringUtils.convertSqlString(aRm.getComma()) + "'";
		}

		// ****************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		if (!aRm.getCommaQualificante().equals("") && !aRm.getCommaQualificante().equals("-")) {
			lCondizioni += " AND REATO.COMMA_QUALIFICANTE = '"
					+ StringUtils.convertSqlString(aRm.getCommaQualificante()) + "'";
		}
		// ****************************************************************************

		if (!aRm.getLettera().equals("")) {
			lCondizioni += " AND REATO.LETTERA = '" + StringUtils.convertSqlString(aRm.getLettera()) + "'";
		}

		if (!aRm.getNumero().equals("")) {
			lCondizioni += " AND REATO.NUMERO = '" + StringUtils.convertSqlString(aRm.getNumero()) + "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND REATO.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo()) + "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FASC.COD_UFFICIO_INSERIMENTO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		if (aRm.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND REATO.FAS_SIE_ID_FASCICOLO_SIEP = '" + aRm.getFasSieIdFascicoloSiep() + "'";
		}
		if (aRm.getProgrReato() != null) {
			lCondizioni += " AND REATO.PROGR_REATO = '" + aRm.getProgrReato() + "'";
		}

		lCondizioni += filtroDataReato(aRm.getDataInizio(), aRm.getDataFine());

		if ("I".equals(aRm.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA = '039' ";
		} else if ("S".equals(aRm.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA != '039' ";
		}

		if ("NA".equals(aRm.getCodStatoFascicolo())) {
			lCondizioni += " AND FASC.COD_STATO_FASCICOLO != '01' ";
		}

		return lCondizioni;
	}

	// MEV Agosto 2014 - Ricerca Procedimeto per Reato
	public String setCondizioneCountProcperReato(ReatoModel aRm, Boolean solocumulati) {
		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND REATO.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte()) + "'";
		}

		if (aRm.getAnnoFonte() != null && !aRm.getAnnoFonte().toString().equals("")) {
			lCondizioni += " AND REATO.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND REATO.NUMERO_FONTE = '" + StringUtils.convertSqlString(aRm.getNumeroFonte())
					+ "'";
		}

		if (!aRm.getCodSottonumerazione().equals("") && !aRm.getCodSottonumerazione().equals("-")) {
			lCondizioni += " AND REATO.COD_SOTTONUMERAZIONE = '"
					+ StringUtils.convertSqlString(aRm.getCodSottonumerazione()) + "'";
		}

		if (!aRm.getComma().equals("")) {
			lCondizioni += " AND REATO.COMMA = '" + StringUtils.convertSqlString(aRm.getComma()) + "'";
		}

		// ****************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		if (!aRm.getCommaQualificante().equals("") && !aRm.getCommaQualificante().equals("-")) {
			lCondizioni += " AND REATO.COMMA_QUALIFICANTE = '"
					+ StringUtils.convertSqlString(aRm.getCommaQualificante()) + "'";
		}
		// ****************************************************************************

		if (!aRm.getLettera().equals("")) {
			lCondizioni += " AND REATO.LETTERA = '" + StringUtils.convertSqlString(aRm.getLettera()) + "'";
		}

		if (!aRm.getNumero().equals("")) {
			lCondizioni += " AND REATO.NUMERO = '" + StringUtils.convertSqlString(aRm.getNumero()) + "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND REATO.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo()) + "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			// lCondizioni += " AND FASC.COD_UFFICIO_INSERIMENTO = '" +
			// StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
			lCondizioni += " AND FASC.CHIAVE_UFFICIO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		if (aRm.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND REATO.FAS_SIE_ID_FASCICOLO_SIEP = '" + aRm.getFasSieIdFascicoloSiep() + "'";
		}
		if (aRm.getProgrReato() != null) {
			lCondizioni += " AND REATO.PROGR_REATO = '" + aRm.getProgrReato() + "'";
		}

		lCondizioni += filtroDataReato(aRm.getDataInizio(), aRm.getDataFine());

		if ("I".equals(aRm.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA = '039' ";
		} else if ("S".equals(aRm.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA != '039' ";
		}
		//
		// Si possono ricrcare i Definiti, i NON Definiti, o Tutti
		if (aRm.getCodStatoFascicolo() != null && !aRm.getCodStatoFascicolo().equals("")) {
			if ("NA".equals(aRm.getCodStatoFascicolo())) {
				lCondizioni += " AND FASC.COD_STATO_FASCICOLO != '01' ";
			} else if ("AD".equals(aRm.getCodStatoFascicolo())) {
				lCondizioni += " AND FASC.COD_STATO_FASCICOLO = '01' ";
			}
		}
		//
		// Si possono ricrcare anche Solo i Cumulati
		if (solocumulati) {
			lCondizioni += " AND FASC.FLAG_CUMULANTE = 'S' ";
		}

		return lCondizioni;

	} // Chiude metodo setCondizioneCountProcperReato()

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_REATO = " + aKey;
	}

	public String setOrder() {
		return " ORDER BY PROGR_REATO, PROGR_CIRCOSTANZA";
	}

	public String setOrderReato() {
		return " ORDER BY PROGR_REATO ";
	}

	/**
	 * Metodo per ric ercareil massimo progrtessivo dei reati associati ad un fascicolo.
	 * 
	 * @param idFas
	 * @return
	 * @throws DAOException
	 */
	public int getMaxProgrReato(BigDecimal idFas) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_REATO) aMAX";
		lStatement += " FROM REATO";
		lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + idFas;

		setStatement(lStatement);

		this.start();

		int maxId = 0;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			maxId = this.getInt("aMAX");

		this.stop();

		return maxId;
	}

	///////////////// SIGE ////////////////////

	/**
	 * La funzione restituisce il massimo PROGR_REATO tra i reati in tabella legati allo stessa
	 * Sentenza-ProcedimentoSIGE. Funzione ricavata dalla getProgressivoReato.
	 * 
	 * @param aKey
	 * @return
	 * @throws DAOException
	 */

	public BigDecimal getProgressivoReatoSige(BigDecimal aKeyFasSigeSen) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_REATO) aMAX";
		lStatement += " FROM REATO JOIN REATO_SENTENZA_SIGE ON ID_REATO = REA_ID_REATO";
		lStatement += " WHERE FAS_SIGE_SEN_ID = " + aKeyFasSigeSen;

		setStatement(lStatement);

		start();

		BigDecimal lProgressivo = null;
		if (next() && (getBigDecimal("aMAX") != null))
			lProgressivo = getBigDecimal("aMAX");

		stop();

		if (lProgressivo == null)
			lProgressivo = new BigDecimal(0);

		return lProgressivo;
	}

	/**
	 * La funzione restituisce il massimo PROGR_CIRCOSTANZA tra i reati in tabella legati allo stessa
	 * Sentenza-ProcedimentoSIGE e con lo stesso PROGR_REATO. Funzione ricavata dalla
	 * getProgressivoCircostanza.
	 * 
	 * @param aProgReato
	 * @param aKeyFasSigeSen
	 * @return
	 * @throws DAOException
	 */

	public BigDecimal getProgressivoCircostanzaSige(BigDecimal aProgReato, BigDecimal aKeyFasSigeSen)
			throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_CIRCOSTANZA) aMAX";
		lStatement += " FROM REATO JOIN REATO_SENTENZA_SIGE ON ID_REATO = REA_ID_REATO";
		lStatement += " WHERE PROGR_REATO = " + aProgReato;
		lStatement += " AND FAS_SIGE_SEN_ID = " + aKeyFasSigeSen;

		setStatement(lStatement);

		this.start();

		BigDecimal lProgressivo = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgressivo = this.getBigDecimal("aMAX");

		this.stop();

		if (lProgressivo == null)
			lProgressivo = new BigDecimal(0);

		return lProgressivo;
	}

	/**
	 * Funzione ottenuta comme copia della ricercaReato(...). Questa per isomorfismo richiama una nuova
	 * funzione setCondizione(...).
	 * 
	 * @param aModel
	 *            : ReatoSentenzaSigeModel
	 * @throws DAOException
	 */
	public void ricercaReato(ReatoSentenzaSigeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	/**
	 * Condizione nel caso di ricerca Reati X SIGE
	 * 
	 * @param aModel
	 * @return
	 */

	public String setCondizione(ReatoSentenzaSigeModel aModel) {
		String lCondizioni = new String("");
		// boolean lInserito = false;
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
			// lInserito=true;
		}
		if (aModel.getProgrReato() != null) {
			lCondizioni += " AND PROGR_REATO = " + aModel.getProgrReato();
			// lInserito=true;
		}
		if (aModel.getProgrCircostanza() != null) {
			lCondizioni += " AND PROGR_CIRCOSTANZA = " + aModel.getProgrCircostanza();
			// lInserito=true;
		}

		if (aModel.getFasSigeSenId() != null) {
			lCondizioni += " AND ID_REATO IN (SELECT REA_ID_REATO FROM REATO_SENTENZA_SIGE WHERE FAS_SIGE_SEN_ID = "
					+ aModel.getFasSigeSenId() + ")";
			// lInserito=true;
		}

		return lCondizioni;
	}

	// Da utilizzare solo se dobbiamo girare i Reati x NSC
	public void RicercaReatiByFascicoloSiepOnlyNSC(long aFascicoloSIEP) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicoloSIEP;
		// lSql += " AND COD_FONTE NOT IN ('10', '26', '23', '12', '24', '25', '14', '09', '17', '19')";
		// lSql += " AND COD_PERIODO_CONSUMAZIONE NOT IN ('05', '06', '12')";
		lSql += " ORDER BY PROGR_REATO ASC, PROGR_CIRCOSTANZA ASC";

		setStatement(lSql);

	}

	public void ContaRecordPerReato(long aFascicoloSIEP, int aProgrReato) throws DAOException {

		String lSql = "SELECT COUNT(*) as ContaRecord FROM REATO ";

		lSql += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicoloSIEP;
		lSql += " AND PROGR_REATO=" + aProgrReato;

		setStatement(lSql);

	}

	public int getRisultatoContaRecordPerReato() throws DAOException {
		int lContaRecord = this.getInt("ContaRecord");
		return lContaRecord;
	}

	// AMBROSINO 04/2011

	public void RicercaReatiNoCircostanzaByFascicoloeProgr(BigDecimal aKey, String NRea) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND PROGR_REATO = " + NRea;
		lSql += " " + setOrder();

		setStatement(lSql);
	}

	// MEV 26 CUMULO - Ricerca Procedimeto per Reato e reato_Cumulo
	public String setCondizioneCountProcperReato_Cumulo(ReatoModel aRm, Boolean solocumulati) {
		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND RCUM.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte()) + "'";
		}

		if (aRm.getAnnoFonte() != null && !aRm.getAnnoFonte().toString().equals("")) {
			lCondizioni += " AND RCUM.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND RCUM.NUMERO_FONTE = '" + StringUtils.convertSqlString(aRm.getNumeroFonte())
					+ "'";
		}

		if (!aRm.getCodSottonumerazione().equals("") && !aRm.getCodSottonumerazione().equals("-")) {
			lCondizioni += " AND RCUM.COD_SOTTONUMERAZIONE = '"
					+ StringUtils.convertSqlString(aRm.getCodSottonumerazione()) + "'";
		}

		if (!aRm.getComma().equals("")) {
			lCondizioni += " AND RCUM.COMMA = '" + StringUtils.convertSqlString(aRm.getComma()) + "'";
		}

		if (!aRm.getCommaQualificante().equals("") && !aRm.getCommaQualificante().equals("-")) {
			lCondizioni += " AND RCUM.COMMA_QUALIFICANTE = '"
					+ StringUtils.convertSqlString(aRm.getCommaQualificante()) + "'";
		}

		if (!aRm.getLettera().equals("")) {
			lCondizioni += " AND RCUM.LETTERA = '" + StringUtils.convertSqlString(aRm.getLettera()) + "'";
		}

		if (!aRm.getNumero().equals("")) {
			lCondizioni += " AND RCUM.NUMERO = '" + StringUtils.convertSqlString(aRm.getNumero()) + "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND RCUM.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo()) + "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FAS.CHIAVE_UFFICIO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		// if (aRm.getFasSieIdFascicoloSiep() != null) {
		// lCondizioni += " AND REATO.FAS_SIE_ID_FASCICOLO_SIEP = '" + aRm.getFasSieIdFascicoloSiep() + "'";
		// }

		if (aRm.getProgrReato() != null) {
			lCondizioni += " AND RCUM.PROGR_REATO = '" + aRm.getProgrReato() + "'";
		}

		lCondizioni += filtroDataReato_Cumulo(aRm.getDataInizio(), aRm.getDataFine());

		if ("I".equals(aRm.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA = '039' ";
		} else if ("S".equals(aRm.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA != '039' ";
		}
		//
		// Si possono ricrcare i Definiti, i NON Definiti, o Tutti
		if (aRm.getCodStatoFascicolo() != null && !aRm.getCodStatoFascicolo().equals("")) {
			if ("NA".equals(aRm.getCodStatoFascicolo())) {
				lCondizioni += " AND FAS.COD_STATO_FASCICOLO != '01' ";
			} else if ("AD".equals(aRm.getCodStatoFascicolo())) {
				lCondizioni += " AND FAS.COD_STATO_FASCICOLO = '01' ";
			}
		}
		//
		// Si possono ricrcare anche Solo i Cumulati
		if (solocumulati) {
			lCondizioni += " AND FAS.FLAG_CUMULANTE = 'S' ";
		}

		return lCondizioni;

	} // Chiude metodo setCondizioneCountProcperReato_Cumulo()

	private String filtroDataReato_Cumulo(Date inizio, Date fine) {
		String ret = "";

		if (inizio != null || fine != null) {
			String cond = "";
			if (inizio == null) {
				// è stata inserita solo la data fine
				cond = "<= TO_DATE('" + DateUtils.getDateToString(fine, "ddMMyyyy") + "', 'DDMMYYYY')";
			} else if (fine == null) {
				// è stata inserita solo la data inizio
				cond = ">= TO_DATE('" + DateUtils.getDateToString(inizio, "ddMMyyyy") + "', 'DDMMYYYY')";
			} else {
				// sono state valorizzate entrambe
				cond = "BETWEEN TO_DATE('" + DateUtils.getDateToString(inizio, "ddMMyyyy")
						+ "', 'DDMMYYYY') AND TO_DATE('" + DateUtils.getDateToString(fine, "ddMMyyyy")
						+ "', 'DDMMYYYY')";
			}

			StringBuffer sb = new StringBuffer();
			sb.append(" and ( ");
			sb.append(" ((RCUM.DATA_INIZIO is not null) and (RCUM.DATA_INIZIO " + cond + " )) ");
			sb.append(
					" or ((RCUM.DATA_INIZIO is null) and (RCUM.ANNO_INIZIO is not null) and (RCUM.MESE_INIZIO is not null) and (RCUM.MESE_INIZIO <10 ) and (TO_DATE('0'||RCUM.MESE_INIZIO||RCUM.ANNO_INIZIO, 'MMYYYY') "
							+ cond + " )) ");
			sb.append(
					" or ((RCUM.DATA_INIZIO is null) and (RCUM.ANNO_INIZIO is not null) and (RCUM.MESE_INIZIO is not null) and (RCUM.MESE_INIZIO >=10 ) and (TO_DATE(RCUM.MESE_INIZIO||RCUM.ANNO_INIZIO, 'MMYYYY') "
							+ cond + " )) ");
			sb.append(
					" or ((RCUM.DATA_INIZIO is null) and (RCUM.ANNO_INIZIO is not null) and (RCUM.MESE_INIZIO is null) and (TO_DATE(RCUM.ANNO_INIZIO, 'YYYY') "
							+ cond + " )) ");
			sb.append(" ) ");
			ret = sb.toString();
		}

		return ret;

	} // Chiude filtroDataReato_Cumulo()

	public String setCondizioneDataMaxEventoCumulo() {
		String lStringa = new String();

		lStringa += " AND EVENTO.DATA_INSERIMENTO =";
		lStringa += " (SELECT max (EVDMAX.DATA_INSERIMENTO)";
		lStringa += " FROM EVENTO EVDMAX, ISTRUTTORIA_CUMULO ISTR_C";
		lStringa += " WHERE 1=1";
		lStringa += " AND EVDMAX.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStringa += " AND EVDMAX.FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lStringa += " AND ISTR_C.EVE_ID_EVENTO_PROV = EVDMAX.ID_EVENTO";
		lStringa += " )";

		return lStringa;

	} // Chiude setCondizioneDataMaxEventoCumulo()

}

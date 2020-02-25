package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import siap.dao.SIAPSqlDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.reato.model.ReatoModel;
import siap.sige.circostanza.model.CircostanzaSigeModel;

/**
 * <p>
 * Title: CircostanzaCumuloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Circostanza_Cumulo
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
public class CircostanzaCumuloSqlDAO extends SIAPSqlDAO {

	public CircostanzaCumuloSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaCircostanza(CircostanzaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setCondizioniDataInserimentoDesc();

		setStatement(lSql);
	}

	public void ricercaCircostanzaCumuloByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void ricercaCircostanzeByIdFascicoloByArtByCodFonte(BigDecimal aIdFascicolo, String aArticolo,
			String aCodFonte) throws DAOException {
		String lSql = getSqlQuery();

		// lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);
		lSql += " " + setCondizioniByArticoloCodFonte(aArticolo, aCodFonte);

		setStatement(lSql);
	}

	public void ricercaCircostanzeCumuloByTitolo(BigDecimal aIdKeyTito) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByTitolo(aIdKeyTito);
		lSql += " " + setCondizioniDataInserimentoDesc();

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + "ID_CIRCOSTANZA_CUMULO, " + "COD_FONTE, " + "ANNO_FONTE, "
				+ "NUMERO_FONTE, " + "COD_SOTTONUMERAZIONE, " + "COMMA, COMMA_QUALIFICANTE, " + "LETTERA, "
				+ "NUMERO, " + "ARTICOLO, " + "NOTE, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "FLAG_SENTENZA_APPLICAZ_PENA, "
				+ "FLAG_GIUDIZIO_ABBREVIATO, " + "COD_BILANCIAMENTO_CIRCOSTANZE, " + "NOTE_BILANCIAMENTO, "
				+ "DECOBILAN.RV_MEANING DESCBILAN, "
				+ "ID_CIRCOSTANZA_ORIGINE, FLAG_STATO, MOTIVO_MODIFICA, TIT_ID_TITOLO_CUMULATO, ";

		lStatement += "DECOFONTE.RV_MEANING DESCFONTE, ";
		lStatement += "DECOSOTTONUM.RV_MEANING DESCSOTTONUM, ";
		lStatement += "DECOCOMMAQUAL.RV_MEANING DESCCOMMAQUALIFICANTE ";

		lStatement += "FROM CIRCOSTANZA_CUMULO, CG_REF_CODES DECOSOTTONUM, CG_REF_CODES DECOFONTE, ";
		lStatement += "CG_REF_CODES DECOBILAN, CG_REF_CODES DECOCOMMAQUAL ";
		lStatement += "WHERE ";

		lStatement += "DECOSOTTONUM.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOSOTTONUM.RV_LOW_VALUE=COD_SOTTONUMERAZIONE ";
		lStatement += "AND DECOFONTE.RV_DOMAIN='FONTE' AND DECOFONTE.RV_LOW_VALUE=COD_FONTE ";
		lStatement += "AND (DECOBILAN.RV_DOMAIN='BILANCIAMENTO_CIRCOSTANZE' AND DECOBILAN.RV_LOW_VALUE= NVL(COD_BILANCIAMENTO_CIRCOSTANZE,'-') ) ";
		lStatement += "AND DECOCOMMAQUAL.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOCOMMAQUAL.RV_LOW_VALUE=COMMA_QUALIFICANTE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		CircostanzaCumuloModel aModel = new CircostanzaCumuloModel();

		aModel.setIdCircostanzaCumulo(getBigDecimal("ID_CIRCOSTANZA_CUMULO"));
		// aModel.setCodTipoCircostanza (getString("COD_TIPO_CIRCOSTANZA") );
		aModel.setCodFonte(getString("COD_FONTE"));
		aModel.setDescrFonte(getString("DESCFONTE"));
		aModel.setAnnoFonte(getBigDecimal("ANNO_FONTE"));
		aModel.setNumeroFonte(getString("NUMERO_FONTE"));
		aModel.setArticolo(getString("ARTICOLO"));
		aModel.setCodSottonumerazione(getString("COD_SOTTONUMERAZIONE"));
		aModel.setDescrSottonumerazione(getString("DESCSOTTONUM"));
		aModel.setComma(getString("COMMA"));
		aModel.setCommaQualificante(getString("COMMA_QUALIFICANTE"));
		aModel.setDescrCommaQualificante(getString("DESCCOMMAQUALIFICANTE"));
		aModel.setLettera(getString("LETTERA"));
		aModel.setNumero(getString("NUMERO"));

		aModel.setNote(getString("NOTE"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		aModel.setFlagSentenzaApplicazPena(getString("FLAG_SENTENZA_APPLICAZ_PENA"));
		aModel.setCodBilanciamentoCircostanze(getString("COD_BILANCIAMENTO_CIRCOSTANZE"));
		aModel.setDescrBilanciamentoCircostanze(getString("DESCBILAN"));
		aModel.setFlagGiudizioAbbreviato(getString("FLAG_GIUDIZIO_ABBREVIATO"));
		aModel.setNoteBilanciamento(getString("NOTE_BILANCIAMENTO"));

		aModel.setIdCircostanzaOrigine(getBigDecimal("ID_CIRCOSTANZA_ORIGINE"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setMotivoModifica(getString("MOTIVO_MODIFICA"));
		aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO"));

		return aModel;
	}

	public String setCondizione(CircostanzaModel aModel) {
		String lCondizioni = new String("");

		// boolean lInserito = false;
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
			// lInserito = true;
		}
		// Pena Accessoria SIGE si ricerca attraverso la tabella di relazione PENA_ACCESSORIA_SENTENZA_SIGE
		if (aModel instanceof siap.sige.circostanza.model.CircostanzaSigeModel) {
			lCondizioni = " AND ID_CIRCOSTANZA IN (SELECT CIR_ID_CIRCOSTANZA FROM CIRCOSTANZA_SENTENZA_SIGE WHERE FAS_SIGE_SEN_ID = "
					+ ((CircostanzaSigeModel) aModel).getFasSigeSenId() + ")";
			// lInserito = true;
		}

		return lCondizioni;
	}

	public String setCondizioniByTitolo(BigDecimal aIdTitolo) {
		return " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
	}

	public String setCondizioniByArticoloCodFonte(String aArt, String aCodFonte) {
		return " AND COD_FONTE = " + aCodFonte + " AND ARTICOLO = " + aArt;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_CIRCOSTANZA_CUMULO = " + aKey;

	}

	public String setCondizioniDataInserimentoDesc() {
		return " ORDER BY DATA_INSERIMENTO DESC ";
	}

	// MEV Agosto 2014 - Ricerca Procedimeto per Reato e Circostanze Aggravanti
	public void getCountCircostanze(CircostanzaModel aModel, ReatoModel aRea, Boolean solocumulati)
			throws DAOException {
		String lStatement = "SELECT COUNT (a.ID_FASCICOLO_SIEP) howmanyrecords ";
		lStatement += "  FROM (SELECT DISTINCT ID_FASCICOLO_SIEP FROM FASCICOLO_SIEP FASC,CIRCOSTANZA,SOGGETTO SOGG,UFFICIO UFF,";
		// lStatement += " CG_REF_CODES DECOBILAN, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF";
		lStatement += " CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIEP = CIRCOSTANZA.FAS_SIE_ID_FASCICOLO_SIEP";

		lStatement += "  AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO  ";
		lStatement += "  AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += "  AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "  AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE ";
		lStatement += "  AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";

		lStatement += setCondizioneCountProcperReatoCirco(aModel, aRea, solocumulati);
		lStatement += " )a";

		setStatement(lStatement);
	}

	public void getCountReaCircos(CircostanzaModel aModel, ReatoModel aRea, Boolean solocumulati)
			throws DAOException {
		String lStatement = "SELECT COUNT (a.ID_FASCICOLO_SIEP) howmanyrecords ";

		lStatement += "  FROM (SELECT DISTINCT ID_FASCICOLO_SIEP FROM FASCICOLO_SIEP FASC,REATO,CIRCOSTANZA,";
		lStatement += " 	SOGGETTO SOGG,UFFICIO UFF,CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF";

		lStatement += " WHERE FASC.ID_FASCICOLO_SIEP = REATO.FAS_SIE_ID_FASCICOLO_SIEP";
		lStatement += "  AND FASC.ID_FASCICOLO_SIEP = CIRCOSTANZA.FAS_SIE_ID_FASCICOLO_SIEP";
		lStatement += "  AND  FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO  ";
		lStatement += "  AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += "  AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "  AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE ";
		lStatement += "  AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";

		lStatement += setCondizioneCountProcperReato(aRea, solocumulati);
		lStatement += setCondizioneCircoAggr(aModel);
		lStatement += " )a";

		setStatement(lStatement);
	}

	//
	public String setCondizioneCountProcperReatoCirco(CircostanzaModel aRm, ReatoModel aRea,
			Boolean solocumulati) {
		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte())
					+ "'";
		}

		if (aRm.getAnnoFonte() != null && !"".equals(aRm.getAnnoFonte().toString())) {
			lCondizioni += " AND CIRCOSTANZA.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.NUMERO_FONTE = '"
					+ StringUtils.convertSqlString(aRm.getNumeroFonte()) + "'";
		}

		if (!aRm.getCodSottonumerazione().equals("") && !aRm.getCodSottonumerazione().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_SOTTONUMERAZIONE = '"
					+ StringUtils.convertSqlString(aRm.getCodSottonumerazione()) + "'";
		}

		if (!aRm.getComma().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.COMMA = '" + StringUtils.convertSqlString(aRm.getComma()) + "'";
		}

		if (!aRm.getCommaQualificante().equals("") && !aRm.getCommaQualificante().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COMMA_QUALIFICANTE = '"
					+ StringUtils.convertSqlString(aRm.getCommaQualificante()) + "'";
		}

		if (!aRm.getLettera().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.LETTERA = '" + StringUtils.convertSqlString(aRm.getLettera())
					+ "'";
		}

		if (!aRm.getNumero().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.NUMERO = '" + StringUtils.convertSqlString(aRm.getNumero())
					+ "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo())
					+ "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FASC.CHIAVE_UFFICIO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		if (aRm.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND CIRCOSTANZA.FAS_SIE_ID_FASCICOLO_SIEP = '" + aRm.getFasSieIdFascicoloSiep()
					+ "'";
		}

		if (!aRm.getCodBilanciamentoCircostanze().equals("")
				&& !aRm.getCodBilanciamentoCircostanze().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_BILANCIAMENTO_CIRCOSTANZE = '"
					+ StringUtils.convertSqlString(aRm.getCodBilanciamentoCircostanze()) + "'";
		}

		// lCondizioni += filtroDataReato(aRm.getDataInizio(), aRm.getDataFine());

		if ("I".equals(aRea.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA = '039' ";
		} else if ("S".equals(aRea.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA != '039' ";
		}
		//
		// Si possono ricrcare i Definiti, i NON Definiti, o Tutti
		if (aRea.getCodStatoFascicolo() != null && !aRea.getCodStatoFascicolo().equals("")) {
			if ("NA".equals(aRea.getCodStatoFascicolo())) {
				lCondizioni += " AND FASC.COD_STATO_FASCICOLO != '01' ";
			} else if ("AD".equals(aRea.getCodStatoFascicolo())) {
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

	// MEV Agosto 2014 - Ricerca Procedimeto per Reato
	public String setCondizioneCountProcperReato(ReatoModel aRm, Boolean solocumulati) {
		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND REATO.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte()) + "'";
		}

		if (aRm.getAnnoFonte() != null && !"".equals(aRm.getAnnoFonte().toString())) {
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

		if (!aRm.getCommaQualificante().equals("") && !aRm.getCommaQualificante().equals("-")) {
			lCondizioni += " AND REATO.COMMA_QUALIFICANTE = '"
					+ StringUtils.convertSqlString(aRm.getCommaQualificante()) + "'";
		}

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

	public String setCondizioneCircoAggr(CircostanzaModel aRm) {
		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte())
					+ "'";
		}

		if (aRm.getAnnoFonte() != null && !"".equals(aRm.getAnnoFonte().toString())) {
			lCondizioni += " AND CIRCOSTANZA.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.NUMERO_FONTE = '"
					+ StringUtils.convertSqlString(aRm.getNumeroFonte()) + "'";
		}

		if (!aRm.getCodSottonumerazione().equals("") && !aRm.getCodSottonumerazione().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_SOTTONUMERAZIONE = '"
					+ StringUtils.convertSqlString(aRm.getCodSottonumerazione()) + "'";
		}

		if (!aRm.getComma().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.COMMA = '" + StringUtils.convertSqlString(aRm.getComma()) + "'";
		}

		if (!aRm.getCommaQualificante().equals("") && !aRm.getCommaQualificante().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COMMA_QUALIFICANTE = '"
					+ StringUtils.convertSqlString(aRm.getCommaQualificante()) + "'";
		}

		if (!aRm.getLettera().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.LETTERA = '" + StringUtils.convertSqlString(aRm.getLettera())
					+ "'";
		}

		if (!aRm.getNumero().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.NUMERO = '" + StringUtils.convertSqlString(aRm.getNumero())
					+ "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo())
					+ "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FASC.CHIAVE_UFFICIO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		if (aRm.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND CIRCOSTANZA.FAS_SIE_ID_FASCICOLO_SIEP = '" + aRm.getFasSieIdFascicoloSiep()
					+ "'";
		}

		if (!aRm.getCodBilanciamentoCircostanze().equals("")
				&& !aRm.getCodBilanciamentoCircostanze().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_BILANCIAMENTO_CIRCOSTANZE = '"
					+ StringUtils.convertSqlString(aRm.getCodBilanciamentoCircostanze()) + "'";
		}
		return lCondizioni;
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

} // CHIUDE DAO
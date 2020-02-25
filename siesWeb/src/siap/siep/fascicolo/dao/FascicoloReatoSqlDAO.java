package siap.siep.fascicolo.dao;

/**
 * <p>Title: FascicoloReatoSqlDAO</p>
 * <p>Description: Realizza Sql DAO della ricerca di Fascicolo Siep per reato
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.model.ReatoModel;

public class FascicoloReatoSqlDAO extends SIAPSqlDAO {

	public FascicoloReatoSqlDAO(Connection aCon) {
		super(aCon);
	}

	// MEV Agosto 2014 - Ricerca Procedimenti By Reato - Aggiunto criterio di Ricerca - Cumulati
	public void ricercaFascicoloReatoPaged(ReatoModel aModel, Boolean solocumulati, int aPage) {
		String reatoFascicolo = "";
		String lPaginedStatement = new String("");

		reatoFascicolo += getFascicoloSqlReatoQuery();
		reatoFascicolo += setCondizione(aModel, solocumulati);
		// MEV 26 Cumulo - Ricerca Procedimenti By Reato - Aggiungo ricerca su REATO_CUMULO
		reatoFascicolo += " UNION ";
		reatoFascicolo += getFascicoloSqlReato_Cumulo_Query();
		reatoFascicolo += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP ";
		reatoFascicolo += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		reatoFascicolo += " AND ISTRU.EVE_ID_EVENTO_PROV = EVENTO.ID_EVENTO ";
		reatoFascicolo += " AND TITCUM.ISTR_ID_ISTRUTTORIA_CUMULO = ISTRU.ID_ISTRUTTORIA_CUMULO";
		reatoFascicolo += " AND RCUM.TIT_ID_TITOLO_CUMULATO = TITCUM.ID_TITOLO_CUMULATO";
		reatoFascicolo += setCondizioneReato_Cumulo(aModel, solocumulati);

		// Ricerca dell'ultimo provvedimento di Cumulo Valido
		reatoFascicolo += setCondizioneDataMaxEventoCumulo();
		// END MEV 26
		reatoFascicolo += setOrder();

		// MEV 17
		// Ricerca paginata
		// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0
		if (aPage > 0) {
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + reatoFascicolo
					+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		} else {
			lPaginedStatement = reatoFascicolo;
		}
		// settaggio della stringa SQL appena costruita prima della query
		setStatement(lPaginedStatement);
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

	/**
	 * Settaggio della condizione sul reato
	 *
	 * @param aRm
	 * @return
	 */
	// MEV Agosto 2014 - Ricerca Procedimenti By Reato - Aggiunto criterio di Ricerca - Cumulati
	private String setCondizione(ReatoModel aRm, Boolean solocumulati) {

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

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND REATO.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo()) + "'";
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

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FASC.CHIAVE_UFFICIO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		lCondizioni += filtroDataReato(aRm.getDataInizio(), aRm.getDataFine());

		if ("I".equals(aRm.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA = '039' ";
		} else if ("S".equals(aRm.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA != '039' ";
		}

		// MEV Agosto 2014 - Ricerca Fascicoli By Reato -
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
	} // Chiude metodo setCondizione()

	protected String getFascicoloSqlReatoQuery() {
		String lStatement = new String();

		lStatement += " SELECT DISTINCT FASC.ID_FASCICOLO_SIEP,SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, DESCR_SOGGETTO.RV_MEANING NAZIONE,";

		// MEV Agosto 2014 - Ricerca fascicolo by reato - Aggiunti criteri di Ricerca: Cumulati
		// lStatement += " FASC.CHIAVE_ANNO, ";
		// lStatement += " FASC.CHIAVE_PROGR , FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING
		// DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";
		lStatement += " FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR , FASC.CHIAVE_UFFICIO, FASC.FLAG_CUMULANTE,";
		lStatement += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";
		//
		lStatement += " FROM FASCICOLO_SIEP FASC,SOGGETTO SOGG, ";
		lStatement += " UFFICIO UFF, REATO, ";
		lStatement += " CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES DESCR_SOGGETTO, COMUNE DESCR_COM_UFF";

		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO AND REATO.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";

		lStatement += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE";

		lStatement += " AND DESCR_SOGGETTO.RV_DOMAIN = 'NAZIONE' ";
		lStatement += " AND SOGG.COD_STATO_NASCITA = DESCR_SOGGETTO.RV_LOW_VALUE";

		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		return lStatement;
	}

	private String setOrder() {

		// MEV Agosto 2014 - Cambia ordinamento: CHIAVE_PROG diventa Crescente

		String lOrder = new String();
		// lOrder = " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR DESC";
		lOrder = " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR";

		return lOrder;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setFlagCumulante(getString("FLAG_CUMULANTE"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDescrNazionalita(getString("NAZIONE"));

		lFascicolo.setSoggetto(lSoggetto);

		return lFascicolo;
	}

	// MEV Agosto 2014 - Ricerca Procedimenti By Reato e Circostanze Aggravanti
	public void ricercaFascicoloCircAggravaPaged(ReatoModel aModel, CircostanzaModel aModAggrava,
			Boolean solocumulati, int aPage) {
		String CircoAggFascicolo = "";
		String lPaginedStatement = new String("");

		CircoAggFascicolo += getFascicoloSqlCircoQuery();
		CircoAggFascicolo += setCondizioneCirco(aModel, aModAggrava, solocumulati);

		// MEV 26 Cumulo - Ricerca Procedimenti By Circostanze Aggravanti - Aggiungo ricerca su
		// CIRCOSTANZA_CUMULO
		CircoAggFascicolo += " UNION ";
		CircoAggFascicolo += getFascicoloSqlCirco_Cumulo_Query();
		CircoAggFascicolo += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP ";
		CircoAggFascicolo += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		CircoAggFascicolo += " AND ISTRU.EVE_ID_EVENTO_PROV = EVENTO.ID_EVENTO ";
		CircoAggFascicolo += " AND TITCUM.ISTR_ID_ISTRUTTORIA_CUMULO = ISTRU.ID_ISTRUTTORIA_CUMULO";
		CircoAggFascicolo += " AND CIRCUM.TIT_ID_TITOLO_CUMULATO = TITCUM.ID_TITOLO_CUMULATO";
		CircoAggFascicolo += setCondizioneCirco_Cumulo(aModAggrava);
		CircoAggFascicolo += setRestantiCondizioni(aModel, solocumulati);

		// Ricerca dell'ultimo provvedimento di Cumulo Valido
		CircoAggFascicolo += setCondizioneDataMaxEventoCumulo();

		// END MEV 26

		CircoAggFascicolo += setOrder();

		if (aPage > 0) {
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + CircoAggFascicolo
					+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		} else {
			lPaginedStatement = CircoAggFascicolo;
		}

		setStatement(lPaginedStatement);
	}

	protected String getFascicoloSqlCircoQuery() {
		String lStatement = new String();

		lStatement += " SELECT DISTINCT FASC.ID_FASCICOLO_SIEP,SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, DESCR_SOGGETTO.RV_MEANING NAZIONE,";
		lStatement += " FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR , FASC.CHIAVE_UFFICIO, FASC.FLAG_CUMULANTE,";
		lStatement += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";
		//
		lStatement += " FROM FASCICOLO_SIEP FASC,SOGGETTO SOGG, ";
		lStatement += " UFFICIO UFF, CIRCOSTANZA, ";
		lStatement += " CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES DESCR_SOGGETTO, COMUNE DESCR_COM_UFF";

		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO AND CIRCOSTANZA.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";

		lStatement += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE";

		lStatement += " AND DESCR_SOGGETTO.RV_DOMAIN = 'NAZIONE' ";
		lStatement += " AND SOGG.COD_STATO_NASCITA = DESCR_SOGGETTO.RV_LOW_VALUE";

		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		return lStatement;
	}

	private String setCondizioneCirco(ReatoModel aRea, CircostanzaModel aRm, Boolean solocumulati) {

		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte())
					+ "'";
		}

		if (aRm.getAnnoFonte() != null && !aRm.getAnnoFonte().toString().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.NUMERO_FONTE = '"
					+ StringUtils.convertSqlString(aRm.getNumeroFonte()) + "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo())
					+ "'";
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

		if (!aRm.getCodBilanciamentoCircostanze().equals("")
				&& !aRm.getCodBilanciamentoCircostanze().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_BILANCIAMENTO_CIRCOSTANZE = '"
					+ StringUtils.convertSqlString(aRm.getCodBilanciamentoCircostanze()) + "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FASC.CHIAVE_UFFICIO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		// lCondizioni += filtroDataReato(aRm.getDataInizio(), aRm.getDataFine());

		if ("I".equals(aRea.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA = '039' ";
		} else if ("S".equals(aRea.getNazionalita())) {
			lCondizioni += " AND SOGG.COD_STATO_NASCITA != '039' ";
		}

		// MEV Agosto 2014 - Ricerca Fascicoli By Reato -
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
	} // Chiude metodo setCondizioneCirco()
		//
		// ================
		//

	public void ricercaFascicoloReatoCircostanzeAggravaPaged(ReatoModel aModel, CircostanzaModel aModAggrava,
			Boolean solocumulati, int aPage) {
		String ReaCircoAggFascicolo = "";
		String lPaginedStatement = new String("");

		ReaCircoAggFascicolo += getFascicoloSqlReatoCircoQuery();
		ReaCircoAggFascicolo += setCondizione(aModel, solocumulati);
		ReaCircoAggFascicolo += setCondizioneCircostanza(aModAggrava);

		// MEV 26 Cumulo - Ricerca Procedimenti By Reato e Circostanze Aggravanti - Aggiungo ricerca su
		// REATO_CUMULO e CIRCOSTANZA_CUMULO
		ReaCircoAggFascicolo += " UNION ";
		ReaCircoAggFascicolo += getFascicoloSqlReatoCirco_Cumulo_Query();
		ReaCircoAggFascicolo += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP ";
		ReaCircoAggFascicolo += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		ReaCircoAggFascicolo += " AND ISTRU.EVE_ID_EVENTO_PROV = EVENTO.ID_EVENTO ";
		ReaCircoAggFascicolo += " AND TITCUM.ISTR_ID_ISTRUTTORIA_CUMULO = ISTRU.ID_ISTRUTTORIA_CUMULO";

		ReaCircoAggFascicolo += " AND RCUM.TIT_ID_TITOLO_CUMULATO = TITCUM.ID_TITOLO_CUMULATO";
		ReaCircoAggFascicolo += " AND CIRCUM.TIT_ID_TITOLO_CUMULATO = TITCUM.ID_TITOLO_CUMULATO";

		ReaCircoAggFascicolo += setCondizioneReato_Cumulo(aModel, solocumulati);
		ReaCircoAggFascicolo += setCondizioneCirco_Cumulo(aModAggrava);

		// Ricerca dell'ultimo provvedimento di Cumulo Valido
		ReaCircoAggFascicolo += setCondizioneDataMaxEventoCumulo();
		// END MEV 26

		ReaCircoAggFascicolo += setOrder();

		if (aPage > 0) {
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + ReaCircoAggFascicolo
					+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		} else {
			lPaginedStatement = ReaCircoAggFascicolo;
		}

		setStatement(lPaginedStatement);
	}

	protected String getFascicoloSqlReatoCircoQuery() {
		String lStatement = new String();

		lStatement += " SELECT DISTINCT FASC.ID_FASCICOLO_SIEP,SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, DESCR_SOGGETTO.RV_MEANING NAZIONE,";
		lStatement += " FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR , FASC.CHIAVE_UFFICIO, FASC.FLAG_CUMULANTE,";
		lStatement += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";
		//
		lStatement += " FROM FASCICOLO_SIEP FASC,SOGGETTO SOGG, ";
		lStatement += " UFFICIO UFF, REATO, CIRCOSTANZA,";
		lStatement += " CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES DESCR_SOGGETTO, COMUNE DESCR_COM_UFF";

		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO AND REATO.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP";
		lStatement += " AND CIRCOSTANZA.FAS_SIE_ID_FASCICOLO_SIEP = FASC.ID_FASCICOLO_SIEP";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";

		lStatement += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE";

		lStatement += " AND DESCR_SOGGETTO.RV_DOMAIN = 'NAZIONE' ";
		lStatement += " AND SOGG.COD_STATO_NASCITA = DESCR_SOGGETTO.RV_LOW_VALUE";

		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		return lStatement;
	}

	private String setCondizioneCircostanza(CircostanzaModel aRm) {

		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte())
					+ "'";
		}

		if (aRm.getAnnoFonte() != null && !aRm.getAnnoFonte().toString().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.NUMERO_FONTE = '"
					+ StringUtils.convertSqlString(aRm.getNumeroFonte()) + "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND CIRCOSTANZA.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo())
					+ "'";
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

		if (!aRm.getCodBilanciamentoCircostanze().equals("")
				&& !aRm.getCodBilanciamentoCircostanze().equals("-")) {
			lCondizioni += " AND CIRCOSTANZA.COD_BILANCIAMENTO_CIRCOSTANZE = '"
					+ StringUtils.convertSqlString(aRm.getCodBilanciamentoCircostanze()) + "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FASC.CHIAVE_UFFICIO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		return lCondizioni;
	} // Chiude metodo setCondizioneCircostanza()

	// MEV 26 CUMULO - Aggiungo REATO_CUMULO alla ricerca REATO
	protected String getFascicoloSqlReato_Cumulo_Query() {
		String lStatement = new String();

		lStatement += " SELECT DISTINCT FAS.ID_FASCICOLO_SIEP, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, DESCR_SOGGETTO.RV_MEANING NAZIONE,";

		lStatement += " FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR , FAS.CHIAVE_UFFICIO, FAS.FLAG_CUMULANTE,";
		lStatement += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";

		lStatement += " FROM FASCICOLO_SIEP FAS, SOGGETTO SOGG,";
		lStatement += "  UFFICIO UFF, REATO_CUMULO RCUM, TITOLO_CUMULATO TITCUM, ISTRUTTORIA_CUMULO ISTRU, EVENTO,";
		lStatement += "  CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES DESCR_SOGGETTO, COMUNE DESCR_COM_UFF";

		lStatement += " WHERE FAS.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND UFF.COD_UFFICIO = FAS.CHIAVE_UFFICIO";

		lStatement += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE";

		lStatement += " AND DESCR_SOGGETTO.RV_DOMAIN = 'NAZIONE' ";
		lStatement += " AND SOGG.COD_STATO_NASCITA = DESCR_SOGGETTO.RV_LOW_VALUE";

		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		return lStatement;

	} // Chiude metodo getFascicoloSqlReato_Cumulo_Query()

	public String setCondizioneReato_Cumulo(ReatoModel aRm, Boolean solocumulati) {
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

	} // Chiude metodo setCondizioneReato_Cumulo()

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

	protected String getFascicoloSqlCirco_Cumulo_Query() {
		String lStatement = new String();

		lStatement += " SELECT DISTINCT FAS.ID_FASCICOLO_SIEP, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, DESCR_SOGGETTO.RV_MEANING NAZIONE,";
		lStatement += " FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR , FAS.CHIAVE_UFFICIO, FAS.FLAG_CUMULANTE,";
		lStatement += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";
		//
		lStatement += " FROM FASCICOLO_SIEP FAS, SOGGETTO SOGG, ";
		lStatement += " UFFICIO UFF, CIRCOSTANZA_CUMULO CIRCUM, TITOLO_CUMULATO TITCUM, ISTRUTTORIA_CUMULO ISTRU, EVENTO,";
		lStatement += " CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES DESCR_SOGGETTO, COMUNE DESCR_COM_UFF";

		lStatement += " WHERE FAS.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND UFF.COD_UFFICIO = FAS.CHIAVE_UFFICIO";

		lStatement += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE";

		lStatement += " AND DESCR_SOGGETTO.RV_DOMAIN = 'NAZIONE' ";
		lStatement += " AND SOGG.COD_STATO_NASCITA = DESCR_SOGGETTO.RV_LOW_VALUE";

		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		return lStatement;

	} // Chiude getFascicoloSqlCirco_Cumulo_Query

	public String setCondizioneCirco_Cumulo(CircostanzaModel aRm) {
		String lCondizioni = new String();

		if (!aRm.getCodFonte().equals("") && !aRm.getCodFonte().equals("-")) {
			lCondizioni += " AND CIRCUM.COD_FONTE = '" + StringUtils.convertSqlString(aRm.getCodFonte())
					+ "'";
		}

		if (aRm.getAnnoFonte() != null && !aRm.getAnnoFonte().toString().equals("")) {
			lCondizioni += " AND CIRCUM.ANNO_FONTE = '" + aRm.getAnnoFonte() + "'";
		}

		if (!aRm.getNumeroFonte().equals("")) {
			lCondizioni += " AND CIRCUM.NUMERO_FONTE = '" + StringUtils.convertSqlString(aRm.getNumeroFonte())
					+ "'";
		}

		if (!aRm.getCodSottonumerazione().equals("") && !aRm.getCodSottonumerazione().equals("-")) {
			lCondizioni += " AND CIRCUM.COD_SOTTONUMERAZIONE = '"
					+ StringUtils.convertSqlString(aRm.getCodSottonumerazione()) + "'";
		}

		if (!aRm.getComma().equals("")) {
			lCondizioni += " AND CIRCUM.COMMA = '" + StringUtils.convertSqlString(aRm.getComma()) + "'";
		}

		if (!aRm.getCommaQualificante().equals("") && !aRm.getCommaQualificante().equals("-")) {
			lCondizioni += " AND CIRCUM.COMMA_QUALIFICANTE = '"
					+ StringUtils.convertSqlString(aRm.getCommaQualificante()) + "'";
		}

		if (!aRm.getLettera().equals("")) {
			lCondizioni += " AND CIRCUM.LETTERA = '" + StringUtils.convertSqlString(aRm.getLettera()) + "'";
		}

		if (!aRm.getNumero().equals("")) {
			lCondizioni += " AND CIRCUM.NUMERO = '" + StringUtils.convertSqlString(aRm.getNumero()) + "'";
		}

		if (!aRm.getArticolo().equals("")) {
			lCondizioni += " AND CIRCUM.ARTICOLO = '" + StringUtils.convertSqlString(aRm.getArticolo()) + "'";
		}

		if (!aRm.getCodUfficioInserimento().equals("")) {
			lCondizioni += " AND FAS.CHIAVE_UFFICIO = '"
					+ StringUtils.convertSqlString(aRm.getCodUfficioInserimento()) + "'";
		}

		if (!aRm.getCodBilanciamentoCircostanze().equals("")
				&& !aRm.getCodBilanciamentoCircostanze().equals("-")) {
			lCondizioni += " AND CIRCUM.COD_BILANCIAMENTO_CIRCOSTANZE = '"
					+ StringUtils.convertSqlString(aRm.getCodBilanciamentoCircostanze()) + "'";
		}

		return lCondizioni;

	} // Chiude setCondizioneCirco_Cumulo()

	public String setRestantiCondizioni(ReatoModel aRea, Boolean solocumulati) {
		String AltreCondizioni = new String();

		if ("I".equals(aRea.getNazionalita())) {
			AltreCondizioni += " AND SOGG.COD_STATO_NASCITA = '039' ";
		} else if ("S".equals(aRea.getNazionalita())) {
			AltreCondizioni += " AND SOGG.COD_STATO_NASCITA != '039' ";
		}
		//
		// Si possono ricrcare i Definiti, i NON Definiti, o Tutti
		if (aRea.getCodStatoFascicolo() != null && !aRea.getCodStatoFascicolo().equals("")) {
			if ("NA".equals(aRea.getCodStatoFascicolo())) {
				AltreCondizioni += " AND FAS.COD_STATO_FASCICOLO != '01' ";
			} else if ("AD".equals(aRea.getCodStatoFascicolo())) {
				AltreCondizioni += " AND FAS.COD_STATO_FASCICOLO = '01' ";
			}
		}
		//
		// Si possono ricrcare anche Solo i Cumulati
		if (solocumulati) {
			AltreCondizioni += " AND FAS.FLAG_CUMULANTE = 'S' ";
		}

		return AltreCondizioni;

	} // Chiude setRestantiCondizioni()

	protected String getFascicoloSqlReatoCirco_Cumulo_Query() {
		String lStatement = new String();

		lStatement += " SELECT DISTINCT FAS.ID_FASCICOLO_SIEP, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, DESCR_SOGGETTO.RV_MEANING NAZIONE,";
		lStatement += " FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR , FAS.CHIAVE_UFFICIO, FAS.FLAG_CUMULANTE,";
		lStatement += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";
		//
		lStatement += " FROM FASCICOLO_SIEP FAS, SOGGETTO SOGG, ";
		lStatement += " UFFICIO UFF, REATO_CUMULO RCUM, CIRCOSTANZA_CUMULO CIRCUM, TITOLO_CUMULATO TITCUM, ISTRUTTORIA_CUMULO ISTRU, EVENTO,";
		lStatement += " CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES DESCR_SOGGETTO, COMUNE DESCR_COM_UFF";

		lStatement += " WHERE FAS.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND UFF.COD_UFFICIO = FAS.CHIAVE_UFFICIO";

		lStatement += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE";

		lStatement += " AND DESCR_SOGGETTO.RV_DOMAIN = 'NAZIONE' ";
		lStatement += " AND SOGG.COD_STATO_NASCITA = DESCR_SOGGETTO.RV_LOW_VALUE";

		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		return lStatement;

	} // CHIUDO getFascicoloSqlReatoCirco_Cumulo_Query()

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
		// END MEV 26 CUMULO

} // Chiude DAO
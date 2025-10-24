package siap.sius.esecuzionemisuraalternativa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.esecuzionemisuraalternativa.model.EMAFascGPModel;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * EsecuzioneMisuraAlternativaSqlDAO - Classe SqlDAO che rappresenta la tabella EsecuzioneMisuraAlternativa
 *
 * @version 1.0
 */
public class EsecuzioneMisuraAlternativaSqlDAO extends SIAPSqlDAO {

	public EsecuzioneMisuraAlternativaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaEsecuzioneMisuraAlternativa(EsecuzioneMisuraAlternativaModel aModel)
			throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaEsecuzioneMisuraAlternativaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaEsecuzioneMisuraAlternativaByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFascicolo(aKey);
		setStatement(lSql);
	}

	public void ricercaEsecuzioneMisuraAlternativaByAnnoProg(BigDecimal aAnno, BigDecimal aProg)
			throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE ANNO_S07 = " + aAnno;
		lSql += " AND PROGR_S07 = " + aProg;
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT ID_ESECUZIONE_MISURA_ALTERNATI, ANNO_S07, PROGR_S07, "
				+ "DATA_ORDINANZA, COD_AUTORITA_EMITT_ORD, COD_TIPO_AUTORITA_EMITT_ORD, "
				+ "DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_AUTORITA_EMITT_ORD, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_LUOGO_AUTORITA_EMITT_ORD, "
				+ "COD_LUOGO_AUTORITA_EMITT_ORD, COD_TIPO_MISURA, "
				+ "DESCR_TIPO_MISURA.RV_MEANING DESCR_TIPO_MISURA, DATA_INIZIO_MISURA, "
				+ "DATA_TERMINE_INIZIALE, DATA_TERMINE_ATTUALE, DATA_DECLARATORIA_EP, "
				+ "DATA_TX_ATTI_EST_PENA, DEP_DEC_ID_DEPOSITO_DECRETO, "
				+ "DEP_OPID_DEPOSITO_ORDINANZA_PC, NOTE, EMA.COD_OPERATORE_INSERIMENTO, "
				+ "EMA.DATA_INSERIMENTO, EMA.COD_UFFICIO_INSERIMENTO, "
				+ "EMA.COD_OPERATORE_AGGIORNAMENTO, EMA.DATA_AGGIORNAMENTO, "
				+ "EMA.COD_UFFICIO_AGGIORNAMENTO, GEN_PRID_GENERALE_PROCEDIMENTO, "
				+ "NVL(LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA ";
		// JOIN per la descrizione dei campi
		lStatement += " FROM ESECUZIONE_MISURA_ALTERNATIVA EMA";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON EMA.COD_AUTORITA_EMITT_ORD = UFF.COD_UFFICIO";
		lStatement += "LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_MISURA ON (EMA.COD_TIPO_MISURA = "
				+ "DESCR_TIPO_MISURA.RV_LOW_VALUE AND DESCR_TIPO_MISURA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')";
		lStatement += " INNER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO = "
				+ "DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " INNER  JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE) ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		EsecuzioneMisuraAlternativaModel aModel = new EsecuzioneMisuraAlternativaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdEsecuzioneMisuraAlternati(getBigDecimal("ID_ESECUZIONE_MISURA_ALTERNATI"));
		aModel.setAnnoS07(getBigDecimal("ANNO_S07"));
		aModel.setProgrS07(getBigDecimal("PROGR_S07"));
		aModel.setDataOrdinanza(getDate("DATA_ORDINANZA"));
		aModel.setCodAutoritaEmittOrd(getString("COD_AUTORITA_EMITT_ORD"));
		aModel.setDescrAutoritaEmittOrd((""));
		aModel.setCodTipoAutoritaEmittOrd(getString("COD_TIPO_AUTORITA_EMITT_ORD"));
		aModel.setDescrTipoAutoritaEmittOrd(getString("DESCR_TIPO_AUTORITA_EMITT_ORD"));
		aModel.setCodLuogoAutoritaEmittOrd(getString("COD_LUOGO_AUTORITA_EMITT_ORD"));
		aModel.setDescrLuogoAutoritaEmittOrd(getString("DESCR_LUOGO_AUTORITA_EMITT_ORD"));
		aModel.setCodTipoMisura(getString("COD_TIPO_MISURA"));
		aModel.setDescrTipoMisura(getString("DESCR_TIPO_MISURA"));
		aModel.setDataInizioMisura(getDate("DATA_INIZIO_MISURA"));
		aModel.setDataTermineIniziale(getDate("DATA_TERMINE_INIZIALE"));
		aModel.setDataTermineAttuale(getDate("DATA_TERMINE_ATTUALE"));
		aModel.setDataDeclaratoriaEp(getDate("DATA_DECLARATORIA_EP"));
		aModel.setDataTxAttiEstPena(getDate("DATA_TX_ATTI_EST_PENA"));
		aModel.setDepDecIdDepositoDecreto(getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"));
		aModel.setDepOpidDepositoOrdinanzaPc(getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento((""));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento((""));
		aModel.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
		aModel.setLuogoEsecuzioneMisura(getString("LUOGO_ESECUZIONE_MISURA"));
		return aModel;
	}

	/* condizioni di ricerca generale */
	public String setCondizione(EsecuzioneMisuraAlternativaModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;

		if (aModel.getIdEsecuzioneMisuraAlternati() != null) {
			lCondizioni += " ID_ESECUZIONE_MISURA_ALTERNATI = " + aModel.getIdEsecuzioneMisuraAlternati();
			lInserito = true;
		}

		if (aModel.getAnnoS07() != null) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " ANNO_S07 = " + aModel.getAnnoS07();
		}
		if (aModel.getProgrS07() != null) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " PROGR_S07 = " + aModel.getAnnoS07();
		}
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 1) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
		}
		if (aModel.getCodUfficioAggiornamento() != null
				&& aModel.getCodUfficioAggiornamento().trim().length() > 1) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "'";
		}
		if (aModel.getCodAutoritaEmittOrd() != null && aModel.getCodAutoritaEmittOrd().trim().length() > 1) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " COD_AUTORITA_EMITT_ORD = '" + aModel.getCodAutoritaEmittOrd() + "'";
		}
		if (aModel.getGenPridGeneraleProcedimento() != null) {
			lCondizioni += " GEN_PRID_GENERALE_PROCEDIMENTO = " + aModel.getGenPridGeneraleProcedimento();
			lInserito = true;
		}

		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " WHERE ID_ESECUZIONE_MISURA_ALTERNATI = " + aKey;
		return lCondizioni;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " , GENERALE_PROCEDIMENTO GP ";
		lCondizioni += " WHERE GP.FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		lCondizioni += " AND GP.ID_GENERALE_PROCEDIMENTO=EMA.GEN_PRID_GENERALE_PROCEDIMENTO ";
		return lCondizioni;
	}

	/**
	 * Ricerca Esecuzione Misure Alternative
	 *
	 * @parametri lAnno
	 * @parametri lProgr
	 * @parametri lAnnoIniziale
	 * @parametri lProgrIniziale
	 * @parametri lAnnoFinale
	 * @parametri lProgrFinale
	 * @parametri lUfficioUtenteConnesso
	 * @return lCondizioni
	 */
	public void ricercaEsecuzioneMisureAlternative(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso) {
		String EsecuzioneMisuraAlternativa = "";

		EsecuzioneMisuraAlternativa += getEsecuzioneMisuraAlternativa();
		EsecuzioneMisuraAlternativa += setCondizioneEsecuzioneMisuraAlternativa(lAnno, lProgr, lAnnoIniziale,
				lProgrIniziale, lAnnoFinale, lProgrFinale, lUfficioUtenteConnesso);
		EsecuzioneMisuraAlternativa += setOrderAnnoProgr();
		// settaggio della stringa SQL appena costruita prima della query.
		setStatement(EsecuzioneMisuraAlternativa);
	}

	public void ricercaDettaglioEsecuzioneMAbyFascicolo(BigDecimal lIdFascicolo,
			String lUfficioUtenteConnesso) {
		String lStatement = "";

		// Si costruisce la query relativa ai Fascicoli SIUS Con Provvedimenti.
		lStatement += getFascicoliFigliConProvvedimenti(lIdFascicolo, lUfficioUtenteConnesso);

		lStatement += " UNION ";

		// Si costruisce la query relativa ai Fascicoli SIUS Senza Provvedimenti.
		lStatement += getFascicoliFigliSenzaProvvedimenti(lIdFascicolo, lUfficioUtenteConnesso);

		// settaggio della stringa SQL appena costruita prima della query.
		setStatement(lStatement);
	}

	public void ricercaDettaglioEsecuzioneMA(BigDecimal aEseMAKey, String lUfficioUtenteConnesso) {
		String lStatement = "";

		// Si costruisce la query relativa ai Fascicoli SIUS Con Provvedimenti.
		lStatement += getFascicoliConProvvedimentiDellaMisura(aEseMAKey, lUfficioUtenteConnesso);
		// lStatement += setCondizione(aModel);

		lStatement += " UNION ";
		// Si costruisce la query relativa ai Fascicoli SIUS Senza Provvedimenti.
		lStatement += getFascicoliSenzaProvvedimentiDellaMisura(aEseMAKey, lUfficioUtenteConnesso);
		// lStatement += setCondizione(aModel);

		// lStatement += setOrderAnnoProgr();
		setStatement(lStatement);
	}

	// STUB 03/11/2005 Nuova fase di ricerca Procedimenti Correlati alla Misura.
	public void ricercaProcedimentiCorrelatiAllEMA(BigDecimal aIdFascicoloEMA,
			String lUfficioUtenteConnesso) {
		String lStatement = "";
		// Si costruisce la query relativa ai Fascicoli SIUS Correlati all'EMA.
		lStatement += getFascicoliCorrelatiConProvvedimentiDellaMisura(aIdFascicoloEMA,
				lUfficioUtenteConnesso);
		lStatement += " UNION ";
		lStatement += getFascicoliCorrelatiSenzaProvvedimentiDellaMisura(aIdFascicoloEMA,
				lUfficioUtenteConnesso);

		setStatement(lStatement);
	}

	protected String getEsecuzioneMisuraAlternativa() {
		String lStatement = new String();

		lStatement += "SELECT EMA.ID_ESECUZIONE_MISURA_ALTERNATI, EMA.ANNO_S07, EMA.PROGR_S07, "
				+ "EMA.DATA_ORDINANZA, EMA.COD_AUTORITA_EMITT_ORD, UFFICIO_AUT.COD_TIPO_UFFICIO "
				+ "COD_TIPO_AUTORITA_EMITT_ORD,";
		lStatement += " UFFICIO_AUT.COD_COMUNE COD_LUOGO_AUTORITA_EMITT_ORD, COMUNE_AUT.DESCRIZIONE "
				+ "DESCR_LUOGO_AUTORITA_EMITT_ORD, DESCR_TIPO_AUT.RV_MEANING DESCR_TIPO_AUTORITA_EMITT_ORD,";
		lStatement += " EMA.COD_TIPO_AUTORITA_EMITT_ORD, EMA.COD_LUOGO_AUTORITA_EMITT_ORD, "
				+ "EMA.COD_TIPO_MISURA, EMA.DATA_INIZIO_MISURA, EMA.DATA_TERMINE_INIZIALE,";
		lStatement += " EMA.DATA_TERMINE_ATTUALE, EMA.DATA_DECLARATORIA_EP, EMA.DATA_TX_ATTI_EST_PENA, "
				+ "EMA.DEP_DEC_ID_DEPOSITO_DECRETO, EMA.DEP_OPID_DEPOSITO_ORDINANZA_PC,";
		lStatement += " EMA.NOTE, EMA.COD_OPERATORE_INSERIMENTO, EMA.DATA_INSERIMENTO, "
				+ "EMA.COD_UFFICIO_INSERIMENTO, EMA.COD_OPERATORE_AGGIORNAMENTO,";
		lStatement += " EMA.DATA_AGGIORNAMENTO, EMA.COD_UFFICIO_AGGIORNAMENTO, "
				+ "EMA.GEN_PRID_GENERALE_PROCEDIMENTO, "
				+ "DESCR_MISURA_ALTERNATIVA.RV_MEANING DESCR_MISURA_ALTERNATIVA,";
		lStatement += " FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, "
				+ "FASC.DATA_INSERIMENTO DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
				+ "UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO,";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA DATA_NASCITA, "
				+ "SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, DESCR_COM_NASCITA.DESCRIZIONE "
				+ "DESCR_COMUNE_NASCITA, ";
		lStatement += "FASC_SIEP.CHIAVE_ANNO ANNO_FASCICOLO_SIEP, FASC_SIEP.CHIAVE_PROGR PROGR_FASCICOLO_SIEP, "
				+ "FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA, NVL(GP.COD_AUTORITA_DELEGATA, '-') "
				+ "COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += "GP.ANNO_S1 ANNO_S1, GP.PROGR_S1 PROGR_S1, NVL(EMA.LUOGO_ESECUZIONE_MISURA, '') "
				+ "LUOGO_ESECUZIONE_MISURA, FASC.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += "FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, "
				+ "FASCICOLO_SIEP FASC_SIEP, ";
		lStatement += " UFFICIO UFF, CG_REF_CODES DESCR_MISURA_ALTERNATIVA, ";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, ESECUZIONE_MISURA_ALTERNATIVA EMA, ";
		lStatement += " COMUNE COMUNE_AUT, UFFICIO UFFICIO_AUT, CG_REF_CODES DESCR_TIPO_AUT ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = 'U004' ";
		lStatement += " AND FASC.FAS_SIE_ID_FASCICOLO_SIEP = FASC_SIEP.ID_FASCICOLO_SIEP(+)  ";
		lStatement += " AND UFFICIO_AUT.COD_UFFICIO = EMA.COD_AUTORITA_EMITT_ORD ";
		lStatement += " AND UFFICIO_AUT.COD_COMUNE = COMUNE_AUT.COD_COMUNE ";
		lStatement += " AND DESCR_TIPO_AUT.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND DESCR_TIPO_AUT.RV_LOW_VALUE = UFFICIO_AUT.COD_TIPO_UFFICIO ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS  ";
		lStatement += " AND DESCR_MISURA_ALTERNATIVA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND EMA.COD_TIPO_MISURA = DESCR_MISURA_ALTERNATIVA.RV_LOW_VALUE  ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";
		lStatement += " AND EMA.GEN_PRID_GENERALE_PROCEDIMENTO = GP.ID_GENERALE_PROCEDIMENTO ";

		return lStatement;
	}

	/**
	 * Settaggio della condizione per Esecuzione Misure Alternative
	 *
	 * @param lAnno
	 * @param lProgr
	 * @param lAnnoIniziale
	 * @param lProgrIniziale
	 * @param lAnnoFinale
	 * @param lProgrFinale
	 * @param lUfficioUtenteConnesso
	 * @return lCondizioni
	 */
	private String setCondizioneEsecuzioneMisuraAlternativa(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso) {

		String s = new String();
		s += " AND  UFF.COD_UFFICIO = '" + lUfficioUtenteConnesso + "'";
		if (lAnno.length() > 2) {
			s += " AND PROGR_S1  = nvl('" + lProgr + "',PROGR_S1)";
			s += " AND ANNO_S1  = nvl('" + lAnno + "',ANNO_S1)";
		} else {
			// 20191023 [SG]: modificata query
			if (lAnnoIniziale.length() > 2) {
				// s += " AND ANNO_S1 >= nvl('" + lAnnoIniziale + "',ANNO_S1)";
				// s += " AND PROGR_S1 >= nvl('" + lProgrIniziale + "',PROGR_S1)";
				s += " AND ((ANNO_S1 > nvl('" + lAnnoIniziale + "',ANNO_S1)";
				s += " OR (ANNO_S1 = nvl('" + lAnnoIniziale + "',ANNO_S1) AND PROGR_S1 >= nvl('"
						+ lProgrIniziale + "',PROGR_S1))))";
			}
			if (lAnnoFinale.length() > 2) {
				// s += " AND ANNO_S1 <= nvl('" + lAnnoFinale + "',ANNO_S1) ";
				// s += " AND PROGR_S1 <= nvl('" + lProgrFinale + "',PROGR_S1) ";
				s += " AND ((ANNO_S1 < nvl('" + lAnnoFinale + "',ANNO_S1)";
				s += " OR (ANNO_S1 = nvl('" + lAnnoFinale + "',ANNO_S1) AND PROGR_S1 <= nvl('" + lProgrFinale
						+ "',PROGR_S1))))";
			}
		}
		return s;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getEsecuzioneMisureAlternative() throws DAOException {
		EMAFascGPModel lEMAFascGP = new EMAFascGPModel();

		// Caricamento EsecuzioneMA.
		lEMAFascGP.getEsecuzioneMAModel()
				.setIdEsecuzioneMisuraAlternati(getBigDecimal("ID_ESECUZIONE_MISURA_ALTERNATI"));
		lEMAFascGP.getEsecuzioneMAModel().setAnnoS07(getBigDecimal("ANNO_S07"));
		lEMAFascGP.getEsecuzioneMAModel().setProgrS07(getBigDecimal("PROGR_S07"));
		lEMAFascGP.getEsecuzioneMAModel().setDataOrdinanza(getDate("DATA_ORDINANZA"));
		lEMAFascGP.getEsecuzioneMAModel().setCodAutoritaEmittOrd(getString("COD_AUTORITA_EMITT_ORD"));
		lEMAFascGP.getEsecuzioneMAModel()
				.setCodTipoAutoritaEmittOrd(getString("COD_TIPO_AUTORITA_EMITT_ORD"));
		lEMAFascGP.getEsecuzioneMAModel()
				.setDescrTipoAutoritaEmittOrd(getString("DESCR_TIPO_AUTORITA_EMITT_ORD"));
		lEMAFascGP.getEsecuzioneMAModel()
				.setCodLuogoAutoritaEmittOrd(getString("COD_LUOGO_AUTORITA_EMITT_ORD"));
		lEMAFascGP.getEsecuzioneMAModel()
				.setDescrLuogoAutoritaEmittOrd(getString("DESCR_LUOGO_AUTORITA_EMITT_ORD"));
		lEMAFascGP.getEsecuzioneMAModel().setCodTipoMisura(getString("COD_TIPO_MISURA"));
		lEMAFascGP.getEsecuzioneMAModel().setDataInizioMisura(getDate("DATA_INIZIO_MISURA"));
		lEMAFascGP.getEsecuzioneMAModel().setDataTermineIniziale(getDate("DATA_TERMINE_INIZIALE"));
		lEMAFascGP.getEsecuzioneMAModel().setDataTermineAttuale(getDate("DATA_TERMINE_ATTUALE"));
		lEMAFascGP.getEsecuzioneMAModel().setDataDeclaratoriaEp(getDate("DATA_DECLARATORIA_EP"));
		lEMAFascGP.getEsecuzioneMAModel().setDataTxAttiEstPena(getDate("DATA_TX_ATTI_EST_PENA"));
		lEMAFascGP.getEsecuzioneMAModel()
				.setDepDecIdDepositoDecreto(getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"));
		lEMAFascGP.getEsecuzioneMAModel()
				.setDepOpidDepositoOrdinanzaPc(getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC"));
		lEMAFascGP.getEsecuzioneMAModel().setNote(getString("NOTE"));
		lEMAFascGP.getEsecuzioneMAModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lEMAFascGP.getEsecuzioneMAModel().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lEMAFascGP.getEsecuzioneMAModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lEMAFascGP.getEsecuzioneMAModel()
				.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lEMAFascGP.getEsecuzioneMAModel().setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lEMAFascGP.getEsecuzioneMAModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lEMAFascGP.getEsecuzioneMAModel()
				.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
		lEMAFascGP.getEsecuzioneMAModel().setLuogoEsecuzioneMisura(getString("LUOGO_ESECUZIONE_MISURA"));

		lEMAFascGP.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lEMAFascGP.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lEMAFascGP.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lEMAFascGP.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lEMAFascGP.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lEMAFascGP.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lEMAFascGP.getFascicoloSiusModel().setChiaveAnnoSIEP(getBigDecimal("ANNO_FASCICOLO_SIEP"));
		lEMAFascGP.getFascicoloSiusModel().setChiaveProgrSIEP(getBigDecimal("PROGR_FASCICOLO_SIEP"));
		lEMAFascGP.getFascicoloSiusModel()
				.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		lEMAFascGP.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lEMAFascGP.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
		// lEMAFascGP.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lEMAFascGP.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		lEMAFascGP.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lEMAFascGP.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lEMAFascGP.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lEMAFascGP.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		lEMAFascGP.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));

		lEMAFascGP.getFascicoloSiusModel().setSoggetto(lSoggetto);
		// Generale procedimento
		lEMAFascGP.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		// lEMAFascGP.getGeneraleProcedimentoModel().setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		lEMAFascGP.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lEMAFascGP.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		lEMAFascGP.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE"));
		lEMAFascGP.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_MISURA_ALTERNATIVA"));
		lEMAFascGP.getGeneraleProcedimentoModel().setAnnoS1(getBigDecimal("ANNO_S1"));
		lEMAFascGP.getGeneraleProcedimentoModel().setProgrS1(getBigDecimal("PROGR_S1"));
		lEMAFascGP.getGeneraleProcedimentoModel().setDataDefinizione(getDate("DATA_INIZIO_MISURA"));

		return lEMAFascGP;
	}

	private String setOrderAnnoProgr() {
		String lOrder = new String();
		lOrder = " ORDER BY CHIAVE_ANNO , CHIAVE_PROGR ASC";
		return lOrder;
	}

	protected String getDettaglioEsecuzioneMA(BigDecimal aEseMAKey, String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, "
				+ "FASC.DATA_INSERIMENTO DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
				+ "UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, SOGG.COGNOME COGNOME,";
		lStatement += " SOGG.NOME NOME, SOGG.DATA_NASCITA, "
				+ "DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO, "
				+ "GP.DATA_RICHIESTA,";
		lStatement += " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
				+ "NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE ";
		lStatement += " NVL(EMA.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA, "
				+ "FASC.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += "FROM ESECUZIONE_MISURA_ALTERNATIVA EMA, FASCICOLO_SIUS FASC, SOGGETTO  SOGG, "
				+ "GENERALE_PROCEDIMENTO GP, CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,";
		lStatement += " UFFICIO UFF,  COMUNE DESCR_COM_UFF,COMUNE DESCR_COM_NASCITA ";
		lStatement += "WHERE EMA.ID_ESECUZIONE_MISURA_ALTERNATI (+) = '" + aEseMAKey + "' ";
		lStatement += " AND EMA.GEN_PRID_GENERALE_PROCEDIMENTO (+) = GP.ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND UFF.COD_UFFICIO = nvl('" + lUfficioUtenteConnesso + "', UFF.COD_UFFICIO ) ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";
		lStatement += "ORDER BY 2, 3 ";

		return lStatement;
	}

	/**
	 * getFascicoloSiusGPModel
	 *
	 * @Restituisce Il Model del FascicoloGPModel.
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusGPModel() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		// N.B. Utilizzo CodStatoFascicolo come contenitore del cod evento prelevato dalla tabella EVENTO
		// associata
		lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.getFascicoloSiusModel()
				.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		// STUB 26/10/2005 N.B. Utilizzo NumFascicoliUnificati come contenitore del Numero Procedimenti del
		// Tribunale Correlati.
		lFascicolo.getFascicoloSiusModel().setNumeroFascicoliUnificati(getBigDecimal("NUM_FIGLI"));
		// STUB 02/11/2005
		lFascicolo.getFascicoloSiusModel()
				.setIdFascicoloSiusOrigine(getBigDecimal("ID_FASCICOLO_SIUS_ORIGINE"));
		// STUB 06/06/2006
		lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));
		// STUB 26/04/2007
		lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento
		lFascicolo.getGeneraleProcedimentoModel()
				.setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setAnnoS1(getBigDecimal("ANNO_S1"));
		lFascicolo.getGeneraleProcedimentoModel().setProgrS1(getBigDecimal("PROGR_S1"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_TIPO_PROCEDIMENTO"));
		// Utilizzo DescrPosGiuridica per trasportare le informazioni relative a DESCR_COD_PROCEDIMENTO
		lFascicolo.getGeneraleProcedimentoModel().setDescrPosGiuridica(getString("DESCR_COD_PROCEDIMENTO"));

		// Utilizzo DescrTipoAtto momentaneamente per ospitare DESCR_PROVVEDIMENTO
		// e DescrDefinizione per ospitare DESCR_DEFINIZIONE
		lFascicolo.getGeneraleProcedimentoModel().setDescrTipoAtto(getString("DESCR_PROVVEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_DEFINIZIONE"));

		// Utilizzo "DATA_RICHIESTA " per riportare la DATA_EMISSIONE dell'evento
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
		lFascicolo.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		// Utilizzo setDataDefinizione come vettore per EV.DATA_EMISSIONE
		lFascicolo.getGeneraleProcedimentoModel().setDataDefinizione(getDate("DATA_RICHIESTA"));
		// Utilizzo setDescrMittente come vettore per EV.DESCR_TIPO_PROCEDIMENTO
		lFascicolo.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_TIPO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrTipoMittenteAtto(getString("DESCR_MISURA_ALTERNATIVA"));
		// Utilizzo setDescrRichiestaDelegazione come vettore per EMA.LUOGO_ESECUZIONE_MISURA //STUB
		// 23/11/2004
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrRichiestaDelegazione(getString("LUOGO_ESECUZIONE_MISURA"));

		return lFascicolo;
	}

	protected String getFascicoliConProvvedimentiDellaMisura(BigDecimal aEseMAKey,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, "
				+ "FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
				+ "FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO "
				+ "DESCR_TIPO_UFFICIO, ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, "
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA "
				+ "COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO "
				+ "DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
				+ "NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
				+ "DESCR_MISURA_ALTERNATIVA.RV_MEANING DESCR_MISURA_ALTERNATIVA, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, "
				+ "EV.DATA_EMISSIONE DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, "
				+ "GP.ANNO_S1, GP.PROGR_S1, ";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, "
				+ "DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE, ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, ";
		// lStatement += " NVL(EMA.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += " NVL(DD.LUOGO_SVOLGIMENTO_PROVA, '') LUOGO_ESECUZIONE_MISURA ";
		// STUB 26/10/2005 Rework EMA.
		lStatement += ",(select count(*) FROM FASCICOLO_SIUS FASCICOLO_FIGLIO2, FASCICOLO_SIUS FASC2 ";
		lStatement += " WHERE FASC_FIGLIO.ID_FASCICOLO_SIUS is not null AND FASC2.ID_FASCICOLO_SIUS = "
				+ "FASC.ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = FASCICOLO_FIGLIO2.ID_FASCICOLO_SIUS_ORIGINE) ";
		lStatement += " NUM_FIGLI, null ID_FASCICOLO_SIUS_FIGLIO, null ID_FASCICOLO_SIUS_ORIGINE ";

		lStatement += " FROM ESECUZIONE_MISURA_ALTERNATIVA EMA, GENERALE_PROCEDIMENTO GP_PADRE, "
				+ "GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC_PADRE, FASCICOLO_SIUS FASC, SOGGETTO SOGG, "
				+ "CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV, "
				+ "CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO, "
				+ "CG_REF_CODES DESCR_MISURA_ALTERNATIVA, DEPOSITO_DECRETO DD ";
		lStatement += " ,FASCICOLO_SIUS FASC_FIGLIO "; // STUB 26/10/2005 Rework EMA.
		lStatement += " WHERE EMA.ID_ESECUZIONE_MISURA_ALTERNATI = '" + aEseMAKey + "' ";
		lStatement += " AND EMA.GEN_PRID_GENERALE_PROCEDIMENTO = GP_PADRE.ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND GP_PADRE.ANNO_S1 = GP.ANNO_S1";
		lStatement += " AND GP_PADRE.PROGR_S1 = GP.PROGR_S1";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO != 'U004'";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND DESCR_MISURA_ALTERNATIVA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND EMA.COD_TIPO_MISURA = DESCR_MISURA_ALTERNATIVA.RV_LOW_VALUE  ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND (EV.DATA_INSERIMENTO,FASC.ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// 20250731: [SG] aggiunta condizione sull'ufficio
		lStatement += " AND FASC.CHIAVE_UFFICIO = '" + lUfficioUtenteConnesso + "'";
		lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02'  OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += "	where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		lStatement += "	AND (EV3.COD_TIPO_PROVVEDIMENTO = '02' OR EV3.COD_TIPO_PROVVEDIMENTO = '03')) ) ";
		lStatement += " AND DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_MOTIVO = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND EV.ID_EVENTO = DD.ID_EVENTO_GENERATO (+)";
		lStatement += " AND DD.COD_TIPO_DECRETO(+) ='19'"; // 11/04/2008 Soluzione MAC a8/rr/086
		// STUB 26/10/2005 Rework EMA.
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE(+) = FASC.ID_FASCICOLO_SIUS";
		return lStatement;
	}

	protected String getFascicoliSenzaProvvedimentiDellaMisura(BigDecimal aEseMAKey,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, "
				+ "FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
				+ "FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, "
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, ";
		lStatement += " '-' DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, '-' "
				+ "DESCR_COMUNE_NASCITA, ";
		lStatement += " '-' COD_PROVINCIA_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, "
				+ "GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, ";
		lStatement += " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
				+ "NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
				+ "DESCR_MISURA_ALTERNATIVA.RV_MEANING DESCR_MISURA_ALTERNATIVA, ";
		lStatement += " '-' COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, "
				+ "null DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, "
				+ "GP.PROGR_S1, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-'  DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING "
				+ "DESCR_COD_PROCEDIMENTO, ";
		// lStatement += " NVL(EMA.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += "     NULL LUOGO_ESECUZIONE_MISURA "; // 11/04/2008 Soluzione MAC a8/rr/086
		// STUB 02/11/2005 Rework EMA.
		lStatement += ", 0 NUM_FIGLI, null ID_FASCICOLO_SIUS_FIGLIO, null ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += "FROM ESECUZIONE_MISURA_ALTERNATIVA EMA, GENERALE_PROCEDIMENTO GP_PADRE, "
				+ "GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC_PADRE, FASCICOLO_SIUS FASC, ";
		lStatement += "SOGGETTO  SOGG, UFFICIO UFF, COMUNE DESCR_COM_UFF, "
				+ "CG_REF_CODES DESCR_COD_PROCEDIMENTO, CG_REF_CODES DESCR_MISURA_ALTERNATIVA";
		lStatement += " WHERE EMA.ID_ESECUZIONE_MISURA_ALTERNATI = '" + aEseMAKey + "' ";
		lStatement += " AND EMA.GEN_PRID_GENERALE_PROCEDIMENTO = GP_PADRE.ID_GENERALE_PROCEDIMENTO";
		lStatement += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND GP_PADRE.ANNO_S1 = GP.ANNO_S1";
		lStatement += " AND GP_PADRE.PROGR_S1 = GP.PROGR_S1";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO != 'U004'";
		lStatement += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_MISURA_ALTERNATIVA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lStatement += " AND EMA.COD_TIPO_MISURA = DESCR_MISURA_ALTERNATIVA.RV_LOW_VALUE";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO  ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND (FASC.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS  ";
        // 20250731: [DF] si esclude la condizione sul max data_ins non necessaria per la NOT IN
//		lStatement += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, EVENTO EV";
//		lStatement += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
//		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " FROM FASCICOLO_SIUS FASC2, EVENTO EV";
		lStatement += " WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND FASC2.CHIAVE_UFFICIO = '" + lUfficioUtenteConnesso + "'";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03')  ";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS ";
		lStatement += " ) ";
        // 20250731: [DF]--------------------------------------------------------
//		// 20250731: [SG] aggiunta condizione sull'ufficio
//		lStatement += " AND FASC2.CHIAVE_UFFICIO = '" + lUfficioUtenteConnesso + "'";
//		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,"
//				+ " FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2";
//		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
//		// 20250731: [SG] aggiunta condizione sull'ufficio
//		lStatement += " AND FASC.CHIAVE_UFFICIO = '" + lUfficioUtenteConnesso + "'";
//		lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02'  OR EV2.COD_TIPO_PROVVEDIMENTO = '03')";
//		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3";
//		lStatement += " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
//		lStatement += " AND (EV3.COD_TIPO_PROVVEDIMENTO = '02'  OR EV3.COD_TIPO_PROVVEDIMENTO = '03')))";
//		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03')";
//		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS";
//		lStatement += " ) ";
        
		return lStatement;
	}

	protected String getFascicoliFigliConProvvedimenti(BigDecimal aIdFascicolo,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, "
				+ "FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
				+ "FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, "
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
				+ "DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO "
				+ "DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
				+ "NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
				+ "DESCR_MISURA_ALTERNATIVA.RV_MEANING DESCR_MISURA_ALTERNATIVA, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, "
				+ "EV.DATA_EMISSIONE DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, "
				+ "GP.ANNO_S1, GP.PROGR_S1, ";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, "
				+ "DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE, ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, ";
		// lStatement += " NVL(EMA.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += " NVL(DD.LUOGO_SVOLGIMENTO_PROVA, '') LUOGO_ESECUZIONE_MISURA, "
				+ "FASC.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += " FROM ESECUZIONE_MISURA_ALTERNATIVA EMA, GENERALE_PROCEDIMENTO GP_PADRE, "
				+ "GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC_PADRE, FASCICOLO_SIUS FASC, SOGGETTO SOGG, "
				+ "CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,CG_REF_CODES "
				+ "DESCR_ESITO_PROVVEDIMENTO, ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO, "
				+ "CG_REF_CODES DESCR_MISURA_ALTERNATIVA, DEPOSITO_DECRETO DD  ";
		lStatement += " WHERE EMA.GEN_PRID_GENERALE_PROCEDIMENTO = GP_PADRE.ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicolo + "' ";
		lStatement += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND GP_PADRE.ANNO_S1 = GP.ANNO_S1";
		lStatement += " AND GP_PADRE.PROGR_S1 = GP.PROGR_S1";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO != 'U004'";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND DESCR_MISURA_ALTERNATIVA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND EMA.COD_TIPO_MISURA = DESCR_MISURA_ALTERNATIVA.RV_LOW_VALUE ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND (EV.DATA_INSERIMENTO,FASC.ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02'  OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += "	where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		lStatement += "	AND (EV3.COD_TIPO_PROVVEDIMENTO = '02' OR EV3.COD_TIPO_PROVVEDIMENTO = '03'))) ";
		lStatement += " AND DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_MOTIVO = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND EV.ID_EVENTO = DD.ID_EVENTO_GENERATO (+)";

		return lStatement;
	}

	protected String getFascicoliFigliSenzaProvvedimenti(BigDecimal aIdFascicolo,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, "
				+ "FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
				+ "FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, "
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, ";
		lStatement += " '-' DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
				+ "'-' DESCR_COMUNE_NASCITA, ";
		lStatement += " '-' COD_PROVINCIA_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, "
				+ "GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, ";
		lStatement += " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
				+ "NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
				+ "DESCR_MISURA_ALTERNATIVA.RV_MEANING DESCR_MISURA_ALTERNATIVA, ";
		lStatement += " '-' COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, "
				+ "null DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, "
				+ "GP.PROGR_S1, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-'  DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING "
				+ "DESCR_COD_PROCEDIMENTO, ";
		lStatement += " NVL(EMA.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA, "
				+ "FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += "FROM ESECUZIONE_MISURA_ALTERNATIVA EMA, GENERALE_PROCEDIMENTO GP_PADRE, "
				+ "GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC_PADRE, FASCICOLO_SIUS FASC, ";
		lStatement += " SOGGETTO  SOGG, UFFICIO UFF, COMUNE DESCR_COM_UFF, "
				+ "CG_REF_CODES DESCR_COD_PROCEDIMENTO, CG_REF_CODES DESCR_MISURA_ALTERNATIVA ";
		lStatement += " WHERE EMA.GEN_PRID_GENERALE_PROCEDIMENTO = GP_PADRE.ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicolo + "' ";
		lStatement += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND GP_PADRE.ANNO_S1 = GP.ANNO_S1";
		lStatement += " AND GP_PADRE.PROGR_S1 = GP.PROGR_S1";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO != 'U004'";
		lStatement += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_MISURA_ALTERNATIVA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND EMA.COD_TIPO_MISURA = DESCR_MISURA_ALTERNATIVA.RV_LOW_VALUE  ";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO  ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND (FASC.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS  ";
		lStatement += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV ";
		lStatement += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02'  OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS  ";
		lStatement += " AND (EV3.COD_TIPO_PROVVEDIMENTO = '02'  OR EV3.COD_TIPO_PROVVEDIMENTO = '03') ) ) ";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03')  ";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS ";
		lStatement += " ) ";
		return lStatement;
	}

	// STUB 03/11/2005 Nuova fase di ricerca Procedimenti Correlati alla Misura.
	protected String getFascicoliCorrelatiConProvvedimentiDellaMisura(BigDecimal aIdFascicoloEMA,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();
		lStatement += "SELECT FASC_FIGLIO.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, "
				+ "FASC_FIGLIO.CHIAVE_ANNO CHIAVE_ANNO, FASC_FIGLIO.DATA_ISCRIZIONE DATA_ISCRIZIONE, "
				+ "SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC_FIGLIO.CHIAVE_PROGR CHIAVE_PROGR, FASC_FIGLIO.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
				+ "FASC_FIGLIO.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO "
				+ "DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, "
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
				+ "DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP_FIGLIO.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, "
				+ "GP_FIGLIO.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, "
				+ "NVL(GP_FIGLIO.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
				+ "NVL(GP_FIGLIO.DESCR_MITTENTE, '-') DESCR_MITTENTE, '-' DESCR_MISURA_ALTERNATIVA, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, "
				+ "EV.DATA_EMISSIONE DATA_RICHIESTA, GP_FIGLIO.ID_GENERALE_PROCEDIMENTO "
				+ "ID_GENERALE_PROCEDIMENTO, GP_FIGLIO.ANNO_S1, GP_FIGLIO.PROGR_S1, ";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, "
				+ "DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE, ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, ";
		lStatement += " NVL(DD.LUOGO_SVOLGIMENTO_PROVA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += ",0 NUM_FIGLI, null ID_FASCICOLO_SIUS_FIGLIO, FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE "
				+ "ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += " FROM GENERALE_PROCEDIMENTO GP_FIGLIO, FASCICOLO_SIUS FASC, SOGGETTO  SOGG, "
				+ "CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,CG_REF_CODES "
				+ "DESCR_ESITO_PROVVEDIMENTO, ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO, "
				+ "DEPOSITO_DECRETO DD ";
		lStatement += " ,FASCICOLO_SIUS FASC_FIGLIO ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloEMA + "' ";
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE(+) = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS = GP_FIGLIO.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND (EV.DATA_INSERIMENTO,FASC_FIGLIO.ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02'  OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += "	where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS ";
		lStatement += "	AND (EV3.COD_TIPO_PROVVEDIMENTO = '02' OR EV3.COD_TIPO_PROVVEDIMENTO = '03')))";
		lStatement += " AND DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_MOTIVO = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP_FIGLIO.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND UFF.COD_UFFICIO = FASC_FIGLIO.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS";
		lStatement += " AND EV.ID_EVENTO = DD.ID_EVENTO_GENERATO (+)";
		return lStatement;
	}

	// STUB 03/11/2005 Nuova fase di ricerca Procedimenti Correlati alla Misura.
	protected String getFascicoliCorrelatiSenzaProvvedimentiDellaMisura(BigDecimal aIdFascicoloEMA,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();
		lStatement += "SELECT FASC_FIGLIO.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC_FIGLIO.CHIAVE_ANNO "
				+ "CHIAVE_ANNO, FASC_FIGLIO.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC_FIGLIO.CHIAVE_PROGR CHIAVE_PROGR, FASC_FIGLIO.CHIAVE_UFFICIO CHIAVE_UFFICIO, "
				+ "FASC_FIGLIO.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO "
				+ "DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, "
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " '-' DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " '-' DESCR_COMUNE_NASCITA, '-' COD_PROVINCIA_NASCITA,";
		lStatement += " GP_FIGLIO.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, "
				+ "GP_FIGLIO.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, "
				+ "NVL(GP_FIGLIO.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, "
				+ "NVL(GP_FIGLIO.DESCR_MITTENTE, '-') DESCR_MITTENTE, '-' DESCR_MISURA_ALTERNATIVA, ";
		lStatement += " '-' COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, "
				+ "null DATA_RICHIESTA, GP_FIGLIO.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, "
				+ "GP_FIGLIO.ANNO_S1, GP_FIGLIO.PROGR_S1, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-' DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING "
				+ "DESCR_COD_PROCEDIMENTO, '-' LUOGO_ESECUZIONE_MISURA ";
		lStatement += ",0 NUM_FIGLI, null ID_FASCICOLO_SIUS_FIGLIO, FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE "
				+ "ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += " FROM GENERALE_PROCEDIMENTO GP_FIGLIO, FASCICOLO_SIUS FASC, SOGGETTO  SOGG, "
				+ "CG_REF_CODES DESCR_COD_PROCEDIMENTO, UFFICIO UFF, ";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, FASCICOLO_SIUS FASC_FIGLIO ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloEMA + "' ";
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE(+) = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS = GP_FIGLIO.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND (FASC_FIGLIO.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS";
		lStatement += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV ";
		lStatement += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP_FIGLIO.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02'  OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS  ";
		lStatement += " AND (EV3.COD_TIPO_PROVVEDIMENTO = '02'  OR EV3.COD_TIPO_PROVVEDIMENTO = '03')))";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03')";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS)";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP_FIGLIO.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC_FIGLIO.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		return lStatement;
	}

}
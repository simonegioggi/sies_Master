package siap.sius.esecuzionemisurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.esecuzionemisurasicurezza.model.EMSFascGPModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: EsecuzioneMisuraSicurezzaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella EsecuzioneMisuraSicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class EsecuzioneMisuraSicurezzaSqlDAO extends SIAPSqlDAO {

	public EsecuzioneMisuraSicurezzaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaEsecuzioneMisuraSicurezza(EsecuzioneMisuraSicurezzaModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaEsecuzioneMisuraSicurezzaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaEsecuzioneMisuraSicurezzaByIdOrdinanza(BigDecimal aIdOrdinanza) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdOrdinanza(aIdOrdinanza);
		setStatement(lSql);
	}

	public void ricercaEsecuzioneMisuraSicurezzaByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFascicolo(aKey);
		setStatement(lSql);
	}

	public void ricercaEsecuzioneMisuraSicurezzaByAnnoProg(BigDecimal aAnno, BigDecimal aProg)
			throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE ANNO_S07 = " + aAnno;
		lSql += " AND PROGR_S07 = " + aProg;
		setStatement(lSql);
	}

	/**
	 * ricercaEsecuzioneMisuraSicurezzaByIdFascicoloFiglio Trova l'Esecuzione Misura Sicurezza del fascicolo
	 * padre a partire dal'ID del fascicolo figlio
	 *
	 * @return stringa SQL
	 */
	public void ricercaEsecuzioneMisuraSicurezzaByIdFascicoloFiglio(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFascicoloFiglio(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ESECUZIONE_MISURA_SICUREZZA, " + "ANNO_S07, " + "PROGR_S07, "
				+ "DATA_ORDINANZA, " + "COD_AUTORITA_EMITT_ORD, " + "COD_TIPO_AUTORITA_EMITT_ORD, "
				+ "DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_AUTORITA_EMITT_ORD, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_LUOGO_AUTORITA_EMITT_ORD, "
				+ "COD_LUOGO_AUTORITA_EMITT_ORD, " + "COD_TIPO_MISURA, "
				+ "DESCR_TIPO_MISURA.RV_MEANING DESCR_TIPO_MISURA, " + "DATA_INIZIO_MISURA, "
				+ "DATA_TERMINE_INIZIALE, " + "DATA_TERMINE_ATTUALE, " + "DATA_DECLARATORIA_EMS, "
				+ "DEP_DEC_ID_DEPOSITO_DECRETO, " + "DEP_OPID_DEPOSITO_ORDINANZA_PC, " + "NOTE, "
				+ "EMS.COD_OPERATORE_INSERIMENTO, " + "EMS.DATA_INSERIMENTO, "
				+ "EMS.COD_UFFICIO_INSERIMENTO, " + "EMS.COD_OPERATORE_AGGIORNAMENTO, "
				+ "EMS.DATA_AGGIORNAMENTO, " + "EMS.COD_UFFICIO_AGGIORNAMENTO, "
				+ "GEN_PRID_GENERALE_PROCEDIMENTO, "
				+ "NVL(LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA, " + "NUM_GIORNI_MISURA, "
				+ "NUM_MESI_MISURA, " + "NUM_ANNI_MISURA ";
		// JOIN per la descrizione dei campi
		lStatement += " FROM ESECUZIONE_MISURA_SICUREZZA EMS";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON NVL(EMS.COD_AUTORITA_EMITT_ORD, '-') = UFF.COD_UFFICIO";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_MISURA ON (EMS.COD_TIPO_MISURA=DESCR_TIPO_MISURA.RV_LOW_VALUE AND DESCR_TIPO_MISURA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')";
		lStatement += " INNER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " INNER  JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE) ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		EsecuzioneMisuraSicurezzaModel aModel = new EsecuzioneMisuraSicurezzaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdEsecuzioneMisuraSicurezza(getBigDecimal("ID_ESECUZIONE_MISURA_SICUREZZA"));
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
		aModel.setDataDeclaratoriaEMS(getDate("DATA_DECLARATORIA_EMS"));
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
		aModel.setNumGiorniMisura(getBigDecimal("NUM_GIORNI_MISURA"));
		aModel.setNumMesiMisura(getBigDecimal("NUM_MESI_MISURA"));
		aModel.setNumAnniMisura(getBigDecimal("NUM_ANNI_MISURA"));
		return aModel;
	}

	/* condizioni di ricerca generale */
	public String setCondizione(EsecuzioneMisuraSicurezzaModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;

		if (aModel.getIdEsecuzioneMisuraSicurezza() != null) {
			lCondizioni += " ID_ESECUZIONE_MISURA_SICUREZZA = " + aModel.getIdEsecuzioneMisuraSicurezza();
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
			lCondizioni += " NVL (COD_AUTORITA_EMITT_ORD = '" + aModel.getCodAutoritaEmittOrd() + "', '-'";
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
		lCondizioni += " WHERE ID_ESECUZIONE_MISURA_SICUREZZA = " + aKey;
		return lCondizioni;
	}

	public String setCondizioniByIdOrdinanza(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " WHERE DEP_OPID_DEPOSITO_ORDINANZA_PC = " + aKey;
		return lCondizioni;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " , GENERALE_PROCEDIMENTO GP ";
		lCondizioni += " WHERE GP.FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		lCondizioni += " AND GP.ID_GENERALE_PROCEDIMENTO=EMS.GEN_PRID_GENERALE_PROCEDIMENTO ";
		return lCondizioni;
	}

	public String setCondizioniByIdFascicoloFiglio(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += "  , GENERALE_PROCEDIMENTO GP_PADRE, ";
		lCondizioni += " GENERALE_PROCEDIMENTO GP_FIGLIO, ";
		lCondizioni += " FASCICOLO_SIUS FASC_PADRE, ";
		lCondizioni += " FASCICOLO_SIUS FASC_FIGLIO ";
		lCondizioni += " WHERE GP_FIGLIO.FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		lCondizioni += " AND GP_PADRE.ANNO_S1 = GP_FIGLIO.ANNO_S1 ";
		lCondizioni += " AND GP_PADRE.PROGR_S1 = GP_FIGLIO.PROGR_S1 ";
		lCondizioni += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lCondizioni += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS = GP_FIGLIO.FAS_SIU_ID_FASCICOLO_SIUS ";
		lCondizioni += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC_FIGLIO.CHIAVE_UFFICIO ";
		lCondizioni += " AND GP_PADRE.ID_GENERALE_PROCEDIMENTO = EMS.GEN_PRID_GENERALE_PROCEDIMENTO ";
		return lCondizioni;
	}

	/**
	 * Ricerca Esecuzione Misure Sicurezza
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
	public void ricercaEsecuzioneMisureSicurezza(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso) {
		String EsecuzioneMisuraSicurezza = "";

		EsecuzioneMisuraSicurezza += getEsecuzioneMisuraSicurezza();
		EsecuzioneMisuraSicurezza += setCondizioneEsecuzioneMisuraSicurezza(lAnno, lProgr, lAnnoIniziale,
				lProgrIniziale, lAnnoFinale, lProgrFinale, lUfficioUtenteConnesso);
		EsecuzioneMisuraSicurezza += setOrderAnnoProgr();
		// settaggio della stringa SQL appena costruita prima della query.
		setStatement(EsecuzioneMisuraSicurezza);
	}

	public void ricercaDettaglioEsecuzioneMSbyFascicolo(BigDecimal lIdFascicolo,
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

	public void ricercaDettaglioEsecuzioneMS(BigDecimal aEseMSKey, String lUfficioUtenteConnesso) {
		String lStatement = "";

		// Si costruisce la query relativa ai Fascicoli SIUS Con Provvedimenti.
		lStatement += getFascicoliConProvvedimentiDellaMisura(aEseMSKey, lUfficioUtenteConnesso);
		// lStatement += setCondizione(aModel);

		lStatement += " UNION ";
		// Si costruisce la query relativa ai Fascicoli SIUS Senza Provvedimenti.
		lStatement += getFascicoliSenzaProvvedimentiDellaMisura(aEseMSKey, lUfficioUtenteConnesso);
		// lStatement += setCondizione(aModel);

		// lStatement += setOrderAnnoProgr();
		setStatement(lStatement);
	}

	// Fase di ricerca Procedimenti Correlati alla Misura.
	public void ricercaProcedimentiCorrelatiAllEMS(BigDecimal aIdFascicoloEMS,
			String lUfficioUtenteConnesso) {
		String lStatement = "";
		// Si costruisce la query relativa ai Fascicoli SIUS Correlati all'EMS.
		lStatement += getFascicoliCorrelatiConProvvedimentiDellaMisura(aIdFascicoloEMS,
				lUfficioUtenteConnesso);
		lStatement += " UNION ";
		lStatement += getFascicoliCorrelatiSenzaProvvedimentiDellaMisura(aIdFascicoloEMS,
				lUfficioUtenteConnesso);

		setStatement(lStatement);
	}

	protected String getEsecuzioneMisuraSicurezza() {
		String lStatement = new String();

		lStatement += "SELECT EMS.ID_ESECUZIONE_MISURA_SICUREZZA, EMS.ANNO_S07, EMS.PROGR_S07, EMS.DATA_ORDINANZA, EMS.COD_AUTORITA_EMITT_ORD, UFFICIO_AUT.COD_TIPO_UFFICIO COD_TIPO_AUTORITA_EMITT_ORD,";
		lStatement += " UFFICIO_AUT.COD_COMUNE COD_LUOGO_AUTORITA_EMITT_ORD, COMUNE_AUT.DESCRIZIONE DESCR_LUOGO_AUTORITA_EMITT_ORD, DESCR_TIPO_AUT.RV_MEANING DESCR_TIPO_AUTORITA_EMITT_ORD,";
		lStatement += " EMS.COD_TIPO_AUTORITA_EMITT_ORD, EMS.COD_LUOGO_AUTORITA_EMITT_ORD, EMS.COD_TIPO_MISURA, EMS.DATA_INIZIO_MISURA, EMS.DATA_TERMINE_INIZIALE,";
		lStatement += " EMS.DATA_TERMINE_ATTUALE, EMS.DATA_DECLARATORIA_EMS, EMS.DEP_DEC_ID_DEPOSITO_DECRETO, EMS.DEP_OPID_DEPOSITO_ORDINANZA_PC,";
		lStatement += " EMS.NOTE, EMS.COD_OPERATORE_INSERIMENTO, EMS.DATA_INSERIMENTO, EMS.COD_UFFICIO_INSERIMENTO, EMS.COD_OPERATORE_AGGIORNAMENTO,";
		lStatement += " EMS.DATA_AGGIORNAMENTO, EMS.COD_UFFICIO_AGGIORNAMENTO, EMS.GEN_PRID_GENERALE_PROCEDIMENTO, DESCR_MISURA_SICUREZZA.RV_MEANING DESCR_MISURA_SICUREZZA,";
		lStatement += " EMS.NUM_GIORNI_MISURA, EMS.NUM_MESI_MISURA, EMS.NUM_ANNI_MISURA,";
		lStatement += " FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, FASC.DATA_INSERIMENTO DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO , UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO,";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA DATA_NASCITA, SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, ";
		lStatement += " FASC_SIEP.CHIAVE_ANNO ANNO_FASCICOLO_SIEP, FASC_SIEP.CHIAVE_PROGR PROGR_FASCICOLO_SIEP, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += " GP.ANNO_S1 ANNO_S1, GP.PROGR_S1 PROGR_S1, NVL(EMS.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA, FASC.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, FASCICOLO_SIEP FASC_SIEP, ";
		lStatement += " UFFICIO UFF, CG_REF_CODES DESCR_MISURA_SICUREZZA, ";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, ESECUZIONE_MISURA_SICUREZZA EMS, ";
		lStatement += " COMUNE COMUNE_AUT, UFFICIO UFFICIO_AUT, CG_REF_CODES DESCR_TIPO_AUT ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = 'U024' ";
		lStatement += " AND FASC.FAS_SIE_ID_FASCICOLO_SIEP = FASC_SIEP.ID_FASCICOLO_SIEP(+)  ";
		lStatement += " AND UFFICIO_AUT.COD_UFFICIO = NVL(EMS.COD_AUTORITA_EMITT_ORD, '-') ";
		lStatement += " AND UFFICIO_AUT.COD_COMUNE = COMUNE_AUT.COD_COMUNE ";
		lStatement += " AND DESCR_TIPO_AUT.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND DESCR_TIPO_AUT.RV_LOW_VALUE = UFFICIO_AUT.COD_TIPO_UFFICIO ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS  ";
		lStatement += " AND DESCR_MISURA_SICUREZZA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND EMS.COD_TIPO_MISURA = DESCR_MISURA_SICUREZZA.RV_LOW_VALUE  ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";
		lStatement += " AND EMS.GEN_PRID_GENERALE_PROCEDIMENTO = GP.ID_GENERALE_PROCEDIMENTO ";

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
	private String setCondizioneEsecuzioneMisuraSicurezza(String lAnno, String lProgr, String lAnnoIniziale,
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
	public GenericModel getEsecuzioneMisureSicurezza() throws DAOException {
		EMSFascGPModel lEMSFascGP = new EMSFascGPModel();

		// Caricamento EsecuzioneMS.
		lEMSFascGP.getEsecuzioneMSModel()
				.setIdEsecuzioneMisuraSicurezza(getBigDecimal("ID_ESECUZIONE_MISURA_SICUREZZA"));
		lEMSFascGP.getEsecuzioneMSModel().setAnnoS07(getBigDecimal("ANNO_S07"));
		lEMSFascGP.getEsecuzioneMSModel().setProgrS07(getBigDecimal("PROGR_S07"));
		lEMSFascGP.getEsecuzioneMSModel().setDataOrdinanza(getDate("DATA_ORDINANZA"));
		lEMSFascGP.getEsecuzioneMSModel().setCodAutoritaEmittOrd(getString("COD_AUTORITA_EMITT_ORD"));
		lEMSFascGP.getEsecuzioneMSModel()
				.setCodTipoAutoritaEmittOrd(getString("COD_TIPO_AUTORITA_EMITT_ORD"));
		lEMSFascGP.getEsecuzioneMSModel()
				.setDescrTipoAutoritaEmittOrd(getString("DESCR_TIPO_AUTORITA_EMITT_ORD"));
		lEMSFascGP.getEsecuzioneMSModel()
				.setCodLuogoAutoritaEmittOrd(getString("COD_LUOGO_AUTORITA_EMITT_ORD"));
		lEMSFascGP.getEsecuzioneMSModel()
				.setDescrLuogoAutoritaEmittOrd(getString("DESCR_LUOGO_AUTORITA_EMITT_ORD"));
		lEMSFascGP.getEsecuzioneMSModel().setCodTipoMisura(getString("COD_TIPO_MISURA"));
		lEMSFascGP.getEsecuzioneMSModel().setDataInizioMisura(getDate("DATA_INIZIO_MISURA"));
		lEMSFascGP.getEsecuzioneMSModel().setDataTermineIniziale(getDate("DATA_TERMINE_INIZIALE"));
		lEMSFascGP.getEsecuzioneMSModel().setDataTermineAttuale(getDate("DATA_TERMINE_ATTUALE"));
		lEMSFascGP.getEsecuzioneMSModel().setDataDeclaratoriaEMS(getDate("DATA_DECLARATORIA_EMS"));
		lEMSFascGP.getEsecuzioneMSModel()
				.setDepDecIdDepositoDecreto(getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"));
		lEMSFascGP.getEsecuzioneMSModel()
				.setDepOpidDepositoOrdinanzaPc(getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC"));
		lEMSFascGP.getEsecuzioneMSModel().setNote(getString("NOTE"));
		lEMSFascGP.getEsecuzioneMSModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lEMSFascGP.getEsecuzioneMSModel().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lEMSFascGP.getEsecuzioneMSModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lEMSFascGP.getEsecuzioneMSModel()
				.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lEMSFascGP.getEsecuzioneMSModel().setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lEMSFascGP.getEsecuzioneMSModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lEMSFascGP.getEsecuzioneMSModel()
				.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
		lEMSFascGP.getEsecuzioneMSModel().setLuogoEsecuzioneMisura(getString("LUOGO_ESECUZIONE_MISURA"));
		lEMSFascGP.getEsecuzioneMSModel().setNumGiorniMisura(getBigDecimal("NUM_GIORNI_MISURA"));
		lEMSFascGP.getEsecuzioneMSModel().setNumMesiMisura(getBigDecimal("NUM_MESI_MISURA"));
		lEMSFascGP.getEsecuzioneMSModel().setNumAnniMisura(getBigDecimal("NUM_ANNI_MISURA"));

		lEMSFascGP.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lEMSFascGP.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lEMSFascGP.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lEMSFascGP.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lEMSFascGP.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lEMSFascGP.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lEMSFascGP.getFascicoloSiusModel().setChiaveAnnoSIEP(getBigDecimal("ANNO_FASCICOLO_SIEP"));
		lEMSFascGP.getFascicoloSiusModel().setChiaveProgrSIEP(getBigDecimal("PROGR_FASCICOLO_SIEP"));
		lEMSFascGP.getFascicoloSiusModel()
				.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		lEMSFascGP.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lEMSFascGP.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
		// lEMSFascGP.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lEMSFascGP.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		lEMSFascGP.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lEMSFascGP.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lEMSFascGP.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lEMSFascGP.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		lEMSFascGP.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));

		lEMSFascGP.getFascicoloSiusModel().setSoggetto(lSoggetto);
		// Generale procedimento
		lEMSFascGP.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		// lEMSFascGP.getGeneraleProcedimentoModel().setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		lEMSFascGP.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lEMSFascGP.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		lEMSFascGP.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE"));
		lEMSFascGP.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_MISURA_SICUREZZA"));
		lEMSFascGP.getGeneraleProcedimentoModel().setAnnoS1(getBigDecimal("ANNO_S1"));
		lEMSFascGP.getGeneraleProcedimentoModel().setProgrS1(getBigDecimal("PROGR_S1"));
		lEMSFascGP.getGeneraleProcedimentoModel().setDataDefinizione(getDate("DATA_INIZIO_MISURA"));

		return lEMSFascGP;
	}

	private String setOrderAnnoProgr() {
		String lOrder = new String();
		lOrder = " ORDER BY CHIAVE_ANNO , CHIAVE_PROGR ASC";
		return lOrder;
	}

	protected String getDettaglioEsecuzioneMS(BigDecimal aEseMSKey, String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, FASC.DATA_INSERIMENTO DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO , UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, SOGG.COGNOME COGNOME,";
		lStatement += " SOGG.NOME NOME, SOGG.DATA_NASCITA,  DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO,  DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA,";
		lStatement += " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE ";
		lStatement += " NVL(EMS.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA, FASC.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += "FROM ESECUZIONE_MISURA_SICUREZZA EMS, FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,";
		lStatement += " UFFICIO UFF,  COMUNE DESCR_COM_UFF,COMUNE DESCR_COM_NASCITA ";
		lStatement += "WHERE EMS.ID_ESECUZIONE_MISURA_SICUREZZA (+) = '" + aEseMSKey + "' ";
		lStatement += " AND EMS.GEN_PRID_GENERALE_PROCEDIMENTO (+) = GP.ID_GENERALE_PROCEDIMENTO ";
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
		// N.B. Utilizzo NumFascicoliUnificati come contenitore del Numero Procedimenti del Tribunale
		// Correlati.
		lFascicolo.getFascicoloSiusModel().setNumeroFascicoliUnificati(getBigDecimal("NUM_FIGLI"));
		lFascicolo.getFascicoloSiusModel()
				.setIdFascicoloSiusOrigine(getBigDecimal("ID_FASCICOLO_SIUS_ORIGINE"));
		lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));
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
				.setDescrTipoMittenteAtto(getString("DESCR_MISURA_SICUREZZA"));
		// Utilizzo setDescrRichiestaDelegazione come vettore per EMS.LUOGO_ESECUZIONE_MISURA
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrRichiestaDelegazione(getString("LUOGO_ESECUZIONE_MISURA"));

		return lFascicolo;
	}

	protected String getFascicoliConProvvedimentiDellaMisura(BigDecimal aEseMSKey,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, DESCR_MISURA_SICUREZZA.RV_MEANING DESCR_MISURA_SICUREZZA, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, EV.DATA_EMISSIONE DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, ";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE, ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, ";
		// lStatement += " NVL(EMS.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += " NVL(DD.LUOGO_SVOLGIMENTO_PROVA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += ",(select count(*) FROM FASCICOLO_SIUS FASCICOLO_FIGLIO2, FASCICOLO_SIUS FASC2 ";
		lStatement += " WHERE FASC_FIGLIO.ID_FASCICOLO_SIUS is not null AND FASC2.ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = FASCICOLO_FIGLIO2.ID_FASCICOLO_SIUS_ORIGINE) ";
		lStatement += " NUM_FIGLI, null ID_FASCICOLO_SIUS_FIGLIO, null ID_FASCICOLO_SIUS_ORIGINE ";

		lStatement += " FROM ESECUZIONE_MISURA_SICUREZZA EMS, GENERALE_PROCEDIMENTO GP_PADRE, GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC_PADRE, FASCICOLO_SIUS FASC, SOGGETTO  SOGG, CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO, CG_REF_CODES DESCR_MISURA_SICUREZZA, DEPOSITO_DECRETO DD ";
		lStatement += " ,FASCICOLO_SIUS FASC_FIGLIO ";
		lStatement += " WHERE EMS.ID_ESECUZIONE_MISURA_SICUREZZA = '" + aEseMSKey + "' ";
		lStatement += " AND EMS.GEN_PRID_GENERALE_PROCEDIMENTO = GP_PADRE.ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND GP_PADRE.ANNO_S1 = GP.ANNO_S1";
		lStatement += " AND GP_PADRE.PROGR_S1 = GP.PROGR_S1";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO != 'U024'";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND DESCR_MISURA_SICUREZZA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND EMS.COD_TIPO_MISURA = DESCR_MISURA_SICUREZZA.RV_LOW_VALUE  ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND (EV.DATA_INSERIMENTO,FASC.ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
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
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE(+) = FASC.ID_FASCICOLO_SIUS";
		return lStatement;
	}

	protected String getFascicoliSenzaProvvedimentiDellaMisura(BigDecimal aEseMSKey,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, ";
		lStatement += " '-' DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, '-' DESCR_COMUNE_NASCITA, ";
		lStatement += " '-' COD_PROVINCIA_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, ";
		lStatement += " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, DESCR_MISURA_SICUREZZA.RV_MEANING DESCR_MISURA_SICUREZZA, ";
		lStatement += " '-' COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, null DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-'  DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, ";
		lStatement += " NVL(EMS.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += ",0 NUM_FIGLI, null ID_FASCICOLO_SIUS_FIGLIO, null ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += "FROM ESECUZIONE_MISURA_SICUREZZA EMS, GENERALE_PROCEDIMENTO GP_PADRE, GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC_PADRE, FASCICOLO_SIUS FASC, ";
		lStatement += " SOGGETTO  SOGG, UFFICIO UFF, COMUNE DESCR_COM_UFF, CG_REF_CODES DESCR_COD_PROCEDIMENTO, CG_REF_CODES DESCR_MISURA_SICUREZZA ";
		lStatement += " WHERE EMS.ID_ESECUZIONE_MISURA_SICUREZZA = '" + aEseMSKey + "' ";
		lStatement += " AND EMS.GEN_PRID_GENERALE_PROCEDIMENTO = GP_PADRE.ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND GP_PADRE.ANNO_S1 = GP.ANNO_S1";
		lStatement += " AND GP_PADRE.PROGR_S1 = GP.PROGR_S1";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO != 'U024'";
		lStatement += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_MISURA_SICUREZZA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND EMS.COD_TIPO_MISURA = DESCR_MISURA_SICUREZZA.RV_LOW_VALUE  ";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO  ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND (FASC.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS  ";
		lStatement += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV ";
		lStatement += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
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

	protected String getFascicoliFigliConProvvedimenti(BigDecimal aIdFascicolo,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, DESCR_MISURA_SICUREZZA.RV_MEANING DESCR_MISURA_SICUREZZA, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, EV.DATA_EMISSIONE DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, ";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE, ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, ";
		// lStatement += " NVL(EMS.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += " NVL(DD.LUOGO_SVOLGIMENTO_PROVA, '') LUOGO_ESECUZIONE_MISURA, FASC.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += " FROM ESECUZIONE_MISURA_SICUREZZA EMS, GENERALE_PROCEDIMENTO GP_PADRE, GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC_PADRE, FASCICOLO_SIUS FASC, SOGGETTO  SOGG, CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO, CG_REF_CODES DESCR_MISURA_SICUREZZA, DEPOSITO_DECRETO DD  ";
		lStatement += " WHERE EMS.GEN_PRID_GENERALE_PROCEDIMENTO = GP_PADRE.ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicolo + "' ";
		lStatement += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND GP_PADRE.ANNO_S1 = GP.ANNO_S1";
		lStatement += " AND GP_PADRE.PROGR_S1 = GP.PROGR_S1";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO != 'U024'";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND DESCR_MISURA_SICUREZZA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND EMS.COD_TIPO_MISURAA = DESCR_MISURA_SICUREZZA.RV_LOW_VALUE  ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND (EV.DATA_INSERIMENTO,FASC.ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
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

		return lStatement;
	}

	protected String getFascicoliFigliSenzaProvvedimenti(BigDecimal aIdFascicolo,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, ";
		lStatement += " '-' DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, '-' DESCR_COMUNE_NASCITA, ";
		lStatement += " '-' COD_PROVINCIA_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, ";
		lStatement += " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, DESCR_MISURA_SICUREZZA.RV_MEANING DESCR_MISURA_SICUREZZA, ";
		lStatement += " '-' COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, null DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-'  DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, ";
		lStatement += " NVL(EMS.LUOGO_ESECUZIONE_MISURA, '') LUOGO_ESECUZIONE_MISURA, FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += "FROM ESECUZIONE_MISURA_SICUREZZA EMS, GENERALE_PROCEDIMENTO GP_PADRE, GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC_PADRE, FASCICOLO_SIUS FASC, ";
		lStatement += " SOGGETTO  SOGG, UFFICIO UFF, COMUNE DESCR_COM_UFF, CG_REF_CODES DESCR_COD_PROCEDIMENTO, CG_REF_CODES DESCR_MISURA_SICUREZZA ";
		lStatement += " WHERE EMS.GEN_PRID_GENERALE_PROCEDIMENTO = GP_PADRE.ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicolo + "' ";
		lStatement += " AND FASC_PADRE.ID_FASCICOLO_SIUS = GP_PADRE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND GP_PADRE.ANNO_S1 = GP.ANNO_S1";
		lStatement += " AND GP_PADRE.PROGR_S1 = GP.PROGR_S1";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO != 'U024'";
		lStatement += " AND FASC_PADRE.CHIAVE_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_MISURA_SICUREZZA.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND EMS.COD_TIPO_MISURA = DESCR_MISURA_SICUREZZA.RV_LOW_VALUE  ";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO  ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND (FASC.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS  ";
		lStatement += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV ";
		lStatement += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
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

	// Fase di ricerca Procedimenti Correlati alla Misura.
	protected String getFascicoliCorrelatiConProvvedimentiDellaMisura(BigDecimal aIdFascicoloEMS,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();
		lStatement += "SELECT FASC_FIGLIO.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC_FIGLIO.CHIAVE_ANNO CHIAVE_ANNO, FASC_FIGLIO.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC_FIGLIO.CHIAVE_PROGR CHIAVE_PROGR, FASC_FIGLIO.CHIAVE_UFFICIO CHIAVE_UFFICIO, FASC_FIGLIO.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP_FIGLIO.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP_FIGLIO.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP_FIGLIO.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP_FIGLIO.DESCR_MITTENTE, '-') DESCR_MITTENTE, '-' DESCR_MISURA_SICUREZZA, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, EV.DATA_EMISSIONE DATA_RICHIESTA, GP_FIGLIO.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, GP_FIGLIO.ANNO_S1, GP_FIGLIO.PROGR_S1, ";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE, ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, ";
		lStatement += " NVL(DD.LUOGO_SVOLGIMENTO_PROVA, '') LUOGO_ESECUZIONE_MISURA ";
		lStatement += ",0 NUM_FIGLI, null ID_FASCICOLO_SIUS_FIGLIO, FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += " FROM GENERALE_PROCEDIMENTO GP_FIGLIO, FASCICOLO_SIUS FASC, SOGGETTO  SOGG, CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO, DEPOSITO_DECRETO DD ";
		lStatement += " ,FASCICOLO_SIUS FASC_FIGLIO ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloEMS + "' ";
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE(+) = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS = GP_FIGLIO.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND (EV.DATA_INSERIMENTO,FASC_FIGLIO.ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02'  OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += "	where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS ";
		lStatement += "	AND (EV3.COD_TIPO_PROVVEDIMENTO = '02' OR EV3.COD_TIPO_PROVVEDIMENTO = '03')) ) ";
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

	// Fase di ricerca Procedimenti Correlati alla Misura.
	protected String getFascicoliCorrelatiSenzaProvvedimentiDellaMisura(BigDecimal aIdFascicoloEMS,
			String lUfficioUtenteConnesso) {
		String lStatement = new String();
		lStatement += "SELECT FASC_FIGLIO.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC_FIGLIO.CHIAVE_ANNO CHIAVE_ANNO, FASC_FIGLIO.DATA_ISCRIZIONE DATA_ISCRIZIONE, SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC_FIGLIO.CHIAVE_PROGR CHIAVE_PROGR, FASC_FIGLIO.CHIAVE_UFFICIO CHIAVE_UFFICIO, FASC_FIGLIO.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " '-' DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " '-' DESCR_COMUNE_NASCITA, '-' COD_PROVINCIA_NASCITA,";
		lStatement += " GP_FIGLIO.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP_FIGLIO.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP_FIGLIO.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP_FIGLIO.DESCR_MITTENTE, '-') DESCR_MITTENTE, '-' DESCR_MISURA_SICUREZZA, ";
		lStatement += " '-' COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO DESCR_STATO_FASCICOLO, null DATA_RICHIESTA, GP_FIGLIO.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, GP_FIGLIO.ANNO_S1, GP_FIGLIO.PROGR_S1, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-' DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, '-' LUOGO_ESECUZIONE_MISURA ";
		lStatement += ",0 NUM_FIGLI, null ID_FASCICOLO_SIUS_FIGLIO, FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE ID_FASCICOLO_SIUS_ORIGINE ";
		lStatement += " FROM GENERALE_PROCEDIMENTO GP_FIGLIO, FASCICOLO_SIUS FASC, SOGGETTO  SOGG, CG_REF_CODES DESCR_COD_PROCEDIMENTO, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, FASCICOLO_SIUS FASC_FIGLIO ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloEMS + "' ";
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS_ORIGINE(+) = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND FASC_FIGLIO.ID_FASCICOLO_SIUS = GP_FIGLIO.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";
		lStatement += " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND (FASC_FIGLIO.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS  ";
		lStatement += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV ";
		lStatement += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP_FIGLIO.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02'  OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC_FIGLIO.ID_FASCICOLO_SIUS  ";
		lStatement += " AND (EV3.COD_TIPO_PROVVEDIMENTO = '02'  OR EV3.COD_TIPO_PROVVEDIMENTO = '03') ) ) ";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03')  ";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS  ) ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP_FIGLIO.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC_FIGLIO.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		return lStatement;
	}

}
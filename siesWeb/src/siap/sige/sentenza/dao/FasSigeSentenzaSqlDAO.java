package siap.sige.sentenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.sentenza.model.FasSigeSentenzaModel;
import siap.sige.sentenza.model.FasSigeSentenzaRicercaModel;

/**
 * <p>
 * Title: FasSigeSentenzaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella FasSigeSentenza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class FasSigeSentenzaSqlDAO extends SIAPSqlDAO {

	public FasSigeSentenzaSqlDAO(Connection aCon) {
		super(aCon);
	}

	protected String getFasSigeSentenzaSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT ID_FAS_SIGE_SENTENZA, " + "SEN_ID_SENTENZA, "
				+ "ID_SENTENZA, COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
				+ "ANNO_REGE_PM, NUMERO_REGE_PM, DATA_ARRIVO_ATTO, DATA_ISCRIZIONE, DATA_PROVVEDIMENTO, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "ANNO_SENTENZA, NUMERO_SENTENZA, DATA_IRREVOCABILITA, FLAG_SENTENZA_APPLICAZ_PENA, "
				+ "COD_TIPO_PROVV_RIF, TIPO_PROVV_RIF.RV_MEANING DESCR_TIPO_PROVV_RIF, DATA_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, TIPO_AUTORITA_PROVV_RIF.RV_MEANING DESCR_TIPO_AUTORITA_PROVV_RIF, "
				+ "ANNO_PROVV_RIF, NUMERO_PROVV_RIF, COD_LUOGO_PROVV_RIF, LUOGO_PROVV_RIF.DESCRIZIONE DESCR_LUOGO_PROVV_RIF, "
				+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, COD_TIPO_DECISIONE_CASSAZIONE, TIPO_DECISIONE_CASSAZIONE.RV_MEANING DESCR_TIPO_DECISIONE_CASS, "
				+ "NOTE1_DECISIONE_CASSAZIONE, NOTE2_DECISIONE_CASSAZIONE, ANNO_SENTENZA_CASSAZIONE, NUMERO_SENTENZA_CASSAZIONE, "
				+ "ANNO_RACCOLTA_GENERALE, NUMERO_RACCOLTA_GENERALE, FLAG_ALTRE_SENTENZE, DESCR_ALTRE_SENTENZE, "
				+ "ANNO_REGISTRO_35, NUM_REGISTRO_35, NOTE, DESCR_NUM_CAMPIONE_PENALE, "
				+ "ANNO_REGE_GIP, NUMERO_REGE_GIP, ANNO_REGE_DIB, NUMERO_REGE_DIB, ANNO_REGE_CAS, NUMERO_REGE_CAS, "
				+ "ANNO_REGE_CAP, NUMERO_REGE_CAP, ANNO_REGE_CASAP, NUMERO_REGE_CASAP, "
				// MEV_66: aggiunte quattro nuove proprietà
				+ "ANNO_REGE_GUP, NUMERO_REGE_GUP, ANNO_REGE_CAPSM, NUMERO_REGE_CAPSM, "
				+ "COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, "
				+ "COD_BILANCIAMENTO_CIRCOSTANZE, DECOBILAN.RV_MEANING DESCBILAN, FLAG_GIUDIZIO_ABBREVIATO, COD_TIPO_RITO ";
		lStatement += " FROM FAS_SIGE_SENTENZA, SENTENZA, CG_REF_CODES TIPO_PROVVEDIMENTO, CG_REF_CODES TIPO_AUTORITA_EMITTENTE,";
		lStatement += " COMUNE LUOGO_EMITTENTE, CG_REF_CODES TIPO_PROVV_RIF, CG_REF_CODES TIPO_AUTORITA_PROVV_RIF,";
		lStatement += " COMUNE LUOGO_PROVV_RIF, CG_REF_CODES TIPO_DECISIONE_CASSAZIONE, CG_REF_CODES DECOBILAN ";
		lStatement += " WHERE SEN_ID_SENTENZA = ID_SENTENZA ";
		lStatement += " AND (TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO)";
		lStatement += " AND (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE)";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE)";
		lStatement += " AND (DECOBILAN.RV_DOMAIN='BILANCIAMENTO_CIRCOSTANZE' AND DECOBILAN.RV_LOW_VALUE=COD_BILANCIAMENTO_CIRCOSTANZE) ";
		lStatement += " AND (TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_PROVV_RIF)";
		lStatement += " AND (TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN ='TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_AUTORITA_PROVV_RIF)";
		lStatement += " AND (LUOGO_PROVV_RIF.COD_COMUNE = COD_LUOGO_PROVV_RIF)";
		lStatement += " AND (TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE)";

		return lStatement;
	}

	protected String getNumeroFasSigeSentenzaSqlQuery(String majorOffice) {
		String lStatement = new String("");

		lStatement += "SELECT COUNT (*) NUM_FASCICOLI_SIGE," + "SEN_ID_SENTENZA ";
		lStatement += " FROM SENTENZA, FAS_SIGE_SENTENZA";
		// MEV_57: aggiunto parametro di passaggio
		if (StringUtils.checkValidValue(majorOffice))
			lStatement += " LEFT OUTER JOIN V_SOGSIGE_ETA vse ON (FAS_SIGE_SENTENZA.FAS_ID_FASCICOLO_SIGE = vse.ID_FASCICOLO_SIGE), fascicolo_sige s";
		lStatement += " WHERE ID_SENTENZA = SEN_ID_SENTENZA ";

		return lStatement;
	}

	/**
	 * Restituisce un vettore di FasSigeSentenza corrispondente alle condizioni impostate all'interno di
	 * SentenzaModel, raggruppando per ID_SENTENZA. passata come input.
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaSentenzePerSIGEPaged(SentenzaModel aModel, int aPage, String majorOffice)
			throws DAOException {

		String lStatement = getNumeroFasSigeSentenzaSqlQuery(majorOffice);

		String lPaginedStatement = new String("");

		// Condizioni di query
		lStatement += " " + setCondizioni(aModel);

		// MEV_57: aggiunta condizione
		if (StringUtils.checkValidValue(majorOffice)) {
			// 20181031: aggiunto parametro di passaggio
			lStatement += /* MinorMask.minorCondition("vse", "FAS_SIGE_SENTENZA", majorOffice); */
					" and ((nvl(vse.ETA_SOGGETTO_ORA, 18) >= 18) or (FAS_SIGE_SENTENZA.cod_ufficio_inserimento = '"
							+ majorOffice + "'))";
			lStatement += " and s.sog_id_soggetto = vse.COD_SOGGETTO"
					+ " and s.id_fascicolo_sige = FAS_SIGE_SENTENZA.FAS_ID_FASCICOLO_SIGE";
		}

		// Raggruppamento
		lStatement += " GROUP BY SEN_ID_SENTENZA ";

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/**
	 * Set condizione sulla query di SQL
	 *
	 * @param aModel
	 * @return String
	 */
	private String setCondizioni(SentenzaModel aModel) {
		String lCondizioni = new String();

		if (aModel.getCodUfficioInserimento() != null) {
			lCondizioni += " AND FAS_SIGE_SENTENZA.COD_UFFICIO_INSERIMENTO = '"
					+ aModel.getCodUfficioInserimento() + "' ";
		}

		if (aModel.getDataProvvedimentoIniziale() != null)
			lCondizioni += " AND TO_CHAR(SENTENZA.DATA_PROVVEDIMENTO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimentoIniziale(), "yyyyMMdd") + "'";

		if (aModel.getDataProvvedimentoFinale() != null)
			lCondizioni += " AND TO_CHAR(SENTENZA.DATA_PROVVEDIMENTO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimentoFinale(), "yyyyMMdd") + "'";

		if (aModel.getDataProvvedimento() != null)
			// 20181031: modificata query
			// lCondizioni += " AND TO_CHAR(SENTENZA.DATA_PROVVEDIMENTO,'YYYYMMDD') = '"
			// + DateUtils.getDateToString(aModel.getDataProvvedimento(), "yyyyMMdd") + "'";
			lCondizioni += " AND TO_CHAR(SENTENZA.DATA_PROVVEDIMENTO,'YYYYMMDD') between '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimento(), "yyyyMMdd") + "'" + " and '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimento(), "yyyyMMdd") + "'";

		if (aModel.getDataIrrevocabilitaIniziale() != null)
			lCondizioni += " AND TO_CHAR(SENTENZA.DATA_IRREVOCABILITA,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aModel.getDataIrrevocabilitaIniziale(), "yyyyMMdd") + "'";

		if (aModel.getDataIrrevocabilitaFinale() != null)
			lCondizioni += " AND TO_CHAR(SENTENZA.DATA_IRREVOCABILITA,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aModel.getDataIrrevocabilitaFinale(), "yyyyMMdd") + "'";

		if ((aModel.getAnnoSentenza() != null))
			lCondizioni += " AND SENTENZA.ANNO_SENTENZA = " + aModel.getAnnoSentenza() + "";

		if ((aModel.getNumeroSentenza() != null) && (!aModel.getNumeroSentenza().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_SENTENZA = '" + aModel.getNumeroSentenza() + "'";

		if ((aModel.getAnnoRegeCap() != null))
			lCondizioni += " AND SENTENZA.ANNO_REGE_CAP = " + aModel.getAnnoRegeCap() + "";

		if ((aModel.getNumeroRegeCap() != null) && (!aModel.getNumeroRegeCap().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_REGE_CAP = '" + aModel.getNumeroRegeCap() + "'";

		if ((aModel.getAnnoRegeGip() != null))
			lCondizioni += " AND SENTENZA.ANNO_REGE_GIP = " + aModel.getAnnoRegeGip() + "";

		if ((aModel.getNumeroRegeGip() != null) && (!aModel.getNumeroRegeGip().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_REGE_GIP = '" + aModel.getNumeroRegeGip() + "'";

		if ((aModel.getAnnoRegeCas() != null))
			lCondizioni += " AND SENTENZA.ANNO_REGE_CAS = " + aModel.getAnnoRegeCas() + "";

		if ((aModel.getNumeroRegeCas() != null) && (!aModel.getNumeroRegeCas().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_REGE_CAS = '" + aModel.getNumeroRegeCas() + "'";

		if ((aModel.getAnnoRegeDib() != null))
			lCondizioni += " AND SENTENZA.ANNO_REGE_DIB= " + aModel.getAnnoRegeDib() + "";

		if ((aModel.getNumeroRegeDib() != null) && (!aModel.getNumeroRegeDib().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_REGE_DIB = '" + aModel.getNumeroRegeDib() + "'";

		if ((aModel.getAnnoRegeCasap() != null))
			lCondizioni += " AND SENTENZA.ANNO_REGE_CASAP= " + aModel.getAnnoRegeCasap() + "";

		if ((aModel.getNumeroRegeCasap() != null) && (!aModel.getNumeroRegeCasap().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_REGE_CASAP ='" + aModel.getNumeroRegeCasap() + "'";

		if ((aModel.getAnnoRegePm() != null))
			lCondizioni += " AND SENTENZA.ANNO_REGE_PM = " + aModel.getAnnoRegePm() + "";

		if ((aModel.getNumeroRegePm() != null) && (!aModel.getNumeroRegePm().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_REGE_PM = '" + aModel.getNumeroRegePm() + "'";

		if ((aModel.getCodTipoAutoritaEmittente() != null)
				&& (!aModel.getCodTipoAutoritaEmittente().equals("")))
			lCondizioni += " AND SENTENZA.COD_TIPO_AUTORITA_EMITTENTE = '"
					+ aModel.getCodTipoAutoritaEmittente() + "'";

		if ((aModel.getCodLuogoEmittente() != null) && (!aModel.getCodLuogoEmittente().equals("")))
			lCondizioni += " AND SENTENZA.COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "'";

		// MEV_66: aggiunte quattro nuove proprietà
		if ((aModel.getAnnoRegeGup() != null))
			lCondizioni += " AND SENTENZA.ANNO_REGE_GUP = " + aModel.getAnnoRegeGup() + "";

		if ((aModel.getNumeroRegeGup() != null) && (!aModel.getNumeroRegeGup().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_REGE_GUP = '" + aModel.getNumeroRegeGup() + "'";

		if ((aModel.getAnnoRegeCapsm() != null))
			lCondizioni += " AND SENTENZA.ANNO_REGE_CAPSM = " + aModel.getAnnoRegeCapsm() + "";

		if ((aModel.getNumeroRegeCapsm() != null) && (!aModel.getNumeroRegeCapsm().equals("")))
			lCondizioni += " AND SENTENZA.NUMERO_REGE_CAPSM = '" + aModel.getNumeroRegeCapsm() + "'";

		return lCondizioni;
	}

	/**
	 * Set condizione sulla query di SQL
	 *
	 * @param aModel
	 * @return String
	 */
	// private String setCondizionByKey(BigDecimal aId) {
	// String lCondizioni = " AND ID_SENTENZA = " + aId;
	// return lCondizioni;
	// }

	/**
	 * Set condizione sulla query SQL per Cod Ufficio
	 *
	 * @param aModel
	 * @return String
	 */
	private String setCondizioneUfficio(SentenzaModel aModel) {
		String lCondizioni = new String();

		if (aModel.getCodUfficioInserimento() != null) {
			lCondizioni += " AND FAS_SIGE_SENTENZA.COD_UFFICIO_INSERIMENTO = '"
					+ aModel.getCodUfficioInserimento() + "' ";
		}
		return lCondizioni;
	}

	/**
	 * Restituisce la rappresentazione dei dati selezionati in Model
	 *
	 * @return GenericModel
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		FasSigeSentenzaRicercaModel lModel = new FasSigeSentenzaRicercaModel();

		lModel.setNumFascicoliSige(getBigDecimal("NUM_FASCICOLI_SIGE"));
		lModel.getFasSigeSentenzaModel().setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));

		return lModel;
	}

	public void getCountFasSigeSentenze(SentenzaModel aModel) throws DAOException {
		String lStatement = getNumeroSentenzeSigeSqlQuery();

		// Condizioni di query
		lStatement += " " + setCondizioni(aModel);

		// Raggruppamento
		lStatement += " GROUP BY SEN_ID_SENTENZA )";

		setStatement(lStatement);
	}

	protected String getNumeroSentenzeSigeSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT COUNT (*) HowManyRecords ";
		lStatement += " FROM SENTENZA ";
		lStatement += " WHERE ID_SENTENZA in ";
		lStatement += " (SELECT sen_id_sentenza ";
		lStatement += " FROM sentenza, fas_sige_sentenza ";
		lStatement += " WHERE id_sentenza = sen_id_sentenza ";

		return lStatement;
	}

	/**
	 * Restituisce un vettore di FasSigeSentenza corrispondente ad un particolare IdSentenza e con il codice
	 * ufficio SIGE impostato eventualmente in SentenzaModel
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFasSIGEPerIdSentenza(SentenzaModel aModel, String majorOffice) throws DAOException {

		// 20181031: aggiunto parametro di passaggio
		String lStatement = getFasSigePerIdSentenzaSqlQuery(aModel, majorOffice);

		// Condizioni di query
		lStatement += " " + setCondizioneUfficio(aModel);

		// MEV_57: aggiunta condizione
		if (StringUtils.checkValidValue(majorOffice)) {
			// 20181031: aggiunto parametro di passaggio
			lStatement += /* MinorMask.minorCondition("vse", "FAS_SIGE_SENTENZA", majorOffice); */
					" and ((nvl(vse.ETA_SOGGETTO_ORA, 18) >= 18) or (FAS_SIGE_SENTENZA.cod_ufficio_inserimento = '"
							+ majorOffice + "'))";
			lStatement += " and sog_id_soggetto = vse.COD_SOGGETTO";
			// + " and id_fascicolo_sige = FAS_SIGE_SENTENZA.FAS_ID_FASCICOLO_SIGE";
		}

		setStatement(lStatement);
	}

	/**
	 * 20181031: aggiunto parametro di passaggio
	 *
	 * @param aModel
	 * @param majorOffice
	 * @return
	 */
	protected String getFasSigePerIdSentenzaSqlQuery(SentenzaModel aModel, String majorOffice) {

		String lStatement = new String("");

		lStatement += "SELECT s.ID_FASCICOLO_SIGE, ID_SOGGETTO";
		lStatement += " FROM FASCICOLO_SIGE s, FAS_SIGE_SENTENZA";
		// MEV_57: aggiunto parametro di passaggio
		if (StringUtils.checkValidValue(majorOffice))
			lStatement += " LEFT OUTER JOIN V_SOGSIGE_ETA vse ON (FAS_SIGE_SENTENZA.FAS_ID_FASCICOLO_SIGE = vse.ID_FASCICOLO_SIGE), SOGGETTO";
		else
			lStatement += " , SOGGETTO";

		lStatement += " WHERE SEN_ID_SENTENZA = '" + aModel.getIdSentenza() + "'";
		lStatement += " AND   FAS_ID_FASCICOLO_SIGE = s.ID_FASCICOLO_SIGE ";
		lStatement += " AND   SOG_ID_SOGGETTO = ID_SOGGETTO ";

		return lStatement;
	}

	/**
	 * Restituisce un vettore di FasSigeSentenza corrispondente ad un particolare IdSentenza e con il codice
	 * ufficio SIGE impostato eventualmente in SentenzaModel
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFasSigeSentenzaPerIdSentenza(SentenzaModel aModel, BigDecimal aIdFascicoloSige)
			throws DAOException {
		String lStatement = getFasSigeSentenzaPerIdSentenzaSqlQuery(aModel, aIdFascicoloSige);

		// Condizioni di query
		lStatement += " " + setCondizioneUfficio(aModel);

		setStatement(lStatement);
	}

	protected String getFasSigeSentenzaPerIdSentenzaSqlQuery(SentenzaModel aModel,
			BigDecimal aIdFascicoloSige) {
		String lStatement = new String("");

		lStatement += "SELECT ID_FAS_SIGE_SENTENZA,FAS_ID_FASCICOLO_SIGE,SEN_ID_SENTENZA,ID_FASCICOLO_SIGE, ID_SOGGETTO ";
		lStatement += " FROM FAS_SIGE_SENTENZA A, FASCICOLO_SIGE B, SOGGETTO C";
		lStatement += " WHERE A.SEN_ID_SENTENZA = '" + aModel.getIdSentenza() + "'";
		lStatement += " AND A.FAS_ID_FASCICOLO_SIGE = B.ID_FASCICOLO_SIGE ";
		lStatement += " AND B.SOG_ID_SOGGETTO = C.ID_SOGGETTO ";
		lStatement += " AND B.ID_FASCICOLO_SIGE = " + aIdFascicoloSige;

		return lStatement;
	}

	public GenericModel getFasSigePerIdSentenza() throws DAOException {
		FascicoloSigeModel fasSigeModel = new FascicoloSigeModel();

		fasSigeModel.setIdFascicoloSige(getBigDecimal("ID_FASCICOLO_SIGE"));
		fasSigeModel.setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		return fasSigeModel;
	}

	public GenericModel getFasSigeSentenzaPerIdSentenza() throws DAOException {
		FasSigeSentenzaModel fasSigeModel = new FasSigeSentenzaModel();
		fasSigeModel.setIdFasSigeSentenza(getBigDecimal("ID_FAS_SIGE_SENTENZA"));
		fasSigeModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		fasSigeModel.setFasIdFascicoloSige(getBigDecimal("FAS_ID_FASCICOLO_SIGE"));
		return fasSigeModel;
	}

	public void ricercaSentenzaByIdFascicolo(BigDecimal aIdFascolo) throws DAOException {
		String lStatement = getSentenzaByFascSqlQuery(aIdFascolo);

		setStatement(lStatement);
	}

	protected String getSentenzaByFascSqlQuery(BigDecimal aIdFascolo) {
		String lStatement = new String("");

		lStatement += " SELECT S.ANNO_SENTENZA, S.NUMERO_SENTENZA, ";
		lStatement += " S.COD_TIPO_AUTORITA_EMITTENTE, TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, ";
		lStatement += " S.COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE ";
		lStatement += " FROM FAS_SIGE_SENTENZA FSS, SENTENZA S, ";
		lStatement += " CG_REF_CODES TIPO_AUTORITA_EMITTENTE, COMUNE LUOGO_EMITTENTE ";
		lStatement += " WHERE FSS.SEN_ID_SENTENZA = S.ID_SENTENZA ";
		lStatement += " AND FSS.FAS_ID_FASCICOLO_SIGE = " + aIdFascolo;
		lStatement += " AND FSS.FLAG_COMPETENZA = 'S' ";
		lStatement += " AND (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE)";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE)";

		return lStatement;
	}

	public GenericModel getSentenzaByFascicolo() throws DAOException {
		SentenzaModel sentenzaModel = new SentenzaModel();

		sentenzaModel.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		;
		sentenzaModel.setNumeroSentenza(getString("NUMERO_SENTENZA"));
		sentenzaModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		sentenzaModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));

		return sentenzaModel;
	}

}
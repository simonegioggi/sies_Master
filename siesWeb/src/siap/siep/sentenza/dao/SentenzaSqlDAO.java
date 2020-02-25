package siap.siep.sentenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.sentenza.model.SentenzaModel;

/**
 * <p>
 * Title: SentenzaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Sentenza
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
public class SentenzaSqlDAO extends SIAPSqlDAO {

	// MEV 16: aggiunta variabile di classe
	private boolean isThisForCumulo = false;

	public SentenzaSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void getCountSentenze(SentenzaModel aModel) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords ";

		lStatement += " FROM SENTENZA, CG_REF_CODES TIPO_PROVVEDIMENTO, CG_REF_CODES TIPO_AUTORITA_EMITTENTE,";
		lStatement += " COMUNE LUOGO_EMITTENTE, CG_REF_CODES TIPO_PROVV_RIF, CG_REF_CODES TIPO_AUTORITA_PROVV_RIF,";
		lStatement += " COMUNE LUOGO_PROVV_RIF, CG_REF_CODES TIPO_DECISIONE_CASSAZIONE, CG_REF_CODES DECOBILAN, UFFICIO_ACCORPATO UA ";

		lStatement += " WHERE (TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO)";
		lStatement += " AND (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE)";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE)";
		lStatement += " AND (DECOBILAN.RV_DOMAIN='BILANCIAMENTO_CIRCOSTANZE' AND DECOBILAN.RV_LOW_VALUE=COD_BILANCIAMENTO_CIRCOSTANZE) ";

		lStatement += " AND (TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_PROVV_RIF)";
		lStatement += " AND (TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN ='TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_AUTORITA_PROVV_RIF)";
		lStatement += " AND (LUOGO_PROVV_RIF.COD_COMUNE = COD_LUOGO_PROVV_RIF)";
		lStatement += " AND (TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE)";
		lStatement += " AND (COD_UFFICIO_INSERIMENTO = UA.COD_UFFICIO(+))";
		lStatement += " AND  NVL(UA.COD_UFFICIO_NEW, COD_UFFICIO_INSERIMENTO) = '"
				+ aModel.getCodUfficioInserimento() + "' ";

		lStatement += setCondizioni(aModel);

		setStatement(lStatement);
	}

	public void countElencoTitoliEsecutiviIscrittiaSIGE(BigDecimal idFascicolo) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords "
				+ "FROM SENTENZA, CG_REF_CODES TIPO_PROVVEDIMENTO, "
				+ "CG_REF_CODES TIPO_AUTORITA_EMITTENTE, COMUNE LUOGO_EMITTENTE, "
				+ "CG_REF_CODES TIPO_PROVV_RIF, CG_REF_CODES TIPO_AUTORITA_PROVV_RIF, "
				+ "COMUNE SEDE_NOTIZIA, CG_REF_CODES TIPO_PROVVEDIMENTO_RIF, "
				+ "CG_REF_CODES TIPO_PROVVEDIMENTO_ALTRO, COMUNE LUOGO_PROVV_RIF, "
				+ "CG_REF_CODES TIPO_DECISIONE_CASSAZIONE, CG_REF_CODES DECOBILAN, "
				+ "UFFICIO_ACCORPATO UA, fas_sige_sentenza c, fascicolo_sige b " + " WHERE "
				+ "(TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO) AND "
				+ "(TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE) AND "
				+ "(LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE) AND "
				+ "(DECOBILAN.RV_DOMAIN='BILANCIAMENTO_CIRCOSTANZE' AND DECOBILAN.RV_LOW_VALUE=COD_BILANCIAMENTO_CIRCOSTANZE) AND "
				+ "(TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_PROVV_RIF) AND "
				+ "(TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN ='TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_AUTORITA_PROVV_RIF) AND "
				+ "(LUOGO_PROVV_RIF.COD_COMUNE = COD_LUOGO_PROVV_RIF) AND "
				+ "(TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE) AND "
				+ "(TIPO_PROVVEDIMENTO_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO_RIF.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO_RIF) AND "
				+ "(TIPO_PROVVEDIMENTO_ALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO_ALTRO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO_ALTRO) AND "
				+ "(SEDE_NOTIZIA.COD_COMUNE = SENTENZA.COD_SEDE_NOTIZIA_REATO) AND (SENTENZA.COD_UFFICIO_INSERIMENTO = UA.COD_UFFICIO(+)) AND "
				+ "b.id_fascicolo_sige = " + idFascicolo.toString() + " and  "
				+ "b.id_fascicolo_sige = c.fas_id_fascicolo_sige and "
				+ "c.sen_id_sentenza = SENTENZA.id_sentenza " + "ORDER BY SENTENZA.DATA_PROVVEDIMENTO ";

		setStatement(lStatement);
	}

	protected String getSentenzaSqlQuery() {
		String lStatement = new String("");

		// MEV 16: aggiunto alias per tabella sentenza
		lStatement += "SELECT " + "ID_SENTENZA, " + "S.COD_TIPO_PROVVEDIMENTO, "
				+ "TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, " + "S.ANNO_REGE_PM, "
				// Paolo Cherubini aggiungo questo campo per ordinare x RGNR
				+ "S.NUMERO_REGE_PM, TO_NUMBER(RTRIM(S.NUMERO_REGE_PM,'NC')) RGNR, " + "S.DATA_ARRIVO_ATTO, "
				+ "S.DATA_ISCRIZIONE, " + "S.DATA_PROVVEDIMENTO, " + "S.COD_TIPO_AUTORITA_EMITTENTE, "
				+ "TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "
				+ "S.COD_LUOGO_EMITTENTE, " + "LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "
				+ "S.NUM_SEZIONE_AUTORITA_EMITTENTE, " + "S.ANNO_SENTENZA, " + "S.NUMERO_SENTENZA, "
				+ "S.ANNO_PROVVEDIMENTO, " + "S.NUMERO_PROVVEDIMENTO, " + "S.DATA_IRREVOCABILITA, "
				+ "FLAG_SENTENZA_APPLICAZ_PENA, " + "S.COD_TIPO_PROVV_RIF, "
				+ "TIPO_PROVV_RIF.RV_MEANING DESCR_TIPO_PROVV_RIF, " + "S.DATA_PROVV_RIF, "
				+ "S.COD_TIPO_AUTORITA_PROVV_RIF, "
				+ "TIPO_AUTORITA_PROVV_RIF.RV_MEANING DESCR_TIPO_AUTORITA_PROVV_RIF, " + "S.ANNO_PROVV_RIF, "
				+ "S.NUMERO_PROVV_RIF, " + "S.COD_LUOGO_PROVV_RIF, "
				+ "LUOGO_PROVV_RIF.DESCRIZIONE DESCR_LUOGO_PROVV_RIF, " + "S.NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "S.COD_TIPO_DECISIONE_CASSAZIONE, "
				+ "TIPO_DECISIONE_CASSAZIONE.RV_MEANING DESCR_TIPO_DECISIONE_CASS, "
				+ "S.NOTE1_DECISIONE_CASSAZIONE, " + "S.NOTE2_DECISIONE_CASSAZIONE, "
				+ "S.ANNO_SENTENZA_CASSAZIONE, " + "S.NUMERO_SENTENZA_CASSAZIONE, "
				+ "S.ANNO_RACCOLTA_GENERALE, " + "S.NUMERO_RACCOLTA_GENERALE, " + "FLAG_ALTRE_SENTENZE, "
				+ "DESCR_ALTRE_SENTENZE, " + "ANNO_REGISTRO_35, " + "NUM_REGISTRO_35, " + "S.NOTE, "
				+ "DESCR_NUM_CAMPIONE_PENALE, " + "ANNO_REGE_GIP, " + "NUMERO_REGE_GIP, " + "ANNO_REGE_DIB, "
				+ "NUMERO_REGE_DIB, " + "ANNO_REGE_CAS, " + "NUMERO_REGE_CAS, " + "ANNO_REGE_CAP, "
				+ "NUMERO_REGE_CAP, " + "ANNO_REGE_CASAP, " + "NUMERO_REGE_CASAP, "
				// MEV_66: aggiunte quattro nuove proprietà
				+ "ANNO_REGE_GUP, " + "NUMERO_REGE_GUP, " + "ANNO_REGE_CAPSM, " + "NUMERO_REGE_CAPSM, "
				+ "S.COD_OPERATORE_INSERIMENTO, " + "S.DATA_INSERIMENTO, " + "S.COD_UFFICIO_INSERIMENTO, "
				+ "S.COD_OPERATORE_AGGIORNAMENTO, " + "S.DATA_AGGIORNAMENTO, "
				+ "S.COD_UFFICIO_AGGIORNAMENTO, "
				+ "COD_BILANCIAMENTO_CIRCOSTANZE, DECOBILAN.RV_MEANING DESCBILAN, "
				+ "FLAG_GIUDIZIO_ABBREVIATO, " + "S.COD_TIPO_RITO, " + "S.COD_TIPO_PROVVEDIMENTO_RIF, "
				+ "S.COD_TIPO_PROVVEDIMENTO_ALTRO, " + "S.COD_SEDE_NOTIZIA_REATO,"
				+ "TIPO_PROVVEDIMENTO_RIF.RV_MEANING DESCR_TIPO_PROVVEDIMENTO_RIF, "
				+ "TIPO_PROVVEDIMENTO_ALTRO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO_ALTRO, "
				+ "SEDE_NOTIZIA.DESCRIZIONE DESCR_SEDE_NOTIZIA_REATO, " + "FLAG_VISIBILITA";
		lStatement += " FROM SENTENZA S, CG_REF_CODES TIPO_PROVVEDIMENTO, CG_REF_CODES TIPO_AUTORITA_EMITTENTE,";
		lStatement += " COMUNE LUOGO_EMITTENTE, CG_REF_CODES TIPO_PROVV_RIF, CG_REF_CODES TIPO_AUTORITA_PROVV_RIF,";
		lStatement += " COMUNE SEDE_NOTIZIA, CG_REF_CODES TIPO_PROVVEDIMENTO_RIF, CG_REF_CODES TIPO_PROVVEDIMENTO_ALTRO,";
		lStatement += " COMUNE LUOGO_PROVV_RIF, CG_REF_CODES TIPO_DECISIONE_CASSAZIONE, CG_REF_CODES DECOBILAN, UFFICIO_ACCORPATO UA ";
		// MEV 16: aggiunto controllo
		if (isThisForCumulo)
			lStatement += ", FASCICOLO_SIEP FS, ISTRUTTORIA_CUMULO IC, SOGGETTO_CUMULATO SC, TITOLO_CUMULATO TC";
		lStatement += " WHERE (TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = S.COD_TIPO_PROVVEDIMENTO)";
		lStatement += " AND (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = S.COD_TIPO_AUTORITA_EMITTENTE)";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = S.COD_LUOGO_EMITTENTE)";
		lStatement += " AND (DECOBILAN.RV_DOMAIN = 'BILANCIAMENTO_CIRCOSTANZE' AND DECOBILAN.RV_LOW_VALUE = COD_BILANCIAMENTO_CIRCOSTANZE) ";
		lStatement += " AND (TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_PROVV_RIF.RV_LOW_VALUE = S.COD_TIPO_PROVV_RIF)";
		lStatement += " AND (TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN ='TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE = S.COD_TIPO_AUTORITA_PROVV_RIF)";
		lStatement += " AND (LUOGO_PROVV_RIF.COD_COMUNE = S.COD_LUOGO_PROVV_RIF)";
		lStatement += " AND (TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = S.COD_TIPO_DECISIONE_CASSAZIONE)";
		lStatement += " AND (TIPO_PROVVEDIMENTO_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO_RIF.RV_LOW_VALUE = S.COD_TIPO_PROVVEDIMENTO_RIF)";
		lStatement += " AND (TIPO_PROVVEDIMENTO_ALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO_ALTRO.RV_LOW_VALUE = S.COD_TIPO_PROVVEDIMENTO_ALTRO)";
		lStatement += " AND (SEDE_NOTIZIA.COD_COMUNE = S.COD_SEDE_NOTIZIA_REATO)";
		lStatement += " AND (S.COD_UFFICIO_INSERIMENTO = UA.COD_UFFICIO(+))";

		// valore di ritorno
		return lStatement;
	}

	protected String getElencoTitoliEscutiviSqlQuery(BigDecimal idFascicolo) {
		String lStatement = new String("");

		lStatement += "SELECT SENTENZA.ID_SENTENZA, " + "SENTENZA.COD_TIPO_PROVVEDIMENTO, "
				+ "TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, " + "ANNO_REGE_PM, "
				+ "NUMERO_REGE_PM, " + "TO_NUMBER(RTRIM(NUMERO_REGE_PM,'NC')) RGNR, "
				+ "SENTENZA.DATA_ARRIVO_ATTO, " + "SENTENZA.DATA_ISCRIZIONE, " + "DATA_PROVVEDIMENTO, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, "
				+ "TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE,"
				+ "COD_LUOGO_EMITTENTE, " + "LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, " + "ANNO_SENTENZA, " + "ANNO_PROVVEDIMENTO, "
				+ "NUMERO_SENTENZA, " + "NUMERO_PROVVEDIMENTO, " + "SENTENZA.DATA_IRREVOCABILITA, "
				+ "FLAG_SENTENZA_APPLICAZ_PENA, " + "COD_TIPO_PROVV_RIF, "
				+ "TIPO_PROVV_RIF.RV_MEANING DESCR_TIPO_PROVV_RIF, " + "DATA_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, "
				+ "TIPO_AUTORITA_PROVV_RIF.RV_MEANING DESCR_TIPO_AUTORITA_PROVV_RIF," + "ANNO_PROVV_RIF, "
				+ "NUMERO_PROVV_RIF, " + "COD_LUOGO_PROVV_RIF, "
				+ "LUOGO_PROVV_RIF.DESCRIZIONE DESCR_LUOGO_PROVV_RIF, " + "NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "COD_TIPO_DECISIONE_CASSAZIONE, "
				+ "TIPO_DECISIONE_CASSAZIONE.RV_MEANING DESCR_TIPO_DECISIONE_CASS, "
				+ "NOTE1_DECISIONE_CASSAZIONE, " + "NOTE2_DECISIONE_CASSAZIONE, "
				+ "ANNO_SENTENZA_CASSAZIONE, " + "NUMERO_SENTENZA_CASSAZIONE, " + "ANNO_RACCOLTA_GENERALE, "
				+ "NUMERO_RACCOLTA_GENERALE, " + "FLAG_ALTRE_SENTENZE, " + "DESCR_ALTRE_SENTENZE, "
				+ "ANNO_REGISTRO_35, " + "NUM_REGISTRO_35, " + "SENTENZA.NOTE, "
				+ "DESCR_NUM_CAMPIONE_PENALE, " + "ANNO_REGE_GIP, " + "NUMERO_REGE_GIP, " + "ANNO_REGE_DIB, "
				+ "NUMERO_REGE_DIB, " + "ANNO_REGE_CAS, " + "NUMERO_REGE_CAS, " + "ANNO_REGE_CAP, "
				+ "NUMERO_REGE_CAP, " + "ANNO_REGE_CASAP, " + "NUMERO_REGE_CASAP, "
				// MEV_66: aggiunte quattro nuove proprietà
				+ "ANNO_REGE_GUP, " + "NUMERO_REGE_GUP, " + "ANNO_REGE_CAPSM, " + "NUMERO_REGE_CAPSM, "
				+ "SENTENZA.COD_OPERATORE_INSERIMENTO, " + "SENTENZA.DATA_INSERIMENTO, "
				+ "SENTENZA.COD_UFFICIO_INSERIMENTO, " + "SENTENZA.COD_OPERATORE_AGGIORNAMENTO, "
				+ "SENTENZA.DATA_AGGIORNAMENTO, " + "SENTENZA.COD_UFFICIO_AGGIORNAMENTO, "
				+ "COD_BILANCIAMENTO_CIRCOSTANZE, " + "DECOBILAN.RV_MEANING DESCBILAN, "
				+ "FLAG_GIUDIZIO_ABBREVIATO, " + "COD_TIPO_RITO, " + "COD_TIPO_PROVVEDIMENTO_RIF, "
				+ "COD_TIPO_PROVVEDIMENTO_ALTRO, " + "COD_SEDE_NOTIZIA_REATO, "
				+ "TIPO_PROVVEDIMENTO_RIF.RV_MEANING DESCR_TIPO_PROVVEDIMENTO_RIF, "
				+ "TIPO_PROVVEDIMENTO_ALTRO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO_ALTRO, "
				// NUOVA INFRASTRUTTURA: aggiunto campo in estrazione
				+ "SEDE_NOTIZIA.DESCRIZIONE DESCR_SEDE_NOTIZIA_REATO, FLAG_VISIBILITA " + "FROM "
				+ "SENTENZA, CG_REF_CODES TIPO_PROVVEDIMENTO, " + "CG_REF_CODES TIPO_AUTORITA_EMITTENTE, "
				+ "COMUNE LUOGO_EMITTENTE, " + "CG_REF_CODES TIPO_PROVV_RIF, "
				+ "CG_REF_CODES TIPO_AUTORITA_PROVV_RIF, " + "COMUNE SEDE_NOTIZIA, "
				+ "CG_REF_CODES TIPO_PROVVEDIMENTO_RIF, " + "CG_REF_CODES TIPO_PROVVEDIMENTO_ALTRO, "
				+ "COMUNE LUOGO_PROVV_RIF, " + "CG_REF_CODES TIPO_DECISIONE_CASSAZIONE, "
				+ "CG_REF_CODES DECOBILAN, " + "UFFICIO_ACCORPATO UA, " + "fas_sige_sentenza c, "
				+ "fascicolo_sige b " + " WHERE "
				+ "(TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO) AND "
				+ "(TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE) AND "
				+ "(LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE) AND "
				+ "(DECOBILAN.RV_DOMAIN='BILANCIAMENTO_CIRCOSTANZE' AND DECOBILAN.RV_LOW_VALUE=COD_BILANCIAMENTO_CIRCOSTANZE) AND "
				+ "(TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_PROVV_RIF) AND "
				+ "(TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN ='TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_AUTORITA_PROVV_RIF) AND "
				+ "(LUOGO_PROVV_RIF.COD_COMUNE = COD_LUOGO_PROVV_RIF) AND "
				+ "(TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE) AND "
				+ "(TIPO_PROVVEDIMENTO_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO_RIF.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO_RIF) AND "
				+ "(TIPO_PROVVEDIMENTO_ALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO_ALTRO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO_ALTRO) AND "
				+ "(SEDE_NOTIZIA.COD_COMUNE = SENTENZA.COD_SEDE_NOTIZIA_REATO) AND (SENTENZA.COD_UFFICIO_INSERIMENTO = UA.COD_UFFICIO(+)) AND "
				+ "b.id_fascicolo_sige = " + idFascicolo.toString() + " and  "
				+ "b.id_fascicolo_sige = c.fas_id_fascicolo_sige and "
				+ "c.sen_id_sentenza = SENTENZA.id_sentenza " + "ORDER BY SENTENZA.DATA_PROVVEDIMENTO ";

		return lStatement;
	}

	/*
	 * protected String getSentenzaFascicoloSqlQuery() { String lStatement = new String("");
	 *
	 * lStatement += "SELECT " + "ID_SENTENZA, "+ "COD_TIPO_PROVVEDIMENTO, "+
	 * "TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "+ "ANNO_REGE_PM, "+ "NUMERO_REGE_PM, "+
	 * "DATA_ARRIVO_ATTO, "+ "DATA_PROVVEDIMENTO, "+ "COD_TIPO_AUTORITA_EMITTENTE, "+
	 * "TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "+ "COD_LUOGO_EMITTENTE, "+
	 * "LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "+ "NUM_SEZIONE_AUTORITA_EMITTENTE, "+
	 * "ANNO_SENTENZA, "+ "NUMERO_SENTENZA, "+ "DATA_IRREVOCABILITA, "+ "FLAG_SENTENZA_APPLICAZ_PENA, "+
	 * "COD_TIPO_PROVV_RIF, "+ "TIPO_PROVV_RIF.RV_MEANING DESCR_TIPO_PROVV_RIF, "+ "DATA_PROVV_RIF, "+
	 * "COD_TIPO_AUTORITA_PROVV_RIF, "+ "TIPO_AUTORITA_PROVV_RIF.RV_MEANING DESCR_TIPO_AUTORITA_PROVV_RIF, "+
	 * "ANNO_PROVV_RIF, "+ "NUMERO_PROVV_RIF, "+ "COD_LUOGO_PROVV_RIF, "+
	 * "LUOGO_PROVV_RIF.DESCRIZIONE DESCR_LUOGO_PROVV_RIF, "+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, "+
	 * "COD_TIPO_DECISIONE_CASSAZIONE, "+ "TIPO_DECISIONE_CASSAZIONE.RV_MEANING DESCR_TIPO_DECISIONE_CASS, "+
	 * "NOTE1_DECISIONE_CASSAZIONE, "+ "NOTE2_DECISIONE_CASSAZIONE, "+ "ANNO_SENTENZA_CASSAZIONE, "+
	 * "NUMERO_SENTENZA_CASSAZIONE, "+ "ANNO_RACCOLTA_GENERALE, "+ "NUMERO_RACCOLTA_GENERALE, "+
	 * "FLAG_ALTRE_SENTENZE, "+ "DESCR_ALTRE_SENTENZE, "+ "ANNO_REGISTRO_35, "+ "NUM_REGISTRO_35, "+
	 * "SEN.NOTE, "+ "DESCR_NUM_CAMPIONE_PENALE, "+ "ANNO_REGE_GIP, "+ "NUMERO_REGE_GIP, "+ "ANNO_REGE_DIB, "+
	 * "NUMERO_REGE_DIB, "+ "ANNO_REGE_CAS, "+ "NUMERO_REGE_CAS, "+ "ANNO_REGE_CAP, "+ "NUMERO_REGE_CAP, "+
	 * "ANNO_REGE_CASAP, "+ "NUMERO_REGE_CASAP, "+ "SEN.COD_OPERATORE_INSERIMENTO, "+ // MEV_66: aggiunte
	 * quattro nuove proprietà "ANNO_REGE_GUP, " + "NUMERO_REGE_GUP, " + "ANNO_REGE_CAPSM, " +
	 * "NUMERO_REGE_CAPSM, "+ "SEN.DATA_INSERIMENTO, "+ "SEN.COD_UFFICIO_INSERIMENTO, "+
	 * "SEN.COD_OPERATORE_AGGIORNAMENTO, "+ "SEN.DATA_AGGIORNAMENTO, "+ "SEN.COD_UFFICIO_AGGIORNAMENTO ";
	 * lStatement +=
	 * " FROM SENTENZA SEN, CG_REF_CODES TIPO_PROVVEDIMENTO, CG_REF_CODES TIPO_AUTORITA_EMITTENTE,";
	 * lStatement +=
	 * " COMUNE LUOGO_EMITTENTE, CG_REF_CODES TIPO_PROVV_RIF, CG_REF_CODES TIPO_AUTORITA_PROVV_RIF,";
	 * lStatement += " COMUNE LUOGO_PROVV_RIF, CG_REF_CODES TIPO_DECISIONE_CASSAZIONE, FASCICOLO_SIEP FAS";
	 * lStatement +=
	 * " WHERE (TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO)"
	 * ; lStatement +=
	 * " AND (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE)"
	 * ; lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE)"; lStatement +=
	 * " AND FAS.SEN_ID_SENTENZA = ID_SENTENZA AND FAS.FLAG_VALIDATO = 'S' "; // Se il COD_UFFICIO_INSERIMENTO
	 * corrisponde ad una PROCURA_GENERALE ('02', '07')
	 *
	 * /*
	 *
	 * if (aModel.getCodUfficioInserimento().length()> 9) { if( aModel.getCodUfficioInserimento().substring(7,
	 * 9).equals("02") || aModel.getCodUfficioInserimento().substring(7, 8).equals("07")) lStatement +=
	 * " AND (TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_PG' AND TIPO_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_PROVV_RIF)"
	 * ; else lStatement +=
	 * " AND (TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_PROVV_RIF)"
	 * ; }
	 */
	/*
	 * lStatement +=
	 * " AND (TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF' AND TIPO_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_PROVV_RIF)"
	 * ; lStatement +=
	 * " AND (TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN ='TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_AUTORITA_PROVV_RIF)"
	 * ; lStatement += " AND (LUOGO_PROVV_RIF.COD_COMUNE = COD_LUOGO_PROVV_RIF)"; lStatement +=
	 * " AND (TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE)"
	 * ;
	 *
	 * return lStatement; }
	 */

	/**
	 * Restituisce un insieme di Sentenze corrispondente alle condizioni impostate all'interno della
	 * SentenzaModel passata come input.
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaSentenzaPaged(SentenzaModel aModel, int aPage) throws DAOException {
		String lStatement = getSentenzaSqlQuery();
		String lPaginedStatement = new String("");

		// Ufficio per la prima query
		if (aModel.getCodUfficioInserimento() != null) {
			lStatement += " AND NVL(UA.COD_UFFICIO_NEW, COD_UFFICIO_INSERIMENTO) = '"
					+ aModel.getCodUfficioInserimento() + "' ";
		}

		lStatement += " " + setCondizioni(aModel);

		if (aModel.getCodOrdinamento().equals("RGNR")) { // Paolo Cherubini 03/03/2011 aggiungo ordinamento
															// per RGNR
			lStatement += " ORDER BY anno_rege_pm, RGNR ";
		} else if (aModel.getCodOrdinamento().equals("default") || aModel.getCodOrdinamento().equals("")
				|| aModel.getCodOrdinamento() == null)
			lStatement += " ORDER BY s.DATA_PROVVEDIMENTO ";

		else {
			if (aModel.getCodOrdinamento().equals("OrdDataIrr") || aModel.getCodOrdinamento() == null)
				lStatement += " ORDER BY s.DATA_IRREVOCABILITA ";
		}

		// Union per fascicoli validati di altri uffici
		// -- lStatement += " UNION ";
		// -- lStatement += getSentenzaFascicoloSqlQuery();
		// -- lStatement += " " + setCondizioni(aModel);
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void ricercaElencoTitoliEsecutiviIscrittiaSIGEPaged(BigDecimal idFascicolo, int aPage)
			throws DAOException {
		String lStatement = getElencoTitoliEscutiviSqlQuery(idFascicolo);
		String lPaginedStatement = new String("");

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void ricercaSentenzaDuplicataPaged(SentenzaModel aModel, int aPage) throws DAOException {
		String lStatement = getSentenzaSqlQuery();
		String lPaginedStatement = new String("");

		ricercaSentenzaDuplicata(aModel);

		lStatement = mStatement;

		if (aModel.getCodOrdinamento().equals("default") || aModel.getCodOrdinamento().equals("")
				|| aModel.getCodOrdinamento() == null)
			lStatement += " ORDER BY s.DATA_PROVVEDIMENTO ";

		else {
			if (aModel.getCodOrdinamento().equals("OrdDataIrr") || aModel.getCodOrdinamento() == null)
				lStatement += " ORDER BY s.DATA_IRREVOCABILITA ";
		}

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void ricercaSentenza(SentenzaModel aModel) throws DAOException {
		String lStatement = getSentenzaSqlQuery();
		// String lPaginedStatement = new String("");

		// Ufficio per la prima query
		if (aModel.getCodUfficioInserimento() != null) {
			lStatement += " AND NVL(UA.COD_UFFICIO_NEW, COD_UFFICIO_INSERIMENTO) = '"
					+ aModel.getCodUfficioInserimento() + "' ";
		}

		lStatement += " " + setCondizioni(aModel);
		lStatement += " ORDER BY s.ANNO_SENTENZA,s.NUMERO_SENTENZA ";
		// Union per fascicoli validati di altri uffici
		// -- lStatement += " UNION ";
		// -- lStatement += getSentenzaFascicoloSqlQuery();
		// -- lStatement += " " + setCondizioni(aModel);

		setStatement(lStatement);
	}

	public void ricercaSentenza(String aNumero, BigDecimal aAnno, String aAutorita, String aLuogo)
			throws DAOException {
		String lStatement = getSentenzaSqlQuery();

		lStatement += " AND COD_TIPO_AUTORITA_EMITTENTE='" + aAutorita + "' ";
		lStatement += " AND COD_LUOGO_EMITTENTE='" + aLuogo + "' ";
		lStatement += " AND ANNO_SENTENZA=" + aAnno + " ";
		lStatement += " AND NUMERO_SENTENZA='" + aNumero + "' ";
		// Union per fascicoli validati di altri uffici
		// -- lStatement += " UNION ";
		// -- lStatement += getSentenzaFascicoloSqlQuery();
		// -- lStatement += " " + setCondizioni(aModel);

		setStatement(lStatement);
	}

	/**
	 * MEV 16: aggiunti parametri di passaggio per differenziare collegato al cumulo
	 *
	 * @param aModel
	 * @param isForCumulo
	 * @param idFascicoloSiep
	 * @param idIstruttoriaCumulo
	 * @throws DAOException
	 */
	public void ricercaSentenzaWebServices(SentenzaModel aModel, boolean isForCumulo, String idFascicoloSiep,
			String idIstruttoriaCumulo) throws DAOException {

		// MEV 16: aggiunto set di variabile di classe preliminare
		if (isForCumulo)
			isThisForCumulo = isForCumulo;

		String lStatement = getSentenzaSqlQuery();
		// MEV 16 CUMULO: eseguita replace
		lStatement = lStatement.replace("SELECT", "SELECT DISTINCT");
		lStatement += " AND S.COD_TIPO_PROVVEDIMENTO='" + aModel.getCodTipoProvvedimento() + "' ";
		lStatement += " AND S.COD_TIPO_AUTORITA_EMITTENTE='" + aModel.getCodTipoAutoritaEmittente() + "' ";
		lStatement += " AND S.ANNO_SENTENZA=" + aModel.getAnnoSentenza() + " ";
		lStatement += " AND S.NUMERO_SENTENZA='" + aModel.getNumeroSentenza() + "' ";

		// Data Sentenza
		if (aModel.getDataProvvedimento() != null) {
			lStatement += " AND TO_CHAR(S.DATA_PROVVEDIMENTO,'YYYYMMDD') = '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimento(), "yyyyMMdd") + "'";
		}

		if (aModel.getCodLuogoEmittente() != null && !aModel.getCodLuogoEmittente().equals("")) {
			lStatement += " AND S.COD_LUOGO_EMITTENTE='" + aModel.getCodLuogoEmittente() + "' ";
		}

		/*
		 * [MEV REL. 5.0] - Tolta Data Irrevocabilita' dalla Sentenza if (aModel.getDataIrrevocabilita() !=
		 * null) { lStatement += " AND TO_CHAR(DATA_IRREVOCABILITA,'YYYYMMDD') = '" +
		 * DateUtils.getDateToString(aModel.getDataIrrevocabilita(), "yyyyMMdd") + "'"; }
		 */

		// Aggiunta la condizione else sui successivi campi 09/07/2009
		if (aModel.getCodTipoProvvRif() != null && !aModel.getCodTipoProvvRif().equals("")) {
			lStatement += " AND S.COD_TIPO_PROVV_RIF='" + aModel.getCodTipoProvvRif() + "' ";
		} else {
			lStatement += " AND S.COD_TIPO_PROVV_RIF IS NULL ";
		}

		if (aModel.getDataProvvRif() != null) {
			lStatement += " AND TO_CHAR(S.DATA_PROVV_RIF,'YYYYMMDD') = '"
					+ DateUtils.getDateToString(aModel.getDataProvvRif(), "yyyyMMdd") + "'";
		} else {
			lStatement += " AND S.DATA_PROVV_RIF IS NULL ";
		}

		if (aModel.getCodTipoAutoritaProvvRif() != null && !aModel.getCodTipoAutoritaProvvRif().equals("")) {
			lStatement += " AND S.COD_TIPO_AUTORITA_PROVV_RIF='" + aModel.getCodTipoAutoritaProvvRif() + "' ";
		} else {
			lStatement += " AND S.COD_TIPO_AUTORITA_PROVV_RIF IS NULL ";
		}

		if (aModel.getAnnoProvvRif() != null) {
			lStatement += " AND S.ANNO_PROVV_RIF='" + aModel.getAnnoProvvRif() + "' ";
		} else {
			lStatement += " AND S.ANNO_PROVV_RIF IS NULL ";
		}

		if (aModel.getNumeroProvvRif() != null && !aModel.getNumeroProvvRif().equals("")) {
			lStatement += " AND S.NUMERO_PROVV_RIF='" + aModel.getNumeroProvvRif() + "' ";
		} else {
			lStatement += " AND S.NUMERO_PROVV_RIF IS NULL ";
		}

		if (aModel.getCodLuogoProvvRif() != null && !aModel.getCodLuogoProvvRif().equals("")) {
			lStatement += " AND S.COD_LUOGO_PROVV_RIF='" + aModel.getCodLuogoProvvRif() + "' ";
		} else {
			lStatement += " AND S.COD_LUOGO_PROVV_RIF IS NULL ";
		}

		if (aModel.getCodTipoDecisioneCassazione() != null
				&& !aModel.getCodTipoDecisioneCassazione().equals("")) {
			lStatement += " AND S.COD_TIPO_DECISIONE_CASSAZIONE='" + aModel.getCodTipoDecisioneCassazione()
					+ "' ";
		} else {
			lStatement += " AND S.COD_TIPO_DECISIONE_CASSAZIONE IS NULL ";
		}

		if (aModel.getAnnoSentenzaCassazione() != null) {
			lStatement += " AND S.ANNO_SENTENZA_CASSAZIONE='" + aModel.getAnnoSentenzaCassazione() + "' ";
		} else {
			lStatement += " AND S.ANNO_SENTENZA_CASSAZIONE IS NULL ";
		}

		if (aModel.getNumeroSentenzaCassazione() != null
				&& !aModel.getNumeroSentenzaCassazione().equals("")) {
			lStatement += " AND S.NUMERO_SENTENZA_CASSAZIONE='" + aModel.getNumeroSentenzaCassazione() + "' ";
		} else {
			lStatement += " AND S.NUMERO_SENTENZA_CASSAZIONE IS NULL ";
		}

		// MEV 16: aggiunte AND condition per gestione sentenza legata a cumulo
		if (isForCumulo) {
			// lStatement += " AND ID_SENTENZA = CU.SEN_ID_SENTENZA";
			// lStatement += " AND S.FLAG_VISIBILITA = 'N'";
			// lStatement += " AND SO.ID_SOGGETTO = FS.SOG_ID_SOGGETTO";
			// lStatement += " AND FS.ID_FASCICOLO_SIEP = CU.FAS_SIE_ID_FASCICOLO_SIEP";
			// lStatement += " AND CU.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep;
			// lStatement += " AND (CU.FLAG_VALIDATO = 'S' OR CU.FLAG_VALIDATO = 'N' OR CU.FLAG_VALIDATO IS
			// NULL)";
			// MERGE REPOSITORY VERSION: 11.3 into 11.3_NEW TODO: controllare correttezza query
			// MEV 16 CUMULO: aggiunte tre LEFT OUTER JOIN
			lStatement += " AND SC.ID_SOGGETTO_ORIGINE = FS.SOG_ID_SOGGETTO(+)";
			lStatement += " AND ID_SENTENZA = FS.SEN_ID_SENTENZA(+)";
			lStatement += " AND FS.SEN_ID_SENTENZA(+) = TC.ID_SENTENZA_ORIGINE";
			lStatement += " AND TC.ISTR_ID_ISTRUTTORIA_CUMULO = IC.ID_ISTRUTTORIA_CUMULO";
			lStatement += " AND IC.ID_ISTRUTTORIA_CUMULO = " + idIstruttoriaCumulo;
			lStatement += " AND IC.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep;
			// FIXME: fine merge 20190416
		}

		setStatement(lStatement);
	}

	public void ricercaSentenzaDuplicataRege(SentenzaModel aSentenza) throws DAOException {
		String lStatement = getSentenzaSqlQuery();

		lStatement += " AND COD_TIPO_PROVVEDIMENTO='" + aSentenza.getCodTipoProvvedimento() + "' ";
		lStatement += " AND COD_TIPO_AUTORITA_EMITTENTE='" + aSentenza.getCodTipoAutoritaEmittente() + "' ";
		lStatement += " AND COD_LUOGO_EMITTENTE='" + aSentenza.getCodLuogoEmittente() + "' ";
		lStatement += " AND ANNO_SENTENZA=" + aSentenza.getAnnoSentenza() + " ";
		lStatement += " AND NUMERO_SENTENZA='" + aSentenza.getNumeroSentenza() + "' ";

		setStatement(lStatement);
	}

	public void ricercaSentenzaDuplicata(SentenzaModel aSentenza) throws DAOException {
		String lStatement = getSentenzaSqlQuery();

		lStatement += " AND NVL(UA.COD_UFFICIO_NEW, COD_UFFICIO_INSERIMENTO)='"
				+ aSentenza.getCodUfficioInserimento() + "' ";

		lStatement += " AND COD_TIPO_AUTORITA_EMITTENTE='" + aSentenza.getCodTipoAutoritaEmittente() + "' ";
		lStatement += " AND COD_LUOGO_EMITTENTE='" + aSentenza.getCodLuogoEmittente() + "' ";
		lStatement += " AND ANNO_SENTENZA=" + aSentenza.getAnnoSentenza() + " ";
		lStatement += " AND NUMERO_SENTENZA='" + aSentenza.getNumeroSentenza() + "' ";

		lStatement += " AND COD_TIPO_PROVV_RIF='" + aSentenza.getCodTipoProvvRif() + "' ";

		// Modifica per segnalazione a7-rr-327
		if (aSentenza.getCodTipoProvvedimento() != null && aSentenza.getCodTipoProvvedimento().length() > 1)
			lStatement += " AND COD_TIPO_PROVVEDIMENTO= '" + aSentenza.getCodTipoProvvedimento() + "'";

		if (aSentenza.getAnnoProvvRif() == null)
			lStatement += " AND ANNO_PROVV_RIF is null ";
		else
			lStatement += " AND ANNO_PROVV_RIF=" + aSentenza.getAnnoProvvRif() + " ";

		if (aSentenza.getNumeroProvvRif() == null || aSentenza.getNumeroProvvRif().equals(""))
			lStatement += " AND (NUMERO_PROVV_RIF is null or NUMERO_PROVV_RIF = '')";
		else
			lStatement += " AND NUMERO_PROVV_RIF='" + aSentenza.getNumeroProvvRif() + "' ";

		lStatement += " AND COD_TIPO_AUTORITA_PROVV_RIF='" + aSentenza.getCodTipoAutoritaProvvRif() + "' ";

		lStatement += " AND COD_LUOGO_PROVV_RIF='" + aSentenza.getCodLuogoProvvRif() + "' ";

		if (aSentenza.getNote1DecisioneCassazione() == null
				|| aSentenza.getNote1DecisioneCassazione().equals(""))
			lStatement += " AND (NOTE1_DECISIONE_CASSAZIONE is null or NOTE1_DECISIONE_CASSAZIONE = '')";
		else
			lStatement += " AND NOTE1_DECISIONE_CASSAZIONE='" + aSentenza.getNote1DecisioneCassazione()
					+ "' ";

		if (aSentenza.getNote2DecisioneCassazione() == null
				|| aSentenza.getNote2DecisioneCassazione().equals(""))
			lStatement += " AND (NOTE2_DECISIONE_CASSAZIONE is null or NOTE2_DECISIONE_CASSAZIONE = '')";
		else
			lStatement += " AND NOTE2_DECISIONE_CASSAZIONE='" + aSentenza.getNote2DecisioneCassazione()
					+ "' ";

		lStatement += " AND COD_TIPO_DECISIONE_CASSAZIONE='" + aSentenza.getCodTipoDecisioneCassazione()
				+ "' ";

		setStatement(lStatement);
	}

	// AMBROSINO 04/2011 - la "ricercaSentenzaDuplicata" ,invocata prima di Inserire sentenza in copia,
	// Non si accorgeva della differenza tra sentenza e sentenza cassazone e non permette di inserire
	// due sentenze che per lui sono uguali

	public void ricercaSentenzaDuplicataCassazione(SentenzaModel aSentenza) throws DAOException {
		String lStatement = getSentenzaSqlQuery();

		lStatement += " AND NVL(UA.COD_UFFICIO_NEW, COD_UFFICIO_INSERIMENTO)='"
				+ aSentenza.getCodUfficioInserimento() + "' ";

		lStatement += " AND COD_TIPO_AUTORITA_EMITTENTE='" + aSentenza.getCodTipoAutoritaEmittente() + "' ";
		lStatement += " AND COD_LUOGO_EMITTENTE='" + aSentenza.getCodLuogoEmittente() + "' ";
		lStatement += " AND ANNO_SENTENZA=" + aSentenza.getAnnoSentenza() + " ";
		lStatement += " AND NUMERO_SENTENZA='" + aSentenza.getNumeroSentenza() + "' ";

		lStatement += " AND COD_TIPO_PROVV_RIF='" + aSentenza.getCodTipoProvvRif() + "' ";

		// Modifica per segnalazione a7-rr-327
		if (aSentenza.getCodTipoProvvedimento() != null && aSentenza.getCodTipoProvvedimento().length() > 1)
			lStatement += " AND COD_TIPO_PROVVEDIMENTO= '" + aSentenza.getCodTipoProvvedimento() + "'";

		if (aSentenza.getAnnoProvvRif() == null)
			lStatement += " AND ANNO_PROVV_RIF is null ";
		else
			lStatement += " AND ANNO_PROVV_RIF=" + aSentenza.getAnnoProvvRif() + " ";

		if (aSentenza.getNumeroProvvRif() == null || aSentenza.getNumeroProvvRif().equals(""))
			lStatement += " AND (NUMERO_PROVV_RIF is null or NUMERO_PROVV_RIF = '')";
		else
			lStatement += " AND NUMERO_PROVV_RIF='" + aSentenza.getNumeroProvvRif() + "' ";

		lStatement += " AND COD_TIPO_AUTORITA_PROVV_RIF='" + aSentenza.getCodTipoAutoritaProvvRif() + "' ";

		lStatement += " AND COD_LUOGO_PROVV_RIF='" + aSentenza.getCodLuogoProvvRif() + "' ";

		if (aSentenza.getNote1DecisioneCassazione() == null
				|| aSentenza.getNote1DecisioneCassazione().equals(""))
			lStatement += " AND (NOTE1_DECISIONE_CASSAZIONE is null or NOTE1_DECISIONE_CASSAZIONE = '')";
		else
			lStatement += " AND NOTE1_DECISIONE_CASSAZIONE='" + aSentenza.getNote1DecisioneCassazione()
					+ "' ";

		if (aSentenza.getNote2DecisioneCassazione() == null
				|| aSentenza.getNote2DecisioneCassazione().equals(""))
			lStatement += " AND (NOTE2_DECISIONE_CASSAZIONE is null or NOTE2_DECISIONE_CASSAZIONE = '')";
		else
			lStatement += " AND NOTE2_DECISIONE_CASSAZIONE='" + aSentenza.getNote2DecisioneCassazione()
					+ "' ";

		lStatement += " AND COD_TIPO_DECISIONE_CASSAZIONE='" + aSentenza.getCodTipoDecisioneCassazione()
				+ "' ";

		// -- AMBROSINO 04/2011 - Aggiunto pezzo per casszione

		if (aSentenza.getCodTipoProvvedimentoAltro() != null
				&& aSentenza.getCodTipoProvvedimentoAltro().length() > 1)
			lStatement += " AND COD_TIPO_PROVVEDIMENTO_ALTRO='" + aSentenza.getCodTipoProvvedimentoAltro()
					+ "' ";

		if (aSentenza.getAnnoSentenzaCassazione() != null
				&& !aSentenza.getAnnoSentenzaCassazione().toString().equals(""))
			lStatement += " AND ANNO_SENTENZA_CASSAZIONE ='" + aSentenza.getAnnoSentenzaCassazione() + "' ";

		if (aSentenza.getNumeroSentenzaCassazione() != null
				&& !aSentenza.getNumeroSentenzaCassazione().equals(""))
			lStatement += " AND NUMERO_SENTENZA_CASSAZIONE ='" + aSentenza.getNumeroSentenzaCassazione()
					+ "' ";

		setStatement(lStatement);
	}

	/**
	 * Ricerca Sentenza tramite chiave
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaSentenzaBykey(BigDecimal aKey) throws DAOException {
		String lStatement = getSentenzaSqlQuery();

		lStatement += " " + setCondizionByKey(aKey);

		setStatement(lStatement);
	}

	/**
	 * Set condizione sulla query di SQL
	 *
	 * @param aModel
	 * @return String
	 */
	private String setCondizioni(SentenzaModel aModel) {
		String lCondizioni = new String();

		if (aModel.getDataProvvedimentoIniziale() != null) {
			lCondizioni += " AND TO_CHAR(DATA_PROVVEDIMENTO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimentoIniziale(), "yyyyMMdd") + "'";
		}
		if (aModel.getDataProvvedimentoFinale() != null) {
			lCondizioni += " AND TO_CHAR(DATA_PROVVEDIMENTO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimentoFinale(), "yyyyMMdd") + "'";
		}
		/****************************** modifica 26 marzo 04 **************************************/
		if (aModel.getDataProvvedimento() != null) {
			lCondizioni += " AND TO_CHAR(DATA_PROVVEDIMENTO,'YYYYMMDD') = '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimento(), "yyyyMMdd") + "'";
		}
		/*
		 * if (aModel.getDataIrrevocabilita() != null) { lCondizioni +=
		 * " AND TO_CHAR(DATA_IRREVOCABILITA,'YYYYMMDD') = '" +
		 * DateUtils.getDateToString(aModel.getDataIrrevocabilita(), "yyyyMMdd") + "'"; }
		 */
		if (aModel.getDataIrrevocabilitaIniziale() != null) {
			lCondizioni += " AND TO_CHAR(DATA_IRREVOCABILITA,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aModel.getDataIrrevocabilitaIniziale(), "yyyyMMdd") + "'";
		}
		if (aModel.getDataIrrevocabilitaFinale() != null) {
			lCondizioni += " AND TO_CHAR(DATA_IRREVOCABILITA,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aModel.getDataIrrevocabilitaFinale(), "yyyyMMdd") + "'";
		}

		if (aModel.getAnnoRGNRIniziale() != null) {
			lCondizioni += " AND ANNO_REGE_PM >= " + aModel.getAnnoRGNRIniziale();
		}

		if (aModel.getNumeroRGNRIniziale() != null) {
			lCondizioni += " AND NUMERO_REGE_PM >= " + aModel.getNumeroRGNRIniziale();
		}

		if (aModel.getAnnoRGNRFinale() != null) {
			lCondizioni += " AND ANNO_REGE_PM <= " + aModel.getAnnoRGNRFinale();
		}

		if (aModel.getNumeroRGNRFinale() != null) {
			lCondizioni += " AND NUMERO_REGE_PM <= " + aModel.getNumeroRGNRFinale();
		}

		if ((aModel.getAnnoSentenza() != null)) {
			lCondizioni += " AND ANNO_SENTENZA = " + aModel.getAnnoSentenza() + "";
		}
		if ((aModel.getNumeroSentenza() != null) && (!aModel.getNumeroSentenza().equals(""))) {
			lCondizioni += " AND NUMERO_SENTENZA = '" + aModel.getNumeroSentenza() + "'";
		}
		if ((aModel.getAnnoRegeCap() != null)) {
			lCondizioni += " AND ANNO_REGE_CAP = " + aModel.getAnnoRegeCap() + "";
		}
		if ((aModel.getNumeroRegeCap() != null) && (!aModel.getNumeroRegeCap().equals(""))) {
			lCondizioni += " AND NUMERO_REGE_CAP = '" + aModel.getNumeroRegeCap() + "'";
		}
		if ((aModel.getAnnoRegeGip() != null)) {
			lCondizioni += " AND ANNO_REGE_GIP = " + aModel.getAnnoRegeGip() + "";
		}
		if ((aModel.getNumeroRegeGip() != null) && (!aModel.getNumeroRegeGip().equals(""))) {
			lCondizioni += " AND NUMERO_REGE_GIP = '" + aModel.getNumeroRegeGip() + "'";
		}
		if ((aModel.getAnnoRegeCas() != null)) {
			lCondizioni += " AND ANNO_REGE_CAS = " + aModel.getAnnoRegeCas() + "";
		}
		if ((aModel.getNumeroRegeCas() != null) && (!aModel.getNumeroRegeCas().equals(""))) {
			lCondizioni += " AND NUMERO_REGE_CAS = '" + aModel.getNumeroRegeCas() + "'";
		}
		if ((aModel.getAnnoRegeDib() != null)) {
			lCondizioni += " AND ANNO_REGE_DIB= " + aModel.getAnnoRegeDib() + "";
		}
		if ((aModel.getNumeroRegeDib() != null) && (!aModel.getNumeroRegeDib().equals(""))) {
			lCondizioni += " AND NUMERO_REGE_DIB = '" + aModel.getNumeroRegeDib() + "'";
		}
		if ((aModel.getAnnoRegeCasap() != null)) {
			lCondizioni += " AND ANNO_REGE_CASAP= " + aModel.getAnnoRegeCasap() + "";
		}
		if ((aModel.getNumeroRegeCasap() != null) && (!aModel.getNumeroRegeCasap().equals(""))) {
			lCondizioni += " AND NUMERO_REGE_CASAP ='" + aModel.getNumeroRegeCasap() + "'";
		}
		if ((aModel.getAnnoRegePm() != null)) {
			lCondizioni += " AND ANNO_REGE_PM = " + aModel.getAnnoRegePm() + "";
		}
		if ((aModel.getNumeroRegePm() != null) && (!aModel.getNumeroRegePm().equals(""))) {
			lCondizioni += " AND NUMERO_REGE_PM = '" + aModel.getNumeroRegePm() + "'";
		}
		if ((aModel.getCodTipoAutoritaEmittente() != null)
				&& (!aModel.getCodTipoAutoritaEmittente().equals(""))) {
			lCondizioni += " AND COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente()
					+ "'";
		}
		if ((aModel.getCodLuogoEmittente() != null) && (!aModel.getCodLuogoEmittente().equals(""))) {
			lCondizioni += " AND COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "'";
		}
		// MEV_66: aggiunte quattro nuove proprietà
		if ((aModel.getAnnoRegeGup() != null)) {
			lCondizioni += " AND ANNO_REGE_GUP = " + aModel.getAnnoRegeGup() + "";
		}
		if ((aModel.getNumeroRegeGup() != null) && (!aModel.getNumeroRegeGup().equals(""))) {
			lCondizioni += " AND NUMERO_REGE_GUP = '" + aModel.getNumeroRegeGup() + "'";
		}
		if ((aModel.getAnnoRegeCapsm() != null)) {
			lCondizioni += " AND ANNO_REGE_CAPSM = " + aModel.getAnnoRegeCapsm() + "";
		}
		if ((aModel.getNumeroRegeCapsm() != null) && (!aModel.getNumeroRegeCapsm().equals(""))) {
			lCondizioni += " AND NUMERO_REGE_CAPSM = '" + aModel.getNumeroRegeCapsm() + "'";
		}

		// MEV 16 - Interoperabilita' SIEP-NSC
		// condizione necessaria per escludere dalla query i Titoli Esecutivi
		// appartenenti ad un Fascicolo di Cumulo
		// lCondizioni += " AND FLAG_VISIBILITA IS NULL ";

		return lCondizioni;
	}

	/**
	 * Set condizione sulla query di SQL
	 *
	 * @param aModel
	 * @return String
	 */
	private String setCondizionByKey(BigDecimal aId) {
		String lCondizioni = " AND ID_SENTENZA = " + aId;

		return lCondizioni;
	}

	/**
	 * Restituisce la rappresentazione dei dati selezionati in Model
	 *
	 * @return GenericModel
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		SentenzaModel lModel = new SentenzaModel();

		lModel.setIdSentenza(getBigDecimal("ID_SENTENZA"));
		lModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lModel.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		lModel.setAnnoRegePm(getBigDecimal("ANNO_REGE_PM"));
		lModel.setNumeroRegePm(getString("NUMERO_REGE_PM"));
		// lModel.setDataArrivoAtto(getDate("DATA_ARRIVO_ATTO") );
		lModel.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));

		lModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		lModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		lModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		lModel.setNumSezioneAutoritaEmittente(getString("NUM_SEZIONE_AUTORITA_EMITTENTE"));
		lModel.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		lModel.setNumeroSentenza(getString("NUMERO_SENTENZA"));
		lModel.setAnnoProvvedimento(getBigDecimal("ANNO_PROVVEDIMENTO"));
		lModel.setNumeroProvvedimento(getString("NUMERO_PROVVEDIMENTO"));
		// lModel.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA") );
		// lModel.setFlagSentenzaApplicazPena(getString("FLAG_SENTENZA_APPLICAZ_PENA") );
		lModel.setCodTipoProvvRif(getString("COD_TIPO_PROVV_RIF"));
		lModel.setDescrTipoProvvRif(getString("DESCR_TIPO_PROVV_RIF"));
		lModel.setDataProvvRif(getDate("DATA_PROVV_RIF"));
		lModel.setCodTipoAutoritaProvvRif(getString("COD_TIPO_AUTORITA_PROVV_RIF"));
		lModel.setDescrTipoAutoritaProvvRif(getString("DESCR_TIPO_AUTORITA_PROVV_RIF"));
		lModel.setAnnoProvvRif(getBigDecimal("ANNO_PROVV_RIF"));
		lModel.setNumeroProvvRif(getString("NUMERO_PROVV_RIF"));
		lModel.setCodLuogoProvvRif(getString("COD_LUOGO_PROVV_RIF"));
		lModel.setDescrLuogoProvvRif(getString("DESCR_LUOGO_PROVV_RIF"));
		lModel.setNumSezioneAutoritaProvvRif(getString("NUM_SEZIONE_AUTORITA_PROVV_RIF"));
		lModel.setCodTipoDecisioneCassazione(getString("COD_TIPO_DECISIONE_CASSAZIONE"));
		lModel.setDescrTipoDecisioneCassazione(getString("DESCR_TIPO_DECISIONE_CASS"));
		lModel.setNote1DecisioneCassazione(getString("NOTE1_DECISIONE_CASSAZIONE"));
		lModel.setNote2DecisioneCassazione(getString("NOTE2_DECISIONE_CASSAZIONE"));
		lModel.setAnnoSentenzaCassazione(getBigDecimal("ANNO_SENTENZA_CASSAZIONE"));
		lModel.setNumeroSentenzaCassazione(getString("NUMERO_SENTENZA_CASSAZIONE"));
		lModel.setAnnoRaccoltaGenerale(getBigDecimal("ANNO_RACCOLTA_GENERALE"));
		lModel.setNumeroRaccoltaGenerale(getString("NUMERO_RACCOLTA_GENERALE"));
		lModel.setFlagAltreSentenze(getString("FLAG_ALTRE_SENTENZE"));
		lModel.setDescrAltreSentenze(getString("DESCR_ALTRE_SENTENZE"));
		lModel.setAnnoRegistro35(getBigDecimal("ANNO_REGISTRO_35"));
		lModel.setNumRegistro35(getString("NUM_REGISTRO_35"));
		lModel.setNote(getString("NOTE"));
		// lModel.setDescrNumCampionePenale(getString("DESCR_NUM_CAMPIONE_PENALE") );
		lModel.setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP"));
		lModel.setNumeroRegeGip(getString("NUMERO_REGE_GIP"));
		lModel.setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB"));
		lModel.setNumeroRegeDib(getString("NUMERO_REGE_DIB"));
		lModel.setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS"));
		lModel.setNumeroRegeCas(getString("NUMERO_REGE_CAS"));
		lModel.setAnnoRegeCap(getBigDecimal("ANNO_REGE_CAP"));
		lModel.setNumeroRegeCap(getString("NUMERO_REGE_CAP"));
		lModel.setAnnoRegeCasap(getBigDecimal("ANNO_REGE_CASAP"));
		lModel.setNumeroRegeCasap(getString("NUMERO_REGE_CASAP"));
		lModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		lModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lModel.setCodBilanciamentoCircostanze(getString("COD_BILANCIAMENTO_CIRCOSTANZE"));
		lModel.setDescrBilanciamentoCircostanze(getString("DESCBILAN"));
		lModel.setFlagGiudizioAbbreviato(getString("FLAG_GIUDIZIO_ABBREVIATO"));
		lModel.setCodTipoRito(getString("COD_TIPO_RITO"));
		lModel.setCodTipoProvvedimentoRif(getString("COD_TIPO_PROVVEDIMENTO_RIF"));
		lModel.setCodTipoProvvedimentoAltro(getString("COD_TIPO_PROVVEDIMENTO_ALTRO"));
		lModel.setCodSedeNotiziaReato(getString("COD_SEDE_NOTIZIA_REATO"));
		lModel.setDescrTipoProvvedimentoRif(getString("DESCR_TIPO_PROVVEDIMENTO_RIF"));
		lModel.setDescrTipoProvvedimentoAltro(getString("DESCR_TIPO_PROVVEDIMENTO_ALTRO"));
		lModel.setDescrSedeNotiziaReato(getString("DESCR_SEDE_NOTIZIA_REATO"));

		if (lModel.getCodTipoRito() == null || lModel.getCodTipoRito().equals("")) {

			lModel.setDescrTipoRito("-");
		} else {
			lModel.setDescrTipoRito(DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getTipoRitoSentenza(), lModel.getCodTipoRito()));
		}

		lModel.setFlagVisibilita(getString("FLAG_VISIBILITA"));

		// MEV_66: aggiunte quattro nuove proprietà
		lModel.setAnnoRegeGup(getBigDecimal("ANNO_REGE_GUP"));
		lModel.setNumeroRegeGup(getString("NUMERO_REGE_GUP"));
		lModel.setAnnoRegeCapsm(getBigDecimal("ANNO_REGE_CAPSM"));
		lModel.setNumeroRegeCapsm(getString("NUMERO_REGE_CAPSM"));

		return lModel;
	}

}
package siap.sico.evento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.util.SIESSwitch;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: EventoPerStampaSqlDAO
 * </p>
 * <p>
 * Description: Evento SQL per la stampa
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EventoPerStampaSqlDAO extends SIAPSqlDAO {

	public EventoPerStampaSqlDAO(Connection con) {
		super(con);
	}

	public void ricercaEventoByFascicoloSiepXStampa(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		// ---GDV 22122003 -- tutti gli eventi lStatement += " AND COD_TIPO_EVENTO IN ('01','02','04')"; // Le
		// ISTANZE vengono cercate in una select a parte
		lStatement += " AND COD_MOTIVO <> '0670'"; // 26/03/2019 MEV70 - Esclusione delle Comunicazioni
													// cumulo.
		lStatement += " AND FLAG_STAMPA_SIEP = 'S'";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

		if (SIESSwitch.isReworkStatoEsecuzioneOn()) {// Per il rework dello stato esecuzione vengono
														// selezionati a parte le ordinanze
														// lStatement += " AND COD_TIPO_PROVVEDIMENTO <> '02'
														// ";
														// lStatement += " AND COD_TIPO_PROVVEDIMENTO <> '03'
														// ";
		}
		// Gli eventi vengono ordinati
		// per data emissione, data inserimento, id evento
		lStatement += " ORDER BY DATA_EMISSIONE , DATA_INSERIMENTO, ID_EVENTO";

		setStatement(lStatement);
	}

	public void ricercaEventoOrdinanzaDecretoByFascicoloSiepXStampa(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		// ---GDV 22122003 -- tutti gli eventi lStatement += " AND COD_TIPO_EVENTO IN ('01','02','04')"; // Le
		// ISTANZE vengono cercate in una select a parte
		/*
		 * ISSUE MAC : modificata condizione sul FLAG_STAMPA_SIEP Numero MAC : 20191129018  Autore : monica
		 * Data : 23/dic/2019 Branch : 11.2.4
		 */
		// lStatement += " AND FLAG_STAMPA_SIEP = 'S'";
		lStatement += " AND (FLAG_STAMPA_SIEP = 'S' or (FLAG_STAMPA_SIEP = 'N' and evento.fas_siu_id_fascicolo_sius is not null))";
		// ***** FINE INTERVENTO 20191129018  *****//
		lStatement += " AND COD_TIPO_PROVVEDIMENTO IN ('02','03') ";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

		// Gli eventi vengono ordinati
		// per data emissione, data inserimento, id evento
		lStatement += " ORDER BY DATA_EMISSIONE , DATA_INSERIMENTO, ID_EVENTO";

		setStatement(lStatement);
	}

	public void ricercaEveIdEventi(BigDecimal aKey) throws DAOException {
		String lStatement = "select eve_id_evento from evento where ";

		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND FLAG_STAMPA_SIEP = 'S'";
		// lStatement += " AND COD_TIPO_PROVVEDIMENTO IN ('02','03') ";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

		// Gli eventi vengono ordinati
		// per data emissione, data inserimento, id evento
		lStatement += " ORDER BY DATA_EMISSIONE , DATA_INSERIMENTO, ID_EVENTO";

		setStatement(lStatement);
	}

	public Vector getModelEveIdEventi() throws DAOException {
		Vector lRisultati = new Vector();

		start();
		while (next())
			lRisultati.add(getBigDecimal("EVE_ID_EVENTO"));

		stop();
		return lRisultati;
	}

	public void ricercaEventoIstanzaByFascicoloSiepXStampa(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND COD_TIPO_EVENTO = '03'";
		lStatement += " AND FLAG_STAMPA_SIEP = 'S'";
		lStatement += " ORDER BY DATA_EMISSIONE , DATA_INSERIMENTO  ";

		// lStatement += setOrder();

		setStatement(lStatement);
	}

	public void ricercaEventiByFascicoloSiepXTrasferimento(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

		lStatement += setOrderAsc();

		setStatement(lStatement);
	}

	protected String getSqlQuery() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		// GDV
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " COD_OPERATORE_INSERIMENTO, ";
		lStatement += " DATA_INSERIMENTO,";
		lStatement += " COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " DATA_AGGIORNAMENTO,";
		lStatement += " COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  ";
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP,  ";
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " ISTR_ID_ISTRUTTORIA_CUMULO ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI ";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND"; // ****
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE ";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";

		return lStatement;
	}

//	private String setOrder() {
//		String lCondizioni = new String(" ORDER BY DATA_EMISSIONE DESC ");
//
//		return lCondizioni;
//	}

	private String setOrderAsc() {
		String lCondizioni = new String(" ORDER BY DATA_INSERIMENTO,ID_EVENTO ");

		return lCondizioni;
	}

//	private String setOrderEmissioneInserimentoDesc() {
//		String lCondizioni = " ORDER BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC";
//
//		return lCondizioni;
//	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		EventoModel aModel = new EventoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		aModel.setDescrTipoEvento(getString("COD_EVE"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("COD_PRO"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("COD_MOV"));
		aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("LUO_EMI"));
		aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
		aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("COD_ESI"));
		aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		// -- aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		aModel.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
		aModel.setFasSiuIdFascicoloSiusDest(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST"));
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setLegge(getString("COD_ABBR"));
		aModel.setCodTipoUfficioEmittente(getString("COD_TIPO_UFF_EMITTENTE"));
		aModel.setDecIdDecretoOrdinanzaSiep(getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP"));
		aModel.setEveIdEventoRevoca(getBigDecimal("EVE_ID_EVENTO_REVOCA"));
		aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
		aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));
		aModel.setDataEspulsioneSanzSost(getDate("DATA_ESPULSIONE_SANZ_SOST"));

		aModel.setIstruidIstruttoriaCumulo(getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"));

		return aModel;
	}

}
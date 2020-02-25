package siap.sico.evento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;

/**
 * <p>
 * Title: EventoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Evento
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
public class EventoSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public EventoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaEvento(EventoModel aModel) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " " + setCondizioni(aModel) + " " + setOrder();

		setStatement(lStatement);
	}

	public void ricercaEventoByKey(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND ID_EVENTO = " + aKey;

		setStatement(lStatement);
	}

	public void ricercaEventoByKeyTenore(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQueryTenore();
		lStatement += " AND ID_EVENTO = " + aKey;

		setStatement(lStatement);
	}

	public void ricercaEventoByIdFascicoloDescrMotivo(BigDecimal aKey, String aMotivo) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE '" + aMotivo + "%' ";
		if (aMotivo != null && aMotivo.equals("LS")) {
			lStatement += " OR CODMOV.RV_ALT2_VALUE LIKE '" + aMotivo + "%' ";
		}
		lStatement += " ) ";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO =  'S'";
		lStatement += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lStatement);
	}

	public void ricercaEventoTipoCodMotiviProvValidato(EventoModel aModel) throws DAOException {
		String lStatement = getSqlQuery();

		if (aModel.getCodMotivo() != null) {
			if (aModel.getCodMotivo().compareTo("") != 0)
				lStatement += " AND COD_MOTIVO ='" + aModel.getCodMotivo() + "'";
		}

		if (aModel.getCodTipoEvento() != null) {
			if (aModel.getCodTipoEvento().compareTo("") != 0)
				lStatement += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aModel.getCodTipoProvvedimento() != null) {
			if (aModel.getCodTipoProvvedimento().compareTo("") != 0)
				lStatement += " AND COD_TIPO_PROVVEDIMENTO ='" + aModel.getCodTipoProvvedimento() + "'";
		}
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO ='S')";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoEsitoParereInamm(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQueryEsitoParereInamm();
		lStatement += " WHERE ID_EVENTO = " + aKey;

		setStatement(lStatement);
	}

	public void ricercaEventoByEveIdEvento(BigDecimal aKey) throws DAOException {

		// AMBROSINO 30/06/2011 --> in questa ricerca ('ricercaEventoByEveIdEvento') viene aggiunto
		// un ORDER BY data-emissione DESC;
		// e' chiamata da ExAggiornaEventoInserisciCampoNota (MisuraAlternativaIndultinoController)
		// e da ExRicercaEventoNotificaByEveIdEvento(EventoController)
		// e da ExRicercaEventoByEveIdEvento(EventoSimeoneController)

		String lStatement = getSqlQuery();
		lStatement += " AND EVE_ID_EVENTO = " + aKey;

		// 02/12/2005 DL : Modifica per evitare che vengano considerate
		// nelle misure alternative eventi annullati
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO != 'A'";

		lStatement += " ORDER BY DATA_EMISSIONE DESC";

		setStatement(lStatement);
	}

	public void ricercaEventoInoltroDispPMByEveIdEvento(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND EVE_ID_EVENTO = " + aKey;
		lStatement += " AND COD_TIPO_EVENTO = '03' AND COD_TIPO_PROVVEDIMENTO = '08' ";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO != 'A' ";
		lStatement += " AND (COD_MOTIVO = '1001' OR COD_MOTIVO = '1002') ";

		setStatement(lStatement);
	}

	public void ricercaUltimoEventoByIdFascicolo(BigDecimal aKeyFasc) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKeyFasc;
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	// Ricerca evento di concessione misura alternativa per fascicolo sius
	public void ricercaEventoMisuraAlternativaByIdFasSius(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		lStatement += " AND (TO_NUMBER(EVENTO.COD_MOTIVO) >= 1 AND TO_NUMBER(EVENTO.COD_MOTIVO) <= 13) ";
		lStatement += " AND EVENTO.COD_ESITO = '0001' ";
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03' ";
		setStatement(lStatement);
	}

	public void ricercaEventoIstanzaByKey(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND ID_EVENTO = " + aKey;

		setStatement(lStatement);
	}

	public void ricercaEventoByKeyPerMotivo(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		// lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND ID_EVENTO = " + aKey;
		lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE 'LS%' OR CODMOV.RV_HIGH_VALUE LIKE 'LED%')";

		setStatement(lStatement);
	}

	// ricerca evento ordinanza
	public void ricercaEventoOrdinanzaOrdineScarcerazioneIdFascicolo(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
		lStatement += " AND EVENTO.COD_MOTIVO = '0081'";
		lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoOrdinanzaOSLiberazioneAnticipataMAIdFascicolo(BigDecimal aKey)
			throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
		lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
		lStatement += " AND EVENTO.COD_MOTIVO = '0083'";
		lStatement += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	public void ricercaEventoOrdinanzaRipriAffidamentoByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '02'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
		lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'UDS'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('2145','2146','2147')";
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoOrdinanzaRipriDetDomByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '02'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
		lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'UDS'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('2149','2150','2151','2153')";
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoOrdinanzaAmmissionaADetDomIdFascicolo(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
		lStatement += " AND EVENTO.COD_MOTIVO = '0194'";
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoOrdinanzaRipriSemLibByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '02'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
		lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'UDS'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('2148')";
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoPerditaAffidamentoByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '02'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
		lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'UDS'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('2160','2161','2162','2163','2164','2165','2166')";
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	// LS
	public void ricercaEventoByKeyCondannato(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE 'LS%' OR CODMOV.RV_HIGH_VALUE LIKE 'LED%')";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO =  'S'";
		lStatement += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lStatement);
	}

	/**
	 * Ricerca tutte le Ordinanze legate a computi, validate o meno ordinate per DATA_EMISSIONE DESC,
	 * DATA_INSERIMENTO DESC, ID_EVENTO DESC Computi 0121 - computo Misura Cautelare stesso Reato art. 657
	 * c.p.p. (presofferto) 0212 - computo Misura Cautelare Altro Reato art. 657 c.p.p. 0213 - computo Pena
	 * Detentiva Espiata per Altro Reato (fungibilita') art. 657 c.p.p. Richieste al GE 0122 - Richiesta
	 * applicazione amnistia / indulto 0210 - Applicazione Depenalizzazione 0211 - Applicazione
	 * Incostituzionalita' Decisioni del GE 0284 - Applicazione Amnistia / Indulto 0285 - Applicazione
	 * depenalizzazione 0286 - Applicazione incostituzionalita'
	 *
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaEventoOrdinanzaAnnotazioneManuale(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('0122','0212','0213','0210','0211','0121','0284','0285','0286')";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoProvvedimentoAnnotazioneManuale(BigDecimal aKey) throws DAOException {
		// Modificata la Query perche' alcune Annotazioni hanno ora il CodTipoProvvedimento = 26
		// e non piu' 04. Al tempo stesso si vuole mantenere la compatibilita' con dati vecchi.
		// Luigi 29-09-2005
		String lStatement = getSqlQuery();

		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";

		lStatement += " AND (( EVENTO.COD_TIPO_PROVVEDIMENTO = '04'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('0122','0212','0213','0210','0211','0121'))";

		lStatement += " OR ( EVENTO.COD_TIPO_PROVVEDIMENTO = '26'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('0122','0210','0211','0121')))";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	// ***************************************************

	public void ricercaEventoByKeyForTrasmAtti(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQueryForTrasmAtti();
		lStatement += " AND ID_EVENTO = " + aKey;

		setStatement(lStatement);
	}

	/**
	 * Ricerca eventi
	 *
	 * @param aKey
	 * @param aTipoEvento
	 * @throws DAOException
	 */
	public void ricercaEventoByFascicoloSiep(BigDecimal aKey, String[] aTipoEvento) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		// if (aTipoEvento != null)
		// lStatement += " AND COD_TIPO_EVENTO = '" + aTipoEvento + "'";
		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += " AND COD_TIPO_EVENTO IN (";
				for (int i = 0; i < aTipoEvento.length; i++) {
					lStatement += "'" + aTipoEvento[i] + "'";
					if (aTipoEvento.length > 1 && i < aTipoEvento.length - 1)
						lStatement += ",";

				}
				lStatement += ")";
			}
		}

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	/**
	 * Ricerca eventi
	 *
	 * @param aKey
	 * @param aTipoEvento
	 * @throws DAOException
	 */
	public void ricercaEventoByFascicoloSiepAsc(BigDecimal aKey, String[] aTipoEvento) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += " AND COD_TIPO_EVENTO IN (";
				for (int i = 0; i < aTipoEvento.length; i++) {
					lStatement += "'" + aTipoEvento[i] + "'";
					if (aTipoEvento.length > 1 && i < aTipoEvento.length - 1)
						lStatement += ",";

				}
				lStatement += ")";
			}
		}
		lStatement += setOrderEventoAsc();

		setStatement(lStatement);
	}

	/**
	 * 16/06/2011 Ricerca eventi
	 *
	 * @param aChiaveFascicolo
	 * @param aChiaveUfficio
	 * @param aTipoEvento
	 * @throws DAOException
	 */
	public void ricercaEventoByFascicoloSiepAsc(BigDecimal aChiaveFascicolo, String aChiaveUfficio,
			String[] aTipoEvento) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aChiaveFascicolo;
		if (aChiaveUfficio != null)
			lStatement += " AND COD_UFFICIO_EMITTENTE = " + aChiaveUfficio;

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += " AND COD_TIPO_EVENTO IN (";
				for (int i = 0; i < aTipoEvento.length; i++) {
					lStatement += "'" + aTipoEvento[i] + "'";
					if (aTipoEvento.length > 1 && i < aTipoEvento.length - 1)
						lStatement += ",";
				}
				lStatement += ")";
			}
		}
		lStatement += setOrderEventoAsc();

		setStatement(lStatement);
	}

	/**
	 * Ricerca eventi tramite provv
	 *
	 * @param aKey
	 * @param aTipoEvento
	 * @throws DAOException
	 */
	public void ricercaEventoByFascicoloSiepProvvSiusAsc(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND COD_TIPO_PROVVEDIMENTO IN ('02', '03')";
		/*
		 * if (aTipoEvento != null) { if(aTipoEvento.length>0) { lStatement +=
		 * " AND COD_TIPO_PROVVEDIMENTO IN ("; for(int i=0;i<aTipoEvento.length;i++) { lStatement += "'"
		 * +aTipoEvento[i]+ "'"; if(aTipoEvento.length>1 && i<aTipoEvento.length-1) lStatement += ",";
		 *
		 * } lStatement += ")"; } }
		 */
		lStatement += setOrderEventoAsc();

		setStatement(lStatement);
	}

	/**
	 *
	 * @param aKey
	 * @param aUfficio
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aPage
	 * @param aOrdinamento
	 * @throws DAOException
	 */
	public void ricercaEventoByFascicoloTipEveTipProvSiepAsc(BigDecimal aKey,
			// String aCodUfficioUtenteConnesso,
			UfficioModel aUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv, int aPage,
			String aOrdinamento) throws DAOException {
		String lStatement = "";
		String lPaginedStatement = new String("");

		// Nel caso non sia stato passato l'Id del Fascicolo
		// significa che si vuole tutti i provvedimenti
		// NON VALIDATI
		if (aKey != null) {
			/**
			 * Nel caso sia definito il fascicolo verranno selezionati tutti i provvedimenti se l'utente
			 * connesso appartiene all'ufficio di competenza, altrimenti solo quelli validati. Luigi 7-4-2011
			 */
			lStatement = getSqlQuery();
			lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
			// lStatement += " AND (COD_UFFICIO_INSERIMENTO ='"+aCodUfficioUtenteConnesso+"'";
			lStatement += " AND (COD_UFFICIO_INSERIMENTO "
					+ condizioneUfficiCompetenti(aUfficioUtenteConnesso) + "";
			lStatement += " OR FLAG_DOCUMENTO_REGISTRATO = 'S')";
		} else {
			lStatement = getSqlQueryEventoFascicolo();
			lStatement += " AND CHIAVE_UFFICIO ='" + aUfficioUtenteConnesso.getCodUfficio() + "'";
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		}

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoPlusDecrIrre(aTipoProv);
			}
		}

		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		if ("EM".equals(aOrdinamento)) {
			lStatement += setOrderEventoAsc();
		} else if ("IN".equals(aOrdinamento)) {
			lStatement += setOrderEventoDataInserimentoAsc();
		}

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM ( " + lStatement
				+ " ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	// 26/03/2019 MEV70 - Esclusione dei CodMotivo nella lista.
	/**
	 *
	 * @param aKey
	 * @param aUfficio
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aCodMotivo
	 * @param aPage
	 * @param aOrdinamento
	 * @throws DAOException
	 */
	public void ricercaEventoByFascicoloTipEveTipProvSiepAsc(BigDecimal aKey,
			// String aCodUfficioUtenteConnesso,
			UfficioModel aUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv,
			String[] aCodMotivo, int aPage, String aOrdinamento) throws DAOException {
		String lStatement = "";
		String lPaginedStatement = new String("");

		// Nel caso non sia stato passato l'Id del Fascicolo
		// significa che si vuole tutti i provvedimenti
		// NON VALIDATI
		if (aKey != null) {
			/**
			 * Nel caso sia definito il fascicolo verranno selezionati tutti i provvedimenti se l'utente
			 * connesso appartiene all'ufficio di competenza, altrimenti solo quelli validati. Luigi 7-4-2011
			 */
			lStatement = getSqlQuery();
			lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
			// lStatement += " AND (COD_UFFICIO_INSERIMENTO ='"+aCodUfficioUtenteConnesso+"'";
			lStatement += " AND (COD_UFFICIO_INSERIMENTO "
					+ condizioneUfficiCompetenti(aUfficioUtenteConnesso) + "";
			lStatement += " OR FLAG_DOCUMENTO_REGISTRATO = 'S')";
		} else {
			lStatement = getSqlQueryEventoFascicolo();
			lStatement += " AND CHIAVE_UFFICIO ='" + aUfficioUtenteConnesso.getCodUfficio() + "'";
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		}

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoPlusDecrIrre(aTipoProv);
			}
		}

		if (aCodMotivo != null) {
			ArrayList<String> arrayCodMotivo = new ArrayList<>(Arrays.asList(aCodMotivo));
			if (arrayCodMotivo.contains("esclude")) {
				lStatement += condizioneEsclusioneCodMotivo(aCodMotivo);
			} else {
				lStatement += condizioneInclusioneCodMotivo(aCodMotivo);
			}
		}

		// 29/03/2019 MEV70 Nel caso di inclusione dei COD_MOTIVO "0670" non si filtra per FLAG_VIDEO_SIEP
		if (lStatement.indexOf("COD_MOTIVO IN ('0670')") < 0)
			lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		if ("EM".equals(aOrdinamento)) {
			lStatement += setOrderEventoAsc();
		} else if ("IN".equals(aOrdinamento)) {
			lStatement += setOrderEventoDataInserimentoAsc();
		}

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM ( " + lStatement
				+ " ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void ricercaEventoByFascicoloTipEveTipProvSiepDescPerEventoDaAnnullareCancellare(BigDecimal aKey,
			String[] aTipoEvento, String[] aTipoProv, String aOrdinamento) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoPlusDecrIrre(aTipoProv);
			}
		}

		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";
		lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'S' OR FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		if ("EM".equals(aOrdinamento))
			lStatement += setOrderEventoDesc();
		else if ("IN".equals(aOrdinamento))
			lStatement += setOrderEventoDataInserimentoDesc();

		setStatement(lStatement);
	}

	// 26/03/2019 MEV70 - Esclusione dei CodMotivo nella lista.
	public void ricercaEventoByFascicoloTipEveTipProvSiepDescPerEventoDaAnnullareCancellare(BigDecimal aKey,
			String[] aTipoEvento, String[] aTipoProv, String[] aCodMotivo, String aOrdinamento)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoPlusDecrIrre(aTipoProv);
			}
		}

		if (aCodMotivo != null) {
			ArrayList<String> arrayCodMotivo = new ArrayList<>(Arrays.asList(aCodMotivo));
			if (arrayCodMotivo.contains("esclude")) {
				lStatement += condizioneEsclusioneCodMotivo(aCodMotivo);
				lStatement += " AND FLAG_VIDEO_SIEP = 'S'";
			} else {
				lStatement += condizioneInclusioneCodMotivo(aCodMotivo);
			}
		}

		lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'S' OR FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		if ("EM".equals(aOrdinamento))
			lStatement += setOrderEventoDesc();
		else if ("IN".equals(aOrdinamento))
			lStatement += setOrderEventoDataInserimentoDesc();

		setStatement(lStatement);
	}

	public void getCountEventoByFascicoloSiepTipEventoNOTTipProv(BigDecimal aFascKey,
			String aCodUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv) throws DAOException

	{
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM EVENTO";

		// Nel caso non sia stato passato l'Id del fascicolo
		// significa che si vuole tutti i provvedimenti
		// NON VALIDATI
		if (aFascKey != null) {
			lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aFascKey;
		} else {
			lStatement += ", FASCICOLO_SIEP fasc";
			lStatement += " WHERE CHIAVE_UFFICIO ='" + aCodUfficioUtenteConnesso + "'";
			lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = fasc.ID_FASCICOLO_SIEP";
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL) ";
		}

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoPlusDecrIrre(aTipoProv);
			}
		}

		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		lStatement += " " + setOrderEventoAsc();

		setStatement(lStatement);
	}

	// 26/03/2019 MEV70 - Esclusione dei CodMotivo nella lista.
	public void getCountEventoByFascicoloSiepTipEventoNOTTipProv(BigDecimal aFascKey,
			String aCodUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv, String[] aCodMotivo)
			throws DAOException

	{
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM EVENTO";

		// Nel caso non sia stato passato l'Id del fascicolo
		// significa che si vuole tutti i provvedimenti
		// NON VALIDATI
		if (aFascKey != null) {
			lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aFascKey;
		} else {
			lStatement += ", FASCICOLO_SIEP fasc";
			lStatement += " WHERE CHIAVE_UFFICIO ='" + aCodUfficioUtenteConnesso + "'";
			lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = fasc.ID_FASCICOLO_SIEP";
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL) ";
		}

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoPlusDecrIrre(aTipoProv);
			}
		}

		if (aCodMotivo != null) {
			ArrayList<String> arrayCodMotivo = new ArrayList<>(Arrays.asList(aCodMotivo));
			if (arrayCodMotivo.contains("esclude")) {
				lStatement += condizioneEsclusioneCodMotivo(aCodMotivo);
			} else {
				lStatement += condizioneInclusioneCodMotivo(aCodMotivo);
			}
		}

		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		lStatement += " " + setOrderEventoAsc();

		setStatement(lStatement);
	}

	public void ricercaEventoByFascicoloSiepTipEventoNOTTipProvNONAnnullati(BigDecimal aKey,
			String[] aTipoEvento, String[] aTipoProv) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				// modifica a seguito di una modifica precendete di luigi per far visualizzare anche nel
				// dettaglio in ultimi eventi il decreto di irreperibilita' -- Dario -- Viviana --22-05-06
				lStatement += condizioneTipoProvvedimentoPlusDecrIrre(aTipoProv);
			}
		}

		// SI POSSO VISUALIZZARE GLI EVENTI CHE HANNO FLAG_VIDEO_SIEP AD S........--DARIO--INDICAZIONE:LUCIANA
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";
		lStatement += " " + setCondizioneEventiNonAnnullati();
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	private String condizioneTipoEvento(String[] aTipoEvento) {
		String lCondizioni = " AND COD_TIPO_EVENTO IN (";
		for (int i = 0; i < aTipoEvento.length; i++) {
			lCondizioni += "'" + aTipoEvento[i] + "'";
			if (aTipoEvento.length > 1 && i < aTipoEvento.length - 1)
				lCondizioni += ",";

		}
		lCondizioni += ")";
		return lCondizioni;
	}

	// private String condizioneTipoProvvedimento(String[] aTipoProv) {
	// String lCondizioni = " AND COD_TIPO_PROVVEDIMENTO NOT IN (";
	// for (int i = 0; i < aTipoProv.length; i++) {
	// lCondizioni += "'" + aTipoProv[i] + "'";
	// if (aTipoProv.length > 1 && i < aTipoProv.length - 1)
	// lCondizioni += ",";
	//
	// }
	// lCondizioni += ")";
	//
	// return lCondizioni;
	// }

	// Versione modificata della funzione precedente
	// per selezionare sempre anche i Decreti di Irreparabilita'.
	// Luigi 6-2-2006
	private String condizioneTipoProvvedimentoPlusDecrIrre(String[] aTipoProv) {
		String lCondizioni = " AND ( COD_TIPO_PROVVEDIMENTO NOT IN (";
		for (int i = 0; i < aTipoProv.length; i++) {
			lCondizioni += "'" + aTipoProv[i] + "'";
			if (aTipoProv.length > 1 && i < aTipoProv.length - 1)
				lCondizioni += ",";

		}
		lCondizioni += ")";
		// Aggiunto il Decreto di Irreparibilita'
		lCondizioni += " OR (COD_TIPO_PROVVEDIMENTO = '02' AND COD_MOTIVO = '0282' AND FAS_SIU_ID_FASCICOLO_SIUS IS NULL)) ";
		return lCondizioni;
	}

	// 26/03/2019 MEV70 - Condizione di esclusione dei CodMotivo nella lista.
	private String condizioneEsclusioneCodMotivo(String[] aCodMotivo) {
		String lCondizioni = " AND ( COD_MOTIVO NOT IN (";
		for (int i = 0; i < aCodMotivo.length; i++) {
			lCondizioni += "'" + aCodMotivo[i] + "'";
			if (aCodMotivo.length > 1 && i < aCodMotivo.length - 1)
				lCondizioni += ",";

		}
		lCondizioni += "))";
		return lCondizioni;
	}

	// 27/03/2019 MEV70 - Condizione di inclusione dei CodMotivo nella lista.
	private String condizioneInclusioneCodMotivo(String[] aCodMotivo) {
		String lCondizioni = " AND ( COD_MOTIVO IN (";
		for (int i = 0; i < aCodMotivo.length; i++) {
			if (aCodMotivo[i].length() == 4) {
				lCondizioni += "'" + aCodMotivo[i] + "',";
			}
		}
		lCondizioni = (lCondizioni.substring(0, lCondizioni.length() - 1)) + "))";

		// 29/03/2018 Se l'ultimo elemento di CodMotivo è numerico con lunghezza > 4, va impostato come filtro
		// di ricerca per EVE_ID_EVENTO
		if ((aCodMotivo[aCodMotivo.length - 1].length() > 4)
				&& (aCodMotivo[aCodMotivo.length - 1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))) {
			lCondizioni += " AND EVE_ID_EVENTO = " + aCodMotivo[aCodMotivo.length - 1];
		}

		return lCondizioni;
	}

	private String condizioneTipoProvvedimentoIn(String[] aTipoProv) {
		String lCondizioni = " AND COD_TIPO_PROVVEDIMENTO IN (";
		for (int i = 0; i < aTipoProv.length; i++) {
			lCondizioni += "'" + aTipoProv[i] + "'";
			if (aTipoProv.length > 1 && i < aTipoProv.length - 1)
				lCondizioni += ",";

		}
		lCondizioni += ")";

		return lCondizioni;
	}

	/**
	 * Imposta la condizione IN (elenco uffici competenti) da utilizzare per es su COD_UFFICIO_INSERIMENTO
	 * Uffici competenti = (cod Ufficio + cod uffici accorpati)
	 *
	 * @param aUfficio
	 *            ufficio competente n.b. deve aver valorizzata la property mUfficiAccorpati
	 * @return
	 * @since 20/09/2013 per gestire le condizioni di competenza sugli eventi dei fascioli migrati da altro
	 *        ufficio
	 */
	@SuppressWarnings("rawtypes")
	private String condizioneUfficiCompetenti(UfficioModel aUfficio) {
		String lCondizioni = " IN ('" + aUfficio.getCodUfficio() + "'";

		if (aUfficio.getUfficiAccorpati() != null) {
			Iterator itxUffAccorpati = aUfficio.getUfficiAccorpati().iterator();

			while (itxUffAccorpati.hasNext()) {
				UfficioAccorpatoModel uffAcc = (UfficioAccorpatoModel) itxUffAccorpati.next();
				lCondizioni += ",'" + uffAcc.getCodUfficio() + "'";
			}
		}
		lCondizioni += ")";
		return lCondizioni;
	}

	public void ricercaEventoTipoCodMotiviProvNonValidato(EventoModel aModel, String[] aTipoProv)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "'";
		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoIn(aTipoProv);
			}
		}
		lStatement += " AND COD_MOTIVO = '" + aModel.getCodMotivo() + "'";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO ='N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	// AMBROS DECRETO LEGGE GIUGNO 2013
	public void ricercaComunicazioneNonRegistratoByFascicoloSiep(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '12')";

		// lStatement += " AND (EVENTO.COD_MOTIVO IN ('0057','0058','0059','0060'))";

		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	public void ricercaOrdineEsecuzioneNonRegistratoByFascicoloSiep(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '06')";
		// ordine esecuzione e ordine esecuzione con sospensione
		// commentato per poter aggiornare anche un evento di ordine esecuzione che abbia come motivo il
		// codice 0000 25-02-04
		// lStatement +=
		// " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%'OR CODMOV.RV_HIGH_VALUE
		// LIKE 'RS%')";

		// lStatement += " AND (EVENTO.COD_MOTIVO IN ('0057','0058','0059','0060'))";

		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// RICERCA EVENTO DI MISURA ALTERNATIVA DET DOM

	public void ricercaEventoMANonRegistratoMAByFascicoloSiep(BigDecimal aIdFascicolo, BigDecimal aEveKey)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '04')";
		lStatement += " AND  EVENTO.EVE_ID_EVENTO = " + aEveKey;
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// RICERCA EVENTO DI MISURA ALTERNATIVA DET DOM

	// ORDINE SCARCERAZIONE - Ordinanza
	public void ricercaEventoOSNonRegistratoMAByFascicoloSiep(BigDecimal aIdFascicolo, BigDecimal aEveKey)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '03')";
		lStatement += " AND  EVENTO.EVE_ID_EVENTO = " + aEveKey;
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// Decreto Sospensione
	public void ricercaOrdineEsecuzioneByIdFascicoloDecretoSospensione(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_MOTIVO IN ('0061','0062','0117','0105','0104','0063', '0364', '5509', '5510', '5511', '5512', '5513', '5506', '5507', '5508', '5525', '5526', '5530', '5531', '5532'))";
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='S') ";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// RICERCA OE LEGGE SIMEONE
	public void ricercaOrdineEsecuzioneLSNonRegistratoByFascicoloSiep(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '06')";
		lStatement += " AND (EVENTO.COD_MOTIVO IN ('0061','0063','0105','0104','0117'))";
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// RICERCA REVOCA OE LEGGE SIMEONE
	public void ricercaOrdineEsecuzioneRevocaLSNonRegistratoByFascicoloSiep(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '16')";
		lStatement += " AND (EVENTO.COD_MOTIVO IN ('0078','0079','0080'))";
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// RICERCA REVOCA OE LEGGE SIMEONE
	public void ricercaOrdineEsecuzioneRevocaLAlfNonRegistratoByFascicoloSiep(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '12')";
		lStatement += " AND (EVENTO.COD_MOTIVO IN ('0495','0496','0497'))";
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// 09/12/2010 RICERCA DECRETO SOSPENSIONE LEGGE ALFANO
	public void ricercaDecretoSospensioneLANonRegistratoByFascicoloSiep(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '04')";
		lStatement += " AND (EVENTO.COD_MOTIVO IN ('0499'))";
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// ricerca revoca acquisita
	public void ricercaRevocaOrdinanzaAcquisitaByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '03')";
		lStatement += " AND (EVENTO.COD_ESITO = '0002')";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		setStatement(lStatement);
	}

	// Istanza Rigettata
	public void ricercaRevocaOrdinanzaRigettataByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '03')";
		lStatement += " AND (EVENTO.COD_ESITO = '0002')";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	// Istanza con Inamissibilita'
	public void ricercaRevocaDecretoInammisibilitaByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO = '02')";
		lStatement += " AND (EVENTO.COD_ESITO = '0003')";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	public void ricercaEventoByFascicoloSius(BigDecimal aKey, String aTipoEvento) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		// MEV 39 IN ESTRAZIONE DEI PROVVEDIMNTI DELLA SORVEGLIANZA NON DEVE ESTRARRE GLI ORDINI DI
		// DIFFERIMENTO
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO <> '65' ";
		if (aTipoEvento != null)
			lStatement += " AND COD_TIPO_EVENTO = '" + aTipoEvento + "'";

		// lStatement += setOrder();
		lStatement += setOrderEventoDesc(); // Impostato un ordinamento piu' preciso del precedente.

		setStatement(lStatement);
	}

	/**
	 * MEV10-s3: aggiunto parametro di passaggio per gestire tipologia ufficio minorenni
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param strCodTipoUfficio
	 * @throws DAOException
	 */
	public void ricercaEventoXCFC(BigDecimal aKey, String aTipoEvento, String strCodTipoUfficio)
			throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;

		if (aTipoEvento != null)
			lStatement += " AND COD_TIPO_EVENTO = '" + aTipoEvento + "'";

		if ("TDSM".equals(strCodTipoUfficio) || "UDSM".equals(strCodTipoUfficio))
			lStatement += " AND (COD_TIPO_PROVVEDIMENTO = '01' OR COD_TIPO_PROVVEDIMENTO = '02' "
					+ "OR COD_TIPO_PROVVEDIMENTO = '03') ";
		else
			lStatement += " AND (COD_TIPO_PROVVEDIMENTO = '02' OR COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND DATA_TRASMISSIONE_ATTI IS NOT NULL  ";
		lStatement += setOrder();

		setStatement(lStatement);
	}

	public void ricercaEventoByFascEsitoParereInamm(BigDecimal aKey, String aTipoEvento) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		lStatement += " AND (COD_MOTIVO = '0750' OR COD_MOTIVO = '0751' OR COD_MOTIVO = '0752')";
		lStatement += " AND COD_TIPO_EVENTO = '08'";

		lStatement += setOrder();

		setStatement(lStatement);
	}

	public void ricercaEventoByIdFascicoloCodiceMotivo(BigDecimal aKey, String[] aMotivo)
			throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		if (aMotivo.length > 0) {
			lStatement += " AND COD_MOTIVO IN (";
			for (int i = 0; i < aMotivo.length; i++) {
				lStatement += "'" + aMotivo[i] + "'";
				if (aMotivo.length > 1 && i < aMotivo.length - 1)
					lStatement += ",";

			}
			lStatement += ")";
		}

		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO =  'S'";
		lStatement += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lStatement);
	}

	public void ricercaAltroEventoByFascicoloSius(BigDecimal aKey, String aTipoEvento) throws DAOException {
		String lStatement = getSqlQueryAltroEvento();
		lStatement += " AND EVENTO.COD_TIPO_EVENTO != '01' AND EVENTO.COD_TIPO_EVENTO != '05' ";
		lStatement += "AND FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;

		// if (aTipoEvento != null)
		// lStatement += " AND COD_TIPO_EVENTO = '" + aTipoEvento + "'";

		lStatement += setOrder();

		setStatement(lStatement);
	}

	public void ricercaOrdineEsecuzioneByIdFascicolo(BigDecimal aIdFascicoloSiep) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep + ")";
		lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%' ";
		lStatement += "  OR  CODMOV.RV_HIGH_VALUE LIKE 'RS%' OR CODMOV.RV_HIGH_VALUE LIKE 'LED%')";
		lStatement += setOrder();

		setStatement(lStatement);
	}

	public void ricercaOrdineEsecuzioneByIdFascicoloDescrInserimento(BigDecimal aIdFascicoloSiep)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep + ")";
		lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%' ";
		lStatement += "   OR CODMOV.RV_HIGH_VALUE LIKE 'RS%' OR CODMOV.RV_HIGH_VALUE LIKE 'LED%')";
		lStatement += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lStatement);
	}

	public void ricercaEventoByDataInserimentoUguale(EventoModel aModel) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		lStatement += " AND COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "'";
		lStatement += " AND DATA_INSERIMENTO = TO_DATE('"
				+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy HH:mm:ss")
				+ "','DD/MM/YYYY HH24:MI:SS')";

		setStatement(lStatement);
	}

	public void ricercaOrdineEsecuzioneSoloEventiVisualizzazioneByIdFascicolo(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		lStatement += setOrderEventoAsc();

		setStatement(lStatement);
	}

	/**
	 * Effettua la ricerca di tutti gli eventi presenti su un certo fascicolo ordinati per <b>DATA
	 * EMISSIONE</b> decrescente Restituisce anche gli eventi annullati o non validati
	 *
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaEventoByIdFascicoloSiep(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		lStatement += setOrder();

		setStatement(lStatement);
	}

	public void ricercaEventiValidatiPerDataInserimentoDescByIdFascicoloSiep(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";
		// Nell'Elenco Provvedimenti PM anche i Verbali. Luigi 3-2-06
		// Nell'Elenco Provvedimenti PM anche le richieste. Dario 14-3-06
		// Nell'Elenco Provvedimenti PM anche le Pene accessorie. Vincenzo 29-3-06
		lStatement += " AND COD_TIPO_EVENTO IN ('01','07','02','16','17','18')";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO NOT IN ('02','03')";
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";
		lStatement += " ORDER BY  DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	public void ricercaOrdineEsecuzioneSoloEventiIstanzeVisualizzazioneByIdFascicolo(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		lStatement += setOrder();

		setStatement(lStatement);
	}

	public void ricercaEventoTipoProv(EventoModel aModel) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		lStatement += " AND COD_TIPO_PROVVEDIMENTO ='" + aModel.getCodTipoProvvedimento() + "'";
		lStatement += " AND COD_MOTIVO IN ('0001','0002','0003')";
		lStatement += setOrder();

		setStatement(lStatement);
	}

	// ricerca evento annotazioni manuali
	public void ricercaEventoTipoMotProvEveDataEmis(EventoModel aModel) throws DAOException {
		String lStatement = getSqlQuery();

		String lDateStr = DateUtils.getDateToString(aModel.getDataEmissione(), "dd/MM/yyyy");

		lStatement += " AND COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "'";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "'";
		if (lDateStr != null) {
			lStatement += " AND DATA_EMISSIONE = TO_DATE('" + lDateStr + "','DD/MM/YYYY')";
		}
		if (aModel.getCodMotivo() != null) {
			lStatement += " AND COD_MOTIVO = '" + aModel.getCodMotivo() + "'";
		}
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();

		if (aModel != null && "N".equals(aModel.getFlagDocumentoRegistrato()))
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		if (aModel != null && "S".equals(aModel.getFlagDocumentoRegistrato()))
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'S')";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	/**
	 * Ricerca tutti gli eventi dello stesso tipo di quello passato in input (tipo evento, tipo provvedimento,
	 * motivo provvedimento) ma <b>NON VALIDATI</b>, ordinati per DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC,
	 * ID_EVENTO DESC
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaEventoTipoCodMotProvNonValidato(EventoModel aModel) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "'";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "'";
		lStatement += " AND COD_MOTIVO = '" + aModel.getCodMotivo() + "'";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		// Luigi 15-06-2009
		lStatement += " AND COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";

		lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO ='N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	/**
	 * Ricerca dell' ID_EVENTO di un record EVENTO che soddisfi le condizioni di filtro sui campi:
	 * COD_TIPO_EVENTO, COD_TIPO_PROVVEDIMENTO,COD_MOTIVO, FLAG_DOCUMENTO_REGISTRATO e
	 * FAS_SIE_ID_FASCICOLO_SIEP passate attraverso EventoModel aModel e che oltre tali condizioni sia anche
	 * collegato ad un PROVVEDIMENTO_SIGE e ad una ANNOTAZIONE_MANUALE. La funzione serve a trovare una
	 * Ordinanza del GE relativa alla concessione di un benefici (Amnistia/Indulto) e legata ad una Richiesta
	 * del PM attaverso l'ID del Prccedimento SIEP).
	 *
	 * @param EventoModel
	 *            aModel : filtro di ricerca sulla tabella EVENTO.
	 * @return BigDecimal IdEvento (null se la ricerca non restituisce nessun record)
	 * @throws DAOException
	 */

	public BigDecimal ricercaIdEventoProvSigeAnnMan(EventoModel aModel) throws DAOException {
		BigDecimal lIdEvento = null;

		String lStatement = "SELECT E.ID_EVENTO FROM EVENTO E JOIN PROVVEDIMENTO_SIGE P ON (P.ID_EVENTO_GENERATO = E.ID_EVENTO AND P.DATA_DEPOSITO IS NOT NULL)"
				+ "JOIN ANNOTAZIONE_MANUALE A ON (A.EVE_ID_EVENTO = E.ID_EVENTO)";

		lStatement += " WHERE E.COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "'";
		lStatement += " AND E.COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "'";
		lStatement += " AND E.COD_MOTIVO = '" + aModel.getCodMotivo() + "'";
		lStatement += " AND E.FLAG_DOCUMENTO_REGISTRATO = '" + aModel.getFlagDocumentoRegistrato() + "'";
		lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();

		lStatement += " ORDER BY ID_EVENTO DESC";

		setStatement(lStatement);

		start();

		if (next())
			lIdEvento = getBigDecimal("ID_EVENTO");
		stop();

		return lIdEvento;
	}

	public void ricercaEventoDecretoOrdinanzaNonRegistrato(BigDecimal aIdFascicoloSiep) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND COD_TIPO_EVENTO = '01'"; // PROVVEDIMENTO
		lStatement += " AND (COD_TIPO_PROVVEDIMENTO = '02' OR COD_TIPO_PROVVEDIMENTO = '03')"; // DECRETO o
																								// ORDINANZA
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
		lStatement += " AND DEC_ID_DECRETO_ORDINANZA_SIEP IS NOT NULL";
		lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO ='N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	/**
	 * Ricerca l'ultimo provvedimento (01-04) <b>non validato</b> collegato a un decreto_ordinanza_siep. ORDER
	 * BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC
	 *
	 * @param aIdFascicoloSiep
	 * @throws DAOException
	 */
	public void ricercaEventoDecretoOrdinanzaInterruzioneNonRegistrato(BigDecimal aIdFascicoloSiep)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND COD_TIPO_EVENTO = '01'"; // PROVVEDIMENTO
		lStatement += " AND (COD_TIPO_PROVVEDIMENTO = '04')"; // DECRETO o ORDINANZA
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
		lStatement += " AND DEC_ID_DECRETO_ORDINANZA_SIEP IS NOT NULL";
		lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO ='N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoTipoMotProvEveDocReg(EventoModel aModel) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "'";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "'";
		lStatement += " AND COD_MOTIVO IN ('0121','0122')";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = '" + aModel.getFlagDocumentoRegistrato() + "'";
		lStatement += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	/**
	 * Imposta la ricerca degli eventi in base a idFascicolo, tipoEvento, tipoProvvedimento,
	 * motivoProvvedimento recuperati dal model. Ordinati per data Inserimento decrescente
	 *
	 * @param aModel
	 *            - EventoModel con i parametri per la ricerca
	 * @param lFlagDocReg
	 *            se N o non specificato ricerca gli eventi non validati se S solo quelli non validati
	 * @throws DAOException
	 */
	public void ricercaEventoTipoEveTipoProvMot(EventoModel aModel, String lFlagDocReg) throws DAOException {
		String lStatement = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}

		if (aModel.getCodMotivo() != null) {
			if (aModel.getCodMotivo().compareTo("") != 0)
				lStatement += " AND COD_MOTIVO ='" + aModel.getCodMotivo() + "'";
		}

		if (aModel.getCodTipoEvento() != null) {
			if (aModel.getCodTipoEvento().compareTo("") != 0)
				lStatement += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aModel.getCodTipoProvvedimento() != null) {
			if (aModel.getCodTipoProvvedimento().compareTo("") != 0)
				lStatement += " AND COD_TIPO_PROVVEDIMENTO ='" + aModel.getCodTipoProvvedimento() + "'";
		}

		if (lFlagDocReg != null && "N".equals(lFlagDocReg))
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		if (lFlagDocReg != null && "S".equals(lFlagDocReg))
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'S')";

		lStatement += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	public void ricercaIstanzeByFascicolo(BigDecimal aIdFascicoloSiep) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '03')";
		lStatement += " AND (EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep
				+ ") ORDER BY DATA_EMISSIONE desc ";

		setStatement(lStatement);
	}

	// ricerca evento non registrato
	public void ricercaEventoNonRegistrato(EventoModel aModel) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " " + setCondizioni(aModel);

		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		// lStatement += " ORDER BY DATA_INSERIMENTO DESC ";

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoPerMotivo(String[] aMotivo, EventoModel aModel) throws DAOException {
		String lCondizioni = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}

		if (aModel.getCodTipoEvento() != null) {
			if (aModel.getCodTipoEvento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aModel.getCodTipoProvvedimento() != null) {
			if (aModel.getCodTipoProvvedimento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_PROVVEDIMENTO ='" + aModel.getCodTipoProvvedimento() + "'";
		}

		if (aMotivo.length > 0) {
			lCondizioni += " AND COD_MOTIVO IN (";
			for (int i = 0; i < aMotivo.length; i++) {
				lCondizioni += "'" + aMotivo[i] + "'";
				if (aMotivo.length > 1 && i < aMotivo.length - 1)
					lCondizioni += ",";

			}
			lCondizioni += ")";
		}

		if (aModel.getFlagDocumentoRegistrato() != null) {
			if (aModel.getFlagDocumentoRegistrato().compareTo("") != 0)
				lCondizioni += " AND FLAG_DOCUMENTO_REGISTRATO ='" + aModel.getFlagDocumentoRegistrato()
						+ "'";
		}

		lCondizioni += " ORDER BY DATA_INSERIMENTO DESC ";
		setStatement(lCondizioni);
	}

	public void ricercaEventoPerMotivoOrderDesc(String[] aMotivo, EventoModel aModel) throws DAOException {
		String lCondizioni = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}

		if (aModel.getCodTipoEvento() != null) {
			if (aModel.getCodTipoEvento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aModel.getCodTipoProvvedimento() != null) {
			if (aModel.getCodTipoProvvedimento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_PROVVEDIMENTO ='" + aModel.getCodTipoProvvedimento() + "'";
		}

		if (aMotivo != null && aMotivo.length > 0) {
			lCondizioni += " AND COD_MOTIVO IN (";
			for (int i = 0; i < aMotivo.length; i++) {
				lCondizioni += "'" + aMotivo[i] + "'";
				if (aMotivo.length > 1 && i < aMotivo.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		if (aModel.getFlagDocumentoRegistrato() != null) {
			if (aModel.getFlagDocumentoRegistrato().compareTo("") != 0)
				lCondizioni += " AND FLAG_DOCUMENTO_REGISTRATO ='" + aModel.getFlagDocumentoRegistrato()
						+ "'";
		}

		lCondizioni += setOrderEventoDesc();

		setStatement(lCondizioni);
	}

	// STUB 29/09/2005 REWORK STATO ESECUZIONE
	/**
	 * Imposta la query di ricerca di tutti gli eventi che hanno codice motivo e tipo provvedimento tra quelli
	 * passati in input, mentre le condizione su idFascicolo codTipoEvento e FlagDocumento registrato vengono
	 * recuperati del Model. I dati vengono restituiti per data inserimento decrescente.
	 *
	 * @param aMotivo
	 *            vettore contenente l'elenco dei codici motivo su cui effettuare la ricerca
	 * @param aTipoProvv
	 *            vettore contenente l'elenco dei codici tipo provvedimento su cui effettuare la ricerca
	 * @param aModel
	 *            model da cui vengono estratti IdFascicolo, codTipoEvento e flagDocumentoRegistrato
	 */
	public void ricercaEventoPerMotivoPerProvv(String[] aMotivo, String[] aTipoProvv, EventoModel aModel)
			throws DAOException {
		String lCondizioni = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}

		if (aModel.getCodTipoEvento() != null) {
			if (aModel.getCodTipoEvento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aTipoProvv.length > 0) {
			lCondizioni += " AND COD_TIPO_PROVVEDIMENTO IN (";
			for (int i = 0; i < aTipoProvv.length; i++) {
				lCondizioni += "'" + aTipoProvv[i] + "'";
				if (aTipoProvv.length > 1 && i < aTipoProvv.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		if (aMotivo.length > 0) {
			lCondizioni += " AND COD_MOTIVO IN (";
			for (int i = 0; i < aMotivo.length; i++) {
				lCondizioni += "'" + aMotivo[i] + "'";
				if (aMotivo.length > 1 && i < aMotivo.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		// nella funzionalità 'Reclamo Rimedi Risarcitori D.L. 26 n. 92'
		// vengono scartati dalla lista i record con ESITO_PROVVEDIMENTO = '0601' (Fissazione Udienza)
		if (aMotivo.length > 0) {
			for (int i = 0; i < aMotivo.length; i++) {
				// 20170905: [SG] prevenzione nullpointer, invertiti parametri del confronto
				if ("9027".equals(aMotivo[i]))
					lCondizioni += "AND COD_ESITO NOT IN ('0601')";
			}
		}

		if (aModel.getFlagDocumentoRegistrato() != null) {
			if (aModel.getFlagDocumentoRegistrato().compareTo("") != 0)
				lCondizioni += " AND FLAG_DOCUMENTO_REGISTRATO ='" + aModel.getFlagDocumentoRegistrato()
						+ "'";
		}

		lCondizioni += " ORDER BY DATA_INSERIMENTO DESC ";
		setStatement(lCondizioni);
	}

	public void ricercaEventoNonRegistratoByKey(BigDecimal aIdEvento) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND ID_EVENTO =" + aIdEvento;
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		setStatement(lStatement);
	}

	/**
	 * Recupera l'ultimo evento (idEvento) inserito o aggiornato dall'operatore)
	 *
	 * @param aCodUtente
	 *            - codOperatore Inserimento o aggiornamento
	 * @throws DAOException
	 */
	public void ricercaUltimoEventoGeneratoByCodUtente(String aCodUtente) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND ID_EVENTO=(";
		lStatement += "SELECT MAX(ID_EVENTO) FROM EVENTO WHERE COD_OPERATORE_INSERIMENTO='" + aCodUtente
				+ "' OR COD_OPERATORE_AGGIORNAMENTO='" + aCodUtente + "')";
		setStatement(lStatement);
	}

	public void ricercaUltimoEventoByFasSius(String aIdFasSius) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND ID_EVENTO IN (";
		lStatement += "SELECT MAX(ID_EVENTO) FROM EVENTO WHERE FAS_SIU_ID_FASCICOLO_SIUS='"
				+ aIdFasSius.toString() + "')";
		setStatement(lStatement);
	}

	public void ricercaUltimoEventoByFasSiusINCodProvvNOTCodEsiti(String aIdFasSius,
			String[] aCodTipiProvvedimento, String[] aCodEsiti) throws DAOException {
		String lStatement = getSqlQuery();

		// include i tipi provvedimento nell'array
		if (aCodTipiProvvedimento.length > 0) {
			lStatement += " AND COD_TIPO_PROVVEDIMENTO IN( ";
			boolean isInserted = false;
			for (String tmp : aCodTipiProvvedimento) {
				lStatement += (isInserted ? "," : "") + "'" + tmp + "'";
				isInserted = true;
			}
			lStatement += " )";
		}

		// Esclude gli esiti nell'array
		if (aCodEsiti.length > 0) {
			lStatement += " AND COD_ESITO NOT IN( ";
			boolean isInserted = false;
			for (String tmp : aCodEsiti) {
				lStatement += (isInserted ? "," : "") + "'" + tmp + "'";
				isInserted = true;
			}
			lStatement += " )";
		}

		lStatement += " AND ID_EVENTO IN ( ";
		lStatement += "SELECT MAX(ID_EVENTO) FROM EVENTO WHERE FAS_SIU_ID_FASCICOLO_SIUS='"
				+ aIdFasSius.toString() + "')";
		setStatement(lStatement);
	}

	public void ricercaEventoValidoByRicConvFasSiusCodProvvCodMotivo(String aIdRicConv, EventoModel aModel,
			String[] aCodTipoProvvedimento, String[] aCodMotivo, String[] aCodEsito) throws DAOException {

		String lStatement = new String("");

		lStatement = " SELECT EV.ID_EVENTO from EVENTO EV "
				+ " join FASCICOLO_SIUS FS on ( EV.FAS_SIU_ID_FASCICOLO_SIUS = FS.ID_FASCICOLO_SIUS ";

		// include i tipi provvedimento nell'array
		if (aCodTipoProvvedimento.length > 0) {
			lStatement += " and EV.COD_TIPO_PROVVEDIMENTO IN( ";
			boolean isInserted = false;
			for (String tmp : aCodTipoProvvedimento) {
				lStatement += (isInserted ? "," : "") + "'" + tmp + "'";
				isInserted = true;
			}
			lStatement += " )";
		}
		// Include i codici motivo nell'array
		if (aCodMotivo.length > 0) {
			lStatement += " and EV.COD_MOTIVO IN( ";
			boolean isInserted = false;
			for (String tmp : aCodMotivo) {
				lStatement += (isInserted ? "," : "") + "'" + tmp + "'";
				isInserted = true;
			}
			lStatement += " )";
		}
		// Esclude i codici esito nell'array
		if (aCodEsito.length > 0) {
			lStatement += " and EV.COD_ESITO NOT IN( ";
			boolean isInserted = false;
			for (String tmp : aCodEsito) {
				lStatement += (isInserted ? "," : "") + "'" + tmp + "'";
				isInserted = true;
			}
			lStatement += " )";
		}
		lStatement += " and EV.FLAG_DOCUMENTO_REGISTRATO <> 'A') ";
		lStatement += " join RICHIESTA_CONVERSIONE RI on ( RI.FAS_SIU_ID_FASCICOLO_SIUS = FS.ID_FASCICOLO_SIUS ) ";
		lStatement += " WHERE RI.ID_RICHIESTA_CONVERSIONE = '" + aIdRicConv.toString() + "'";
		lStatement += " AND EV.ID_EVENTO IN ( ";
		lStatement += " SELECT MAX(ID_EVENTO) FROM EVENTO WHERE FAS_SIU_ID_FASCICOLO_SIUS = FS.ID_FASCICOLO_SIUS) ";
		setStatement(lStatement);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Query >>>>>>>>> " + lStatement);
	}

	public void ricercaEventoValidatoByAnnIdAnnotazioneManuale(BigDecimal aAnnIdAnnotazioneManuale)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND ANN_ID_ANNOTAZIONE_MANUALE = " + aAnnIdAnnotazioneManuale;
		lStatement += setCondizioneEventiValidati();
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	/**
	 * Effettua la ricerca di tutti gli eventi presenti su un certo fascicolo ordinati per <b>DATA
	 * EMISSIONE</b> decrescente Restituisce anche gli eventi annullati o non validati
	 *
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaEventoByIdFascicoloSiepValidati(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += setCondizioneEventiValidati();
		lStatement += setOrder();

		setStatement(lStatement);
	}

	/**
	 * Costruisce la query in join con la tabella CG_REF_CODES per recuperare le descrizioni dei codici
	 *
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQuery() throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		// GDV
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE,";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI,";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " COD_OPERATORE_INSERIMENTO, ";
		lStatement += " DATA_INSERIMENTO,";
		lStatement += " COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " DATA_AGGIORNAMENTO,";
		lStatement += " COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  "; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP, ";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA, "; // STUB 09/02/2006
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " ISTR_ID_ISTRUTTORIA_CUMULO, ";
		lStatement += " ESTREMI_SOGG_RICH_ISTR, ";
		lStatement += " KEY_ESEC_NSC, ";
		lStatement += " DATA_INVIO_ATTI, ";
		lStatement += " UFF_EMI.COD_TIPO_UFFICIO as TIPO_COD_UFFICIO_EMITTENTE, ";
		lStatement += " TIPOLOGIA_INVIO_ATTI ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		// STUB 07/10/2005 lStatement +=
		// " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR
		// CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND";
		// //****
		lStatement += " ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') OR";
		lStatement += " (EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) AND";

		// Modifica del 30/11/2016, la riga successiva provoca la duplicazione dei record
		// pertanto si commenta e si sostituisce con la riga successiva
		// lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN in
		// ('ESITO_PROVVEDIMENTO','TENORE_DECISIONE_RICORSO_SIGE') AND ";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND (CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' OR (CODESI.RV_DOMAIN = 'TENORE_DECISIONE_RICORSO_SIGE' AND CODESI.RV_LOW_VALUE <> '-')) AND ";

		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

		return lStatement;
	}

	/**
	 * Costruisce la query in join con la tabella CG_REF_CODES per recuperare le descrizioni dei codici
	 * (ESITO_TENORE)
	 *
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQueryTenore() throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		// GDV
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " COD_OPERATORE_INSERIMENTO, ";
		lStatement += " DATA_INSERIMENTO,";
		lStatement += " COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " DATA_AGGIORNAMENTO,";
		lStatement += " COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  "; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP, ";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA, "; // STUB 09/02/2006
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO, ";
		lStatement += " EVENTO.ESTREMI_SOGG_RICH_ISTR, ";
		lStatement += " KEY_ESEC_NSC ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		// STUB 07/10/2005 lStatement +=
		// " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR
		// CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND";
		// //****
		lStatement += " ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') OR";
		lStatement += " (EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_TENORE'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

		return lStatement;
	}

	protected String getSqlQueryEventoFascicolo() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		// GDV
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " EVENTO.COD_OPERATORE_INSERIMENTO, ";
		lStatement += " EVENTO.DATA_INSERIMENTO,";
		lStatement += " EVENTO.COD_UFFICIO_INSERIMENTO,";
		lStatement += " EVENTO.COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " EVENTO.DATA_AGGIORNAMENTO,";
		lStatement += " EVENTO.COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " EVENTO.FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " EVENTO.FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  "; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP, ";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA, "; // STUB 09/02/2006
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " fasc.CHIAVE_ANNO, ";
		lStatement += " fasc.CHIAVE_PROGR, ";
		lStatement += " EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO, ";
		lStatement += " EVENTO.ESTREMI_SOGG_RICH_ISTR, ";

		// modifica 17-01-2008 dario
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " KEY_ESEC_NSC ";

		lStatement += " FROM EVENTO, FASCICOLO_SIEP fasc, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = fasc.ID_FASCICOLO_SIEP AND";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		// STUB 07/10/2005 lStatement +=
		// " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR
		// CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND";
		// //****
		lStatement += " ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') OR";
		lStatement += " (EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

		return lStatement;
	}

	/**
	 *
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQueryEsitoParereInamm() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, null COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, null COD_PRO,";
		// GDV
		lStatement += " null COD_ABBR,";
		lStatement += " COD_MOTIVO, null COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, null DESC_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, null LUO_EMI, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " null COD_TIPO_UFFICIO_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, null COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, null LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " COD_OPERATORE_INSERIMENTO, ";
		lStatement += " DATA_INSERIMENTO,";
		lStatement += " COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " DATA_AGGIORNAMENTO,";
		lStatement += " COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " null DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  "; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP,  ";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA,  "; // STUB 09/02/2006
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO, ";
		lStatement += " EVENTO.ESTREMI_SOGG_RICH_ISTR, ";
		lStatement += " KEY_ESEC_NSC ";
		lStatement += " FROM EVENTO";

		return lStatement;
	}

	protected String getSqlQueryAltroEvento() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		// GDV---
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " COD_OPERATORE_INSERIMENTO, ";
		lStatement += " DATA_INSERIMENTO,";
		lStatement += " COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " DATA_AGGIORNAMENTO,";
		lStatement += " COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  "; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP,  ";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA, "; // STUB 09/02/2006
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO, ";
		lStatement += " EVENTO.ESTREMI_SOGG_RICH_ISTR, ";
		lStatement += " KEY_ESEC_NSC ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_EVENTO != '01' AND EVENTO.COD_TIPO_EVENTO != '05' AND ";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

		return lStatement;
	}

	protected String getSqlQueryForTrasmAtti() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO, COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , COD_LUOGO_EMITTENTE, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " LUOEMI.DESCRIZIONE LUO_EMI, UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, COGNOME_SOGGETTO_PRESENTANTE, DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI, FLAG_PIU_MENO, DATA_TRASMISSIONE_ATTI, DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES, ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, DOC_BLOB, COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, FLAG_DOCUMENTO_REGISTRATO, COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, FAS_SIU_ID_FASCICOLO_SIUS_DEST, EVE_ID_EVENTO, EVE_ID_EVENTO_REVOCA, ANN_ID_ANNOTAZIONE_MANUALE, PEN_ID_PENA_RESIDUA, ";
		lStatement += " TEM_ID_TEMPLATE, FLAG_STAMPA_SIEP, FLAG_STAMPA_SIUS, FLAG_VIDEO_SIEP, FLAG_VIDEO_SIUS, DEC_ID_DECRETO_ORDINANZA_SIEP, PEN_ACC_ID_PENA_ACCESSORIA, "; // add
																																												// by
																																												// Enzo

		// MEV26 Cumulo
		lStatement += " EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO, ";
		lStatement += " EVENTO.ESTREMI_SOGG_RICH_ISTR, ";
		// modifica 17-01-2008 dario
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " KEY_ESEC_NSC ";

		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI, CG_REF_CODES UFF_TIPO_DES,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO";

		return lStatement;
	}

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
		// MEV10-s3: aggiunto campo in estrazione
		aModel.setCodTipoUfficioEmittente(getString("COD_TIPO_UFFICIO_EMITTENTE"));
		aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
		aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("COD_ESI"));
		aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setDescrLuogoDestinatario(getString("LUO_DES"));
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
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));

		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));

		aModel.setDecIdDecretoOrdinanzaSiep(getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP"));
		aModel.setPenAccIdPenaAccessoria(getBigDecimal("PEN_ACC_ID_PENA_ACCESSORIA")); // STUB 09/02/2006
		aModel.setLegge(getString("COD_ABBR"));
		aModel.setEveIdEventoRevoca(getBigDecimal("EVE_ID_EVENTO_REVOCA"));
		aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
		aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));
		aModel.setDataEspulsioneSanzSost(getDate("DATA_ESPULSIONE_SANZ_SOST"));
		aModel.setDataRichiesta(getDate("DATA_RICHIESTA"));
		aModel.setKeyEsecNsc(getBigDecimal("KEY_ESEC_NSC"));

		// MEV26-Cumulo
		try {
			aModel.setIstruidIstruttoriaCumulo(getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"));
		} catch (Exception sqex) {
		}
		try {
			aModel.setEstremiSoggRichIstr(getString("ESTREMI_SOGG_RICH_ISTR"));
		} catch (Exception sqex) {
		}
		// 20200129 [SG]: TICKET#20200128019 quanto sopra scrive eccezione in server.log
		// if (findColumn("ISTR_ID_ISTRUTTORIA_CUMULO"))
		// aModel.setIstruidIstruttoriaCumulo(getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"));
		// if (findColumn("ESTREMI_SOGG_RICH_ISTR"))
		// aModel.setEstremiSoggRichIstr(getString("ESTREMI_SOGG_RICH_ISTR"));

		if (findColumn("TIPOLOGIA_INVIO_ATTI"))
			aModel.setCodTipologiaInvioAtti(getString("TIPOLOGIA_INVIO_ATTI"));

		if (findColumn("DESCRIZIONE_INVIO_ATTI"))
			aModel.setDescrizioneInvioAtti(getString("DESCRIZIONE_INVIO_ATTI"));

		if (findColumn("DATA_INVIO_ATTI"))
			aModel.setDataInvioAtti(getDate("DATA_INVIO_ATTI"));

		if (findColumn("DESCR_TIPOLOGIA_INVIO_ATTI"))
			aModel.setDescrizioneTipologiaInvioAtti(getString("DESCR_TIPOLOGIA_INVIO_ATTI"));

		if (findColumn("DATA_INVIO_ATTI"))
			aModel.setDataInvioAtti(getDate("DATA_INVIO_ATTI"));

		if (findColumn("TIPOLOGIA_INVIO_ATTI"))
			aModel.setCodTipologiaInvioAtti(getString("TIPOLOGIA_INVIO_ATTI"));

		if (findColumn("TIPO_COD_UFFICIO_EMITTENTE"))
			aModel.setCodTipoUfficioEmittente(getString("TIPO_COD_UFFICIO_EMITTENTE"));

		return aModel;
	}

	public GenericModel getModelEventoFascicolo() throws DAOException {
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
		// MEV10-s3: aggiunto campo in estrazione
		aModel.setCodTipoUfficioEmittente(getString("COD_TIPO_UFFICIO_EMITTENTE"));
		aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
		aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("COD_ESI"));
		aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setDescrLuogoDestinatario(getString("LUO_DES"));
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
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		// Add 20030713 By paolo
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));

		aModel.setDecIdDecretoOrdinanzaSiep(getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP"));
		aModel.setPenAccIdPenaAccessoria(getBigDecimal("PEN_ACC_ID_PENA_ACCESSORIA")); // STUB 09/02/2006
		aModel.setLegge(getString("COD_ABBR"));
		aModel.setEveIdEventoRevoca(getBigDecimal("EVE_ID_EVENTO_REVOCA"));
		aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
		aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));

		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));

		aModel.setDataRichiesta(getDate("DATA_RICHIESTA"));
		aModel.setKeyEsecNsc(getBigDecimal("KEY_ESEC_NSC"));

		try {
			aModel.setEstremiSoggRichIstr(getString("ESTREMI_SOGG_RICH_ISTR"));
		} catch (Exception sqex) {
		}

		return aModel;
	}

	public MisuraAlternativaAggregatoModel getModelDecretoOrdinanzaUfficio() throws DAOException {
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
		// MEV10-s3: aggiunto campo in estrazione
		aModel.setCodTipoUfficioEmittente(getString("COD_TIPO_UFFICIO_EMITTENTE"));
		aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
		aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("COD_ESI"));
		// mev56 Inizio
		aModel.setDescEsitoTemplate(getString("DESC_ESITO_TEMPLARE"));
		// mev56 Fine
		aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setDescrLuogoDestinatario(getString("LUO_DES"));
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
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));

		aModel.setDecIdDecretoOrdinanzaSiep(getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP"));
		aModel.setPenAccIdPenaAccessoria(getBigDecimal("PEN_ACC_ID_PENA_ACCESSORIA")); // STUB 09/02/2006
		aModel.setLegge(getString("COD_ABBR"));
		aModel.setEveIdEventoRevoca(getBigDecimal("EVE_ID_EVENTO_REVOCA"));
		aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
		aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));
		aModel.setCodUfficio(getString("cod_tipo_ufficio"));

		aModel.setDataRichiesta(getDate("DATA_RICHIESTA"));
		aModel.setKeyEsecNsc(getBigDecimal("KEY_ESEC_NSC"));

		EventoNotificaModel lEveNotMd = new EventoNotificaModel();
		lEveNotMd.setEvento(aModel);

		DepositoDecretoModel lDepDec = new DepositoDecretoModel();
		lDepDec.setAnnoS72(getBigDecimal("ANNO_S72"));
		lDepDec.setNumS72(getBigDecimal("NUM_S72"));

		DepositoOrdinanzaPcModel lDepOrd = new DepositoOrdinanzaPcModel();
		lDepOrd.setAnnoS3(getBigDecimal("ANNO_S3"));
		lDepOrd.setNumS3(getBigDecimal("NUM_S3"));

		MisuraAlternativaAggregatoModel lAgg = new MisuraAlternativaAggregatoModel();
		lAgg.setDepositoDecreto(lDepDec);
		lAgg.setDepositoOrdinanzaPc(lDepOrd);
		lAgg.setEventoNotifica(lEveNotMd);

		return lAgg;
	}

	/**
	 * Costruisce la query in join con la tabella CG_REF_CODES per recuperare le descrizioni dei codici
	 *
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQueryDecretoOrdinanzaUfficio() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";
		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " EVENTO.DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		// mev56 Inizio
		lStatement += " CODESI.RV_ABBREVIATION DESC_ESITO_TEMPLARE,";
		// mev56 Fine
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " EVENTO.COD_OPERATORE_INSERIMENTO, ";
		lStatement += " EVENTO.DATA_INSERIMENTO,";
		lStatement += " EVENTO.COD_UFFICIO_INSERIMENTO,";
		lStatement += " EVENTO.COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " EVENTO.DATA_AGGIORNAMENTO,";
		lStatement += " uff_emi.cod_tipo_ufficio,";
		lStatement += " EVENTO.COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " EVENTO.COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  ";
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP, ";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA, ";
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DEPOSITO_DECRETO.ANNO_S72, ";
		lStatement += " DEPOSITO_DECRETO.NUM_S72, ";
		lStatement += " DEPOSITO_ORDINANZA_PC.ANNO_S3, ";
		lStatement += " DEPOSITO_ORDINANZA_PC.NUM_S3, ";
		// MEV26Cumulo
		lStatement += " EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO, ";
		// modifica 17-01-2008 dario
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " KEY_ESEC_NSC ";

		lStatement += " FROM EVENTO,DEPOSITO_DECRETO,DEPOSITO_ORDINANZA_PC, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') OR";
		lStatement += " (EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";
		lStatement += " AND(DEPOSITO_ORDINANZA_PC.ID_EVENTO_GENERATO(+) = ID_EVENTO AND  DEPOSITO_DECRETO.ID_EVENTO_GENERATO(+) = ID_EVENTO)";

		return lStatement;
	}

	/**
	 * Imposta l'order by DATA_EMISSIONE desc
	 *
	 * @return
	 */
	private String setOrderEventoDesc() {
		String lCondizioni = " ORDER BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC";

		return lCondizioni;
	}

	/**
	 * Imposta l'order by DATA_INSERIMENTO desc
	 *
	 * @return
	 */
	private String setOrderEventoDataInserimentoDesc() {
		String lCondizioni = " ORDER BY DATA_INSERIMENTO DESC, DATA_EMISSIONE DESC, ID_EVENTO DESC";

		return lCondizioni;
	}

	/**
	 * Imposta l'orderby DATA_EMISSIONE ASC
	 *
	 * @return
	 */
	private String setOrderEventoAsc() {
		String lCondizioni = " ORDER BY EVENTO.DATA_EMISSIONE, EVENTO.DATA_INSERIMENTO, EVENTO.ID_EVENTO ";

		return lCondizioni;
	}

	/**
	 * Imposta l'orderby DATA_INSERIMENTO ASC
	 *
	 * @return
	 */
	private String setOrderEventoDataInserimentoAsc() {
		String lCondizioni = " ORDER BY EVENTO.DATA_INSERIMENTO, EVENTO.DATA_EMISSIONE, EVENTO.ID_EVENTO ";

		return lCondizioni;
	}

	/**
	 *
	 * @param aModel
	 * @return
	 */
	public String setCondizioni(EventoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}

		// Paolo Cherubini modifico con quella sotto per accettare piu' motivi
		/*
		 * if (aModel.getCodMotivo() != null) { if (aModel.getCodMotivo().compareTo("") != 0) lCondizioni +=
		 * " AND COD_MOTIVO ='" + aModel.getCodMotivo() + "'"; }
		 */

		if (aModel.getCodMotivo() != null) {
			String[] aMotivo = aModel.getCodMotivo().split(",");
			if (aMotivo.length > 0) {
				lCondizioni += " AND COD_MOTIVO IN (";
				for (int i = 0; i < aMotivo.length; i++) {
					lCondizioni += "'" + aMotivo[i] + "'";
					if (aMotivo.length > 1 && i < aMotivo.length - 1)
						lCondizioni += ",";

				}
				lCondizioni += ")";
			}
		}
		// fine paolo

		if (aModel.getCodTipoEvento() != null) {
			if (aModel.getCodTipoEvento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aModel.getCodTipoProvvedimento() != null) {
			if (aModel.getCodTipoProvvedimento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_PROVVEDIMENTO ='" + aModel.getCodTipoProvvedimento() + "'";
		}

		if (aModel.getFasSiuIdFascicoloSius() != null) {
			if (aModel.getFasSiuIdFascicoloSius().intValue() != 0)
				lCondizioni += " AND FAS_SIU_ID_FASCICOLO_SIUS ='" + aModel.getFasSiuIdFascicoloSius() + "'";
		}

		if (aModel.getFlagDocumentoRegistrato() != null) {
			if (aModel.getFlagDocumentoRegistrato().compareTo("S") == 0) {
				lCondizioni += " AND FLAG_DOCUMENTO_REGISTRATO = 'S' ";
			} else {
				if (aModel.getFlagDocumentoRegistrato().compareTo("N") == 0)
					lCondizioni += " AND FLAG_DOCUMENTO_REGISTRATO = 'N' ";
			}
		}

		if (aModel.getDecIdDecretoOrdinanzaSiep() != null) {
			lCondizioni += " AND DEC_ID_DECRETO_ORDINANZA_SIEP = '" + aModel.getDecIdDecretoOrdinanzaSiep()
					+ "'";
		}

		return lCondizioni;
	}

	/**
	 *
	 * @return
	 */
	private String setOrder() {
		String lCondizioni = new String(" ORDER BY DATA_EMISSIONE DESC ");

		return lCondizioni;
	}

	private String setCondizioneEventiNonAnnullati() {
		String lCondizioni = "AND (FLAG_DOCUMENTO_REGISTRATO IS NULL OR FLAG_DOCUMENTO_REGISTRATO  <> 'A') ";

		return lCondizioni;
	}

	private String setCondizioneEventiValidati() {
		String lCondizioni = " AND FLAG_DOCUMENTO_REGISTRATO = 'S' ";

		return lCondizioni;
	}

	/**
	 * Ritorna dalla tabella evento lo stato del flag documento registrato.
	 * <p>
	 *
	 * @param aIdEvento
	 *            id dell'evento.
	 * @return il valore del flag del documento.
	 * @throws DAOException
	 *             propagazioe dell'errore di eccezione.
	 */
	public String getFlagDocumentoRegistrato(BigDecimal aIdEvento) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT FLAG_DOCUMENTO_REGISTRATO ";
		lStatement += " FROM EVENTO ";
		lStatement += " WHERE EVENTO.ID_EVENTO = " + aIdEvento;

		setStatement(lStatement);
		start();

		String lFlag = null;

		if (next())
			lFlag = getString("FLAG_DOCUMENTO_REGISTRATO");

		stop();

		return lFlag;
	}

	public BigDecimal getProgressivo(EventoModel aEvento) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_PROTOCOLLO) aMAX";
		lStatement += " FROM EVENTO ";
		lStatement += " WHERE EVENTO.ANNO_PROTOCOLLO = " + aEvento.getAnnoProtocollo();
		lStatement += " AND EVENTO.COD_UFFICIO_INSERIMENTO = '" + aEvento.getCodUfficioInserimento() + "'";
		lStatement += " AND EVENTO.COD_MOTIVO = '" + aEvento.getCodMotivo() + "'";

		setStatement(lStatement);
		start();

		BigDecimal lBigDec = new BigDecimal(0);

		if (next() && (getBigDecimal("aMAX") != null))
			lBigDec = getBigDecimal("aMAX");

		stop();

		if (lBigDec == null)
			lBigDec = new BigDecimal(0);

		return lBigDec;
	}

	/**
	 * Metodo che esegue la ricerca degli eventi afferenti al rinvio ( cod_esito = 603 ) udienza per id
	 * fascicolo sius e che abbiamo la data_inserimento maggiore dell'evento di riferimento. Come parametri
	 * sono passati id fascicolo sius e id evento di riferimento. N.B.: il metodo esegue lo start e stop del
	 * dao per eseguire la ricerca ritornando il numero di eventi riscontrati.
	 * <p>
	 *
	 * @param aIdFasSius
	 *            id fascicolo sius.
	 * @param aIdEvento
	 *            id evento di riferimento
	 * @return numero di eventi occorsi.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public int getNumEventiPerCheckCancRinvioUdienza(BigDecimal aIdFasSius, BigDecimal aIdEvento)
			throws DAOException {
		int lCount = 0;
		String lStatement = new String();

		lStatement = "SELECT count(*) AS COUNT FROM EVENTO " + " WHERE FAS_SIU_ID_FASCICOLO_SIUS = "
				+ aIdFasSius + "  AND COD_ESITO = '0603' "
				+ "  AND ( FLAG_DOCUMENTO_REGISTRATO <> 'A' OR FLAG_DOCUMENTO_REGISTRATO IS NULL ) "
				+ "  AND DATA_INSERIMENTO > ( SELECT DATA_INSERIMENTO FROM EVENTO WHERE ID_EVENTO ="
				+ aIdEvento + " )";

		setStatement(lStatement);

		start();

		if (next())
			lCount = getInt("COUNT");

		stop();

		return lCount;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di documenti allegati ad un provvedimento
	 * (evento).
	 * </p>
	 * La ricerca viene effettuata nella tabella DOCUMENTO_ALLEGATO con chiave di ricerca EVE_ID_EVENTO.
	 *
	 * @param BigDecimal
	 *            aIdEve : Identificativo Evento
	 * @return int : numero documenti allegati all'evento.
	 * @throws DAOException
	 */

	public int getNumDocumentiAllegati(BigDecimal aIdEve, String aCodTipoDoc) throws DAOException {
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from DOCUMENTO_ALLEGATO  where EVE_ID_EVENTO = "
				+ aIdEve;
		if (aCodTipoDoc != null && aCodTipoDoc.length() > 1)
			lStatement += " AND COD_TIPO_DOCUMENTO = '" + aCodTipoDoc + "'";
		lStatement += " AND DATA_ANNULLAMENTO IS  NULL ";

		setStatement(lStatement);

		start();
		if (next()) {
			lCount = getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di documenti allegati validati relativi ad un
	 * provvedimento (evento).
	 * </p>
	 * La ricerca viene effettuata nella tabella DOCUMENTO_ALLEGATO con chiave di ricerca EVE_ID_EVENTO.
	 *
	 * @param BigDecimal
	 *            aIdEve : Identificativo Evento
	 * @return int : numero documenti allegati all'evento nello stato VALIDATI.
	 * @throws DAOException
	 */

	public int getNumAllegatiValidati(BigDecimal aIdEve) throws DAOException {
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from DOCUMENTO_ALLEGATO  where FLAG_DOCUMENTO_REGISTRATO = 'S' AND EVE_ID_EVENTO = "
				+ aIdEve;
		setStatement(lStatement);

		start();
		if (next()) {
			lCount = getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di Provvedimenti Depositati relativi ad un
	 * Fascicolo SIUS.
	 * </p>
	 *
	 * @param BigDecimal
	 *            aIdFasSius : Identificativo Fascicolo SIUS
	 * @return int : numero provvedimenti trovati.
	 * @throws DAOException
	 */
	public int getNumProvSIUSDEpositati(BigDecimal aIdFasSius) throws DAOException {
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from EVENTO  where FLAG_DOCUMENTO_REGISTRATO = 'S'  AND DATA_TRASMISSIONE_ATTI IS NOT NULL AND FAS_SIU_ID_FASCICOLO_SIUS = "
				+ aIdFasSius;
		setStatement(lStatement);

		start();
		if (next()) {
			lCount = getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di Provvedimenti Depositati relativi ad un
	 * Fascicolo SIUS, fatta eccezione di quelli di sospensione per rimessione atti e di rinvio udienza
	 * </p>
	 *
	 * @param BigDecimal
	 *            aIdFasSius : Identificativo Fascicolo SIUS
	 * @return int : numero provvedimenti trovati.
	 * @throws DAOException
	 */
	public int getNumProvSIUSDEpositatiNonDefinitori(BigDecimal aIdFasSius) throws DAOException {
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from EVENTO  where FLAG_DOCUMENTO_REGISTRATO = 'S'  AND DATA_TRASMISSIONE_ATTI IS NOT NULL AND COD_ESITO NOT IN ('0601', '0602', '0603', '0605') AND FAS_SIU_ID_FASCICOLO_SIUS = "
				+ aIdFasSius;
		setStatement(lStatement);

		start();
		if (next()) {
			lCount = getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * La funzione prepara la Select per cercare Eventi con coppie COD_MOTIVO e COD_TIPO_PROVVEDIMENTO passati
	 * attraverso due array di String. Gli eventi vengono restituiti ordinati per DATA_INSERIMENTO DESC
	 * Attenzione alla condizione sullo stato di validazione. Se non viene specificato nulla recupera anche
	 * gli eventi annullati in quanto non impone condizioni sul flag documento registrato Se
	 * aModel.getFlagDocumentoRegistrato()
	 *
	 * @param aModel
	 *            : Model Evento di ricerca
	 * @param aTipoProv
	 *            : String[] array di tipoProv
	 * @param aCodMotiv
	 *            : String[] array di aCodMotiv
	 * @throws DAOException
	 */
	public void ricercaEventoTipoProvTipoMot(EventoModel aModel, String[] aTipoProv, String[] aCodMotiv)
			throws DAOException {
		String lStatement = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}
		if (aModel.getCodMotivo() != null && aModel.getCodMotivo().trim().length() > 0) {
			lStatement += " AND COD_MOTIVO ='" + aModel.getCodMotivo() + "'";
		}

		if (aModel.getCodTipoEvento() != null && aModel.getCodTipoEvento().trim().length() > 0) {
			lStatement += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aModel.getCodTipoProvvedimento() != null
				&& aModel.getCodTipoProvvedimento().trim().length() > 0) {
			lStatement += " AND COD_TIPO_PROVVEDIMENTO ='" + aModel.getCodTipoProvvedimento() + "'";
		}

		if (aModel.getFlagDocumentoRegistrato() != null
				&& aModel.getFlagDocumentoRegistrato().compareTo("N") == 0)
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		else if (aModel.getFlagDocumentoRegistrato() != null
				&& aModel.getFlagDocumentoRegistrato().compareTo("S") == 0)
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'S')";
		else if (aModel.getFlagDocumentoRegistrato() != null
				&& aModel.getFlagDocumentoRegistrato().compareTo("NOANN") == 0)
			lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO IS NULL OR FLAG_DOCUMENTO_REGISTRATO  <> 'A')";

		// =====================
		if (aTipoProv != null && aTipoProv.length > 0 && aCodMotiv != null && aCodMotiv.length > 0
				&& aTipoProv.length == aCodMotiv.length) {
			lStatement += " AND ( ";
			for (int i = 0; i < aTipoProv.length; i++) {
				if (i > 0)
					lStatement += " OR ";
				lStatement += "(COD_TIPO_PROVVEDIMENTO = '" + aTipoProv[i] + "' AND COD_MOTIVO = '"
						+ aCodMotiv[i] + "' )";
			}
			lStatement += " )";
		}
		lStatement += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	// ricerca evento annotazioni manuali con 2 possibili Codici Tipo Provvedimento
	// Inserito per gestire sia annotazioni con il vecchio valore 04 che i nuovi 26
	public void ricercaEventoTipoMotProvEveDataEmis(EventoModel aModel, String aCodTipoProv)
			throws DAOException {
		String lStatement = getSqlQuery();

		String lDateStr = DateUtils.getDateToString(aModel.getDataEmissione(), "dd/MM/yyyy");

		lStatement += " AND COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "'";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO IN ( '" + aModel.getCodTipoProvvedimento() + "', '"
				+ aCodTipoProv + "') ";
		if (lDateStr != null) {
			lStatement += " AND DATA_EMISSIONE = TO_DATE('" + lDateStr + "','DD/MM/YYYY')";
		}
		if (aModel.getCodMotivo() != null) {
			lStatement += " AND COD_MOTIVO = '" + aModel.getCodMotivo() + "'";
		}
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventiNOTAnnullati(BigDecimal aKey, String[] aMotivo, String aTipoProv,
			String aTipoEve) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND COD_TIPO_PROVVEDIMENTO = '" + aTipoProv + "'";
		lStatement += " AND COD_TIPO_EVENTO = '" + aTipoEve + "'";

		if (aMotivo.length > 0) {
			lStatement += " AND EVENTO.COD_MOTIVO IN (";
			for (int i = 0; i < aMotivo.length; i++) {
				lStatement += "'" + aMotivo[i] + "'";
				if (aMotivo.length > 1 && i < aMotivo.length - 1)
					lStatement += ",";
			}

			lStatement += ")";
		}

		lStatement += setCondizioneEventiNonAnnullati();

		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	/**
	 * Effettua la ricerca degli ordini di esecuzione (validati) ai fini della richiesta di restituzione
	 *
	 * @param aIdFascicoloSiep
	 * @throws DAOException
	 */
	public void ricercaOrdiniEsecuzioneByIdFascicoloPerRestituzione(BigDecimal aIdFascicoloSiep)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
		lStatement += " AND (EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep + ")";
		// 17/04/2019 MEV70 Aggiunta recupero degli OE con Sospensione generati da Cumulo (RV_ALT2_VALUE like
		// 'LS%' ).
		// lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%' OR
		// CODMOV.RV_HIGH_VALUE LIKE 'LED%')";
		lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%' OR CODMOV.RV_HIGH_VALUE LIKE 'LED%' OR CODMOV.RV_ALT2_VALUE LIKE 'LS%' )";
		lStatement += " AND (FLAG_DOCUMENTO_REGISTRATO = 'S')";
		lStatement += " ORDER BY DATA_EMISSIONE DESC";

		setStatement(lStatement);
	}

	/**
	 * Restituisce la query senza impostare le join condition sulla CG_REF_CODES
	 *
	 * @return
	 * @throws DAOException
	 */
	protected String getSqlQueryNoJoin() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, '' COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, '' COD_PRO,";
		lStatement += " '' COD_ABBR,";
		lStatement += " COD_MOTIVO, '' COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, '' DESC_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, '' LUO_EMI, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " '' COD_TIPO_UFFICIO_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, '' COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, '' LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " '' DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  "; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP, ";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA, "; // STUB 09/02/2006
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST, ";
		lStatement += " EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO, ";
		lStatement += " EVENTO.ESTREMI_SOGG_RICH_ISTR, ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " KEY_ESEC_NSC ";
		lStatement += " FROM EVENTO";
		lStatement += " WHERE ";

		return lStatement;
	}

	protected String getSqlQueryByIdFascicoloSige() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO,";
		lStatement += " COD_TIPO_EVENTO, '' COD_EVE,";
		lStatement += " EV.COD_TIPO_PROVVEDIMENTO AS  COD_TIPO_PROVVEDIMENTO, '' COD_PRO,";
		lStatement += " '' COD_ABBR,";
		lStatement += " COD_MOTIVO, '' COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, '' DESC_UFF_EMITTENTE,";
		lStatement += " COD_LUOGO_EMITTENTE, '' LUO_EMI,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, COGNOME_SOGGETTO_PRESENTANTE,";
		lStatement += " EV.DATA_EMISSIONE AS DATA_EMISSIONE,";
		lStatement += " COD_ESITO, '' COD_ESI,";
		lStatement += " FLAG_PIU_MENO,";
		lStatement += " DATA_TRASMISSIONE_ATTI,";
		lStatement += " DATA_RICEZIONE_ATTI,";
		lStatement += " EV.COD_UFFICIO_DESTINATARIO as COD_UFFICIO_DESTINATARIO,";
		lStatement += " COD_LUOGO_DESTINATARIO, '' LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, PROGR_PROTOCOLLO,";
		lStatement += " DOC_BLOB,";
		lStatement += " EV.COD_OPERATORE_INSERIMENTO as COD_OPERATORE_INSERIMENTO, EV.DATA_INSERIMENTO as DATA_INSERIMENTO, EV.COD_UFFICIO_INSERIMENTO as COD_UFFICIO_INSERIMENTO,";
		lStatement += " EV.COD_OPERATORE_AGGIORNAMENTO as COD_OPERATORE_AGGIORNAMENTO, EV.DATA_AGGIORNAMENTO as DATA_AGGIORNAMENTO, EV.COD_UFFICIO_AGGIORNAMENTO as COD_UFFICIO_AGGIORNAMENTO,";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP,";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS,";
		lStatement += " '' DESC_UFF_DESTINATARIO,";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO,";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO,";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,";
		lStatement += " TEM_ID_TEMPLATE,"; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP,";
		lStatement += " FLAG_STAMPA_SIUS,";
		lStatement += " FLAG_VIDEO_SIEP,";
		lStatement += " FLAG_VIDEO_SIUS,";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP,";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA,"; // STUB 09/02/2006
		lStatement += " EVE_ID_EVENTO,";
		lStatement += " EVE_ID_EVENTO_REVOCA,";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE,";
		lStatement += " PEN_ID_PENA_RESIDUA,";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST,";
		lStatement += " DATA_RICHIESTA,";
		lStatement += " KEY_ESEC_NSC,";
		lStatement += " TIPOLOGIA_INVIO_ATTI,";
		lStatement += " DESCRIZIONE_INVIO_ATTI,";
		lStatement += " DATA_INVIO_ATTI,";
		lStatement += " CRC.RV_MEANING as DESCR_TIPOLOGIA_INVIO_ATTI,";
		// MERGE v10 --> MEV10-s3: aggiunto campo in estrazione
		lStatement += " '' COD_TIPO_UFFICIO_EMITTENTE";
		lStatement += " FROM EVENTO EV, PROVVEDIMENTO_SIGE PROVV, CG_REF_CODES CRC";
		lStatement += " WHERE";

		return lStatement;
	}

	/**
	 * Effettua la ricerca di tutti gli eventi presenti su un certo fascicolo ordinati per <b>Data
	 * Inserimento</b> decrescente, dal piu' recente al piu' vecchio. Se viene specificato anche il parametro
	 * aDateAl, vengono recuperati solo gli eventi antecedenti la data specificata(<=). Restituisce anche gli
	 * eventi annullati o non validati. Non va in join con la CG_REF_CODES per cui non sono disponibili le
	 * descrizioni dei codici.
	 *
	 * @param aIdFascicolo
	 * @param aDateAl
	 *            data massima esclusa
	 * @throws DAOException
	 */
	public void ricercaEventoByIdFascicoloSiepDataInsDesc(BigDecimal aIdFascicolo, Date aDataAl)
			throws DAOException {
		String lStatement = getSqlQueryNoJoin();

		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		if (aDataAl != null) {
			// n.b. il segno = serve per recuperare anche l'evento di riferimento perche'
			// se e' un evento di pena iniziale deve essere restituito
			lStatement += " AND DATA_INSERIMENTO <= to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}

		lStatement += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	public void ricercaEventoPerMotivoProvv(String[] aMotivo, String[] aTipoProvv, EventoModel aModel)
			throws DAOException {
		String lCondizioni = getSqlQueryDecretoOrdinanzaUfficio();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}

		if (aModel.getCodTipoEvento() != null) {
			if (aModel.getCodTipoEvento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aTipoProvv.length > 0) {
			lCondizioni += " AND COD_TIPO_PROVVEDIMENTO IN (";
			for (int i = 0; i < aTipoProvv.length; i++) {
				lCondizioni += "'" + aTipoProvv[i] + "'";
				if (aTipoProvv.length > 1 && i < aTipoProvv.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		if (aMotivo.length > 0) {
			lCondizioni += " AND COD_MOTIVO IN (";
			for (int i = 0; i < aMotivo.length; i++) {
				lCondizioni += "'" + aMotivo[i] + "'";
				if (aMotivo.length > 1 && i < aMotivo.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		if (aModel.getFlagDocumentoRegistrato() != null) {
			if (aModel.getFlagDocumentoRegistrato().compareTo("") != 0)
				lCondizioni += " AND FLAG_DOCUMENTO_REGISTRATO ='" + aModel.getFlagDocumentoRegistrato()
						+ "'";
		}

		lCondizioni += " ORDER BY DATA_INSERIMENTO DESC ";
		setStatement(lCondizioni);
	}

	public void ricercaEventoVerbaleByIdEve(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQueryDesignaIstituto();
		lStatement += " AND ID_EVENTO = " + aKey;

		setStatement(lStatement);
	}

	protected String getSqlQueryDesignaIstituto() throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT ID_EVENTO, COD_TIPO_EVENTO, CODEVE.RV_MEANING TIPO_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING TIPO_PRO,";
		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING MOTIVO_PRO,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMI, ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE DESC_LUO_EMI, EVENTO.FAS_SIE_ID_FASCICOLO_SIEP,";
		lStatement += " EVENTO.DATA_EMISSIONE, FLAG_DOCUMENTO_REGISTRATO, FLAG_STAMPA_SIEP, FLAG_VIDEO_SIEP,";

		lStatement += " ID_VERBALE, VER.DATA_EMISSIONE DATA_DESIGNAZIONE, DATA_PERVENIMENTO,";
		lStatement += " COD_TIPO_UFFICIO_FIRMATARIO, COD_UFF_FIR.RV_MEANING AUTORITA,";
		lStatement += " COD_LUOGO_UFFICIO_FIRMATARIO, COD_LUO_FIR.DESCRIZIONE ,";
		lStatement += " EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO, ";
		lStatement += " VER.IST_DET_ID_ISTITUTO_DETENZIONE, NUMERO_PROTOCOLLO";

		lStatement += " FROM EVENTO, VERBALE VER, UFFICIO UFF_EMI, CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, CG_REF_CODES CODMOV,";
		lStatement += " CG_REF_CODES UFF_TIPO_EMI, CG_REF_CODES COD_VER, CG_REF_CODES COD_UFF_FIR, COMUNE LUOEMI, COMUNE COD_LUO_FIR";

		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE = LUOEMI.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";

		lStatement += " AND COD_VER.RV_DOMAIN = 'TIPO_VERBALE' AND COD_VER.RV_LOW_VALUE = COD_TIPO_VERBALE";
		lStatement += " AND COD_UFF_FIR.RV_DOMAIN = 'TIPO_AUTORITA' AND COD_UFF_FIR.RV_LOW_VALUE = COD_TIPO_UFFICIO_FIRMATARIO";
		lStatement += " AND COD_LUO_FIR.COD_COMUNE = COD_LUOGO_UFFICIO_FIRMATARIO";
		lStatement += " AND ID_EVENTO = VER.EVE_ID_EVENTO";

		return lStatement;
	}

	public EventoVerbaleModel getModelIstituto() throws DAOException {
		EventoModel lEveMod = new EventoModel();
		lEveMod.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEveMod.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		lEveMod.setDescrTipoEvento(getString("TIPO_EVE"));
		lEveMod.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEveMod.setDescrTipoProvvedimento(getString("TIPO_PRO"));
		lEveMod.setCodMotivo(getString("COD_MOTIVO"));
		lEveMod.setDescrMotivo(getString("MOTIVO_PRO"));
		lEveMod.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		// lEveMod.setCodTipoUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		lEveMod.setDescrUfficioEmittente(getString("DESC_UFF_EMI"));
		lEveMod.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lEveMod.setDescrLuogoEmittente(getString("DESC_LUO_EMI"));
		lEveMod.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		lEveMod.setDataEmissione(getDate("DATA_EMISSIONE")); // Data Emissione
		lEveMod.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		lEveMod.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		lEveMod.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));

		VerbaleModel lVerMod = new VerbaleModel();
		lVerMod.setIdVerbale(getBigDecimal("ID_VERBALE"));
		lVerMod.setCodLuogoUfficioFirmatario(getString("COD_TIPO_UFFICIO_FIRMATARIO"));
		lVerMod.setCodTipoUfficioFirmatario(getString("COD_LUOGO_UFFICIO_FIRMATARIO"));
		lVerMod.setDataEmissione(getDate("DATA_DESIGNAZIONE")); // Data Designazione
		lVerMod.setDataPervenimento(getDate("DATA_PERVENIMENTO")); // Data pervenimento Richiesta
		lVerMod.setDescrLuogoUfficioFirmatario(getString("DESCRIZIONE"));
		lVerMod.setDescrTipoUfficioFirmatario(getString("AUTORITA"));
		lVerMod.setNumeroProtocollo(getString("NUMERO_PROTOCOLLO"));
		lVerMod.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));

		EventoVerbaleModel lMod = new EventoVerbaleModel();
		lMod.setEvento(lEveMod);
		lMod.setVerbale(lVerMod);

		return lMod;
	}

	/*****************************************************************************
	 * Recupera la data emissione del Certificato Casellario Giudiziario
	 ****************************************************************************/
	public Date getDataEmissioneCertCasellario(BigDecimal aFascKey, String aChiaveUfficio, String aTipoEvento,
			String aCodMotivo) throws DAOException {
		Date dataEmissione = null;
		String lStatement = new String("");

		lStatement += " SELECT ";
		lStatement += " MAX(DATA_EMISSIONE) DATA_EMISSIONE ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') OR";
		lStatement += " (EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aFascKey;
		if (aChiaveUfficio != null)
			lStatement += " AND COD_UFFICIO_EMITTENTE = '" + aChiaveUfficio + "' ";
		if (aTipoEvento != null)
			lStatement += " AND COD_TIPO_EVENTO = '" + aTipoEvento + "' ";
		if (aCodMotivo != null)
			lStatement += " AND COD_MOTIVO = '" + aCodMotivo + "' ";

		setStatement(lStatement);

		start();

		if (next() && (getDate("DATA_EMISSIONE") != null))
			dataEmissione = getDate("DATA_EMISSIONE");

		stop();

		return dataEmissione;

	}

	public void ricercaDataInvioAtti(BigDecimal idFascicoloSige) throws DAOException {
		String lStatement = getSqlQueryByIdFascicoloSige();
		lStatement += " EV.ID_EVENTO=PROVV.ID_EVENTO_GENERATO AND ";
		lStatement += " PROVV.COD_TIPO_PROVVEDIMENTO='25' AND ";
		lStatement += " PROVV.COD_TIPO_PROVVEDIMENTO_SIGE='63' AND ";
		lStatement += " PROVV.FAS_ID_FASCICOLO_SIGE=" + idFascicoloSige.toString() + " AND ";
		lStatement += " CRC.RV_DOMAIN='ATTI_ARCHIVIO' AND ";
		lStatement += " CRC.RV_LOW_VALUE=EV.TIPOLOGIA_INVIO_ATTI ";

		super.setStatement(lStatement);

	}

	private boolean findColumn(String aValue) {
		try {
			mRs.findColumn(aValue);
		} catch (Exception sqex) {
			return false;
		}
		return true;
	}

	/**
	 * MEV 16: aggiunto metodo di estrazione info esistenza FC
	 *
	 * @param idEvento
	 * @throws DAOException
	 */
	public boolean ricercaEsistenzaFoglioComplementare(BigDecimal idEvento) throws DAOException {

		boolean exist = false;
		String lStatement = new String();
		lStatement += " SELECT KEY_ESEC_NSC";
		lStatement += " FROM EVENTO";
		lStatement += " WHERE ID_EVENTO = " + idEvento;
		setStatement(lStatement);
		start();
		if (next() && (getBigDecimal("KEY_ESEC_NSC") != null))
			exist = true;
		stop();
		return exist;
	}

	// MEV 26 CUMULO
	public void ricercaEventoByTipoEveKeyIstruttoriaCumulo(BigDecimal aKey, String[] aTipoEvento)
			throws DAOException {
		// String lStatement = getSqlQueryIstruttoriaCumulo();
		String lStatement = getSqlQuery();

		lStatement += " AND ISTR_ID_ISTRUTTORIA_CUMULO = " + aKey;

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += " AND (COD_TIPO_EVENTO IN (";
				for (int i = 0; i < aTipoEvento.length; i++) {
					lStatement += "'" + aTipoEvento[i] + "'";
					if (aTipoEvento.length > 1 && i < aTipoEvento.length - 1)
						lStatement += ",";
				}
				lStatement += ")";
				// Richiesta 'Notizie Espulsione Sanzione Sostitutiva'
				lStatement += " OR (COD_TIPO_EVENTO = '02' AND COD_TIPO_PROVVEDIMENTO = '26' AND COD_MOTIVO IN ('0565','0566') ) )";
			}
		}

		lStatement += setOrderEventoAsc();
		setStatement(lStatement);
	}

	protected String getSqlQueryIstruttoriaCumulo() throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " EVE_ID_EVENTO,  ";
		lStatement += " DATA_RICHIESTA, ";
		lStatement += " ISTR_ID_ISTRUTTORIA_CUMULO ";

		lStatement += " FROM EVENTO, cg_ref_codes CODMOV,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		// STUB 07/10/2005 lStatement +=
		// " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR
		// CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND";
		// //****
		lStatement += " ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') OR";
		lStatement += " (EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'))";

		return lStatement;
	}

	/**
	 * MEV 16 CUMULO: aggiunto metodo di controllo
	 *
	 * @param codMotivo
	 * @param idEvento
	 * @return
	 * @throws DAOException
	 */
	public boolean isCumulo(String codMotivo, BigDecimal idEvento) throws DAOException {

		boolean isCumulo = false;
		int count = 0;
		String lStatement = new String();

		lStatement += "select count(*) as count" + "  from cg_ref_codes c, evento e, istruttoria_cumulo i"
				+ " where c.rv_domain = 'MOTIVO_PROVVEDIMENTO'" + "   and c.rv_high_value = 'CUMULO_NEW'"
				+ "   and c.rv_low_value = '" + codMotivo + "'" + "   and i.eve_id_evento_prov = e.id_evento"
				+ "   and e.id_evento = '" + idEvento + "'" + "   and i.data_chiusura is not null"
				+ "   and i.flag_stato = 'C'";

		setStatement(lStatement);
		start();
		if (next())
			count = getInt("COUNT");
		stop();
		if (count > 0)
			isCumulo = true;
		return isCumulo;
	}

	// Modifica del 28/11/2016 MEV_15_S4
	// La modifica si è resa necessaria per integrare la funzionalità
	// alla "Nuova Gestione del Cumulo" introdotta con la MEV_26
	// Vengono estratti dalla tabella Evento, tutti gli eventi legati al Fascicolo Siep,
	// che hanno COD_MOTIVO legati al cumulo
	public void ricercaEventoCumulo(BigDecimal aIdFascicoloSiep) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicoloSiep;

		lStatement += " AND COD_MOTIVO IN ('0277','0222','0223','0224','0225','0630','0635','0636','0637','0638','0639','0640','0641','0642','0643','0644','0645','0646','0647','0648','0649','0650','0651','0652','0661')";

		lStatement += " ORDER BY DATA_INSERIMENTO ASC ";

		// lStatement += " " + setCondizioneEventoCumulo(aIdFascicoloSiep);

		setStatement(lStatement);
	}

	public String setCondizioneEventoCumulo(BigDecimal aIdFascicoloSiep) {
		String lCondizioni = new String();

		lCondizioni = " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicoloSiep;

		lCondizioni += " AND COD_MOTIVO IN ('0277','0222','0223','0224','0225','0630','0635','0636','0637','0638','0639','0640','0641','0642','0643','0644','0645','0646','0647','0648','0649','0650','0651','0652','0661')";

		lCondizioni += " ORDER BY DATA_INSERIMENTO ASC ";

		return lCondizioni;
	}

} // Chiude DAO
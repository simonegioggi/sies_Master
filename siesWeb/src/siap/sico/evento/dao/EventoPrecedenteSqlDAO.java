package siap.sico.evento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoPrecedenteModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

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
public class EventoPrecedenteSqlDAO extends SIAPSqlDAO {

	public EventoPrecedenteSqlDAO(Connection con) {
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

	public void ricercaOrdineEsecuzioneSoloEventiVisualizzazioneByIdFascicolo(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		lStatement += setOrderEventoAsc();

		setStatement(lStatement);
	}

	public void ricercaEventoByIdFascicoloSiep(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		lStatement += setOrder();

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

	// STUB 24/10/2005 REWORK STATO ESECUZIONE
	public void ricercaEventoPerProvvedimenti(String[] aProvvedimenti, EventoModel aModel)
			throws DAOException {
		String lCondizioni = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}

		if (aModel.getCodTipoEvento() != null) {
			if (aModel.getCodTipoEvento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_EVENTO ='" + aModel.getCodTipoEvento() + "'";
		}

		if (aModel.getCodMotivo() != null) {
			if (aModel.getCodMotivo().compareTo("") != 0)
				lCondizioni += " AND COD_MOTIVO ='" + aModel.getCodMotivo() + "'";
		}

		if (aProvvedimenti.length > 0) {
			lCondizioni += " AND COD_TIPO_PROVVEDIMENTO IN (";
			for (int i = 0; i < aProvvedimenti.length; i++) {
				lCondizioni += "'" + aProvvedimenti[i] + "'";
				if (aProvvedimenti.length > 1 && i < aProvvedimenti.length - 1)
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

	public void ricercaEventoNonRegistratoByKey(BigDecimal aIdEvento) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND ID_EVENTO =" + aIdEvento;
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";

		setStatement(lStatement);
	}

	public GenericModel getModel() throws DAOException {
		EventoPrecedenteModel aModel = new EventoPrecedenteModel();

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
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
		aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));
		aModel.setDecIdDecretoOrdinanzaSiep(getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP"));
		aModel.setPenAccIdPenaAccessoria(getBigDecimal("PEN_ACC_ID_PENA_ACCESSORIA")); // STUB 09/02/2006
		aModel.setDataEspulsioneSanzSost(getDate("DATA_ESPULSIONE_SANZ_SOST"));
		aModel.setLegge(getString("COD_ABBR"));

		return aModel;
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
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
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
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA, "; // STUB 13/02/2006
		lStatement += " EVE_ID_EVENTO, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND"; // ****
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

		return lStatement;
	}

	/*
	 * public long getSizeBlob() { BLOB lBlob = this.getDocBlob("DOC_BLOB"); }
	 */
	private String setOrderEventoDesc() {
		String lCondizioni = " ORDER BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC";

		return lCondizioni;
	}

	private String setOrderEventoAsc() {
		String lCondizioni = " ORDER BY DATA_EMISSIONE, DATA_INSERIMENTO, ID_EVENTO ";

		return lCondizioni;
	}

	public String setCondizioni(EventoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}
		if (aModel.getCodMotivo() != null) {
			if (aModel.getCodMotivo().compareTo("") != 0)
				lCondizioni += " AND COD_MOTIVO ='" + aModel.getCodMotivo() + "'";
		}

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

	private String setOrder() {
		String lCondizioni = new String(" ORDER BY DATA_EMISSIONE DESC ");

		return lCondizioni;
	}

	// private String setCondizioneEventiNonAnnullati() {
	// String lCondizioni = "AND (FLAG_DOCUMENTO_REGISTRATO IS  NULL OR FLAG_DOCUMENTO_REGISTRATO  <> 'A') ";
	// return lCondizioni;
	// }

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
		this.start();

		BigDecimal lBigDec = new BigDecimal(0);

		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lBigDec = this.getBigDecimal("aMAX");

		this.stop();

		if (lBigDec == null)
			lBigDec = new BigDecimal(0);

		return lBigDec;
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

	public int getNumDocumentiAllegati(BigDecimal aIdEve) throws DAOException {
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from DOCUMENTO_ALLEGATO  where EVE_ID_EVENTO = "
				+ aIdEve;
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

}
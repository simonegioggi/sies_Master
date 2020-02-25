package siap.siep.istanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel;

/**
 * <p>
 * Title: IstanzaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Istanza
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

public class IstanzaSqlDAO extends SIAPSqlDAO {
	public IstanzaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaIstanza(IstanzaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	// METODO RICERCA DEL SOGGETTO()
	public void ricercaIstanzaSoggetto(SoggettoModel aSogMod) throws DAOException {
		String lSql = getSqlQuerySoggetto();

		lSql += " " + setCondizioneSoggetto(aSogMod);
		lSql += " " + "ORDER BY COGNOME, NOME ";

		setStatement(lSql);
	}

	// METODO RICERCA DEL SOGGETTO()----Paginazione
	public void ricercaIstanzaSoggettoPaged(SoggettoModel aSogMod, int aPage) throws DAOException {
		String lSql = getSqlQuerySoggettoPaged();
		String lPaginedStatement = new String("");

		lSql += " " + setCondizioneSoggetto(aSogMod);
		lSql += " " + "ORDER BY COGNOME, NOME ";

		lPaginedStatement = " SELECT * FROM (SELECT INNER.ID_SOGGETTO, INNER.COGNOME,INNER.NOME,INNER.DATA_NASCITA,INNER.COD_COMUNE_NASC,"
				+ " INNER.COMUNE,INNER.COD_PROVINCIA_NASC,INNER.PROVINCIA,INNER.COD_STATO_NASCITA,"
				+ " INNER.DESC_COMUNE_NASCITA_ESTERO,INNER.STATO,INNER.ID_ISTANZA,INNER.COD_MOTIVO,INNER.DESCR_OGGETTO,"
				+ " INNER.ID_EVENTO,INNER.CHIAVE_AN,INNER.CHIAVE_PR,INNER.COD_STATO_ISTANZA,"
				+ " Rownum rn FROM (" + lSql + "  ) INNER ) WHERE rn between  "
				+ ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND "
				+ (aPage) * IWebConstants.RESULT_PER_PAGE;
		setStatement(lPaginedStatement);
	}

	public void getCountIstanzaSoggettoPaged(SoggettoModel aSogMod) throws DAOException

	{
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM (" + getSqlQuerySoggetto()
				+ setCondizioneSoggetto(aSogMod) + "ORDER BY COGNOME, NOME " + ")";
		setStatement(lStatement);
	}

	public void ricercaIstanzaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void ricercaIstanzaOggetto(IstanzaModel aIstMod) throws DAOException {
		String lSql = getSqlQuerySoggetto();

		lSql += " " + setCondizioneOggetto(aIstMod);
		lSql += " " + "ORDER BY COGNOME, NOME ";

		setStatement(lSql);
	}

	public void ricercaIstanzaOggettoPaged(IstanzaModel aIstMod, int aPage) throws DAOException {
		String lSql = getSqlQuerySoggettoPaged();
		String lPaginedStatement = new String("");

		lSql += " " + setCondizioneOggetto(aIstMod);
		lSql += " " + "ORDER BY COGNOME, NOME ";

		lPaginedStatement = " SELECT * FROM (SELECT INNER.ID_SOGGETTO, INNER.COGNOME,INNER.NOME,INNER.DATA_NASCITA,INNER.COD_COMUNE_NASC,"
				+ " INNER.COMUNE,INNER.COD_PROVINCIA_NASC,INNER.PROVINCIA,INNER.COD_STATO_NASCITA,"
				+ " INNER.DESC_COMUNE_NASCITA_ESTERO,INNER.STATO,INNER.ID_ISTANZA,INNER.COD_MOTIVO,INNER.DESCR_OGGETTO,"
				+ " INNER.ID_EVENTO,INNER.CHIAVE_AN,INNER.CHIAVE_PR,INNER.COD_STATO_ISTANZA,"
				+ " Rownum rn FROM (" + lSql + "  ) INNER ) WHERE rn between  "
				+ ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND "
				+ (aPage) * IWebConstants.RESULT_PER_PAGE;
		setStatement(lPaginedStatement);
	}

	public void getCountIstanzaOggettoPaged(IstanzaModel aIstMod) throws DAOException

	{
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM (" + getSqlQuerySoggetto()
				+ setCondizioneOggetto(aIstMod) + "ORDER BY COGNOME, NOME " + ")";
		setStatement(lStatement);
	}

	public void ricercaIstanzeByFascicolo(BigDecimal aIdFascicoloSiep) throws DAOException {
		String lStatement = getSqlQueryIstanzePerFascicolo();

		lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '03')";
		lStatement += " AND (EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep + ")";
		lStatement += " ORDER BY DATA_EMISSIONE DESC";

		setStatement(lStatement);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + "ID_ISTANZA, "
				+ "COD_MOTIVO, OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO, " + "NOTE, "
				+ "COGNOME_SOGGETTO_PRESENTANTE, " + "NOME_SOGGETTO_PRESENTANTE, " + "DATA_PRESENTAZIONE, "
				+ "COD_ESITO, ESITO_PROVV.RV_MEANING DESCR_ESITO, " + "ANNO_REGISTRO, " + "PROGR_REGISTRO, "
				+ "COD_TIPO_UFFICIO_DESTINATARIO, UFFICIO_DESTINATARIO.RV_MEANING DESCR_TIPO_UFF_DEST, "
				+ "COD_LUOGO_DESTINATARIO, LUOGO_DESTINATARIO.DESCRIZIONE DESCR_LUOGO_DESTINATARIO, "
				+ "COD_UFFICIO_DESTINATARIO, " + // DESCR
				"COGNOME_AVVOCATO, " + "NOME_AVVOCATO, " + "FORO_COMPETENZA, " + "ANNO_SENTENZA, "
				+ "NUMERO_SENTENZA, " + "DATA_SENTENZA, " + "DATA_IRREVOCABILITA, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, AUT_EMI.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, LUOGO_EMI.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "
				+ "COD_STATO_ISTANZA, STATO_ISTANZA.RV_MEANING DESCR_STATO_ISTANZA, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "SOG_ID_SOGGETTO, " + "EVE_ID_EVENTO, " + "CAM_ID_CAMPO_NOTE ";
		lStatement += "FROM ISTANZA, CG_REF_CODES AUT_EMI, COMUNE LUOGO_EMI, ";
		lStatement += "CG_REF_CODES STATO_ISTANZA, CG_REF_CODES OGGETTO_PROCEDIMENTO, ";
		lStatement += "CG_REF_CODES ESITO_PROVV, CG_REF_CODES UFFICIO_DESTINATARIO, COMUNE LUOGO_DESTINATARIO ";
		lStatement += "WHERE AUT_EMI.RV_DOMAIN = 'TIPO_UFFICIO' AND AUT_EMI.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE ";
		lStatement += "AND LUOGO_EMI.COD_COMUNE = COD_LUOGO_EMITTENTE ";
		lStatement += "AND STATO_ISTANZA.RV_DOMAIN = 'STATO_ISTANZA' AND STATO_ISTANZA.RV_LOW_VALUE = COD_STATO_ISTANZA ";
		lStatement += "AND OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND OGGETTO_PROCEDIMENTO.RV_LOW_VALUE = COD_MOTIVO ";
		lStatement += "AND ESITO_PROVV.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND ESITO_PROVV.RV_LOW_VALUE = COD_ESITO AND ESITO_PROVV.RV_HIGH_VALUE='I' ";
		lStatement += "AND UFFICIO_DESTINATARIO.RV_DOMAIN = 'TIPO_UFFICIO' AND UFFICIO_DESTINATARIO.RV_LOW_VALUE = COD_TIPO_UFFICIO_DESTINATARIO ";
		lStatement += "AND LUOGO_DESTINATARIO.COD_COMUNE = COD_LUOGO_DESTINATARIO ";

		return lStatement;
	}

	protected String getSqlQueryIstanzePerFascicolo() {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		lStatement += " EVENTO.COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " EVENTO.COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		lStatement += " EVENTO.NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " EVENTO.COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " EVENTO.COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " EVENTO.COD_UFFICIO_DESTINATARIO, ";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " EVENTO.COD_OPERATORE_INSERIMENTO, ";
		lStatement += " EVENTO.DATA_INSERIMENTO,";
		lStatement += " EVENTO.COD_UFFICIO_INSERIMENTO,";
		lStatement += " EVENTO.COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " EVENTO.DATA_AGGIORNAMENTO,";
		lStatement += " EVENTO.COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, EVENTO.COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE, "; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP, ";
		lStatement += " FLAG_STAMPA_SIUS, ";
		lStatement += " FLAG_VIDEO_SIEP, ";
		lStatement += " FLAG_VIDEO_SIUS, ";
		lStatement += " ID_ISTANZA, COD_STATO_ISTANZA ";
		lStatement += " FROM EVENTO, ISTANZA IST, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE EVENTO.ID_EVENTO = IST.EVE_ID_EVENTO";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

		return lStatement;
	}

	protected String getSqlQuerySoggetto() {
		String lStatement = new String("");

		lStatement += " SELECT ID_ISTANZA, ID_SOGGETTO, ";
		lStatement += " S.COGNOME, S.NOME, S.DATA_NASCITA, S.COD_COMUNE_NASCITA, ";
		lStatement += " S.COD_PROVINCIA_NASCITA, I.COD_MOTIVO, F.CHIAVE_ANNO, F.CHIAVE_PROGR, ";
		lStatement += " S.COD_COMUNE_NASCITA, ";
		lStatement += " COM.DESCRIZIONE COMUNE, ";
		lStatement += " S.COD_PROVINCIA_NASCITA, ";
		lStatement += " PRO.RV_MEANING PROVINCIA, OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO, ";
		lStatement += " DESC_COMUNE_NASCITA_ESTERO, COD_STATO_NASCITA, NAZ.RV_MEANING STATO, ";
		lStatement += " E.ID_EVENTO, F.CHIAVE_ANNO, F.CHIAVE_PROGR ";

		lStatement += " FROM SOGGETTO S, ISTANZA I, EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES NAZ, ";
		lStatement += " CG_REF_CODES PRO, COMUNE COM, CG_REF_CODES OGGETTO_PROCEDIMENTO ";

		lStatement += " WHERE S.ID_SOGGETTO = I.SOG_ID_SOGGETTO ";
		lStatement += " AND I.EVE_ID_EVENTO = E.ID_EVENTO(+) ";
		lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = F.ID_FASCICOLO_SIEP(+) ";
		lStatement += " AND PRO.RV_LOW_VALUE = S.COD_PROVINCIA_NASCITA ";
		lStatement += " AND PRO.RV_DOMAIN = 'PROVINCIA' AND S.COD_COMUNE_NASCITA = COM.COD_COMUNE ";
		lStatement += " AND NAZ.RV_LOW_VALUE = COD_STATO_NASCITA AND NAZ.RV_DOMAIN = 'NAZIONE' ";
		lStatement += " AND OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND OGGETTO_PROCEDIMENTO.RV_LOW_VALUE = I.COD_MOTIVO ";
		lStatement += " AND I.COD_STATO_ISTANZA <>'A'";

		return lStatement;
	}

	protected String getSqlQuerySoggettoPaged() {
		String lStatement = new String("");

		lStatement += " SELECT ID_ISTANZA, ID_SOGGETTO, ";
		lStatement += " S.COGNOME, S.NOME, S.DATA_NASCITA, S.COD_COMUNE_NASCITA, ";
		lStatement += " S.COD_PROVINCIA_NASCITA, I.COD_MOTIVO, ";
		lStatement += " S.COD_COMUNE_NASCITA COD_COMUNE_NASC, ";
		lStatement += " COM.DESCRIZIONE COMUNE, ";
		lStatement += " S.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASC, ";
		lStatement += " PRO.RV_MEANING PROVINCIA, OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO, ";
		lStatement += " DESC_COMUNE_NASCITA_ESTERO, COD_STATO_NASCITA, NAZ.RV_MEANING STATO, ";
		lStatement += " E.ID_EVENTO, F.CHIAVE_ANNO CHIAVE_AN, F.CHIAVE_PROGR CHIAVE_PR, COD_STATO_ISTANZA ";

		lStatement += " FROM SOGGETTO S, ISTANZA I, EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES NAZ, ";
		lStatement += " CG_REF_CODES PRO, COMUNE COM, CG_REF_CODES OGGETTO_PROCEDIMENTO ";

		lStatement += " WHERE S.ID_SOGGETTO = I.SOG_ID_SOGGETTO ";
		lStatement += " AND I.EVE_ID_EVENTO = E.ID_EVENTO(+) ";
		lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = F.ID_FASCICOLO_SIEP(+) ";
		lStatement += " AND PRO.RV_LOW_VALUE = S.COD_PROVINCIA_NASCITA ";
		lStatement += " AND PRO.RV_DOMAIN = 'PROVINCIA' AND S.COD_COMUNE_NASCITA = COM.COD_COMUNE ";
		lStatement += " AND NAZ.RV_LOW_VALUE = COD_STATO_NASCITA AND NAZ.RV_DOMAIN = 'NAZIONE' ";
		lStatement += " AND OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND OGGETTO_PROCEDIMENTO.RV_LOW_VALUE = I.COD_MOTIVO ";
		lStatement += " AND I.COD_STATO_ISTANZA <>'A'";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		IstanzaModel aModel = new IstanzaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdIstanza(getBigDecimal("ID_ISTANZA"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("DESCR_OGGETTO"));
		aModel.setNote(getString("NOTE"));
		aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
		aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
		aModel.setDataPresentazione(getDate("DATA_PRESENTAZIONE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("DESCR_ESITO"));
		aModel.setAnnoRegistro(getBigDecimal("ANNO_REGISTRO"));
		aModel.setProgrRegistro(getBigDecimal("PROGR_REGISTRO"));
		aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
		aModel.setDescrTipoUfficioDestinatario(getString("DESCR_TIPO_UFF_DEST"));
		aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setDescrLuogoDestinatario(getString("DESCR_LUOGO_DESTINATARIO"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		// aModel.setDescrUfficioDestinatario(getString("") ); // DESCR
		aModel.setCognomeAvvocato(getString("COGNOME_AVVOCATO"));
		aModel.setNomeAvvocato(getString("NOME_AVVOCATO"));
		aModel.setForoCompetenza(getString("FORO_COMPETENZA"));
		aModel.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		aModel.setNumeroSentenza(getString("NUMERO_SENTENZA"));
		aModel.setDataSentenza(getDate("DATA_SENTENZA"));
		aModel.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		aModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		aModel.setCodStatoIstanza(getString("COD_STATO_ISTANZA"));
		aModel.setDescrStatoIstanza(getString("DESCR_STATO_ISTANZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setCamIdCampoNote(getBigDecimal("CAM_ID_CAMPO_NOTE"));

		return aModel;
	}

	public GenericModel getModelIstSogEveFasc() throws DAOException {
		IstanzaSoggettoEventoFascicoloSiepModel lMod = new IstanzaSoggettoEventoFascicoloSiepModel();

		SoggettoModel lSoggetto = new SoggettoModel();
		IstanzaModel lIstanza = new IstanzaModel();
		EventoModel lEvento = new EventoModel();
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("COMUNE"));

		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		lSoggetto.setDescrProvinciaNascita(getString("PROVINCIA"));
		lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		lSoggetto.setDescrStatoNascita(getString("STATO"));

		// Oggetto Istanza
		lIstanza.setIdIstanza(getBigDecimal("ID_ISTANZA"));
		lIstanza.setCodMotivo(getString("COD_MOTIVO"));
		lIstanza.setDescrMotivo(getString("DESCR_OGGETTO"));

		// N.Siep
		lEvento.setIdEvento(getBigDecimal("ID_EVENTO"));

		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));

		lMod.setSoggetto(lSoggetto);
		lMod.setIstanza(lIstanza);
		lMod.setEvento(lEvento);
		lMod.setFascicoloSiep(lFascicolo);

		return lMod;
	}

	public GenericModel getModelIstSogEveFascForPaged() throws DAOException {
		IstanzaSoggettoEventoFascicoloSiepModel lMod = new IstanzaSoggettoEventoFascicoloSiepModel();

		SoggettoModel lSoggetto = new SoggettoModel();
		IstanzaModel lIstanza = new IstanzaModel();
		EventoModel lEvento = new EventoModel();
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASC"));
		lSoggetto.setDescrComuneNascita(getString("COMUNE"));

		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASC"));
		lSoggetto.setDescrProvinciaNascita(getString("PROVINCIA"));
		lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		lSoggetto.setDescrStatoNascita(getString("STATO"));

		// Oggetto Istanza
		lIstanza.setIdIstanza(getBigDecimal("ID_ISTANZA"));
		lIstanza.setCodMotivo(getString("COD_MOTIVO"));
		lIstanza.setDescrMotivo(getString("DESCR_OGGETTO"));
		lIstanza.setCodStatoIstanza(getString("COD_STATO_ISTANZA"));

		// N.Siep
		lEvento.setIdEvento(getBigDecimal("ID_EVENTO"));

		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_AN"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PR"));

		lMod.setSoggetto(lSoggetto);
		lMod.setIstanza(lIstanza);
		lMod.setEvento(lEvento);
		lMod.setFascicoloSiep(lFascicolo);

		return lMod;
	}

	public GenericModel getModelIstEve() throws DAOException {
		IstanzaSoggettoEventoFascicoloSiepModel lMod = new IstanzaSoggettoEventoFascicoloSiepModel();

		IstanzaModel lIstanza = new IstanzaModel();
		EventoModel lEvento = new EventoModel();

		// Evento
		lEvento.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEvento.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		lEvento.setDescrTipoEvento(getString("COD_EVE"));
		lEvento.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEvento.setDescrTipoProvvedimento(getString("COD_PRO"));
		lEvento.setCodMotivo(getString("COD_MOTIVO"));
		lEvento.setDescrMotivo(getString("COD_MOV"));
		lEvento.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		lEvento.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
		lEvento.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lEvento.setDescrLuogoEmittente(getString("LUO_EMI"));
		lEvento.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
		lEvento.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
		lEvento.setDataEmissione(getDate("DATA_EMISSIONE"));
		lEvento.setCodEsito(getString("COD_ESITO"));
		lEvento.setDescrEsito(getString("COD_ESI"));
		lEvento.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		lEvento.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		lEvento.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
		lEvento.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		lEvento.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		lEvento.setDescrLuogoDestinatario(getString("LUO_DES"));
		lEvento.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		lEvento.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
		lEvento.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lEvento.setDataInserimento(getDate("DATA_INSERIMENTO"));
		lEvento.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lEvento.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lEvento.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lEvento.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lEvento.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		lEvento.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		lEvento.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		lEvento.setCodMagistrato(getString("COD_MAGISTRATO"));
		lEvento.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
		lEvento.setFasSiuIdFascicoloSiusDest(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST"));
		lEvento.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		lEvento.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		lEvento.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		lEvento.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		lEvento.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		lEvento.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		lEvento.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));

		// ISTANZA
		lIstanza.setIdIstanza(getBigDecimal("ID_ISTANZA"));
		lIstanza.setCodStatoIstanza(getString("COD_STATO_ISTANZA"));

		lMod.setIstanza(lIstanza);
		lMod.setEvento(lEvento);

		return lMod;
	}

	public String setCondizione(IstanzaModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ISTANZA = " + aKey;
	}

	public String setCondizioneOggetto(IstanzaModel aIstMod) {
		String lCondizioni = new String();

		if (aIstMod.getCodMotivo() != null && !aIstMod.getCodMotivo().equals("")
				&& !aIstMod.getCodMotivo().equals("-")) {
			lCondizioni += " AND I.COD_MOTIVO = '" + aIstMod.getCodMotivo() + "'";
		}

		return lCondizioni;
	}

	/**
	 * Ricerca Istanza dalla chiave evento...
	 * 
	 * @param aDecMod
	 * @return
	 */
	public String ricercaByKeyEvento(BigDecimal aDecMod) {
		String lCondizioni = getSqlQuery();

		lCondizioni += " AND EVE_ID_EVENTO = " + aDecMod;

		setStatement(lCondizioni);

		return lCondizioni;
	}

	private String setCondizioneSoggetto(SoggettoModel aSm) {
		String lCondizioni = new String();

		if (aSm.getIdSoggetto().doubleValue() == 0) {
			if (!(aSm.getCognome().equals(""))) {
				lCondizioni += " AND COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
			}

			if (!(aSm.getNome().equals(""))) {
				lCondizioni += " AND NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
			}
		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}

		return lCondizioni;
	}

	public BigDecimal getProgressivoRegistro(BigDecimal aAnnoRegistro, String aUfficio) throws DAOException {
		String lStatement = new String();

		lStatement += "SELECT MAX(PROGR_REGISTRO) aMAX";
		lStatement += " FROM ISTANZA";
		lStatement += " WHERE ISTANZA.ANNO_REGISTRO = " + aAnnoRegistro;
		lStatement += " AND ISTANZA.COD_UFFICIO_INSERIMENTO = '" + aUfficio + "'";

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
}

package siap.siep.decretoordinanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: DecretoOrdinanzaSiepSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella DecretoOrdinanzaSiep
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
public class DecretoOrdinanzaSiepSqlDAO extends SqlDAO {

	public DecretoOrdinanzaSiepSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaDecretoOrdinanzaSiep(DecretoOrdinanzaSiepModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	// ricerca per oggetto procedimento

	public void ricercaDecretoOrdinanzaSiepByOggettoProcedimento(String[] aOggetto, FascicoloSiepModel aModel)
			throws DAOException {
		String lSql = getSqlQuery();

		if (aModel.getIdFascicoloSiep() != null) {
			lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getIdFascicoloSiep();
		}

		if (aOggetto.length > 0) {
			lSql += " AND COD_OGGETTO_PROCEDIMENTO IN (";
			for (int i = 0; i < aOggetto.length; i++) {
				lSql += "'" + aOggetto[i] + "'";
				if (aOggetto.length > 1 && i < aOggetto.length - 1)
					lSql += ",";

			}
			lSql += ")";

		}
		lSql += " AND FLAG_ELABORATO='N'";

		setStatement(lSql);
	}

	public void ricercaDecretoOrdinanzaSiepByIdEvento(BigDecimal aIdEvento) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEvento(aIdEvento);

		setStatement(lSql);
	}

	public void ricercaDecretoOrdinanzaSiepByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaDecretoOrdinanzaSiepByFascicoloSiepDesc(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		lStatement += setOrderDateDesc();

		setStatement(lStatement);
	}

	public void ricercaDecretoOrdinanzaSiepByFascicoloSiepDataInsDesc(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		lStatement += "  ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lStatement);
	}

	/**
	 * Ricerca l'ultimo Decreto per il fascicolo non ancora elaborato
	 * 
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaDecretoOrdinanzaSiepByFascicoloSiepFlagElaborato(BigDecimal aIdFascicolo)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL) ";

		lStatement += setOrderDateDesc();

		setStatement(lStatement);
	}

	// 01-09-2015 - --------> MEV_2 (Misure Sicurezza) STRP_2
	public void ricercaDecretoOrdinanzaSiepByIdEventoSemplice(BigDecimal aIdEvento) throws DAOException {
		String lSql = getSqlQuerySemplice();

		lSql += " " + setCondizioniByIdEvento(aIdEvento);

		setStatement(lSql);
	}

	protected String getSqlQuerySemplice() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_DECRETO_ORDINANZA_SIEP, " + "DATA_RICEZIONE_PROVVEDIMENTO, "
				+ "DATA_EMISSIONE_PROVVEDIMENTO, "
				+ "COD_TIPO_REGISTRO_ORDINANZA, TIPO_REGISTRO_ORDINANZA.RV_LOW_VALUE DESCR_TIPO_REGISTRO_ORDINANZA, "
				+ "ANNO_REGISTRO, " + "NUM_REGISTRO, " + "ANNO_PROVVEDIMENTO, " + "NUM_PROVVEDIMENTO, "
				+ "COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, TIPO_UFFICIO.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,"
				+ "DATA_SOSPENSIONE_ESECUZIONE, " + "DATA_DIFFERIMENTO, " + "DATA_RINVIO, "
				+ "DATA_FINE_INTERRUZIONE, " + "DATA_DEPOSITO_ISTANZA, " + "DATA_INTERRUZIONE_PENA, "
				+ "COD_OGGETTO_DECISIONE, OGGETTO_DECISIONE.RV_MEANING DESCR_OGGETTO_DECISIONE, "
				+ "MOTIVAZIONI, " + "NOTE, " + "FLAG_SCARCERARE_SCARCERATO, " + "FLAG_PRESENTANTE_ISTANZA, "
				+ "COD_CONTENUTO_DECRETO, CONTENUTO_DECRETO.RV_MEANING DESCR_CONTENUTO_DECRETO,"
				+ "COD_OGGETTO_PROCEDIMENTO, OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO_PROCEDIMENTO, "
				+ "FLAG_DATA_INTERRUZIONE_INVALID, " + "PROTOCOLLO, " + "ALTRA_AUTORITA, " + "ALTRO_LUOGO, "
				+ "ID_EVENTO_GENERATO, " + "FLAG_ELABORATO, " + "DATA_ESPULSIONE, "
				+ "FLAG_DECISIONE_TRIBUNALE, " + "COD_ESITO, ESITO.RV_MEANING DESCR_ESITO,"
				+ "NUM_ANNI_RINVIO, " + "NUM_MESI_RINVIO, " + "NUM_GIORNI_RINVIO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "DATA_REVOCA_SOSPENSIONE, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "ANNO_REG_GEN, NUMERO_REG_GEN, TIPO_REG_GEN";

		lStatement += " FROM DECRETO_ORDINANZA_SIEP, CG_REF_CODES TIPO_REGISTRO_ORDINANZA,";
		lStatement += " CG_REF_CODES TIPO_PROVVEDIMENTO,";
		lStatement += " COMUNE LUOGO_EMITTENTE,";
		lStatement += " CG_REF_CODES TIPO_UFFICIO,";
		lStatement += " CG_REF_CODES CONTENUTO_DECRETO,";
		lStatement += " CG_REF_CODES OGGETTO_PROCEDIMENTO,";
		lStatement += " CG_REF_CODES ESITO, CG_REF_CODES OGGETTO_DECISIONE";
		lStatement += " WHERE TIPO_REGISTRO_ORDINANZA.RV_DOMAIN = 'TIPO_REGISTRO_ORDINANZA' ";
		lStatement += " AND TIPO_REGISTRO_ORDINANZA.RV_HIGH_VALUE = NVL(DECRETO_ORDINANZA_SIEP.COD_TIPO_REGISTRO_ORDINANZA, '-')";
		lStatement += " AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_TIPO_PROVVEDIMENTO";
		lStatement += " AND LUOGO_EMITTENTE.COD_COMUNE = DECRETO_ORDINANZA_SIEP.COD_LUOGO_EMITTENTE";
		lStatement += " AND TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO'";
		lStatement += " AND TIPO_UFFICIO.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_TIPO_AUTORITA_EMITTENTE";
		lStatement += " AND CONTENUTO_DECRETO.RV_DOMAIN = 'CONTENUTO_DECRETO' AND CONTENUTO_DECRETO.RV_LOW_VALUE = NVL(DECRETO_ORDINANZA_SIEP.COD_CONTENUTO_DECRETO, '-')";
		lStatement += " AND OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND OGGETTO_PROCEDIMENTO.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_OGGETTO_PROCEDIMENTO";
		lStatement += " AND ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND ESITO.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_ESITO";
		lStatement += " AND OGGETTO_DECISIONE.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND OGGETTO_DECISIONE.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_OGGETTO_DECISIONE";

		return lStatement;
	}

	protected String getSqlQueryConEvento() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_DECRETO_ORDINANZA_SIEP, " + "DATA_RICEZIONE_PROVVEDIMENTO, "
				+ "DATA_EMISSIONE_PROVVEDIMENTO, "
				+ "COD_TIPO_REGISTRO_ORDINANZA, TIPO_REGISTRO_ORDINANZA.RV_LOW_VALUE DESCR_TIPO_REGISTRO_ORDINANZA, "
				+ "ANNO_REGISTRO, " + "NUM_REGISTRO, " + "ANNO_PROVVEDIMENTO, " + "NUM_PROVVEDIMENTO, "
				+ "DEC.COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, TIPO_UFFICIO.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "
				+ "DEC.COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,"
				+ "DATA_SOSPENSIONE_ESECUZIONE, " + "DATA_DIFFERIMENTO, " + "DATA_RINVIO, "
				+ "DATA_FINE_INTERRUZIONE, " + "DATA_DEPOSITO_ISTANZA, " + "DATA_INTERRUZIONE_PENA, "
				+ "COD_OGGETTO_DECISIONE, OGGETTO_DECISIONE.RV_MEANING DESCR_OGGETTO_DECISIONE, "
				+ "MOTIVAZIONI, " + "NOTE, " + "FLAG_SCARCERARE_SCARCERATO, " + "FLAG_PRESENTANTE_ISTANZA, "
				+ "COD_CONTENUTO_DECRETO, CONTENUTO_DECRETO.RV_MEANING DESCR_CONTENUTO_DECRETO,"
				+ "COD_OGGETTO_PROCEDIMENTO, OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO_PROCEDIMENTO, "
				+ "FLAG_DATA_INTERRUZIONE_INVALID, " + "PROTOCOLLO, " + "ALTRA_AUTORITA, " + "ALTRO_LUOGO, "
				+ "ID_EVENTO_GENERATO, " + "FLAG_ELABORATO, " + "DATA_ESPULSIONE, "
				+ "FLAG_DECISIONE_TRIBUNALE, " + "DEC.COD_ESITO, ESITO.RV_MEANING DESCR_ESITO,"
				+ "NUM_ANNI_RINVIO, " + "NUM_MESI_RINVIO, " + "NUM_GIORNI_RINVIO, "
				+ "DEC.COD_OPERATORE_INSERIMENTO, " + "DEC.COD_UFFICIO_INSERIMENTO, "
				+ "DEC.DATA_INSERIMENTO, " + "DEC.COD_OPERATORE_AGGIORNAMENTO, "
				+ "DEC.COD_UFFICIO_AGGIORNAMENTO, " + "DEC.DATA_AGGIORNAMENTO, " + "DATA_REVOCA_SOSPENSIONE, "
				+ "DEC.FAS_SIE_ID_FASCICOLO_SIEP, " + "ANNO_REG_GEN, NUMERO_REG_GEN, TIPO_REG_GEN";

		lStatement += " FROM DECRETO_ORDINANZA_SIEP DEC, EVENTO,";
		lStatement += " CG_REF_CODES TIPO_REGISTRO_ORDINANZA,";
		lStatement += " CG_REF_CODES TIPO_PROVVEDIMENTO,";
		lStatement += " COMUNE LUOGO_EMITTENTE,";
		lStatement += " CG_REF_CODES TIPO_UFFICIO,";
		lStatement += " CG_REF_CODES CONTENUTO_DECRETO,";
		lStatement += " CG_REF_CODES OGGETTO_PROCEDIMENTO,";
		lStatement += " CG_REF_CODES ESITO, CG_REF_CODES OGGETTO_DECISIONE";
		lStatement += " WHERE TIPO_REGISTRO_ORDINANZA.RV_DOMAIN = 'TIPO_REGISTRO_ORDINANZA' ";
		lStatement += " AND TIPO_REGISTRO_ORDINANZA.RV_HIGH_VALUE = NVL(DEC.COD_TIPO_REGISTRO_ORDINANZA, '-')";
		lStatement += " AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = DEC.COD_TIPO_PROVVEDIMENTO";
		lStatement += " AND LUOGO_EMITTENTE.COD_COMUNE = DEC.COD_LUOGO_EMITTENTE";
		lStatement += " AND TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO'";
		lStatement += " AND TIPO_UFFICIO.RV_LOW_VALUE = DEC.COD_TIPO_AUTORITA_EMITTENTE";
		lStatement += " AND CONTENUTO_DECRETO.RV_DOMAIN = 'CONTENUTO_DECRETO' AND CONTENUTO_DECRETO.RV_LOW_VALUE = NVL(DEC.COD_CONTENUTO_DECRETO, '-')";
		lStatement += " AND OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND OGGETTO_PROCEDIMENTO.RV_LOW_VALUE = DEC.COD_OGGETTO_PROCEDIMENTO";
		lStatement += " AND ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND ESITO.RV_LOW_VALUE = DEC.COD_ESITO";
		lStatement += " AND OGGETTO_DECISIONE.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND OGGETTO_DECISIONE.RV_LOW_VALUE = DEC.COD_OGGETTO_DECISIONE";
		lStatement += " AND DEC.ID_EVENTO_GENERATO = ID_EVENTO AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

		return lStatement;
	}

	public void RicercaDecretoOrdinanzaSiepGiudiceCassazione(BigDecimal aIdFas, String[] aCodici)
			throws DAOException {
		String lSql = getSqlQueryConEvento();

		lSql += " AND DEC.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFas;
		lSql += " " + setCondizioniCodOggettodecisione(aCodici);

		setStatement(lSql);
	}

	public String setCondizioniCodOggettodecisione(String[] aCodiceDec) {
		String lCondizione = "";
		if (aCodiceDec.length > 0) {
			lCondizione += " AND COD_OGGETTO_DECISIONE IN (";
			for (int i = 0; i < aCodiceDec.length; i++) {
				lCondizione += "'" + aCodiceDec[i] + "'";
				if (aCodiceDec.length > 1 && i < aCodiceDec.length - 1) {
					lCondizione += ",";
				}
			}

			lCondizione += ")";
		}

		return lCondizione;
	}

	public String setCondizioniByIdFasSiep(BigDecimal aIdFascSiep) {
		return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascSiep;
	}

	// ---------------- > END MEV_2 STEP_2

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_DECRETO_ORDINANZA_SIEP, " + "DATA_RICEZIONE_PROVVEDIMENTO, "
				+ "DATA_EMISSIONE_PROVVEDIMENTO, "
				+ "COD_TIPO_REGISTRO_ORDINANZA, TIPO_REGISTRO_ORDINANZA.RV_LOW_VALUE DESCR_TIPO_REGISTRO_ORDINANZA, "
				+ "ANNO_REGISTRO, " + "NUM_REGISTRO, " + "ANNO_PROVVEDIMENTO, " + "NUM_PROVVEDIMENTO, "
				+ "COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,"
				+ "DATA_SOSPENSIONE_ESECUZIONE, " + "DATA_DIFFERIMENTO, " + "DATA_RINVIO, "
				+ "DATA_FINE_INTERRUZIONE, " + "DATA_DEPOSITO_ISTANZA, " + "DATA_INTERRUZIONE_PENA, "
				+ "COD_OGGETTO_DECISIONE, OGGETTO_DECISIONE.RV_MEANING DESCR_OGGETTO_DECISIONE, "
				+ "MOTIVAZIONI, " + "NOTE, " + "FLAG_SCARCERARE_SCARCERATO, " + "FLAG_PRESENTANTE_ISTANZA, "
				+ "COD_CONTENUTO_DECRETO, CONTENUTO_DECRETO.RV_MEANING DESCR_CONTENUTO_DECRETO, "
				+ "COD_OGGETTO_PROCEDIMENTO, OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO_PROCEDIMENTO, "
				+ "FLAG_DATA_INTERRUZIONE_INVALID, " + "PROTOCOLLO, " + "ALTRA_AUTORITA, " + "ALTRO_LUOGO, "
				+ "ID_EVENTO_GENERATO, " + "FLAG_ELABORATO, " + "DATA_ESPULSIONE, "
				+ "FLAG_DECISIONE_TRIBUNALE, " + "COD_ESITO, ESITO.RV_MEANING DESCR_ESITO,"
				+ "NUM_ANNI_RINVIO, " + "NUM_MESI_RINVIO, " + "NUM_GIORNI_RINVIO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "DATA_REVOCA_SOSPENSIONE, " + "FAS_SIE_ID_FASCICOLO_SIEP, " +
				// 01-09-2015 - MEV_2 (Misure Sicurezza) STRP_2
				"ANNO_REG_GEN, NUMERO_REG_GEN, TIPO_REG_GEN";

		lStatement += " FROM DECRETO_ORDINANZA_SIEP, CG_REF_CODES TIPO_REGISTRO_ORDINANZA,";
		lStatement += " CG_REF_CODES TIPO_PROVVEDIMENTO,";
		lStatement += " CG_REF_CODES TIPO_AUTORITA_EMITTENTE, COMUNE LUOGO_EMITTENTE,";
		lStatement += " CG_REF_CODES CONTENUTO_DECRETO, CG_REF_CODES OGGETTO_PROCEDIMENTO,";
		lStatement += " CG_REF_CODES ESITO, CG_REF_CODES OGGETTO_DECISIONE";
		lStatement += " WHERE TIPO_REGISTRO_ORDINANZA.RV_DOMAIN = 'TIPO_REGISTRO_ORDINANZA' AND TIPO_REGISTRO_ORDINANZA.RV_HIGH_VALUE = DECRETO_ORDINANZA_SIEP.COD_TIPO_REGISTRO_ORDINANZA";
		lStatement += " AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_TIPO_PROVVEDIMENTO";
		lStatement += " AND TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO_SOSP' AND TIPO_AUTORITA_EMITTENTE.RV_HIGH_VALUE = DECRETO_ORDINANZA_SIEP.COD_TIPO_AUTORITA_EMITTENTE";
		lStatement += " AND TIPO_AUTORITA_EMITTENTE.RV_ABBREVIATION = DECRETO_ORDINANZA_SIEP.COD_TIPO_REGISTRO_ORDINANZA";
		lStatement += " AND LUOGO_EMITTENTE.COD_COMUNE = DECRETO_ORDINANZA_SIEP.COD_LUOGO_EMITTENTE";
		lStatement += " AND CONTENUTO_DECRETO.RV_DOMAIN = 'CONTENUTO_DECRETO' AND CONTENUTO_DECRETO.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_CONTENUTO_DECRETO";
		lStatement += " AND OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND OGGETTO_PROCEDIMENTO.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_OGGETTO_PROCEDIMENTO";
		lStatement += " AND ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND ESITO.RV_LOW_VALUE = DECRETO_ORDINANZA_SIEP.COD_ESITO";
		lStatement += " AND OGGETTO_DECISIONE.RV_DOMAIN = 'OGGETTO_SOSPENSIONI' AND OGGETTO_DECISIONE.RV_ABBREVIATION = DECRETO_ORDINANZA_SIEP.COD_OGGETTO_DECISIONE";

		// commentato serena e da ripristinare lStatement += " AND OGGETTO_DECISIONE.RV_DOMAIN =
		// 'OGGETTO_SOSPENSIONI' AND OGGETTO_DECISIONE.RV_LOW_VALUE =
		// DECRETO_ORDINANZA_SIEP.COD_OGGETTO_DECISIONE";
		lStatement += " AND OGGETTO_DECISIONE.RV_HIGH_VALUE = DECRETO_ORDINANZA_SIEP.COD_TIPO_REGISTRO_ORDINANZA";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		DecretoOrdinanzaSiepModel aModel = new DecretoOrdinanzaSiepModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdDecretoOrdinanzaSiep(getBigDecimal("ID_DECRETO_ORDINANZA_SIEP"));
		aModel.setDataRicezioneProvvedimento(getDate("DATA_RICEZIONE_PROVVEDIMENTO"));
		aModel.setDataEmissioneProvvedimento(getDate("DATA_EMISSIONE_PROVVEDIMENTO"));
		aModel.setCodTipoRegistroOrdinanza(getString("COD_TIPO_REGISTRO_ORDINANZA"));
		aModel.setDescrTipoRegistroOrdinanza(getString("DESCR_TIPO_REGISTRO_ORDINANZA"));
		aModel.setAnnoRegistro(getBigDecimal("ANNO_REGISTRO"));
		aModel.setNumRegistro(getBigDecimal("NUM_REGISTRO"));
		aModel.setAnnoProvvedimento(getBigDecimal("ANNO_PROVVEDIMENTO"));
		aModel.setNumProvvedimento(getBigDecimal("NUM_PROVVEDIMENTO"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		aModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		aModel.setDataSospensioneEsecuzione(getDate("DATA_SOSPENSIONE_ESECUZIONE"));
		aModel.setDataDifferimento(getDate("DATA_DIFFERIMENTO"));
		aModel.setDataRinvio(getDate("DATA_RINVIO"));
		aModel.setDataFineInterruzione(getDate("DATA_FINE_INTERRUZIONE"));
		aModel.setDataDepositoIstanza(getDate("DATA_DEPOSITO_ISTANZA"));
		aModel.setDataInterruzionePena(getDate("DATA_INTERRUZIONE_PENA"));
		aModel.setCodOggettoDecisione(getString("COD_OGGETTO_DECISIONE"));
		aModel.setDescrOggettoDecisione(getString("DESCR_OGGETTO_DECISIONE"));
		aModel.setMotivazioni(getString("MOTIVAZIONI"));
		aModel.setNote(getString("NOTE"));
		aModel.setFlagScarcerareScarcerato(getString("FLAG_SCARCERARE_SCARCERATO"));
		aModel.setFlagPresentanteIstanza(getString("FLAG_PRESENTANTE_ISTANZA"));
		aModel.setCodContenutoDecreto(getString("COD_CONTENUTO_DECRETO"));
		aModel.setDescrContenutoDecreto(getString("DESCR_CONTENUTO_DECRETO"));
		aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		aModel.setDescrOggettoProcedimento(getString("DESCR_OGGETTO_PROCEDIMENTO"));
		aModel.setFlagDataInterruzioneInvalid(getString("FLAG_DATA_INTERRUZIONE_INVALID"));
		aModel.setProtocollo(getString("PROTOCOLLO"));
		aModel.setAltraAutorita(getString("ALTRA_AUTORITA"));
		aModel.setAltroLuogo(getString("ALTRO_LUOGO"));
		aModel.setIdEventoGenerato(getBigDecimal("ID_EVENTO_GENERATO"));
		aModel.setFlagElaborato(getString("FLAG_ELABORATO"));
		aModel.setDataEspulsione(getDate("DATA_ESPULSIONE"));
		aModel.setFlagDecisioneTribunale(getString("FLAG_DECISIONE_TRIBUNALE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("DESCR_ESITO"));
		aModel.setNumAnniRinvio(getBigDecimal("NUM_ANNI_RINVIO"));
		aModel.setNumMesiRinvio(getBigDecimal("NUM_MESI_RINVIO"));
		aModel.setNumGiorniRinvio(getBigDecimal("NUM_GIORNI_RINVIO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setDataRevocaSospensione(getDate("DATA_REVOCA_SOSPENSIONE"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		// 01-09-2015 - MEV_2 (Misure Sicurezza) STRP_2
		aModel.setAnnoRegGen(getBigDecimal("ANNO_REG_GEN"));
		aModel.setNumeroRegGen(getBigDecimal("NUMERO_REG_GEN"));
		aModel.setTipoRegGen(getString("TIPO_REG_GEN"));

		return aModel;
	}

	public String setCondizione(DecretoOrdinanzaSiepModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_DECRETO_ORDINANZA_SIEP = " + aKey;
	}

	public String setCondizioniByIdEvento(BigDecimal aIdEvento) {
		return " AND ID_EVENTO_GENERATO = " + aIdEvento;
	}

	private String setOrderDateDesc() {
		String lCondizioni = " ORDER BY DATA_EMISSIONE_PROVVEDIMENTO DESC, DATA_INSERIMENTO DESC";

		return lCondizioni;
	}

}
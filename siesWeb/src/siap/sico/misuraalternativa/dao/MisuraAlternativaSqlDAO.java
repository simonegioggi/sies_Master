package siap.sico.misuraalternativa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: MisuraAlternativaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella MisuraAlternativa
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
public class MisuraAlternativaSqlDAO extends SqlDAO {

	public MisuraAlternativaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaMisuraAlternativaByAnnoProgr(BigDecimal anno, BigDecimal progr) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByAnnoProgr(anno, progr);
		// MERGE v10 COLLAUDO: aggiunto ordinamento
		lSql += " ORDER BY DATA_INSERIMENTO DESC";
		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEvento(aKey);

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaCorrenteByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEvento(aKey);
		lSql += setOrderMisuraDesc();

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaCorrenteByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aKey);
		lSql += setOrderMisuraDesc();

		setStatement(lSql);
	}

	// /////////misura alternativa Dichiarazione di Efficacia affidamento in prova//////////
	public void ricercaMisuraAlternativaDicEffAffInProvaByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'DI'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND COD_TIPO_DECISIONE = '03'";
		lSql += " AND COD_TIPO_MISURA IN ('0021','0190','0191')";

		setStatement(lSql);
	}

	// /////////misura alternativa concessa detenzione domiciliare speciale Ammissione Affidamento //////////
	public void ricercaMisuraAlternativaDetDomSpeAmmAffByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'CO'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND COD_TIPO_DECISIONE = '03'";
		lSql += " AND COD_TIPO_MISURA IN ('0192')";

		setStatement(lSql);
	}

	// MISURA ALTERNATIVA CONCESSA CORRENTE BY ID_FASCICOLO
	public void ricercaMisuraAlternativaConcessaCorrenteByIdFascicolo(BigDecimal aKeyFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'CO'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += setOrderMisuraDesc();

		setStatement(lSql);
	}

	// MISURA ALTERNATIVA SOSPESA CORRENTE BY ID_FASCICOLO
	public void ricercaMisuraAlternativaSospesaCorrenteByIdFascicolo(BigDecimal aKeyFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'SP'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += setOrderMisuraDesc();

		setStatement(lSql);
	}

	// detenzione domiciliare a termine
	public void ricercaMADetenzioneDomATermineByByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'DD'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND COD_TIPO_DECISIONE = '03'";
		lSql += " AND COD_TIPO_MISURA = '0011'";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaOSLiberazioneAnticipataByIdEvento(BigDecimal aKeyEvento)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'CO'";
		lSql += " AND EVE_ID_EVENTO = " + aKeyEvento;
		lSql += " AND COD_TIPO_MISURA = '0081'";

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaOSLiberazioneAnticipataMAByIdEvento(BigDecimal aKeyEvento)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'CO'";
		lSql += " AND EVE_ID_EVENTO = " + aKeyEvento;
		lSql += " AND COD_TIPO_MISURA = '0083'";

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaAmmissioneADetDomMAByIdEvento(BigDecimal aKeyEvento)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'CO'";
		lSql += " AND EVE_ID_EVENTO = " + aKeyEvento;
		lSql += " AND COD_TIPO_MISURA = '0194'";

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaConcessioneLibCondByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'CO'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND COD_TIPO_DECISIONE = '03'";
		lSql += " AND COD_TIPO_MISURA IN ('0025')";

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaByIdEventoDataInizio(BigDecimal aKey, PosizioneGiuridicaModel alPos)
			throws DAOException {
		String dataInizio = DateUtils.getDateToString(alPos.getDataInizio(), "dd/MM/yyyy");

		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEvento(aKey);
		lSql += " AND DATA_INIZIO_MISURA  = TO_DATE('" + dataInizio + "','DD/MM/YYYY')";

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaByIdFascicolo(BigDecimal aKey) throws DAOException
	// modifico il metodo per ovviare al fatto che la misura alternativa viene sempre caricata
	// invece va caricata sul dettaglio siep solo quando è legata ad un evento non annullato
	/*
	 * public void ricercaMisuraAlternativaByIdFascicolo(BigDecimal aKey) throws DAOException { String lSql =
	 * getSqlQuery();
	 * 
	 * lSql += " " + setCondizioniByIdFascicolo(aKey); lSql += setOrderMisuraDesc();
	 * 
	 * setStatement(lSql); }
	 */
	{
		String lSql = getSqlQueryEventoValidato();
		// lSql += " " + setCondizioniByIdFascicolo(aKey);
		lSql += " AND ma.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " and ev.id_evento = ma.eve_id_evento";

		// paolo cherubini 17/06/2011
		// cè un problema questa query ignora le LA per farle prendere
		// 1) aggiungo un (+) in maniera che estrae la misura alternativa anche se non esiste l'evento
		// successivo inoltre nel controllo ufficio
		// 2) faccio una forzatura con nvl che se non esiste l'evento successivo i codici ufficio sono uguali

		// Paolo Cherubini 19/11/2010
		// inserisco una condizione per la quale la misura alternativa non è più visibile se:
		// lSql += " and ev.id_evento = evesuccessivo.eve_id_evento";
		lSql += " and ev.id_evento = evesuccessivo.eve_id_evento (+) "; // 1) aggiungo un (+)

		// inserita da SIUS e viene annullato il provvedimento successivo di SIEP
		// quello SIUS non può naturalmente essere annullato perchè non di proprietà
		lSql += " and ((evesuccessivo.flag_documento_registrato = 'S'";
		lSql += " and evesuccessivo.cod_ufficio_inserimento != ev.cod_ufficio_inserimento)";

		// inserita da SIEP e devono essere annullati entrambi i provvedimenti sia lato PM che SORV
		// se viene annullato uno solo la misura è ancora visibile per ricordare all'utente di annullare
		// entrambi i provvedimenti
		lSql += " or ((evesuccessivo.flag_documento_registrato = 'S' or ev.flag_documento_registrato = 'S')";
		// lSql += " and evesuccessivo.cod_ufficio_inserimento = ev.cod_ufficio_inserimento))";
		lSql += " and nvl(evesuccessivo.cod_ufficio_inserimento,ev.cod_ufficio_inserimento) = ev.cod_ufficio_inserimento))"; // 2)
																																// faccio
																																// una
																																// forzatura
																																// con
																																// nvl
		// fine Paolo Cherubini 19/11/2010

		lSql += setOrderMisuraDesc();
		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaByFascicoloOrdinanza(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryOrdinanza(aKey);

		setStatement(lSql);
	}

	// ricerca ordinanza Det. Dom. Spe. Ammissione Affidamento
	public void ricercaEventoDetDomSpeAmmAffByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lStatement = getSqlQueryEvento();

		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
		lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('0192')";
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	// ricerca misura alternativa Poroga ulteriore periodo
	public void ricercaMisuraAlternativaProrogaUltPeriodoByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'CO'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND COD_TIPO_MISURA IN ('0077')";

		setStatement(lSql);
	}

	// ricerca misura alternativa ripristino det dom speciale
	public void ricercaMisuraAlternativaRipristinoDetDomSpecIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_NATURA_DECISIONE = 'RI'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND COD_TIPO_MISURA IN ('0194')";

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaByIdFascicoloNaturaDecisione(BigDecimal aKey, String aNatura)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND COD_NATURA_DECISIONE = '" + aNatura + "'";
		lSql += setOrderMisuraDesc();

		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisioneOrderDescData(BigDecimal aKey,
			String[] aDecisione, String[] aNatura, String[] aTipoMisura) throws DAOException {
		String lSql = getSqlQuery();

		// inserisco l'evento x scartare le misure alternative annullate cherubini 21/04/2010
		lSql = lSql.replace("FROM MISURA_ALTERNATIVA MA,", "FROM MISURA_ALTERNATIVA MA, EVENTO EV,");

		// inserisco anche l'evento collegato x scartare le misure alternative annullate Ambrosino 26/07/2010
		// lSql =lSql.replace("FROM MISURA_ALTERNATIVA MA,",
		// "FROM MISURA_ALTERNATIVA MA, EVENTO EV, EVENTO EVECOLL,");

		if (aDecisione != null && aDecisione.length > 0) {
			lSql += " AND MA.COD_TIPO_DECISIONE IN (";
			for (int i = 0; i < aDecisione.length; i++) {
				lSql += "'" + aDecisione[i] + "'";
				if (aDecisione.length > 1 && i < aDecisione.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		if (aNatura != null && aNatura.length > 0) {
			lSql += " AND MA.COD_NATURA_DECISIONE IN (";
			for (int i = 0; i < aNatura.length; i++) {
				lSql += "'" + aNatura[i] + "'";
				if (aNatura.length > 1 && i < aNatura.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		if (aTipoMisura != null && aTipoMisura.length > 0) {
			lSql += " AND MA.COD_TIPO_MISURA IN (";
			for (int i = 0; i < aTipoMisura.length; i++) {
				lSql += "'" + aTipoMisura[i] + "'";
				if (aTipoMisura.length > 1 && i < aTipoMisura.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		lSql += " AND MA.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		// inserisco l'evento x scartare le misure alternative annullate cherubini 21/04/2010
		lSql += " AND MA.EVE_ID_EVENTO = EV.ID_EVENTO ";
		lSql += " AND EV.flag_documento_registrato != 'A'";

		/*
		 * // inserisco l'evento Collegato x scartare le misure alternative annullate Ambrosino 26/07/2010
		 * lSql += " AND EV.ID_EVENTO = EVECOLL.EVE_ID_EVENTO "; lSql +=
		 * " AND EVECOLL.flag_documento_registrato != 'A'";
		 */

		lSql += setOrderMisuraDesc();
		setStatement(lSql);
	}

	public void ricercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisionePerVerbale(BigDecimal aKey,
			String[] aDecisione, String[] aNatura, String[] aTipoMisura) throws DAOException {
		String lSql = getSqlQuery();

		// inserisco l'evento x scartare le misure alternative annullate cherubini 21/04/2010
		// lSql =lSql.replace("FROM MISURA_ALTERNATIVA MA,", "FROM MISURA_ALTERNATIVA MA, EVENTO EV,");

		// inserisco anche l'evento collegato x scartare le misure alternative annullate Ambrosino 26/07/2010
		lSql = lSql.replace("FROM MISURA_ALTERNATIVA MA,",
				"FROM MISURA_ALTERNATIVA MA, EVENTO EV, EVENTO EVECOLL,");

		if (aDecisione != null && aDecisione.length > 0) {
			lSql += " AND MA.COD_TIPO_DECISIONE IN (";
			for (int i = 0; i < aDecisione.length; i++) {
				lSql += "'" + aDecisione[i] + "'";
				if (aDecisione.length > 1 && i < aDecisione.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		if (aNatura != null && aNatura.length > 0) {
			lSql += " AND MA.COD_NATURA_DECISIONE IN (";
			for (int i = 0; i < aNatura.length; i++) {
				lSql += "'" + aNatura[i] + "'";
				if (aNatura.length > 1 && i < aNatura.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		if (aTipoMisura != null && aTipoMisura.length > 0) {
			lSql += " AND MA.COD_TIPO_MISURA IN (";
			for (int i = 0; i < aTipoMisura.length; i++) {
				lSql += "'" + aTipoMisura[i] + "'";
				if (aTipoMisura.length > 1 && i < aTipoMisura.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		lSql += " AND MA.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		// inserisco l'evento x scartare le misure alternative annullate cherubini 21/04/2010
		lSql += " AND MA.EVE_ID_EVENTO = EV.ID_EVENTO ";
		lSql += " AND EV.flag_documento_registrato != 'A'";

		// inserisco l'evento Collegato x scartare le misure alternative annullate Ambrosino 26/07/2010
		lSql += " AND EV.ID_EVENTO = EVECOLL.EVE_ID_EVENTO ";
		lSql += " AND EVECOLL.flag_documento_registrato != 'A'";

		lSql += setOrderMisuraDesc();
		setStatement(lSql);
	}

	protected String getSqlQueryOrdinanza(BigDecimal aKey) {
		String lStatement = new String("");

		lStatement += "  SELECT  ";
		lStatement += "  ID_MISURA_ALTERNATIVA,   ";
		lStatement += "  COD_TIPO_DECISIONE,  ";
		lStatement += "  COD_NATURA_DECISIONE,  ";
		lStatement += "  u1.RV_MEANING DESCRUFFSORV,  ";
		lStatement += "  COD_TIPO_MISURA,  ";
		lStatement += "  DATA_DECISIONE,   ";
		lStatement += "  MA.COD_MAGISTRATO,   ";
		lStatement += "  COD_UFFICIO_SORVEGLIANZA, ";
		lStatement += "  CSS_ID_CSSA,  ";
		lStatement += "  DESCR_LUOGO_PROVA,  ";
		lStatement += "  NUM_ANNI_MISURA, ";
		lStatement += "  NUM_MESI_MISURA, ";
		lStatement += "  NUM_GIORNI_MISURA, ";
		lStatement += "  DATA_INIZIO_MISURA, ";
		lStatement += "  DATA_FINE_MISURA, ";
		lStatement += "  CHIAVE_ANNO_FASCICOLO_SIUS, ";
		lStatement += "  PROV.RV_MEANING TIPODECISIONE, ";
		lStatement += "  NAT.RV_MEANING NATURA, ";
		lStatement += "  MPROV.RV_ABBREVIATION MISURA, ";
		lStatement += "  CHIAVE_UFFICIO_FASCICOLO_SIUS, ";
		// DARIO
		lStatement += " UFFSOR.RV_MEANING DESCUFSORV, ";
		lStatement += " COM.DESCRIZIONE DESCCOM, ";

		lStatement += " TDSCOMP.RV_MEANING DESCUFFTDS, ";
		lStatement += " COMTDSCOMP.DESCRIZIONE DESCCOMTDS, ";

		lStatement += "  CHIAVE_PROGR_FASCICOLO_SIUS,   ";
		lStatement += "  ANNO_REGISTRO, ";
		lStatement += "  NUMERO_REGISTRO,  ";
		lStatement += "  MA.COD_OPERATORE_INSERIMENTO,   ";
		lStatement += "  MA.DATA_INSERIMENTO,  ";
		lStatement += "  MA.COD_UFFICIO_INSERIMENTO,  ";
		lStatement += "  MA.COD_OPERATORE_AGGIORNAMENTO,   ";
		lStatement += "  MA.DATA_AGGIORNAMENTO, ";
		lStatement += "  MA.COD_UFFICIO_AGGIORNAMENTO,  ";
		lStatement += "  MA.FAS_SIE_ID_FASCICOLO_SIEP, 	 ";
		lStatement += "  MA.EVE_ID_EVENTO , ";
		lStatement += "  MA.NOTE , ";
		lStatement += "  MA.DATA_SCARCERAZIONE,  ";
		lStatement += "  MA.DATA_INGRESSO_ISTITUTO,  ";
		lStatement += "  MA.COD_TIPO_UFFICIO_SCARCERAZIONE,  ";

		// DARIO
		lStatement += "  MA.DATA_INIZIO_REVOCA,  ";
		lStatement += "  MA.NUM_ANNI_REVOCA_RECLUSIONE,  ";
		lStatement += "  MA.NUM_MESI_REVOCA_RECLUSIONE,  ";
		lStatement += "  MA.NUM_GIORNI_REVOCA_RECLUSIONE,  ";
		lStatement += "  MA.NUM_ANNI_REVOCA_ARRESTO,  ";
		lStatement += "  MA.NUM_MESI_REVOCA_ARRESTO,  ";
		lStatement += "  MA.NUM_GIORNI_REVOCA_ARRESTO,  ";
		lStatement += "  MA.ANNO_ALTRO_TITOLO,  ";
		lStatement += "  MA.NUM_ALTRO_TITOLO,  ";
		lStatement += "  MA.DATA_ALTRO_TITOLO,  ";
		lStatement += "  MA.COD_LUOGO_ALTRO_TITOLO,  ";
		lStatement += "  LUOGO_ALTRO.DESCRIZIONE DESCR_LUOGO, ";

		lStatement += "  MA.COD_AUTORITA_ALTRO_TITOLO,  ";
		lStatement += "  TIPO_AUTORITA.RV_MEANING DESCR_TIPO_AUTORITA, ";

		lStatement += "  MA.FLAG_UFFICIO_INSERIMENTO, ";
		lStatement += "  MA.DATA_SCADENZA_PROROGA,MA.FLAG_DECISIONE_TRIBUNALE,MA.COD_TDS_COMPETENTE, ";
		lStatement += "  MA.FLAG_SITUAZIONE, ";
		lStatement += "  MA.DATA_ESECUTIVITA, "; // MEV_9
		lStatement += "  MA.FLAG_PERIODO_ESPIATO ";
		
		lStatement += "  FROM MISURA_ALTERNATIVA MA, CG_REF_CODES PROV, CG_REF_CODES NAT, CG_REF_CODES UFFSCA, ";
		lStatement += "  CG_REF_CODES MPROV, CG_REF_CODES TIPO_AUTORITA,COMUNE LUOGO_ALTRO, ";
		// DARIO
		lStatement += "  CG_REF_CODES UFFSOR, UFFICIO SORU, COMUNE COM, ";
		lStatement += "  CG_REF_CODES TDSCOMP, UFFICIO TDSCOMUFF, COMUNE COMTDSCOMP, ";

		lStatement += "  (SELECT U.COD_UFFICIO,UFF.RV_MEANING FROM CG_REF_CODES UFF, UFFICIO U WHERE UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO) u1, ";
		lStatement += "  (SELECT E.ID_EVENTO,MAX(E.DATA_INSERIMENTO) FROM EVENTO E WHERE E.COD_TIPO_PROVVEDIMENTO='03' AND E.FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKey + "  GROUP BY E.ID_EVENTO) E1 ";
		
		lStatement += "  WHERE PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'  ";
		lStatement += "  AND NAT.RV_DOMAIN = 'NATURA_DECISIONE'   ";
		lStatement += "  AND MPROV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'   ";
		lStatement += "  AND PROV.RV_LOW_VALUE = COD_TIPO_DECISIONE   ";
		lStatement += "  AND NAT.RV_LOW_VALUE = COD_NATURA_DECISIONE   ";
		lStatement += "  AND MPROV.RV_LOW_VALUE = COD_TIPO_MISURA   ";
		lStatement += "  AND u1.COD_UFFICIO(+) = MA.COD_UFFICIO_SORVEGLIANZA   ";

		lStatement += "  AND TIPO_AUTORITA.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA.RV_LOW_VALUE = MA.COD_AUTORITA_ALTRO_TITOLO ";
		lStatement += "  AND LUOGO_ALTRO.COD_COMUNE = MA.COD_LUOGO_ALTRO_TITOLO ";

		lStatement += "  AND UFFSCA.RV_DOMAIN = 'TIPO_UFFICIO_SCARCERAZIONE'  ";
		lStatement += "  AND UFFSCA.RV_LOW_VALUE = COD_TIPO_UFFICIO_SCARCERAZIONE   ";
		lStatement += "  AND MA.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += "  AND MA.EVE_ID_EVENTO = E1.ID_EVENTO   ";
		lStatement += "  AND MA.COD_TIPO_DECISIONE = '03'   ";
		lStatement += "  AND MA.COD_NATURA_DECISIONE = 'CO'   ";

		// DARIO
		lStatement += " AND SORU.COD_UFFICIO = MA.CHIAVE_UFFICIO_FASCICOLO_SIUS  ";
		lStatement += " AND COM.COD_COMUNE = SORU.COD_COMUNE ";
		lStatement += " AND UFFSOR.RV_DOMAIN = 'TIPO_UFFICIO'   ";
		lStatement += " AND UFFSOR.RV_LOW_VALUE = SORU.COD_TIPO_UFFICIO  ";

		lStatement += " AND TDSCOMUFF.COD_UFFICIO = NVL(MA.COD_TDS_COMPETENTE, '-')";
		lStatement += " AND COMTDSCOMP.COD_COMUNE = TDSCOMUFF.COD_COMUNE ";
		lStatement += " AND TDSCOMP.RV_DOMAIN = 'TIPO_UFFICIO'   ";
		lStatement += " AND TDSCOMP.RV_LOW_VALUE = TDSCOMUFF.COD_TIPO_UFFICIO  ";

		return lStatement;
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT "
				+ "MA.ID_MISURA_ALTERNATIVA, "
				+ "MA.COD_TIPO_DECISIONE, "
				+ "PROV.RV_MEANING TIPODECISIONE, "
				+ "MA.COD_NATURA_DECISIONE, "
				+ "NAT.RV_MEANING NATURA, "
				+ "u1.RV_MEANING DESCRUFFSORV,"
				+ "MA.COD_TIPO_MISURA, "
				+ "MPROV.RV_MEANING MISURA, "
				+
				// gdv
				"MPROV.RV_ABBREVIATION LEGGE_MISURA, "
				+
				"MA.DATA_DECISIONE, "
				+ "MA.COD_MAGISTRATO, "
				+ "MA.COD_UFFICIO_SORVEGLIANZA, "
				+ "MA.CSS_ID_CSSA, "
				+ "MA.DESCR_LUOGO_PROVA, "
				+ "MA.NUM_ANNI_MISURA, "
				+ "MA.NUM_MESI_MISURA, "
				+ "MA.NUM_GIORNI_MISURA, "
				+ "MA.DATA_INIZIO_MISURA, "
				+ "MA.DATA_FINE_MISURA, "
				+ "MA.CHIAVE_ANNO_FASCICOLO_SIUS, "
				+ "MA.CHIAVE_UFFICIO_FASCICOLO_SIUS, "
				+
				// DARIO
				"UFFSOR.RV_MEANING DESCUFSORV, " + "COM.DESCRIZIONE DESCCOM, " +
				"TDSCOMP.RV_MEANING DESCUFFTDS, " + "COMTDSCOMP.DESCRIZIONE DESCCOMTDS, " +
				"MA.CHIAVE_PROGR_FASCICOLO_SIUS, " + "MA.ANNO_REGISTRO, "
				+ "MA.NUMERO_REGISTRO, "
				+ "MA.COD_OPERATORE_INSERIMENTO, "
				+ "MA.DATA_INSERIMENTO, "
				+ "MA.COD_UFFICIO_INSERIMENTO, "
				+ "MA.COD_OPERATORE_AGGIORNAMENTO, "
				+ "MA.DATA_AGGIORNAMENTO, "
				+ "MA.COD_UFFICIO_AGGIORNAMENTO, "
				+ "MA.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "MA.EVE_ID_EVENTO, "
				+ "MA.NOTE, "
				+ "MA.DATA_SCARCERAZIONE, "
				+ "MA.DATA_INGRESSO_ISTITUTO, "
				+ "MA.COD_TIPO_UFFICIO_SCARCERAZIONE, "
				+
				// dario
				" MA.DATA_INIZIO_REVOCA,  " + " MA.NUM_ANNI_REVOCA_RECLUSIONE,  "
				+ " MA.NUM_MESI_REVOCA_RECLUSIONE,  " + " MA.NUM_GIORNI_REVOCA_RECLUSIONE,  "
				+ " MA.NUM_ANNI_REVOCA_ARRESTO,  " + " MA.NUM_MESI_REVOCA_ARRESTO,  "
				+ " MA.NUM_GIORNI_REVOCA_ARRESTO,  " + " MA.FLAG_PERIODO_ESPIATO, "
				+ " MA.ANNO_ALTRO_TITOLO,  " + " MA.NUM_ALTRO_TITOLO,  " + " MA.DATA_ALTRO_TITOLO,  "
				+ " MA.COD_LUOGO_ALTRO_TITOLO,  " + " MA.COD_AUTORITA_ALTRO_TITOLO,  "
				+ " LUOGO_ALTRO.DESCRIZIONE DESCR_LUOGO, "
				+ " TIPO_AUTORITA.RV_MEANING DESCR_TIPO_AUTORITA, "
				+ " MA.DATA_SCADENZA_PROROGA, MA.FLAG_DECISIONE_TRIBUNALE,MA.COD_TDS_COMPETENTE, "
				+ " MA.FLAG_SITUAZIONE, " + " MA.FLAG_UFFICIO_INSERIMENTO ";

		// DL 146/2013
		lStatement += ", MA.COD_TIPO_DECISIONE_MA_AT, DESC_TIPO_DECISIONE_MA_AT.RV_MEANING descTipoDecisioneMaAt "
				+ // +descrizione
				", MA.COD_TIPO_MISURA_MA_AT, DESC_TIPO_MISURA_MA_AT.RV_MEANING descTipoMisuraMaAt "
				+ // +descrizione
				", MA.DATA_DECISIONE_MA_AT "
				+ ", MA.CHIAVE_ANNO_FAS_SIUS_MA_AT "
				+ ", MA.CHIAVE_PROGR_FAS_SIUS_MA_AT "
				+ ", MA.CHIAVE_UFF_FAS_SIUS_MA_AT, TIPO_UFF_AT.RV_MEANING descTipoUffMaAt " + // +descrizione
				", MA.ANNO_REGISTRO_MA_AT " + ", MA.NUMERO_REGISTRO_MA_AT ";
		lStatement += ", MA.FL_FORMA_MISURA ";
		lStatement += ", MA.DATA_ESECUTIVITA "; // MEV_9
		lStatement += ", MA.DESCRIZIONE_COMUNITA ";

		lStatement += " FROM MISURA_ALTERNATIVA MA, CG_REF_CODES PROV, CG_REF_CODES NAT,CG_REF_CODES UFFSCA, ";
		lStatement += " CG_REF_CODES MPROV,CG_REF_CODES TIPO_AUTORITA,COMUNE LUOGO_ALTRO, ";

		// DARIO
		lStatement += " CG_REF_CODES UFFSOR, UFFICIO SORU, COMUNE COM, ";
		lStatement += " CG_REF_CODES TDSCOMP, UFFICIO TDSCOMUFF, COMUNE COMTDSCOMP, ";

		lStatement += " CG_REF_CODES DESC_TIPO_DECISIONE_MA_AT, "; // desc TIPO_DECISIONE_MA_AT dominio
																	// TIPO_PROVVEDIMENTO
		lStatement += " CG_REF_CODES DESC_TIPO_MISURA_MA_AT,  "; // desc TIPO_MISURA_MA_AT dominio
																	// MOTIVO_PROVVEDIMENTO
		lStatement += " CG_REF_CODES TIPO_UFF_AT, UFFICIO UFF_MA_AT, COMUNE COMUFF_MA_AT, "; // DESC
																								// UFFICIO_MA_AT

		lStatement += "    (SELECT U.COD_UFFICIO,UFF.RV_MEANING FROM CG_REF_CODES UFF, UFFICIO U WHERE UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO) u1";
		lStatement += " WHERE PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
		// MEV_39: aggiunte 2 left outer join sulla natura decisione
		lStatement += " AND NAT.RV_DOMAIN(+) = 'NATURA_DECISIONE' ";
		lStatement += " AND MPROV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND PROV.RV_LOW_VALUE = MA.COD_TIPO_DECISIONE ";
		lStatement += " AND NAT.RV_LOW_VALUE(+) = MA.COD_NATURA_DECISIONE "; // MEV_39 
		lStatement += " AND MPROV.RV_LOW_VALUE = MA.COD_TIPO_MISURA ";
		lStatement += " AND UFFSCA.RV_DOMAIN = 'TIPO_UFFICIO_SCARCERAZIONE'   ";
		lStatement += " AND UFFSCA.RV_LOW_VALUE = MA.COD_TIPO_UFFICIO_SCARCERAZIONE   ";
		lStatement += " AND u1.COD_UFFICIO(+) = MA.COD_UFFICIO_SORVEGLIANZA ";
		lStatement += "  AND TIPO_AUTORITA.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA.RV_LOW_VALUE = MA.COD_AUTORITA_ALTRO_TITOLO ";
		lStatement += "  AND LUOGO_ALTRO.COD_COMUNE = MA.COD_LUOGO_ALTRO_TITOLO ";
		// DARIO
		lStatement += " AND SORU.COD_UFFICIO = MA.CHIAVE_UFFICIO_FASCICOLO_SIUS  ";
		lStatement += " AND COM.COD_COMUNE = SORU.COD_COMUNE ";
		lStatement += " AND UFFSOR.RV_DOMAIN = 'TIPO_UFFICIO'   ";
		lStatement += " AND UFFSOR.RV_LOW_VALUE = SORU.COD_TIPO_UFFICIO  ";

		lStatement += " AND TDSCOMUFF.COD_UFFICIO = NVL(MA.COD_TDS_COMPETENTE, '-')";
		lStatement += " AND COMTDSCOMP.COD_COMUNE = TDSCOMUFF.COD_COMUNE ";
		lStatement += " AND TDSCOMP.RV_DOMAIN = 'TIPO_UFFICIO'   ";
		lStatement += " AND TDSCOMP.RV_LOW_VALUE = TDSCOMUFF.COD_TIPO_UFFICIO  ";

		// join x ufficio SIUS MA_AT (misura alternativa altro titolo)
		lStatement += " AND UFF_MA_AT.COD_UFFICIO = NVL(MA.CHIAVE_UFF_FAS_SIUS_MA_AT, '-')";
		lStatement += " AND COMUFF_MA_AT.COD_COMUNE = UFF_MA_AT.COD_COMUNE ";
		lStatement += " AND TIPO_UFF_AT.RV_DOMAIN = 'TIPO_UFFICIO'   ";
		lStatement += " AND TIPO_UFF_AT.RV_LOW_VALUE = UFF_MA_AT.COD_TIPO_UFFICIO  ";

		lStatement += " AND DESC_TIPO_DECISIONE_MA_AT.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND DESC_TIPO_DECISIONE_MA_AT.RV_LOW_VALUE = NVL(MA.COD_TIPO_DECISIONE_MA_AT,'-')  ";
		lStatement += " AND DESC_TIPO_MISURA_MA_AT.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND DESC_TIPO_MISURA_MA_AT.RV_LOW_VALUE = NVL(MA.COD_TIPO_MISURA_MA_AT,'-')  ";

		return lStatement;
	}

	// creo un nuovo metodo per ovviare al fatto che la misura alternativa viene sempre caricata
	// invece va caricata sul dettaglio siep solo quando è legata ad un evento non annullato
	protected String getSqlQueryEventoValidato() {
		String lStatement = new String("");
		lStatement += " SELECT "
				+ "MA.ID_MISURA_ALTERNATIVA, "
				+ "MA.COD_TIPO_DECISIONE, "
				+ "PROV.RV_MEANING TIPODECISIONE, "
				+ "MA.COD_NATURA_DECISIONE, "
				+ "NAT.RV_MEANING NATURA, "
				+ "u1.RV_MEANING DESCRUFFSORV,"
				+ "MA.COD_TIPO_MISURA, "
				+ "MPROV.RV_MEANING MISURA, "
				+
				// gdv
				"MPROV.RV_ABBREVIATION LEGGE_MISURA, "
				+

				"MA.DATA_DECISIONE, "
				+ "MA.COD_MAGISTRATO, "
				+ "MA.COD_UFFICIO_SORVEGLIANZA, "
				+ "MA.CSS_ID_CSSA, "
				+ "MA.DESCR_LUOGO_PROVA, "
				+ "MA.NUM_ANNI_MISURA, "
				+ "MA.NUM_MESI_MISURA, "
				+ "MA.NUM_GIORNI_MISURA, "
				+ "MA.DATA_INIZIO_MISURA, "
				+ "MA.DATA_FINE_MISURA, "
				+ "MA.CHIAVE_ANNO_FASCICOLO_SIUS, "
				+ "MA.CHIAVE_UFFICIO_FASCICOLO_SIUS, "
				+
				// DARIO
				"UFFSOR.RV_MEANING DESCUFSORV, " + "COM.DESCRIZIONE DESCCOM, " +

				"TDSCOMP.RV_MEANING DESCUFFTDS, " + "COMTDSCOMP.DESCRIZIONE DESCCOMTDS, " +

				"MA.CHIAVE_PROGR_FASCICOLO_SIUS, " + "MA.ANNO_REGISTRO, "
				+ "MA.NUMERO_REGISTRO, "
				+ "MA.COD_OPERATORE_INSERIMENTO, "
				+ "MA.DATA_INSERIMENTO, "
				+ "MA.COD_UFFICIO_INSERIMENTO, "
				+ "MA.COD_OPERATORE_AGGIORNAMENTO, "
				+ "MA.DATA_AGGIORNAMENTO, "
				+ "MA.COD_UFFICIO_AGGIORNAMENTO, "
				+ "MA.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "MA.EVE_ID_EVENTO, "
				+ "MA.NOTE, "
				+ "MA.DATA_SCARCERAZIONE, "
				+ "MA.DATA_INGRESSO_ISTITUTO, "
				+ "MA.COD_TIPO_UFFICIO_SCARCERAZIONE, "
				+
				// dario
				" MA.DATA_INIZIO_REVOCA,  " + " MA.NUM_ANNI_REVOCA_RECLUSIONE,  "
				+ " MA.NUM_MESI_REVOCA_RECLUSIONE,  " + " MA.NUM_GIORNI_REVOCA_RECLUSIONE,  "
				+ " MA.NUM_ANNI_REVOCA_ARRESTO,  " + " MA.NUM_MESI_REVOCA_ARRESTO,  "
				+ " MA.NUM_GIORNI_REVOCA_ARRESTO,  " + " MA.FLAG_PERIODO_ESPIATO, "
				+ " MA.ANNO_ALTRO_TITOLO,  " + " MA.NUM_ALTRO_TITOLO,  " + " MA.DATA_ALTRO_TITOLO,  "
				+ " MA.COD_LUOGO_ALTRO_TITOLO,  " + " MA.COD_AUTORITA_ALTRO_TITOLO,  "
				+ " LUOGO_ALTRO.DESCRIZIONE DESCR_LUOGO, "
				+ " TIPO_AUTORITA.RV_MEANING DESCR_TIPO_AUTORITA, "
				+ " MA.DATA_ESECUTIVITA, "  // MEV_9
				+ " MA.DATA_SCADENZA_PROROGA, MA.FLAG_DECISIONE_TRIBUNALE,MA.COD_TDS_COMPETENTE, "
				+ " MA.FLAG_SITUAZIONE, " + " MA.FLAG_UFFICIO_INSERIMENTO ";
		lStatement += " FROM EVENTO EV,EVENTO EVESUCCESSIVO,MISURA_ALTERNATIVA MA, CG_REF_CODES PROV, CG_REF_CODES NAT,CG_REF_CODES UFFSCA, ";
		lStatement += " CG_REF_CODES MPROV,CG_REF_CODES TIPO_AUTORITA,COMUNE LUOGO_ALTRO, ";

		// DARIO
		lStatement += " CG_REF_CODES UFFSOR, UFFICIO SORU, COMUNE COM, ";
		lStatement += " CG_REF_CODES TDSCOMP, UFFICIO TDSCOMUFF, COMUNE COMTDSCOMP, ";

		lStatement += "    (SELECT U.COD_UFFICIO,UFF.RV_MEANING FROM CG_REF_CODES UFF, UFFICIO U WHERE UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO) u1";
		lStatement += " WHERE PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
		lStatement += " AND NAT.RV_DOMAIN = 'NATURA_DECISIONE' ";
		lStatement += " AND MPROV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND PROV.RV_LOW_VALUE = MA.COD_TIPO_DECISIONE ";
		lStatement += " AND NAT.RV_LOW_VALUE = MA.COD_NATURA_DECISIONE ";
		lStatement += " AND MPROV.RV_LOW_VALUE = MA.COD_TIPO_MISURA ";
		lStatement += " AND UFFSCA.RV_DOMAIN = 'TIPO_UFFICIO_SCARCERAZIONE'   ";
		lStatement += " AND UFFSCA.RV_LOW_VALUE = MA.COD_TIPO_UFFICIO_SCARCERAZIONE   ";
		lStatement += " AND u1.COD_UFFICIO(+) = MA.COD_UFFICIO_SORVEGLIANZA ";
		lStatement += "  AND TIPO_AUTORITA.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA.RV_LOW_VALUE = MA.COD_AUTORITA_ALTRO_TITOLO ";
		lStatement += "  AND LUOGO_ALTRO.COD_COMUNE = MA.COD_LUOGO_ALTRO_TITOLO ";
		// DARIO
		lStatement += " AND SORU.COD_UFFICIO = MA.CHIAVE_UFFICIO_FASCICOLO_SIUS  ";
		lStatement += " AND COM.COD_COMUNE = SORU.COD_COMUNE ";
		lStatement += " AND UFFSOR.RV_DOMAIN = 'TIPO_UFFICIO'   ";
		lStatement += " AND UFFSOR.RV_LOW_VALUE = SORU.COD_TIPO_UFFICIO  ";

		lStatement += " AND TDSCOMUFF.COD_UFFICIO = NVL(MA.COD_TDS_COMPETENTE, '-')";
		lStatement += " AND COMTDSCOMP.COD_COMUNE = TDSCOMUFF.COD_COMUNE ";
		lStatement += " AND TDSCOMP.RV_DOMAIN = 'TIPO_UFFICIO'   ";
		lStatement += " AND TDSCOMP.RV_LOW_VALUE = TDSCOMUFF.COD_TIPO_UFFICIO  ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		MisuraAlternativaModel aModel = new MisuraAlternativaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdMisuraAlternativa(getBigDecimal("ID_MISURA_ALTERNATIVA"));
		aModel.setCodTipoDecisione(getString("COD_TIPO_DECISIONE"));
		aModel.setDescrTipoDecisione(getString("TIPODECISIONE"));
		aModel.setCodNaturaDecisione(getString("COD_NATURA_DECISIONE"));
		aModel.setDescrNaturaDecisione(getString("NATURA"));
		aModel.setCodTipoMisura(getString("COD_TIPO_MISURA"));
		aModel.setDescrTipoMisura(getString("MISURA"));
		aModel.setDataDecisione(getDate("DATA_DECISIONE"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setCodUfficioSorveglianza(getString("COD_UFFICIO_SORVEGLIANZA"));
		aModel.setDescrUfficioSorveglianza(getString("DESCRUFFSORV"));
		aModel.setCssIdCssa(getBigDecimal("CSS_ID_CSSA"));
		aModel.setDescrLuogoProva(getString("DESCR_LUOGO_PROVA"));
		aModel.setNumAnniMisura(getBigDecimal("NUM_ANNI_MISURA"));
		aModel.setNumMesiMisura(getBigDecimal("NUM_MESI_MISURA"));
		aModel.setNumGiorniMisura(getBigDecimal("NUM_GIORNI_MISURA"));
		aModel.setDataInizioMisura(getDate("DATA_INIZIO_MISURA"));
		aModel.setDataFineMisura(getDate("DATA_FINE_MISURA"));
		aModel.setChiaveAnnoFascicoloSius(getBigDecimal("CHIAVE_ANNO_FASCICOLO_SIUS"));
		aModel.setChiaveUfficioFascicoloSius(getString("CHIAVE_UFFICIO_FASCICOLO_SIUS"));
		aModel.setChiaveProgrFascicoloSius(getBigDecimal("CHIAVE_PROGR_FASCICOLO_SIUS"));
		aModel.setAnnoRegistro(getBigDecimal("ANNO_REGISTRO"));
		aModel.setNumeroRegistro(getBigDecimal("NUMERO_REGISTRO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setNote(getString("NOTE"));
		aModel.setDataScarcerazione(getDate("DATA_SCARCERAZIONE"));
		aModel.setDataIngressoIstituto(getDate("DATA_INGRESSO_ISTITUTO"));
		aModel.setCodTipoUfficioScarcerazione(getString("COD_TIPO_UFFICIO_SCARCERAZIONE"));
		aModel.setFlagUfficioInserimento(getString("FLAG_UFFICIO_INSERIMENTO"));
		// dario
		aModel.setDataInizioRevoca(getDate("DATA_INIZIO_REVOCA"));
		aModel.setNumAnniRevocaReclusione(getBigDecimal("NUM_ANNI_REVOCA_RECLUSIONE"));
		aModel.setNumMesiRevocaReclusione(getBigDecimal("NUM_MESI_REVOCA_RECLUSIONE"));
		aModel.setNumGiorniRevocaReclusione(getBigDecimal("NUM_GIORNI_REVOCA_RECLUSIONE"));
		aModel.setNumAnniRevocaArresto(getBigDecimal("NUM_ANNI_REVOCA_ARRESTO"));
		aModel.setNumMesiRevocaArresto(getBigDecimal("NUM_MESI_REVOCA_ARRESTO"));
		aModel.setNumGiorniRevocaArresto(getBigDecimal("NUM_GIORNI_REVOCA_ARRESTO"));
		aModel.setFlagPeriodoEspiato(getString("FLAG_PERIODO_ESPIATO"));

		aModel.setAnnoAltroTitolo(getBigDecimal("ANNO_ALTRO_TITOLO"));
		aModel.setNumAltroTitolo(getString("NUM_ALTRO_TITOLO"));
		aModel.setDataAltroTitolo(getDate("DATA_ALTRO_TITOLO"));
		aModel.setCodLuogoAltroTitolo(getString("COD_LUOGO_ALTRO_TITOLO"));
		aModel.setCodAutoritaAltroTitolo(getString("COD_AUTORITA_ALTRO_TITOLO"));

		aModel.setDescAutoritaAltroTitolo(getString("DESCR_TIPO_AUTORITA"));
		aModel.setDescLuogoAltroTitolo(getString("DESCR_LUOGO"));
		// DARIO
		aModel.setDescrChiaveUfficioFascicoloSius(getString("DESCUFSORV") + " di " + getString("DESCCOM"));
		aModel.setLegge(getString("LEGGE_MISURA"));

		aModel.setDataScadenzaProroga(getDate("DATA_SCADENZA_PROROGA"));
		aModel.setFlagDecisioneTribunale(getString("FLAG_DECISIONE_TRIBUNALE"));
		aModel.setCodTdsCompetente(getString("COD_TDS_COMPETENTE"));
		aModel.setFlagSituazione(getString("FLAG_SITUAZIONE"));

		aModel.setDescTdsCompetente(getString("DESCUFFTDS"));
		aModel.setDescSedeTdsCompetente(getString("DESCCOMTDS"));
		
		aModel.setDataEsecutivita(getDate("DATA_ESECUTIVITA")); // MEV_9
		
		// DL146/2013
		if (findColumn("COD_TIPO_DECISIONE_MA_AT")) {
			aModel.setCodTipoDecisioneMaAt(getString("COD_TIPO_DECISIONE_MA_AT"));
			aModel.setDescrTipoDecisioneMaAt(getString("descTipoDecisioneMaAt"));
			aModel.setCodTipoMisuraMaAt(getString("COD_TIPO_MISURA_MA_AT"));
			aModel.setDescrTipoMisuraMaAt(getString("descTipoMisuraMaAt"));
			aModel.setDataDecisioneMaAt(getDate("DATA_DECISIONE_MA_AT"));
			aModel.setChiaveAnnoFascicoloSiusMaAt(getBigDecimal("CHIAVE_ANNO_FAS_SIUS_MA_AT"));
			aModel.setChiaveProgrFascicoloSiusMaAt(getBigDecimal("CHIAVE_PROGR_FAS_SIUS_MA_AT"));
			aModel.setChiaveUfficioFascicoloSiusMaAt(getString("CHIAVE_UFF_FAS_SIUS_MA_AT"));
			aModel.setDescrChiaveUfficioFascicoloSiusMaAt(getString("descTipoUffMaAt"));
			aModel.setAnnoRegistroMaAt(getBigDecimal("ANNO_REGISTRO_MA_AT"));
			aModel.setNumeroRegistroMaAt(getBigDecimal("NUMERO_REGISTRO_MA_AT"));
		}

		if (findColumn("FL_FORMA_MISURA")) {
			aModel.setFlFormaMisura(getBigDecimal("FL_FORMA_MISURA"));
		}

		if (findColumn("DESCRIZIONE_COMUNITA")) {
			aModel.setDescrizioneComunita(getString("DESCRIZIONE_COMUNITA"));
		}

		return aModel;
	}

	public GenericModel getModelEvento() throws DAOException {
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

		// Add 20030713 By paolo
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
		
		return aModel;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		String lCondizioni = new String();

		lCondizioni += " AND ID_MISURA_ALTERNATIVA = " + aKey;

		return lCondizioni;
	}

	public String setCondizioniByAnnoProgr(BigDecimal anno, BigDecimal progr) {
		String lCondizioni = new String();

		lCondizioni += " AND CHIAVE_ANNO_FASCICOLO_SIUS = " + anno;
		lCondizioni += " AND CHIAVE_PROGR_FASCICOLO_SIUS = " + progr;

		return lCondizioni;
	}

	public boolean eventoCoRiRe(BigDecimal aIdEvento) throws DAOException {
		String lStatement = new String();
		/*
		 * STUB 14/04/2005 Nuova casistica per la scrittura della MISURA_ALTERNATIVA. lStatement +=
		 * "select count(*) as COUNT from EVENTO EVE, CG_REF_CODES ESITO, CG_REF_CODES MOTIVO "; lStatement +=
		 * "WHERE EVE.ID_EVENTO = '" + aIdEvento + "'"; lStatement +=
		 * " AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND EVE.COD_ESITO = ESITO.RV_LOW_VALUE) ";
		 * lStatement +=
		 * " AND (MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND EVE.COD_MOTIVO = MOTIVO.RV_LOW_VALUE  AND MOTIVO.RV_HIGH_VALUE IN ('C001','C002','C003','C004','C008') )"
		 * ;
		 */
		lStatement += "select count(*) as COUNT from EVENTO EVE, CG_REF_CODES ESITO, CG_REF_CODES MOTIVO, CG_REF_CODES INSERIMENTO ";
		lStatement += "WHERE EVE.ID_EVENTO = '" + aIdEvento + "'";
		lStatement += " AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND EVE.COD_ESITO = ESITO.RV_LOW_VALUE) ";
		lStatement += " AND (MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND EVE.COD_MOTIVO = MOTIVO.RV_LOW_VALUE ) ";
		lStatement += " AND (INSERIMENTO.RV_DOMAIN = 'INSERIMENTO_MA' AND MOTIVO.RV_HIGH_VALUE = INSERIMENTO.RV_LOW_VALUE ) ";

		setStatement(lStatement);

		this.start();

		BigDecimal lCount = null;

		if (this.next())
			lCount = this.getBigDecimal("COUNT");

		if (lCount.intValue() > 0)
			return true;
		else
			return false;
	}

	public String ricecaNaturaDecisione(BigDecimal aIdEvento) throws DAOException {
		String lStatement = new String();
		lStatement += "SELECT ESITO.RV_HIGH_VALUE NATURA_DECISIONE from EVENTO EVE, CG_REF_CODES ESITO ";
		lStatement += "WHERE EVE.ID_EVENTO = '" + aIdEvento + "'";
		lStatement += " AND ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND EVE.COD_ESITO = ESITO.RV_LOW_VALUE ";

		setStatement(lStatement);

		this.start();

		String lNaturaDecisione = new String();

		if (this.next())
			lNaturaDecisione = this.getString("NATURA_DECISIONE");

		return lNaturaDecisione;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		return lCondizioni;
	}

	public String setCondizioniByIdEvento(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " AND EVE_ID_EVENTO = " + aKey;

		return lCondizioni;
	}

	private String setOrderMisuraDesc() {
		String lCondizioni = " ORDER BY MA.DATA_DECISIONE DESC, MA.DATA_INSERIMENTO DESC";

		return lCondizioni;
	}

	private String setOrderEventoDesc() {
		String lCondizioni = " ORDER BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC";

		return lCondizioni;
	}

	protected String getSqlQueryEvento() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
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
		lStatement += " FLAG_VIDEO_SIUS  ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
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

	private boolean findColumn(String aValue) {
		try {
			mRs.findColumn(aValue);
		} catch (Exception sqex) {
			return false;
		}
		return true;
	}

}
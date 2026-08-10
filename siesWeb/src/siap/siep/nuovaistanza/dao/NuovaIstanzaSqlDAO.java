package siap.siep.nuovaistanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;

/**
 * Classe SqlDAO che rappresenta la tabella NuovaIstanza
 *
 * @version 1.0
 */
public class NuovaIstanzaSqlDAO extends SIAPSqlDAO {

	/*****************************************************************************
	 * Costruttore
	 *
	 * @param con
	 ****************************************************************************/
	public NuovaIstanzaSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountNuovaIstanza(NuovaIstanzaModel aModel) throws DAOException {

		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM NUOVA_ISTANZA WHERE";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		lStatement += lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountNuoveIstanzeByAnnoProgr(NuovaIstanzaModel aModel, int annoIni, int progrIni,
			int annoFine, int progrFine) throws DAOException {

		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM NUOVA_ISTANZA WHERE";

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioniByAnnoProgr(aModel, annoIni, progrIni, annoFine, progrFine);

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		lStatement += lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountNuoveIstanzeBySoggetto(NuovaIstanzaModel aModel, SoggettoModel aSogMod)
			throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM NUOVA_ISTANZA WHERE";

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioniBySoggetto(aModel, aSogMod);

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		lStatement += lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 *
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNuovaIstanzaPaged(NuovaIstanzaModel aModel, int aPage) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		lStatement += lCondizioni;

		// lStatement += " "+getOrderBy()+" ";
		lStatement += " " + getOrderByAnnoProgr() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************/
	/*****************************************************************************/
	public void ricercaFlagValNuovaIstanzaPaged(BigDecimal aId, int aPage) throws DAOException {
		String lStatement = new String("");

		// lStatement += " "+getOrderBy()+" ";
		lStatement = "SELECT evento.flag_documento_registrato VAL_FLAG from evento, nuova_istanza "
				+ "where nuova_istanza.eve_id_evento=evento.eve_id_evento and " + "evento.eve_id_evento = "
				+ aId + "and evento.id_evento = "
				+ "(select max (evento.id_evento) from evento where evento.eve_id_evento = " + aId + ")"
				+ "union SELECT evento.flag_documento_registrato VAL_FLAG from evento, nuova_istanza "
				+ "where nuova_istanza.eve_id_evento=evento.id_evento and evento.id_evento = " + aId;
		// lStatement += " "+getOrderByAnnoProgr()+" ";

		setStatement(lStatement);
	}

	/* ANNA*********************************************************************** */
	/*****************************************************************************/
	public void ricercaFlagValInoltroNuovaIstanza(BigDecimal aId) throws DAOException {
		String lStatement = new String("");
		lStatement = "SELECT evento.id_evento EVE_ID, evento.flag_documento_registrato VAL_FLAG from evento, nuova_istanza "
				+ "where nuova_istanza.eve_id_evento=evento.eve_id_evento and "
				+ "evento.cod_motivo='1001'and evento.eve_id_evento = " + aId
				+ " and evento.id_evento = (SELECT max(evento.id_evento) "
				+ "from evento where evento.cod_motivo='1001'and evento.eve_id_evento = " + aId + ")";
		setStatement(lStatement);
	}

	/* ANNA*********************************************************************** */
	/*****************************************************************************/
	public void ricercaFlagValDisposizioneNuovaIstanza(BigDecimal aId) throws DAOException {
		String lStatement = new String("");
		lStatement = "SELECT evento.id_evento EVE_ID, evento.flag_documento_registrato VAL_FLAG from evento, nuova_istanza "
				+ "where nuova_istanza.eve_id_evento=evento.eve_id_evento and "
				+ "evento.cod_motivo='1002'and evento.eve_id_evento = " + aId
				+ " and evento.id_evento = (SELECT max(evento.id_evento) "
				+ "from evento where evento.cod_motivo='1002'and evento.eve_id_evento = " + aId + ")";

		setStatement(lStatement);
	}

	/* ANNA*********************************************************************** */
	public void cancellaNoteDisposizioneNuovaIstanza(BigDecimal aId) throws DAOException {
		String lStatement = new String("");
		lStatement = "delete from campo_nota where campo_nota.eve_id_evento = " + aId;
		setStatement(lStatement);
	}

	/* ANNA*********************************************************************** */
	public void cancellaNotificheDisposizioneNuovaIstanza(BigDecimal aId) throws DAOException {
		String lStatement = new String("");
		lStatement = "delete from notifica where notifica.eve_id_evento = " + aId;
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagina
	 * passata in input
	 *
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNuoveIstanzeByAnnoProgrPaged(NuovaIstanzaModel aModel, int annoIni, int progrIni,
			int annoFine, int progrFine, int aPage) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioniByAnnoProgr(aModel, annoIni, progrIni, annoFine, progrFine);

		lStatement += lCondizioni;

		// lStatement += " "+getOrderBy()+" ";
		lStatement += " " + getOrderByAnnoProgr() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagina
	 * passata in input
	 *
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNuoveIstanzeBySoggettoPaged(NuovaIstanzaModel aModel, SoggettoModel aSogMod, int aPage)
			throws DAOException {
		String lStatement = new String("");

		// lStatement += getSqlQuery();
		lStatement += getSqlQueryBySoggetto();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioniBySoggettoFasc(aModel, aSogMod);

		lStatement += lCondizioni;

		lStatement += " " + getOrderBySoggetto() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNuovaIstanza(NuovaIstanzaModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 *
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNuovaIstanzaByKey(BigDecimal aIdNuovaIstanza) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += " AND ID_NUOVA_ISTANZA = " + aIdNuovaIstanza;
		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per l'Evento
	 *
	 * @param aIdEvento
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNuovaIstanzaByEveIdEvento(BigDecimal aIdEvento) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += " AND EVE_ID_EVENTO = " + aIdEvento;
		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave dell'ultima nuova istanza iscritta per il
	 * fascicolo
	 *
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaUltimaNuovaIstanzaByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryProva();

		// Aggiunge le where condition per chiave
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		// Aggiunge la where condition per ultima istanza
		// Att. se l'ultima nuova istanza inserita non risponde ai requisiti della getSqlQueryProva non
		// preleva nulla

		lSql += " AND ID_NUOVA_ISTANZA = (SELECT MAX(ID_NUOVA_ISTANZA) FROM NUOVA_ISTANZA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aIdFascicolo + ")";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 *
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNuovaIstanzaByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryProva();

		// Aggiunge le where condition per chiave
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 *
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNuovaIstanzaNonAnnullataByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryConEvento();

		// Aggiunge le where condition per chiave
		lSql += " AND NUOVA_ISTANZA.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		// Ticket#202311090129 — SIUS - Presa in carico da altra BDI (Urgente)
		// Si recuperano solo le istanza collegate ad eventi Validati
		// Aggiunge le where condition per evento non annullato
		// lSql += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO <> 'A' ";
		lSql += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// Ticket#202311090129 - FINE

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT ID_NUOVA_ISTANZA, COD_CONTENUTO," + " CODCONTENUTO.RV_MEANING DESC_CONTENUTO,"
				+ " DATA_ISTANZA, NOTE, " + " FLAG_PRESDEP, " + " SOGG_PRESENTANTE, "
				+ " SOGG_PRESENTANTE_IDENTIFICATO, AVV_ID_AVVOCATO_PRESENTANTE, COD_AUTORITA_MITTENTE,"
				+ " CODAUTORITAMITTENTE.RV_MEANING DESC_AUTORITA_MITTENTE," + " COD_SEDE_MITTENTE,"
				+ " CODSEDEMITTENTE.DESCRIZIONE DESC_SEDE_MITTENTE, DESCR_MITTENTE,"
				+ " AVV_ID_AVVOCATO, COD_ESITO," + " CODESITO.RV_MEANING DESC_ESITO,"
				+ " ANNO_REGISTRO, PROGR_REGISTRO, COD_TIPO_UFFICIO_DESTINATARIO,"
				+ " CODTIPOUFFICIODESTINATARIO.RV_MEANING DESC_TIPO_UFFICIO_DESTINATARIO,"
				+ " COD_LUOGO_DESTINATARIO," + " CODLUOGODESTINATARIO.DESCRIZIONE DESC_LUOGO_DESTINATARIO,"
				+ " COD_UFFICIO_DESTINATARIO, COD_STATO_ISTANZA,"
				+ " CODSTATOISTANZA.RV_MEANING DESC_STATO_ISTANZA," + " COD_OPERATORE_INSERIMENTO, "
				+ " DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, "
				+ " COD_UFFICIO_AGGIORNAMENTO, FAS_SIE_ID_FASCICOLO_SIEP, EVE_ID_EVENTO,  "
				+ " DATA_NOTIFICA_AVVOCATO, TIPO_AVVOCATO, TIPOAVVOCATO.RV_MEANING DESC_TIPO_AVVOCATO, "
				+ " NUOVA_ISTANZA.DATA_INOLTRO_PM " + " FROM NUOVA_ISTANZA " + ", CG_REF_CODES CODCONTENUTO"
				+ " ,CG_REF_CODES CODAUTORITAMITTENTE" + " ,  CG_REF_CODES CODESITO,"
				+ "   CG_REF_CODES CODSTATOISTANZA," + "	CG_REF_CODES CODTIPOUFFICIODESTINATARIO,"
				+ "   CG_REF_CODES TIPOAVVOCATO," + "	COMUNE CODSEDEMITTENTE,"
				+ "	COMUNE CODLUOGODESTINATARIO" + " WHERE "
				+ " (NUOVA_ISTANZA.COD_CONTENUTO = CODCONTENUTO.RV_LOW_VALUE AND CODCONTENUTO.RV_DOMAIN = 'CONTENUTO_ISTANZA' )"
				+ " AND (NUOVA_ISTANZA.COD_AUTORITA_MITTENTE = CODAUTORITAMITTENTE.RV_LOW_VALUE AND CODAUTORITAMITTENTE.RV_DOMAIN = 'MITTENTE_ISTANZA' ) "
				+ " AND (NUOVA_ISTANZA.COD_SEDE_MITTENTE = CODSEDEMITTENTE.COD_COMUNE) "
				+ " AND (NUOVA_ISTANZA.COD_ESITO = CODESITO.RV_LOW_VALUE AND CODESITO.RV_DOMAIN = 'ESITO_ISTANZA' ) "
				+ " AND (NUOVA_ISTANZA.COD_TIPO_UFFICIO_DESTINATARIO = CODTIPOUFFICIODESTINATARIO.RV_LOW_VALUE AND CODTIPOUFFICIODESTINATARIO.RV_DOMAIN = 'TIPO_UFFICIO' ) "
				+ " AND (NUOVA_ISTANZA.COD_LUOGO_DESTINATARIO = CODLUOGODESTINATARIO.COD_COMUNE ) "
				+ " AND (NUOVA_ISTANZA.COD_STATO_ISTANZA = CODSTATOISTANZA.RV_LOW_VALUE AND CODSTATOISTANZA.RV_DOMAIN = 'STATO_NUOVA_ISTANZA' )  "
				+ " AND (TIPOAVVOCATO.RV_DOMAIN='TIPO_AVVOCATO' AND NUOVA_ISTANZA.TIPO_AVVOCATO = TIPOAVVOCATO.RV_LOW_VALUE)";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQueryBySoggetto() {
		String lStatement = new String("");

		lStatement += " SELECT ID_NUOVA_ISTANZA, COD_CONTENUTO," + " CODCONTENUTO.RV_MEANING DESC_CONTENUTO,"
				+ " DATA_ISTANZA, NUOVA_ISTANZA.NOTE, " + " FLAG_PRESDEP, " + " SOGG_PRESENTANTE, "
				+ " SOGG_PRESENTANTE_IDENTIFICATO, AVV_ID_AVVOCATO_PRESENTANTE, COD_AUTORITA_MITTENTE,"
				+ " CODAUTORITAMITTENTE.RV_MEANING DESC_AUTORITA_MITTENTE," + " COD_SEDE_MITTENTE,"
				+ " CODSEDEMITTENTE.DESCRIZIONE DESC_SEDE_MITTENTE, DESCR_MITTENTE,"
				+ " AVV_ID_AVVOCATO, COD_ESITO," + " CODESITO.RV_MEANING DESC_ESITO,"
				+ " ANNO_REGISTRO, PROGR_REGISTRO, COD_TIPO_UFFICIO_DESTINATARIO,"
				+ " CODTIPOUFFICIODESTINATARIO.RV_MEANING DESC_TIPO_UFFICIO_DESTINATARIO,"
				+ " COD_LUOGO_DESTINATARIO," + " CODLUOGODESTINATARIO.DESCRIZIONE DESC_LUOGO_DESTINATARIO,"
				+ " COD_UFFICIO_DESTINATARIO, COD_STATO_ISTANZA,"
				+ " CODSTATOISTANZA.RV_MEANING DESC_STATO_ISTANZA,"
				+ " NUOVA_ISTANZA.COD_OPERATORE_INSERIMENTO, "
				+ " NUOVA_ISTANZA.DATA_INSERIMENTO, NUOVA_ISTANZA.COD_UFFICIO_INSERIMENTO, NUOVA_ISTANZA.COD_OPERATORE_AGGIORNAMENTO, NUOVA_ISTANZA.DATA_AGGIORNAMENTO, "
				+ " NUOVA_ISTANZA.COD_UFFICIO_AGGIORNAMENTO, NUOVA_ISTANZA.FAS_SIE_ID_FASCICOLO_SIEP, EVE_ID_EVENTO,  "
				+ " DATA_NOTIFICA_AVVOCATO, TIPO_AVVOCATO, TIPOAVVOCATO.RV_MEANING DESC_TIPO_AVVOCATO, "
				+ " NUOVA_ISTANZA.DATA_INOLTRO_PM " + " FROM NUOVA_ISTANZA " + ", FASCICOLO_SIEP"
				+ ", SOGGETTO" + ", CG_REF_CODES CODCONTENUTO" + " ,CG_REF_CODES CODAUTORITAMITTENTE"
				+ " ,  CG_REF_CODES CODESITO," + "   CG_REF_CODES CODSTATOISTANZA,"
				+ "	CG_REF_CODES CODTIPOUFFICIODESTINATARIO," + "   CG_REF_CODES TIPOAVVOCATO,"
				+ "	COMUNE CODSEDEMITTENTE," + "	COMUNE CODLUOGODESTINATARIO" + " WHERE "
				+ " (NUOVA_ISTANZA.COD_CONTENUTO = CODCONTENUTO.RV_LOW_VALUE AND CODCONTENUTO.RV_DOMAIN = 'CONTENUTO_ISTANZA' )"
				+ " AND (NUOVA_ISTANZA.COD_AUTORITA_MITTENTE = CODAUTORITAMITTENTE.RV_LOW_VALUE AND CODAUTORITAMITTENTE.RV_DOMAIN = 'MITTENTE_ISTANZA' ) "
				+ " AND (NUOVA_ISTANZA.COD_SEDE_MITTENTE = CODSEDEMITTENTE.COD_COMUNE) "
				+ " AND (NUOVA_ISTANZA.COD_ESITO = CODESITO.RV_LOW_VALUE AND CODESITO.RV_DOMAIN = 'ESITO_ISTANZA' ) "
				+ " AND (NUOVA_ISTANZA.COD_TIPO_UFFICIO_DESTINATARIO = CODTIPOUFFICIODESTINATARIO.RV_LOW_VALUE AND CODTIPOUFFICIODESTINATARIO.RV_DOMAIN = 'TIPO_UFFICIO' ) "
				+ " AND (NUOVA_ISTANZA.COD_LUOGO_DESTINATARIO = CODLUOGODESTINATARIO.COD_COMUNE ) "
				+ " AND (NUOVA_ISTANZA.COD_STATO_ISTANZA = CODSTATOISTANZA.RV_LOW_VALUE AND CODSTATOISTANZA.RV_DOMAIN = 'STATO_NUOVA_ISTANZA' )  "
				+ " AND (TIPOAVVOCATO.RV_DOMAIN='TIPO_AVVOCATO' AND NUOVA_ISTANZA.TIPO_AVVOCATO = TIPOAVVOCATO.RV_LOW_VALUE) "
				+ " AND (FASCICOLO_SIEP.ID_FASCICOLO_SIEP = NUOVA_ISTANZA.FAS_SIE_ID_FASCICOLO_SIEP) "
				+ " AND (SOGGETTO.ID_SOGGETTO = FASCICOLO_SIEP.SOG_ID_SOGGETTO) ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQueryProva() {
		String lStatement = new String("");

		lStatement += " SELECT ID_NUOVA_ISTANZA, COD_CONTENUTO," + " CODCONTENUTO.RV_MEANING DESC_CONTENUTO,"
				+ " DATA_ISTANZA, NOTE, " + " FLAG_PRESDEP, " + " SOGG_PRESENTANTE, "
				+ " SOGG_PRESENTANTE_IDENTIFICATO, AVV_ID_AVVOCATO_PRESENTANTE, COD_AUTORITA_MITTENTE,"
				+ " CODAUTORITAMITTENTE.RV_MEANING DESC_AUTORITA_MITTENTE," + " COD_SEDE_MITTENTE,"
				+ " CODSEDEMITTENTE.DESCRIZIONE DESC_SEDE_MITTENTE, DESCR_MITTENTE,"
				+ " AVV_ID_AVVOCATO, COD_ESITO," + " CODESITO.RV_MEANING DESC_ESITO,"
				+ " ANNO_REGISTRO, PROGR_REGISTRO, COD_TIPO_UFFICIO_DESTINATARIO,"
				+ " CODTIPOUFFICIODESTINATARIO.RV_MEANING DESC_TIPO_UFFICIO_DESTINATARIO,"
				+ " COD_LUOGO_DESTINATARIO," + " CODLUOGODESTINATARIO.DESCRIZIONE DESC_LUOGO_DESTINATARIO,"
				+ " COD_UFFICIO_DESTINATARIO, COD_STATO_ISTANZA,"
				+ " CODSTATOISTANZA.RV_MEANING DESC_STATO_ISTANZA," + " COD_OPERATORE_INSERIMENTO, "
				+ " DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, "
				+ " COD_UFFICIO_AGGIORNAMENTO, FAS_SIE_ID_FASCICOLO_SIEP, EVE_ID_EVENTO,  "
				+ " DATA_NOTIFICA_AVVOCATO, TIPO_AVVOCATO, TIPOAVVOCATO.RV_MEANING DESC_TIPO_AVVOCATO, "
				+ " DATA_INOLTRO_PM " + " FROM NUOVA_ISTANZA " + ", CG_REF_CODES CODCONTENUTO"
				+ " ,CG_REF_CODES CODAUTORITAMITTENTE" + " ,  CG_REF_CODES CODESITO,"
				+ "   CG_REF_CODES CODSTATOISTANZA," + "	CG_REF_CODES CODTIPOUFFICIODESTINATARIO,"
				+ "   CG_REF_CODES TIPOAVVOCATO," + "	COMUNE CODSEDEMITTENTE,"
				+ "	COMUNE CODLUOGODESTINATARIO" + " WHERE "
				+ " (NUOVA_ISTANZA.COD_CONTENUTO = CODCONTENUTO.RV_LOW_VALUE AND CODCONTENUTO.RV_DOMAIN = 'CONTENUTO_ISTANZA' )"
				+ " AND (NUOVA_ISTANZA.COD_AUTORITA_MITTENTE = CODAUTORITAMITTENTE.RV_LOW_VALUE AND CODAUTORITAMITTENTE.RV_DOMAIN = 'MITTENTE_ISTANZA' ) "
				+ " AND (NUOVA_ISTANZA.COD_SEDE_MITTENTE = CODSEDEMITTENTE.COD_COMUNE) "
				+ " AND (NUOVA_ISTANZA.COD_ESITO = CODESITO.RV_LOW_VALUE AND CODESITO.RV_DOMAIN = 'ESITO_ISTANZA' ) "
				+ " AND (NUOVA_ISTANZA.COD_TIPO_UFFICIO_DESTINATARIO = CODTIPOUFFICIODESTINATARIO.RV_LOW_VALUE AND CODTIPOUFFICIODESTINATARIO.RV_DOMAIN = 'TIPO_UFFICIO' ) "
				+ " AND (NUOVA_ISTANZA.COD_LUOGO_DESTINATARIO = CODLUOGODESTINATARIO.COD_COMUNE ) "
				+ " AND (NUOVA_ISTANZA.COD_STATO_ISTANZA = CODSTATOISTANZA.RV_LOW_VALUE AND CODSTATOISTANZA.RV_DOMAIN = 'STATO_NUOVA_ISTANZA' )  "
				+ " AND (TIPOAVVOCATO.RV_DOMAIN='TIPO_AVVOCATO' AND NUOVA_ISTANZA.TIPO_AVVOCATO = TIPOAVVOCATO.RV_LOW_VALUE)";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 *
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		NuovaIstanzaModel aModel = new NuovaIstanzaModel();

		aModel.setIdNuovaIstanza(getBigDecimal("ID_NUOVA_ISTANZA"));
		aModel.setCodContenuto(getString("COD_CONTENUTO"));
		aModel.setDescrContenuto(getString("DESC_CONTENUTO"));
		aModel.setDataIstanza(getDate("DATA_ISTANZA"));
		aModel.setNote(getString("NOTE"));
		aModel.setFlagPresdep(getString("FLAG_PRESDEP"));
		aModel.setSoggPresentante(getString("SOGG_PRESENTANTE"));
		aModel.setSoggPresentanteIdentificato(getString("SOGG_PRESENTANTE_IDENTIFICATO"));
		aModel.setAvvIdAvvocatoPresentante(getBigDecimal("AVV_ID_AVVOCATO_PRESENTANTE"));
		aModel.setCodAutoritaMittente(getString("COD_AUTORITA_MITTENTE"));
		aModel.setDescrAutoritaMittente(getString("DESC_AUTORITA_MITTENTE"));
		aModel.setCodSedeMittente(getString("COD_SEDE_MITTENTE"));
		aModel.setDescrSedeMittente(getString("DESC_SEDE_MITTENTE"));
		aModel.setDescrMittente(getString("DESCR_MITTENTE"));
		aModel.setAvvIdAvvocato(getBigDecimal("AVV_ID_AVVOCATO"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("DESC_ESITO"));
		aModel.setAnnoRegistro(getBigDecimal("ANNO_REGISTRO"));
		aModel.setProgrRegistro(getBigDecimal("PROGR_REGISTRO"));
		aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_TIPO_UFFICIO_DESTINATARIO"));
		aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setDescrLuogoDestinatario(getString("DESC_LUOGO_DESTINATARIO"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		// aModel.setDescrUfficioDestinatario (getString ("") );
		aModel.setCodStatoIstanza(getString("COD_STATO_ISTANZA"));
		aModel.setDescrStatoIstanza(getString("DESC_STATO_ISTANZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setOraInserimento(DateUtils.getDateToString(aModel.getDataInserimento(), "HH:mm:ss"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento (getString ("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento (getString ("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setDataNotificaAvvocato(getDate("DATA_NOTIFICA_AVVOCATO"));
		aModel.setTipoAvvocato(getString("TIPO_AVVOCATO"));
		aModel.setDescrTipoAvvocato(getString("DESC_TIPO_AVVOCATO"));

		aModel.setDataInoltroPM(getDate("DATA_INOLTRO_PM"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 *
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(NuovaIstanzaModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdNuovaIstanza() != null) {
			lCondizioni += " and ID_NUOVA_ISTANZA = " + aModel.getIdNuovaIstanza() + "";
		}
		if (aModel.getCodContenuto() != null && aModel.getCodContenuto().length() > 0) {
			lCondizioni += " and COD_CONTENUTO = '" + aModel.getCodContenuto() + "' ";
		}
		if (aModel.getDataIstanza() != null) {
			lCondizioni += " and to_char(DATA_ISTANZA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataIstanza(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getFlagPresdep() != null && aModel.getFlagPresdep().length() > 0) {
			lCondizioni += " and FLAG_PRESDEP = '" + aModel.getFlagPresdep() + "' ";
		}
		if (aModel.getSoggPresentante() != null && aModel.getSoggPresentante().length() > 0) {
			lCondizioni += " and SOGG_PRESENTANTE = '" + aModel.getSoggPresentante() + "' ";
		}
		if (aModel.getSoggPresentanteIdentificato() != null
				&& aModel.getSoggPresentanteIdentificato().length() > 0) {
			lCondizioni += " and SOGG_PRESENTANTE_IDENTIFICATO = '" + aModel.getSoggPresentanteIdentificato()
					+ "' ";
		}
		if (aModel.getAvvIdAvvocatoPresentante() != null) {
			lCondizioni += " and AVV_ID_AVVOCATO_PRESENTANTE = " + aModel.getAvvIdAvvocatoPresentante() + "";
		}
		if (aModel.getCodAutoritaMittente() != null && aModel.getCodAutoritaMittente().length() > 0) {
			lCondizioni += " and COD_AUTORITA_MITTENTE = '" + aModel.getCodAutoritaMittente() + "' ";
		}
		if (aModel.getCodSedeMittente() != null && aModel.getCodSedeMittente().length() > 0) {
			lCondizioni += " and COD_SEDE_MITTENTE = '" + aModel.getCodSedeMittente() + "' ";
		}
		if (aModel.getDescrMittente() != null && aModel.getDescrMittente().length() > 0) {
			lCondizioni += " and DESCR_MITTENTE = '" + aModel.getDescrMittente() + "' ";
		}
		if (aModel.getAvvIdAvvocato() != null) {
			lCondizioni += " and AVV_ID_AVVOCATO = " + aModel.getAvvIdAvvocato() + "";
		}
		if (aModel.getCodEsito() != null && aModel.getCodEsito().length() > 0) {
			lCondizioni += " and COD_ESITO = '" + aModel.getCodEsito() + "' ";
		}
		if (aModel.getAnnoRegistro() != null) {
			lCondizioni += " and ANNO_REGISTRO = " + aModel.getAnnoRegistro() + "";
		}
		if (aModel.getProgrRegistro() != null) {
			lCondizioni += " and PROGR_REGISTRO = " + aModel.getProgrRegistro() + "";
		}
		if (aModel.getCodTipoUfficioDestinatario() != null
				&& aModel.getCodTipoUfficioDestinatario().length() > 0) {
			lCondizioni += " and COD_TIPO_UFFICIO_DESTINATARIO = '" + aModel.getCodTipoUfficioDestinatario()
					+ "' ";
		}
		if (aModel.getCodLuogoDestinatario() != null && aModel.getCodLuogoDestinatario().length() > 0) {
			lCondizioni += " and COD_LUOGO_DESTINATARIO = '" + aModel.getCodLuogoDestinatario() + "' ";
		}
		if (aModel.getCodUfficioDestinatario() != null && aModel.getCodUfficioDestinatario().length() > 0) {
			lCondizioni += " and COD_UFFICIO_DESTINATARIO = '" + aModel.getCodUfficioDestinatario() + "' ";
		}
		if (aModel.getCodStatoIstanza() != null && aModel.getCodStatoIstanza().length() > 0) {
			lCondizioni += " and COD_STATO_ISTANZA = '" + aModel.getCodStatoIstanza() + "' ";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
		}
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 *
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioniByAnnoProgr(NuovaIstanzaModel aModel, int annoIni, int progrIni, int annoFine,
			int progrFine) {

		String lCondizioni = new String();

		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and NUOVA_ISTANZA.COD_UFFICIO_INSERIMENTO = '"
					+ aModel.getCodUfficioInserimento() + "' ";
		}

		if (aModel.getFlagPresdep() != null && aModel.getFlagPresdep().length() > 0) {
			lCondizioni += " and FLAG_PRESDEP = '" + aModel.getFlagPresdep() + "' ";
		}
		if (aModel.getCodStatoIstanza() != null && aModel.getCodStatoIstanza().length() > 0) {
			lCondizioni += " and COD_STATO_ISTANZA = '" + aModel.getCodStatoIstanza() + "' ";
		}

		lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP in ( ";
		lCondizioni += " select ID_FASCICOLO_SIEP from FASCICOLO_SIEP where ";

		if (annoIni > annoFine) {
			lCondizioni += " 1 > 2) ";
		}
		if (annoIni == annoFine) {
			lCondizioni += " (CHIAVE_ANNO = " + annoIni + " and CHIAVE_PROGR  between " + progrIni + " and "
					+ progrFine + "))  ";
		}
		if (annoIni < annoFine) {
			lCondizioni += " (CHIAVE_PROGR between 90001 and 99999) and (";
			lCondizioni += " (CHIAVE_ANNO = " + annoIni + " and CHIAVE_PROGR  >= " + progrIni + ") or ";
			lCondizioni += " (CHIAVE_ANNO between " + (annoIni + 1) + " and " + (annoFine - 1) + ") or ";
			lCondizioni += " (CHIAVE_ANNO = " + annoFine + " and CHIAVE_PROGR <= " + progrFine + ")))";
		}

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 *
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioniBySoggetto(NuovaIstanzaModel aModel, SoggettoModel aSogMod) {

		String lCondizioni = new String();

		if (aModel.getFlagPresdep() != null && aModel.getFlagPresdep().length() > 0) {
			lCondizioni += " and FLAG_PRESDEP = '" + aModel.getFlagPresdep() + "' ";
		}
		if (aModel.getCodStatoIstanza() != null && aModel.getCodStatoIstanza().length() > 0) {
			lCondizioni += " and COD_STATO_ISTANZA = '" + aModel.getCodStatoIstanza() + "' ";
		}

		lCondizioni += " and NUOVA_ISTANZA.FAS_SIE_ID_FASCICOLO_SIEP in ( ";
		lCondizioni += " select ID_FASCICOLO_SIEP from FASCICOLO_SIEP where SOG_ID_SOGGETTO in ( ";
		lCondizioni += " select ID_SOGGETTO from SOGGETTO where ";
		
		// 2026.07.30 - Errore durante i test per ticket Ticket#20260729015 sui nominativi con apostrofi
		/*lCondizioni += " COGNOME like '%" + aSogMod.getCognome() + "%' ";
		if (aSogMod.getNome() != null)
			lCondizioni += " and NOME like '%" + aSogMod.getNome() + "%' ";*/		
		
		lCondizioni += " COGNOME like '%" + StringUtils.convertSqlString(aSogMod.getCognome()) + "%' ";
		if (aSogMod.getNome() != null)
			lCondizioni += " and NOME like '%" + StringUtils.convertSqlString(aSogMod.getNome()) + "%' ";
		// 2026.07.30 - FINE 
		
		
		if (aSogMod.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " and trunc(DATA_NASCITA) = TO_DATE('"
					+ DateUtils.getDateToString(aSogMod.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		if (aSogMod.getCodComuneNascita() != null && aSogMod.getCodComuneNascita().length() > 1)
			lCondizioni += " and COD_COMUNE_NASCITA = '" + aSogMod.getCodComuneNascita() + "' ";
		lCondizioni += " )) ";

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 *
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioniBySoggettoFasc(NuovaIstanzaModel aModel, SoggettoModel aSogMod) {

		String lCondizioni = new String();

		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and NUOVA_ISTANZA.COD_UFFICIO_INSERIMENTO = '"
					+ aModel.getCodUfficioInserimento() + "' ";
		}

		if (aModel.getFlagPresdep() != null && aModel.getFlagPresdep().length() > 0) {
			lCondizioni += " and FLAG_PRESDEP = '" + aModel.getFlagPresdep() + "' ";
		}
		if (aModel.getCodStatoIstanza() != null && aModel.getCodStatoIstanza().length() > 0) {
			lCondizioni += " and COD_STATO_ISTANZA = '" + aModel.getCodStatoIstanza() + "' ";
		}

		/*
		 * lCondizioni += " and NUOVA_ISTANZA.FAS_SIE_ID_FASCICOLO_SIEP in ( "; lCondizioni +=
		 * " select ID_FASCICOLO_SIEP from FASCICOLO_SIEP where SOG_ID_SOGGETTO in ( "; lCondizioni +=
		 * " select ID_SOGGETTO from SOGGETTO where ";
		 */
		// 2026.07.30 - Errore durante i test per ticket Ticket#20260729015 sui nominativi con apostrofi
		/*
		lCondizioni += " and COGNOME like '" + aSogMod.getCognome() + "%' ";
		if (aSogMod.getNome() != null)
			lCondizioni += " and NOME like '" + aSogMod.getNome() + "%' ";
		 */
		
		lCondizioni += " and COGNOME like '" + StringUtils.convertSqlString(aSogMod.getCognome()) + "%' ";
		
		if (aSogMod.getNome() != null)
			lCondizioni += " and NOME like '" + StringUtils.convertSqlString(aSogMod.getNome()) + "%' ";
		// 2026.07.30 - FINE
		
		if (aSogMod.getNome() != null)
			// 2026.07.30 - Errore durante i test per ticket sui nominativi con apostrofi
			//lCondizioni += " and NOME like '" + aSogMod.getNome() + "%' ";
			lCondizioni += " and NOME like '" + StringUtils.convertSqlString(aSogMod.getNome()) + "%' ";
			// 2026.07.30 - FINE		
		
		if (aSogMod.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " and trunc(DATA_NASCITA) = TO_DATE('"
					+ DateUtils.getDateToString(aSogMod.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		if (aSogMod.getCodComuneNascita() != null && aSogMod.getCodComuneNascita().length() > 1)
			lCondizioni += " and COD_COMUNE_NASCITA = '" + aSogMod.getCodComuneNascita() + "' ";
		// lCondizioni += " )) ";

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 *
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdNuovaIstanza) {
		String lCondizioni = new String();

		lCondizioni += " and ID_NUOVA_ISTANZA = " + aIdNuovaIstanza;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodi per la costruzione della sezione order by
	 *
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		// orderBy = " ORDER BY ";
		return orderBy;
	}

	protected String getOrderByAnnoProgr() {
		String orderBy = new String("");
		orderBy = " ORDER BY FAS_SIE_ID_FASCICOLO_SIEP desc, ID_NUOVA_ISTANZA desc ";
		return orderBy;
	}

	protected String getOrderBySoggetto() {
		String orderBy = new String("");
		orderBy = " ORDER BY COGNOME, NOME, COD_COMUNE_NASCITA, DATA_NASCITA, FAS_SIE_ID_FASCICOLO_SIEP desc, ID_NUOVA_ISTANZA desc ";
		return orderBy;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQueryConEvento() {

		String lStatement = new String("");
		lStatement += " SELECT ID_NUOVA_ISTANZA, COD_CONTENUTO," + " CODCONTENUTO.RV_MEANING DESC_CONTENUTO,"
				+ " DATA_ISTANZA, NOTE, " + " FLAG_PRESDEP, " + " SOGG_PRESENTANTE, "
				+ " SOGG_PRESENTANTE_IDENTIFICATO, AVV_ID_AVVOCATO_PRESENTANTE, COD_AUTORITA_MITTENTE,"
				+ " CODAUTORITAMITTENTE.RV_MEANING DESC_AUTORITA_MITTENTE," + " COD_SEDE_MITTENTE,"
				+ " CODSEDEMITTENTE.DESCRIZIONE DESC_SEDE_MITTENTE, DESCR_MITTENTE,"
				+ " AVV_ID_AVVOCATO, NUOVA_ISTANZA.COD_ESITO," + " CODESITO.RV_MEANING DESC_ESITO,"
				+ " ANNO_REGISTRO, PROGR_REGISTRO, NUOVA_ISTANZA.COD_TIPO_UFFICIO_DESTINATARIO,"
				+ " CODTIPOUFFICIODESTINATARIO.RV_MEANING DESC_TIPO_UFFICIO_DESTINATARIO,"
				+ " NUOVA_ISTANZA.COD_LUOGO_DESTINATARIO,"
				+ " CODLUOGODESTINATARIO.DESCRIZIONE DESC_LUOGO_DESTINATARIO,"
				+ " NUOVA_ISTANZA.COD_UFFICIO_DESTINATARIO, COD_STATO_ISTANZA,"
				+ " CODSTATOISTANZA.RV_MEANING DESC_STATO_ISTANZA,"
				+ " NUOVA_ISTANZA.COD_OPERATORE_INSERIMENTO, "
				+ " NUOVA_ISTANZA.DATA_INSERIMENTO, NUOVA_ISTANZA.COD_UFFICIO_INSERIMENTO, NUOVA_ISTANZA.COD_OPERATORE_AGGIORNAMENTO, NUOVA_ISTANZA.DATA_AGGIORNAMENTO, "
				+ " NUOVA_ISTANZA.COD_UFFICIO_AGGIORNAMENTO, NUOVA_ISTANZA.FAS_SIE_ID_FASCICOLO_SIEP, NUOVA_ISTANZA.EVE_ID_EVENTO,  "
				+ " DATA_NOTIFICA_AVVOCATO, TIPO_AVVOCATO, TIPOAVVOCATO.RV_MEANING DESC_TIPO_AVVOCATO, "
				+ " DATA_INOLTRO_PM " + " FROM NUOVA_ISTANZA, EVENTO " + ", CG_REF_CODES CODCONTENUTO"
				+ " ,CG_REF_CODES CODAUTORITAMITTENTE" + " ,  CG_REF_CODES CODESITO,"
				+ "   CG_REF_CODES CODSTATOISTANZA," + "	CG_REF_CODES CODTIPOUFFICIODESTINATARIO,"
				+ "   CG_REF_CODES TIPOAVVOCATO," + "	COMUNE CODSEDEMITTENTE,"
				+ "	COMUNE CODLUOGODESTINATARIO" + " WHERE "
				+ " (NUOVA_ISTANZA.EVE_ID_EVENTO = EVENTO.ID_EVENTO) "
				+ " AND (NUOVA_ISTANZA.COD_CONTENUTO = CODCONTENUTO.RV_LOW_VALUE AND CODCONTENUTO.RV_DOMAIN = 'CONTENUTO_ISTANZA' )"
				+ " AND (NUOVA_ISTANZA.COD_AUTORITA_MITTENTE = CODAUTORITAMITTENTE.RV_LOW_VALUE AND CODAUTORITAMITTENTE.RV_DOMAIN = 'MITTENTE_ISTANZA' ) "
				+ " AND (NUOVA_ISTANZA.COD_SEDE_MITTENTE = CODSEDEMITTENTE.COD_COMUNE) "
				+ " AND (NUOVA_ISTANZA.COD_ESITO = CODESITO.RV_LOW_VALUE AND CODESITO.RV_DOMAIN = 'ESITO_ISTANZA' ) "
				+ " AND (NUOVA_ISTANZA.COD_TIPO_UFFICIO_DESTINATARIO = CODTIPOUFFICIODESTINATARIO.RV_LOW_VALUE AND CODTIPOUFFICIODESTINATARIO.RV_DOMAIN = 'TIPO_UFFICIO' ) "
				+ " AND (NUOVA_ISTANZA.COD_LUOGO_DESTINATARIO = CODLUOGODESTINATARIO.COD_COMUNE ) "
				+ " AND (NUOVA_ISTANZA.COD_STATO_ISTANZA = CODSTATOISTANZA.RV_LOW_VALUE AND CODSTATOISTANZA.RV_DOMAIN = 'STATO_NUOVA_ISTANZA' )  "
				+ " AND (TIPOAVVOCATO.RV_DOMAIN='TIPO_AVVOCATO' AND NUOVA_ISTANZA.TIPO_AVVOCATO = TIPOAVVOCATO.RV_LOW_VALUE)";
		return lStatement;
	}

}
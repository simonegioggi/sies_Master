package siap.siep.scambiosanzione.dao;

/**
* <p>Title: ScambioSanzioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella ScambioSanzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;

public class ScambioSanzioneSqlDAO extends SIAPSqlDAO {
	/*****************************************************************************
	 * Costruttore
	 *
	 * @param con
	 ****************************************************************************/
	public ScambioSanzioneSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountScambioSanzione(ScambioSanzioneModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM SCAMBIO_SANZIONE ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

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
	public void ricercaScambioSanzionePaged(ScambioSanzioneModel aModel, int aPage) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		lStatement += " AND EVENTO.flag_documento_registrato='S' "; // 10/04/2015

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " AND " + lCondizioni;

		lStatement += " " + getOrderBy() + " ";

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
	public void ricercaScambioSanzione(ScambioSanzioneModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += " AND EVENTO.flag_documento_registrato='S' "; // 10/04/2015

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " AND " + lCondizioni;

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
	public void ricercaScambioSanzioneByKey(BigDecimal aIdScambioSanzione) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += " AND EVENTO.flag_documento_registrato='S' "; // 10/04/2015

		// Aggiunge le where condition per chiave
		lSql += " AND " + setCondizioniByKey(aIdScambioSanzione);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave (pene Pecuniarie)
	 *
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaScambioSanzioneByKeyXRichConv(BigDecimal aIdScambioSanzione) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryXRichConv();

		// Aggiunge le where condition per chiave
		lSql += " AND " + setCondizioniByKey(aIdScambioSanzione);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca di ScambioSanzione e RichiestaConversione in base ai dati specificati nel
	 * model di Input
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaScambioSanzioneRichConv(String TipoDec, BigDecimal aFascicolo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryPenPec();

		lSql += " AND SCAMBIO_SANZIONE.COD_TIPO_DECISIONE = " + TipoDec;
		lSql += " AND RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;
		lSql += " AND RICHIESTA_CONVERSIONE.DATA_DEPOSIT0 IS NOT NULL";
		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaScambioSanzioneByEveIdEvento(BigDecimal aIdEvento) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += " AND EVENTO.flag_documento_registrato='S' "; // 10/04/2015

		// Aggiunge le where condition per chiave
		lSql += " AND SCAMBIO_SANZIONE.EVE_ID_EVENTO = " + aIdEvento;

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaScambioSanzioneByEveIdEventoNoCtrlnValid(BigDecimal aIdEvento) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();
		// lSql+=" AND EVENTO.flag_documento_registrato='N' ";

		// Aggiunge le where condition per chiave
		lSql += " AND SCAMBIO_SANZIONE.EVE_ID_EVENTO = " + aIdEvento;

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

		lStatement += " SELECT " + "ID_SCAMBIO_SANZIONE, " + "COD_TIPO_DECISIONE, "
				+ "D_TIPO_DECISIONE.RV_MEANING DESCR_TIPO_DECISIONE, " + "COD_NATURA_SANZIONE, "
				+ "D_NATURA_SANZIONE.RV_MEANING DESCR_NATURA_SANZIONE, " + "COD_TIPO_SANZIONE, "
				+ "D_TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE, " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "NOTE, " + "ANNO_REGISTRO, " + "NUMERO_REGISTRO, " + "CHIAVE_ANNO_FASCICOLO_SIUS, "
				+ "CHIAVE_PROGR_FASCICOLO_SIUS, " + "COD_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_TIPO_UFFICIO DESCR_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_COMUNE COMUNE_UFFICIO_SORVEGLIANZA, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_TIPO_UFFICIO DESCR_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_COMUNE COMUNE_UFFICIO_EMITTENTE, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_INSERIMENTO, " + "SCAMBIO_SANZIONE.DATA_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_INSERIMENTO, "
				+ "D_UFF_INS.DESCR_TIPO_UFFICIO DESCR_UFFICIO_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_AGGIORNAMENTO, " + "SCAMBIO_SANZIONE.DATA_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_AGGIORNAMENTO, "
				+ "D_UFF_AGG.DESCR_TIPO_UFFICIO DESCR_UFFICIO_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.DATA_EMISSIONE, " + "SCAMBIO_SANZIONE.EVE_ID_EVENTO, "
				+ "SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP, " +
				// conversione della sanzione sostitutiva paolo c. 3/3/2008
				"NUM_GIORNI_RECLUSIONE, " + "NUM_MESI_RECLUSIONE, " + "NUM_ANNI_RECLUSIONE, "
				+ "NUM_GIORNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_ANNI_ARRESTO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM EVENTO, SCAMBIO_SANZIONE ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_DECISIONE ON (COD_TIPO_DECISIONE = D_TIPO_DECISIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_DECISIONE.RV_DOMAIN='TIPO_PROVVEDIMENTO' )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_NATURA_SANZIONE ON (COD_NATURA_SANZIONE = D_NATURA_SANZIONE.RV_LOW_VALUE"
				+ " AND D_NATURA_SANZIONE.RV_DOMAIN = 'NATURA_DECISIONE')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_SANZIONE ON (COD_TIPO_SANZIONE = D_TIPO_SANZIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_SANZIONE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_SORV ON (COD_UFFICIO_SORVEGLIANZA = D_UFF_SORV.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_EM ON (COD_UFFICIO_EMITTENTE = D_UFF_EM.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_INS ON (COD_UFFICIO_INSERIMENTO = D_UFF_INS.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_AGG ON (COD_UFFICIO_AGGIORNAMENTO = D_UFF_AGG.COD_UFFICIO)";

		// lStatement += " WHERE scambio_sanzione.eve_id_evento = EVENTO.id_evento and
		// EVENTO.flag_documento_registrato='S'";
		lStatement += " WHERE scambio_sanzione.eve_id_evento = EVENTO.id_evento ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query di ricercaScambioSanzioneByKeyXRichConv
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQueryXRichConv() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_SCAMBIO_SANZIONE, " + "COD_TIPO_DECISIONE, "
				+ "D_TIPO_DECISIONE.RV_MEANING DESCR_TIPO_DECISIONE, " + "COD_NATURA_SANZIONE, "
				+ "D_NATURA_SANZIONE.RV_MEANING DESCR_NATURA_SANZIONE, " + "COD_TIPO_SANZIONE, "
				+ "D_TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE, " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "NOTE, " + "ANNO_REGISTRO, " + "NUMERO_REGISTRO, " + "CHIAVE_ANNO_FASCICOLO_SIUS, "
				+ "CHIAVE_PROGR_FASCICOLO_SIUS, " + "COD_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_TIPO_UFFICIO DESCR_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_COMUNE COMUNE_UFFICIO_SORVEGLIANZA, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_TIPO_UFFICIO DESCR_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_COMUNE COMUNE_UFFICIO_EMITTENTE, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_INSERIMENTO, " + "SCAMBIO_SANZIONE.DATA_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_INSERIMENTO, "
				+ "D_UFF_INS.DESCR_TIPO_UFFICIO DESCR_UFFICIO_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_AGGIORNAMENTO, " + "SCAMBIO_SANZIONE.DATA_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_AGGIORNAMENTO, "
				+ "D_UFF_AGG.DESCR_TIPO_UFFICIO DESCR_UFFICIO_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.DATA_EMISSIONE, " + "SCAMBIO_SANZIONE.EVE_ID_EVENTO, "
				+ "SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP, " +
				// conversione della sanzione sostitutiva paolo c. 3/3/2008
				"NUM_GIORNI_RECLUSIONE, " + "NUM_MESI_RECLUSIONE, " + "NUM_ANNI_RECLUSIONE, "
				+ "NUM_GIORNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_ANNI_ARRESTO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM EVENTO, SCAMBIO_SANZIONE ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_DECISIONE ON (COD_TIPO_DECISIONE = D_TIPO_DECISIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_DECISIONE.RV_DOMAIN='TIPO_PROVVEDIMENTO' )";
		// Ticket#20190913017 — Minori SIEP - SELEZIONE PROVVEDIMENTO DELLA SORVEGLIANZA DALLA LISTA -
		// conversione pene pecuniarie: aggiunta OR condition per estrarre la descr della NATURA SANZIONE
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_NATURA_SANZIONE ON ((COD_NATURA_SANZIONE ="
				+ " D_NATURA_SANZIONE.RV_LOW_VALUE OR COD_NATURA_SANZIONE ="
				+ " D_NATURA_SANZIONE.RV_HIGH_VALUE)" +
				// lStatement += " LEFT OUTER JOIN CG_REF_CODES D_NATURA_SANZIONE ON (COD_NATURA_SANZIONE =
				// D_NATURA_SANZIONE.RV_LOW_VALUE" +
				" AND D_NATURA_SANZIONE.RV_DOMAIN = 'ESITO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_SANZIONE ON (COD_TIPO_SANZIONE = D_TIPO_SANZIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_SANZIONE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_SORV ON (COD_UFFICIO_SORVEGLIANZA = D_UFF_SORV.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_EM ON (COD_UFFICIO_EMITTENTE = D_UFF_EM.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_INS ON (COD_UFFICIO_INSERIMENTO = D_UFF_INS.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_AGG ON (COD_UFFICIO_AGGIORNAMENTO = D_UFF_AGG.COD_UFFICIO)";

		lStatement += " WHERE scambio_sanzione.eve_id_evento = EVENTO.id_evento and EVENTO.flag_documento_registrato='S'";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query di ricercaScambioSanzioneRichConv
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQueryPenPec() {
		String lStatement = new String("");

		lStatement += " SELECT " + "SCAMBIO_SANZIONE.ID_SCAMBIO_SANZIONE, "
				+ "SCAMBIO_SANZIONE.COD_TIPO_DECISIONE, "
				+ "D_TIPO_DECISIONE.RV_MEANING DESCR_TIPO_DECISIONE, "
				+ "SCAMBIO_SANZIONE.COD_NATURA_SANZIONE, "
				+ "D_NATURA_SANZIONE.RV_MEANING DESCR_NATURA_SANZIONE, "
				+ "SCAMBIO_SANZIONE.COD_TIPO_SANZIONE, " + "D_TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE, "
				+ "SCAMBIO_SANZIONE.DATA_INIZIO, " + "SCAMBIO_SANZIONE.DATA_FINE, "
				+ "SCAMBIO_SANZIONE.NOTE, " + "SCAMBIO_SANZIONE.ANNO_REGISTRO, "
				+ "SCAMBIO_SANZIONE.NUMERO_REGISTRO, " + "SCAMBIO_SANZIONE.CHIAVE_ANNO_FASCICOLO_SIUS, "
				+ "SCAMBIO_SANZIONE.CHIAVE_PROGR_FASCICOLO_SIUS, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_TIPO_UFFICIO DESCR_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_COMUNE COMUNE_UFFICIO_SORVEGLIANZA, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_TIPO_UFFICIO DESCR_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_COMUNE COMUNE_UFFICIO_EMITTENTE, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_INSERIMENTO, " + "SCAMBIO_SANZIONE.DATA_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_INSERIMENTO, "
				+ "D_UFF_INS.DESCR_TIPO_UFFICIO DESCR_UFFICIO_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_AGGIORNAMENTO, " + "SCAMBIO_SANZIONE.DATA_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_AGGIORNAMENTO, "
				+ "D_UFF_AGG.DESCR_TIPO_UFFICIO DESCR_UFFICIO_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.DATA_EMISSIONE, " + "SCAMBIO_SANZIONE.EVE_ID_EVENTO, "
				+ "SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP, " +
				// conversione della sanzione sostitutiva paolo c. 3/3/2008
				"SCAMBIO_SANZIONE.NUM_GIORNI_RECLUSIONE, " + "SCAMBIO_SANZIONE.NUM_MESI_RECLUSIONE, "
				+ "SCAMBIO_SANZIONE.NUM_ANNI_RECLUSIONE, " + "SCAMBIO_SANZIONE.NUM_GIORNI_ARRESTO, "
				+ "SCAMBIO_SANZIONE.NUM_MESI_ARRESTO, " + "SCAMBIO_SANZIONE.NUM_ANNI_ARRESTO, " +
				// aggiungo campi di RichiestaConversione
				"RICHIESTA_CONVERSIONE.ID_RICHIESTA_CONVERSIONE, "
				+ "RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "RICHIESTA_CONVERSIONE.DATA_DEPOSITO, " + "RICHIESTA_CONVERSIONE.EVE_ID_EVENTO, "
				+ "RICHIESTA_CONVERSIONE.DURATA_ESITO_ANNI, " + "RICHIESTA_CONVERSIONE.DURATA_ESITO_MESI, "
				+ "RICHIESTA_CONVERSIONE.DURATA_ESITO_GIORNI, " + "RICHIESTA_CONVERSIONE.NUMERO_RATE, "
				+ "RICHIESTA_CONVERSIONE.VALORE_RATA, " + "RICHIESTA_CONVERSIONE.VALORE_ULTIMA_RATA, "
				+ "RICHIESTA_CONVERSIONE.DATA_INIZIO_PAGAMENTO, "
				+ "RICHIESTA_CONVERSIONE.NUMERO_GIORNI_INIZIO_PAGAMENTO ";

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM EVENTO, SCAMBIO_SANZIONE, RICHIESTA_CONVERSIONE ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_DECISIONE ON (SCAMBIO_SANZIONE.COD_TIPO_DECISIONE = D_TIPO_DECISIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_DECISIONE.RV_DOMAIN='TIPO_PROVVEDIMENTO' )";
		// Ticket#20190913017 — Minori SIEP - SELEZIONE PROVVEDIMENTO DELLA SORVEGLIANZA DALLA LISTA -
		// conversione pene pecuniarie: aggiunta OR condition per estrarre la descr della NATURA SANZIONE
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_NATURA_SANZIONE ON ((SCAMBIO_SANZIONE.COD_NATURA_SANZIONE ="
				+ " D_NATURA_SANZIONE.RV_LOW_VALUE OR SCAMBIO_SANZIONE.COD_NATURA_SANZIONE ="
				+ " D_NATURA_SANZIONE.RV_HIGH_VALUE)"
				// lStatement += " LEFT OUTER JOIN CG_REF_CODES D_NATURA_SANZIONE ON
				// (SCAMBIO_SANZIONE.COD_NATURA_SANZIONE = D_NATURA_SANZIONE.RV_LOW_VALUE"
				+ " AND D_NATURA_SANZIONE.RV_DOMAIN = 'ESITO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_SANZIONE ON (SCAMBIO_SANZIONE.COD_TIPO_SANZIONE = D_TIPO_SANZIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_SANZIONE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_SORV ON (SCAMBIO_SANZIONE.COD_UFFICIO_SORVEGLIANZA = D_UFF_SORV.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_EM ON (SCAMBIO_SANZIONE.COD_UFFICIO_EMITTENTE = D_UFF_EM.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_INS ON (SCAMBIO_SANZIONE.COD_UFFICIO_INSERIMENTO = D_UFF_INS.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_AGG ON (SCAMBIO_SANZIONE.COD_UFFICIO_AGGIORNAMENTO = D_UFF_AGG.COD_UFFICIO)";

		lStatement += " WHERE";
		lStatement += " SCAMBIO_SANZIONE.EVE_ID_EVENTO = EVENTO.ID_EVENTO AND";
		lStatement += " SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP = RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP AND";
		lStatement += " EVENTO.FLAG_DOCUMENTO_REGISTRATO ='S'";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 *
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		ScambioSanzioneModel aModel = new ScambioSanzioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdScambioSanzione(getBigDecimal("ID_SCAMBIO_SANZIONE"));
		aModel.setCodTipoDecisione(getString("COD_TIPO_DECISIONE"));
		aModel.setCodNaturaSanzione(getString("COD_NATURA_SANZIONE"));
		aModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setNote(getString("NOTE"));
		aModel.setAnnoRegistro(getBigDecimal("ANNO_REGISTRO"));
		aModel.setNumeroRegistro(getBigDecimal("NUMERO_REGISTRO"));
		aModel.setChiaveAnnoFascicoloSius(getBigDecimal("CHIAVE_ANNO_FASCICOLO_SIUS"));
		aModel.setChiaveProgrFascicoloSius(getBigDecimal("CHIAVE_PROGR_FASCICOLO_SIUS"));
		aModel.setCodUfficioSorveglianza(getString("COD_UFFICIO_SORVEGLIANZA"));
		aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		aModel.setDescrTipoDecisione(getString("DESCR_TIPO_DECISIONE"));
		aModel.setDescrNaturaSanzione(getString("DESCR_NATURA_SANZIONE"));
		aModel.setDescrTipoSanzione(getString("DESCR_TIPO_SANZIONE"));
		aModel.setDescrUfficioSorveglianza(getString("DESCR_UFFICIO_SORVEGLIANZA"));
		aModel.setComuneUfficioSorveglianza(getString("COMUNE_UFFICIO_SORVEGLIANZA"));
		aModel.setDescrUfficioEmittente(getString("DESCR_UFFICIO_EMITTENTE"));
		aModel.setComuneUfficioEmittente(getString("COMUNE_UFFICIO_EMITTENTE"));
		aModel.setDescrUfficioInserimento(getString("DESCR_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioAggiornamento(getString("DESCR_UFFICIO_AGGIORNAMENTO"));
		// conversione della sanzione sostitutiva paolo c. 3/3/2008
		aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
		aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
		aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
		aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
		aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
		aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 *
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(ScambioSanzioneModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdScambioSanzione() != null) {
			lCondizioni += " and ID_SCAMBIO_SANZIONE = " + aModel.getIdScambioSanzione() + "";
		}
		if (aModel.getCodTipoDecisione() != null && aModel.getCodTipoDecisione().length() > 0) {
			lCondizioni += " and COD_TIPO_DECISIONE = '" + aModel.getCodTipoDecisione() + "' ";
		}
		if (aModel.getCodNaturaSanzione() != null && aModel.getCodNaturaSanzione().length() > 0) {
			lCondizioni += " and COD_NATURA_SANZIONE = '" + aModel.getCodNaturaSanzione() + "' ";
		}
		if (aModel.getCodTipoSanzione() != null && aModel.getCodTipoSanzione().length() > 0) {
			lCondizioni += " and COD_TIPO_SANZIONE = '" + aModel.getCodTipoSanzione() + "' ";
		}
		if (aModel.getDataInizio() != null) {
			lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizio(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFine() != null) {
			lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFine(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getAnnoRegistro() != null) {
			lCondizioni += " and ANNO_REGISTRO = " + aModel.getAnnoRegistro() + "";
		}
		if (aModel.getNumeroRegistro() != null) {
			lCondizioni += " and NUMERO_REGISTRO = " + aModel.getNumeroRegistro() + "";
		}
		if (aModel.getChiaveAnnoFascicoloSius() != null) {
			lCondizioni += " and CHIAVE_ANNO_FASCICOLO_SIUS = " + aModel.getChiaveAnnoFascicoloSius() + "";
		}
		if (aModel.getChiaveProgrFascicoloSius() != null) {
			lCondizioni += " and CHIAVE_PROGR_FASCICOLO_SIUS = " + aModel.getChiaveProgrFascicoloSius() + "";
		}
		if (aModel.getCodUfficioSorveglianza() != null && aModel.getCodUfficioSorveglianza().length() > 0) {
			lCondizioni += " and COD_UFFICIO_SORVEGLIANZA = '" + aModel.getCodUfficioSorveglianza() + "' ";
		}
		if (aModel.getCodUfficioEmittente() != null && aModel.getCodUfficioEmittente().length() > 0) {
			lCondizioni += " and COD_UFFICIO_EMITTENTE = '" + aModel.getCodUfficioEmittente() + "' ";
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
		if (aModel.getDataEmissione() != null) {
			lCondizioni += " and to_char(DATA_EMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataEmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 *
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdScambioSanzione) {
		String lCondizioni = new String();

		lCondizioni += " and ID_SCAMBIO_SANZIONE = " + aIdScambioSanzione;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 *
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		orderBy = " ORDER BY SCAMBIO_SANZIONE.DATA_INSERIMENTO DESC";
		return orderBy;
	}

	/*****************************************************************************
	 * Imposta la condizione di order by per la ricerca
	 *
	 *****************************************************************************/
	public void ricercaByIdFascicoloNaturaTipo(BigDecimal aFascicolo, String[] aTipoDecisone,
			String[] aNaturaSanzione, String[] aTipoSanzione) {
		String lSql = getSqlQuery();

		lSql += " AND EVENTO.flag_documento_registrato='S' "; // 10/04/2015
		lSql += " AND SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;

		if (aTipoDecisone != null && aTipoDecisone.length > 0) {
			lSql += " AND COD_TIPO_DECISIONE IN (";
			for (int i = 0; i < aTipoDecisone.length; i++) {
				lSql += "'" + aTipoDecisone[i] + "'";
				if (aTipoDecisone.length > 1 && i < aTipoDecisone.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		if (aNaturaSanzione != null && aNaturaSanzione.length > 0) {
			lSql += " AND COD_NATURA_SANZIONE IN (";
			for (int i = 0; i < aNaturaSanzione.length; i++) {
				lSql += "'" + aNaturaSanzione[i] + "'";
				if (aNaturaSanzione.length > 1 && i < aNaturaSanzione.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		if (aTipoSanzione != null && aTipoSanzione.length > 0) {
			lSql += " AND COD_TIPO_SANZIONE IN (";
			for (int i = 0; i < aTipoSanzione.length; i++) {
				lSql += "'" + aTipoSanzione[i] + "'";
				if (aTipoSanzione.length > 1 && i < aTipoSanzione.length - 1)
					lSql += ",";
			}
			lSql += ")";
		}

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaScambioSanzionePerStatoEsecuzione(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery() + " AND EVENTO.flag_documento_registrato='S' " + // 10/04/2015
				" AND SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey +

				" ORDER BY EVENTO.DATA_EMISSIONE DESC ,EVENTO.DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	public void ricercaByIdFascicolo(BigDecimal aFascicolo) {
		String lSql = getSqlQuery();

		lSql += " AND EVENTO.flag_documento_registrato='S' "; // 10/04/2015

		lSql += " AND SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo che verifica l'esistenza di evento, per inserimento SS. ( Sanzioni Sostitutive ).
	 * <p>
	 *
	 * @param aIdEvento
	 *            Id dell'evento
	 * @return valore booleano per stato logico.
	 * @throws DAOException
	 *             propaga errore di eccezione
	 */
	public boolean eventoSS(BigDecimal aIdEvento) throws DAOException {
		String lStatement = new String();

		lStatement += "SELECT COUNT(*) AS COUNT " + " FROM EVENTO EVE, " + "      CG_REF_CODES ESITO, "
				+ "      CG_REF_CODES MOTIVO, " + "      CG_REF_CODES INSERIMENTO "
				+ " WHERE EVE.ID_EVENTO = '" + aIdEvento + "'"
				+ "   AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND EVE.COD_ESITO = ESITO.RV_LOW_VALUE) "
				+ "   AND (MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND EVE.COD_MOTIVO = MOTIVO.RV_LOW_VALUE ) "
				+ "   AND (INSERIMENTO.RV_DOMAIN = 'INSERIMENTO_SS' AND MOTIVO.RV_HIGH_VALUE = INSERIMENTO.RV_LOW_VALUE ) ";

		super.setStatement(lStatement);

		super.start();

		int lCount = 0;

		if (super.next())
			lCount = super.getInt("COUNT");

		if (lCount > 0)
			return true;
		else
			return false;
	}

	/**
	 * Esegue l'individuazione del valore natura decisione, per un evento, attraverso il valore contenuto del
	 * campo cod_esito, opportunamente relazionato con ESITO_PROVVEDIMENTO della CG_REF_CODES, prelevando il
	 * valore corrispondente contenuto nel campo RV_HIGH_VALUE. Tale valore viene utilizzato nel dominio delle
	 * sanzioni sostitutive, per inserimento del COD_NATURA_DECISIONE in scambio_sanzione.
	 * <p>
	 *
	 * @param aIdEvento
	 *            id dell'evento.
	 * @return stringa contenente il valore NATURA_DECISIONE.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public String getNaturaDecisioneSS(BigDecimal aIdEvento) throws DAOException {
		String lStatement = new String();

		lStatement += "SELECT ESITO.RV_HIGH_VALUE NATURA_DECISIONE FROM EVENTO EVE, CG_REF_CODES ESITO "
				+ " WHERE EVE.ID_EVENTO = '" + aIdEvento + "'"
				+ "   AND ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND EVE.COD_ESITO = ESITO.RV_LOW_VALUE ";

		super.setStatement(lStatement);

		super.start();

		String lNaturaDecisione = new String();

		if (super.next())
			lNaturaDecisione = super.getString("NATURA_DECISIONE");

		return lNaturaDecisione;
	}
}
package siap.siep.istruttoriacumulo.dao;

/**
* <p>Title: IstruttoriaCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.model.EventoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

public class IstruttoriaCumuloSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 *
	 * @param con
	 ****************************************************************************/
	public IstruttoriaCumuloSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountIstruttoriaCumulo(IstruttoriaCumuloModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM ISTRUTTORIA_CUMULO ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

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
	public void getCountIstruttoriaPerTitoloCumulato(IstruttoriaCumuloModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM ISTRUTTORIA_CUMULO I  "
				+ " INNER JOIN TITOLO_CUMULATO T "
				+ " ON T.ISTR_ID_ISTRUTTORIA_CUMULO = I.ID_ISTRUTTORIA_CUMULO ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizionePerTitoloCumulato(aModel.getProvvedimentoCumulo());

		// Condizione ufficio di appartenenza
		lStatement += " WHERE I.CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' ";

		// Aggiunte le condizioni di ricerca.
		if (lCondizioni.trim().length() > 3)
			lStatement += lCondizioni;

		// Imposta lo statement da eseguire
		// siesLogger.info("-XX- statement = "+ lStatement);
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountIstruttoriaPerFasSiep(FascicoloSiepModel aModel, String ufficio) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM ISTRUTTORIA_CUMULO I  "
				+ " INNER JOIN TITOLO_CUMULATO T  on I.ID_ISTRUTTORIA_CUMULO = T.ISTR_ID_ISTRUTTORIA_CUMULO "
				+ " INNER JOIN PROCEDIMENTO_CUMULATO P  on P.TIT_ID_TITOLO_CUMULATO = T.ID_TITOLO_CUMULATO "
				+ " INNER JOIN FASCICOLO_SIEP F  on F.CHIAVE_ANNO = P.CHIAVE_ANNO_FAS_CUMULATO and "
				+ "F.CHIAVE_PROGR = P.CHIAVE_PROGR_FAS_CUMULATO and "
				+ "F.CHIAVE_PROGR = P.CHIAVE_PROGR_FAS_CUMULATO  ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizionePerFasSIEP(aModel);

		// Condizione ufficio di appartenenza
		lStatement += " WHERE I.CHIAVE_UFFICIO = '" + ufficio + "' ";

		// Aggiunte le condizioni di ricerca.
		if (lCondizioni.trim().length() > 3)
			lStatement += lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * 23/04/2019 MEV70 Restituisce il numero di presenze in Istruttoria del titolo da cumulare. Se >0 va
	 * segnalato e bloccato il processo di aggregazione in cumulo.
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountPresenzeTitoloInIstruttoria(BigDecimal aIdIstruttoria,
			FascicoloSiepModel aFasSiepModel, String ufficio) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM TITOLO_CUMULATO T "
				+ " INNER JOIN ISTRUTTORIA_CUMULO I  on I.ID_ISTRUTTORIA_CUMULO = T.ISTR_ID_ISTRUTTORIA_CUMULO "
				+ " INNER JOIN PROCEDIMENTO_CUMULATO P  on P.TIT_ID_TITOLO_CUMULATO = T.ID_TITOLO_CUMULATO "
				+ " INNER JOIN FASCICOLO_SIEP F  on F.CHIAVE_ANNO = P.CHIAVE_ANNO_FAS_CUMULATO and "
				+ "F.CHIAVE_PROGR = P.CHIAVE_PROGR_FAS_CUMULATO and "
				+ "F.CHIAVE_PROGR = P.CHIAVE_PROGR_FAS_CUMULATO  ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizionePerTitoloCumulato(aFasSiepModel);

		// Condizione ufficio di appartenenza
		lStatement += " WHERE I.CHIAVE_UFFICIO = '" + ufficio + "' ";

		// Aggiunte le condizioni di ricerca.
		if (lCondizioni.trim().length() > 3)
			lStatement += lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Recupera il numero di protocollo dell'ultima istruttoria
	 *
	 * @param aIdFascicolo
	 ****************************************************************************/
	public BigDecimal getProgressivoIstruttoria(String aIdUfficio) throws DAOException {
		String lStatement = new String("");

		lStatement = " SELECT MAX(NUM_PROTOCOLLO) aMAX " + "   FROM ISTRUTTORIA_CUMULO "
				+ "  WHERE CHIAVE_UFFICIO = '" + aIdUfficio + "'"
				+ "    AND ANNO_PROTOCOLLO = TO_CHAR(SYSDATE,('yyyy')) ";

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

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 *
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaIstruttoriaCumuloPaged(IstruttoriaCumuloModel aModel, int aPage) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		lStatement += " " + getOrderByIdIstruttoria() + " ";

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
	public void ricercaIstruttoriaCumulo(IstruttoriaCumuloModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderByIdIstruttoria() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 *
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaIstruttoriaCumuloByKey(BigDecimal aIdIstruttoriaCumulo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdIstruttoriaCumulo);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Istruttorie riferite ai Titoli Cumulati i cui parametri di ricerca sono stati
	 * impostati in IstruttoriaCumuloModel.ProvvedimentoCumulo
	 *
	 * @param IstruttoriaCumuloModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaIstruttoriaPerTitoloCumulato(IstruttoriaCumuloModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQueryPerTitoloCumulato(aModel.getProvvedimentoCumulo());

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizionePerTitoloCumulato(aModel.getProvvedimentoCumulo());

		// Sostituisco la prima keyWord 'AND' con 'WHERE'
		if (lCondizioni.trim().length() > 3)
			lStatement += " WHERE " + lCondizioni.substring(4);

		lStatement += " " + getOrderByIdIstruttoria() + " ";

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Istruttorie riferite ai Titoli Cumulati i cui parametri di ricerca sono stati
	 * impostati in IstruttoriaCumuloModel.ProvvedimentoCumulo Restituisce solo i risultati nel range di
	 * record che vanno inseriti nella pagina passata in input
	 *
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaIstruttoriaPerTitoloCumulatoPaged(IstruttoriaCumuloModel aModel, int aPage)
			throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQueryPerTitoloCumulato(aModel.getProvvedimentoCumulo());

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizionePerTitoloCumulato(aModel.getProvvedimentoCumulo());

		// Condizione ufficio di appartenenza
		lStatement += " WHERE I.CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' ";

		// Aggiunte le condizioni di ricerca.
		if (lCondizioni.trim().length() > 3)
			lStatement += lCondizioni;

		lStatement += " " + getOrderByIdIstruttoria() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Istruttorie afferenti al Fascicolo SIEP ( Il Model contiene i filtri di
	 * ricerca )
	 *
	 * @param FascicoloSiepModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaIstruttoriaPerFasSIEPpaged(FascicoloSiepModel aModel, String ufficio, int aPage)
			throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQueryPerFasSIEP(aModel);

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizionePerFasSIEP(aModel);

		// Condizione ufficio di appartenenza
		lStatement += " WHERE I.CHIAVE_UFFICIO = '" + ufficio + "' ";

		// Aggiunte le condizioni di ricerca.
		lStatement += lCondizioni;

		lStatement += " " + getOrderByIdIstruttoria() + " ";

		// Imposta lo statement da eseguire
		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ISTRUTTORIA_CUMULO, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "EVE_ID_EVENTO_ISTR, " + "EVE_ID_EVENTO_PROV, " + "DATA_APERTURA, " + "DATA_CHIUSURA, "
				+ "ANNO_PROTOCOLLO, " + "NUM_PROTOCOLLO, " + "CHIAVE_UFFICIO, " + "NOTE, " + "FLAG_STATO, "
				+ "ORDINAMENTO_TITOLI, "
				+ "COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM ISTRUTTORIA_CUMULO";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query per TITOLO_CUMULATO
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQueryPerTitoloCumulato(EventoModel aModel) {
		String lStatement = new String("");
		lStatement += " SELECT " + "ID_ISTRUTTORIA_CUMULO, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "EVE_ID_EVENTO_ISTR, " + "EVE_ID_EVENTO_PROV, " + "DATA_APERTURA, " + "DATA_CHIUSURA, "
				+ "ANNO_PROTOCOLLO, " + "NUM_PROTOCOLLO, " + "CHIAVE_UFFICIO, "
				+ "case when (FAS_SIE_ID_FASCICOLO_SIEP <> P.ID_FASCICOLO_SIEP_ORIGINE) then '*'  else I.NOTE END as NOTE, "
				+ "I.FLAG_STATO, " + "ORDINAMENTO_TITOLI, "
				+ "I.COD_OPERATORE_INSERIMENTO, I.DATA_INSERIMENTO, I.COD_UFFICIO_INSERIMENTO, "
				+ "I.COD_OPERATORE_AGGIORNAMENTO, I.DATA_AGGIORNAMENTO, I.COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM ISTRUTTORIA_CUMULO I " + " INNER JOIN TITOLO_CUMULATO T "
				+ " ON T.ISTR_ID_ISTRUTTORIA_CUMULO = I.ID_ISTRUTTORIA_CUMULO "
				+ " INNER JOIN PROCEDIMENTO_CUMULATO P "
				+ " ON P.TIT_ID_TITOLO_CUMULATO = T.ID_TITOLO_CUMULATO ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query per FASCICOLO_SIEP
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQueryPerFasSIEP(FascicoloSiepModel aModel) {
		String lStatement = new String("");
		lStatement += " SELECT " + "I.ID_ISTRUTTORIA_CUMULO, " + "I.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "I.EVE_ID_EVENTO_ISTR, " + "I.EVE_ID_EVENTO_PROV, " + "I.DATA_APERTURA, "
				+ "I.DATA_CHIUSURA, " + "I.ANNO_PROTOCOLLO, " + "I.NUM_PROTOCOLLO, " + "I.CHIAVE_UFFICIO, "
				+ "case when (I.FAS_SIE_ID_FASCICOLO_SIEP <> P.ID_FASCICOLO_SIEP_ORIGINE) then '*'  else I.NOTE END as NOTE, "
				+ "I.FLAG_STATO, " + "I.ORDINAMENTO_TITOLI, "
				+ "I.COD_OPERATORE_INSERIMENTO, I.DATA_INSERIMENTO, I.COD_UFFICIO_INSERIMENTO, "
				+ "I.COD_OPERATORE_AGGIORNAMENTO, I.DATA_AGGIORNAMENTO, I.COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM ISTRUTTORIA_CUMULO I "
				+ " INNER JOIN TITOLO_CUMULATO T  on I.ID_ISTRUTTORIA_CUMULO = T.ISTR_ID_ISTRUTTORIA_CUMULO "
				+ " INNER JOIN PROCEDIMENTO_CUMULATO P  on P.TIT_ID_TITOLO_CUMULATO = T.ID_TITOLO_CUMULATO "
				+ " INNER JOIN FASCICOLO_SIEP F  on F.CHIAVE_ANNO = P.CHIAVE_ANNO_FAS_CUMULATO and "
				+ "F.CHIAVE_PROGR = P.CHIAVE_PROGR_FAS_CUMULATO and "
				+ "F.CHIAVE_UFFICIO = P.COD_UFFICIO_FAS_CUMULATO  ";
		return lStatement;
	}

	public String setCondizionePerTitoloCumulato(EventoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getCodTipoProvvedimento() != null)
			lCondizioni += " AND T.COD_TIPO_PROVVEDIMENTO = '"
					+ StringUtils.convertSqlString(aModel.getCodTipoProvvedimento()) + "'";

		if (aModel.getAnnoProtocollo() != null)
			lCondizioni += " AND T.ANNO_SENTENZA = " + aModel.getAnnoProtocollo();

		if (aModel.getProgrProtocollo() != null)
			lCondizioni += " AND T.NUMERO_SENTENZA = '" + aModel.getProgrProtocollo().toString() + "'";

		if (aModel.getDataEmissione() != null)
			lCondizioni += " AND T.DATA_PROVVEDIMENTO = to_date('"
					+ DateUtils.getDateToString(aModel.getDataEmissione(), "dd/MM/yyyy") + "','DD-MM-YYYY')";

		if (aModel.getCodTipoUfficioEmittente() != null && aModel.getCodTipoUfficioEmittente().length() > 0)
			lCondizioni += " AND T.COD_TIPO_AUTORITA_EMITTENTE = '"
					+ StringUtils.convertSqlString(aModel.getCodTipoUfficioEmittente()) + "'";

		if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0)
			lCondizioni += " AND T.COD_LUOGO_EMITTENTE = '"
					+ StringUtils.convertSqlString(aModel.getCodLuogoEmittente()) + "'";

		return lCondizioni;
	}

	// 23/04/2019 MEV70 Overloading di setCondizionePerTitoloCumulato con parametro Fascicolo SIEP
	public String setCondizionePerTitoloCumulato(FascicoloSiepModel aModel) {
		String lCondizioni = new String();

		if (aModel.getSentenza().getAnnoSentenza() != null)
			lCondizioni += " AND T.ANNO_SENTENZA = " + aModel.getSentenza().getAnnoSentenza();

		if (aModel.getSentenza().getNumeroSentenza() != null)
			lCondizioni += " AND T.NUMERO_SENTENZA = '" + aModel.getSentenza().getNumeroSentenza().toString()
					+ "'";

		if (aModel.getSentenza().getDataProvvedimento() != null)
			lCondizioni += " AND T.DATA_PROVVEDIMENTO = to_date('"
					+ DateUtils.getDateToString(aModel.getSentenza().getDataProvvedimento(), "dd/MM/yyyy")
					+ "','DD-MM-YYYY')";

		if (aModel.getSentenza().getCodTipoAutoritaEmittente() != null
				&& aModel.getSentenza().getCodTipoAutoritaEmittente().length() > 0)
			lCondizioni += " AND T.COD_TIPO_AUTORITA_EMITTENTE = '"
					+ StringUtils.convertSqlString(aModel.getSentenza().getCodTipoAutoritaEmittente()) + "'";

		if (aModel.getSentenza().getCodLuogoEmittente() != null
				&& aModel.getSentenza().getCodLuogoEmittente().length() > 0)
			lCondizioni += " AND T.COD_LUOGO_EMITTENTE = '"
					+ StringUtils.convertSqlString(aModel.getSentenza().getCodLuogoEmittente()) + "'";

		return lCondizioni;
	}

	public String setCondizionePerFasSIEP(FascicoloSiepModel aModel) {
		String lCondizioni = new String();

		if (aModel.getChiaveAnno() != null)
			lCondizioni += " AND P.CHIAVE_ANNO_FAS_CUMULATO = " + aModel.getChiaveAnno();

		if (aModel.getChiaveProgr() != null)
			lCondizioni += " AND P.CHIAVE_PROGR_FAS_CUMULATO = " + aModel.getChiaveProgr();

		if (aModel.getChiaveUfficio() != null)
			lCondizioni += " AND P.COD_UFFICIO_FAS_CUMULATO = '" + aModel.getChiaveUfficio() + "'";

		// Completare con gli altri parametri di ricerca per Fascicolo.
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 *
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		IstruttoriaCumuloModel aModel = new IstruttoriaCumuloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdIstruttoriaCumulo(getBigDecimal("ID_ISTRUTTORIA_CUMULO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setEveIdEventoIstr(getBigDecimal("EVE_ID_EVENTO_ISTR"));
		aModel.setEveIdEventoProv(getBigDecimal("EVE_ID_EVENTO_PROV"));
		aModel.setDataApertura(getDate("DATA_APERTURA"));
		aModel.setDataChiusura(getDate("DATA_CHIUSURA"));
		aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		aModel.setNumProtocollo(getBigDecimal("NUM_PROTOCOLLO"));
		aModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		aModel.setNote(getString("NOTE"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setOrdinamentoTitoli(getString("ORDINAMENTO_TITOLI"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 *
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(IstruttoriaCumuloModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdIstruttoriaCumulo() != null) {
			lCondizioni += " and ID_ISTRUTTORIA_CUMULO = " + aModel.getIdIstruttoriaCumulo() + "";
		}
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
		}
		if (aModel.getEveIdEventoIstr() != null) {
			lCondizioni += " and EVE_ID_EVENTO_ISTR = " + aModel.getEveIdEventoIstr() + "";
		}
		if (aModel.getEveIdEventoProv() != null) {
			lCondizioni += " and EVE_ID_EVENTO_PROV = " + aModel.getEveIdEventoProv() + "";
		}
		if (aModel.getDataApertura() != null) {
			lCondizioni += " and to_char(DATA_APERTURA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataApertura(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataChiusura() != null) {
			lCondizioni += " and to_char(DATA_CHIUSURA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataChiusura(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoProtocollo() != null) {
			lCondizioni += " and ANNO_PROTOCOLLO = " + aModel.getAnnoProtocollo() + "";
		}
		if (aModel.getNumProtocollo() != null) {
			lCondizioni += " and NUM_PROTOCOLLO = " + aModel.getNumProtocollo() + "";
		}
		if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().length() > 0) {
			lCondizioni += " and CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) {
			lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' ";
		}
		if (aModel.getOrdinamentoTitoli() != null && aModel.getOrdinamentoTitoli().length() > 0) {
			lCondizioni += " and ORDINAMENTO_TITOLI = '" + aModel.getOrdinamentoTitoli() + "' ";
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
		if (aModel.getAnnoProtocolloIniziale() != null) {
			if (aModel.getNumProtocolloIniziale() == null) {
				lCondizioni += " and ANNO_PROTOCOLLO >= " + aModel.getAnnoProtocolloIniziale() + "";
			} else {
				lCondizioni += " and ((ANNO_PROTOCOLLO  = " + aModel.getAnnoProtocolloIniziale() + " and "
						+ "NUM_PROTOCOLLO >= " + aModel.getNumProtocolloIniziale() + ") "
						+ " or ANNO_PROTOCOLLO  > " + aModel.getAnnoProtocolloIniziale() + " )";
			}
		}
		if (aModel.getAnnoProtocolloFinale() != null) {
			if (aModel.getNumProtocolloFinale() == null) {
				lCondizioni += " and ANNO_PROTOCOLLO <= " + aModel.getAnnoProtocolloFinale() + "";
			} else {
				lCondizioni += " and ((ANNO_PROTOCOLLO  = " + aModel.getAnnoProtocolloFinale() + " and "
						+ "NUM_PROTOCOLLO <= " + aModel.getNumProtocolloFinale() + ") "
						+ " or ANNO_PROTOCOLLO  < " + aModel.getAnnoProtocolloFinale() + " )";
			}
		}

		// MEV_70 : RICERCA ISTRUTTORIA PER 'Intervallo Data Iscrizione' : Intervento in caso di Data_INIZIO =
		// Data_FINE
		// ===== ==>
		if (aModel.getDataIscrizioneIniziale() != null && aModel.getDataIscrizioneFinale() != null) {
			// Aggiungo un giorno alla Data_FINE e il confronto diventa: "Data_ISCRIZIONE < Data_FINE", e NON
			// più "Data_ISCRIZIONE <= Data_FINE"
			aModel.setDataIscrizioneFinale(DateUtils.getDayAfter(aModel.getDataIscrizioneFinale()));

			lCondizioni += " and DATA_INSERIMENTO >= TO_DATE ('"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneIniziale(), "ddMMyyyy")
					+ "', 'DDMMYYYY')";
			lCondizioni += " and DATA_INSERIMENTO < TO_DATE ('"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneFinale(), "ddMMyyyy")
					+ "', 'DDMMYYYY')";

			// Riporto la Data_FINE al valore originale
			aModel.setDataIscrizioneFinale(DateUtils.getDayBefore(aModel.getDataIscrizioneFinale()));

		} else {
			if (aModel.getDataIscrizioneIniziale() != null) {
				lCondizioni += " and DATA_INSERIMENTO >= TO_DATE ('"
						+ DateUtils.getDateToString(aModel.getDataIscrizioneIniziale(), "ddMMyyyy")
						+ "', 'DDMMYYYY')";
			}
			if (aModel.getDataIscrizioneFinale() != null) {
				lCondizioni += " and DATA_INSERIMENTO <= TO_DATE ('"
						+ DateUtils.getDateToString(aModel.getDataIscrizioneFinale(), "ddMMyyyy")
						+ "', 'DDMMYYYY')";
			}
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
	public String setCondizioniByKey(BigDecimal aIdIstruttoriaCumulo) {
		String lCondizioni = new String();

		lCondizioni += " and ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoriaCumulo;

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
		// orderBy = " ORDER BY ";
		return orderBy;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 *
	 * @return
	 ****************************************************************************/
	protected String getOrderByIdIstruttoria() {
		String orderBy = new String("");
		orderBy = " ORDER BY ID_ISTRUTTORIA_CUMULO ";
		return orderBy;
	}

	// MEV 26
	public void RicercaIstruttoriaCumuloByIdFascicoloSiepIdEvento(BigDecimal aIdFascicolo,
			BigDecimal aIdEvento) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT DISTINCT ISTRU.ID_ISTRUTTORIA_CUMULO,";
		lStatement += " ISTRU.FAS_SIE_ID_FASCICOLO_SIEP, ISTRU.EVE_ID_EVENTO_ISTR, ISTRU.EVE_ID_EVENTO_PROV, ";
		lStatement += " ISTRU.DATA_APERTURA, ISTRU.DATA_CHIUSURA, ISTRU.ANNO_PROTOCOLLO, ISTRU.NUM_PROTOCOLLO, ";
		lStatement += " ISTRU.CHIAVE_UFFICIO, ISTRU.NOTE, ISTRU.FLAG_STATO, ISTRU.ORDINAMENTO_TITOLI, ";
		lStatement += " ISTRU.COD_OPERATORE_INSERIMENTO, ISTRU.DATA_INSERIMENTO, ISTRU.COD_UFFICIO_INSERIMENTO,";
		lStatement += " ISTRU.COD_OPERATORE_AGGIORNAMENTO, ISTRU.DATA_AGGIORNAMENTO, ISTRU.COD_UFFICIO_AGGIORNAMENTO";
		lStatement += " FROM FASCICOLO_SIEP FAS, ISTRUTTORIA_CUMULO ISTRU, EVENTO";
		lStatement += " WHERE ";
		lStatement += " EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND FAS.ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND EVENTO.ID_EVENTO = " + aIdEvento;
		lStatement += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lStatement += " AND ISTRU.EVE_ID_EVENTO_PROV = EVENTO.ID_EVENTO";

		setStatement(lStatement);
	}

	/**
	 * Ricerca tutte le istruttorie legate a provvedimenti validati ordinati dalla più recente
	 * EVENTO.DATA_EMISSIONE
	 *
	 * @param aIdFascicoloSiep
	 * @throws DAOException
	 */
	public void ricercaIstruttoriaCumuloByIdFas(BigDecimal aIdFascicoloSiep) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + " ISTR.ID_ISTRUTTORIA_CUMULO, " + " ISTR.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ " ISTR.EVE_ID_EVENTO_ISTR, " + " ISTR.EVE_ID_EVENTO_PROV, " + " ISTR.DATA_APERTURA, "
				+ " ISTR.DATA_CHIUSURA, " + " ISTR.ANNO_PROTOCOLLO, " + " ISTR.NUM_PROTOCOLLO, "
				+ " ISTR.CHIAVE_UFFICIO, " + " ISTR.NOTE, " + " ISTR.FLAG_STATO, "
				+ " ISTR.ORDINAMENTO_TITOLI, "
				+ " ISTR.COD_OPERATORE_INSERIMENTO, ISTR.DATA_INSERIMENTO, ISTR.COD_UFFICIO_INSERIMENTO, "
				+ " ISTR.COD_OPERATORE_AGGIORNAMENTO, ISTR.DATA_AGGIORNAMENTO, ISTR.COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM ISTRUTTORIA_CUMULO ISTR, EVENTO EV ";
		lStatement += " WHERE ISTR.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
		lStatement += " AND ISTR.FLAG_STATO = 'C' "; // Chiusa
		lStatement += " AND EV.ISTR_ID_ISTRUTTORIA_CUMULO = ISTR.ID_ISTRUTTORIA_CUMULO ";
		lStatement += " AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S' "; // Evento validato non annullato
		lStatement += " ORDER BY EV.DATA_EMISSIONE DESC, EV.ID_EVENTO DESC "; // da più recente

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}


	/**
	 * MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
	 * Si verifica se presenta istruttoria annullata priva di evento per perdita competenza
	 * prima di emissione provvedimento di cumulo
	 * @param aIdFascicoloSiep
	 * @throws DAOException
	 */
	public void ricercaIstruttoriaCumuloAnnullataByIdFas(BigDecimal aIdFascicoloSiep) throws DAOException {

		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
		lSql += " AND FLAG_STATO = 'N' "; // Annullata
		// Chiusa senza evento 
		lSql += " AND NOT EXISTS (SELECT 1 ";
		lSql += "        FROM EVENTO ";
		lSql += "       WHERE EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = "+ aIdFascicoloSiep;
		lSql += "         AND EVENTO.ISTR_ID_ISTRUTTORIA_CUMULO = ISTRUTTORIA_CUMULO.ID_ISTRUTTORIA_CUMULO ";
		lSql += " ) ";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}
	
	// Cerca Una ISTRUTTORIA_CUMULO in stato 'Aperta' by Fas_Sie_ID_Fascicolo_Siep
	public void RicercaIstruttoriaCumuloApertaByIdFasSiep(BigDecimal aIdFascicoloSiep) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
		lSql += " AND FLAG_STATO = 'A' "; // Aperta
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * 30/04/2019 MEV70 Individua l'eventuale primo Titolo doppio in una ISTRUTTORIA_CUMULO.
	 *
	 * @param aIdIstruttoria
	 * @return SentenzaModel
	 ****************************************************************************/
	public Vector<TitoloCumulatoModel> titoloDoppioInIstruttoria(BigDecimal aIdIstruttoria)
			throws DAOException {
		Vector<TitoloCumulatoModel> lTitoliDoppi = new Vector<TitoloCumulatoModel>();
		// start();
		// while(next()) {
		// lElencoFC.add(getModel());
		// }
		// stop();
		// return lElencoFC;

		TitoloCumulatoModel lTitolo = null;
		String lStatement = new String("");

		lStatement = "  SELECT ID_SENTENZA_ORIGINE, ANNO_SENTENZA, NUMERO_SENTENZA, DATA_PROVVEDIMENTO, COD_TIPO_AUTORITA_EMITTENTE, COD_LUOGO_EMITTENTE, "
				+ "		   COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
				+ "		   TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, COUNT(*) "
				+ "    FROM TITOLO_CUMULATO T "
				+ "   INNER JOIN ISTRUTTORIA_CUMULO I  on I.ID_ISTRUTTORIA_CUMULO = T.ISTR_ID_ISTRUTTORIA_CUMULO "
				+ "   INNER JOIN CG_REF_CODES TIPO_PROVVEDIMENTO on (TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = T.COD_TIPO_PROVVEDIMENTO) "
				+ "   INNER JOIN CG_REF_CODES TIPO_AUTORITA_EMITTENTE on (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = T.COD_TIPO_AUTORITA_EMITTENTE) "
				+ "   INNER JOIN  COMUNE LUOGO_EMITTENTE on  LUOGO_EMITTENTE.COD_COMUNE = T.COD_LUOGO_EMITTENTE "
				+ "   WHERE I.ID_ISTRUTTORIA_CUMULO = '" + aIdIstruttoria + "'"
				+ "GROUP BY ID_SENTENZA_ORIGINE, ANNO_SENTENZA, NUMERO_SENTENZA, DATA_PROVVEDIMENTO, COD_TIPO_AUTORITA_EMITTENTE, COD_LUOGO_EMITTENTE, "
				+ "		   COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING, TIPO_AUTORITA_EMITTENTE.RV_MEANING, LUOGO_EMITTENTE.DESCRIZIONE "
				+ "  HAVING COUNT(*) > 1 ";

		setStatement(lStatement);
		this.start();

		while (this.next() && (this.getBigDecimal("ID_SENTENZA_ORIGINE") != null)) {
			lTitolo = new TitoloCumulatoModel();
			lTitolo.setIdSentenzaOrigine(this.getBigDecimal("ID_SENTENZA_ORIGINE"));
			lTitolo.setAnnoSentenza(this.getBigDecimal("ANNO_SENTENZA"));
			lTitolo.setNumeroSentenza(this.getString("NUMERO_SENTENZA"));
			lTitolo.setDataProvvedimento(this.getDate("DATA_PROVVEDIMENTO"));
			lTitolo.setCodTipoProvvedimento(this.getString("COD_TIPO_PROVVEDIMENTO"));
			lTitolo.setDescrTipoProvvedimento(this.getString("DESCR_TIPO_PROVVEDIMENTO"));
			lTitolo.setCodTipoAutoritaEmittente(this.getString("COD_TIPO_AUTORITA_EMITTENTE"));
			lTitolo.setDescrTipoAutoritaEmittente(this.getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
			lTitolo.setCodLuogoEmittente(this.getString("COD_LUOGO_EMITTENTE"));
			lTitolo.setDescrLuogoEmittente(this.getString("DESCR_LUOGO_EMITTENTE"));

			lTitoliDoppi.add(lTitolo);
		}
		this.stop();

		return lTitoliDoppi;
	}

}
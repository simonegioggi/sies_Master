package siap.siep.pagoPA.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.pagoPA.model.BollettinoPagopaModel;

/**
 * Title: BollettinoPagopaSqlDAO 
 * Description: Classe SqlDAO per la gestione del Bollettino PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class BollettinoPagopaSqlDAO extends SIAPSqlDAO {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);
	private static Logger pagoPaLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);

	public BollettinoPagopaSqlDAO(Connection con) {

		super(con);
	}

	protected String getSqlQuery() {

		String s = new String("");

		s += "SELECT BP.ID_BOLLETTINO_PAGOPA, BP.PROG_RATA, BP.NUMERO_RATE,"
				+ " BP.TIPO_RATEIZZAZIONE, BP.IUV, BP.IMPORTO_RATA, BP.IMPORTO_PAGATO,"
				+ " BP.DATA_AVV_PAGAMENTO, BP.DATA_SCADENZA, BP.DATA_SCADENZA_RICH,"
				+ " BP.STATO_PAGAMENTO, BP.COD_OPERATORE_INSERIMENTO,"
				+ " BP.DATA_INSERIMENTO, BP.COD_UFFICIO_INSERIMENTO,"
				+ " BP.COD_OPERATORE_AGGIORNAMENTO, BP.DATA_AGGIORNAMENTO,"
				+ " BP.COD_UFFICIO_AGGIORNAMENTO, BP.FAS_SIE_ID_FASCICOLO_SIEP, BP.RAT_ID_RATEIZZAZIONE_PP,"
				+ " BP.CODICE_DISTRETTO, BP.CODICE_FISCALE, BP.DATA_ULTIMO_CONTROLLO, BP.STATO_PAGOPA,"
				+ " BP.ERRORE_PAGOPA,"
				+ " BP.DATA_GENERAZIONE_BOLLETTINO, "
				+ " TR.RV_MEANING DESCR_TIPO_RATEIZZAZIONE, SP.RV_MEANING DESCR_STATO_PAGAMENTO"
				+ " FROM BOLLETTINO_PAGOPA BP"
				+ " LEFT OUTER JOIN CG_REF_CODES TR ON (BP.TIPO_RATEIZZAZIONE = TR.RV_LOW_VALUE"
				+ " AND TR.RV_DOMAIN = 'TIPO_RATEIZZAZIONE')"
				+ " LEFT OUTER JOIN CG_REF_CODES SP ON (BP.STATO_PAGAMENTO = SP.RV_LOW_VALUE"
				// MEV_2023-33: aggiunte condizioni per storicizzazione eventi
				+ " AND SP.RV_DOMAIN = 'STATO_PAGAMENTO'), RATEIZZAZIONE_PP R, EVENTO E"
				+ " WHERE 1 = 1"
				+ "	AND BP.RAT_ID_RATEIZZAZIONE_PP = R.ID_RATEIZZAZIONE_PP"
				+ "	AND R.EVE_ID_EVENTO = E.ID_EVENTO"
				+ "	AND E.FLAG_DOCUMENTO_REGISTRATO <> 'A'";

		// valore di ritorno
		return s;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		BollettinoPagopaModel aModel = new BollettinoPagopaModel();

		aModel.setIdBollettinoPagopa(getBigDecimal("ID_BOLLETTINO_PAGOPA"));
		aModel.setProgRata(getInt("PROG_RATA"));
		aModel.setNumeroRate(getInt("NUMERO_RATE"));
		aModel.setTipoRateizzazione(getString("TIPO_RATEIZZAZIONE"));
		aModel.setIuv(getString("IUV"));
		aModel.setImportoRata(getBigDecimal("IMPORTO_RATA"));
		aModel.setImportoPagato(getBigDecimal("IMPORTO_PAGATO"));
		aModel.setDataAvvPagamento(getDate("DATA_AVV_PAGAMENTO"));
		aModel.setDataScadenza(getDate("DATA_SCADENZA"));
		aModel.setDataScadenzaRich(getDate("DATA_SCADENZA_RICH"));
		aModel.setStatoPagamento(getString("STATO_PAGAMENTO"));
		// aModel.setDocBollBlob(getBlob("DOC_BOLL_BLOB"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setFasSieIdFascicolSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setRatIdRateizzazionePP(getBigDecimal("RAT_ID_RATEIZZAZIONE_PP"));
		aModel.setDescrTipoRateizzazione(getString("DESCR_TIPO_RATEIZZAZIONE"));
		aModel.setDescrStatoPagamento(getString("DESCR_STATO_PAGAMENTO"));
		aModel.setDataUltimoControllo(getDate("DATA_ULTIMO_CONTROLLO"));
		aModel.setCodiceFiscale(getString("CODICE_FISCALE"));
		aModel.setStatoPagopa(getString("STATO_PAGOPA"));
		aModel.setErrorePagopa(getString("ERRORE_PAGOPA"));
		aModel.setCodiceDistretto(getString("CODICE_DISTRETTO"));
		aModel.setDataGenerazioneBollettino(getDate("DATA_GENERAZIONE_BOLLETTINO"));

		return aModel;
	}

	
	public void ricercaBollettinoPagopaByIdEvento(BigDecimal aIdEvento) throws DAOException {
		String s = getSqlQuery();
		s += " AND R.EVE_ID_EVENTO = " + aIdEvento;
		s += " ORDER BY BP.PROG_RATA ";
		setStatement(s);
	}
	
	public void ricercaBollettinoPagopaByFasSieIdFascicoloSiep(BigDecimal fasSieIdFascicoloSiep, String chiamante)
			throws DAOException {

		String s = getSqlQuery();
		s += setCondizioniByFasSieIdFascicoloSiep(fasSieIdFascicoloSiep);
		
		// MEV_2023-33: si cambia l'ordinamento
		// s += " ORDER BY BP.ID_BOLLETTINO_PAGOPA";
		s += " ORDER BY BP.PROG_RATA ";
		// MEV_2023-33: FINE
		setStatement(s);

		if ("batch".equalsIgnoreCase(chiamante))
			// info per il log
			pagoPaLogger.info("Query >>>>>>>>> " + s);
		else
			// info per il log
			siesLogger.info("Query >>>>>>>>> " + s);
	}

	public String setCondizioniByFasSieIdFascicoloSiep(BigDecimal fasSieIdFascicoloSiep) {

		String condizioni = new String();
		condizioni += " AND BP.FAS_SIE_ID_FASCICOLO_SIEP = " + fasSieIdFascicoloSiep;

		return condizioni;
	}

	public void ricercaBollettinoPagopaByKey(BigDecimal aKey) throws DAOException {

		String s = getSqlQuery();
		s += " " + setCondizionByKey(aKey);
		setStatement(s);

		// info per il log
		siesLogger.info("Query >>>>>>>>> " + s);
	}

	private String setCondizionByKey(BigDecimal aId) {

		String condizioni = "AND BP.ID_BOLLETTINO_PAGOPA = " + aId;
		return condizioni;
	}

	public void getBollettinoByIdBollettinoPagopa(BigDecimal idBollettinoPagopa) {

		String s = new String();

		s += " SELECT DOC_BOLL_BLOB ";
		s += " FROM BOLLETTINO_PAGOPA WHERE ";
		s += " ID_BOLLETTINO_PAGOPA = " + idBollettinoPagopa;
		setStatement(s);

		// info per il log
		siesLogger.info("Query >>>>>>>>> " + s);
	}

	public void ricercaCodiciUfficiProduzione(String codUfficio) {

		String s = new String();
		s += "select t.codice_ufficio codUfficio, t.codice_gl codGl from UFFICI_PRODUZIONE t"
				+ " where t.cod_ufficio_sies = '" + codUfficio + "'";
		setStatement(s);

		// info per il log
		siesLogger.info("Query >>>>>>>>> " + s);
	}

	/**
	 *
	 * @param dayOffset
	 * @throws DAOException
	 */
	public void ricercaBollettiniPagopaNonPagati(int dayOffset) throws DAOException {

		String s = getSqlQuery();
		s += " AND STATO_PAGAMENTO = 'PN' "; // PN = NON PAGATO
		if (dayOffset > 0) {
			s += " AND DATA_ULTIMO_CONTROLLO < (SYSDATE-" + dayOffset + ")"; //
		}
		setStatement(s);

		// info per il log
		pagoPaLogger.info("Query >>>>>>>>> " + s);
	}

	public void ricercaBollettiniPagopaNonPagatiByCF(String aCodiceFiscale, int dayOffset)
			throws DAOException {

		String s = getSqlQuery();
		s += " AND STATO_PAGAMENTO = 'PN' "; // PN = NON PAGATO
		s += " AND CODICE_FISCALE = '" + aCodiceFiscale + "' ";
		if (dayOffset > 0) {
			s += " AND DATA_ULTIMO_CONTROLLO < (SYSDATE-" + dayOffset + ")"; //
		}
		setStatement(s);

		// info per il log
		pagoPaLogger.info("Query >>>>>>>>> " + s);
	}

	/**
	 * 
	 * @param inScadenzaTraGiorni
	 * @param controllateDaGiorni
	 * @param generatiDaGiorni
	 * @param controllarePerGiorni
	 * @throws DAOException
	 */
	public void ricercaDebitoriConPosizioniAperte(int inScadenzaTraGiorni, int controllateDaGiorni
			, int generatiDaGiorni, int controllarePerGiorni)
			throws DAOException {

		String s = "SELECT DISTINCT CODICE_FISCALE, CODICE_DISTRETTO FROM BOLLETTINO_PAGOPA" + " WHERE 1 = 1";
		s += " AND CODICE_FISCALE IS NOT NULL"; // Codice fiscale Valorizzato
		s += " AND IUV IS NOT NULL"; // Bollettino generato
		s += " AND STATO_PAGAMENTO = 'PN'"; // PN = NON PAGATO

		if (inScadenzaTraGiorni > 0) {
			s += " AND (    DATA_SCADENZA BETWEEN (SYSDATE - " + inScadenzaTraGiorni + ")";
			s +=      " AND (SYSDATE + " + inScadenzaTraGiorni + ")";
			if (generatiDaGiorni > 0 && controllarePerGiorni > 0) {
				s +=      " OR (    DATA_SCADENZA IS NULL ";
				s +=          " AND SYSDATE >= (DATA_GENERAZIONE_BOLLETTINO + "+generatiDaGiorni+") ";
				s +=          " AND SYSDATE <= (DATA_GENERAZIONE_BOLLETTINO + "+generatiDaGiorni+" + "+controllarePerGiorni+" ) ";
				s +=	     " )";
			}
			s +=	  ")";
		}
		else if (inScadenzaTraGiorni==0 && generatiDaGiorni > 0 && controllarePerGiorni > 0) {
			// In assenza del vincolo sulla data scadenza posso usare comunque il vincolo 
			// sulla DATA_GENERAZIONE_BOLLETTINO.
			// n.b. anche in presenza della data scadenza sul bollettino.
			s += " AND SYSDATE >= (DATA_GENERAZIONE_BOLLETTINO + "+generatiDaGiorni+") ";
			s += " AND SYSDATE <= (DATA_GENERAZIONE_BOLLETTINO + "+generatiDaGiorni+" + "+controllarePerGiorni+" ) ";
		}
		
		if (controllateDaGiorni > 0) {
			s += " AND DATA_ULTIMO_CONTROLLO < (SYSDATE-" + controllateDaGiorni + ")";
		}
		setStatement(s);

		// info per il log
		pagoPaLogger.info("Query >>>>>>>>> " + s);
	}

	public GenericModel getModelDebitori() throws DAOException {

		BollettinoPagopaModel aModel = new BollettinoPagopaModel();
		aModel.setCodiceFiscale(getString("CODICE_FISCALE"));
		aModel.setCodiceDistretto(getString("CODICE_DISTRETTO"));

		return aModel;
	}

	public void ricercaBollettinoPagopaByIUV(String codiceCRS) throws DAOException {

		String s = getSqlQuery();
		s += " AND IUV = '" + codiceCRS + "' ";
		setStatement(s);

		// info per il log
		pagoPaLogger.info("Query >>>>>>>>> " + s);
	}

	public void ricercaBollettinoPagopaByReteizzazione(BigDecimal idRateizzazione) throws DAOException {

		String s = getSqlQuery();
		s += " AND RAT_ID_RATEIZZAZIONE_PP = " + idRateizzazione;
		setStatement(s);

		// info per il log
		siesLogger.info("Query >>>>>>>>> " + s);
	}
	
	
	public void ricercaBollettiniPagopaByIdInvocazione (BigDecimal idInvocazione) throws DAOException {

		String lStatement = new String("");

		lStatement += "SELECT BP.ID_BOLLETTINO_PAGOPA, BP.PROG_RATA, BP.NUMERO_RATE,"
				+ " BP.TIPO_RATEIZZAZIONE, BP.IUV, BP.IMPORTO_RATA, BP.IMPORTO_PAGATO,"
				+ " BP.DATA_AVV_PAGAMENTO, BP.DATA_SCADENZA, BP.DATA_SCADENZA_RICH,"
				+ " BP.STATO_PAGAMENTO, BP.COD_OPERATORE_INSERIMENTO,"
				+ " BP.DATA_INSERIMENTO, BP.COD_UFFICIO_INSERIMENTO,"
				+ " BP.COD_OPERATORE_AGGIORNAMENTO, BP.DATA_AGGIORNAMENTO,"
				+ " BP.COD_UFFICIO_AGGIORNAMENTO, BP.FAS_SIE_ID_FASCICOLO_SIEP, BP.RAT_ID_RATEIZZAZIONE_PP,"
				+ " BP.CODICE_DISTRETTO, BP.CODICE_FISCALE, BP.DATA_ULTIMO_CONTROLLO"
				// Sostituiso lo STATO_PAGOPA ultimo memorizzato sul bollettino con il dato storicizzato sulla BOLLETTINO_BATCH_PAGOPA
				+ ", BOLLETTINO_BATCH_PAGOPA.STATO_PAGOPA, "
				+ " BP.ERRORE_PAGOPA,"
				+ " BP.DATA_GENERAZIONE_BOLLETTINO, "
				+ " TR.RV_MEANING DESCR_TIPO_RATEIZZAZIONE, SP.RV_MEANING DESCR_STATO_PAGAMENTO ";
		
		lStatement +=  " FROM BOLLETTINO_PAGOPA BP"
				+ " LEFT OUTER JOIN CG_REF_CODES TR ON (BP.TIPO_RATEIZZAZIONE = TR.RV_LOW_VALUE "
				+ " AND TR.RV_DOMAIN = 'TIPO_RATEIZZAZIONE') "
				+ " LEFT OUTER JOIN CG_REF_CODES SP ON (BP.STATO_PAGAMENTO = SP.RV_LOW_VALUE "
				+ " AND SP.RV_DOMAIN = 'STATO_PAGAMENTO') " 
				+ " , BOLLETTINO_BATCH_PAGOPA ";
		
		lStatement += " WHERE 1 = 1";
		lStatement +=   " AND BOLLETTINO_BATCH_PAGOPA.FK_ID_BOLLETTINO_PAGOPA = BP.ID_BOLLETTINO_PAGOPA ";
		lStatement +=   " AND BOLLETTINO_BATCH_PAGOPA.FK_ID_INVOCAZIONE_PAGOPA = "+idInvocazione; 

		lStatement +=   " ORDER BY BP.PROG_RATA ";
		
		setStatement(lStatement);

		// info per il log
		//pagoPaLogger.info("Query >>>>>>>>> " + lStatement);
	}
	

}
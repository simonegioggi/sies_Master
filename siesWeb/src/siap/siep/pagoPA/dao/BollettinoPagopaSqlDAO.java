package siap.siep.pagoPA.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.pagoPA.model.BollettinoPagopaModel;

/**
 * Title: BollettinoPagopaSqlDAO Description: Classe SqlDAO per la gestione del Bollettino PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class BollettinoPagopaSqlDAO extends SIAPSqlDAO {

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
				+ " TR.RV_MEANING DESCR_TIPO_RATEIZZAZIONE, SP.RV_MEANING DESCR_STATO_PAGAMENTO"
				+ " FROM BOLLETTINO_PAGOPA BP"
				+ " LEFT OUTER JOIN CG_REF_CODES TR ON (BP.TIPO_RATEIZZAZIONE = TR.RV_LOW_VALUE"
				+ " AND TR.RV_DOMAIN = 'TIPO_RATEIZZAZIONE')"
				+ " LEFT OUTER JOIN CG_REF_CODES SP ON (BP.STATO_PAGAMENTO = SP.RV_LOW_VALUE"
				+ " AND SP.RV_DOMAIN = 'STATO_PAGAMENTO')" + " WHERE 1 = 1";
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

		return aModel;
	}

	public void ricercaBollettinoPagopaByFasSieIdFascicoloSiep(BigDecimal fasSieIdFascicoloSiep)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += setCondizioniByFasSieIdFascicoloSiep(fasSieIdFascicoloSiep);
		setStatement(lSql);
	}

	public String setCondizioniByFasSieIdFascicoloSiep(BigDecimal fasSieIdFascicoloSiep) {

		String condizioni = new String();
		condizioni += " AND BP.FAS_SIE_ID_FASCICOLO_SIEP = " + fasSieIdFascicoloSiep;
		condizioni += " ORDER BY BP.ID_BOLLETTINO_PAGOPA";
		return condizioni;
	}

	public void ricercaBollettinoPagopaByKey(BigDecimal aKey) throws DAOException {

		String s = getSqlQuery();
		s += " " + setCondizionByKey(aKey);
		setStatement(s);
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
	}

	public void ricercaCodiciUfficiProduzione(String codUfficio) {

		String s = new String();

		s += "select t.codice_ufficio codUfficio, t.codice_gl codGl from UFFICI_PRODUZIONE t"
				+ " where t.cod_ufficio_sies = '" + codUfficio + "'";
		setStatement(s);
	}

	/**
	 *
	 * @param offset
	 * @throws DAOException
	 */
	public void ricercaBollettiniPagopaNonPagati(int dayOffset) throws DAOException {

		String s = getSqlQuery();
		s += " AND STATO_PAGAMENTO = 'PN' "; // PN = NON PAGATO
		if (dayOffset > 0) {
			s += " AND DATA_ULTIMO_CONTROLLO < (SYSDATE-" + dayOffset + ")"; //
		}
		setStatement(s);
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
	}

	public void ricercaDebitoriConPosizioniAperte(int inScadenzaTraGiorni, int controllateDaGiorni)
			throws DAOException {

		String s = "SELECT DISTINCT CODICE_FISCALE, CODICE_DISTRETTO " + " FROM BOLLETTINO_PAGOPA "
				+ " WHERE 1=1 ";
		s += " AND CODICE_FISCALE IS NOT NULL "; // Codice fiscale Valorizzato
		s += " AND IUV IS NOT NULL "; // Bollettino generato
		s += " AND STATO_PAGAMENTO = 'PN' "; // PN = NON PAGATO

		if (inScadenzaTraGiorni > 0) {
			s += " AND DATA_SCADENZA > (SYSDATE-" + controllateDaGiorni + ")"; //
		}

		if (controllateDaGiorni > 0) {
			s += " AND DATA_ULTIMO_CONTROLLO < (SYSDATE-" + controllateDaGiorni + ")"; //
		}
		setStatement(s);
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
	}

}
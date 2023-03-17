package siap.siep.pagoPA.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.pagoPA.model.BollettinoPagopaModel;

/**
 * MEV_2023-13 Title: BollettinoPagopaSqlDAO Description: Classe SqlDAO per la gestione del Bollettino PagoPA
 *
 * @author sgioggi
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
				+ " TR.RV_MEANING DESCR_TIPO_RATEIZZAZIONE, SP.RV_MEANING DESCR_STATO_PAGAMENTO"
				+ " FROM BOLLETTINO_PAGOPA BP"
				+ " LEFT OUTER JOIN CG_REF_CODES TR ON (BP.TIPO_RATEIZZAZIONE = TR.RV_LOW_VALUE"
				+ " AND TR.RV_DOMAIN = 'TIPO_RATEIZZAZIONE')"
				+ " LEFT OUTER JOIN CG_REF_CODES SP ON (BP.STATO_PAGAMENTO = SP.RV_LOW_VALUE"
				+ " AND SP.RV_DOMAIN = 'STATO_PAGAMENTO')"
				+ " WHERE 1 = 1";
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

}
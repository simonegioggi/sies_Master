package siap.siep.pagoPaBatch.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;

/**
 * Batch per PagoPA
 *
 * @author d.fiorletta
 * @since MEV_2023-13
 * @version 1.0
 */
public class BatchPagopaDAO extends SIAPTableDAO {

	public BatchPagopaDAO(Connection con) {

		super(con);
		setTable("BATCH_PAGOPA");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_BATCH_PAGOPA", "BATCH_PAGOPA_SEQ");
		setFieldKey("ID_BATCH_PAGOPA", BIG_DECIMAL);

		setField("DATA_INIZIO_ESECUZIONE", DATE);
		setField("DATA_FINE_ESECUZIONE", DATE);
		setField("NUM_POS_DEBITORIE_VERIFICATE", BIG_DECIMAL);
		setField("NUM_BOLLETTINI_AGGIORNATI", BIG_DECIMAL);
		setField("ESITO_ESECUZIONE", STRING);
		setField("ERRORE_ESECUZIONE", STRING);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdBatchPagopa() throws DAOException {
		return getBigDecimal("ID_BATCH_PAGOPA");
	}

	public Date getDataInizioEsecuzione() throws DAOException {
		return getDate("DATA_INIZIO_ESECUZIONE");
	}

	public Date getDataFineEsecuzione() throws DAOException {
		return getDate("DATA_FINE_ESECUZIONE");
	}

	public BigDecimal getNumPosDebitorieVerificate() throws DAOException {
		return getBigDecimal("NUM_POS_DEBITORIE_VERIFICATE");
	}

	public BigDecimal getNumBollettiniAggiornati() throws DAOException {
		return getBigDecimal("NUM_BOLLETTINI_AGGIORNATI");
	}

	public String getEsitoEsecuzione() throws DAOException {
		return getString("ESITO_ESECUZIONE");
	}

	public String getErroreEsecuzione() throws DAOException {
		return getString("ERRORE_ESECUZIONE");
	}

	//
	// METODI SET()
	//
	public void setIdBatchPagopa(BigDecimal aValore) {
		setBigDecimal("ID_BATCH_PAGOPA", aValore);
	}

	public void setDataInizioEsecuzione(Date aValore) {
		setDate("DATA_INIZIO_ESECUZIONE", aValore);
	}

	public void setDataFineEsecuzione(Date aValore) {
		setDate("DATA_FINE_ESECUZIONE", aValore);
	}

	public void setNumPosDebitorieVerificate(BigDecimal aValore) {
		setBigDecimal("NUM_POS_DEBITORIE_VERIFICATE", aValore);
	}

	public void setNumBollettiniAggiornati(BigDecimal aValore) {
		setBigDecimal("NUM_BOLLETTINI_AGGIORNATI", aValore);
	}

	public void setEsitoEsecuzione(String aValore) {
		setString("ESITO_ESECUZIONE", aValore);
	}

	public void setErroreEsecuzione(String aValore) {

		setString("ERRORE_ESECUZIONE", aValore);
	}

	public GenericModel getModel() throws DAOException {

		BatchPagopaModel lModel = new BatchPagopaModel();

		lModel.setIdBatchPagopa(getIdBatchPagopa());
		lModel.setDataInizioEsecuzione(getDataInizioEsecuzione());
		lModel.setDataFineEsecuzione(getDataFineEsecuzione());
		lModel.setNumPosDebitorieVerificate(getNumPosDebitorieVerificate());
		lModel.setNumBollettiniAggiornati(getNumBollettiniAggiornati());
		lModel.setEsitoEsecuzione(getEsitoEsecuzione());
		lModel.setErroreEsecuzione(getErroreEsecuzione());

		return lModel;
	}

	public void setDAOFromModel(BatchPagopaModel aModel) throws DAOException {

		setIdBatchPagopa(aModel.getIdBatchPagopa());
		setDataInizioEsecuzione(aModel.getDataInizioEsecuzione());
		setDataFineEsecuzione(aModel.getDataFineEsecuzione());
		setNumPosDebitorieVerificate(aModel.getNumPosDebitorieVerificate());
		setNumBollettiniAggiornati(aModel.getNumBollettiniAggiornati());
		setEsitoEsecuzione(aModel.getEsitoEsecuzione());
		setErroreEsecuzione(aModel.getErroreEsecuzione());
	}

	public void setDAOFromModelForUpdate(BatchPagopaModel aModel) throws DAOException {

		// setIdBatchPagopa(aModel.getIdBatchPagopa());
		setDataInizioEsecuzione(aModel.getDataInizioEsecuzione());
		setDataFineEsecuzione(aModel.getDataFineEsecuzione());
		setNumPosDebitorieVerificate(aModel.getNumPosDebitorieVerificate());
		setNumBollettiniAggiornati(aModel.getNumBollettiniAggiornati());
		setEsitoEsecuzione(aModel.getEsitoEsecuzione());
		setErroreEsecuzione(aModel.getErroreEsecuzione());
	}

	public void setCondizioneUpdate(BigDecimal IdBatchPagopa) {

		setCondition(" ID_BATCH_PAGOPA = " + IdBatchPagopa);
	}

	public void selCondizioneDeleteByKey(BigDecimal IdBatchPagopa) {

		setCondition(" ID_BATCH_PAGOPA = " + IdBatchPagopa);
	}

}
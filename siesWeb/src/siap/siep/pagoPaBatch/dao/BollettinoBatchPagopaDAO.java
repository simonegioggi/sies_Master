package siap.siep.pagoPaBatch.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import siap.dao.SIAPTableDAO;
import siap.siep.pagoPaBatch.model.BollettinoBatchPagopaModel;

/**
 * DAO per inserimento deti nella tabella di relazione
 *
 * @author d.fiorletta
 *
 */
public class BollettinoBatchPagopaDAO extends SIAPTableDAO {

	public BollettinoBatchPagopaDAO(Connection con) {

		super(con);
		setTable("BOLLETTINO_BATCH_PAGOPA");

		// Settare la Sequence e i campi chiave
		// setSequenceField("ID_INVOCAZIONE_PAGOPA", "INVOCAZIONE_PAGOPA_SEQ");

		// setFieldKey("ID_INVOCAZIONE_PAGOPA", BIG_DECIMAL);

		setField("FK_ID_INVOCAZIONE_PAGOPA", BIG_DECIMAL);
		setField("FK_ID_BOLLETTINO_PAGOPA", BIG_DECIMAL);
		setField("FK_ID_BATCH_PAGOPA", BIG_DECIMAL);
		setField("STATO_PAGOPA", STRING);
	}

	public BigDecimal getFkIdInvocazionePagopa() throws DAOException {
		return getBigDecimal("FK_ID_INVOCAZIONE_PAGOPA");
	}

	public BigDecimal getFkIdBollettinoPagopa() throws DAOException {
		return getBigDecimal("FK_ID_BOLLETTINO_PAGOPA");
	}

	public BigDecimal getFkIdBatchPagopa() throws DAOException {
		return getBigDecimal("FK_ID_BATCH_PAGOPA");
	}

	public String getStatoPagopa() throws DAOException {
		return getString("STATO_PAGOPA");
	}

	public void setFkIdInvocazionePagopa(BigDecimal aValore) {
		setBigDecimal("FK_ID_INVOCAZIONE_PAGOPA", aValore);
	}

	public void setFkIdBollettinoPagopa(BigDecimal aValore) {
		setBigDecimal("FK_ID_BOLLETTINO_PAGOPA", aValore);
	}

	public void setFkIdBatchPagopa(BigDecimal aValore) {
		setBigDecimal("FK_ID_BATCH_PAGOPA", aValore);
	}

	public void setStatoPagopa(String aValore) {
		setString("STATO_PAGOPA", aValore);
	}

	public void setDAOFromModel(BollettinoBatchPagopaModel aModel) throws DAOException {
		setFkIdInvocazionePagopa(aModel.getFkIdInvocazionePagopa());
		setFkIdBollettinoPagopa(aModel.getFkIdBollettinoPagopa());
		setFkIdBatchPagopa(aModel.getFkIdBatchPagopa());
		setStatoPagopa(aModel.getStatoPagopa());
	}

}
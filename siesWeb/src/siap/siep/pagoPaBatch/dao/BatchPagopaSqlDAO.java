package siap.siep.pagoPaBatch.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;

public class BatchPagopaSqlDAO extends SIAPSqlDAO {
    public BatchPagopaSqlDAO(Connection con) {
        super(con);
    }
    
    protected String getSqlQuery() {
        String lStatement = new String("");

        lStatement += " SELECT ID_BATCH_PAGOPA, "
                           + " DATA_INIZIO_ESECUZIONE, DATA_FINE_ESECUZIONE, "
                           + " NUM_POS_DEBITORIE_VERIFICATE, NUM_BOLLETTINI_AGGIORNATI, "
                           + " ESITO_ESECUZIONE, ERRORE_ESECUZIONE "; 
        lStatement += " FROM BATCH_PAGOPA";
        lStatement += " WHERE 1=1 ";

        return lStatement;
    }
    
	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_BATCH_PAGOPA = " + aKey;
	}
    
    public GenericModel getModel() throws DAOException {
        BatchPagopaModel aModel = new BatchPagopaModel();

        aModel.setIdBatchPagopa             (getBigDecimal("ID_BATCH_PAGOPA"));
        aModel.setDataInizioEsecuzione      (getDate("DATA_INIZIO_ESECUZIONE"));
        aModel.setDataFineEsecuzione        (getDate("DATA_FINE_ESECUZIONE"));
        aModel.setNumPosDebitorieVerificate (getBigDecimal("NUM_POS_DEBITORIE_VERIFICATE"));
        aModel.setNumBollettiniAggiornati   (getBigDecimal("NUM_BOLLETTINI_AGGIORNATI"));
        aModel.setEsitoEsecuzione           (getString("ESITO_ESECUZIONE"));
        aModel.setErroreEsecuzione          (getString("ERRORE_ESECUZIONE"));

        return aModel;
    }
    
    public void ricercaEsecuzioneBatch (BatchPagopaModel aModel) throws DAOException {
        String lSql = getSqlQuery();
        
        lSql += " ORDER BY ID_BATCH_PAGOPA DESC";

        setStatement(lSql);
    }
    
    
    public void ricercaEsecuzioneBatchPaged (BatchPagopaModel aModel, int aPage) throws DAOException {
        String lSql = getSqlQuery();
        
        lSql += " ORDER BY ID_BATCH_PAGOPA DESC";
        
		if (aPage > 0) {
			lSql = " SELECT * FROM (SELECT INNER.* , Rownum rn FROM ( " + lSql
					+ " ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		}

        setStatement(lSql);
    }
    
	public void ricercaBatchByKey (BigDecimal aIdbatch) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aIdbatch);
		setStatement(lSql);
	}
    
    
}

package siap.siep.pagoPaBatch.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.siep.pagoPaBatch.model.InvocazionePagopaModel;

/**
 *
 * @author d.fiorletta
 * @since MEV_2023-33
 * @version 1.0
 */
public class InvocazionePagopaSqlDAO extends SIAPSqlDAO {
    public InvocazionePagopaSqlDAO(Connection con) {
        super(con);
    }
    
    protected String getSqlQuery() {
        String lStatement = new String("");

        lStatement += " SELECT ID_INVOCAZIONE_PAGOPA, "
                           + " DATA_INVOCAZIONE, "
                           + " CODICE_FISCALE, IUV, "
                           // Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
                           //+ " XML_RICHIESTA, XML_RISPOSTA, "
                           + " XML_RICHIESTA, XML_RISPOSTA_CLOB, "
                           // Ticket#202405210111 - 20240521012 - FINE
                           + " ERRORE, FK_ID_BATCH, "
                           + " COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "
                           + " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO "; 
        lStatement += " FROM INVOCAZIONE_PAGOPA";
        lStatement += " WHERE 1=1 ";

        return lStatement;
    }    
    
	public GenericModel getModel() throws DAOException {

		InvocazionePagopaModel lModel = new InvocazionePagopaModel();

		lModel.setIdInvocazionePagopa  (getBigDecimal("ID_INVOCAZIONE_PAGOPA"));
		lModel.setDataInvocazione      (getDate("DATA_INVOCAZIONE"));
		lModel.setCodiceFiscale        (getString("CODICE_FISCALE"));
		lModel.setIuv                  (getString("IUV"));
		lModel.setXmlRichiesta         (getString("XML_RICHIESTA"));
		// Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
		//lModel.setXmlRisposta          (getString("XML_RISPOSTA"));
		lModel.setXmlRispostaClob      (getString("XML_RISPOSTA_CLOB"));
	  // Ticket#202405210111 - 20240521012 - FINE
	
		lModel.setErrore               (getString("ERRORE"));
		lModel.setFkIdBatch            (getBigDecimal("FK_ID_BATCH"));
		
		lModel.setCodOperatoreInserimento    (getString("COD_OPERATORE_INSERIMENTO"));
		lModel.setDataInserimento            (getDate("DATA_INSERIMENTO"));
		lModel.setCodUfficioInserimento      (getString("COD_UFFICIO_INSERIMENTO"));
		lModel.setCodOperatoreAggiornamento  (getString("COD_OPERATORE_AGGIORNAMENTO"));
		lModel.setDataAggiornamento          (getDate("DATA_AGGIORNAMENTO"));
		lModel.setCodUfficioAggiornamento    (getString("COD_UFFICIO_AGGIORNAMENTO"));
		
		
		return lModel;
	}
	
	
    public void ricercaInvocazioniByIdBatch (BigDecimal aIdBatch) throws DAOException {
        String lSql = getSqlQuery();
        
        lSql += " AND FK_ID_BATCH = "+aIdBatch;
        
        lSql += " ORDER BY FK_ID_BATCH";

        setStatement(lSql);
    }
	
    public void ricercaInvocazioniByIdBatchPaged (BigDecimal aIdBatch, int aPage) throws DAOException {
        String lSql = getSqlQuery();
        
        lSql += " AND FK_ID_BATCH = "+aIdBatch;
        
        lSql += " ORDER BY ID_INVOCAZIONE_PAGOPA ";

        
		if (aPage > 0) {
			lSql = " SELECT * FROM (SELECT INNER.* , Rownum rn FROM ( " + lSql
					+ " ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		}
		
        setStatement(lSql);
    }
	
    public void ricercaInvocazioniById (BigDecimal aIdInvocazione) throws DAOException {
        String lSql = getSqlQuery();
        
        lSql += " AND ID_INVOCAZIONE_PAGOPA = "+aIdInvocazione;
        
        setStatement(lSql);
    }
}

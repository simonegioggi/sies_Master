package siap.siep.pagoPaBatch.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.siep.pagoPaBatch.model.InvocazionePagopaModel;

/**
 *
 * @author d.fiorletta
 * @since MEV_2023-33
 * @version 1.0
 */
public class InvocazionePagopaDAO extends SIAPTableDAO {
	public InvocazionePagopaDAO(Connection con) {

		super(con);
		setTable("INVOCAZIONE_PAGOPA");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_INVOCAZIONE_PAGOPA", "INVOCAZIONE_PAGOPA_SEQ");
		
		setFieldKey("ID_INVOCAZIONE_PAGOPA", BIG_DECIMAL);

		setField("DATA_INVOCAZIONE", DATE);
		setField("CODICE_FISCALE", STRING);
		setField("IUV", STRING);
		setField("XML_RICHIESTA", STRING);
	  // Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
		// setField("XML_RISPOSTA", STRING);		
    setField("XML_RISPOSTA_CLOB", CLOB); 
    // Ticket#202405210111 - 20240521012 - FINE
		setField("ERRORE", STRING);
		setField("FK_ID_BATCH", BIG_DECIMAL);		
		
	    setField("COD_OPERATORE_INSERIMENTO", STRING);
	    setField("DATA_INSERIMENTO", DATE);
	    setField("COD_UFFICIO_INSERIMENTO", STRING);
	    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
	    setField("DATA_AGGIORNAMENTO", DATE);
	    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	}
	
	//GET
	public BigDecimal getIdInvocazionePagopa() throws DAOException { return getBigDecimal("ID_INVOCAZIONE_PAGOPA");}
	public Date       getDataInvocazione()     throws DAOException { return getDate("DATA_INVOCAZIONE");}
	public String     getCodiceFiscale()       throws DAOException { return getString("CODICE_FISCALE");}
	public String     getIuv()                 throws DAOException { return getString("IUV");}
	public String     getXmlRichiesta()        throws DAOException { return getString("XML_RICHIESTA");}
	
	// Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
	//public String     getXmlRisposta()         throws DAOException { return getString("XML_RISPOSTA");}
  public String       getXmlRispostaClob()     throws DAOException { return getString("XML_RISPOSTA_CLOB");}	
  // Ticket#202405210111 - 20240521012 - FINE
  
	public String     getErrore()              throws DAOException { return getString("ERRORE");}
	public BigDecimal getFkIdBatch()           throws DAOException { return getBigDecimal("FK_ID_BATCH");}

	public String     getCodOperatoreInserimento()   throws DAOException { return getString("COD_OPERATORE_INSERIMENTO");}
	public Date       getDataInserimento()           throws DAOException { return getDate("DATA_INSERIMENTO");}
	public String     getCodUfficioInserimento()     throws DAOException { return getString("COD_UFFICIO_INSERIMENTO");}
	public String     getCodOperatoreAggiornamento() throws DAOException { return getString("COD_OPERATORE_AGGIORNAMENTO");}
	public Date       getDataAggiornamento()         throws DAOException { return getDate("DATA_AGGIORNAMENTO");}
	public String     getCodUfficioAggiornamento()   throws DAOException { return getString("COD_UFFICIO_AGGIORNAMENTO");}
	
	//SET
	public void setIdInvocazionePagopa (BigDecimal aValore) { setBigDecimal("ID_INVOCAZIONE_PAGOPA", aValore); }
	public void setDataInvocazione     (Date       aValore) { setDate("DATA_INVOCAZIONE", aValore);  }
	public void setCodiceFiscale       (String     aValore) { setString("CODICE_FISCALE", aValore);  }
	public void setIuv                 (String     aValore) { setString("IUV", aValore); }
	public void setXmlRichiesta        (String     aValore) { setString("XML_RICHIESTA", aValore); }
  // Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
	// public void setXmlRisposta         (String     aValore) { setString("XML_RISPOSTA", aValore);  }
  public void setXmlRispostaClob         (String     aValore) { setString("XML_RISPOSTA_CLOB", aValore);  }	
  // Ticket#202405210111 - 20240521012 - 
	public void setErrore              (String     aValore) { setString("ERRORE", aValore); }
	public void setFkIdBatch           (BigDecimal aValore) { setBigDecimal("FK_ID_BATCH", aValore);  }

	public void setCodOperatoreInserimento   (String aValore) { setString("COD_OPERATORE_INSERIMENTO", aValore);  }
	public void setDataInserimento           (Date   aValore) { setDate("DATA_INSERIMENTO", aValore);  }
	public void setCodUfficioInserimento     (String aValore) { setString("COD_UFFICIO_INSERIMENTO", aValore);  }
	public void setCodOperatoreAggiornamento (String aValore) { setString("COD_OPERATORE_AGGIORNAMENTO", aValore);  }
	public void setDataAggiornamento         (Date   aValore) { setDate("DATA_AGGIORNAMENTO", aValore);  }
	public void setCodUfficioAggiornamento   (String aValore) { setString("COD_UFFICIO_AGGIORNAMENTO", aValore);  }
	
	
	public GenericModel getModel() throws DAOException {

		InvocazionePagopaModel lModel = new InvocazionePagopaModel();

		lModel.setIdInvocazionePagopa(getIdInvocazionePagopa());
		lModel.setDataInvocazione(getDataInvocazione());
		lModel.setCodiceFiscale(getCodiceFiscale());
		lModel.setIuv(getIuv());
		lModel.setXmlRichiesta(getXmlRichiesta());
		// Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
		//lModel.setXmlRisposta(getXmlRisposta());
		lModel.setXmlRispostaClob (getXmlRispostaClob());
	  // Ticket#202405210111 - 20240521012 - FINE
		
		lModel.setErrore(getErrore());
		lModel.setFkIdBatch(getFkIdBatch());
		
		lModel.setCodOperatoreInserimento(getCodOperatoreInserimento());
		lModel.setDataInserimento(getDataInserimento());
		lModel.setCodUfficioInserimento(getCodUfficioInserimento());
		lModel.setCodOperatoreAggiornamento(getCodOperatoreAggiornamento());
		lModel.setDataAggiornamento(getDataAggiornamento());
		lModel.setCodUfficioAggiornamento(getCodUfficioAggiornamento());		
		
		return lModel;
	}
	
	
	public void setDAOFromModel(InvocazionePagopaModel aModel) throws DAOException {

		setIdInvocazionePagopa(aModel.getIdInvocazionePagopa());
		setDataInvocazione(aModel.getDataInvocazione());
		setCodiceFiscale(aModel.getCodiceFiscale());
		setIuv(aModel.getIuv());
		setXmlRichiesta(aModel.getXmlRichiesta());
	  // Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
		// setXmlRisposta(aModel.getXmlRisposta());
    setXmlRispostaClob(aModel.getXmlRispostaClob());  
    // Ticket#202405210111 - 20240521012
		setErrore(aModel.getErrore());
		setFkIdBatch(aModel.getFkIdBatch());
		
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
	}
	
	public void setDAOFromModelForUpdate (InvocazionePagopaModel aModel) throws DAOException {

		// setIdInvocazionePagopa(aModel.getIdInvocazionePagopa());
		// setDataInvocazione(aModel.getDataInvocazione());
		// setCodiceFiscale(aModel.getCodiceFiscale());
		// setIuv(aModel.getIuv());
		setXmlRichiesta(aModel.getXmlRichiesta());
		
		// Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
		//setXmlRisposta(aModel.getXmlRisposta());		
		setXmlRispostaClob(aModel.getXmlRispostaClob());	
		// Ticket#202405210111 - 20240521012
		
		setErrore(aModel.getErrore());
		//setFkIdBatch(aModel.getFkIdBatch());
		
		//setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		//setDataInserimento(aModel.getDataInserimento());
		//setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
	}
	
	public void setCondizioneUpdate(BigDecimal IdInvocazionePagopa) {

		setCondition(" ID_INVOCAZIONE_PAGOPA = " + IdInvocazionePagopa);
	}
}

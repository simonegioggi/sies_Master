package siap.siep.pagoPaBatch.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;
import siap.siep.pagoPA.model.BollettinoPagopaModel;

/**
 * 
 *
 * @author d.fiorletta
 * @since MEV_2023-33
 * @version 1.0
 */
public class InvocazionePagopaModel extends GenericModel {
  private static final long serialVersionUID = 3481007881258575439L;

  private BigDecimal mIdInvocazionePagopa;
  private Date mDataInvocazione;
  private String mCodiceFiscale;
  private String mIuv;
  private String mXmlRichiesta;
  // Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
  // private String mXmlRisposta;    
  private String mXmlRispostaClob;  
  // Ticket#202405210111 - 20240521012 - FINE
  private String mErrore;
  private BigDecimal mFkIdBatch;
  
  private String mCodOperatoreInserimento;
  private Date mDataInserimento;
  private String mCodUfficioInserimento;

  private String mCodOperatoreAggiornamento;
  private Date mDataAggiornamento;
  private String mCodUfficioAggiornamento;
  
  private Vector <BollettinoPagopaModel> mListaBollettini;
  
  // GETTER
  public BigDecimal getIdInvocazionePagopa()       { return mIdInvocazionePagopa;  }
  public Date       getDataInvocazione()           { return mDataInvocazione;  }
  public String     getCodiceFiscale()             { return mCodiceFiscale;  }
  public String     getIuv()                       { return mIuv;  }
  public String     getXmlRichiesta()              { return mXmlRichiesta; }
  // Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
  // public String     getXmlRisposta()               { return mXmlRisposta;  }
  public String     getXmlRispostaClob()           { return mXmlRispostaClob;  }
  // Ticket#202405210111 - 20240521012 - FINE
  public String     getErrore()                    { return mErrore; }
  public BigDecimal getFkIdBatch()                 { return mFkIdBatch;  }
  
  public String     getCodOperatoreInserimento()   { return mCodOperatoreInserimento;  }
  public Date       getDataInserimento()           { return mDataInserimento;  }
  public String     getCodUfficioInserimento()     { return mCodUfficioInserimento;  }
  public String     getCodOperatoreAggiornamento() { return mCodOperatoreAggiornamento;  }
  public Date       getDataAggiornamento()         { return mDataAggiornamento;  }
  public String     getCodUfficioAggiornamento()   { return mCodUfficioAggiornamento;  }
  
  public Vector <BollettinoPagopaModel> getListaBollettini() { return mListaBollettini;  }
  
  // Setter
  public void setIdInvocazionePagopa (BigDecimal aValore) { this.mIdInvocazionePagopa = aValore;  }
  public void setDataInvocazione     (Date       aValore) { this.mDataInvocazione = aValore;  }
  public void setCodiceFiscale       (String     aValore) { this.mCodiceFiscale = aValore;  }
  public void setIuv                 (String     aValore) { this.mIuv = aValore;  }
  public void setXmlRichiesta        (String     aValore) { this.mXmlRichiesta = aValore; }
  // Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
  // public void setXmlRisposta         (String     aValore) { this.mXmlRisposta = aValore;  }
  public void setXmlRispostaClob     (String     aValore) { this.mXmlRispostaClob = aValore;  }
  // Ticket#202405210111 - 20240521012 - FINE
  public void setErrore              (String     aValore) { this.mErrore = aValore; }
  public void setFkIdBatch           (BigDecimal aValore) { this.mFkIdBatch = aValore;  }

  public void setCodOperatoreInserimento   (String aValore) { this.mCodOperatoreInserimento = aValore;  }
  public void setDataInserimento           (Date   aValore) { this.mDataInserimento = aValore;  }
  public void setCodUfficioInserimento     (String aValore) { this.mCodUfficioInserimento = aValore;  }
  public void setCodOperatoreAggiornamento (String aValore) { this.mCodOperatoreAggiornamento = aValore;  }
  public void setDataAggiornamento         (Date   aValore) { this.mDataAggiornamento = aValore;  }
  public void setCodUfficioAggiornamento   (String aValore) { this.mCodUfficioAggiornamento = aValore;  }
  
  public void setListaBollettini(Vector <BollettinoPagopaModel> aValore) { this.mListaBollettini = aValore;  }
}

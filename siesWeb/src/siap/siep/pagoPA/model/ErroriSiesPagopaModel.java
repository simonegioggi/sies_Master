package siap.siep.pagoPA.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * MEV_2023-33
 */
public class ErroriSiesPagopaModel extends GenericModel {

  private static final long serialVersionUID = -3760831160392472201L;
  
  private BigDecimal mIdErroriSiesPagopa;
  private BigDecimal mIdFascicoloSiep;
  private BigDecimal mIdEvento;
  
  private String mAzioneContestoJava;
  private String mDescrizioneFunzione;
  private String mCodUtente;
  private String mCodUfficio;
  private String mErroreEsecuzione;
  
  private Date mDataInserimento;
  private Date mDataVisualizzazione;
  private String mCodUtenteVisualizzazione;
  
  // Solo per le ricerche 
  private Date mDataInserimentoAl;
  private BigDecimal mChiaveAnno;
  private BigDecimal mChiaveProgr;
  private String mCognome;
  private String mNome;
  private String mDescMotivoEvento;
  private Date mDataEmissione;
  
  public ErroriSiesPagopaModel () {  
    this.mIdErroriSiesPagopa = null;
    this.mIdFascicoloSiep = null;
    this.mIdEvento = null;
    this.mAzioneContestoJava = "";
    this.mDescrizioneFunzione = "";
    this.mCodUtente = "";
    this.mCodUfficio = "";
    this.mErroreEsecuzione = "";
    this.mDataInserimento = null;
    this.mDataVisualizzazione = null;
    this.mCodUtenteVisualizzazione = "";
    
    this.mDataInserimentoAl = null;
  }

  
  public ErroriSiesPagopaModel (ErroriSiesPagopaModel aModel) {
    this.mIdErroriSiesPagopa = aModel.mIdErroriSiesPagopa;
    this.mIdFascicoloSiep = aModel.mIdFascicoloSiep;
    this.mIdEvento = aModel.mIdEvento;
    this.mAzioneContestoJava = aModel.mAzioneContestoJava;
    this.mDescrizioneFunzione = aModel.mDescrizioneFunzione;
    this.mCodUtente = aModel.mCodUtente;
    this.mCodUfficio = aModel.mCodUfficio;
    this.mErroreEsecuzione = aModel.mErroreEsecuzione;
    this.mDataInserimento = aModel.mDataInserimento;
    this.mDataVisualizzazione = aModel.mDataVisualizzazione;
    this.mCodUtenteVisualizzazione = aModel.mCodUtenteVisualizzazione;
    
    this.mDataInserimentoAl = aModel.mDataInserimentoAl;
  }
  
  // Metodi Getter  
  public BigDecimal getIdErroriSiesPagopa()       { return mIdErroriSiesPagopa;  }
  public BigDecimal getIdFascicoloSiep()          { return mIdFascicoloSiep;  }
  public BigDecimal getIdEvento()                 { return mIdEvento;  }
  public String     getAzioneContestoJava()       { return mAzioneContestoJava;  }
  public String     getDescrizioneFunzione()      { return mDescrizioneFunzione;  }
  public String     getCodUtente()                { return mCodUtente;  }
  public String     getCodUfficio()               { return mCodUfficio;  }
  public String     getErroreEsecuzione()         { return mErroreEsecuzione;  }
  public Date       getDataInserimento()          { return mDataInserimento;  }
  public Date       getDataVisualizzazione()      { return mDataVisualizzazione;  }
  public String     getCodUtenteVisualizzazione() { return mCodUtenteVisualizzazione;  }
  
  //Campi aggiunti per esito ricerche
  public Date       getDataInserimentoAl() { return mDataInserimentoAl;  }
  public BigDecimal getChiaveAnno()        { return mChiaveAnno; }
  public BigDecimal getChiaveProgr()       { return mChiaveProgr; }
  public String     getCognome()           { return mCognome; }
  public String     getNome()              { return mNome; }
  public String     getDescMotivoEvento()  { return mDescMotivoEvento; }
  public Date       getDataEmissione()     { return mDataEmissione;  }
  
  //Metodi Setter
  public void setIdErroriSiesPagopa (BigDecimal mIdErroriSiesPagopa) { this.mIdErroriSiesPagopa = mIdErroriSiesPagopa; }
  public void setIdFascicoloSiep (BigDecimal mIdFascicoloSiep) { this.mIdFascicoloSiep = mIdFascicoloSiep; }
  public void setIdEvento (BigDecimal mIdEvento) { this.mIdEvento = mIdEvento; }
  public void setAzioneContestoJava (String mAzioneContestoJava) { this.mAzioneContestoJava = mAzioneContestoJava; }
  public void setDescrizioneFunzione (String mDescrizioneFunzione) { this.mDescrizioneFunzione = mDescrizioneFunzione; }
  public void setCodUtente (String mCodUtente) { this.mCodUtente = mCodUtente; }
  public void setCodUfficio (String mCodUfficio) { this.mCodUfficio = mCodUfficio; }
  public void setErroreEsecuzione (String mErroreEsecuzione) { this.mErroreEsecuzione = mErroreEsecuzione; }
  public void setDataInserimento (Date mDataInserimento) { this.mDataInserimento = mDataInserimento; }
  public void setDataVisualizzazione (Date mDataVisualizzazione) { this.mDataVisualizzazione = mDataVisualizzazione; }
  public void setCodUtenteVisualizzazione (String mCodUtenteVisualizzazione) { this.mCodUtenteVisualizzazione = mCodUtenteVisualizzazione; }
 
  // Campi aggiunti per esito ricerche
  public void setDataInserimentoAl (Date mDataInserimentoAl) { this.mDataInserimentoAl = mDataInserimentoAl; }
  public void setChiaveAnno  (BigDecimal mChiaveAnno) { this.mChiaveAnno = mChiaveAnno; }
  public void setChiaveProgr (BigDecimal mChiaveProgr) { this.mChiaveProgr = mChiaveProgr;  }
  public void setCognome     (String mCognome) { this.mCognome = mCognome;  }
  public void setNome        (String mNome) { this.mNome = mNome;  }
  public void setDescMotivoEvento( String mDescMotivoEvento) { this.mDescMotivoEvento = mDescMotivoEvento;  }
  public void setDataEmissione (Date mDataEmissione) { this.mDataEmissione = mDataEmissione;  }
  
  public String toString() {
    String lStr = new String();

    lStr = "ErroriSiesPagopaModel: \n" ;
    lStr += "[ mIdErroriSiesPagopa       = " + mIdErroriSiesPagopa + " ]\n";
    lStr += "[ mIdFascicoloSiep          = " + mIdFascicoloSiep + " ]\n";
    lStr += "[ mIdEvento                 = " + mIdEvento + " ]\n";
    lStr += "[ mAzioneContestoJava       = " + mAzioneContestoJava + " ]\n";
    lStr += "[ mDescrizioneFunzione      = " + mDescrizioneFunzione + " ]\n";
    lStr += "[ mCodUtente                = " + mCodUtente + " ]\n";
    lStr += "[ mCodUfficio               = " + mCodUfficio + " ]\n";
    lStr += "[ mErroreEsecuzione         = " + mErroreEsecuzione + " ]\n";
    lStr += "[ mDataInserimento          = " + mDataInserimento + " ]\n";
    lStr += "[ mDataVisualizzazione      = " + mDataVisualizzazione + " ]\n";
    lStr += "[ mCodUtenteVisualizzazione = " + mCodUtenteVisualizzazione + " ]\n";
    // Campi aggiunti per esito ricerche
    lStr += "[ mDataInserimentoAl        = " + mDataInserimentoAl + " ]\n";
    lStr += "[ mChiaveAnno               = " + mChiaveAnno + " ]\n";
    lStr += "[ mChiaveProgr              = " + mChiaveProgr + " ]\n";
    lStr += "[ mCognome                  = " + mCognome + " ]\n";
    lStr += "[ mNome                     = " + mNome + " ]\n";
    lStr += "[ mDescMotivoEvento         = " + mDescMotivoEvento + " ]\n";
    lStr += "[ mDataEmissione            = " + mDataEmissione + " ]";

    return lStr;
  }






  
  
}

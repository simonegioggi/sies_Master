package siap.sico.jms.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class PresaInCaricoModel extends GenericModel {

  private static final long serialVersionUID = 253433991405896906L;
  
  private BigDecimal mIdPresaInCarico;
  private BigDecimal mFasSieIdFascicoloSiep;

  private Date   mDataPresaInCarico; 
  private String mCodOperatorePresaInCarico;
  private String mCodUfficioPresaInCarico;
  
  
  // COSTRUTTORE DI DEFAULT
  public PresaInCaricoModel() {
    this.mIdPresaInCarico = null;
    this.mFasSieIdFascicoloSiep = null;
    this.mDataPresaInCarico = null;
    this.mCodOperatorePresaInCarico = "";
    this.mCodUfficioPresaInCarico = "";
  }

  public PresaInCaricoModel(BigDecimal aIdPresaInCarico, BigDecimal aFasSieIdFascicoloSiep, Date aDataPresaInCarico
      , String aCodOperatorePresaInCarico, String aCodUfficioPresaInCarico) {
    this.mIdPresaInCarico = aIdPresaInCarico;
    this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
    this.mDataPresaInCarico = aDataPresaInCarico;
    this.mCodOperatorePresaInCarico = aCodOperatorePresaInCarico;
    this.mCodUfficioPresaInCarico = aCodUfficioPresaInCarico;
  }

  public BigDecimal getIdPresaInCarico()       { return mIdPresaInCarico;  }
  public BigDecimal getFasSieIdFascicoloSiep() { return mFasSieIdFascicoloSiep;  }
  public Date getDataPresaInCarico()           { return mDataPresaInCarico;  }
  public String getCodUfficioPresaInCarico()   { return mCodUfficioPresaInCarico;  }
  public String getCodOperatorePresaInCarico() { return mCodOperatorePresaInCarico;  }

  public void setIdPresaInCarico           (BigDecimal mIdPresaInCarico)       { this.mIdPresaInCarico = mIdPresaInCarico;  }
  public void setFasSieIdFascicoloSiep     (BigDecimal mFasSieIdFascicoloSiep) { this.mFasSieIdFascicoloSiep = mFasSieIdFascicoloSiep; }
  public void setDataPresaInCarico         (Date mDataPresaInCarico)           { this.mDataPresaInCarico = mDataPresaInCarico;  }
  public void setCodUfficioPresaInCarico   (String mCodUfficioPresaInCarico)   { this.mCodUfficioPresaInCarico = mCodUfficioPresaInCarico;  }
  public void setCodOperatorePresaInCarico (String mCodOperatorePresaInCarico) { this.mCodOperatorePresaInCarico = mCodOperatorePresaInCarico;  }

  public String toString() {
    String lStr = new String();

    lStr = "PresaInCaricoModel:\n" 
        + "[ mIdPresaInCarico           = " + mIdPresaInCarico + " ]\n"
        + "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
        + "[ mDataPresaInCarico         = " + mDataPresaInCarico + " ]\n"
        + "[ mCodOperatorePresaInCarico = " + mCodOperatorePresaInCarico + " ]\n"
        + "[ mCodUfficioPresaInCarico   = " + mCodUfficioPresaInCarico + " ]";

    return lStr;
  }        
}
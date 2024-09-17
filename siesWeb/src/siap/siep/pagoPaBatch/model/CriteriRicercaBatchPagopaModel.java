package siap.siep.pagoPaBatch.model;

import java.util.Date;

import f3b.model.GenericModel;

public class CriteriRicercaBatchPagopaModel extends GenericModel {
  private static final long serialVersionUID = 3481007881258575439L;
  
  private Date mDataInizioEsecuzioneDal;  
  private Date mDataInizioEsecuzioneAl;
  
  public CriteriRicercaBatchPagopaModel() {
    this.mDataInizioEsecuzioneDal = null;
    this.mDataInizioEsecuzioneAl = null;
  }  
  
  public Date getDataInizioEsecuzioneDal() {
    return mDataInizioEsecuzioneDal;
  }
  public Date getDataInizioEsecuzioneAl() {
    return mDataInizioEsecuzioneAl;
  }
  
  public void setDataInizioEsecuzioneDal(Date mDataInizioEsecuzioneDal) {
    this.mDataInizioEsecuzioneDal = mDataInizioEsecuzioneDal;
  }
  public void setDataInizioEsecuzioneAl(Date mDataInizioEsecuzioneAl) {
    this.mDataInizioEsecuzioneAl = mDataInizioEsecuzioneAl;
  }
  
}

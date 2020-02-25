package siap.sico.misuraalternativa.model;

import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>Title: MisuraAlternativaAggregatoModel</p>
 * <p>Description: Misura Alternativa Aggregato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 */
public class MisuraAlternativaAggregatoModel
{
  private MisuraAlternativaModel mMisuraAlternativa;
  private DepositoOrdinanzaPcModel mDepositoOrdinanzaPC;
  private DepositoDecretoModel mDepositoDecreto;
  private TenoreModel[] mTenori;
  private EventoNotificaModel mEventoNotifica;
  private PenaResiduaModel mPenaResidua;
  private FungibilitaModel mFungibilita;
  private String mTipoMisura;

  public MisuraAlternativaAggregatoModel()
  {
    mTipoMisura = "";
    mMisuraAlternativa = new MisuraAlternativaModel();
    mDepositoOrdinanzaPC = new DepositoOrdinanzaPcModel();
    mDepositoDecreto = new DepositoDecretoModel();
    mEventoNotifica = new EventoNotificaModel();
    mPenaResidua = null;
    mFungibilita = null;
  }

//Metodi get
  public MisuraAlternativaModel getMisuraAlternativa() {return  mMisuraAlternativa;}
  public DepositoOrdinanzaPcModel getDepositoOrdinanzaPc() {return  mDepositoOrdinanzaPC;}
  public DepositoDecretoModel getDepositoDecreto() {return  mDepositoDecreto;}
  public TenoreModel[] getTenori() {return  mTenori;}
  public EventoNotificaModel getEventoNotifica() {return  mEventoNotifica;}
  public PenaResiduaModel getPenaResidua() {return  mPenaResidua;}
  public FungibilitaModel getFungibilita() {return  mFungibilita;}
  public String getTipoMisura() {return  mTipoMisura ;}

//Metodi set
  public void setMisuraAlternativa(MisuraAlternativaModel aValore) {  mMisuraAlternativa = aValore;}
  public void setDepositoOrdinanzaPc(DepositoOrdinanzaPcModel aValore) {  mDepositoOrdinanzaPC = aValore;}
  public void setDepositoDecreto(DepositoDecretoModel aValore)         {  mDepositoDecreto = aValore;}
  public void setTenori(TenoreModel[] aValore) {  mTenori = aValore;}
  public void setEventoNotifica(EventoNotificaModel aValore) {  mEventoNotifica = aValore;}
  public void setPenaResidua( PenaResiduaModel aValore) {  mPenaResidua = aValore;}
  public void setFungibilita( FungibilitaModel aValore) {  mFungibilita = aValore;}
  public void setTipoMisura(String aValore) {  mTipoMisura = aValore;}

}
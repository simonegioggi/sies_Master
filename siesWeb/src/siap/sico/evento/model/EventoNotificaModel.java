package siap.sico.evento.model;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.riepilogoprovvedimento.model.RiepilogoProvvedimentoModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
//import siap.sius.fascicolo.model.FascicoloSiusModel;
//import siap.sius.fascicolo.model.FascicoloGPModel;  // STUB 19/04/2005
import siap.sius.fascicolo.model.FascicoloGPTPModel;  // STUB 26/04/2005
import f3b.model.GenericModel;

/**
* <p>Title: EventoNotificaModel</p>
* <p>Description: Classe Model che rappresenta l'Evento legata alla Notifica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class EventoNotificaModel extends GenericModel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -7612993732286800429L;
private  EventoModel mEvento;
 // private  MagistratoCompetenteMagistratoModel mMagistrato;
  private  MagistratoModel mMagistrato;
  private  NotificaModel[] mNotifiche;
  private  String mNomeTemplate;
  private  CampoNotaModel[] mCampoNote;
  private  AvvocatoSiepModel[] mAvvocati;
  private  AvvocatoSiusModel[] mAvvocatiSius;

  //GDV - Aggiunto per il trasferimento dell'Evento...
  //private  FascicoloSiusModel mFascicoloSius;
  //STUB 19/04/2005 Vincenzo - Il Fascicolo SIUS deve viaggiare con i dati aggregati.
  //private  FascicoloGPModel mFascicoloGP;
  private  FascicoloGPTPModel mFascicoloGPTP;

  //Aggiunto per eventi migrati da RES
  private  RiepilogoProvvedimentoModel mRiepilogoProv;

  private  AvvocatoSigeModel[] mAvvocatiSige; //23/12/2008

  //COSTRUTTORE DI DEFAULT
  public EventoNotificaModel ()
  {
    mEvento = new EventoModel();
    mMagistrato = new MagistratoModel();
    mNomeTemplate = null;
    //mFascicoloSius = null;  // STUB 19/04/2005
    //mFascicoloGP = null;  // STUB 19/04/2005
    mFascicoloGPTP = null;  // STUB 26/04/2005
    mRiepilogoProv = null;
  }

  //COSTRUTTORE DI COPIA
  public EventoNotificaModel ( EventoNotificaModel aModel )
  {
    mEvento = aModel.getEvento();
    mNotifiche = aModel.getNotifiche();
    mMagistrato = aModel.getMagistrato();
    // mNomeTemplate = aModel.
    mCampoNote = aModel.getCampoNote();
    mAvvocati = aModel.getAvvocati();
    mAvvocatiSius = aModel.getAvvocatiSius();
    // mFascicoloSius = aModel.getFascicoloSius();  // STUB 19/04/2005
    // mFascicoloGP = aModel.getFascicoloGP();         // STUB 19/04/2005
    mFascicoloGPTP = aModel.getFascicoloGPTP();        // STUB 19/04/2005
    mRiepilogoProv = aModel.getRiepilogoProvvedimento();
  }


  public EventoNotificaModel ( EventoModel aModel )
  {
    mEvento = aModel;
  }

  //COSTRUTTORE MODEL
  public EventoNotificaModel (EventoModel aEvento, NotificaModel[] aNotifica)
  {
    this.mEvento = aEvento;
    this.mNotifiche = aNotifica;
  }

  //COSTRUTTORE MODEL
  public EventoNotificaModel (EventoModel aEvento, NotificaModel[] aNotifica, CampoNotaModel[] aCampoNote )
  {
    this.mEvento = aEvento;
    this.mNotifiche = aNotifica;
    this.mCampoNote = aCampoNote;
  }

  //
  // METODI GET()
  //
  public EventoModel getEvento()                              { return mEvento; }
  public NotificaModel[] getNotifiche()                       { return mNotifiche;}
  public MagistratoModel getMagistrato()                      { return mMagistrato;}
  public String getNomeTemplate()                             { return mNomeTemplate;}
  public CampoNotaModel[] getCampoNote()                      { return mCampoNote;}
  public AvvocatoSiepModel[] getAvvocati()                    { return mAvvocati;}
  public AvvocatoSiusModel[] getAvvocatiSius()                    { return mAvvocatiSius;}
  //public FascicoloSiusModel getFascicoloSius()                  { return mFascicoloSius;}  // STUB 19/04/2005
  //public FascicoloGPModel getFascicoloGP()                    { return mFascicoloGP;}      // STUB 19/04/2005
  public FascicoloGPTPModel getFascicoloGPTP()                  { return mFascicoloGPTP;}    // STUB 26/04/2005
  public RiepilogoProvvedimentoModel getRiepilogoProvvedimento()  { return mRiepilogoProv;}
  public AvvocatoSigeModel[] getAvvocatiSige()                    { return mAvvocatiSige;}
  //
  // METODI SET()
  //

  public void setEvento(EventoModel aValore )                             { mEvento = aValore;}
  public void setNotifiche(NotificaModel[] aValore)                       { mNotifiche = aValore;}
  public void setMagistrato( MagistratoModel aValore)                     { mMagistrato = aValore;}
  public void setNomeTemplate( String aValore )                           { mNomeTemplate = aValore;}
  public void setCampoNote(CampoNotaModel[] aValore)                      { mCampoNote = aValore;}
  public void setAvvocati(AvvocatoSiepModel[] aValore)                    { mAvvocati = aValore;}
  public void setAvvocatiSius(AvvocatoSiusModel[] aValore)                { mAvvocatiSius = aValore;}
  //public void setFascicoloSiusModel(FascicoloSiusModel aValore)           { mFascicoloSius = aValore;}  // STUB 10/04/2005
  //public void setFascicoloGP(FascicoloGPModel aValore)                    { mFascicoloGP = aValore;}    // STUB 10/04/2005
  public void setFascicoloGPTP(FascicoloGPTPModel aValore)                  { mFascicoloGPTP = aValore;}  // STUB 26/04/2005
  public void setRiepilogoProvvedimento(RiepilogoProvvedimentoModel aValore)  { mRiepilogoProv = aValore;}
  public void setAvvocatiSige(AvvocatoSigeModel[] aValore)                { mAvvocatiSige = aValore;}


  public String toString()
  {
    String lStr = new String();

    if (mEvento!=null)
      lStr = mEvento.toString() + " - "+ mNomeTemplate;


    if ( mNotifiche !=null)
    {
      int count = 0;
      while (count < mNotifiche.length)
      {
        lStr += mNotifiche[count].toString();
        count++;
      }
    }

    if (mMagistrato != null)
      lStr += " - "+ mMagistrato.toString();

    if ( mCampoNote !=null)
    {
      int count = 0;
      while (count < mCampoNote.length)
      {
        lStr += mCampoNote[count].toString();
        count++;
      }
    }
/*
    if ( mAvvocatiSius !=null)
    {
      int count = 0;
      while (count < mAvvocatiSius.length)
      {
        lStr += mAvvocatiSius[count].toString();
        count++;
      }

    }
*/
    return lStr;
  }
}
package siap.siep.provvedimentopm.action;


/**
* <p>Title: ActLoadInserisciProvvedimento</p>
* <p>Description: Classe Action per la load inserisci di Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActLoadInserisciProvvedimento extends ActionSiap implements ICostantiProvvedimento
{
  public String processRequest() throws F3BException
  {
    // STUB:20030219 - Da inserire in una classe a livello di siap
    // es.: siap.web.util
    if( isSessionAttributeNullObj("fascicolo") )
      throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare il procedimento." );

    if( isSessionAttributeNullObj("soggetto") && isSessionAttributeNullObj("sentenza") )
      throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare il soggetto e la sentenza." );

    if( isSessionAttributeNullObj("soggetto") )
      throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare il soggetto." );

    if( isSessionAttributeNullObj("sentenza") )
      throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare la sentenza." );

    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
    PosizioneGiuridicaModel lPosizioneModel = new PosizioneGiuridicaModel();
    lPosizioneModel.setFasSieIdFascicoloSiep (lFascicoloModel.getIdFascicoloSiep());

    IPosizioneGiuridica lPosGiuridica = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    //lPosGiuridica.ExRicercaPosizioneGiuridicaCorrente(lPosizioneModel);


    setRequestAttribute("posizione", lPosGiuridica.ExRicercaPosizioneGiuridicaCorrente(lPosizioneModel));
    setRequestAttribute("modalita","I");

    return PG_LOAD_INSERISCIPROVVEDIMENTO;  //restituisce la jsp di VIEW

  }
}
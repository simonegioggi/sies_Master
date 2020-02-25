package siap.siep.modulocumulo.action;


import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il caricamento delle Richieste Emesse/inviate dal P.M. dell'Esecuzione 
 * al GE relative ad una specifica istruttoria cumulo
 * 
 * @author 
 */
public class ActLoadVisualizzaRichieste extends ActionModuloCumulo implements ICostantiModuloCumulo
{
  /**
   * Recupera tutti le Richieste Emesse/Inviate e le singole voci(Richieste) associate
   */
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    //super.getDatiCumulo();
    
    IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
    Vector<RichiesteInviateCumModel> VecRichiesteInv = null;
    
 // Questa Query produce in Output un Elenco di richieste ; Il secondo parametro serve per sapere quali richieste cercare: tutte - dainviare - inviate 
    VecRichiesteInv = new Vector<RichiesteInviateCumModel>(lCtrlRic.ExRicercaRichiesteInviateCumuloByIdIstruttoria(lIstruttoriaModel.getIdIstruttoriaCumulo(),ICostantiRichiestePmInCumulo.RICHIESTA_AL_GE) );
    setRequestAttribute("ListaRichiesteInviate", VecRichiesteInv);

    return PG_LOAD_ELENCO_RICHIESTE_DEL_PM;
  }
}

package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il caricamento delle Griglia per la gestione dei Dati Analitici dei
 * singoli fascicoli (titoli) coinvolti nel Cumulo. 
 * 
 * @author 
 */

public class ActLoadGrigliaDatiAnalitici extends ActionModuloCumulo implements ICostantiModuloCumulo
{
  /**
   * Può essere invocata dalla lista dei fascicoli coinvolti
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
    super.getDatiIstruttoria();
    TitoloCumulatoModel lTitoloModel = super.getDatiTitoloCumulato();
    
    //==========================================================================
    // Recupero lo stato di esecuzione caricato
    //==========================================================================   
    IStatoEsecTitoloCumulato lCtrlSET = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    Vector <StatoEsecTitoloCumulatoModel> lListaEventiSET= lCtrlSET.ExRicercaStatoEsecTitoloCumulatoByIdTitolo(lTitoloModel.getIdTitoloCumulato());
    setRequestAttribute("StatoEsecuzioneTitolo", lListaEventiSET);    
    
    
    return PG_LOAD_GRIGLIA_DATI_ANALITICI;
  }
}

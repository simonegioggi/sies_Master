package siap.siep.modulocumulo.action;

import java.util.Vector;

import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IContinuazioneCumulo;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActListaTitoliPerContinuazione extends ActionModuloCumulo implements ICostantiPenaComplessivaCumulo
{
  public String processRequest() throws Exception
  {   
    //==========================================================================
    // Recupero i dati dei Istruttoria e Titolo Cumulato da passare alla form
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaCumMod =  super.getDatiIstruttoria();
    TitoloCumulatoModel lTitoloCorrente = super.getDatiTitoloCumulato();  
    
    //==========================================================================
    // Recupero i titoli in istruttoria per poter gestire la continuazione
    //==========================================================================
    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    Vector <TitoloCumulatoModel> lListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoria (lIstruttoriaCumMod.getIdIstruttoriaCumulo());
    setRequestAttribute("ListaTitoli", lListaTitoli);
    
    // Recupero le continuazioni 
    IContinuazioneCumulo lCtrlContinuazioni = SIEPLookupRemote.getContinuazioneCumuloRemote();
    Vector <ContinuazioneCumuloModel> lListaContinuazioni = lCtrlContinuazioni.ExRicercaContinuazioneByIDTitolo(lTitoloCorrente.getIdTitoloCumulato());
    setRequestAttribute("ListaContinuazioni", lListaContinuazioni);
    
    return PG_POPUP_TITOLI_PER_CONTINUAZIONE;  
  }
}

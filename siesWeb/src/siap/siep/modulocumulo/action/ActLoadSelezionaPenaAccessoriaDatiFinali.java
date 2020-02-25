package siap.siep.modulocumulo.action;

import java.util.Vector;

import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActLoadSelezionaPenaAccessoriaDatiFinali extends ActionModuloCumulo implements ICostantiModuloCumulo
{
  public String processRequest() throws F3BException
  {
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaMdel = super.getDatiIstruttoria();
    super.getDatiFinaliCumuloAggregato();  

    //==========================================================================
    // Effettua la ricerca delle PeneAccessorieCumuloModel con i deti del
    // relativo Titolo collegato
    //==========================================================================
    Vector <PenaAccessoriaCumuloModel> lElencoPA = new Vector <PenaAccessoriaCumuloModel>();
    IPenaAccessoriaCumulo lCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
    
    lElencoPA = lCtrl.ExRicercaPenaAccessoriaCumuloByIdIstruttoria(lIstruttoriaMdel.getIdIstruttoriaCumulo(),false);
    
    
    setRequestAttribute("ElencoPeneAccessorieInIstruttoria", lElencoPA);
    
    return PG_LOAD_SELEZIONA_PENA_ACCESSORIA;
  } 
}

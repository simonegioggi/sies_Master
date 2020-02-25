package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * Action Load del dettaglio della singola richiesta al GE con anticipazione 
 * degli effetti presente in dati finali cumulo
 * 
 * @author d.fiorletta
 *
 */
public class ActDettaglioRichiesteComputiDatiFinali extends ActionModuloCumulo 
   implements ICostantiDatiFinaliCumulo, ICostantiComputiCumulo
{

  public String processRequest() throws F3BException
  {
    
    super.getDatiIstruttoria();
    
    BigDecimal lIdComputo = getRequestBigDecimalParameter ( CAMPO_ID_COMPUTI_CUMULO) ;
    
    IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
    ComputiCumuloModel lComputoMod = lCtrlComputi.ExRicercaComputiCumuloById (lIdComputo);
    
    setRequestAttribute("ComputoCumulo", lComputoMod);
    
    return PG_LOAD_DETTAGLIO_RICHIESTE_GE;
    
  }
}

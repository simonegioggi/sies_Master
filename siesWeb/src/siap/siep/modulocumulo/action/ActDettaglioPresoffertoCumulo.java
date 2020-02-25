package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;

import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la visualizzazione del Dettaglio del provvedimento di presofferto
 * @author 
 *
 */
public class ActDettaglioPresoffertoCumulo  extends ActionModuloCumulo implements ICostantiComputiCumulo, ICostantiStatoEsecTitoloCumulato
{
  public String processRequest() throws F3BException 
  {
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
  
    
    BigDecimal idStatoEsec = null;
    if (!isRequestParameterNullObj(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO))
      idStatoEsec = getRequestBigDecimalParameter (CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
    
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    StatoEsecTitoloCumulatoModel lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull (idStatoEsec);  
        
    setRequestAttribute("Provvedimento", lStato);
    
    return PG_LOAD_DETTAGLIO_PRESOFFERTI_CUMULO;
  }
}

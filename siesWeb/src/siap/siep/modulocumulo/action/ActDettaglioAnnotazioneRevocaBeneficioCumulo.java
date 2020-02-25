package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la visualizzazione del Dettaglio dell'Annotazione di Revoca Sospensione/Non Menzione
 * @author Intersistemi Spa
 *
 */
public class ActDettaglioAnnotazioneRevocaBeneficioCumulo  extends ActionModuloCumulo implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo
{
  public String processRequest() throws F3BException 
  {
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
  
    
    BigDecimal idStatoEsec = null;
    if (!isRequestParameterNullObj(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO))
      idStatoEsec = getRequestBigDecimalParameter (CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
    
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    StatoEsecTitoloCumulatoModel lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoById(idStatoEsec); 
        
    setRequestAttribute("Provvedimento", lStato);
    
    
    return PG_DETTAGLIO_REVOCA_BENEFICIO_CUMULO;
  }
}

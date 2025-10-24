package siap.siep.misuraalternativa.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.web.ISIAPCostantiWeb;

/**
 * Action per agganciare la nuova griglia dei tasti della sospensione
 * 
 * 
 * @since MAV_9-SIEP 03.2024
 */
public class ActMisuraAlternativaGrigliaSospensione extends ActionSiap implements ICostantiMisuraAlternativa{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isEventoNonValidato();
    setRequestAttribute("strFunzione", "Sospensione Esecuzione Pena"); // è il titolo visualizzato nella griglia

    /*  VERIFICARE
    String lFlagSanzione = "N";
    if(!isRequestAttributeNullObj("lFlagSanzione") && "S".equals(getRequestAttribute("lFlagSanzione")))
    {
       lFlagSanzione = "S";                  
    }    
    setRequestAttribute("lFlagSanzione",lFlagSanzione);
    */
    
    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;
  }
}

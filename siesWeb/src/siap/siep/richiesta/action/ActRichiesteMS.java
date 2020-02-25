package siap.siep.richiesta.action;

import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

public class ActRichiesteMS extends ActionSiap
                            implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {
/*
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }
*/
    setRequestAttribute("strFunzione", "Richiesta al Magistrato dell'Esecuzione");

    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;
  }
}
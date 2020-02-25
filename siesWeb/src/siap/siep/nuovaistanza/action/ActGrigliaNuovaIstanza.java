package siap.siep.nuovaistanza.action;

import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

public class ActGrigliaNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza
{
  public String processRequest() throws F3BException
  {
    setRequestAttribute("strFunzione", "Iscrizione Istanza");
    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;
  }
}

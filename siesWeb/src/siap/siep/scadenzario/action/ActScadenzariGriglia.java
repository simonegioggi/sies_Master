package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

public class ActScadenzariGriglia extends ActionSiap implements ICostantiScadenzario
{
  public String processRequest() throws F3BException
  {
    setRequestAttribute("strFunzione", "Scadenzario");
    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;
  }
}

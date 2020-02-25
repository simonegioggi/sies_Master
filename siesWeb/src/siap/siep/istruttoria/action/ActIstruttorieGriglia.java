package siap.siep.istruttoria.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActIstruttorieGriglia extends ActionSiap implements ICostantiIstruttoria
{
  /*public String processRequest() throws F3BException
  {
    setRequestAttribute("strFunzione", "Istruttorie");
    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;
  }*/

  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
        {
          //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
          setRequestAttribute("fascicoloNotInSession", "S");
        }

    setRequestAttribute("strFunzione", "Istruttorie/Richieste");

    return ICostantiIstruttoria.PG_GRIGLIA_ISTRUTTORIE;
  }

}

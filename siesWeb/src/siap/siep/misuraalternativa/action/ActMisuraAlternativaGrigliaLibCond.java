package siap.siep.misuraalternativa.action;


import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

public class ActMisuraAlternativaGrigliaLibCond extends ActionSiap implements ICostantiMisuraAlternativa
{
  public String processRequest() throws F3BException
  {

  //avviso di pagina in costruzione
   return ISIAPCostantiWeb.PG_UNDER_CONSTRUCTION;

   /* scommentare appena si toglie l'avviso
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isEventoNonValidato();
    setRequestAttribute("strFunzione", "Liberazione Condizionale");

    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;*/
  }
}

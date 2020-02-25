package siap.siep.misuraalternativa.action;


import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

public class ActMisuraAlternativaGrigliaEspPressoDom extends ActionSiap implements ICostantiMisuraAlternativa
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isEventoNonValidato();
    setRequestAttribute("strFunzione", "Espiazione Pena Presso Domicilio");

    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;
  }
}

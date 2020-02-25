package siap.siep.sospensione.action;


import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * Action per il caricamento delle griglia dei bottoni del Differimento
 * @author Test01
 */
public class ActSospensioneGrigliaDiffer extends ActionSiap
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }
 
    this.isEventoNonValidato();
    setRequestAttribute("strFunzione", "Differimento");

    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;
  }
}

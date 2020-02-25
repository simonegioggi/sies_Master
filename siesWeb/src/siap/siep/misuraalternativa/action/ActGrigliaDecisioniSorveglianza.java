package siap.siep.misuraalternativa.action;


import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>Title: ActGrigliaDecisioniSorveglianza</p>
 * <p>Description: Action che visualizza la griglia delle funzioni Decisioni Sorveglianza </p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class ActGrigliaDecisioniSorveglianza extends ActionSiap implements ICostantiMisuraAlternativa
{
  public String processRequest() throws F3BException
  {
      if (this.isSessionAttributeNullObj("fascicolo"))
        {
          //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
         setRequestAttribute("fascicoloNotInSession", "S");
        }

    setRequestAttribute("strFunzione", "Decisioni Sorveglianza");

    return ICostantiMisuraAlternativa.PG_GRIGLIA_DECISIONE_SORVEGLIANZA;
  }
}

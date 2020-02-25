package siap.siep.beneficio.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * Inserimento della Revoca dei Benefici (menù verticale)
 * Nuova Bersione 4.0 Clo
 */
public class ActGrigliaRevocaBenefici extends ActionSiap
                            implements ICostantiBeneficio
{  
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

    setRequestAttribute("strFunzione", "Revoca Benefici in altro provvedimento");

    return PG_GRIGLIA_REVOCA_BENEFICIO;

  }
}

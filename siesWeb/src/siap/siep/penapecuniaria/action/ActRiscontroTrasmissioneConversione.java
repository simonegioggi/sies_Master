package siap.siep.penapecuniaria.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * gestione Conversione Pena Pecuniaria
 * per 4.0
 * 
 */
public class ActRiscontroTrasmissioneConversione extends ActionSiap
                                    implements ICostantiPenaPecuniaria
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

    setRequestAttribute("strFunzione", "Gestione Conversione Pene Pecuniarie");

    return PG_GRIGLIA_CONVERSIONE;
  }
}

package siap.siep.annotazionemanuale.action;

import siap.sico.web.ActionSiap;
//import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * Decisioni del GE (menù verticale)
 *
 */
public class ActDecisioneGE extends ActionSiap
                            implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

    setRequestAttribute("strFunzione", "Decisione del GE");

    return PG_GRIGLIA_DECISIONI_GE;
  }
}
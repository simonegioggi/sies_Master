package siap.siep.annotazionemanuale.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

//import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * Rideterminazione Pena (menù verticale)
 *
 */
public class ActRidetPena extends ActionSiap
                          implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

    setRequestAttribute("strFunzione", "Rideterminazione della pena");

    return PG_GRIGLIA_RIDETERMINAZIONE_PENA;
  }
}
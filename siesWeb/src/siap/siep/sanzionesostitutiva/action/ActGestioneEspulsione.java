package siap.siep.sanzionesostitutiva.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * Gestione  (Espulsione) Sanzioni Sostitutive.
 *
 */
public class ActGestioneEspulsione extends ActionSiap
                            implements ICostantiSanzioneSostitutiva
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

    setRequestAttribute("strFunzione", "Espulsione");

    return PG_GRIGLIA_ESPULSIONE;
  }
}

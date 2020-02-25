package siap.siep.sanzionesostitutiva.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * Gestione Riscontro Trasmissione Atti
 *
 */
public class ActGestioneRiscontroTrasmissioneAtti extends ActionSiap
                            implements ICostantiSanzioneSostitutiva
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

    setRequestAttribute("strFunzione", "Riscontro Trasmissione Atti per Esecuzione");

    return PG_GRIGLIA_RISCONTRO_TRASMISSIONE;
  }
}

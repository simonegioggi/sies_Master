package siap.siep.misurasicurezza.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;

/**
 * Action ActGestioneRichiesteTrasmissioni  per il caricamento della pagina con la griglia dei bottoni per la
 * gestione delle Richieste / Trasmissioni Misure di Sicurezza
 * per 4.0
 * 
 */
public class ActGestioneRichiesteTrasmissioni extends ActionSiap
                                    implements ICostantiMisuraSicurezza,ICostantiFascicoloSiep
{
	  public String processRequest() throws F3BException
	  {
		    if (this.isSessionAttributeNullObj("fascicolo"))
		    {
		    	return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		    }
		
		    setRequestAttribute("strFunzione", "Richieste / Trasmissione Atti Misure di Sicurezza");
		
		    return PG_GRIGLIA_MISURE_SICU_RICH;
	  }
}

package siap.siep.misurasicurezza.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;

/**
 * Action ActGestioneMisureSicurezza  per il caricamento della pagina con la griglia dei bottoni per la
 * gestione Conversione delle Misure di Sicurezza
 * per 4.0
 * @author AMBROS
 * 
 */
public class ActGestioneMisureSicurezza extends ActionSiap
                                    implements ICostantiMisuraSicurezza,ICostantiFascicoloSiep
{
	  public String processRequest() throws F3BException
	  {
		    if (this.isSessionAttributeNullObj("fascicolo"))
		    {
		      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		    	setRequestAttribute("fascicoloNotInSession", "S");
		    }
		    setRequestAttribute("strFunzione", "Misure Sicurezza");
		
		    return PG_GRIGLIA_MISURE_SICU;
	  }
}

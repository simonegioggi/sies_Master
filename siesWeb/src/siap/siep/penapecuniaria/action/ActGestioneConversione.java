package siap.siep.penapecuniaria.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * gestione Conversione Pena Pecuniaria
 * per 4.0
 * 
 */
public class ActGestioneConversione extends ActionSiap
                                    implements ICostantiPenaPecuniaria,ICostantiFascicoloSiep
{
  public String processRequest() throws F3BException
  {
	/*	06-10-2015 : Passa anche senza fascicolo in sessione  	*/
  // if (this.isSessionAttributeNullObj("fascicolo"))
  //  {
    		////////   	setRequestAttribute("fascicoloNotInSession", "S");
  //    return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      
  //  }

    // Paolo Cherubini 20/04/2011
    // si accede a tali funzioni solo per fascicoli di classe VII
    // -----------------------------
    // Paolo Cherubini 10/06/2011
    // su segnalazione di Testa possono accedere tutte la classi
    // N.B. 
/*    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    int lFascProg = lFascMod.getChiaveProgr().intValue(); 
	 if  (lFascProg < 70000 || lFascProg > 80000)
	 {	  
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
	        "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + 
	        lFascMod.getChiaveProgr() + " " + "Il Procedimento non è di conversione delle pene pecuniarie");
	      return IWebConstants.PG_MESSAGE;
	 } // fine paolo
*/    
    setRequestAttribute("strFunzione", "Gestione Conversione Pene Pecuniarie");

    return PG_GRIGLIA_CONVERSIONE;
  }
}

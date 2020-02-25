package siap.siep.penapecuniaria.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * Action per il caricamento della pagina con la griglia dei bottoni per la
 * gestione Conversione Pena Pecuniaria
 * per 4.0
 * 
 */
public class ActLoadRiscontroTrasmissioneConversione extends ActionSiap
                                    implements ICostantiPenaPecuniaria
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

//  autorità per la conversione
//    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaSanzioni() , "-");
//    setRequestAttribute("autoritaConv", "" + lOption );
    
	 // autorità per la conversione
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutorita() , "-");
    lOption.setFilter( new String[] {"-","36", "99", "57","37", "97", "38"});
    setRequestAttribute("autoritaConv", "" + lOption );     
    
    setRequestAttribute("strFunzione", "Ricerca Atti Trasmessi per Conversione");

    return PG_LOAD_RISCONTRO_TRASMISSIONE_CONVERSIONE;
  }
}

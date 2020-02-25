package siap.sige.richiestaatti.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciRichiestaAltraAutorita </p>
 * <p>Description: Classe di Azione responsabile della composizione dei
 * dati per le combobox e ritorna la chiamata alla corrispondente JSP.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciRichiestaAltraAutorita extends ActionSiap
implements ICostantiRichiestaAtti
{
  
	public String processRequest() throws Exception
	  {
		
		
		// Riempimento  ComboBoX
	    Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
	    setRequestAttribute("autorita1", "" + lOption);			
		
	    
	    // Riempimento  seconda ComboBoX
	    Option lOption1 = new Option(DecodificheManager.getInstance().getTipoAutorita());
	    setRequestAttribute("autorita", "" + lOption1);

	    return PG_LOAD_RICHIESTA_ALTRAAUTORITA;
	 
	  }
}
	
  
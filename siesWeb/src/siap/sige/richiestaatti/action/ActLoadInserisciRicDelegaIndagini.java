package siap.sige.richiestaatti.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciRicDelegaIndagini </p>
 * <p>Description: Classe di Azione responsabile della composizione dei
 * dati per le combobox e ritorna la chiamata alla corrispondente JSP.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciRicDelegaIndagini extends ActionSiap
implements ICostantiRichiestaAtti
{
  
	public String processRequest() throws Exception
	  {
		
		String[] lFiltro = {"-","19" ,"20","28","32","58","59","60","61","70","71","72","73","74","79","92","93","94"}; 
		
		// Riempimento  ComboBoX
		
		
	    Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
	    lOption.setFilter( lFiltro );
	    setRequestAttribute("autorita1", "" + lOption);			
		
	    
	    // Riempimento  seconda ComboBoX
	    Option lOption1 = new Option(DecodificheManager.getInstance().getTipoAutorita());
	    setRequestAttribute("autorita", "" + lOption1);

	    return PG_LOAD_RICHIESTA_DELEGAINDAGINI;
	 
	  }
}
	
  
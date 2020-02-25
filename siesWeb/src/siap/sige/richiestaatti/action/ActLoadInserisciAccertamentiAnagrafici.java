package siap.sige.richiestaatti.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciAccertamentiAnagrafici </p>
 * <p>Description: Classe di Azione responsabile della composizione dei
 * dati per le combobox e ritorna la chiamata alla corrispondente JSP.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciAccertamentiAnagrafici extends ActionSiap implements ICostantiRichiestaAtti
{
  
	public String processRequest() throws Exception
	  {
		
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaSanzioni());
	    setRequestAttribute("autorita", "" + lOption );
	    
	    return PG_LOAD_RICHIESTA_ACCERTAMENTIANAGRAFICI;
	 
	  }
}
	
  
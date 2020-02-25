package siap.sige.richiestaatti.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciRicSIntegrale </p>
 * <p>Description: Classe di Azione responsabile della composizione dei
 * dati per le combobox e ritorna la chiamata alla corrispondente JSP.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciRicSIntegrale extends ActionSiap
implements ICostantiRichiestaAtti
{
  
	public String processRequest() throws Exception
	  {
		 // Riempimento  ComboBoX
		 // 28/06/2010 Sostituzione Elenco Autorità Emittenti
		 // Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());
		 Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente());
		 setRequestAttribute("autorita", "" + lOption);

	     return PG_LOAD_RICHIESTA_SENTENZAINTEGRALE;
	 
	  }
}
	
  
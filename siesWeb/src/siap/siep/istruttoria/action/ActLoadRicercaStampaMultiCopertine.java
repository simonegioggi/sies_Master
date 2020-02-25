package siap.siep.istruttoria.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * ActLoadRicercaStampaMultiCopertine - 
 * @author Giselda De Vita
 *
 */
public class ActLoadRicercaStampaMultiCopertine extends ActionSiap implements ICostantiIstruttoria{
	 public String processRequest() throws F3BException
	  {
		 return ICostantiIstruttoria.PG_LOAD_RICERCA_COPERTINE;
	  }
}

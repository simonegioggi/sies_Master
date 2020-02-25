package siap.siep.istruttoria.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * ActLoadRicercaStampaMultiCopertine - 
 * @author Giselda De Vita
 *
 */
public class ActLoadRicercaStampaMultiInizioEsecuzione extends ActionSiap implements ICostantiIstruttoria{
	 public String processRequest() throws F3BException
	  {
		 return IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/LoadRicercaInizioEsecuzione.jsp";
	  }
}

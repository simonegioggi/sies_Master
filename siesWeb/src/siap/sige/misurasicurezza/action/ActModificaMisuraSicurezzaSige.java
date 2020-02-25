package siap.sige.misurasicurezza.action;


/**
* <p>Title: ActModificaMisuraSicurezzaSige</p>
* <p>Description: Classe Action per la modifica di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

import siap.siep.misurasicurezza.action.ActModificaMisuraSicurezza;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActModificaMisuraSicurezzaSige extends ActModificaMisuraSicurezza 
{

	public String processRequest() throws F3BException
    {
	    String lId = getRequestStringParameter(CAMPO_ID_MISURA_SICUREZZA);

		super.processRequest();
     
       //Prepara la "pagina" di destinActione.
       RedirectTo lRedirigi = new RedirectTo();
       lRedirigi.setPage(IWebConstants.PG_MAIN);
       lRedirigi.setAction("siap.sige.misurasicurezza.action.ActLoadDettaglioMisuraSicurezzaSige");
       lRedirigi.setParameter(CAMPO_ID_MISURA_SICUREZZA, lId);
       lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "10");
     
       return lRedirigi.toString();
       
    }
}
package siap.sige.beneficio.action;


/**
* <p>Title: ActModificaBeneficioSige</p>
* <p>Description: Classe Action per la modifica di Beneficio</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

import siap.siep.beneficio.action.ActModificaBeneficio;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActModificaBeneficioSige extends ActModificaBeneficio
{

	public String processRequest() throws F3BException
    {
	    String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);

		super.processRequest();
     
       //Prepara la "pagina" di destinActione.
       RedirectTo lRedirigi = new RedirectTo();
       lRedirigi.setPage(IWebConstants.PG_MAIN);
       lRedirigi.setAction("siap.sige.beneficio.action.ActLoadDettaglioBeneficioSige");
       lRedirigi.setParameter(CAMPO_ID_BENEFICIO, lId);
       lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "10");
     
       return lRedirigi.toString();
       
    }
}
package siap.sige.penacomplessiva.action;


import org.apache.log4j.Logger;

import siap.siep.penacomplessiva.action.ActModificaPenaComplessiva;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
/**
 * <p>Title: ActModificaPenaCompSige</p>
* <p>Description: Classe Action per la modifica di una Pena Complessiva.
*  </p>
*  L'action specializza   siap.siep.penacomplessiva.action.ActModificaPenaComplessiva  
*  poichè la form di input utilizzata e la funzione di aggiornamento dati sono le stesse; l'unica 
*  cosa che cambia è la pagina di destinazione.
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @author luigi
* @version 1.0
 */

public class ActModificaPenaCompSige extends ActModificaPenaComplessiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : inizio");

	  super.processRequest();
	   
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : fine");

	  return paginaDestinazione("siap.sige.penacomplessiva.action.ActLoadDettaglioPenaCompSige");  //restituisce la jsp di VIEW
  }
  
  //Prepara la pagina di destinazione
	protected String paginaDestinazione(String aAction)  throws Exception
	{
	      // Costruzione della pagina di redirect
	      RedirectTo lRedirectTo = new RedirectTo();
		  lRedirectTo.setPage(IWebConstants.PG_MAIN);
		 
	     lRedirectTo.setAction(aAction);
		  // Passaggio dei parametri inerenti il bottone di ritorno
		  if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			  lRedirectTo.setParameter( IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO) );
		  else if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			  lRedirectTo.setParameter( IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO) );
		
		// Pagina di destinazione
		String lPage = lRedirectTo.toString();

		return lPage;
	}
}
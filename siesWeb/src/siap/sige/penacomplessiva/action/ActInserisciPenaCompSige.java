package siap.sige.penacomplessiva.action;


import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.siep.penacomplessiva.action.ActInserisciPenaComplessiva;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
/**
 * <p>Title: ActInserisciPenaCompSige</p>
* <p>Description: Classe Action per l'inserimento di Pena Complessiva.  </p>
*  L'action specializza   siap.siep.penacomplessiva.action.ActInserisciPenaComplessiva.
*  Poichè la form di input utilizzata è la stessa viene utilizzzata la funzione 
*  letturaDati(...) ereditata per leggere i dati dalla request e valorizzare quelli da inserire.
*  Viene poi richiamata la funzione specifica per l'inserimento della Pena Complessiva SIGE. 
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @author luigi
* @version 1.0
 */

public class ActInserisciPenaCompSige extends ActInserisciPenaComplessiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : inizio");

	   // Viene chiamata la funzione ereditata per leggere i dati dalla form e valorizzare la Pena Complessiva
	    letturaDati(null);
	   
	   // Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
		  BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
	  
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug("ID_FAS_SISGE_SENTENZA : " + lIdFasSigeSen);
	   
	   if(mPenaCompMod == null)
	        throw new F3BException( F3BException.USER_MESSAGE, "Specificare la Pena Complessiva !" );
	  
	    // Inserimento
	    IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
	    lCtrl.ExInserisciPenaCompSige(mPenaCompMod, mSanzSostMod, mContList, lIdFasSigeSen);
	   
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : fine");
	
	   //pagina di destinazione
	  return paginaDestinazione("siap.sige.penacomplessiva.action.ActLoadDettaglioPenaCompSige");  //restituisce la jsp di VIEW

  }
  
  /**
   * Prepara la pagina di destinazione da richiamare dopo l'inserimento.
   * @param aAction
   * @return
   * @throws Exception
   */
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
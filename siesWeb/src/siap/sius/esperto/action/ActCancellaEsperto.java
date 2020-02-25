package siap.sius.esperto.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActCancellaEsperto</p>
* <p>Description: Classe Action per la cancellazione di un Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActCancellaEsperto extends ActionSiap implements ICostantiEsperto
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    String lIdEsperto = getRequestStringParameter(CAMPO_ID_ESPERTO);
    if (lIdEsperto.equals("0"))
      throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la cancellazione di questo record: Esperto di default.");

    // Chiama il controller.
    IEsperto lCtrl = SIUSLookupRemote.getEspertoRemote();
    lCtrl.ExCancellaEsperto(getRequestBigDecimalParameter(CAMPO_ID_ESPERTO));

    // Setta la risposta nella request.
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    // Prepara la "pagina" di destinAction.
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    lRedirigi.setAction("siap.sius.esperto.action.ActLoadRicercaEsperto" );
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi );
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW  }
  }
}
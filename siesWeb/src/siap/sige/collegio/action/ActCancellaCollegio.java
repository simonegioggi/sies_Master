package siap.sige.collegio.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActCancellaCollegio</p>
* <p>Description: Classe Action per la cancellazione di un Collegio</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
public class ActCancellaCollegio extends ActionSiap implements ICostantiCollegio
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");

    // Chiama il controller.
    ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
    lCtrl.ExCancellaCollegio(getRequestBigDecimalParameter(CAMPO_ID_COLLEGIO));

    // Setta la risposta nella request.
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    // Prepara la "pagina" di destinAction.
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    lRedirigi.setAction("siap.sige.collegio.action.ActLoadRicercaCollegio" );
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi );
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW  }
  }
}
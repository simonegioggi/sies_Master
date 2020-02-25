package siap.sige.magistrato.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActCancellaMagistrato</p>
* <p>Description: Classe Action per la rimozione di un Magistrato</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
*/
public class ActCancellaMagistrato extends ActionSiap implements ICostantiMagistrato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    String lId = getRequestStringParameter(CAMPO_COD_MAGISTRATO);

    // Chiama il controller
    IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
    lCtrl.ExCancellaMagistrato(lId, getCodUfficioUtenteConnesso());

    // Imposta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    // Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sige.magistrato.action.ActLoadRicercaMagistrato" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW  }
  }
}
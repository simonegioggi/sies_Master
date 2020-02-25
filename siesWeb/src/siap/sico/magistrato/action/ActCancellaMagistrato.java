package siap.sico.magistrato.action;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActCancellaMagistrato</p>
* <p>Description: Classe Action per la cancellazione di un Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaMagistrato extends ActionSiap implements ICostantiMagistrato
{
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(CAMPO_COD_MAGISTRATO);

    if (lId.equals("-"))
      throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la cancellazione di questo record: magistrato di default.");

    // chiama il controller
    IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
    lCtrl.ExCancellaMagistrato(lId, this.getCodUfficioUtenteConnesso());

    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sico.magistrato.action.ActLoadRicercaMagistrato" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW  }
  }
}
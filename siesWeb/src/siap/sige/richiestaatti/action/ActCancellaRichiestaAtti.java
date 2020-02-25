package siap.sige.richiestaatti.action;


import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.richiestaatti.controller.IRichiestaAtti;
import siap.sius.util.SIUSLookupRemote;

/**
* <p>Title: ActCancellaRichiestaAtti</p>
* <p>Description: Classe Action per cancellare Richiesta Istruttoria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaRichiestaAtti extends ActionSiap
{
  public String processRequest() throws Exception
    {
    // chiama il controller per la cancellazione
    IRichiestaAtti lCtrl = SIUSLookupRemote.getRichiestaAttiRemote();
    lCtrl.ExCancellaRichiestaAtti(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO ));

    return  ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);
  }
}
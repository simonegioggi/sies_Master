package siap.siepe.attivita.action;

import siap.sico.web.ActionSiap;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.util.SIEPELookupRemote;

/**
* <p>Title: ActCancellaAttivita</p>
* <p>Description: Classe Action per cancellare l'Attività</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaAttivita extends ActionSiap
implements ICostantiAttivita
{
  public String processRequest() throws Exception
  {
    // chiama il controller
    IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote();
    lAttCtrl.ExCancellaAttivita(getRequestBigDecimalParameter(CAMPO_ID_ATTIVITA));

    String retPage = null;

    retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);
    return retPage;
  }
}

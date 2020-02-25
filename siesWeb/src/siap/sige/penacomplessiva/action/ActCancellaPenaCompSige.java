package siap.sige.penacomplessiva.action;

/**
 * <p>Title: ActCancellaPenaCompSige</p>
 * <p>Description: Azione di Cancellazione Pena Complessiva SIGE</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia </p>
 * @version 1.0
 */

import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;

public class ActCancellaPenaCompSige extends ActionSige implements ICostantiPenaComplessiva
{
  /**
   * Azione di cancellazione Pena Complessiva
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws Exception
   */
  public String processRequest() throws Exception
  {
    // riempie il model
    PenaComplessivaModel lMod = new PenaComplessivaModel();

    lMod.setIdPenaComplessiva(getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA));

    IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
    lCtrl.ExCancellaPenaComplessivaSige(lMod);

	String lRetPage = ritornoDopoCancellazione("Pena Complessiva cancellata", null);

    return lRetPage;
  }
}

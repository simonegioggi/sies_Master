package siap.sige.circostanza.action;

import siap.siep.circostanza.action.ICostantiCircostanza;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.circostanza.model.CircostanzaSigeModel;
import siap.sige.web.ActionSige;

public class ActCancellaCircostanzaSige extends ActionSige
{
  /**
   * Azione di cancellazione della Circostanza.
   */
	
  public String processRequest() throws Exception
  {
	gestioneRitorno();
    
	// il model viene valorizzato con l'ID della pena da cancellare
	CircostanzaSigeModel lCircostanza = new CircostanzaSigeModel();
    lCircostanza.setIdCircostanza(getRequestBigDecimalParameter(ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA));

    // chiama il controller
    ICircostanza lReaCtrl = SIEPLookupRemote.getCircostanzaRemote();
    lReaCtrl.ExCancellaCircostanza(lCircostanza);
 
    return ritornoDopoCancellazione("Circostanza cancellata", null);

  }
}

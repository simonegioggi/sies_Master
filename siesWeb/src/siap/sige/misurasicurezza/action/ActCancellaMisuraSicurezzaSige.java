package siap.sige.misurasicurezza.action;

import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.misurasicurezza.model.MisuraSicurezzaSigeModel;
import siap.sige.web.ActionSige;

public class ActCancellaMisuraSicurezzaSige extends ActionSige
{
  /**
   * Azione di cancellazione della Misura di Sicurezza.
   */
	
  public String processRequest() throws Exception
  {
	gestioneRitorno();
   
	// il model viene valorizzato con l'ID della misura da cancellare
    MisuraSicurezzaSigeModel lMisMod = new MisuraSicurezzaSigeModel();
    lMisMod.setIdMisuraSicurezza(getRequestBigDecimalParameter(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA));

    // chiama il controller
    IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
    lCtrl.ExCancellaMisuraSicurezza(lMisMod);
 
    return ritornoDopoCancellazione("Misura di Sicurezza cancellata", null);

  }
}

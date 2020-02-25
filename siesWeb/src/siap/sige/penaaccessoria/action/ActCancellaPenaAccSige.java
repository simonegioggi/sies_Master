package siap.sige.penaaccessoria.action;

import siap.siep.penaaccessoria.action.ICostantiPenaAccessoria;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.penaaccessoria.model.PenaAccSigeModel;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;

public class ActCancellaPenaAccSige extends ActionSige
{
  /**
   * Azione di cancellazione della Pena Accessoria.
   */
  public String processRequest() throws Exception
  {
	gestioneRitorno();
    
	// il model viene valorizzato con l'ID della pena da cancellare
	PenaAccSigeModel lPenMod = new PenaAccSigeModel();
	lPenMod.setIdPenaAccessoria(getRequestBigDecimalParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA));

    // chiama il controller
    IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
    boolean esistePAsostitutiva = lCtrl.ExistPASostitutiva(lPenMod.getIdPenaAccessoria()) ;
    if (esistePAsostitutiva)
      throw new F3BException(F3BException.USER_MESSAGE,"Operazione non consentita causa presenza P.A. sostitutiva: procedere prima alla cancellazione di quest'ultima !");
    else
      lCtrl.ExCancellaPenaAccessoria(lPenMod);

    return ritornoDopoCancellazione("Pena Accessoria cancellata", null);

  }
}

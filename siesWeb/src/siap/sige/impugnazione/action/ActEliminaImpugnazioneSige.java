package siap.sige.impugnazione.action;

import org.apache.log4j.Logger;

import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;

public class ActEliminaImpugnazioneSige extends ActionSige implements ICostantiImpugnazioneSige, ICostantiFascicoloSige{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
      this.gestioneRitorno();
      IImpugnazioneSige lCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();       
      ImpugnazioneSigeModel impugnazione = lCtrl.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
      lCtrl.ExEliminaImpugnazione(impugnazione);
      String ritorno=super.ritornoDopoCancellazione("L'impugnazione e' stata Cancellata.", null);
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( getClass().getName() + ".processRequest: fine" );
      return ritorno;
  }
}
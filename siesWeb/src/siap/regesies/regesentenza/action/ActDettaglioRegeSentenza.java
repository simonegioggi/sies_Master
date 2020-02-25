package siap.regesies.regesentenza.action;

//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.siep.sentenza.controller.SentenzaController;
import org.apache.log4j.Logger;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesentenza.controller.IRegeSentenza;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>Title: ActDettaglioProvvedimento</p>
 * <p>Description: Dettaglio Provvedimento proveniente da ReGe</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class ActDettaglioRegeSentenza extends ActionRegeSiap
   implements ICostantiRegeSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    // Parse della request
    String lId = getRequestStringParameter(CAMPO_ID_FILE);

    //Ricerca la sentenza in Rege
    IRegeSentenza lCtrl = RegeSiesLookupRemote.getRegeSentenzaRemote();
    RegeSentenzaModel lProv = lCtrl.ExRicercaRegeSentenzaByKey(lId);

    setRequestAttribute("regesentenza", lProv);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("RegeSentenza = " + lProv);

    String lPage ="";
    if(lProv.getCodTipoProvvedimento().equals("01"))
      lPage = PG_LOAD_DETTAGLIOREGESENTENZA;
    else
      lPage = PG_LOAD_DETTAGLIOREGE_DECRETO;

    return lPage;
  }
}
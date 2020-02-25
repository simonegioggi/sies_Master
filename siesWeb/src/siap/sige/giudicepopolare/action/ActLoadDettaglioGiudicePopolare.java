package siap.sige.giudicepopolare.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadDettaglioGiudicePopolare</p>
* <p>Description: Classe Action per la load dettaglio del GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadDettaglioGiudicePopolare extends ActionSiap 
implements ICostantiGiudicePopolare
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    setLinkRitorno();
    
    // Chiama il controller
    IGiudicePopolare lCtrl = SIGELookupRemote.getGiudicePopolareRemote();
    GiudicePopolareModel lGiuPopMod = 
      lCtrl.ExRicercaGiudicePopolareByKey(getRequestBigDecimalParameter(CAMPO_ID_GIUDICE_POPOLARE));

    // Passaggio alla request dei dati.
    setRequestAttribute("giudicepopolare", lGiuPopMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_DETTAGLIOGIUDICE_POPOLARE;
  }
}
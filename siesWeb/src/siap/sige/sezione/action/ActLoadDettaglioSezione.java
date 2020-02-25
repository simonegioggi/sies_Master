package siap.sige.sezione.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadDettaglioSezione</p>
* <p>Description: Classe Action per la load dettaglio della Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadDettaglioSezione extends ActionSiap implements ICostantiSezione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    setLinkRitorno();
    
    // chiama il controller
    ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
    SezioneModel lSezMod = lCtrl.ExRicercaSezioneByKey(getRequestBigDecimalParameter(CAMPO_ID_SEZIONE));

    // passaggio alla request
    setRequestAttribute("sezione", lSezMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_DETTAGLIOSEZIONE;
  }
}
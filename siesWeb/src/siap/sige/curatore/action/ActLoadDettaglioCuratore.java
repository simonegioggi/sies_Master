package siap.sige.curatore.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadDettaglioCuratore</p>
* <p>Description: Classe Action per la load dettaglio del Curatore</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadDettaglioCuratore extends ActionSiap implements ICostantiCuratore
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    setLinkRitorno();
    
    // chiama il controller
    ICuratore lCtrl = SIGELookupRemote.getCuratoreRemote();
    CuratoreModel lCurMod = lCtrl.ExRicercaCuratoreByKey(getRequestBigDecimalParameter(CAMPO_ID_CURATORE));

    // passaggio alla request
    setRequestAttribute("curatore", lCurMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_DETTAGLIOCURATORE;
  }
}
package siap.sige.aula.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadDettaglioAula</p>
* <p>Description: Classe Action per la load dettaglio dell'Aula</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActDettaglioAula extends ActionSiap implements ICostantiAula
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    setLinkRitorno();
    
    // Chiama il controller.
    IAula lCtrl = SIGELookupRemote.getAulaRemote();
    AulaUdienzaModel lAulaMod = lCtrl.ExRicercaAulaByKey(getRequestBigDecimalParameter(CAMPO_ID_AULA), getRequestBigDecimalParameter(CAMPO_ID_SEZIONE));

    // Passaggio alla request.
    setRequestAttribute("aula", lAulaMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_DETTAGLIO_AULA;
  }
}
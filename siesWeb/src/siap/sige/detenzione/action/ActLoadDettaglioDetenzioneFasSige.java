package siap.sige.detenzione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
/**
* <p>Title: ActLoadDettaglioDetenzioneFasSige</p>
* <p>Description: Classe Action per la load dettaglio Detenzione x Fascicolo SIGE </p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: </p>
* @version 1.0
*/
public class ActLoadDettaglioDetenzioneFasSige extends ActionSige implements ICostantiFasSigeDetenzione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    String lIdFSD = getRequestStringParameter(CAMPO_ID_FAS_SIGE_DETENZIONE);
    
    IFasSigeDetenzione lCtrlFSD = SIGELookupRemote.getFasSigeDetenzioneRemote();
    FasSigeDetenzioneModel lFasSigeDet = lCtrlFSD.ExRicercaFasSigeDetenzione(new BigDecimal(lIdFSD) );

    setRequestAttribute("FasSigeDetenzione", lFasSigeDet);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return PG_LOAD_DETTAGLIOLUOGODETENZIONE;
  }
}
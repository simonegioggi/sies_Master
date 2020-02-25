package siap.siepe.assistentesociale.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadDettaglioAssistenteSociale</p>
* <p>Description: Classe Action per la load dettaglio di AssistenteSociale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadDettaglioAssistenteSociale extends ActionSiap implements ICostantiAssistenteSociale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    // Chiama il controller e invoca il metodo per la ricerca dell'assistente sociale.
    IAssistenteSociale lCtrl = SIEPELookupRemote.getAssistenteSocialeRemote();
    AssistenteSocialeModel lAssSocMod = lCtrl.ExRicercaAssistenteSocialeByKey(getRequestBigDecimalParameter(CAMPO_ID_ASSISTENTE_SOCIALE));

    // Decodifica di Flag_stato.
    //Collection lCol = (DecodificheManager.getInstance()).getFlagStato();
    //lAssSocMod.setFlagStato(DecodificheUtils.getDescbyCode(lCol,lAssSocMod.getFlagStato()));
    //lAssSocMod.setDescrFlagStato(DecodificheUtils.getDescbyCode(lCol,lAssSocMod.getFlagStato()));

    // Passaggio alla request.
    setRequestAttribute("assistentesociale", lAssSocMod);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return PG_LOAD_DETTAGLIOASSISTENTESOCIALE;
  }
}
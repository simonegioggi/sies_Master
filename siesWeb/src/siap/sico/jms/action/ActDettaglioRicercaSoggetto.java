package siap.sico.jms.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
 * Dettaglio Esito ricerca su altre BDI
 * <p>Title: ActDettaglioRicercaSoggetto</p>
 * <p>Description: Detaglio dei risultati di una ricerca soggetto su altr BDI </p>
 * <p>Copyright: Copyright (c) 2002</p>
 */
public class ActDettaglioRicercaSoggetto extends ActionSiap implements ICostantiSicoJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws Exception
  {
    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKeyMultipleBDI(lIdMessage);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

    String lPageReturn = "";

    this.setRequestAttribute("soggetto", lParser.getSoggetto());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("Messaggio = " +lMess + "\nSOGGETTO = " + lParser.getSoggetto());

 //   if (lMess.getMessaggiCorrelati() != null)
 //   {
      lPageReturn = IWebConstants.ROOT_DIR + "files/siap/sico/jms/DettaglioMessaggioRicercaSoggetto.jsp";
 /*   }
    else
      lPageReturn = IWebConstants.ROOT_DIR + "files/siap/siep/jms/DettaglioFascicoloNonTrovato.jsp";
*/
    return lPageReturn;
  }

}
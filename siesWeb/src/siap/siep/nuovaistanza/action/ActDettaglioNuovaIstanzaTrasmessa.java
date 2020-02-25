package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;


/**
 * <p>Title: ActDettaglioNuovaIstanzaTrasmessa</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Agile</p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioNuovaIstanzaTrasmessa extends ActionSiap 
implements ICostantiNuovaIstanza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("inizio");
    
    setLinkRitorno();      
    
    BigDecimal lIdMessage = 
      getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = 
      lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    MessaggioModel lMessCorrelato = 
      lCrtl.ExRicercaMessaggioByCorrelationId(lMess.getIdMessaggio().toString());

    lMess.setMessaggioCorrelato(lMessCorrelato);

    setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

    setRequestAttribute("evento", lParser.getEvento());
    setSessionAttribute("fascicolo", lParser.getFascicolo());
    setRequestAttribute("nuovaistanza", lParser.getNuovaIstanza());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("fine");
    
    return PG_DETTAGLIO_MESSAGGIO_TRASMESSO;
  }
}
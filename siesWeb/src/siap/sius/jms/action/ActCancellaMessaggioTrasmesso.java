package siap.sius.jms.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;

/**
 * <p>Title: ActCancellaMessaggioTrasmesso</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActCancellaMessaggioTrasmesso extends ActionSiap implements ICostantiSiusJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    String lRectPage =  "";

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();

    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    MessaggioModel lMessCorr =  lCrtl.ExRicercaMessaggioByCorrelationId(lMess.getIdMessaggio().toString());

    lCrtl.ExCancellaMessaggio(lMess.getIdMessaggio());

    // Se esiste, viene cancellato anche il messaggio correlato.
    if (lMessCorr != null && lMessCorr.getIdMessaggio() != null )
      lCrtl.ExCancellaMessaggio(lMessCorr.getIdMessaggio());

    lRectPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", lRectPage);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    return lRectPage;
  }
}
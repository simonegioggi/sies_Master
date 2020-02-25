package siap.siep.istanza.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;

/**
 * <p>Title: ActDettaglioIstanzaTrasmessa</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioIstanzaTrasmessa extends ActionSiap implements ICostantiIstanza
{
  public String processRequest() throws Exception
  {
    this.setLinkRitorno();  //STUB 11/05/2005
    // SIAPReceiver.getInstance();

    //DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
    // tabella DI MESSAGGIO.

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    MessaggioModel lMessCorr =  lCrtl.ExRicercaMessaggioByCorrelationId(lMess.getIdMessaggio().toString());

    lMess.setMessaggioCorrelato(lMessCorr);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

    //this.setRequestAttribute("soggetto", lParser.getSoggetto());
    this.setRequestAttribute("evento", lParser.getEvento());
    //this.setRequestAttribute("fascicolo", lParser.getFascicolo());
    this.setSessionAttribute("fascicolo", lParser.getFascicolo());
    this.setRequestAttribute("istanza", lParser.getIstanza());

    return PG_DETTAGLIO_MESSAGGIO_TRASMESSO;
  }
}

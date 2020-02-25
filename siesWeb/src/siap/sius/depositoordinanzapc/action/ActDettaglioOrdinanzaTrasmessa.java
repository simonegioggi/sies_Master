package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;

/**
 * <p>Title: ActDettaglioIstanzaTrasmessa</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioOrdinanzaTrasmessa extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
  public String processRequest() throws Exception
  {
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

    this.setSessionAttribute("fascicoloSiusGP", lParser.getFascicoloGPSius());

    //EVENTO
    EventoNotificaModel lEveNot = lParser.getEvento();

/* !!!! IL COD_ESITO_TENORE piu' significativo = COD_ESITO EVENTO !!!
    //TENORI
    List lTenori = lParser.getTenori();

    if(lTenori != null && lEveNot != null && lEveNot.getEvento() != null)
    {
      for (int i=0; i<lTenori.size(); i++)
      {
        TenoreModel lTenore = (TenoreModel)lTenori.get(i);
        if (lTenore.getCodOggettoTenore().equals(lEveNot.getEvento().getCodMotivo()))
        {
          setRequestAttribute("tenoreEsito", lTenore );
          break;
        }
      }
    }
*/
    this.setRequestAttribute("eventoNotifica", lEveNot);

    return PG_DETTAGLIO_MESSAGGIO_TRASMESSO;
  }
}

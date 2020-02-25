package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;

/**
 * <p>Title: ActDettaglioIstanzaTrasmessa</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioOrdinanzaRicevuta extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
  public String processRequest() throws Exception
  {

   // SIAPReceiver.getInstance();

    //DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
    // tabella DI MESSAGGIO.

    this.gestioneRitorno();

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());


    if (lParser.getFascicoloGPSius() == null)
        throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Procedimento SIUS. " );

    if(lParser.getSoggetto()!=null)
         lParser.getFascicoloGPSius().getFascicoloSiusModel().setSoggetto(lParser.getSoggetto());
    else
     if(lParser.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto()==null)
         throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Soggetto." );

    this.setRequestAttribute("fascicoloSiusGP", lParser.getFascicoloGPSius());

    //EVENTO
    EventoNotificaModel lEveNot = new EventoNotificaModel();

    if (lParser.getEvento()!=null)
      lEveNot = lParser.getEvento();
    else
      throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione dell'Evento." );

    this.setRequestAttribute("eventoNotifica", lEveNot);

    return PG_DETTAGLIO_MESSAGGIO_RICEVUTO;
  }
}

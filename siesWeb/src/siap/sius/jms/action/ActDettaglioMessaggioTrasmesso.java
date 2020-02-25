package siap.sius.jms.action;

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
 * <p>Title: ActDettaglioMessaggioTrasmesso</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioMessaggioTrasmesso extends ActionSiap implements ICostantiSiusJMS
{
  public String processRequest() throws Exception
  {
    //DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
    // tabella DI MESSAGGIO.
    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();

    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    MessaggioModel lMessCorr =  lCrtl.ExRicercaMessaggioByCorrelationId(lMess.getIdMessaggio().toString());

    lMess.setMessaggioCorrelato(lMessCorr);

    this.setRequestAttribute("Messaggio", lMess);

    //ParserMessage lParser = new ParserMessage(lMess.getTreeModel());
    ParserMessage lParser;

    // STUB 04/11/2004 controllo di consistenza del fascicoloSiusGP presente nel messaggio.
    // Se la classe ParserMessage "guarda" i model inclusi nel blob del messaggio con un formato diverso, il parsing Fallisce!
    if (lMess.getTreeModel()!= null)
      lParser = new ParserMessage(lMess.getTreeModel());
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Messaggio non conforme al Modello attuale: Rivolgersi all'amministratore di sistema!");

    // STUB 04/11/04 this.setSessionAttribute("fascicoloSiusGP", lParser.getFascicoloGPSius());
    // Parsing del Fascicolo SIUS.
    if (lParser.getFascicoloGPSius()!=null)
      this.setSessionAttribute("fascicoloSiusGP", lParser.getFascicoloGPSius());
    else
      throw new SIUSException( SIUSException.USER_MESSAGE, "Errore! Procedimento SIUS non conforme al Modello attuale: Rivolgersi all'amministratore di sistema!" );

    // Parsing dell'EVENTO.
    EventoNotificaModel lEveNot = new EventoNotificaModel() ;
    if (lParser.getEvento()!=null)
    {
      lEveNot = lParser.getEvento();
      this.setRequestAttribute("eventoNotifica", lEveNot);
    }
    else
      throw new SIUSException( SIUSException.USER_MESSAGE, "Errore! l'EVENTO incluso nel messaggio non è conforme al Modello attuale: Rivolgersi all'amministratore di sistema!" );

    return PG_DETTAGLIO_MESSAGGIO_TRASMESSO;
  }
}

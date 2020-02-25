package siap.siepe.ricezioneatti.action;

 /**
 * <p>Title: ActDettaglioProvvedimentoRicevuto</p>
 * <p>Description: Classe Action per la load dettaglio documento ricevuto</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.SICOException;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;

public class ActDettaglioProvvedimentoRicevuto extends ActionSiap implements ICostantiRicezioneAtti
{

  public String processRequest() throws Exception
  {
    this.gestioneRitorno();

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

    //PARSER di EVENTO
    EventoNotificaModel lEveNot = new EventoNotificaModel();
    if (lParser.getEvento()!=null)
      lEveNot = lParser.getEvento();
    else
      throw new SICOException( SICOException.USER_MESSAGE, "Errore nella Ricezione dell'Evento riferito al Provvedimento Trasmesso. " );

    //PARSER di SOGGETTO
    SoggettoModel lSogg = new SoggettoModel();
    if (lParser.getSoggetto()!=null)
      lSogg = lParser.getSoggetto();
    else
      throw new SICOException( SICOException.USER_MESSAGE, "Errore nella Ricezione del Soggetto riferito al Provvedimento Trasmesso. " );

    //PARSER di SENTENZA
    SentenzaModel lSentenza = new SentenzaModel();
    if (lParser.getSentenza()!=null)
      lSentenza = lParser.getSentenza();
    else
      throw new SICOException( SICOException.USER_MESSAGE, "Errore nella Ricezione della Sentenza riferita al Provvedimento Trasmesso. " );

    //PARSER di FASCICOLO SIEP
    FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
    if (lParser.getFascicolo()!=null)
      lFascicolo = lParser.getFascicolo();
    else
      throw new SICOException( SICOException.USER_MESSAGE, "Errore nella Ricezione del Fascicolo SIEP riferito al Provvedimento Trasmesso. " );

    this.setRequestAttribute("eventoNotifica", lEveNot);
    this.setRequestAttribute("evento", lEveNot.getEvento());
    this.setRequestAttribute("fascicolo", lFascicolo);
    this.setRequestAttribute("soggetto", lSogg);
    this.setRequestAttribute("sentenza", lSentenza);

    return PG_DETTAGLIO_PROVVEDIMENTO_RICEVUTO;
  }
}
package siap.siepe.ricezioneatti.action;

 /**
 * <p>Title: ActDettaglioProvvedimentoRicevuto</p>
 * <p>Description: Classe Action per la load dettaglio documento ricevuto</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import org.apache.log4j.Logger;

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
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.log.LogF3B;

public class ActDettaglioRichiestaRelRicevuta extends ActionSiap implements ICostantiRicezioneAtti
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
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
      throw new SICOException( SICOException.USER_MESSAGE, "Errore nella Ricezione dell'Evento riferito alla Richiesta Relazione Trasmessa. " );

    //PARSER di SOGGETTO
    SoggettoModel lSogg = new SoggettoModel();
    if (lParser.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto()!=null)
      lSogg = lParser.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto();
    else
      throw new SICOException( SICOException.USER_MESSAGE, "Errore nella Ricezione del Soggetto riferito alla Richiesta Relazione Trasmessa. " );

    //PARSER di SENTENZA
    SentenzaModel lSentenza = new SentenzaModel();
    if (lParser.getSentenza()!=null)
      lSentenza = lParser.getSentenza();
    else
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "Sentenza assente nella Richiesta Relazione Trasmessa. " );

    //PARSER di FASCICOLO SIEP
    FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
    if (lParser.getFascicolo()!=null)
      lFascicolo = lParser.getFascicolo();
    else
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "Fascicolo SIEP assente nella Richiesta Relazione Trasmessa. " );

    //PARSER di FASCICOLO SIUS
    FascicoloGPModel lFasSiusGP = new FascicoloGPModel();
    if (lParser.getFascicoloGPSius()!=null)
      lFasSiusGP = lParser.getFascicoloGPSius();
    else
      throw new SICOException( SICOException.USER_MESSAGE, "Errore nella Ricezione del Fascicolo SIUS riferito alla Richiesta Relazione Trasmessa. " );

    //PARSER di Luogo Detenzione
    LuogoDetenzioneModel lLuoDet = new LuogoDetenzioneModel();
    if (lParser.getLuogoDetenzione()!=null)
      lLuoDet = lParser.getLuogoDetenzione();
    else
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "Luogo Detenzione assente nella Richiesta Relazione Trasmessa. " );

    this.setRequestAttribute("eventoNotifica", lEveNot);
    this.setRequestAttribute("evento", lEveNot.getEvento());
    this.setRequestAttribute("fascicoloSiusGP", lFasSiusGP);
    this.setRequestAttribute("luogoDetenzione", lLuoDet);
    this.setRequestAttribute("fascicolo", lFascicolo);
    this.setRequestAttribute("soggetto", lSogg);
    this.setRequestAttribute("sentenza", lSentenza);

    return PG_DETTAGLIO_RICHIESTA_REL_RICEVUTA;
  }
}
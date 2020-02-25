package siap.sius.presaincarico.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.sius.SIUSException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActVisualizzaRichiestaSiepeRicevuta</p>
 * <p>Description: Carica il Documento BLOB dal TreeModel di Attività Ricevuta </p>
 * <p>Copyright: Copyright (c) 2006</p>
 */

public class ActVisualizzaRichiestaSiepeRicevuta extends ActionSiap implements ICostantiPresaincarico
{
  /**
   * Azione di Visualizzazione della Richiesta SIEPE Ricevuta
   * <p>
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws Exception propaga errore di eccezione.
   */
  public String processRequest() throws Exception
   {
     this.gestioneRitorno();
  
     BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
  
     IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
     MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
     
     // Da Eliminare ?
     //this.setRequestAttribute("Messaggio", lMess);
  
     ParserMessage lParser = new ParserMessage(lMess.getTreeModel());
  
     //Esegue la lettura della Richiesta ricevuta.
     RichiestaModel lRichiestaRicevuta = new RichiestaModel();
     
     // Se il contenuto nel parse della richiesta è diverso da null
     // si esgue la lettura e la relativa istanza viene associata all'istanza 
     // del model.
     if (lParser.getRichiesta()!=null)
       lRichiestaRicevuta = lParser.getRichiesta();
     else
       throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Documento relativo alla Richiesta." );
  
     // Preleva il contenuto del BLOB, presente nel model di pertinenza.
     
     // Inizializza lo Streamer.
     ByteArrayOutputStream lReport = new ByteArrayOutputStream();
     // Verifica ed eventualmente recupera dal model il Document, il quale
     // viene scritto nello Streamer.
     if(lRichiestaRicevuta.getDocPerTrasferimento()!=null && lRichiestaRicevuta.getDocPerTrasferimento().length >1)
       lReport.write(lRichiestaRicevuta.getDocPerTrasferimento());
     // Se lo Streame è vuoto lancia errore di eccezione.
     if (lReport.size() == 0)
       throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Documento Associato");
  
     //Prepara la pagina di destinazione
     setRequestAttribute("report", lReport);
  
     return IWebConstants.PG_DOWNLOAD;
   }
}

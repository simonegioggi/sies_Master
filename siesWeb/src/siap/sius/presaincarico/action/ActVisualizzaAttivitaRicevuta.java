package siap.sius.presaincarico.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import siap.siepe.attivita.model.AttivitaModel;
import siap.sius.SIUSException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActVisualizzaAttivitaRicevuta</p>
 * <p>Description: Carica il Documento BLOB dal TreeModel di Attività Ricevuta </p>
 * <p>Copyright: Copyright (c) 2006</p>
 */

public class ActVisualizzaAttivitaRicevuta extends ActionSiap
  implements ICostantiPresaincarico
{

   public String processRequest() throws Exception
   {
     this.gestioneRitorno();

     BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

     IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
     MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

     this.setRequestAttribute("Messaggio", lMess);

     ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

     //ATTIVITA' Ricevuta.
     AttivitaModel lAttivitaRicevuta = new AttivitaModel();
     if (lParser.getAttivita()!=null)
       lAttivitaRicevuta = lParser.getAttivita();
     else
       throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Documento relativo all'Attività." );

     ByteArrayOutputStream lReport = new ByteArrayOutputStream();
     if(lAttivitaRicevuta.getDocPerTrasferimento()!=null && lAttivitaRicevuta.getDocPerTrasferimento().length >1)
			 lReport.write(lAttivitaRicevuta.getDocPerTrasferimento());

    if (lReport.size() == 0)
      throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Documento Associato");

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
    }
}

package siap.siepe.ricezioneatti.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.relazione.model.RelazioneModel;
import siap.siepe.richiesta.model.RichiestaModel;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActVisualizzaDocumentoRicevuto</p>
 * <p>Description: Carica il Documento BLOB dal TreeModel Ricevuto </p>
 * <p>Copyright: Copyright (c) 2006</p>
 */

public class ActVisualizzaDocumentoRicevuto extends ActionSiap
implements ICostantiRicezioneAtti
{
  
  /* 2007-06-25 Commentato e sostituito dal quello successivo, per la
   * gestione di altri tipi di documenti di atti ricevuti. 
   public String processRequest() throws Exception
   {
     this.gestioneRitorno();

     BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

     IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
     MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

     this.setRequestAttribute("Messaggio", lMess);

     ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

     //EVENTO Ricevuto.
     EventoModel lEventoRicevuto = new EventoModel();
     if (lParser.getEvento()!=null && lParser.getEvento().getEvento()!=null)
       lEventoRicevuto = lParser.getEvento().getEvento();
     else
       throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Documento." );

     ByteArrayOutputStream lReport = new ByteArrayOutputStream();
     if(lEventoRicevuto.getDocPerTrasferimento()!=null && lEventoRicevuto.getDocPerTrasferimento().length >1)
			 lReport.write(lEventoRicevuto.getDocPerTrasferimento());

    if (lReport.size() == 0)
      throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun Documento Associato");

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
    }
    */

  public String processRequest() throws Exception
  {
    this.gestioneRitorno();

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

    ByteArrayOutputStream lReport = new ByteArrayOutputStream();
    

    if (lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_ORDINANZA ) || 
        lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_DECRETO ) ||
        lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO ) ||
        lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RICHIESTA_RELAZIONE) ||
        lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_SENTENZA ) )
    {

      //EVENTO Ricevuto.
      EventoModel lEventoRicevuto = new EventoModel();
      if (lParser.getEvento()!=null && lParser.getEvento().getEvento()!=null)
      {
        lEventoRicevuto = lParser.getEvento().getEvento();
        if(lEventoRicevuto.getDocPerTrasferimento()!=null && lEventoRicevuto.getDocPerTrasferimento().length >1)
          lReport.write(lEventoRicevuto.getDocPerTrasferimento());
      }
      else
        throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Documento." );
      
    }
    else if (lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE))
    {
      // Richiesta Ricevuta
      RichiestaModel lRichiesta = new RichiestaModel();
      if (lParser.getRichiesta()!=null)
      {
        lRichiesta = lParser.getRichiesta();
        if(lRichiesta.getDocPerTrasferimento()!=null && lRichiesta.getDocPerTrasferimento().length >1)
          lReport.write(lRichiesta.getDocPerTrasferimento());
      }
      else
        throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Documento." );
    }
    else if (lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RELAZIONE_RICHIESTA_UEPE))
    {
      // Relazione di Richiesta UEPE Ricevuta
      RelazioneModel lRelazione = new RelazioneModel();
      if (lParser.getRichiesta()!=null && lParser.getRichiesta().getRelazioni()!=null &&
          lParser.getRichiesta().getRelazioni().length > 0 && lParser.getRichiesta().getRelazioni()[0] != null )
      {
        lRelazione = lParser.getRichiesta().getRelazioni()[0];
        if(lRelazione.getDocPerTrasferimento()!=null && lRelazione.getDocPerTrasferimento().length >1)
          lReport.write(lRelazione.getDocPerTrasferimento());
      }
      else
        throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Documento." );

    }
    else if (lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_ATTIVITA))
    {
      // Attivita Ricevuta
      AttivitaModel lAttivita = new AttivitaModel();
      if (lParser.getAttivita()!=null)
      {
        lAttivita = lParser.getAttivita();
        if(lAttivita.getDocPerTrasferimento()!=null && lAttivita.getDocPerTrasferimento().length >1)
          lReport.write(lAttivita.getDocPerTrasferimento());
      }
      else
        throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Documento." );
    }
    else if (lMess.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RELAZIONE_ATTIVITA))
    {
      // Relazione di Attivita UEPE Ricevuta
      RelazioneModel lRelazione = new RelazioneModel();
      if (lParser.getAttivita()!=null && lParser.getAttivita().getRelazioni()!=null &&
          lParser.getAttivita().getRelazioni().length > 0 && lParser.getAttivita().getRelazioni()[0] != null )
      {
        lRelazione = lParser.getAttivita().getRelazioni()[0];
        if(lRelazione.getDocPerTrasferimento()!=null && lRelazione.getDocPerTrasferimento().length >1)
          lReport.write(lRelazione.getDocPerTrasferimento());
      }
      else
        throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Documento." );
      
    }
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Tipo di Atto non gestito !" );

    
     if (lReport.size() == 0)
       throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun Documento Associato");

     //Prepara la pagina di destinazione
     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
   }
}
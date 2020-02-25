package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActDettaglioRelazioneRichiestaSiepeRicevuta</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioRelazioneRichiestaSiepeRicevuta extends ActionSiap implements ICostantiPresaincarico
{
  public String processRequest() throws Exception
  {
    gestioneRitorno();
    //Controllo che non si stia lavorando su una entità in modifica ad altri.
    LockModel lck = lockIfNotLocked("caricorichiesta", getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La presa in carico di questa Relazione Di Attività è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      //Prepara la "pagina" di destinazione
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction("siap.sius.presaincarico.action.ActLoadRicercaAttiSiepe");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

    FascicoloSiepeEstesoModel lFasSiepeEsteso = new FascicoloSiepeEstesoModel();
    if (lParser.getFascicoloSiepeEsteso()!=null)
      lFasSiepeEsteso =lParser.getFascicoloSiepeEsteso();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Procedimento SIEPE Esteso. <BR>Rivolgersi all'amministratore di sistema! " );
    
    // Soggetto.
    if (lParser.getFascicoloSiepeEsteso().getSoggetto()!=null)
      lFasSiepeEsteso.setSoggetto(lParser.getFascicoloSiepeEsteso().getSoggetto());
    else if (lParser.getFascicoloSiepeEsteso().getSoggetto()==null)
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!" );

    this.setRequestAttribute("FascicoloSiepeEsteso", lFasSiepeEsteso);
    
    //EVENTO
    /*
    EventoModel lEvento = new EventoModel();
    if (lParser.getFascicoloSiepeEsteso().getEvento()!=null)
      lEvento = lParser.getFascicoloSiepeEsteso().getEvento();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione dell'Evento. <BR>Rivolgersi all'amministratore di sistema!" );

    this.setRequestAttribute("evento", lEvento);
    */
    
    // FascicoloSiep.
    if (lParser.getFascicoloSiepeEsteso().getFascicoloSiep()!=null)
      this.setSessionAttribute("fascicolo", lParser.getFascicoloSiepeEsteso().getFascicoloSiep());

    // Richiesta Trasmessa.
    if (lParser.getRichiesta() != null)
      this.setRequestAttribute("richiesta", lParser.getRichiesta());

    // Relazione di Attività Trasmessa.
    if (lParser.getRelazione() != null)
      this.setRequestAttribute("relazione", lParser.getRelazione());

    // Mette in sessione il model di fascicolo inviato da Siepe.
    this.setSessionAttribute("FascicoloSiepeEsteso", lFasSiepeEsteso);

    return PG_DETTAGLIO_RELAZIONE_RICHIESTA_RICEVUTA;
  }
}
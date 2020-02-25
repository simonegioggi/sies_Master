package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.sius.SIUSException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActDettaglioRichiestaSiepeRicevuta</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioRichiestaSiepeRicevuta extends ActionSiap implements ICostantiPresaincarico
{
  /**
   * Azione di Dettaglio della Richiesta SIEPE Ricevuta
   * <p>
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws Exception propaga errore di eccezione.
   */
  public String processRequest() throws Exception
  {
    gestioneRitorno();
    //Controllo che non si stia lavorando su una entità in modifica ad altri.
    LockModel lck = lockIfNotLocked("caricorichiestasiepe", getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
    
    // L'entità è già presa in gestione da altro utente
    if (lck != null)
    {
      // Da Eliminare ?
      //String lPage = IWebConstants.PG_MESSAGE;
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La presa in carico di questa Richiesta è in gestione ad un altro utente! <BR>Riprovare più tardi!");
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
    
    // Verifica se nel parser esite l'oggetto del FascicoloSiepeEsteso
    // in caso di esito positivo viene associato al model di pertinenza.
    if (lParser.getFascicoloSiepeEsteso()!=null)
      lFasSiepeEsteso =lParser.getFascicoloSiepeEsteso();
    else
      throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Procedimento SIEPE Esteso. <BR>Rivolgersi all'amministratore di sistema! " );

    // Verifica se nel parser esite l'oggetto del Soggetto
    // in caso di esito positivo viene impostato nel model FascicoloSiepeEstesoModel.
    if (lParser.getFascicoloSiepeEsteso().getSoggetto()!=null)
      lFasSiepeEsteso.setSoggetto(lParser.getFascicoloSiepeEsteso().getSoggetto());
    else if (lParser.getFascicoloSiepeEsteso().getSoggetto()==null)
      throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!" );

    this.setRequestAttribute("FascicoloSiepeEsteso", lFasSiepeEsteso);

    // Recupera il FascicoloSiep.
    if (lParser.getFascicoloSiepeEsteso().getFascicoloSiep()!=null)
      this.setSessionAttribute("fascicolo", lParser.getFascicoloSiepeEsteso().getFascicoloSiep());

    // Richiesta Siepe Ricevuta.
    if (lParser.getRichiesta()!=null)
      this.setRequestAttribute("richiesta", lParser.getRichiesta());

    // Mette in sessione il model di fascicolo inviato da Siepe.
    this.setSessionAttribute("FascicoloSiepeEsteso", lFasSiepeEsteso);

    return PG_DETTAGLIO_RICHIESTA_SIEPE_RICEVUTA;
  }
}
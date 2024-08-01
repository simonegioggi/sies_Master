package siap.siep.istanza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPSender;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.jms.model.PresaInCaricoModel;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Classe action che effettua l'effettivo inserimento dei dati dell'istanza
 * trasmessa da altro ufficio tramite jms.
 * 
 * Dopo la presa in carico invia la risposta all'ufficio mittente.
 *  
 * @author 
 *
 */
public class ActConfermaPresaInCarico extends ActionSiap implements ICostantiJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    //Controllo che non si stia lavorando su una entità in modifica ad altri
    LockModel lck =
    lockIfNotLocked("caricoistanza", getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute (IWebConstants.MESSAGE_TEXT, "La presa in carico di questa istanza è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      return IWebConstants.PG_MESSAGE;
    }

//SIAPReceiver.getInstance(); eliminata! viene comunque effettuata dalla SIAPSender.send()

    
    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

    UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());
    String lEsito = "00000"; // Setto l'esito positivo

    MessaggioModel lMessIns =  new MessaggioModel(lMess);
    MessaggioModel lMessReturn =null;

    try
    {
      IPresaInCaricoJMS lPres = SIEPLookupRemote.getPresaInCarico();
      // MEV_2024-DNA - Si passano al controller anche le informazione su data utente e ufficio 
      //                che precede alla presa in carico
      PresaInCaricoModel lPresaIncaricoModel = new PresaInCaricoModel();
      lPresaIncaricoModel.setDataPresaInCarico (DateUtils.getSysDate());
      lPresaIncaricoModel.setCodOperatorePresaInCarico (getCodUtenteConnesso());
      lPresaIncaricoModel.setCodUfficioPresaInCarico (getCodUfficioUtenteConnesso());
      //lMessReturn  = lPres.ExInserisciIstanzaTrasmessa(lMessIns);
      lMessReturn  = lPres.ExInserisciIstanzaTrasmessa (lMessIns, lPresaIncaricoModel);
      // MEV_2024-DNA - FINE
    }
    catch (Exception ex)
    {
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.error("Exception nell'Inserimento dell'Istanza Trasmessa >>> " + ex);
       // se non rilanci l'eccezione non se ne accorge nessuno!!!!
    }

    //==========================================================================
    // Preparo ed invio il messaggio di risposta di avvenuta presa in carico
    //==========================================================================
    MessaggioModel lMessage = new MessaggioModel();

    lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
    lMessage.setCodBdiMittente(lBDI.getCodDistretto());
    lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setCodTipoMessaggio(ESITO);
    lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_ISTANZA);
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    //--- GDV---lMessage.setJmsCorrelationIdMessage(lMess.getJmsIdMessaggio());
    lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
    lMessage.setDataInvio(DateUtils.getSysDate());
    lMessage.setDataEsito(DateUtils.getSysDate());
    lMessage.setCodEsito(lEsito);
    lMessage.setTreeModel(lMess.getTreeModel());

    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

    //Elimino il messaggio di richiesta evaso.
    //--------- TEST ----- Per ORa non cancello -------- lCrtl.ExCancellaMessaggio(lIdMess);

    lMess.setFlagVisto("S");
    lMess.setDataEsito(DateUtils.getSysDate());
    lCrtl.ExModificaMessaggio(lMess);

    // setta la risposta nella request
   /*  setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Ricezione Istanza Completata e Esito rispedito al Mittente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.istanza.action.ActListaIstanzeRicevute" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;*/


     this.setRequestAttribute("Messaggio", lMessReturn);

     return f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/jms/RapportoTrasferimentoFascicolo.jsp";

  }
}
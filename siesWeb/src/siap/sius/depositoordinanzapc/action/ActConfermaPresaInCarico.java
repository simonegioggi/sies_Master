package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.lock.model.LockModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.SIUSException;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 *
 * <p>Title: ActConfermaPresaInCarico</p>
 * <p>Description: Prende in Carico l'ordinanza </p>
 */
public class ActConfermaPresaInCarico extends ActionSiap implements ICostantiJMS
{
  public String processRequest() throws Exception
  {
    //Controllo che non si stia lavorando su una entità in modifica ad altri
    LockModel lck =
    lockIfNotLocked("caricoordinanza", getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute (IWebConstants.MESSAGE_TEXT, "La presa in carico di questa ordinanza è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      return IWebConstants.PG_MESSAGE;
    }
    
    //
    if (   JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE")!=null
        && JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")
       )
    {
      SIAPReceiver.getInstance().testInArrivo();
      SIAPReceiver.getInstance().testInPartenza();
      SIAPReceiver.getInstance().testStampa();
    }
    else{
      SIAPReceiver.getInstance();
    }
      

    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    //DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
    // tabella DI MESSAGGIO.

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

    UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

    //!!!RENDERE DINAMICO!!!!
    String lEsito = "00000"; //Setto l'esito positivo

    MessaggioModel lMessIns        = new MessaggioModel(lMess);

    MisuraAlternativaModel lMisAlt = new MisuraAlternativaModel();
    // Luigi 30-06-2006
   // lMisAlt.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
    lMisAlt.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lMisAlt.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lMisAlt.setDataAggiornamento(DateUtils.getSysDate());

//    try
//    {
    /* Sostituito
      IPresaInCaricoJMS lPres = SIUSLookupRemote.getPresaInCarico();
      MessaggioModel lMessReturn  = lPres.ExPresaInCaricoOrdinanza(lMessIns, lMisAlt);
    */
      // lUIGI 18-06-2006 Viene chiamata la nuova Presa In Carico.
      // 13/12/2007 Cambiato il riferimento da SIUS.PresaInCaricoJMSController a SICO.PresaInCaricoController.
      //IPresaInCarico lPres = SIUSLookupRemote.getPresaInCaricoSIEPE();
      IPresaInCarico lPres = SICOLookupRemote.getPresaInCaricoRemote();
      MessaggioModel lMessReturn  = lPres.ExPresaInCaricoOrdinanza(lMessIns, lMisAlt);


//    }
//    catch (Exception ex)
//    {
//    }

    MessaggioModel lMessage = new MessaggioModel();

    lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());

    //>>>>>>>>> Destinazxione fissa e non da maschera.......

    lMessage.setCodBdiMittente(lBDI.getCodDistretto());
    lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setCodTipoMessaggio(ESITO);
    lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_ORDINANZA);
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
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Ricezione Ordinanza Completata e Esito rispedito al Mittente!");

    // setta il fascicolo interessato in sessione--27/12/04--viviana--dario
    //------------------------------------------------------------------------/
     ParserMessage lPars;
     FascicoloSiepModel lFascicoloRicevuto = new FascicoloSiepModel();
     if (lMessReturn.getTreeModel()!= null)
     {
       lPars = new ParserMessage(lMessReturn.getTreeModel());
       if(lPars.getFascicolo() != null && lPars.getFascicolo()!= null && lPars.getSentenza() != null &&
          lPars.getFascicoloGPSius() != null && lPars.getFascicoloGPSius().getFascicoloSiusModel() != null &&
          lPars.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto() != null)

       {
         lFascicoloRicevuto = lPars.getFascicolo();
         lFascicoloRicevuto.setSoggetto(lPars.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto());
         lFascicoloRicevuto.setSentenza(lPars.getSentenza());

         this.setSessionAttribute("fascicolo", lFascicoloRicevuto);
       }else
         throw new SIUSException(SIUSException.USER_MESSAGE, "Impossibile porre il Fascicolo SIEP in sessione!");
     }
     else
       throw new SIUSException(SIUSException.USER_MESSAGE, "Impossibile porre il Fascicolo SIEP in sessione!");
   //----------------------------------------------------------------------------/


    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction("siap.sius.depositoordinanzapc.action.ActListaOrdinanzeRicevute&FlagConfermaPresaInCarico=s");
   // setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    String retPage = ritornoDopoCancellazione("Presa in carico effettuata", lRedirigi.toString());


   // return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
   return retPage;
  }
}
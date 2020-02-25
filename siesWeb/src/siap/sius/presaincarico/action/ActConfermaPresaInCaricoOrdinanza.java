package siap.sius.presaincarico.action;

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
import siap.sico.evento.model.EventoModel;
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActConfermaPresaInCaricoOrdinanza extends ActionSiap implements ICostantiJMS
{
  public String processRequest() throws Exception
  {
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

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

    UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

    String lEsito = "00000"; //Setto l'esito positivo

    MessaggioModel lMessIns = new MessaggioModel(lMess);

    MisuraAlternativaModel lMisAlt = new MisuraAlternativaModel();
    lMisAlt.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
    lMisAlt.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lMisAlt.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lMisAlt.setDataAggiornamento(DateUtils.getSysDate());

    //IPresaInCaricoJMS lPres = SIUSLookupRemote.getPresaInCarico();
    IPresaInCarico lPres = SICOLookupRemote.getPresaInCaricoRemote();
    MessaggioModel lMessReturn  = lPres.ExPresaInCaricoOrdinanza(lMessIns, lMisAlt);

    MessaggioModel lMessage = new MessaggioModel();

    lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
    lMessage.setCodBdiMittente(lBDI.getCodDistretto());
    lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setCodTipoMessaggio(ESITO);
    lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_ORDINANZA);
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
    lMessage.setDataInvio(DateUtils.getSysDate());
    lMessage.setDataEsito(DateUtils.getSysDate());
    lMessage.setCodEsito(lEsito);
    lMessage.setTreeModel(lMess.getTreeModel());

    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

    //Esclusione del messaggio di richiesta evaso.
    lMess.setFlagVisto("S");
    lMess.setDataEsito(DateUtils.getSysDate());
    lCrtl.ExModificaMessaggio(lMess);

    //Prepara la "pagina" di destinAction
    ParserMessage lPars;
    if (lMessReturn.getTreeModel()!= null)
      lPars = new ParserMessage(lMessReturn.getTreeModel());
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Impossibile porre il Fascicolo SIEP in sessione!");

    EventoModel lEventoInviato = lPars.getEvento().getEvento();
    setSessionAttribute( "IdEventoInviato", lEventoInviato.getIdEvento() );

    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );

    String lMessageOut = "";
    SoggettoModel lSoggettoInviato = null;
    FascicoloSiepModel lFasSiepInviato = lPars.getFascicolo();

    FascicoloGPModel lFasSiusInviato = lPars.getFascicoloGPSius();


    if (lPars.getFascicoloGPSius() != null && lPars.getFascicoloGPSius().getFascicoloSiusModel() != null)
    {
      setSessionAttribute("fascicoloSiusGP", lFasSiusInviato);
      lSoggettoInviato = lPars.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto();
    }
    else
      lMessageOut += " Errore: Fascicolo SIUS non ricevuto ! \n";

    SentenzaModel lSentenzaInviata = lPars.getSentenza();
    if (lSentenzaInviata == null)

    if (lFasSiepInviato != null)
    {
      lFasSiepInviato.setSoggetto(lSoggettoInviato);
      lFasSiepInviato.setSentenza(lSentenzaInviata);
      setSessionAttribute("fascicolo", lFasSiepInviato);
    }
    else
    {
      removeSessionAttribute("fascicolo");  // Rimozione del fascicolo SIEP dalla sessione.
    }

    if(lSoggettoInviato != null &&
       lFasSiusInviato  != null )
    {
      lRedirigi.setAction( "siap.sius.iscrizioneprocedimento.action.ActLoadIscrProcedimentoDaSius" );
      setSessionAttribute("soggetto", lSoggettoInviato);
      setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
      lMessageOut += " Ricezione Ordinanza Completata e Esito rispedito al Mittente. \n";
      lMessageOut += " Iscrivere il procedimento Sius!";
    }
    else
    {
      lMessageOut += " Ricezione Ordinanza Priva di Procedimento di sorveglianza. \n";
      lMessageOut += " Non è possibile iscrivere il procedimento Sius!";
    }
    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, lMessageOut);
    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
  }
}
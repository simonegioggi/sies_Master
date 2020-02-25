package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IRicercaJMS;
import siap.siep.util.SIEPLookupRemote;


/**
 * Effettua la trasmissione telematica della richiesta atti
 * 
 * @author d.fiorletta
 *
 */
public class ActTrasferisciRichiestaTrasmissioneTitolo extends ActionSiap implements ICostantiJMS
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
    
    
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    // Recupero l'evento
    BigDecimal lIdEvento =  getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);    
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();    
    EventoNotificaModel lEveMod = new EventoNotificaModel();        
    lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
    
    // Recupero i dati della COMPETENZA
    ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();  
    CompetenzaModel mCompModel = (CompetenzaModel)lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);     

    

    // Recupero i dati del Soggetto da aggiungere a messaggio model
    SoggettoModel lSogg = new SoggettoModel(lFascicoloModel.getSoggetto()); 
    

    //==========================================================================
    // Preparo il messaggio di invio.
    // Ciò che viene inviato è l'intero fascicolo, come nel caso del trasferimento
    // per competenza
    // FIXME basterebbero solo il fascicolo, sentenza e soggetto
    //       i dati non verranno acquisiti pa servono solo per precompilare la
    //       form di trasmissione
    //       Necessario anche l'ultimo record competenza
    //==========================================================================
    IRicercaJMS lCtrlMes = SIEPLookupRemote.getRicercaJMS();
    //MessaggioModel lMessage = lCtrlMes.ExRicercaFascicoloSiepPerTrasferimento(lFascicoloModel);
    MessaggioModel lMessage = lCtrlMes.ExRicercaFascicoloSiepPerRichiestaTrasferimento(lFascicoloModel);
    
    //==========================================================================
    // BDI e Ufficio mittente
    // n.b. Recupero i dati dell'ufficio di Corte di Appello (BDI) per ottenere
    //      la descrizione della BDI da inserire nelle setStringProperty del JmsMessaggio
    //==========================================================================
    UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
    lMessage.setCodBdiMittente       (lBDIMittente.getCodUfficio());   // COD BDI
    lMessage.setDescrBdiMittente     (lBDIMittente.getDescrComune());  // COMUNE BDI es Distretto di POTENZA
    lMessage.setCodUfficioMittente   (getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente (this.getCodUtenteConnesso());
    
    //==========================================================================
    // BDI e Ufficio destinatario
    // n.b. Recupero i dati dell'ufficio di Corte di Appello (BDI) per ottenere
    //      la descrizione della BDI da inserire nelle setStringProperty del JmsMessaggio
    //==========================================================================
    String lCodiceUfficioDest = getRequestStringParameter(ICostantiJMS.COD_UFFICIO_DESTINATARIO);
    UfficioModel lUfficioDest     = getUfficioByCodUfficio(lCodiceUfficioDest);
    UfficioModel lBDIDestinataria = getUfficioByCodUfficio(lUfficioDest.getCodDistretto());
        
    lMessage.setCodBdiDestinataria     (lBDIDestinataria.getCodUfficio());
    lMessage.setDescrBdiDestinataria   (lBDIDestinataria.getDescrComune());     
    lMessage.setCodUfficioDestinatario (lCodiceUfficioDest);
    
    
    
    // Tipo Messaggio
    lMessage.setCodTipoMessaggio  (RICHIESTA);
    lMessage.setCodTipoOperazione (RICHIESTA_TRASMISSIONE_ATTI_PER_COMP);      
    lMessage.setDeliveryMode      (DELIVERY_MODE_INVIATO);
    lMessage.setCodEsito          ("-");
    
    lMessage.setDataInvio(DateUtils.getSysDate());
    
    // Inserisco le Motivazioni nelle note
    if( lEveMod!=null && lEveMod.getCampoNote()!= null && 
       	lEveMod.getCampoNote().length >0 && lEveMod.getCampoNote()[0] != null &&
   		lEveMod.getCampoNote()[0].getDescr() != null)
    {
    	lMessage.setNote(lEveMod.getCampoNote()[0].getDescr() );
    }
    
    // Dati del Soggetto
    lMessage.setNomeSoggetto     (lSogg.getNome());
    lMessage.setCognomeSoggetto  (lSogg.getCognome());
    lMessage.setDataNascita      (lSogg.getDataNascita());
    lMessage.setCodComuneNascita (lSogg.getCodComuneNascita());
    lMessage.setCodStatoNascita  (lSogg.getCodStatoNascita());
      
    // SETTA RIFERIMENTI FASCICOLO SIEP CHE SI RICHIEDE
    lMessage.setChiaveAnnoSiep    (mCompModel.getChiaveAnno());
    lMessage.setChiaveProgrSiep   (mCompModel.getChiaveProgr());
    lMessage.setChiaveUfficioSiep (mCompModel.getChiaveUfficio());

    // SETTA RIFERIMENTI AL FASCICOLO SIEP COMPETENTE (CUMULANTE)
    lMessage.setChiaveAnnoFasCumulante    (lFascicoloModel.getChiaveAnno());
    lMessage.setChiaveProgrFasCumulante   (lFascicoloModel.getChiaveProgr());
    lMessage.setChiaveUfficioFasCumulante (lFascicoloModel.getChiaveUfficio());

    //=======================
    // Invio del Messaggio
    //=======================
    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);
    
    //================================================================================================
    // AGGIORNO L'EVENTO CON LA DATA DI TRASMISSIONE E LA COMPETENZA CON ID MESSAGGIO DI RICHIESTA
    //================================================================================================
    lEveMod.getEvento().setDataTrasmissioneAtti (DateUtils.getSysDate());
    
    lEveMod.getEvento().setDataAggiornamento         (DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lEveMod.getEvento().setCodOperatoreAggiornamento (getCodUtenteConnesso());
    
    mCompModel.setIdMessaggiorichiesta(lMessage.getIdMessaggio());
    
    mCompModel.setDataAggiornamento         (DateUtils.getSysDate());
    mCompModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    mCompModel.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    
    lCompCtrl.ExModificaEvento_e_Competenza(lEveMod.getEvento(), mCompModel);

    //==========================================================================
    
    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Richiesta sottomessa al Sistema!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.modulocumulo.action.ActDettaglioRichiestaTrasmissioneTitolo" );
    lRedirigi.setParameter( ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lMessage.getIdMessaggio().toString() );
    lRedirigi.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString() );

    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;
    //================================================================================
    
  }
  
  
}

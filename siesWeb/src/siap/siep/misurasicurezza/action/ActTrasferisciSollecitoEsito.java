package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IRicercaJMS;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;



/**
 * <p>Title: ActTrasferisciSollecitoEsito</p>
 * <p>Description: Si occupa di inviare il Sollecito Esito Trasmissione per Competenza delle Misure di Sicurezza
 *  all'ufficio che ha in carico la richiesta</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author d.f.
 * @version 1.0
 */

public class ActTrasferisciSollecitoEsito extends ActionSiap implements ICostantiMisuraSicurezza, ICostantiJMS 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

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
    EventoModel lEveMod = new EventoModel();        
    lEveMod = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
    
    //
    //=========================================================
    // Recupero i dati del sollecito
    //=========================================================
    ISollecitoEsitoTrasmissione lCtrlSollecito = SIEPLookupRemote.getSollecitoEsitoTrasmissioneRemote();
    SollecitoEsitoTrasmissioneModel lSollecitoModel = lCtrlSollecito.ExRicercaSollecitoEsitoTrasmissioneByIdEvento(lEveMod.getIdEvento());

    
    // Recupero i dati del Soggetto da aggiungere a messaggio model
    SoggettoModel lSogg = new SoggettoModel(lFascicoloModel.getSoggetto()); 
    

    //==========================================================================
    // Preparo il messaggio di invo.
    // Ciò che viene inviato è l'intero fascicolo, come nel caso del trasferimento
    // per competenza
    //  TODO 'MS' verificare se i dati trasmessi sono sufficienti
    //==========================================================================
    IRicercaJMS lCtrlMes = SIEPLookupRemote.getRicercaJMS();
    MessaggioModel lMessage = lCtrlMes.ExRicercaFascicoloSiepPerTrasferimento(lFascicoloModel);
    
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
    String lCodiceUfficioDest     = lSollecitoModel.getCodUffSollecitato();
    UfficioModel lUfficioDest     = getUfficioByCodUfficio(lCodiceUfficioDest);
    UfficioModel lBDIDestinataria = getUfficioByCodUfficio(lUfficioDest.getCodDistretto());
        
    lMessage.setCodBdiDestinataria     (lBDIDestinataria.getCodUfficio());
    lMessage.setDescrBdiDestinataria   (lBDIDestinataria.getDescrComune());     
    lMessage.setCodUfficioDestinatario (lCodiceUfficioDest);
    
    
    
    // Tipo Messaggio
    lMessage.setCodTipoMessaggio  (RICHIESTA);
    lMessage.setCodTipoOperazione (SOLLECITO_TRASFERIMENTO_COMPETENZA_MS);
    lMessage.setDeliveryMode      (DELIVERY_MODE_INVIATO);
    lMessage.setCodEsito          ("-");
    
    lMessage.setDataInvio(DateUtils.getSysDate());
    
    // Dati del Soggetto
    lMessage.setNomeSoggetto     (lSogg.getNome());
    lMessage.setCognomeSoggetto  (lSogg.getCognome());
    lMessage.setDataNascita      (lSogg.getDataNascita());
    lMessage.setCodComuneNascita (lSogg.getCodComuneNascita());
    lMessage.setCodStatoNascita  (lSogg.getCodStatoNascita());
      
    //SETTA RIFERIMENTI FASCICOLO SIEP
    lMessage.setChiaveAnnoSiep    (lFascicoloModel.getChiaveAnno());
    lMessage.setChiaveProgrSiep   (lFascicoloModel.getChiaveProgr());
    lMessage.setChiaveUfficioSiep (lFascicoloModel.getChiaveUfficio());
    
    // Setto il irferimento al messaggio di Richiesta di cui si sollecita la risposta
    lMessage.setIdMessaggioSollecitato (lSollecitoModel.getMesIdMessaggioSollecitato().toEngineeringString());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Messaggioda inviare: "+lMessage);
    
    
    //=======================
    // Invio del Messaggio
    //=======================
    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);
    
    //===============================================
    // AGGIORNO L'EVENTO CON LA DATA DI TRASMISSIONE 
    //===============================================
    lEveMod.setDataTrasmissioneAtti (DateUtils.getSysDate());
    
    lEveMod.setDataAggiornamento         (DateUtils.getSysDate());
    lEveMod.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lEveMod.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    lCtrl.ExModificaEvento(lEveMod);
    //lCtrl.ExModificaEventoTrasmissioneCompetenza(lModel, lFascicoloModel,  "01");
    
    
    //==========================================================================
    
    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Provvedimento sottomessa al Sistema!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.misurasicurezza.action.ActLoadDettaglioSollecitoEsitoTrasmissione" );
    //lRedirigi.setParameter( ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lMessage.getIdMessaggio().toString() );
    lRedirigi.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString() );

    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;
    //================================================================================
    
  }
}
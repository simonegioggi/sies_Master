package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IRicercaJMS;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * Effettua la trasmissione telematica della comunicazione di cumulo alle Procure Competenti
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActTrasferisciComunicazioniProcure extends ActionSiap implements ICostantiJMS
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
    
    // Recupero i dati del Soggetto da aggiungere a messaggio model
    SoggettoModel lSogg = new SoggettoModel(lFascicoloModel.getSoggetto()); 
    

    //==========================================================================
    // Preparo il messaggio di invio.
    // Se diversa BDI allego il fascicolo cumulante per consentire all'utente 
    // di visualizzare eventualmente i dati del cumulante dopo presa in carico
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
    
    //=========================================================
    // Recupero le notifiche complete
    //=========================================================
    INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
    Vector<NotificaModel> lNotifiche = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(lIdEvento);

    // Preparazione e invio dei messaggi (1 per destinatario).
    for (int j=0; j<lNotifiche.size(); j++) {
    	
      NotificaModel lNotMod = lNotifiche.get(j);
      
      //FIXME mettere in try catch il singolo invio e loggare su DB l'avvenuto invio

      if (lNotMod.getUfficio()!=null  &&
          lNotMod.getUfficio().getCodUfficio()!=null &&
          lNotMod.getCurIdCuratore()!=null) 
      {
        // n.b. getCurIdCuratore = idTitolo e valorizzato solo per le Procure
        UfficioModel lUfficioDest     = getUfficioByCodUfficio(lNotMod.getUfficio().getCodUfficio());
        UfficioModel lBDIDestinataria = getUfficioByCodUfficio(lUfficioDest.getCodDistretto());
        
        lMessage.setCodBdiDestinataria     (lBDIDestinataria.getCodUfficio());
        lMessage.setDescrBdiDestinataria   (lBDIDestinataria.getDescrComune());     
        lMessage.setCodUfficioDestinatario (lNotMod.getUfficio().getCodUfficio());
    
        // Recupero i dati del procedimento Cumulato per i riferimenti al Fascicolo SIEP.
        ITitoloCumulato lTitCumCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();  
        BigDecimal lIdTitolo = lNotMod.getCurIdCuratore();
        
        TitoloCumulatoModel mTitCumModel = (TitoloCumulatoModel)lTitCumCtrl.ExRicercaTitoloCumulatoById (lIdTitolo);
        if (mTitCumModel!=null &&
            mTitCumModel.getIdTitoloCumulato()!=null) 
        {
          ProcedimentoCumulatoModel mProCumModel = (ProcedimentoCumulatoModel)lTitCumCtrl.ExRicercaProcedimentoCumulatoByIdTitolo( mTitCumModel.getIdTitoloCumulato() );
          if (mProCumModel!=null ) 
          {
            // Tipo Messaggio
            lMessage.setCodTipoMessaggio  (ESITO);
            //lMessage.setCodTipoMessaggio  (RICHIESTA);
            
            lMessage.setCodTipoOperazione (COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);      
            lMessage.setDeliveryMode      (DELIVERY_MODE_INVIATO);
            lMessage.setCodEsito          (ASSORBITO_IN_CUMULO);
            lMessage.setDataEsito(DateUtils.getSysDate());
            lMessage.setDataInvio(DateUtils.getSysDate());
        
            // Inserisco le Motivazioni nelle note
            if(   lEveMod.getCampoNote()!= null 
               && lEveMod.getCampoNote().length >0 
               && lEveMod.getCampoNote()[0] != null 
               && lEveMod.getCampoNote()[0].getDescr() != null)
            {
              lMessage.setNote(lEveMod.getCampoNote()[0].getDescr() );
            }
          
            // Dati del Soggetto
            lMessage.setNomeSoggetto     (lSogg.getNome());
            lMessage.setCognomeSoggetto  (lSogg.getCognome());
            lMessage.setDataNascita      (lSogg.getDataNascita());
            lMessage.setCodComuneNascita (lSogg.getCodComuneNascita());
            lMessage.setCodStatoNascita  (lSogg.getCodStatoNascita());
            
            // SETTA RIFERIMENTI FASCICOLO SIEP CUMULATO
            lMessage.setChiaveAnnoSiep    (mProCumModel.getChiaveAnnoFasCumulato());
            lMessage.setChiaveProgrSiep   (mProCumModel.getChiaveProgrFasCumulato());
            lMessage.setChiaveUfficioSiep (mProCumModel.getCodUfficioFasCumulato());
               
            // SETTA RIFERIMENTI AL FASCICOLO SIEP COMPETENTE (CUMULANTE)
            lMessage.setChiaveAnnoFasCumulante    (lFascicoloModel.getChiaveAnno());
            lMessage.setChiaveProgrFasCumulante   (lFascicoloModel.getChiaveProgr());
            lMessage.setChiaveUfficioFasCumulante (lFascicoloModel.getChiaveUfficio());
            // mev 39: dal fascicolo cumulante devo recuperare l'evento 'provv di cumulo' da cui recuperare la data emissione
            if(lFascicoloModel!=null && lFascicoloModel.getIdFascicoloSiep()!= null){
            	 EventoNotificaModel lEveCumulo = new EventoNotificaModel();        
            	 BigDecimal idEventoCumulo = null;
            	 if(lEveMod.getEvento() != null && lEveMod.getEvento().getEveIdEvento()!=null){
            		 idEventoCumulo=lEveMod.getEvento().getEveIdEvento();
            		 lEveCumulo = lCtrlEvento.ExRicercaEventoNotificaByKey(idEventoCumulo);
                	 lMessage.setDataEmissioneCumulo(lEveCumulo.getEvento().getDataEmissione());                	
            	 }            	
            }
      
            //=======================
            // Invio del Messaggio
            //=======================
            SIAPSender lSender = new SIAPSender();
            System.out.println(">>>>>>>>>>>>> DataEmissioneCumulo in Partenza >>>>>>>>>>>>>" + lMessage.getDataEmissioneCumulo());                	

            lSender.send(lMessage);
          }
        }
      }
    }
    
    //==========================================================================    
    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Gli Atti sono stati trasmessi agli Uffici indicati!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.modulocumulo.action.ActDettaglioComunicazioniProcure" );
    lRedirigi.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString() );

    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;
    //================================================================================
    
  }
  
  
}

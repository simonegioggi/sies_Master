package siap.siep.misurasicurezza.action;

import org.apache.log4j.Logger;
import java.util.Vector;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Action per la ricerca degli atti presi in carico me per i quali non si � ancora
 * proceduto all'iscrizione dei provvedimenti di classe IV (esecuzione misure di sicurezza)
 * 
 * @author d.fiorletta
 *
 */
public class ActRicercaAttiPresiInCaricoDaIscrivere  extends ActionSiap implements ICostantiMisuraSicurezza, ICostantiJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException, Exception
  {
    //==========================================================================
    // Avvio i Listener
    //==========================================================================
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
    
    this.setLinkRitorno();    
    setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
    
    
    /**
    MessaggioModel lMessaggio = new MessaggioModel();
    
    lMessaggio.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());
    lMessaggio.setCodTipoMessaggio   (ICostantiJMS.RICHIESTA);
    lMessaggio.setCodTipoOperazione  (ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS);
    lMessaggio.setCodEsito           (ICostantiJMS.PRESAINCARICO); 
    
    lMessaggio.setFlagVisto("S"); // 
    
    // Ricerca Messaggi
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    Vector lVect = lCrtl.ExRicercaMessaggio(lMessaggio);
    */
    
    
    Vector <String> lListaTipoOperazione = new Vector <String> ();
    lListaTipoOperazione.add (ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS);
    lListaTipoOperazione.add (ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS);
    
    Vector <String> lListaEsiti = new Vector <String> ();
    lListaEsiti.add(ICostantiJMS.PRESAINCARICO);
    
    
    IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
    Vector <MessaggioModel> lVect = lCtrl.ExRicercaMessaggi (ICostantiJMS.DELIVERY_MODE_RICEVUTO
                                                           , ICostantiJMS.RICHIESTA
                                                           , lListaTipoOperazione
                                                           , lListaEsiti
                                                           , "S" // Flag_visto.
                                                           , null // aChiaveAnnoSiep
                                                           , null // aChiaveProgrSiep
                                                           , null // aChiaveUfficioSiep
                                                           , null   // aCodUfficioMitt
                                                           , getCodUfficioUtenteConnesso() // ufficio dest
                                                           , null //lDataTrasmissioneDal
                                                           , null //lDataTrasmissioneAl 
                                                           , 0); 
    
    
    //==========================================================================
    // Verifico se presente fascicolo di Classe IV associato.
    // Per essere considerato iscritto, bisogna verificare che il fascicolo trasmesso
    // (anno, numero, ufficio) sia presente sulla tabella FASC_MS_TO_FASC_SIEP
    // e che sia legato a un fascicolo dell'ufficio dell'utente connesso.
    // Questo legame viene creato in fase di iscrizione del fascicolo di classe IV
    // a partire da un fascicolo di classe I.
    //==========================================================================
    // Ricerca sulla tabella FASC_MS_TO_FASC_SIEP
    // FASC_MS_TO_FASC_SIEP.CHIAVE_ANNO_SIEP    = lMessaggio.getChiaveAnnoSiep() 
    // FASC_MS_TO_FASC_SIEP.CHIAVE_PROGR_SIEP   = lMessaggio.getChiaveProgrSiep()
    // FASC_MS_TO_FASC_SIEP.CHIAVE_UFFICIO_SIEP = lMessaggio.getCodUfficioMittente()    
    // FASC_MS_TO_FASC_SIEP.CHIAVE_UFFICIO_CLASSE_IV = getCodUfficioUtenteConnesso()
//    for (int i=0;i<lVect.size();i++){
//      MessaggioModel lMessaggioTrovato = (MessaggioModel) lVect.elementAt(i);
//      
//      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//      siesLogger.debug(lMessaggioTrovato);
//      FascMsToFascSiepModel lFascMsToFascSiepModel = new FascMsToFascSiepModel();
//      
//      lFascMsToFascSiepModel.setChiaveAnnoSiep        (lMessaggioTrovato.getChiaveAnnoSiep());
//      lFascMsToFascSiepModel.setChiaveProgrSiep       (lMessaggioTrovato.getChiaveProgrSiep());
//      lFascMsToFascSiepModel.setChiaveUfficioSiep     (lMessaggioTrovato.getCodUfficioMittente());
//      lFascMsToFascSiepModel.setChiaveUfficioClasseIV (getCodUfficioUtenteConnesso());
//      
//      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//      siesLogger.debug(lFascMsToFascSiepModel);
//      
//      IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
//      Vector <FascMsToFascSiepModel> lLista = lCtrl.ExRicercaFascMsToFascSiepByFascSiep(lFascMsToFascSiepModel);
//      
//      if (lLista!=null && lLista.size()>0) {
//        // Presente Fascicolo di classe IV Iscritto
//        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//        siesLogger.debug("Presente fascicolo di classe IV iscritto. Rimuovo.");
//        lVect.remove(i);
//        i--;
//      }
//    }
    
    // FIXME 2 problemi:
    // 1) la presenza del record sulla tabella FASC_MS_TO_FASC_SIEP non implica
    //    che il fascicolo di classe IV sia stato iscritto a seguito della presa 
    //    in carico del messaggio. Se presenti pi� trasmissioni andrebbe testato
    //    ogni MESSAGGIO per effettuare la presa in carico. Se il fascicolo 2008/13
    //    viene trasmesso 2 volte per iscrivere 2 fascicoli distinti di classe IV,
    //    il secondo invio non comparirebbe nella lista perch� troverebbe il 
    //    record sulla tabella FASC_MS_TO_FASC_SIEP relativo al primo invio.
    // 2) il filtro a posteriori con il tempo risulta poco performante, infatti
    //    i messaggio in stato preso in carico andranno ad aumentare e quindi
    //    anche il numero di query per verificare se presente il record FASC_MS_TO_FASC_SIEP
    
    // Meglio cambiare lo stato del CDO_ESITO sulla richiesta in fase di iscrizione
    // Tale esito � utile anche in caso si volessa mandare un messaggio al mittente
    // quando il fascicolo viene iscritto
    // -     : Valore iniziale
    // 01001 : Preso in Carico
    // 01003 : Restituito
    // 01005 : Iscritto
    
    
    setRequestAttribute("Messaggi", lVect);
    //setRequestAttribute("TornaQui", "20");
   
    
    return PG_LISTA_ATTI_PRESI_IN_CARICO_DA_ISCRIVERE;
  }
}

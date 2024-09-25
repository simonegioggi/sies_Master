package siap.siep.presaincarico.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.manage.ManageSeguitoAtti;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.jms.model.PresaInCaricoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action di presa in carico atti ricevuti. Utilizzata sia per atti provenienti 
 * stessa BDI che altra BDI.
 * Solo CUMULO.
 * 
 * @author d.fiorletta
 *
 */
public class ActConfermaPresaInCaricoAttiSiep extends ActionSiap implements ICostantiJMS
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

    // Verifico se provengo dall'struttoria
    BigDecimal lIdIstruttoria = null;
    if(!isRequestParameterNullObj (ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)){
      lIdIstruttoria = this.getRequestBigDecimalParameter (ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    }
    

    // Recupero il messaggio
    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);
    
    
    // Verifico se il procedimento da acquisire è di questa BDI. 
    //==========================================================================
    // OTTIMIZZAZIONE Campo BLOB
    // Se il fascicolo trasmesso è della stessa BDI, non è stato inserito nel 
    // Messaggio (Campo blob) per contenere lo spazio. Recupero i dati direttamente
    // dalla BDI    
    boolean lStessaBDI = false;
    UfficioModel lUfficioFascicoloRicevuto = getUfficioByCodUfficio (lMess.getChiaveUfficioSiep());
    if (lUfficioFascicoloRicevuto.getCodDistretto().equals(getCodDistrettoUtenteConnesso())) {
      siesLogger.debug("Fascicolo da Cumulare della stessa BDI");
      lStessaBDI = true;
    }
    else 
    {
      siesLogger.debug("Fascicolo da Cumulare proveniente da fuori Distretto");
      lStessaBDI = false;
    }
      
    /*
    ParserMessage lPars = new ParserMessage(lMess.getTreeModel());
    String lChiaveUffFasc = lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getChiaveUfficio();
    UfficioModel lUfficioAtti = this.getUfficioByCodUfficio(lChiaveUffFasc);    
    
    boolean lStessaBDI = false;
    if (lUfficioAtti.getCodDistretto().equalsIgnoreCase(getCodDistrettoUtenteConnesso())) {
      lStessaBDI = true;
    }
    else {
      lStessaBDI = false;
    }
    */

    siesLogger.debug("lStessaBDI = "+lStessaBDI);
    
    // Se fascicolo di altra BDI procedo all'acquisizione a sistema
    MessaggioModel lMessReturn = new MessaggioModel();
    if (!lStessaBDI) {
      try {
        MessaggioModel lMessIns =  new MessaggioModel(lMess);
        IPresaInCaricoJMS lPres = SIEPLookupRemote.getPresaInCarico();
        
        // MEV_2024-DNA - Si passano al controller anche le informazione su data utente e ufficio 
        //                che precede alla presa in carico
        PresaInCaricoModel lPresaIncaricoModel = new PresaInCaricoModel();
        lPresaIncaricoModel.setDataPresaInCarico (DateUtils.getSysDate());
        lPresaIncaricoModel.setCodOperatorePresaInCarico (getCodUtenteConnesso());
        lPresaIncaricoModel.setCodUfficioPresaInCarico (getCodUfficioUtenteConnesso());
        
        // lMessReturn = lPres.ExInserisciFascicoloSiep (lMessIns);  
        lMessReturn = lPres.ExInserisciFascicoloSiep (lMessIns, lPresaIncaricoModel); 
        // MEV_2024-DNA - FINE 
        // n.b. l'esito dell'acquisizione viene registrato in lMessReturn.getRapportoEsito();
      }
      catch (F3BException ex) {
         siesLogger.error("Exception >>> " + ex,ex);
         throw new SIEPException(SIEPException.USER_MESSAGE, "Si è verificato un errore durante il caricamento dell'Atto pervenuto.<br> Riprovare in seguito.");
      }
    }

   
    //==================================================
    // Preparo e invio il Messaggio di risposta
    //==================================================
    MessaggioModel lMessage = new MessaggioModel();

    lMessage.setDescrBdiDestinataria   (lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria     (lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario (lMess.getCodUfficioMittente());


    lMessage.setCodBdiMittente        (getCodDistrettoUtenteConnesso());
    lMessage.setCodUfficioMittente    (this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente  (this.getCodUtenteConnesso());
    
    lMessage.setCodTipoMessaggio  (ESITO);
    lMessage.setCodEsito          (PRESAINCARICO);
    
    if("00066".equals(lMess.getCodTipoOperazione()) )	{
    	lMessage.setCodTipoOperazione (ESITO_TRASFERIMENTO_COMPETENZA);
    } else if("00078".equals(lMess.getCodTipoOperazione()) ) {
    	lMessage.setCodTipoOperazione (ESITO_SEGUITO_ATTI);
    }

    lMessage.setChiaveAnnoSiep    (lMess.getChiaveAnnoSiep());
    lMessage.setChiaveProgrSiep   (lMess.getChiaveProgrSiep());
    lMessage.setChiaveUfficioSiep (lMess.getChiaveUfficioSiep());
    
    //dati soggetto
    lMessage.setNomeSoggetto      (lMess.getNomeSoggetto());
    lMessage.setCognomeSoggetto   (lMess.getCognomeSoggetto());
    lMessage.setDataNascita       (lMess.getDataNascita());
    lMessage.setCodComuneNascita  (lMess.getCodComuneNascita());
    lMessage.setCodStatoNascita   (lMess.getCodStatoNascita());        
    
    lMessage.setChiaveAnnoFasCumulante    (lMess.getChiaveAnnoFasCumulante());
    lMessage.setChiaveProgrFasCumulante   (lMess.getChiaveProgrFasCumulante());
    lMessage.setChiaveUfficioFasCumulante (lMess.getChiaveUfficioFasCumulante());
  
    lMessage.setJmsCorrelationIdMessage (lMess.getJmsCorrelationIdMessage());

    lMessage.setDataInvio (lMess.getDataInvio()); //FIXME CUMULO verificare prechè non data di sistema
    lMessage.setDataEsito (DateUtils.getSysDate());

    //lMessage.setTreeModel (lMess.getTreeModel()); //FIXME A che serve? Il mittente ha già i dati a sistema
    // Non viene utilizzato da nessuno. E' inutile occupare spazio. Si manda un tree vuoto e non null
    // perchè altrimenti la MessaggioModel.VerifyMessage() rilancia errore
    lMessage.setTreeModel (new TreeModel()); 
    
    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

    //==========================================
    // Marco il messaggio di richiesta evaso.
    //==========================================
    lMess.setFlagVisto("S");
    lMess.setDataEsito(DateUtils.getSysDate());
    lMess.setCodEsito("01001");
    lCrtl.ExModificaMessaggio(lMess);
    
    //==========================================================================
    // Pagina di risposta.
    // Se stessa BDI non ho effettuato alcuna acquisizione
    // Se altra BDI visualizzo l'esito lMessReturn.getRapportoEsito();
    //==========================================================================
    if (lIdIstruttoria!=null){
      siesLogger.debug("Provengo dall'istruttoria recupero il fascicolo iscritto");
      // Provengo dalla presa in carico dall'istruttoria. In questo caso chiedo all'utente
      // se procedere direttamente all'inserimento in istruttoria del fascicolo.
      
      /*
      FascicoloSiepModel lFascSiepModel = lPars.getDettaglioFascicoloSiep().getFascicoloSiep();
      
      // n.b. Questa query non servirebbe. L'idFascicolo è già noto, è quello del Messaggio,
      //      le chiavi infatti non cambiano. Si effettua la query solo x conferma
      //      acquisizione dati.
      siesLogger.debug("lFascSiepModel = "+lFascSiepModel);
      IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
      lFascSiepModel = lCtrlFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio (lFascSiepModel);
      siesLogger.debug("lFascSiepModel = "+lFascSiepModel);
      */
     
      IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFascSiepModel = new FascicoloSiepModel();
      lFascSiepModel.setChiaveAnno    (lMess.getChiaveAnnoSiep());
      lFascSiepModel.setChiaveProgr   (lMess.getChiaveProgrSiep());
      lFascSiepModel.setChiaveUfficio (lMess.getChiaveUfficioSiep());
      
      lFascSiepModel = lCtrlFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio (lFascSiepModel);
      
      setRequestAttribute (ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO,""+lIdIstruttoria);      
      setRequestAttribute ("idFasDaCumulare",""+lFascSiepModel.getIdFascicoloSiep());
      setRequestAttribute ("messaggiodiarrivoatti", lMess);
      
      // Solo per il "Seguito Atti" è necessario trovare ProcedimentoCumulato per poter
      //  poi trovare il corrispondente TitoloCumulato; 
      ITitoloCumulato TitoCtlr = SIEPLookupRemote.getTitoloCumulatoRemote();
      ProcedimentoCumulatoModel ProcMod = new ProcedimentoCumulatoModel();
      
      ProcMod.setChiaveAnnoFasCumulato(lFascSiepModel.getChiaveAnno());
      ProcMod.setChiaveProgrFasCumulato(lFascSiepModel.getChiaveProgr());
      ProcMod.setCodUfficioFasCumulato(lFascSiepModel.getChiaveUfficio());
      ProcMod.setIdFascicoloSiepOrigine(lFascSiepModel.getIdFascicoloSiep());
//      ProcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
      
      ProcMod = TitoCtlr.ExRicercaProcedimentoCumulatoByDatiFascicoloSiep(ProcMod, lIdIstruttoria);
      setRequestAttribute ("ProcedimentoCumulato", ProcMod);
      
      //========================================================================
      // In caso di seguito atti aggiorno automaticamente anche lo stato esecuzione
      // del titolo con i nuovi provvedimenti
      //========================================================================
      // FIXME gestire eventuali errori
      
      DatiOperazioneModel lDatiOper = new DatiOperazioneModel();      
      lDatiOper.setCodOperatore (getCodUtenteConnesso());
      lDatiOper.setData         (DateUtils.getSysDate());
      lDatiOper.setCodUfficio   (getCodUfficioUtenteConnesso());
      
      ManageSeguitoAtti lManagerSeguito = new ManageSeguitoAtti();
      lManagerSeguito.aggiornaStatoEsecuzioneTitolo(lFascSiepModel.getIdFascicoloSiep(),lIdIstruttoria, lDatiOper);
      
      //========================================================================
      return ICostantiIstruttoriaCumulo.PG_MESSAGGIO_CONFERMA_ISCRIZIONE;      
    }
    else 
    {   
	      if (lStessaBDI) 
	      {
	        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Ricezione Atto Completata e Esito rispedito al Mittente. Il fascicolo è stato preso in carico!");
	  
	        RedirectTo lRedirigi = new RedirectTo();
	        lRedirigi.setPage( IWebConstants.PG_MAIN );
	        lRedirigi.setAction( "siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti" );
	        
	        setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
	        return IWebConstants.PG_MESSAGE;
	      }
	      else {
	        this.setRequestAttribute("Messaggio", lMessReturn);
	        return ICostantiPresaincarico.PG_RAPPORTO_PRESA_IN_CARICO;
	      }
    }
  }
}
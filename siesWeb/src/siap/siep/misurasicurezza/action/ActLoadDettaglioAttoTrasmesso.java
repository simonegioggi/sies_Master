package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

public class ActLoadDettaglioAttoTrasmesso extends ActionSiap implements ICostantiMisuraSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    
    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    super.setLinkRitorno();
    
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMessaggio = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    
    boolean isRecordFittizio = false;
    if (!isRequestParameterNullObj("RecordInoltroSimulato")) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("RecordInoltroSimulato");
      isRecordFittizio = true;

      MessaggioModel lMessFittizio = new MessaggioModel(lMessaggio);
      lMessFittizio.setCodEsito                 ("-"); // In attesa di risposta
      lMessFittizio.setDataInvio                (null);
      lMessFittizio.setDescrEsito               ("-");
      lMessFittizio.setNote                     (null);
      lMessFittizio.setCodUfficioMittente       (lMessFittizio.getCodUfficioInoltro());
      lMessFittizio.setDescrUfficioMittente     (lMessFittizio.getDescrUfficioInoltro());
      lMessFittizio.setDescrSedeUfficioMittente (lMessFittizio.getDescrSedeUfficioInoltro()); 
      
      this.setRequestAttribute("Messaggio", lMessFittizio);
      this.setRequestAttribute("MessaggioInoltrante", lMessaggio);
    }
    else {
      this.setRequestAttribute("Messaggio", lMessaggio);
    }
    
    
    //this.setRequestAttribute("isRecordInoltroSimulato", isRecordInoltroSimulato);
    
    MessaggioModel lMessaggioRichiesta = null;
    Vector <MessaggioModel> lVectSolleciti = null;
    
    //==========================================================================
    // Il messaggio di cui è stata richiesto il dettaglio potrebbe essere il 
    // messaggio originariamente inviato oppure un risposta
    if (ICostantiJMS.RICHIESTA.equals(lMessaggio.getCodTipoMessaggio()))
    {
      // Recupero i messaggio correlati
      Vector <MessaggioModel> lListaMessaggiCorrelati = null;
      lCrtl.ExRicercaMessaggiCorrelati(lMessaggio.getIdMessaggio().toString());
      
      lMessaggioRichiesta = lMessaggio;
      lMessaggioRichiesta.setMessaggiCorrelati(lListaMessaggiCorrelati);
      

      //========================================================================
      // Recupero gli eventuali Solleciti inviati
      //========================================================================
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sto visualizzando il dettaglio di una RICHIESTA. Recupero gli eventuali Solleciti inviati");

      IMisuraSicurezza lCtrlMisSic = SIEPLookupRemote.getMisuraSicurezzaRemote();
      lVectSolleciti = lCtrlMisSic.ExRicercaSollecitiByIdRich (ICostantiJMS.DELIVERY_MODE_INVIATO
                                                             , ICostantiJMS.RICHIESTA
                                                             , ICostantiJMS.SOLLECITO_TRASFERIMENTO_COMPETENZA_MS
                                                             , lMessaggioRichiesta.getIdMessaggio()
                                                             , null //"N" // Flag_visto.
                                                             , lMessaggio.getCodUfficioDestinatario() // ufficio dest
                                                             ); 
      
    }
    else {
      // Sto visualizzando il dettaglio di una risposta (esito). Recupero i dati degli
      // Atti trasmessi (Richiesta)
      
      lMessaggioRichiesta = lCrtl.ExRicercaMessaggioByKey(new BigDecimal(lMessaggio.getJmsCorrelationIdMessage()));

      //========================================================================
      // Recupero gli eventuali Solleciti inviati
      //========================================================================
      String lUffDestSollecito = null;
      if (isRecordFittizio){
        lUffDestSollecito = lMessaggio.getCodUfficioInoltro();
      } else {
        lUffDestSollecito = lMessaggio.getCodUfficioMittente();
      }
      
        
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sto visualizzando il dettaglio di una risposta (esito). Recupero gli eventuali Solleciti inviati");
      IMisuraSicurezza lCtrlMisSic = SIEPLookupRemote.getMisuraSicurezzaRemote();
      lVectSolleciti = lCtrlMisSic.ExRicercaSollecitiByIdRich (ICostantiJMS.DELIVERY_MODE_INVIATO
                                                             , ICostantiJMS.RICHIESTA
                                                             , ICostantiJMS.SOLLECITO_TRASFERIMENTO_COMPETENZA_MS
                                                             , lMessaggioRichiesta.getIdMessaggio()
                                                             , null //"N" // Flag_visto.
                      /* uff destinatario del sollecito*/    , lUffDestSollecito // in caso di risposta mi interessano i solleciti inviati all'ufficio che mi ha risposto
                                                             ); 
    }
    
    this.setRequestAttribute("MessaggioRichiesta", lMessaggioRichiesta);
    this.setRequestAttribute("listaSolleciti", lVectSolleciti);
    
    //==========================================================================
    // Recupero gli eventuali Solleciti inviati
    //==========================================================================
    
    
    // Passo alla form i dati identificativi degli atti trasmessi
    // - FascicoloSiep (Soggetto, Sentenza)
    if (lMessaggioRichiesta.getTreeModel()!=null) {
      ParserMessage lParser = new ParserMessage(lMessaggioRichiesta.getTreeModel());
      
      this.setRequestAttribute("fascicolo", lParser.getDettaglioFascicoloSiep().getFascicoloSiep()); 
      this.setRequestAttribute("penaresidua", lParser.getDettaglioFascicoloSiep().getPenaResidua());   
    }
    else {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.warn("ERRORE nel Parsing del BLOB messaggio trasmesso per possibile errore Versione classe");
      // Se il DAO non è riuscito a deserializzare il blob, rovo a recuperare i 
      // dati direttamente dal DB
      FascicoloSiepModel lFasMod = new FascicoloSiepModel();

      lFasMod.setChiaveUfficio (lMessaggioRichiesta.getChiaveUfficioSiep());
      lFasMod.setChiaveProgr   (lMessaggioRichiesta.getChiaveProgrSiep());
      lFasMod.setChiaveAnno    (lMessaggioRichiesta.getChiaveAnnoSiep());
      
      IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

      if (lFasRet!=null){
        this.setRequestAttribute("fascicolo", lFasRet);
      }
      else {
        // Messaggio di errore e ritorno sulla maschera elenco risultati ricerca
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Non è possibile visualizzare il dettaglio del Messaggio in quanto inviato con una versione SIEP differente da quella attualmente in uso.");
        
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" +  super.goToRitorno());
        
        return IWebConstants.PG_MESSAGE;
      }
    }
    
    return PG_DETTAGLIO_ATTI_TRASMESSI;
  }
}
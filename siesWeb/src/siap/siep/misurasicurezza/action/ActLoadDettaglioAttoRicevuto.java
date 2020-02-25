package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * Questa Action recupera i dati dal messaggio di trasmissione atti misure di 
 * sicurezza. Effettua il parsing del treeModel e passa alla jsp di visualizzazione
 * i dati 'esplosi' in modo da consentire all'utente di visualizzare il contenuto
 * degli atti trasmessi e decidere se prendere in carico gli atti (acquisirli sul 
 * DB) oppure restituirli al mittente.
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadDettaglioAttoRicevuto extends ActionSiap implements ICostantiMisuraSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException, Exception
  {
    //=======================================
    // Recupero il messaggio da visualizzare
    //=======================================
    BigDecimal lIdMessage = null;
    if(!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)){
      lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    }
    else if(!this.isRequestAttributeNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)){
      lIdMessage = (BigDecimal)this.getRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    }
    
    MessaggioModel lMessModel = new MessaggioModel();
    if(lIdMessage != null){
     IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
     lMessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    }
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lMessModel.getIsErroreParser() = "+lMessModel.getIsErroreParser());
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lMessModel = "+lMessModel);
    
    if (lMessModel.getTreeModel()!=null){
      //======================================
      // Parsing del messaggio
      //======================================
      ParserMessage lParser = null;  
      if(lMessModel != null){
        lParser = new ParserMessage(lMessModel.getTreeModel());
      }
      
      //================================================================
      //  Passo il DettaglioFascicoloModel alla jsp di visualizzazione
      //================================================================
      DettaglioFascicoloModel lDettaglioFasModel = null;
      if (lParser != null && lParser.getDettaglioFascicoloSiep() != null)
        lDettaglioFasModel = lParser.getDettaglioFascicoloSiep();
      else
        throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Procedimento. <BR>Rivolgersi all'amministratore di sistema! " );
      
      this.setRequestAttribute("dettaglioFasSIEP", lDettaglioFasModel);
      this.setRequestAttribute("Messaggio", lMessModel);
    }
    else {
      // Il DAO non è riuscito a deserializzare il blob. Al 99% è cambiata la
      // versione SIEP.
      
      this.setRequestAttribute("Messaggio", lMessModel);
      this.setRequestAttribute("isErrParser", "SI");
      
      UfficioModel lUffFascicolo = getUfficioByCodUfficio(lMessModel.getChiaveUfficioSiep());      
      this.setRequestAttribute("uffFascicolo", lUffFascicolo);
      //ComuneModel lComuneNascita = getDatiComuneByCodDescr(lMessModel.getCodComuneNascita(), null);
      ComuneModel lComuneNascita = DecodificheUtils.getComuneByCod(lMessModel.getCodComuneNascita());
      this.setRequestAttribute("comuneNascita", lComuneNascita);
      
      
      // In realtà anche se il parsing è andato male, il dettaglio del fasciolo
      // posso comunque visualizzarlo nel caso di atti già presi in carico in
      // quanto a sistema. 
      // n.b. In questo caso non visualizzo gli atti ricevuti bensì quelli a 
      //      sistema che potrebbero essere stati aggiornati
      // n.b. E' da considerare anche il caso della trasmissione stessa BDI.
      //      in questo caso la presa in carico è fittizia, i dati presenti
      //      nel messaggio non verranno mai acquisiti a sistema per cui l'errore
      //      del parser consentirebbe comunque anche la presa in carico.
      // n.b. c'è inoltre il caso di dati già presenti a sistema anche se provenienti
      //      da altra BDI. In questo caso potrebbero essere obsoleti. Per procedere
      //      andrebbero comunque aggiornati
      if (   ICostantiJMS.ISCRITTO_CLASSE_IV.equals(lMessModel.getCodEsito()) 
          || ICostantiJMS.PRESAINCARICO.equals(lMessModel.getCodEsito())
         )
      {
        try {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug("Provo a recuperare il fascicolo se a sistema...");
          IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
          
          FascicoloSiepModel lFasModel = new FascicoloSiepModel();
          lFasModel.setChiaveAnno    (lMessModel.getChiaveAnnoSiep());
          lFasModel.setChiaveProgr   (lMessModel.getChiaveProgrSiep());
          lFasModel.setChiaveUfficio (lMessModel.getChiaveUfficioSiep());
          
          lFasModel = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasModel);
          
          if (lFasModel!=null && lFasModel.getIdFascicoloSiep()!=null){
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.debug("Fascicolo trovato provo a caricare il dettaglio...");
            
            DettaglioFascicoloModel lDettaglio = null;
            lDettaglio = lCtrl.ExDettaglioFascicoloSiep(lFasModel.getIdFascicoloSiep());
            
            this.setRequestAttribute("dettaglioFasSIEP", lDettaglio);
            this.setRequestAttribute("isErrParser", "NO");
          }
          else {
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.debug("...Fascicolo non trovato a sistema");
          }
        }
        catch (Exception e){}
      }
      
      //FIXME 'MS' se ANCORA DA PRENDERE IN CARICO ma STESSA BDI posso comunque 
      //      recuperare i dati direttamente dal DB in quanto già presenti
      //
      //     
//      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Non è possibile visualizzare il dettaglio del Messaggio in quanto inviato con una versione SIEP differente da quella attualmente in uso.");
//      
//      setRequestAttribute(IWebConstants.GOTO_PAGE, "" +  super.goToRitorno());
//      
//      return IWebConstants.PG_MESSAGE;      
      

    }
    
    
    this.setLinkRitorno(); 
    //setRequestAttribute("TornaQui", "10");
    
    return PG_LOAD_DETTAGLIO_ATTO_RICEVUTO_MS;
  }
}
  
package siap.siep.fascicolo.action;


import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Action invocata dalla lista degi atti presi in carico (trasmissione atti per 
 * competenza) per visualizzare i dati del fascicolo.
 * 
 * Richiama la ActLoadDettaglioFascicolo 
 *
 * 
 * @author 
 *
 */
public class ActLoadFascicoloDaRicercaMessaggio extends ActionSiap implements ICostantiFascicoloSiep
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    //Leggo le informazioni sul fascicolo da ricercare sul messaggio
    BigDecimal lIdMessaggio = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    
    IMessaggio lCrtlMsg = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel aMsg = lCrtlMsg.ExRicercaMessaggioByKey(lIdMessaggio);
    
    if(aMsg!=null){
  
      //Istanzio il Model
      FascicoloSiepModel lFasMod = new FascicoloSiepModel();
      
      if(aMsg.getChiaveProgrSiep()!=null && aMsg.getChiaveAnnoSiep()!=null){
        BigDecimal lChiaveAnno    = aMsg.getChiaveAnnoSiep();
        BigDecimal lChiaveProgr   = aMsg.getChiaveProgrSiep();
        String     lChiaveUfficio = aMsg.getCodUfficioMittente();
        //String     lChiaveUfficio = aMsg.getChiaveUfficioSiep();
        
        lFasMod.setChiaveAnno    (lChiaveAnno);
        lFasMod.setChiaveProgr   (lChiaveProgr);
        lFasMod.setChiaveUfficio (lChiaveUfficio);
      }
      else{
        throw new SIEPException(F3BException.USER_MESSAGE,"Dati insufficienti per recuperare il Fascicolo: "+aMsg.getChiaveAnnoSiep()+"/"+aMsg.getChiaveProgrSiep()); 
      }


      IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

      if (lFasRet == null)
        throw new SIEPException(F3BException.USER_MESSAGE,"Nessun Fascicolo trovato!");
       
      String lReturnPage = "";

      lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
          "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +
          CAMPO_ID_FASCICOLO_SIEP + "=" +
          lFasRet.getIdFascicoloSiep().toString();

      setRequestAttribute("fascicolo", lFasRet);
      
      // Carico il fascicolo in sessione
      setSessionAttribute("fascicolo",lFasRet);
      setSessionAttribute("soggetto",lFasRet.getSoggetto());
      setSessionAttribute("sentenza",lFasRet.getSentenza());
      
      //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug("<<<<<<<" + getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE));
      if (!isRequestParameterNullObj(CAMPO_AZIONE_CHIAMANTE)) //?????
      {       
        // lPage = getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE);
        lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
            "=" + getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE) +"&"+FASCICOLO_RICERCATO+"=SI" ;      
        
      }
      return lReturnPage;

    }
    else{   
      throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Fascicolo con trovato!");
    }

  }
}

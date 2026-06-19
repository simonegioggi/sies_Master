package siap.siep.modulocumulo.action;

import org.apache.log4j.Logger;
import java.math.BigDecimal;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;


/**
 * Action per la cancellazione del provvedimento di Cumulo dirttamente dal 
 * dettaglio
 * @author d.fiorletta
 *
 */
public class ActCancellaProvvedimentoCumulo extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter("IdEvento");
    
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    
    EventoModel lEveModRic = lCtrlEvento.ExRicercaEventoByKey(lIdEvento); 
    
    
    if(   lEveModRic != null 
       && (   lEveModRic.getFlagDocumentoRegistrato() == null 
           || lEveModRic.getFlagDocumentoRegistrato().equals("") 
           || lEveModRic.getFlagDocumentoRegistrato().equals("N")
          )
      )
    {
      //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug("Evento trovato "+lEveModRic);
      IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
      lCtrl.ExCancellaEventoConStoreProcedure(lEveModRic);
    }
    
    
    // Dopo la cancellazione per ora ricarico il dattagli DatiFinali (la prima form)
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo&" 
            + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

    
    return lPage;
  }
}

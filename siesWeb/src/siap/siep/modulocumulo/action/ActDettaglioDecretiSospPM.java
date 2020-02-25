package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Iterator;

import f3b.log.LogF3B;
import f3b.util.F3BException;

import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.NotificaCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

public class ActDettaglioDecretiSospPM extends ActionModuloCumulo 
  implements ICostantiStatoEsecTitoloCumulato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
  
    
    BigDecimal idStatoEsec = null;
    if (!isRequestParameterNullObj(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO))
      idStatoEsec = getRequestBigDecimalParameter (CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
    
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    StatoEsecTitoloCumulatoModel lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull (idStatoEsec);  
    
    if (lStato.getListaNotifiche()!=null) {
      Iterator <NotificaCumuloModel> iterNotifiche = lStato.getListaNotifiche().iterator();
      while (iterNotifiche.hasNext()){
        NotificaCumuloModel lNotifica = iterNotifiche.next();
        
        try {
          if (lNotifica.getUffCodUfficio()!=null) {
            lNotifica.setUfficio (getUfficioByCodUfficio(lNotifica.getUffCodUfficio()));
          }
        } catch (Exception e) {}
      }
    }
      
    siesLogger.debug("Provvedimento = "+lStato);
    
    setRequestAttribute("Provvedimento", lStato);
    
    return PG_LOAD_DETTAGLIO_SOSP_PM;
  }
}

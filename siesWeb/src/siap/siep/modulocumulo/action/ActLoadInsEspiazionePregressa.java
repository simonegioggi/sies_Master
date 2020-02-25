package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

public class ActLoadInsEspiazionePregressa extends ActionModuloCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();

    StatoEsecTitoloCumulatoModel lStato = null;

    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    String lIdCompDaModificare = "";

    if ("I".equals(lModalita)){
      // Inserimento
      
    }
    else if ("M".equals(lModalita) ){
        // Modifica 
        BigDecimal lIdStat = getRequestBigDecimalParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

        lIdCompDaModificare = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO);

        siesLogger.debug("Sto In modifica ("+lModalita+"), idStat = "+lIdStat+", lIdComp = "+lIdCompDaModificare);
        
        // Recupero i dati e li passo alla form
        IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
        
        lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull (lIdStat);  
        setRequestAttribute("aProvvedimento", lStato );
        setRequestAttribute("aIdComputo", ""+lIdCompDaModificare );

        if (lStato.getListaComputi().elementAt(0)!=null		&&
        	lStato.getListaComputi().elementAt(0).getIstDetIdIstitutoDetenzione()!=null	){
        	
            IIstitutoDetenzione lIstitutoCtrl = SIEPLookupRemote.getIstitutoDetenzioneRemote();
            
            IstitutoDetenzioneModel lIstituto = lIstitutoCtrl.ExRicercaIstitutoDetenzioneByKey (lStato.getListaComputi().elementAt(0).getIstDetIdIstitutoDetenzione() );
            setRequestAttribute("IstitutoDetenzione", lIstituto);
        }
    }
    
    else {
      //Rilanciare Eccezione - Operazione non supportata
    }
    
    //==========================================================================
    // Caricare qui i dati delle combo
    //==========================================================================
    // Motivo Provvedimento
    Option lMotivoProvv = new Option(DecodificheManager.getInstance().getMotivoProvvedimento() );
    lMotivoProvv.setFilter(new String[]{"-","0900","0901","0902","0903","0937","0267","0270","0675"});

    if (lStato!=null && lStato.getCodMotivo()!=null)
        lMotivoProvv.setSelected (lStato.getCodMotivo() );
    
    setRequestAttribute("motivoProvvedimento", "" + lMotivoProvv);  
    
    setRequestAttribute("modalita", lModalita);
    
    return PG_LOAD_INSERISCI_ESPIAZIONE_PREGRESSA;    
  }
}

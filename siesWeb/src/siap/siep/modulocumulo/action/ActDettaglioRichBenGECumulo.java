package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;

import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la visualizzazione del Dettaglio delle richieste la GE:
 * Amnistia/Indulto/depenalizzazione/Incostituzionalita' (modulo cumulo)
 *
 * @since MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
 */
public class ActDettaglioRichBenGECumulo  extends ActionModuloCumulo implements ICostantiComputiCumulo, ICostantiStatoEsecTitoloCumulato
{
  public String processRequest() throws F3BException 
  {
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();  
    
    BigDecimal idStatoEsec = null;
    if (!isRequestParameterNullObj(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO))
      idStatoEsec = getRequestBigDecimalParameter (CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
    
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    StatoEsecTitoloCumulatoModel lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull (idStatoEsec);  
        
    setRequestAttribute("Provvedimento", lStato);
    
    // Lettura dell'Ufficio Emittente del provvedimento
    if (lStato.getListaComputi()!=null && lStato.getListaComputi().size()>0
            && lStato.getListaComputi().get(0).getCodUfficioEmittenteProvv()!=null){
      UfficioModel lUffEmittente = getUfficioByCodUfficio(lStato.getListaComputi().get(0).getCodUfficioEmittenteProvv() );
      setRequestAttribute("UfficioDestinatarioProvv", lUffEmittente);
    }
    
    // Lettura del ReatoCumulo
    if (lStato.getListaComputi()!=null && lStato.getListaComputi().size()>0
            && lStato.getListaComputi().get(0).getReaIdReatoCum()!=null){
        IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
        ReatoCumuloModel lReaMod = lCtrl.ExRicercaReatoCumuloByKey(lStato.getListaComputi().get(0).getReaIdReatoCum());
        
        setRequestAttribute("lReato", lReaMod);
    }
    
    return PG_LOAD_DETTAGLIO_RICH_BENEFICI_GE_CUMULO;
  }
}

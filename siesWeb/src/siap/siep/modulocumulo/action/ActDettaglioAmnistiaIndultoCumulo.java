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
 * Action per la visualizzazione del Dettaglio del provvedimento di Amnistia/Indulto (modulo cumulo)
 * @author 
 *
 */
public class ActDettaglioAmnistiaIndultoCumulo  extends ActionModuloCumulo implements ICostantiComputiCumulo, ICostantiStatoEsecTitoloCumulato
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
    if (lStato.getListaComputi().get(0).getCodUfficioEmittenteProvv()!=null){
      UfficioModel lUffEmittente = getUfficioByCodUfficio(lStato.getListaComputi().get(0).getCodUfficioEmittenteProvv() );
      setRequestAttribute("UfficioEmittenteProvv", lUffEmittente);
    }
    
    // Lettura del ReatoCumulo
    if (lStato.getListaComputi().get(0).getReaIdReatoCum()!=null){
        IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
        ReatoCumuloModel lReaMod = lCtrl.ExRicercaReatoCumuloByKey(lStato.getListaComputi().get(0).getReaIdReatoCum());
        
        setRequestAttribute("lReato", lReaMod);
      }
    
    return PG_LOAD_DETTAGLIO_AMNISTIA_INDULTO_CUMULO;
  }
}

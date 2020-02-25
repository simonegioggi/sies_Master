package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActLoadDettaglioProcedimentoCumulato extends ActionModuloCumulo implements ICostantiProcedimentoCumulato
{

  public String processRequest() throws F3BException 
  {
    
    super.getDatiIstruttoria();
    //super.getDatiTitoloCumulato();
    
    //==========================================
    // Recupera la key del record da Visualizzare 
    //==========================================
    BigDecimal lIdProcedimentoCumulato = getRequestBigDecimalParameter ( CAMPO_ID_PROCEDIMENTO_CUMULATO) ;

    //========================================== 
    // Recupera i dati del record da visualizzare  
    //==========================================    
    ITitoloCumulato lCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
    
    ProcedimentoCumulatoModel lProcMod = lCtrl.ExRicercaProcedimentoCumulatoById( lIdProcedimentoCumulato);
    
    //
    ITitoloCumulato lTitoloCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
    TitoloCumulatoModel lTitoloModel = null;
    lTitoloModel = lTitoloCtrl.ExRicercaTitoloCumulatoById(lProcMod.getTitIdTitoloCumulato());
    setRequestAttribute("TitoloInCumulo", lTitoloModel);

    
    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    if  ("S".equals(lProcMod.getFlagAccorpato())) {
      UfficioModel lUfficioOrigine = getUfficioByCodUfficio(lProcMod.getChiaveUfficioOrigine());
      lProcMod.setUfficioOrigine (lUfficioOrigine);
    }

    setRequestAttribute("procedimentocumulato", lProcMod);
    
    
    return PG_LOAD_DETTAGLIOPROCEDIMENTOCUMULATO;
  }
}
package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActLoadDettaglioPenaComplessivaCumulo</p>
 * <p>Description: Classe Action per la load dettaglio di PenaComplessiva</p>
 * <p>    in ambito Cumulo (Pena_complessiva_Cumulo) </p>
 * @version 4.0
 */

public class ActLoadDettaglioPenaComplessivaCumulo extends ActionModuloCumulo implements ICostantiPenaComplessivaCumulo
{
  public String processRequest() throws Exception
  {
   //==========================================================================
   //
   //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();      
   
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA_CUM);
    IPenaComplessivaCumulo lCtrl = SIEPLookupRemote.getPenaComplessivaCumuloRemote();

    DettaglioPenaComplessivaCumuloModel lDetCumMod = null;
    lDetCumMod = lCtrl.ExRicercaPenaCompSanzioneSostContinuazioniCumByKey(lId);


    if(lDetCumMod == null)
    { //??????????????????????????
      String lPage = "";
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadInserisciPenaComplessivaCumulo";
      return lPage;
    }
    else
    { 
      setRequestAttribute("dettaglioPenaComplessivaCum", lDetCumMod);
      return PG_LOAD_DETTAGLIOPENACOMPLESSIVA_CUM;
    }
  
  } 

} 
  
package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>Title: ActLoadDettaglioReatoCumulo</p>
 * <p>Description: Classe Action per la load dettaglio di Reato</p>
 * <p>		in ambito CUMULO (tab Reato_Cumulo)</p>
 */

public class ActLoadDettaglioReatoCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo 
{
  public String processRequest() throws Exception
  {
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_REATO_CUM);

    IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
    ReatoCumuloModel lReaMod = lCtrl.ExRicercaReatoCumuloByKey(lId);
    
    setRequestAttribute("reato", lReaMod);
    
   //==========================================================================
   // Recupero i dati da passare alla form
   // di DettaglioTitoloCumulato.jsp
   //==========================================================================
   super.getDatiIstruttoria();
   super.getDatiTitoloCumulato();      

    return PG_LOAD_DETTAGLIOREATO_CUM;
  }
}
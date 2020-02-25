package siap.siep.modulocumulo.action;

/**
 * <p>Title: ActCancellaPenaComplessivaCumulo</p>
 * <p>Description: Azione di Cancellazione Pena Complessiva</p>
 * <p>		in ambito Cumulo (Pena_complessiva_Cumulo) </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */

import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActCancellaPenaComplessivaCumulo extends ActionModuloCumulo implements ICostantiPenaComplessivaCumulo
{
  /**
   * Azione di cancellazione Pena Complessiva
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws Exception
   */
  public String processRequest() throws Exception
  {
    // riempie il model
    PenaComplessivaCumuloModel lMod = new PenaComplessivaCumuloModel();

    lMod.setIdPenaComplessivaCum(getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA_CUM));

    IPenaComplessivaCumulo lCtrl = SIEPLookupRemote.getPenaComplessivaCumuloRemote();
    lCtrl.ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioniCum(lMod);

    String lPage = "";
   // lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;
   // lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaPenaComplessivaCumulo";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici";
    
    return lPage;
  }
}

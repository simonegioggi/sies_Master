package siap.siep.penacomplessiva.action;

/**
 * <p>Title: ActCancellaPenaComplessiva</p>
 * <p>Description: Azione di Cancellazione Pena Complessiva</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActCancellaPenaComplessiva extends ActionSiap implements ICostantiPenaComplessiva
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
    PenaComplessivaModel lMod = new PenaComplessivaModel();

    lMod.setIdPenaComplessiva(getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA));

    IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
    lCtrl.ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioni(lMod);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;

    return lPage;
  }
}

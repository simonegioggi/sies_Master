package siap.siep.continuazione.action;

/**
 * <p>Title: ActCancellaContinuazione</p>
 * <p>Description: Azione di Cancellazione Continuazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
import siap.sico.web.ActionSiap;
import siap.siep.continuazione.controller.IContinuazione;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActCancellaContinuazione extends ActionSiap implements ICostantiContinuazione
{
  /**
   * Azione di cancellazione Continuazione
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws Exception
   */
  public String processRequest() throws Exception
  {
    // riempie il model
    ContinuazioneModel lMod = new ContinuazioneModel();

    lMod.setIdContinuazione(getRequestBigDecimalParameter(CAMPO_ID_CONTINUAZIONE));

    IContinuazione lCtrl = SIEPLookupRemote.getContinuazioneRemote();
    lCtrl.ExCancellaContinuazione(lMod);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;

    return lPage;
  }
}

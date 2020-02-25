package siap.sige.detenzione.action;

/**
 * <p>Title: ActCancellaDetenzioneFasSige</p>
 * <p>Description: Classe Action per la cancellazione dell'ultimo Luogo detenzione x fascicolo SIGE</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.IWebConstants;

public class ActCancellaDetenzioneFasSige extends ActionSige implements ICostantiFasSigeDetenzione
{
  public String processRequest() throws Exception
  {
    // Attivazione punto di Ritorno
    setLinkRitorno();

    FascicoloSigeEstesoModel lEstesoModel = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
    BigDecimal lIdFascicoloSige = lEstesoModel.getFascicoloSige().getIdFascicoloSige();

    IFasSigeDetenzione lCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
    lCtrl.exCancellaUltimaDetenzioneFascicoloSige(lIdFascicoloSige);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.detenzione.action.ActRicercaLuogoDetenzioneByProcedimentoSige&"+CAMPO_FAS_ID_FAS_SIGE+"="+lIdFascicoloSige.toString();
    return lPage;
    
  }
}
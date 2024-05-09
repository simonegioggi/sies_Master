package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione di inserimento dei Solleciti per Ordini di Ingiunzione al Pagamento
 * @author d.fiorletta
 * @since MEV_2023-33
 */
public class ActInserisciSollecitiOIPP extends ActionSiap implements ICostantiNotifica {
  public String processRequest() throws F3BException {
    BigDecimal lIdNot = this.getRequestBigDecimalParameter(ICostantiNotifica.CAMPO_ID_NOTIFICA);

    RinnovoModel lRinMod = new RinnovoModel();

    ComuneModel lComMod = null;

    lRinMod.setCodTipoRinnovo(this.getRequestStringParameter("TipoSollecito"));
    lRinMod.setCodTipoAutoritaRinnovo(this.getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO));
    lComMod = new ComuneModel(
        getCodComuneByDescr(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO)));
    lRinMod.setCodLuogoRinnovo(lComMod.getCodComune());
    lRinMod.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO,
                                                   ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO, 
                                                   ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO));
    // indirizzo va dentro note
    lRinMod.setNote(this.getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE));
    lRinMod.setNotIdNotifica(lIdNot);
    
    lRinMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lRinMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lRinMod.setDataInserimento(DateUtils.getSysDate());

    // INSERIMENTO RINNOVO
    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    RinnovoModel lRinModel = lCtrl.ExInserisciRinnovo(lRinMod);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
        + "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioSollecitiOIPP&"
        + ICostantiRinnovo.CAMPO_ID_RINNOVO + "=" + lRinModel.getIdRinnovo().toString();
    return lPage;
  }
}

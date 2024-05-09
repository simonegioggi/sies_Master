package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.controller.IRinnovoStampa;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione di stampa per la Rinnovazione notifica per gli Ordini di ingiunzione
 * @author d.fiorletta
 * @since MEV_2023-33 
 */
public class ActStampaRinnovazioneOIPP extends ActionSiap implements ICostantiNotifica{
  public String processRequest() throws F3BException {

    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    String lIdRinnovo = getRequestStringParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);

    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    RinnovoModel lRinModel = lCtrl.ExRicercaRinnovoByKey(new BigDecimal(lIdRinnovo));

    lRinModel.setDataAggiornamento(DateUtils.getSysDate());
    lRinModel.setCodUfficioAggiornamento(lUff.getCodUfficio());
    lRinModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    
    lRinModel.setFlagDocumentoRegistrato("N");

    String flagTemplate = null;
    String lCodMotivo = "0281"; //FIXME da gestire ilcodice
    if (lRinModel.getCodTipoRinnovo().equals("P")) {
      flagTemplate = "1";
    } else if (lRinModel.getCodTipoRinnovo().equals("U")) {
      flagTemplate = "0";
    }

    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    TemplateModel lTemMod = new TemplateModel();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null, null, lCodMotivo,
        flagTemplate);
    if (lTemMod != null) {
      lRinModel.setTemIdTemplate(lTemMod.getIdTemplate());
    }

    IRinnovoStampa lCtrlStampa = SIEPLookupRemote.getRinnovoStampaRemote();
    ByteArrayOutputStream lReport = lCtrlStampa.ExStampaDocumento(lRinModel, lUtenteMod); 
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}

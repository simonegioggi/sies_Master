package siap.siep.notifica.action;

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
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.controller.IRinnovoStampa;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: ActStampaOEOmessaNotifica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class ActStampaOEOmessaNotifica extends ActionSiap implements ICostantiNotifica {

	public String processRequest() throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		String lNotifica = this.getRequestStringParameter("notifica");
		String lUfficiali = this.getRequestStringParameter("ufficiali");

		String lId = getRequestStringParameter("idrinnovo");

		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		RinnovoModel lRinModel = lCtrl.ExRicercaRinnovoByKey(new BigDecimal(lId));

		lRinModel.setDataAggiornamento(DateUtils.getSysDate());
		lRinModel.setCodUfficioAggiornamento(lUff.getCodUfficio());
		lRinModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lRinModel.setFlagDocumentoRegistrato("N");

		String flagTemplate = null;
		String lCodMotivo = null;
		if (lNotifica != null && lNotifica.equals("FP")) {
			lCodMotivo = "0278";
			flagTemplate = "0"; // templ= SIEP_GDS_OE_RIN_RIC
		} else if (lNotifica != null && lNotifica.equals("UG")) {
			lCodMotivo = "0279";
			if (lUfficiali != null && lUfficiali.equals("RN")) {
				flagTemplate = "2"; // templ = SIEP_GDS_OE_NOT_UFFG
			} else if (lUfficiali != null && lUfficiali.equals("AR")) {
				flagTemplate = "3"; // templ = SIEP_GDS_OE_RIC_POL
			}
		}

		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null, null, lCodMotivo,
				flagTemplate);
		if (lTemMod != null) {
			lRinModel.setTemIdTemplate(lTemMod.getIdTemplate());
		}

		IRinnovoStampa lCtrlStampa = SIEPLookupRemote.getRinnovoStampaRemote();
		ByteArrayOutputStream lReport = lCtrlStampa.ExStampaDocumento(lRinModel, lUtenteMod); // setta la
																								// risposta
																								// nella
																								// request

		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}
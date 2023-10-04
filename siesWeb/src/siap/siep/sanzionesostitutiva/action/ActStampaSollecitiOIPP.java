package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.controller.IRinnovoStampa;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Azione per la load inserisci Solleciti per gli Ordini di Ingiunzione al pagamento
 *
 * @author d.fiorletta
 * @since MAV_2023-33
 */
public class ActStampaSollecitiOIPP extends ActionSiap implements ICostantiRinnovo {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		BigDecimal lIdRinnovo = getRequestBigDecimalParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);

		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		RinnovoModel lRinModel = lCtrl.ExRicercaRinnovoByKey(lIdRinnovo);

		lRinModel.setDataAggiornamento(DateUtils.getSysDate());
		lRinModel.setCodUfficioAggiornamento(lUff.getCodUfficio());
		lRinModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lRinModel.setFlagDocumentoRegistrato("N");

		String flagTemplate = null;
		String lCodMotivo = "0283"; // FIXME

		if ("SP".equals(lRinModel.getCodTipoRinnovo())) {
			flagTemplate = "1";
		} else if ("SU".equals(lRinModel.getCodTipoRinnovo())) {
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
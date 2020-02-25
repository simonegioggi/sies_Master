package siap.siep.modulocumulo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActStampaRichiestaTrasmissioneTitolo extends ActionSiap {

	public String processRequest() throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		UtenteModel lUtenteMod = this.getUtenteConnesso();
		// UfficioModel lUff = this.getUfficioUtenteConnesso();

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

		if ("S".equalsIgnoreCase(lEveMod.getEvento().getFlagDocumentoRegistrato())) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il documento è stato validato!<BR>Impossibile procedere con la Stampa");

			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.modulocumulo.action.ActDettaglioRichiestaTrasmissioneTitolo&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

			return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
		}

		// ===============================================
		// Recupero il template
		// ===============================================
		String flagTemplate = "0";

		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEveMod.getEvento().getCodTipoEvento(), lEveMod.getEvento().getCodTipoProvvedimento(),
				lEveMod.getEvento().getCodMotivo(), flagTemplate);

		lEveMod.setNomeTemplate(lTemMod.getIdTemplate());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEveMod.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());

		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}
}

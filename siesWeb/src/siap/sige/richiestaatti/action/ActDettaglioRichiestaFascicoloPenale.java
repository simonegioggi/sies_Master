package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioEstrattoSentenza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio Richiesta Sentenza Integrale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActDettaglioRichiestaFascicoloPenale extends ActSIESDettaglioProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal lIdProvvedimento = null;
		lIdProvvedimento = getRequestBigDecimalParameter(
				ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);

		// Ricerca Provvedimento dalla chiave
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		// ProvvedimentoSigeModel lProvvedimento =
		// lCtrlProv.ExRicercaProvDefinitorioByFasc(mFasEsteso.getFascicoloSige().getIdFascicoloSige());

		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		// setRequestAttribute("provvedimento", lProvvedimento);
		setRequestAttribute("ProvvedimentoEvento", lProvEvento);

		BigDecimal lId = lProvEvento.getEventoNotifica().getEvento().getIdEvento();

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lNots = lCtrlNot
				.ExRicercaNotificaByKeyEvento(lProvEvento.getEventoNotifica().getEvento().getIdEvento());

		lEveMod.setNotifiche((NotificaModel[]) lNots.toArray(new NotificaModel[0]));

		setRequestAttribute("eventonotifica", lEveMod);

		String lUfficio = null;
		if (lEveMod.getNotifiche().length > 0) {
			lUfficio = lEveMod.getNotifiche()[0].getUffCodUfficio();
		}

		UfficioModel lUffMod = new UfficioModel();

		if (lUfficio != null) {
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			lUffMod = lUff.getUfficioByKey(lEveMod.getNotifiche()[0].getUffCodUfficio().toUpperCase());
		}

		// ANGELA
		// Modificabilità
		String lModificabile = "NO";
		String lStampabile = "NO";

		// Stampabilità
		if (lEveMod.getEvento().getFlagDocumentoRegistrato() == null
				|| lEveMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
			// Modificabilità e Stampabilità coincidono
			lStampabile = "SI";
			lModificabile = "SI";
		}
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);
		setRequestAttribute("ufficio", lUffMod);

		// ANGELA
		// Creazione della combobox, nell'evento non sia già impostato uno
		// specifico template. Si è adottata tale soluzione che impatta
		// su tutte le funzionalità di stampa, al fine di evitare che si debba
		// legarsi a condizioni di if sui determinati codici motivo.
		if (lEveMod.getEvento().getTemIdTemplate() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>> Il CodMotivo nell'evento è : " + lEveMod.getEvento().getCodMotivo());

			ITemplate lCtrlTemplate = SICOLookupRemote.getTemplateRemote();
			TemplateModel lTemplateMod = new TemplateModel();

			lTemplateMod.setCodMotivo(lEveMod.getEvento().getCodMotivo());

			Collection lTemplates = lCtrlTemplate.ExListaCbxTemplate(lTemplateMod);
			// Prepara la ComboBox
			Option lOptionTemp = new Option(lTemplates);

			setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, lOptionTemp.toString());

		}

		return ICostantiRichiestaAtti.PG_DETTAGLIO_RICHIESTA_FASCICOLOPENALE;
	}

}
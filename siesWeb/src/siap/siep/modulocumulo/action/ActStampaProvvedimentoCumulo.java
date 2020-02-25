package siap.siep.modulocumulo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action Per la Stampa del provvedimento di cumulo
 *
 * @author d.fiorletta
 *
 */
public class ActStampaProvvedimentoCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Controllo che non ci siano Titoli Esclusi
		// BigDecimal lIdIstru = getRequestBigDecimalParameter(
		// ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		// Vector ListaTitoli = super.getListaTitoli();
		// boolean lEscluso = false;
		// Iterator itx = ListaTitoli.iterator();
		// while (itx.hasNext()) {
		// TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) itx.next();
		// if (lTitoloModel != null && lTitoloModel.getIdTitoloCumulato() != null) {
		// if (lTitoloModel.getFlagEscluso() != null
		// && lTitoloModel.getFlagEscluso().compareTo("S") == 0) {
		// lEscluso = true;
		// }
		// }
		// }

		// if(lEscluso) {
		// throw new SIEPException (SIEPException.USER_MESSAGE, "Uno o più Titoli iscritti in istruttoria
		// risulta momentaneamente escluso. Per procedere alla emissione del provvedimento di Cumulo è
		// necessario prima provvedere ad escludere o includere definitivamente detto Titolo.");
		// }

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

		// Cod_Motivo = 0652 : provvedimento del GE di Unificazione di Pene Concorrenti
		if ("0652".equals(lEveNotMod.getEvento().getCodMotivo())) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Stampa non prevista per questo tipo di provvedimento. E' possibile solo procedere all'upload");
		}

		// ===============================================
		// Recupero il template
		// ===============================================
		String flagTemplate = null;

		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEveNotMod.getEvento().getCodTipoEvento(), lEveNotMod.getEvento().getCodTipoProvvedimento(),
				lEveNotMod.getEvento().getCodMotivo(), flagTemplate);

		// lEveNotMod.setNomeTemplate (lTemMod.getIdTemplate());
		lEveNotMod.getEvento().setTemIdTemplate(lTemMod.getIdTemplate());

		lEveNotMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveNotMod.getEvento().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEveNotMod.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());

		lEveNotMod.getEvento().setFlagDocumentoRegistrato("N");

		UtenteModel lUtente = getUtenteConnesso();
		UfficioModel lUfficio = getUfficioUtenteConnesso();

		IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
		ByteArrayOutputStream lReport = lCtrlDatiFinali.ExStampaProvvedimentoCumulo(lEveNotMod,
				lFascicoloModel, lUtente, lUfficio);

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}
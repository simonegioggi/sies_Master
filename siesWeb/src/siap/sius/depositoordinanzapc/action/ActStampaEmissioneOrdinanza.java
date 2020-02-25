package siap.sius.depositoordinanzapc.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaEmissioneOrdinanza extends ActionSiap implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Stampa dell'Ordinanza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoNotificaModel lEveMod = new EventoNotificaModel();
		// chiama il controller
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrlEve.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(
				lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

		UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		// Se il template non è stato definito sono in un Emissione Ordinanza UDS
		if (lEveMod.getEvento().getTemIdTemplate() == null
				|| lEveMod.getEvento().getTemIdTemplate().trim().length() < 1) {
			if (!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE)) {
				lEveMod.getEvento().setTemIdTemplate(
						this.getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE));
			}
		}

		// lEveMod.setNomeTemplate(TEMPLATE_ORDINANZA_AFFIDAMENTO);
		lEveMod.setNomeTemplate(lEveMod.getEvento().getTemIdTemplate());
		if (lEveMod.getNomeTemplate() != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Nome del template di stampa ordinanza : " + lEveMod.getNomeTemplate());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Nome del template di stampa ordinanza assente");

		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();

		// ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lFasGPMod,lEveMod);
		// .......Sostituzione
		ByteArrayOutputStream lReport = lCtrl.ExStampEmissioneOrdinanza(lEveMod.getEvento(),
				getCodUfficioUtenteConnesso(), super.getUtenteConnesso());

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

		setRequestAttribute("eventonotifica", lEveMod);

		// return IWebConstants.PG_DOWNLOAD;
		return IWebConstants.PG_DOWNLOAD_NEW;
	}

}
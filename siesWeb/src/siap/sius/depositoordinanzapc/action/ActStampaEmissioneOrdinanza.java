package siap.sius.depositoordinanzapc.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
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

		FascicoloGPModel fgpm = new FascicoloGPModel();
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		String idEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoNotificaModel enm = new EventoNotificaModel();
		// chiama il controller
		IEvento ie = SICOLookupRemote.getEventoRemote();
		enm = ie.ExRicercaEventoNotificaByKey(new BigDecimal(idEvento));
		enm.getEvento().setIdEvento(new BigDecimal(idEvento));
		enm.getEvento()
				.setFasSieIdFascicoloSiep(fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		UfficioModel um = new UfficioModel(getUfficioUtenteConnesso());
		enm.getEvento().setDescrLuogoEmittente(um.getDescrComune());
		enm.getEvento().setDescrUfficioEmittente(um.getDescrTipoUfficio());
		enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioAggiornamento(um.getCodUfficio());
		enm.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		enm.getEvento().setFlagDocumentoRegistrato("N");

		// Se il template non è stato definito sono in un Emissione Ordinanza UDS
		if (enm.getEvento().getTemIdTemplate() == null
				|| enm.getEvento().getTemIdTemplate().trim().length() < 1) {
			if (!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE)) {
				enm.getEvento()
						.setTemIdTemplate(getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE));
			}
		}

		enm.setNomeTemplate(enm.getEvento().getTemIdTemplate());
		if (enm.getNomeTemplate() != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Nome del template di stampa ordinanza : " + enm.getNomeTemplate());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Nome del template di stampa ordinanza assente");

		IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		ByteArrayOutputStream baos = idop.ExStampEmissioneOrdinanza(enm.getEvento(),
				getCodUfficioUtenteConnesso(), super.getUtenteConnesso());
		// Prepara la pagina di destinazione
		if (baos != null)
			setRequestAttribute("report", baos);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

		setRequestAttribute("eventonotifica", enm);

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD_NEW;
	}

}
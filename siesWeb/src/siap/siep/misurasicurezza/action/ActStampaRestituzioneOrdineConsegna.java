package siap.siep.misurasicurezza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActStampaRestituzioneOrdineConsegna
 * </p>
 * <p>
 * Description: Stampa della Restituzione Ordine di consegna
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author SGIOGGI
 * @version 1.0
 */
public class ActStampaRestituzioneOrdineConsegna extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String idEvento = getRequestStringParameter("IdEvento");
		String flagTemplate = "0";

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel utm = getUtenteConnesso();
		UfficioModel ufm = getUfficioUtenteConnesso();

		// Controllo se trattasi di: a) Fascicolo con Misura nata in sentenza; b) Misura Disposta Fuori
		// Sentenza e/o Misura Provvisoria
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = iEvento.ExRicercaEventoNotificaByKey(new BigDecimal(idEvento));
		enm.getEvento().setIdEvento(new BigDecimal(idEvento));
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		enm.getEvento().setDescrLuogoEmittente(ufm.getDescrComune());
		enm.getEvento().setDescrUfficioEmittente(ufm.getDescrTipoUfficio());
		enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioAggiornamento(ufm.getCodUfficio());
		enm.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		enm.getEvento().setFlagDocumentoRegistrato("N");

		// ========================================
		// Recupero il template se esiste
		// ========================================
		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("inzio ricerca >>> - flagTemplate = >" + flagTemplate + "<");
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(enm.getEvento()
					.getCodTipoEvento(), enm.getEvento().getCodTipoProvvedimento(), enm.getEvento()
					.getCodMotivo(), flagTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod  >>>" + lTemMod);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}

		if (lTemMod != null && lTemMod.getIdTemplate() != null && !lTemMod.getIdTemplate().equals("")) {
			enm.setNomeTemplate(lTemMod.getIdTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod.getIdTemplate  >>>" + lTemMod.getIdTemplate());
		} else {
			enm.setNomeTemplate("VUOTO");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod.getIdTemplate Vuoto  >>>VUOTO<<<<");
		}

		ByteArrayOutputStream lReport = iEvento.ExStampaDocumento(enm, utm);
		// setta la risposta nella request
		setRequestAttribute("report", lReport);

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD;
	}

} // Chiude classe ActStampaRestituzioneOrdineConsegna
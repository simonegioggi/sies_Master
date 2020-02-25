package siap.siep.richiesta.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;

/**
 * <p>
 * Title: ActStampaTrasmissioneAttiExArt51Bis
 * </p>
 * <p>
 * Description: innesca la stampa della Trasmissione del provvedimento
 * </p>
 * <p>
 * 'ATTI di Richiesta Cessazione/Prosecuzione Mis. Alt (Ex art 51 Bis)'
 * </p>
 * <p>
 * verso il tribunale/Magistrato di sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author C.Ambrosino
 * @version 1.0
 */
public class ActStampaTrasmissioneAttiExArt51Bis extends ActionSiap
		implements ICostantiRichiesta, ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter("IdEvento");
		// String lCod = getRequestStringParameter("CodMotivo");

		// String lIdPos = "";
		// if (!isRequestParameterNullObj("IdPosizioneGiuridica"))
		// lIdPos = getRequestStringParameter("IdPosizioneGiuridica");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		if (lEventoModel.getCodMotivo().equals("5440")) // richiesta cessazione Misura
			lEveMod.setNomeTemplate("SIEP_MA_440");

		if (lEventoModel.getCodMotivo().equals("5441")) // richiesta Prosecuzione Misura
			lEveMod.setNomeTemplate("SIEP_MA_441");

		// ========================================
		// Recupero il template se esiste, paolo modifica 28 aprile 2009
		// ========================================

		String flagTemplate = "0";
		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("inzio ricerca >>>");
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
					lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(),
					lEventoModel.getCodMotivo(), flagTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod  >>>" + lTemMod);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}

		if (lTemMod != null && lTemMod.getIdTemplate() != null && !lTemMod.getIdTemplate().equals("")) {
			lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod.getIdTemplate  >>>" + lTemMod.getIdTemplate());
		}

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}
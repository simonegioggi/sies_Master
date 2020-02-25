package siap.siep.misurasicurezza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

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
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActStampaRichiestaDAP
 * </p>
 * <p>
 * Description: Stampa Richiesta al DAP della designazione
 * </p>
 * <p>
 * Istituto per applicazione Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 * @version 1.0
 */
public class ActStampaRichiestaDAP extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter("IdEvento");
		String lCod = getRequestStringParameter("CodMotivo");

//		String codPos = "";
//		if (!isRequestParameterNullObj("PosizioneGiur"))
//			codPos = getRequestStringParameter("PosizioneGiur");

//		String codiceMis = "";
//		if (!isRequestParameterNullObj("misuraCod"))
//			codiceMis = getRequestStringParameter("misuraCod");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// Controllo se trattasi di: a) Fascicolo con Misura nata in sentenza; b) Misura Disposta Fuori
		// Sentenza e/o Misura Provvisoria
		String CodTipoSentenza = "";
		CodTipoSentenza = this.getTipoSentenza();
		//
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

		String flagTemplate = "0";

		// Richiesta al DAP
		if (lCod.equals("1127")) {
			if (CodTipoSentenza.compareTo("01") == 0) {
				// Misura nate in sentenza
				// lEveMod.setNomeTemplate("SIEP_MS_998"); // SIEP_MS_RICDESDAP
			} else {
				// Misura provvisorie e/o disposte Fuori Sentenza
				// lEveMod.setNomeTemplate("SIEP_MS_952"); // SIEP_MS_FS_RICDESDAP --> OK fatta
				flagTemplate = "1";
			}
		}

		// Designazione Istituto
		if (lCod.equals("1130")) {
			if (CodTipoSentenza.compareTo("01") == 0) {
				// Misura nate in sentenza
				// lEveMod.setNomeTemplate("SIEP_MS_993"); // SIEP_MS_COMDESIST
			} else {
				// Misura provvisorie e/o disposte Fuori Sentenza
				// lEveMod.setNomeTemplate("SIEP_MS_953"); // SIEP_MS_FS_COMDESIST --> OK fatta
				flagTemplate = "1";
			}
		}

		// ========================================
		// Recupero il template se esiste, paolo modifica 28 aprile 2009
		// ========================================

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
		} else {
			lEveMod.setNomeTemplate("VUOTO");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod.getIdTemplate Vuoto  >>>VUOTO<<<<");
		}

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}
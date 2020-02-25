package siap.siep.richiesta.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActUploadEmissioneComunicazioni
 * </p>
 * <p>
 * Description: Action che valida le Comunicazioni legate a una Richiesta o Concessione di Amnistia/Indulto,
 * Depenalizzazione e Incostituzionalità (Richieste al GE o Decisioni del GE)
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class ActUploadEmissioneComunicazioni extends ActionSiap implements ICostantiEvento {
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================================
		// Recupero l'evento della Comunicazione da validare
		// ==========================================================================
		EventoModel lModelComunicazione = new EventoModel();
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lModelComunicazione = lCtrlEvento
				.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModelComunicazione.setDocBlobIn(lSt);
		}

		lModelComunicazione.setDataAggiornamento(DateUtils.getSysDate());
		lModelComunicazione.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModelComunicazione.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		// ==========================================================================
		// Ricerco l'evento legato alla Comunicazione che è una Richiesta (01-26)
		// nel caso di Richieste al GE, o un Provvedimento (01-04) nel caso di
		// Decisione del GE
		// ==========================================================================
		EventoModel lEveModRic = new EventoModel();
		EventoModel lEveMod = null;

		lEveModRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveModRic.setCodTipoProvvedimento("04");
		lEveModRic.setCodTipoEvento("01");

		// String[] motivi ={"0210", "0211", "0122"};
		String[] provv = { "04", "26" }; // STUB 12-10-2005 REWORK STATO ESECUZIONE.

		// ANNOTAZIONI per Decisione del GE
		// 0300 - Depenalizzazione, Incostituzionalità
		// 0301 - Amnistia/Indulto
		if (lModelComunicazione.getCodMotivo().equals("0300")
				|| lModelComunicazione.getCodMotivo().equals("0301")) {
			String[] motiviGE = { "0284", "0285", "0286" };
			lEveMod = lCtrlEvento.ExRicercaEventoPerMotivoPerProvv(motiviGE, provv, lEveModRic);
		} else {
			// GE - Richieste al GE
			// ========================================================================
			// Recupero l'evento al quale è agganciata l'annotazione manuale
			// ========================================================================
			if (lModelComunicazione != null) {
				IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
				AnnotazioneManualeModel lAnnManMod = lCtrlAnn
						.ExRicercaAnnotazioneManualeByKey(lModelComunicazione.getAnnIdAnnotazioneManuale());

				if (lAnnManMod != null) {
					lEveMod = lCtrlEvento.ExRicercaEventoByKey(lAnnManMod.getEveIdEvento());

					if (lEveMod != null && lEveMod.getAnnIdAnnotazioneManuale() != null) {
						lEveMod = null;
					}

					if (lEveMod != null) {
						lEveMod.setDataAggiornamento(DateUtils.getSysDate());
						lEveMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
						lEveMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					}
				}
			}
		}

		IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			// Valido la Comunicazione
			lModelComunicazione.setFlagDocumentoRegistrato("S");

			if (lEveMod != null) {
				// Valido anche la Richiesta/Provvedimento contestualmente alla Comunicazione
				// n.b. potrebbe già essere stata validato
				lEveMod.setFlagDocumentoRegistrato("S");
				lEveMod.setDataAggiornamento(DateUtils.getSysDate());
				lEveMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lEveMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			}

			lCtrlRich.ExUpdateValidaEmissioneComunicazioni(lEveMod, lModelComunicazione, lFascMod);
		} else {
			lModelComunicazione.setFlagDocumentoRegistrato("N");

			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lModelComunicazione);
		}

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)) {
			String codice = this.getRequestStringParameter("codice");
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO) + "&codice=" + codice);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}
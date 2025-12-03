package siap.siep.ordineesecuzione.action;

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
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzioneAlfano;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActUploadL78del2013 - Classe Action per il caricamento legge 78/2013
 *
 * @version 1.0
 */
public class ActUploadL78del2013 extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));
		// Ticket#202511060155 — SIEP
		// si commenta il cod motivo altrimenti non è validabile dal cambio magistrato
		// lModel.setCodMotivo(getRequestStringParameter("motivo"));

		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());

		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		String lNonValidato = "N";
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			lModel.setFlagDocumentoRegistrato("S");

			IOrdineEsecuzioneAlfano lCtrl = SIEPLookupRemote.getOrdineEsecuzioneAlfanoRemote();
			lCtrl.ExUpdateValidaL78del2013(lModel, lFascMod);
		} else {
			lModel.setFlagDocumentoRegistrato("N");

			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lModel);

			lNonValidato = "S";

		}

		// Prepara la "pagina" di destinazione
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		String titolo = null;
		if (!isRequestParameterNullObj("titolo"))
			titolo = getRequestStringParameter("titolo");

		// il flag lNonValidato serve solo nel caso degli arresti domiciliari,perchè è l'unico caso che
		// ridirige su l'azione di trasferimento al tds...
		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
			String lAzione = getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO);
			if (lAzione.equals("siap.siep.ordineesecuzione.action.ActLoadTrasferisciProvvedimentoLS")
					&& lNonValidato.equals("S")) {

				lAzione = "siap.siep.ordineesecuzione.action.ActDettaglioLSArrestiDomiciliari";
			}

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(lAzione + "&" + CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO) + "&fc="
					+ getRequestStringParameter("fc") + "&titolo=" + titolo);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		}

		// pagina di ritorno
		return IWebConstants.PG_MESSAGE;
	}

}
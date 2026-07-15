package siap.siep.calcolopena.action;

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
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActUploadComputoCustodiaCautelare - Classe che gestisce il caricamento del computo custodia cautelere
 *
 * @version 1.0
 */
public class ActUploadComputoCustodiaCautelare extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
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

		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lCtrlPos
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		String codicePosizione = lPosMod.getCodPosizioneGiuridica();
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			lModel.setFlagDocumentoRegistrato("S");

			// if((codicePosizione.equals("07") || codicePosizione.equals("10")) ||
			// !lFascMod.getFlagAltraCausa().equals("S"))
			IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			lCtrlAnn.ExUpdatePeneEspiateSenzaTitolo(lModel, codicePosizione, lFascMod);
		} else {
			lModel.setFlagDocumentoRegistrato("N");
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lModel);
		}

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
			String flagPage = this.getRequestStringParameter("flagPage");
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO
					+ "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO) + "&flagPage="
					+ flagPage + "&lIdposizione=" + getRequestStringParameter("IdPosizioneGiuridica"));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}
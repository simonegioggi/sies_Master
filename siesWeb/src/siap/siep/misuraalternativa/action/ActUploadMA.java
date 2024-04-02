package siap.siep.misuraalternativa.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * ActUploadMA - Classe per la Validazione della misura alternativa
 *
 * @version 1.0
 */
public class ActUploadMA extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));

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

		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			lModel.setFlagDocumentoRegistrato("S");
			IMisuraAlternativa lCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			// String tipoMisura = getRequestStringParameter("tipoMisura");
			// lCtrl.ExUpdateValidaMA(lModel, lFascMod, tipoMisura,
			// getRequestStringParameter("IdEventoAmmProvvAff"));
			lCtrl.ExUpdateValidaMA(lModel, lFascMod);
		} else {
			lModel.setFlagDocumentoRegistrato("N");

			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lModel);
		}

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO
					+ "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}
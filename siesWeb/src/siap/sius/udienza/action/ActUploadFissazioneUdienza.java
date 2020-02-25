package siap.sius.udienza.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActUploadFissazioneUdienza extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		EventoModel lModel = new EventoModel();
		BigDecimal lIdEvento = new BigDecimal(getRequestStringParameter(CAMPO_ID_EVENTO));
		lModel.setIdEvento(lIdEvento);

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

		if (isRequestChecked(CAMPO_VALIDA))
			lModel.setFlagDocumentoRegistrato("S");
		else
			lModel.setFlagDocumentoRegistrato("N");

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lCtrl.ExUpdateDocument(lModel);

		// Prepara la pagina di destinazione
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		// lRedirigi.setAction("siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale" );
		lRedirigi.setAction("siap.sius.udienza.action.ActLoadDettaglioFissazioneUdienza&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEvento);
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}
package siap.siep.sanzionesostitutiva.action;

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

/**
 * <p>
 * Title: ActUploadTrasmissioneAttiEsecuzione
 * </p>
 * <p>
 * Description: Validazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActUploadTrasmissioneAttiEsecuzione extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
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

		lModel.setFlagDocumentoRegistrato("S");

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lCtrl.ExUpdateDocument(lModel);

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siep.sanzionesostitutiva.action.ActDettaglioTrasmissioneAttiEsecuzione");
		lRedirigi.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lModel.getIdEvento().toString());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}
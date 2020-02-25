package siap.siep.modulocumulo.action;

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
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action di validazione del provvedimento di Richiesta Trasmissione Atti
 * 
 * @author Intersistemi S.p.A.
 * @since 06/2017
 */
public class ActUploadComunicazioniProcure extends ActionSiap {

	public String processRequest() throws Exception {

		// ==========================================================================
		// Recupero l'evento da Validare
		// ==========================================================================
		EventoModel lEveModel = new EventoModel();
		lEveModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		// ==========================================================================
		// Scarico l'eventuale report
		// ==========================================================================
		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lEveModel.setDocBlobIn(lSt);
		}

		// ==========================================================================
		// Imposto i dati dell'aggiornamento
		// ==========================================================================
		lEveModel.setDataAggiornamento(DateUtils.getSysDate());
		lEveModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEveModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		// ==========================================================================
		// Validazione
		// ==========================================================================
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			lEveModel.setFlagDocumentoRegistrato("S");
			IModuloCumulo lCtrlModCum = SIEPLookupRemote.getModuloCumuloRemote();

			lCtrlModCum.ExUpdateValidaRichiestaTrasmissioneAtti(lEveModel);
		} else {
			lEveModel.setFlagDocumentoRegistrato("N");

			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lEveModel);
		}

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}
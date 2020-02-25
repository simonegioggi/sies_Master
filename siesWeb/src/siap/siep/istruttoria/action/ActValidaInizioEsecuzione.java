package siap.siep.istruttoria.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ActUploadDocument;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActValidaInizioEsecuzione extends ActUploadDocument {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdEvento = null;
		if (getRequestStringParameter(CAMPO_ID_EVENTO).equals("null")) {
			FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			EventoModel lEveMod = new EventoModel();

			lEveMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			lEveMod.setCodMotivo("0047");
			lEveMod.setCodTipoEvento("05");

			Vector lVect = new Vector();

			// chiama il controller
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();

			try {
				lVect = lCtrl.ExRicercaEvento(lEveMod);
			} catch (F3BException ex) {
				// Non è stato trovato nessun elemento
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>> +++ *** Nessun Evento Trovato!");
			}
			if (lVect != null && lVect.size() > 0)
				lEveMod = (EventoModel) lVect.firstElement();

			lIdEvento = lEveMod.getIdEvento();

			setRequestAttribute(CAMPO_ID_EVENTO, lEveMod.getIdEvento());
			this.getRequest().setAttribute(CAMPO_ID_EVENTO, lEveMod.getIdEvento());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("in request CAMPO_ID_EVENTO = " + lEveMod.getIdEvento() + " In request "
					+ getRequestStringParameter(CAMPO_ID_EVENTO));

		} else
			lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): inizio");
		String lPage = IWebConstants.PG_MESSAGE;

		// Lettura ID Evento

		// Flag che segnala la necessità del controllo della presenza del documento nel BLOB
		boolean lControlloBlob = true;
		// Se si proviene dalla form di Warning non si effettua il controllo sul BLOB
		if (!isRequestParameterNullObj(CAMPO_CK_WARNING))
			lControlloBlob = false;

		if (lControlloBlob) {
			// Lettura del file di Upload
			InputStream lInput = null;
			lInput = getFile(ICostantiEvento.CAMPO_BLOB);

			if (lInput != null && lInput.available() > 0) {
				byte[] lBuffer = new byte[lInput.available()];
				lInput.read(lBuffer);
				mInStr = new ByteArrayInputStream(lBuffer);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("BYTE ARRAY INPUT LENGTH >>> " + mInStr.available());
				lControlloBlob = false;
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("file di Upload non disponibile !");
		}

		if (!isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
			lControlloBlob = false;

		boolean lisUpdate = true;

		if (lControlloBlob) {
			// Controllo esistenza documento di stampa per consentire la validazione
			try {
				leggiDocumento(lIdEvento);
			} catch (Exception e) {
				lisUpdate = false;
				lPage = PG_WARNING;
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione: E' stata richiesta la validazione di un documento privo di stampa !");
				passaggioParametri();
			}
		}
		// Update
		if (lisUpdate) {
			updateTabella(lIdEvento);
			// Prepara la "pagina" di destinAction
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");
		}
		// Se c'è lo stack di ritorno effettua un ritorno in cima
		String lRitorno = goToRitorno();
		if (lRitorno == null && !isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO
					+ "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			if (!isSessionAttributeNullObj(IWebConstants.STACK_RITORNO))
				lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): fine");
		return lPage;
	}

}
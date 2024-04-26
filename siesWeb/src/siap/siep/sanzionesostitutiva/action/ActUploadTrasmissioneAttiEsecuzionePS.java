package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * Azione per l'upload della Trasmissione Atti Esecuzione Pena Sostitutiva
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActUploadTrasmissioneAttiEsecuzionePS extends ActionSiap implements ICostantiEvento {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		EventoModel em = new EventoModel();
		em.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));

		InputStream is = getFile(ICostantiEvento.CAMPO_BLOB);

		if (is != null) {
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			em.setDocBlobIn(bais);

		}

		em.setDataAggiornamento(DateUtils.getSysDate());
		em.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		em.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		em.setFlagDocumentoRegistrato("S");

		IEvento ie = SICOLookupRemote.getEventoRemote();
		ie.ExUpdateDocument(em);

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setAction("siap.siep.sanzionesostitutiva.action.ActLoadDettaglioTrasmissioneAttiEsecuzione");
		rt.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, em.getIdEvento().toString());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_MESSAGE;
	}

}
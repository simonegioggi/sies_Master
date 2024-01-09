package siap.siep.rateizzazionepp.action;

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
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per fare upload del Provvedimento Estinzione Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActUploadProvvedimentoEstinzionePP extends ActionSiap implements ICostantiEvento {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		// ===============================================
		// Recupero l'id dell'evento
		// ===============================================
		EventoModel em = new EventoModel();
		em.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));

		InputStream is = getFile(ICostantiEvento.CAMPO_BLOB);

		if (is != null) {
			byte[] buffer = new byte[is.available()];

			is.read(buffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(buffer);
			em.setDocBlobIn(lSt);
		}

		em.setDataAggiornamento(DateUtils.getSysDate());
		em.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		em.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			// devo effettuare la validazione
			// ====================================================
			// Invoco la funzione di validazione
			// ====================================================
			em.setFlagDocumentoRegistrato("S");

			IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
			irpp.exUpdateProvvedimentoEstinzionePP(em);
		} else { // aggiorno solo il blob
			em.setFlagDocumentoRegistrato("N");
			IEvento ie = SICOLookupRemote.getEventoRemote();
			ie.ExUpdateDocument(em);
		}

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
		{
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO));
			rt.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, em.getIdEvento().toString());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
		}
		
		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_MESSAGE;
	}

}
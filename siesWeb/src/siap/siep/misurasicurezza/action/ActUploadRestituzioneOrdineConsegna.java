package siap.siep.misurasicurezza.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * MEV_39
 * <p>
 * Title: ActUploadRestituzioneOrdineConsegna
 * </p>
 * <p>
 * Description: Valida la restituzione dell'ordine di consegna
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author SGIOGGI
 * @version 1.0
 */
public class ActUploadRestituzioneOrdineConsegna extends ActionSiap implements ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel em = new EventoModel();
		em.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		InputStream is = getFile(ICostantiEvento.CAMPO_BLOB);
		if (is != null) {
			byte[] lBuffer = new byte[is.available()];
			is.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			em.setDocBlobIn(lSt);
			is.close();
		}

		em.setDataAggiornamento(DateUtils.getSysDate());
		em.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		em.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		String valida = "";
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
			valida = getRequestStringParameter(ICostantiEvento.CAMPO_VALIDA);
		if ("S".equals(valida) || "1".equals(valida)) {
			em.setFlagDocumentoRegistrato("S");
			IRichiesta iRichiesta = SIEPLookupRemote.getRichiestaRemote();
			iRichiesta.ExValidaAnnotazioneOComunicazioneApplicazioneMS(em, fsm);
		} else {
			em.setFlagDocumentoRegistrato("N");
			IEvento iEvento = SICOLookupRemote.getEventoRemote();
			iEvento.ExUpdateDocument(em);
		}

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
		}

		// valore di ritorno
		return IWebConstants.PG_MESSAGE;
	}

}
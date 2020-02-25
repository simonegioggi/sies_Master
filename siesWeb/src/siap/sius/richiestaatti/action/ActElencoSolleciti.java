package siap.sius.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActElencoSolleciti
 * </p>
 * <p>
 * Description: Azione specializzazione per la ricerca dei solleciti
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActElencoSolleciti extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNot = lCtrl.ExRicercaEventoNotificaByKeyForRichiestaAtti(lIdEvento);

		// Ricerca del documento allegato
		IDocumentoAllegato lCtrlDoc = SIUSLookupRemote.getDocumentoAllegatoRemote();
		Vector lDocs = lCtrlDoc.ExRicercaSollecitoByIdEvento(lIdEvento);

		setRequestAttribute("eventoNotifica", lEveNot);
		setRequestAttribute("allegati", lDocs);

		// si ricava l'eventuale Action di ritorno dalla jsp
		if (!isRequestParameterNullObj("CAMPO_ACTION_RET"))
			setRequestAttribute("actRet", getRequestStringParameter("CAMPO_ACTION_RET"));

		return PG_ELENCOSOLLECITI;
	}

}
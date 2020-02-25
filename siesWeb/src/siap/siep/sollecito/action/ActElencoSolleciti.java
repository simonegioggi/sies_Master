package siap.siep.sollecito.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.util.SIUSLookupRemote;

public class ActElencoSolleciti extends ActionSiap implements ICostantiSollecito {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNot = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

		// Ricerca del documento allegato
		IDocumentoAllegato lCtrlDoc = SIUSLookupRemote.getDocumentoAllegatoRemote();
		Vector lDocs = lCtrlDoc.ExRicercaSollecitoByIdEvento(lIdEvento);

		setRequestAttribute("eventoNotifica", lEveNot);
		setRequestAttribute("allegati", lDocs);
		setRequestAttribute("idNotifica", getRequestBigDecimalParameter(ICostantiNotifica.CAMPO_ID_NOTIFICA));

		return PG_ELENCOSOLLECITI;
	}

}
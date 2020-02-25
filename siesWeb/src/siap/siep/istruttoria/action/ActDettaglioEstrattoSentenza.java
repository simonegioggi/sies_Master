package siap.siep.istruttoria.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActDettaglioEstrattoSentenza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio Richiesta Sentenza Integrale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActDettaglioEstrattoSentenza extends ActSIESDettaglioProvvedimento {

	public String processRequest() throws Exception {

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lNots = lCtrlNot.ExRicercaNotificaByKeyEvento(new BigDecimal(lId));

		lEveMod.setNotifiche((NotificaModel[]) lNots.toArray(new NotificaModel[0]));

		setRequestAttribute("eventonotifica", lEveMod);

		String lUfficio = lEveMod.getNotifiche()[0].getUffCodUfficio();
		UfficioModel lUffMod = new UfficioModel();

		if (lUfficio != null) {
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			lUffMod = lUff.getUfficioByKey(lEveMod.getNotifiche()[0].getUffCodUfficio().toUpperCase());
		}

		setRequestAttribute("ufficio", lUffMod);

		return ICostantiIstruttoria.PG_DETTAGLIO_ESTRATTO_SENTENZA;
	}

}
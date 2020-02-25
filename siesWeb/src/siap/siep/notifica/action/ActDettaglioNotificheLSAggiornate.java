package siap.siep.notifica.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioNotificheLSAggiornate
 * </p>
 * <p>
 * Description: Classe Action
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
@SuppressWarnings("rawtypes")
public class ActDettaglioNotificheLSAggiornate extends ActionSiap implements ICostantiNotifica {

	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		INotifica lCtrl = SIEPLookupRemote.getNotificaRemote();
		Vector lNot = lCtrl.ExRicercaEstesaNotificaByKeyEvento(lIdEvento);

		setRequestAttribute("notifiche", lNot);

		return PG_LOAD_DETTAGLIO_NOTIFICHE_LS;
	}

}
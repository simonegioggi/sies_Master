package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActModificaNotificheEventoSige
 * </p>
 * <p>
 * Description: Classe Action per la ricerca delle Notifiche legate ad un Evento
 * </p>
 * La ricerca delle notifiche viene effettuata per modificare le date di notifica.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActModificaNotificheEventoSige extends ActionSiap implements ICostantiProvvedimentoSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		INotifica lCtrl = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = lCtrl.ExRicercaEstesaNotificaByKeyEvento(lIdEvento);
		setRequestAttribute("notifiche", lVect);
		setRequestAttribute("IdEvento", lIdEvento);
		setRequestAttribute("modalita", "M");
		return PG_NOTIFICHE_EVENTO_SIGE;
	}

}
package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioNotificheEventoSige
 * </p>
 * <p>
 * Description: Classe Action per la ricerca delle Notifiche legate ad un Evento-Provvedimento in Sige
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
public class ActDettaglioNotificheEventoSige extends ActionSiap implements ICostantiProvvedimentoSige {

	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		INotifica lCtrl = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = lCtrl.ExRicercaEstesaNotificaByKeyEvento(lIdEvento);
		setRequestAttribute("notifiche", lVect);
		setRequestAttribute("IdEvento", lIdEvento);
		setRequestAttribute("modalita", "D");
		return PG_NOTIFICHE_EVENTO_SIGE;
	}

}
package siap.siep.penaaccessoria.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioRichiestaGE
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Richiesta al GE per Pena Accessoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadDettaglioRichiestaGE extends ActSIESDettaglioProvvedimento
		implements ICostantiPenaAccessoria, ICostantiEvento {

	public String processRequest() throws F3BException {

		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lId = getRequestStringParameter(CAMPO_ID_EVENTO);

		// Si Imposta il model lEveMod;
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// Si Invoca il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

		setRequestAttribute("eventonotifica", lEveMod);

		// Si Imposta il model lPenMod;
		PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();

		if (lEveMod.getEvento().getPenAccIdPenaAccessoria() != null
				&& lEveMod.getEvento().getPenAccIdPenaAccessoria().toString().length() > 1) {
			lPenMod.setIdPenaAccessoria(lEveMod.getEvento().getPenAccIdPenaAccessoria());
			// Si Invoca il controller
			IPenaAccessoria lCtrl2 = SIEPLookupRemote.getPenaAccessoriaRemote();
			lPenMod = lCtrl2.ExRicercaPenaAccessoriaByKey(lPenMod);
		} else
			lPenMod.setDescrTipoPenaAccessoria(" Richiesta non associata a Pena Accessoria");
		setRequestAttribute("penaaccessoria", lPenMod);

		return PG_LOAD_DETTAGLIORICHIESTAGE;
	}
}

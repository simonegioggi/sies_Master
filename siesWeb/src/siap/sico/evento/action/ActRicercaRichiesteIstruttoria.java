package siap.sico.evento.action;

/**
* <p>Title: ActRicercaRichiesteIstruttoria</p>
* <p>Description: Classe Action per la ricerca di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActRicercaRichiesteIstruttoria extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// lEve.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		String[] lTipoEvento = { "05" };
		Vector lVect = lCtrl.ExRicercaEventoNotificaByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep(),
				lTipoEvento);
		setRequestAttribute("eventi", lVect);

		return PG_RICERCAEVENTO;
	}

}
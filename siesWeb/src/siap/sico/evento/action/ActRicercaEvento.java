package siap.sico.evento.action;

/**
* <p>Title: ActRicercaEvento</p>
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
public class ActRicercaEvento extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// lEve.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		Vector lVect = lCtrl.ExRicercaEventoNotificaByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep(),
				null);
		setRequestAttribute("eventi", lVect);

		return PG_RICERCAEVENTO;
	}

}
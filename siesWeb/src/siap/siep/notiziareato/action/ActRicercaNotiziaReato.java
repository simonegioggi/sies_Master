package siap.siep.notiziareato.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notiziareato.controller.NotiziaReatoController;
import siap.siep.notiziareato.model.NotiziaReatoModel;

/**
 * <p>
 * Title: ActRicercaNotiziaReato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di NotiziaReato
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
public class ActRicercaNotiziaReato extends ActionSiap implements ICostantiNotiziaReato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// istanzia il Model
		NotiziaReatoModel lNotMod = new NotiziaReatoModel();

		// prende dalla Session l'ID del procedimento
		lNotMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());

		// chiama il controller
		// INotiziaReato lCtrl = SIEPLookupRemote.getNotiziaReatoRemote();
		NotiziaReatoController lCtrl = new NotiziaReatoController();

		// Instanzia il vettore
		Vector lVect = lCtrl.ExRicercaNotiziaReato(lNotMod);

		// setta la risposta nella request
		setRequestAttribute("elenconotiziareato", lVect);

		// Apre la pagina con l'elenco delle Notizie di Reato relative ad un procedimento
		return PG_RICERCANOTIZIAREATO;
	}

}
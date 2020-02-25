package siap.sico.ufficio.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.IComune;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * Azione di ricerca di tutti i comuni nel Distretto sedi UNEP.
 * 
 * @author Lesposito
 *
 */
@SuppressWarnings("rawtypes")
public class ActLoadUNEPdistretto extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		// Interfaccia al Controller che effettua la ricerca
		IComune lCCon = SICOLookupRemote.getComuneRemote();

		// Lettura dalla form di input del codice Distretto
		String lcodDistretto = getRequestStringParameter("distretto");

		// Ricerca dei Comuni sedi di uffici UNEP nel Distretto
		Vector lComuniUNEP = lCCon.ExGetListaSediUNEPperDistretto(lcodDistretto);

		// passaggio all'output della lista di comuni
		setRequestAttribute("ListaUfficiDistretto", lComuniUNEP);

		return PG_SEDI_UNEP_DISTRETTO;
	}

}
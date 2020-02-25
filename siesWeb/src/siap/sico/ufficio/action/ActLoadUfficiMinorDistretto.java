package siap.sico.ufficio.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

@SuppressWarnings("rawtypes")
public class ActLoadUfficiMinorDistretto extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		IUfficio lUCon = SICOLookupRemote.getUfficioRemote();

		Vector lUffDistr = null;
		if (!isRequestParameterNullObj("distretto")) {
			lUffDistr = lUCon.ListaUfficiMinorDistretto(getRequestStringParameter("distretto"));
		} else {
			lUffDistr = lUCon.ListaUfficiPerTipo("PMM");
		}

		setRequestAttribute("ListaUfficiDistretto", lUffDistr);
		setRequestAttribute("NomeLista", "Elenco Procure Minorili");

		return PG_UFFICIDISTRETTO;
	}

}
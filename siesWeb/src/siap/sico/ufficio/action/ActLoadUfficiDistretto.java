package siap.sico.ufficio.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

@SuppressWarnings("rawtypes")
public class ActLoadUfficiDistretto extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		// UfficioModel lUMod = new UfficioModel();

		// STUB: 2003/02/17 - UfficioController lUCon = new UfficioController();
		IUfficio lUCon = SICOLookupRemote.getUfficioRemote();

		// String codDistretto = this.getRequestStringParameter("distretto");
		/*
		 * Luigi 19-4-2004 String codTipoUff=this.getRequestStringParameter("codTipoUff");
		 */
		Vector lUffDistr = lUCon.ListaUfficiDistretto(getRequestStringParameter("distretto"));

		setRequestAttribute("ListaUfficiDistretto", lUffDistr);

		return PG_UFFICIDISTRETTO;
	}

}
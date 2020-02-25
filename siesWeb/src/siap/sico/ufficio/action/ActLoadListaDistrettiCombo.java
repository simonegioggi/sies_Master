package siap.sico.ufficio.action;

import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadListaDistrettiCombo extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		// Chiamo il controller
		// UfficioController lUctrl = new UfficioController();
		IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();

		Vector lUfficio = lUctrl.ListaDistretti();

		// *assign vector ricerca per distretti");
		// imposta la risposta nella request

		String codTipoUff = this.getRequestStringParameter("codTipoUff");
		this.setRequestAttribute("codTipoUff", codTipoUff);

		setRequestAttribute("ListaDistretti", lUfficio);

		return PG_LISTADISTRETTI_COMBO; // restituisce la jsp di VIEW
	}

}
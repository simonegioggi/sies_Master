package siap.siep.istitutodetenzione.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActListaIstituto extends ActionSiap implements ICostantiIstitutoDetenzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaIstituto());

		IUfficio lUfficio = SICOLookupRemote.getUfficioRemote();
		Vector lDistretti = lUfficio.ListaDistretti();

		setRequestAttribute("tipoistituto", "" + lOption);
		setRequestAttribute("distretti", lDistretti);

		return PG_ISTITUTO_LISTA; // restituisce la jsp di VIEW
	}

}
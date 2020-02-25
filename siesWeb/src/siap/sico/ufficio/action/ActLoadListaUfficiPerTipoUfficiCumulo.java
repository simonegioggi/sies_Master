package siap.sico.ufficio.action;

import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadListaUfficiPerTipoUfficiCumulo extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		// String strFormname = getRequestStringParameter("formname");

		String lCodTipoUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO);

		if (lCodTipoUfficio.equals("-"))
			throw new F3BException(F3BException.USER_MESSAGE, "Specificare l' Autorità");

		IUfficio lCtrl = SICOLookupRemote.getUfficioRemote();
		Vector lListaUffici = lCtrl.ListaUfficiPerTipoUfficiCumulo(lCodTipoUfficio);

		// Ricavo la Descrizione del tipo ufficio
		String lDescrTipoUfficio = "";
		if (!lListaUffici.isEmpty()) {
			UfficioModel lUff = (UfficioModel) lListaUffici.get(0);
			lDescrTipoUfficio = lUff.getDescrTipoUfficio();
		}

		// imposta la risposta nella request
		setRequestAttribute("ListaUffici", lListaUffici);
		setRequestAttribute("Funzione", "Lista Uffici :" + lDescrTipoUfficio);

		return PG_LISTA_UFFICI_PER_TIPO; // restituisce la jsp di VIEW
	}

}
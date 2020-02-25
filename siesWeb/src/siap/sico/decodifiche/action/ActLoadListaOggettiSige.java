package siap.sico.decodifiche.action;

import java.util.Collection;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActLoadListaOggettiSige extends ActionSiap implements ICostantiDecodifiche {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String strFormName = getRequestStringParameter("formname");
		String strFieldName = getRequestStringParameter("fieldname");

		String strFieldCode = "";
		if (!(isRequestParameterNullObj("fieldcode")))
			strFieldCode = getRequestStringParameter("fieldcode");

		IDecodifiche lCtrl = SICOLookupRemote.getDecodificheRemote();
		Collection lOggetti = lCtrl.ExListaOggettiSige();

		// Imposta la risposta nella request.
		setRequestAttribute("ListaOggetti", lOggetti);
		setRequestAttribute("fieldcode", strFieldCode);
		setRequestAttribute("fieldname", strFieldName);
		setRequestAttribute("formname", strFormName);

		return PG_LISTAOGGETTI_SIGE; // restituisce la jsp di VIEW
	}

}
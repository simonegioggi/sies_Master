package siap.sico.decodifiche.action;

import java.util.Collection;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActLoadListaOggetti extends ActionSiap implements ICostantiDecodifiche {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
//		String strFormname = getRequestStringParameter("formname");
		String strContenuto = getRequestStringParameter("field_contenuto");
//		String strFieldname = getRequestStringParameter("fieldname");
//		String strFieldcodes = getRequestStringParameter("fieldcodes");
		// STUB 12/11/2003 Aggiunta i codici dei dettagliOggetto.
//		String strFieldcodesdet = getRequestStringParameter("fieldcodesdet");

		String strIFieldcodes = "";
		if (!(isRequestParameterNullObj("ifieldcodes")))
			strIFieldcodes = getRequestStringParameter("ifieldcodes");

		String strIFieldcodesdet = "";
		if (!(isRequestParameterNullObj("ifieldcodesdet")))
			strIFieldcodesdet = getRequestStringParameter("ifieldcodesdet");

		IDecodifiche lCtrl = SICOLookupRemote.getDecodificheRemote();
		Collection lOggetti = lCtrl.ExListaOggetti(strContenuto, strCodTipoUfficio);

		// imposta la risposta nella request
		setRequestAttribute("ListaOggetti", lOggetti);
		setRequestAttribute("InputFieldCodes", strIFieldcodes);
		setRequestAttribute("InputFieldCodesDet", strIFieldcodesdet);

		return PG_LISTAOGGETTI; // restituisce la jsp di VIEW
	}

}
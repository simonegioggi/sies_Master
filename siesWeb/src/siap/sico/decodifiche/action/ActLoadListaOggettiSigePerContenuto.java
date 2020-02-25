package siap.sico.decodifiche.action;

import java.util.Collection;
import java.util.Iterator;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.OggettiModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;

public class ActLoadListaOggettiSigePerContenuto extends ActionSige implements ICostantiDecodifiche {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String strFormName = getRequestStringParameter("formname");
		String strFieldName = getRequestStringParameter("fieldname");
		String codContenuto = getRequestStringParameter("CodContenuto");

		if (codContenuto.equals("-"))
			throw new F3BException(F3BException.USER_MESSAGE, "Specificare il Contenuto");

		String strFieldCode = "";
		if (!(isRequestParameterNullObj("fieldcode")))
			strFieldCode = getRequestStringParameter("fieldcode");

		IDecodifiche lCtrl = SICOLookupRemote.getDecodificheRemote();
		Collection lOggetti = lCtrl.ExListaOggettiSigePerContenuto(codContenuto);

		// Ricavo la Descrizione del Contenuto
		String lDescrContenuto = "";

		Iterator itx = lOggetti.iterator();
		while (itx.hasNext()) {
			OggettiModel oggettoModel = (OggettiModel) itx.next();
			lDescrContenuto = oggettoModel.getDescContenuto();
		}

		// Imposta la risposta nella request.
		setRequestAttribute("ListaOggetti", lOggetti);
		setRequestAttribute("Funzione", "Lista Oggetti per il Contenuto :" + lDescrContenuto);

		setRequestAttribute("fieldcode", strFieldCode);
		setRequestAttribute("fieldname", strFieldName);
		setRequestAttribute("formname", strFormName);

		return PG_LISTAOGGETTI_SIGE_PER_CONTENUTO; // restituisce la jsp di VIEW
	}

}
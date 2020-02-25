package siap.sico.decodifiche.action;

import java.util.Collection;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

@SuppressWarnings("rawtypes")
public class ActLoadListaAttivita extends ActionSiap implements ICostantiDecodifiche {

	public String processRequest() throws Exception {

		// String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// String lNomeForm = getRequestStringParameter("formname");
		String lCodIncarico = getRequestStringParameter("codice_incarico");
		// String lNomeCampoCodiceAttivita = getRequestStringParameter("fieldname");
		// String lNomeCampoDescrizioneAttivita = getRequestStringParameter("fieldcodes");

		IDecodifiche lCtrl = SICOLookupRemote.getDecodificheRemote();
		Collection lListaAttivita = lCtrl.ExRicercaAttivitaByIncarico(lCodIncarico);

		// imposta la risposta nella request
		setRequestAttribute("ListaAttivita", lListaAttivita);

		return PG_LISTATTIVITA; // restituisce la jsp di Pop UP
	}

}
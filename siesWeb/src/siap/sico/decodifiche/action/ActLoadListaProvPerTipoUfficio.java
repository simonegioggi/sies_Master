package siap.sico.decodifiche.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.IComuneProvincia;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadListaProvPerTipoUfficio extends ActionSiap implements ICostantiComune {

	public String processRequest() throws F3BException {

		// Recupero l'utente dalla sessione.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// ComuneProvinciaController lCPctrl = new ComuneProvinciaController();
		IComuneProvincia lCPctrl = SICOLookupRemote.getComuneProvinciaRemote();

		Vector lProvTipoPerUfficio = lCPctrl
				.ExListaProvPerTipoUfficio(lUtenteMod.getUfficioUtente().getCodTipoUfficio());
		// imposta la risposta nella request
		setRequestAttribute("ProvList", lProvTipoPerUfficio);

		setRequestAttribute("codProvinciaUtenteConnesso", lUtenteMod.getUfficioUtente().getCodProvincia());

		return PG_LISTAPROV; // restituisce la jsp di VIEW
	}

}
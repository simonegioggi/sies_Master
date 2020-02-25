package siap.sico.ufficio.action;

import java.util.Vector;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadListaPMM extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		// Individuo l'utente in sessione e passo il suo codice distretto alla request
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getCodDistretto();
		setRequestAttribute("CodiceDistrettoUtente", StrCodiceDistrettoUtente);

		// Chiamo il controller
		// UfficioController lUctrl = new UfficioController();
		IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();

		Vector lUfficioPMM = lUctrl.ListaPMM();
		// imposta la risposta nella request

		setRequestAttribute("ListaDistretti", lUfficioPMM);

		return PG_LISTADISTRETTI; // restituisce la jsp di VIEW
	}

}
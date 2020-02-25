package siap.sico.ufficio.action;

import java.util.Vector;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadListaUDS extends ActionSiap implements ICostantiUfficio {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		String ret = "";
		Vector lUDS = null;
		IUfficio lCtrl = SICOLookupRemote.getUfficioRemote();

		if (!isRequestParameterNullObj("typename")) {

			if ("UDSM".equals(getRequestStringParameter("typename"))
					|| "TDSM".equals(getRequestStringParameter("typename"))) {
				lUDS = lCtrl.ListaUDSM();
				// MEV10-s3: aggiunta modifica etichette nella lista
				if ("TDSM".equals(getRequestStringParameter("typename")))
					ret = PG_LISTAUDS + "?NomeLista=Lista Tribunali di Sorveglianza per i Minorenni";
				else {
					UtenteModel lUtenteMod = new UtenteModel(
							(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
					String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
					if ("PM".equals(lCodTipoUfficio) || "PMM".equals(lCodTipoUfficio) || "PGCAP".equals(lCodTipoUfficio))
						ret = PG_LISTAUDS + "?NomeLista=Lista Uffici del Magistrato di Sorveglianza per i Minorenni";
					else
			    		ret = PG_LISTAUDS + "?NomeLista=Lista Uffici di Sorveglianza per i Minorenni";
				}
			} else {
				lUDS = lCtrl.ListaUDS();
				ret = PG_LISTAUDS + "?NomeLista=Lista Uffici di Sorveglianza";
			}

		} else {
			lUDS = lCtrl.ListaUDS();
			ret = PG_LISTAUDS;
		}

		// imposta la risposta nella request
		setRequestAttribute("ListaUDS", lUDS);

		return ret; // restituisce la jsp di VIEW
	}

}

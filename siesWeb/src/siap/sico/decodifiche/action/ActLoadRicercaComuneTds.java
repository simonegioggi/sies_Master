package siap.sico.decodifiche.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.IComune;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadRicercaComuneTds extends ActionSiap implements ICostantiComune, ICostantiUfficio {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		String ret = "";

		Vector lCom = null;
		String nomeLista = "";

		IComune lCCon = SICOLookupRemote.getComuneRemote();
		IUfficio lCtrl = SICOLookupRemote.getUfficioRemote();

		if (!isRequestParameterNullObj("typename")) {
			String typename = getRequestStringParameter("typename");
			if ("UDSM".equals(typename)) {
				// MEV10-s3: aggiunta modifica etichette nella lista
				UtenteModel lUtenteMod = new UtenteModel(
						(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
				String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
				lCom = lCtrl.ListaUDSM();
				if ("PM".equals(lCodTipoUfficio) || "PMM".equals(lCodTipoUfficio) || "PGCAP".equals(lCodTipoUfficio)) {
					ret = PG_LISTAUDS + "?NomeLista=Lista Uffici del Magistrato di Sorveglianza per i Minorenni";
		    	} else
		    		ret = PG_LISTAUDS + "?NomeLista=Lista Uffici di Sorveglianza per i Minorenni";
				nomeLista = "ListaUDS";
			} else if ("UDS".equals(typename)) {
				lCom = lCtrl.ListaUDS();
				ret = PG_LISTAUDS + "?NomeLista=Lista Uffici di Sorveglianza";
				nomeLista = "ListaUDS";
			} else if ("TDSM".equals(typename)) {
				lCom = lCCon.ExGetListaComuniTdsm();
				ret = PG_RICERCA_COMUNE_TDS + "?NomeLista=Lista Tribunali di Sorveglianza per i Minorenni";
				nomeLista = "ListaComuni";
			} else {
				lCom = lCCon.ExGetListaComuniTds();
				ret = PG_RICERCA_COMUNE_TDS + "?NomeLista=Lista Tribunali di Sorveglianza";
				nomeLista = "ListaComuni";
			}

		} else {
			lCom = lCCon.ExGetListaComuniTds();
			ret = PG_RICERCA_COMUNE_TDS;
			nomeLista = "ListaComuni";
		}

		// imposta la risposta nella request
		setRequestAttribute(nomeLista, lCom);

		return ret; // restituisce la jsp di VIEW
	}

}
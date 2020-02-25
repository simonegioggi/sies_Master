package siap.sico.utente.action;

import java.util.Vector;

import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaUtentiAttiviPerUfficio
 * </p>
 * <p>
 * Description: Classe Action per la ricerca degli Utenti Attivi per un Ufficio
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaUtentiAttiviPerUfficio extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		UtenteModel utenteCorrente = (UtenteModel) getSession().getAttribute("UtenteConnesso");
		String codUfficio = utenteCorrente.getUfficioUtente().getCodUfficio();

		String cognome = getRequestStringParameter(ICostantiUtente.CAMPO_COGNOME);
		IUtente lCtrl = SICOLookupRemote.getUtenteRemote();

		Vector lVect = lCtrl.ExRicercaUtentiAttiviPerUfficio(codUfficio, cognome);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("field2", getRequestStringParameter("field2"));
		setRequestAttribute("field3", getRequestStringParameter("field3"));

		setRequestAttribute("utenti", lVect);

		return ROOT_DIR + "files/siap/sico/utente/RicercaUtente.jsp";
	}

}
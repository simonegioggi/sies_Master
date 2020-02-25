package siap.sico.utente.action;

/**
* <p>Title: ActModificaPassword</p>
* <p>Description: Classe Action per la modifica della password Utente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;

public class ActModificaPassword extends ActionSiap implements ICostantiUtente {

	/**
	 * Azione di Modifica del Utente
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// String lPage=new String();

		if (((UtenteModel) getSessionAttribute("UtenteConnesso")).getPwd() != null) {
			if (!((UtenteModel) getSessionAttribute("UtenteConnesso")).getPwd()
					.equals(Utils.cryptPassword(getRequestStringParameter("oldPwd")))) {
				setRequestAttribute("msg", "<font color=red><b>Vecchia Password Errata</b></font>");

				return ICostantiSecurity.PG_CHANGE_PASSWORD;
			}
		}

		String lId = getRequestStringParameter(CAMPO_COD_UTENTE);

		UtenteModel lUt = new UtenteModel();
		lUt.setUserId(lId);
		lUt.setPwd(getRequestStringParameter("Pwd"));

		IUtente lIut = SICOLookupRemote.getUtenteRemote();

		lIut.ExModificaPassword(lUt);

		// return IWebConstants.PAGE_EXIT_TO_FRAMESET;
		return IWebConstants.PAGE_OPEN_FRAMESET;
	}

}
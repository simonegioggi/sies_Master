package siap.sico.ufficio.action;

import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadModificaUfficio
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica Ufficio
 * </p>
 * Viene caricata
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadModificaUfficio extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		Vector lUffici = new Vector();
		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();

		if (getUtenteConnesso().isSysAdmin()) { // Se l'utente connesso è un SuperAdministrator con id profilo
												// 99, ritorna
												// l'elenco della lista degli uffici appartenete al proprio
												// distretto.
												// lUffici =
												// lUffCtrl.ListaUfficiCompletaDistrettoAbilitatiLogin(F3BProperties.getInstance().getProperty("Bdi.DistrictCode"));
			lUffici = lUffCtrl.ListaUfficiCompletaDistrettoAbilitatiLogin(this
					.getCodDistrettoUtenteConnesso());
		} else {
			UfficioModel lUfficioSession = getUfficioUtenteConnesso();
			String CodUfficio = lUfficioSession.getCodUfficio();
			UfficioModel lUfficio = lUffCtrl.ExRicercaUfficioByCod(CodUfficio);

			// Se Esiste un ufficio lo stesso viene inserito nel Vector
			if (lUfficio != null)
				lUffici.add(lUfficio);
		}

		setRequestAttribute("uffici", lUffici);
		setRequestAttribute("modalita", "M");

		return PG_MODIFICA_UFFICIO;
	}

}
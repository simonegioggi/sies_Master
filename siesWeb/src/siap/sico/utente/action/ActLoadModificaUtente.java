package siap.sico.utente.action;

import java.util.Vector;

import siap.sico.profilo.controller.IProfilo;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadModificaUtente
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di Utente
 * </p>
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
public class ActLoadModificaUtente extends ActionSiap implements ICostantiUtente {

	public String processRequest() throws F3BException {

		Vector lProfili = new Vector();
		Vector lUffici = new Vector();

		// Preleva dalla request l'id dell'utente da modificare
		String lId = getRequestStringParameter(CAMPO_COD_UTENTE);

		IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		UtenteModel llUteMod = lCtrl.ExRicercaUtenteByKey(lId); // Preleva l'utente oggetto di modifica

		IProfilo lPrf = SICOLookupRemote.getProfiloRemote();

		// Verifica se l'utente di sessione è un System Admin profilo 99
		if (getUtenteConnesso().isSysAdmin()) {
			// Se l'utente connesso è un SysAdmin con id profilo 99, ritorna l'elenco completo dei profili.
			lProfili = lPrf.ExRicercaListaProfili();
			// Se l'utente connesso è un SuperAdministrator con id profilo 99, ritorna l'elenco della lista
			// degli uffici
			// appartenente al proprio distretto.
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			// lUffici =
			// lUff.ListaUfficiCompletaDistrettoAbilitatiLogin(F3BProperties.getInstance().getProperty("Bdi.DistrictCode"));
			lUffici = lUff.ListaUfficiCompletaDistrettoAbilitatiLogin(this.getCodDistrettoUtenteConnesso());
		} else {
			// Preleva l'Ufficio dalla sessione, per inserrlo nel vector.
			UfficioModel lUfficio = getUfficioUtenteConnesso();
			// Se Esiste un ufficio lo stesso viene inserito nel Vector
			if (lUfficio != null)
				lUffici.add(lUfficio);
			// Se l'utente connesso non ha i diritti di SysAdmin, preleva solo i profili associati
			// al Codice tipo uffcio
			lProfili = lPrf.ExRicercaProfiliByCodTipoUfficio(lUfficio.getCodTipoUfficio());
		}

		setRequestAttribute("utente", llUteMod);
		setRequestAttribute("profili", lProfili);
		setRequestAttribute("uffici", lUffici);

		return PG_LOAD_MODIFICAUTENTE;
	}

}
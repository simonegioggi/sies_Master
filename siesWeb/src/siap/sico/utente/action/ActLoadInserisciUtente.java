package siap.sico.utente.action;

import java.util.Vector;

import siap.sico.profilo.controller.IProfilo;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciUtente
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Utente
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
public class ActLoadInserisciUtente extends ActionSiap implements ICostantiUtente {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		Vector lProfili = new Vector();
		Vector lUffici = new Vector();

		IProfilo lPrf = SICOLookupRemote.getProfiloRemote();

		IUfficio lUff = SICOLookupRemote.getUfficioRemote();

		// Verifica se l'utente di sessione è un System Admin profilo 99
		if (getUtenteConnesso().isSysAdmin()) { // Se l'utente connesso è un SysAdmin con id profilo 99, ritorna
												// l'elenco completo dei profili.
			lProfili = lPrf.ExRicercaListaProfili();
			// Se l'utente connesso è un SuperAdministrator con id profilo 99, ritorna l'elenco della lista degli uffici
			// appartenete al proprio distretto.
			// lUffici =
			// lUff.ListaUfficiCompletaDistrettoAbilitatiLogin(F3BProperties.getInstance().getProperty("Bdi.DistrictCode"));
			lUffici = lUff.ListaUfficiCompletaDistrettoAbilitatiLogin(this.getCodDistrettoUtenteConnesso());

			// MERGE v10 COLLAUDO: elimino la modifica su richiesta utente
			// MEV10-s3: elimino un elemento dalla lista vettoriale: "Tribunale per i minorenni"
//			Iterator it = lUffici.iterator();
//			while (it.hasNext()) {
//				UfficioModel um = (UfficioModel) it.next();
//				if ("DIBM".equals(um.getCodTipoUfficio()))
//					it.remove();
//			}
		} else {
			// Preleva l'ufficio model dalla sessione.
			UfficioModel lUfficio = getUfficioUtenteConnesso();
			// Se Esiste un ufficio lo stesso viene inserito nel Vector
			if (lUfficio != null)
				lUffici.add(lUfficio);

			// Se l'utente connesso non ha i diritti di SysAdmin, preleva solo i profili associati
			// al Codice tipo uffcio
			lProfili = lPrf.ExRicercaProfiliByCodTipoUfficio(lUfficio.getCodTipoUfficio());

		}

		setRequestAttribute("profili", lProfili);
		setRequestAttribute("uffici", lUffici);
		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCIUTENTE; // restituisce la jsp di VIEW
	}
}

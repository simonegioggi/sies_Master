package siap.sico.security.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import siap.sico.helponline.controller.IHelponline;
import siap.sico.helponline.model.HelponlineModel;
//import siap.sico.security.controller.SecurityController;
import siap.sico.security.controller.ISecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.security.model.FunctionModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadOrizontalMenu extends ActionSiap implements ICostantiSecurity {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(SESSION_UTENTE_CONNESSO);

		FunctionModel lFunRadiceMenuOrz = (FunctionModel) getSessionAttribute(FUN_RADICE_MENU_ORZ);
		FunctionModel lFunRadiceMenuVrt = (FunctionModel) getSessionAttribute(FUN_RADICE_MENU_VRT);
		LinkedList lFunAntenate = (LinkedList) getSessionAttribute(FUN_ANTENATE);

		ArrayList lFunFiglieMenuOrz = lFunRadiceMenuOrz.getDaughtersFunctions();

		ArrayList lFunFiglieMenuVrt = lFunRadiceMenuVrt.getDaughtersFunctions();

		FunctionModel lFunRichiesta = new FunctionModel(getRequestBigDecimalParameter(CAMPO_ID_FUNZIONE));

		// ----- AGGIUNTO PER L'HELP --------------------------------------------
		setSessionAttribute("LastFunctionID", "" + lFunRichiesta.getFunctionId());

		IHelponline lCtrl = SICOLookupRemote.getHelponlineRemote();

		HelponlineModel lHelMod = lCtrl.ExRicercaHelponlineById(lFunRichiesta.getFunctionId());

		if (lHelMod != null) {
			setSessionAttribute("HelpPage", lHelMod.getNomePagina());
		}
		// ----- AGGIUNTO PER L'HELP --------------------------------------------

		int i = lFunFiglieMenuOrz.lastIndexOf(lFunRichiesta); // indice menu orizzontale
		int j = lFunFiglieMenuVrt.lastIndexOf(lFunRichiesta); // indice menu verticale
		int k = lFunAntenate.lastIndexOf(lFunRichiesta); // indice funzioni antenate

		if (i != -1) // La funzione è nel menu orizzontale
		{

			lFunRichiesta = (FunctionModel) lFunFiglieMenuOrz.get(i);

			lFunAntenate.addLast(lFunRichiesta);
		} else if (j != -1) // La funzione è nel menu verticale
		{

			lFunRichiesta = (FunctionModel) lFunFiglieMenuVrt.get(j);

			lFunAntenate = new LinkedList();
			lFunAntenate.addLast(lFunRichiesta);
		} else if (k != -1) // La funzione è negli antenati
		{

			ListIterator lListIterator = lFunAntenate.listIterator(k);

			lListIterator.next();
			while (lListIterator.hasNext()) {
				lListIterator.next();
				lListIterator.remove();
			}
		} else // La funzione non è disponbile
		{
			// throw new F3BException("Funzione non disponibile");
			return "/html/blank.htm"; // Torna alla pagina iniziale del menu orizzontale
		}

		// SecurityController lSctrl = new SecurityController();
		ISecurity lSctrl = SICOLookupRemote.getSecurityRemote();
		FunctionModel lNewFunRadiceMenuOrz = lSctrl.ExLoadFunzioniMenu(lUtenteConnesso.getUserProfile(),
				lFunRichiesta);

		setSessionAttribute(FUN_RADICE_MENU_ORZ, lNewFunRadiceMenuOrz);
		setSessionAttribute(FUN_ANTENATE, lFunAntenate);

		// MEV_65: rimuovo info di sessioni
		removeSessionAttribute("model");
		removeSessionAttribute("modelRO");

		return IWebConstants.PG_ORIZONTAL_MENU;
	}

}
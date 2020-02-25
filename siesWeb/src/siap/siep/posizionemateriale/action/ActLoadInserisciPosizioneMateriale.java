package siap.siep.posizionemateriale.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.util.UfficioUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadInserisciPosizioneMateriale
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PosizioneMateriale
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
public class ActLoadInserisciPosizioneMateriale extends ActionSiap implements ICostantiPosizioneMateriale {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Costruzione della lista degli uffici
		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
		// Vector lUffici =
		// lUffCtrl.ListaUfficiProcuraXDistrettoAbilitatiLogin(F3BProperties.getInstance().getProperty("Bdi.DistrictCode"));
		Vector lUffici = lUffCtrl
				.ListaUfficiProcuraXDistrettoAbilitatiLogin(this.getCodDistrettoUtenteConnesso());

		// Verifica che l'utente non sia un super ossia che abbia profilo != 99
		if (!getUtenteConnesso().isSysAdmin()) {
			lUffici = UfficioUtils.estraeUfficio(lUffici, super.getCodUfficioUtenteConnesso());

			if (lUffici.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE,
						"Ufficio non esistente per la gestione della funzione!");
		}

		setRequestAttribute("uffici", lUffici);
		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCIPOSIZIONEMATERIALE; // restituisce la jsp di VIEW
	}

}
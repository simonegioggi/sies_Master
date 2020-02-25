package siap.sico.ufficio.action;

import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.parametro.action.ICostantiParametro;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciPeriodoFeriale
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di inserimento peridodo feriale
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
public class ActLoadInserisciPeriodoFeriale extends ActionSiap
		implements ICostantiUfficio, ICostantiParametro {

	public String processRequest() throws F3BException {

		IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();

		ParametroModel lParMod = lCtrlPar.ExRicercaParametroUfficioConnesso(PERIODO_FERIALE,
				this.getCodUfficioUtenteConnesso());

		if (lParMod != null) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Per l'ufficio già esiste un periodo feriale personalizzato, utilizzare la funzione di modifica.");
		}

		Vector lUffici = new Vector();

		IUfficio lUff = SICOLookupRemote.getUfficioRemote();

		if (getUtenteConnesso().isSysAdmin()) {
			// Se l'utente connesso è un SuperAdministrator con id profilo 99, ritorna l'elenco della lista
			// degli uffici
			// appartenete al proprio distretto.
			// lUffici =
			// lUff.ListaUfficiCompletaDistrettoAbilitatiLogin(F3BProperties.getInstance().getProperty("Bdi.DistrictCode"));
			lUffici = lUff.ListaUfficiCompletaDistrettoAbilitatiLogin(this.getCodDistrettoUtenteConnesso());
		} else {
			// Preleva l'ufficio model dalla sessione.
			UfficioModel lUfficio = getUfficioUtenteConnesso();
			// Se Esiste un ufficio lo stesso viene inserito nel Vector
			if (lUfficio != null)
				lUffici.add(lUfficio);
		}

		setRequestAttribute("uffici", lUffici);
		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCI_PERIODO_FERIALE;
	}

}
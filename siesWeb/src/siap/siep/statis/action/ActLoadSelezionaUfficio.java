package siap.siep.statis.action;

/**
* <p>Title: ActLoadSelezionaUfficio</p>
* <p>Description: Classe Action pre-load di ricerca di StatoFascicoloRes, che fa la Load della Selezione UFFICIO</p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: Is</p>
* @version 6.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActLoadSelezionaUfficio extends ActionSiap implements ICostantiStatis {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "RIEPILOGO_STATI_PROCEDIMENTI",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// NGG - Statistiche SIEP - Recupero Cod Tipo Ufficio e Cod Ufficio Utente
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();
		String lTipoUffUte = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		// recupero gli eventuali uffci accorpati
		IUfficio lCtrlUf = SICOLookupRemote.getUfficioRemote();
		Vector lListaUffici = lCtrlUf.ListaUfficiAccorpati(lTipoUffUte, lUffUtente);
		// riempio il model
		UfficioModel UffMod = new UfficioModel();
		UffMod.setUfficiAccorpati(lListaUffici);
		UffMod.setCodTipoUfficio(lTipoUffUte);
		UffMod.setCodUfficio(lUffUtente);

		setRequestAttribute("ufficiomod", UffMod);
		setRequestAttribute("ListaUffAcc", lListaUffici);

		return PG_LOAD_SELEZIONA_UFFICIO; // restituisce la jsp di VIEW

	} // Chiude processRequest

} // Chiude classe
//
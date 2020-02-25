package siap.siep.statis.action;

/**
* <p>Title: ActLoadTempiIscrizioni</p>
* <p>Description: Classe Action per la load della ricerca per tempi Iscrizione Fascicoli</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.DateUtils;
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

public class ActLoadTempiIscrizioni extends ActionSiap implements ICostantiStatis {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "TEMPI_ISCRIZIONE_FASCICOLI", "1",
				getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// RECUPERO SYSDATE
		String sysDate = DateUtils.getSysDate("dd/MM/yyyy");

		setRequestAttribute("sysdate", sysDate);

		// NGG - Statistiche SIEP - Recupero Ufficio Utente
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();
		String lTipoUffUte = lUtenteMod.getUfficioUtente().getCodTipoUfficio();

		IUfficio lCtrl = SICOLookupRemote.getUfficioRemote();
		Vector lListaUffici = lCtrl.ListaUfficiAccorpati(lTipoUffUte, lUffUtente);

		UfficioModel UffMod = new UfficioModel();
		UffMod.setUfficiAccorpati(lListaUffici);
		UffMod.setCodTipoUfficio(lTipoUffUte);
		UffMod.setCodUfficio(lUffUtente);

		setRequestAttribute("ufficiomod", UffMod);
		setRequestAttribute("ListaUffAcc", lListaUffici);

		return PG_LOAD_TEMPIISCRIZIONI; // restituisce la jsp di VIEW

	}
}
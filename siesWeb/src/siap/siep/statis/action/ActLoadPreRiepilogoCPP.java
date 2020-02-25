package siap.siep.statis.action;

/**
* <p>Title: ActLoadPreRiepilogoCPP				</p>
* <p>Description: Classe Action per la load della Pre-selezione di date euffici per  	</p>
* <p>	la Successiva Statistica "Riepilogo Procedimenti Pendenti per la Classe VII"	</p>
* <p>		(C.P.P. - Conversione Pene Pecuniarie)				</p>
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

public class ActLoadPreRiepilogoCPP extends ActionSiap implements ICostantiStatis {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "STATISTICA_PROC_PENDENTI_CPP",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// RECUPERO SYSDATE
		String sysDate = DateUtils.getSysDate("dd/MM/yyyy");

		setRequestAttribute("sysdate", sysDate);

		// Recupero Ufficio Utente
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

		return PG_LOAD_PRE_RIEPILOGO_PENDENTI_CPP;
		// return PG_LOAD_STAT_RIEPILOGO_ISCRIZIONI_E_ATTIVITA_CPP;
	}

}
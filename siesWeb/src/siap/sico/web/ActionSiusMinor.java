package siap.sico.web;

import java.util.HashSet;
import java.util.Set;

import siap.sius.ActionSius;
import f3b.util.F3BException;

public class ActionSiusMinor extends ActionSius {

	protected static Set<String> ufficiMinori = new HashSet<String>();
	static {
		ufficiMinori.add("PMM");
		ufficiMinori.add("DIBM");
		ufficiMinori.add("GIPM");
		ufficiMinori.add("GUPM");
		ufficiMinori.add("CAPSM");
		ufficiMinori.add("TDSM");
		ufficiMinori.add("UDSM");
	}

	protected String checkMinori() throws F3BException {
		String ret = "";
		if (getUtenteConnesso().getUfficioUtente() != null && getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
			String tipoUfficioUtente = getUtenteConnesso().getUfficioUtente().getCodTipoUfficio();
			if (!ufficiMinori.contains(tipoUfficioUtente)) {
				ret = getUtenteConnesso().getUfficioUtente().getCodUfficio();
			}
		}
		return ret;
	}

}

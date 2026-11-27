package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

// 20261124-MEV_2026-124_SG - form di ricerca dello scadenzario pene sostitutive scadute
public class ActLoadRicercaScadenzarioPenaSostitutiva extends ActionSiap {

	public static final String PG_LOAD = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioPenaSostitutiva.jsp";

	public String processRequest() throws F3BException {
		return PG_LOAD;
	}
}

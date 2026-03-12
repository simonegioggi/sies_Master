package siap.sico.utenzaAdn.util;

import f3b.util.F3BException;
import f3b.util.LookupClass;
import siap.sico.utenzaAdn.controller.IAssocUtenteSiesAdn;
import siap.sico.utenzaAdn.controller.IUtenzaAdn;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class ADNLookupRemote extends LookupClass {

	public static IUtenzaAdn getUtenzaAdnRemote() throws F3BException {

		Object o;
		IUtenzaAdn iua;
		o = lookup("siap.sico.utenzaAdn.controller.UtenzaAdnController");
		iua = (IUtenzaAdn) o;

		return iua;
	}

	public static IAssocUtenteSiesAdn getAssocUtenteSiesAdnRemote() throws F3BException {

		Object o;
		IAssocUtenteSiesAdn iausa;
		o = lookup("siap.sico.utenzaAdn.controller.AssocUtenteSiesAdnController");
		iausa = (IAssocUtenteSiesAdn) o;

		return iausa;
	}

}
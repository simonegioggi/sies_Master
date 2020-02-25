package siap.siep.notifica.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadRicercaSolleciti
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Solleciti
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
public class ActLoadRicercaSolleciti extends ActionSiap implements ICostantiNotifica {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// if (!this.isRequestParameterNullObj("fieldname") &&
		// this.getRequestStringParameter("fieldname") != null )
		// if (!this.isRequestAttributeNullObj("fieldname") && getRequestAttribute("fieldname") != null)

		// In "fieldname" o c'e l'id della notifica (> 4) o c'e 'null'
		BigDecimal lKey = null;
		if (this.getRequestStringParameter("fieldname").length() > 4) {
			lKey = new BigDecimal(getRequestStringParameter("fieldname"));
		} else {
			throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste alcun Sollecito precedente -1-.");
		}

		Vector lVectRinMod = new Vector();
		String[] lTipoRinno = { "S" };
		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		lVectRinMod = lCtrl.ExRicercaRinnovoIdNotificaCodTipoRinnovo(lKey, lTipoRinno);

		if (lVectRinMod != null && lVectRinMod.size() == 0) {
			throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste alcun Sollecito precedente -2-.");
		}

		setRequestAttribute("notifica", getRequestStringParameter("fieldname_1"));
		setRequestAttribute("rinnovo", lVectRinMod);

		return PG_LOAD_RICERCA_SOLLECITI;
	}

}
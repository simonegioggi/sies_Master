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
 * Title: ActLoadRicercaRinnovazioneNotifica
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Omesse Notifica
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
public class ActLoadRicercaRinnovazioneNotifica extends ActionSiap implements ICostantiNotifica {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BigDecimal lKey = new BigDecimal(getRequestStringParameter("fieldname"));

		Vector lVectRinMod = new Vector();
		String[] lTipoRinno = { "U", "P" };
		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		lVectRinMod = lCtrl.ExRicercaRinnovoIdNotificaCodTipoRinnovo(lKey, lTipoRinno);

		if (lVectRinMod != null && lVectRinMod.size() == 0) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non Esiste alcuna Rinnovazione Notifica precedente.");
		}

		setRequestAttribute("notifica", getRequestStringParameter("fieldname_1"));
		setRequestAttribute("rinnovo", lVectRinMod);

		return PG_LOAD_RICERCA_NOTIFICA_LISTA_RINNOVAZIONE;
	}

}
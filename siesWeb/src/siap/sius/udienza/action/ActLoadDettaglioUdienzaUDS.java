package siap.sius.udienza.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Udienza
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

public class ActLoadDettaglioUdienzaUDS extends ActionSiap implements ICostantiUdienza {
	public String processRequest() throws F3BException {

		// Modifica del 04/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		// In fase di Fissazione Udienza, dare la possibilità all'utente di definire
		// una nuova udienza direttamente dalla pagina "Inserimento Fissazione Udienza"
		// senza passare dalle Funzioni Amministrative
		String checkInsFissUdienza = null;
		if (!this.isRequestParameterNullObj(ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA)) {
			checkInsFissUdienza = this.getRequestStringParameter(ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA);
		}
		setRequestAttribute("checkInsFissUdienza", checkInsFissUdienza);

		String lId = getRequestStringParameter(CAMPO_ID_UDIENZA);
		// riempie il model
		// chiama il controller

		IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
		UdienzaModel llUdiMod = lCtrl.ExRicercaUdienzaByKeyUDS(new BigDecimal(lId));
		setRequestAttribute("udienza", llUdiMod);

		// MEV10-s3: modificato il codice tipo ufficio: lo prelevo dalla tipologia di utente connesso
		String lCodTipoUfficio = getCodTipoUfficioConnesso();
		setRequestAttribute("codTipoUfficio", "" + lCodTipoUfficio);

		return PG_LOAD_DETTAGLIOUDIENZA_UDS;
	}

}
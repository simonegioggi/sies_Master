package siap.siep.sentenza.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title : ActLoadDettaglioDatiProvvedimentoMSFuoriSent
 * </p>
 * <p>
 * Description: Load del Dettaglio dati del decreto/ordinanza relativo a un fascicolo
 * </p>
 * <p>
 * di Misura Sicurezza disposta Fuori Sentenza o Provvisoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Intersistemi Italia s.p.a.
 * </p>
 * 
 * @version 8.3
 */

public class ActLoadDettaglioDatiProvvedimentoMSFuoriSent extends ActionSiap implements ICostantiSentenza {

	public String processRequest() throws Exception {

		// UtenteModel lUtenteMod = new
		// UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SENTENZA);

		// Riempie il model con idsentenza
		SentenzaModel lSmod = new SentenzaModel();
		lSmod.setIdSentenza(lId);

		ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();
		SentenzaModel lSen = lCtrl.ExRicercaSentenzaByKey(lSmod.getIdSentenza());

		// Inserisce in session la sentenza model.
		setSessionAttribute("sentenza", lSen);
		setRequestAttribute("sentenza", lSen);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.info("Provvedimento ======> " + lSen);

		// Modificabilità
		String lModificabile = "NO";
		if (lSen.getCodUfficioInserimento().compareTo(getCodUfficioUtenteConnesso()) == 0)
			lModificabile = "SI";

		setRequestAttribute("Modificabile", lModificabile);
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("Provvedimento modificabile : " + lModificabile);

		if (!isRequestParameterNullObj("TornaQui"))
			this.setRequestAttribute("TornaQui", this.getRequestStringParameter("TornaQui"));

		// Ritorna sull'azione Dettaglio Fascicolo
		setRequestAttribute(IWebConstants.LINK_RITORNO, "10");

		return PG_DETTAGLIO_DATI_PROVVEDIMENTO_MS_FUORI_SENT;
	}

}
package siap.siepe.espertoattivita.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.siepe.attivita.action.ActLoadDettaglioAttivita;
import siap.siepe.espertoattivita.controller.IEspertoAttivita;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciEsperto
 * </p>
 * <p>
 * Description: Classe Action per la gestione degli Esperti relativi ad una Attività.
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
public class ActLoadInserisciEsperto extends ActLoadDettaglioAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Si richiama il Dettaglio
		String lpage = super.processRequest();

		BigDecimal lIdAttivita = getRequestBigDecimalParameter(CAMPO_ID_ATTIVITA);

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ATTIVITA",
				getRequestStringParameter(CAMPO_ID_ATTIVITA), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L' " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Chiama il controller per risalire alla lista degli Esperti già assegnati all'Attivitàattività
		IEspertoAttivita lCtrl = SIEPELookupRemote.getEspertoAttivitaRemote();
		Vector lEsperti = lCtrl.ExRicercaEspertiXAttivita(lIdAttivita);
		// La lista degli esperti viene passata all request
		setRequestAttribute("esperti", lEsperti);

		// Imposta Modalità.
		setRequestAttribute("modalita", "E");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return lpage; // restituisce la jsp di VIEW
	}

	public void ritorno() throws Exception {
		gestioneRitorno();
	}

}
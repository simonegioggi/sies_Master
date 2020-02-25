package siap.sius.udienza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

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

public class ActLoadDettaglioUdienza extends ActionSius implements ICostantiUdienza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
	 * Azione di Dettaglio dell'Udienza
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Modifica del 04/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		// In fase di Fissazione Udienza, dare la possibilità all'utente di definire
		// una nuova udienza direttamente dalla pagina "Inserimento Fissazione Udienza"
		// senza passare dalle Funzioni Amministrative
		String checkInsFissUdienza = null;
		if (!this.isRequestParameterNullObj(ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA)) {
			checkInsFissUdienza = this.getRequestStringParameter(ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA);
		}
		setRequestAttribute("checkInsFissUdienza", checkInsFissUdienza);

		// Preleva dalla request l'id dell'udienza da visualizzare.
		String lId = getRequestStringParameter(CAMPO_ID_UDIENZA);
		gestioneRitorno();

		// Chiama il controller e popola il model.
		IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
		UdienzaModel llUdiMod = lCtrl.ExRicercaUdienzaByKey(new BigDecimal(lId));

		// Debug Stampa il contenuto del model.
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info(llUdiMod);

		// Imposta in request i dati dell'udienza.
		setRequestAttribute("udienza", llUdiMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		// MEV10-s3: il codice tipo ufficio lo prelevo dalla tipologia di utente connesso
		String lCodTipoUfficio = getCodTipoUfficioConnesso();
		setRequestAttribute("codTipoUfficio", "" + lCodTipoUfficio);

		// valore di ritorno
		return PG_LOAD_DETTAGLIOUDIENZA;
	}
}
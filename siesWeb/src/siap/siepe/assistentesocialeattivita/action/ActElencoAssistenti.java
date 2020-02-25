package siap.siepe.assistentesocialeattivita.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.siepe.assistentesocialeattivita.controller.IAssistenteSocialeAttivita;
import siap.siepe.attivita.action.ActLoadDettaglioAttivita;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActElencoAssistenti
 * </p>
 * <p>
 * Description: Classe Action per la visualizzazione dello "Storico Assistenti Sociali x Attività".
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
public class ActElencoAssistenti extends ActLoadDettaglioAttivita {

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

		// Chiama il controller per risalire alla lista degli Assistenti Sociali assegnati all'Attività nel
		// tempo
		IAssistenteSocialeAttivita lAssCtrl = SIEPELookupRemote.getAssistenteSocialeAttivitaRemote();
		Vector lAssistentiSociali = lAssCtrl.ExRicercaAssistentiSocialiXAttivita(lIdAttivita);

		// La lista degli assistenti sociali viene passata all request
		setRequestAttribute("assistenti", lAssistentiSociali);

		// Imposta Modalità.
		setRequestAttribute("modalita", "A");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return lpage; // restituisce la jsp di VIEW
	}

	public void ritorno() throws Exception {
		gestioneRitorno();
	}

}
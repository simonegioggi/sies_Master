package siap.sius.ulterioreistanza.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.ulterioreistanza.controller.IUlterioreIstanza;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaUlterioreIstanza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di UlterioreIstanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaUlterioreIstanza extends ActionSius implements ICostantiUlterioreIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo dell'Azione che innesca la ricerca dell'elenco delle Ulteriori Istanze e ritorna la pagina di
	 * destinazione per il layout.
	 * <p>
	 * 
	 * @return String pagina di layout.
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): inizio");

		// Inizializza e popola il model con il dato utile per la ricerca.
		// id del fascicolo sius
		UlterioreIstanzaModel lUltMod = new UlterioreIstanzaModel();
		lUltMod.setFasSiuIdFascicoloSius(
				getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));

		// Chiama il controller e esegue la ricerca delle ulteriori istanze.
		IUlterioreIstanza lCtrl = SIUSLookupRemote.getUlterioreIstanzaRemote();
		Vector lVect = lCtrl.ExRicercaUlterioreIstanza(lUltMod);

		// Imposta l'elenco delle ulteriori istanze.
		setRequestAttribute("ulterioriistanze", lVect);

		this.setLinkRitorno(); // Marca il punto di ritorno.

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): fine");

		return PG_RICERCAULTERIOREISTANZA;
	}

}
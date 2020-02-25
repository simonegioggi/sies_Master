package siap.sige.sentenza.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.sentenza.action.ActLoadModificaSentenza;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActLoadModificaSentenzaSige
 * </p>
 * <p>
 * Description: Specializzazione della Action di Preparazione alla modifica di una Sentenza per il sistema
 * SIGE.
 * </p>
 * <p>
 * Questa ricerca i Fascicoli Sige collegati alla sentenza da modificare e li passa nella request.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Luigi
 * @version 1.0
 */
public class ActLoadModificaSentenzaSige extends ActLoadModificaSentenza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Elenco dei Fascicoli Sige associati alla Sentenza
		Vector lFascicoliSige = new Vector();
		// Elenco dei Fascicoli Sige associati alla Sentenza ed allo stesso Ufficio dell'operatore
		Vector lFascicoliSigeUff = new Vector();
		// Elenco dei Fascicoli Sige associati alla Sentenza e ad Uffici diversi da quello dell'operatore
		Vector lFascicoliSigeAltroUff = new Vector();

		// Elaborazione dati nella request demandata all'ancestor
		super.processRequest();

		// Viene istanziato il controller per la ricerca dei Fascicoli SIGE collegati alla sentenza
		IFascicoloSige lFasSigeCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		lFascicoliSige = lFasSigeCtrl
				.ExRicercaFascicoloSigeBySentenza(getRequestBigDecimalParameter(CAMPO_ID_SENTENZA));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(
				"Num.ro Fascicoli Sige associati alla Sentenza da modificare -> " + lFascicoliSige.size());

		if (lFascicoliSige.size() > 0) {
			// Si discriminano Fascicoli modificabili da quelli non modificabili
			for (int i = 0; i < lFascicoliSige.size(); i++) {
				FascicoloSigeModel lFascModVerifica = (FascicoloSigeModel) lFascicoliSige.get(i);

				// Fascicoli modificabili se appartenenti all?Ufficio dell'operatore e se in stato Iscritto
				if (lFascModVerifica.getChiaveUfficio().equals(getCodUfficioUtenteConnesso())
						&& lFascModVerifica.getCodStatoFascicolo().equalsIgnoreCase("02")) {
					lFascicoliSigeUff.add(lFascModVerifica);
				} else {
					lFascicoliSigeAltroUff.add(lFascModVerifica);
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(
					"Num.ro Fascicoli Sige associati al Soggetto dell'Ufficio di competenza modificabili -> "
							+ lFascicoliSigeUff.size());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(
					"Num.ro Fascicoli Sige associati al Soggetto non modificabili -> "
							+ lFascicoliSigeAltroUff.size());

			// Si passano nella request le liste dei Fascicoli
			setRequestAttribute("fascicoli", lFascicoliSige);
			setRequestAttribute("fascicoliUfficio", lFascicoliSigeUff);
			setRequestAttribute("fascicoliAltriUffici", lFascicoliSigeAltroUff);
		}

		return ICostantiFasSigeSentenza.PG_LOAD_MODIFICASENTENZA; // restituisce la jsp di VIEW
	}

}
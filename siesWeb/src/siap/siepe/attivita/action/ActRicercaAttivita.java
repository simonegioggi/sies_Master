package siap.siepe.attivita.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
//import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.attivita.controller.IAttivita;
//import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.action.ActRicercaFasSiepePuntuale;
import siap.siepe.fascicolo.action.ICostantiFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActRicercaAttivita
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Richiesta
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
public class ActRicercaAttivita extends ActRicercaFasSiepePuntuale implements ICostantiAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		AttivitaModel lAttMod = new AttivitaModel(); // Istanzia il Model per i criteri di ricerca

		this.setLinkRitorno(); // Ritorna dal chiamante

		if (this.isRequestParameterNullObj(ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE)) {
			// La chiamata proviene dalla voce di menù
			super.processRequest();
			lAttMod.setFasSieIdFasSiepe(
					((FascicoloSiepeModel) getRequestAttribute("fascicolosiepe")).getIdFascicoloSiepe());
		} else {
			// Imposta l'id del fascicolo SIEPE per la ricerca
			lAttMod.setFasSieIdFasSiepe(
					getRequestBigDecimalParameter(ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE));
		}

		// Chiama il controller di richiesta
		IAttivita lCtrl = SIEPELookupRemote.getAttivitaRemote();
		Vector lVect = lCtrl.ExRicercaAttivita(lAttMod);

		setRequestAttribute("FlagFasSiepe", "SI"); // Imposta il flag per abilitare la visualizzazione dei
													// dati di sintesi SIEPE
		setRequestAttribute("attivita", lVect);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_RICERCAATTIVITA;
	}

}
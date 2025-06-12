package siap.siepe.attivita.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
// Import per le combo
import f3b.web.html.Option;
//import siap.sico.web.ActionSiap;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.action.ActRicercaFasSiepePuntuale;
import siap.siepe.fascicolo.action.ICostantiFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * ActLoadInserisciRichiesta - Classe Action per la load inserisci di FasSiepePuntuale Attivita
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciFSPAttivita extends ActRicercaFasSiepePuntuale implements ICostantiAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// STUB : 2006-09-25 ( pensare dove è meglio inserire il controllo )
		// if (this.isRequestParameterNullObj("ritorno"))
		if (!this.isRequestParameterNullObj(ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Si è chiamata la classe dalla voce di menù!");
			// Invoca la process Request della superclasse se si proviene dal menu'.
			super.processRequest();
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				this.getClass().getName() + "." + this.getClass().getName() + ".processRequest(): inizio");
		// Il Fascicolo è in sessione
		FascicoloSiepeEstesoModel lFasEsteso = (FascicoloSiepeEstesoModel) this
				.getSessionAttribute("FascicoloSiepeEsteso");
		FascicoloSiepeModel lFascicolo = lFasEsteso.getFascicoloSiepe();

		// Ricerca Attività già assegnate al fascicolo
		IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote();
		AttivitaModel lAttivitaRicerca = new AttivitaModel();
		lAttivitaRicerca.setFasSieIdFasSiepe(lFascicolo.getIdFascicoloSiepe());
		Collection lElencoAttivita = lAttCtrl.ExRicercaAttivita(lAttivitaRicerca);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro attività già assegnate ->" + lElencoAttivita.size());

		// Preparazione della Combo Attività
		IDecodifiche lDecoCtrl = SICOLookupRemote.getDecodificheRemote();
		Collection lListaAttivita = lDecoCtrl.ExRicercaAttivitaByIncarico(lFascicolo.getCodIncarico());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro attività complessive ->" + lListaAttivita.size());

		lListaAttivita = EliminaAttivitaAssegnate(lListaAttivita, lElencoAttivita);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro attività ancora da assegnare ->" + lListaAttivita.size());

		if (lListaAttivita.size() < 1)
			throw new SIEPEException(F3BException.USER_MESSAGE, "Non esistono nuove attività da assegnare!");

		this.setLinkRitorno();
		Option lOption = new Option(lListaAttivita);
		setRequestAttribute("ListaAttivita", "" + lOption);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("FlagFasSiepe", "SI");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				this.getClass().getName() + "." + this.getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_INSERISCIATTIVITA; // restituisce la jsp di VIEW
	}

	private Collection EliminaAttivitaAssegnate(Collection aElencoAttivita, Collection aElencoAssegnate) {

		// Iteratore sull'elenco delle attività già assegnate
		Iterator itxAssegnate = aElencoAssegnate.iterator();

		while (itxAssegnate.hasNext()) {
			AttivitaModel lAtt = (AttivitaModel) itxAssegnate.next();

			// codice attività da eliminare
			String lCodAttivita = lAtt.getCodTipoAttivita();

			// Iteratore sull'elenco completo delle attività
			Iterator itxAttivita = aElencoAttivita.iterator();
			while (itxAttivita.hasNext()) {
				DecodificheModel ldecodeModel = (DecodificheModel) itxAttivita.next();

				if ((ldecodeModel.getCode()).equals(lCodAttivita)) {
					aElencoAttivita.remove(ldecodeModel);
					break;
				}
			}
		}

		// valore di ritorno
		return aElencoAttivita;
	}

}
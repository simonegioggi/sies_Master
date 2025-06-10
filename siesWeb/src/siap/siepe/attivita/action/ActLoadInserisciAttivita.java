package siap.siepe.attivita.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * ActLoadInserisciAttivita - Classe Action per la load inserisci di Attivita
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciAttivita extends ActionSiap implements ICostantiAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				this.getClass().getName() + "." + this.getClass().getName() + ".processRequest(): inizio");
		// Il Fascicolo è in sessione
		FascicoloSiepeEstesoModel lFasEsteso = (FascicoloSiepeEstesoModel) this
				.getSessionAttribute("FascicoloSiepeEsteso");
		FascicoloSiepeModel lFascicolo = lFasEsteso.getFascicoloSiepe();

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "FASCICOLO_SIEPE",
				lFascicolo.getIdFascicoloSiepe().toString(), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L' " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

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

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCIATTIVITA;
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
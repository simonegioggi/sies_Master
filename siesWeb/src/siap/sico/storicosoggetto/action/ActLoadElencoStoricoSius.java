package siap.sico.storicosoggetto.action;

/**
 * <p>Title: ActLoadElencoStorico</p>
 * <p>Description: Azione Load del Dettaglio del Soggetto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.storicosoggetto.controller.IStoricoSoggetto;
import siap.sico.storicosoggetto.model.SoggettoStoricoSoggettoModel;
import siap.sico.storicosoggetto.model.StoricoSoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadElencoStoricoSius extends ActionSiap implements ICostantiSoggetto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di caricamento del Dettaglio del Soggetto
	 * <p>
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Session ID : " + getSessionId());

		SoggettoModel lSoggettoModel = new SoggettoModel();
		SoggettoModel lSoggettoModelNuovo = null;

		Vector lStoriciSogg = null;
		Vector lStoriciSoggNuovi = null;
		// SoggettoStoricoSoggettoModel lSogStorico = null;
		// SoggettoStoricoSoggettoModel lSogStoricoNew = null;
		StoricoSoggettoModel lStoSoggMod = null;
		Vector lModFascNuovi = null;
		lSoggettoModel.setIdSoggetto(((SoggettoModel) getSessionAttribute("soggetto")).getIdSoggetto());

		// Chiama il controller.
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();

		IFascicoloSius lFascCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloSiusModel lFacRic = new FascicoloSiusModel();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("----->" + lSoggettoModel.getIdSoggetto());
		// RICERCA SOGGETTO IN SESSIONE
		SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lSoggettoModel.getIdSoggetto());
		this.setRequestAttribute("soggettoSessione", lSoggetto);

		IStoricoSoggetto lCtrlStorico = SICOLookupRemote.getStoricoSoggettoRemote();
		// RICERCA GLI STORICI DEL SOGGETTO IN SESSIONE IN STORICO SOGGETTO PER ID_VARIATO

		lStoriciSogg = lCtrlStorico.ExRicercaStoricoSoggettoSiusByIdSogVariato(lSoggetto.getIdSoggetto());
		SoggettoStoricoSoggettoModel storiciSoggetto = (SoggettoStoricoSoggettoModel) lStoriciSogg.get(0);
		List lModStor = storiciSoggetto.getStoricoSoggetto();

		List lFascicoli = storiciSoggetto.getFascicoloSius();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFascicoli." + lFascicoli.size());
		Vector FascicoliUfficio = new Vector();
		Vector FascicoliAltroUfficio = new Vector();
		FascicoloSiusModel FascModVerifica = null;
		FascicoloGPModel FascSiusGP = null;
		for (int i = 0; i < lFascicoli.size(); i++) {
			FascSiusGP = (FascicoloGPModel) lFascicoli.get(i);

			FascModVerifica = (FascicoloSiusModel) FascSiusGP.getFascicoloSiusModel();
			if (FascModVerifica.getChiaveUfficio().equals(this.getCodUfficioUtenteConnesso())) {
				FascicoliUfficio.add(FascSiusGP);
			} else {
				FascicoliAltroUfficio.add(FascSiusGP);
			}

		}
		this.setRequestAttribute("fascicoliUfficio", FascicoliUfficio);
		this.setRequestAttribute("fascicoliAltriUffici", FascicoliAltroUfficio);

		this.setRequestAttribute("storici", lStoriciSogg);

		if (lModStor != null && lModStor.size() > 0) {
			for (int i = 0; i < lModStor.size(); i++) {
				if (lModStor != null && lModStor.size() > 0) {
					lStoSoggMod = (StoricoSoggettoModel) lModStor.get(i);

				}
				// RICERCA GLI STORICI DEL SOGGETTO IN SESSIONE IN STORICO SOGGETTO PER ID_NUOVO DEL
				// ID_VARIATO
				if (lStoSoggMod != null) {
					lFacRic.setSogIdSoggetto(lStoSoggMod.getIdSoggettoNuovo());
				} else {
					lFacRic.setSogIdSoggetto(lSoggetto.getIdSoggetto());

				}
				// lFacRic.setChiaveUfficio(this.getCodUfficioUtenteConnesso());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("STORICO----->" + lStoSoggMod);
				// FascicoloSiusModel lFascNuovi = null;
				if (storiciSoggetto != null && lStoSoggMod != null
						&& lStoSoggMod.getIdSoggettoNuovo() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("soggetto nuovo---->" + lStoSoggMod.getIdSoggettoNuovo());
					lStoriciSoggNuovi = lCtrlStorico
							.ExRicercaStoricoSoggettoByIdSogNuovo(lStoSoggMod.getIdSoggettoNuovo());

					lSoggettoModelNuovo = lSogCtrl.ExRicercaSoggettoByKey(lStoSoggMod.getIdSoggettoNuovo());
					Vector FascicoliNew = new Vector();

					// ricerca fascicoli associati al nuovo soggetto
					try {
						lModFascNuovi = lFascCtrl
								.ExRicercaFascicoloSiusBySoggettoForStorico(lSoggettoModelNuovo);
						for (int y = 0; y < lModFascNuovi.size(); y++) {
							FascSiusGP = (FascicoloGPModel) lModFascNuovi.get(y);

							// lFascNuovi = (FascicoloSiusModel) FascSiusGP.getFascicoloSiusModel();
							FascicoliNew.add(FascSiusGP);
						}
					} catch (Exception e) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Nessun Elemento trovato");
					}
					// if (lStoriciSoggNuovi.size() > 0) {
					// for (int y = 0; y < lStoriciSoggNuovi.size(); y++) {
					// lSogStoricoNew = (SoggettoStoricoSoggettoModel) lStoriciSoggNuovi.get(y);
					// }
					// }
					this.setRequestAttribute("fascicoliNuovi", FascicoliNew);
				}
			}
		}
		this.setRequestAttribute("storiciNuovi", lStoriciSoggNuovi);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("soggettoVariato-------------->" + lSoggettoModelNuovo);
		this.setRequestAttribute("soggettoVariato", lSoggettoModelNuovo);

		return PG_LOAD_ELENCO_STORICI_SOGGETTO_SIUS;
	}

}
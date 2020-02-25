package siap.sico.storicosoggetto.action;

import java.util.List;
import java.util.Vector;

import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.storicosoggetto.controller.IStoricoSoggetto;
import siap.sico.storicosoggetto.model.SoggettoStoricoSoggettoModel;
import siap.sico.storicosoggetto.model.StoricoSoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.security.model.ProfileModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActLoadElencoStorico
 * </p>
 * <p>
 * Description: Azione Load del Dettaglio del Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadElencoStorico extends ActionSiap implements ICostantiSoggetto {

	/**
	 * Azione di caricamento del Dettaglio del Soggetto
	 * <p>
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		SoggettoModel lSoggettoModel = new SoggettoModel();
		SoggettoModel lSoggettoModelNuovo = null;

		Vector lStoriciSogg = null;
		Vector lStoriciSoggNuovi = null;
//		SoggettoStoricoSoggettoModel lSogStorico = null;
//		SoggettoStoricoSoggettoModel lSogStoricoNew = null;
		StoricoSoggettoModel lStoSoggMod = null;
		Vector lModFascNuovi = null;
		lSoggettoModel.setIdSoggetto(((SoggettoModel) getSessionAttribute("soggetto")).getIdSoggetto());

		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute("UtenteConnesso");

		ProfileModel lProfilo = (ProfileModel) lUtenteMod.getUserProfile();

		String lPage = "";
		this.setRequestAttribute("profilo", lProfilo.getProfileId());

//		String profilo = lProfilo.getProfileId().toString();
		if (lProfilo.isSius()) // profilo sius
		{
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.storicosoggetto.action.ActLoadElencoStoricoSius";

			/*
			 * RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
			 * lRedirigi.setAction("siap.sico.storicosoggetto.action.ActLoadElencoStoricoSius");
			 * setRequestAttribute(IWebConstants.GOTO_PAGE, lRedirigi);
			 */
		} else {

			// Chiama il controller.
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();

			IFascicoloSiep lFascCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFacRic = new FascicoloSiepModel();

			// RICERCA SOGGETTO IN SESSIONE
			SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lSoggettoModel.getIdSoggetto());
			this.setRequestAttribute("soggettoSessione", lSoggetto);

			IStoricoSoggetto lCtrlStorico = SICOLookupRemote.getStoricoSoggettoRemote();
			// RICERCA GLI STORICI DEL SOGGETTO IN SESSIONE IN STORICO SOGGETTO PER ID_VARIATO

			lStoriciSogg = lCtrlStorico.ExRicercaStoricoSoggettoByIdSogVariato(lSoggetto.getIdSoggetto());
			SoggettoStoricoSoggettoModel storiciSoggetto = (SoggettoStoricoSoggettoModel) lStoriciSogg.get(0);
			List lModStor = storiciSoggetto.getStoricoSoggetto();

			List lFascicoli = storiciSoggetto.getFascicoloSiep();
			Vector FascicoliUfficio = new Vector();
			Vector FascicoliAltroUfficio = new Vector();
			FascicoloSiepModel FascModVerifica = null;

			for (int i = 0; i < lFascicoli.size(); i++) {
				FascModVerifica = (FascicoloSiepModel) lFascicoli.get(i);
				if (FascModVerifica.getChiaveUfficio().equals(this.getCodUfficioUtenteConnesso())) {
					FascicoliUfficio.add(FascModVerifica);
				} else {
					FascicoliAltroUfficio.add(FascModVerifica);
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
					if (storiciSoggetto != null && lStoSoggMod != null
							&& lStoSoggMod.getIdSoggettoNuovo() != null) {
						lStoriciSoggNuovi = lCtrlStorico.ExRicercaStoricoSoggettoByIdSogNuovo(lStoSoggMod
								.getIdSoggettoNuovo());

						lSoggettoModelNuovo = lSogCtrl.ExRicercaSoggettoByKey(lStoSoggMod
								.getIdSoggettoNuovo());

						// ricerca fascicoli associati al nuovo soggetto
						try {
							lModFascNuovi = lFascCtrl.ExRicercaFascicoloSiepSoggetto(lFacRic);
						} catch (Exception e) {
							// Nessun Elemento trovato
						}
						if (lStoriciSoggNuovi.size() > 0) {
							for (int y = 0; y < lStoriciSoggNuovi.size(); y++) {
								/*lSogStoricoNew = (SoggettoStoricoSoggettoModel)*/ lStoriciSoggNuovi.get(y);

							}
						}
						this.setRequestAttribute("fascicoliNuovi", lModFascNuovi);
					}
				}
			}
			this.setRequestAttribute("storiciNuovi", lStoriciSoggNuovi);

			this.setRequestAttribute("soggettoVariato", lSoggettoModelNuovo);
			lPage = PG_LOAD_ELENCO_STORICI_SOGGETTO;

		}
		return lPage;
	}

}
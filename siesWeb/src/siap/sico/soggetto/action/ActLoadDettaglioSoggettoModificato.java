package siap.sico.soggetto.action;

/**
 * <p>Title: ActLoadDettaglioSoggetto</p>
 * <p>Description: Azione Load del Dettaglio del Soggetto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadDettaglioSoggettoModificato extends ActionSiap implements ICostantiSoggetto {

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

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO);

		// Chiama il controller.
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lId);
		setRequestAttribute("soggetto", lSoggetto);
		setSessionAttribute("soggetto", lSoggetto);

		// verifica se il profilo è sige o sius o siep
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute("UtenteConnesso");
		ProfileModel lProfilo = (ProfileModel) lUtenteMod.getUserProfile();

		// String lPage = "";
		setRequestAttribute("profilo", lProfilo.getProfileId());

		FascicoloSiepModel lFascMod = new FascicoloSiepModel();
		if (lProfilo.isSige()) {
			// Viene istanziato il controller per la ricerca dei Fascicoli SIGE
			IFascicoloSige lFasSigeCtrl = SIGELookupRemote.getFascicoloSigeRemote();
			// Si effettua la ricerca dei Fascicolo SIGE associati al Soggetto modificato
			FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
			lFascicolo.setSogIdSoggetto(lSoggetto.getIdSoggetto());
			Vector lFascicoliSigexSoggetto = lFasSigeCtrl.ExRicercaFascicoloSige(lFascicolo);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Num.ro Fascicoli Sige associati al Soggetto da modificare -> "
					+ lFascicoliSigexSoggetto.size());
			// Si passano nella request le liste dei Fascicoli
			setRequestAttribute("fascicoliUfficio", lFascicoliSigexSoggetto);

		} else if (lProfilo.isSius()) {
			// UTENTE SIUS

			IFascicoloSius lFascSogSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			Vector lFascicoliSiusSoggetti = new Vector();
			try {
				lFascicoliSiusSoggetti = lFascSogSiusCtrl
						.ExRicercaFascicoloSiusBySoggettoForStorico(lSoggetto);
			} catch (Exception sEx) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Fascicolo vuoto");
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("----------->NUMERO FASCICOLI sius -----> " + lFascicoliSiusSoggetti.size());
			FascicoloGPModel FascSiusGP = null;
			FascicoloSiusModel FascSiusModVerifica = null;
			Vector FascicoliSiusUfficio = new Vector();
			// Vector FascicoliSiusAltroUfficio = new Vector();

			if (lFascicoliSiusSoggetti.size() > 0) {
				for (int i = 0; i < lFascicoliSiusSoggetti.size(); i++) {
					FascSiusGP = (FascicoloGPModel) lFascicoliSiusSoggetti.get(i);
					FascSiusModVerifica = (FascicoloSiusModel) FascSiusGP.getFascicoloSiusModel();
					if (FascSiusModVerifica.getChiaveUfficio().equals(this.getCodUfficioUtenteConnesso())) {
						FascicoliSiusUfficio.add(FascSiusGP);
					}

				}

				setRequestAttribute("fascicoli", FascicoliSiusUfficio);

			}
		} else // profilo SIEP
		{
			lFascMod.setSogIdSoggetto(lSoggetto.getIdSoggetto());
			lFascMod.setChiaveUfficio(getCodUfficioUtenteConnesso());

			IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			// String uffcio = this.getCodUfficioUtenteConnesso();
			Vector lFascicoliSoggettiAppo = new Vector();
			Vector lFascicoliSoggetti = new Vector();
			try {
				lFascicoliSoggettiAppo = lFascSogCtrl.ExRicercaFascicoloSiepSoggetto(lFascMod);
				// Paolo Cherubini 12 dicembre 2011
				// aggiungo il ciclo sui fascicolo per scartare se presente un cumulo
				for (int i = 0; i < lFascicoliSoggettiAppo.size(); i++) {
					// Vector FascicoliUfficio = new Vector();
					FascicoloSiepModel FascModVerifica = (FascicoloSiepModel) lFascicoliSoggettiAppo.get(i);
					int NumFasc = FascModVerifica.getChiaveProgr().intValue();
					if (!(NumFasc > 700000 && NumFasc < 800000)) {
						lFascicoliSoggetti.add(FascModVerifica);
						break;
					}
				}

			} catch (SIEPException sEx) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Fascicolo vuoto");
			}

			setRequestAttribute("fascicoli", lFascicoliSoggetti);
		} // end if profilo

		// Inserisce il model soggetto in sessione
		// setSessionAttribute("soggetto", lSoggetto);

		if (!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		if (!this.isRequestParameterNullObj("NomeAzione")) {
			this.setRequestAttribute("NomeAzione", this.getRequestStringParameter("NomeAzione"));
		}

		gestioneRitorno();

		return PG_LOAD_DETTAGLIOSOGGETTO_MODIFICATO;

	}

}
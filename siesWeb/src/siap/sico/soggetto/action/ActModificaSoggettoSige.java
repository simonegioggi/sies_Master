package siap.sico.soggetto.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.richiesta.controller.IRichiestaSige;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActModificaSoggettoSige
 * </p>
 * <p>
 * Description: Classe Action per la modifica di Soggetto da profilo SIGE
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
public class ActModificaSoggettoSige extends ActModificaSoggetto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		return super.processRequest();
	}

	/**
	 * Funzione ridefinita per personalizzarla alla gestione dei Fascicoli SIGE associati al Soggetto da
	 * modificare.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected String modificaSoggetto() throws F3BException {

		String lPage = "";
		// Elenco dei Fascicoli Sige associati al Soggetto
		Vector lFascicoliSigexSoggetto = new Vector();
		// Elenco dei Fascicoli Sige associati al Soggetto ed allo stesso Ufficio dell'operatore
		Vector lFascicoliSigexSogUff = new Vector();
		// Elenco dei Fascicoli Sige associati al Soggetto e ad Uffici diversi da quello dell'operatore
		Vector lFascicoliSigexSogAltroUff = new Vector();
		// Flag che indica l'avvenuto aggiornamento del Soggetto
		boolean lAggiornamento = false;

		// Determinazione profilo dell'operatore
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute("UtenteConnesso");
		ProfileModel lProfilo = (ProfileModel) lUtenteMod.getUserProfile();
		setRequestAttribute("profilo", lProfilo.getProfileId());
		String profilo = lProfilo.getProfileId().toString();

		// Viene istanziato il controller per la ricerca dei Fascicoli SIGE
		IFascicoloSige lFasSigeCtrl = SIGELookupRemote.getFascicoloSigeRemote();

		// Si effettua la ricerca dei Fascicolo SIGE associati al Soggetto da modificare
		FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
		lFascicolo.setSogIdSoggetto(lSogMod.getIdSoggetto());
		lFascicoliSigexSoggetto = lFasSigeCtrl.ExRicercaFascicoloSige(lFascicolo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Num.ro Fascicoli Sige associati al Soggetto da modificare -> "
				+ lFascicoliSigexSoggetto.size());

		// Viene istanziato il controller per la ricerca delle Richieste associate ai Fascicoli SIGE
		IRichiestaSige lRicSigeCtrl = SIGELookupRemote.getRichiestaSigeRemote();

		if (lFascicoliSigexSoggetto.size() > 0) {
			// Si discriminano Fascicoli modificabili da quelli non modificabili
			for (int i = 0; i < lFascicoliSigexSoggetto.size(); i++) {
				FascicoloSigeModel lFascModVerifica = (FascicoloSigeModel) lFascicoliSigexSoggetto.get(i);

				// Fascicoli modificabili se appartenenti all?Ufficio dell'operatore e se privi di Fascicolo
				// SIEP
				if (lFascModVerifica.getChiaveUfficio().equals(getCodUfficioUtenteConnesso())
						&& lFascModVerifica.getCodStatoFascicolo().equalsIgnoreCase("02")
						&& lRicSigeCtrl.ExRicercaRichiestaSigeByKey(lFascModVerifica.getRicIdRichiestaSige())
								.getFasSieIdFascicoloSiep() == null) {
					lFascicoliSigexSogUff.add(lFascModVerifica);
				} else {
					lFascicoliSigexSogAltroUff.add(lFascModVerifica);
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("Num.ro Fascicoli Sige associati al Soggetto dell'Ufficio di competenza modificabili -> "
							+ lFascicoliSigexSogUff.size());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Num.ro Fascicoli Sige associati al Soggetto non modificabili -> "
					+ lFascicoliSigexSogAltroUff.size());

			// Si passano nella request le liste dei Fascicoli
			setRequestAttribute("fascicoli", lFascicoliSigexSoggetto);
			setRequestAttribute("fascicoliUfficio", lFascicoliSigexSogUff);
			setRequestAttribute("fascicoliAltriUffici", lFascicoliSigexSogAltroUff);

			// Caso in cui il soggetto abbia piu' di un fascicolo l'utente deve associare la
			// Modifica a uno o piu' fascicoli
			if (lFascicoliSigexSogUff.size() > 1
					|| (lFascicoliSigexSogUff.size() == 1 && lFascicoliSigexSogAltroUff.size() > 0)) {
				setRequestAttribute("soggettonuovo", lSogMod);
				setRequestAttribute("soggettovecchio", lSogVec);
				setRequestAttribute("chiaveUfficio", getCodUfficioUtenteConnesso());
				setRequestAttribute("contaUffici", "" + lFascicoliSigexSogAltroUff.size());
				return IWebConstants.ROOT_DIR
						+ "files/siap/sico/storicosoggetto/LoadModificaSoggettoPerFascicolo.jsp";
			}

		} // endif lFascicoliSigeSoggetti.size()

		// Il soggetto ha uno o nessun fascicolo
		SoggettoModel lSogRet = new SoggettoModel();

		if ((lFascicoliSigexSoggetto.size() == 0 || lFascicoliSigexSogUff.size() == 1)
				&& (lFascicoliSigexSogAltroUff.size() == 0)) { // Se non ho fascicoli oppure ho solo uno del
																// mio ufficio faccio l'update:
			BigDecimal lKeyFascicoloUnico = null;

			/*
			 * Non si passa l'ID del Fascicolo SIGE !! if (lFascicoliSigexSogUff.size() == 1)
			 * lKeyFascicoloUnico = ((FascicoloSigeModel)
			 * lFascicoliSigexSoggetto.firstElement()).getIdFascicoloSige();
			 */
			// aggiornamento Storico
			lSogRet = lSogCtrl.ExModificaSoggettoStorico(lSogMod, profilo, lSogVec, lKeyFascicoloUnico);

			lSogRet.setMessage("Aggiornamento");
			lAggiornamento = true;
		}

		if (lFascicoliSigexSogUff.size() == 0 && lFascicoliSigexSogAltroUff.size() > 0) {
			// SE L'utente ha SOLO FASCICOLI ALTRI UFFICI DUPLICO
			lSogMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lSogMod.setDataInserimento(DateUtils.getSysDate());
			lSogMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lSogMod.setFlagPresenzaFascicolo("N");
			lSogRet = lSogCtrl.ExInserisciSoggetto(lSogMod);
			lSogRet.setMessage("Aggiornamento");
			lAggiornamento = true;
		}

		// Controllo se lSogRet nullo
		// if (lSogRet == null)
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// // LogF3B.getLogger()
		// siesLogger.debug("colpa di lSogRet");
		// else {
		// setta la risposta nella request
		setRequestAttribute("soggetto", lSogRet);
		if (lSogRet.getMessage() == null) {
			lSogRet.setMessage("Problemi durante la modifica del soggetto");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("colpa di getMessage");
		}
		// }

		if (lAggiornamento) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&" + CAMPO_ID_SOGGETTO + "="
					+ lSogRet.getIdSoggetto().toString();
		} else {
			lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, lSogRet.getMessage());
			// Prepara la "pagina" di destinazione
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.sico.soggetto.action.ActLoadDettaglioSoggetto");
			lRedirigi.setParameter(CAMPO_ID_SOGGETTO, lSogRet.getIdSoggetto().toString());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return lPage;
	}

}
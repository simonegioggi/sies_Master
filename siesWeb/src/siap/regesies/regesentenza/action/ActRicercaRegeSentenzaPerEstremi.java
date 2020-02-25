package siap.regesies.regesentenza.action;

import java.util.Vector;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesentenza.controller.IRegeSentenza;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.regesoggetto.controller.IRegeSoggetto;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaRegeSentenzaPerEstremi
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di RegeSentenza per Estremi
 * </p>
 */
public class ActRicercaRegeSentenzaPerEstremi extends ActionRegeSiap implements ICostantiRegeSentenza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		RegeSentenzaModel lRegMod = new RegeSentenzaModel();
		lRegMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String lComune = "";
		lComune = lUtenteConnesso.getUfficioUtente().getDescrComune();

		if (this.isUfficioSecondoGrado()) {
			// Controllo l'esistenza del comune
			this.getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)).getCodComune();
			lRegMod.setCodLuogoEmittente(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE).toUpperCase());
			// Controllo l'esistenza dell'ufficio
			this.getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
					getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));
		} else {
			lRegMod.setCodLuogoEmittente(lComune);
			this.getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE), lUtenteConnesso
							.getUfficioUtente().getDescrComune());
		}

		lRegMod.setAnnoSentenza(getRequestIntParameter(CAMPO_ANNO_SENTENZA));
		lRegMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));

		// Ricerca il Provvedimento
		IRegeSentenza lCtrl = RegeSiesLookupRemote.getRegeSentenzaRemote();
		ProvvedimentoModel lProvv = lCtrl.ExRicercaRegeSentenzaPerEstremi(lRegMod);

		String lPage = "";

		if (lProvv.getRegeSentenza().getCountSoggetti() == 1) {
			lPage = PG_LOAD_DETTAGLIO_PROVVEDIMENTO;
			setSessionAttribute("provvedimentoRege", lProvv);
			// Ricerca soggetti per lo stesso provvedimento SIEP
			setRequestAttribute("provvedimento", lProvv);
		} else {
			IRegeSoggetto lCtrlSogg = RegeSiesLookupRemote.getRegeSoggettoRemote();
			Vector lVect = lCtrlSogg.ExRicercaRegeSoggettoPerProvvedimento(lProvv.getRegeSentenza());
			if (lVect != null && lVect.size() != 0) {
				setRequestAttribute("provvedimento", lProvv.getRegeSentenza());
				// setSessionAttribute("provvedimentoRege", lProvv.getRegeSentenza());
				setRequestAttribute("soggetti", lVect);

				// Ricerco i Soggetti associati
				lPage = PG_ELENCO_SOGGETTI;
			}
		}
		return lPage;
	}

}
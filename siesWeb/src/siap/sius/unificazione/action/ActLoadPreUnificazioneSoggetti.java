package siap.sius.unificazione.action;

import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.unificazione.controller.IUnificazione;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadPreUnificazioneSoggetti
 * </p>
 * <p>
 * Description: Classe Action per la LoadPreUnificazioneSoggetti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadPreUnificazioneSoggetti extends ActionSiap implements ICostantiUnificazione {

	public String processRequest() throws Exception {

		// Recupero l'utente dalla sessione
		// UtenteModel lUtenteMod =
		// (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// Verifica della Unificabilità dei soggetti per i procedimenti indicati.

		// Se Unificabili i 2 soggetti, vengono letti i relativi procedimenti e passati alla request per
		// presentarli a confronto.
		IUnificazione lCtrl = SIUSLookupRemote.getUnificazioneRemote();
		String aCodUffUnificante = this.getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_UNIFICANTE),
				getRequestStringParameter(ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_UNIFICANTE));
		FascicoloGPModel lFasUnificante = lCtrl.ExVerificaSoggettoUnificante(
				getRequestStringParameter(ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE),
				getRequestStringParameter(ICostantiUnificazione.CAMPO_NUMERO_UNIFICANTE), aCodUffUnificante);
		String aCodUffDaUnif = this.getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_DA_UNIF),
				getRequestStringParameter(ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_DA_UNIF));
		// 20/04/2007 Modifica per l'UNIFICAZIONE SOGGETTI: aggiunto il parametro ID_SOGGETTO_UNIFICANTE
		// FascicoloGPModel lFasDaUnificare = lCtrl.ExVerificaSoggettoDaUnificare(getRequestStringParameter(
		// ICostantiUnificazione.CAMPO_ANNO_DA_UNIF), getRequestStringParameter(
		// ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF), aCodUffDaUnif );
		FascicoloGPModel lFasDaUnificare = lCtrl.ExVerificaSoggettoDaUnificare(
				getRequestStringParameter(ICostantiUnificazione.CAMPO_ANNO_DA_UNIF),
				getRequestStringParameter(ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF), aCodUffDaUnif,
				lFasUnificante.getFascicoloSiusModel().getSogIdSoggetto());

		// Se i 2 Procedimenti sono riferiti allo stesso soggetto, l'unificazione non ha senso.
		if (lFasUnificante.getFascicoloSiusModel().getSogIdSoggetto()
				.compareTo(lFasDaUnificare.getFascicoloSiusModel().getSogIdSoggetto()) == 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! I due Procedimenti selezionati fanno già riferimento allo stesso soggetto!");

		// Se il fascicolo da Unificare fa riferimento a titolo esecutivo, l'unificazione non è consentita.
		if (lFasDaUnificare.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! Il Procedimento da Unificare fà riferimento a Titolo Esecutivo: richiedere l'unificazione alla Procura Competente!");

		// Presentazione della FORM di inserimento Data di Unificazione.
		setRequestAttribute("fasDaUnificare", lFasDaUnificare);
		setRequestAttribute("fasUnificante", lFasUnificante);

		// Si pone il Procedimento Unificante in sessione.
		setSessionAttribute("fascicoloSiusGP", lFasUnificante);

		return PG_LOAD_INS_UNIFICAZIONESOGGETTI; // restituisce la jsp di VIEW
	}

}
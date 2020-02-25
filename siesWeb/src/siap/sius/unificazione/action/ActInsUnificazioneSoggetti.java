package siap.sius.unificazione.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.unificazione.controller.IUnificazione;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInsUnificazioneSoggetti
 * </p>
 * <p>
 * Description: Classe Action di inserimento Unificazione di due Soggetti riferiti da 2 Procedimenti SIUS.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull S.p.A.
 * </p>
 */
public class ActInsUnificazioneSoggetti extends ActionSiap implements ICostantiUnificazione {

	public String processRequest() throws Exception {

		super.processRequest();

		// Recupero dell'utente e del Fascicolo SIUS Unificante dalla sessione.
		// UtenteModel lUtenteMod =
		// (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		FascicoloGPModel lFasGPUnificante = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Recupero degli identificativi dei fascicoli relativi ai soggetti da unificare.
		// String idFascicoloUnificante = getRequestStringParameter( CAMPO_ID_FASCICOLO_UNIFICANTE);
		String idFascicoloDaUnificare = getRequestStringParameter(CAMPO_ID_FASCICOLO_UNIFICATO);

		// Rilettura del Fascicolo Sius Da Unificare.
		IFascicoloSius lCtrlFS = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel lFasGPDaUnificare = lCtrlFS
				.ExRicercaFascicoloByKey(new BigDecimal(idFascicoloDaUnificare));

		// Recupero degli identificativi dei soggetti da unificare.
		// String idSoggettoUnificante = getRequestStringParameter( CAMPO_ID_SOGGETTO_UNIFICANTE);
		// String idSoggettoDaUnificare = getRequestStringParameter( CAMPO_ID_SOGGETTO_UNIFICATO);

		// Valorizzazione dei campi in aggiornamento.
		lFasGPUnificante.getFascicoloSiusModel().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																										// dell'operatore
																										// che
																										// inserisce
		lFasGPUnificante.getFascicoloSiusModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																											// dell'operatore
																											// che
																											// inserisce
		lFasGPUnificante.getFascicoloSiusModel().setDataAggiornamento(DateUtils.getSysDate());

		// Unificazione dei 2 Soggetti.
		IUnificazione lCtrl = SIUSLookupRemote.getUnificazioneRemote();
		// EventoModel lEveUnificazione = lCtrl.ExInserisciUnificazione(annoDaUnif, numeroDaUnif,
		// annoUnificante, numeroUnificante, getCodUfficioUtenteConnesso(), getCodUtenteConnesso(),
		// getCodComuneUtenteConnesso(), dataUnificazione );
		lCtrl.ExInsUnificazioneSoggetti(lFasGPUnificante.getFascicoloSiusModel(),
				lFasGPDaUnificare.getFascicoloSiusModel(), getCodUfficioUtenteConnesso(),
				getCodUtenteConnesso(), getCodComuneUtenteConnesso());

		// restituisce la jsp di VIEW.
		// Nella request viaggia l'ID dell'Evento di Unificazione appena inserito.
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActRicercaProcedimentiSoggettoDaFascicolo";
	}

}
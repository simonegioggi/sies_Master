package siap.sius.generaleprocedimento.action;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.util.SIUSLookupRemote;

public class ActModificaNoteProcedimento extends ActionSiap implements ICostantiFascicoloSius {

	/**
	 * Azione di Modifica delle NoteProcedimento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// Recupero l'utente e il Fascicolo SIUS dalla sessione
		// UtenteModel lUtenteMod =
		// (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// Istanzio il Model e lo carico con quello posto in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Caricamento Generale Procedimento (solo campo Note)
		lFasGPMod.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																										// dell'operatore
																										// che
																										// inserisce
		lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																											// dell'operatore
																											// che
																											// inserisce
		lFasGPMod.getGeneraleProcedimentoModel().setDataAggiornamento(DateUtils.getSysDate());

		lFasGPMod.getGeneraleProcedimentoModel().setAnnotazione(getRequestStringParameter(CAMPO_NOTE));

		// chiama il controller
		IGeneraleProcedimento lCtrl = SIUSLookupRemote.getGeneraleProcedimentoRemote();
		lCtrl.ExModificaNoteProcedimento(lFasGPMod.getGeneraleProcedimentoModel());

		// restituisce la jsp di VIEW
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
	}

}
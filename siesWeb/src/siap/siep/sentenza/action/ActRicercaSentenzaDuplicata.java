package siap.siep.sentenza.action;

import java.math.BigDecimal;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaFascicoliModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActRicercaSentenzaDuplicata extends ActionSiap implements ICostantiSentenza {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		SentenzaModel lSdup = new SentenzaModel();
		lSdup = (SentenzaModel) getRequestAttribute("sentenza_duplicata");

		SentenzaModel lSmod = new SentenzaModel();

		UtenteModel lUtenteMod = this.getUtenteConnesso();

		lSmod.setCodTipoProvvedimento(lSdup.getCodTipoProvvedimento());
		lSmod.setCodTipoAutoritaEmittente(lSdup.getCodTipoAutoritaEmittente());
		lSmod.setCodLuogoEmittente(lSdup.getCodLuogoEmittente());
		lSmod.setAnnoSentenza(lSdup.getAnnoSentenza());
		lSmod.setNumeroSentenza(lSdup.getNumeroSentenza());
		lSmod.setCodTipoProvvRif(lSdup.getCodTipoProvvRif());
		lSmod.setAnnoProvvRif(lSdup.getAnnoProvvRif());
		lSmod.setNumeroProvvRif(lSdup.getNumeroProvvRif());
		lSmod.setCodTipoAutoritaProvvRif(lSdup.getCodTipoAutoritaProvvRif());
		lSmod.setCodLuogoProvvRif(lSdup.getCodLuogoProvvRif());
		lSmod.setNote1DecisioneCassazione(lSdup.getNote1DecisioneCassazione());
		lSmod.setNote2DecisioneCassazione(lSdup.getNote2DecisioneCassazione());
		lSmod.setCodTipoDecisioneCassazione(lSdup.getCodTipoDecisioneCassazione());

		lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();
		// Ticket#202204010111 - si sostituisce la funzione di ricerca
	    // Vector lSentenze = lSCtrl.ExRicercaSentenzaDuplicata(lSmod);
		Vector <SentenzaFascicoliModel> lSentenze = lSCtrl.ExRicercaSentenzaDuplicataFascicoli(lSmod);
		// Ticket#202204010111 - FINE
		
		// imposta la risposta la risposta nella request
		String lReturnPage = "";

		if (lSentenze.size() == 1) {
			// Ticket#202204010111 - si recupera la sentenza dal model strutturato
			/*
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sentenza.action.ActLoadInserisciSentenzaDuplicata&" + CAMPO_ID_SENTENZA
					+ "=" + ((SentenzaModel) lSentenze.get(0)).getIdSentenza().toString();*/
			BigDecimal idSentenza = ((SentenzaFascicoliModel) lSentenze.get(0)).getSentenza().getIdSentenza();
			
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sentenza.action.ActLoadInserisciSentenzaDuplicata&" + CAMPO_ID_SENTENZA
					+ "=" + idSentenza.toString();
			// Ticket#202204010111 - FINE
		} else {
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, "NO");
			setRequestAttribute("sentenze", lSentenze);
			lReturnPage = PG_RICERCASENTENZE;

			String lAzione = "siap.siep.sentenza.action.ActRicercaSentenzaDuplicata";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);
		}

		return lReturnPage; // restituisce la jsp di VIEW
	}

}
package siap.siep.sentenza.action;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiapMinor;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaSentenza
 * </p>
 * <p>
 * Description: Ricerca Titolo Esecutivo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * not attributable 1.0
 */
public class ActRicercaProcedimentoRGNR extends ActionSiapMinor implements ICostantiSentenza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lPagina = "1";
		String flagRicercaData = "N";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		SentenzaModel lSmod = new SentenzaModel();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		if (!isRequestParameterNullObj(CAMPO_NUMERO_RGNR_INIZIALE))
			lSmod.setNumeroRGNRIniziale(getRequestBigDecimalParameter(CAMPO_NUMERO_RGNR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_ANNO_RGNR_INIZIALE))
			lSmod.setAnnoRGNRIniziale(getRequestBigDecimalParameter(CAMPO_ANNO_RGNR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_NUMERO_RGNR_FINALE))
			lSmod.setNumeroRGNRFinale(getRequestBigDecimalParameter(CAMPO_NUMERO_RGNR_FINALE));

		if (!isRequestParameterNullObj(CAMPO_ANNO_RGNR_FINALE))
			lSmod.setAnnoRGNRFinale(getRequestBigDecimalParameter(CAMPO_ANNO_RGNR_FINALE));

		if (lSmod.getAnnoRGNRFinale() == null)
			// Paolo Cherubini 03/03/2011 se non inserisce anno numero finali li forzo io
			lSmod.setAnnoRGNRFinale(lSmod.getAnnoRGNRIniziale());

		if (lSmod.getNumeroRGNRFinale() == null)
			// Paolo Cherubini 03/03/2011 se non inserisce anno numero finali li forzo io
			lSmod.setNumeroRGNRFinale(lSmod.getNumeroRGNRIniziale());

		if (!isRequestParameterNullObj(PARAMETRO_ORDINAMENTO)) // ma non dovrebbe essere mai nullo
			lSmod.setCodOrdinamento(getRequestStringParameter(PARAMETRO_ORDINAMENTO));
		lSmod.setCodOrdinamento("RGNR"); // Paolo Cherubini 03/03/2011 aggiungo ordinamento per RGNR

		// Riempie il model
		lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		String majorOffice = checkMinori();

		IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		Vector lSentenze = lFascSogCtrl.ExRicercaFascicoloSiepByRGNRPaged(lSmod, Integer.parseInt(lPagina),
				majorOffice);

		// imposta la risposta la risposta nella request
		String lReturnPage = "";

		if (lSentenze.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&" + CAMPO_ID_SENTENZA + "="
					+ ((FascicoloSiepModel) lSentenze.get(0)).getSentenza().getIdSentenza().toString(); // Paolo
																										// Cherubini
																										// 03/03/2011
		} else {
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lFascSogCtrl.ExGetCountFascicoloSiepByRGNRPaged(lSmod, majorOffice);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute("flagRicercaData", flagRicercaData);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("sentenze", lSentenze);
			lReturnPage = PG_RICERCAPROCEDIMENTORGNR;

			String lAzione = "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);
		}

		return lReturnPage; // restituisce la jsp di VIEW
	}

}
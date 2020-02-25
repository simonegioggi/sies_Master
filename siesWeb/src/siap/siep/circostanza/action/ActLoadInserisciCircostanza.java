package siap.siep.circostanza.action;

import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciCircostanza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Circostanza
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

public class ActLoadInserisciCircostanza extends ActionSiap implements ICostantiCircostanza {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		boolean proceed = true;
		FascicoloSiepModel lFascMod = new FascicoloSiepModel();

		lFascMod.setIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFascMod = lCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			proceed = false;
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile Aggiungere Circostanze Ai Capi di Imputazione");
		}
		if (lFascMod.getFlagValidato().equalsIgnoreCase("S")) {
			proceed = false;
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il fascicolo è stato validato! Impossibile Aggiungere Circostanze Ai Capi di Imputazione");
		}

		ICircostanza lCtrlCirc = SIEPLookupRemote.getCircostanzaRemote();
		Vector lCirc = null;
		lCirc = lCtrlCirc.ExRicercaCircostanzaByIdFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("circostanze", lCirc);
		if (proceed) {
			Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), "-");
			setRequestAttribute("TipiFontiReato", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
			setRequestAttribute("TipiSottonumerazione", "" + lOption);

			// Campo in SENTENZA
			// SentenzaModel lSentenza = ((FascicoloSiepModel)getSessionAttribute("fascicolo")).getSentenza();
			lOption = new Option(DecodificheManager.getInstance().getBilanciamentoCircostanze());
			setRequestAttribute("BilanciamentoCircostanze", "" + lOption);

			setRequestAttribute("modalita", "I");

			return PG_LOAD_INSERISCICIRCOSTANZA; // restituisce la jsp di VIEW
		} else {
			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
		}
	}
}
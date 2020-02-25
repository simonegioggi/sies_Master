package siap.siep.reato.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciReato
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Reato
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
public class ActLoadInserisciUlterioriReati extends ActionSiap implements ICostantiReato {

	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
			if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ID_REATO)) {
				this.setRequestAttribute("idReato",
						this.getRequestStringParameter(ICostantiReato.CAMPO_ID_REATO));
			}
		}

		boolean proceed = true;
		FascicoloSiepModel lFascMod = new FascicoloSiepModel();

		lFascMod.setIdFascicoloSiep(((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep());
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFascMod = lCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			proceed = false;
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile aggiungere Capi di Imputazione");
		}
		if (lFascMod.getFlagValidato().equalsIgnoreCase("S")) {
			proceed = false;
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il fascicolo è stato validato! Impossibile aggiungere Capi di Imputazione");
		}

		if (proceed) {

			Option lOption = new Option(DecodificheManager.getInstance().getTipoReato());
			setRequestAttribute("TipiReato", "" + lOption);
			lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato());
			setRequestAttribute("TipiFontiReato", "" + lOption);
			lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
			setRequestAttribute("TipiSottonumerazione", "" + lOption);
			lOption = new Option(DecodificheManager.getInstance().getPeriodoConsumazione());
			setRequestAttribute("PeriodoConsumazione", "" + lOption);
			lOption = new Option(DecodificheManager.getInstance().getTipoPenaDetentiva());
			setRequestAttribute("TipiPeneDetentive", "" + lOption);

			// **************************************************************************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
			setRequestAttribute("TipiCommaQualificante", "" + lOption);
			// **************************************************************************************************

			lOption = new Option(DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
			setRequestAttribute("Valute", "" + lOption);

			setRequestAttribute("modalita", "I");

			return PG_LOAD_INSERISCIULTERIORIREATI; // restituisce la jsp di VIEW
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
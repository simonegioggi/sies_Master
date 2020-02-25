package siap.siep.beneficio.action;

import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
//import per le combo
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciBeneficioIndulto
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Beneficio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 3.0
 */

public class ActLoadInserisciBeneficioIndulto extends ActionSiap implements ICostantiBeneficio {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		boolean proceed = true;
		FascicoloSiepModel lFascMod = new FascicoloSiepModel();

		// Cerca il Fascicolo Siep
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

		if (proceed) {
			// ricerca pena complessiva
			IPenaComplessiva lCtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
			PenaComplessivaModel lPenComMod = lCtrlPenCom
					.ExRicercaPenaComplessivaByIdFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("penacomplessiva", lPenComMod);

			// ricerca pena accessoria
			PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
			lPenMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			IPenaAccessoria lCtrlPenAc = SIEPLookupRemote.getPenaAccessoriaRemote();
			Vector lVect = lCtrlPenAc.ExRicercaPenaAccessoriaNoError(lPenMod);
			setRequestAttribute("peneaccessorie", lVect);

			return preparaForm(); // Restituisce la jsp di VIEW
		} else {
			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE; // Restituisce la jsp di VIEW
		}
	}

	protected String preparaForm() {

		Option lOption = new Option(DecodificheManager.getInstance().getTipoBeneficio(), "03");
		setRequestAttribute("tipoBeneficio", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getDPR(), "24");
		setRequestAttribute("listaDPR", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getSottoTipoBeneficioIndulto());
		setRequestAttribute("sottotipobeneficio", "" + lOption);

		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCI_BENEFICIO_INDULTO; // Restituisce la jsp di VIEW

	}

}
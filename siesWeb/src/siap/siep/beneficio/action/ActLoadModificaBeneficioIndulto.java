package siap.siep.beneficio.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
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
 * Title: ActLoadModificaBeneficioIndulto
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di Beneficio Indulto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadModificaBeneficioIndulto extends ActionSiap implements ICostantiBeneficio {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

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

			return preparaDatiForm();
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

	protected String preparaDatiForm() throws F3BException {
		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("beneficio", getRequestStringParameter(CAMPO_ID_BENEFICIO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}
		BeneficioModel lBenMod = new BeneficioModel();
		lBenMod.setIdBeneficio(getRequestBigDecimalParameter(ICostantiBeneficio.CAMPO_ID_BENEFICIO));
		IBeneficio lBCtrl = SIEPLookupRemote.getBeneficioRemote();
		lBenMod = lBCtrl.ExRicercaBeneficioByKey(lBenMod.getIdBeneficio());
		setRequestAttribute("lBeneficio", lBenMod);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoBeneficio(),
				lBenMod.getCodTipoBeneficio());
		setRequestAttribute("tipoBeneficio", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getDPR(), lBenMod.getCodDpr());
		setRequestAttribute("listaDPR", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getSottoTipoBeneficioIndulto(),
				lBenMod.getCodSottotipoBeneficio());
		setRequestAttribute("sottotipobeneficio", "" + lOption);

		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCI_BENEFICIO_INDULTO; // restituisce la jsp di VIEW
	}

}
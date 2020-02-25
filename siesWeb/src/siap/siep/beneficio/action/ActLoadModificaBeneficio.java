package siap.siep.beneficio.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
//import per le combo
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.tipologiaorario.controller.ITipologiaOrario;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadModificaBeneficio
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di Beneficio
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

public class ActLoadModificaBeneficio extends ActionSiap implements ICostantiBeneficio {
	// indica Beneficio di tipo Indulto
	protected boolean isBeneficioIndulto = false;

	public String processRequest() throws F3BException {
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

	@SuppressWarnings("rawtypes")
	protected String preparaDatiForm() throws F3BException {
		BeneficioModel lBenMod = new BeneficioModel();
		lBenMod.setIdBeneficio(getRequestBigDecimalParameter(ICostantiBeneficio.CAMPO_ID_BENEFICIO));
		IBeneficio lBCtrl = SIEPLookupRemote.getBeneficioRemote();
		lBenMod = lBCtrl.ExRicercaBeneficioByKey(lBenMod.getIdBeneficio());

		if (lBenMod != null && ("03".equals(lBenMod.getCodTipoBeneficio())
				|| "04".equals(lBenMod.getCodTipoBeneficio()))) {
			isBeneficioIndulto = true;
			String lPage = "";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.beneficio.action.ActLoadModificaBeneficioIndulto&" + CAMPO_ID_BENEFICIO
					+ "=" + lBenMod.getIdBeneficio();

			return lPage;
		}

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("beneficio", getRequestStringParameter(CAMPO_ID_BENEFICIO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// controllo se è stata inserita una non menzione
		BeneficioModel llBenNMMod = lBCtrl.ExRicercaBeneficioByBenIdBeneficio(lBenMod.getIdBeneficio());
		setRequestAttribute("beneficiononmenzione", llBenNMMod);

		ITipologiaOrario CtrlTip = SIEPLookupRemote.getTipologiaOrarioRemote();
		Vector lTipilogia = CtrlTip.ExRicercaTipologiaOrarioByIdBeneficio(lBenMod.getIdBeneficio());
		setRequestAttribute("tipologiaorario", lTipilogia);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("beneficio", lBenMod);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoSospSubordinata(),
				lBenMod.getCodTipoSospSubordinata());
		setRequestAttribute("sospensioneSubordinata", "" + lOption);

		Option lOptionSotto = new Option(DecodificheManager.getInstance().getSottoTipoBeneficio(),
				lBenMod.getCodSottotipoBeneficio());
		setRequestAttribute("sottotipobeneficio", "" + lOptionSotto);

		return PG_LOAD_INSERISCIBENEFICIO; // restituisce la jsp di VIEW
	}

}
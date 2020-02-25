package siap.siep.penapecuniaria.action;

import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciAnnotazione
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci Annotazione Provvedimento Sanzione Sostitutiva
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

public class ActLoadInserisciAnnotazioneProvvedimento extends ActionSiap implements ICostantiPenaPecuniaria {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();
		this.isEventoNonValidato();

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);
		// setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
			return IWebConstants.PG_MESSAGE;

		// carico Ufficio Competente
		Option lOptionUffCom = new Option(DecodificheManager.getInstance().getTipoAutorita());
		lOptionUffCom.setFilter(new String[] { "-", "36", "99", "57", "37", "98", "38" });
		setRequestAttribute("ufficiocompetente", "" + lOptionUffCom);

		IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		Vector lRichiestaConversioni = new Vector();
		lRicMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		// lRichiestaConversioni=lCtrlRic.ExRicercaRichiestaConversione(lRicMod);
		lRichiestaConversioni = lCtrlRic.ExRicercaRichiesteConversioniValide(lRicMod);

		// Combo Tipo Provvedimento
		Vector<DecodificheModel> lTipoProvvSorv = new Vector<>();
		lTipoProvvSorv.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("02", "Decreto", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));
		Option lOptionTipoProvvSorv = new Option(lTipoProvvSorv);
		lOptionTipoProvvSorv.setSelected("-");
		setRequestAttribute("comboTipoProvvSorv", "" + lOptionTipoProvvSorv);

		// Combo Uffici Emittente
		// Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionUffSIUS.setFilter(new String[] { "UDS", "TDS", "UDSM", "TDSM" });
		setRequestAttribute("comboTipoUfficioSIUS", "" + lOptionUffSIUS);

		// Combo Oggetto Provvedimento
		Option lOptionMotivo = new Option(DecodificheManager.getInstance().getOggettiConversionePP());
		setRequestAttribute("comboOggettoProvv", "" + lOptionMotivo);

		// Combo Esito
		// Option lOptionEsito = new Option(DecodificheManager.getInstance().getEsitiConversionePP());
		Option lOptionEsito = new Option(DecodificheManager.getInstance().getEsitoProvvedimento());
		lOptionEsito.setFilter(new String[] { "0156", "0157", "0158", "0159", "0160", "0148", "0149", "0150",
				"0151", "0152", "0153", "0154", "0155", "0003", "0005", "0002", "0004" });
		setRequestAttribute("comboEsitoProvv", "" + lOptionEsito);

		// 01/12/2015 Ricerca ScambioSanzione preesistente al fine di inibire l'inserimento manuale.
		Vector[] lScaSanTipPro = new Vector[2];
		Vector lScambioSanzioneRC = new Vector();
		Vector lCodTipProv = new Vector();

		IScambioSanzione lCtrlSanzione = SIEPLookupRemote.getScambioSanzionRemote();
		String[] TipoDec = { "02", "03" };

		lScaSanTipPro = lCtrlSanzione.ExRicercaScambioSanzioneRichConv(TipoDec,
				lFascMod.getIdFascicoloSiep());
		lScambioSanzioneRC = lScaSanTipPro[0];
		lCodTipProv = lScaSanTipPro[1];
		setRequestAttribute("lScaSanzRC", lScambioSanzioneRC);
		setRequestAttribute("lCodTipPro", lCodTipProv);

		if (lRichiestaConversioni.size() > 0) {
			lRicMod = (RichiestaConversioneModel) lRichiestaConversioni.get(0);
			lRicMod = lCtrlRic.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
			setRequestAttribute("richiestaconversione", lRicMod);
			return PG_LOAD_INSERISCI_ANNOTAZIONE_PROVVEDIMENTO;
		} else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Richiesta conversione Inesistente.");
			return IWebConstants.PG_MESSAGE;
		}
	}
}
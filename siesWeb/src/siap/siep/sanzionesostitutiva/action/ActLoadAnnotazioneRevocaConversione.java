package siap.siep.sanzionesostitutiva.action;

import java.util.ArrayList;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadAnnotazioneRevocaConversione
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci Annotazione Provvedimento Sanzione Sostitutiva
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
public class ActLoadAnnotazioneRevocaConversione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
			return IWebConstants.PG_MESSAGE;

		// ricerco le sanzione sostitutive
		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),
				"S");

		// Inserisco la SS residua nel model della PR
		if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
			lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "N");
		}

		lPenaResMod.setSanzSostResidua(lSSResiduaModel);

		setRequestAttribute("penaresidua", lPenaResMod);

		// Imposta Combo posizione Giuridica
		if (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07"))
			lPos.getPosizioneGiuridica().setCodPosizioneGiuridica("10");

		// 20191002 [SG]: intervento post collaudo 11.3 --> cambiata la query
		// Option lOptionPG = new Option(DecodificheManager.getInstance().getPosizioniSanzione(),
		Option lOptionPG = new Option(DecodificheManager.getInstance().getPosizioneGiuridica(),
				lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());
		setRequestAttribute("posizioneGiuridica", "" + lOptionPG);

		String lSelPosGiuAltra = "-";
		if (lPos.getAltraCausa() != null) {
			lSelPosGiuAltra = lPos.getAltraCausa().getCodTipoPosGiuridica();
		}

		Option lOptionAC = new Option(DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa(),
				lSelPosGiuAltra);
		setRequestAttribute("posizioneAltraCausa", "" + lOptionAC);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoSanzioneSostitutiva(), "-");
		setRequestAttribute("tipoSanzioneSostitutiva", "" + lOption);

		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

		Vector lDefiAltro = new Vector(DecodificheManager.getInstance().getMotivoDefiAltro());
		lDefiAltro.remove(new DecodificheModel("-", "-", "DEFI_ALTRO", "-", "-", "-", "-", "-", "-"));
		setRequestAttribute("provvedimento", lDefiAltro);

		ArrayList lAutorita = new ArrayList();
		lAutorita.add(DecodificheManager.getInstance().getAutoritaGE());
		lAutorita.add(DecodificheManager.getInstance().getAutoritaAltro());
		lAutorita.add(DecodificheManager.getInstance().getAutoritaSorveglianza());
		setRequestAttribute("autorita", lAutorita);

		return PG_LOAD_INSERISCI_ANNOTAZIONI_REVOCA;
	}

}
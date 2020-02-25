package siap.siep.penapecuniaria.action;

import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
// 01-06-2016 - Riciclo dopo Primo Collaudo per V.10
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

// 01-06-2016 - END Riciclo

/**
 * <p>
 * Title: ActLoadAnnotazioneRevocaConversioneSanzSost
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci Annotazione Provvedimento Sanzione Sostitutiva
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Intersistemi
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadAnnotazioneRevocaConversioneSanzSost extends ActionSiap implements
		ICostantiPenaPecuniaria {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

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
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

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

		// Ricerca della Richiesta Conversione
		RichiestaConversioneModel lRCModel = null;
		try {
			lRCModel = new RichiestaConversioneModel();
			IRichiestaConversione lRCCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
			lRCModel = lRCCtrl.ExRicercaRichiestaConversioneByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lRCModel.setDescrTipoSanzione(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getTipoSanzioneConvertita(), lRCModel.getCodTipoSanzione()));
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("ActLoadAnnotazioneRevocaConversioneSanzSost Exception");
		}

		setRequestAttribute("richiestaconversione", lRCModel);

		// Inserisco la SS residua nel model della PR
		if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
			lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "N");
		}

		lPenaResMod.setSanzSostResidua(lSSResiduaModel);

		setRequestAttribute("penaresidua", lPenaResMod);

		// Imposta Combo posizione Giuridica
		if (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07")) {
			lPos.getPosizioneGiuridica().setCodPosizioneGiuridica("10");
		}

		Option lOptionPG = new Option(DecodificheManager.getInstance().getPosizioniSanzione(), lPos
				.getPosizioneGiuridica().getCodPosizioneGiuridica());
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

		lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimento(), "-");
		lOption.setFilter(new String[] { "-", "0261", "0262" });
		setRequestAttribute("motivoprovvedimento", "" + lOption);

		Vector lDefiAltro = new Vector(DecodificheManager.getInstance().getMotivoDefiAltro());
		lDefiAltro.remove(new DecodificheModel("-", "-", "DEFI_ALTRO", "-", "-", "-", "-", "-", "-"));
		setRequestAttribute("provvedimento", lDefiAltro);

		ArrayList lAutorita = new ArrayList();
		lAutorita.add(DecodificheManager.getInstance().getAutoritaGE());
		lAutorita.add(DecodificheManager.getInstance().getAutoritaAltro());
		lAutorita.add(DecodificheManager.getInstance().getAutoritaSorveglianza());
		setRequestAttribute("autorita", lAutorita);

		// 01-06-2016 - Riciclo dopo Primo Collaudo per V.10
		// =====================================
		// Ricerca Magistrato Competente
		// =====================================
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null) {
			setRequestAttribute("magistratocompetente", lMagMod);
		}
		// 01-06-2016 - END Riciclo

		return PG_LOAD_INSERISCI_ANNOTAZIONI_REVOCA_CONV_SANZ_SOST;
	}

}
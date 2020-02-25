package siap.siep.sanzionesostitutiva.action;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
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
public class ActLoadInserisciAnnotazioneProvvedimento extends ActionSiap
		implements ICostantiSanzioneSostitutiva {

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
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);
		setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
			return IWebConstants.PG_MESSAGE;

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

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

		// ricerca penacomplessiva e sanzione sostitutiva
		IPenaComplessiva lCtrlPena = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaSanzioneSostitutivaModel lPenSanMod = lCtrlPena
				.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("lPenComSanSost", lPenSanMod);

		// Autorità Esterna
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// carico il tipo provvedimento
		Option lOptionTipProv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		// lOptionTipProv.setFilter( new String[] {"-","02", "03"} ); //DECRETO o ORDINANZA
		lOptionTipProv.setFilter(new String[] { "02", "03" }); // DECRETO o ORDINANZA
		// lOptionTipProv.setSelected("-");
		lOptionTipProv.setSelected("02");
		// setRequestAttribute("tipoprovvedimento", "" + lOptionTipProv );

		// OGGETTO PROVVEDIMENTO
		Option lOptionObjProv = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		// lOptionObjProv.setFilter(new String[] {"-","2080", "2081"});
		lOptionObjProv.setFilter(new String[] { "2080", "2081" });
		// lOptionObjProv.setSelected("-");
		lOptionObjProv.setSelected("2080");
		// setRequestAttribute("oggettoprovvedimento", "" + lOptionObjProv);

		// setRequestAttribute("filtroEsito", new String[] {"-","1140", "1142","1144", "1145", "1146"});
		// setRequestAttribute("filtroEsito", new String[] {"1140", "1142","1144", "1145", "1146"});

		// List lEsito = new ArrayList(DecodificheManager.getInstance().getEsitoTenore());
		// setRequestAttribute("esito", lEsito);

		return PG_LOAD_INSERISCI_ANNOTAZIONI_PROVVEDIMENTO;
	}

}
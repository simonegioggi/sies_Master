package siap.siep.sanzionesostitutiva.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
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
 * Title: ActLoadInserisciTrasmissioneAttiEsecuzione
 * Description: Azione Load della Trasmissione atti esecuzione nel caso di Cumulo
 *
 * @version 1.0
 */
public class ActLoadInserisciTrasmissioneAttiEsecuzione extends ActionSiap
		implements ICostantiSanzioneSostitutiva {
	/**
	 * Azione di caricamento della form d'inserimento della trasmissione atti esecuzione
	 *
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		this.isFascicoloSiepDiCompetenza();

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		this.isEventoNonValidato();

		// residenza
		IResidenza lResCtrl = SICOLookupRemote.getResidenzaRemote();
		Vector lVec = lResCtrl.ExRicercaResidenzeByIdFascicolo(lFascMod.getIdFascicoloSiep());

		ResidenzaAssociataModel lResAssMod = new ResidenzaAssociataModel();

		if (lVec != null && !lVec.isEmpty()) {
			lResAssMod = (ResidenzaAssociataModel) lVec.get(0);
		}

		this.setRequestAttribute("residenzaassociata", lResAssMod);

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

		setRequestAttribute("penaresidua", lPenaResMod);

		// ricerco le sanzione sostitutive
		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),
				"S");

		// Inserisco la SS residua nel model della PR
		if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
			lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "N");
		}

		lPenaResMod.setSanzSostResidua(lSSResiduaModel);

		// solo se provengo da annotazione provvedimento
		if (!this.isRequestParameterNullObj("lAnnotazione")) {
			setRequestAttribute("lAnnotazione", this.getRequestStringParameter("lAnnotazione"));
			setRequestAttribute("lSedeUfficio", this.getRequestStringParameter("lSedeUfficio"));
		}

		// ricerca penacomplessiva e sanzione sostitutiva
		IPenaComplessiva lCtrlPena = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaSanzioneSostitutivaModel lPenSanMod = lCtrlPena
				.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("lPenComSanSost", lPenSanMod);

		// misure cautelari
		IMisuraCautelare lCtrlMis = SIEPLookupRemote.getMisuraCautelareRemote();
		Vector lVectMis = lCtrlMis.ExRicercaMisureCautelariByIdFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("misurecautelari", lVectMis);

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		return PG_LOAD_INSERISCI_TRASMISSIONE_ATTI_ESECUZIONE;
	}

}
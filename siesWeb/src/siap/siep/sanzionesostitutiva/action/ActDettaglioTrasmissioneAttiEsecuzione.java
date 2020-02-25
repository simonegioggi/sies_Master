package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActDettaglioTrasmissioneAttiEsecuzione
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio trasmissione atti esecuzione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */

public class ActDettaglioTrasmissioneAttiEsecuzione extends ActSIESDettaglioProvvedimento
		implements ICostantiSanzioneSostitutiva {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("evento", lEveMod);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// residenza
		IResidenza lResCtrl = SICOLookupRemote.getResidenzaRemote();
		Vector lVec = lResCtrl.ExRicercaResidenzeByIdFascicolo(lFascMod.getIdFascicoloSiep());

		ResidenzaAssociataModel lResAssMod = new ResidenzaAssociataModel();

		if (lVec != null && !lVec.isEmpty()) {
			lResAssMod = (ResidenzaAssociataModel) lVec.get(0);
		}

		this.setRequestAttribute("residenzaassociata", lResAssMod);

		// Penaresidua
		PenaResiduaModel lPenaResidua = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		// ricerco le sanzione sostitutive
		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),
				"S");

		// Inserisco la SS residua nel model della PR
		if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
			lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "N");
		}

		lPenaResidua.setSanzSostResidua(lSSResiduaModel);

		setRequestAttribute("penaresidua", lPenaResidua);

		// ricerca penacomplessiva e sanzione sostitutiva
		IPenaComplessiva lCtrlPena = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaSanzioneSostitutivaModel lPenSanMod = lCtrlPena
				.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("lPenComSanSost", lPenSanMod);

		// misure cautelari
		IMisuraCautelare lCtrlMis = SIEPLookupRemote.getMisuraCautelareRemote();
		Vector lVectMis = lCtrlMis.ExRicercaMisureCautelariByIdFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("misurecautelari", lVectMis);

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		return PG_DETTAGLIO_TRASMISSIONE_ATTI_ESECUZIONE;
	}
}
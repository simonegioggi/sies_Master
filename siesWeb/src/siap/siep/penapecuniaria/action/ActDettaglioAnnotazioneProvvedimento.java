package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.scambiosanzione.action.ICostantiScambioSanzione;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActDettaglioAnnotazioneProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio del provveidmento sanzione sostitutiva
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
public class ActDettaglioAnnotazioneProvvedimento extends ActSIESDettaglioProvvedimento implements
		ICostantiPenaPecuniaria, ICostantiScambioSanzione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

		setRequestAttribute("eventonotifica", lEveNotMod);

		// scambio sanazione
		IScambioSanzione lCtrlSc = SIEPLookupRemote.getScambioSanzionRemote();
		// 28/07/2015 Recupero di SCAMBIO_SANZIONE attraverso ID_EVENTO anche se l'Evento non è validato.
		// ScambioSanzioneModel scSanzioneMod =
		// lCtrlSc.ExRicercaScambioSanzioneByEveIdEvento(lEveNotMod.getEvento().getEveIdEvento());
		ScambioSanzioneModel scSanzioneMod = lCtrlSc
				.ExRicercaScambioSanzioneByEveIdEventoNoControlValid(lEveNotMod.getEvento().getEveIdEvento());

		setRequestAttribute("ScaSan", scSanzioneMod);

		// Richiesta Conversione
		IRichiestaConversione lCtrlCon = SIEPLookupRemote.getRichiestaConversioneRemote();
		// 28/07/2015 Recupero dei quantum della RICHIESTA_CONVERSIONE attraverso FAS_SIE_ID_FASCICOLO_SIEP e
		// non attraverso EVE_ID_EVENTO
		// RichiestaConversioneModel RicMod =
		// lCtrlCon.ExRicercaRichiestaConversioneByIdEvento(lEveNotMod.getEvento().getEveIdEvento());
		RichiestaConversioneModel RicMod = lCtrlCon.ExRicercaRichiestaConversioneByIdFascicoloSiep(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("RichiCon", RicMod);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Penaresidua
		/*PenaResiduaModel lPenaResidua = */getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		return PG_DETTAGLIO_INSERISCI_ANNOTAZIONI_PROVVEDIMENTO;
	}

}
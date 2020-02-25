package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioTrasmissioneAttiEsecuzione
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio trasmissione atti per conversione
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
@SuppressWarnings("rawtypes")
public class ActDettaglioTrasmissioneConversione extends ActSIESDettaglioProvvedimento
		implements ICostantiPenaPecuniaria {

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

		// ricerco la'prima richiesta di conversione e con l'Id trovato faccio
		// la ricerca by Id che contiene all'interno del Model tutte le Descrizioni
		// che serviranno nella form di dettaglio
		IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		lRicMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// Vector lRichieste=lCtrlRic.ExRicercaRichiestaConversione((lRicMod));
		Vector lRichieste = lCtrlRic.ExRicercaRichiesteConversioniValide((lRicMod));

		lRicMod = (RichiestaConversioneModel) lRichieste.get(0);
		lRicMod = lCtrlRic.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
		setRequestAttribute("richiestaconversione", lRicMod);

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		return PG_DETTAGLIO_TRASMISSIONE_CONVERSIONE;
	}

}
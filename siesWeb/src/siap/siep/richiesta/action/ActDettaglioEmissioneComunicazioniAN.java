package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioEmissioneComunicazioniAN
 * </p>
 * <p>
 * Action per il caricamento del dettaglio dei una Comunicazione di Concessione Amnistia/Indulto e
 * Depenalizzazione/Incostituzionalità e relative notifiche (Decisione del GE)
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioEmissioneComunicazioniAN extends ActSIESDettaglioProvvedimento
		implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		/********* Posizione Giuridica ***************/
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaModel lPosMod =
		 * lCtrlPos.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		 */

		// ==========================================================================
		// Recupero la Comunicazione e le notifiche associate
		// ==========================================================================
		// id dell'evento inserito (Comunicazione)
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);

		String codice = lEveMod.getEvento().getCodMotivo();
		String partenza = "AN";
		this.setRequestAttribute("partenza", partenza);
		this.setRequestAttribute("codice", codice);

		// ==========================================================================
		// Recupero il Provvedimento di concessione
		// ==========================================================================
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEveModRic = new EventoModel();
		lEveModRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveModRic.setCodTipoProvvedimento("04");
		lEveModRic.setCodTipoEvento("01");
		String[] motivi = { "0285", "0286", "0287" };

		// STUB 03/10/2005 REWORK STATO ESECUZIONE
		// EventoModel lEveModel = lCtrlEve.ExRicercaEventoPerMotivo(motivi,lEveModRic);
		String[] tipoProvv = { "04", "26" };
		EventoModel lEveModel = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(motivi, tipoProvv, lEveModRic);

		// ==========================================================================
		// Recupero le Annotazioni collegate al Provvedimento
		// ==========================================================================
		Vector lAnnMod = new Vector();
		if (lEveModel != null) {
			IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveModel.getIdEvento());
			this.setRequestAttribute("annotazioneManuale", lAnnMod);
		}

		// ==========================================================================
		// Recupero la posizione giuridica da passare alla form di visualizzazione
		// ==========================================================================
		PosizioneGiuridicaModel lPosMod = this.getPosizioneGiuridica(lIdEvento,
				lFascMod.getIdFascicoloSiep());
		this.setRequestAttribute("posizioneGiuridica", lPosMod);

		// ==========================================================================
		// Ricerca Magistrato
		// ==========================================================================
		IMagistrato lWCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lWCtrl.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		if (!this.isRequestParameterNullObj("modifica")) {// provengo dal dettaglio dei provvedimenti del PM
			setRequestAttribute("dettaglioPM", "SI");
		}

		// ==========================================================================
		// 0301 - Decisione del GE - Indulto/Amnistia
		// 0298 - Richiesta al GE - Indulto/Amnistia
		// else
		// 0300 - Decisioni del GE - Depenalizzazione/Incostituzionalità
		// 0299 - Richieste al GE - Depenalizzazione/Incostituzionalità
		// ==========================================================================
		if (codice.equals("0301") || codice.equals("0298")) {
			return PG_DETTAGLIO_EMISSIONE_COMUNICAZIONI_CONCESSE;
		} else {
			return PG_DETTAGLIO_EMISSIONE_COMUNICAZIONI_REVOCATE;
		}
	}

}
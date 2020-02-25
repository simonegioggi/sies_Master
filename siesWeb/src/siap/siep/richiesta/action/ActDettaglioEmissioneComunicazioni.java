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
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 *
 * <p>
 * Title: ActDettaglioEmissioneComunicazioni
 * </p>
 * <p>
 * Action per il caricamento del dettaglio dei una Comunicazione di Richiesta Amnistia/Indulto e relative
 * notifiche (Richieste al GE)
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActDettaglioEmissioneComunicazioni extends ActSIESDettaglioProvvedimento
		implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito (Comunicazione)
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		/*
		 * ********Posizione Giuridica************** REWORK DETTAGLIO IPosizioneGiuridica lCtrlPos =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); PosizioneGiuridicaModel lPosMod =
		 * lCtrlPos.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		 */

		PosizioneGiuridicaModel lPosMod = this.getPosizioneGiuridica(lIdEvento,
				lFascMod.getIdFascicoloSiep());

		this.setRequestAttribute("posizioneGiuridica", lPosMod);

		// ==========================================================================
		// Recupero la Comunicazione e le notifiche associate
		// ==========================================================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);
		String codice = lEveMod.getEvento().getCodMotivo();
		this.setRequestAttribute("codice", codice);

		// ==========================================================================
		// Recupero la Richiesta (01-26-xxxx)
		// n.b. Poichè la 'Comunicazione' non è collegata alla 'Richiesta' che la ha
		// generata devo recuperare per forza l'ultima Richiesta
		// ==========================================================================
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();

		Vector lAnnMod = new Vector();
		if (lEveMod != null && lEveMod.getEvento() != null
				&& lEveMod.getEvento().getAnnIdAnnotazioneManuale() != null) {
			BigDecimal lIdAnnotazioneManuale = lEveMod.getEvento().getAnnIdAnnotazioneManuale();

			IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			AnnotazioneManualeModel aAnnManMod = lCtrlAnn
					.ExRicercaAnnotazioneManualeByKey(lIdAnnotazioneManuale);

			if (aAnnManMod != null) {
				lAnnMod.add(aAnnManMod);
			}

			this.setRequestAttribute("annotazioneManuale", lAnnMod);
		} else {
			EventoModel lEveModRic = new EventoModel();
			lEveModRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lEveModRic.setCodTipoEvento("01");
			// lEveModRic.setCodTipoProvvedimento("04");
			String[] motivi = { "0210", "0211", "0122" };

			// STUB 29/09/2005 REWORK STATO ESECUZIONE
			String[] tipoProvv = { "04", "26" };
			EventoModel lEveModel = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(motivi, tipoProvv, lEveModRic);

			// ==========================================================================
			// Recupero le Annotazioni collegate alla Richiesta
			// ==========================================================================
			// Vector lAnnMod = new Vector();
			if (lEveModel != null) {
				IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
				lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveModel.getIdEvento());

				this.setRequestAttribute("annotazioneManuale", lAnnMod);
			}
		}

		// Ricerca Magistrato
		IMagistrato lWCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lWCtrl.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		if (!this.isRequestParameterNullObj("modifica")) {// provengo dal dettaglio dei provvedimenti del PM
			setRequestAttribute("dettaglioPM", "SI");
		}

		// ==========================================================================
		// 0301 - Decisione del GE - Indulto (mai vera)
		// 0298 - Richiesta al GE - Indulto
		// else
		// 0300 - Decisioni del GE - Amnistia (mai Vera)
		// 0299 - Richieste al GE - Amnistia
		// ==========================================================================
		if (codice.equals("0301") || codice.equals("0298")) {
			return PG_DETTAGLIO_EMISSIONE_COMUNICAZIONI_CONCESSE;
		} else {
			return PG_DETTAGLIO_EMISSIONE_COMUNICAZIONI_REVOCATE;
		}
	}

}
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
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 *
 * <p>
 * Title: ActDettaglioOrdineScarcerazioneProvv
 * </p>
 * <p>
 * Description: Dettaglio Ordine Provvisorio di Scarcerazione per Indulto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 */
public class ActDettaglioOrdineScarcerazioneProvv extends ActSIESDettaglioProvvedimento implements
		ICostantiRichiesta {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		AnnotazioneManualeModel lAnnModel = null;
		Vector reati = new Vector();

		/********* Posizione Giuridica ***************/
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaModel lPosMod =
		 * lCtrlPos.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		 */

		PosizioneGiuridicaModel lPosMod = this
				.getPosizioneGiuridica(lIdEvento, lFascMod.getIdFascicoloSiep());

		this.setRequestAttribute("posizioneGiuridica", lPosMod);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);

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
			lEveModRic.setCodTipoProvvedimento("04");
			lEveModRic.setCodTipoEvento("01");
			lEveModRic.setCodMotivo("0122");
			lEveModRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

			// STUB 12/10/2005 REWORK STATO ESECUZIONE
			/*
			 * Vector evento = lCtrlEvento.ExRicercaEvento(lEveModRic); if(evento.size()>0) lEveModRic =
			 * (EventoModel)evento.get(0);
			 */
			String[] lMotivi = { "0122" };
			String[] lTipoProvv = { "04", "26" };
			lEveModRic = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(lMotivi, lTipoProvv, lEveModRic);

			// Vector lAnnMod = new Vector();
			if (lEveModRic != null) {
				IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
				lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveModRic.getIdEvento());

				this.setRequestAttribute("annotazioneManuale", lAnnMod);
			}
		}

		for (int i = 0; i < lAnnMod.size(); i++) {
			lAnnModel = (AnnotazioneManualeModel) lAnnMod.get(i);
			if (lAnnModel.getReaIdReato() != null) {
				IReato lCtrlReato = SIEPLookupRemote.getReatoRemote();
				ReatoModel lReatoModel = lCtrlReato.ExRicercaReatoByKey(lAnnModel.getReaIdReato());
				reati.add(lReatoModel);
				this.setRequestAttribute("reati", reati);
			}
		}

		/*
		 *  *****************************Ricerca ordine esecuzione per codice 9
		 * ********************************************************** / EventoModel lEveOESet = new
		 * EventoModel(); lEveOESet.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * lEveOESet.setCodTipoEvento("01"); lEveOESet.setCodTipoProvvedimento("06"); Vector eventi = new
		 * Vector(); try {
		 * 
		 * eventi = lCtrlEve.ExRicercaEvento(lEveOESet); }
		 * 
		 * catch (Exception ex) { }
		 * 
		 * EventoModel lEveOE = new EventoModel(); if(eventi.size()>0) { lEveOE = (EventoModel) eventi.get(0);
		 * if(lEveOE != null) { this.setRequestAttribute("lEveOE",lEveOE); } } /
		 * ***************************************************************************************
		 */

		// Ricerca Magistrato
		IMagistrato lWCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lWCtrl.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// notifiche
		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			NotificaModel lNotMod = new NotificaModel();
			lNotMod = lEveMod.getNotifiche()[i];

			if (lNotMod != null && lNotMod.getAutEstIdAutoritaEsterna() != null) {
				setRequestAttribute("autoritaEsterna", lNotMod.getAutoritaEsterna());
				setRequestAttribute("noteautoritaEsterna", lNotMod.getNote());
			} else if (lNotMod != null && lNotMod.getIstDetIdIstitutoDetenzione() != null) {
				setRequestAttribute("istituto", lNotMod.getIstitutoDetenzione());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& lNotMod.getUfficio().getCodTipoUfficio().equals("TDS")) {
				setRequestAttribute("ufficiotds", lNotMod.getUfficio());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& lNotMod.getUfficio().getCodTipoUfficio().equals("UDS")) {
				setRequestAttribute("ufficiouds", lNotMod.getUfficio());
			} else if (lNotMod != null && lNotMod.getCSSA() != null) {
				setRequestAttribute("cssa", lNotMod.getCSSA());
			}
		}

		return PG_DETTAGLIO_ORDINE_SCARCERAZIONE_PROVV;
	}

}
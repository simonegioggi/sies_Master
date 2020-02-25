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
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 *
 * <p>
 * Title: ActDettaglioRichDetPenAboReato
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
public class ActDettaglioRichDetPenAboReato extends ActSIESDettaglioProvvedimento
		implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

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

		PosizioneGiuridicaModel lPosMod = this.getPosizioneGiuridica(lIdEvento,
				lFascMod.getIdFascicoloSiep());

		this.setRequestAttribute("posizioneGiuridica", lPosMod);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);

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
			lEveModRic.setFasSieIdFascicoloSiep(lFascMod.getFasSieIdFascicoloSiep());
			lEveModRic.setCodTipoProvvedimento("26");

			lEveModRic.setCodTipoEvento("01");
			lEveModRic.setCodMotivo("0210");
			Vector evento = lCtrlEvento.ExRicercaEvento(lEveModRic);
			if (evento.size() > 0)
				lEveModRic = (EventoModel) evento.get(0);
			this.setRequestAttribute("evento", lEveModRic);

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

		// Ricerca Magistrato
		IMagistrato lWCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lWCtrl.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		return PG_DETTAGLIO_RICHIESTA_DET_PEN_ABO_REATO;
	}

}
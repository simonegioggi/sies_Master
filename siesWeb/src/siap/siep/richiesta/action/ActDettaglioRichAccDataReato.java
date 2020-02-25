package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
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
 * Title: ActDettaglioRichAccDataReato
 * </p>
 * <p>
 * Description: Dettaglio per la Richiesta accertamento data commesso reato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 */
public class ActDettaglioRichAccDataReato extends ActSIESDettaglioProvvedimento implements ICostantiRichiesta {

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

		PosizioneGiuridicaModel lPosMod = this
				.getPosizioneGiuridica(lIdEvento, lFascMod.getIdFascicoloSiep());

		this.setRequestAttribute("posizioneGiuridica", lPosMod);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);

		UfficioModel lUf = lEveMod.getNotifiche()[0].getUfficio();
		IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
		lUf = lCtrlUff.getUfficioByCodTipoUffDescrComune(lUf.getCodTipoUfficio(), lUf.getDescrComune());
		setRequestAttribute("ufficio", lUf);

		// -
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
			EventoModel lEveModRicerca = new EventoModel();
			lEveModRicerca.setFasSieIdFascicoloSiep(lFascMod.getFasSieIdFascicoloSiep());
			lEveModRicerca.setCodTipoEvento("01");
			String[] lTipoProv = { "04", "26" };
			String[] lCodMotivo = { "0122", "0122" };
			Vector evento = lCtrlEvento.ExRicercaEventoTipoProvTipoMot(lEveModRicerca, lTipoProv, lCodMotivo);

			// Evento risultato della ricerca
			EventoModel lEveModRicAmn = null;
			if (evento.size() > 0)
				lEveModRicAmn = (EventoModel) evento.get(0);

			if (lEveModRicAmn != null) {
				IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
				lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveModRicAmn.getIdEvento());

				this.setRequestAttribute("annotazioneManuale", lAnnMod);
			}
		}
		// -

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

		return PG_DETTAGLIO_RICHIESTA_ACC_DATA_REATO;
	}

}
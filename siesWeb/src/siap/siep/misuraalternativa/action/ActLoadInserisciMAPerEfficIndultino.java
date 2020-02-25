package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.refertoscarcerazione.controller.IRefertoScarcerazione;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciMAPerEfficIndultino
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MisuraAlternativa
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

public class ActLoadInserisciMAPerEfficIndultino extends ActPerditaEfficacia {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = this.getPerditaEfficacia();
		if (!lRitorno.equals(""))
			return lRitorno;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ricerca referto di scarcerazione
		String lFlagReferto = "N";
		EventoModel lEvento = new EventoModel();
		IEventoSimeone lCtrlEveSimeone = SICOLookupRemote.getEventoSimeoneRemote();
		lEvento = lCtrlEveSimeone.ExRicercaEventoByFascicoloSiepTipEventoTipProvCodMotivo(
				lFascMod.getIdFascicoloSiep(), "14", "19", "0315");
		if (lEvento != null && lEvento.getIdEvento() != null) {
			EventoModel lEventoProv = new EventoModel();
			lEventoProv = lCtrlEveSimeone.ExRicercaEventoByEveIdEvento(lEvento.getIdEvento());

			if (lEventoProv != null && lEventoProv.getCodMotivo() != null
					&& lEventoProv.getCodMotivo().equals("2289")) {

				IRefertoScarcerazione lCtrlSca = SIEPLookupRemote.getRefertoScarcerazioneRemote();
				RefertoScarcerazioneModel lRef = new RefertoScarcerazioneModel();
				lRef = lCtrlSca.ExRicercaRefertoScarcerazioneByEveIdEvento(lEvento.getIdEvento());

				if (lRef != null && lRef.getIdRefertoScarcerazione() != null) {
					this.setRequestAttribute("referto", lRef);
					this.setRequestAttribute("eventoreferto", lEvento);
					this.setRequestAttribute("eventopro", lEventoProv);
					lFlagReferto = "S";
				}
			}
		}
		this.setRequestAttribute("lFlagReferto", lFlagReferto);

		// setto il campo codice motivo
		Collection lmotivo = DecodificheManager.getInstance().getMotivoProvvedimentoMAPerEfficIndultino();
		String lDesMotivo = "";
		String lCodiceMotivo = "";
		if (lmotivo != null && !lmotivo.isEmpty()) {
			Iterator lIter = lmotivo.iterator();
			if (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();
				lDesMotivo = lDecMod.getDescription();
				lCodiceMotivo = lDecMod.getCode();
			}
		}

		setRequestAttribute("motivoProvv", lDesMotivo);
		setRequestAttribute("codicemotivo", lCodiceMotivo);

		setRequestAttribute("tipoSospensione", "INDULTINO");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_PER_EFF;
	}

}
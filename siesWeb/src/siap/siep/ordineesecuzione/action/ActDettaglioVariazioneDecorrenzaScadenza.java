package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioOrdineScarcerazione
 * </p>
 * <p>
 * Description: Classe Action per la il Dettaglio Ordine Scarcerazione
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
public class ActDettaglioVariazioneDecorrenzaScadenza extends ActSIESDettaglioProvvedimento implements
		ICostantiOrdineEsecuzione {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		BigDecimal lIdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEve);
		/*
		 * REWORK DETTAGLIO PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEve, lEveMod.getEvento()
						.getFasSieIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		int lIndex = 0;

		Vector lVectAvvocati = new Vector();

		for (lIndex = 0; lIndex < lEveMod.getNotifiche().length; lIndex++) {
			// Controllo se c'e' un Avvocato associato alla Notifica
			if (lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep() != null) {
				IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
				AvvocatoSiepModel lAvvocato = lAvvCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lEveMod
						.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep());
				lVectAvvocati.add(lAvvocato);
			}
		}

		if (lVectAvvocati.size() > 0) {
			setRequestAttribute("avvocati", lVectAvvocati);
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
//		Date lDataFinePenaM = null;

		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); //PenaResiduaModel
		 * llPenMod = lCtrlp.ExRicercaPenaResiduaByIdEvento(lEveMod.getEvento().getIdEvento());
		 * PenaResiduaModel llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEve, lEveMod.getEvento()
				.getFasSieIdFascicoloSiep());

		if (llPenMod != null) {
			lDataInizioPena = llPenMod.getDataInizio();
//			lDataFinePenaM = llPenMod.getDataFine();
			lDataFinePenaA = llPenMod.getDataFinePresunta();
			setRequestAttribute("Strd" + "ataInizioPena",
					DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
			setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
			setRequestAttribute("penaresidua", llPenMod);
		}
		setRequestAttribute("eventonotifica", lEveMod);

		// recupero eventuali motivazioni
		CampoNotaModel[] lNote = lEveMod.getCampoNote();
		if (lNote != null && lNote.length > 0) {

			setRequestAttribute("motivazioni", lNote[0].getDescr());
		} else {

			setRequestAttribute("motivazioni", "");
		}

		// Ricerca se c'è Fungibilità
		FungibilitaModel lFungiMod = new FungibilitaModel();
		IFungibilita lFungiCtrl = SIEPLookupRemote.getFungibilitaRemote();
		lFungiMod = lFungiCtrl.ExRicercaFungibilitaByKeyEvento(lEveMod.getEvento().getIdEvento());

		if (lFungiMod == null) {
			setRequestAttribute("flagfungibilita", "N");
		} else {
			setRequestAttribute("fungibilita", lFungiMod);
			setRequestAttribute("flagfungibilita", "S");
		}

		return PG_DETTAGLIO_VARIAZIONE_DECORRENZA_SCADENZA;
	}

}
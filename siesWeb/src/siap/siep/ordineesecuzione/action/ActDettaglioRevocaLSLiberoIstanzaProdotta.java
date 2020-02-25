package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioRevocaLSLiberoIstanzaProdotta
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio Revoca LS Libero Istanza Prodotta
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
public class ActDettaglioRevocaLSLiberoIstanzaProdotta extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// Seleziono l'evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		/*
		 * REWORK PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lEveMod.
		 * getEvento().getFasSieIdFascicoloSiep());
		 */
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lId,
						lEveMod.getEvento().getFasSieIdFascicoloSiep());
		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);
		/*
		 * int lIndex = 0; Vector lVectAvvocati = new Vector(); for (lIndex = 0; lIndex <
		 * lEveMod.getNotifiche().length; lIndex++) { //Controllo se c'e' un Avvocato associato alla Notifica
		 * if (lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep() != null) { IAvvocato lAvvCtrl =
		 * SIEPLookupRemote.getAvvocatoRemote(); AvvocatoSiepModel lAvvocato =
		 * lAvvCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lEveMod.getNotifiche()[lIndex].
		 * getAvvIdAvvocatoFascicoloSiep()); lVectAvvocati.add(lAvvocato); } } if (lVectAvvocati.size() > 0) {
		 * setRequestAttribute("avvocati", lVectAvvocati); }
		 */

		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("C")) {
				setRequestAttribute("fogliocomplementare", "1");
				break; // se lo trova esce, altrimenti potrebbe "sporcare" l'attributo nel successivo ciclo
						// del for
			} else {
				setRequestAttribute("fogliocomplementare", "0");
			}
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrlp
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		// lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);
		setRequestAttribute("eventonotifica", lEveMod);

		// decreto di Sospensione
		EventoModel lEventoMod = new EventoModel();
		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		lEventoMod = lEventoCtrl.ExRicercaEventoByFascicoloSiepDecretoSospensione(
				lEveMod.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("decreto", lEventoMod);

		/*
		 * Istanza EventoModel lEveIstanzaMod = new EventoModel();
		 * lEveIstanzaMod.setFasSieIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep()); IEvento
		 * lEvCtrl = SICOLookupRemote.getEventoRemote(); Vector lVectEvento = null; try { lVectEvento =
		 * lEvCtrl.ExRicercaEventoIstanzaRigettata(lEveIstanzaMod); //lVectEvento =
		 * lEvCtrl.ExRicercaEvento(lEveMod); } catch (F3BException e) { if (e.getErrorCode() !=
		 * F3BException.USER_MESSAGE) { throw e; } } if (lVectEvento != null) { if (lVectEvento.size() > 0) {
		 * setRequestAttribute("istanza", (EventoModel) lVectEvento.firstElement()); } }
		 * //setRequestAttribute("ufficiotribunale",lUfficio);
		 */

		EventoModel lEventoIstanza = this.getEventoIstanza(lEveMod.getEvento().getEveIdEvento(),
				lEveMod.getEvento().getFasSieIdFascicoloSiep());
		if (lEventoIstanza != null)
			setRequestAttribute("istanza", lEventoIstanza);

		return PG_DETTAGLIO_REVOCA_LS_LIBERO_CON_ISTANZA;
	}

}
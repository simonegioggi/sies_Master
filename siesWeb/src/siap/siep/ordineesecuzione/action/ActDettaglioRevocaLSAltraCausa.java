package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 *
 * <p>
 * Title: ActDettaglioRevocaLSAltraCausa
 * </p>
 * <p>
 * Description: Action di Dettaglio Revoca LS Altra Causa
 * </p>
 * <p>
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class ActDettaglioRevocaLSAltraCausa extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller

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

		/*
		 * REWORK IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */
		PenaResiduaModel llPenMod = this.getPenaResidua(lId, lEveMod.getEvento().getFasSieIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		// lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		setRequestAttribute("eventonotifica", lEveMod);

		/*----
		 decreto di Sospensione
		 Viene cercato un decreto di sospensione a partire dal fascicolo siep
		 Purtroppo qui non si puo' fare niente dato che òl'evento è precedente...
		
		 La JSP non lo utilizza---
		EventoModel lEventoMod = new EventoModel();
		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		lEventoMod = lEventoCtrl.ExRicercaEventoByFascicoloSiepDecretoSospensione(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		
		setRequestAttribute("decreto", lEventoMod);
		*/
		// Istanza
		EventoModel lEveIstanzaMod = new EventoModel();

		lEveIstanzaMod.setFasSieIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		// lEveMod.setCodTipoEvento("03");
		/*
		 * IEvento lEvCtrl = SICOLookupRemote.getEventoRemote(); Vector lVectEvento = null; try { lVectEvento
		 * = lEvCtrl.ExRicercaEventoIstanzaRigettata(lEveIstanzaMod); //lVectEvento =
		 * lEvCtrl.ExRicercaEvento(lEveMod); } catch (F3BException e) { if (e.getErrorCode() !=
		 * F3BException.USER_MESSAGE) { throw e; } } if (lVectEvento != null) { if (lVectEvento.size() > 0) {
		 * setRequestAttribute("istanza", (EventoModel) lVectEvento.firstElement()); } }
		 */
		// setRequestAttribute("ufficiotribunale",lUfficio);
		EventoModel lEventoIstanza = this.getEventoIstanzaRigettata(lEveMod.getEvento().getEveIdEvento(),
				lEveMod.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("istanza", lEventoIstanza);

		return PG_DETTAGLIO_REVOCA_LS_ALTRA_CAUSA;
	}

}
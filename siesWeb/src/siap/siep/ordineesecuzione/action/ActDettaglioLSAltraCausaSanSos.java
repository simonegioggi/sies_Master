package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioLSAltraCausa
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Evento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 */
public class ActDettaglioLSAltraCausaSanSos extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lId,
						lEveMod.getEvento().getFasSieIdFascicoloSiep());

		/*
		 * REWORK PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lEveMod.
		 * getEvento().getFasSieIdFascicoloSiep());
		 */
		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		/*
		 * REWORK int lIndex = 0;
		 * 
		 * Vector lVectAvvocati = new Vector();
		 * 
		 * for (lIndex = 0; lIndex < lEveMod.getNotifiche().length; lIndex++) { //Controllo se c'e' un
		 * Avvocato associato alla Notifica if (lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep()
		 * != null) { IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote(); AvvocatoSiepModel lAvvocato =
		 * lAvvCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lEveMod.getNotifiche()[lIndex].
		 * getAvvIdAvvocatoFascicoloSiep()); lVectAvvocati.add(lAvvocato); } }
		 */

		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("C")) {
				setRequestAttribute("fogliocomplementare", "1");
			} else {
				setRequestAttribute("fogliocomplementare", "0");
			}
		}
		/*
		 * if (lVectAvvocati.size() > 0) { setRequestAttribute("avvocati", lVectAvvocati); }
		 */

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		/*
		 * IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */
		PenaResiduaModel llPenMod = getPenaResidua(lId, lEveMod.getEvento().getFasSieIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		// lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		setRequestAttribute("eventonotifica", lEveMod);

		/*
		 * REWORK--- IN questo caso di sicuro l'istanza non c'e'. Altrimenti saremo nel caso
		 * ActDettaglioLSAltraCausaIstanzaprodotta!
		 * 
		 * EventoModel lEveModel = new EventoModel();
		 * 
		 * lEveModel.setFasSieIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 * lEveModel.setCodTipoEvento("03");
		 * 
		 * IEvento lEvCtrl = SICOLookupRemote.getEventoRemote(); Vector lVectEvento = null; try { lVectEvento
		 * =lEvCtrl.ExRicercaEventoIstanza(lEveMod.getEvento().getFasSieIdFascicoloSiep()); } catch
		 * (F3BException e) { if (e.getErrorCode() != F3BException.USER_MESSAGE) { throw e; } }
		 * 
		 * if (lVectEvento != null) { if(lVectEvento.size() > 0) { setRequestAttribute("istanza",
		 * (EventoModel)lVectEvento.firstElement()); }
		 * 
		 * }
		 */

		return PG_DETTAGLIO_LS_ALTRA_CAUSA_SAN_SOS;
	}

}
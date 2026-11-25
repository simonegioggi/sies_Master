package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.FascicoloSiepController;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
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
public class ActDettaglioLSAltraCausa extends ActSIESDettaglioProvvedimento
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
				break; // se lo trova esce, altrimenti potrebbe "sporcare" l'attributo nel successivo ciclo
						// del for
			} else {
				setRequestAttribute("fogliocomplementare", "0"); // se non lo trova continua a cercare nel
																	// successivo ciclo del for
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
		PenaResiduaModel llPenMod = this.getPenaResidua(lId, lEveMod.getEvento().getFasSieIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		// lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		setRequestAttribute("eventonotifica", lEveMod);

		FascicoloSiepController lCtrlFascicoloSiep = new FascicoloSiepController();
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal aId = lFascicoloModel.getIdFascicoloSiep();
		DettaglioFascicoloModel lDettaglio = lCtrlFascicoloSiep.ExDettaglioFascicoloSiep(aId);
		setRequestAttribute("dettagliofascicolo", lDettaglio);

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
		
		// MEV_2026-105 - Si corregge il problema che prevederva un solo codice evento sia per istaza prodotta che senza istanza
		// 01-06-5513 - Ordine di Esecuzione -- Pos.Giur. : LIBERO - Detenuto altra causa (81)
		// 01-06-0761 - Ordine di Esecuzione -- Pos.Giur. : LIBERO - Detenuto altra causa (57) (new MEV 2026-105)
		// 
		// Ma 2 Action di dettaglio. NOTA le 2 action vengono aggaciate dalla ActInserisciOrdineEsecuzioneSimeone
		// - ActDettaglioLSAltraCausaIstanzaProdotta
		// - ActDettaglioLSAltraCausa
		// Aprendo il dettaglio da elenco provv del PM o dal link NON VALIDATO (dett fasciolo) agganciava sempre
		// il dettaglio senza istanza prodotta (ActDettaglioLSAltraCausa) essendo associato sulla DETTAGLIO_PROVVEDIMENTO
		// all'unico codice evento
		// Nota il problema è per parecchi codici, ovvero tutti ALTRA CAUSA, in questi casi il codice evento è unico
		// 5509, 5510, 5511,5512, 5513
		// 
		// Quindi in sostenza questa action viene richiamata da elenco provv PM sia in caso di Istanza prodotta che non prodotta,
		// mentre dalla ActInserisci solo in caso di istanza NON prodotta.
		// Per gestire il caso di Istanza prodotta si testa la sua presenza
		// Provo a recuperare l'istanza se esiste
		// Attenzione!! il metodo getEventoIstanza se ide evtno è null recuper auna istanza a caso anche annullata 
		// collegata al fascicolo
		EventoModel lEventoIstanza = null;
		if (lEveMod.getEvento().getEveIdEvento()!=null) {
		    lEventoIstanza = this.getEventoIstanza(lEveMod.getEvento().getEveIdEvento(),
				lEveMod.getEvento().getFasSieIdFascicoloSiep());
		}
		
		if (lEventoIstanza!=null && lEventoIstanza.getIdEvento()!=null) {
			setRequestAttribute("istanza", lEventoIstanza);
			return PG_DETTAGLIO_LS_ALTRA_CAUSA_ISTANZA;
		} else {
			return PG_DETTAGLIO_LS_ALTRA_CAUSA;
		}

		//return PG_DETTAGLIO_LS_ALTRA_CAUSA;
		// MEV_2026-105 - FINE
	}

}
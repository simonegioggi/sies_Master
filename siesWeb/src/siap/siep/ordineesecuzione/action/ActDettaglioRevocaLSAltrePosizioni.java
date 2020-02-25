package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.motivoevento.controller.IMotivoEvento;
import siap.siep.motivoevento.model.MotivoEventoModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioRevocaLSAltrePosizioni
 * </p>
 * <p>
 * Description: Classe Action per il Dettaglio Revoca LS Altre Posizioni
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

public class ActDettaglioRevocaLSAltrePosizioni extends ActSIESDettaglioProvvedimento implements
		ICostantiOrdineEsecuzione {
	public String processRequest() throws F3BException {
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
//		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lId);
		this.setRequestAttribute("eventonotifica", lEveMod);

		// ricerca motivo_evento
		IMotivoEvento lCtrlMotivoEvento = SIEPLookupRemote.getMotivoEventoRemote();
		MotivoEventoModel lMotivoEveMod = new MotivoEventoModel();
		lMotivoEveMod = lCtrlMotivoEvento.ExRicercaMotivoEventoByEveIdEvento(lId);
		this.setRequestAttribute("motivoevento", lMotivoEveMod);

		// ricerca misura alternativa per id evento
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lOrdinanza = new MisuraAlternativaModel();
		lOrdinanza = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento()
				.getEveIdEvento());
		setRequestAttribute("misuraalternativa", lOrdinanza);

		if (lOrdinanza != null && lOrdinanza.getChiaveUfficioFascicoloSius() != null) {
			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUffMod = new UfficioModel();
			lUffMod = lCtrlUffEmi.getUfficioByKey(lOrdinanza.getChiaveUfficioFascicoloSius());
			setRequestAttribute("UfficioEmittente", lUffMod);
		}

		/*
		 * REWORK PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lId, lEveMod.getEvento()
						.getFasSieIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("C")) {
				setRequestAttribute("fogliocomplementare", "1");
				break; // se lo trova esce, altrimenti potrebbe "sporcare" l'attributo nel
						// successivo ciclo del for
			} else {
				setRequestAttribute("fogliocomplementare", "0");
			}
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
//		Date lDataFinePenaM = null;

		/*
		 * REWORK IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod = lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().
		 * getFasSieIdFascicoloSiep());
		 */

		PenaResiduaModel llPenMod = this.getPenaResidua(lId, lEveMod.getEvento()
				.getFasSieIdFascicoloSiep());
		lDataInizioPena = llPenMod.getDataInizio();
//		lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena",
				DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA",
				DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		/*
		 * //Istanza EventoModel lEveIstanzaMod = new EventoModel();
		 * lEveIstanzaMod.setFasSieIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 * IEvento lEvCtrl = SICOLookupRemote.getEventoRemote(); Vector lVectEvento = null; try {
		 * lVectEvento = lEvCtrl.ExRicercaEventoIstanzaRigettata(lEveIstanzaMod); } catch
		 * (F3BException e) { if (e.getErrorCode() != F3BException.USER_MESSAGE) { throw e; } } if
		 * (lVectEvento != null) { if (lVectEvento.size() > 0) { setRequestAttribute("istanza",
		 * (EventoModel) lVectEvento.firstElement()); } }
		 */

		EventoModel lEventoIstanza = this.getEventoIstanza(lEveMod.getEvento().getEveIdEvento(),
				lEveMod.getEvento().getFasSieIdFascicoloSiep());
		if (lEventoIstanza != null)
			setRequestAttribute("istanza", lEventoIstanza);

		// MEV 16: aggiunto recupero di info ed impostato l'attributo nella richiesta
		IDocumentoAllegato mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		DocumentoAllegatoModel mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(
				lEveMod.getEvento().getIdEvento(), "06");
		setRequestAttribute("documentoAllegato", mDocAll);

		// valore di ritorno
		return PG_DETTAGLIO_REVOCA_LS_ALTRE_POSIZIONI;
	}
}
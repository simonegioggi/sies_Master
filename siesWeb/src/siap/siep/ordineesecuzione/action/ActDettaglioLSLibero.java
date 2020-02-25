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
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioLSLibero
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio LS Libero
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
public class ActDettaglioLSLibero extends ActSIESDettaglioProvvedimento implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		/*
		 * rework PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
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
				setRequestAttribute("fogliocomplementare", "0"); // se non lo trova continua a cercare nel
																	// successivo ciclo del for
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

		// provvedimento:
		// se il provvedimento è stato annullato (EVENTO.FLAG_DOCUMENTO_REGISTRATO=A || null),
		// il bottone "FC" non deve essere visibile
		// *****************************************
		// foglio complementare:
		// ricerca nella tabella Documento_Allegato
		// se il foglio complementare non esiste o è stato annullato tramite il bottone "FC" si deve
		// permettere l'inserimento
		// altrimenti si va in modifica
		DocumentoAllegatoModel mDocAll = null;
		IDocumentoAllegato mDocAllCtrl = null;
		mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lEveMod.getEvento().getIdEvento(),
				"06");
		setRequestAttribute("documentoAllegato", mDocAll);

		/*
		 * REWORK --- L'istanza di sicuro non c'e' altrimenti saremo nel caso LS-i istanza prodotta!
		 * EventoModel lEveModel = new EventoModel();
		 * lEveModel.setFasSieIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 * lEveModel.setCodTipoEvento("03"); IEvento lEvCtrl = SICOLookupRemote.getEventoRemote(); Vector
		 * lVectEvento = null; try { lVectEvento
		 * =lEvCtrl.ExRicercaEventoIstanza(lEveMod.getEvento().getFasSieIdFascicoloSiep()); } catch
		 * (F3BException e) { if (e.getErrorCode() != F3BException.USER_MESSAGE) { throw e; } } if
		 * (lVectEvento != null) { if(lVectEvento.size() > 0) { setRequestAttribute("istanza",
		 * (EventoModel)lVectEvento.firstElement()); } }
		 */

		return PG_DETTAGLIO_LS_LIBERO;
	}

}
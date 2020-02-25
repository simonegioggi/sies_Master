package siap.siep.calcolopena.action;

/**
* <p>Title: ActLoadDettaglioEmissioneProvvedimento</p>
* <p>Description: Classe Action per la load dettaglio di Ordine Esecuzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

public class ActLoadDettaglioEmissioneProvvedimento extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione, ICostantiAnnotazioneManuale {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// riempie il model
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		// Recupero l'evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

		// Recupero la Posizione giuridica e il luogo di detenzione
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lEveNotMod.getEvento().getFasSieIdFascicoloSiep());

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Recupera la pena residua
		IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(
				lEveNotMod.getEvento().getFasSieIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		lDataFinePenaA = llPenMod.getDataFinePresunta();

		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		// Recupera gli avvocati destinatari
		int lIndex = 0;
		Vector lVectAvvocati = new Vector();
		for (lIndex = 0; lIndex < lEveNotMod.getNotifiche().length; lIndex++) {
			// Controllo se c'e' un Avvocato associato alla Notifica
			if (lEveNotMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep() != null) {
				IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
				AvvocatoSiepModel lAvvocato = lAvvCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(
						lEveNotMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep());
				lVectAvvocati.add(lAvvocato);
			}
		}

		if (lVectAvvocati.size() > 0)
			setRequestAttribute("avvocati", lVectAvvocati);

		setRequestAttribute("eventonotifica", lEveNotMod);

		if (!this.isRequestParameterNullObj("flagPage")) {
			this.setRequestAttribute("flagPage", this.getRequestStringParameter("flagPage"));
		}

		return PG_LOAD_DETTAGLIO_EMISSIONE_PROVVEDIMENTO;
	}

}
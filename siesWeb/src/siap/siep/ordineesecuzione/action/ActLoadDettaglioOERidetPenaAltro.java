package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * Classe Action per la load del Dettaglio dell'Ordine di esecuzione a seguito rideterminazione pena altro
 * 
 * @author diego
 *
 */
public class ActLoadDettaglioOERidetPenaAltro extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// id dell'OE
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ==========================================================================
		// Recupero i dati dell'evento e delle notifiche da visualizzare nella
		// maschera
		// ==========================================================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

		this.setRequestAttribute("eventonotifica", lEveNotMod);

		// recupero codiceMotivo="0959" da EventoComputo
		String lParamValue = null;
		// String codMotivoEventoComputo = null;
		lParamValue = this.getParameter("codMotivoEventoComputo");
		if (lParamValue != null) {
			setRequestAttribute("codMotivoEventoComputo", lParamValue);
		} else if (lEveNotMod.getEvento().getCodMotivo() != null) {
			setRequestAttribute("codMotivoEventoComputo", lEveNotMod.getEvento().getCodMotivo());
		}

		// ============================================
		// ricerca posizione giuridica
		// ============================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ============================================
		// ricerca la pena residua
		// ============================================
		IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenRes = lCtrlPenaRes.ExRicercaPenaResiduaByIdEvento(lIdEvento);

		setRequestAttribute("penaresidua", lPenRes);

		// ========================================================
		// Restituisce la pagina di visualizzazione del Dettaglio
		// ========================================================
		return PG_DETTAGLIO_OE_RIDET_PENA_ALTRO;
	}

}
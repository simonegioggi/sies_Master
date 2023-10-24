package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il dettaglio del Provvedimento Estinzione pena pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActDettaglioProvvedimentoEstinzionePP extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lId);
		setRequestAttribute("eventonotifica", lEveNotMod);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lEveNotMod.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// FIXME non ho dati collegati all'evento
		// Ricerca i pagamenti per id Fascicolo
		/*
		 * Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>(); IRateizzazionePP lRateCTRL =
		 * SIEPLookupRemote.getRateizzazionePPRemote();
		 * 
		 * // MEV33 si recuperano le rate collegat listaRateizzazioni =
		 * lRateCTRL.exRicercaRateizzazioniByIdEvento(lEveNotMod.getEvento().getIdEvento());
		 * siesLogger.debug("listaRateizzazioni.size() = "+listaRateizzazioni.size());
		 * 
		 * setRequestAttribute("listaRateizzazioni", listaRateizzazioni);
		 */

		// MAGISTRATO
		MagistratoModel lMag = lEveNotMod.getMagistrato();
		setRequestAttribute("magistrato", lMag);

		// NOTIFICHE
		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		List lListAvvocatiSiep = new ArrayList();
		List lListaObbligati = new ArrayList();

		for (int i = 0; i < lNotifiche.length; i++) {
			// Autorita Esterne
			if (lNotifiche[i].getAvvIdAvvocatoFascicoloSiep() == null
					&& lNotifiche[i].getIdCivilmenteObbligato() == null) {
				setRequestAttribute("notificaAlCondannato", lNotifiche[i]);
			}

			// Avvocati Siep
			if (lNotifiche[i].getAvvIdAvvocatoFascicoloSiep() != null) {
				lListAvvocatiSiep.add(lNotifiche[i]);
			}

			// Civilmente Obbligati
			if (lNotifiche[i].getIdCivilmenteObbligato() != null) {
				lListaObbligati.add(lNotifiche[i]);
			}
		}
		setRequestAttribute("listaNotAvvSiep", lListAvvocatiSiep);
		setRequestAttribute("lListaNotObbligati", lListaObbligati);

		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return PG_DETTAGLIO_PROVVEDIMENTO_ESTINZIONE_PP;
	}

}
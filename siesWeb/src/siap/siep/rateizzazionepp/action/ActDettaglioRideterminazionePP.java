package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il dettaglio della rideterminazione della pena pecuniaria
 * 
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActDettaglioRideterminazionePP extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

    	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// Annotazione Manuale
		IAnnotazioneManuale iam = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel amm = iam.ExRicercaAnnotazioniManualiByIdEvento(idEvento);
		setRequestAttribute("annotazioneManuale", amm);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				enm.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Ricerca i pagamenti per id Fascicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
		listaRateizzazioni = lRateCTRL
				.exRicercaRateizzazioniByIdFasc(enm.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

		// MAGISTRATO
		MagistratoModel lMag = enm.getMagistrato();
		setRequestAttribute("magistrato", lMag);

		// NOTIFICHE
		NotificaModel[] lNotifiche = enm.getNotifiche();
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

    	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return PG_DETTAGLIO_RIDETERMINAZIONE_PP;
	}

}
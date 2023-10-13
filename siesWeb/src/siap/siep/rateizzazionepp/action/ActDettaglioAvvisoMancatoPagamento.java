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
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il dettaglio dell'Avviso Mancato Pagamento
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActDettaglioAvvisoMancatoPagamento extends ActionSiap implements ICostantiRateizzazionePP {

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

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				enm.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Ricerca i pagamenti per id Fascicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		listaRateizzazioni = irpp.exRicercaRateizzazioniByIdEvento(idEvento);
		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

		// MAGISTRATO
		MagistratoModel mm = enm.getMagistrato();
		setRequestAttribute("magistrato", mm);

		// NOTIFICHE
		NotificaModel[] nmArray = enm.getNotifiche();
		List listaNotificheAvvocatiSiep = new ArrayList();
		List listaNotificheCivilmenteObbligati = new ArrayList();

		for (int i = 0; i < nmArray.length; i++) {
			// Autorita Esterne
			if (nmArray[i].getAvvIdAvvocatoFascicoloSiep() == null
					&& nmArray[i].getIdCivilmenteObbligato() == null) {
				setRequestAttribute("notificaAlCondannato", nmArray[i]);
			}

			// Avvocati Siep
			if (nmArray[i].getAvvIdAvvocatoFascicoloSiep() != null) {
				listaNotificheAvvocatiSiep.add(nmArray[i]);
			}

			// Civilmente Obbligati
			if (nmArray[i].getIdCivilmenteObbligato() != null) {
				listaNotificheCivilmenteObbligati.add(nmArray[i]);
			}
		}
		setRequestAttribute("listaNotAvvSiep", listaNotificheAvvocatiSiep);
		setRequestAttribute("listaNotObbligati", listaNotificheCivilmenteObbligati);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return PG_DETTAGLIO_AVVISO_MANCATO_PAGAMENTO;
	}

}
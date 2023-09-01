package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per la load della form di inserimento e modifica delle notifiche
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadNotificheRideterminazionePP extends ActionSiap implements ICostantiRateizzazionePP {

	// variabile per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = null;
		if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			String esitoCheck = checkEventi();
			if (esitoCheck != null)
				return esitoCheck;
		} else {
			BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			em = ie.ExRicercaEventoByKey(idEvento);
		}

		// Recupero l'evento di rideterminazione della pena
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(em.getIdEvento());
		setRequestAttribute("eventonotifica", enm);

		// Verifica per ogni destinatario se già registrate l'avvenuta notifica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				enm.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Carico i dati in form
		// Autorita che ha effettuato la notifica
		Option tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("comboAutNotifica", "" + tipoAutorita);

		// NOTIFICHE
		NotificaModel[] nmArray = enm.getNotifiche();
		List listAvvocatiSiep = new ArrayList();
		List listaObbligati = new ArrayList();
		String notifichePending = "NO";

		int contaAvvenute = 0;
		for (int i = 0; i < nmArray.length; i++) {
			if (nmArray[i].getDataAvvenutaNotifica() != null)
				contaAvvenute++;

			// Autorita Esterne
			if (nmArray[i].getAvvIdAvvocatoFascicoloSiep() == null
					&& nmArray[i].getIdCivilmenteObbligato() == null) {
				setRequestAttribute("notificaAlCondannato", nmArray[i]);
			}

			// Avvocati Siep
			if (nmArray[i].getAvvIdAvvocatoFascicoloSiep() != null) {
				listAvvocatiSiep.add(nmArray[i]);
			}

			// Civilmente Obbligati
			if (nmArray[i].getIdCivilmenteObbligato() != null) {
				listaObbligati.add(nmArray[i]);
			}

			if (!"03".equals(nmArray[i].getCodEsito()))
				notifichePending = "SI";
		}
		setRequestAttribute("listaNotAvvSiep", listAvvocatiSiep);
		setRequestAttribute("lListaNotObbligati", listaObbligati);
		setRequestAttribute("notifichePending", notifichePending);

		if (contaAvvenute > 0 && isRequestParameterNullEmptyObj("modifica"))
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.rateizzazionepp.action.ActDettaglioNotificaRideterminazionePP" + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + em.getIdEvento();

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return PG_LOAD_INSERIMENTO_NOTIFICHE_RPP;
	}

	private String checkEventi() throws F3BException {

		String returnPage = null;

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Recupero la lista degli eventi e i dati da visualizzare
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRideterminazioniPP = irpp
				.exRicercaEventoRateizzazionePP(fsm.getIdFascicoloSiep(), "rpp");

		if (listaRideterminazioniPP.isEmpty()) {
			// non ho trovato rideterminazioni pena: esco con errore
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione! Non sono presenti Rideterminazioni di Pena pecuniaria per questo fascicolo.");
			rt.setAction("siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzioneAltriPagamenti");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		} else if (listaRideterminazioniPP.size() > 0) {
			// Carico la pagina con la scelta delle rideterminazioni PP
			setRequestAttribute("listaRideterminazioniPP", listaRideterminazioniPP);
			setRequestAttribute("azioneChiamante", getClass().getName());
			return PG_LOAD_SELEZIONA_RIDETERMINAZIONE_PP;
		}

		return returnPage;
	}

}
package siap.siep.sanzionesostitutiva.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
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
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13: aggiunta classe
 *
 * Classe per la load della form di inserimento e mnodifica delle notifiche
 *
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadNotificheOrdineIngiunzione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Verifico esistenza Ordine di ingiunzione
		IEvento eventoCtrl = SICOLookupRemote.getEventoRemote();

		EventoModel lEveRicerca = new EventoModel();
		lEveRicerca.setCodTipoEvento("01");
		lEveRicerca.setCodTipoProvvedimento("06");
		lEveRicerca.setCodMotivo("0622");

		lEveRicerca.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveRicerca.setFlagDocumentoRegistrato("S");

		EventoModel lOrdineIngiunzione = eventoCtrl.ExRicercaUltimoTipoEventoByIdFascicolo(lEveRicerca);
		if (lOrdineIngiunzione == null || lOrdineIngiunzione.getIdEvento() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Sul Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è presente alcun ordine di ingiunzione valido. Impossibile procedere.");
			lRedirigi.setAction("siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Recupero l'ordine di ingiunzione
		EventoNotificaModel lEveNotMod = eventoCtrl
				.ExRicercaEventoNotificaByKey(lOrdineIngiunzione.getIdEvento());
		setRequestAttribute("ordineIngiunzione", lEveNotMod);

		// Verifica per ogni destinatario se già registrate l'avvenuta notifica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lEveNotMod.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Carico i dati in form
		// Autorita che ha effettuato la notifica
		Option lComboAutNotifica = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("comboAutNotifica", "" + lComboAutNotifica);

		// NOTIFICHE
		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		List lListAvvocatiSiep = new ArrayList();
		List lListaObbligati = new ArrayList();
		String notifichePending = "NO";

		int contaAvvenute = 0;
		for (int i = 0; i < lNotifiche.length; i++) {

			if (lNotifiche[i].getDataAvvenutaNotifica() != null)
				contaAvvenute++;

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

			if (!"03".equals(lNotifiche[i].getCodEsito()))
				notifichePending = "SI";
		}
		setRequestAttribute("listaNotAvvSiep", lListAvvocatiSiep);
		setRequestAttribute("lListaNotObbligati", lListaObbligati);
		setRequestAttribute("notifichePending", notifichePending);

		if (contaAvvenute > 0 && isRequestParameterNullEmptyObj("modifica"))
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sanzionesostitutiva.action.ActDettaglioNotificaOrdineIngiunzione";

		// 2023/05/02 - a seguito collaudo si richiede il blocco della registrazione delle notifiche in caso
		// in cui
		// non siano stati emessi i bollettini. Per problema sull'aggiornamento della data scadenza
		// IBollettinoPagopa lBollCtrl = SIEPLookupRemote.getBollettinoPagopaRemote();
		// Vector<BollettinoPagopaModel> listaBollettini =
		// lBollCtrl.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep (lFascMod.getIdFascicoloSiep(), null);
		// if (listaBollettini == null || listaBollettini.size() == 0) {
		// RedirectTo lRedirigi = new RedirectTo();
		// lRedirigi.setPage(IWebConstants.PG_MAIN);
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		// "Nessun bollettino generato per il Procedimento corrente. Per poter procedere alla registrazione
		// delle notifiche"
		// + " e' necessario prima produrre i relativi bollettini.");
		// lRedirigi.setAction("siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzione&"
		// + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		//
		// return IWebConstants.PG_MESSAGE;
		// }

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return PG_LOAD_INSERIMENTO_NOTIFICHE_OI;
	}

}
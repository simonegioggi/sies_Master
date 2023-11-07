package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
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
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * MEV2023-33
 *
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciRinnovoRicercheOIPP extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Controlli preliminari all'inserimento di un nuovo evento
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non e' stato Validato. Impossibile inserire il Rinnovo Ricerche!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// Verifico se presenti più Ordini di Ingiunzione
		// se assenti - errore
		// se presente solo uno lo seleziono
		// se presente più di uno restituisco la pagina di scelta
		BigDecimal idEvento = new BigDecimal(0);
		if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			// Recupero la lista degli eventi e i dati da visualizzare
			IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();

	    Vector<EventoRateizzazionePPModel> listaOrdiniIngiunzione = irpp
	        .exRicercaEventoRateizzazionePP(lFascMod.getIdFascicoloSiep(), "ALL");			
			
			if (listaOrdiniIngiunzione.isEmpty()) {
				// non ho trovato ordini di ingiunzione esco con errore
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione! Non sono presenti ordini di ingiunzione per questo fascicolo.");
				rt.setAction("siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzione");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
				return IWebConstants.PG_MESSAGE;
			} else if (listaOrdiniIngiunzione.size() > 0) { // n.b per test deve essere >1
				// Carico la pagina con la scelta degli OI
				setRequestAttribute("listaOrdiniIngiunzione", listaOrdiniIngiunzione);
				setRequestAttribute("azioneChiamante", this.getClass().getName());

				return PG_LOAD_SELEZIONA_ORDINE_INGIUNZIONE;
			}
			else {
				idEvento = listaOrdiniIngiunzione.elementAt(0).getEvento().getIdEvento();
			}			
		} else {
			// Ho selezionato l'evento dalla lista, carico la pagina
			// Verifica per ogni destinatario se già registrate l'avvenuta notifica
			idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		}
		
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(idEvento);
			setRequestAttribute("ordineIngiunzione", lEveNotMod);

			// Verifica se presente il Verbale vane ricerche che punta l'ordine di ingiunzione
			EventoModel lEveVerbale = new EventoModel();
			lEveVerbale.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lEveVerbale.setCodTipoEvento("07");
			lEveVerbale.setCodTipoProvvedimento("17");
			lEveVerbale.setCodMotivo("0313");
			Vector <EventoModel> listaEventiVerbali = new Vector <EventoModel>();
			try {
				listaEventiVerbali = lCtrlEvento.ExRicercaEventoTipoEveTipoProvMot(lEveVerbale, "S");
			} catch (F3BException e) {
				if (F3BException.USER_MESSAGE == e.getErrorCode()) {
				  siesLogger.warn("Nessun Verbale Vane Ricerche Registrato per l'ordine di Ingiunzione selezionato");
					/*throw new F3BException(F3BException.USER_MESSAGE,
							"Nessun Verbale Vane Ricerche Registrato per l'ordine di Ingiunzione selezionato");*/
				}
			}

			EventoModel verbaleVR = null;
			for (EventoModel eveVerbale : listaEventiVerbali) {
				if (eveVerbale.getEveIdEvento().compareTo(lEveNotMod.getEvento().getIdEvento()) == 0) {
					verbaleVR = eveVerbale;
					break;
				}
			}

			// Recupero il Verbale vane ricerche per la visualizzazione
			IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();
			VerbaleModel lVermod = new VerbaleModel();
			if (verbaleVR != null)
				lVermod = lCtrlVer.ExRicercaVerbaleByCodTipoIdEvento(verbaleVR.getIdEvento(), "02");

			//
			NotificaModel lNotificaEsecuzione = null;
			for (int i = 0; i < lEveNotMod.getNotifiche().length; i++) {
				NotificaModel lNotMod = lEveNotMod.getNotifiche()[i];
				if (lNotMod.getCodTipoNotifica().equals("E")) {
					lNotificaEsecuzione = lNotMod;
				}
			}

			// Recupero eventuali RINNOVI già inseriti da visualizzare in elenco e collegati alla notifica per
			// l'esecuzione
			Vector<RinnovoModel> lListaRinnovi = new Vector<RinnovoModel>();
			String[] lTipoRinno = { "R", "N", "A" };
			IRinnovo lCtrlRinnovi = SIEPLookupRemote.getRinnovoRemote();
			lListaRinnovi = lCtrlRinnovi.ExRicercaRinnovoIdNotificaCodTipoRinnovoStato(
					lNotificaEsecuzione.getIdNotifica(), lTipoRinno, null);

			//
			Option lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita());

			setRequestAttribute("notifica", lNotificaEsecuzione);
			setRequestAttribute("verbale", lVermod);
			setRequestAttribute("listaRinnovi", lListaRinnovi);
			setRequestAttribute("tipoAutoritaAltra", lOptionAutoritaAltra.toString());

			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
			IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
					lEveNotMod.getEvento().getFasSieIdFascicoloSiep());
			setRequestAttribute("posizioneluogoaltra", lPos);
		

		return PG_LOAD_INSERISCI_RINNOVO_RICERCHE;
	}

	/**
	 * 
	 * @deprecated
	 */
	private String checkEventi(BigDecimal idEvento) throws Exception {

		String returnPage = null;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Recupero la lista degli eventi e i dati da visualizzare
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		
		// 2023.09.26 - si includono nella ricerca anche i codici
		//Vector<EventoRateizzazionePPModel> listaOrdiniIngiunzione = irpp
		//		.exRicercaEventoRateizzazionePP(lFascMod.getIdFascicoloSiep(), "");
    Vector<EventoRateizzazionePPModel> listaOrdiniIngiunzione = irpp
        .exRicercaEventoRateizzazionePP(lFascMod.getIdFascicoloSiep(), "ALL");
    // 2023.09.26 - FINE
			
		
		if (listaOrdiniIngiunzione.isEmpty()) {
			// non ho trovato ordini di ingiunzione esco con errore
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione! Non sono presenti ordini di ingiunzione per questo fascicolo.");
			rt.setAction("siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzione");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		} else if (listaOrdiniIngiunzione.size() > 1) { // n.b per test deve essere >1
			// Carico la pagina con la scelta degli OI
			setRequestAttribute("listaOrdiniIngiunzione", listaOrdiniIngiunzione);
			setRequestAttribute("azioneChiamante", this.getClass().getName());

			return PG_LOAD_SELEZIONA_ORDINE_INGIUNZIONE;
		}
		else {
			idEvento = listaOrdiniIngiunzione.elementAt(0).getEvento().getIdEvento();
			siesLogger.debug("idEvento = "+idEvento);
		}
		
		return returnPage;
	}

}

package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.produzioneatti.action.ICostantiProduzioneAtti;

public class ActInserisciEsitoParereInamm extends ActionSiap implements ICostantiRichiestaAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = lCtrl.ExRicercaEventoEsitoParereInamm(lIdEvento);

		// Data Emissione Parere
		Date lDataEmissione2 = getRequestDateParameter(ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2,
				ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2,
				ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2);
		String lEsitoParere = "Favorevole";
		String Ck_Parere = getRequestStringParameter(ICostantiProduzioneAtti.CAMPO_ESITO_PARERE);

		if (Ck_Parere.equals("1")) {
			lEsitoParere = "Contrario";
		}
		if (Ck_Parere.equals("2")) {
			lEsitoParere = "Parzialmente favorevole";
		}
		if (Ck_Parere.equals("3")) {
			lEsitoParere = "Non esprime parere";
		}

		// Preleva dalla sessione il FascicoloGPModel.
		// FascicoloSigeEstesoModel lFascicoloGPModel
		// =(FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

		// Prepara il model EventoNotifica.
		// Imposta i dati necessari per la gestione dell'evento.
		EventoModel lEveNot = new EventoModel();
		lEveNot = lEve;

		String CodTipoEvento = "05";
		lEveNot.setCodTipoEvento(CodTipoEvento);

		// Vincenzo 19/01/2007 e 09/05/2007
		lEveNot.setIdEvento(lIdEvento);

		if (lEsitoParere.compareTo("Favorevole") == 0) {
			lEveNot.setCodEsito("0751");
		} else if (lEsitoParere.compareTo("Contrario") == 0) {
			lEveNot.setCodEsito("0752");
		} else if (lEsitoParere.compareTo("Parzialmente favorevole") == 0) {
			lEveNot.setCodEsito("0753");
		} else if (lEsitoParere.compareTo("Non esprime parere") == 0) {
			lEveNot.setCodEsito("0754");
		} else {
			lEveNot.setCodEsito("0750");
		}

		lEveNot.setDataRicezioneAtti(lDataEmissione2);

		lEveNot.setCodUfficioEmittente(lCodiceUfficio);
		lEveNot.setCodLuogoEmittente(lCodComune);

		lEveNot.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEveNot.setCodUfficioAggiornamento(lCodiceUfficio);
		lEveNot.setDataAggiornamento(DateUtils.getSysDate());

		// Chiamata al Controller
		// Update dell'evento
		IEvento lCtrl2 = SICOLookupRemote.getEventoRemote();
		lCtrl2.ExModificaEvento(lEveNot);

		// Prepara la pagina di destinazione, precisamente punta
		// all'azione di dettaglio.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sige.richiestaatti.action.ActLoadDettaglioEsitoParereInamm");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEveNot.getIdEvento());
		if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			lPage.setParameter(IWebConstants.LINK_RITORNO, "10");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		return "" + lPage;
	}

}
package siap.sius.misuresicurezzarichiestaatti.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.richiestaatti.action.ActInserisciRicAtti;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciInfCondottaLFE extends ActInserisciRicAtti implements ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di: recupero dati in request, creazione evento e notifiche.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		// String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		// Preleva dati dalla form.
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);
		String[] lSedi = getRequestStringParameters(CAMPO_SEDE);
		String[] lDestinatari = getRequestStringParameters(CAMPO_COD_DESTINATARIO);
		String[] lCampiNoteReq = getRequestStringParameters(CAMPO_AGGIUNTIVO);
		String[] lNote = getRequestStringParameters(CAMPO_NOTE);
		String[] lTipoDest = getRequestStringParameters("tipoDest"); // Tipo di destinatario

		// Preleva dalla sessione il FascicoloGPModel.
		FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva dal FascicoloGPModel.
		FascicoloSiusModel lFasSius = lFascicoloGPModel.getFascicoloSiusModel();

		// Prepara il model EventoNotifica.
		// Imposta i dati necessari per la gestione dell'evento.
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.getEvento().setCodTipoEvento(ICostantiRichiestaAtti.CODTIPOEVENTO);
		lEveNot.getEvento().setDataEmissione(lDataEmissione);

		lEveNot.getEvento().setCodMotivo("0567"); // CodMotivo - Richiesta Condotta LFE
		lEveNot.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEveNot.getEvento().setCodLuogoEmittente(lCodComune);
		lEveNot.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEveNot.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEveNot.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveNot.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveNot.getEvento().setCodEsito("-");
		lEveNot.getEvento().setCodTipoProvvedimento("-");
		lEveNot.getEvento().setCodLuogoDestinatario("-");
		lEveNot.getEvento().setCodTipoUfficioDestinatario("-");
		lEveNot.getEvento().setFasSiuIdFascicoloSius(lFasSius.getIdFascicoloSius());

		// Imposta i dati necessari per la gestione delle Notifica
		Collection lNotifiche = new ArrayList();
		// Cicla sui tipi di destinatari.
		for (int x = 0; x < lTipoDest.length; x++) {
			NotificaModel lNotifica = null;

			if (lTipoDest[x].equalsIgnoreCase("AUT_EXT")) // Gestione Autorità Esterna
			{
				if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(">>> Lavorazione TipoDest : " + lTipoDest[x]);

					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);
					lNotifica.setUffCodUfficio("-");
					// Preleva e verifica il codice comune della sede inputata.
					String lCodComuneSede = getCodComuneByDescr(lSedi[x]).getCodComune();
					// Popola il model dell'autorità esterna
					AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
					lAutorita.setCodTipoAutorita(lDestinatari[x]);
					lAutorita.setCodSede(lCodComuneSede);
					lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
					lAutorita.setCodUfficioInserimento(lCodiceUfficio);
					lAutorita.setDataInserimento(DateUtils.getSysDate());

					// Aggiunge il model Autorità Esterna alla Notifica.
					lNotifica.setAutoritaEsterna(lAutorita);

					// Inserisce le Note se sono diverse dal vuoto.
					if (!lNote[x].equals(""))
						lNotifica.setNote(lNote[x]);
				}
			}
			// Inserisce la notifica nel vettore delle notifiche.
			lNotifiche.add(lNotifica);
		}

		// Inserisce le notifiche nell'eventoModel, prelevando
		// un array di oggetti dal vettore.
		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// Popola il model campo note aggiuntive
		if (lCampiNoteReq != null) {
			Collection lCampiNote = new ArrayList();
			int lSize = lCampiNoteReq.length;
			// Cicla per i cmapi note aggiuntive.
			for (int x = 0; x < lSize; x++) {
				// Se il campo nota aggiuntivo è valorizzato, crea il model
				// impostatndo i valori appropriati.
				if (!lCampiNoteReq[x].equals("")) {
					CampoNotaModel lCampoNotaMod = new CampoNotaModel();
					lCampoNotaMod.setDescr(lCampiNoteReq[x]);
					lCampoNotaMod.setCodOperatoreInserimento(lCodiceOperatore);
					lCampoNotaMod.setCodUfficioInserimento(lCodiceUfficio);
					lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
					lCampiNote.add(lCampoNotaMod);
				}
			}
			// Se esistono campi di note aggiuntive
			// inserisce il contenuto del vettore nell'eventoModel
			if (lCampiNote.size() != 0)
				lEveNot.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
		}

		// Chiamata al Controller, per l'inserimento evento.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveNot = lCtrl.ExInserisciEventoNotifica(lEveNot);

		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.misuresicurezzarichiestaatti.action.ActLoadDettaglioMisureSicurezza");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEveNot.getEvento().getIdEvento());
		lPage.setParameter(IWebConstants.LINK_RITORNO, "10");

		return lPage.toString();

		// return getPaginaDettaglio ( lEveNot.getEvento().getIdEvento() );
	}

	/**
	 * Metodo private che ha la responsabilità di creare e popolare la notifica model nelle parti comuni.
	 * <p>
	 * 
	 * @return Istanza di NotificaModel.
	 */
	private NotificaModel creaNotifica(Date lDataEmissione, String lCodiceOperatore, String lCodiceUfficio) {

		NotificaModel lNotifica = new NotificaModel();
		lNotifica.setCodTipoNotifica(ICostantiRichiestaAtti.CODTIPONOTIFICA);
		lNotifica.setDataInvio(lDataEmissione);
		lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
		lNotifica.setDataInserimento(DateUtils.getSysDate());
		lNotifica.setCodUfficioInserimento(lCodiceUfficio);
		lNotifica.setCodEsito("-");

		return lNotifica;
	}

}
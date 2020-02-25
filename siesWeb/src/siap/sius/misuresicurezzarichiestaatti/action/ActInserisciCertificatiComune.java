package siap.sius.misuresicurezzarichiestaatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.istruttoria.action.ICostantiIstruttoria;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.richiestaatti.action.ActInserisciRicAtti;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;

public class ActInserisciCertificatiComune extends ActInserisciRicAtti implements ICostantiRichiestaAtti {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();

		// Preleva dati dalla form.
		String lDescrComune = getRequestStringParameter(CAMPO_SEDE);
		String lNote = getRequestStringParameter(ICostantiIstruttoria.TIPO_DOCUMENTO);
		String lCodTipoUfficioDest = getRequestStringParameter(CAMPO_COD_DESTINATARIO);
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);
		String[] lCampiNoteReq = getRequestStringParameters(CAMPO_AGGIUNTIVO);
		String[] lCampiNoteN = getRequestStringParameters(CAMPO_NOTE);
		int lSize = 0;
		int lSizeN = 0;
		int x = 0;

		// Controlla e preleva il codice comune di sede.
		// Tale metodo ereditato, effettua un'accesso alla
		// base dati ove verifica e ritorna il codice del comune.
		String lCodSede = getCodComuneByDescr(lDescrComune).getCodComune();

		// Preleva dalla sessione il FascicoloGPModel.
		FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva dal FascicoloGPModel
		FascicoloSiusModel lFasSius = lFascicoloGPModel.getFascicoloSiusModel();

		// Prepara il model EventoNotifica.
		// Imposta i dati necessari per la gestione dell'evento.
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.getEvento().setCodTipoEvento(ICostantiRichiestaAtti.CODTIPOEVENTO);
		lEveNot.getEvento().setDataEmissione(lDataEmissione);
		lEveNot.getEvento().setCodMotivo("0569"); // Certificati Comune
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
		NotificaModel lNotifica = new NotificaModel();

		lNotifica.setCodTipoNotifica(ICostantiRichiestaAtti.CODTIPONOTIFICA);
		lNotifica.setDataInvio(lDataEmissione);
		lNotifica.setNote(lNote);
		lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
		lNotifica.setDataInserimento(DateUtils.getSysDate());
		lNotifica.setCodUfficioInserimento(lCodiceUfficio);
		lNotifica.setCodEsito("-");
		lNotifica.setUffCodUfficio("-");
		// Crea Model Autorità Esterna
		AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
		lAutorita.setCodTipoAutorita(lCodTipoUfficioDest);
		lAutorita.setCodSede(lCodSede);
		lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
		lAutorita.setCodUfficioInserimento(lCodiceUfficio);
		lAutorita.setDataInserimento(DateUtils.getSysDate());

		// Aggiunge il model Autorità Esterna alla Notifica
		lNotifica.setAutoritaEsterna(lAutorita);

		// Crea un oggetto NotificaModel, come
		// Array di Classi e inserisce la notifica nell'array
		NotificaModel lNotifiche[] = new NotificaModel[1];
		lNotifiche[0] = lNotifica;

		// Impostiamo l'array di notifiche nell'evento model
		lEveNot.setNotifiche(lNotifiche);

		Vector lCampiNote = new Vector();
		// Popola il model campo note aggiuntive
		if (lCampiNoteReq != null) {
			lSize = lCampiNoteReq.length;

			for (x = 0; x < lSize; x++) {
				if (!lCampiNoteReq[x].equals("")) {
					CampoNotaModel lCampoNotaMod = new CampoNotaModel();

					DecodificheModel lModel = new DecodificheModel();
					IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
					lModel.setContesto("TIPO_CERTIFICATO");
					Collection lCertificati = lDecodifiche.ExRicercaDecodifiche(lModel);

					lCampoNotaMod.setDescr(
							"Certificato " + DecodificheUtils.getDescbyCode(lCertificati, lCampiNoteReq[x]));
					// lCampoNotaMod.setDescr( "Certificato "+lCampiNoteReq[x] );

					lCampoNotaMod.setCodOperatoreInserimento(lCodiceOperatore);
					lCampoNotaMod.setCodUfficioInserimento(lCodiceUfficio);
					lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
					lCampiNote.add(lCampoNotaMod);
				}
			}

		}

		// Lettura Campo Nota + Altro Certificato
		if (lCampiNoteN != null) {
			lSizeN = lCampiNoteN.length;

			for (int z = 0; z < lSizeN; z++) {
				if (!lCampiNoteN[z].equals("")) {
					CampoNotaModel lCampoNotaMod = new CampoNotaModel();

					lCampoNotaMod.setDescr(lCampiNoteN[z]);

					lCampoNotaMod.setCodOperatoreInserimento(lCodiceOperatore);
					lCampoNotaMod.setCodUfficioInserimento(lCodiceUfficio);
					lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
					lCampiNote.add(lCampoNotaMod);
				}
			}

		}
		// Se esistono campi di note aggiuntive
		// inserisce il contenuto del vettore nell'eventoModel
		if (lCampiNoteN != null) {
			if (lCampiNote.size() != 0)
				lEveNot.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
		}

		// Chiamata al Controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveNot = lCtrl.ExInserisciEventoNotifica(lEveNot);

		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.misuresicurezzarichiestaatti.action.ActLoadDettaglioMisureSicurezza");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEveNot.getEvento().getIdEvento());
		lPage.setParameter(IWebConstants.LINK_RITORNO, "10");

		return lPage.toString();
		// return getPaginaDettaglio( lEveNot.getEvento().getIdEvento());
	}

}
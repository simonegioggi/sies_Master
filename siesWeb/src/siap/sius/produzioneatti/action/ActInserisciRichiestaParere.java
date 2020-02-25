package siap.sius.produzioneatti.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.motivazionedecreto.action.ActInserisciMotivazioneDecretoInammissibilita;
import siap.sius.motivazionedecreto.controller.IMotivazioneDecreto;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciRichiestaParere extends ActInserisciMotivazioneDecretoInammissibilita
		implements ICostantiProduzioneAtti {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();

		// Preleva dati dalla form.
		String lDescrComune = getRequestStringParameter(CAMPO_SEDE);
		// String lNote = getRequestStringParameter( CAMPO_NOTE );
		String lNote = "";
		String lCodTipoUfficioDest = getRequestStringParameter(CAMPO_COD_DESTINATARIO);
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);
		String[] lCampiNoteReq = null;

		// Lettura Campo Note
		if (!isRequestParameterNullObj(CAMPO_AGGIUNTIVO)) {
			lCampiNoteReq = getRequestStringParameters(CAMPO_AGGIUNTIVO);
			setRequestAttribute("campiNote", lCampiNoteReq);
		}
		// Lettura Motivazioni Decreto Inammissibilità
		MotivazioneDecretoModel[] lMotivazioni = super.letturaMotivazioni();

		// Controlla e preleva il codice comune di sede.
		// Tale metodo ereditato, effettua un'accesso alla base dati ove verifica e ritorna il codice del
		// comune.
		/* String lCodSede = */getCodComuneByDescr(lDescrComune).getCodComune();

		// Preleva il codice d'ufficio per la coppia comune e codice tipo ufficio.
		String lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioDest, lDescrComune);

		// Preleva dalla sessione il FascicoloGPModel.
		FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva dal FascicoloGPModel
		FascicoloSiusModel lFasSius = lFascicoloGPModel.getFascicoloSiusModel();

		// Prepara il model EventoNotifica.
		// Imposta i dati necessari per la gestione dell'evento.
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.getEvento().setCodTipoEvento(ICostantiProduzioneAtti.CODTIPOEVENTO);
		lEveNot.getEvento().setDataEmissione(lDataEmissione);
		lEveNot.getEvento().setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));
		lEveNot.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEveNot.getEvento().setCodLuogoEmittente(lCodComune);
		lEveNot.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEveNot.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEveNot.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveNot.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveNot.getEvento().setCodEsito("-");
		lEveNot.getEvento().setCodTipoProvvedimento("11");
		lEveNot.getEvento().setCodLuogoDestinatario("-");
		lEveNot.getEvento().setCodTipoUfficioDestinatario("-");
		lEveNot.getEvento().setFasSiuIdFascicoloSius(lFasSius.getIdFascicoloSius());

		// Imposta i dati necessari per la gestione delle Notifica
		NotificaModel lNotifica = new NotificaModel();
		lNotifica.setCodTipoNotifica(ICostantiProduzioneAtti.CODTIPONOTIFICA);
		lNotifica.setDataInvio(lDataEmissione);
		lNotifica.setNote(lNote);
		lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
		lNotifica.setDataInserimento(DateUtils.getSysDate());
		lNotifica.setCodUfficioInserimento(lCodiceUfficio);
		lNotifica.setCodEsito("-");
		lNotifica.setUffCodUfficio(lCodUfficioDest);

		// Crea un oggetto NotificaModel, come
		// Array di Classi e inserisce la notifica nell'array
		NotificaModel lNotifiche[] = new NotificaModel[1];
		lNotifiche[0] = lNotifica;

		// Impostiamo l'array di notifiche nell'evento model
		lEveNot.setNotifiche(lNotifiche);

		// Popola il model campo note aggiuntive
		if (lCampiNoteReq != null) {
			Vector lCampiNote = new Vector();
			int lSize = lCampiNoteReq.length;

			for (int x = 0; x < lSize; x++) {
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

		// Chiamata al Controller
		IMotivazioneDecreto lCtrl = SIUSLookupRemote.getMotivazioneDecretoRemote();
		lEveNot = lCtrl.ExInserisciRichiestaParere(lEveNot, lMotivazioni);

		// Prepara la pagina di destinazione, precisamente punta
		// all'azione di dettaglio.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.produzioneatti.action.ActLoadDettaglioRichiestaParere");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEveNot.getEvento().getIdEvento());

		return "" + lPage;
	}

}
package siap.sius.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;

public class ActInserisciOrdinDecrAltroTdSUdS extends ActInserisciRicAtti implements ICostantiRichiestaAtti {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();

		// Preleva i campi dalla request
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);
		String[] lSedi = getRequestStringParameters(CAMPO_SEDE);
		String[] lDestinatari = getRequestStringParameters(CAMPO_COD_DESTINATARIO);
		String[] lCampiNoteReq = getRequestStringParameters(CAMPO_AGGIUNTIVO);
		int lSize = 0;

		// Preleva dati dalla Form
		String lDescrComune = getRequestStringParameter(CAMPO_SEDE);
		String lCodTipoUfficioDest = getRequestStringParameter(CAMPO_COD_DESTINATARIO);

		// Controlla e preleva il codice comune di sede.
		// Tale metodo ereditato, effettua un'accesso alla
		// base dati ove verifica e ritorna il codice del comune.
		/* String lCodSede = */getCodComuneByDescr(lDescrComune).getCodComune();

		// Preleva il codice d'ufficio per la coppia comune e codice
		// tipo ufficio.
		String lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioDest, lDescrComune);

		// Preleva dalla sessione il FascicoloGPModel.
		FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva dal FascicoloGPModel
		FascicoloSiusModel lFasSius = lFascicoloGPModel.getFascicoloSiusModel();

		// Prepara il model EventoNotifica.
		// Imposta i dati necessari per la gestione dell'evento.
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.getEvento().setCodTipoEvento(ICostantiRichiestaAtti.CODTIPOEVENTO);
		lEveNot.getEvento().setDataEmissione(lDataEmissione);
		lEveNot.getEvento().setCodMotivo("0543");
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

		// INIZIO: MEV_9 (D.lgs. 123/2018)
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_RESTITUZIONE)) {
			Date lDataRestituzioneAtti = getRequestDateParameter(CAMPO_ANNO_DATA_RESTITUZIONE
					                                           , CAMPO_MESE_DATA_RESTITUZIONE
				                                               , CAMPO_GIORNO_DATA_RESTITUZIONE  );
			lEveNot.getEvento().setDataRestituzioneAi(lDataRestituzioneAtti);
		}
		// FINE: MEV_9 (D.lgs. 123/2018)
		
		// Prepara le notifiche.
		Vector lNotifiche = new Vector();
		lSize = lDestinatari.length;

		for (int x = 0; x < lSize; x++) {
			if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
				/* String lCodComuneSede = */getCodComuneByDescr(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica(ICostantiRichiestaAtti.CODTIPONOTIFICA);
				lNotifica.setDataInvio(lDataEmissione);
				lNotifica.setNote("");
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio(lCodUfficioDest);
				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);

			}
		}

		// Inserisce le notifiche nell'eventoModel, prelevando
		// un array di oggetti dal vettore.
		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// Popola il model campo note aggiuntive
		if (lCampiNoteReq != null) {
			Vector lCampiNote = new Vector();
			lSize = lCampiNoteReq.length;

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
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveNot = lCtrl.ExInserisciEventoNotifica(lEveNot);

		return getPaginaDettaglio(lEveNot.getEvento().getIdEvento());
	}

}
package siap.siep.penaaccessoria.action;

/**
* <p>Title: ActRichiestaGE</p>
* <p>Description: Classe Action per l'esecuzione della Pena Accessoria</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
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
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRichiestaGE extends ActionSiap implements ICostantiPenaAccessoria {

	/**
	 * Azione di Inserimento dell'Evento correlato alla Richiesta al GE per Pena Accessoria.
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione.
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();

		// Preleva dati dalla form.
		Date lDataRichiesta = getRequestDateParameter(CAMPO_ANNO_DATA_RICHIESTAGE,
				CAMPO_MESE_DATA_RICHIESTAGE, CAMPO_GIORNO_DATA_RICHIESTAGE);
		String lCodTipoRichiestaGE = getRequestStringParameter(CAMPO_COD_TIPO_RICHIESTA_GE);

		String[] lSedi = getRequestStringParameters(CAMPO_SEDE);
		String[] lDestinatari = getRequestStringParameters(CAMPO_COD_DESTINATARIO);
		String[] lCampiNoteReq = getRequestStringParameters(CAMPO_AGGIUNTIVO);
		String[] lNote = getRequestStringParameters(CAMPO_NOTE);
		int lSize = 0;

		// Preleva dalla sessione il Fascicolo SIEP.
		FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Prepara il model EVENTONOTIFICA.
		// Imposta i dati necessari per la gestione dell'evento.
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.getEvento().setCodTipoEvento("17"); // Richiesta al GE per Pena Accessoria
		lEveNot.getEvento().setDataEmissione(lDataRichiesta);
		lEveNot.getEvento().setCodMotivo("5" + getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA));
		lEveNot.getEvento().setDescrMotivo(getRequestStringParameter(CAMPO_DESCR_TIPO_PENA_ACCESSORIA));
		lEveNot.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEveNot.getEvento().setCodLuogoEmittente(lCodComune);
		lEveNot.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEveNot.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEveNot.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveNot.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEveNot.getEvento().setCodEsito("-");

		// lEveNot.getEvento().setCodTipoProvvedimento("33"); // Richiesta al GE per Pena Accessoria

		// Recupero HIGH_VALUE per associazione TIPO_PROVVEDIMENTO a TIPO_RICHIESTA_GE
		Collection lColTipoRichiestaGE = null;
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_RICHIESTA_GE");
		lColTipoRichiestaGE = lDecodifiche.ExRicercaDecodifiche(lModel);
		String lCodProvvXRichiestaGE = DecodificheUtils.getCodAltebyCode(lColTipoRichiestaGE,
				lCodTipoRichiestaGE);
		lEveNot.getEvento().setCodTipoProvvedimento(lCodProvvXRichiestaGE); // Tipo Richiesta al GE per Pena
																			// Accessoria
		lEveNot.getEvento().setCodLuogoDestinatario("-");
		lEveNot.getEvento().setCodTipoUfficioDestinatario("-");
		lEveNot.getEvento().setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
		lEveNot.getEvento()
				.setPenAccIdPenaAccessoria(getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA));
		lEveNot.getEvento().setFlagVideoSiep("S");

		// Imposta i dati necessari per la gestione delle Notifica
		Vector lNotifiche = new Vector();
		lSize = lDestinatari.length;

		if (!lDestinatari[0].equals("-") && !lSedi[0].equals("")) {
			// Preleva il codice d'ufficio per la coppia comune e codice tipo ufficio.
			String lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune(lDestinatari[0], lSedi[0]);

			NotificaModel lNotifica = new NotificaModel();

			lNotifica.setCodTipoNotifica(ICostantiPenaAccessoria.CODTIPONOTIFICA);
			lNotifica.setDataInvio(lDataRichiesta);
			if (!lNote[0].equals("")) {
				lNotifica.setNote(lNote[0]);
			}
			lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
			lNotifica.setDataInserimento(DateUtils.getSysDate());
			lNotifica.setCodUfficioInserimento(lCodiceUfficio);
			lNotifica.setCodEsito("-");
			lNotifica.setUffCodUfficio(lCodUfficioDest);

			// Aggiunge il model delle notifiche al vettore.
			lNotifiche.add(lNotifica);
		}

		for (int x = 1; x < lSize; x++) {
			if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica(ICostantiPenaAccessoria.CODTIPONOTIFICA);
				lNotifica.setDataInvio(lDataRichiesta);
				if (!lNote[x].equals("")) {
					lNotifica.setNote(lNote[x]);
				}
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");

				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatari[x]);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
				lAutorita.setCodUfficioInserimento(lCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());

				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
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

		// Aggiorna la Pena Accessoria nel campo nuovo tipo P.A. SOLO SE C'E' SOSTITUZIONE.
		String lCodTipoPenaAccessoriaNuovo = "-";
		if (lEveNot.getEvento().getCodTipoProvvedimento().compareTo("45") == 0
				|| lEveNot.getEvento().getCodTipoProvvedimento().compareTo("46") == 0) {
			PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
			lPenMod.setIdPenaAccessoria(getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA));

			if (isRequestParameterNullObj(CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO))
				lCodTipoPenaAccessoriaNuovo = "-";
			else
				lCodTipoPenaAccessoriaNuovo = getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO)
						.toUpperCase();

			lPenMod.setCodNuovoTipoPenaAccessoria(lCodTipoPenaAccessoriaNuovo);
			lPenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lPenMod.setDataAggiornamento(DateUtils.getSysDate());
			lPenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());

			// Chiama il controller per aggiornare la Pena Accessoria (Dati dell'ordinanza).
			IPenaAccessoria lCtrl2 = SIEPLookupRemote.getPenaAccessoriaRemote();
			lPenMod = lCtrl2.ExModificaCodNuovoTipoPA(lPenMod);
		}

		// Prepara la pagina di destinazione, precisamente punta
		// all'azione di dettaglio.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.siep.penaaccessoria.action.ActLoadDettaglioRichiestaGE");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEveNot.getEvento().getIdEvento());

		return lPage.toString();
	}
}

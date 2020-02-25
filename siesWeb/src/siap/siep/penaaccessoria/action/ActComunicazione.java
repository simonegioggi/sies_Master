package siap.siep.penaaccessoria.action;

/**
* <p>Title: ActComunicazione</p>
* <p>Description: Classe Action per la Comunicazione per Pena Accessoria</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActComunicazione extends ActionSiap implements ICostantiPenaAccessoria {

	/**
	 * Azione di Inserimento dell'Evento correlato alla PenaAccessoria da eseguire.
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione.
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();

		// Preleva dati dalla form.
		Date lDataComunicazione = getRequestDateParameter(CAMPO_ANNO_DATA_COMUNICAZIONE,
				CAMPO_MESE_DATA_COMUNICAZIONE, CAMPO_GIORNO_DATA_COMUNICAZIONE);
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
		lEveNot.getEvento().setCodTipoEvento("18"); // Comunicazione Modifica Esecuzione Pena Accessoria
		lEveNot.getEvento().setDataEmissione(lDataComunicazione);
		lEveNot.getEvento().setCodMotivo("5" + getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA));
		lEveNot.getEvento().setDescrMotivo(getRequestStringParameter(CAMPO_DESCR_TIPO_PENA_ACCESSORIA));
		lEveNot.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEveNot.getEvento().setCodLuogoEmittente(lCodComune);
		lEveNot.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEveNot.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEveNot.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveNot.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveNot.getEvento().setCodEsito("-");

		// lEveNot.getEvento().setCodTipoProvvedimento("32"); // Comunicazione Esecuzione PA
		// Recupero HIGH_VALUE per associazione TIPO_PROVVEDIMENTO a TIPO_COMUNICAZIONE_PA
		Collection lColTipoComunicazionePA = null;
		String lCodTipoComunicazionePA = getRequestStringParameter(CAMPO_COD_TIPO_COMUNICAZIONE);
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_COMUNICAZIONE_PA");
		lColTipoComunicazionePA = lDecodifiche.ExRicercaDecodifiche(lModel);
		String lCodProvvXComunicazionePA = DecodificheUtils.getCodAltebyCode(lColTipoComunicazionePA,
				lCodTipoComunicazionePA);
		lEveNot.getEvento().setCodTipoProvvedimento(lCodProvvXComunicazionePA); // Tipo Comunicazione per Pena
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

		for (int x = 0; x < lSize; x++) {
			if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
				String lCodComuneSede = getCodComuneByDescr(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica(ICostantiPenaAccessoria.CODTIPONOTIFICA);
				lNotifica.setDataInvio(lDataComunicazione);
				if (!lNote[x].equals("")) {
					lNotifica.setNote(lNote[x]);
				}
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				// lNotifica.setUffCodUfficio("-");
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

		String lCodTipoUfficioPA = "-";
		String lDescrComunePA = "-";
		// Codice Nuovo Tipo Pena Accessoria
		String lCodTipoPenaAccessoriaNuovo = "-";
		boolean esistePAsostitutiva = false;

		try {
			// Chiamata ai Controller
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveNot = lCtrl.ExInserisciEventoNotifica(lEveNot);

			if (isRequestParameterNullObj(CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO))
				lCodTipoPenaAccessoriaNuovo = "-";
			else
				lCodTipoPenaAccessoriaNuovo = getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO)
						.toUpperCase();

			IPenaAccessoria lCtrl2 = SIEPLookupRemote.getPenaAccessoriaRemote();

			// Aggiorna la Pena Accessoria nei campi x Ordinanza Condono/Revoca/Sostituzione SOLO SE NON E' DI
			// TIPO ESPIATA PENA ACCESSORIA.
			if (lEveNot.getEvento().getCodTipoProvvedimento().compareTo("37") != 0
					&& !isRequestParameterNullObj(CAMPO_ANNO_DATA_ORDINANZA_PA)) {
				PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
				lPenMod.setIdPenaAccessoria(getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA));
				lPenMod.setCodNuovoTipoPenaAccessoria(lCodTipoPenaAccessoriaNuovo);

				// 24/04/2006 Modifica DescrAltrePA se necessario.
				if (!isRequestParameterNullObj(CAMPO_DESCR_ALTRE_PA))
					lPenMod.setDescrAltrePA(getRequestStringParameter(CAMPO_DESCR_ALTRE_PA));

				// 24/04/2006 Aggiunta campi Ordinanza Condono/Revoca/Sostituzione.

				if (!isRequestParameterNullObj(CAMPO_FLAG_CONDONATA)) {
					lPenMod.setCodFonteGE(getRequestStringParameter("CodFonte"));
					lPenMod.setAnnoFonteGE(getRequestStringParameter("AnnoFonte"));
					lPenMod.setNumeroFonteGE(getRequestStringParameter("NumeroFonte"));
					lPenMod.setArticoloGE(getRequestStringParameter("Articolo"));
					lPenMod.setCodSottonumerazioneGE(getRequestStringParameter("CodSottonumerazione"));
					lPenMod.setCommaGE(getRequestStringParameter("Comma"));
					lPenMod.setLetteraGE(getRequestStringParameter("Lettera"));
					lPenMod.setNumeroGE(getRequestStringParameter("Numero"));

					lPenMod.setDataOrdinanzaPA(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_PA,
							CAMPO_MESE_DATA_ORDINANZA_PA, CAMPO_GIORNO_DATA_ORDINANZA_PA));
					lPenMod.setAnnoOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_PA));
					lPenMod.setNumeroOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_PA));

					lCodTipoUfficioPA = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_ORDINANZA_PA);
					lPenMod.setCodTipoUfficioOrdinanzaPA(lCodTipoUfficioPA);
					lDescrComunePA = getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA)
							.toUpperCase();
					if (lDescrComunePA != null && !lDescrComunePA.equals("")) {
						String lCodComunePA = (getCodComuneByDescr(lDescrComunePA)).getCodComune();
						lPenMod.setCodLuogoUfficioOrdinanzaPA(lCodComunePA);
					} else
						lPenMod.setCodLuogoUfficioOrdinanzaPA("-");
					lPenMod.setFlagCondonata(getRequestStringParameter(CAMPO_FLAG_CONDONATA));
				}

				if (lCodTipoPenaAccessoriaNuovo.compareTo("-") != 0)
					lPenMod.setDataFineValidita(DateUtils.getSysDate());

				lPenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
				lPenMod.setDataAggiornamento(DateUtils.getSysDate());
				lPenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());

				// Chiama il controller per aggiornare la Pena Accessoria (Dati dell'ordinanza).
				lPenMod = lCtrl2.ExModificaOrdinanzaPenaAccessoria(lPenMod);
			}

			// Eventuale inserimento della Pena Accessoria Sostitutiva.
			esistePAsostitutiva = lCtrl2
					.ExistPASostitutiva(getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA));
			if (lCodTipoPenaAccessoriaNuovo.compareTo("-") != 0 && !esistePAsostitutiva) {
				PenaAccessoriaModel lPenModNew = new PenaAccessoriaModel();
				lPenModNew
						.setIdPenaAccessoriaOrigine(getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA));
				lPenModNew.setCodTipoPenaAccessoria(lCodTipoPenaAccessoriaNuovo.trim());

				lPenModNew.setDurata(getRequestStringParameter(CAMPO_DURATA));
				lPenModNew.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
				lPenModNew.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
				lPenModNew.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
				lPenModNew.setDataOrdinanzaGE(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_PA,
						CAMPO_MESE_DATA_ORDINANZA_PA, CAMPO_GIORNO_DATA_ORDINANZA_PA));
				lPenModNew.setAnnoOrdinanzaGE(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_PA));
				lPenModNew.setNumeroOrdinanzaGE(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_PA));
				lPenModNew.setCodOperatoreInserimento(lCodiceOperatore);
				lPenModNew.setCodUfficioInserimento(lCodiceUfficio);
				lPenModNew.setDataInserimento(DateUtils.getSysDate());
				lPenModNew.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
				lPenModNew.setFlagRevocaCondono("N");
				lPenModNew.setFlagDichiarazioneFalsita("N");
				lPenModNew.setFlagCondonata("-");

				/*
				 * 05/04/2006 Se Sostituzione e Condono, si aggiungono i campi per richiesta al giudice
				 * dell'esecuzione. if( !isRequestParameterNullObj(CAMPO_COD_TIPO_COMUNICAZIONE) &&
				 * getRequestStringParameter( CAMPO_COD_TIPO_COMUNICAZIONE).compareTo("03")==0 ) {
				 * lPenModNew.setCodFonteGE(getRequestStringParameter("CodFonte"));
				 * lPenModNew.setAnnoFonteGE(getRequestStringParameter("AnnoFonte"));
				 * lPenModNew.setNumeroFonteGE(getRequestStringParameter("NumeroFonte"));
				 * lPenModNew.setArticoloGE(getRequestStringParameter("Articolo"));
				 * lPenModNew.setCodSottonumerazioneGE(getRequestStringParameter("CodSottonumerazione"));
				 * lPenModNew.setCommaGE(getRequestStringParameter("Comma"));
				 * lPenModNew.setLetteraGE(getRequestStringParameter("Lettera"));
				 * lPenModNew.setNumeroGE(getRequestStringParameter("Numero")); }
				 */
				String lCodTipoUfficioGE = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_ORDINANZA_PA);
				lPenModNew.setCodTipoUfficioOrdinanzaGE(lCodTipoUfficioGE);
				String lDescrComuneGE = getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA)
						.toUpperCase();
				if (lDescrComuneGE != null && !lDescrComuneGE.equals("")) {
					String lCodComuneGE = (getCodComuneByDescr(lDescrComuneGE)).getCodComune();
					lPenModNew.setCodLuogoUfficioOrdinanzaGE(lCodComuneGE);
				} else
					lPenModNew.setCodLuogoUfficioOrdinanzaGE("-");

				// In caso di sostituzione e Condono occorre valorizzare anche i dati dell'ordinanza PA.
				if (lCodProvvXComunicazionePA.compareTo("41") == 0) {
					lPenModNew.setDataOrdinanzaPA(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_PA,
							CAMPO_MESE_DATA_ORDINANZA_PA, CAMPO_GIORNO_DATA_ORDINANZA_PA));
					lPenModNew.setAnnoOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_PA));
					lPenModNew.setNumeroOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_PA));
					lPenModNew.setDataOrdinanzaPA(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_PA,
							CAMPO_MESE_DATA_ORDINANZA_PA, CAMPO_GIORNO_DATA_ORDINANZA_PA));
					lPenModNew.setAnnoOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_PA));
					lPenModNew.setNumeroOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_PA));
					lPenModNew.setCodTipoUfficioOrdinanzaPA(lCodTipoUfficioPA);
					if (lDescrComunePA != null && !lDescrComunePA.equals("")) {
						String lCodComunePA = (getCodComuneByDescr(lDescrComunePA)).getCodComune();
						lPenModNew.setCodLuogoUfficioOrdinanzaPA(lCodComunePA);
					} else
						lPenModNew.setCodLuogoUfficioOrdinanzaPA("-");
					lPenModNew.setFlagCondonata("C");
				}
				lPenModNew = lCtrl2.ExInserisciPenaAccessoria(lPenModNew);
			}
		} catch (SIEPException e) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					e.getMessage() + " Impossibile effettuare la Comunicazione di Aggiornamento.");
		}

		// Prepara la pagina di destinazione, precisamente punta
		// all'azione di dettaglio.
		if (lCodTipoPenaAccessoriaNuovo.compareTo("-") != 0 && !esistePAsostitutiva) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Inserita Nuova Pena Accessoria: Completare le modifiche necessarie.");
			// Prepara la "pagina" di destinazione
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.penaaccessoria.action.ActLoadDettaglioComunicazione");
			lRedirigi.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEveNot.getEvento().getIdEvento());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;

		} else {
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.siep.penaaccessoria.action.ActLoadDettaglioComunicazione");
			lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEveNot.getEvento().getIdEvento());

			return lPage.toString();
		}
	}

}

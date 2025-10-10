package siap.sius.richiestaatti.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: ActInserisciRichiestaAltreIstruttorie
 * </p>
 * <p>
 * Description: Classe di tipo Azione responsabile della raccolta dei dati inputati nella form d'inserimento.
 * Inoltre, creazione evento e relative notifiche.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActInserisciRichiestaAltreIstruttorie extends ActInserisciRicAtti implements
		ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di: recupero dati in request, creazione evento e notifiche.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

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

		// Gestione del Codice tipo ufficio connesso ( UDS o TDS ).
		if (lCodTipoUfficio.equalsIgnoreCase("TDS"))
			lEveNot.getEvento().setCodMotivo("0555");
		else if (lCodTipoUfficio.equalsIgnoreCase("UDS"))
			lEveNot.getEvento().setCodMotivo("0556");
		// MEV10-s3: aggiunti ulteriori controlli per gestire altri tipi uffici collegati
		else if (lCodTipoUfficio.equalsIgnoreCase("TDSM"))
			lEveNot.getEvento().setCodMotivo("0555");
		else if (lCodTipoUfficio.equalsIgnoreCase("UDSM"))
			lEveNot.getEvento().setCodMotivo("0556");

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

		// INIZIO: MEV_2019-09 (D.lgs. 123/2018)
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_RESTITUZIONE)) {
			Date lDataRestituzioneAtti = getRequestDateParameter(CAMPO_ANNO_DATA_RESTITUZIONE
					                                           , CAMPO_MESE_DATA_RESTITUZIONE
				                                               , CAMPO_GIORNO_DATA_RESTITUZIONE  );
			lEveNot.getEvento().setDataRestituzioneAi(lDataRestituzioneAtti);
		}
		// FINE: MEV_2019-09 (D.lgs. 123/2018)
		
		// Imposta i dati necessari per la gestione delle Notifica
		Collection lNotifiche = new ArrayList();
		// Cicla sui tipi di destinatari.
		for (int x = 0; x < lTipoDest.length; x++) {
			NotificaModel lNotifica = null;
			if (lTipoDest[x].equalsIgnoreCase("UFF_GIUD")) {
				// Gestione UFF_GIUD
				if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(">>> Lavorazione TipoDestinatario : " + lTipoDest[x]);

					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);

					// Preleva il codice d'ufficio per la coppia comune e codice
					// tipo ufficio.
					String lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune(lDestinatari[x],
							lSedi[x]);
					lNotifica.setUffCodUfficio(lCodUfficioDest);
				}
			} else if (lTipoDest[x].equalsIgnoreCase("IST_DET")) {
				// Gestione IST_DET
				if (!lDestinatari[x].equals("")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(">>> Lavorazione TipoDestinatario : " + lTipoDest[x]);

					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);
					lNotifica.setIstDetIdIstitutoDetenzione(lDestinatari[x]);
					lNotifica.setUffCodUfficio("-");
				}
				// MEV10-s3: aggiunta or condition
			} else if (lTipoDest[x].equalsIgnoreCase("CSSA") || "40".equalsIgnoreCase(lTipoDest[x])) {
				// Gestione CSSA
				if (!lSedi[x].equals("")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(">>> Lavorazione TipoDest : " + lTipoDest[x]);

					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);
					String lDescrCSSA = lSedi[x]; // Gestione CSSA.
					// 20170915: [SG] non funziona poichè ci sono 2 descrizioni e prende la prima!!!
					// Preleva l'id del CSSA attraverso la propria descrizione.
					// BigDecimal lIdCSSA = getIdCSSAByDescrComune(lDescrCSSA);
					BigDecimal lIdCSSA = getIdCSSAByDescrComuneETipo(lDescrCSSA, "UEPE");
					// Imposta l'IdCSSA nella Notifica.
					lNotifica.setCssIdCssa(lIdCSSA);
				}
			}
			// MEV10-s3: aggiunta gestione dati per tipologia uffici
			else if ("B5".equalsIgnoreCase(lTipoDest[x])) {
				if (!"".equals(lSedi[x])) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(">>> Lavorazione TipoDest : " + lTipoDest[x]);
					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);
					String lDescrCSSA = lSedi[x]; // Gestione USSM.
					// Preleva l'id del USSM attraverso la propria descrizione ed il tipo.
					BigDecimal lIdCSSA = getIdCSSAByDescrComuneETipo(lDescrCSSA, "USSM");
					// Imposta l'IdCSSA nella Notifica.
					lNotifica.setCssIdCssa(lIdCSSA);
				}
				if (!lNote[x].equals(""))
					lNotifica.setNote(lNote[x]);
			} else if (lTipoDest[x].equalsIgnoreCase("AUT_EXT")) {
				// Gestione Autorità Esterna
				if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(">>> Lavorazione TipoDest : " + lTipoDest[x]);
					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);
					lNotifica.setUffCodUfficio("-");
					// Preleva e verifica il codice comune della sede inputata.
					String lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();
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
					if (!lNote[x].equals("")) {
						lNotifica.setNote(lNote[x]);
					}
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

		return getPaginaDettaglio(lEveNot.getEvento().getIdEvento());
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
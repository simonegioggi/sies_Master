package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciDifferimentoOE</p>
 * <p>Description: Classe Action per l'inserimento del Provvedimento dell'Esecuzione
 *                 legato a un Differimento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciDifferimentoOE extends ActionSiap implements ICostantiSospensione, ICostantiEvento {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
	 * Azione di Inserimento del Provvedimento dell' Esecuzione, e delle relative Notifiche, a seguito di una
	 * Ordinanza/Decreto di Differimento. Deve registrare: l'evento, la Pena Residua, la Sospensione, le
	 * Notifiche.
	 *
	 *
	 * Recupera i dati dalla form Il problema è che l'inserimento viene effettuato dal controller!! Creare un
	 * nuovo metodo del controller a cui vengono passati i dati, il ctrl deve solo inserirli, NON deve fare
	 * calcoli
	 * 
	 * @return
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		String lSedeCodiceUfficio = this.getCodComuneUtenteConnesso();

		// Id dell'evento associato al Provvedimento della Sorveglianza
		BigDecimal idEventoGenerato = this.getRequestBigDecimalParameter("idEventoGenerato");

		String tipoProvvedimento = getRequestStringParameter(TIPO_DIFFERIMENTO);

		// ==========================================================================
		// Carico l'Evento (Ordine Scarcerazione o Comunicazione)
		// ==========================================================================
		EventoModel lEveMod = new EventoModel();

		lEveMod.setCodTipoEvento("01"); // Sempre Provvedimento

		// Il Tipo ed il Motivo del Provvedimento dipendono dallo stato del detenuto
		// (Scarcerato o non) e dalla Autorità emittente (TDS /UDS ).
		// Il CodTipoProvvedimento e CodMotivo dipendono dal tipo provvedimento
		// e dalla Posizione Giuridica del soggetto e sono stati passati alla
		// form direttamente dalla ActLoadInsDifferimentoOE
		lEveMod.setCodTipoProvvedimento(getRequestStringParameter("CodTipoProvvedimento"));
		lEveMod.setCodMotivo(getRequestStringParameter("CodMotivo"));
		lEveMod.setCodUfficioEmittente(lCodiceUfficio);
		lEveMod.setCodLuogoEmittente(lSedeCodiceUfficio);
		lEveMod.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveMod.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));

		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		// Lego l'evento corrente all'evento "SIUS", quello generato in fase di
		// inserimento del decreto/ordinanza
		lEveMod.setEveIdEvento(idEventoGenerato);
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagDocumentoRegistrato("N");
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");

		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodEsito("-");

		lEveMod.setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// setto l'evento();
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEveMod);

		// ==========================================================================
		// Creo l'array delle NOTIFICHE a:
		// - TDS (se scarcerato) o Istituto di Detenzione (se da scarcerare)
		// - Avvocati
		// ==========================================================================
		ArrayList lNotifiche = setNotifiche();

		// Aggiungo l'array delle notifiche all'evento
		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// ==========================================================================
		// Inserimento il Provvedimento (EVENTO, PENA_RESIDUA, SOSPENSIONE, NOTIFICHE)
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Evento notifiche Model \n" + lEveNot);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

		ISospensione lCtrlEve = SIEPLookupRemote.getSospensioneRemote();
		EventoNotificaModel lEveNotMod = lCtrlEve.ExInserisciEventoDifferimento(tipoProvvedimento, lEveNot,
				lCalcoloPenaModel);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadDettaglioDifferimentoOE&" + CAMPO_ID_EVENTO + "="
				+ lEveNotMod.getEvento().getIdEvento().toString();

		return lPage;
	}

	/**
	 * Carico i Destinatari. Variano in funzione del tipo di provvedimento e della posizione giuridica del
	 * soggetto, nessun destinatario è obbligatorio ma almeno uno va indicato (se previsti) - Differimento
	 * Provvisorio - Libero: (07,10,16,20,26,30,46,47) isLibero - Tds, Autorità Competente per la Notifica al
	 * Condannato, UNEP (eventualmente autorità per la restituzione OE) - Detenuto (03): - se già scarcerato:
	 * Tds, Autorità Competente per la Notifica al Condannato, UNEP - se da scarcerare: Tds, Istituto di
	 * detenzione, UNEP - In Misura Alternativa (12,13,14,27,04) - se Semilibero (14), in Detenzione
	 * Domiciliare (12), in Affidamento in Prova (13) Indultino (L. 207/2003) (27) Tds, Istituto di
	 * detenzione, CSSA, Autorità di Polizia, UNEP (12,13,14,27) - se agli Arresti Domiciliari (04) (come
	 * detenuto) - se da scarcerare: Tds, Istituto di detenzione, UNEP - se già scarcerato: Tds, Autorità
	 * Competente per la Notifica al Condannato, UNEP - In tutti gli altri casi, non si indicano destinatari
	 * (doc vuoto)
	 * 
	 * - Differimento Definitivo - Libero in Differimento Provvisorio - Istituto di detenzione, CSSA, Autorità
	 * di Polizia, UNEP - Libero (????????????) - Detenuto - Istituto di detenzione, CSSA, Autorità di
	 * Polizia, UNEP - In Misura Alternativa - Istituto di detenzione, CSSA, Autorità di Polizia, UNEP -
	 * Revoca - Rigetto
	 */
	private ArrayList setNotifiche() throws F3BException {
		ArrayList lNotifiche = new ArrayList();
		// ==========================================================================
		// Il metodo verifica quali destinatari sono presenti e li carica nell'array
		// delle notifiche. n.b. non entra nel merito di QUALI devono essere presenti
		// ma prende per buono che quelli che gli vengono passati dalla form siano
		// quelli previsti.
		// I Destinatari possibili sono:
		// - TDS (TS)
		// - ISTITUTO DI DETENZIONE (E)
		// - AUTORITA' COMPETENTE PER LA NOTIFICA (???)
		// - CSSA (CS)
		// - AVVOCATI (N)
		// - AUTORITA' PER LA RESTITUZIONE ORDINE ESECUZIONE (R)
		// ==========================================================================

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		Date lDataEmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		// ==========================================================================
		// SETTO TDS (se presente e valorizzato)
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiSospensione.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();

			// MEV10-s3: aggiunto controllo preventivo e modificato parametro di passaggio
			String uff = "TDS";
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE).equals(""))
				uff = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);

			String lTribunale = this.getCodUfficioByCodTipoUfficioDescrComune(uff,
					getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS));
			lNotModTDS.setUffCodUfficio(lTribunale);

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodTipoNotifica("TS"); // Tribunale Sorveglianza
			lNotModTDS.setDataInvio(lDataEmissione);

			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);

			lNotifiche.add(lNotModTDS);
		}

		// ==========================================================================
		// SETTO ISTITUTO DETENZIONE (se presente e valorizzato)
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {

			NotificaModel lNotModIst = new NotificaModel();

			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);

			lNotModIst.setCodEsito("-");
			lNotModIst.setCodTipoNotifica("E");
			lNotModIst.setDataInvio(lDataEmissione);

			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());

			lNotifiche.add(lNotModIst);
		}

		// ==========================================================================
		// Autorità Competente per la Notifica al Condannato
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E)
				&& this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E) != null
				&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E).equals("")
				&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E)
						.equals("-")) {

			NotificaModel lNotModPol = new NotificaModel();

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
				String lNotePolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodTipoNotifica("E"); // TODO da verificare
			lNotModPol.setDataInvio(lDataEmissione);

			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			String lPolizia = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E);
			String lSedePolizia = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E);

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());

			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());

			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// ==========================================================================
		// CSSA
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& this.getRequestStringParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null
				&& !getRequestStringParameter(ICostantiCSSA.CAMPO_ID_CSSA).equals("-")
				&& !getRequestStringParameter(ICostantiCSSA.CAMPO_ID_CSSA).equals("")
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA).compareTo(new BigDecimal(0)) != 0) {
			NotificaModel lNotModCSSA = new NotificaModel();

			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA)) {
				String lNoteCssa = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA);
				lNotModCSSA.setNote(lNoteCssa);
			}

			lNotModCSSA.setCodEsito("-");

			if (isRequestParameterNullObj("cssaDetenuto") && !isRequestParameterNullObj("cssaE")
					&& getRequestStringParameter("cssaE") != null
					&& getRequestStringParameter("cssaE").equals("S")) {
				lNotModCSSA.setCodTipoNotifica("E");
			} else {
				lNotModCSSA.setCodTipoNotifica("C");
			}

			lNotModCSSA.setDataInvio(lDataEmissione);
			lNotModCSSA.setCssIdCssa(lCssa);

			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);

			lNotifiche.add(lNotModCSSA);
		}

		// ==========================================================================
		// AVVOCATI ()
		// ==========================================================================
		int lIndex = 0;
		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				NotificaModel lNotAvv = new NotificaModel();

				lNotAvv.setCodTipoNotifica("N");
				// lNot.setNote(lArrayNote[lIndMisura]);
				lNotAvv.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
						ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
				lNotAvv.setCodEsito("-");
				lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

				lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
				lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
				lNotAvv.setDataInserimento(DateUtils.getSysDate());

				// Recupero i dati dell'autorità esterna se specificata (cod tipo e sede)
				// e delle note (per l'autorità esterna)
				if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF)) {
					String[] lTipoAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF);
					String[] lSedeAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF);

					String[] lNoteAvvocato = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);

					lNotAvv.setNote(lNoteAvvocato[lIndex]);
					// String lNoteAvvocato = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
					// lNotAvv.setNote(lNoteAvvocato);

					AutoritaEsternaModel lAut = new AutoritaEsternaModel();
					lAut = new AutoritaEsternaModel();
					lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);

					ComuneModel lComMod = new ComuneModel(
							getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
					lAut.setCodSede(lComMod.getCodComune());

					lAut.setCodOperatoreInserimento(lCodiceOperatore);
					lAut.setCodUfficioInserimento(lCodiceUfficio);
					lAut.setDataInserimento(DateUtils.getSysDate());

					lNotAvv.setAutoritaEsterna(lAut);
				}

				lNotifiche.add(lNotAvv);
			}
		}

		// ==========================================================================
		// AUTORITA' PER LA RESTITUZIONE ORDINE ESECUZIONE
		// ==========================================================================
		if (!isRequestParameterNullObj(CAMPO_RESTITUZIONE_OE)) {
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
					&& this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R) != null
					&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("")
					&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("-")) {

				NotificaModel lNotModPol = new NotificaModel();

				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R)) {
					String lNotePolizia = this
							.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R);
					lNotModPol.setNote(lNotePolizia);
				}

				lNotModPol.setCodEsito("-");
				lNotModPol.setCodTipoNotifica("R");
				lNotModPol.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_DATA_EMISSIONE,
						"dd-MM-yyyy"));

				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				String lPolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R);
				String lSedePolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R);

				lAut.setCodTipoAutorita(lPolizia);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
				lAut.setCodSede(lComMod.getCodComune());

				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(DateUtils.getSysDate());

				lNotModPol.setAutoritaEsterna(lAut);

				lNotifiche.add(lNotModPol);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("--------------------------");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("lNotModPol = " + lNotModPol);
			}
		}
		return lNotifiche;
	}
}
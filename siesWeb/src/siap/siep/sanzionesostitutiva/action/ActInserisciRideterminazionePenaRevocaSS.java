package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciRideterminazionePenaRevocaSS
 * </p>
 * <p>
 * Description: Classe Action per l'Inserimento dell'Ordine di Esecuzione a seguito revoca/conversione
 * Sanzioni Sostitutive (in caso di cumulo?)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActInserisciRideterminazionePenaRevocaSS extends ActionSiap
		implements ICostantiSanzioneSostitutiva {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * La funzione prevede l'inserimento di un unico evento (per ora): 01-12-0489 Comunicazione Nuovo Residuo
	 * Pena per Computo Misure Cautelari
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {
		//
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ======================================
		// Carico i dati dell'evento OE
		// ======================================
		EventoModel lEventoOE = new EventoModel();

		lEventoOE.setFasSieIdFascicoloSiep(lIdFascicolo);

		lEventoOE.setCodTipoEvento("01"); // 01 - Provvedimento
		lEventoOE.setCodTipoProvvedimento("06"); // 06 - Ordine di Scarcerazione

		// Il codice motivo dipende dalla posizione giuridica
		String lCodPosGiuridica = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		if (lCodPosGiuridica.equals("07") && lFascMod.getFlagAltraCausa() != null
				&& lFascMod.getFlagAltraCausa().equals("S")) {
			// detenuto altra causa
			lEventoOE.setCodMotivo("0398"); // 0398 - Per la carcerazione - Detenuto altra causa - Conversione
											// Sanzione Sostitutiva
		} else if (lCodPosGiuridica.equals("07")) {
			// Libero
			lEventoOE.setCodMotivo("0397"); // 0397 - Per la carcerazione - Libero - Conversione Sanzione
											// Sostitutiva
		} else if (lCodPosGiuridica.equals("03")) {
			lEventoOE.setCodMotivo("0399"); // 0399 - Per la carcerazione - Detenuto per questa causa -
											// Conversione Sanzione Sostitutiva
		} else {
			lEventoOE.setCodMotivo("0935"); // 0935 - Per la carcerazione - Espiazione Pena in Misura
											// Alternativa - Conversione Sanzione Sostitutiva
		}

		lEventoOE.setCodEsito("-");

		lEventoOE.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEventoOE.setCodLuogoEmittente(getCodComuneUtenteConnesso());

		lEventoOE.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		// lEventoComunicazione.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
		// ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
		// ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		lEventoOE.setFlagDocumentoRegistrato("N");
		lEventoOE.setFlagStampaSiep("S");
		lEventoOE.setFlagVideoSiep("S");

		if (!this.isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			lEventoOE.setCodMagistrato(
					this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		} else {
			lEventoOE.setCodMagistrato("-");
		}

		lEventoOE.setCodLuogoDestinatario("-");
		lEventoOE.setCodUfficioDestinatario("-");
		lEventoOE.setCodTipoUfficioDestinatario("-");

		lEventoOE.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEventoOE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEventoOE.setDataInserimento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEventoOE = " + lEventoOE);

		// Recupero l'id dell'annotazione e lego l'OE all'annotazione
		BigDecimal lIdEventoAnnotazione = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO);
		;
		lEventoOE.setEveIdEvento(lIdEventoAnnotazione);

		// =============================================================
		//
		// =============================================================
		EventoNotificaModel lEvNotModel = new EventoNotificaModel();
		lEvNotModel.setEvento(lEventoOE);

		// ==========================================================================
		// Calcolo la pena residua da espiare (n.b. la ActLoadInserisci la calcola e
		// la passa sulla form, ma non la inserisce dato che non ha un evento a cui
		// agganciarla, per cui devo ricalcolarla)
		// Verificare la possibilità di modificare il fine pena
		// ==========================================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Calcolo la nuova pena residua");

		ActCalcoloPenaMain lCalcPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcPenaModel = lCalcPenaMain.calcoloPena(lIdFascicolo, null);
		PenaResiduaModel lNuovaPenaResidua = null;
		try {
			lNuovaPenaResidua = lCalcPenaModel.getPenaDaEspiare(lUltimaPenaValidata.getDataInizio(), null,
					"all", null);
		} catch (Exception e) {
			throw new F3BException(e);
		}

		lNuovaPenaResidua.setFasSieIdFascicoloSiep(lIdFascicolo);

		lNuovaPenaResidua.setFlagValidato("N");
		lNuovaPenaResidua.setDiesAQuo("S");
		lNuovaPenaResidua.setFlagErgastolo("N");

		lNuovaPenaResidua.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNuovaPenaResidua.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lNuovaPenaResidua.setDataInserimento(DateUtils.getSysDate());

		// ===========================================
		// Carico i dati delle notifiche (destinatari)
		// ===========================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Carico le notifiche");
		ArrayList lNotifiche = new ArrayList();

		Date lDataTrasmissione = null;
		lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		// ==========================================================================
		// Autorità competente per il territorio
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			NotificaModel lNotMod = new NotificaModel();

			lNotMod.setCodEsito("-");
			lNotMod.setCodTipoNotifica("C"); // Comunicazione (?)
			lNotMod.setDataInvio(lDataTrasmissione);

			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E)) {
				lNotMod.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E));
			}

			lNotMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotMod.setDataInserimento(DateUtils.getSysDate());

			// Autorità esterna
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());

			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());

			lNotMod.setAutoritaEsterna(lAut);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica Autorità Esterna = " + lNotMod);

			lNotifiche.add(lNotMod);
		}

		// ==========================================================================
		// UEPE
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA)
						.compareTo(new BigDecimal(0)) != 0
				&& !getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA).toString().equals("-")) {
			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA)) {
				String lNoteCssa = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA);
				lNotModCSSA.setNote(lNoteCssa);
			}

			lNotModCSSA.setCodEsito("-");

			lNotModCSSA.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			if (isRequestParameterNullObj("cssaDetenuto") && !isRequestParameterNullObj("cssaE")
					&& getRequestStringParameter("cssaE") != null
					&& getRequestStringParameter("cssaE").equals("S")) {
				lNotModCSSA.setCodTipoNotifica("E");
			} else {
				lNotModCSSA.setCodTipoNotifica("C");
			}

			lNotModCSSA.setDataInvio(lDataTrasmissione);
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica UEPE = " + lNotModCSSA);

		}

		// ==========================================================================
		// Magistrato di Sorveglianza (se presente)
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_MDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS).equals("")) {
			NotificaModel lNotModMDS = new NotificaModel();

			String lTribunale = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));
			lNotModMDS.setUffCodUfficio(lTribunale);

			lNotModMDS.setCodEsito("-");
			lNotModMDS.setCodTipoNotifica("MS"); // Magistrato di Sorveglianza
			lNotModMDS.setDataInvio(lDataTrasmissione);

			lNotModMDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModMDS.setDataInserimento(DateUtils.getSysDate());
			lNotModMDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lNotifiche.add(lNotModMDS);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica MDS Autorità = " + lNotModMDS);
		}

		// ==========================================================================
		// Tribunale di Sorveglianza (se presente)
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();

			String lTribunale = this.getCodUfficioByCodTipoUfficioDescrComune("TDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
			lNotModTDS.setUffCodUfficio(lTribunale);

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodTipoNotifica("TS"); // Tribunale di Sorveglianza
			lNotModTDS.setDataInvio(lDataTrasmissione);

			lNotModTDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lNotifiche.add(lNotModTDS);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica TDS Autorità = " + lNotModTDS);
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

				lNotAvv.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNotAvv.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
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

					//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//					ComuneModel lComMod = new ComuneModel(
//							getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
					ComuneModel lComMod = new ComuneModel(
							getCodComuneByDescrFlagVal(lSedeAutoritaEsternaAvvocato[lIndex]));	
					//FINE: MEV_21
					
					lAut.setCodSede(lComMod.getCodComune());

					lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
					lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lAut.setDataInserimento(DateUtils.getSysDate());

					lNotAvv.setAutoritaEsterna(lAut);
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Notifica Avvocato = " + lNotAvv);
				lNotifiche.add(lNotAvv);
			}
		}

		// ==========================================================================
		// Aggiungo l'array delle notifiche all'evento
		// ==========================================================================
		lEvNotModel.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// ===========================
		// Effettuo la registrazione
		// ===========================
		ISanzioneSostitutiva lSanzioneCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();

		EventoNotificaModel lEventoNotInserito = lSanzioneCtrl.exInserisciOENuovoResiduoPena(lEvNotModel,
				lNuovaPenaResidua);

		// ==============================================
		// Restituisco la pagina di Dettaglio
		// ==============================================
		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRideterminazionePenaRevocaSS&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "="
				+ lEventoNotInserito.getEvento().getIdEvento().toString();
		return lPage;

	}
}
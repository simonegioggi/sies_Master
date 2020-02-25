package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.sospensione.action.ICostantiSospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Azione di inserimento dell'Ordine di Esecuzione collegato a un provvedimento di Rideterminazione Pena Altro
 * 
 * @author
 * @since 4.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOERidetPenaAltro extends ActOrdineEsecuzione implements ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * 
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdEveComputo = getRequestBigDecimalParameter("IdEventoComputo");

		String lPosGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		// L'unico "Oggetto" per cui deve essere presente il tasto "FC"
		// e' "Rideterminazione della pena a seguito di ordinanza ex art. 669 ccp"
		// con codiceMotivo="0959" nella tabella CG_REF_CODES
		String lParamValue = null;
		String codMotivoEventoComputo = null;
		lParamValue = this.getParameter("codMotivoEventoComputo");
		if (lParamValue != null) {
			codMotivoEventoComputo = getRequestStringParameter("codMotivoEventoComputo");
		}

		// ==========================================================================
		// Carico l'evento Ordine di Esecuzione
		// ==========================================================================
		EventoModel lEveOE = new EventoModel();

		lEveOE.setCodMotivo(codMotivoEventoComputo); // "Rideterminazione della pena a seguito di ordinanza ex
														// art. 669 ccp"

		lEveOE.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEveOE.setCodTipoProvvedimento("06"); // Tipo Provvedimento = ORDINE ESECUZIONE

		// Imposto il codice Motivo che cambia in funzione della Posizione Giuridica
		if (lFascicoloModel.getFlagAltraCausa() != null && lFascicoloModel.getFlagAltraCausa().equals("S")
				&& (lPosGiu.equals("07") || lPosGiu.equals("10"))) { // Libero detenuto Altra Causa
			lEveOE.setCodMotivo("0962"); // 0962 = per la Carcerazione con rideterminazione pena - detenuto
											// per altra causa
		} else if (lPosGiu.equals("07") || lPosGiu.equals("10") || lPosGiu.equals("20")
				|| lPosGiu.equals("46") || lPosGiu.equals("47") // Libero in sospensione
				|| lPosGiu.equals("16") || lPosGiu.equals("17") // Libero in differimento
		) { // libero Questa Causa
			lEveOE.setCodMotivo("0960"); // 0960 = per la Carcerazione con rideterminazione pena - libero
		} else if (lPosGiu.equals("02") || lPosGiu.equals("04")) { // Arresti Domiciliari
			lEveOE.setCodMotivo("0961"); // 0961 = per la Carcerazione con rideterminazione pena - arresti
											// domiciliari
		}

		lEveOE.setFlagStampaSiep("S");
		lEveOE.setFlagVideoSiep("S");
		// lEveOE.setFlagDocumentoRegistrato();

		lEveOE.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveOE.setEveIdEvento(lIdEveComputo);

		// Data emissione
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveOE.setDataEmissione(lDataEmissione);

		lEveOE.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEveOE.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// lEveOE.setProgrProtocollo(); Verrà impostato dal controller. Unico per anno e ufficio.

		lEveOE.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveOE.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());

		lEveOE.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveOE.setDataInserimento(DateUtils.getSysDate());
		lEveOE.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// lEveOE.setCodOperatoreAggiornamento(lCodiceOperatore);
		// lEveOE.setCodUfficioAggiornamento(lCodiceUfficio);
		// lEveOE.setDataAggiornamento(DateUtils.getSysDate());

		// Altri campi da inizializzare a "-" per evitare che falliscano le join
		// con la CG_REF_CODES
		lEveOE.setCodEsito("-");
		lEveOE.setCodLuogoDestinatario("-");
		lEveOE.setCodTipoUfficioDestinatario("-");

		// ==========================================================================
		// Carico le notifiche
		// ==========================================================================
		ArrayList lArrayNotifiche = this.setNotifiche();
		NotificaModel[] lNotifiche = (NotificaModel[]) lArrayNotifiche.toArray(new NotificaModel[0]);

		// ==========================================================================
		// Carico EventoNotificaModel con evento e modifiche
		// ==========================================================================
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod.setEvento(lEveOE);
		lEveNotMod.setNotifiche(lNotifiche);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Evento: " + lEveNotMod.getEvento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Notifiche: " + lEveNotMod.getNotifiche().length);
		for (int i = 0; i < lNotifiche.length; i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(i + ") " + lNotifiche[i]);
		}

		// ==========================================================================
		// Inserisco i dati a sistema
		// ==========================================================================
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOERidetPenaAltro(lEveNotMod);

		// ==========================================================================
		// Invoco la Action di dettaglio
		// ==========================================================================
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Effettuato Correttamente!");
		// return IWebConstants.PG_MESSAGE;

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOERidetPenaAltro&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getIdEvento() + "&codMotivoEventoComputo="
				+ codMotivoEventoComputo;
		return lPage;
	}

	/**
	 * Carica le notifiche dell'ordine di esecuzione e l'eventuale restituzione dell'ordine di esecuzione già
	 * emesso. n.b. i Destinatari variano in funzione della posizione giuridica: - Autorità per l'esecuzione -
	 * Istituto di detenzione - Magistrato di Sorveglianza (UDS) - Autorità di Polizia (Comunicazione) -
	 * Avvocati
	 * 
	 * @return
	 * @throws F3BException
	 */
	private ArrayList setNotifiche() throws F3BException {
		ArrayList lArrayNotifiche = new ArrayList();

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataInserimento = DateUtils.getSysDate();

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		// ==========================================================================
		// Autorità per l'esecuzione: tipo notifica = E
		// Nel caso detenuto altra causa agli arresti domiciliari (PG=23) oppure se
		// libero (questa causa).
		// - ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E
		// - ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E
		// - ICostantiNotifica.CAMPO_NOTE_E
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E).equals("-")
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E).trim()
						.length() > 0) {
			NotificaModel lNotMod_E = new NotificaModel();

			lNotMod_E.setCodTipoNotifica("E"); // Esecuzione
			lNotMod_E.setDataInvio(lDataTrasmissione);
			lNotMod_E.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E));

			lNotMod_E.setCodEsito("-");

			lNotMod_E.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod_E.setDataInserimento(lDataInserimento);
			lNotMod_E.setCodUfficioInserimento(lCodiceUfficio);

			// =====
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E));
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E)));
			lAutMod.setCodSede(lComModel.getCodComune());

			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(lDataInserimento);

			lNotMod_E.setAutoritaEsterna(lAutMod);
			// ====

			lArrayNotifiche.add(lNotMod_E);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiunta Notifica per l'Esecuzione");
		}

		// ==========================================================================
		// Istituto di detenzione: tipo notifica = E
		// Nel caso detenuto altra causa o detenuto questa causa
		// - ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE
		// - ICostantiNotifica.CAMPO_NOTE_E
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.trim().length() > 0) {
			NotificaModel lNotMod_E = new NotificaModel();

			lNotMod_E.setCodTipoNotifica("E"); // Esecuzione
			lNotMod_E.setDataInvio(lDataTrasmissione);
			lNotMod_E.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
			lNotMod_E.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E));

			lNotMod_E.setCodEsito("-");

			lNotMod_E.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod_E.setDataInserimento(lDataInserimento);
			lNotMod_E.setCodUfficioInserimento(lCodiceUfficio);

			lArrayNotifiche.add(lNotMod_E);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiunta Notifica all'Istituto di Detenzione");
		}

		// ==========================================================================
		// Magistrato di Sorveglianza: tipo notifica = MS
		// Se in MIS_ALT 12 - Espiazione Pena in Regime di Detenzione Domiciliare
		// - ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS)
				&& getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS).trim()
						.length() > 0) {
			NotificaModel lNotMod_UDS = new NotificaModel();

			lNotMod_UDS.setCodTipoNotifica("MS"); // Magistrato Sorveglianza
			lNotMod_UDS.setDataInvio(lDataTrasmissione);

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS));
			lNotMod_UDS.setUffCodUfficio(lCodiceUff);

			lNotMod_UDS.setCodEsito("-");
			// lNotMod_UDS.setIstDetIdIstitutoDetenzione();

			lNotMod_UDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod_UDS.setDataInserimento(lDataInserimento);
			lNotMod_UDS.setCodUfficioInserimento(lCodiceUfficio);

			lArrayNotifiche.add(lNotMod_UDS);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiunta Notifica al Magistrato di Sorveglianza");

		}

		// ==========================================================================
		// Notifiche agli Avvocati, ne può essere presente più di uno: tipo notifica = N
		// - ICostantiAvvocato.CAMPO_ID_AVVOCATO
		// - ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA (UNEP)
		// - ICostantiAutoritaEsterna.CAMPO_COD_SEDE
		// - ICostantiNotifica.CAMPO_NOTE
		// ==========================================================================
		String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		String[] lArrayTipoAutorita = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		String[] lArraySedeAutorita = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		String[] lArrayNote = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);

		int lIndex = 0;
		for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
			NotificaModel lNotAvv = new NotificaModel();

			lNotAvv.setCodTipoNotifica("N");
			lNotAvv.setDataInvio(lDataTrasmissione);

			lNotAvv.setCodEsito("-");
			lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

			lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
			lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
			lNotAvv.setDataInserimento(lDataInserimento);

			lNotAvv.setNote(lArrayNote[lIndex]);

			// Recupero i dati dell'autorità esterna se specificata (cod tipo e sede)
			// e delle note (per l'autorità esterna)
			// Verificare se tutti obbligatori
			// =======
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lArrayTipoAutorita[lIndex]);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lArraySedeAutorita[lIndex]));
			lAut.setCodSede(lComMod.getCodComune());

			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(lDataInserimento);

			lNotAvv.setAutoritaEsterna(lAut);
			// =======

			lArrayNotifiche.add(lNotAvv);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiunta Notifica per l'Avvocato");
		}

		// ==========================================================================
		// Comunicazione alla Polizia: tipo notifica = AA
		// Se in MIS_ALT 12 - Espiazione Pena in Regime di Detenzione Domiciliare
		// - ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C
		// - ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C
		// - ICostantiNotifica.CAMPO_NOTE_C
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C).trim()
						.length() > 0) {
			NotificaModel lNotMod_AA = new NotificaModel();

			lNotMod_AA.setCodTipoNotifica("AA"); // Altra Autorità
			lNotMod_AA.setDataInvio(lDataTrasmissione);

			lNotMod_AA.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C));

			lNotMod_AA.setCodEsito("-");

			lNotMod_AA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod_AA.setDataInserimento(lDataInserimento);
			lNotMod_AA.setCodUfficioInserimento(lCodiceUfficio);

			// =====
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C));
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C)));
			lAutMod.setCodSede(lComModel.getCodComune());

			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(lDataInserimento);

			lNotMod_AA.setAutoritaEsterna(lAutMod);
			// ====

			lArrayNotifiche.add(lNotMod_AA);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiunta Notifica per Altra Autorità");
		}

		// ==========================================================================
		// AUTORITA' PER LA RESTITUZIONE ORDINE ESECUZIONE
		// ==========================================================================
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_RESTITUZIONE_OE)) {
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
					&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("")
					&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("-")) {

				NotificaModel lNotModPol = new NotificaModel();
				lNotModPol.setCodTipoNotifica("R");

				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R)) {
					String lNotePolizia = this
							.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R);
					lNotModPol.setNote(lNotePolizia);
				}

				lNotModPol.setCodEsito("-");
				lNotModPol.setDataInvio(
						getRequestDateParameter(ICostantiEvento.CAMPO_DATA_EMISSIONE, "dd-MM-yyyy"));

				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(lDataInserimento);
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
				lAut.setDataInserimento(lDataInserimento);

				lNotModPol.setAutoritaEsterna(lAut);

				lArrayNotifiche.add(lNotModPol);
				// lNotifiche[lNotifiche.length]=lNotModPol;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiunta Notifica Restituzione OE");
			}
		}

		return lArrayNotifiche;
	}

}
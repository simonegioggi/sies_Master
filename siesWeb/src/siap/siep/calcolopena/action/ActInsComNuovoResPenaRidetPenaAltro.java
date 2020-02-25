package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ActOrdineEsecuzione;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.sospensione.action.ICostantiSospensione;
import siap.siep.util.SIEPLookupRemote;

/**
 * Azione di inserimento della Comunicazione Nuovo Residuo Pena collegato a un provvedimento di
 * Rideterminazione Pena Altro
 * 
 * @author
 * @since 4.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInsComNuovoResPenaRidetPenaAltro extends ActOrdineEsecuzione
		implements ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * 
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdEveComputo = getRequestBigDecimalParameter("IdEventoComputo");
		// String lPosGiu =
		// getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		// Recupero il provvedimento di computo
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveComputo = lEveCtrl.ExRicercaEventoByKey(lIdEveComputo);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEveComputo = " + lEveComputo);

		// ==========================================================================
		// Carico l'evento Comunicazione
		// ==========================================================================
		EventoModel lEveComunicazione = new EventoModel();

		lEveComunicazione.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEveComunicazione.setCodTipoProvvedimento("12"); // Tipo Provvedimento = Comunicazione
		// Imposto il codice Motivo che è legato a quello del Provvedimento di Computo
		lEveComunicazione.setCodMotivo(this.getCodiceMotivo(lEveComputo.getCodMotivo()));

		lEveComunicazione.setFlagStampaSiep("S");
		lEveComunicazione.setFlagVideoSiep("S");
		// lEveOE.setFlagDocumentoRegistrato();

		lEveComunicazione.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveComunicazione.setEveIdEvento(lIdEveComputo);

		// Data emissione
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveComunicazione.setDataEmissione(lDataEmissione);

		lEveComunicazione.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEveComunicazione.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// lEveComunicazione.setProgrProtocollo(); Verrà impostato dal controller. Unico per anno e ufficio.

		lEveComunicazione.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveComunicazione.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());

		lEveComunicazione.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveComunicazione.setDataInserimento(DateUtils.getSysDate());
		lEveComunicazione.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// lEveOE.setCodOperatoreAggiornamento(lCodiceOperatore);
		// lEveOE.setCodUfficioAggiornamento(lCodiceUfficio);
		// lEveOE.setDataAggiornamento(DateUtils.getSysDate());

		// Altri campi da inizializzare a "-" per evitare che falliscano le join
		// con la CG_REF_CODES
		lEveComunicazione.setCodEsito("-");
		lEveComunicazione.setCodLuogoDestinatario("-");
		lEveComunicazione.setCodTipoUfficioDestinatario("-");

		// ==========================================================================
		// Carico le notifiche
		// ==========================================================================
		ArrayList lArrayNotifiche = this.setNotifiche();
		NotificaModel[] lNotifiche = (NotificaModel[]) lArrayNotifiche.toArray(new NotificaModel[0]);

		// ==========================================================================
		// Carico EventoNotificaModel con evento e notifiche
		// ==========================================================================
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod.setEvento(lEveComunicazione);
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
		ICalcoloPena lCtrlCalcoloPEna = SIEPLookupRemote.getCalcoloPenaRemote();
		EventoModel lRetModel = lCtrlCalcoloPEna.ExInserisciCOMRidetPenaAltro(lEveNotMod);

		// ==========================================================================
		// Invoco la Action di dettaglio
		// ==========================================================================
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Effettuato Correttamente!");
		// return IWebConstants.PG_MESSAGE;

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.calcolopena.action.ActLoadDettComNuovoResPenaRidetPenaAltro&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getIdEvento();
		return lPage;
	}

	/**
	 * Ritorna il codice motivo della COmunicazione in funzione del codice motivo del Provvedimento di computo
	 * 
	 * @param aCodMotivoComputo
	 * @return
	 */
	private String getCodiceMotivo(String aCodMotivoComputo) {
		// 28/09/2011 String lCodMotivo = "";
		String lCodMotivo = "-";

		if (aCodMotivoComputo.equals("0950"))
			lCodMotivo = "0975";
		else if (aCodMotivoComputo.equals("0951"))
			lCodMotivo = "0976";
		else if (aCodMotivoComputo.equals("0952"))
			lCodMotivo = "0977";
		else if (aCodMotivoComputo.equals("0948"))
			lCodMotivo = "0978";
		else if (aCodMotivoComputo.equals("0949"))
			lCodMotivo = "0979";
		else if (aCodMotivoComputo.equals("0953"))
			lCodMotivo = "0980";
		else if (aCodMotivoComputo.equals("0954"))
			lCodMotivo = "0981";
		else if (aCodMotivoComputo.equals("0955"))
			lCodMotivo = "0982";
		else if (aCodMotivoComputo.equals("0958"))
			lCodMotivo = "0983";
		else if (aCodMotivoComputo.equals("0957"))
			lCodMotivo = "0984";
		else if (aCodMotivoComputo.equals("0956"))
			lCodMotivo = "0985";
		else if (aCodMotivoComputo.equals("0959"))
			lCodMotivo = "0986";
		else if (aCodMotivoComputo.equals("0987"))
			lCodMotivo = "0991";
		else if (aCodMotivoComputo.equals("0988"))
			lCodMotivo = "0992";
		else if (aCodMotivoComputo.equals("0994")) // 06/10/2011
			lCodMotivo = "0994";
		else if (aCodMotivoComputo.equals("0999")) // 06/10/2011
			lCodMotivo = "0999";
		else if (aCodMotivoComputo.equals("1000")) // 16/06/2011
			lCodMotivo = "1006";
		else if (aCodMotivoComputo.equals("1003")) // 04/10/2011
			lCodMotivo = "1003";
		else if (aCodMotivoComputo.equals("1004")) // 04/10/2011
			lCodMotivo = "1004";
		else if (aCodMotivoComputo.equals("1005")) // 04/10/2011
			lCodMotivo = "1005";
		else if (aCodMotivoComputo.equals("1006")) // 04/10/2011
			lCodMotivo = "1006";
		else if (aCodMotivoComputo.equals("1015")) // 28/04/2015
			lCodMotivo = "1015";
		else if (aCodMotivoComputo.equals("1016")) // 28/04/2015
			lCodMotivo = "1016";
		// 04/10/2011 Segnalazione mancata associazione CodMotivo.
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Attenzione: Mancata associazione CodMotivoComputo= " + aCodMotivoComputo);

		return lCodMotivo;

	}

	/**
	 * Carica le notifiche della Comunicazione n.b. i Destinatari variano in funzione della posizione
	 * giuridica: - Istituto di detenzione - Autorità di Polizia (Comunicazione) - Magistrato di Sorveglianza
	 * (UDS) - Tribunale di Sorveglianza (TDS) - Avvocati
	 * 
	 * @return Array di Notifiche Model
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
		// Istituto di detenzione: tipo notifica = E
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
		// - ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_MDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS).trim().length() > 0) {
			NotificaModel lNotMod_UDS = new NotificaModel();

			lNotMod_UDS.setCodTipoNotifica("MS"); // Magistrato Sorveglianza
			lNotMod_UDS.setDataInvio(lDataTrasmissione);

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));
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
		// Tribunale di Sorveglianza: tipo notifica = TS
		// - ICostantiOrdineEsecuzione.CAMPO_SEDE_TDS
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS).trim().length() > 0) {
			NotificaModel lNotMod_TDS = new NotificaModel();

			lNotMod_TDS.setCodTipoNotifica("TS"); // Magistrato Sorveglianza
			lNotMod_TDS.setDataInvio(lDataTrasmissione);

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("TDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
			lNotMod_TDS.setUffCodUfficio(lCodiceUff);

			lNotMod_TDS.setCodEsito("-");
			// lNotMod_UDS.setIstDetIdIstitutoDetenzione();

			lNotMod_TDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod_TDS.setDataInserimento(lDataInserimento);
			lNotMod_TDS.setCodUfficioInserimento(lCodiceUfficio);

			lArrayNotifiche.add(lNotMod_TDS);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiunta Notifica al Tribunale di Sorveglianza");

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
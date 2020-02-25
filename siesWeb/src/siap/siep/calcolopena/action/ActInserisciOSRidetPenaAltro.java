package siap.siep.calcolopena.action;

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
import siap.siep.util.MinorMask;
import siap.siep.util.SIEPLookupRemote;

/**
 * Azione di inserimento dell'Ordine di scarcerazione Nuovo Residuo Pena collegato a un provvedimento di
 * Rideterminazione Pena Altro
 * 
 * @author
 * @since 4.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOSRidetPenaAltro extends ActOrdineEsecuzione implements ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * 
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdEveComputo = getRequestBigDecimalParameter("IdEventoComputo");
		// String lPosGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		// Recupero il provvedimento di computo
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveComputo = lEveCtrl.ExRicercaEventoByKey(lIdEveComputo);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEveComputo = " + lEveComputo);

		// ==========================================================================
		// Carico l'evento da inserire
		// ==========================================================================
		EventoModel lEveModel = new EventoModel();

		lEveModel.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		// Imposto il codice Motivo che è legato a quello del Provvedimento di Computo
		lEveModel.setCodMotivo(this.getCodiceMotivo(lEveComputo.getCodMotivo()));
		// Imposto il codice Motivo che è legato a quello del Provvedimento di Computo
		lEveModel.setCodTipoProvvedimento(this.getCodTipoProvvedimento(lEveModel.getCodMotivo()));

		lEveModel.setFlagStampaSiep("S");
		lEveModel.setFlagVideoSiep("S");
		// lEveOE.setFlagDocumentoRegistrato();

		lEveModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveModel.setEveIdEvento(lIdEveComputo);

		// Data emissione
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveModel.setDataEmissione(lDataEmissione);

		lEveModel.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEveModel.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// lEveComunicazione.setProgrProtocollo(); Verrà impostato dal controller. Unico per anno e ufficio.

		lEveModel.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveModel.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());

		lEveModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveModel.setDataInserimento(DateUtils.getSysDate());
		lEveModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// lEveModel.setCodOperatoreAggiornamento(lCodiceOperatore);
		// lEveModel.setCodUfficioAggiornamento(lCodiceUfficio);
		// lEveModel.setDataAggiornamento(DateUtils.getSysDate());

		// Altri campi da inizializzare a "-" per evitare che falliscano le join
		// con la CG_REF_CODES
		lEveModel.setCodEsito("-");
		lEveModel.setCodLuogoDestinatario("-");
		lEveModel.setCodTipoUfficioDestinatario("-");

		// ==========================================================================
		// Carico le notifiche
		// ==========================================================================
		ArrayList lArrayNotifiche = this.setNotifiche();
		NotificaModel[] lNotifiche = (NotificaModel[]) lArrayNotifiche.toArray(new NotificaModel[0]);

		// ==========================================================================
		// Carico EventoNotificaModel con evento e modifiche
		// ==========================================================================
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod.setEvento(lEveModel);
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
		EventoModel lRetModel = lCtrlCalcoloPEna.ExInserisciOSRidetPenaAltro(lEveNotMod);

		// ==========================================================================
		// Invoco la Action di dettaglio
		// ==========================================================================
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Effettuato Correttamente!");
		// return IWebConstants.PG_MESSAGE;

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.calcolopena.action.ActLoadDettaglioOSRidetPenaAltro&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getIdEvento();
		return lPage;
	}

	/**
	 * Ritorna il codice motivo della Rideterminazione Pena in funzione del codice motivo del Provvedimento di
	 * computo
	 * 
	 * @param aCodMotivoComputo
	 * @return
	 */
	private String getCodiceMotivo(String aCodMotivoComputo) {

		String lCodMotivo = "";

		if (aCodMotivoComputo.equals("0950"))
			lCodMotivo = "0963";
		else if (aCodMotivoComputo.equals("0951"))
			lCodMotivo = "0964";
		else if (aCodMotivoComputo.equals("0952"))
			lCodMotivo = "0965";
		else if (aCodMotivoComputo.equals("0948"))
			lCodMotivo = "0966";
		else if (aCodMotivoComputo.equals("0949"))
			lCodMotivo = "0967";
		else if (aCodMotivoComputo.equals("0953"))
			lCodMotivo = "0968";
		else if (aCodMotivoComputo.equals("0954"))
			lCodMotivo = "0969";
		else if (aCodMotivoComputo.equals("0955"))
			lCodMotivo = "0970";
		else if (aCodMotivoComputo.equals("0958"))
			lCodMotivo = "0971";
		else if (aCodMotivoComputo.equals("0957"))
			lCodMotivo = "0972";
		else if (aCodMotivoComputo.equals("0956"))
			lCodMotivo = "0973";
		else if (aCodMotivoComputo.equals("0959"))
			lCodMotivo = "0974";
		else if (aCodMotivoComputo.equals("0987"))
			lCodMotivo = "0989";
		else if (aCodMotivoComputo.equals("0988"))
			lCodMotivo = "0990";
		else if (aCodMotivoComputo.equals("1015")) // 19/11/2015
			lCodMotivo = "1015";
		else if (aCodMotivoComputo.equals("1016")) // 19/11/2015
			lCodMotivo = "1016";

		// Paolo Cherubini su richiesta di nunzia 11/03/2011
		else if (aCodMotivoComputo.equals("0999")) // rideterminazione pena
			lCodMotivo = "1003";
		else if (aCodMotivoComputo.equals("1000") // rideterminazione pena altro ufficio SORV
				|| aCodMotivoComputo.equals("1005") // rideterminazione pena altro ufficio AUFF
				|| aCodMotivoComputo.equals("1006")) // rideterminazione pena altro ufficio GE
			lCodMotivo = "1004";
		else if (aCodMotivoComputo.equals("0994")) // MEV 19 d.f. il codice 0994 introdotto nel 2011 non
													// veniva gestito
			lCodMotivo = "1010";

		return lCodMotivo;

	}

	/**
	 * Ritorna il cod Tipo Provvedimento in funzione del codice Motivo dell'evento di rideterminazione
	 * 
	 * @param aCodMotivoComputo
	 * @return
	 */
	private String getCodTipoProvvedimento(String aCodMotivoRidet) {

		String lCodTipoProvvedimento = "";

		if (aCodMotivoRidet.equals("0963"))
			lCodTipoProvvedimento = "04";
		else if (aCodMotivoRidet.equals("0964"))
			lCodTipoProvvedimento = "04";
		else if (aCodMotivoRidet.equals("0965"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0966"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0967"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0968"))
			lCodTipoProvvedimento = "04";
		else if (aCodMotivoRidet.equals("0969"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0970"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0971"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0972"))
			lCodTipoProvvedimento = "04";
		else if (aCodMotivoRidet.equals("0973"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0974"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0989"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("0990"))
			lCodTipoProvvedimento = "12";
		// Paolo Cherubini su richiesta di nunzia 11/03/2011
		else if (aCodMotivoRidet.equals("1003"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("1004"))
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("1010")) // d.f. 09/2014, il codice 0994 aggiunto nel 2011 non era
													// gestito
			lCodTipoProvvedimento = "12";
		else if (aCodMotivoRidet.equals("1015")) // 19/11/2015, Rideterminazione della pena detentiva a
													// seguito di conversione della Sanzione Sostitutiva.
			lCodTipoProvvedimento = "04";
		else if (aCodMotivoRidet.equals("1016")) // 19/11/2015, Rideterminazione della pena detentiva a
													// seguito di revoca della Sanzione Sostitutiva.
			lCodTipoProvvedimento = "04";

		return lCodTipoProvvedimento;

	}

	/**
	 * Carica le notifiche della Rideterminazione n.b. i Destinatari variano in funzione della posizione
	 * giuridica: - Istituto di detenzione - Autorità di Esterna per l'Esecuzione (E) - Autorità di Polizia
	 * (C) - UEPE - Magistrato di Sorveglianza (UDS) - Tribunale di Sorveglianza (TDS) - Avvocati
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
		// Magistrato di Sorveglianza: tipo notifica = MS o MM
		// - ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_MDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS).trim().length() > 0) {
			NotificaModel lNotMod_UDS = new NotificaModel();

			/*
			 * ISSUE MAC : filtro sui minorenni Numero MAC : 20191129017 Autore : monica Data : 05/dic/2019
			 * Branch : 11.2.4
			 */
			// lNotMod_UDS.setCodTipoNotifica("MS"); // Magistrato Sorveglianza
			String codUdsTipo = "";
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)) {
				codUdsTipo = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
				if (codUdsTipo.equals("UDSM")) {
					lNotMod_UDS.setCodTipoNotifica("MM");
				} else {
					lNotMod_UDS.setCodTipoNotifica("MS");
				}
				// Magistrato Sorveglianza
			}
			// ***** FINE INTERVENTO MAC_numero_MAC *****//

			lNotMod_UDS.setDataInvio(lDataTrasmissione);

			/*
			 * ISSUE MAC : filtro sui minorenni Numero MAC : 20191129017 Autore : monica Data : 05/dic/2019
			 * Branch : 11.2.4
			 */
			// String lCodiceUff =
			// getCodUfficioByCodTipoUfficioDescrComune("UDS",getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(codUdsTipo,
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));
			// ***** FINE INTERVENTO MAC_numero_MAC *****//
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
		// Tribunale di Sorveglianza: tipo notifica = TS o TM
		// - ICostantiOrdineEsecuzione.CAMPO_SEDE_TDS
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS).trim().length() > 0) {
			NotificaModel lNotMod_TDS = new NotificaModel();

			/*
			 * ISSUE MAC : filtro sui minorenni Numero MAC : 20191129017 Autore : monica Data : 05/dic/2019
			 * Branch : 11.2.4
			 */
			// lNotMod_TDS.setCodTipoNotifica("TS"); // Magistrato Sorveglianza
			String codTribunale = "";
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)) {
				codTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
				if (codTribunale.equals("TDSM")) {
					lNotMod_TDS.setCodTipoNotifica("TM");
				} else {
					lNotMod_TDS.setCodTipoNotifica("TS");
				}
			}
			// Magistrato Sorveglianza
			// ***** FINE INTERVENTO MAC_numero_MAC *****//

			lNotMod_TDS.setDataInvio(lDataTrasmissione);

			/*
			 * ISSUE MAC : filtro sui minorenni Numero MAC : 20191129017 Autore : monica Data : 05/dic/2019
			 * Branch : 11.2.4
			 */
			// String lCodiceUff =
			// getCodUfficioByCodTipoUfficioDescrComune("TDS",getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(codTribunale,
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
			lNotMod_TDS.setUffCodUfficio(lCodiceUff);
			// ***** FINE INTERVENTO MAC_numero_MAC *****//

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
		// Comunicazione alla Polizia: tipo notifica = E
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
		// Autorità per l'esecuzione nel caso di Misure Alternativa: tipo notifica = E
		// - ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E
		// - ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E
		// - ICostantiNotifica.CAMPO_NOTE_E
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E).trim()
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
			siesLogger.debug("Aggiunta Notifica per l'Esecuzione (Mis. Alt.)");
		}

		// ==========================================================================
		// Notifica all'UNEP
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)) {
			NotificaModel lNotModCSSA = new NotificaModel();

			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);
			/*
			 * ISSUE MAC : filtro sui minorenni Numero MAC : 20191129017 Autore : monica Data : 05/dic/2019
			 * Branch : 11.2.4
			 */
			// lNotModCSSA.setCodTipoNotifica("CS");
			if (!this.isRequestParameterNullObj(MinorMask.ComboCSSAId)) {
				// String codTipoNotificaCSSA = getRequestStringParameter(MinorMask.ComboCSSAId);
				lNotModCSSA.setCodTipoNotifica("C");
				/*
				 * if(codTipoNotificaCSSA.equals("UEPE")){ lNotModCSSA.setCodTipoNotifica("CS"); } else{
				 * lNotModCSSA.setCodTipoNotifica("CS"); }
				 */
			}
			// ***** FINE INTERVENTO MAC_numero_MAC *****//

			lNotModCSSA.setDataInvio(lDataTrasmissione);
			lNotModCSSA.setCssIdCssa(lCssa);

			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);

			lArrayNotifiche.add(lNotModCSSA);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiunta Notifica al CSSA");

		}

		return lArrayNotifiche;
	}

}
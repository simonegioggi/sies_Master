package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.action.ICostantiSospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * Azione Helper di tutte le Action dell'Ordine di Eseczione.
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActOrdineEsecuzione extends ActionSiap implements ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Imposta tutti gli attributi dell'Evento
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	protected EventoModel setEventoOrdineEsecuzione(EventoModel aEvento) throws F3BException {
		EventoModel lEve = new EventoModel(aEvento);

		lEve.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEve.setCodTipoProvvedimento("06"); // Tipo Provvedimento = ORDINE ESECUZIONE
		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");
		IDecodifiche lDec = SICOLookupRemote.getDecodificheRemote();
		DecodificheModel lDecMod = lDec.ExRicercaDecodificheByHighValue(aEvento.getDescrMotivo());
		if (lDecMod != null && lDecMod.getCode() != null)
			lEve.setCodMotivo(lDecMod.getCode());

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);

		// UtenteModel lUtenteMod = new
		// UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEve.setCodUfficioAggiornamento(lCodiceUfficio);
		lEve.setDataAggiornamento(DateUtils.getSysDate());

		lEve.setCodEsito("-");
		// lEve.setCodMotivo("-");
		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");
		lEve.setCodMagistrato(calcolaMagistrato());

		return lEve;
	}

	/**
	 * Cerca la Posizione Giuridica, Se Esiste Cerca Altra Causa.
	 * 
	 * @return Integer
	 * @throws Exception
	 */
	protected Integer CercaPosizioneGiuridicaSeEsisteCercaAltraCausa() throws Exception {
		// Questo metodo è utilizzato negli switch()...
		// Per le posizioni giuridiche dalla '01' alla '09'
		// i valori restituiti sono '1','2',....,'9'

		// ============================================================
		// Cerca POSIZIONE_GIURIDICA corrente e se esiste ALTRA_CAUSA
		// ============================================================

		Integer lPosInt = new Integer(
				getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
//		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		IPosizioneGiuridica lCtrlPosizioneGiuridica = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lCtrlPosizioneGiuridica
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		AltraCausaModel lAcModel = new AltraCausaModel();
		if (lPosMod.getAltCauIdAltraCausa() != null || lPosInt == 07) {
			IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
			// lAcModel = lAC.ExRicercaAltraCausaByFascicolo(lFascicoloModel.getIdFascicoloSiep());
			lAcModel = lAC.ExRicercaAltraCausaIstitutoByKey(lPosMod.getAltCauIdAltraCausa());
			if (lAcModel != null) {
				lPosInt = Integer.valueOf(lAcModel.getCodTipoPosGiuridica());
			}
		}

		return lPosInt;
	}

	/**
	 * Cerca la Posizione Giuridica, Se Esiste Cerca Altra Causa
	 * 
	 * @return String
	 * @throws Exception
	 */
	protected String CercaPosizioneGiuridicaSeEsisteCercaAltraCausaString() throws Exception {

		// ============================================================
		// Cerca POSIZIONE_GIURIDICA corrente e se esiste ALTRA_CAUSA
		// ============================================================

		String lPosGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
//		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		IPosizioneGiuridica lCtrlPosizioneGiuridica = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lCtrlPosizioneGiuridica
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		AltraCausaModel lAcModel = new AltraCausaModel();
		if (lPosMod.getAltCauIdAltraCausa() != null || (lPosGiu != null && lPosGiu.equals("07"))) {
			IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
			// lAcModel = lAC.ExRicercaAltraCausaByFascicolo(lFascicoloModel.getIdFascicoloSiep());
			lAcModel = lAC.ExRicercaAltraCausaIstitutoByKey(lPosMod.getAltCauIdAltraCausa());
			if (lAcModel != null) {
				lPosGiu = lAcModel.getCodTipoPosGiuridica();
			}
		}

		return lPosGiu;
	}

	protected EventoModel setEventoAnnMan(EventoModel aEvento) throws F3BException {
		EventoModel lEve = new EventoModel(aEvento);
		lEve.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEve.setCodTipoProvvedimento("04");
		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");
		IDecodifiche lDec = SICOLookupRemote.getDecodificheRemote();
		/*DecodificheModel lDecMod = */lDec.ExRicercaDecodificheByHighValue(aEvento.getDescrMotivo());
		// lEve.setCodMotivo(lDecMod.getCode());

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);

		// UtenteModel lUtenteMod = new
		// UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEve.setCodUfficioAggiornamento(lCodiceUfficio);
		lEve.setDataAggiornamento(DateUtils.getSysDate());

		lEve.setCodEsito("-");
		// lEve.setCodMotivo("-");
		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");
		lEve.setCodMagistrato(calcolaMagistrato());

		return lEve;
	}

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 * 
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheOrdineEsecuzione() throws F3BException {
//		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date lTrasmissione = lDataEmissione;
		ArrayList lNotificheArray = new ArrayList();

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			lTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		String[] lArrayDestinatari = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		String[] lArraySedeDestinatari = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		String[] lArrayNote = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
		String[] lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		// modifica relativa al tipo istituto
		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;
		String lDestinatario_EAE = null;
		String lNote_E = null;

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
			lDestinatario_EAE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		if (!isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
			lNote_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

		// Dimensione dell'Array di Notifiche...
		// Gli Avvocati più Una Notifica di Esecuzione
		// Il caso dell'inserimento del Foglio Complementare!
		int lIndNotifiche = 0;
		int lNumAvvNotifiche = lAvvocati.length;
		if (lArrayDestinatari[0].compareTo("-") == 0)
			lNumAvvNotifiche -= 1;

		// modifica relativa al tipo istituto
		// Si istanzia l'array delle notifiche alla dimensione calcolata
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("E");
		lNotMod.setDataInvio(lTrasmissione);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setNote(lNote_E);

		// Prima notifica esecuzione
		if (lDestinatario_E != null)
			lNotMod.setIstDetIdIstitutoDetenzione(lDestinatario_E);

		if (lDestinatario_EAE != null) {
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_EAE);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setIstDetIdIstitutoDetenzione("");

			lNotMod.setAutoritaEsterna(lAutMod);

		}
		lNotificheArray.add(lNotMod);

		// SETTO TDS
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS).equals("")) {

			NotificaModel lNotModTDS = new NotificaModel();

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("TS");
			lNotModTDS.setDataInvio(lDataEmissione);
			// String lCodiceUff =
			// getCodUfficioByCodTipoUfficioDescrComune("TDS",getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
			String lCodiceUff = "";
			if (!isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS_TDSM)
					&& getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS_TDSM) != null
					&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS_TDSM).equals(
							"")) {
				lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
						getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS_TDSM),
						getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
			} else {
				lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("TDS",
						getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
			}
			lNotModTDS.setUffCodUfficio(lCodiceUff);
			lNotificheArray.add(lNotModTDS);
		}

		// SETTO UDS
		if (!isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS)
				&& getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS) != null
				&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS).equals("")) {

			NotificaModel lNotModUDS = new NotificaModel();

			lNotModUDS.setCodEsito("-");
			lNotModUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModUDS.setDataInserimento(DateUtils.getSysDate());
			lNotModUDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModUDS.setCodTipoNotifica("MS");
			lNotModUDS.setDataInvio(lDataEmissione);
			// String lCodiceUff =
			// getCodUfficioByCodTipoUfficioDescrComune("UDS",getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS));
			String lCodiceUff = "";
			if (!isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM)
					&& getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM) != null
					&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM).equals(
							"")) {
				lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
						getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM),
						getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS));
			} else {
				lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("UDS",
						getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS));
			}
			lNotModUDS.setUffCodUfficio(lCodiceUff);
			lNotificheArray.add(lNotModUDS);
		}

		// Notifiche all'avvocato
		while (lIndNotifiche < lNumAvvNotifiche) {

			NotificaModel lNot = new NotificaModel();
			lNot.setCodTipoNotifica("N");
			lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
			lNot.setNote(lArrayNote[lIndNotifiche]);
			lNot.setDataInvio(lTrasmissione);
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lArrayDestinatari[lIndNotifiche]);

			//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
			//ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lArraySedeDestinatari[lIndNotifiche]));
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lArraySedeDestinatari[lIndNotifiche]));
			//FINE: MEV_21
			
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNot.setAutoritaEsterna(lAut);
			lIndNotifiche++;
			lNotificheArray.add(lNot);
		}

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")) {
			String lPoliziaC = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			String lSedePoliziaC = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
			NotificaModel lNotModPolC = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_C)) {
				String lNotePolizia = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C);
				lNotModPolC.setNote(lNotePolizia);
			}
			lNotModPolC.setCodEsito("-");
			lNotModPolC.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPolC.setDataInserimento(DateUtils.getSysDate());
			lNotModPolC.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPolC.setCodTipoNotifica("AA");
			lNotModPolC.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPoliziaC);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePoliziaC));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPolC.setAutoritaEsterna(lAut);

			lNotificheArray.add(lNotModPolC);
		}

		// Controllo Foglio Complementare per Ordini di Sospensione Simeone
		NotificaModel lNot = new NotificaModel();
		if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE)) {
			if (isRequestChecked(FOGLIO_COMPLEMENTARE)) {
				lNot = new NotificaModel();

				lNot.setCodTipoNotifica("C");
				lNot.setDataInvio(lTrasmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(lCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(lCodiceUfficio);

				ComuneModel lComCasellarioMod = new ComuneModel(
						getCodComuneByDescr(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS)));
				/*
				 * SedeGiudiziariaModel lSedeGiuMod = null; String lCodCas =
				 * lFascicoloModel.getSoggetto().getCodComuneCasellario(); ISedeGiudiziaria lCtrl =
				 * SIEPLookupRemote.getSedeGiudiziariaRemote(); SedeGiudiziariaModel lSedGiuMod =
				 * lCtrl.ExRicercaSedeGiudiziariaByKey(lCodCas);
				 */

				AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

				lAutMod.setCodTipoAutorita("24");

				// lAutMod.setCodSede(lSedGiuMod.getCodComune());
				lAutMod.setCodSede(lComCasellarioMod.getCodComune());
				lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
				lAutMod.setCodUfficioInserimento(lCodiceUfficio);
				lAutMod.setDataInserimento(DateUtils.getSysDate());

				// Setto l'Autorita Esterna per la notifica corrente
				lNot.setAutoritaEsterna(lAutMod);

				lNotificheArray.add(lNot);
			}
		}

		// Modifica relativa a notifiche per autorità competente BDMC
		String lDestinatario_EAE_BDMC = null;
		String lSedeDestinatario_E_BDMC = null;
		String lDestinatario_E_BDMC = null;
		String lNote_E_BDMC = null;
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E + "_BDMC")) {
			lDestinatario_EAE_BDMC = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E + "_BDMC");
			lSedeDestinatario_E_BDMC = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E + "_BDMC");
		}
		if (!isRequestParameterNullObj("detenzioneBdmc"))
			lDestinatario_E_BDMC = getRequestStringParameter("detenzioneBdmc");

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E + "_BDMC"))
			lNote_E_BDMC = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E + "_BDMC");

		// modifica relativa al tipo istituto
		// Si istanzia l'array delle notifiche alla dimensione calcolata
		if ((lDestinatario_EAE_BDMC != null) && (lDestinatario_EAE_BDMC.compareTo("-") != 0)) {
			NotificaModel lNotModBdmc = new NotificaModel();
			lNotModBdmc.setCodTipoNotifica("E");
			lNotModBdmc.setDataInvio(lTrasmissione);
			lNotModBdmc.setCodEsito("-");
			lNotModBdmc.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModBdmc.setDataInserimento(DateUtils.getSysDate());
			lNotModBdmc.setCodUfficioInserimento(lCodiceUfficio);
			lNotModBdmc.setNote(lNote_E_BDMC);

			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_EAE_BDMC);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeDestinatario_E_BDMC));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotModBdmc.setIstDetIdIstitutoDetenzione("");

			lNotModBdmc.setAutoritaEsterna(lAutMod);

			lNotificheArray.add(lNotModBdmc);
		}

		// modifica relativa alla notifica per BDMC
		// Si istanzia l'array delle notifiche alla dimensione calcolata
		if (lDestinatario_E_BDMC != null && !lDestinatario_E_BDMC.equals("")) {
			NotificaModel lNotModBdmc = new NotificaModel();
			lNotModBdmc.setCodTipoNotifica("E");
			lNotModBdmc.setDataInvio(lTrasmissione);
			lNotModBdmc.setCodEsito("-");
			lNotModBdmc.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModBdmc.setDataInserimento(DateUtils.getSysDate());
			lNotModBdmc.setCodUfficioInserimento(lCodiceUfficio);
			lNotModBdmc.setNote(lNote_E_BDMC);

			// Prima notifica esecuzione BDMC
			lNotModBdmc.setIstDetIdIstitutoDetenzione(lDestinatario_E_BDMC);

			lNotificheArray.add(lNotModBdmc);
		}
		// Fine Modifica relativa a notifiche per autorità competente BDMC

		// ==========================================================================
		// 09/12/2010 AUTORITA' PER LA RESTITUZIONE ORDINE ESECUZIONE
		// ==========================================================================
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_RESTITUZIONE_OE)) {
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("")
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("-")) {
				NotificaModel lNotModPol = new NotificaModel();
				Date lDataInserimento = DateUtils.getSysDate();
				lNotModPol.setCodTipoNotifica("R");

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R)) {
					String lNotePolizia = this
							.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R);
					lNotModPol.setNote(lNotePolizia);
				}

				lNotModPol.setCodEsito("-");
				lNotModPol.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_DATA_EMISSIONE,
						"dd-MM-yyyy"));

				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(lDataInserimento);
				lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				String lPolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R);
				String lSedePolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R);

				lAut.setCodTipoAutorita(lPolizia);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
				lAut.setCodSede(lComMod.getCodComune());

				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(lDataInserimento);

				lNotModPol.setAutoritaEsterna(lAut);

				lNotificheArray.add(lNotModPol);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiunta Notifica Restituzione OE");
			}
		}

//		String lPosGiurid = "00";
//		if (!isRequestParameterNullObj(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA))
//			lPosGiurid = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		return (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
	}

	/**
	 * calcolaMagistrato
	 * 
	 * @return
	 */

	protected String calcolaMagistrato() throws F3BException {
		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME));
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME));

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}

			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}

		return lCodiceMagistrato;
	}

	protected EventoModel setEventoMisuraAlternativa(EventoModel aEvento) throws F3BException {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		EventoModel lEve = new EventoModel(aEvento);
		lEve.setCodTipoEvento("01");
		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");
		lEve.setEveIdEvento(aEvento.getIdEvento());
		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);
		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEve.setCodUfficioAggiornamento(lCodiceUfficio);
		lEve.setDataAggiornamento(DateUtils.getSysDate());
		lEve.setFlagDocumentoRegistrato(null);
		if (aEvento.getCodEsito() != null) {
			lEve.setCodEsito(aEvento.getCodEsito());
		} else {
			lEve.setCodEsito("-");
		}
		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");
		lEve.setCodMagistrato(calcolaMagistrato());

		return lEve;
	}

	protected EventoNotificaModel getEventoVariazioneDecorrenzaScadenza(EventoModel aEvento)
			throws F3BException {
		EventoNotificaModel lEveModNew = new EventoNotificaModel();

		lEveModNew.getMagistrato().setCodMagistrato(
				getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModNew.getEvento().setCodMotivo(aEvento.getCodMotivo());
		lEveModNew.getEvento().setCodTipoEvento("01");
		lEveModNew.getEvento().setCodTipoProvvedimento("12");
		lEveModNew.getEvento().setCodEsito("-");
		lEveModNew.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveModNew.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveModNew.getEvento().setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveModNew.getEvento().setCodLuogoDestinatario("-");
		lEveModNew.getEvento().setCodTipoUfficioDestinatario("-");
		lEveModNew.getEvento().setDataEmissione(
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveModNew.getEvento().setDataTrasmissioneAtti(
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		// lEveModNew.getEvento().setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
		lEveModNew.getEvento().setCodMagistrato(
				getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModNew.getEvento().setFlagStampaSiep("S");
		lEveModNew.getEvento().setFlagVideoSiep("S");
		lEveModNew.getEvento().setFlagPiuMeno("-");

		lEveModNew.getEvento().setDataRicezioneAtti(
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEveModNew.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveModNew.getEvento().setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEveModNew.getEvento().setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEveModNew.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		if (getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_MOTIVAZIONI_VARIAZIONE).trim().length() > 0) {

			Vector lCampiNote = new Vector();
			CampoNotaModel lCampoNotaMod = new CampoNotaModel();
			lCampoNotaMod.setDescr(getRequestStringParameter(
					ICostantiOrdineEsecuzione.CAMPO_MOTIVAZIONI_VARIAZIONE).trim());
			lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
			lCampoNotaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampoNotaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lCampoNotaMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			lCampiNote.add(lCampoNotaMod);

			lEveModNew.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
		}

		return lEveModNew;
	}

	protected NotificaModel[] setNotificheVariazioneDecorrenzaScadenza() throws F3BException {
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date lTrasmissione = lDataEmissione;
		ArrayList lNotificheArray = new ArrayList();

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			lTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		String[] lArrayDestinatari = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		String[] lArraySedeDestinatari = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		String[] lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		// modifica relativa al tipo istituto
//		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;
//		String lDestinatario_EAE = null;
		String lNote_E = null;

		if (!isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDestinatario_E = getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		// Dimensione dell'Array di Notifiche...
		// Gli Avvocati più Una Notifica di Esecuzione
		// Il caso dell'inserimento del Foglio Complementare!
		int lIndNotifiche = 0;
		int lNumAvvNotifiche = lAvvocati.length;
		if (lArrayDestinatari[0].compareTo("-") == 0)
			lNumAvvNotifiche -= 1;

		// modifica relativa al tipo istituto
		// Si istanzia l'array delle notifiche alla dimensione calcolata
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("C");
		lNotMod.setDataInvio(lTrasmissione);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setNote(lNote_E);

		// Prima notifica esecuzione
		if (lDestinatario_E != null)
			lNotMod.setIstDetIdIstitutoDetenzione(lDestinatario_E);

//		if (lDestinatario_EAE != null) {
//			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
//			lAutMod.setCodTipoAutorita(lDestinatario_EAE);
//			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeDestinatario_E));
//			lAutMod.setCodSede(lComModel.getCodComune());
//			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
//			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
//			lAutMod.setDataInserimento(DateUtils.getSysDate());
//			lNotMod.setIstDetIdIstitutoDetenzione("");
//			lNotMod.setAutoritaEsterna(lAutMod);
//		}

		lNotificheArray.add(lNotMod);

		// Notifiche all'avvocato
		while (lIndNotifiche < lNumAvvNotifiche) {
			NotificaModel lNot = new NotificaModel();
			lNot.setCodTipoNotifica("N");
			lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
			// lNot.setNote(lArrayNote[lIndNotifiche]);
			lNot.setDataInvio(lTrasmissione);
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lArrayDestinatari[lIndNotifiche]);

			//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
			// ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lArraySedeDestinatari[lIndNotifiche]));
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lArraySedeDestinatari[lIndNotifiche]));
			//FINE: MEV_21
			
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNot.setAutoritaEsterna(lAut);
			lIndNotifiche++;
			lNotificheArray.add(lNot);
		}
		return (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
	}

	/**
	 * Imposta tutti gli attributi dell'Evento per il Decreto di Sospensione OE Legge Alfano.
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	protected EventoModel setEventoDecretoSospensione(EventoModel aEvento) throws F3BException {
		EventoModel lEve = new EventoModel(aEvento);

		lEve.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEve.setCodTipoProvvedimento("04"); // Tipo Provvedimento = PROVVEDIMENTO
		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");
		IDecodifiche lDec = SICOLookupRemote.getDecodificheRemote();
		DecodificheModel lDecMod = lDec.ExRicercaDecodificheByHighValue(aEvento.getDescrMotivo());
		if (lDecMod != null && lDecMod.getCode() != null)
			lEve.setCodMotivo(lDecMod.getCode());

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEve.setCodUfficioAggiornamento(lCodiceUfficio);
		lEve.setDataAggiornamento(DateUtils.getSysDate());

		lEve.setCodEsito("-");
		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");
		lEve.setCodMagistrato(calcolaMagistrato());

		return lEve;
	}

	// AMBROS : DECRETO LEGGE GIUGNO 2013 - Notifiche per Comunicazione Legge 78 del 2013
	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 * 
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheL78del2013() throws F3BException {
//		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date lTrasmissione = lDataEmissione;
		ArrayList lNotificheArray = new ArrayList();

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			lTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		// Difensore
		String[] lArrayDestinatari = null;
		String[] lArraySedeDestinatari = null;
		String[] lArrayNote = null;
		String[] lAvvocati = null;

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
			lArrayDestinatari = this
					.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			lArraySedeDestinatari = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			lArrayNote = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
			lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		}

		// modifica relativa al tipo istituto
		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;
		String lDestinatario_EAE = null;
		String lNote_E = null;

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			lDestinatario_EAE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		if (!isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
			lNote_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

		// Dimensione dell'Array di Notifiche...
		// Gli Avvocati più Una Notifica di Esecuzione
		int lIndNotifiche = 0;
		int lNumAvvNotifiche = 0;
		if (lAvvocati != null)
			lNumAvvNotifiche = lAvvocati.length;

		if (lArrayDestinatari != null)
			if (lArrayDestinatari[0].compareTo("-") == 0)
				lNumAvvNotifiche -= 1;

		// modifica relativa al tipo istituto
		// Si istanzia l'array delle notifiche alla dimensione calcolata
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("E");
		lNotMod.setDataInvio(lTrasmissione);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setNote(lNote_E);

		// Prima notifica esecuzione
		if (lDestinatario_E != null)
			lNotMod.setIstDetIdIstitutoDetenzione(lDestinatario_E);

		if (lDestinatario_EAE != null) {
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_EAE);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setIstDetIdIstitutoDetenzione("");

			lNotMod.setAutoritaEsterna(lAutMod);

		}

		if ((lDestinatario_E != null) || (lDestinatario_EAE != null)) {
			lNotificheArray.add(lNotMod);
		}

		// SETTO Autorita Controllo
		/*
		 * if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C) &&
		 * getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C)!= null &&
		 * !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C).equals("")) { NotificaModel
		 * lNotModctr = new NotificaModel();
		 * 
		 * lNotModctr.setCodEsito("-"); lNotModctr.setCodOperatoreInserimento(lCodiceOperatore);
		 * lNotModctr.setDataInserimento(DateUtils.getSysDate());
		 * lNotModctr.setCodUfficioInserimento(lCodiceUfficio); lNotModctr.setCodTipoNotifica("PC");
		 * lNotModctr.setNote(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_NOTE_C) );
		 * lNotModctr.setDataInvio(lDataEmissione); String lCodiceUff =
		 * getCodUfficioByCodTipoUfficioDescrComune
		 * ("TDS",getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS));
		 * lNotModctr.setUffCodUfficio(lCodiceUff); lNotificheArray.add(lNotModctr); }
		 */
		// SETTO UDS
		if (!isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA)
				&& getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA) != null
				&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA).equals(
						"")) {
			NotificaModel lNotModUDS = new NotificaModel();

			lNotModUDS.setCodEsito("-");
			lNotModUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModUDS.setDataInserimento(DateUtils.getSysDate());
			lNotModUDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModUDS.setCodTipoNotifica("MS");
			lNotModUDS.setNote(getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_UDS));
			lNotModUDS.setDataInvio(lDataEmissione);
			// String lCodiceUff =
			// getCodUfficioByCodTipoUfficioDescrComune("UDS",getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA));
			String lCodiceUff = "";
			if (!isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM)
					&& getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM) != null
					&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM).equals(
							"")) {
				lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
						getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM),
						getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA));
			} else {
				lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("UDS",
						getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA));
			}
			lNotModUDS.setUffCodUfficio(lCodiceUff);
			lNotificheArray.add(lNotModUDS);
		}

		// Notifiche all'avvocato
		if (lAvvocati != null) {
			if (lAvvocati.length > 0 && lArrayDestinatari[0].compareTo("-") != 0) {
				while (lIndNotifiche < lNumAvvNotifiche) {
					NotificaModel lNot = new NotificaModel();
					lNot.setCodTipoNotifica("N");
					lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
					lNot.setNote(lArrayNote[lIndNotifiche]);
					lNot.setDataInvio(lTrasmissione);
					lNot.setCodEsito("-");
					lNot.setCodOperatoreInserimento(lCodiceOperatore);
					lNot.setDataInserimento(DateUtils.getSysDate());
					lNot.setCodUfficioInserimento(lCodiceUfficio);

					AutoritaEsternaModel lAut = new AutoritaEsternaModel();
					lAut.setCodTipoAutorita(lArrayDestinatari[lIndNotifiche]);

					
					//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//					ComuneModel lComMod = new ComuneModel(
//							getCodComuneByDescr(lArraySedeDestinatari[lIndNotifiche]));
					ComuneModel lComMod = new ComuneModel(
							getCodComuneByDescrFlagVal(lArraySedeDestinatari[lIndNotifiche]));
					//FINE: MEV_21
					lAut.setCodSede(lComMod.getCodComune());
					lAut.setCodOperatoreInserimento(lCodiceOperatore);
					lAut.setCodUfficioInserimento(lCodiceUfficio);
					lAut.setDataInserimento(DateUtils.getSysDate());

					// Setto l'Autorita Esterna per la notifica corrente
					lNot.setAutoritaEsterna(lAut);
					lIndNotifiche++;
					lNotificheArray.add(lNot);
				}
			}
		}

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")) {
			String lPoliziaC = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			String lSedePoliziaC = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
			NotificaModel lNotModPolC = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_C)) {
				String lNotePolizia = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C);
				lNotModPolC.setNote(lNotePolizia);
			}

			lNotModPolC.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C));

			lNotModPolC.setCodEsito("-");
			lNotModPolC.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPolC.setDataInserimento(DateUtils.getSysDate());
			lNotModPolC.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPolC.setCodTipoNotifica("PC");
			lNotModPolC.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPoliziaC);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePoliziaC));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPolC.setAutoritaEsterna(lAut);

			lNotificheArray.add(lNotModPolC);
		}

		// Modifica relativa a notifiche per autorità competente BDMC
		String lDestinatario_EAE_BDMC = null;
		String lSedeDestinatario_E_BDMC = null;
		String lDestinatario_E_BDMC = null;
		String lNote_E_BDMC = null;
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E + "_BDMC")) {
			lDestinatario_EAE_BDMC = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E + "_BDMC");
			lSedeDestinatario_E_BDMC = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E + "_BDMC");
		}
		if (!isRequestParameterNullObj("detenzioneBdmc"))
			lDestinatario_E_BDMC = getRequestStringParameter("detenzioneBdmc");

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E + "_BDMC"))
			lNote_E_BDMC = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E + "_BDMC");

		// modifica relativa al tipo istituto
		// Si istanzia l'array delle notifiche alla dimensione calcolata
		if ((lDestinatario_EAE_BDMC != null) && (lDestinatario_EAE_BDMC.compareTo("-") != 0)) {
			NotificaModel lNotModBdmc = new NotificaModel();
			lNotModBdmc.setCodTipoNotifica("E");
			lNotModBdmc.setDataInvio(lTrasmissione);
			lNotModBdmc.setCodEsito("-");
			lNotModBdmc.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModBdmc.setDataInserimento(DateUtils.getSysDate());
			lNotModBdmc.setCodUfficioInserimento(lCodiceUfficio);
			lNotModBdmc.setNote(lNote_E_BDMC);

			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_EAE_BDMC);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeDestinatario_E_BDMC));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotModBdmc.setIstDetIdIstitutoDetenzione("");

			lNotModBdmc.setAutoritaEsterna(lAutMod);

			lNotificheArray.add(lNotModBdmc);
		}

		// modifica relativa alla notifica per BDMC
		// Si istanzia l'array delle notifiche alla dimensione calcolata
		if (lDestinatario_E_BDMC != null) {
			NotificaModel lNotModBdmc = new NotificaModel();
			lNotModBdmc.setCodTipoNotifica("E");
			lNotModBdmc.setDataInvio(lTrasmissione);
			lNotModBdmc.setCodEsito("-");
			lNotModBdmc.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModBdmc.setDataInserimento(DateUtils.getSysDate());
			lNotModBdmc.setCodUfficioInserimento(lCodiceUfficio);
			lNotModBdmc.setNote(lNote_E_BDMC);

			// Prima notifica esecuzione BDMC
			lNotModBdmc.setIstDetIdIstitutoDetenzione(lDestinatario_E_BDMC);

			lNotificheArray.add(lNotModBdmc);
		}
		// Fine Modifica relativa a notifiche per autorità competente BDMC

		// ==========================================================================
		// 09/12/2010 AUTORITA' PER LA RESTITUZIONE ORDINE ESECUZIONE
		// ==========================================================================
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_RESTITUZIONE_OE)) {
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("")
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("-")) {
				NotificaModel lNotModPol = new NotificaModel();
				Date lDataInserimento = DateUtils.getSysDate();
				lNotModPol.setCodTipoNotifica("R");

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R)) {
					String lNotePolizia = this
							.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R);
					lNotModPol.setNote(lNotePolizia);
				}

				lNotModPol.setCodEsito("-");
				lNotModPol.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_DATA_EMISSIONE,
						"dd-MM-yyyy"));

				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(lDataInserimento);
				lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				String lPolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R);
				String lSedePolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R);

				lAut.setCodTipoAutorita(lPolizia);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
				lAut.setCodSede(lComMod.getCodComune());

				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(lDataInserimento);

				lNotModPol.setAutoritaEsterna(lAut);

				lNotificheArray.add(lNotModPol);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiunta Notifica Restituzione OE");
			}
		}

//		String lPosGiurid = "00";
//		if (!isRequestParameterNullObj(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA))
//			lPosGiurid = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		return (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
	}

}
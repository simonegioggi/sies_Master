package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActInserisciVariazioneDecorrenzaScadenzaQC
 * </p>
 * <p>
 * Description: Inserimento della Variazione decorrenza e scadenza pena Questa Causa
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciVariazioneDecorrenzaScadenzaQC extends ActOrdineEsecuzione
		implements ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================
		// Imposta l'evento (Comunicazione)
		// ==========================================================
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod.getEvento().setCodMotivo("0368"); // 0368 - Variazione decorrenza e scadenza pena QC
		lEveNotMod = getEventoVariazioneDecorrenzaScadenza(lEveNotMod.getEvento());
		lEveNotMod.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Eventuale data pervenimento Richiesta
		if (!isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE)
				&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE)
						.equals("")) {
			Date lDataRichiesta = getRequestDateParameter(
					ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE,
					ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE,
					ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE);
			lEveNotMod.getEvento().setDataRichiesta(lDataRichiesta);
		}

		// =======================================================
		// Carico ed Inserisco l'array di Notifiche nell'Evento
		// =======================================================
		NotificaModel[] lNotifiche = this.setNotificheVariazioneDecorrenzaScadenzaQC();
		// for (int i=0; i<lNotifiche.length;i++){
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("i: "+lNotifiche[i]);
		// }
		lEveNotMod.setNotifiche(lNotifiche);

		// ==========================================================================
		// Recupero l'ultima pena per verificare se trattasi di ergastolo o meno
		// ed eventualmente per duplicarla modificando silo l'inizio pena
		// ==========================================================================
		PenaResiduaModel lUltimaPenMod = new PenaResiduaModel();
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lUltimaPenMod = IPenRes
				.ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(lFascicoloModel.getIdFascicoloSiep());

		PenaResiduaModel lNuovaPenaModel = new PenaResiduaModel();
		lNuovaPenaModel.setFlagPenaSospesa(null);

		// ==========================================================================
		// Gestione Pena Residua: rifaccio il calcolo e modifico il fine pena
		// ==========================================================================
		Date lNuovaDataInizio = getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO,
				ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO, ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO);
		// Se Ergastolo Non c'e lNuovaDataFine
		// Data fine pena
		Date lNuovaDataFine = null;
		if (lUltimaPenMod.getFlagErgastolo() == null || lUltimaPenMod.getFlagErgastolo().equals("N")) {
			lNuovaDataFine = getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE);
		}

		// ========================================================================
		// Recupero i quantum di pena Validati che concorrono al calcolo della
		// pena
		// ========================================================================
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain
				.calcoloPena(lFascicoloModel.getIdFascicoloSiep(), null);

		// ==========================================================================
		// NO ERGASTOLO: calcolo le date in funzione del quantum a sistema e della
		// data di arresto (data inizio)
		// ==========================================================================
		if (lUltimaPenMod.getFlagErgastolo() == null || lUltimaPenMod.getFlagErgastolo().equals("N")) {
			lNuovaPenaModel = lCalcoloPenaModel.getPenaDaEspiare(lNuovaDataInizio, null, "all");
			lNuovaPenaModel.setDataFinePresunta(lNuovaPenaModel.getDataFine());
			lNuovaPenaModel.setDataFine(lNuovaDataFine);
		} else { // ergastolo
			lNuovaPenaModel = new PenaResiduaModel(lUltimaPenMod);
			Date lDataFinePenaErga = DateUtils.getDate(9999, 12, 31);
			lNuovaPenaModel.setDataFine(lDataFinePenaErga);
		}

		lNuovaPenaModel.setDataInizio(lNuovaDataInizio);

		lNuovaPenaModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lNuovaPenaModel.setFlagErgastolo(lUltimaPenMod.getFlagErgastolo());
		lNuovaPenaModel.setDiesAQuo("S");
		lNuovaPenaModel.setFlagValidato("N");

		lNuovaPenaModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lNuovaPenaModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lNuovaPenaModel.setDataInserimento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lNuovaPenaModel = " + lNuovaPenaModel);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEveMod = " + lEveNotMod.getEvento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lNotifiche = " + lEveNotMod.getNotifiche().length);
		for (int i = 0; i < lEveNotMod.getNotifiche().length; i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(lEveNotMod.getNotifiche()[i]);
		}
		// ==========================================================================
		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti
		// viene modificato
		// n.b. in realtà noi stiamo inserendo una comunicazione (01-12-0249) e non
		// un OE.
		// ==========================================================================
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione
				.ExInserisciVariazioneDecorrenzaScadenzaQC(lEveNotMod, lNuovaPenaModel);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioVariazioneDecorrenzaScadenzaQC&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * Carica le notifiche: Se detenuto (03) - Istituto di Detenzione + Avvocati Se agli Arresti Domiciliari
	 * (04) - Autorità per l'esecuzione - UEPE - Tribunale di Sorveglianza (TDS) - Ufficio/Magistrato di
	 * Sorveglianza (MDS) - Avvocati
	 * 
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheVariazioneDecorrenzaScadenzaQC() throws F3BException {
		/*
		 * Istituto - ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE
		 * 
		 * Autorità Esterna - ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E -
		 * ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E - ICostantiNotifica.CAMPO_NOTE_E
		 * 
		 * UEPE - ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_SSPA - ICostantiOrdineEsecuzione.CAMPO_NOTE_SSPA
		 * 
		 * MDS - ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS - ICostantiOrdineEsecuzione.CAMPO_NOTE_UDS
		 * 
		 * TDS - ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS - ICostantiOrdineEsecuzione.CAMPO_NOTE_TDS
		 * 
		 * Campi Notifiche agli Avvocati: - ICostantiAvvocato.CAMPO_ID_AVVOCATO -
		 * ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA - ICostantiAutoritaEsterna.CAMPO_COD_SEDE -
		 * ICostantiNotifica.CAMPO_NOTE
		 */

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date lDataTrasmissione = lDataEmissione;

		ArrayList lNotificheArray = new ArrayList();

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		// ==========================================================================
		// ISTITUTO DI DETENZIONE
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {
			String lIdIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			NotificaModel lNotMod = new NotificaModel();
			lNotMod.setCodTipoNotifica("C");
			lNotMod.setIstDetIdIstitutoDetenzione(lIdIstituto);
			lNotMod.setDataInvio(lDataTrasmissione);

			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);

			lNotificheArray.add(lNotMod);
		}

		// ==========================================================================
		// AUTORITA' PER L'ESECUZIONE
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("")) {
			String lCodTipo = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSede = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			String lNote = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel lNotMod = new NotificaModel();
			lNotMod.setCodTipoNotifica("E");
			lNotMod.setDataInvio(lDataTrasmissione);
			lNotMod.setNote(lNote);
			lNotMod.setCodEsito("-");

			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);

			// Autorità Esterna
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lCodTipo);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSede));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNotMod.setAutoritaEsterna(lAut);

			lNotificheArray.add(lNotMod);
		}

		// ==========================================================================
		// UEPE - Ufficio Esecuzione Penale Esterna
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_SSPA)
				&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_SSPA).equals("")) {
			BigDecimal lIdSSPA = new BigDecimal(
					getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_SSPA));
			String lNote = this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_SSPA);

			NotificaModel lNotificheServiziSociali = new NotificaModel();
			lNotificheServiziSociali.setCodTipoNotifica("N");
			lNotificheServiziSociali.setCssIdCssa(lIdSSPA);
			lNotificheServiziSociali.setDataInvio(lDataTrasmissione);
			lNotificheServiziSociali.setNote(lNote);
			lNotificheServiziSociali.setCodEsito("-");

			lNotificheServiziSociali.setCodOperatoreInserimento(lCodiceOperatore);
			lNotificheServiziSociali.setDataInserimento(DateUtils.getSysDate());
			lNotificheServiziSociali.setCodUfficioInserimento(lCodiceUfficio);

			lNotificheArray.add(lNotificheServiziSociali);
		}

		// ==========================================================================
		// Ufficio/Magistrato Di Sorveglianza
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS)
				&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS).equals("")) {
			String lSede = this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS);
			String lNote = this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_UDS);

			NotificaModel lNotModUDS = new NotificaModel();
			lNotModUDS.setCodTipoNotifica("MS");
			lNotModUDS.setDataInvio(lDataTrasmissione);
			lNotModUDS.setNote(lNote);

			lNotModUDS.setCodEsito("-");

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("UDS", lSede);
			lNotModUDS.setUffCodUfficio(lCodiceUff);

			lNotModUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModUDS.setDataInserimento(DateUtils.getSysDate());
			lNotModUDS.setCodUfficioInserimento(lCodiceUfficio);

			lNotificheArray.add(lNotModUDS);
		}

		// ==========================================================================
		// Tribunale di Sorveglianza
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS)
				&& !getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS).equals("")) {
			String lSede = this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS);
			String lNote = this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_TDS);

			NotificaModel lNotModTDS = new NotificaModel();
			lNotModTDS.setCodTipoNotifica("TS");
			lNotModTDS.setDataInvio(lDataTrasmissione);
			lNotModTDS.setNote(lNote);
			lNotModTDS.setCodEsito("-");

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("TDS", lSede);
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);

			lNotificheArray.add(lNotModTDS);
		}

		// ==========================================================================
		// Caricamento Avvocati
		// ==========================================================================
		String[] lArrayDestinatari = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		String[] lArraySedeDestinatari = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		String[] lArrayNote = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
		String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		int lIndNotifiche = 0;
		int lNumAvvNotifiche = lAvvocati.length;
		if (lArrayDestinatari[0].compareTo("-") == 0) // ????
			lNumAvvNotifiche -= 1;

		// Notifiche all'avvocato
		while (lIndNotifiche < lNumAvvNotifiche) {
			NotificaModel lNot = new NotificaModel();
			lNot.setCodTipoNotifica("N");
			lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
			lNot.setNote(lArrayNote[lIndNotifiche]);
			lNot.setDataInvio(lDataTrasmissione);
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);

			// Autorità Esterna Per la Notifica All'avvocato
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

		return (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
	}

}
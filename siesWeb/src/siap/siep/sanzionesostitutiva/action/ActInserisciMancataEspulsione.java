package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * <p>
 * Title: ActInserisciMancataEspulsione
 * </p>
 * <p>
 * Description: Classe Action per la l'Inserimento dell'annotazione Mancata espulsione.
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

public class ActInserisciMancataEspulsione extends ActionSiap implements ICostantiSanzioneSostitutiva {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * La funzione prevede l'inserimento di un unico evento (per ora): 01-25-{da combo} Annotazione Mancata
	 * Espulsione
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		//
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// //==========================================================================
		// // Recupero la Sanzione Sostitutiva In Sentenza
		// //==========================================================================
		// IPenaComplessiva lCtrlPenaComp = SIEPLookupRemote.getPenaComplessivaRemote();
		// PenaComplessivaSanzioneSostitutivaModel lPenaCompSanzModel =
		// lCtrlPenaComp.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lIdFascicolo);
		//
		// SanzioneSostitutivaModel lSanzSost = lPenaCompSanzModel.getSanzioneSostitutiva();

		// ======================================
		// Carico i dati dell'evento Annotazione
		// ======================================
		EventoModel lEventoAnnotazione = new EventoModel();

		lEventoAnnotazione.setFasSieIdFascicoloSiep(lIdFascicolo);

		lEventoAnnotazione.setCodTipoEvento("01"); // 01 - Provvedimento
		lEventoAnnotazione.setCodTipoProvvedimento("25"); // 25 - Annotazione
		lEventoAnnotazione.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)); // Da
																											// combo
																											// Annotazione
		lEventoAnnotazione.setCodEsito("-");

		lEventoAnnotazione.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEventoAnnotazione.setCodLuogoEmittente(getCodComuneUtenteConnesso());

		// Data Emissione= Data Annotazione (per ora)
		lEventoAnnotazione.setDataEmissione(getRequestDateParameter(
				ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
		// lEventoVerbale.setDataRicezioneAtti(aValore);

		lEventoAnnotazione.setFlagDocumentoRegistrato("N");
		lEventoAnnotazione.setFlagStampaSiep("S");
		lEventoAnnotazione.setFlagVideoSiep("S");

		if (!this.isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			lEventoAnnotazione.setCodMagistrato(
					this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		} else {
			lEventoAnnotazione.setCodMagistrato("-");
		}

		lEventoAnnotazione.setCodLuogoDestinatario("-");
		lEventoAnnotazione.setCodUfficioDestinatario("-");
		lEventoAnnotazione.setCodTipoUfficioDestinatario("-");

		lEventoAnnotazione.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEventoAnnotazione.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEventoAnnotazione.setDataInserimento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEventoAnnotazione = " + lEventoAnnotazione);

		// ==========================================================================
		// Carico il verbale.
		// n.b. carico un verbale per appoggiare i dati delle comunicazione mancata
		// espulsione
		// ==========================================================================
		VerbaleModel lVerbaleMod = new VerbaleModel();

		lVerbaleMod.setCodTipoVerbale("08"); // Nota Mancata Espulsione
		lVerbaleMod.setNumeroProtocollo(getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO));
		// Data Emissione = Data Nota
		lVerbaleMod.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));
		// Data Pervenimento = data Annotazione
		lVerbaleMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
		// Ufficio Firmatario
		lVerbaleMod.setCodTipoUfficioFirmatario(
				getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));

		// n.b. l'ufficio deve essere una questura,quindi prima verifico che il comune
		// esiste, e quindi che sia il comune di una Questura
		ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
				getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)));

		// String lDesc =
		// DecodificheUtils.getFiltrobyCode(DecodificheManager.getInstance().getQuesture(),lComMod.getCodComune());
		Iterator itx = DecodificheManager.getInstance().getQuesture().iterator();
		String lCode = lComMod.getCodComune();
		boolean lIsQuestura = false;
		while (itx.hasNext()) {
			DecodificheModel ldecodeModel = (DecodificheModel) itx.next();
			if ((ldecodeModel.getCode()).equals(lCode)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("!!!!!!!! TROVATO!!!!!! ");
				lIsQuestura = true;
				break;
			}
		}

		if (!lIsQuestura) {
			// il comune esiste ma non è sede di una Questura
			throw new F3BException(F3BException.USER_MESSAGE,
					"Il comune " + lComMod.getDescrizione() + " non risulta essere sede di una Questura");
		}

		lVerbaleMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
		lVerbaleMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE)); // indirizzo non previsto
																						// nella form

		lVerbaleMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lVerbaleMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lVerbaleMod.setDataInserimento(lEventoAnnotazione.getDataInserimento());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Verbale = " + lVerbaleMod);
		// .............................

		EventoNotificaModel lEvNotModel = new EventoNotificaModel();
		lEvNotModel.setEvento(lEventoAnnotazione);

		// ==========================================================================
		// Carico l'eventuale nota
		// ==========================================================================
		CampoNotaModel lCampoNotaModel = null;
		if (!isRequestParameterNullObj("CampoNota") && getRequestStringParameter("CampoNota").length() > 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la nota ");
			String lTestoNota = getRequestStringParameter("CampoNota");

			lCampoNotaModel = new CampoNotaModel();

			lCampoNotaModel.setProgressivo(new BigDecimal("1"));
			lCampoNotaModel.setDescr(lTestoNota);

			lCampoNotaModel.setFasSieIdFascicoloSiep(lIdFascicolo);

			lCampoNotaModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampoNotaModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lCampoNotaModel.setDataInserimento(lEventoAnnotazione.getDataInserimento());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("CampoNotaModel = " + lCampoNotaModel);
		}

		// ==========================================================================
		// Carico i dati delle notifiche se previste
		// n.b. dal 26/05/2008 non è più prevista una stampa per cui non sono presenti
		// destinatari
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Carico le notifiche");
		// ==========================================================================
		// Notifica a Autorità di Polizia
		// ==========================================================================
		/*
		 * NotificaModel lNotModPol = new NotificaModel();
		 * 
		 * lNotModPol.setCodEsito("-"); lNotModPol.setCodTipoNotifica("C"); // Comunicazione
		 * lNotModPol.setDataInvio(lEventoAnnotazione.getDataEmissione());
		 * 
		 * lNotModPol.setCodOperatoreInserimento (lEventoAnnotazione.getCodOperatoreInserimento());
		 * lNotModPol.setCodUfficioInserimento (lEventoAnnotazione.getCodUfficioInserimento());
		 * lNotModPol.setDataInserimento (lEventoAnnotazione.getDataInserimento());
		 * 
		 * // Autorità esterna AutoritaEsternaModel lAut = new AutoritaEsternaModel(); String lPolizia =
		 * this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C); String
		 * lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
		 * 
		 * lAut.setCodTipoAutorita(lPolizia);
		 * 
		 * lComMod = null; lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
		 * lAut.setCodSede(lComMod.getCodComune());
		 * 
		 * if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE)) { String
		 * lIndirizzo = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE);
		 * lAut.setDescrizione(lIndirizzo); }
		 * 
		 * lAut.setCodOperatoreInserimento (lEventoAnnotazione.getCodOperatoreInserimento());
		 * lAut.setCodUfficioInserimento (lEventoAnnotazione.getCodUfficioInserimento());
		 * lAut.setDataInserimento (lEventoAnnotazione.getDataInserimento());
		 * 
		 * lNotModPol.setAutoritaEsterna(lAut);
		 * 
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.debug("lNotModPol = "+lNotModPol); // NotificaModel[] lNotifiche =
		 * new NotificaModel[1]; lNotifiche[0] = lNotModPol;
		 * 
		 * lEvNotModel.setNotifiche(lNotifiche);
		 */

		// ===========================
		// Effettuo la registrazione
		// ===========================
		// EventoNotificaModel lEventoNotInserito = null;
		ISanzioneSostitutiva lSanzioneCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();

		EventoNotificaModel lEventoNotInserito = lSanzioneCtrl.exInserisciMancataEspulsione(lEvNotModel,
				lVerbaleMod, lCampoNotaModel);

		// ==============================================
		// Restituisco la pagina di Dettaglio
		// ==============================================
		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioMancataEspulsione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "="
				+ lEventoNotInserito.getEvento().getIdEvento().toString();
		return lPage;

	}
}
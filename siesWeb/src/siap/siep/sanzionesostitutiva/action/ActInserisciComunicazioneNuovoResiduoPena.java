package siap.siep.sanzionesostitutiva.action;

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
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciComunicazioneNuovoResiduoPena
 * </p>
 * <p>
 * Description: Classe Action per l'Inserimento della Comunicazione Nuovo Residuo Pena Sanzioni Sostitutive
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

public class ActInserisciComunicazioneNuovoResiduoPena extends ActionSiap
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

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ======================================
		// Carico i dati dell'evento Comunicazione
		// ======================================
		EventoModel lEventoComunicazione = new EventoModel();

		lEventoComunicazione.setFasSieIdFascicoloSiep(lIdFascicolo);

		lEventoComunicazione.setCodTipoEvento("01"); // 01 - Provvedimento
		lEventoComunicazione.setCodTipoProvvedimento("12"); // 12 - Comunicazione
		lEventoComunicazione.setCodMotivo("0489"); // 0489 - Comunicazione Nuovo Residuo Pena per Computo
													// Misure Cautelari

		lEventoComunicazione.setCodEsito("-");

		lEventoComunicazione.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEventoComunicazione.setCodLuogoEmittente(getCodComuneUtenteConnesso());

		lEventoComunicazione.setDataEmissione(getRequestDateParameter(
				ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		// lEventoComunicazione.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
		// ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
		// ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		lEventoComunicazione.setFlagDocumentoRegistrato("N");
		lEventoComunicazione.setFlagStampaSiep("S");
		lEventoComunicazione.setFlagVideoSiep("S");

		if (!this.isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			lEventoComunicazione.setCodMagistrato(
					this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		} else {
			lEventoComunicazione.setCodMagistrato("-");
		}

		lEventoComunicazione.setCodLuogoDestinatario("-");
		lEventoComunicazione.setCodUfficioDestinatario("-");
		lEventoComunicazione.setCodTipoUfficioDestinatario("-");

		lEventoComunicazione.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEventoComunicazione.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEventoComunicazione.setDataInserimento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEventoComunicazione = " + lEventoComunicazione);

		// =============================================================
		//
		// =============================================================
		EventoNotificaModel lEvNotModel = new EventoNotificaModel();
		lEvNotModel.setEvento(lEventoComunicazione);

		// ===========================================
		// Carico i dati delle notifiche (destinatari)
		// ===========================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Carico le notifiche");
		ArrayList lNotifiche = new ArrayList();

		Date lDataEmissione = null;
		lDataEmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		// ==========================================================================
		// Magistrato di sorveglianza
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiUfficio.CAMPO_COD_COMUNE)
				&& getRequestStringParameter(ICostantiUfficio.CAMPO_COD_COMUNE) != null
				&& !getRequestStringParameter(ICostantiUfficio.CAMPO_COD_COMUNE).equals("")) {
			NotificaModel lNotModMDS = new NotificaModel();

			String lTribunale = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiUfficio.CAMPO_COD_COMUNE));
			lNotModMDS.setUffCodUfficio(lTribunale);

			lNotModMDS.setCodEsito("-");
			lNotModMDS.setCodTipoNotifica("MS"); // Magistrato di Sorveglianza
			lNotModMDS.setDataInvio(lDataEmissione);

			if (!this.isRequestParameterNullObj("MDS" + ICostantiNotifica.CAMPO_NOTE)) {
				lNotModMDS.setNote(getRequestStringParameter("MDS" + ICostantiNotifica.CAMPO_NOTE));
			}

			lNotModMDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModMDS.setDataInserimento(DateUtils.getSysDate());
			lNotModMDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lNotifiche.add(lNotModMDS);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica MDS Autorità = " + lNotModMDS);
		}

		// ==========================================================================
		// Altra Autorità
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")) {
			NotificaModel lNotMod = new NotificaModel();

			lNotMod.setCodEsito("-");
			lNotMod.setCodTipoNotifica("C"); // Comunicazione
			lNotMod.setDataInvio(lDataEmissione);

			if (!this.isRequestParameterNullObj("AA" + ICostantiNotifica.CAMPO_NOTE)) {
				lNotMod.setNote(getRequestStringParameter("AA" + ICostantiNotifica.CAMPO_NOTE));
			}

			lNotMod.setCodOperatoreInserimento(lEventoComunicazione.getCodOperatoreInserimento());
			lNotMod.setCodUfficioInserimento(lEventoComunicazione.getCodUfficioInserimento());
			lNotMod.setDataInserimento(lEventoComunicazione.getDataInserimento());

			// Autorità esterna
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());

			lAut.setCodOperatoreInserimento(lEventoComunicazione.getCodOperatoreInserimento());
			lAut.setCodUfficioInserimento(lEventoComunicazione.getCodUfficioInserimento());
			lAut.setDataInserimento(lEventoComunicazione.getDataInserimento());

			lNotMod.setAutoritaEsterna(lAut);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica Altra Autorità = " + lNotMod);

			lNotifiche.add(lNotMod);
		}

		// ==========================================================================
		// Aggiungo l'array delle notifiche all'evento
		// ==========================================================================
		lEvNotModel.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// ===========================
		// Effettuo la registrazione
		// ===========================
		ISanzioneSostitutiva lSanzioneCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();

		EventoNotificaModel lEventoNotInserito = lSanzioneCtrl
				.exInserisciComunicazioneNuovoResiduoPena(lEvNotModel);

		// ==============================================
		// Restituisco la pagina di Dettaglio
		// ==============================================
		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioComunicazioneNuovoResiduoPena&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "="
				+ lEventoNotInserito.getEvento().getIdEvento().toString();
		return lPage;

	}
}
package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordinescarcerazione.action.ICostantiOrdineScarcerazione;
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActInserisciComunicazioneLAErgastolo
 * </p>
 * <p>
 * Description: ActInserisciComunicazioneLAErgastolo
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
public class ActInserisciComunicazioneLAErgastolo extends ActionSiap implements
		ICostantiLicenzaLibanticipata, ICostantiOrdineScarcerazione {

	/**
	 * Azione di Inserimento del MisuraAlternativa
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		// ====================================================================
		// Carico i dati dell'evento
		// ====================================================================
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		EventoModel lEveModel = new EventoModel();
		ArrayList lNotifiche = new ArrayList();

		lEveModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveModel.setCodTipoEvento("01"); // Provvedimento
		lEveModel.setCodTipoProvvedimento("12");// Comunicazione
		lEveModel.setCodMotivo("0922"); // Comunicazione Concessione Liberazione Anticipata - condannato in
										// Ergastolo

		lEveModel.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveModel.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lEveModel.setCodUfficioDestinatario("-");
		lEveModel.setCodTipoUfficioDestinatario("-");
		lEveModel.setCodLuogoDestinatario("-");
		lEveModel.setCodEsito("-");

		lEveModel.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		lEveModel.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));

		lEveModel.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModel.setFlagStampaSiep("S");
		lEveModel.setFlagVideoSiep("S");

		lEveModel.setCodOperatoreInserimento(lCodiceOperatore);
		lEveModel.setDataInserimento(DateUtils.getSysDate());
		lEveModel.setCodUfficioInserimento(lCodiceUfficio);
		lEveModel.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO));

		lEveMod.setEvento(lEveModel);

		// =========================================
		// Notifica al Tribunale Di Sorveglianza
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE)) {
			NotificaModel lNotModTDS = new NotificaModel();
			String lTribunale = this
					.getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_COD_TRIBUNALE);
			String lSedeTribunale = this
					.getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE);

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			// String lCodiceUff =
			// getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_COD_TRIBUNALE),
			// getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE));
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lTribunale, lSedeTribunale);
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModTDS);
		}

		// =========================================
		// Notifica all'Ufficio Di Sorveglianza
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS)) {
			NotificaModel lNotModMDS = new NotificaModel();

			lNotModMDS.setCodEsito("-");
			lNotModMDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModMDS.setDataInserimento(DateUtils.getSysDate());
			lNotModMDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModMDS.setCodTipoNotifica("C");
			lNotModMDS.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			String lMds = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
			String lSedeMds = this.getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lMds, lSedeMds);
			lNotModMDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModMDS);
		}

		// =========================================
		// Notifica all'Istituto di Detenzione
		// =========================================
		if ((!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))) {
			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_IST)) {
				String lNoteIstituto = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_IST);
				lNotModIst.setNote(lNoteIstituto);
			}

			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);
			lNotModIst.setCodTipoNotifica("E");
			lNotModIst.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		// =========================================
		// Notifiche agli Avvocati
		// =========================================
		String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		int lIndex = 0;

		for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
			NotificaModel lNotAvv = new NotificaModel();

			lNotAvv.setCodTipoNotifica("N");
			// lNot.setNote(lArrayNote[lIndMisura]);
			lNotAvv.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotAvv.setCodEsito("-");
			lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
			lNotAvv.setDataInserimento(DateUtils.getSysDate());
			lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
			lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

			if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
				String[] lTipoAutoritaEsternaAvvocato = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
				String[] lSedeAutoritaEsternaAvvocato = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
				String[] lNoteAvvocato = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);

				lNotAvv.setNote(lNoteAvvocato[lIndex]);
				// String lNoteAvvocato = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
				// lNotAvv.setNote(lNoteAvvocato);

				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);
				
				//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//				ComuneModel lComMod = new ComuneModel(
//						getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
				ComuneModel lComMod = new ComuneModel(
						getCodComuneByDescrFlagVal(lSedeAutoritaEsternaAvvocato[lIndex]));
				//FINE: MEV_21
				
				lAut.setCodSede(lComMod.getCodComune());
				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setAutoritaEsterna(lAut);
			}

			lNotifiche.add(lNotAvv);
		}

		//
		lEveMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// =========================================================================
		// Inserisco l'Ordine di Scarcerazione
		// =========================================================================
		IOrdineScarcerazione lOScarc = SIEPLookupRemote.getOrdineScarcerazione();
		EventoNotificaModel lRetModel = lOScarc.ExInserisciOModificaEventoNotifica(lEveMod);

		// Preparo la pagina di ritorno
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sico.libertaanticipata.action.ActDettaglioComunicazioneLAErgastolo&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}
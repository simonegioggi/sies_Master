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
 * Title: ActInserisciComunicazioneRimediRisarcitori
 * </p>
 * <p>
 * Description: Action per l'inserimento della comunicazione concessione Rimedi Risarcitori DL92/2014
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciComunicazioneRimediRisarcitori extends ActionSiap
		implements ICostantiLicenzaLibanticipata, ICostantiOrdineScarcerazione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ====================================================================
		// Carico i dati dell'evento
		// ====================================================================
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		EventoModel lEveModel = new EventoModel();
		ArrayList lNotifiche = new ArrayList();

		lEveModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveModel.setCodTipoEvento("01"); // Provvedimento
		lEveModel.setCodTipoProvvedimento("12"); // Comunicazione

		if ("S".equals(getRequestStringParameter("isErgastolo"))) {
			lEveModel.setCodMotivo("5494"); // Concessione Risarcimento Danni D.L. 92/2014 - Condannato in
											// Ergastolo
		} else if ("S".equals(getRequestStringParameter("isLibero"))) {
			lEveModel.setCodMotivo("5493"); // Concessione Risarcimento Danni D.L. 92/2014 - Condannato Libero
		} else {
			lEveModel.setCodMotivo("5495"); // Concessione Risarcimento Danni D.L. 92/2014 - Condannato già
											// Scarcerato
		}

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
		lEveModel.setFlagDocumentoRegistrato(null);

		lEveModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveModel.setDataInserimento(DateUtils.getSysDate());

		// Si lega all'ordinanza/decreto della Sorveglianza
		lEveModel.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO));

		lEveMod.setEvento(lEveModel);

		// =========================================
		// Notifica all'Autorità Esterna
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
			NotificaModel lNotModAut = new NotificaModel();
			String lNoteAutorita = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			lNotModAut.setCodEsito("-");
			lNotModAut.setCodTipoNotifica("E");
			lNotModAut.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			lNotModAut.setNote(lNoteAutorita);

			lNotModAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModAut.setDataInserimento(DateUtils.getSysDate());

			// ============================
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
			String lAutorita = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedeAutorita = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);

			lAutMod.setCodTipoAutorita(lAutorita);

			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeAutorita));

			lAutMod.setCodSede(lComModel.getCodComune());

			lAutMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAutMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAutMod.setDataInserimento(DateUtils.getSysDate());

			lNotModAut.setAutoritaEsterna(lAutMod);

			lNotifiche.add(lNotModAut);
		}

		// =========================================
		// Notifica al Tribunale Di Sorveglianza
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE)) {
			NotificaModel lNotModTDS = new NotificaModel();

			String lTribunale = this
					.getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_COD_TRIBUNALE);
			String lSedeTribunale = this
					.getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lTribunale, lSedeTribunale);
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			lNotModTDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModTDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());

			lNotifiche.add(lNotModTDS);
		}

		// =========================================
		// Notifica all'Ufficio Di Sorveglianza
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS)) {
			NotificaModel lNotModMDS = new NotificaModel();

			String lMds = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
			String lSedeMds = this.getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lMds, lSedeMds);
			lNotModMDS.setUffCodUfficio(lCodiceUff);

			lNotModMDS.setCodEsito("-");
			lNotModMDS.setCodTipoNotifica("C");
			lNotModMDS.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			lNotModMDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModMDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModMDS.setDataInserimento(DateUtils.getSysDate());

			lNotifiche.add(lNotModMDS);
		}

		// =========================================
		// Notifica all'Istituto di Detenzione
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			NotificaModel lNotModIst = new NotificaModel();

			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_IST)) {
				String lNoteIstituto = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_IST);
				lNotModIst.setNote(lNoteIstituto);
			}

			lNotModIst.setCodEsito("-");
			lNotModIst.setCodTipoNotifica("E");
			lNotModIst.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			lNotModIst.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModIst.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModIst.setDataInserimento(DateUtils.getSysDate());

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

			lNotAvv.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotAvv.setCodEsito("-");
			lNotAvv.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotAvv.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotAvv.setDataInserimento(DateUtils.getSysDate());

			lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

			if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
				String[] lTipoAutoritaEsternaAvvocato = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
				String[] lSedeAutoritaEsternaAvvocato = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
				String[] lNoteAvvocato = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);

				lNotAvv.setNote(lNoteAvvocato[lIndex]);

				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);

				ComuneModel lComMod = new ComuneModel(
						getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
				lAut.setCodSede(lComMod.getCodComune());

				lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
				lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lAut.setDataInserimento(DateUtils.getSysDate());

				lNotAvv.setAutoritaEsterna(lAut);
			}

			lNotifiche.add(lNotAvv);
		}

		//
		lEveMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// =========================================================================
		// Inserisco la Comunicazione
		// =========================================================================
		IOrdineScarcerazione lOScarc = SIEPLookupRemote.getOrdineScarcerazione();
		EventoNotificaModel lRetModel = lOScarc.ExInserisciOModificaEventoNotifica(lEveMod);

		// Preparo la pagina di ritorno
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sico.libertaanticipata.action.ActDettaglioComunicazioneRimediRisarcitori&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}
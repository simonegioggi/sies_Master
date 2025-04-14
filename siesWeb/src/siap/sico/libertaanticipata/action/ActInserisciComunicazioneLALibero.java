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
 * Title: ActInserisciComunicazioneLALibero
 * </p>
 * <p>
 * Description: ActInserisciComunicazioneLALibero
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
public class ActInserisciComunicazioneLALibero extends ActionSiap implements ICostantiLicenzaLibanticipata,
		ICostantiOrdineScarcerazione {

	/**
	 * Azione di Inserimento della Comunicazione LA Libero
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
		lEveModel.setCodMotivo("0923"); // Comunicazione Concessione Liberazione Anticipata - condannato
										// libero

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
		// Notifica all'Autorità Esterna
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
			NotificaModel lNotModAut = new NotificaModel();
			String lAutorita = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedeAutorita = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			String lNoteAutorita = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			lNotModAut.setCodEsito("-");
			lNotModAut.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModAut.setDataInserimento(DateUtils.getSysDate());
			lNotModAut.setCodUfficioInserimento(lCodiceUfficio);
			lNotModAut.setCodTipoNotifica("E");
			lNotModAut.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			// String lCodiceUff =
			// getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_COD_TRIBUNALE),
			// getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE));

			lNotModAut.setNote(lNoteAutorita);

			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lAutorita);

			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeAutorita));

			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());

			lNotModAut.setAutoritaEsterna(lAutMod);

			lNotifiche.add(lNotModAut);
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
				+ "=siap.sico.libertaanticipata.action.ActDettaglioComunicazioneLALibero&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}
package siap.siep.ordinescarcerazione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciOSLiberazioneAnticipata
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Ordine di Scarcerazione nuovo fine pena a seguito
 * concessione Liberazione Anticipata. Soggetto NON in misura
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOSLiberazioneAnticipata extends ActOrdineScarcerazione implements
		ICostantiOrdineScarcerazione {

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
		lEveModel.setCodTipoProvvedimento("09");// Ordine Scarcerazione
		lEveModel.setCodMotivo("0081"); // Nuova scadenza pena a seguito concessione Liberazione Anticipata -
										// condannato detenuto

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

		//Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
		Date dataInvio = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		//Ticket#20210521012 -FINE
		
		// =========================================
		// Notifica all'Istituto di Detenzione
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
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

			// Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModIst.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
			//		ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotModIst.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);

			lNotifiche.add(lNotModIst);
		}

		// MEV29 - d.f. - 13/03/2015
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E).equals("")
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E).equals("-")) {
			String lTipoAutoritaEsternaE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedeAutoritaEsternaE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			String lSedeNoteE = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel lNotModPolE = new NotificaModel();

			lNotModPolE.setNote(lSedeNoteE);
			lNotModPolE.setCodEsito("-");
			lNotModPolE.setCodTipoNotifica("C");

			// lNotModPolE.setDataInvio(DateUtils.getSysDate());
			// Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModPolE.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
			//		ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotModPolE.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE
			lNotModPolE.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPolE.setDataInserimento(DateUtils.getSysDate());
			lNotModPolE.setCodUfficioInserimento(lCodiceUfficio);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lTipoAutoritaEsternaE);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedeAutoritaEsternaE));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setDescrSede(lSedeAutoritaEsternaE);
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPolE.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPolE);
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

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("C");
			
			// Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
			//		ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotModTDS.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE
			
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
			
			// Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModMDS.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
			//		ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotModMDS.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE
			
			// String lMds = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
			String lMds = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
			String lSedeMds = this.getRequestStringParameter(ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lMds, lSedeMds);
			lNotModMDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModMDS);
		}


		// ===================================================
		// Notifica all'Autorita' competente per Territorio
		// ===================================================
		if ((!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E))) {
			NotificaModel lNotModAutEst = new NotificaModel();
			AutoritaEsternaModel lAutEst = new AutoritaEsternaModel();

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
				String lNotePolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
				lNotModAutEst.setNote(lNotePolizia);
			}

			lNotModAutEst.setCodEsito("-");
			lNotModAutEst.setCodTipoNotifica("E");
			
			// Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModAutEst.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
			//		ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotModAutEst.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE
			
			lNotModAutEst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModAutEst.setDataInserimento(DateUtils.getSysDate());
			lNotModAutEst.setCodUfficioInserimento(lCodiceUfficio);

			String codTipoAut = this
					.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E);
			lAutEst.setCodTipoAutorita(codTipoAut);

			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(this
							.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E)));
			lAutEst.setCodSede(lComMod.getCodComune());
			lAutEst.setCodOperatoreInserimento(lCodiceOperatore);
			lAutEst.setCodUfficioInserimento(lCodiceUfficio);
			lAutEst.setDataInserimento(DateUtils.getSysDate());

			lNotModAutEst.setAutoritaEsterna(lAutEst);

			lNotifiche.add(lNotModAutEst);
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
			
			// Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotAvv.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
			//		ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lNotAvv.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE			
			
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

				ComuneModel lComMod = new ComuneModel(
						getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
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
				+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipata&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}
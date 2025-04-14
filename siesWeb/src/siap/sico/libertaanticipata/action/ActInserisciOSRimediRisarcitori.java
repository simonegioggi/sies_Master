package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
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
 * Title: ActInserisciOSRimediRisarcitori
 * </p>
 * <p>
 * Description: Action per l'inserimento dell'ordine di scarcerazione nuovo fine pena x concessione Rimedi
 * Risarcitori DL92/2014
 * </p>
 *
 * @author d.f
 * @version 1.0
 * @since 10/2014
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOSRimediRisarcitori extends ActionSiap implements ICostantiLicenzaLibanticipata {

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
		lEveModel.setCodTipoProvvedimento("09"); // Odine di scarcerazione

     // inizio ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori 
  	 String codMotivo = "";
     if (getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO)!=null) {
		BigDecimal idEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO);
		// devo recuperare il codice motivo dell'evento
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		EventoModel em = null;
		em = iEvento.ExRicercaEventoByKey(idEve);
		if(em!=null)
			codMotivo = em.getCodMotivo();
  	 }
  	   
  	
	if ("N".equals(getRequestStringParameter("isMisura"))) {
		if("9027".equals(codMotivo)){  // ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori
			lEveModel.setCodMotivo("9154"); // Nuova scadenza pena a seguito concessione reclamo risarcimento danni D.L. 92/2014 
		}
		else
			lEveModel.setCodMotivo("5491"); // Nuova scadenza pena a seguito concessione risarcimento danni
  											  // D.L. 92/2014 - Condannato Detenuto
  	 } else {
		if("9027".equals(codMotivo)){  // ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori
			lEveModel.setCodMotivo("9254"); // Nuova scadenza pena a seguito concessione reclamo risarcimento danni D.L. 92/2014 
		}
		else
			lEveModel.setCodMotivo("5492"); // Nuova scadenza pena a seguito concessione risarcimento danni
										// D.L. 92/2014 - Condannato In Misura
  	 }
  	 // fine ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori 

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
		// Notifica all'UEPE
		// =========================================
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)) {
			NotificaModel lNotModUEPE = new NotificaModel();

			BigDecimal lIdCSSA = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			lNotModUEPE.setCssIdCssa(lIdCSSA);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA)) {
				String lNoteCSSA = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA);
				lNotModUEPE.setNote(lNoteCSSA);
			}

			lNotModUEPE.setCodEsito("-");
			lNotModUEPE.setCodTipoNotifica("E");
			lNotModUEPE.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			lNotModUEPE.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModUEPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModUEPE.setDataInserimento(DateUtils.getSysDate());

			lNotifiche.add(lNotModUEPE);
		}

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

				//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//				ComuneModel lComMod = new ComuneModel(
//						getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
				ComuneModel lComMod = new ComuneModel(
						getCodComuneByDescrFlagVal(lSedeAutoritaEsternaAvvocato[lIndex]));
				//FINE: MEV_21
				
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
		// Inserisco l'Ordine di Scarcerazione
		// =========================================================================
		IOrdineScarcerazione lOScarc = SIEPLookupRemote.getOrdineScarcerazione();
		EventoNotificaModel lRetModel = lOScarc.ExInserisciOModificaEventoNotifica(lEveMod);

		// Preparo la pagina di ritorno
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sico.libertaanticipata.action.ActDettaglioOSRimediRisarcitori&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}
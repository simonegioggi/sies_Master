package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.action.ICostantiArchiviazione;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciArchiviazioneManuale
 * </p>
 * <p>
 * Description: Inserimento del Provvedimento ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * Archiviazione manuale (appl. MIS. SIC.)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROSINO
 */
public class ActInserisciArchiviazioneManuale extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// recupero dati Magistrato competente per fascicolo SIEP
//		MagistratoCompetenteMagistratoModel lMagCoMag = new MagistratoCompetenteMagistratoModel();
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		/*lMagCoMag = */lMagCtrl.ExRicercaMagistratoCompetenteByFascicolo(lFascicoloModel.getIdFascicoloSiep());

//		String lCodPosGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		String lPage = "";

		Date dataOd = DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy");

		// Operatore e Ufficio Inserimento
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		Date lDataArch = getRequestDateParameter(ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE);

		// String ordEveKey = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		String codRich = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC);

		IUfficio lctrlUf = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffMod = new UfficioModel();
		String CodTipoUff = "";

		lUffMod = lctrlUf.ExRicercaUfficioByCod(lCodiceUfficio);
		CodTipoUff = lUffMod.getCodTipoUfficio().trim();

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();

		// = = EVENTO = =
		lEve.getEvento().setCodTipoEvento("01");
		lEve.getEvento().setCodTipoProvvedimento("25");
		lEve.getEvento().setCodMotivo(codRich);

		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");

		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.getEvento().setDataEmissione(lDataArch);

		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);

		lEve.getEvento().setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);

		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");
		// = = = = = = = = = = = = =

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" Codice Uff : lCodiceUfficio    = "+lCodiceUfficio);

		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Notifiche ---------> Altra Autorità
		// EventoNotificaModel lRetModel = new EventoNotificaModel();
		ArrayList lNotificheArray = new ArrayList();

		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;
		String lNote_E = null;

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
			lNote_E = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

		// Si istanzia l'array delle notifiche
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("AA");
		lNotMod.setDataInvio(dataOd);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setNote(lNote_E);

		if (lDestinatario_E != null) {
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_E);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setIstDetIdIstitutoDetenzione("");

			lNotMod.setAutoritaEsterna(lAutMod);
			lNotificheArray.add(lNotMod);
		}

		// Notifiche ---------> Destinatari UDS
		if (!isRequestParameterNullObj("tipoUDS") && !getRequestStringParameter("tipoUDS").equals("-")) {
			String lTipoUds = getRequestStringParameter("tipoUDS");
			String lComuneUds = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);
			String lCodUffUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUds, lComuneUds);

			NotificaModel lNotUffUDS = new NotificaModel();

			lNotUffUDS.setCodEsito("-");
			lNotUffUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotUffUDS.setDataInserimento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotUffUDS.setCodTipoNotifica("MS");
			lNotUffUDS.setDataInvio(dataOd);

			lNotUffUDS.setUffCodUfficio(lCodUffUDS);

			lNotificheArray.add(lNotUffUDS);
		}

		// Notifica Istituto di detenzione
		String lDestinatario_ist = null;
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !this.getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE).equals("")) {
			lDestinatario_ist = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		}

		if (lDestinatario_ist != null) {
			NotificaModel lNotistMod = new NotificaModel();
			lNotistMod.setCodTipoNotifica("E");
			lNotistMod.setDataInvio(dataOd);
			lNotistMod.setCodEsito("-");
			lNotistMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotistMod.setDataInserimento(DateUtils.getSysDate());
			lNotistMod.setCodUfficioInserimento(lCodiceUfficio);

			lNotistMod.setIstDetIdIstitutoDetenzione(lDestinatario_ist);
			lNotificheArray.add(lNotistMod);
		}

		// ========================================================================
		// Recupero le notifiche se indicate in form
		if (isRequestChecked("Difesa")) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Notifiche Atti (Difensore - Condannato) selezionato");

			String[] lAvvocati = null;
			if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
				lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			}

			String[] lSedeDestinatario_avv = null;
			String[] lDestinatario_avv = null;
			String[] lNote_avv = null;

			// Tipo Autorità
			if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
				lDestinatario_avv = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			}

			// Sede
			if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)) {
				lSedeDestinatario_avv = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			}

			// Indirizzo
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)) {
				lNote_avv = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
			}

			int lIndNotifiche = 0;
			int lNumAvvNotifiche = 0;

			if (lAvvocati != null)
				lNumAvvNotifiche = lAvvocati.length;

			if (lSedeDestinatario_avv != null) {
				if (lAvvocati != null) {
					while (lIndNotifiche < lNumAvvNotifiche) {
						NotificaModel lNot = new NotificaModel();

						lNot.setCodTipoNotifica("ND");

						lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
						lNot.setNote(lNote_avv[lIndNotifiche]);
						lNot.setDataInvio(dataOd);
						lNot.setCodEsito("-");

						lNot.setCodOperatoreInserimento(this.getCodUtenteConnesso());
						lNot.setDataInserimento(DateUtils.getSysDate());
						lNot.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

						//
						AutoritaEsternaModel lAut = new AutoritaEsternaModel();
						if (isRequestChecked("SiNoTe")) { // Sistema Notifiche Telematiche
															// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
															// variabile di istanza siesLogger al posto di
															// LogF3B.getLogger()
															// siesLogger.debug("Sistema Notifiche Telematiche");
							lAut.setCodTipoAutorita("C0");
							lAut.setCodSede("-");
						} else {
							// Notifica tramite UNEP
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug("Notifiche all'UNEP");
							lAut.setCodTipoAutorita(lDestinatario_avv[lIndNotifiche]);
							ComuneModel lComMod = new ComuneModel(
									getCodComuneByDescr(lSedeDestinatario_avv[lIndNotifiche]));
							lAut.setCodSede(lComMod.getCodComune());
							lAut.setDescrizione(lComMod.getDescrizione());
						}

						lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
						lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
						lAut.setDataInserimento(DateUtils.getSysDate());

						// Setto l'Autorita Esterna per la notifica corrente
						lNot.setAutoritaEsterna(lAut);
						lIndNotifiche++;
						lNotificheArray.add(lNot);
					}
				}

			}

		} // End Notifiche al Difensore
			// ========================================================================

		NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);

		lEve.setNotifiche(lNotifiche);

		// = = PENA RESIDUA = =
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		lPenaRes.setIdPenaResidua(lIdPenaRes);

		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		// = = ARCHIVIAZIONE = =

		// lArcMod.setCodTipoProvvedimento("55"); // ARCHIVIAZIONE new
		lArcMod.setCodTipoProvvedimento("25"); // ANNTAZIOE old per provare

		// Ufficio Emittente: Locale; Archiviazione Manuale (COD = "-" ; RV_Domain = DEFI_ALTRO)
		lArcMod.setCodProvvedimento("-");

		lArcMod.setDataDefinizione(lDataArch);
		lArcMod.setDataEmissione(dataOd);
		lArcMod.setCodOggettoDefinizione(codRich);

		lArcMod.setCodTipoEmittente("-");
		lArcMod.setCodTipoAutoritaEmittente(CodTipoUff);
		lArcMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lArcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataInserimento(DateUtils.getSysDate());
		lArcMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		if (!isRequestParameterNullObj(ICostantiArchiviazione.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE).equals("")) {
			lArcMod.setNote(getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE));
		}

		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcModRet = lCtrlArc.ExInserisciEventoNotificaArchiviazione(lEve, lArcMod,
				lFascicoloModel);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioArchiviazioneManuale&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lArcModRet.getEveIdEvento();

		return lPage;
	} // Chiude processRequest

} // Chiude Action
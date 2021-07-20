package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.ufficio.action.ICostantiUfficio;
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
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActInserisciArchiviazionePerProvvSorveglianza
 * </p>
 * <p>
 * Description: Form per Inserimento del Provvedimento
 * </p>
 * <p>
 * ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * Archiviazione per Provvedimento della Sorveglianza (appl. MIS. SIC.)
 * </p>
 * <p>
 * La form prevede la possibilità di scaricare il Provv. della Sorveglianza
 * </p>
 * <p>
 * o di inserirlo manualmente
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: INTERSISTEMI ITALIA S.P.A.
 * </p>
 * 
 * @author AMBROSINO
 *
 */
public class ActInserisciArchiviazionePerProvvSorveglianza extends ActionSiap implements
		ICostantiMisuraSicurezza {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// recupero dati Magistrato competente per fascicolo SIEP
//		MagistratoCompetenteMagistratoModel lMagCoMag = new MagistratoCompetenteMagistratoModel();
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		/*lMagCoMag = */lMagCtrl.ExRicercaMagistratoCompetenteByFascicolo(lFascicoloModel.getIdFascicoloSiep());

//		String lCodPosGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		String lPage = "";
		// dati operatore/ufficio Inserimento
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		// id Evento dell'ordinanza SIUS selezionata nella form precedente
//		String IdEve = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		Date lDataRicezione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI);

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		Date lDataArch = getRequestDateParameter(ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE);

		// String ordEveKey = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// in codRich ci metto il Cod. OGGETTO_DEFINIZIONE
		String codRich = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();

		// = = EVENTO = =
		lEve.getEvento().setCodTipoEvento("01");
		lEve.getEvento().setCodTipoProvvedimento("25");
		lEve.getEvento().setCodMotivo(codRich);

		// Ufficio Emittente è l'ufficio inserimento dell'evento
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.getEvento().setDataEmissione(lDataArch);

		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);

		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");
		lEve.getEvento().setCodUfficioDestinatario("-");

		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO)
				&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO) != null
				&& !getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO).equals("")) {
			lEve.getEvento().setChiaveAnno(
					getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO));
			lEve.getEvento().setChiaveProgr(
					getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR));
		}

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO) != null
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO).equals("")) {
			lEve.getEvento().setAnnoProtocollo(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
			lEve.getEvento().setProgrProtocollo(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
		} else
			lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		// Notifiche ---------> Altra Autorità

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
		lNotMod.setDataInvio(lDataArch);
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

		// Notifiche ---------> Destinatari UDS (che è anche l'autorità cha ha emesso il provvedimento SIUS)
		if (!isRequestParameterNullObj("tipoUDS") && !getRequestStringParameter("tipoUDS").equals("-")) {
			String lTipoUds = getRequestStringParameter("tipoUDS"); // per es. TDS
			String lComuneUds = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO); // per es.
																								// BARI
			String lCodUffUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUds, lComuneUds);

			NotificaModel lNotUffUDS = new NotificaModel();

			lNotUffUDS.setCodEsito("-");
			lNotUffUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotUffUDS.setDataInserimento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotUffUDS.setCodTipoNotifica("MS");
			lNotUffUDS.setDataInvio(lDataArch);

			lNotUffUDS.setUffCodUfficio(lCodUffUDS);

			lNotificheArray.add(lNotUffUDS);
		}

		/*
		 * //Notifiche ---------> Destinatari UDS (che è anche l'autorità cha ha emesso il provvedimento SIUS)
		 * if(!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE) &&
		 * getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE)!=null &&
		 * !isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) &&
		 * getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)!=null ) { String lTipoUds =
		 * getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE); String lComuneUds =
		 * getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE); String lCodUffUDS =
		 * getCodUfficioByCodTipoUfficioDescrComune(lTipoUds,lComuneUds);
		 * 
		 * NotificaModel lNotUffUDS = new NotificaModel();
		 * 
		 * lNotUffUDS.setCodEsito("-"); lNotUffUDS.setCodOperatoreInserimento (lCodiceOperatore);
		 * lNotUffUDS.setDataInserimento (DateUtils.getSysDate()); lNotUffUDS.setCodUfficioInserimento
		 * (getCodUfficioUtenteConnesso()); lNotUffUDS.setCodTipoNotifica("MS");
		 * lNotUffUDS.setDataInvio(lDataArch);
		 * 
		 * lNotUffUDS.setUffCodUfficio(lCodUffUDS);
		 * 
		 * lNotificheArray.add(lNotUffUDS); }
		 */

		// Notifica Istituto di detenzione
		String lDestinatario_ist = null;
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !this.getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE).equals(""))
			lDestinatario_ist = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		if (lDestinatario_ist != null) {
			NotificaModel lNotistMod = new NotificaModel();
			lNotistMod.setCodTipoNotifica("E");
			lNotistMod.setDataInvio(lDataArch);
			lNotistMod.setCodEsito("-");
			lNotistMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotistMod.setDataInserimento(DateUtils.getSysDate());
			lNotistMod.setCodUfficioInserimento(lCodiceUfficio);

			lNotistMod.setIstDetIdIstitutoDetenzione(lDestinatario_ist);
			lNotificheArray.add(lNotistMod);
		}

		// ========================================================================
		// Recupera, se presenti le notifiche agli avvocati.
		// Le notifiche possono essere effettuate o Tramite Unep o tramite il
		// 'Sistema Notifiche Telematiche'. Ciò che cambia è il record Autorità esterna.

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
						lNot.setDataInvio(lDataArch);
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
							
							//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//							ComuneModel lComMod = new ComuneModel(
//									getCodComuneByDescr(lSedeDestinatario_avv[lIndNotifiche]));
							ComuneModel lComMod = new ComuneModel(
									getCodComuneByDescrFlagVal(lSedeDestinatario_avv[lIndNotifiche]));							
							//FINE: MEV_21
							
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
		}
		// ========================================================================

		NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);

		lEve.setNotifiche(lNotifiche);

		// = = = = = = = = = = = = =

		// = = PENA RESIDUA = =
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		lPenaRes.setIdPenaResidua(lIdPenaRes);

		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		// = = ARCHIVIAZIONE = =

		lArcMod.setCodTipoProvvedimento("25"); // ANNTAZIOE
		// Ufficio Emittente: Archiviazione provvedimento Giudice Sorveglianza (COD = 0003 RV_Domain =
		// DEFI_ALTRO)
		lArcMod.setCodProvvedimento("0003");
		// Ordinanza/decreto
		lArcMod.setCodTipoProvvedimentoArc(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));

		lArcMod.setDataDefinizione(lDataArch);
		lArcMod.setDataEmissione(lDataEmissione);
		lArcMod.setDataRicezione(lDataRicezione);
		lArcMod.setCodOggettoDefinizione(codRich);
		lArcMod.setCodTipoEmittente("-");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO) != null
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO).equals("")) {
			lArcMod.setAnnoProvvedimento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
			lArcMod.setNumProvvedimento(getRequestStringParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
		}

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE) != null
				&& !isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) != null) {
			lArcMod.setCodTipoAutoritaEmittente(getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE));
			ComuneModel lCom = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)));
			// MEV2 STEP2 - 01-03-16 - Metodo inserito per controllare l'esistenza dell'uffico nel comune
			// selezionato
			/*String lCodiceuf = */getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE),
					lCom.getDescrizione());

			lArcMod.setCodLuogoEmittente(lCom.getCodComune());
		}

		lArcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataInserimento(DateUtils.getSysDate());

		lArcMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		if (!isRequestParameterNullObj(ICostantiArchiviazione.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE).equals("")) {
			lArcMod.setNote(getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE));
		}

		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO)
				&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO) != null
				&& !getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO).equals("")) {
			lArcMod.setChiaveAnno(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO));
			lArcMod.setChiaveProgr(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR));
		}

		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcModRet = lCtrlArc.ExInserisciEventoNotificaArchiviazione(lEve, lArcMod,
				lFascicoloModel);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioArchiviazionePerProvvSorveglianza&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lArcModRet.getEveIdEvento();

		return lPage;

	} // Chiude processRequest

} // Chiude Action
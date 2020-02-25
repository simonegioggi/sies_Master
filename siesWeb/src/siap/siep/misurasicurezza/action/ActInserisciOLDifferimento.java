package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActInserisciOLDifferimento
 * </p>
 * <p>
 * Description: Classe l' Emissione O.L. per Differimento per esecuzione Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author SGIOGGI
 * @version 1.0
 */
public class ActInserisciOLDifferimento extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String codiceOperatore = getCodUtenteConnesso();
		String codiceUfficio = getCodUfficioUtenteConnesso();

		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date dataTrasmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

		EventoNotificaModel enm = new EventoNotificaModel();
		PenaResiduaModel prm = new PenaResiduaModel();

		enm.getEvento().setCodTipoEvento("01"); // provvedimento
		// 06 (ORDINE ESECUZIONE) se scarcera il PM, 25 (COMUNICAZIONE) se gia' scarcerato
		// INTERVENTO POST COLLAUDO 11.3 (CREARE UN NUOVO COD TIPO PROVVEDIMETNO = ORDINE DI LIBERAZIONE) INSERITO CODICE 65
		enm.getEvento().setCodTipoProvvedimento("65"); // Ordine Esecuzione

		Date dataScarcerazione = null;
		String codTipoUfficioScarcerazione = "-";
		if (!isRequestParameterNullObj("AnnoDataScarcerazione")) {
			dataScarcerazione = getRequestDateParameter("AnnoDataScarcerazione", "MeseDataScarcerazione",
					"GiornoDataScarcerazione");
			// scarcerato --> SORV; daScarcerare --> PROC
			codTipoUfficioScarcerazione = "PROC";
			if ("scarcerato".equals(getRequestStringParameter("isScarcerato"))) {
				codTipoUfficioScarcerazione = "SORV";
				// intervento post collado 11.3 (il tipo provvedimento deve essere comunicazione - codice 12 e non 23 annotazione)
				enm.getEvento().setCodTipoProvvedimento("12"); // Comunicazione (12)
			}
		}
		enm.getEvento().setCodMotivo("1132"); // per Differimento
		enm.getEvento().setFlagStampaSiep("S");
		enm.getEvento().setFlagVideoSiep("S");
		enm.getEvento().setDataEmissione(dataEmissione);
		enm.getEvento().setDataTrasmissioneAtti(dataTrasmissione);
		enm.getEvento().setCodOperatoreInserimento(codiceOperatore);
		enm.getEvento().setDataInserimento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioInserimento(codiceUfficio);
		enm.getEvento().setCodLuogoEmittente(getCodComuneUtenteConnesso());
		enm.getEvento().setCodUfficioEmittente(codiceUfficio);
		enm.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// MEV39 (INTERVENTO POST TRASFERTA TORINO): SETTO L'ESITO GIUSTO SULL'EVENTO E NON IL -
		String codEsitoSIEP = getRequestStringParameter(ICostantiEvento.CAMPO_COD_ESITO);
		if(codEsitoSIEP!=null)
			enm.getEvento().setCodEsito(codEsitoSIEP);
		else{
			throw new F3BException(F3BException.USER_MESSAGE, "Attenzione! Codice Esito mancante!");
		}
		
		enm.getEvento().setCodLuogoDestinatario("-");
		enm.getEvento().setCodTipoUfficioDestinatario("-");
		enm.getMagistrato().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		enm.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// ID FASCICOLO SIEP
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());

		// ID FASCICOLO SIUS
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS))
			enm.getEvento().setFasSiuIdFascicoloSius(
					getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));

		// Notifiche
		EventoNotificaModel enmRet = new EventoNotificaModel();
		ArrayList notifiche = new ArrayList();

		// destinatario istituto (è la struttura designata, che si appoggia sui campi dell'istituto di detenzione)
		String lDestinatario_ist = null;
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDestinatario_ist = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		// destinatario autorita' esterna
		String lDestinatario_E = null;
		String lSedeDestinatario_E = null;
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !"-".equals(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E))) {
			lDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		// campo note
		String lNote_E = null;
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
			lNote_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

		// notifiche al Destinatario per Esecuzione
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("E");
		lNotMod.setDataInvio(dataTrasmissione);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(codiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(codiceUfficio);
		lNotMod.setNote(lNote_E);

		// notifiche al destinatario autorita' esterna
		if (lDestinatario_E != null) {
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
			lAutMod.setCodTipoAutorita(lDestinatario_E);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(codiceOperatore);
			lAutMod.setCodUfficioInserimento(codiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setIstDetIdIstitutoDetenzione("");
			lNotMod.setAutoritaEsterna(lAutMod);
			notifiche.add(lNotMod);
		}

		// notifiche al destinatario UDS
		if (!isRequestParameterNullObj("tipoUDS") && !"-".equals(getRequestStringParameter("tipoUDS"))) {
			String lTipoUds = getRequestStringParameter("tipoUDS");
			String lComuneUds = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);
			String lCodUffUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUds, lComuneUds);
			NotificaModel lNotUffUDS = new NotificaModel();
			lNotUffUDS.setCodEsito("-");
			lNotUffUDS.setCodOperatoreInserimento(codiceOperatore);
			lNotUffUDS.setDataInserimento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioInserimento(codiceUfficio);
			lNotUffUDS.setCodTipoNotifica("MS");
			lNotUffUDS.setDataInvio(dataTrasmissione);
			lNotUffUDS.setUffCodUfficio(lCodUffUDS);
			notifiche.add(lNotUffUDS);
		}

		// notifiche all'Istituto
		if (lDestinatario_ist != null) {
			NotificaModel lNotistMod = new NotificaModel();
			lNotistMod.setCodTipoNotifica("E");
			lNotistMod.setDataInvio(dataTrasmissione);
			lNotistMod.setCodEsito("-");
			lNotistMod.setCodOperatoreInserimento(codiceOperatore);
			lNotistMod.setDataInserimento(DateUtils.getSysDate());
			lNotistMod.setCodUfficioInserimento(codiceUfficio);
			lNotistMod.setIstDetIdIstitutoDetenzione(lDestinatario_ist);
			notifiche.add(lNotistMod);
		}

		// =================================================
		// Notifiche agli Avvocati (Notifica Difensori)
		// Solo se selezionate in Maschera il check: Notifiche Atti (Difensore - Condannato)
		// =================================================
		if (isRequestChecked("Difesa")) {
			String[] lAvvocati = null;
			if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO))
				lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

			String[] lSedeDestinatario_avv = null;
			String[] lDestinatario_avv = null;
			String[] lNote_avv = null;

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE))
				lSedeDestinatario_avv = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA))
				lDestinatario_avv = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);

			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE))
				lNote_avv = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);

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
						lNot.setDataInvio(dataTrasmissione);
						lNot.setCodEsito("-");
						lNot.setCodOperatoreInserimento(codiceOperatore);
						lNot.setDataInserimento(DateUtils.getSysDate());
						lNot.setCodUfficioInserimento(codiceUfficio);

						AutoritaEsternaModel lAut = new AutoritaEsternaModel();

						if (isRequestChecked("SiNoTe")) { // Sistema Notifiche Telematiche
							lAut.setCodTipoAutorita("C0");
							lAut.setCodSede("-");
						} else {
							// Notifica tramite UNEP
							lAut.setCodTipoAutorita(lDestinatario_avv[lIndNotifiche]);
							ComuneModel lComMod = new ComuneModel(
									getCodComuneByDescr(lSedeDestinatario_avv[lIndNotifiche]));
							lAut.setCodSede(lComMod.getCodComune());
							lAut.setDescrizione(lComMod.getDescrizione());
						}
						lAut.setCodOperatoreInserimento(codiceOperatore);
						lAut.setCodUfficioInserimento(codiceUfficio);
						lAut.setDataInserimento(DateUtils.getSysDate());

						// Setto l'Autorita Esterna per la notifica corrente
						lNot.setAutoritaEsterna(lAut);
						lIndNotifiche++;
						notifiche.add(lNot);
					}
				}
			}
		} // End notifiche al difensore

		NotificaModel[] nm = (NotificaModel[]) notifiche.toArray(new NotificaModel[0]);

		// Pena residua (va impostata = null se assente)
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)) {
			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			prm.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				prm.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		} else
			prm = null;

		// Data Differimento
		Date dataDifferimento = getRequestDateParameter(
				ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO,
				ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO,
				ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO);

		// Data Fine Rinvio
		Date dataFineRinvio = new Date();
		dataFineRinvio = getRequestDateParameter(
				ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA,
				ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA,
				ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA);
		
		// mev 39 (trasferta torino): impostare data Fine rinvio con (data differimento + anni/mesi / giorni di sospensione)
		if(dataDifferimento != null && dataFineRinvio == null){
			//  la data Fine Rinvio DEVE ESSERE Calcolata (DATA DIFFERIMENTO + ANNI/MSI/GIORNI)
						
			Calendar dataFineMisura = Calendar.getInstance();
			dataFineMisura.setTime(dataDifferimento);
			String as = getRequestStringParameter("numAnniSospensione");
			String ms = getRequestStringParameter("numMesiSospensione");
			String gs = getRequestStringParameter("numGiorniSospensione");
			if (Utils.isPresent(gs)){
				int numGiorni = new Integer(gs).intValue();
				dataFineMisura.add(Calendar.DAY_OF_YEAR, numGiorni);
			}			
			if (Utils.isPresent(ms)){
				int numMesi = new Integer(ms).intValue();
				dataFineMisura.add(Calendar.MONTH, numMesi);
			}			
			if (Utils.isPresent(as)){
				int numAnni = new Integer(as).intValue();
				dataFineMisura.add(Calendar.YEAR, numAnni);
			}			
			dataFineRinvio = dataFineMisura.getTime();
		}

		// BigDecimal idEveFascSius = getRequestBigDecimalParameter("idEventoFascSius");
		MisuraAlternativaModel mam = new MisuraAlternativaModel();
		mam.setCodTipoDecisione(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));
		// recupero il valore da cg_ref_codes
		String codEsito = getRequestStringParameter(ICostantiEvento.CAMPO_COD_ESITO);
		Collection<DecodificheModel> c = DecodificheManager.getInstance().getEsitoProvvedimento();
		String codNaturaDecisione = "";
		if (!c.isEmpty()) {
			for (DecodificheModel dm : c) {
				if (dm.getCode().equals(codEsito)) {
					codNaturaDecisione = dm.getCodiceAlternativo();
					break;
				}
			}
		}
		mam.setCodNaturaDecisione(codNaturaDecisione); // "CO"
		mam.setCodTipoMisura(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		Date dataDecisione = getRequestDateParameter(ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO,
				ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO,
				ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO);
		mam.setDataDecisione(dataDecisione);
		mam.setDataInizioMisura(dataDifferimento);
		mam.setDataFineMisura(dataFineRinvio);
		mam.setChiaveAnnoFascicoloSius(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO));
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO))
			mam.setChiaveUfficioFascicoloSius(getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO));
		else {
			String autoritaEmittente = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE),
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE));
			// ComuneModel sedeAutoritaEmittente = new ComuneModel(
			// getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)));
			mam.setChiaveUfficioFascicoloSius(autoritaEmittente);
		}
		mam.setChiaveProgrFascicoloSius(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR));
		mam.setAnnoRegistro(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
		mam.setNumeroRegistro(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
		mam.setDataScarcerazione(dataScarcerazione);
		mam.setCodTipoUfficioScarcerazione(codTipoUfficioScarcerazione);
		if ("PROC".equals(codTipoUfficioScarcerazione))
			mam.setFlagUfficioInserimento("P");
		else
			mam.setFlagUfficioInserimento("S");
		mam.setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		// mam.setEveIdEvento(idEveFascSius);
		mam.setFlagDecisioneTribunale("N");
		if (isRequestChecked("flagDecisioneTribunale"))
			mam.setFlagDecisioneTribunale("S");
		mam.setCodOperatoreInserimento(codiceOperatore);
		mam.setDataInserimento(DateUtils.getSysDate());
		mam.setCodUfficioInserimento(codiceUfficio);
		// DURATA
		String as = getRequestStringParameter("numAnniSospensione");
		String ms = getRequestStringParameter("numMesiSospensione");
		String gs = getRequestStringParameter("numGiorniSospensione");
		if (Utils.isPresent(as))
			mam.setNumAnniMisura(new BigDecimal(as));
		if (Utils.isPresent(ms))
			mam.setNumMesiMisura(new BigDecimal(ms));
		if (Utils.isPresent(gs))
			mam.setNumGiorniMisura(new BigDecimal(gs));
		mam.setDescrLuogoProva(getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA));

		// INSERIMENTO OE
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		enm.setNotifiche(nm);
		enmRet = lCtrl.ExInserisciOLDifferimentoConNotifiche(enm, prm, mam);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioOLDifferimento&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + enmRet.getEvento().getIdEvento();
	} // Chiude processRequest

} // Chiude Action
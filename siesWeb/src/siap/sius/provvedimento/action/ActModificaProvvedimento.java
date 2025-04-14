package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.action.ICostantiDepositoSentenza;
import siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Action che realizza la modifica del Provvedimento SIUS: Ordinanza, Decreto o Sentenza. Gli unici dati
 * coinvolti in un'operazione di modifica sono: gli esiti degli oggetti, la data di emissione e la data
 * decorrenza della misura di sicurezza : Per alcuni provvedimenti ci sono le L.A. e i relativi periodi; per
 * altri ci sono le M.S.
 *
 * @author Lesposito
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaProvvedimento extends ActionSius implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Inizializzazione data odierna
	private Date mOggi = DateUtils.getSysDate();

	// Data emissione
	private Date mDataEmissione;
	// Data decorrenza della Misura di Sicurezza
	private Date mDataDecorrenza;

	private class PeriodoClass {
		Date mDataIni = null;
		Date mDataFine = null;
	}

	private PeriodoClass[] mPeriodi = null;
	private int mInd = 0;
	private String mTipoConcessione; /* modalità di scelta dei periodi concessi. (S/C) L.A. ordinaria */
	// private String mFlagConcessione = "C"; /* flag Concessione per generazione periodi libertà anticipata
	// */

	private PeriodoClass[] mPeriodi_spe = null;
	private int mInd_spe = 0;
	private String mTipoConcessione_spe; /* modalità di scelta dei periodi concessi. (S/C) L.A. SPECIALE */

	private PeriodoClass[] mPeriodi_int = null;
	private int mInd_int = 0;
	/*
	 * modalità di scelta dei periodi concessi. (S/C) L.A. INTEGRAZIONE
	 */
	private String mTipoConcessione_int;

	public String processRequest() throws Exception {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ----------------------- > - ActModificaProvvedimento INIZIO ");
		String lRetPage = IWebConstants.PG_MESSAGE;
		BigDecimal lIdOrdinanza = null;
		BigDecimal lIdDecreto = null;
		BigDecimal lIdSentenza = null;

		// Lettura data di emissione
		mDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" -----> C - ActModificaProvvedimento - mDataEmissione = " + mDataEmissione);
		// Lettura data decorrenza
		if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA)
				&& getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA)
						.equals("S")) {
			mDataDecorrenza = getRequestDateParameter(ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA,
					ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA,
					ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA);
		} else {
			mDataDecorrenza = null;
		}

		// Lettura ID Ordinanza, Decreto o Sentenza
		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC)
						.trim().length() > 0)
			lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC);
		else if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO)
				&& getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO).trim()
						.length() > 0)
			lIdDecreto = getRequestBigDecimalParameter(ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO);
		else if (!isRequestParameterNullObj(ICostantiDepositoSentenza.CAMPO_ID_DEPOSITO_SENTENZA)
				&& getRequestStringParameter(ICostantiDepositoSentenza.CAMPO_ID_DEPOSITO_SENTENZA).trim()
						.length() > 0)
			lIdSentenza = getRequestBigDecimalParameter(ICostantiDepositoSentenza.CAMPO_ID_DEPOSITO_SENTENZA);
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"ID Provvedimento non valorizzato nella form !");
		//
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" -----> C - ActModificaProvvedimento - lIdOrdinanza = " + lIdOrdinanza);
		//
		// Lettura ID Evento
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoModel lEvento = null;
		TenoreModel[] lTenori = null;
		MisuraSicurezzaModel lMisuraSicurezza = null;

		// ===== EVENTO E TENORE/I =====

		lEvento = generaEvento();
		lTenori = generaTenori();

		// Modifica del 20/09/2013 mev "Revisione Misure di Sicurezza"
		// Aggiorna Data Decorrenza della Misura di Sicurezza
		// legata al Fascicolo SIUS
		IMisuraSicurezza lCtrlMisSic = SIEPLookupRemote.getMisuraSicurezzaRemote();

		FascicoloGPModel mFasGPMod = null;
		List<MisuraSicurezzaModel> lMisureSicurezza = null;

		// Si preleva dalla sessione il fascicolo GPModel.
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		mFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lMisureSicurezza = lCtrlMisSic.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(
					mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante ");

		if (lMisureSicurezza.size() > 0) {
			Iterator<MisuraSicurezzaModel> itxMis = lMisureSicurezza.iterator();
			while (itxMis.hasNext()) {
				MisuraSicurezzaModel lMisSicuSius = itxMis.next();
				lMisuraSicurezza = generaMisuraSicurezza(lMisSicuSius);
				// Effettuo la Modifica della Misura di Sicurezza

				if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA)) {
					BigDecimal pIdMisuraSicurezza = getRequestBigDecimalParameter(
							ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA);
					if (pIdMisuraSicurezza.compareTo(lMisuraSicurezza.getIdMisuraSicurezza()) == 0) {
						lMisuraSicurezza.setNumAnni(getRequestBigDecimalParameter(
								ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA));
						lMisuraSicurezza.setNumMesi(getRequestBigDecimalParameter(
								ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA));
						lMisuraSicurezza.setNumGiorni(getRequestBigDecimalParameter(
								ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA));
						if (!isRequestParameterNullObj(
								ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA))
							lMisuraSicurezza.setDataDecorrenza(getRequestDateParameter(
									ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA,
									ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA,
									ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA));
					}
					setRequestAttribute("misuraSicurezza", lMisuraSicurezza);
				}
				lCtrlMisSic.ExModificaMisuraSicurezza(lMisuraSicurezza);
			}
		}

		// Modifica del 23/01/2014 Codice eliminato causa presenza
		// MAC nel rilascio della MEV1 (Errore in fase di eliminazione
		// del Provvedimento per presenza Misura di sicurezza)
		// In fase di modifica del provvedimento non bisogna inserire
		// una Nuova Misura di Sicurezza, se non è presente
		/*
		 * else { MisuraSicurezzaModel lNuovaMisura = new MisuraSicurezzaModel();
		 *
		 * lNuovaMisura.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		 * lNuovaMisura.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		 * lNuovaMisura.setDataInserimento(DateUtils.getSysDate());
		 * lNuovaMisura.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		 * lNuovaMisura.setCodNatura("-"); lNuovaMisura.setCodTipo("-");
		 * lNuovaMisura.setEveIdEvento(lIdEvento); lNuovaMisura.setDataDecorrenza(mDataDecorrenza); //
		 * Effettuo l'Inserimento della Misura di Sicurezza lNuovaMisura =
		 * lCtrlMisSic.ExInserisciMisuraSicurezza(lNuovaMisura); }
		 */

		// 10102014 - DL 92 2014 - In caso di Ordinanza col LICLIBANTICIPATA, l'aggiornamento viene
		// fatto dopo la preparazione dei periodi di LICLIBANTICIPATA

		String CodOrdinanza = "";
		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA) != null) {
			CodOrdinanza = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA);
		}

		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		if (CodOrdinanza.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) != 0
				|| CodOrdinanza.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMI_CEDU) == 0) {
			// Aggiornamento dei dati
			// lCtrl.ExAggiornaTenoriEvento(lTenori, lEvento, lIdOrdinanza, lIdDecreto);
			lCtrl.ExAggiornaTenoriEventoDOS(lTenori, lEvento, lIdOrdinanza, lIdDecreto, lIdSentenza);
		}

		// MEV_2023-35: aggiungo gestione rateizzazione
		if (CodOrdinanza
				.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE_MANCATO_PAGAMENTO) == 0) {
			if (mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U142")
					|| mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U145")) {
				// gestione rate anche in modifica???
				boolean isRateizzaPagamento = false;
				for (int i = 0; i < lTenori.length; i++) {
					TenoreModel tm = lTenori[i];
					if ("0159".equals(tm.getCodEsitoTenore())) {
						isRateizzaPagamento = true;
						break;
					}
				}
				if (!isRateizzaPagamento) {
					IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
					Vector<RateizzazionePPModel> rate = irpp.exRicercaRateizzazioniByIdFascicoloSius(
							mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
					if (!rate.isEmpty())
						irpp.exCancellaRateizzazioniByIdFascicoloSius(
								mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				}
			}
			if (mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U142")
					|| mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U143")) {
				boolean isConversione = false;
				for (int i = 0; i < lTenori.length; i++) {
					TenoreModel tm = lTenori[i];
					if ("0276".equals(tm.getCodEsitoTenore()) || "0277".equals(tm.getCodEsitoTenore())
							|| "0278".equals(tm.getCodEsitoTenore()) || "0279".equals(tm.getCodEsitoTenore())
							|| "0281".equals(tm.getCodEsitoTenore())) {
						isConversione = true;
						break;
					}
				}
				if (!isConversione) {
					DepositoOrdinanzaPcModel dopm = lCtrl.ExRicercaDepositoOrdinanzaPcByGenProc(
							mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					if (Utils.isPresent(dopm.getCodTipoSanzione())) {
						dopm.setSommaRisarcimento(null);
						dopm.setNumGiorniDetenzioneDom(null);
						dopm.setNumMesiDetenzioneDom(null);
						dopm.setNumAnniDetenzioneDom(null);
						dopm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
						dopm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
						dopm.setDataAggiornamento(DateUtils.getSysDate());
						dopm.setCodTipoSanzione(null);
						lCtrl.ExModificaDepositoOrdinanzaPc(dopm);
					}
				}
			}
		}
		// Aggiungo gestione Ordinanza Reclamo Avverso Revoca Pena Sostitutiva (C063)
		if (CodOrdinanza.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_AVVERSO_REVOCA_PENA_SOSTITUTIVA) == 0
				&& mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals(
						ICostantiDepositoOrdinanzaPc.COD_OGGETTO_RECLAMO_AVVERSO_REVOCA_PENA_SOSTITUTIVA)) {
			DepositoOrdinanzaPcModel dopm = lCtrl.ExRicercaDepositoOrdinanzaPcByGenProc(
					mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			IEsecuzioneSS iess = SIUSLookupRemote.getEsecuzioneSSRemote();
			EsecuzioneSanzioneSostitutivaModel essm = iess
					.ExRicercaEsecuzioneSanzioneSostitutivaByIdDepositoOrd(dopm.getIdDepositoOrdinanzaPc());
			boolean isReclamo = false;
			for (int i = 0; i < lTenori.length; i++) {
				TenoreModel tm = lTenori[i];
				// 3127 C063 0273 Accoglie reclamo e converte in altra pena sostitutiva
				if ("0273".equals(tm.getCodEsitoTenore())) {
					isReclamo = true;
					break;
				}
			}
			if (!isReclamo) {
				if (!Utils.isNullObj(essm) && !Utils.isNullObj(essm.getIdEsecuzioneSanzioneSost())) {
					// cancello il record di ESS se ho modificato l'esito dell'ordinanza
					iess.ExCancellaEsecuzioneSanzioneSostitutiva(essm.getIdEsecuzioneSanzioneSost());
					siesLogger.debug("HO CANCELLATO IL RECORD DI ESECUZIONE PENA SOSTITUTIVA!");
				}
			} else {
				EsecuzioneSanzioneSostitutivaModel essmNew = new EsecuzioneSanzioneSostitutivaModel();
				essmNew.setAnnoS07(mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1());
				essmNew.setProgrS07(mFasGPMod.getGeneraleProcedimentoModel().getProgrS1());
				essmNew.setCodTipoSanzione(
						getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE));
				essmNew.setNumAnniSanzione(
						getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE));
				essmNew.setNumMesiSanzione(
						getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_MESI_SANZIONE));
				essmNew.setNumGiorniSanzione(
						getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_GIORNI_SANZIONE));
				essmNew.setGenPridGeneraleProcedimento(
						mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				essmNew.setDepOpidDepositoOrdinanzaPc(dopm.getIdDepositoOrdinanzaPc());
				if (Utils.isNullObj(essm)) {
					// inserisco il record di ESS se ho modificato l'esito dell'ordinanza
					// e prima NON c'era il record di ESS
					essmNew.setCodOperatoreInserimento(getCodUtenteConnesso());
					essmNew.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					essmNew.setDataInserimento(DateUtils.getSysDate());
					iess.ExInserisciEsecuzioneSanzioneSostitutiva(essmNew);
				} else {
					// modifico il record di ESS se ho modificato l'esito dell'ordinanza
					// e prima c'era il record di ESS
					essmNew.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					essmNew.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					essmNew.setDataAggiornamento(DateUtils.getSysDate());
					essmNew.setIdEsecuzioneSanzioneSost(essm.getIdEsecuzioneSanzioneSost());
					iess.ExModificaEsecuzioneSanzioneSostitutiva(essmNew);
				}
			}
		}
		// FINE MEV_2023-35

		if (lIdOrdinanza != null) { // ORDINANZE
			DepositoOrdinanzaPcModel lOrdinanza = lCtrl.ExRicercaDepositoOrdinanzaPcByKey(lIdOrdinanza);
			// ============================================================================================================================
			// 10102014 - DL 92 2014 Violazione CEDU - COD_TIPO_ORDINANZA = "VC"
			// ============================================================================================================================
			if ((!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA))
					&& (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)
							.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) == 0
							|| getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)
											.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMI_CEDU) == 0)) {
				// =========================== DEPOSITO_ORDINANZA_PC ======================================
				// eventuali giorni di di Riduzione pena concessi
				int GiorniRiduzione = 0;
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
						&& getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU) != null
						&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
								.equals("")) {
					GiorniRiduzione = getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU).intValue();
				}

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" ActInserisciOrdinanzaViolazioneCEDU - XXXXX Periodo Concesso , gg = "+
				// GiorniRiduzione);

				// eventuale somma da liquidare per risarcimento
				String SommaRisarcimento = "";
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU)
						&& getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU) != null
						&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU)
								.equals("")) {
					SommaRisarcimento += getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU);
				}

				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU)
						&& getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU) != null
						&& !getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU).equals("")) {
					SommaRisarcimento += "." + getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU);
				}

				lOrdinanza.setNumGiorniRiduzionePena(new BigDecimal(GiorniRiduzione));
				if (!SommaRisarcimento.equals(""))
					lOrdinanza.setSommaRisarcimento(new BigDecimal(SommaRisarcimento));
				else
					lOrdinanza.setSommaRisarcimento(new BigDecimal(0));

				if (!isRequestParameterNullObj(
						ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)) {
					if (getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)
									.compareTo("") != 0)
						lOrdinanza.setCodUfficioMagistratoComp(
								getCodUfficioByCodTipoUfficioDescrComune("UDS", getRequestStringParameter(
										ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)));
					else
						lOrdinanza.setCodUfficioMagistratoComp("-");
				}

				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE)) {
					if (getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE) != null
							&& !getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE).equals("")
							&& !getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE).equals("-")) {
						lOrdinanza.setUlterioreDescrizione(getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE));
					}
				}

				// =========================== LICENZALIBANTICIPATA E PERIODILIBANTICIPATA
				// ======================================

				LicenzaPeriodiLibAnticipataModel[] lLicenze = null;
				lLicenze = AggiornaLicenzePeriodiCEDU(mFasGPMod, GiorniRiduzione, SommaRisarcimento);

				// -----------> MODIFICA = ExAggiornaTenoriEventoPeriodiLA;
				// parametri = Tenori, Evento, DeeposotoOrdinanzaPc, DepositoDecreto,
				// LicenzePeriodoLibAnticipata
				//
				lCtrl.ExAggiornaTenoriEventoPeriodiLA(lTenori, lEvento, lOrdinanza, null, lLicenze);
			} // chiude if (CAMPO_COD_TIPO_ORDINANZA = VC ) Ordinanza VIOLAZIONE CEDU

			// ============================================================================================================================
			// DL 146 2013 Nuova Ordinanza L.A. - COD_TIPO_ORDINANZA = "LA"
			// ============================================================================================================================
			if ((!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA))
					&& (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)
							.compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) == 0)) {
				int SommatotLA = 0; // somma gg di Ordinanza L.A. ORDINARIA
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA)) {
					if (getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA) != null
							&& getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA)
											.intValue() > 0) {
						if (getRequestBigDecimalParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue() > 0) {
							SommatotLA = getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue();
						}
					} else if (getRequestBigDecimalParameter(
							ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA) != null) {
						if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA)
								.intValue() > 0) {
							SommatotLA = getRequestBigDecimalParameter(
									ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA).intValue();
						}
					}
				}

				int SommatotLS = 0; // somma gg di Ordinanza L.A. SPECIALE
				if (!isRequestParameterNullObj(
						ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE)) {
					if (getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE) != null
							&& getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE)
											.intValue() > 0) {
						if (getRequestBigDecimalParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE)
										.intValue() > 0) {
							SommatotLS = getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE)
											.intValue();
						}
					} else if (getRequestBigDecimalParameter(
							ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE) != null) {
						if (getRequestBigDecimalParameter(
								ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE).intValue() > 0) {
							SommatotLS = getRequestBigDecimalParameter(
									ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE).intValue();
						}
					}
				}

				int SommatotLI = 0; // somma gg di Ordinanza L.A. INTEGRAZIONE
				if (!isRequestParameterNullObj(
						ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT)) {
					if (getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT) != null
							&& getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT)
											.intValue() > 0) {
						if (getRequestBigDecimalParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT)
										.intValue() > 0) {
							SommatotLI = getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT)
											.intValue();
						}
					} else if (getRequestBigDecimalParameter(
							ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT) != null) {
						if (getRequestBigDecimalParameter(
								ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT).intValue() > 0) {
							SommatotLI = getRequestBigDecimalParameter(
									ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT).intValue();
						}
					}
				}

				// lNumGiorniLA = somma di tutti i giorni concessi di tutti gli oggetti ORDINANZA
				int lNumGiorniTotLA = 0;
				lNumGiorniTotLA = SommatotLA + SommatotLS + SommatotLI;

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ------> MODIFICA - Numero Giorni Totali che vanno in in DepoOrdinanzaPC= "
						+ lNumGiorniTotLA);

				lOrdinanza.setNumGiorniLibanticipata(new BigDecimal(lNumGiorniTotLA));
				lCtrl.ExModificaDepositoOrdinanzaPc(lOrdinanza);

				// Cancellazione da LICENZA_LIBANTICIPATA (cascata PERIODO_LIBANTICIPATA) a partire
				// dall'evento

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl
						.ExCancellaLicenzeLibanticipataByEve(lOrdinanza.getIdEventoGenerato());
				// ----------

				// Inserimento dei nuovi record di licenza libanticipata
				// lLicenze = generaLicenzaPeriodiLibAnticipata(lOrdinanza.getIdEventoGenerato());
				// Nuova Ordinanza L.A. - Decreto 2013/146
				// Ordinanza L.A. ordinaria (LA)
				LicenzaPeriodiLibAnticipataModel[] lLicenze = null;
				LicenzaLibAnticipataModel lLicenzaC = null;

				if (!isRequestParameterNullObj(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE)) {
					mTipoConcessione = this.getRequestStringParameter(
							ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE);
					// lLicenze = generaLicenzaPeriodiLibAnticipataLA(lOrdinanza.getIdEventoGenerato(),
					// mFasGPMod, SommatotLA);
					lLicenze = generaLicenzaPeriodiLibAnticipataLA(mFasGPMod, SommatotLA);
					if (lLicenze != null) {
						EventoModel lEventoLicenze = null;
						IEvento lctrlEv = SICOLookupRemote.getEventoRemote();
						lEventoLicenze = lctrlEv.ExRicercaEventoByKey(lIdEvento);

						for (int i = 0; i < lLicenze.length; i++) {
							lLicenze[i].getLicenza().setEveIdEvento(lEventoLicenze.getIdEvento());
							lLicenze[i].getLicenza()
									.setFasSieIdFascicoloSiep(lEventoLicenze.getFasSieIdFascicoloSiep());
							lLicenze[i].getLicenza()
									.setDataEmissioneOrdinanza(lEventoLicenze.getDataEmissione());
							lLicenze[i].getLicenza()
									.setCodLuogoEmittente(lEventoLicenze.getCodLuogoEmittente());
							lLicenze[i].getLicenza()
									.setCodUfficioEmittente(lEventoLicenze.getCodUfficioEmittente());
							// Se necessario aggiungere anche info su aggiornamento (operatore, ufficio, data)
						}
					}

					String flagConcesso = "C";
					if (!isRequestParameterNullObj(
							ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO)) {
						// Controlla e imposta il flag
						if (getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO)
										.compareTo("S") == 0)
							flagConcesso = "S";
					}

					if (mTipoConcessione.compareTo("C") == 0
							&& !isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO)
							&& SommatotLA > 0) {
						// Periodo Unico senza check periodo, cioè con solo numero gg senza date dal .. al ..
						lLicenzaC = new LicenzaLibAnticipataModel();
						lLicenzaC.setCodTipoLicenza("LA");
						lLicenzaC.setCodOperatoreInserimento(getCodUtenteConnesso());
						lLicenzaC.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						lLicenzaC.setDataInserimento(mOggi);
						lLicenzaC.setFlagConcesso(flagConcesso);

						if (SommatotLA > 0)
							lLicenzaC.setNumeroGiorni(new BigDecimal(SommatotLA));

						if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
							lLicenzaC.setFasSiuIdFascicoloSius(
									mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

						lLicenzaC
								.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
						lLicenzaC.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
						// L.A. NORMALE
						lLicenzaC.setDescrStatoPermesso("LAU");
						lLicenzaC.setFlagScorta(mTipoConcessione);
					}
				}

				// Ordinanza L.A. SPECIALE (LS)
				LicenzaPeriodiLibAnticipataModel[] lLicenze_spe = null;
				LicenzaLibAnticipataModel lLicenzaC_SPE = null;

				if (!isRequestParameterNullObj(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE)) {
					mTipoConcessione_spe = this.getRequestStringParameter(
							ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE);
					// lLicenze_spe =
					// generaLicenzaPeriodiLibAnticipataLA_SPE(lOrdinanza.getIdEventoGenerato(), mFasGPMod,
					// SommatotLS);
					lLicenze_spe = generaLicenzaPeriodiLibAnticipataLA_SPE(mFasGPMod, SommatotLS);
					if (lLicenze_spe != null) {
						EventoModel lEventoLicenze = null;
						IEvento lctrlEv = SICOLookupRemote.getEventoRemote();
						lEventoLicenze = lctrlEv.ExRicercaEventoByKey(lIdEvento);

						for (int i = 0; i < lLicenze_spe.length; i++) {
							lLicenze_spe[i].getLicenza().setEveIdEvento(lEventoLicenze.getIdEvento());
							lLicenze_spe[i].getLicenza()
									.setFasSieIdFascicoloSiep(lEventoLicenze.getFasSieIdFascicoloSiep());
							lLicenze_spe[i].getLicenza()
									.setDataEmissioneOrdinanza(lEventoLicenze.getDataEmissione());
							lLicenze_spe[i].getLicenza()
									.setCodLuogoEmittente(lEventoLicenze.getCodLuogoEmittente());
							lLicenze_spe[i].getLicenza()
									.setCodUfficioEmittente(lEventoLicenze.getCodUfficioEmittente());
							// Se necessario aggiungere anche info su aggiornamento (operatore, ufficio, data)
						}
					}

					String flagConcesso_spe = "C";

					if (!isRequestParameterNullObj(
							ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO)) {
						// Controlla e imposta il flag
						if (getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO)
										.compareTo("S") == 0)
							flagConcesso_spe = "S";
					}

					if (mTipoConcessione_spe.compareTo("C") == 0
							&& !isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE)
							&& SommatotLS > 0) {
						lLicenzaC_SPE = new LicenzaLibAnticipataModel();
						lLicenzaC_SPE.setCodTipoLicenza("LA");
						lLicenzaC_SPE.setCodOperatoreInserimento(getCodUtenteConnesso());
						lLicenzaC_SPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						lLicenzaC_SPE.setDataInserimento(mOggi);
						lLicenzaC_SPE.setFlagConcesso(flagConcesso_spe);

						if (SommatotLS > 0)
							lLicenzaC_SPE.setNumeroGiorni(new BigDecimal(SommatotLS));

						if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
							lLicenzaC_SPE.setFasSiuIdFascicoloSius(
									mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

						lLicenzaC_SPE
								.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
						lLicenzaC_SPE.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
						// L.A. SPECIALE
						lLicenzaC_SPE.setDescrStatoPermesso("LSU");
						lLicenzaC_SPE.setFlagScorta(mTipoConcessione_spe);
					}
				}

				// Ordinanza L.A. INTEGRAZIONE (LI)
				LicenzaPeriodiLibAnticipataModel[] lLicenze_int = null;
				LicenzaLibAnticipataModel lLicenzaC_INT = null;

				if (!isRequestParameterNullObj(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT)) {
					mTipoConcessione_int = this.getRequestStringParameter(
							ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT);
					lLicenze_int = generaLicenzaPeriodiLibAnticipataLA_INT(mFasGPMod, SommatotLI);
					if (lLicenze_int != null) {
						EventoModel lEventoLicenze = null;
						IEvento lctrlEv = SICOLookupRemote.getEventoRemote();
						lEventoLicenze = lctrlEv.ExRicercaEventoByKey(lIdEvento);

						for (int i = 0; i < lLicenze_int.length; i++) {
							lLicenze_int[i].getLicenza().setEveIdEvento(lEventoLicenze.getIdEvento());
							lLicenze_int[i].getLicenza()
									.setFasSieIdFascicoloSiep(lEventoLicenze.getFasSieIdFascicoloSiep());
							lLicenze_int[i].getLicenza()
									.setDataEmissioneOrdinanza(lEventoLicenze.getDataEmissione());
							lLicenze_int[i].getLicenza()
									.setCodLuogoEmittente(lEventoLicenze.getCodLuogoEmittente());
							lLicenze_int[i].getLicenza()
									.setCodUfficioEmittente(lEventoLicenze.getCodUfficioEmittente());
							// Se necessario aggiungere anche info su aggiornamento (operatore, ufficio, data)
						}
					}

					String flagConcesso_int = "C";

					if (!isRequestParameterNullObj(
							ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO)) {
						// Controlla e imposta il flag
						if (getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO)
										.compareTo("S") == 0)
							flagConcesso_int = "S";
					}

					if (mTipoConcessione_int.compareTo("C") == 0
							&& !isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT)
							&& SommatotLI > 0) {
						lLicenzaC_INT = new LicenzaLibAnticipataModel();
						lLicenzaC_INT.setCodTipoLicenza("LA");
						lLicenzaC_INT.setCodOperatoreInserimento(getCodUtenteConnesso());
						lLicenzaC_INT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						lLicenzaC_INT.setDataInserimento(mOggi);
						lLicenzaC_INT.setFlagConcesso(flagConcesso_int);

						if (SommatotLI > 0)
							lLicenzaC_INT.setNumeroGiorni(new BigDecimal(SommatotLI));

						if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
							lLicenzaC_INT.setFasSiuIdFascicoloSius(
									mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

						lLicenzaC_INT
								.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
						lLicenzaC_INT.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
						// L.A. INTEGRAZIONE
						lLicenzaC_INT.setDescrStatoPermesso("LIU");
						lLicenzaC_INT.setFlagScorta(mTipoConcessione_int);
					}
				} // chiude
					// if(!isRequestParameterNullObj(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT))

				// INSERIMENTO ORDINANZA MODIFICATA
				LicenzaPeriodiLibAntCtrl.ExInserisciNewLicenzeLibanticipata(lLicenze, lLicenze_spe,
						lLicenze_int, lLicenzaC, lLicenzaC_SPE, lLicenzaC_INT,
						lOrdinanza.getIdEventoGenerato());
			} // chiude if ((!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA
				// = L.A. ))

			/*
			 * ISSUE MEV : aggiunta nuova gestione campi rinvio 
			 * Numero MEV : 39 
			 * Autore : Gioggi 
			 * Data : 09/giu/2017 
			 * Branch : MEV_39
			 */
			String codTipoOrdinanza = "";
			if (lOrdinanza != null)
				codTipoOrdinanza = lOrdinanza.getCodTipoOrdinanza();
			if (codTipoOrdinanza.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MS) == 0
					|| codTipoOrdinanza.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MSIC) == 0) {
				// Data Decorrenza Sospensione
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO)
						&& !isRequestParameterNullObj(
								ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO)
						&& !isRequestParameterNullObj(
								ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO)) {
					Date dataInizioPeriodo = getRequestDateParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO,
							ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO,
							ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO);
					lOrdinanza.setDataInizioPeriodo(dataInizioPeriodo);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data Differimento Esecuzione: " + dataInizioPeriodo);
				}
				// Data Fino al
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA)
						&& !isRequestParameterNullObj(
								ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA)
						&& !isRequestParameterNullObj(
								ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA)) {
					Date dataFineMisura = getRequestDateParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA,
							ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA,
							ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA);
					lOrdinanza.setDataFineMisura(dataFineMisura);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data Rinvio Fino al: " + dataFineMisura);
				}
				// #### Periodo Sospensione #####
				// ------ Anni Periodo Sospensione ----
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS)) {
					BigDecimal lAnniSospensioneSS = getRequestBigDecimalParameter(
							ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS);
					lOrdinanza.setSospensioneAASS(lAnniSospensioneSS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Anni Rinvio nella Misura di: " + lAnniSospensioneSS);
				}
				// ------ Mesi Periodo Sospensione ----
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS)) {
					BigDecimal lMesiSospensioneSS = getRequestBigDecimalParameter(
							ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS);
					lOrdinanza.setSospensioneMMSS(lMesiSospensioneSS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Mesi Rinvio nella Misura di: " + lMesiSospensioneSS);
				}
				// ------ Giorni Periodo Sospensione ----
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS)) {
					BigDecimal lGiorniSospensioneSS = getRequestBigDecimalParameter(
							ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS);
					lOrdinanza.setSospensioneGGSS(lGiorniSospensioneSS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Giorni Rinvio nella Misura di: " + lGiorniSospensioneSS);
				}
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA)) {
					// Lettura Luogo Svolgimento della Prova.
					String mLuogo = getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA);
					lOrdinanza.setLuogoSvolgimentoProva(mLuogo);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Luogo di Ricovero: " + mLuogo);
				}
				lCtrl.ExModificaDepositoOrdinanzaPc(lOrdinanza);
			}
			// ***** FINE INTERVENTO MEV_39 *****//

			/*
			 * ISSUE MEV : aggiunto codice per gestione oggetto C029 
			 * Numero MEV : 39 
			 * Autore : Gioggi 
			 * Data : 19/giu/2017 
			 * Branch : MEV_39
			 */
			if (codTipoOrdinanza.equalsIgnoreCase(ICostantiDepositoOrdinanzaPc.APPELLO_MS)) {
				IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
				for (TenoreModel tenore : lTenori) {
					String codEsitoTenore = tenore.getCodEsitoTenore();
					if (ICostantiMisuraSicurezza.COD_ACCOGLIE_APPELLO_E_MODIFICA_MDS
							.equalsIgnoreCase(codEsitoTenore)) {
						// ricerco ms per id evento
						List ms = ims.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(
								mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
						MisuraSicurezzaModel misuraSicurezzaModel = new MisuraSicurezzaModel();
						misuraSicurezzaModel.setEveIdEvento(lIdEvento);
						misuraSicurezzaModel.setCodTipo(getRequestStringParameter("codiTipoNuovaMisura"));
						Collection c = DecodificheManager.getInstance().getTipoMisuraSicurezza();
						String natura = DecodificheUtils.getFiltrobyCode(c,
								getRequestStringParameter("codiTipoNuovaMisura"));
						misuraSicurezzaModel.setCodNatura(natura);
						// 20191018 [SG]: aggiunto codice
						String descrTipo = DecodificheUtils.getDescbyCode(c,
								getRequestStringParameter("codiTipoNuovaMisura"));
						misuraSicurezzaModel.setDescrTipo(descrTipo);
						if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA))
							misuraSicurezzaModel.setFlFormaMisura(getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
						if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA))
							misuraSicurezzaModel.setDescrizioneComunita(getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
						misuraSicurezzaModel.setFasSiuIdFascicoloSius(
								mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
						misuraSicurezzaModel.setNumAnni(getRequestBigDecimalParameter("anniDurataNuovaMS"));
						misuraSicurezzaModel.setNumMesi(getRequestBigDecimalParameter("mesiDurataNuovaMS"));
						misuraSicurezzaModel
								.setNumGiorni(getRequestBigDecimalParameter("giorniDurataNuovaMS"));
						Date dataDecorrenzaNuovaMS = null;
						if (!isRequestParameterNullObj("annoDataDecorrenzaNuovaMS")) {
							dataDecorrenzaNuovaMS = getRequestDateParameter("annoDataDecorrenzaNuovaMS",
									"meseDataDecorrenzaNuovaMS", "giornoDataDecorrenzaNuovaMS");
						}
						misuraSicurezzaModel.setDataDecorrenza(dataDecorrenzaNuovaMS);
						if (ms.isEmpty()) {
							misuraSicurezzaModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
							misuraSicurezzaModel.setCodOperatoreInserimento(getCodUtenteConnesso());
							misuraSicurezzaModel.setDataInserimento(DateUtils.getSysDate());
							ims.ExInserisciMisuraSicurezza(misuraSicurezzaModel);
						} else {
							MisuraSicurezzaModel originalMS = (MisuraSicurezzaModel) ms.get(0);
							misuraSicurezzaModel.setIdMisuraSicurezza(originalMS.getIdMisuraSicurezza());
							misuraSicurezzaModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
							misuraSicurezzaModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
							misuraSicurezzaModel.setDataAggiornamento(DateUtils.getSysDate());
							ims.ExModificaMisuraSicurezza(misuraSicurezzaModel);
						}
						// 20191018 [SG]: aggiunto codice
						setRequestAttribute("misuraSicurezza", misuraSicurezzaModel);
					}
				}
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE)) {
					if (getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE) != null
							&& !getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE).equals("")
							&& !getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE).equals("-")) {
						lOrdinanza.setUlterioreDescrizione(getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE));
					}
				}
				lCtrl.ExModificaDepositoOrdinanzaPc(lOrdinanza);
			}
			// ***** FINE INTERVENTO MEV_39 *****//

			// MEV_63: aggiunto codice per gestire modifica MA
			// String codOggettoProcedimento = mFasGPMod.getGeneraleProcedimentoModel()
			// .getCodOggettoProcedimento();
			// if ((codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_PROC_CONCESSIONE_MISURE_ALTERNATIVA)
			// || codOggettoProcedimento
			// .equalsIgnoreCase(COD_OGGETTO_PROC_CONCESSIONE_LIBERAZIONE_CONDIZIONALE))
			// && super.isUserTDSM()) {
			// modificaMisuraAlternativa();
			// }
			//
			// if ((codOggettoProcedimento
			// .equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA)
			// || codOggettoProcedimento
			// .equalsIgnoreCase(COD_OGGETTO_ESECUZIONE_PRESSO_DOMICILIO_PENA_DETENTIVA))
			// && super.isUserUDSM()) {
			// modificaMisuraAlternativa();
			// }
		} else if (lIdDecreto != null) { // DECRETO
			IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
			DepositoDecretoModel lDecretoMod = lCtrlDD.ExRicercaDepositoDecretoByEvento(lIdEvento);
			LicenzaPeriodiLibAnticipataModel[] lLicenze = null;
			// MEV_39: aggiunta variabile
			String codTipoDecreto = "";
			if (lDecretoMod != null)
				codTipoDecreto = lDecretoMod.getCodTipoDecreto();
			// uso CodOrdinanza anche se si tratta di DECRETO (Il cod è lo stesso)
			if (CodOrdinanza.compareTo("VC") == 0) {
				// IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
				// DepositoDecretoModel lDecretoMod = lCtrlDD.ExRicercaDepositoDecretoByEvento(lIdEvento);
				// ------------> DEPOSITO_DECRETO
				// eventuali giorni di di Riduzione pena concessi
				int GiorniRiduzione = 0;
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
						&& getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU) != null
						&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
								.equals("")) {
					GiorniRiduzione = getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU).intValue();
				}

				// eventuale somma da liquidare per risarcimento
				String SommaRisarcimento = "";
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU)
						&& getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU) != null
						&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU)
								.equals("")) {
					SommaRisarcimento += getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU);
				}

				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU)
						&& getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU) != null
						&& !getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU).equals("")) {
					SommaRisarcimento += "." + getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU);
				}

				lDecretoMod.setNumeroGiorniRiduzionePena(new BigDecimal(GiorniRiduzione));
				if (!SommaRisarcimento.equals(""))
					lDecretoMod.setSommaRisarcimentoDanni(new BigDecimal(SommaRisarcimento));
				else
					lDecretoMod.setSommaRisarcimentoDanni(new BigDecimal(0));

				if (!isRequestParameterNullObj(
						ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)) {
					if (getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)
									.compareTo("") != 0)
						lDecretoMod.setCodUfficioCompetente(
								getCodUfficioByCodTipoUfficioDescrComune("UDS", getRequestStringParameter(
										ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)));
					else
						lDecretoMod.setCodUfficioCompetente("-");
				}

				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE)) {
					if (getRequestStringParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE) != null
							&& !getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE).equals("")
							&& !getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE).equals("-")) {
						lDecretoMod.setNote(getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE));
					}
				}

				// ----------> LICENZALIBANTICIPATA e PERIODOLIBANTICIPATA
				lLicenze = AggiornaLicenzePeriodiCEDU(mFasGPMod, GiorniRiduzione, SommaRisarcimento);
			} // Chiude DECRETO VIOLAZIONE CEDU
			/*
			 * ISSUE MEV : aggiunta nuova gestione campi rinvio 
			 * Numero MEV : 39 
			 * Autore : Gioggi 
			 * Data : 09/giu/2017 
			 * Branch : MEV_39
			 */
			else if (codTipoDecreto.compareTo("42") == 0) {
				// Data Decorrenza Sospensione
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS)
						&& !isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS)
						&& !isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS)) {
					Date lDataDecorrenzaSS = getRequestDateParameter(
							ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS,
							ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS,
							ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS);
					lDecretoMod.setDataSospensioneSS(lDataDecorrenzaSS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data Differimento Esecuzione: " + lDataDecorrenzaSS);
				}
				// Data Fino al
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS)
						&& !isRequestParameterNullObj(
								ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS)
						&& !isRequestParameterNullObj(
								ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS)) {
					Date lDataFinoAlSS = getRequestDateParameter(
							ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS,
							ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS,
							ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS);
					lDecretoMod.setDataScadenzaSospensioneSS(lDataFinoAlSS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data Rinvio Fino al: " + lDataFinoAlSS);
				}
				// #### Periodo Sospensione #####
				// ------ Anni Periodo Sospensione ----
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS)) {
					BigDecimal lAnniSospensioneSS = getRequestBigDecimalParameter(
							ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS);
					lDecretoMod.setSospensioneAASS(lAnniSospensioneSS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Anni Rinvio nella Misura di: " + lAnniSospensioneSS);
				}
				// ------ Mesi Periodo Sospensione ----
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS)) {
					BigDecimal lMesiSospensioneSS = getRequestBigDecimalParameter(
							ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS);
					lDecretoMod.setSospensioneMMSS(lMesiSospensioneSS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Mesi Rinvio nella Misura di: " + lMesiSospensioneSS);
				}
				// ------ Giorni Periodo Sospensione ----
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS)) {
					BigDecimal lGiorniSospensioneSS = getRequestBigDecimalParameter(
							ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS);
					lDecretoMod.setSospensioneGGSS(lGiorniSospensioneSS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Giorni Rinvio nella Misura di: " + lGiorniSospensioneSS);
				}
				if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA)) {
					// Lettura Luogo Svolgimento della Prova.
					String mLuogo = getRequestStringParameter(
							ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA);
					lDecretoMod.setLuogoSvolgimentoProva(mLuogo);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Luogo di Ricovero: " + mLuogo);
				}
			}
			// ***** FINE INTERVENTO MEV_39 *****//

			lDecretoMod.setDataEmissione(mDataEmissione);
			lDecretoMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lDecretoMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lDecretoMod.setDataAggiornamento(mOggi);

			// -----------> MODIFICA = ExAggiornaTenoriEventoPeriodiLA;
			// parametri = Tenori, Evento, DeeposotoOrdinanzaPc, DepositoDecreto,
			// LicenzePeriodoLibAnticipata
			//
			lCtrl.ExAggiornaTenoriEventoPeriodiLA(lTenori, lEvento, null, lDecretoMod, lLicenze);
		} // CHIUDE else if(lIdDecreto != null)

		// if ((!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA))
		// && (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)
		// .compareTo(ICostantiDepositoOrdinanzaPc.RECLAMI_CEDU) == 0)) {
		// modificaOrdinanzaReclamiCEDU();
		// }

		// Redirezione alla pagina di dettaglio Ordinanza, Decreto o Sentenza
		if (lIdOrdinanza != null)
			lRetPage = dettaglioOrdinanza(lIdEvento);
		else if (lIdSentenza != null)
			lRetPage = dettaglioSentenza(lIdEvento);
		else
			lRetPage = dettaglioDecreto(lIdEvento);

		return lRetPage;
	}

	/**
	 * Prepara il Model per la Misura di Sicurezza da aggiornare.
	 *
	 * @param lMisSicuSius
	 *            Misura di Sicurezza da aggiornare.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return MisuraSicurezzaModel ritorna la Misura di Sicurezza model.
	 */
	private MisuraSicurezzaModel generaMisuraSicurezza(MisuraSicurezzaModel lMisSicuSius)
			throws F3BException {

		lMisSicuSius.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lMisSicuSius.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lMisSicuSius.setDataAggiornamento(DateUtils.getSysDate());
		// MERGE v10: non devo sempre aggiornare questo dato
		// lMisSicuSius.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		// MERGE v10: aggiunta gestione oggetto/esito
		if (Utils.isPresent(lMisSicuSius.getFlFormaMisura())) {
			if (isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA))
				lMisSicuSius.setFlFormaMisura(null);
			else {
				lMisSicuSius.setFlFormaMisura(
						getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
				lMisSicuSius.setDescrizioneComunita(null);
				if (new BigDecimal(2).equals(
						getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA))
						&& !isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA))
					lMisSicuSius.setDescrizioneComunita(
							getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
			}
		}

		lMisSicuSius.setDataDecorrenza(mDataDecorrenza);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Misura di sicurezza = " + lMisSicuSius);

		return lMisSicuSius;
	}

	/**
	 * Prepara il Model per l'evento da aggiornare.
	 * <p>
	 *
	 * @param aDataEmissione
	 *            Date data di emissione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return EventoModel ritorna l'evento model.
	 */
	private EventoModel generaEvento() throws F3BException {

		// Prepara Model Evento.
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" - ActModificaProvvedimento - generaEvento");
		EventoModel lEvento = new EventoModel();
		lEvento.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lEvento.setDataEmissione(mDataEmissione);
		lEvento.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lEvento.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEvento.setDataAggiornamento(mOggi);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-----> C Evento generato = " + lEvento);
		return lEvento;
	}

	/**
	 * Aggiorna l'esito per ognuno dei tenori collegati al provvedimento leggento i dati dalla form di
	 * modifica.
	 *
	 * @return
	 * @throws F3BException
	 */
	private TenoreModel[] generaTenori() throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" - ActModificaProvvedimento - generaTenori");
		TenoreModel[] lTenori = null;

		// Lettura ID ed Esito per i Tenori.

		String[] lIdTenori = getRequestStringParameters(ICostantiTenore.CAMPO_ID_TENORE);
		String[] lCodEsiti = getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE);
		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();

		int lSizeArray = lCodEsiti.length;
		if (lSizeArray > 0) {
			lTenori = new TenoreModel[lSizeArray];
			for (int i = 0; i < lSizeArray; i++) {
				TenoreModel lTenModel = new TenoreModel();

				lTenModel.setIdTenore(new BigDecimal(lIdTenori[i]));
				lTenModel.setCodEsitoTenore(lDecCtrl.ExRicercaCodEsitiProvByCodTenore(lCodEsiti[i]));
				// Codice dell'ufficio dell'operatore che aggiorna
				lTenModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				// Codice dell'operatore che aggiorna
				lTenModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lTenModel.setDataAggiornamento(mOggi);
				// Data del tenore è uguale alla data di emissione
				lTenModel.setData(mDataEmissione);

				// Inserimenti i-esimo Tenore
				lTenori[i] = lTenModel;

				// Impostazione del Flag Concessione ad "S" per l'esito di Scomputo
				// if (lTenModel.getCodEsitoTenore().compareTo("0034") == 0)
				// mFlagConcessione = "S";

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("------> C - Tenore Generato N." + i + " = " + lTenori[i]);

			}
		}
		return lTenori;
	}

	//
	/**
	 * Aggiorna i periodi di liberazione anticipata leggento i dati dalla form di modifica.
	 *
	 * @return
	 * @throws F3BException
	 */
	private LicenzaPeriodiLibAnticipataModel[] generaLicenzaPeriodiLibAnticipataLA(FascicoloGPModel mFasGPMod,
			int SommatotLA) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("- ActModificaprovvedimento - generaLicenzaPeriodiLibAnticipataLA - L.A. NORMALE ");

		// -------------------------------------------------------------
		// >>>>>>>>>>>>>>>>> L.A. NORMALE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//
		LicenzaPeriodiLibAnticipataModel[] lLicenze = null;

		String[] lChecks = null;
		// String[] lDate = null;
		// PeriodoClass[] periodi = null;

		int numCheck = 0;
		int numCheckConcessi = 0;

		boolean lConcessi = false;
		boolean lPeriodo = false;
		boolean lRigettati = false;
		boolean lInammissibili = false;
		boolean lNLP = false;

		mTipoConcessione = this
				.getRequestStringParameter(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE); // S
																										// semestri
																										// - C
																										// periodo
																										// Unico

		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI)) {
			// Numero chek periodi da 45 gg ( se CAMPO_RADIO_TIPO_CONCESSIONE = S)
			lChecks = getRequestStringParameters(ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI);
			numCheck += lChecks.length;
			lConcessi = true;
			numCheckConcessi = numCheck;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO)) {
			// check per inserimento periodo in periodo Unico ( se CAMPO_RADIO_TIPO_CONCESSIONE = C)
			numCheck++;
			lPeriodo = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI)) {
			numCheck++;
			lRigettati = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI)) {
			numCheck++;
			lInammissibili = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_NLP)) {
			numCheck++;
			lNLP = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek L.A. " + numCheck);

		// Per i Reclami in materia di LA se l'esito è 'Accoglie reclamo del PM'
		// il FLAG_CONCESSO va impostato ad 'S' -- Michele

		String flagConcesso = "C";

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO)
					.compareTo("S") == 0)
				flagConcesso = "S";
		}

		if (numCheck > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("LicLib");
			lLicenze = new LicenzaPeriodiLibAnticipataModel[numCheck];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi = leggiDate();

			if (lConcessi) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" --> Semestri Concessi");

				if (flagConcesso.compareTo("S") != 0)
					flagConcesso = "C";
				while (i < numCheckConcessi) {
					lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));
					lLicenze[i].getLicenza().setFlagScorta(mTipoConcessione);
					setPeriodiInLicenze(lLicenze[i]);
					i++;
				}
			}
			if (lPeriodo) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("--> PeriodoUnico con date dal al");
				if (flagConcesso.compareTo("S") != 0)
					flagConcesso = "C";

				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));

				if (SommatotLA > 0)
					lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLA));

				lLicenze[i].getLicenza().setFlagScorta(mTipoConcessione);
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}

			if (lRigettati) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" -------> Rigettati");
				flagConcesso = "R";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}
			if (lInammissibili) {
				flagConcesso = "I";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}
			if (lNLP) {
				flagConcesso = "N";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}

		} // Chiude if numCheck > 0

		return lLicenze;
		//
		// END L.A. NORMALE
		//
	} //

	private LicenzaPeriodiLibAnticipataModel[] generaLicenzaPeriodiLibAnticipataLA_SPE(
			FascicoloGPModel mFasGPMod, int SommatotLS) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				" ActModificaprovvedimento - generaLicenzaPeriodiLibAnticipataLA_SPE - L.A. SPECIALE ");
		//
		// >>>>>>>>>>>>>>>>> L.A. SPECIALE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//
		LicenzaPeriodiLibAnticipataModel[] lLicenze_spe = null;

		String[] lChecks_spe = null;
		// String[] lDate_spe = null;
		// PeriodoClass[] periodi_spe = null;

		int numCheck_spe = 0;
		int numCheckConcessi_spe = 0;

		boolean lConcessi_spe = false;
		boolean lPeriodo_spe = false;
		boolean lRigettati_spe = false;
		boolean lInammissibili_spe = false;
		boolean lNLP_spe = false;
		//
		mTipoConcessione_spe = this
				.getRequestStringParameter(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE); // S
																											// semestri
																											// -
																											// C
																											// periodo
																											// Unico

		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE)) { // Numero chek periodi da
																						// 75 gg ( se
																						// CAMPO_RADIO_TIPO_CONCESSIONE
																						// = S)
			lChecks_spe = getRequestStringParameters(ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE);
			numCheck_spe += lChecks_spe.length;
			lConcessi_spe = true;
			numCheckConcessi_spe = numCheck_spe;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE)) { // check per inserimento
																					// periodo in periodo
																					// Unico ( se
																					// CAMPO_RADIO_TIPO_CONCESSIONE
																					// = C)
			numCheck_spe++;
			lPeriodo_spe = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE)) {
			numCheck_spe++;
			lRigettati_spe = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE)) {
			numCheck_spe++;
			lInammissibili_spe = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE)) {
			numCheck_spe++;
			lNLP_spe = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek per L.A. Speciale " + numCheck_spe);
		// Per i Reclami in materia di LA se l'esito è 'Accoglie reclamo del PM'
		// il FLAG_CONCESSO va impostato ad 'S' -- Michele

		String flagConcesso_spe = "C";

		if (!isRequestParameterNullObj(
				ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO)
							.compareTo("S") == 0)
				flagConcesso_spe = "S";

		}

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A.SPECIALE e Periodi concessi.
		if (numCheck_spe > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" --> numCheck_spe > 0 = SPE "+numCheck_spe);
			lLicenze_spe = new LicenzaPeriodiLibAnticipataModel[numCheck_spe];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi_spe = leggiDate_spe();

			if (lConcessi_spe) {
				if (flagConcesso_spe.compareTo("S") != 0)
					flagConcesso_spe = "C";

				while (i < numCheckConcessi_spe) {
					lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
					lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);
					setPeriodiInLicenze_spe(lLicenze_spe[i]);
					i++;
				}
			}
			if (lPeriodo_spe) {
				if (flagConcesso_spe.compareTo("S") != 0)
					flagConcesso_spe = "C";

				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));

				if (SommatotLS > 0)
					lLicenze_spe[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLS));

				lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lRigettati_spe) {
				flagConcesso_spe = "R";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lInammissibili_spe) {
				flagConcesso_spe = "I";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lNLP_spe) {
				flagConcesso_spe = "N";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

		} // chiude if numcheck_spe > 0

		return lLicenze_spe;
		//
		// END L.A. SPECIALE
		//
	} //

	private LicenzaPeriodiLibAnticipataModel[] generaLicenzaPeriodiLibAnticipataLA_INT(
			FascicoloGPModel mFasGPMod, int SommatotLI) throws F3BException {

		// -------------------------------------------------------------
		// >>>>>>>>>>>>>>>>> L.A. INTEGRAZIONE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				" actmodificaprovvedimento - generaLicenzaPeriodiLibAnticipataLA_INT - L.A. INTEGRAZIONE");
		//
		LicenzaPeriodiLibAnticipataModel[] lLicenze_int = null;

		String[] lChecks_int = null;
		// String[] lDate_int = null;
		// PeriodoClass[] periodi_int = null;

		int numCheck_int = 0;
		int numCheckConcessi_int = 0;

		boolean lConcessi_int = false;
		boolean lPeriodo_int = false;
		boolean lRigettati_int = false;
		boolean lInammissibili_int = false;
		boolean lNLP_int = false;
		//

		mTipoConcessione_int = this
				.getRequestStringParameter(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--> tipo concessione _int (radio Button semestri/periodo unico) = " +
		// mTipoConcessione_int);

		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT)) {
			lChecks_int = getRequestStringParameters(ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT);
			numCheck_int += lChecks_int.length;
			lConcessi_int = true;
			numCheckConcessi_int = numCheck_int;
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> LA I - CAMPO_CHECK_CONCESSI_INT - numCheckConcessi_int = " +
			// numCheckConcessi_int);
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT)) {
			numCheck_int++;
			lPeriodo_int = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT)) {
			numCheck_int++;
			lRigettati_int = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT)) {
			numCheck_int++;
			lInammissibili_int = true;
		}
		if (isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT)) {
			numCheck_int++;
			lNLP_int = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek per L.A. Integrazione" + numCheck_int);

		// Per i Reclami in materia di LA se l'esito è 'Accoglie reclamo del PM'
		// il FLAG_CONCESSO va impostato ad 'S' -- Michele

		String flagConcesso_int = "C";

		if (!isRequestParameterNullObj(
				ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO)
							.compareTo("S") == 0)
				flagConcesso_int = "S";

		}

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A.INTEGRAZIONE e Periodi concessi.
		if (numCheck_int > 0) {
			lLicenze_int = new LicenzaPeriodiLibAnticipataModel[numCheck_int];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi_int = leggiDate_int();

			if (lConcessi_int) {
				if (flagConcesso_int.compareTo("S") != 0)
					flagConcesso_int = "C";

				while (i < numCheckConcessi_int) {
					lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
					lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione_int);
					setPeriodiInLicenze_int(lLicenze_int[i]);
					i++;
				}
			}
			if (lPeriodo_int) {
				if (flagConcesso_int.compareTo("S") != 0)
					flagConcesso_int = "C";

				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));

				if (SommatotLI > 0)
					lLicenze_int[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLI));

				lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione_int);
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}

			if (lRigettati_int) {
				flagConcesso_int = "R";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}
			if (lInammissibili_int) {
				flagConcesso_int = "I";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}
			if (lNLP_int) {
				flagConcesso_int = "N";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}

		} // chiude if numcheck_int > 0

		return lLicenze_int;
		//
		// END L.A. INTEGRAZIONE
		//
	}

	// >>>>>>>> L.A NORMALE preparazione di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate() throws F3BException {

		PeriodoClass[] lPeriodi = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("leggiDate");

		lDateInizio = getRequestDateParameters(ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO,
				ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO,
				ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO);
		lDateFine = getRequestDateParameters(ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE,
				ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE,
				ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Numero date " + lNumDate);

		lPeriodi = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi[i] = new PeriodoClass();
			lPeriodi[i].mDataIni = lDateInizio[i];
			lPeriodi[i].mDataFine = lDateFine[i];
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataIni = " + lPeriodi[i].mDataIni);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataFine = " +
			// lPeriodi[i].mDataFine);

		}

		return lPeriodi;
	} // chude leggiDate()

	private void setPeriodiInLicenze(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "setPeriodiInLicenze INI");

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd; // Salvataggio del valore corrente di mInd

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("---- > setPeriodiInLicenze - inizio lInd = "+lInd);

		for (int j = 0, k = mInd; j < 10; j++, k++)
			if ((mPeriodi[k].mDataIni != null) && (mPeriodi[k].mDataFine != null))
				num++;

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd
		for (int j = 0; j < num; mInd++) {
			if ((mPeriodi[mInd].mDataIni != null) && (mPeriodi[mInd].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa(
						aLicenza.getLicenza().getFlagConcesso());
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("data nulla ");
		}

		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.

		mInd = lInd + 10;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".setPeriodiInLicenze FINE");
	} // chiude setPeriodiInLicenze

	private LicenzaLibAnticipataModel generaLicenza(String aFlagConcesso, FascicoloGPModel mFasGPMod)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".generaLicenza INI ");

		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza("LA");
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (aFlagConcesso.compareTo("C") == 0 || aFlagConcesso.compareTo("S") == 0)
			lLicenza.setNumeroGiorni(new BigDecimal(45));

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante ");

		// Si inseriscono i dati che servono alla Procura !?! Luigi 30-6-2005
		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
		// L.A. NORMALE
		lLicenza.setDescrStatoPermesso("LA");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".generaLicenza FINE ");

		return lLicenza;
	} // chiude generaLicenza(---)

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa(String aFlagConcesso) throws F3BException {

		// Valorizza un Periodo leggendo le date all'indice mInd nell'Array mPeriodi
		PeriodoLibAnticipataModel lPeriodoLib = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("generaPeriodoLibAnticipa INI ");

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi[mInd].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi[mInd].mDataFine);
		lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPeriodoLib.setDataInserimento(mOggi);
		lPeriodoLib.setFlagConcesso(aFlagConcesso);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("generaPeriodoLibAnticipa FINE ");

		return lPeriodoLib;

	} // chiude generaPeriodoLibAnticipa(..)

	// >>>>>>>> L.A SPECIALE Preparazione di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate_spe() throws F3BException {

		PeriodoClass[] lPeriodi_spe = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("leggiDate_spe");

		lDateInizio = getRequestDateParameters(ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE,
				ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE,
				ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE);
		lDateFine = getRequestDateParameters(ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE,
				ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE,
				ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--> CLOD - Numero date spe " + lNumDate);

		lPeriodi_spe = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi_spe[i] = new PeriodoClass();
			lPeriodi_spe[i].mDataIni = lDateInizio[i];
			lPeriodi_spe[i].mDataFine = lDateFine[i];
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> i = "+i+" - lPeriodi_spe[i].mDataIni = " +
			// lPeriodi_spe[i].mDataIni);
		}
		return lPeriodi_spe;
	}

	private void setPeriodiInLicenze_spe(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd_spe; // Salvataggio del valore corrente di mInd

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" setPeriodiInLicenze_spe - inizio  lInd = " + lInd + " - mInd_spe = " + mInd_spe);

		for (int j = 0, k = mInd_spe; j < 10; j++, k++) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" -----------------> ciclo for - mPeriodi_spe[k] = "+ mPeriodi_spe[k]
			// + " - k = "+ k +" - j = "+j );
			if ((mPeriodi_spe[k].mDataIni != null) && (mPeriodi_spe[k].mDataFine != null)) {
				num++;
			}
		}

		// I Periodi per la Licenza sono in numero di num
		if (num > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("---------------------> Num = "+num );
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);
		}

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd
		for (int j = 0; j < num; mInd_spe++) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" -----------------> SECONDO ciclo for - mPeriodi_spe[mInd_spe] = "+
			// mPeriodi_spe[mInd_spe] + " - mInd_spe = "+ mInd_spe +" - j = "+j );
			if ((mPeriodi_spe[mInd_spe].mDataIni != null) && (mPeriodi_spe[mInd_spe].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa_spe(
						aLicenza.getLicenza().getFlagConcesso());
			}
		}
		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.
		mInd_spe = lInd + 10;
	} // chiude setPeriodiInLicenze_spe

	private LicenzaLibAnticipataModel generaLicenza_spe(String aFlagConcesso, FascicoloGPModel mFasGPMod)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" generaLicenza_spe ini - flag Concesso = " + aFlagConcesso);
		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza("LA");
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (aFlagConcesso.compareTo("C") == 0 || aFlagConcesso.compareTo("S") == 0)
			lLicenza.setNumeroGiorni(new BigDecimal(75));

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante -_spe-");

		// Si inseriscono i dati che servono alla Procura !?! Luigi 30-6-2005
		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
		// L.A. SPECIALE
		lLicenza.setDescrStatoPermesso("LS");

		return lLicenza;
	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa_spe(String aFlagConcesso) throws F3BException {

		// Valorizza un Periodo leggendo le date all'indice mInd nell'Array mPeriodi
		PeriodoLibAnticipataModel lPeriodoLib = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("generaPeriodoLibAnticipa_spe ini ");

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi_spe[mInd_spe].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi_spe[mInd_spe].mDataFine);
		lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPeriodoLib.setDataInserimento(mOggi);
		lPeriodoLib.setFlagConcesso(aFlagConcesso);

		return lPeriodoLib;
	}

	// >>>>>>>> L.A INTEGRAZIONE Preparazione di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate_int() throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Leggi date int - inizio");

		PeriodoClass[] lPeriodi_int = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;

		lDateInizio = getRequestDateParameters(ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT,
				ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT,
				ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT);
		lDateFine = getRequestDateParameters(ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT,
				ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT,
				ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--> CLOD - Numero date int " + lNumDate);

		lPeriodi_int = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi_int[i] = new PeriodoClass();
			lPeriodi_int[i].mDataIni = lDateInizio[i];
			lPeriodi_int[i].mDataFine = lDateFine[i];
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> i = "+i+ " - lPeriodi_int[i].mDataIni = " +
			// lPeriodi_int[i].mDataIni);

		}

		return lPeriodi_int;
	}

	private void setPeriodiInLicenze_int(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd_int; // Salvataggio del valore corrente di mInd

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" setPeriodiInLicenze_int - inizio  lInd = " + lInd + " - mInd_int = " + mInd_int);

		for (int j = 0, k = mInd_int; j < 10; j++, k++)
			if ((mPeriodi_int[k].mDataIni != null) && (mPeriodi_int[k].mDataFine != null))
				num++;

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd
		for (int j = 0; j < num; mInd_int++) {
			if ((mPeriodi_int[mInd_int].mDataIni != null) && (mPeriodi_int[mInd_int].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa_int(
						aLicenza.getLicenza().getFlagConcesso());
			}
		}
		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.
		mInd_int = lInd + 10;
	} // chiude setPeriodiInLicenze_int

	private LicenzaLibAnticipataModel generaLicenza_int(String aFlagConcesso, FascicoloGPModel mFasGPMod)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" generaLicenza_int - inizio  -  ");
		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza("LA");
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (aFlagConcesso.compareTo("C") == 0 || aFlagConcesso.compareTo("S") == 0)
			lLicenza.setNumeroGiorni(new BigDecimal(30));

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante -_int-");

		// Si inseriscono i dati che servono alla Procura !?! Luigi 30-6-2005
		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
		// L.A. INTEGRAZIONE
		lLicenza.setDescrStatoPermesso("LI");

		return lLicenza;
	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa_int(String aFlagConcesso) throws F3BException {

		// Valorizza un Periodo leggendo le date all'indice mInd nell'Array mPeriodi
		PeriodoLibAnticipataModel lPeriodoLib = null;

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi_int[mInd_int].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi_int[mInd_int].mDataFine);
		lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPeriodoLib.setDataInserimento(mOggi);
		lPeriodoLib.setFlagConcesso(aFlagConcesso);

		return lPeriodoLib;
	}
	// END NUOVA ORDINANZA L.A. + L.A. SPECIALE + L.A. INTEGRAZIONE

	/**
	 * Redirige l'uscita al Dettaglio della Sentenza.
	 *
	 * @param aIdEvento
	 * @return
	 */
	private String dettaglioSentenza(BigDecimal aIdEvento) {

		String lRetPage = null;
		// dettaglio della sentenza
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.sius.depositosentenza.action.ActLoadDettaglioSentenza");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, aIdEvento.toString());
		lRetPage = lRedirectTo.toString();

		return lRetPage;
	}

	/**
	 * Redirige l'uscita al Dettaglio dell'Ordinanza.
	 *
	 * @param aIdEvento
	 * @return
	 */
	private String dettaglioOrdinanza(BigDecimal aIdEvento) {

		String lRetPage = null;
		// dettaglio dell'ordinanza
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, aIdEvento.toString());
		lRetPage = lRedirectTo.toString();

		return lRetPage;
	}

	/**
	 * Redirige l'uscita al Dettaglio del Decreto.
	 *
	 * @param aIdEvento
	 * @return
	 */
	private String dettaglioDecreto(BigDecimal aIdEvento) {

		String lRetPage = null;
		// dettaglio dell'ordinanza
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, aIdEvento.toString());
		lRetPage = lRedirectTo.toString();

		return lRetPage;
	}

	// 10102014 - DL 92 2014 Violazione CEDU
	private LicenzaPeriodiLibAnticipataModel[] AggiornaLicenzePeriodiCEDU(FascicoloGPModel mFasGPMod,
			int GiorniRiduzione, String SommaRisarcimento) throws F3BException {

		LicenzaPeriodiLibAnticipataModel[] lLicenze = null;

		int numCheck = 0;
		boolean lPeriodo = false;
		boolean lSomma = false;
		boolean lRigettati = false;
		boolean lInammissibili = false;
		boolean lNLP = false;
		//
		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU)) {
			// check per inserimento periodo
			numCheck++;
			lPeriodo = true;
		}
		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU)) {
			// chek per inserimento risarcimento in E.
			numCheck++;
			lSomma = true;
		}
		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU)) {
			numCheck++;
			lRigettati = true;
		}
		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU)) {
			numCheck++;
			lInammissibili = true;
		}
		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU)) {
			numCheck++;
			lNLP = true;
		}

		String flagConcesso = "";
		String TipoLic = "";

		if (numCheck > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("numCheck > 0");
			lLicenze = new LicenzaPeriodiLibAnticipataModel[numCheck];
			int i = 0;
			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi = leggiDateCEDU();

			if (lPeriodo) {
				flagConcesso = "C";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod));
				if (GiorniRiduzione > 0)
					lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(GiorniRiduzione));
				setPeriodiInLicenzeCEDU(lLicenze[i], 0);
				i++;
			}

			if (lSomma) {
				flagConcesso = "C";
				TipoLic = "SL";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod));
				if (!SommaRisarcimento.equals(""))
					lLicenze[i].getLicenza().setSommaRisarcDanni(new BigDecimal(SommaRisarcimento));
				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenzeCEDU(lLicenze[i], 10);
				i++;
			}

			if (lRigettati) {
				flagConcesso = "R";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod));
				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenzeCEDU(lLicenze[i], 20);
				i++;
			}
			if (lInammissibili) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" -------> Inammissibili");
				flagConcesso = "I";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod));
				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenzeCEDU(lLicenze[i], 30);
				i++;
			}
			if (lNLP) {
				flagConcesso = "N";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod));
				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenzeCEDU(lLicenze[i], 40);
				i++;
			}
		} // Chiude if numCheck > 0

		return lLicenze;
	} // Chiude AggiornaLicenzePeriodiCEDU()

	private LicenzaLibAnticipataModel generaLicenzaCEDU(String aFlagConcesso, String aTipoLicenza,
			FascicoloGPModel mFasGPMod) throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(getClass().getName() + ".generaLicenza ini ");
		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza(aTipoLicenza);
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (mFasGPMod.getFascicoloSiusModel() != null
				&& mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante ");

		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
		lLicenza.setDataEmissioneOrdinanza(mDataEmissione);

		return lLicenza;
	}

	private void setPeriodiInLicenzeCEDU(LicenzaPeriodiLibAnticipataModel aLicenza, int mInd)
			throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		// int lInd = mInd; // Salvataggio del valore corrente di mInd

		for (int j = 0, k = mInd; j < 10; j++, k++)
			if ((mPeriodi[k].mDataIni != null) && (mPeriodi[k].mDataFine != null))
				num++;

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd
		for (int j = 0; j < num; mInd++) {
			if ((mPeriodi[mInd].mDataIni != null) && (mPeriodi[mInd].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipataCEDU(
						aLicenza.getLicenza().getFlagConcesso(), mInd);
			}
		}
		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.
		// mInd = lInd + 10;
	} // chiude setPeriodiInLicenze

	private PeriodoClass[] leggiDateCEDU() throws F3BException {

		PeriodoClass[] lPeriodi = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;

		lDateInizio = getRequestDateParameters(ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO,
				ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO,
				ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO);
		lDateFine = getRequestDateParameters(ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE,
				ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE,
				ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
		lPeriodi = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi[i] = new PeriodoClass();
			lPeriodi[i].mDataIni = lDateInizio[i];
			lPeriodi[i].mDataFine = lDateFine[i];
		}

		return lPeriodi;
	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipataCEDU(String aFlagConcesso, int mInd)
			throws F3BException {

		// Valorizza un Periodo leggendo le date all'indice mInd nell'Array mPeriodi
		PeriodoLibAnticipataModel lPeriodoLib = null;

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi[mInd].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi[mInd].mDataFine);
		lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPeriodoLib.setDataInserimento(mOggi);
		lPeriodoLib.setFlagConcesso(aFlagConcesso);

		return lPeriodoLib;
	}

} // Chiude classe ActModificaProvvedimento()
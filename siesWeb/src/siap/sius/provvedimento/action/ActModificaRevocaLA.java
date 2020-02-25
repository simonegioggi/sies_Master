package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Action che realizza la modifica del Provvedimento SIUS di REVOCA L.A.: Ordinanza o Decreto. Gli unici dati
 * coinvolti in un'operazione di modifica sono: gli esiti degli oggetti, la data di emissione e la data
 * decorrenza della misura di sicurezza
 * 
 * @author Lesposito
 *
 */
public class ActModificaRevocaLA extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Inizializzazione data odierna
	private Date mOggi = DateUtils.getSysDate();

	// Data emissione
	private Date mDataEmissione;
	// Data decorrenza della Misura di Sicurezza
//	private Date mDataDecorrenza;

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
	private String mTipoConcessione_int; /* modalità di scelta dei periodi concessi. (S/C) L.A. INTEGRAZIONE */

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ----------------------- > - ActModificaRevocaLA INIZIO ");
		String lRetPage = IWebConstants.PG_MESSAGE;
		BigDecimal lIdOrdinanza = null;
		BigDecimal lIdDecreto = null;

		// Lettura data di emissione
		mDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" -----> C - ActModificaRevocaLA - mDataEmissione = " + mDataEmissione);
		// Lettura data decorrenza
//		if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA)
//				&& getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA).equals("S")) {
//			mDataDecorrenza = getRequestDateParameter(
//					ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA,
//					ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA,
//					ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA);
//		} else {
//			mDataDecorrenza = null;
//		}

		// Lettura ID Ordinanza o Decreto
		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC)
						.trim().length() > 0)
			lIdOrdinanza = getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC);
		else if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO)
				&& getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO).trim()
						.length() > 0)
			lIdDecreto = getRequestBigDecimalParameter(ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO);
		else
			throw new F3BException(F3BException.USER_MESSAGE, "ID Provvedimento non valorizzato nella form !");
		//
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" -----> C - ActModificaRevocaLA - lIdOrdinanza = " + lIdOrdinanza);
		//
		// Lettura ID Evento
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoModel lEvento = null;
		TenoreModel[] lTenori = null;
//		MisuraSicurezzaModel lMisuraSicurezza = null;

		lEvento = generaEvento();
		lTenori = generaTenori();

		// TODO carmela verificare ************
		// Modifica del 20/09/2013 mev "Revisione Misure di Sicurezza"
		// Aggiorna Data Decorrenza della Misura di Sicurezza
		// legata al Fascicolo SIUS
//		IMisuraSicurezza lCtrlMisSic = SIEPLookupRemote.getMisuraSicurezzaRemote();

		FascicoloGPModel mFasGPMod = null;

		// Si preleva dalla sessione il fascicolo GPModel.
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		mFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// Aggiornamento dei dati
		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		lCtrl.ExAggiornaTenoriEvento(lTenori, lEvento, lIdOrdinanza, lIdDecreto);

		// Aggiornamento periodi di libertà anticipata
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ------> MODIFICA - REVOCA - prima di IF  = "
				+ getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA));
		if (lIdOrdinanza != null || lIdDecreto != null) {
			if ((!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA))
					&& (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)
							.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA) == 0)) {
				// Modifica su DEPOSITO_ORDINANZA_PC (Il numero giorno di libertà anticipata)

				// DepositoOrdinanzaPcModel ModOrdinanza = null;
				// DepositoDecretoModel ModDecreto = null;
				DepositoOrdinanzaPcModel ModOrdinanza = new DepositoOrdinanzaPcModel();
				DepositoDecretoModel ModDecreto = new DepositoDecretoModel();

				IDepositoDecreto lCtrlD = SIUSLookupRemote.getDepositoDecretoRemote();

				if (lIdOrdinanza != null) {
					ModOrdinanza = lCtrl.ExRicercaDepositoOrdinanzaPcByKey(lIdOrdinanza);
				} else if (lIdDecreto != null) {
					ModDecreto = lCtrlD.ExRicercaDepositoDecretoByKey(lIdDecreto);
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ------> MODIFICA - REVOCA ?? = "
						+ getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA));
				// Nuova Ordinanza L.A. - Decreto 2013/146

				int SommatotLA = 0; // somma gg di Ordinanza L.A. ORDINARIA
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA)) {
					if (getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA) != null
							&& getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue() > 0) {
						if (getRequestBigDecimalParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue() > 0) {
							SommatotLA = getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue();
						}
					} else if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA) != null) {
						if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA)
								.intValue() > 0) {
							SommatotLA = getRequestBigDecimalParameter(
									ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA).intValue();
						}
					}

				}

				int SommatotLS = 0; // somma gg di Ordinanza L.A. SPECIALE
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE)) {
					if (getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE) != null
							&& getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE)
									.intValue() > 0) {
						if (getRequestBigDecimalParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE).intValue() > 0) {
							SommatotLS = getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE)
									.intValue();
						}
					} else if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE) != null) {
						if (getRequestBigDecimalParameter(
								ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE).intValue() > 0) {
							SommatotLS = getRequestBigDecimalParameter(
									ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE).intValue();
						}
					}
				}

				int SommatotLI = 0; // somma gg di Ordinanza L.A. INTEGRAZIONE
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT)) {
					if (getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT) != null
							&& getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT)
									.intValue() > 0) {
						if (getRequestBigDecimalParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT).intValue() > 0) {
							SommatotLI = getRequestBigDecimalParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT)
									.intValue();
						}
					} else if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT) != null) {
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

				if (lIdOrdinanza != null) {
					ModOrdinanza.setNumGiorniLibanticipata(new BigDecimal(lNumGiorniTotLA));
					lCtrl.ExModificaDepositoOrdinanzaPc(ModOrdinanza);
				} else if (lIdDecreto != null) {
					ModDecreto.setNumeroGiorniRevocaLA(new BigDecimal(lNumGiorniTotLA));
					lCtrlD.ExModificaDepositoDecreto(ModDecreto);
				}

				// Cancellazione da LICENZA_LIBANTICIPATA (cascata PERIODO_LIBANTICIPATA) a partire
				// dall'evento

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl.ExCancellaLicenzeLibanticipataByEve(lIdEvento);

				// ----------

				// Inserimento dei nuovi record di licenza libanticipata
				// lLicenze = generaLicenzaPeriodiLibAnticipata(ModOrdinanza.getIdEventoGenerato());
				// Nuova Ordinanza L.A. - Decreto 2013/146

				// Ordinanza L.A. ordinaria (LA)

				LicenzaPeriodiLibAnticipataModel[] lLicenze = null;
				LicenzaLibAnticipataModel lLicenzaC = null;

				if (!isRequestParameterNullObj(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE)) {
					mTipoConcessione = this
							.getRequestStringParameter(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE);

					// lLicenze = generaLicenzaPeriodiLibAnticipataLA(ModOrdinanza.getIdEventoGenerato(),
					// mFasGPMod, SommatotLA);
					lLicenze = generaLicenzaPeriodiLibAnticipataLA(mFasGPMod, SommatotLA);

					if (lLicenze != null) {
						EventoModel lEventoLicenze = null;
						IEvento lctrlEv = SICOLookupRemote.getEventoRemote();
						lEventoLicenze = lctrlEv.ExRicercaEventoByKey(lIdEvento);

						for (int i = 0; i < lLicenze.length; i++) {
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug(" MODIFICA - Ord, LA -  ciclo for - lLicenze[i] = "+lLicenze[i]);

							lLicenze[i].getLicenza().setEveIdEvento(lEventoLicenze.getIdEvento());
							lLicenze[i].getLicenza().setFasSieIdFascicoloSiep(
									lEventoLicenze.getFasSieIdFascicoloSiep());
							lLicenze[i].getLicenza().setDataEmissioneOrdinanza(
									lEventoLicenze.getDataEmissione());
							lLicenze[i].getLicenza().setCodLuogoEmittente(
									lEventoLicenze.getCodLuogoEmittente());
							lLicenze[i].getLicenza().setCodUfficioEmittente(
									lEventoLicenze.getCodUfficioEmittente());
							// Se necessario aggiungere anche info su aggiornamento (operatore, ufficio, data)
						}

					}

					String flagConcesso = "";
					if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO)) {
						// Controlla e imposta il flag
						if (getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO)
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

						if (flagConcesso.compareTo("") != 0)
							lLicenzaC.setFlagConcesso(flagConcesso);

						if (SommatotLA > 0)
							lLicenzaC.setNumeroGiorni(new BigDecimal(SommatotLA));

						if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
							lLicenzaC.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel()
									.getIdFascicoloSius());

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
					mTipoConcessione_spe = this
							.getRequestStringParameter(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE);

					// lLicenze_spe =
					// generaLicenzaPeriodiLibAnticipataLA_SPE(ModOrdinanza.getIdEventoGenerato(), mFasGPMod,
					// SommatotLS);
					lLicenze_spe = generaLicenzaPeriodiLibAnticipataLA_SPE(mFasGPMod, SommatotLS);
					if (lLicenze_spe != null) {
						EventoModel lEventoLicenze = null;
						IEvento lctrlEv = SICOLookupRemote.getEventoRemote();
						lEventoLicenze = lctrlEv.ExRicercaEventoByKey(lIdEvento);

						for (int i = 0; i < lLicenze_spe.length; i++) {

							lLicenze_spe[i].getLicenza().setEveIdEvento(lEventoLicenze.getIdEvento());
							lLicenze_spe[i].getLicenza().setFasSieIdFascicoloSiep(
									lEventoLicenze.getFasSieIdFascicoloSiep());
							lLicenze_spe[i].getLicenza().setDataEmissioneOrdinanza(
									lEventoLicenze.getDataEmissione());
							lLicenze_spe[i].getLicenza().setCodLuogoEmittente(
									lEventoLicenze.getCodLuogoEmittente());
							lLicenze_spe[i].getLicenza().setCodUfficioEmittente(
									lEventoLicenze.getCodUfficioEmittente());
							// Se necessario aggiungere anche info su aggiornamento (operatore, ufficio, data)
						}

					}

					String flagConcesso_spe = "";

					if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO)) {
						// Controlla e imposta il flag
						if (getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO)
								.compareTo("S") == 0)
							flagConcesso_spe = "S";

					}

					if (mTipoConcessione_spe.compareTo("C") == 0
							&& !isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE)
							&& SommatotLS > 0) {
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("--> Periodo Unico senza check periodo, cioè con solo numero gg senza date dal .. al .. ");
						lLicenzaC_SPE = new LicenzaLibAnticipataModel();

						lLicenzaC_SPE.setCodTipoLicenza("LA");
						lLicenzaC_SPE.setCodOperatoreInserimento(getCodUtenteConnesso());
						lLicenzaC_SPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						lLicenzaC_SPE.setDataInserimento(mOggi);

						if (flagConcesso_spe.compareTo("") != 0)
							lLicenzaC_SPE.setFlagConcesso(flagConcesso_spe);

						if (SommatotLS > 0)
							lLicenzaC_SPE.setNumeroGiorni(new BigDecimal(SommatotLS));

						if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
							lLicenzaC_SPE.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel()
									.getIdFascicoloSius());

						lLicenzaC_SPE.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr()
								.toString());
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
					mTipoConcessione_int = this
							.getRequestStringParameter(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT);

					lLicenze_int = generaLicenzaPeriodiLibAnticipataLA_INT(mFasGPMod, SommatotLI);
					if (lLicenze_int != null) {
						EventoModel lEventoLicenze = null;
						IEvento lctrlEv = SICOLookupRemote.getEventoRemote();
						lEventoLicenze = lctrlEv.ExRicercaEventoByKey(lIdEvento);

						for (int i = 0; i < lLicenze_int.length; i++) {
							lLicenze_int[i].getLicenza().setEveIdEvento(lEventoLicenze.getIdEvento());
							lLicenze_int[i].getLicenza().setFasSieIdFascicoloSiep(
									lEventoLicenze.getFasSieIdFascicoloSiep());
							lLicenze_int[i].getLicenza().setDataEmissioneOrdinanza(
									lEventoLicenze.getDataEmissione());
							lLicenze_int[i].getLicenza().setCodLuogoEmittente(
									lEventoLicenze.getCodLuogoEmittente());
							lLicenze_int[i].getLicenza().setCodUfficioEmittente(
									lEventoLicenze.getCodUfficioEmittente());
							// Se necessario aggiungere anche info su aggiornamento (operatore, ufficio, data)
						}
					}

					String flagConcesso_int = "";

					if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO)) {
						// Controlla e imposta il flag
						if (getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO)
								.compareTo("S") == 0)
							flagConcesso_int = "S";

					}

					if (mTipoConcessione_int.compareTo("C") == 0
							&& !isRequestChecked(ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT)
							&& SommatotLI > 0) {
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("--> Periodo Unico senza check periodo, cioè con solo numero gg senza date dal .. al .. ");
						lLicenzaC_INT = new LicenzaLibAnticipataModel();

						lLicenzaC_INT.setCodTipoLicenza("LA");
						lLicenzaC_INT.setCodOperatoreInserimento(getCodUtenteConnesso());
						lLicenzaC_INT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						lLicenzaC_INT.setDataInserimento(mOggi);

						if (flagConcesso_int.compareTo("") != 0)
							lLicenzaC_INT.setFlagConcesso(flagConcesso_int);

						if (SommatotLI > 0)
							lLicenzaC_INT.setNumeroGiorni(new BigDecimal(SommatotLI));

						if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
							lLicenzaC_INT.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel()
									.getIdFascicoloSius());

						lLicenzaC_INT.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr()
								.toString());
						lLicenzaC_INT.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
						// L.A. INTEGRAZIONE
						lLicenzaC_INT.setDescrStatoPermesso("LIU");
						lLicenzaC_INT.setFlagScorta(mTipoConcessione_int);
					}

				} // chiude
					// if(!isRequestParameterNullObj(ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT)
					// )

				// INSERIMENTO ORDINANZA MODIFICATA

				LicenzaPeriodiLibAntCtrl.ExInserisciNewLicenzeLibanticipata(lLicenze, lLicenze_spe,
						lLicenze_int, lLicenzaC, lLicenzaC_SPE, lLicenzaC_INT, lIdEvento);

			} // chiude if
				// ((!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)) &&

		} // chiude if (lIdOrdinanza != null)

		// - - - - > Fine aggiornamento periodi libertà anticipata

		// Redirezione alla pagina di dettaglio Ordinanza o Decreto
		if (lIdOrdinanza != null)
			lRetPage = dettaglioOrdinanza(lIdEvento);
		else
			lRetPage = dettaglioDecreto(lIdEvento);

		return lRetPage;
	}

	/**
	 * Prepara il Model per la Misura di Sicurezza da aggiornare.
	 * <p>
	 * 
	 * @param lMisSicuSius
	 *            Misura di Sicurezza da aggiornare.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return MisuraSicurezzaModel ritorna la Misura di Sicurezza model.
	 */
	// private MisuraSicurezzaModel generaMisuraSicurezza(MisuraSicurezzaModel lMisSicuSius) throws
	// F3BException {
	// lMisSicuSius.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
	// lMisSicuSius.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
	// lMisSicuSius.setDataAggiornamento(DateUtils.getSysDate());
	// lMisSicuSius.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
	// lMisSicuSius.setDataDecorrenza(mDataDecorrenza);
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Misura di sicurezza = " + lMisSicuSius);
	//
	// return lMisSicuSius;
	// }

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
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" - ActModificaRevocaLA - generaEvento");
		EventoModel lEvento = new EventoModel();
		lEvento.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lEvento.setDataEmissione(mDataEmissione);
		lEvento.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lEvento.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEvento.setDataAggiornamento(mOggi);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-----> C Evento = " + lEvento);
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
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" - ActModificaRevocaLA - generaTenori");
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
				lTenModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																						// dell'operatore che
																						// aggiorna
				lTenModel.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore che
																				// aggiorna
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
				siesLogger.debug("------> C - Tenore n." + i + " = " + lTenori[i]);

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
	private LicenzaPeriodiLibAnticipataModel[] generaLicenzaPeriodiLibAnticipataLA(
			FascicoloGPModel mFasGPMod, int SommatotLA) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("- ActModificaRevocaLA - generaLicenzaPeriodiLibAnticipataLA - L.A. NORMALE ");

		// -------------------------------------------------------------
		// >>>>>>>>>>>>>>>>> L.A. NORMALE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//
		LicenzaPeriodiLibAnticipataModel[] lLicenze = null;

		String[] lChecks = null;
//		String[] lDate = null;
//		PeriodoClass[] periodi = null;

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

		// Per Le REVOCHE in materia di LA se l'esito è 'Accoglie'
		// il FLAG_CONCESSO va impostato ad 'S' -- Michele

		String flagConcesso = "";

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO)
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
		siesLogger.debug(" ActModificaRevocaLA - generaLicenzaPeriodiLibAnticipataLA_SPE - L.A. SPECIALE ");
		//
		// >>>>>>>>>>>>>>>>> L.A. SPECIALE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//
		LicenzaPeriodiLibAnticipataModel[] lLicenze_spe = null;

		String[] lChecks_spe = null;
//		String[] lDate_spe = null;
//		PeriodoClass[] periodi_spe = null;

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

		String flagConcesso_spe = "";

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO).compareTo("S") == 0)
				flagConcesso_spe = "S";

		}

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A.SPECIALE e Periodi concessi.
		if (numCheck_spe > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" --> numCheck_spe > 0 =  SPE "+numCheck_spe);
			lLicenze_spe = new LicenzaPeriodiLibAnticipataModel[numCheck_spe];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi_spe = leggiDate_spe();

			if (lConcessi_spe) {

				while (i < numCheckConcessi_spe) {
					lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
					lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);
					setPeriodiInLicenze_spe(lLicenze_spe[i]);
					i++;
				}
			}
			if (lPeriodo_spe) {

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
		siesLogger
				.debug(" ActModificaRevocaLA - generaLicenzaPeriodiLibAnticipataLA_INT - L.A. INTEGRAZIONE");
		//
		LicenzaPeriodiLibAnticipataModel[] lLicenze_int = null;

		String[] lChecks_int = null;
//		String[] lDate_int = null;
//		PeriodoClass[] periodi_int = null;

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

		String flagConcesso_int = "";

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO).compareTo("S") == 0)
				flagConcesso_int = "S";

		}

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A.INTEGRAZIONE e Periodi concessi.
		if (numCheck_int > 0) {
			lLicenze_int = new LicenzaPeriodiLibAnticipataModel[numCheck_int];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi_int = leggiDate_int();

			if (lConcessi_int) {
				while (i < numCheckConcessi_int) {
					lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
					lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione_int);
					setPeriodiInLicenze_int(lLicenze_int[i]);
					i++;
				}
			}
			if (lPeriodo_int) {

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

	// ---------------------------------------------------------------------------------------------------

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
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataIni  = " + lPeriodi[i].mDataIni);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataFine = " + lPeriodi[i].mDataFine);

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
		// siesLogger.debug("---- >  setPeriodiInLicenze - inizio  lInd = "+lInd);

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
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa(aLicenza.getLicenza().getFlagConcesso());
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

		if (aFlagConcesso.compareTo("S") == 0)
			lLicenza.setNumeroGiorni(new BigDecimal(45));

		if (aFlagConcesso.compareTo("") != 0)
			lLicenza.setFlagConcesso(aFlagConcesso);

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

	// - - - End

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
			// siesLogger.debug("--> i = "+i+" -  lPeriodi_spe[i].mDataIni = " + lPeriodi_spe[i].mDataIni);
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
			// siesLogger.debug(" -----------------> ciclo for - mPeriodi_spe[k] = "+ mPeriodi_spe[k] +
			// " - k = "+ k +" - j = "+j );
			if ((mPeriodi_spe[k].mDataIni != null) && (mPeriodi_spe[k].mDataFine != null)) {
				num++;
			}
		}

		// I Periodi per la Licenza sono in numero di num
		if (num > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--------------------->  Num = "+num );
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
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa_spe(aLicenza.getLicenza()
						.getFlagConcesso());
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

		if (aFlagConcesso.compareTo("") != 0)
			lLicenza.setFlagConcesso(aFlagConcesso);

		if (aFlagConcesso.compareTo("S") == 0)
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

	// - - - End

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
			// siesLogger.debug("-->  i = "+i+ " - lPeriodi_int[i].mDataIni = " + lPeriodi_int[i].mDataIni);

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
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa_int(aLicenza.getLicenza()
						.getFlagConcesso());
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

		if (aFlagConcesso.compareTo("") != 0)
			lLicenza.setFlagConcesso(aFlagConcesso);

		if (aFlagConcesso.compareTo("S") == 0)
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

}
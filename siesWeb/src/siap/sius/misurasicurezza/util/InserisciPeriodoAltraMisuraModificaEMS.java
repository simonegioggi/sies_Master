package siap.sius.misurasicurezza.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: InserisciPeriodoAltraMisuraModificaEMS
 * </p>
 * <p>
 * Description: Classe di Util per il controllo dei dati e i calcoli della pena per il Periodo Altra Misura e
 * Esecuzione Misura Sicurezza
 * </p>
 * 
 * @version 1.0
 */
public class InserisciPeriodoAltraMisuraModificaEMS extends ActionSiap implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	EsecuzioneMisuraSicurezzaModel lEmsM = null;

	// Metodo che ritorna il model dell'Esecuzione Misura Sicurezza
	public EsecuzioneMisuraSicurezzaModel getEMS() {

		return lEmsM;
	}

	ScadenzarioSiusModel lScadenzarioSiusModSecond = null;

	ScadenzarioSiusModel lScadenzarioSiusModPrincipal = null;

	// Metodo che ritorna il model dello Scadenzario Principal
	public ScadenzarioSiusModel getScadenzarioPrincipal() {

		return lScadenzarioSiusModPrincipal;
	}

	/**
	 * Legge i dati inputati in maschera li carica nel model lPerAltMisMod, cerca l'Esecuzione Misura
	 * Sicurezza per effettuare le modifiche ed esegue i calcoli sul quantum pena.
	 * <p>
	 * 
	 * @param mFasGPMod
	 *            : Model del fascicolo corrente,
	 * @return lPerAltMisMod: Model dell Periodo Altra Misura.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	@SuppressWarnings("rawtypes")
	public PeriodoAltraMisuraModel caricaPeriodoAltraMisuraModEMS(FascicoloGPModel mFasGPMod)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("caricaPeriodoAltraMisuraModEMS: Inizio");

		PeriodoAltraMisuraModel lPerAltMisMod = null;

		// Controlla Data Decorrenza Sospensione per inserire il nuovo record nella tabella
		// PERIODO_ALTRA_MISURA
		// e modificare il record nella tabella ESECUZIONE_MISURA_SICUREZZA
		if ((!isRequestParameterNullObj(CAMPO_GIORNO_SOSPENSIONE_SS) && getRequestStringParameter(
				CAMPO_GIORNO_SOSPENSIONE_SS).length() > 0)
				&& (!isRequestParameterNullObj(CAMPO_MESE_SOSPENSIONE_SS) && getRequestStringParameter(
						CAMPO_MESE_SOSPENSIONE_SS).length() > 0)
				&& (!isRequestParameterNullObj(CAMPO_ANNO_SOSPENSIONE_SS) && getRequestStringParameter(
						CAMPO_ANNO_SOSPENSIONE_SS).length() > 0)) {
//			BigDecimal lGiorniRecupero = null;

//			Date lDataScadenza = null;

			Date lDataTermineIniziale = null;

			lPerAltMisMod = new PeriodoAltraMisuraModel();

			Date lDataDecorrenzaSospensioneMisuraSicurezza = getRequestDateParameter(
					CAMPO_ANNO_SOSPENSIONE_SS, CAMPO_MESE_SOSPENSIONE_SS, CAMPO_GIORNO_SOSPENSIONE_SS);
			lPerAltMisMod.setDataInizioEsecuzione(lDataDecorrenzaSospensioneMisuraSicurezza);

			// #### Periodo Sospensione #####

			BigDecimal lPerAltMisSospAA = new BigDecimal(0);
			BigDecimal lPerAltMisSospMM = new BigDecimal(0);
			BigDecimal lPerAltMisSospGG = new BigDecimal(0);
			// ------ Anni Periodo Durata Minima alla Ripresa ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_AA_SS)
					&& getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_AA_SS) != null) {
				lPerAltMisSospAA = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_AA_SS);
				// lPerAltMisMod.setSospensioneAA(lPerAltMisSospAA);
				// lPerAltMisMod.setResiduaAA(lPerAltMisSospAA);
			}
			lPerAltMisMod.setResiduaAA(lPerAltMisSospAA);
			// ------ Mesi Periodo Durata Minima alla Ripresa ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_MM_SS)
					&& getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_MM_SS) != null) {
				lPerAltMisSospMM = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_MM_SS);
				// lPerAltMisMod.setSospensioneMM(lPerAltMisSospMM);
				// lPerAltMisMod.setResiduaMM(lPerAltMisSospMM);
			}
			lPerAltMisMod.setResiduaMM(lPerAltMisSospMM);
			// ------ Giorni Periodo Durata Minima alla Ripresa ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_GG_SS)
					&& getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_GG_SS) != null) {
				lPerAltMisSospGG = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_GG_SS);
				// lPerAltMisMod.setSospensioneGG(lPerAltMisSospGG);
				// lPerAltMisMod.setResiduaGG(lPerAltMisSospGG);
			}
			lPerAltMisMod.setResiduaGG(lPerAltMisSospGG);

			// Flag Motivo "03" SOSPENSIONE
			lPerAltMisMod.setFlagMotivo("03");

			// Motivazione
			if (!isRequestParameterNullObj(CAMPO_NOTE)) {
				String lNote = getRequestStringParameter(CAMPO_NOTE);
				lPerAltMisMod.setMotivazione(lNote);
			}

			// Operatore, Ufficio e Data Inserimento
			lPerAltMisMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPerAltMisMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPerAltMisMod.setDataInserimento(DateUtils.getSysDate());

			// Cerca ID Fascicolo Padre
			IFascicoloSius lCtrlFS = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel IFascicoloSiusMod = lCtrlFS.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
					mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1(), mFasGPMod
							.getGeneraleProcedimentoModel().getProgrS1(), getCodUfficioUtenteConnesso());

			BigDecimal aIdFascicoloSius = IFascicoloSiusMod.getFascicoloSiusModel().getIdFascicoloSius();

			lPerAltMisMod.setFasSiuIdFascicoloSius(aIdFascicoloSius);
			// ricerca delle precedenti comunicazioni di esecuzione misura sicurezza
			// per il fascicolo
			IPeriodoAltraMisura lCtrl1 = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
			List lListaMisureSius = lCtrl1.ExRicercaMisuraSicurezzaByIdFascicolo(aIdFascicoloSius);

			if (lListaMisureSius != null && !lListaMisureSius.isEmpty()) {
				PeriodoAltraMisuraModel lPeriodoAltraMisuraModel = (PeriodoAltraMisuraModel) lListaMisureSius
						.get(lListaMisureSius.size() - 1);
				if (lPeriodoAltraMisuraModel.getDataScadenza() == null) {
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Inserire prima Data Scadenza di Inizio o Ripresa Misura.");
				}
				if (lPeriodoAltraMisuraModel.getFlagMotivo().equals("03")) {
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! La misura risulta già sospesa!");
				}
				if (lDataDecorrenzaSospensioneMisuraSicurezza.before(lPeriodoAltraMisuraModel
						.getDataInizioEsecuzione())) {
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! La Data Decorrenza non puo essere minore della data Inizio/Ripresa Misura!");
				}
			} else {
				throw new SIUSException(SIUSException.USER_MESSAGE, "Inserire prima Inizio Misura.");
			}
			// Ricerca Esecuzione Misura Sicurezza
			IEsecuzioneMS lCtrlEMS = SIUSLookupRemote.getEsecuzioneMSRemote();
			lEmsM = lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(aIdFascicoloSius);

			// Controlla esistenza del record Esecuzione Misura Sicurezza e della Data Termine Attuale
			if (lEmsM != null && lEmsM.getDataTermineAttuale() != null) {
				// Controlla che la Data Decorrenza Sospensione sia MINORE della DATA_TERMINE_ATTUALE
				// dell'Esecuzione Misura Sicurezza
				if (DateUtils.isLower(lDataDecorrenzaSospensioneMisuraSicurezza,
						lEmsM.getDataTermineAttuale())) {
					lDataTermineIniziale = lEmsM.getDataTermineAttuale();

					Date DataInizio = null;
					Date DataFine = null;
					CalendarUtil lCalUtil = new CalendarUtil();

					// ---------------------------------------------
					// Calcolo della "Misura Sicurezza Espiata"
					// ---------------------------------------------
					DataInizio = lEmsM.getDataInizioMisura();

//					if (lDataScadenza != null)
//						DataFine = lDataScadenza;
//					else
					DataFine = lDataDecorrenzaSospensioneMisuraSicurezza;

					CalendarModel lCalModEspiata = new CalendarModel();
					lCalModEspiata.setDataInizio(DataInizio);
					lCalModEspiata.setDataFine(DataFine);

					// Calcola il quantum
					lCalModEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalModEspiata, true);

					// Ritorna il quantum in giorni
					int lGiorniEspiati = CalendarUtil.getTotGiorni(lCalModEspiata);
//					if (lGiorniRecupero != null) {
//						// Toglie eventuali giorni recupero dal totale pena espiata
//						lGiorniEspiati = lGiorniEspiati - lGiorniRecupero.intValue();
//					}
					lCalModEspiata = new CalendarModel();
					lCalModEspiata.setNumGiorni(lGiorniEspiati);

					// vengono normalizzati i quantum
					lCalModEspiata = lCalUtil.ricalcolaGAM(lCalModEspiata);

					// Scrive i campi in Periodo Altra Misura da Espiare
					lPerAltMisMod.setEspiataGG(new BigDecimal(lCalModEspiata.getNumGiorni()));
					lPerAltMisMod.setEspiataMM(new BigDecimal(lCalModEspiata.getNumMesi()));
					lPerAltMisMod.setEspiataAA(new BigDecimal(lCalModEspiata.getNumAnni()));

					// ---------------------------------------------
					// Calcolo della "Misura Sicurezza Residua"
					// ---------------------------------------------

					// Calcola la Misura Totale
					DataInizio = lEmsM.getDataInizioMisura();
					DataFine = lDataTermineIniziale;

					CalendarModel lCalModIniziale = new CalendarModel();
					lCalModIniziale.setDataInizio(DataInizio);
					lCalModIniziale.setDataFine(DataFine);

					lCalModIniziale = lCalUtil.CalcolaNumGiorniMesiAnni(lCalModIniziale, true);

//					CalendarModel lCalModRes = new CalendarModel();
					// Calcola La Sanzione Sostitutiva Residua
					// Sottraendo dalla Totale L'Espiata
					/*lCalModRes = */lCalUtil.sottraiGiorni(lCalModIniziale, lCalModEspiata);

					// Scrive i campi in Periodo Altra Misura Residua - già calcolati!
					// lPerAltMisMod.setResiduaGG(new BigDecimal(lCalModRes.getNumGiorni()));
					// lPerAltMisMod.setResiduaMM(new BigDecimal(lCalModRes.getNumMesi()));
					// lPerAltMisMod.setResiduaAA(new BigDecimal(lCalModRes.getNumAnni()));

				} else {
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! La Data Decorrenza Sospensione deve essere minore della Data Termine Attuale.");
				}
			} else {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! Data Termine Attuale non trovata.");
			}

			// Codice Tipo Ufficio Sosp
			lPerAltMisMod.setCodTipoUfficioSosp("-");
			// Codice Tipo Autorità
			lPerAltMisMod.setCodTipoAutorita("-");
			// Codice Luogo Autorità
			lPerAltMisMod.setCodLuogoAutorita("-");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("caricaPeriodoAltraMisuraModEMS: Fine");

		}

		return lPerAltMisMod;
	}

	/**
	 * Effetua una ricerca del Periodo Altra Misura, dell'Esecuzione Misura Sicurezza e Scrive lo Scadenzario.
	 * <p>
	 * 
	 * @param mFasGPMod
	 *            : Model del fascicolo corrente,
	 * @param lIdEveDecOrd
	 *            : BigDecimal Id Evento Decreto/Ordinanza,
	 * @return lScadenzarioSiusMod: Model dello scadenzario SIUS.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ScadenzarioSiusModel caricaScadenzarioSiusPerAltMisModEMS(FascicoloGPModel mFasGPMod,
			BigDecimal lIdEveDecOrd, String lCodTipoScadenzarioPrincipal, String lCodTipoScadenzarioSecond)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("caricaScadenzarioSiusPerAltMisModEMS: Inizio");

		Date lDatacorrente = DateUtils.getSysDate();

		// Cerca ID Fascicolo Padre
		IFascicoloSius lCtrlFS = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel IFascicoloSiusMod = lCtrlFS.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
				mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1(), mFasGPMod
						.getGeneraleProcedimentoModel().getProgrS1(), getCodUfficioUtenteConnesso());

		BigDecimal aIdFascicoloSius = IFascicoloSiusMod.getFascicoloSiusModel().getIdFascicoloSius();

		// Ricerca Esecuzione Misura Sicurezza per Id Fascicolo Padre
		IEsecuzioneMS lCtrlEMS = SIUSLookupRemote.getEsecuzioneMSRemote();
		lEmsM = lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(aIdFascicoloSius);

		// Ricerca Periodo Altra Misura per Id Evento Decreto/Ordinanza
		IPeriodoAltraMisura lCtrl1 = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		PeriodoAltraMisuraModel IPAMMod = lCtrl1.ExRicercaMisuraSicurezzaByIdEvento(lIdEveDecOrd);

		// Ricerca Scadenzario Principale
		IScadenzarioSius lCtrlSca = SIUSLookupRemote.getScadenzarioRemote();
		lScadenzarioSiusModPrincipal = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(aIdFascicoloSius,
				lCodTipoScadenzarioPrincipal);

		// Controllo esitenza Scadenzario Principale
		if (lScadenzarioSiusModPrincipal != null) {

			// Controlla Esitenza Periodo Altra Misura
			if (IPAMMod != null && IPAMMod.getDataInizioEsecuzione() != null) {
				// Ricerca Scadenzario Secondario
				lScadenzarioSiusModSecond = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(
						aIdFascicoloSius, lCodTipoScadenzarioSecond);

				if (lScadenzarioSiusModSecond == null) {
					lScadenzarioSiusModSecond = new ScadenzarioSiusModel();
				}

				// Scrive l'Id dell'evento preso dallo scadenzario principale
				lScadenzarioSiusModSecond.setEveIdEvento(lScadenzarioSiusModPrincipal.getEveIdEvento());

				// Scrive la Data Inizio nello Scadenzario Secondario
				lScadenzarioSiusModSecond.setDataInizioScadenza(IPAMMod.getDataInizioEsecuzione());

				// Scrive la Data Fine dello Scadenzario Secondario
				if (IPAMMod.getDataScadenza() != null) {
					lScadenzarioSiusModSecond.setDataFineScadenza(IPAMMod.getDataScadenza());
				} else // Se non trova la Data Scadenza del Periodo Altra Misura inserisce la Data Termine
						// Attuale dell'Esec. Misura Sicurezza
				{
					lScadenzarioSiusModSecond.setDataFineScadenza(lEmsM.getDataTermineAttuale());
				}

				// Se lo Scadenzario Secondario esiste aggiorna i dati aggiornamento
				if (lScadenzarioSiusModSecond.getIdScadenzarioSius() != null) {
					// Scrive Codice Operatore, Codice Ufficio e Data aggiornamento
					lScadenzarioSiusModSecond.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					lScadenzarioSiusModSecond.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lScadenzarioSiusModSecond.setDataAggiornamento(lDatacorrente);
				} else //
				{
					// Scrive il Codice Tipo
					lScadenzarioSiusModSecond.setCodTipoScadenzario(lCodTipoScadenzarioSecond);

					// Scrive Codice Operatore, Codice Ufficio e Data Inserimento
					lScadenzarioSiusModSecond.setCodOperatoreInserimento(getCodUtenteConnesso());
					lScadenzarioSiusModSecond.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lScadenzarioSiusModSecond.setDataInserimento(lDatacorrente);

					// Scrive l'ID Fascicolo Sius Padre
					lScadenzarioSiusModSecond.setFasSiuIdFascicoloSius(aIdFascicoloSius);
				}

				// Controlla che ci siano giorni da recuperare
				if (IPAMMod.getDaRecuperare() != null && IPAMMod.getDaRecuperare().equals("1")) {
					// Modifica la Data Fine Scadenza dello Scadenzario Principale
					// con la Data Termine Attuale dell'Esecuzione Sanzione Sostituttiva
					lScadenzarioSiusModPrincipal.setDataFineScadenza(lEmsM.getDataTermineAttuale());

					// Scrive Codice Operatore, Codice Ufficio e Data aggiornamento
					lScadenzarioSiusModPrincipal.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					lScadenzarioSiusModPrincipal.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lScadenzarioSiusModPrincipal.setDataAggiornamento(lDatacorrente);
				}

			}// FINE -- Controlla Esitenza Periodo Altra Misura

		} else {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! Lo Scadenzario Principale non Esiste.");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("caricaScadenzarioSiusPerAltSanzModESS: Fine");

		return lScadenzarioSiusModSecond;
	}

}
package siap.sius.sanzionesostitutiva.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * InserisciPeriodoAltraSanzioneModificaESS - Classe di Util per il controllo dei dati e i calcoli della pena
 * per il Periodo Altra Sanzione e Esecuzione Sanzione Sostitutiva
 *
 * @version 1.0
 */
public class InserisciPeriodoAltraSanzioneModificaESS extends ActionSiap implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	EsecuzioneSanzioneSostitutivaModel lEssM = null;

	// Metodo che ritorna il model dell'Esecuzione Sanzione Sostitutiva
	public EsecuzioneSanzioneSostitutivaModel getESS() {

		return lEssM;
	}

	ScadenzarioSiusModel lScadenzarioSiusModSecond = null;

	ScadenzarioSiusModel lScadenzarioSiusModPrincipal = null;

	// Metodo che ritorna il model dello Scadenzario Principal
	public ScadenzarioSiusModel getScadenzarioPrincipal() {

		return lScadenzarioSiusModPrincipal;
	}

	/**
	 * Legge i dati inputati in maschera li carica nel model lPerAltSanMod, cerca l'Esecuzione Sanzione
	 * Sostitutiva per effettuare le modifiche ed esegue i calcoli sul quantum pena.
	 *
	 * @param mFasGPMod
	 *            : Model del fascicolo corrente,
	 * @return lPerAltSanMod: Model dell Periodo Altra Sanzione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	@SuppressWarnings("rawtypes")
	public PeriodoAltraSanzioneModel caricaPeriodoAltraSanzioneModESS(FascicoloGPModel mFasGPMod)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("caricaPeriodoAltraSanzioneModESS: Inizio");

		PeriodoAltraSanzioneModel lPerAltSanMod = null;

		// Controlla Data Decorrenza Sospensione per inserire il nuovo record nella tabella
		// PERIODO_ALTRA_SANZIONE
		// e modificare il record nella tabella ESECUZIONE_SANZIONE_SOST
		if ((!isRequestParameterNullObj(CAMPO_GIORNO_SOSPENSIONE_SS)
				&& getRequestStringParameter(CAMPO_GIORNO_SOSPENSIONE_SS).length() > 0)
				&& (!isRequestParameterNullObj(CAMPO_MESE_SOSPENSIONE_SS)
						&& getRequestStringParameter(CAMPO_MESE_SOSPENSIONE_SS).length() > 0)
				&& (!isRequestParameterNullObj(CAMPO_ANNO_SOSPENSIONE_SS)
						&& getRequestStringParameter(CAMPO_ANNO_SOSPENSIONE_SS).length() > 0)) {
			BigDecimal lGiorniRecupero = null;

			Date lDataScadenza = null;

			Date lDataTermineIniziale = null;

			lPerAltSanMod = new PeriodoAltraSanzioneModel();

			Date lDataDecorrenzaSanzSost = getRequestDateParameter(CAMPO_ANNO_SOSPENSIONE_SS,
					CAMPO_MESE_SOSPENSIONE_SS, CAMPO_GIORNO_SOSPENSIONE_SS);
			lPerAltSanMod.setDataInizioEsecuzione(lDataDecorrenzaSanzSost);

			// #### Periodo Sospensione #####

			BigDecimal lPerAltSanSospAA = new BigDecimal(0);
			BigDecimal lPerAltSanSospMM = new BigDecimal(0);
			BigDecimal lPerAltSanSospGG = new BigDecimal(0);
			// ------ Anni Periodo Sospensione ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_AA_SS)
					&& getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_AA_SS) != null) {
				lPerAltSanSospAA = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_AA_SS);
				lPerAltSanMod.setSospensioneAA(lPerAltSanSospAA);
			}
			// ------ Mesi Periodo Sospensione ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_MM_SS)
					&& getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_MM_SS) != null) {
				lPerAltSanSospMM = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_MM_SS);
				lPerAltSanMod.setSospensioneMM(lPerAltSanSospMM);
			}
			// ------ Giorni Periodo Sospensione ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_GG_SS)
					&& getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_GG_SS) != null) {
				lPerAltSanSospGG = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_GG_SS);
				lPerAltSanMod.setSospensioneGG(lPerAltSanSospGG);
			}

			// Flag Recupero SS
			if (!isRequestParameterNullObj(CAMPO_FLAG_RECUPERO_SS)) {
				String lFlagRecupero = getRequestStringParameter(CAMPO_FLAG_RECUPERO_SS);

				// Se il flag recupero è impostato su "da recuperare"(valore= "S") carico anche il campo
				// "Numero Giorni"
				if (lFlagRecupero.equals("S")) {
					lPerAltSanMod.setDaRecuperare("1");
					// Numero Giorni Recupero
					if (!isRequestParameterNullObj(CAMPO_GIORNI_RECUPERO_SS)
							&& getRequestBigDecimalParameter(CAMPO_GIORNI_RECUPERO_SS) != null) {
						lGiorniRecupero = getRequestBigDecimalParameter(CAMPO_GIORNI_RECUPERO_SS);
						// 14/05/2008 lPerAltSanMod.setDaRecuperareGG(lGiorniRecupero);
						lPerAltSanMod.setDaRecuperareGG(lGiorniRecupero);
					}
				} else if (lFlagRecupero.equals("N")) {
					lPerAltSanMod.setDaRecuperare("0");
				}
			}

			// Flag Motivo "03" SOSPENSIONE
			lPerAltSanMod.setFlagMotivo("03");

			// Motivazione
			if (!isRequestParameterNullObj(CAMPO_NOTE)) {
				String lNote = getRequestStringParameter(CAMPO_NOTE);
				lPerAltSanMod.setMotivazione(lNote);
			}

			// Operatore, Ufficio e Data Inserimento
			lPerAltSanMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPerAltSanMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPerAltSanMod.setDataInserimento(DateUtils.getSysDate());

			// Cerca ID Fascicolo Padre
			IFascicoloSius lCtrlFS = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel IFascicoloSiusMod = lCtrlFS.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
					mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1(),
					mFasGPMod.getGeneraleProcedimentoModel().getProgrS1(), getCodUfficioUtenteConnesso());

			BigDecimal aIdFascicoloSius = IFascicoloSiusMod.getFascicoloSiusModel().getIdFascicoloSius();

			lPerAltSanMod.setFasSiuIdFascicoloSius(aIdFascicoloSius);
			// ricerca delle precedenti comunicazioni di esecuzione sanzione sostitutiva
			// per il fascicolo
			IPeriodoAltraSanzione lCtrl1 = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
			List lListaSanzioniSius = lCtrl1.ExRicercaSanzioneSostitutivaByIdFascicolo(aIdFascicoloSius);

			if (lListaSanzioniSius != null && !lListaSanzioniSius.isEmpty()) {
				PeriodoAltraSanzioneModel lPeriodoAltraSanzioneModel = (PeriodoAltraSanzioneModel) lListaSanzioniSius
						.get(lListaSanzioniSius.size() - 1);
				if (lPeriodoAltraSanzioneModel.getDataScadenza() == null) {
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Inserire prima Data Scadenza di Inizio o Ripresa Sanzione.");
				}
				if (lPeriodoAltraSanzioneModel.getFlagMotivo().equals("03")) {
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! La sanzione risulta già sospesa.");
				}
				if (lDataDecorrenzaSanzSost.before(lPeriodoAltraSanzioneModel.getDataInizioEsecuzione())) {
					// MEV_2023-35: aggiunte date esplicative
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! La Data Decorrenza ("
									+ DateUtils.getDateToString(lDataDecorrenzaSanzSost, "dd/MM/yyyy")
									+ ") non puo essere minore della data Inizio/Ripresa Sanzione ("
									+ DateUtils.getDateToString(
											lPeriodoAltraSanzioneModel.getDataInizioEsecuzione(),
											"dd/MM/yyyy")
									+ ").");
				}
			} else {
				throw new SIUSException(SIUSException.USER_MESSAGE, "Inserire prima Inizio Sanzione.");
			}
			// Ricerca Esecuzione Sanzione Sostitutiva
			IEsecuzioneSS lCtrlESS = SIUSLookupRemote.getEsecuzioneSSRemote();
			lEssM = lCtrlESS.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(aIdFascicoloSius);

			// Controlla esistenza del record Esecuzione Sanzione Sostitutiva e della Data Termine Attuale
			if (lEssM != null && lEssM.getDataTermineAttuale() != null) {
				// Controlla che la Data Decorrenza Sospensione sia MINORE della DATA_TERMINE_ATTUALE
				// dell'Esecuzione Sanzione Sostitutiva
				if (DateUtils.isLower(lDataDecorrenzaSanzSost, lEssM.getDataTermineAttuale())) {
					lDataTermineIniziale = lEssM.getDataTermineAttuale();

					// Controllo per Data "Fino al"
					if ((!isRequestParameterNullObj(CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS)
							&& getRequestStringParameter(CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS).length() > 0)
							&& (!isRequestParameterNullObj(CAMPO_MESE_SCADENZA_SOSPENSIONE_SS)
									&& getRequestStringParameter(CAMPO_MESE_SCADENZA_SOSPENSIONE_SS)
											.length() > 0)
							&& (!isRequestParameterNullObj(CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS)
									&& getRequestStringParameter(CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS)
											.length() > 0)) {
						lDataScadenza = getRequestDateParameter(CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS,
								CAMPO_MESE_SCADENZA_SOSPENSIONE_SS, CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS);

						// Controlla che la Data "Fino al" sia MINORE della DATA_TERMINE_ATTUALE
						// dell'Esecuzione Sanzione Sostitutiva
						if (DateUtils.isLower(lDataScadenza, lEssM.getDataTermineAttuale())) {
							lPerAltSanMod.setDataScadenza(lDataScadenza);
						} else {
							// MEV_2023-35: richiesta durante il collaudo la possibilità di inserire oltre
							// la Data Termine Attuale
							if (!"U137".equals(
									mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())) {
								throw new SIUSException(SIUSException.USER_MESSAGE,
										"Attenzione! La Data 'Fino al' ("
												+ DateUtils.getDateToString(lDataScadenza, "dd/MM/yyyy")
												+ ") deve essere minore della Data Termine Attuale ("
												+ DateUtils.getDateToString(lDataDecorrenzaSanzSost,
														"dd/MM/yyyy")
												+ ").");
							}
						}
					}

					// Controllo per "Periodo Sospensione"
					if (lPerAltSanSospAA.intValue() != 0 || lPerAltSanSospMM.intValue() != 0
							|| lPerAltSanSospGG.intValue() != 0) {
						CalendarModel lCalModDurata = new CalendarModel();
						lCalModDurata.setNumAnni(lPerAltSanSospAA.intValue());
						lCalModDurata.setNumMesi(lPerAltSanSospMM.intValue());
						lCalModDurata.setNumGiorni(lPerAltSanSospGG.intValue());

						ICalcoloPena lCal = SIEPLookupRemote.getCalcoloPenaRemote();
						lDataScadenza = lCal.exCalcolaNuovaDataFine(lDataDecorrenzaSanzSost, lCalModDurata,
								false);

						// Controlla che il Periodo Sospensione sia MINORE della DATA_TERMINE_ATTUALE
						// dell'Esecuzione Sanzione Sostitutiva
						if (DateUtils.isLower(lDataScadenza, lEssM.getDataTermineAttuale())) {
							lPerAltSanMod.setDataScadenza(lDataScadenza);
						} else {
							throw new SIUSException(SIUSException.USER_MESSAGE,
									"Attenzione! il 'Periodo Sospensione' ("
											+ DateUtils.getDateToString(lDataScadenza, "dd/MM/yyyy")
											+ ") deve essere minore della Data Termine Attuale ("
											+ DateUtils.getDateToString(lEssM.getDataTermineAttuale(),
													"dd/MM/yyyy")
											+ ").");
						}
					}

					// Numero Giorni Recupero
					if (!isRequestParameterNullObj(CAMPO_GIORNI_RECUPERO_SS)
							&& getRequestBigDecimalParameter(CAMPO_GIORNI_RECUPERO_SS) != null) {
						CalendarModel lCalMod = new CalendarModel();
						lCalMod.setDataInizio(lDataDecorrenzaSanzSost);
						if (lDataScadenza != null) {
							lCalMod.setDataFine(lDataScadenza);
						} else {
							lCalMod.setDataFine(lEssM.getDataTermineAttuale());
						}
						CalendarUtil lCalUtil = new CalendarUtil();
						lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod, false);

						// Controlla che i giorni da recuperare non superino la Data Termine Attuale
						if (CalendarUtil.getTotGiorni(lCalMod) < lGiorniRecupero.intValue()) {
							if (lDataScadenza != null) {
								throw new SIUSException(SIUSException.USER_MESSAGE,
										"Numero di giorni superiore alla Data Termine Sospensione");
							} else {
								throw new SIUSException(SIUSException.USER_MESSAGE,
										"Numero di giorni superiore alla Data Termine Attuale");
							}
						}

						// Somma giorni a DATA_TERMINE_ATTUALE di ESS
						Date lDataTermineAttuale = DateUtils.moveDateTo(lEssM.getDataTermineAttuale(),
								Calendar.DAY_OF_MONTH, lGiorniRecupero.intValue());
						lEssM.setDataTermineAttuale(lDataTermineAttuale);

						// Codice Operatore, Codice Ufficio e Data aggiornamento
						lEssM.setCodOperatoreAggiornamento(getCodUtenteConnesso());
						lEssM.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
						lEssM.setDataAggiornamento(DateUtils.getSysDate());
					}

					Date DataInizio = null;
					Date DataFine = null;
					CalendarUtil lCalUtil = new CalendarUtil();

					// ---------------------------------------------
					// Calcolo della "Sanzione Sostituitiva Espiata"
					// ---------------------------------------------
					DataInizio = lEssM.getDataInizioSanzione();

					if (lDataScadenza != null) {
						DataFine = lDataScadenza;
					} else {
						DataFine = lDataDecorrenzaSanzSost;
					}

					CalendarModel lCalModEspiata = new CalendarModel();
					lCalModEspiata.setDataInizio(DataInizio);
					lCalModEspiata.setDataFine(DataFine);

					// Calcola il quantum
					lCalModEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalModEspiata, true);

					// Ritorna il quantum in giorni
					int lGiorniEspiati = CalendarUtil.getTotGiorni(lCalModEspiata);
					if (lGiorniRecupero != null) {
						// Toglie eventuali giorni recupero dal totale pena espiata
						lGiorniEspiati = lGiorniEspiati - lGiorniRecupero.intValue();
					}
					lCalModEspiata = new CalendarModel();
					lCalModEspiata.setNumGiorni(lGiorniEspiati);

					// vengono normalizzati i quantum
					lCalModEspiata = lCalUtil.ricalcolaGAM(lCalModEspiata);

					// Scrive i campi in Periodo Altra Sanzione da Espiare
					lPerAltSanMod.setEspiataGG(new BigDecimal(lCalModEspiata.getNumGiorni()));
					lPerAltSanMod.setEspiataMM(new BigDecimal(lCalModEspiata.getNumMesi()));
					lPerAltSanMod.setEspiataAA(new BigDecimal(lCalModEspiata.getNumAnni()));

					// ---------------------------------------------
					// Calcolo della "Sanzione Sostituitiva Residua"
					// ---------------------------------------------

					// Calcola la Sanzione Totale
					DataInizio = lEssM.getDataInizioSanzione();
					DataFine = lDataTermineIniziale;

					CalendarModel lCalModIniziale = new CalendarModel();
					lCalModIniziale.setDataInizio(DataInizio);
					lCalModIniziale.setDataFine(DataFine);

					lCalModIniziale = lCalUtil.CalcolaNumGiorniMesiAnni(lCalModIniziale, true);

					CalendarModel lCalModRes = new CalendarModel();
					// Calcola La Sanzione Sostitutiva Residua
					// Sottraendo dalla Totale L'Espiata
					lCalModRes = lCalUtil.sottraiGiorni(lCalModIniziale, lCalModEspiata);

					// Scrive i campi in Periodo Altra Sanzione Residua
					lPerAltSanMod.setResiduaGG(new BigDecimal(lCalModRes.getNumGiorni()));
					lPerAltSanMod.setResiduaMM(new BigDecimal(lCalModRes.getNumMesi()));
					lPerAltSanMod.setResiduaAA(new BigDecimal(lCalModRes.getNumAnni()));
				} else {
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! La Data Decorrenza Sospensione ("
									+ DateUtils.getDateToString(lDataDecorrenzaSanzSost, "dd/MM/yyyy")
									+ ") deve essere minore della Data Termine Attuale ("
									+ DateUtils.getDateToString(lEssM.getDataTermineAttuale(), "dd/MM/yyyy")
									+ ").");
				}
			} else {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! Data Termine Attuale non trovata.");
			}

			// Codice Tipo Ufficio Sosp
			lPerAltSanMod.setCodTipoUfficioSosp("-");
			// Codice Tipo Autorità
			lPerAltSanMod.setCodTipoAutorita("-");
			// Codice Luogo Autorità
			lPerAltSanMod.setCodLuogoAutorita("-");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("caricaPeriodoAltraSanzioneModESS: Fine");
		}

		return lPerAltSanMod;
	}

	/**
	 * Effetua una ricerca del Periodo Altra Sanzione, dell'Esecuzione Sanzione Sostitutiva e Scrive lo
	 * Scadenzario.
	 *
	 * @param mFasGPMod
	 *            : Model del fascicolo corrente,
	 * @param lIdEveDecOrd
	 *            : BigDecimal Id Evento Decreto/Ordinanza,
	 * @return lScadenzarioSiusMod: Model dello scadenzario SIUS.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ScadenzarioSiusModel caricaScadenzarioSiusPerAltSanzModESS(FascicoloGPModel mFasGPMod,
			BigDecimal lIdEveDecOrd, String lCodTipoScadenzarioPrincipal, String lCodTipoScadenzarioSecond)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("caricaScadenzarioSiusPerAltSanzModESS: Inizio");

		Date lDatacorrente = DateUtils.getSysDate();

		// Cerca ID Fascicolo Padre
		IFascicoloSius lCtrlFS = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel IFascicoloSiusMod = lCtrlFS.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
				mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1(),
				mFasGPMod.getGeneraleProcedimentoModel().getProgrS1(), getCodUfficioUtenteConnesso());

		BigDecimal aIdFascicoloSius = IFascicoloSiusMod.getFascicoloSiusModel().getIdFascicoloSius();

		// Ricerca Esecuzione Sanzione Sostitutiva per Id Fascicolo Padre
		IEsecuzioneSS lCtrlESS = SIUSLookupRemote.getEsecuzioneSSRemote();
		lEssM = lCtrlESS.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(aIdFascicoloSius);

		// Ricerca Periodo Altra Sanzione per Id Evento Decreto/Ordinanza
		IPeriodoAltraSanzione lCtrl1 = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		PeriodoAltraSanzioneModel IPASMod = lCtrl1.ExRicercaSanzioneSostitutivaByIdEvento(lIdEveDecOrd);

		// Ricerca Scadenzario Principale
		IScadenzarioSius lCtrlSca = SIUSLookupRemote.getScadenzarioRemote();
		lScadenzarioSiusModPrincipal = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(aIdFascicoloSius,
				lCodTipoScadenzarioPrincipal);

		// Controllo esitenza Scadenzario Principale
		if (lScadenzarioSiusModPrincipal != null) {
			// Controlla Esitenza Periodo Altra Sanzione
			if (IPASMod != null && IPASMod.getDataInizioEsecuzione() != null) {
				// Ricerca Scadenzario Secondario
				lScadenzarioSiusModSecond = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(
						aIdFascicoloSius, lCodTipoScadenzarioSecond);
				if (lScadenzarioSiusModSecond == null) {
					lScadenzarioSiusModSecond = new ScadenzarioSiusModel();
				}

				// Scrive l'Id dell'evento preso dallo scadenzario principale
				lScadenzarioSiusModSecond.setEveIdEvento(lScadenzarioSiusModPrincipal.getEveIdEvento());

				// Scrive la Data Inizio nello Scadenzario Secondario
				lScadenzarioSiusModSecond.setDataInizioScadenza(IPASMod.getDataInizioEsecuzione());

				// Scrive la Data Fine dello Scadenzario Secondario
				if (IPASMod.getDataScadenza() != null) {
					lScadenzarioSiusModSecond.setDataFineScadenza(IPASMod.getDataScadenza());
				} else {
					// Se non trova la Data Scadenza del Periodo Altra Sanzione inserisce la Data Termine
					// Attuale dell'Esec. Sanz. Sostit.
					lScadenzarioSiusModSecond.setDataFineScadenza(lEssM.getDataTermineAttuale());
				}

				// Se lo Scadenzario Secondario esiste aggiorna i dati aggiornamento
				if (lScadenzarioSiusModSecond.getIdScadenzarioSius() != null) {
					// Scrive Codice Operatore, Codice Ufficio e Data aggiornamento
					lScadenzarioSiusModSecond.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					lScadenzarioSiusModSecond.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lScadenzarioSiusModSecond.setDataAggiornamento(lDatacorrente);
				} else {
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
				if (IPASMod.getDaRecuperare() != null && IPASMod.getDaRecuperare().equals("1")) {
					// Modifica la Data Fine Scadenza dello Scadenzario Principale
					// con la Data Termine Attuale dell'Esecuzione Sanzione Sostituttiva
					lScadenzarioSiusModPrincipal.setDataFineScadenza(lEssM.getDataTermineAttuale());
					// Scrive Codice Operatore, Codice Ufficio e Data aggiornamento
					lScadenzarioSiusModPrincipal.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					lScadenzarioSiusModPrincipal.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lScadenzarioSiusModPrincipal.setDataAggiornamento(lDatacorrente);
				}
			} // FINE -- Controlla Esitenza Periodo Altra Sanzione
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
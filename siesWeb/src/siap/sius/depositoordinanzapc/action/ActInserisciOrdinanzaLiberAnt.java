package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciOrdinanzaLiberAnt extends ActInserisciOrdinanzaUDS
		implements ICostantiLibertaAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private class PeriodoClass {
		Date mDataIni = null;
		Date mDataFine = null;
	}

	private PeriodoClass[] mPeriodi = null;
	private int mInd = 0;
	private String mTipoConcessione; /* modalità di scelta dei periodi concessi. (S/C) L.A NORMALE */

	private PeriodoClass[] mPeriodi_spe = null;
	private int mInd_spe = 0;
	private String mTipoConcessione_spe; /* modalità di scelta dei periodi concessi. (S/C) L.A. SPECIALE */

	private PeriodoClass[] mPeriodi_int = null;
	private int mInd_int = 0;
	private String mTipoConcessione_int; /*
											 * modalità di scelta dei periodi concessi. (S/C) L.A.
											 * INTEGRAZIONE
											 */

	public String processRequest() throws Exception {
		return super.processRequest();
	}

	// Funzione da riscrivere per inserire la Richiesta di Libertà Anticipata
	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-------------> - INSERIMENTO L.A. NORMALE ");

		// >>>>>>>>>>>>>>>>> L.A. NORMALE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		LicenzaPeriodiLibAnticipataModel[] lLicenze = null;
		OrdinanzaEventoTenoriGProcModel lModRet = null;
		LicenzaLibAnticipataModel lLicenzaC = null;

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
		// Prendo subito gli eventuali giorni di L.A.
		int SommatotLA = 0;

		if (getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA) != null) {
			if (getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue() > 0) {
				SommatotLA = getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue();
			}
		}
		if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA) != null) {
			if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA)
					.intValue() > 0) {
				SommatotLA = getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA)
						.intValue();
			}
		}

		// S semestri - C periodo Unico
		mTipoConcessione = getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE);

		if (isRequestChecked(CAMPO_CHECK_CONCESSI)) {
			// Numero chek periodi da 45 gg ( se CAMPO_RADIO_TIPO_CONCESSIONE = S)
			lChecks = getRequestStringParameters(CAMPO_CHECK_CONCESSI);
			numCheck += lChecks.length;
			lConcessi = true;
			numCheckConcessi = numCheck;
		}
		if (isRequestChecked(CAMPO_CHECK_PERIODO)) {
			// check per inserimento periodo in periodo Unico ( se CAMPO_RADIO_TIPO_CONCESSIONE = C)
			numCheck++;
			lPeriodo = true;
		}
		if (isRequestChecked(CAMPO_CHECK_RIGETTATI)) {
			numCheck++;
			lRigettati = true;
		}
		if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI)) {
			numCheck++;
			lInammissibili = true;
		}
		if (isRequestChecked(CAMPO_CHECK_NLP)) {
			numCheck++;
			lNLP = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek " + numCheck);

		// Per i Reclami in materia di LA se l'esito è 'Accoglie reclamo del PM'
		// il FLAG_CONCESSO va impostato ad 'S' -- Michele
		String flagConcesso = "C";

		if (!isRequestParameterNullObj(CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO).compareTo("S") == 0)
				flagConcesso = "S";
		}

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A. e Periodi concessi.
		if (numCheck > 0) {
			lLicenze = new LicenzaPeriodiLibAnticipataModel[numCheck];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi = leggiDate();

			if (lConcessi) {
				if (flagConcesso.compareTo("S") != 0)
					flagConcesso = "C";
				while (i < numCheckConcessi) {
					lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze[i].setLicenza(generaLicenza(flagConcesso));
					lLicenze[i].getLicenza().setFlagScorta(mTipoConcessione);
					setPeriodiInLicenze(lLicenze[i]);
					i++;
				}
			}
			if (lPeriodo) {
				if (flagConcesso.compareTo("S") != 0)
					flagConcesso = "C";

				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso));

				if (SommatotLA > 0)
					lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLA));

				lLicenze[i].getLicenza().setFlagScorta(mTipoConcessione);
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}

			if (lRigettati) {
				flagConcesso = "R";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}
			if (lInammissibili) {
				flagConcesso = "I";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}
			if (lNLP) {
				flagConcesso = "N";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}
		} // Chiude if numCheck > 0

		// devo ricontrollarlo perchè 'flagConcesso' potrebbe essere diventato R, oppure I, o N
		if (!isRequestParameterNullObj(CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO)) {
			if (getRequestStringParameter(CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO).compareTo("S") == 0)
				flagConcesso = "S";
		}

		// if (numCheck == 0 && mTipoConcessione.compareTo("C") == 0)
		if (mTipoConcessione.compareTo("C") == 0 && lPeriodo == false && SommatotLA > 0) {
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
				lLicenzaC.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			lLicenzaC.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
			lLicenzaC.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
			// L.A. NORMALE
			lLicenzaC.setDescrStatoPermesso("LAU");
			lLicenzaC.setFlagScorta(mTipoConcessione);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------> - INSERIMENTO L.A. SPECIALE ");

		// >>>>>>>>>>>>>>>>> L.A. SPECIALE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		LicenzaPeriodiLibAnticipataModel[] lLicenze_spe = null;
		LicenzaLibAnticipataModel lLicenzaC_SPE = null;

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
		// Prendo subito gli eventuali giorni di L.A.SPECIALE
		int SommatotLS = 0;

		if (getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE) != null) {
			if (getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE).intValue() > 0) {
				SommatotLS = getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE).intValue();
			}
		}
		if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE) != null) {
			if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE)
					.intValue() > 0) {
				SommatotLS = getRequestBigDecimalParameter(
						ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE).intValue();
			}
		}

		// S semestri - C periodo Unico
		mTipoConcessione_spe = getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE_SPE);

		if (isRequestChecked(CAMPO_CHECK_CONCESSI_SPE)) {
			// Numero chek periodi da 75 gg (se CAMPO_RADIO_TIPO_CONCESSIONE = S)
			lChecks_spe = getRequestStringParameters(CAMPO_CHECK_CONCESSI_SPE);
			numCheck_spe += lChecks_spe.length;
			lConcessi_spe = true;
			numCheckConcessi_spe = numCheck_spe;
		}
		if (isRequestChecked(CAMPO_CHECK_PERIODO_SPE)) {
			// check per inserimento periodo in periodo Unico (se CAMPO_RADIO_TIPO_CONCESSIONE = C)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("--> periodo Unico SPE ");
			numCheck_spe++;
			lPeriodo_spe = true;
		}
		if (isRequestChecked(CAMPO_CHECK_RIGETTATI_SPE)) {
			numCheck_spe++;
			lRigettati_spe = true;
		}
		if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI_SPE)) {
			numCheck_spe++;
			lInammissibili_spe = true;
		}
		if (isRequestChecked(CAMPO_CHECK_NLP_SPE)) {
			numCheck_spe++;
			lNLP_spe = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek L.A. Speciale " + numCheck_spe);

		// Per i Reclami in materia di 'L.A. SPECIALE' se l'esito è 'Accoglie reclamo del PM'
		// il 'flagConcesso_spe' va impostato ad 'S' -- Michele
		String flagConcesso_spe = "C";

		if (!isRequestParameterNullObj(CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO).compareTo("S") == 0)
				flagConcesso_spe = "S";
		}

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A.SPECIALE e Periodi concessi.
		if (numCheck_spe > 0) {
			lLicenze_spe = new LicenzaPeriodiLibAnticipataModel[numCheck_spe];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi_spe = leggiDate_spe();

			if (lConcessi_spe) {
				if (flagConcesso_spe.compareTo("S") != 0)
					flagConcesso_spe = "C";

				while (i < numCheckConcessi_spe) {
					lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe));
					lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);
					setPeriodiInLicenze_spe(lLicenze_spe[i]);
					i++;
				}
			}
			if (lPeriodo_spe) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("--> PeriodoUnico SPE con date dal al");
				if (flagConcesso_spe.compareTo("S") != 0)
					flagConcesso_spe = "C";

				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe));

				if (SommatotLS > 0)
					lLicenze_spe[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLS));

				lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lRigettati_spe) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" -------> Rigettati SPE");
				flagConcesso_spe = "R";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lInammissibili_spe) {
				flagConcesso_spe = "I";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lNLP_spe) {
				flagConcesso_spe = "N";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}
		} // chiude if numcheck_spe > 0

		// devo ricontrollarlo perchè 'flagConcesso_spe' potrebbe essere diventato R, oppure I, o N
		if (!isRequestParameterNullObj(CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO)) {
			if (getRequestStringParameter(CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO).compareTo("S") == 0)
				flagConcesso_spe = "S";
		}

		// if (numCheck_spe == 0 && mTipoConcessione_spe.compareTo("C") == 0)
		if (mTipoConcessione_spe.compareTo("C") == 0 && lPeriodo_spe == false && SommatotLS > 0) {
			lLicenzaC_SPE = new LicenzaLibAnticipataModel();

			lLicenzaC_SPE.setCodTipoLicenza("LA");
			lLicenzaC_SPE.setCodOperatoreInserimento(getCodUtenteConnesso());
			lLicenzaC_SPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lLicenzaC_SPE.setDataInserimento(mOggi);
			lLicenzaC_SPE.setFlagConcesso(flagConcesso_spe);

			if (SommatotLS > 0)
				lLicenzaC_SPE.setNumeroGiorni(new BigDecimal(SommatotLS));

			if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
				lLicenzaC_SPE
						.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			lLicenzaC_SPE.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
			lLicenzaC_SPE.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
			// L.A. SPECIALE
			lLicenzaC_SPE.setDescrStatoPermesso("LSU");
			lLicenzaC_SPE.setFlagScorta(mTipoConcessione_spe);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("------------->  INSERIMENTO L.A. INTEGRAZIONE");
		// >>>>>>>>>>>>>>>>> L.A. INTEGRAZIONE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		LicenzaPeriodiLibAnticipataModel[] lLicenze_int = null;
		LicenzaLibAnticipataModel lLicenzaC_INT = null;

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
		// prendo subito i giorni di L.A. INTEGRAZIONE
		int SommatotLI = 0;

		if (getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA_INT) != null) {
			if (getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA_INT).intValue() > 0) {
				SommatotLI = getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA_INT).intValue();
			}
		}
		if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT) != null) {
			if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT)
					.intValue() > 0) {
				SommatotLI = getRequestBigDecimalParameter(
						ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT).intValue();
			}
		}

		mTipoConcessione_int = getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE_INT);
		if (isRequestChecked(CAMPO_CHECK_CONCESSI_INT)) {
			lChecks_int = getRequestStringParameters(CAMPO_CHECK_CONCESSI_INT);
			numCheck_int += lChecks_int.length;
			lConcessi_int = true;
			numCheckConcessi_int = numCheck_int;
		}
		if (isRequestChecked(CAMPO_CHECK_PERIODO_INT)) {
			numCheck_int++;
			lPeriodo_int = true;
		}
		if (isRequestChecked(CAMPO_CHECK_RIGETTATI_INT)) {
			numCheck_int++;
			lRigettati_int = true;
		}
		if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI_INT)) {
			numCheck_int++;
			lInammissibili_int = true;
		}
		if (isRequestChecked(CAMPO_CHECK_NLP_INT)) {
			numCheck_int++;
			lNLP_int = true;
		}

		// Per i Reclami in materia di 'L.A. IINTEGRAZIONE' se l'esito è 'Accoglie reclamo del PM'
		// il 'flagConcesso_int' va impostato ad 'S' -- Michele
		String flagConcesso_int = "C";

		if (!isRequestParameterNullObj(CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO).compareTo("S") == 0)
				flagConcesso_int = "S";
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek L.A. Integrazione " + numCheck_int);

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
					lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int));
					lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione_int);
					setPeriodiInLicenze_int(lLicenze_int[i]);
					i++;
				}
			}
			if (lPeriodo_int) {
				if (flagConcesso_int.compareTo("S") != 0)
					flagConcesso_int = "C";

				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int));

				if (SommatotLI > 0)
					lLicenze_int[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLI));

				lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione_int);
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}

			if (lRigettati_int) {
				flagConcesso_int = "R";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}
			if (lInammissibili_int) {
				flagConcesso_int = "I";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}
			if (lNLP_int) {
				flagConcesso_int = "N";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}
		} // chiude if numcheck_int > 0

		// devo ricontrollarlo perchè 'flagConcesso_int' potrebbe essere diventato R, oppure I, o N
		if (!isRequestParameterNullObj(CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO)) {
			if (getRequestStringParameter(CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO).compareTo("S") == 0)
				flagConcesso_int = "S";
		}

		// if (numCheck_int == 0 && mTipoConcessione_int.compareTo("C") == 0)
		if (mTipoConcessione_int.compareTo("C") == 0 && lPeriodo_int == false && SommatotLI > 0) {
			lLicenzaC_INT = new LicenzaLibAnticipataModel();

			lLicenzaC_INT.setCodTipoLicenza("LA");
			lLicenzaC_INT.setCodOperatoreInserimento(getCodUtenteConnesso());
			lLicenzaC_INT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lLicenzaC_INT.setDataInserimento(mOggi);
			lLicenzaC_INT.setFlagConcesso(flagConcesso_int);

			if (SommatotLI > 0)
				lLicenzaC_INT.setNumeroGiorni(new BigDecimal(SommatotLI));

			if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
				lLicenzaC_INT
						.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			lLicenzaC_INT.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
			lLicenzaC_INT.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
			// L.A. INTEGRAZIONE
			lLicenzaC_INT.setDescrStatoPermesso("LIU");
			lLicenzaC_INT.setFlagScorta(mTipoConcessione_int);
		}

		// ---------------------------------------------------------------------------------------------------
		// SOMMA TOTALE GIORNI
		//
		// 10-03-2014 ---> Nuova ordinanza L.A. dopo DECRETO 146/2013 //
		// Da oggi in poi in "aOrdEveTenGP" va messo il numero di giorni totali di LA ( LA + LS + LI)
		//
		// SommatotLA = eventuale Num. gg. Totali di L.A. NORMALE
		// SommatotLS = eventuale Num. gg. Totali di L.A. SPECIALE
		// SommatotLI = eventuale Num. gg. Totali di L.A. INTEGRAZIONE

		int Sommatot = 0;
		Sommatot = SommatotLA + SommatotLS + SommatotLI;

		// Somma totale dei giorni concessi in DEPOSITO_ORDINANZA_PC
		aOrdEveTenGP.getOrdinanza().setNumGiorniLibanticipata(new BigDecimal(Sommatot));
		//
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		lModRet = (IDepOrdCtrl.ExInserisciOrdinanzaLibAnt(aOrdEveTenGP, lLicenze, lLicenze_spe, lLicenze_int,
				lLicenzaC, lLicenzaC_SPE, lLicenzaC_INT));

		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO !");
		return lModRet;
	} // Chiude metodo. public OrdinanzaEventoTenoriGProcModel inserimento

	// >>>>>>>> L.A NORMALE Lettura di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate() throws F3BException {

		PeriodoClass[] lPeriodi = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;

		lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
				CAMPO_GIORNO_DATA_INIZIO);
		lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
				CAMPO_GIORNO_DATA_FINE);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
		lPeriodi = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi[i] = new PeriodoClass();
			lPeriodi[i].mDataIni = lDateInizio[i];
			lPeriodi[i].mDataFine = lDateFine[i];
		}

		return lPeriodi;
	}

	private void setPeriodiInLicenze(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd; // Salvataggio del valore corrente di mInd

		for (int j = 0, k = mInd; j < 10; j++, k++) {
			if ((mPeriodi[k].mDataIni != null) && (mPeriodi[k].mDataFine != null))
				num++;
		}

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
			}
		}
		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.

		mInd = lInd + 10;
	} // chiude setPeriodiInLicenze

	private LicenzaLibAnticipataModel generaLicenza(String aFlagConcesso) throws F3BException {

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

		return lLicenza;
	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa(String aFlagConcesso) throws F3BException {

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

	// private void controllo(LicenzaPeriodiLibAnticipataModel[] aLicenze) {
	//
	// if (aLicenze == null)
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("ATTENZIONE ---------------> Licenze assenti");
	// else
	// for (int i = 0; i < aLicenze.length; i++) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("*** Licenza n. " + i + " ***");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug(aLicenze[i].toString());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("********");
	// }
	// }

	/*
	 * >>>>>>>>>>>>>>>>>>>>>>>>>> L.A. SPECIALE
	 */
	// L.A SPECIALE Lettura di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate_spe() throws F3BException {

		PeriodoClass[] lPeriodi_spe = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;

		lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO_SPE, CAMPO_MESE_DATA_INIZIO_SPE,
				CAMPO_GIORNO_DATA_INIZIO_SPE);
		lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE_SPE, CAMPO_MESE_DATA_FINE_SPE,
				CAMPO_GIORNO_DATA_FINE_SPE);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;

		lPeriodi_spe = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi_spe[i] = new PeriodoClass();
			lPeriodi_spe[i].mDataIni = lDateInizio[i];
			lPeriodi_spe[i].mDataFine = lDateFine[i];
		}
		return lPeriodi_spe;
	}

	private void setPeriodiInLicenze_spe(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd_spe; // Salvataggio del valore corrente di mInd

		for (int j = 0, k = mInd_spe; j < 10; j++, k++)
			if ((mPeriodi_spe[k].mDataIni != null) && (mPeriodi_spe[k].mDataFine != null))
				num++;

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd
		for (int j = 0; j < num; mInd_spe++) {
			if ((mPeriodi_spe[mInd_spe].mDataIni != null) && (mPeriodi_spe[mInd_spe].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa_spe(
						aLicenza.getLicenza().getFlagConcesso());
			}
		}
		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.
		mInd_spe = lInd + 10;
	} // chiude setPeriodiInLicenze_spe

	private LicenzaLibAnticipataModel generaLicenza_spe(String aFlagConcesso) throws F3BException {

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

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi_spe[mInd_spe].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi_spe[mInd_spe].mDataFine);
		lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPeriodoLib.setDataInserimento(mOggi);
		lPeriodoLib.setFlagConcesso(aFlagConcesso);

		return lPeriodoLib;
	}

	/*
	 * >>>>>>>>>>>>>>>>>>>>>>>>>> L.A. INTEGRAZIONE
	 */
	// L.A INTEGRAZIONE Lettura di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate_int() throws F3BException {

		PeriodoClass[] lPeriodi_int = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;

		lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO_INT, CAMPO_MESE_DATA_INIZIO_INT,
				CAMPO_GIORNO_DATA_INIZIO_INT);
		lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE_INT, CAMPO_MESE_DATA_FINE_INT,
				CAMPO_GIORNO_DATA_FINE_INT);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;

		lPeriodi_int = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi_int[i] = new PeriodoClass();
			lPeriodi_int[i].mDataIni = lDateInizio[i];
			lPeriodi_int[i].mDataFine = lDateFine[i];
		}

		return lPeriodi_int;
	}

	private void setPeriodiInLicenze_int(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd_int; // Salvataggio del valore corrente di mInd

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

	private LicenzaLibAnticipataModel generaLicenza_int(String aFlagConcesso) throws F3BException {

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

} // Chiude Classe Action
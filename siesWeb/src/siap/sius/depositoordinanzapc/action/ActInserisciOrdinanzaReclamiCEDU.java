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

public class ActInserisciOrdinanzaReclamiCEDU extends ActInserisciOrdinanzaUDS
		implements ICostantiLibertaAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private class PeriodoClass {
		Date mDataIni = null;
		Date mDataFine = null;
	}

	private PeriodoClass[] mPeriodi = null;
	// private int mInd = 0;

	public String processRequest() throws Exception {
		return super.processRequest();
	}

	// Funzione da riscrivere per inserire la Richiesta di Libertà Anticipata
	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-------------> - INSERIMENTO ORDINANZA RECLAMI CEDU PERIODO / SOMMA RISARCIMENTO ");

		// -------------------------------------------------------------
		// >>>>>>>>>>>>>>>>> RIDUZIONE PENA : GIORNI / RISARCIMENTO : EURO <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//

		LicenzaPeriodiLibAnticipataModel[] lLicenze = null;
		OrdinanzaEventoTenoriGProcModel lModRet = null;

		int numCheck = 0;
		boolean lPeriodo = false;
		boolean lSomma = false;
		boolean lRigettati = false;
		boolean lInammissibili = false;
		boolean lNLP = false;
		//
		// eventuali giorni di di Riduzione pena concessi

		int GiorniRiduzione = 0;

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU) != null
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
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU) != null
				&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU)
						.equals("")) {
			SommaRisarcimento += getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU);
		}

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU) != null
				&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU)
						.equals("")) {
			SommaRisarcimento += "."
					+ getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU);
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ActInserisciOrdinanzaViolazioneCEDU - XXXXX Somma Risarcimento = "+
		// SommaRisarcimento);
		//

		if (isRequestChecked(CAMPO_CHECK_PERIODI_CONCESSI_CEDU)) {
			// check per inserimento periodo
			numCheck++;
			lPeriodo = true;
		}
		if (isRequestChecked(CAMPO_CHECK_EURO_CONCESSI_CEDU)) {
			// chek per inserimento risarcimento in E.
			numCheck++;
			lSomma = true;
		}
		if (isRequestChecked(CAMPO_CHECK_RIGETTATI_CEDU)) {
			numCheck++;
			lRigettati = true;
		}
		if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI_CEDU)) {
			numCheck++;
			lInammissibili = true;
		}
		if (isRequestChecked(CAMPO_CHECK_NLP_CEDU)) {
			numCheck++;
			lNLP = true;
		}

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("Numero chek " + numCheck);
		//
		String flagConcesso = "";
		String TipoLic = "";

		if (numCheck > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("numCheck > 0");
			lLicenze = new LicenzaPeriodiLibAnticipataModel[numCheck];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi = leggiDate();

			if (lPeriodo) {
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.debug("--> PeriodoUnico concesso con date dal al");
				flagConcesso = "C";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, TipoLic, aOrdEveTenGP));

				if (GiorniRiduzione > 0)
					lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(GiorniRiduzione));

				setPeriodiInLicenze(lLicenze[i], 0);
				i++;
			}

			if (lSomma) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" --> Riarcimento in denaro Concesso");
				flagConcesso = "C";
				TipoLic = "SL";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, TipoLic, aOrdEveTenGP));

				if (!SommaRisarcimento.equals(""))
					lLicenze[i].getLicenza().setSommaRisarcDanni(new BigDecimal(SommaRisarcimento));

				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenze(lLicenze[i], 10);
				i++;

			}

			if (lRigettati) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" -------> Rigettati");
				flagConcesso = "R";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, TipoLic, aOrdEveTenGP));

				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenze(lLicenze[i], 20);
				i++;
			}
			if (lInammissibili) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" -------> Inammissibili");
				flagConcesso = "I";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, TipoLic, aOrdEveTenGP));

				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenze(lLicenze[i], 30);
				i++;
			}
			if (lNLP) {
				flagConcesso = "N";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, TipoLic, aOrdEveTenGP));

				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenze(lLicenze[i], 40);
				i++;
			}

		} // Chiude if numCheck > 0

		// Somma totale dei giorni concessi e della somma da liquidare in Eu. in DEPOSITO_ORDINANZA_PC
		aOrdEveTenGP.getOrdinanza().setNumGiorniRiduzionePena(new BigDecimal(GiorniRiduzione));
		if (!SommaRisarcimento.equals(""))
			aOrdEveTenGP.getOrdinanza().setSommaRisarcimento(new BigDecimal(SommaRisarcimento));
		//
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		// lModRet = (IDepOrdCtrl.ExInserisciOrdinanzaLibAnt(aOrdEveTenGP, lLicenze, lLicenze_spe,
		// lLicenze_int, lLicenzaC, lLicenzaC_SPE, lLicenzaC_INT) );
		lModRet = (IDepOrdCtrl.ExInserisciOrdinanzaLibAnt(aOrdEveTenGP, lLicenze, null, null, null, null,
				null));

		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO !");
		return lModRet;

	} // Chiude metodo. public OrdinanzaEventoTenoriGProcModel inserimento

	// >>>>>>>> L.A NORMALE Lettura di tutti i periodi di date valorizzati nella form di input.

	private PeriodoClass[] leggiDate() throws F3BException {
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("----> C leggiDate: ");

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
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: Elemento Iesimo = >"+i+"<" );
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataIni = " + lPeriodi[i].mDataIni);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataFine = " + lPeriodi[i].mDataFine);
		}

		return lPeriodi;
	}

	private void setPeriodiInLicenze(LicenzaPeriodiLibAnticipataModel aLicenza, int mInd)
			throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		// int lInd = mInd; // Salvataggio del valore corrente di mInd

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug(" setPeriodiInLicenze - inizio lInd = "+lInd);

		for (int j = 0, k = mInd; j < 10; j++, k++) {
			if ((mPeriodi[k].mDataIni != null) && (mPeriodi[k].mDataFine != null)) {
				num++;
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.debug(" setPeriodiInLicenze - data_ini presa = "+mPeriodi[k].mDataIni);
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.debug(" setPeriodiInLicenze -------------> num = "+num +" - J = "+j +" - k =
				//// "+k);
			}
		}

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd

		for (int j = 0; j < num; mInd++) {
			if ((mPeriodi[mInd].mDataIni != null) && (mPeriodi[mInd].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa(aLicenza.getLicenza().getFlagConcesso(),
						mInd);
			}
		}

		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.
		// mInd = lInd + 10;

	} // chiude setPeriodiInLicenze

	private LicenzaLibAnticipataModel generaLicenza(String aFlagConcesso, String aTipoLicenza,
			OrdinanzaEventoTenoriGProcModel aOrdEveTenGP) throws F3BException {

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug(getClass().getName() + ".generaLicenza ini ");
		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza(aTipoLicenza);
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante ");

		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());

		lLicenza.setCodLuogoEmittente(aOrdEveTenGP.getEvento().getCodLuogoEmittente());
		lLicenza.setCodUfficioEmittente(aOrdEveTenGP.getEvento().getCodUfficioEmittente());

		if (aOrdEveTenGP.getOrdinanza() != null && aOrdEveTenGP.getOrdinanza().getAnnoS3() != null)
			lLicenza.setAnnoOrdinanza(aOrdEveTenGP.getOrdinanza().getAnnoS3());
		if (aOrdEveTenGP.getOrdinanza() != null && aOrdEveTenGP.getOrdinanza().getNumS3() != null)
			lLicenza.setNumeroOrdinanza(aOrdEveTenGP.getOrdinanza().getNumS3());

		lLicenza.setDataEmissioneOrdinanza(aOrdEveTenGP.getOrdinanza().getDataCameraConsiglio());

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug(getClass().getName() + ".generaLicenza fine ");

		return lLicenza;

	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa(String aFlagConcesso, int mInd)
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

} // Chiude Classe Action
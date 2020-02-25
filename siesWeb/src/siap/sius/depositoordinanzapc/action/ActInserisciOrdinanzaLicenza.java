package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

public class ActInserisciOrdinanzaLicenza extends ActInserisciOrdinanzaUDS implements
		ICostantiLicenzaLibanticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		return super.processRequest();
	}

	// Funzione da riscrivere per inserire la Licenza
	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {

		LicenzaPeriodiLibAnticipataModel[] lLicenze = new LicenzaPeriodiLibAnticipataModel[1];
		OrdinanzaEventoTenoriGProcModel lModRet = null;

		lLicenze[0] = new LicenzaPeriodiLibAnticipataModel();
		BigDecimal lNumGiorni = getRequestBigDecimalParameter(CAMPO_NUMERO_GIORNI);
		aOrdEveTenGP.getOrdinanza().setNumGiorniLibanticipata(lNumGiorni);
		// inserimento
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();

		if (lNumGiorni.intValue() > 0) {
			// Generazione del record Licenza solo se concessa
			lLicenze[0].setLicenza(generaLicenza());
			lModRet = (IDepOrdCtrl.ExInserisciOrdinanzaLibAnt(aOrdEveTenGP, lLicenze));
		} else
			lModRet = (IDepOrdCtrl.ExInserisciOrdinanza(aOrdEveTenGP));

		controllo(lLicenze);

		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO.");
		return lModRet;

	}

	private LicenzaLibAnticipataModel generaLicenza() throws F3BException {

		String lOraInizio = null;
		String lOraFine = null;

		// Valorizzazione del record Licenza
		LicenzaLibAnticipataModel lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza(ICostantiDepositoOrdinanzaPc.LICENZA);
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso("C");
		lLicenza.setNumeroGiorni(getRequestBigDecimalParameter(CAMPO_NUMERO_GIORNI));
		lLicenza.setLuogoSvolgimentoProva(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_LUOGO_SVOLGIMENTO_PROVA));
		// Preleva data di inizio licenza
		Date lDataInizio = getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
				CAMPO_GIORNO_DATA_INIZIO);
		// Date lDataInizio = getRequestDateTimeParameter( CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
		// CAMPO_GIORNO_DATA_INIZIO , CAMPO_ORA_INIZIO,CAMPO_MINUTI_INIZIO);

		// Preleva data di fine licenza
		Date lDataFine = getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
				CAMPO_GIORNO_DATA_FINE);
		// Date lDataFine = getRequestDateTimeParameter( CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
		// CAMPO_GIORNO_DATA_FINE , CAMPO_ORA_FINE, CAMPO_MINUTI_FINE);

		lLicenza.setDataInizio(lDataInizio);
		lLicenza.setDataFine(lDataFine);

		lOraInizio = getRequestStringParameter(CAMPO_ORA_INIZIO);
		lOraFine = getRequestStringParameter(CAMPO_ORA_FINE);

		if (lOraInizio.trim().length() > 0) {
			lOraInizio += "," + getRequestStringParameter(CAMPO_MINUTI_INIZIO);
			lLicenza.setOraInizio(lOraInizio);
		}
		if (lOraFine.trim().length() > 0) {
			lOraFine += "," + getRequestStringParameter(CAMPO_MINUTI_FINE);
			lLicenza.setOraFine(lOraFine);
		}

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			siesLogger.debug(" Fascicolo SIUS mancante ");
		/*
		 * lLicenza.setFasSieIdFascicoloSiep();
		 */
		return lLicenza;
	}

	private void controllo(LicenzaPeriodiLibAnticipataModel[] aLicenze) {

		if (aLicenze == null)
			siesLogger.debug("Licenze assenti");
		else {
			for (int i = 0; i < aLicenze.length; i++) {
				siesLogger.debug("Nessun Controllo?");
			}
		}
	}

}
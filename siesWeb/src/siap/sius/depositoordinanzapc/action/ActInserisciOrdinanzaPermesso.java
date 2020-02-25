package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

public class ActInserisciOrdinanzaPermesso extends ActInserisciOrdinanzaUDS implements
		ICostantiLicenzaLibanticipata {

	public String processRequest() throws Exception {

		return super.processRequest();
	}

	// Funzione da riscrivere per inserire la Licenza
	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {

		LicenzaPeriodiLibAnticipataModel[] lLicenze = new LicenzaPeriodiLibAnticipataModel[1];
		OrdinanzaEventoTenoriGProcModel lModRet = null;

		lLicenze[0] = new LicenzaPeriodiLibAnticipataModel();
		/*
		 * BigDecimal lNumGiorni = getRequestBigDecimalParameter(CAMPO_NUMERO_GIORNI);
		 * aOrdEveTenGP.getOrdinanza().setNumGiorniLibanticipata(lNumGiorni); // inserimento
		 */
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		/*
		 * if (lNumGiorni.intValue() > 0) {
		 */
		// Generazione del record Licenza solo se concessa
		lLicenze[0].setLicenza(generaPermesso());
		lModRet = (IDepOrdCtrl.ExInserisciOrdinanzaLibAnt(aOrdEveTenGP, lLicenze));
		/*
		 * } else lModRet = (IDepOrdCtrl.ExInserisciOrdinanza(aOrdEveTenGP));
		 * 
		 * controllo(lLicenze);
		 */
		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO.");
		return lModRet;
	}

	private LicenzaLibAnticipataModel generaPermesso() throws F3BException {

//		String lOraInizio = null;
//		String lOraFine = null;

		// Valorizzazione del record Licenza (Permesso)
		LicenzaLibAnticipataModel lPermesso = new LicenzaLibAnticipataModel();

		lPermesso.setCodTipoLicenza("PP");

		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI)
				&& getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI).trim()
						.length() > 0)
			lPermesso.setNumeroGiorni(new BigDecimal(
					getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI)));

		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE)
				&& getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE).trim().length() > 0)
			lPermesso.setNumeroOre(new BigDecimal(
					getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE)));

		lPermesso.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPermesso.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPermesso.setDataInserimento(mOggi);
		lPermesso.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lPermesso.setFasSieIdFascicoloSiep(mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

		return lPermesso;
	}

}
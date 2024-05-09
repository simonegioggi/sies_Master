package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Class action per l'inserimento della rateizzazione
 * 
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActInserisciRateizzazione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("ActInserisciRateizzazione....");

		Vector<RateizzazionePPModel> aListaRate = this.recuperaRate();

		// Inserimento
		IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
		lRateCTRL.exInserisciRateizzazioni(aListaRate);

		//
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione";
		return lPage;
	}

	protected Vector<RateizzazionePPModel> recuperaRate() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lTipoRateizzazione = getRequestStringParameter(CAMPO_TIPO_RATEIZZAZIONE);
		siesLogger.debug("lTipoRateizzazione = " + lTipoRateizzazione);

		BigDecimal lImportoDaPagare = null;
		if ((getRequestStringParameter(CAMPO_VALORE_IMPORTO_I) != null
				&& !getRequestStringParameter(CAMPO_VALORE_IMPORTO_I).equals(""))
				|| (getRequestStringParameter(CAMPO_VALORE_IMPORTO_D) != null
						&& !getRequestStringParameter(CAMPO_VALORE_IMPORTO_D).equals(""))) {
			lImportoDaPagare = new BigDecimal(getRequestStringParameter(CAMPO_VALORE_IMPORTO_I) + "."
					+ getRequestStringParameter(CAMPO_VALORE_IMPORTO_D));
		}

		Vector<RateizzazionePPModel> aListaRate = new Vector<>();

		if (TIPO_RATEIZZAZIONE_UNICA.equals(lTipoRateizzazione)) {
			siesLogger.debug("Rateizzazione Unica");
			RateizzazionePPModel rateizzazioneModel = new RateizzazionePPModel();

			rateizzazioneModel.setTipoRateizzazione(lTipoRateizzazione);
			rateizzazioneModel.setNumeroRate(new BigDecimal(1));
			rateizzazioneModel
					.setScadenzaGiorni(getRequestBigDecimalParameter(CAMPO_SCADENZA_GIORNI_RATA_UNICA));

			rateizzazioneModel.setProgressivoRata(new BigDecimal(1));

			BigDecimal lImportoRata = null;
			if ((getRequestStringParameter(CAMPO_VALORE_RATA_UNICA_I) != null
					&& !getRequestStringParameter(CAMPO_VALORE_RATA_UNICA_I).equals(""))
					|| (getRequestStringParameter(CAMPO_VALORE_RATA_UNICA_D) != null
							&& !getRequestStringParameter(CAMPO_VALORE_RATA_UNICA_D).equals(""))) {
				lImportoRata = new BigDecimal(getRequestStringParameter(CAMPO_VALORE_RATA_UNICA_I) + "."
						+ getRequestStringParameter(CAMPO_VALORE_RATA_UNICA_D));
			}

			rateizzazioneModel.setImportoDaPagare(lImportoDaPagare);
			rateizzazioneModel.setImportoRata(lImportoRata);

			rateizzazioneModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

			rateizzazioneModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			rateizzazioneModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			rateizzazioneModel.setDataInserimento(DateUtils.getSysDate());

			siesLogger.debug("rateizzazioneModel = " + rateizzazioneModel);

			aListaRate.add(rateizzazioneModel);
		} else if (TIPO_RATEIZZAZIONE_RATEALE.equals(lTipoRateizzazione)) {
			siesLogger.debug("Rateizzazione su più rate");

			int maxNumRate = ICostantiRateizzazionePP.NUM_MAX_RATE;

			for (int i = 0; i < maxNumRate; i++) {
				if (!isRequestParameterNullObj(ICostantiRateizzazionePP.CAMPO_NUM_RATE + "_" + i)) {
					siesLogger.debug("la riga (" + i + ") è abilitata, recupero i dati...");
					RateizzazionePPModel rateizzazioneModel = new RateizzazionePPModel();

					rateizzazioneModel.setTipoRateizzazione(lTipoRateizzazione);
					rateizzazioneModel.setNumeroRate(
							getRequestBigDecimalParameter(ICostantiRateizzazionePP.CAMPO_NUM_RATE + "_" + i));

					rateizzazioneModel.setProgressivoRata(new BigDecimal(i + 1));

					if (i == 0) {
						// Il numero di giorni di scadenza vanno solo sullaprima rata
						rateizzazioneModel.setScadenzaGiorni(
								getRequestBigDecimalParameter(CAMPO_SCADENZA_GIORNI + "_" + i));
					}

					BigDecimal lImportoRata = null;
					if ((getRequestStringParameter(CAMPO_VALORE_RATA_I + "_" + i) != null
							&& !getRequestStringParameter(CAMPO_VALORE_RATA_I + "_" + i).equals(""))
							|| (getRequestStringParameter(CAMPO_VALORE_RATA_D + "_" + i) != null
									&& !getRequestStringParameter(CAMPO_VALORE_RATA_D + "_" + i)
											.equals(""))) {
						lImportoRata = new BigDecimal(getRequestStringParameter(CAMPO_VALORE_RATA_I + "_" + i)
								+ "." + getRequestStringParameter(CAMPO_VALORE_RATA_D + "_" + i));
					}

					rateizzazioneModel.setImportoDaPagare(lImportoDaPagare);
					rateizzazioneModel.setImportoRata(lImportoRata);

					rateizzazioneModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

					rateizzazioneModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					rateizzazioneModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					rateizzazioneModel.setDataInserimento(DateUtils.getSysDate());

					siesLogger.debug("rateizzazioneModel = " + rateizzazioneModel);

					aListaRate.add(rateizzazioneModel);
				}
			}
		}

		return aListaRate;
	}

}
package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria;
import siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria;
import siap.sius.penapecuniaria.model.RichiesteConversioniPerOrdinanzaModel;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActInserisciOrdinanzaConversioneRateizzazionePP - Classe Action per l'inserimento dell'Emissione di un
 * Ordinanaza, inserimento di SCAMBIO_SANZIONE e modifica delle RICHIESTA_CONVERSIONE afferenti
 *
 * @version 1.0
 */
public class ActInserisciOrdinanzaConversioneRateizzazionePP extends ActInserisciOrdinanzaUDS
		implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		return super.processRequest();
	}

	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {

		// Dati relativi alle Richieste Conversioni Pene Pecuniarie.
		RichiesteConversioniPerOrdinanzaModel aRicConModel = new RichiesteConversioniPerOrdinanzaModel();
		int lNumeroRichiesteCPP = 0;
		String lTipoRichiestaCPP = "";
		if (!isRequestParameterNullObj("numeroRichiesteCPP")) {
			lNumeroRichiesteCPP = this.getRequestIntParameter("numeroRichiesteCPP");
			// Lettura ID delle Richieste di Conversione.
			String[] lIdRichiestaConversione = new String[lNumeroRichiesteCPP];
			String[] lCodTipoRichiesta = new String[lNumeroRichiesteCPP];
			String[] lCodTipoSanzione = new String[lNumeroRichiesteCPP];
			lIdRichiestaConversione = this
					.getRequestStringParameters(ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE);

			// Si distingue tra i casi di Conversione e Rateizzazione.
			if (!isRequestParameterNullObj(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE))
				lTipoRichiestaCPP = this.getRequestStringParameter(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE);

			aRicConModel.setIdRichiestaConversione(lIdRichiestaConversione);

			// Ordinanza di Conversione Pena Pecuniaria.
			if (lTipoRichiestaCPP.compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_CONVERSIONE) == 0) {
				String[] lNumGiorniDurataEsito = new String[lNumeroRichiesteCPP];
				String[] lNumMesiDurataEsito = new String[lNumeroRichiesteCPP];
				String[] lNumAnniDurataEsito = new String[lNumeroRichiesteCPP];

				lNumGiorniDurataEsito = this
						.getRequestStringParameters(ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_SS);
				lNumMesiDurataEsito = this
						.getRequestStringParameters(ICostantiSiusPenaPecuniaria.CAMPO_NUM_MESI_SS);
				lNumAnniDurataEsito = this
						.getRequestStringParameters(ICostantiSiusPenaPecuniaria.CAMPO_NUM_ANNI_SS);
				for (int k = 0; k < lNumeroRichiesteCPP; k++) {
					lCodTipoSanzione[k] = this.getRequestStringParameter(
							ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE + k);
					lCodTipoRichiesta[k] = lTipoRichiestaCPP;
				}

				aRicConModel.setNumGiorniDurataEsito(lNumGiorniDurataEsito);
				aRicConModel.setNumMesiDurataEsito(lNumMesiDurataEsito);
				aRicConModel.setNumAnniDurataEsito(lNumAnniDurataEsito);
				aRicConModel.setCodTipoSanzione(lCodTipoSanzione);
				aRicConModel.setCodTipoRichiesta(lCodTipoRichiesta);
			}
			// Ordinanza di Rateizzazione Pena Pecuniaria.
			if (lTipoRichiestaCPP.compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_RATEIZZAZIONE) == 0) {
				String[] lNumeroRate = new String[lNumeroRichiesteCPP];
				String[] lValoreIntRata = new String[lNumeroRichiesteCPP];
				String[] lValoreDecRata = new String[lNumeroRichiesteCPP];
				BigDecimal[] lValoreRata = new BigDecimal[lNumeroRichiesteCPP];
				String[] lValoreIntUltRata = new String[lNumeroRichiesteCPP];
				String[] lValoreDecUltRata = new String[lNumeroRichiesteCPP];
				BigDecimal[] lValoreUltRata = new BigDecimal[lNumeroRichiesteCPP];
				Date lDataInizioPagamento = null;
				BigDecimal lNumGiorniInizioPagamento = null;

				lNumeroRate = this.getRequestStringParameters(ICostantiSiusPenaPecuniaria.CAMPO_NUMERO_RATE);
				lValoreIntRata = this
						.getRequestStringParameters(ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTO_MULTA);
				lValoreDecRata = this
						.getRequestStringParameters(ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTO_MULTA);
				lValoreIntUltRata = this.getRequestStringParameters(
						ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTOFINALE_MULTA);
				lValoreDecUltRata = this.getRequestStringParameters(
						ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTOFINALE_MULTA);

				lDataInizioPagamento = this.getRequestDateParameter(
						ICostantiSiusPenaPecuniaria.CAMPO_ANNO_DATA_TERMINE_PAG,
						ICostantiSiusPenaPecuniaria.CAMPO_MESE_DATA_TERMINE_PAG,
						ICostantiSiusPenaPecuniaria.CAMPO_GIORNO_DATA_TERMINE_PAG);
				lNumGiorniInizioPagamento = this.getRequestBigDecimalParameter(
						ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_PER_PAGAMENTO);

				for (int k = 0; k < lNumeroRichiesteCPP; k++) {
					if (lValoreIntRata[k] != null && lValoreIntRata[k].trim().length() > 0) {
						lValoreRata[k] = new BigDecimal(lValoreIntRata[k]);
						if (lValoreDecRata[k] != null && lValoreDecRata[k].trim().length() > 0)
							lValoreRata[k] = lValoreRata[k].add(new BigDecimal("." + lValoreDecRata[k]));
					} else if (lValoreDecRata[k] != null && lValoreDecRata[k].trim().length() > 0)
						lValoreRata[k] = new BigDecimal("." + lValoreDecRata[k]);

					if (lValoreIntUltRata[k] != null && lValoreIntUltRata[k].trim().length() > 0) {
						lValoreUltRata[k] = new BigDecimal(lValoreIntUltRata[k]);
						if (lValoreDecUltRata[k] != null && lValoreDecUltRata[k].trim().length() > 0)
							lValoreUltRata[k] = lValoreUltRata[k]
									.add(new BigDecimal("." + lValoreDecUltRata[k]));
					} else if (lValoreDecUltRata[k] != null && lValoreDecUltRata[k].trim().length() > 0)
						lValoreUltRata[k] = new BigDecimal("." + lValoreDecUltRata[k]);

					lCodTipoRichiesta[k] = lTipoRichiestaCPP;
					lCodTipoSanzione[k] = this
							.getRequestStringParameter(ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE);
				}
				aRicConModel.setNumeroRate(lNumeroRate);
				aRicConModel.setValoreRata(lValoreRata);
				aRicConModel.setValoreUltimaRata(lValoreUltRata);
				aRicConModel.setCodTipoRichiesta(lCodTipoRichiesta);
				aRicConModel.setDataInizioPagamento(lDataInizioPagamento);
				aRicConModel.setNumGiorniInizioPagamento(lNumGiorniInizioPagamento);
				aRicConModel.setCodTipoSanzione(lCodTipoSanzione);
			}
		}

		// MEV_2023-35: aggiunta gestione nuovo contenuto
		Vector<RateizzazionePPModel> aListaRate = new Vector<>();
		if (getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("U142")) {
			if (!isRequestParameterNullObj(ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I)
					&& ((getRequestStringParameter(ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I) != null
							&& !(getRequestStringParameter(ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I))
									.equals(""))
							|| (getRequestStringParameter(
									ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_D) != null
									&& !(getRequestStringParameter(
											ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_D)).equals("")))) {
				BigDecimal lImportoDaPagare = new BigDecimal(
						getRequestStringParameter(ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_I) + "."
								+ getRequestStringParameter(ICostantiRateizzazionePP.CAMPO_VALORE_IMPORTO_D));
				siesLogger.debug("Rateizzazione SIUS su più rate");
				int maxNumRate = ICostantiRateizzazionePP.NUM_MAX_RATE;
				for (int i = 0; i < maxNumRate; i++) {
					if (!isRequestParameterNullObj(ICostantiRateizzazionePP.CAMPO_NUM_RATE + "_" + i)) {
						siesLogger.debug("la riga (" + i + ") è abilitata, recupero i dati...");
						RateizzazionePPModel rateizzazioneModel = new RateizzazionePPModel();
						rateizzazioneModel
								.setTipoRateizzazione(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE);
						rateizzazioneModel.setNumeroRate(getRequestBigDecimalParameter(
								ICostantiRateizzazionePP.CAMPO_NUM_RATE + "_" + i));
						rateizzazioneModel.setProgressivoRata(new BigDecimal(i + 1));
						BigDecimal lImportoRata = null;
						if ((getRequestStringParameter(
								ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I + "_" + i) != null
								&& !getRequestStringParameter(
										ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I + "_" + i).equals(""))
								|| (getRequestStringParameter(
										ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D + "_" + i) != null
										&& !getRequestStringParameter(
												ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D + "_" + i)
														.equals(""))) {
							lImportoRata = new BigDecimal(getRequestStringParameter(
									ICostantiRateizzazionePP.CAMPO_VALORE_RATA_I + "_" + i) + "."
									+ getRequestStringParameter(
											ICostantiRateizzazionePP.CAMPO_VALORE_RATA_D + "_" + i));
						}
						rateizzazioneModel.setImportoDaPagare(lImportoDaPagare);
						rateizzazioneModel.setImportoRata(lImportoRata);
						rateizzazioneModel.setFasSieIdFascicoloSiep(
								mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
						rateizzazioneModel.setFasSiuIdFascicoloSius(
								mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
						rateizzazioneModel.setCodOperatoreInserimento(getCodUtenteConnesso());
						rateizzazioneModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						rateizzazioneModel.setDataInserimento(DateUtils.getSysDate());
						siesLogger.debug("rateizzazioneModel = " + rateizzazioneModel);
						aListaRate.add(rateizzazioneModel);
					}
				}
			}
		}

		OrdinanzaEventoTenoriGProcModel lModRet = null;
		// inserimento
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		// MEV_2023-35: aggiunto parametro di passaggio
		lModRet = IDepOrdCtrl.ExInserisciOrdinanzaConversioneRateizzazionePP(aOrdEveTenGP, aRicConModel,
				aListaRate);
		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO.");
		return lModRet;
	}

}
package siap.sius.depositoordinanzapc.action;

import java.util.Date;

import f3b.util.F3BException;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * 20260512-MEV_2026-124_SG: ordinanza di Revoca Pena Sostitutiva conseguente alla Conversione Pena Pecuniaria
 * per avvenuto pagamento (art. 660 c. 15 c.p.p.). Registra la data di avvenuto pagamento e la Data Revoca
 * (data emissione) nell'elenco dei periodi di esecuzione della pena sostitutiva.
 */
public class ActInserisciOrdinanzaRevocaPagamentoPPS extends ActInserisciOrdinanzaUDS {

	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {

		String lG = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_PAGAMENTO_PP);
		String lM = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_PAGAMENTO_PP);
		String lA = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PAGAMENTO_PP);
		Date lDataPagamento = null;
		if (lG != null && lM != null && lA != null && lG.trim().length() > 0 && lM.trim().length() > 0
				&& lA.trim().length() > 0) {
			lDataPagamento = getRequestDateParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PAGAMENTO_PP,
					ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_PAGAMENTO_PP,
					ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_PAGAMENTO_PP);
		}
		if (lDataPagamento == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! Indicare la data di avvenuto pagamento della pena pecuniaria.");
		if (lDataPagamento.after(new Date()))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! La data di avvenuto pagamento non puo' essere successiva alla data odierna.");

		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		OrdinanzaEventoTenoriGProcModel lModRet = lCtrl.ExInserisciOrdinanzaRevocaPS(aOrdEveTenGP, null,
				aOrdEveTenGP.getEvento().getDataEmissione(), lDataPagamento);
		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO.");
		return lModRet;
	}
}

package siap.sius.depositodecreto.action;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActCancellaDepositoDecreto - Classe Action per cancellare il decreto depositato
 *
 * @version 1.0
 */
public class ActCancellaDepositoDecreto extends ActionSiap implements ICostantiDepositoDecreto {

	public String processRequest() throws Exception {

		// chiama il controller
		IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();

		// ricerca preventiva del record da cancellare
		DepositoDecretoModel lDepDec = null;
		lDepDec = lCtrl
				.ExRicercaDepositoDecretoByKey(getRequestBigDecimalParameter(CAMPO_ID_DEPOSITO_DECRETO));
		if (lDepDec == null)
			throw new SIUSException("record DepositoDecreto inesistente");

		// Nel Model del DepositoDecreto vengono valorizzati i dati necessari agli aggiornamenti
		lDepDec.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lDepDec.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lDepDec.setDataAggiornamento(DateUtils.getSysDate());

		// Cancellazione
		lCtrl.ExCancellaDepositoDecreto(lDepDec);
		String retPage = null;
		String nextAct = null;

		if (!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE)) {
			nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
			if (!isRequestParameterNullObj("noQuery")) {
				nextAct += "&noQuery=";
				nextAct += "OK";
			}
		}
		retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", nextAct);
		return retPage;
	}

}
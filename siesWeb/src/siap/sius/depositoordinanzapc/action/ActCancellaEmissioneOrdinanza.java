package siap.sius.depositoordinanzapc.action;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActCancellaEmissioneOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per cancellare l'emissione di un'ordinanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActCancellaEmissioneOrdinanza extends ActionSiap implements ICostantiDepositoOrdinanzaPc {

	public String processRequest() throws Exception {

		// chiama il controller
		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();

		// ricerca del record da cancellare
		DepositoOrdinanzaPcModel lDepOrd = null;
		lDepOrd = lCtrl
				.ExRicercaDepositoOrdinanzaPcByEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		if (lDepOrd == null)
			throw new F3BException("record DepositoOrdinanzaPc inesistente");

		// Nel Model del DepositoDecreto vengono valorizzati i dati necessari agli aggiornamenti
		lDepOrd.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lDepOrd.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lDepOrd.setDataAggiornamento(DateUtils.getSysDate());

		// Cancellazione
		lCtrl.ExCancellaDepositoOrdinanza(lDepOrd);

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
package siap.sius.depositodecreto.action;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActCancellaDepositoDecreto
 * </p>
 * <p>
 * Description: Classe Action per cancellare il decreto depositato
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

		/*
		 * ISSUE MEV : aggiunto aggiornamento stato fascicolo per decreto di tipo DM
		 * Numero MEV : 9
		 * Autore : Gioggi
		 * Data : 19 nov 2020
		 * Branch : MEV_2019-09
		 */
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = ie.ExRicercaEventoByKey(lDepDec.getIdEventoGenerato());
		// ***** FINE INTERVENTO MEV_2019-09 *****//

		// Nel Model del DepositoDecreto vengono valorizzati i dati necessari agli aggiornamenti
		lDepDec.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lDepDec.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lDepDec.setDataAggiornamento(DateUtils.getSysDate());

		// Cancellazione
		lCtrl.ExCancellaDepositoDecreto(lDepDec);

		/*
		 * ISSUE MEV : aggiunto aggiornamento stato fascicolo per decreto di tipo DM
		 * Numero MEV : 9
		 * Autore : Gioggi
		 * Data : 19 nov 2020
		 * Branch : MEV_2019-09
		 */
		if ("0610".equals(em.getCodEsito()) || "0271".equals(em.getCodEsito())) {
			IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloSiusModel fsm = new FascicoloSiusModel();
			fsm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			fsm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			fsm.setDataAggiornamento(DateUtils.getSysDate());
			fsm.setCodStatoFascicolo(ICostantiFascicoloSius.COD_ISCRITTO);
			fsm.setIdFascicoloSius(em.getFasSiuIdFascicoloSius());
			ifs.aggiornaStatoFascicoloSius(fsm);
		}
		// ***** FINE INTERVENTO MEV_2019-09 *****//

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
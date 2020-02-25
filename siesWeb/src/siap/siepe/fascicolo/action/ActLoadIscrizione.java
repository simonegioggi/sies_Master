package siap.siepe.fascicolo.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaAttiVistiPerSoggetto
 * </p>
 * <p>
 * Description: Azione di caricamento della form di ricerca degli atti ricevuti e Presi in visione per
 * Soggetto
 * </p>
 * <p>
 * Copyright: Bull Italia Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia
 * </p>
 */
public class ActLoadIscrizione extends ActionSiap implements ICostantiFascicoloSiepe {

	public String processRequest() throws Exception {

		setLinkRitorno();

		if (isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO))
			throw (new F3BException(F3BException.USER_MESSAGE, "Dati del Messaggio Assenti !"));

		// Lettura ID Messaggio Ricevuto
		BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggio = lCrtl.ExRicercaMessaggioByKey(lIdMess);
		setRequestAttribute("messaggio", lMessaggio);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoIncaricoSiepe());
		setRequestAttribute("incarichi", "" + lOption);

		return PG_LOAD_INSERISCIFASCICOLOSIEPE; // restituisce la jsp di VIEW
	}

}
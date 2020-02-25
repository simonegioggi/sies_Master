package siap.sius.depositodecreto.action;

import java.util.Vector;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActListaDecretiRicevuti extends ActionSiap
		implements ICostantiDepositoDecreto, ICostantiMessaggio {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		// DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
		// tabella DI MESSAGGIO.
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector lVect = lCrtl.ExRicercaMessaggioRichiestaPerUfficio(getCodUfficioUtenteConnesso(),
				ICostantiJMS.TRASFERIMENTO_DECRETO);

		this.setRequestAttribute("Messaggi", lVect);

		return PG_LISTA_MESSAGGI_RICEVUTI;
	}

}
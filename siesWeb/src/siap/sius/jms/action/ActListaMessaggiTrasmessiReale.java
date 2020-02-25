package siap.sius.jms.action;

import java.util.Vector;

import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

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
public class ActListaMessaggiTrasmessiReale extends ActionSiap implements ICostantiMessaggio {

	@SuppressWarnings({ "rawtypes" })
	public String processRequest() throws Exception {

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null
				&& JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim()
						.equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		Vector lVect = null;

		try {
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			lVect = lCrtl.ExRicercaMessaggioEsitoPerUfficio(this.getCodUfficioUtenteConnesso());
		} catch (F3BException ex) {
		}

		this.setRequestAttribute("Messaggi", lVect);

		return PG_LISTA_MESSAGGI_SPEDITI_SIUS;
	}

}
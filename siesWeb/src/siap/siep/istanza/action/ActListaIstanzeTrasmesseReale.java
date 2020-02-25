package siap.siep.istanza.action;

import java.util.Vector;

import f3b.util.F3BException;
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
public class ActListaIstanzeTrasmesseReale extends ActionSiap
		implements ICostantiIstanza, ICostantiMessaggio {

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
		Vector lVect = null;

		try {
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			// STUB 01/10/2004 Ricerca di sole istanze (Overloading metodo ExRicercaMessaggioEsitoPerUfficio)
			lVect = lCrtl.ExRicercaMessaggioEsitoPerUfficio(this.getCodUfficioUtenteConnesso(),
					ICostantiJMS.TRASFERIMENTO_ISTANZA);
		} catch (F3BException ex) {

		}

		this.setRequestAttribute("Messaggi", lVect);

		return PG_LISTA_MESSAGGI_SPEDITI;
	}

}
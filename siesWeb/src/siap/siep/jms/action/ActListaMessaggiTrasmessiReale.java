package siap.siep.jms.action;

import java.util.Vector;

import f3b.util.F3BException;
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
public class ActListaMessaggiTrasmessiReale extends ActionSiap
		implements ICostantiSiepJMS, ICostantiMessaggio {
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

		// Questa classe Controlla i dati sulla tabella MESSAGGIO.
		// Estrae i messaggi con tipo Operazione specifico (se il parametro viene passato); con parametro =
		// null, effettua l'estrazione generica.
		Vector lVect = null;

		try {
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			if (!isRequestParameterNullObj("tipoOperazione")) {
				this.setRequestAttribute("tipoOperazione",
						getRequestStringParameter("tipoOperazione").toString());
				// STUB 01/10/2004 Ricerca di Messaggi Esito per il Tipo Operazione scelto (Overloading metodo
				// ExRicercaMessaggioEsitoPerUfficio)
				lVect = lCrtl.ExRicercaMessaggioEsitoPerUfficio(this.getCodUfficioUtenteConnesso(),
						getRequestStringParameter("tipoOperazione").toString());
			} else
				lVect = lCrtl.ExRicercaMessaggioEsitoPerUfficio(this.getCodUfficioUtenteConnesso());
		} catch (F3BException ex) {

		}

		this.setRequestAttribute("Messaggi", lVect);

		return PG_LISTA_MESSAGGI_SPEDITI;
	}
}

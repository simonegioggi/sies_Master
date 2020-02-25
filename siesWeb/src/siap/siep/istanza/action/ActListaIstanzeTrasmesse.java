package siap.siep.istanza.action;

import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;

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
public class ActListaIstanzeTrasmesse extends ActionSiap implements ICostantiIstanza {

	public String processRequest() throws Exception {

		/*
		 * SIAPReceiver.getInstance();
		 * 
		 * //DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla // tabella DI MESSAGGIO.
		 * 
		 * IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote(); Vector lVect =
		 * lCrtl.ExRicercaMessaggioEsitoPerUfficio(this.getCodUfficioUtenteConnesso());
		 * 
		 * this.setRequestAttribute("Messaggi", lVect);
		 */
		// return PG_ATTESA;

//		String lPage = "";

		return /*lPage = */"/jsp/MainAttesa.jsp?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.istanza.action.ActListaIstanzeTrasmesseReale";

		// --- GDV return PG_LISTA_MESSAGGI_SPEDITI_RICEVUTI;
	}

}
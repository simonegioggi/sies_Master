package siap.siepe.ricezioneatti.action;

import java.util.Vector;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActRicercaCruscotto
 * </p>
 * <p>
 * Description: Questa classe Azione effettua la ricerca dei dati aggregati nel cosiddetto "Cruscotto " di
 * SIEPE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActRicercaCruscotto extends ActionSiap implements ICostantiRicezioneAtti {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		String lOrdineDate = "DESC";

		// Attivazione punto di Ritorno
		setLinkRitorno();
		/*
		 * String lPagina = "1"; if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) lPagina =
		 * getRequestStringParameter(IWebConstants.NUM_PAGE);
		 */
		// SIAPReceiver.getInstance();

		// Lettura ordine data_invio
		if (!isRequestParameterNullObj("OrdineDate")) {
			lOrdineDate = getRequestStringParameter("OrdineDate");
			// Inversione dell'ordine di visualizzazione delle date
			if (lOrdineDate.equalsIgnoreCase("desc"))
				lOrdineDate = "ASC";
			else
				lOrdineDate = "DESC";
		}

		setRequestAttribute("OrdineDate", lOrdineDate);
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector lVect = lCrtl.ExRicercaContatoriXCruscotto(getCodUfficioUtenteConnesso(), lOrdineDate);
		this.setRequestAttribute("lista", lVect);
		return PG_CRUSCOTTO;
	}

}
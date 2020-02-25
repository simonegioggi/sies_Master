package siap.siep.jms.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.jms.controller.IEsitoRicercaJMS;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActListaEsitiRicerca
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
 * @author
 * @version 1.0
 * @deprecated non più utilizzata
 */
public class ActListaEsitiRicerca extends ActionSiap implements ICostantiSiepJMS {

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		this.setLinkRitorno(); // STUB 04/03/2005

		// Ricerca Richieste ed Esiti Ricerca
		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Vector lVect = null;

		// IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		IEsitoRicercaJMS lEsiCntrl = SIEPLookupRemote.getEsitoRicercaJMS();

		BigDecimal CountRisultati = new BigDecimal(0);
		if (isRequestParameterNullObj("CountRisultati"))
			CountRisultati = lEsiCntrl.ExGetCountEsitoRicercaPerUfficio(this.getCodUfficioUtenteConnesso(),
					RICERCA_FASCICOLO_PER_TRASFERIMENTO);
		else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		lVect = lEsiCntrl.ExRicercaMessaggioEsitoRicercaPerUfficio(this.getCodUfficioUtenteConnesso(),
				RICERCA_FASCICOLO_PER_TRASFERIMENTO, Integer.parseInt(lPagina));

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("Messaggi", lVect);
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		setRequestAttribute("descrComune", lUtenteConnesso.getUfficioUtente().getDescrComune());

		return PG_LISTA_MESSAGGI_RICERCA_SPEDITI; // restituisce la jsp di VIEW
	}

}
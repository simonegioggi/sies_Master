package siap.siep.nuovaistanza.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;

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
public class ActListaNuoveIstanzeRicevute extends ActionSiap
		implements ICostantiNuovaIstanza, ICostantiMessaggio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

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
		Vector lVect = lCrtl.ExRicercaMessaggiRichiestaPerUfficio(getCodUfficioUtenteConnesso(),
				ICostantiJMS.TRASFERIMENTO_ORDINANZA);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("vettore messaggi " + lVect.size());
		if (!this.isRequestParameterNullObj("FlagConfermaPresaInCarico")) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("flaconferma " + this.getRequestStringParameter("FlagConfermaPresaInCarico"));
		}

		if (lVect.size() == 0 && this.isRequestParameterNullObj("FlagConfermaPresaInCarico")) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio in Arrivo");
		} else if (lVect.size() == 0 && !this.isRequestParameterNullObj("FlagConfermaPresaInCarico")
				&& this.getRequestStringParameter("FlagConfermaPresaInCarico") != null
				&& this.getRequestStringParameter("FlagConfermaPresaInCarico").equals("s")) {
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) this.getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		}

		this.setRequestAttribute("Messaggi", lVect);

		// return PG_LISTA_MESSAGGI_RICEVUTI;
		return IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiNuoveIstanzeRicevuti.jsp";
	}
}
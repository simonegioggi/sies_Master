package siap.sius.presaincarico.action;

import java.util.Vector;

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
public class ActListaAttiSiepRicevuti extends ActionSiap
		implements ICostantiPresaincarico, ICostantiMessaggio {
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

		// Questa classe controlla i MESSAGGI ricevuti dall'ufficio connesso, dall'ufficio selezionato.

		// Si Verifica se l'ufficio emittente sia stato correttamente impostato.
		String lCodice = this.getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO),
				getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO));

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();

		// STUB 15/03/2005 Aggiunta ricerca degli atti gia presi in carico.
		String lIncludeInCarico = "";
		if (isRequestChecked(ICostantiPresaincarico.CAMPO_INCLUDE_INCARICO))
			lIncludeInCarico = "S";

		// STUB 19/07/2004 Aggiunta ricerca per singolo fascicolo.
		Vector lVect = new Vector();
		// STUB 01/10/2004 Controllo della validità dei dati spostato in MessaggioSqlDAO.
		// Inoltre si utilizza la convenzione per cui il TipoOperazione viene filtrato solo in caso di valore
		// utile (Es. "00003").
		lVect = lCrtl.ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(
				this.getCodUfficioUtenteConnesso(), lCodice, "SIEP",
				getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP),
				getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP), lIncludeInCarico);

		this.setLinkRitorno();

		setRequestAttribute("Messaggi", lVect);
		setRequestAttribute("codUfficio", lCodice); // STUB 15/03/2005
		setRequestAttribute("descrUfficio", getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO) + " "
				+ getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)); // STUB 15/03/2005
		setRequestAttribute("annoSiep", getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIEP)); // STUB
																								// 15/03/2005
		setRequestAttribute("progrSiep", getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIEP)); // STUB
																									// 15/03/2005
		setRequestAttribute("flagIncludeInCarico", lIncludeInCarico); // STUB 15/03/2005

		return PG_LISTA_MESSAGGI_RICEVUTI;
		// return PG_LISTA_ATTISIEP_RICEVUTI;
	}

}
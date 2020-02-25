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
public class ActListaAttiSiepeRicevuti extends ActionSiap
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

		// Ricerca degli atti gia presi in carico.
		String lIncludeInCarico = "";
		if (isRequestChecked(ICostantiPresaincarico.CAMPO_INCLUDE_INCARICO))
			lIncludeInCarico = "S";

		// Ricerca per singolo fascicolo.
		Vector lVect = new Vector();
		// Controllo della validità dei dati spostato in MessaggioSqlDAO.
		// Inoltre si utilizza la convenzione per cui il TipoOperazione viene filtrato solo in caso di valore
		// utile (Es. "00003").
		lVect = lCrtl.ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(
				this.getCodUfficioUtenteConnesso(), lCodice, "SIEP",
				getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEPE),
				getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEPE), lIncludeInCarico);

		this.setLinkRitorno();

		setRequestAttribute("Messaggi", lVect);
		setRequestAttribute("codUfficio", lCodice);
		setRequestAttribute("descrUfficio", getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO) + " "
				+ getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO));
		setRequestAttribute("annoSiepe", getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIEPE));
		setRequestAttribute("progrSiepe", getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIEPE));
		setRequestAttribute("flagIncludeInCarico", lIncludeInCarico);

		return PG_LISTA_MESSAGGI_RICEVUTI_PERDATE;
	}

}
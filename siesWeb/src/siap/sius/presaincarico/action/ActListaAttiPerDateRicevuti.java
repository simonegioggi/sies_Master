package siap.sius.presaincarico.action;

import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
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
public class ActListaAttiPerDateRicevuti extends ActionSiap
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
		String lCodice = "-";
		if (!(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO).equalsIgnoreCase("-"))
				&& !(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).equalsIgnoreCase("")))
			lCodice = this.getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO));

		Date lDataInizioTrasmissioneAtti = getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_ATTI,
				CAMPO_MESE_DATA_RICEZIONE_ATTI, CAMPO_GIORNO_DATA_RICEZIONE_ATTI);
		// Se la data inizio non viene impostata la valorizzo con 01/01/1900.
		// 05/03/2008 Data inizio Ricerca impostata al mese precedente.
		if (lDataInizioTrasmissioneAtti == null)
			// lDataInizioTrasmissioneAtti = DateUtils.getDate(1900,01,01);
			lDataInizioTrasmissioneAtti = DateUtils.getMonthBefore(DateUtils.getSysDate());

		Date lDataFineTrasmissioneAtti = getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);
		// Se la data fine non viene impostata la valorizzo con quella odierna.
		if (lDataFineTrasmissioneAtti == null)
			lDataFineTrasmissioneAtti = DateUtils.getSysDate();

		// STUB 15/03/2005 Aggiunta ricerca degli atti gia presi in carico.
		String lIncludeInCarico = "";
		if (isRequestChecked(ICostantiPresaincarico.CAMPO_INCLUDE_INCARICO))
			lIncludeInCarico = "S";

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector lVect = lCrtl.ExRicercaMessaggioRichiestaPerDateUffici(this.getCodUfficioUtenteConnesso(),
				lCodice, lDataInizioTrasmissioneAtti, lDataFineTrasmissioneAtti, lIncludeInCarico);

		this.setLinkRitorno();

		setRequestAttribute("DataInizioTrasmissioneAtti",
				DateUtils.getDateToString(lDataInizioTrasmissioneAtti, "dd/MM/yyyy"));
		setRequestAttribute("DataFineTrasmissioneAtti",
				DateUtils.getDateToString(lDataFineTrasmissioneAtti, "dd/MM/yyyy"));
		setRequestAttribute("Messaggi", lVect);
		setRequestAttribute("codUfficio", lCodice); // STUB 15/03/2005
		setRequestAttribute("descrUfficio", getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO) + " "
				+ getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)); // STUB 15/03/2005
		setRequestAttribute("flagIncludeInCarico", lIncludeInCarico); // STUB 15/03/2005

		return PG_LISTA_MESSAGGI_RICEVUTI_PERDATE;
	}

}
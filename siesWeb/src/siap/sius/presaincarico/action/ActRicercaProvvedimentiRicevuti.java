package siap.sius.presaincarico.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Classe richiamata da più funzioni di ricerca atti ricevuti
 * </p>
 * siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaPresiCarico
 * siap.sius.presaincarico.action.ActLoadRicercaDecretiRicevuti.java
 * siap.sius.presaincarico.action.ActLoadRicercaOrdinanzeRicevute.java
 *
 * Ovvero lato SIEP: Presa in carico Provedimento - Atti Ricevuti x Competenza - Elenco Atti Presi in Carico -
 * Ordinanza - Decreto siap.siep.sanzionesostitutiva.action.ActLoadElencoAttiPresiIncarico.java NO!!
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaProvvedimentiRicevuti extends ActionSiap implements ICostantiPresaincarico {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua la ricerca degli atti (Messaggi) secondo i parametri recuperati dalla request:
	 * MESSAGGIO.COD_TIPO_OPERAZIONE MESSAGGIO.COD_UFFICIO_MITTENTE
	 * 
	 * MESSAGGIO.COD_ESITO MESSAGGIO.DATA_INVIO
	 * 
	 * MESSAGGIO.CHIAVE_ANNO_SIEP MESSAGGIO.CHIAVE_PROGR_SIEP
	 * 
	 * MESSAGGIO.CHIAVE_ANNO_SIUS MESSAGGIO.CHIAVE_PROGR_SIUS
	 * 
	 */
	public String processRequest() throws Exception {

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		siesLogger.debug("INIZIO");

		this.setLinkRitorno();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		MessaggioModel lMessaggio = new MessaggioModel();
		lMessaggio.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());
		lMessaggio.setCodTipoMessaggio("01"); // Richiesta
		lMessaggio.setCodTipoOperazione(getRequestStringParameter(ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE));

		if (!isRequestChecked(CAMPO_INCLUDE_INCARICO))
			lMessaggio.setFlagVisto("N");
		else
			setRequestAttribute("flagIncludeInCarico", "S");

		//
		if (getRequestStringParameter(ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE)
				.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_COMPETENZA)) {
			lMessaggio.setFlagVisto("S");
		}

		// Filtro sull'ufficio mittente
		if (getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO).length() > 0
				&& getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).length() > 0) {
			String lCodUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO),
					getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO));
			lMessaggio.setCodUfficioMittente(lCodUffEmittente);
			setRequestAttribute("codUfficio", lCodUffEmittente);
			setRequestAttribute("descrUfficio", getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO) + " "
					+ getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO)); // STUB 15/03/2005
		}

		// Filtro sul Fascicolo SIEP
		if (getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIEP).trim().length() > 0
				&& getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIEP).trim().length() > 0) {
			lMessaggio.setChiaveAnnoSiep(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP));
			lMessaggio.setChiaveProgrSiep(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP));
			setRequestAttribute("annoSiep", getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIEP));
			setRequestAttribute("progrSiep", getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIEP));
		}

		// Filtro sul Fascicolo SIUS
		if (getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIUS).trim().length() > 0
				&& getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIUS).trim().length() > 0) {
			lMessaggio.setChiaveAnnoSius(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIUS));
			lMessaggio.setChiaveProgrSius(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIUS));
			setRequestAttribute("annoSius", getRequestStringParameter(CAMPO_ANNO_FASCICOLO_SIUS));
			setRequestAttribute("progrSius", getRequestStringParameter(CAMPO_PROGR_FASCICOLO_SIUS));
		}

		// Filtro aggiunto x Atti Presi in carico
		if (!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_COD_ESITO)
				&& !"-".equals(getRequestStringParameter(ICostantiMessaggio.CAMPO_COD_ESITO))) {
			String lEsito = getRequestStringParameter(ICostantiMessaggio.CAMPO_COD_ESITO);
			lMessaggio.setCodEsito(lEsito); // Preso in carico

			setRequestAttribute(ICostantiMessaggio.CAMPO_COD_ESITO, lEsito);
		}

		// Filtro sulla data di trasmissione
		Date lDataInizio = getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_ATTI,
				CAMPO_MESE_DATA_RICEZIONE_ATTI, CAMPO_GIORNO_DATA_RICEZIONE_ATTI);
		Date lDataFine = getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

		// Ricerca Messaggi
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();

		Vector lVect = new Vector();
		if ((getRequestStringParameter(ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE)
				.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_COMPETENZA))
				&& (!isRequestParameterNullObj("stato_fascicoli")
						&& getRequestStringParameter("stato_fascicoli").equalsIgnoreCase("presincarico"))) { // Per
																												// 00066
																												// Presincarico
																												// Effettuo
																												// dopo
																												// la
																												// ricerca
																												// paginata
			siesLogger.debug("--XX--  >>>>>>>>>>>>>>>>>>  NON faccio la Ricerca semplice");
		} else {
			lVect = lCrtl.ExRicercaMessaggio(lMessaggio);
		}

		// 05/03/2008 Se la Data inizio non viene valorizzata si imposta al mese precedente.
		if (lDataInizio == null)
			lDataInizio = DateUtils.getMonthBefore(DateUtils.getSysDate());
		// 05/03/2008 Se la data fine non viene valorizzata si imposta con quella odierna.
		if (lDataFine == null)
			lDataFine = DateUtils.getSysDate();

		if (lDataInizio != null && lDataFine != null) {
			lVect = filtroPerData(lVect, lDataInizio, lDataFine);
			setRequestAttribute("data1", DateUtils.getDateToString(lDataInizio, "dd-MM-yyyy"));
			setRequestAttribute("data2", DateUtils.getDateToString(lDataFine, "dd-MM-yyyy"));
		}

		setRequestAttribute("Messaggi", lVect);
		// se sto ricercando i messaggi presi in carico dopo la trasmissione per competenza reindirizzo alla
		// pagina specifica
		// 10-07-2017 - N.B. : In realtà Non c'è nessun reindirizzamento; Dopo l'elenco, la funzione chiude.
		if (getRequestStringParameter(ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE)
				.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_COMPETENZA)) {
			if (!isRequestParameterNullObj("stato_fascicoli")
					&& getRequestStringParameter("stato_fascicoli").equalsIgnoreCase("presincarico")) {
				// ================================================
				BigDecimal lCountRisultati = null;
				if (isRequestParameterNullObj("CountRisultati")) {
					lCountRisultati = lCrtl.ExCountMessaggiNelPeriodoPaged(lMessaggio, lDataInizio, lDataFine,
							0);
				} else {
					lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
				}

				// siesLogger.debug("--XX-- >>>>>>>>>>>>>>>>>>>>> nuova Ricerca Paginata - Query estrazione
				// Records - Messaggio di input = "+lMessaggio);
				Vector lVectData = lCrtl.ExRicercaMessaggiNelPeriodoPaged(lMessaggio, lDataInizio, lDataFine,
						Integer.parseInt(lPagina));

				setRequestAttribute("Messaggi", lVectData);

				setRequestAttribute("CountRisultati", lCountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				// ===============================================================================

				setRequestAttribute(ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE,
						"siap.sius.presaincarico.action.ActRitrasmissioneAttiCompetenza");
				return ICostantiMessaggio.PG_LISTA_MESSAGGI_INCARICO_TRASMISSIONE_COMPETENZA;
			} else {
				return ICostantiMessaggio.PG_LISTA_MESSAGGI_RICEVUTI_TRASMISSIONE_COMPETENZA;
			}
		}

		return ICostantiMessaggio.PG_LISTA_MESSAGGI_RICEVUTI_SIUS;

	}

	private Vector filtroPerData(Vector aVect, Date aDataIniziale, Date aDataFinale) {

		Vector lRectVect = new Vector();
		MessaggioModel lMessCorrente;
		Date lDataCorr = null;

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			lMessCorrente = (MessaggioModel) itx.next();
			lDataCorr = lMessCorrente.getDataInvio();
			if (!aDataIniziale.after(lDataCorr) && !aDataFinale.before(lDataCorr))
				lRectVect.add(lMessCorrente);
		}
		return lRectVect;
	}

}
package siap.siepe.attivita.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActChiusuraAttivita
 * </p>
 * <p>
 * Description: Classe Action per la chiusura dell'Attivita
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActChiusuraAttivita extends ActionSiap implements ICostantiAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Chiusura del Attivita
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		AttivitaModel lAttMod = new AttivitaModel();

		// Lettura dati da modificare
		lAttMod.setDataChiusura(getRequestDateParameter(CAMPO_ANNO_DATA_CHIUSURA, CAMPO_MESE_DATA_CHIUSURA,
				CAMPO_GIORNO_DATA_CHIUSURA));
		lAttMod.setCodEsitoAttivita(getRequestStringParameter(CAMPO_COD_ESITO));
		lAttMod.setNotaChiusura(getRequestStringParameter(CAMPO_NOTA_CHIUSURA));
		// Lettura ID
		lAttMod.setIdAttivita(getRequestBigDecimalParameter(CAMPO_ID_ATTIVITA));
		// Imposta i dati relativi all'aggiornamento
		lAttMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAttMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAttMod.setDataAggiornamento(DateUtils.getSysDate());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" Attività da chiudere ->" + lAttMod);
		// Chiusura
		IAttivita lCtrl = SIEPELookupRemote.getAttivitaRemote();
		lCtrl.ExChiusuraAttivita(lAttMod);
		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siepe.attivita.action.ActLoadDettaglioAttivita&" + CAMPO_ID_ATTIVITA + "="
				+ lAttMod.getIdAttivita().toString();

		return lPage;
	}

}
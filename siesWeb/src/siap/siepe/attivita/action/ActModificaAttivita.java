package siap.siepe.attivita.action;

import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.action.ICostantiAssistenteSociale;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaAttivita
 * </p>
 * <p>
 * Description: Classe Action per la modifica dei dati dell'Attivita
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
public class ActModificaAttivita extends ActionSiap implements ICostantiAttivita {

	/**
	 * Azione di Modifica del Attivita
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		AttivitaModel lAttMod = new AttivitaModel();

		// Lettura dati da modificare
		lAttMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
				CAMPO_GIORNO_DATA_INIZIO));
		// lAttMod.setDataChiusura( getRequestDateParameter(
		// CAMPO_ANNO_DATA_CHIUSURA,CAMPO_MESE_DATA_CHIUSURA,CAMPO_GIORNO_DATA_CHIUSURA) );
		lAttMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lAttMod.setAssSocIdAssSociale(getRequestBigDecimalParameter(ICostantiAssistenteSociale.CAMPO_ID_ASSISTENTE_SOCIALE));
		// Lettura ID
		lAttMod.setIdAttivita(getRequestBigDecimalParameter(CAMPO_ID_ATTIVITA));
		// Imposta i dati relativi all'aggiornamento
		lAttMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAttMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAttMod.setDataAggiornamento(DateUtils.getSysDate());

		// Modifica
		IAttivita lCtrl = SIEPELookupRemote.getAttivitaRemote();
		lCtrl.ExModificaAttivita(lAttMod);
		// Prepara la pagina di destinazione
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siepe.attivita.action.ActLoadDettaglioAttivita&" + CAMPO_ID_ATTIVITA + "="
				+ lAttMod.getIdAttivita().toString();
	}

}
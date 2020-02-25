package siap.sige.collegio.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.web.ActionSiap;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadRicercaCollegio
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca della Collegio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadRicercaCollegio extends ActionSiap implements ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Carica la form per la ricerca di un Collegio.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione.
	 *         <p>
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		Option lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()),
				Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoSezioni", "" + lOption);

		// intervento per 11.2.1 (eliminare la combo dei numeri collegi ed aggiungere la combo dei magistrati)
		// Crea Lista Elenco Magistrati per ruolo di giudice.
		IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(getCodUfficioUtenteConnesso()));
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoGiudici", "" + lOption);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_LOAD_RICERCACOLLEGIO; // restituisce la jsp di VIEW
	}

}
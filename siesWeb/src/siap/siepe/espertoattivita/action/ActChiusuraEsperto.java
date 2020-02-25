package siap.siepe.espertoattivita.action;

/**
* <p>Title: ActInserisciEspertoAttivita</p>
* <p>Description: Classe Action per l'inserimento di EspertoAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.espertoattivita.controller.IEspertoAttivita;
import siap.siepe.espertoattivita.model.EspertoAttivitaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActChiusuraEsperto extends ActionSiap implements ICostantiEspertoAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Chiusura del EspertoAttivita. Per l'esperto selezionato viene valorizzata la data di fine.
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Valorizzazione del Model
		EspertoAttivitaModel lEspMod = new EspertoAttivitaModel();
		lEspMod.setDataFine(
				getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));
		lEspMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEspMod.setCodOperatoreAggiornamento(this.getCodUfficioUtenteConnesso());
		lEspMod.setDataAggiornamento(DateUtils.getSysDate());
		lEspMod.setEspIdEsperto(getRequestBigDecimalParameter(CAMPO_ESP_ID_ESPERTO));
		lEspMod.setAttIdAttivita(getRequestBigDecimalParameter(CAMPO_ATT_ID_ATTIVITA));

		// Attivazione dell'operazione attraverso il Controller
		IEspertoAttivita lCtrl = SIEPELookupRemote.getEspertoAttivitaRemote();
		/* EspertoAttivitaModel llEspModRet = */lCtrl.ExChiudiEspertoAttivita(lEspMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return ritornoDopoCancellazione("Chiusura Esperto effettuata", null);
	}

}
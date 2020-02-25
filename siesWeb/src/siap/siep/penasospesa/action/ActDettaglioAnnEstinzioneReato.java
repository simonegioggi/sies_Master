package siap.siep.penasospesa.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActDettaglioAnnEstinzioneReato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * @author Luigi
 * </p>
 * 
 * @version 1.0
 */
public class ActDettaglioAnnEstinzioneReato extends ActDettaglioAnnotazioneTermini {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActDettaglioAnnEstinzioneReato: inizio");

		super.processRequest();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActDettaglioAnnEstinzioneReato: fine");

		return PG_DETTAGLIO_ANN_ESTINZIONE_REATO;
	}

}
package siap.siep.sentenzariunita.action;

/**
 * <p>Title: ActCancellaSentenzaRiunita</p>
 * <p>Description: Classe Action per la cancellazione della Sentenza Riunita</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eunics</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActCancellaSentenzaRiunita extends ActionSiap implements ICostantiSentenzaRiunita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		String lId = getRequestStringParameter(CAMPO_ID_SENTENZA_RIUNITA);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActCancellaSentenzaRiunita - ID SENTENZA RIUNITA = " + lId);

		// istanzia il Model
		SentenzaRiunitaModel lSenMod = new SentenzaRiunitaModel();

		// Riempie il model
		lSenMod.setIdSentenzaRiunita(new BigDecimal(lId));

		// chiama il controller
		ISentenzaRiunita lCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();
		// Elimina la Sentenza Riunita
		lCtrl.ExCancellaSentenzaRiunita(lSenMod);

		// trova l'ID della Sentenza
		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));

		// esegue la ricerca delle Sentenze Riunite per controllare quante sono
		SentenzaRiunitaModel lSenRiuMod = new SentenzaRiunitaModel();
		lSenRiuMod.setSenIdSentenza(lSen.getIdSentenza());
		Vector lVect = new Vector();
		// Gestisce l'eccezione
		try {
			lVect = lCtrl.ExRicercaSentenzaRiunita(lSenRiuMod);
		} catch (F3BException e) {
			if (e.getMessage().equals("Nessun Elemento trovato")) {
			} else {
				throw e;
			}
		}
		String lPage = "";
		if (lVect.size() > 0) {
			// se vengono trovate Sentenze Riunite viene aperta la pagina di ricerca delle sentenze riunite
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sentenzariunita.action.ActRicercaSentenzaRiunita";
		} else {
			// se non vengono trovate Sentenze Riunite viene aperto il dettaglio della Sentenza
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&"
					+ ICostantiSentenza.CAMPO_ID_SENTENZA + "=" + lSen.getIdSentenza();
		}

		return lPage;
	}

}
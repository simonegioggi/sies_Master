package siap.siep.altrigradigiudizio.action;

/**
 * <p>Title: ActCancellaltroGradoGiudizio</p>
 * <p>Description: Classe Action per la cancellazione dell'altro grado giudizio</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eunics</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActCancellaAltriGradiGiudizio extends ActionSiap implements ICostantiAltriGradiGiudizio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_ALTRIGRADIGIUDIZIO);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActCancellaAltriGradiGiudizio - ID ALTRI GRADI GIUDIZIO = " + lId);

		// istanzia il Model
		AltriGradiGiudizioModel lSenMod = new AltriGradiGiudizioModel();

		// Riempie il model
		lSenMod.setIdAltrigradigiudizio(new BigDecimal(lId));

		// chiama il controller
		IAltriGradiGiudizio lCtrl = SIEPLookupRemote.getAltriGradiGiudizioRemote();
		// Elimina la Sentenza Riunita

		try {
			lCtrl.ExCancellaAltriGradiGiudizio(lSenMod);
		} catch (F3BException e) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Esiste un fascicolo collegato. Non è possibile cancellare.");
		}

		// trova l'ID della Sentenza
		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));

		// esegue la ricerca delle Sentenze Riunite per controllare quante sono
		AltriGradiGiudizioModel lAGDGMod = new AltriGradiGiudizioModel();
		lAGDGMod.setSenIdSentenza(lSen.getIdSentenza());
		Vector lVect = new Vector();
		// Gestisce l'eccezione
		try {
			lVect = lCtrl.ExRicercaAltriGradiGiudizio(lAGDGMod);
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
					+ "=siap.siep.altrigradigiudizio.action.ActRicercaAltriGradiGiudizio";
		} else {
			// se non vengono trovate Sentenze Riunite viene aperto il dettaglio della Sentenza
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&"
					+ ICostantiSentenza.CAMPO_ID_SENTENZA + "=" + lSen.getIdSentenza();
		}

		return lPage;
	}

}
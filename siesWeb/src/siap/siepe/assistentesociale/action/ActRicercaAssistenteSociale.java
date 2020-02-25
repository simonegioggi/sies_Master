package siap.siepe.assistentesociale.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import siap.sico.decodifiche.controller.DecodificheManager;
// per la decodifica
import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActRicercaAssistenteSociale
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di AssistenteSociale
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
public class ActRicercaAssistenteSociale extends ActionSiap implements ICostantiAssistenteSociale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		String lReturnPage = ""; // pagina jsp di ritorno

		AssistenteSocialeModel lAssSocMod = new AssistenteSocialeModel();

		// Collection di decodifica di Flag_stato
		Collection lCol = (DecodificheManager.getInstance()).getFlagStato();

		lAssSocMod.setCognome(
				StringUtils.convertSqlString(getRequestStringParameter(CAMPO_COGNOME).toUpperCase()));
		lAssSocMod.setNome(StringUtils.convertSqlString(getRequestStringParameter(CAMPO_NOME).toUpperCase()));

		// La ricerca è sempre filtrata per ufficio
		lAssSocMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		IAssistenteSociale lCtrl = SIEPELookupRemote.getAssistenteSocialeRemote();
		Vector lVect = lCtrl.ExRicercaAssistenteSociale(lAssSocMod);

		if (lVect.size() == 1) {
			// unico Esperto
			AssistenteSocialeModel lAssistenteSociale = new AssistenteSocialeModel(
					(AssistenteSocialeModel) lVect.firstElement());

			// Decodifica di Flag_stato
			// lAssistenteSociale.setFlagStato(DecodificheUtils.getDescbyCode(lCol,lAssistenteSociale.getFlagStato()));

			// Inserisce il model Esperto nella request
			setRequestAttribute("assistentesociale", lAssistenteSociale);
			setFunctionsAvailableToRequest(
					"siap.siepe.assistentesociale.action.ActLoadDettaglioAssistenteSociale");

			lReturnPage = PG_LOAD_DETTAGLIOASSISTENTESOCIALE;
		} else { // lista di Assistenti Sociali
			setRequestAttribute("assistentisociali", lVect);
			setRequestAttribute("flags", lCol);

			lReturnPage = PG_RICERCAASSISTENTESOCIALE;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return lReturnPage;
	}

}
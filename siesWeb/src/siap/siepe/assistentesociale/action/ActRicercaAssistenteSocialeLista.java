package siap.siepe.assistentesociale.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActRicercaAssistenteSocialeLista
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
public class ActRicercaAssistenteSocialeLista extends ActionSiap implements ICostantiAssistenteSociale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		String lCognome = null;
		String lUffAppa = null;
		String lModalita = "POP";

		IAssistenteSociale lCtrl = SIEPELookupRemote.getAssistenteSocialeRemote();
		lCognome = StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COGNOME)).toUpperCase());
		lUffAppa = this.getCodUfficioUtenteConnesso();

		AssistenteSocialeModel lAssMod = new AssistenteSocialeModel();
		lAssMod.setCognome(lCognome);
		lAssMod.setCodUfficioAppartenenza(lUffAppa);

		lCtrl = SIEPELookupRemote.getAssistenteSocialeRemote();
		Vector lVect = lCtrl.ExRicercaAssistenteSociale(lAssMod);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("assistentisociali", lVect);
		setRequestAttribute("modalita", lModalita);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_RICERCA_ASSISTENTESOCIALE_LISTA;
	}

}
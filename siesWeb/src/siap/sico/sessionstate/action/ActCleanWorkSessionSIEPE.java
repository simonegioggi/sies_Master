package siap.sico.sessionstate.action;

import java.util.Enumeration;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActCleanWorkSessionSIEPE
 * </p>
 * <p>
 * Description: Azione di cancellazione degli oggetti in sessione di lavoro
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
@SuppressWarnings("rawtypes")
public class ActCleanWorkSessionSIEPE extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Nomi degli attributi da rimuovere dalla sessione
		String lNomi[] = { "FascicoloSiepeEsteso", "fascicolo", "soggetto", "sentenza", "fascicoloSiusGP",
				"IdEventoInviato", "IDepositoDecreto", "magistratorelatore", "eventoNotTA", "avvocatoSius" };

		// SIEP
		/*
		 * setSessionAttribute("fascicolo",null); setSessionAttribute("soggetto",null);
		 * setSessionAttribute("sentenza",null);
		 * 
		 * // SIUS setSessionAttribute("fascicoloSiusGP",null); setSessionAttribute("IdEventoInviato",null);
		 * setSessionAttribute("IDepositoDecreto",null); setSessionAttribute("magistratorelatore",null);
		 * setSessionAttribute("eventoNotTA",null); setSessionAttribute("avvocatoSius",null);
		 */
		// Ciclo cancellazione
		// Nota: la funzione removeSessionAttribute(att) è equivalente a
		// setSessionAttribute(att,null).
		int lNumNomi = lNomi.length;
		for (int i = 0; i < lNumNomi; i++) {
			if (!this.isSessionAttributeNullObj(lNomi[i])) {
				this.removeSessionAttribute(lNomi[i]);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Rimosso dalla sessione : " + lNomi[i]);
			}
		}
		// Controllo degli attributi ancora in sessione
		String lNome = null;
		Enumeration lNomiAttributi = this.getSession().getAttributeNames();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ## Attributi ancora in Sessione ##");
		while (lNomiAttributi.hasMoreElements()) {
			lNome = (String) lNomiAttributi.nextElement();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(lNome);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ##-----------------------##");

		String lPage = "/jsp/files/siap/siepe/sessione/VediStatoSessione.jsp";

		return lPage;
	}

}
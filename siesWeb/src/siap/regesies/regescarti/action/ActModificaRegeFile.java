package siap.regesies.regescarti.action;

/**
* <p>Title: ActModificaRegeFile</p>
* <p>Description: Classe Action per la modifica del File Scartato REGE</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.regesies.regescarti.controller.RegeFileController;
import siap.regesies.regescarti.model.RegeFileModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaRegeFile extends ActionSiap implements ICostantiRegeFile {

	/**
	 * Azione di Modifica del RegeFile
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// istanzia il Model
		RegeFileModel lRegMod = new RegeFileModel();

		// riempie il model
		String lId = getRequestStringParameter(CAMPO_ID_FILE);
		lRegMod.setIdFile(lId);
		// Assegna al campo COD_STATO = 1
		lRegMod.setCodStato(new BigDecimal(1));
		// Svuota il campo Descrizione Errore
		lRegMod.setDescErr("");

		// chiama il controller
		// IRegeFile lCtrl = SIEPLookupRemote.getRegeFileRemote();
		RegeFileController lCtrl = new RegeFileController();

		/* RegeFileModel llRegModRet = */lCtrl.ExModificaRegeFile(lRegMod);

		String lPage = "";
		// Riapre l'elenco dei file scartati RIGE
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.regesies.regescarti.action.ActRicercaRegeFile";
		return lPage;
	}

}
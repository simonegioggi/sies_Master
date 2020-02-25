package siap.sico.ufficio.action;

/**
* <p>Title: ActModificaUfficio</p>
* <p>Description: Classe Action per la modifica di Ufficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActModificaUfficio extends ActionSiap implements ICostantiUfficio {
	
	/**
	 * Azione di Modifica del Ufficio
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// riempie il model
		UfficioModel lUffMod = new UfficioModel();

		// lUffMod.setIdUfficio(new BigDecimal(lId));
		lUffMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lUffMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));
		lUffMod.setCap(getRequestStringParameter(CAMPO_CAP));
		lUffMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		lUffMod.setFax(getRequestStringParameter(CAMPO_FAX));
		lUffMod.setEMail(getRequestStringParameter(CAMPO_E_MAIL));
		lUffMod.setCodOperatoreAgg(getCodUtenteConnesso());
		lUffMod.setCodUfficioAgg(getCodUfficioUtenteConnesso());
		lUffMod.setDataAgg(DateUtils.getSysDate());

		// chiama il controller
		IUfficio lCtrl = SICOLookupRemote.getUfficioRemote();
		/* UfficioModel llUffModRet = */lCtrl.ExModificaUfficio(lUffMod);

		// Attivazione Dettaglio
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sico.ufficio.action.ActLoadDettaglioUfficio");
		lPage.setParameter(CAMPO_COD_UFFICIO, lUffMod.getCodUfficio());
		lPage.setParameter("modalita", "M");

		return lPage.toString();
	}

}
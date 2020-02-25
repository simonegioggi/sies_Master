package siap.siep.posizionemateriale.action;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActInserisciPosizioneMateriale</p>
* <p>Description: Classe Action per l'inserimento di PosizioneMateriale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciPosizioneMateriale extends ActionSiap implements ICostantiPosizioneMateriale {

	/**
	 * Azione di Inserimento del PosizioneMateriale
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		PosizioneMaterialeModel lPosMod = new PosizioneMaterialeModel();
		// Valorizzazione del record da inserire
		lPosMod.setCodPosizioneMateriale(
				getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE).toUpperCase());
		lPosMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lPosMod.setDescPosizioneMateriale(getRequestStringParameter(CAMPO_DESC_POSIZIONE_MATERIALE));
		lPosMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPosMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPosMod.setDataInserimento(DateUtils.getSysDate());
		// Inserimento
		IPosizioneMateriale lCtrl = SIEPLookupRemote.getPosizioneMaterialeRemote();
		/* PosizioneMaterialeModel llPosModRet = */lCtrl.ExInserisciPosizioneMateriale(lPosMod);

		// Si passa al dettaglio
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.siep.posizionemateriale.action.ActLoadDettaglioPosizioneMateriale");
		lPage.setParameter(CAMPO_COD_POSIZIONE_MATERIALE, lPosMod.getCodPosizioneMateriale());
		lPage.setParameter(CAMPO_COD_UFFICIO, lPosMod.getCodUfficio());

		return lPage.toString();
	}

}
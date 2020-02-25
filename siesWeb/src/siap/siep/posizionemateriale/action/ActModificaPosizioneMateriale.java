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

public class ActModificaPosizioneMateriale extends ActionSiap implements ICostantiPosizioneMateriale {

	/**
	 * Azione di Modifica del PosizioneMateriale
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		PosizioneMaterialeModel lPosMod = new PosizioneMaterialeModel();

		// Valorizzazione del record da modificare
		lPosMod.setCodPosizioneMateriale(
				getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE).toUpperCase());
		lPosMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lPosMod.setDescPosizioneMateriale(getRequestStringParameter(CAMPO_DESC_POSIZIONE_MATERIALE));
		lPosMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lPosMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lPosMod.setDataAggiornamento(DateUtils.getSysDate());
		lPosMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
				CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));

		// Modifica
		IPosizioneMateriale lCtrl = SIEPLookupRemote.getPosizioneMaterialeRemote();
		/* PosizioneMaterialeModel llPosModRet = */lCtrl.ExModificaPosizioneMateriale(lPosMod);

		// Si passa al dettaglio
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.siep.posizionemateriale.action.ActLoadDettaglioPosizioneMateriale");
		lPage.setParameter(CAMPO_COD_POSIZIONE_MATERIALE, lPosMod.getCodPosizioneMateriale());
		lPage.setParameter(CAMPO_COD_UFFICIO, lPosMod.getCodUfficio());

		return lPage.toString();
	}

}
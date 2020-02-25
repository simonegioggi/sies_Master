package siap.siep.posizionemateriale.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaPosizioneMateriale
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di PosizioneMateriale
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

public class ActRicercaPosizioneMateriale extends ActionSiap implements ICostantiPosizioneMateriale {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		String lRetPage = PG_RICERCAPOSIZIONEMATERIALE;

		// Gestione numero di pagina
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		}

		// Riempie il model di ricerca
		PosizioneMaterialeModel lPosMod = new PosizioneMaterialeModel();
		lPosMod.setCodPosizioneMateriale(
				getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE).toUpperCase());
		lPosMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lPosMod.setDescPosizioneMateriale(getRequestStringParameter(CAMPO_DESC_POSIZIONE_MATERIALE));
		lPosMod.setFiltroDataValidita(getRequestStringParameter(CAMPO_FILTRO_DATA));

		// Ricerca
		IPosizioneMateriale lCtrl = SIEPLookupRemote.getPosizioneMaterialeRemote();
		Vector lVectPos = lCtrl.ExRicercaPosizioneMaterialePagina(lPosMod, Integer.parseInt(lPagina));

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetNumRicercaPosizioneMaterialePagina(lPosMod);
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		if (lVectPos.size() == 1 && CountRisultati.compareTo(new BigDecimal(1)) == 0) {
			lPosMod = (PosizioneMaterialeModel) lVectPos.get(0);

			// Si passa al dettaglio
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.siep.posizionemateriale.action.ActLoadDettaglioPosizioneMateriale");
			lPage.setParameter(CAMPO_COD_POSIZIONE_MATERIALE, lPosMod.getCodPosizioneMateriale());
			lPage.setParameter(CAMPO_COD_UFFICIO, lPosMod.getCodUfficio());
			lRetPage = lPage.toString();
		} else {
			// Bottone di ritorno
			setLinkRitorno();

			if (lVectPos != null && !lVectPos.isEmpty()) {
				lPosMod = (PosizioneMaterialeModel) lVectPos.get(0);
			}

			// Parametri paginazione
			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("elenco", lVectPos);
			setRequestAttribute("descUfficioRicerca", lPosMod.getDescrUfficio());
			setRequestAttribute("filtroTipoRicerca", getRequestStringParameter(CAMPO_FILTRO_DATA));
		}

		return lRetPage;
	}
}

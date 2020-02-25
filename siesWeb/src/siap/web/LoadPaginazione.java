package siap.web;

import java.util.Vector;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: LoadPaginazione
 * </p>
 * <p>
 * Description: Classe di helper per la paginazione delle ricerche
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class LoadPaginazione implements IWebConstants {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PaginazioneModel calcolaPaginazione(String aNumPagina, Vector aRisultatiRicerca) {

		int aNumeroPagina = new Integer(aNumPagina).intValue();
		PaginazioneModel lPag = new PaginazioneModel();

		int lSizeVector = aRisultatiRicerca.size();

		lPag.setInizioRicerca(((aNumeroPagina - 1) * RESULT_PER_PAGE) + 1);

		if (lSizeVector >= ((aNumeroPagina * RESULT_PER_PAGE)))
			lPag.setFineRicerca((aNumeroPagina * RESULT_PER_PAGE));
		else
			lPag.setFineRicerca(lSizeVector);

		Vector lVect = new Vector();

		for (int i = lPag.getInizioRicerca(); i <= lPag.getFineRicerca(); i++) {
			lVect.add(aRisultatiRicerca.get(i - 1));
		}

		int lNumTotalePag = lSizeVector / RESULT_PER_PAGE;

		lPag.setNumeroTotalePagine(lNumTotalePag);
		lPag.setNumeroPagina(aNumeroPagina);
		lPag.setRisultatiRicerca(lVect);

		return lPag;
	}

}
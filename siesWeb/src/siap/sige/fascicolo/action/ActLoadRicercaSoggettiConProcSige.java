package siap.sige.fascicolo.action;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadRicercaSoggettiConProcSige
 * </p>
 * <p>
 * Description: Azione di caricamento della form di ricerca dei Soggetti con Procedimenti di Giudice di
 * Esecuzione
 * </p>
 * <p>
 * Copyright: Eutelia Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
public class ActLoadRicercaSoggettiConProcSige extends ActionSiap implements ICostantiFascicoloSige {

	public String processRequest() throws F3BException {

		Option lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
		setRequestAttribute("nazioni", "" + lOption);

		// Recupero del codice Ufficio del G.E.
		// IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();
		// String strCodDistretto = getUfficioUtenteConnesso().getCodDistretto();
		// String strCodComune = getUfficioUtenteConnesso().getCodComune();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// String strTipoUfficioRichiesto = new String();
		setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio.trim());

		lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(), 75);
		setRequestAttribute("contenuto", "" + lOption);

		return PG_LOAD_RICERCASOGGETTICONPROCSIGE; // restituisce la jsp di VIEW
	}

}
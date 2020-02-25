package siap.sige.richiestaatti.action;

import java.util.Collection;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadInserisciRicSIntegrale
 * </p>
 * <p>
 * Description: Classe di Azione responsabile della composizione dei dati per le combobox e ritorna la
 * chiamata alla corrispondente JSP.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciRichiestaCorpoReato extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String[] lFiltro = { "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP", "GIPM", "GP",
				"GUPMI", "GUP", "GUPM", "PT", "PM", "PMM", "PMPT", "PGCAP", "PGMI", "PGMID", "PMI", "TRIBSD",
				"CAPSM", "TMI", "DIB", "DIBM" };
		Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoUfficio();

		// Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoUfficioPM();
		// ( DESTINATARIO ) Tipi Autorità Filtrati
		Option lOption = new Option(lElencoTipiAutorita);
		lOption.setFilter(lFiltro);
		setRequestAttribute("autorita", "" + lOption);

		// Riempimento seconda ComboBoX
		// Option lOption1 = new Option(DecodificheManager.getInstance().getTipoUfficioS());
		// setRequestAttribute("autorita", "" + lOption1);

		return PG_LOAD_RICHIESTA_CORPOREATO;
	}

}
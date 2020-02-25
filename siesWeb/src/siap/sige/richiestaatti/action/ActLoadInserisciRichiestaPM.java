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
public class ActLoadInserisciRichiestaPM extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// PM Procura della repubblica c/o il tribunale
		// PGCAP Procura Generale c/o la corte d'appello
		// PMM Procura Minori
		// PMI Procura Militare
		String[] lFiltro = { "PMI", "PMM", "PGCAP", "PM" };

		Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoUfficioPM();
		// ( DESTINATARIO ) Tipi Autorità Filtrati
		Option lOption = new Option(lElencoTipiAutorita);
		lOption.setFilter(lFiltro);

		// Riempimento ComboBoX
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioCertStatoEsec());

		setRequestAttribute("autorita", "" + lOption);

		return PG_LOAD_RICHIESTA_PM;
	}

}
package siap.sige.richiestaatti.action;

import java.util.Collection;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadInserisciRicStatoEsecuzione
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
public class ActLoadInserisciRicStatoEsecuzione extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// PM Procura della repubblica c/o il tribunale
		// PGCAP Procura Generale c/o la corte d'appello
		// PMM Procura Minori
		// PMI Procura Militare
		String[] lFiltro = { "-", "DDA", "DNA", "PGCAP", "PGCSS", "PGMI", "PGMID", "PMI", "PMPT", "PM",
				"PGCAP", "PMM" };

		// LISTA UFFICI
		Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoUfficio();
		// ( DESTINATARIO ) Tipi Autorità Filtrati
		Option lOption = new Option(lElencoTipiAutorita);
		lOption.setFilter(lFiltro);

		setRequestAttribute("autorita", "" + lOption);

		return PG_LOAD_RICHIESTA_STATOESECUZIONE;
	}

}
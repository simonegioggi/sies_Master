package siap.sige.richiestaatti.action;

import java.util.Collection;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadInserisciAnagraficaCittadiniStranieri
 * </p>
 * <p>
 * Description:
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
public class ActLoadInserisciRichiestaGenerica extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Riempimento ComboBoX
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());
		// setRequestAttribute("autorita", "" + lOption );

		// Riempimento prima ComboBoX
		Option lOption1 = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autorita", "" + lOption1);

		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoUfficio();
		String[] lStringFilter = { "-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP",
				"GIPM", "GP", "GUPMI", "GUP", "GUPM", "PT", "PM", "PMM", "PMPT", "PGCAP", "PGMI", "PGMID",
				"PMI", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM" };

		Option lOption = new Option(lTipoIstituto);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		return PG_LOAD_INSERISCI_RICHIESTAGENERICA;
	}

}
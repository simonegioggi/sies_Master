package siap.sico.profilo.action;

import java.math.BigDecimal;

import siap.sico.profilo.controller.IProfilo;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciProfilo
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Profilo
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
public class ActLoadModificaProfilo extends ActionSiap implements ICostantiProfilo {

	public String processRequest() throws F3BException {

		// Inserire Eventuali ComboBOX
		// Option lOption = new Option( DecodificheManager.getInstance().get???());

		// setRequestAttribute("???", "" + lOption );
		// Imposta Modalità.
		BigDecimal lPfrID = getRequestBigDecimalParameter(ICostantiProfilo.CAMPO_COD_PROFILO);
		IProfilo iPrf = SICOLookupRemote.getProfiloRemote();
		ProfiloModel lPrfMod = new ProfiloModel(iPrf.ExRicercaProfiloByKey(lPfrID));

		setRequestAttribute("profilo", lPrfMod);
		setRequestAttribute("modalita", "M");
		return PG_LOAD_INSERISCIPROFILO; // restituisce la jsp di VIEW
	}

}
package siap.sico.profilo.action;

import java.util.Vector;

import siap.sico.profilo.controller.IProfilo;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaProfilo
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Profilo
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
@SuppressWarnings("rawtypes")
public class ActRicercaProfilo extends ActionSiap implements ICostantiProfilo {

	public String processRequest() throws F3BException {

		ProfiloModel lProMod = new ProfiloModel();
		lProMod.setCodProfilo(getRequestBigDecimalParameter(CAMPO_COD_PROFILO));
		lProMod.setDescrizione(getRequestStringParameter(CAMPO_DESCRIZIONE));
		lProMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
				CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));

		IProfilo lCtrl = SICOLookupRemote.getProfiloRemote();
		Vector lVect = lCtrl.ExRicercaProfilo(lProMod);
		setRequestAttribute("profilo", lVect);

		return PG_RICERCAPROFILO;
	}

}
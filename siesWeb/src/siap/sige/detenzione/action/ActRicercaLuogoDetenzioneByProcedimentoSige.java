package siap.sige.detenzione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.detenzione.controller.FasSigeDetenzioneController;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActRicercaLuogoDetenzioneByProcedimentoSige
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Luogo Detenzione per Fascicolo SIGE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaLuogoDetenzioneByProcedimentoSige extends ActionSige implements
		ICostantiFasSigeDetenzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		BigDecimal lIdFascicoloSige = lFasEsteso.getFascicoloSige().getIdFascicoloSige();

		FasSigeDetenzioneController lCtrl = new FasSigeDetenzioneController();
		Vector lVect = lCtrl.exRicercaLuoghiDetenzioneFascicoloSige(lIdFascicoloSige);

		String lModificabile = "NO";

		if (IsFascicoloSigeModificabile())
			lModificabile = "SI";
		else
			lModificabile = "NO";

		setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("luoghiDetenzione", lVect);

		return ICostantiFasSigeDetenzione.PG_RICERCADETENZIONESIGE;
	}

}
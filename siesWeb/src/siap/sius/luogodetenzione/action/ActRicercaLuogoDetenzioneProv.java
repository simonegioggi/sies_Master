package siap.sius.luogodetenzione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaAvvocatoProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di un Avvocato Legato ad un Provvedimento
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
public class ActRicercaLuogoDetenzioneProv extends ActionSiap implements ICostantiLuogoDetenzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// lAvvFascMod.setFasSiuIdFascicoloSius(FasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		// AvvocatoController lCtrl = new AvvocatoController();
		ILuogoDetenzione lCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();

		Vector lVect = lCtrl.ExElencoDatiLuogoDetenzioneSius(lIdFascicolo);

		setRequestAttribute("LuoghiDet", lVect);

		if (lVect.size() == 0) {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Nessun Luogo Detenzione presente per il procedimento.");
		}
		String lPage = PG_RICERCALUOGHIDETENZIONEPROV;

		return lPage;
	}

}
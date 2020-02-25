package siap.sico.magistrato.action;

import java.util.Collection;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioMagistrato
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Magistrato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 * 
 */
@SuppressWarnings("rawtypes")
public class ActLoadDettaglioMagistrato extends ActionSiap implements ICostantiMagistrato {

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_COD_MAGISTRATO);

		IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel llMagMod = lCtrl.ExRicercaMagistratoByCod(lId);

		// Decodifica di Flag_stato
		Collection lCol = (DecodificheManager.getInstance()).getFlagStato();
		llMagMod.setFlagStato(DecodificheUtils.getDescbyCode(lCol, llMagMod.getFlagStato()));

		// llMagMod.setFlagStato(getDescFlag(llMagMod.getFlagStato()));
		setRequestAttribute("magistrato", llMagMod);

		return PG_LOAD_DETTAGLIOMAGISTRATO;
	}

}
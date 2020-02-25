package siap.siep.notifica.action;

import java.util.ArrayList;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadModificaNotificaDifensore
 * </p>
 * <p>
 * Description: Classe Action per la modifica delle notifiche al difensore dello OE Simeone
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eunics
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadModificaNotificaDifensore extends ActLoadRicercaOEDifensore implements ICostantiNotifica {

	public String processRequest() throws F3BException {

		super.processRequest();

		ArrayList lListAutEstDeleg = new ArrayList(DecodificheManager.getInstance().getTipoAutorita());

		setRequestAttribute("autoritaEsternaDelegata", lListAutEstDeleg);

		return PG_LOAD_MODIFICA_NOTIFICA_DIFENSORE;
	}

}
package siap.siep.jms.action;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadListaIstanzeTrasmesse
 * </p>
 * <p>
 * Description: Classe Action per la load della Lista Istanze Trasmesse
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadListaIstanzeTrasmesse extends ActionSiap implements ICostantiSiepJMS {

	public String processRequest() throws Exception {

		this.setLinkRitorno(); // Imposta la combo dei Tipi di Operazioni.

		// JmsCodeController lCrtl = new JmsCodeController();

		// Imposta Tipo Ufficio.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSius());
		setRequestAttribute("tipoUfficio", "" + lOption);

		return PG_LOAD_LISTAISTANZETRASMESSE; // restituisce la jsp di VIEW
	}

}
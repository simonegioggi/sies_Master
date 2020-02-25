package siap.sius.presaincarico.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaAttiSiep
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Atti Siep
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

public class ActLoadRicercaAttiSiep extends ActionSiap implements ICostantiPresaincarico {
	public String processRequest() throws Exception {
		this.setLinkRitorno();
		// Imposta Tipo Procura.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIEP());
		setRequestAttribute("tipoUfficioSIEP", "" + lOption);

		// MEV10-s3: aggiunta impostazione di proprietà nella request
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		UfficioModel lUfficioUtenteConnesso = lUtenteConnesso.getUfficioUtente();
		setRequestAttribute("ufficioUtenteConnesso", lUfficioUtenteConnesso);

		return PG_LOAD_RICERCAATTISIEP; // restituisce la jsp di VIEW
	}
}
package siap.sius.fascicolo.action;

/**
 * <p>Title: ActLoadRicercaFascicolo</p>
 * <p>Description: Classe Action per la load di RicercaFascicolo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadRicercaFascicolo extends ActionSiap implements ICostantiFascicoloSius {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// MEV10-s3: aggiunta impostazione di proprietà nella request
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		UfficioModel lUfficioUtenteConnesso = lUtenteConnesso.getUfficioUtente();
		setRequestAttribute("ufficioUtenteConnesso", lUfficioUtenteConnesso);

		// Imposta Tipo Ufficio con Trattino.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioMinor());
		setRequestAttribute("tipoUfficioSIEPTrattino", "-" + lOption);

		// esegue la query per recuperare l'elenco degli uffici accorpati
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		return PG_LOAD_RICERCAFASCICOLO; // restituisce la jsp di VIEW
	}
}
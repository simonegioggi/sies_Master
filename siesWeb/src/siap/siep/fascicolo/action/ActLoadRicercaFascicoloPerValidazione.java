package siap.siep.fascicolo.action;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;

public class ActLoadRicercaFascicoloPerValidazione extends ActionSiap implements ICostantiFascicoloSiep {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					this.getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
		}

		// model degli elementi del vettore
		// UfficioAccorpatoModel lUAMod = new UfficioAccorpatoModel();
		String codUfficioUtente = getCodUfficioUtenteConnesso();

		// esegue la query per recuperare l'elenco degli uffici accorpati di tipo PM ed appartenenti al
		// distretto dell'utente loggato
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati("PM", codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		return PG_LOAD_RICERCA_PER_VALIDAZIONE; // restituisce la jsp di VIEW
	}

}
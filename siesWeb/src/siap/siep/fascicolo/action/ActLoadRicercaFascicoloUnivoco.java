package siap.siep.fascicolo.action;

import java.util.Vector;

import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadRicercaFascicoloUnivoco extends ActionSiap implements ICostantiFascicoloSiep {

	public String processRequest() throws Exception {

		String lAction = new String();
//		String cumulo = new String();
		lAction = getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE);
		if (!isRequestParameterNullObj(CAMPO_AZIONE_CHIAMANTE)) {
			lAction = getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE);
			if (!isRequestParameterNullObj("Cumulo"))
				setRequestAttribute("cumulo", getRequestStringParameter("Cumulo"));
			else
				setRequestAttribute("cumulo", "0");
			setRequestAttribute("azionechiamante", lAction);
		}
		// modifiche per presa in carico competenza
		if (!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			setRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO,
					getRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO));
			if (!isRequestParameterNullObj("progrRicevuto"))
				setRequestAttribute("progrRicevuto", getRequestStringParameter("progrRicevuto"));
			if (!isRequestParameterNullObj("annoRicevuto"))
				setRequestAttribute("annoRicevuto", getRequestStringParameter("annoRicevuto"));

			return PG_LOAD_RICERCA_PRESAINCARICO_COMPETENZA;
		}

		if (!isRequestParameterNullObj("TipoFasc")) {
			// restituisce la jsp di VIEW
			if (getRequestStringParameter("TipoFasc").equals("NuovaIstanza"))
				return f3b.web.IWebConstants.ROOT_DIR
						+ "files/siap/siep/fascicolo/LoadRicercaNuovaIstanzaUnivoca.jsp";
		}

		// model degli elementi del vettore
//		UfficioAccorpatoModel lUAMod = new UfficioAccorpatoModel();
		String codUfficioUtente = getCodUfficioUtenteConnesso();

		// esegue la query per recuperare l'elenco degli uffici accorpati di tipo PM ed appartenenti al
		// distretto dell'utente loggato
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati("PM", codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		// ===================================
		return PG_LOAD_RICERCA_UNIVOCO; // restituisce la jsp di VIEW
	}

}